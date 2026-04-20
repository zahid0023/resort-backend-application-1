# Image Upload System — Architecture & Design

## Overview

The image upload system is a **reusable commons layer** built on the **Strategy Pattern**.
Any domain (Rooms, Resorts, etc.) calls a single service method, passes the desired
provider at runtime, and gets back a ready-to-persist result — with zero knowledge of
the underlying SDK.

Two responsibilities are kept completely separate:

| Concern | Component | Description |
|---|---|---|
| **Storage** | `ImageUploadService` | Uploads a file to the chosen provider, returns URL + publicId |
| **Persistence** | `ImageService<T>` | Validates business rules and batch-inserts domain image entities |

---

## Package Structure

```
commons/
├── enums/
│   └── ImageHostingProvider.java            ← CLOUDINARY | S3
│
├── imagehosting/
│   ├── ImageHostingStrategy.java            ← Strategy interface
│   ├── CloudinaryHostingStrategy.java       ← Cloudinary implementation
│   ├── S3HostingStrategy.java               ← AWS S3 implementation
│   └── ImageHostingStrategyRegistry.java    ← Map<provider, strategy> (auto-collected)
│
├── config/
│   ├── CloudinaryConfig.java                ← @Bean Cloudinary  (@ConditionalOnProperty)
│   └── AwsS3Config.java                     ← @Bean S3Client    (@ConditionalOnProperty)
│
├── dto/
│   ├── request/
│   │   ├── ImageItemRequest.java            ← multipart file + caption + isDefault + sortOrder
│   │   └── ImageRequest.java               ← assembled image data (url + publicId + metadata)
│   └── response/
│       └── ImageUploadResponse.java        ← raw storage result: imageUrl + publicId
│
├── model/entity/
│   └── ImageEntity.java                    ← @MappedSuperclass: all image columns
│
├── service/
│   ├── ImageUploadService.java             ← upload(file, provider)
│   └── ImageService.java                  ← saveAll(List<T>)
│
└── serviceImpl/
    ├── ImageUploadServiceImpl.java         ← delegates to registry
    └── ImageServiceImpl.java              ← abstract base: validate + batch insert
```

---

## Strategy Pattern Design

### Why Strategy Pattern?

Without it, the service would contain a `switch` on `ImageHostingProvider` that must
be edited every time a new provider is added. With the Strategy Pattern:

- Each provider is an isolated `@Component` — self-contained, independently testable
- Adding a new provider (e.g. GCS, Imgur) requires **one new class only**
- `ImageUploadServiceImpl` never changes — it only talks to the registry

### Class Diagram

```
«interface»
ImageHostingStrategy
├── provider() : ImageHostingProvider
└── upload(MultipartFile) : ImageUploadResponse
         ▲                        ▲
         │                        │
CloudinaryHostingStrategy    S3HostingStrategy
  @ConditionalOnProperty       @ConditionalOnProperty
  (cloudinary.cloud-name)      (aws.s3.bucket)
  └─ Cloudinary (injected)     └─ S3Client (injected)


ImageHostingStrategyRegistry
  Map<ImageHostingProvider, ImageHostingStrategy>
  ← Spring collects all ImageHostingStrategy @Component beans at startup
  get(provider) : ImageHostingStrategy


ImageUploadServiceImpl  implements  ImageUploadService
  └─ registry.get(provider).upload(file)
```

### Spring Bean Auto-Collection

Spring injects `List<ImageHostingStrategy>` into the registry constructor.
Every `@Component` that implements `ImageHostingStrategy` is automatically included.
`@ConditionalOnProperty` controls which strategies are actually registered as beans:

```
Env var present?              Bean created?   In registry?
────────────────────────────────────────────────────────
CLOUDINARY_CLOUD_NAME set  →  YES          →  CLOUDINARY strategy available
CLOUDINARY_CLOUD_NAME unset→  NO           →  CLOUDINARY strategy absent
AWS_S3_BUCKET set          →  YES          →  S3 strategy available
AWS_S3_BUCKET unset        →  NO           →  S3 strategy absent
```

---

## Component Reference

### `ImageHostingProvider` (enum)

```java
public enum ImageHostingProvider {
    CLOUDINARY,
    S3
}
```

Passed by the caller to select which storage backend to use for that specific upload.

---

