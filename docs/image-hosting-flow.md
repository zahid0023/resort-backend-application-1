# Image Hosting Service — Full Flow Documentation

## Table of Contents

1. [Overview](#overview)
2. [Core Concept: Per-Resort Provider Selection](#core-concept-per-resort-provider-selection)
3. [Architecture Layers](#architecture-layers)
4. [Component Map](#component-map)
5. [Config Lifecycle](#config-lifecycle)
   - [Step 1 — Create a Config](#step-1--create-a-config)
   - [Step 2 — Manage Active Config](#step-2--manage-active-config)
   - [Step 3 — Upload an Image](#step-3--upload-an-image)
6. [How Strategy Selection Works](#how-strategy-selection-works)
   - [Config → ImageHostingConfig Object](#config--imaginghostingconfig-object)
   - [Provider → Strategy Lookup](#provider--strategy-lookup)
7. [Full End-to-End Upload Sequence](#full-end-to-end-upload-sequence)
8. [Data Flow Diagram](#data-flow-diagram)
9. [Provider Config Reference](#provider-config-reference)
   - [S3 Config](#s3-config)
   - [Cloudinary Config](#cloudinary-config)
10. [isActive — The "Active Config" Rule](#isactive--the-active-config-rule)
11. [Component Reference](#component-reference)
12. [Error Reference](#error-reference)

---

## Overview

The image hosting system is designed for **multi-resort, multi-provider** operation.

Each resort owner independently chooses where their images are stored — one may use
their company's AWS S3 bucket, another may use Cloudinary. They register their
credentials through the `ResortImageStorageConfig` API. When they upload images,
the system automatically reads the resort's **active config**, selects the correct
provider strategy, builds the provider client from the stored credentials, uploads
the file, and returns the CDN URL.

No global image hosting credentials are required. Every resort is self-contained.

---

## Core Concept: Per-Resort Provider Selection

```
Resort A  →  ResortImageStorageConfig { provider: S3,         config: { bucket, region, ... } }
Resort B  →  ResortImageStorageConfig { provider: CLOUDINARY,  config: { cloud_name, api_key, ... } }
Resort C  →  ResortImageStorageConfig { provider: S3,         config: { bucket: "different-bucket", ... } }
```

When Resort A uploads an image → file goes to their S3 bucket.
When Resort B uploads an image → file goes to their Cloudinary account.
When Resort C uploads an image → file goes to their own separate S3 bucket.

The caller never specifies a provider at upload time — the system resolves it from
the resort's active config automatically.

---

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────────────┐
│  HTTP Layer                                                          │
│  ResortController          ResortImageStorageConfigController        │
└────────────┬───────────────────────────────┬────────────────────────┘
             │                               │
┌────────────▼───────────────┐  ┌────────────▼────────────────────────┐
│  Upload Flow               │  │  Config Management Flow              │
│  ResortService             │  │  ResortImageStorageConfigService      │
│  ImageUploadService        │  │  (CRUD for provider credentials)     │
└────────────┬───────────────┘  └─────────────────────────────────────┘
             │
┌────────────▼───────────────────────────────────────────────────────┐
│  Strategy Layer                                                      │
│  ImageHostingStrategyRegistry                                        │
│  ┌──────────────────────┐   ┌──────────────────────────────────┐    │
│  │ CloudinaryHosting    │   │ S3HostingStrategy                │    │
│  │ Strategy             │   │                                  │    │
│  └──────────────────────┘   └──────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
             │
┌────────────▼───────────────────────────────────────────────────────┐
│  Provider SDKs                                                       │
│  Cloudinary SDK               AWS SDK v2 (S3Client)                 │
└─────────────────────────────────────────────────────────────────────┘
             │
┌────────────▼───────────────────────────────────────────────────────┐
│  Persistence Layer                                                   │
│  ResortImageEntity (resort_images)                                   │
│  ResortImageStorageConfigEntity (resort_image_storage_configs)       │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Component Map

| Component | Package | Role |
|---|---|---|
| `ResortImageStorageConfigController` | `controller` | REST API for managing provider configs per resort |
| `ResortImageStorageConfigService` | `service` | CRUD business logic for configs |
| `ResortImageStorageConfigServiceImpl` | `serviceImpl` | Implementation: validate → save → soft-delete |
| `ResortImageStorageConfigEntity` | `model/entity` | DB record: `resort_id`, `provider`, `config (jsonb)` |
| `ResortImageStorageConfigRepository` | `repository` | JPA queries filtered by `isActive`, `isDeleted` |
| `ImageHostingProvider` (enum) | `commons/enums` | `S3` / `CLOUDINARY` — each holds its required config keys |
| `ImageUploadService` | `commons/service` | Single upload method: `upload(file, provider, config)` |
| `ImageUploadServiceImpl` | `commons/serviceImpl` | Resolves strategy from registry, delegates upload |
| `ImageHostingStrategyRegistry` | `commons/imagehosting` | `Map<provider, strategy>` — O(1) strategy lookup |
| `ImageHostingStrategy` | `commons/imagehosting` | Strategy interface: `provider()` + `upload(file, config)` |
| `CloudinaryHostingStrategy` | `commons/imagehosting` | Uploads to Cloudinary using credentials from config |
| `S3HostingStrategy` | `commons/imagehosting` | Uploads to S3 using credentials from config |
| `CloudinaryConfig` | `commons/config` | `ImageHostingConfig` implementation for Cloudinary |
| `AwsS3Config` | `commons/config` | `ImageHostingConfig` implementation for AWS S3 |
| `ResortController` | `controller` | Resort CRUD + image upload endpoint |
| `ResortService` / `ResortServiceImpl` | `service/serviceImpl` | Resort business logic, delegates upload |
| `ResortImageEntity` | `model/entity` | Persisted image URL + metadata per resort |
| `ResortImageMapper` | `model/mapper` | `ImageRequest` → `ResortImageEntity` |
| `ImageEntity` | `commons/model/entity` | `@MappedSuperclass`: imageUrl, publicId, caption, isDefault, sortOrder |
| `ImageItemRequest` | `commons/dto/request` | Multipart input: image file + caption + isDefault + sortOrder |
| `ImageRequest` | `commons/dto/request` | Assembled after upload: URL + publicId + metadata |
| `ImageUploadResponse` | `commons/dto/response` | Raw result from storage: imageUrl + publicId |

---

## Config Lifecycle

### Step 1 — Create a Config

The resort owner calls the config API with their chosen provider and credentials:

```
POST /api/v1/resorts/{resort-id}/resort-image-storage-configs
Content-Type: application/json

{
  "provider": "CLOUDINARY",
  "config": {
    "cloud_name": "my-resort-cloud",
    "api_key":    "123456789012345",
    "api_secret": "abcDEFghiJKLmnoPQR"
  }
}
```

**What happens internally:**

```
Controller
  └── @Valid validates @NotNull on provider and config
  └── resortService.getResortById(resortId)    ← load parent resort
  └── configService.createResortImageStorageConfig(request, resortEntity)
        └── provider.validate(config)           ← check all required keys present
        └── mapper.fromRequest(request, resort) ← build entity
        └── repository.save(entity)             ← persist to DB
        └── return SuccessResponse { success: true, id: <new-id> }
```

The record is saved with `is_active = true` and `is_deleted = false`.

---

### Step 2 — Manage Active Config

A resort can have **multiple configs** saved (e.g. one for S3, one for Cloudinary).
The **active config** is the one with `is_active = true` and `is_deleted = false`.

**Switching providers:** Update the desired config (change credentials or provider)
or delete the old one and create a new one. Only one config should be active at a time
per resort — enforcing a single-active constraint is the caller's responsibility when
coordinating via the API.

**Deactivating a config:** Call the delete endpoint — this soft-deletes the record:
```
DELETE /api/v1/resorts/{resort-id}/resort-image-storage-configs/{id}
```
Sets `is_deleted = true`, `is_active = false`. The record is never hard-deleted.

**Updating credentials:** Call the update endpoint with new config values:
```
PUT /api/v1/resorts/{resort-id}/resort-image-storage-configs/{id}
{
  "provider": "S3",
  "config": {
    "bucket":     "new-bucket-name",
    "region":     "us-west-2",
    "access_key": "NEW_ACCESS_KEY",
    "secret_key": "NEW_SECRET_KEY"
  }
}
```

---

### Step 3 — Upload an Image

When uploading images for a resort, the system:

1. **Fetches the resort's active config** from `resort_image_storage_configs`
   (filtered by `resort_id`, `is_active = true`, `is_deleted = false`)
2. **Reads** `provider` and `config` map from the entity
3. **Builds** the provider-specific `ImageHostingConfig` object (populates
   `AwsS3Config` or `CloudinaryConfig` from the stored map)
4. **Resolves** the correct `ImageHostingStrategy` from the registry by provider
5. **Uploads** the binary file through the strategy using the built config
6. **Receives** `ImageUploadResponse { imageUrl, publicId }`
7. **Assembles** `ImageRequest` combining URL + publicId + metadata from
   `ImageItemRequest` (caption, isDefault, sortOrder)
8. **Maps** to `ResortImageEntity` and persists

---

## How Strategy Selection Works

### Config → ImageHostingConfig Object

The `config` field in the DB is `Map<String, Object>` (stored as `jsonb`).
Before calling upload, the stored map values are used to populate the matching
`ImageHostingConfig` implementation:

**For S3:**
```
Map from DB:                        AwsS3Config (ImageHostingConfig):
{                                   bucket    = config.get("bucket")
  "bucket":     "my-bucket",    →   region    = config.get("region")
  "region":     "us-east-1",        accessKey = config.get("access_key")
  "access_key": "AKIA...",          secretKey = config.get("secret_key")
  "secret_key": "wJalr..."
}
```

**For Cloudinary:**
```
Map from DB:                        CloudinaryConfig (ImageHostingConfig):
{                                   cloudName = config.get("cloud_name")
  "cloud_name": "my-cloud",     →   apiKey    = config.get("api_key")
  "api_key":    "123...",           apiSecret = config.get("api_secret")
  "api_secret": "abc..."
}
```

### Provider → Strategy Lookup

```
ImageHostingStrategyRegistry
  strategies = {
    CLOUDINARY → CloudinaryHostingStrategy,
    S3         → S3HostingStrategy
  }

registry.get(ImageHostingProvider.S3)         → S3HostingStrategy
registry.get(ImageHostingProvider.CLOUDINARY) → CloudinaryHostingStrategy
```

The registry is populated once at application startup by Spring collecting all
`@Component` beans that implement `ImageHostingStrategy`. The lookup is an O(1)
map get — no if/switch chains.

---

## Full End-to-End Upload Sequence

```
Client
  │
  │  POST /api/v1/resorts/{resort-id}/images
  │  Content-Type: multipart/form-data
  │  { imageItemRequests: [ { image: <file>, caption, isDefault, sortOrder } ] }
  │
  ▼
ResortController
  │
  ├── 1. Load resort's active ResortImageStorageConfigEntity
  │       resortImageStorageConfigRepository
  │         .findByResort_IdAndIsActiveAndIsDeleted(resortId, true, false)
  │       → entity { provider: S3, config: { bucket, region, access_key, secret_key } }
  │
  ├── 2. Build ImageHostingConfig from stored map
  │       AwsS3Config s3Config = new AwsS3Config()
  │       s3Config.setBucket(entity.getConfig().get("bucket"))
  │       s3Config.setRegion(entity.getConfig().get("region"))
  │       s3Config.setAccessKey(entity.getConfig().get("access_key"))
  │       s3Config.setSecretKey(entity.getConfig().get("secret_key"))
  │
  ├── 3. For each ImageItemRequest:
  │
  │       imageUploadService.upload(file, entity.getProvider(), s3Config)
  │           │
  │           └── registry.get(S3)           → S3HostingStrategy
  │           └── strategy.upload(file, s3Config)
  │                   │
  │                   ├── s3Config.init(bucket, region, accessKey, secretKey)
  │                   │       └── validates all fields non-blank
  │                   │       └── builds S3Client with StaticCredentialsProvider
  │                   │
  │                   ├── key = UUID + "_" + filename
  │                   ├── PutObjectRequest { bucket, key, contentType }
  │                   ├── s3Client.putObject(request, bytes)
  │                   └── return ImageUploadResponse {
  │                             imageUrl: "https://{bucket}.s3.{region}.amazonaws.com/{key}",
  │                             publicId: "{key}"
  │                           }
  │
  ├── 4. Assemble ImageRequest
  │       ImageRequest {
  │         imageUrl:  uploadResponse.imageUrl,
  │         publicId:  uploadResponse.publicId,
  │         caption:   imageItemRequest.caption,
  │         isDefault: imageItemRequest.isDefault,
  │         sortOrder: imageItemRequest.sortOrder
  │       }
  │
  ├── 5. Map to ResortImageEntity
  │       ResortImageMapper.fromRequest(imageRequest, resortEntity)
  │       → ResortImageEntity { resortEntity, imageUrl, publicId, caption, isDefault, sortOrder }
  │
  └── 6. Persist + return
          resortService.createResort(..., imageRequestList)
          → repository.save(resortEntity with cascaded images)
          → 201 { success: true, id: <resort-id> }
```

---

## Data Flow Diagram

```
┌──────────────────────────────────────────────────────────────────────────┐
│                         IMAGE UPLOAD REQUEST                              │
│  POST /api/v1/resorts/{id}/images                                        │
│  multipart: [ { image: file, caption, isDefault, sortOrder } ]           │
└─────────────────────────────┬────────────────────────────────────────────┘
                              │
                    ┌─────────▼──────────┐
                    │  ResortController  │
                    └─────────┬──────────┘
                              │
              ┌───────────────▼───────────────────┐
              │  STEP 1: Load Active Config        │
              │                                    │
              │  resort_image_storage_configs      │
              │  WHERE resort_id = ?               │
              │    AND is_active  = true           │
              │    AND is_deleted = false          │
              │                                    │
              │  → { provider, config_map }        │
              └───────────────┬───────────────────┘
                              │
              ┌───────────────▼───────────────────┐
              │  STEP 2: Build ImageHostingConfig  │
              │                                    │
              │  provider = S3?                    │
              │    → populate AwsS3Config          │
              │      from config_map keys          │
              │                                    │
              │  provider = CLOUDINARY?            │
              │    → populate CloudinaryConfig     │
              │      from config_map keys          │
              └───────────────┬───────────────────┘
                              │
              ┌───────────────▼───────────────────┐
              │  STEP 3: Upload via Strategy       │
              │                                    │
              │  imageUploadService                │
              │    .upload(file, provider, config) │
              │        │                           │
              │        └─ registry.get(provider)   │
              │           → matching Strategy      │
              │           → strategy.upload(file,  │
              │               config)              │
              │        └─ returns ImageUpload      │
              │           Response {imageUrl,      │
              │           publicId}                │
              └───────────────┬───────────────────┘
                              │
              ┌───────────────▼───────────────────┐
              │  STEP 4: Persist Image Record      │
              │                                    │
              │  ResortImageEntity {               │
              │    resort_id,                      │
              │    image_url  ← imageUrl,          │
              │    public_id  ← publicId,          │
              │    caption, isDefault, sortOrder   │
              │  }                                 │
              │  → INSERT INTO resort_images       │
              └───────────────┬───────────────────┘
                              │
              ┌───────────────▼───────────────────┐
              │  RESPONSE                          │
              │  201 Created                       │
              │  { success: true, id: <resort-id> }│
              └────────────────────────────────────┘
```

---

## Provider Config Reference

### S3 Config

**Required keys** in the `config` map:

| Key | Description | Example |
|---|---|---|
| `bucket` | S3 bucket name | `my-resort-images` |
| `region` | AWS region code | `us-east-1`, `ap-southeast-1` |
| `access_key` | IAM access key ID | `AKIAIOSFODNN7EXAMPLE` |
| `secret_key` | IAM secret access key | `wJalrXUtnFEMI/K7MDENG/...` |

**How it is used at upload time:**

```java
// S3HostingStrategy.upload()
S3Client s3 = s3Config.init(bucket, region, accessKey, secretKey);
    // → validates all non-blank
    // → builds S3Client with StaticCredentialsProvider

String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
PutObjectRequest req = PutObjectRequest.builder()
    .bucket(bucket).key(key).contentType(contentType).build();
s3.putObject(req, RequestBody.fromBytes(file.getBytes()));

// Final URL:
"https://{bucket}.s3.{region}.amazonaws.com/{key}"
```

**Stored image result:**

| Field | Value |
|---|---|
| `imageUrl` | `https://my-resort-images.s3.us-east-1.amazonaws.com/uuid_filename.jpg` |
| `publicId` | `uuid_filename.jpg` (the S3 object key) |

---

### Cloudinary Config

**Required keys** in the `config` map:

| Key | Description | Example |
|---|---|---|
| `cloud_name` | Cloudinary cloud identifier | `my-resort-cloud` |
| `api_key` | Cloudinary API key | `123456789012345` |
| `api_secret` | Cloudinary API secret | `abcDEFghiJKLmnoPQR` |

**How it is used at upload time:**

```java
// CloudinaryHostingStrategy.upload()
Cloudinary cloudinary = cloudinaryConfig.init(cloudName, apiKey, apiSecret);
    // → validates all non-blank
    // → new Cloudinary({ cloud_name, api_key, api_secret, secure: true })

Map result = cloudinary.uploader().upload(file.getBytes(), emptyMap());
// result contains: secure_url, public_id, width, height, format, ...
```

**Stored image result:**

| Field | Value |
|---|---|
| `imageUrl` | `https://res.cloudinary.com/{cloud}/image/upload/v.../public_id.jpg` |
| `publicId` | Cloudinary asset identifier (used for transforms and deletion) |

---

## isActive — The "Active Config" Rule

`is_active` and `is_deleted` on `ResortImageStorageConfigEntity` control which config
is used at upload time.

| `is_active` | `is_deleted` | Visible via API? | Used for upload? |
|---|---|---|---|
| `true` | `false` | Yes | **Yes** — this is the active config |
| `false` | `false` | No | No |
| `true` | `true` | No | No |
| `false` | `true` | No | No |

**One active config per resort** — the system fetches the first record matching
`(resort_id, is_active=true, is_deleted=false)`. If a resort has multiple configs,
ensure only one is active at a time by deactivating the old one before creating a new one.

**What "active" means in practice:**
- A config is created → `is_active = true` automatically
- A config is soft-deleted → `is_active = false`, `is_deleted = true`
- A config can be updated in-place (change provider or rotate credentials) without
  creating a new record — the update endpoint patches the existing active record

---

## Component Reference

### `ImageHostingStrategy` (interface)

```java
public interface ImageHostingStrategy {
    ImageHostingProvider provider();
    ImageUploadResponse upload(MultipartFile file, ImageHostingConfig config);
}
```

`config` carries the live credentials for this specific upload — populated from the
resort's stored config map. Each call may use different credentials (different resorts,
different buckets/clouds).

---

### `S3HostingStrategy`

- Accepts `AwsS3Config` as config — throws `IllegalArgumentException` if wrong type
- Creates a new `S3Client` per upload (using `try-with-resources` for auto-close)
- Object key format: `{UUID}_{originalFilename}` — ensures uniqueness
- Public URL format: `https://{bucket}.s3.{region}.amazonaws.com/{key}`

---

### `CloudinaryHostingStrategy`

- Accepts `CloudinaryConfig` as config — throws `IllegalArgumentException` if wrong type
- Creates a new `Cloudinary` client per upload using the stored credentials
- Returns `secure_url` (HTTPS) and `public_id` from Cloudinary's upload response

---

### `ImageHostingStrategyRegistry`

```java
// Built once at application startup
public ImageHostingStrategyRegistry(List<ImageHostingStrategy> strategies) {
    this.strategies = strategies.stream()
        .collect(toUnmodifiableMap(ImageHostingStrategy::provider, s -> s));
}

public ImageHostingStrategy get(ImageHostingProvider provider) {
    ImageHostingStrategy strategy = strategies.get(provider);
    if (strategy == null) throw new IllegalArgumentException("No strategy for: " + provider);
    return strategy;
}
```

Spring auto-collects all `@Component` beans implementing `ImageHostingStrategy`.
The map is built once and never changes at runtime.

---

### `AwsS3Config` / `CloudinaryConfig`

Both implement `ImageHostingConfig` (marker interface) and are also
`@ConfigurationProperties` beans. They serve a dual purpose:

| Purpose | How |
|---|---|
| Spring Boot config bean | Populated from `application.yaml` env vars at startup |
| Per-upload credential holder | Populated from the resort's DB config map at upload time |

At upload time, a **new instance** of `AwsS3Config` or `CloudinaryConfig` is created
and populated with the resort's specific stored credentials — the application-level
`@ConfigurationProperties` bean is not used for per-resort uploads.

---

## Error Reference

| Scenario | Layer | Exception | HTTP |
|---|---|---|---|
| `provider` null in config request | Controller (`@Valid`) | `MethodArgumentNotValidException` | 400 |
| `config` null in config request | Controller (`@Valid`) | `MethodArgumentNotValidException` | 400 |
| Required config key missing or blank | Service (`provider.validate()`) | `IllegalArgumentException` | 400 |
| Resort not found | `ResortService` | `EntityNotFoundException` | 404 |
| Active config not found for resort | `ResortImageStorageConfigRepository` | `EntityNotFoundException` | 404 |
| Wrong config type passed to strategy | `S3/CloudinaryHostingStrategy` | `IllegalArgumentException` | 400 |
| S3 upload SDK failure | `S3HostingStrategy` | `IllegalStateException` | 500 |
| Cloudinary upload SDK failure | `CloudinaryHostingStrategy` | `IllegalStateException` | 500 |
| S3 credentials blank at upload time | `AwsS3Config.validate()` | `IllegalStateException` | 500 |
| Cloudinary credentials blank at upload time | `CloudinaryConfig.validate()` | `IllegalStateException` | 500 |
| No strategy registered for provider | `ImageHostingStrategyRegistry.get()` | `IllegalArgumentException` | 400 |
| Concurrent config update | `ResortImageStorageConfigEntity` (`@Version`) | `ObjectOptimisticLockingFailureException` | 409 |
