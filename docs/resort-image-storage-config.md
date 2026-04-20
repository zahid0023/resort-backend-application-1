# Resort Image Storage Config — Documentation

## Table of Contents

1. [Overview](#overview)
2. [How It Fits in the Image Upload System](#how-it-fits-in-the-image-upload-system)
3. [Database Schema](#database-schema)
4. [Package Structure](#package-structure)
5. [Component Reference](#component-reference)
   - [ImageHostingProvider (enum)](#imagehostingprovider-enum)
   - [ResortImageStorageConfigEntity](#resortimagestorageconfigenity)
   - [ResortImageStorageConfigRepository](#resortimagestorageconfigurepository)
   - [Request DTOs](#request-dtos)
   - [Response DTOs](#response-dtos)
   - [ResortImageStorageConfigMapper](#resortimagestorageconfig-mapper)
   - [ResortImageStorageConfigService](#resortimagestorageconfiguservice)
   - [ResortImageStorageConfigServiceImpl](#resortimagestorageconfiguserviceimpl)
   - [ResortImageStorageConfigController](#resortimagestorageconfigucontroller)
6. [Validation Rules](#validation-rules)
7. [API Reference](#api-reference)
   - [POST — Create Config](#post--create-config)
   - [GET — Single Config](#get--single-config)
   - [GET — All Configs (Paginated)](#get--all-configs-paginated)
   - [PUT — Update Config](#put--update-config)
   - [DELETE — Delete Config](#delete--delete-config)
8. [Sequence Diagrams](#sequence-diagrams)
9. [Error Reference](#error-reference)

---

## Overview

A **Resort Image Storage Config** is a per-resort record that stores which image
hosting provider (AWS S3 or Cloudinary) the resort uses, along with the credentials
and settings required to connect to that provider.

**Why per-resort?** Different resorts may be operated by different entities, each with
their own cloud accounts. One resort may use Cloudinary; another may use an S3 bucket
in a different AWS account and region. This model supports that fully without any
global config override.

Key properties:

| Property | Description |
|---|---|
| `provider` | Enum: `S3` or `CLOUDINARY` |
| `config` | `Map<String, String>` — credentials and settings for the chosen provider |
| `resort` | The resort this config belongs to (FK) |
| Soft-delete | Records are never hard-deleted; `is_deleted = true` and `is_active = false` on delete |
| Auditing | All writes tracked via `created_by`, `updated_by`, `created_at`, `updated_at` |
| Optimistic lock | `version` column prevents concurrent overwrites |

---

## How It Fits in the Image Upload System

The broader image upload system (documented in `docs/image-upload-system.md`) uses
`ImageHostingStrategy` + `ImageHostingStrategyRegistry` to upload files.
Those strategies are initialised with an `ImageHostingConfig` object.

The `ResortImageStorageConfig` table **is the persistence layer** for those configs.
When a resort wants to upload an image, the flow is:

```
1. Load ResortImageStorageConfigEntity from DB  (provider + config map)
2. Build the matching ImageHostingConfig object  (AwsS3Config / CloudinaryConfig)
3. Call ImageUploadService.upload(file, provider, config)
4. Persist the returned URL in the resort's image entity
```

Storing configs in the DB instead of application.yaml means:
- No redeploy required to change credentials
- Each resort is fully isolated
- Credentials can be rotated through the API

---

## Database Schema

```sql
create table if not exists resort_image_storage_configs (
    id          bigserial     primary key,
    resort_id   bigint        not null references resorts(id),
    provider    varchar(50)   not null,          -- 'S3' or 'CLOUDINARY'
    config      jsonb         not null,          -- provider-specific key-value map
    created_by  bigint        not null,
    created_at  timestamptz   not null default current_timestamp,
    updated_by  bigint        not null,
    updated_at  timestamptz   not null default current_timestamp,
    version     bigint        not null default 0,
    is_active   boolean       not null default true,
    is_deleted  boolean       not null default false,
    deleted_by  bigint,
    deleted_at  timestamptz
);
```

### Column Notes

| Column | Detail |
|---|---|
| `provider` | Stored as the enum name string — `'S3'` or `'CLOUDINARY'` |
| `config` | PostgreSQL `jsonb` — queried with containment operators if needed |
| `version` | JPA `@Version` — bumped on every update; stale updates throw `OptimisticLockException` |
| `is_deleted` / `is_active` | Soft-delete flags. Active + not-deleted = visible through the API |
| `deleted_by` / `deleted_at` | Populated on delete; `is_active` set to `false`, `is_deleted` set to `true` |

---

## Package Structure

```
resortbackendapplication1/
│
├── commons/
│   └── enums/
│       └── ImageHostingProvider.java          ← Enum with required-key validation
│
├── controller/
│   └── ResortImageStorageConfigController.java
│
├── dto/
│   ├── request/resortimagestorageconfigs/
│   │   ├── ResortImageStorageConfigRequest.java   ← Abstract base (provider + config map)
│   │   ├── CreateResortImageStorageConfigRequest.java
│   │   └── UpdateResortImageStorageConfigRequest.java
│   │
│   └── response/resortimagestorageconfigs/
│       └── ResortImageStorageConfigResponse.java  ← Single-item response wrapper
│
├── model/
│   ├── dto/
│   │   └── ResortImageStorageConfigDto.java       ← Serializable DTO returned to callers
│   ├── entity/
│   │   └── ResortImageStorageConfigEntity.java    ← JPA entity
│   └── mapper/
│       └── ResortImageStorageConfigMapper.java    ← Entity ↔ DTO / Request → Entity
│
├── repository/
│   └── ResortImageStorageConfigRepository.java
│
├── service/
│   └── ResortImageStorageConfigService.java
│
└── serviceImpl/
    └── ResortImageStorageConfigServiceImpl.java
```

---

## Component Reference

### `ImageHostingProvider` (enum)

```java
public enum ImageHostingProvider {
    S3        (List.of("bucket", "region", "access_key", "secret_key")),
    CLOUDINARY(List.of("cloud_name", "api_key", "api_secret"));
}
```

Each enum constant carries the **list of required config map keys** for that provider.
The `validate(Map<String, String> config)` method checks every required key is present
and non-blank, throwing `IllegalArgumentException` with a descriptive message listing
all missing keys if validation fails.

**Required keys per provider:**

| Provider | Required keys |
|---|---|
| `S3` | `bucket`, `region`, `access_key`, `secret_key` |
| `CLOUDINARY` | `cloud_name`, `api_key`, `api_secret` |

---

### `ResortImageStorageConfigEntity`

JPA entity mapped to `resort_image_storage_configs`.

```java
@Entity
@Table(name = "resort_image_storage_configs")
public class ResortImageStorageConfigEntity extends AuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resort_id", nullable = false)
    private ResortEntity resort;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 50)
    private ImageHostingProvider provider;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "config", nullable = false)
    private Map<String, Object> config;
}
```

Inherits from `AuditableEntity`:

| Field | Type | Description |
|---|---|---|
| `id` | `Long` | Auto-generated primary key |
| `createdBy` | `Long` | User ID of creator (0 = SYSTEM) |
| `createdAt` | `Instant` | Creation timestamp |
| `updatedBy` | `Long` | User ID of last modifier |
| `updatedAt` | `Instant` | Last modification timestamp |
| `version` | `Long` | Optimistic lock version |
| `isActive` | `Boolean` | `true` while record is alive |
| `isDeleted` | `Boolean` | `true` after soft-delete |
| `deletedBy` | `Long` | User who deleted the record |
| `deletedAt` | `Instant` | Deletion timestamp |

---

### `ResortImageStorageConfigRepository`

```java
public interface ResortImageStorageConfigRepository
        extends JpaRepository<ResortImageStorageConfigEntity, Long> {

    Optional<ResortImageStorageConfigEntity>
        findByResort_IdAndIdAndIsActiveAndIsDeleted(
            Long resortId, Long id, boolean isActive, boolean isDeleted);

    Page<ResortImageStorageConfigEntity>
        findAllByResort_IdAndIsActiveAndIsDeleted(
            Long resortId, boolean isActive, boolean isDeleted, Pageable pageable);
}
```

All queries hard-filter on `isActive = true` and `isDeleted = false` so deleted records
are never surfaced through the API.

---

### Request DTOs

#### `ResortImageStorageConfigRequest` (abstract base)

```java
public abstract class ResortImageStorageConfigRequest {
    @NotNull(message = "Provider must not be null")
    private ImageHostingProvider provider;

    @NotNull(message = "Config must not be null")
    private Map<String, String> config;
}
```

Both `CreateResortImageStorageConfigRequest` and `UpdateResortImageStorageConfigRequest`
extend this class without adding fields. The abstract base is the single source of truth
for the request shape.

**Request body JSON:**

```json
{
  "provider": "S3",
  "config": {
    "bucket":     "my-resort-images",
    "region":     "ap-southeast-1",
    "access_key": "AKIAIOSFODNN7EXAMPLE",
    "secret_key": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
  }
}
```

**`config` map keys are snake_case.** The table below defines all valid keys:

| Provider | Key | Example value | Notes |
|---|---|---|---|
| `S3` | `bucket` | `my-resort-bucket` | S3 bucket name |
| `S3` | `region` | `us-east-1` | AWS region code |
| `S3` | `access_key` | `AKIA...` | IAM access key ID |
| `S3` | `secret_key` | `wJalr...` | IAM secret access key |
| `CLOUDINARY` | `cloud_name` | `my-cloud` | Cloudinary cloud identifier |
| `CLOUDINARY` | `api_key` | `123456789012345` | Cloudinary API key |
| `CLOUDINARY` | `api_secret` | `abcDEF...` | Cloudinary API secret |

---

### Response DTOs

#### `ResortImageStorageConfigDto`

Returned inside all single-item and paginated responses.

```java
public class ResortImageStorageConfigDto {
    private Long id;
    private Long resortId;
    private String provider;
    private Map<String, Object> config;
}
```

JSON serialisation uses `snake_case` (`resort_id`). `null` fields are excluded via
`@JsonInclude(NON_NULL)`.

#### `ResortImageStorageConfigResponse`

Single-item wrapper:
```json
{
  "data": {
    "id": 1,
    "resort_id": 5,
    "provider": "S3",
    "config": {
      "bucket":     "my-resort-images",
      "region":     "ap-southeast-1",
      "access_key": "AKIAIOSFODNN7EXAMPLE",
      "secret_key": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
    }
  }
}
```

#### `PaginatedResponse<ResortImageStorageConfigDto>`

```json
{
  "data": [ ... ],
  "current_page": 0,
  "total_pages": 3,
  "total_elements": 25,
  "page_size": 10,
  "has_next": true,
  "has_previous": false
}
```

#### `SuccessResponse`

Returned from create, update, and delete endpoints:
```json
{
  "success": true,
  "id": 7
}
```

---

### `ResortImageStorageConfigMapper`

Utility class (no instantiation) handling all conversions:

| Method | Input | Output | Description |
|---|---|---|---|
| `fromRequest(request, resort)` | `CreateResortImageStorageConfigRequest` + `ResortEntity` | `ResortImageStorageConfigEntity` | Maps request fields onto a new entity |
| `updateEntity(entity, request)` | `ResortImageStorageConfigEntity` + `UpdateResortImageStorageConfigRequest` | `void` | Patches only non-null fields on the existing entity |
| `toDto(entity)` | `ResortImageStorageConfigEntity` | `ResortImageStorageConfigDto` | Produces the serializable DTO |

`fromRequest` and `updateEntity` both call `Map.copyOf(request.getConfig())`, which
produces an immutable defensive copy — the stored map cannot be mutated after mapping.

The entity's `config` field is `Map<String, Object>` (to satisfy the JSONB type mapping),
but the incoming request uses `Map<String, String>`. The implicit widening from
`String` → `Object` happens in the copy.

---

### `ResortImageStorageConfigService`

```java
public interface ResortImageStorageConfigService {

    SuccessResponse createResortImageStorageConfig(
        CreateResortImageStorageConfigRequest request, ResortEntity resort);

    ResortImageStorageConfigEntity getResortImageStorageConfigEntity(Long resortId, Long id);

    ResortImageStorageConfigResponse getResortImageStorageConfig(Long resortId, Long id);

    PaginatedResponse<ResortImageStorageConfigDto> getAllResortImageStorageConfigs(
        Long resortId, Pageable pageable);

    SuccessResponse updateResortImageStorageConfig(
        ResortImageStorageConfigEntity entity, UpdateResortImageStorageConfigRequest request);

    SuccessResponse deleteResortImageStorageConfig(Long resortId, Long id);
}
```

---

### `ResortImageStorageConfigServiceImpl`

Key behaviours:

**Create**
1. Calls `request.getProvider().validate(request.getConfig())` — throws
   `IllegalArgumentException` if any required key is missing or blank.
2. Delegates to mapper to build the entity.
3. Persists and returns `SuccessResponse { success: true, id: <new-id> }`.

**Get single**
- Queries by `(resortId, id, isActive=true, isDeleted=false)`.
- Throws `EntityNotFoundException` (→ HTTP 404) if not found.

**Get all**
- Returns a `PaginatedResponse` filtered to active, non-deleted records for the resort.
- Sort fields allowed: `id`, `provider`.

**Update**
- Validates the new config map against the effective provider (new provider if provided,
  existing provider otherwise) before patching.
- Only non-null fields in the request overwrite the entity — partial updates are safe.

**Delete**
- Soft-delete: sets `isDeleted = true`, `isActive = false`.
- The record stays in the database for audit purposes.
- Subsequent GET calls for this record will return 404.

---

### `ResortImageStorageConfigController`

Base path: `/api/v1/resorts/{resort-id}/resort-image-storage-configs`

Responsibilities:
- Validates request bodies with `@Valid` (triggers `@NotNull` constraints).
- Resolves the parent `ResortEntity` via `ResortService.getResortById()` before create.
- Resolves the target entity for update via `getResortImageStorageConfigEntity()` before update.
- Delegates all business logic to `ResortImageStorageConfigService`.

---

## Validation Rules

Validation runs in two layers.

### Layer 1 — Jakarta Bean Validation (`@Valid` in controller)

| Field | Constraint | Error |
|---|---|---|
| `provider` | `@NotNull` | `"Provider must not be null"` |
| `config` | `@NotNull` | `"Config must not be null"` |

Triggered automatically by Spring before the controller method body executes.
Returns HTTP 400 with a validation error body.

### Layer 2 — Provider Config Validation (service layer)

Called explicitly in `createResortImageStorageConfig` and `updateResortImageStorageConfig`:

```
request.getProvider().validate(request.getConfig())
```

| Provider | Required keys | Error message example |
|---|---|---|
| `S3` | `bucket`, `region`, `access_key`, `secret_key` | `"Missing or blank required config keys for provider S3: [region, secret_key]. Required keys: [bucket, region, access_key, secret_key]"` |
| `CLOUDINARY` | `cloud_name`, `api_key`, `api_secret` | `"Missing or blank required config keys for provider CLOUDINARY: [api_secret]. Required keys: [cloud_name, api_key, api_secret]"` |

A key is considered missing if it is absent from the map **or** present with a blank value.
All missing keys are reported together in a single exception — no partial error reporting.

### Update Validation Logic

For updates, the provider used to validate the new config is determined as follows:

```
request.provider != null && request.config != null  →  validate with request.provider
request.provider == null && request.config != null  →  validate with entity.provider (existing)
request.provider != null && request.config == null  →  no config validation (keys unchanged)
both null                                           →  no-op, nothing changes
```

---

## API Reference

### `POST` — Create Config

```
POST /api/v1/resorts/{resort-id}/resort-image-storage-configs
Content-Type: application/json
```

**Path parameter:** `resort-id` — ID of the resort.

**Request body (S3 example):**
```json
{
  "provider": "S3",
  "config": {
    "bucket":     "my-resort-images",
    "region":     "us-east-1",
    "access_key": "AKIAIOSFODNN7EXAMPLE",
    "secret_key": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
  }
}
```

**Request body (Cloudinary example):**
```json
{
  "provider": "CLOUDINARY",
  "config": {
    "cloud_name": "my-resort-cloud",
    "api_key":    "123456789012345",
    "api_secret": "abcDEFghiJKLmnoPQR"
  }
}
```

**Response `201 Created`:**
```json
{
  "success": true,
  "id": 12
}
```

**Failure cases:**

| Scenario | HTTP | Body |
|---|---|---|
| `provider` is null | 400 | Bean validation error |
| `config` is null | 400 | Bean validation error |
| Required config key missing/blank | 400 | `"Missing or blank required config keys for provider S3: [region]..."` |
| `resort-id` not found | 404 | Entity not found error |

---

### `GET` — Single Config

```
GET /api/v1/resorts/{resort-id}/resort-image-storage-configs/{id}
```

**Path parameters:**

| Param | Description |
|---|---|
| `resort-id` | Resort ID |
| `id` | Config record ID |

**Response `200 OK`:**
```json
{
  "data": {
    "id": 12,
    "resort_id": 5,
    "provider": "S3",
    "config": {
      "bucket":     "my-resort-images",
      "region":     "us-east-1",
      "access_key": "AKIAIOSFODNN7EXAMPLE",
      "secret_key": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
    }
  }
}
```

**Failure cases:**

| Scenario | HTTP |
|---|---|
| Config not found or deleted | 404 |
| Config belongs to a different resort | 404 |

---

### `GET` — All Configs (Paginated)

```
GET /api/v1/resorts/{resort-id}/resort-image-storage-configs
```

**Query parameters (all optional):**

| Param | Default | Constraints | Description |
|---|---|---|---|
| `page` | `0` | `>= 0` | Zero-based page index |
| `size` | `10` | `1–50` | Records per page |
| `sort_by` | `id` | `id`, `provider` | Sort field |
| `sort_dir` | `ASC` | `ASC`, `DESC` | Sort direction |

**Response `200 OK`:**
```json
{
  "data": [
    {
      "id": 12,
      "resort_id": 5,
      "provider": "S3",
      "config": { ... }
    },
    {
      "id": 13,
      "resort_id": 5,
      "provider": "CLOUDINARY",
      "config": { ... }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 2,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

**Failure cases:**

| Scenario | HTTP |
|---|---|
| Invalid `sort_by` value | 400 |

---

### `PUT` — Update Config

```
PUT /api/v1/resorts/{resort-id}/resort-image-storage-configs/{id}
Content-Type: application/json
```

Partial updates are supported — only non-null fields overwrite existing values.

**Request body examples:**

*Change provider and replace all config:*
```json
{
  "provider": "CLOUDINARY",
  "config": {
    "cloud_name": "new-cloud",
    "api_key":    "999888777666555",
    "api_secret": "newSecretValue123"
  }
}
```

*Update only the secret key (keep everything else):*
```json
{
  "provider": null,
  "config": {
    "bucket":     "my-resort-images",
    "region":     "us-east-1",
    "access_key": "AKIAIOSFODNN7EXAMPLE",
    "secret_key": "NEW_SECRET_KEY_VALUE"
  }
}
```

> **Note:** Because `config` is stored as a complete JSON object, a partial config update
> must re-supply all required keys for the provider — the map is replaced entirely, not
> merged field by field.

**Response `200 OK`:**
```json
{
  "success": true,
  "id": 12
}
```

**Failure cases:**

| Scenario | HTTP |
|---|---|
| Config record not found or deleted | 404 |
| Required config key missing/blank | 400 |
| `provider` is null but `config` is present and fails validation against existing provider | 400 |
| Concurrent modification (optimistic lock) | 409 |

---

### `DELETE` — Delete Config

```
DELETE /api/v1/resorts/{resort-id}/resort-image-storage-configs/{id}
```

Soft-delete: sets `is_deleted = true`, `is_active = false`. The record is retained
in the database for audit purposes and is no longer returned by any GET endpoint.

**Response `200 OK`:**
```json
{
  "success": true,
  "id": 12
}
```

**Failure cases:**

| Scenario | HTTP |
|---|---|
| Config not found or already deleted | 404 |

---

## Sequence Diagrams

### Create Flow

```
Client                Controller           ResortService     ConfigService         DB
  │                       │                     │                 │                 │
  │─ POST /resorts/5/... ─►│                     │                 │                 │
  │   { provider, config } │                     │                 │                 │
  │                       │─ getResortById(5) ──►│                 │                 │
  │                       │◄─ ResortEntity ───── │                 │                 │
  │                       │                     │                 │                 │
  │                       │─ createConfig(req, resort) ──────────►│                 │
  │                       │                     │  validate(config map)             │
  │                       │                     │  (missing key?) ──► 400 error     │
  │                       │                     │                 │                 │
  │                       │                     │  fromRequest()  │                 │
  │                       │                     │  (build entity) │                 │
  │                       │                     │                 │─ save(entity) ──►│
  │                       │                     │                 │◄─ saved entity ─ │
  │                       │◄── SuccessResponse ─────────────────  │                 │
  │◄─ 201 { success, id } ─│                     │                 │                 │
```

### Get Single Flow

```
Client               Controller           ConfigService          DB
  │                      │                    │                   │
  │─ GET /resorts/5/..../12 ─────────────────►│                   │
  │                      │─ getConfig(5, 12) ─►│                   │
  │                      │                    │─ findBy(5,12,true,false) ──►│
  │                      │                    │◄─ entity (or empty) ───────│
  │                      │                    │                   │
  │                      │                    │ empty? ──► EntityNotFoundException (404)
  │                      │                    │                   │
  │                      │                    │ toDto(entity)     │
  │                      │◄── ConfigResponse ─│                   │
  │◄─ 200 { data: {...} } ─│                   │                   │
```

### Update Flow

```
Client               Controller           ConfigService          DB
  │                      │                    │                   │
  │─ PUT /resorts/5/..../12 ────────────────────────────────────  │
  │   { provider, config }│                   │                   │
  │                      │─ getConfigEntity(5,12) ───────────────►│
  │                      │◄─ entity ──────────────────────────── │
  │                      │                    │                   │
  │                      │─ updateConfig(entity, request) ───────►│
  │                      │                    │ validate(config)  │
  │                      │                    │ (wrong keys?) ──► 400
  │                      │                    │                   │
  │                      │                    │ updateEntity()    │
  │                      │                    │─ save(entity) ────►│
  │                      │                    │◄─ saved ──────── │
  │                      │◄── SuccessResponse ─│                  │
  │◄─ 200 { success, id } ─│                   │                  │
```

### Delete Flow

```
Client               Controller           ConfigService          DB
  │                      │                    │                   │
  │─ DELETE /resorts/5/.../12 ──────────────────────────────────  │
  │                      │─ deleteConfig(5, 12) ─────────────────►│
  │                      │                    │─ findBy(5,12,...) ─►│
  │                      │                    │◄─ entity ─────── │
  │                      │                    │                   │
  │                      │                    │ isDeleted = true  │
  │                      │                    │ isActive  = false │
  │                      │                    │─ save(entity) ────►│
  │                      │◄── SuccessResponse ─│                  │
  │◄─ 200 { success, id } ─│                   │                  │
```

---

## Error Reference

| Scenario | Exception | HTTP Status | Message |
|---|---|---|---|
| `provider` field is null in request | `MethodArgumentNotValidException` | 400 | `"Provider must not be null"` |
| `config` field is null in request | `MethodArgumentNotValidException` | 400 | `"Config must not be null"` |
| Required config key is missing | `IllegalArgumentException` | 400 | `"Missing or blank required config keys for provider S3: [region]..."` |
| Config value is blank (whitespace only) | `IllegalArgumentException` | 400 | Same as above |
| Invalid `sort_by` value in pagination | `IllegalArgumentException` | 400 | `"Invalid sort field: <value>"` |
| Config record not found | `EntityNotFoundException` | 404 | `"ResortImageStorageConfig with id: <id> was not found."` |
| Config belongs to a different resort | `EntityNotFoundException` | 404 | Same (query filters by resort_id) |
| Resort not found on create | `EntityNotFoundException` | 404 | Resort not found message |
| Concurrent write conflict | `ObjectOptimisticLockingFailureException` | 409 | Optimistic lock failure |