### `ImageHostingStrategy` (interface)

```java
public interface ImageHostingStrategy {
    ImageHostingProvider provider();
    ImageUploadResponse upload(MultipartFile file);
}
```

| Method | Description |
|---|---|
| `provider()` | Returns the enum key this strategy handles — used by the registry to build the map |
| `upload(file)` | Uploads the file to the backing service, returns `imageUrl` + `publicId` |

---

### `CloudinaryHostingStrategy`

- **Activated by:** `@ConditionalOnProperty(prefix = "cloudinary", name = "cloud-name")`
- **Depends on:** `Cloudinary` bean (from `CloudinaryConfig`)
- **Upload logic:** calls `cloudinary.uploader().upload(bytes, emptyMap())`
- **Returns:** `secure_url` as `imageUrl`, `public_id` as `publicId`
- **Error:** wraps `IOException` in `IllegalStateException` (→ HTTP 500 via global handler)

---

### `S3HostingStrategy`

- **Activated by:** `@ConditionalOnProperty(prefix = "aws.s3", name = "bucket")`
- **Depends on:** `S3Client` bean (from `AwsS3Config`)
- **Upload logic:** generates a `UUID_filename` key, calls `s3Client.putObject(...)`
- **URL format:** `https://{bucket}.s3.{region}.amazonaws.com/{key}`
- **Returns:** constructed URL as `imageUrl`, object key as `publicId`
- **Error:** wraps `IOException` in `IllegalStateException` (→ HTTP 500 via global handler)

---

### `ImageHostingStrategyRegistry`

```java
public ImageHostingStrategyRegistry(List<ImageHostingStrategy> strategies) {
    this.strategies = strategies.stream()
        .collect(Collectors.toUnmodifiableMap(ImageHostingStrategy::provider, s -> s));
}

public ImageHostingStrategy get(ImageHostingProvider provider) { ... }
```

- Built once at startup from all available `ImageHostingStrategy` beans
- `get(provider)` is an **O(1) map lookup**
- Throws `IllegalArgumentException` if the requested provider has no registered strategy
  (e.g. caller requests `S3` but `AWS_S3_BUCKET` was not set)

---

### `ImageUploadService` (interface)

```java
public interface ImageUploadService {
    ImageUploadResponse upload(MultipartFile file, ImageHostingProvider provider);
}
```

| Parameter | Type | Description |
|---|---|---|
| `file` | `MultipartFile` | The binary file to upload |
| `provider` | `ImageHostingProvider` | Which storage backend to use for this call |

**Returns** `ImageUploadResponse` — raw storage result, no domain-specific fields.

---

### `ImageUploadServiceImpl`

```java
@Override
public ImageUploadResponse upload(MultipartFile file, ImageHostingProvider provider) {
    return registry.get(provider).upload(file);
}
```

Single responsibility: resolve the strategy from the registry and delegate.
Contains no provider-specific logic itself.

---

### `ImageUploadResponse` (DTO)

```java
public class ImageUploadResponse {
    private String imageUrl;   // CDN/S3 URL of the stored file
    private String publicId;   // Provider asset identifier (used for deletion/transforms)
}
```

Raw result from storage. The caller is responsible for combining this with
metadata (`caption`, `isDefault`, `sortOrder`) from `ImageItemRequest`.

---

### `ImageItemRequest` (DTO)

```java
public class ImageItemRequest {
    private MultipartFile image;
    private String caption;
    private Boolean isDefault = false;
    private Integer sortOrder = 0;
}
```

Received as part of a `multipart/form-data` request. Contains both the binary file
and the metadata that will be persisted alongside the URL.

---

### `ImageRequest` (DTO)

```java
public class ImageRequest {
    private String imageUrl;
    private String publicId;
    private String caption;
    private Boolean isDefault;
    private Integer sortOrder;
}
```

Assembled after upload — combines `ImageUploadResponse` with metadata from
`ImageItemRequest`. The controller maps this to a domain entity before calling `saveAll`.

---

### `ImageService<T extends ImageEntity>` (interface)

```java
public interface ImageService<T extends ImageEntity> {
    List<T> saveAll(List<T> images);
}
```

Generic save contract. Each domain provides its own `T` (e.g. `RoomImageEntity`).

---

### `ImageServiceImpl<T, R>` (abstract class)

```java
public abstract class ImageServiceImpl<T extends ImageEntity, R extends JpaRepository<T, Long>>
        implements ImageService<T> {

    protected abstract R getRepository();

    @Override
    public List<T> saveAll(List<T> images) {
        validate(images);
        return getRepository().saveAll(images);
    }
}
```

**Business rules enforced in `validate()`:**

| Rule | Behaviour |
|---|---|
| More than one `isDefault = true` | Throws `IllegalArgumentException` (→ HTTP 400) |
| No image has `isDefault = true` | First image is auto-promoted to default |
| List is null or empty | Returns `List.of()` immediately, no DB call |

Domain subclasses only need to provide `getRepository()` — all validation and
batch-insert logic is inherited.

---

## Configuration

### Environment Variables

| Variable | Activates | Description |
|---|---|---|
| `CLOUDINARY_CLOUD_NAME` | Cloudinary bean | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Cloudinary bean | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary bean | Cloudinary API secret |
| `AWS_S3_BUCKET` | S3 bean | S3 bucket name |
| `AWS_S3_REGION` | S3 bean | AWS region (e.g. `ap-southeast-1`) |
| `AWS_S3_ACCESS_KEY` | S3 bean | IAM access key |
| `AWS_S3_SECRET_KEY` | S3 bean | IAM secret key |

At least one provider's variables must be set or the application will have no
strategies in the registry and any upload call will throw `IllegalArgumentException`.

### application.yaml

```yaml
cloudinary:
  cloud-name: ${CLOUDINARY_CLOUD_NAME:}
  api-key: ${CLOUDINARY_API_KEY:}
  api-secret: ${CLOUDINARY_API_SECRET:}

aws:
  s3:
    bucket: ${AWS_S3_BUCKET:}
    region: ${AWS_S3_REGION:}
    access-key: ${AWS_S3_ACCESS_KEY:}
    secret-key: ${AWS_S3_SECRET_KEY:}
```

### pom.xml Dependencies

```xml
<!-- Cloudinary -->
<dependency>
    <groupId>com.cloudinary</groupId>
    <artifactId>cloudinary-http44</artifactId>
    <version>1.39.0</version>
</dependency>

<!-- AWS S3 -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.25.0</version>
</dependency>
```

---

## Sequence Diagrams

### Single Upload

```
Controller          ImageUploadService       Registry           Strategy          Storage
    │                      │                    │                   │                │
    │─ upload(file, CLOUDINARY) ──────────────► │                   │                │
    │                      │─ get(CLOUDINARY) ─►│                   │                │
    │                      │◄─ CloudinaryStrategy ─────────────────│                │
    │                      │─ strategy.upload(file) ───────────────►│                │
    │                      │                    │  file.getBytes()  │                │
    │                      │                    │                   │─ upload(bytes)─►│
    │                      │                    │                   │◄─ secure_url   │
    │                      │                    │                   │   public_id    │
    │                      │◄── ImageUploadResponse ───────────────────────────────  │
    │◄─ ImageUploadResponse ────────────────────│                   │                │
```

### Strategy Registry Lookup

```
Registry.get(provider)
    │
    ├── strategies.get(CLOUDINARY)
    │       │
    │       ├── found  ──► return CloudinaryHostingStrategy
    │       │
    │       └── null   ──► throw IllegalArgumentException(400)
    │                       "No strategy registered for provider: CLOUDINARY.
    │                        Available: [S3]"
```

### Save Phase (ImageServiceImpl)

```
saveAll(images)
    │
    ├── null/empty? ──► return []
    │
    └── validate(images)
            │
            ├── count(isDefault == true)
            │       ├── > 1  ──► throw IllegalArgumentException (400)
            │       └── == 0 ──► images[0].setIsDefault(true)
            │
            └── repository.saveAll(images)
                    └── batch INSERT → DB
                        return List<T> with generated IDs
```

### Error Paths

| Scenario | Thrown By | Exception | HTTP |
|---|---|---|---|
| Provider not in registry | `Registry.get()` | `IllegalArgumentException` | 400 |
| File missing or null | caller guard | `IllegalArgumentException` | 400 |
| Cloudinary SDK failure | `CloudinaryHostingStrategy` | `IllegalStateException` | 500 |
| S3 SDK failure | `S3HostingStrategy` | `IllegalStateException` | 500 |
| More than 1 `isDefault` | `ImageServiceImpl.validate()` | `IllegalArgumentException` | 400 |
| DB constraint violation | `repository.saveAll()` | `DataIntegrityViolationException` | 409 |

---

## Full Request Flow

```
POST /api/v1/rooms/{roomId}/images
Content-Type: multipart/form-data

ImageItemRequest  (image file + caption + isDefault + sortOrder)
        │
        ▼  Controller calls:
imageUploadService.upload(file, ImageHostingProvider.CLOUDINARY)
        │
        ├── registry.get(CLOUDINARY)  →  CloudinaryHostingStrategy
        └── strategy.upload(file)    →  ImageUploadResponse { imageUrl, publicId }
        │
        ▼  Controller assembles ImageRequest:
ImageRequest { imageUrl, publicId, caption, isDefault, sortOrder }
        │
        ▼  Controller maps to domain entity:
RoomImageEntity { room, imageUrl, publicId, caption, isDefault, sortOrder }
        │
        ▼  Controller calls:
roomImageService.saveAll(entities)
        │
        ├── validate: isDefault rules
        └── repository.saveAll(entities)  →  batch INSERT → DB

Response: 201 Created
```

---

## Adding a New Provider

Only one new class is needed — no existing files change.

**Step 1 — Add enum value:**
```java
public enum ImageHostingProvider {
    CLOUDINARY,
    S3,
    GCS          // ← add here
}
```

**Step 2 — Implement the strategy:**
```java
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "gcs", name = "bucket")
public class GcsHostingStrategy implements ImageHostingStrategy {

    // inject GCS client ...

    @Override
    public ImageHostingProvider provider() {
        return ImageHostingProvider.GCS;
    }

    @Override
    public ImageUploadResponse upload(MultipartFile file) {
        // GCS upload logic ...
    }
}
```

**Step 3 — Set env vars** for the new provider's config.

`ImageHostingStrategyRegistry`, `ImageUploadServiceImpl`, and all existing strategies
remain completely unchanged.

---

## Adding Images to a New Domain

Four files, no commons changes needed.

| File | Responsibility |
|---|---|
| `RoomImageEntity extends ImageEntity` | `@Entity` + FK to `RoomEntity` |
| `RoomImageRepository extends JpaRepository<RoomImageEntity, Long>` | Spring Data repo |
| `RoomImageServiceImpl extends ImageServiceImpl<RoomImageEntity, RoomImageRepository>` | `@Service` + `getRepository()` |
| `RoomImageController` | calls `upload()` → assembles `ImageRequest` → maps to entity → `saveAll()` |

### Minimal `RoomImageServiceImpl`

```java
@Service
@RequiredArgsConstructor
public class RoomImageServiceImpl
        extends ImageServiceImpl<RoomImageEntity, RoomImageRepository> {

    private final RoomImageRepository repository;

    @Override
    protected RoomImageRepository getRepository() {
        return repository;
    }
}
```

### Minimal Controller Pattern

```java
@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> uploadImage(
        @PathVariable Long roomId,
        @ModelAttribute ImageItemRequest request,
        @RequestParam ImageHostingProvider provider) {

    RoomEntity room = roomService.getRoomEntity(roomId);

    // 1. upload to storage
    ImageUploadResponse uploaded = imageUploadService.upload(request.getImage(), provider);

    // 2. assemble full image data
    ImageRequest imageRequest = new ImageRequest();
    imageRequest.setImageUrl(uploaded.getImageUrl());
    imageRequest.setPublicId(uploaded.getPublicId());
    imageRequest.setCaption(request.getCaption());
    imageRequest.setIsDefault(request.getIsDefault());
    imageRequest.setSortOrder(request.getSortOrder());

    // 3. map to entity
    RoomImageEntity entity = new RoomImageEntity();
    entity.setRoom(room);
    entity.setImageUrl(imageRequest.getImageUrl());
    entity.setPublicId(imageRequest.getPublicId());
    entity.setCaption(imageRequest.getCaption());
    entity.setIsDefault(imageRequest.getIsDefault());
    entity.setSortOrder(imageRequest.getSortOrder());

    // 4. validate + persist
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(roomImageService.saveAll(List.of(entity)));
}
```
