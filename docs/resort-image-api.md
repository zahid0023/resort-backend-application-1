# Resort Image API

## Overview

The Resort Image API manages image uploads and metadata for individual resorts. Each resort stores its images with associated metadata (caption, sort order, default flag). Uploads are delegated to the resort's active image hosting provider (S3 or Cloudinary) via the configured credentials stored in `resort_image_storage_configs`.

**Base path:** `/api/v1/resorts/{resort-id}/images`

---

## Upload Flow

```
Client
  │
  ├─ POST /api/v1/resorts/{resort-id}/images
  │   Body: multipart/form-data
  │     - images: List<ImageItemRequest> (file + metadata per image)
  │
  ▼
ResortImageController
  │
  ├─ Fetch ResortEntity by resort-id
  ├─ Fetch active ResortImageStorageConfig for resort-id
  │     └─ Throws 404 if no active config found
  │
  ├─ For each image item:
  │   ├─ Resolve ImageHostingProvider from config.provider
  │   ├─ Upload file via ImageUploadService
  │   │     └─ Selects strategy (S3Strategy / CloudinaryStrategy)
  │   │     └─ Validates required config keys
  │   │     └─ Uploads file, returns { imageUrl, publicId }
  │   └─ Builds ImageRequest { imageUrl, publicId, caption, isDefault, sortOrder }
  │
  └─ resortImageService.saveImages(resortEntity, imageRequests)
        └─ Maps each ImageRequest → ResortImageEntity
        └─ Saves all entities
        └─ Returns SuccessResponse
```

---

## Endpoints

### 1. Upload Images

**`POST /api/v1/resorts/{resort-id}/images`**

Uploads one or more images for a resort. Requires the resort to have an active image hosting configuration.

**Content-Type:** `multipart/form-data`

#### Path Parameters

| Parameter  | Type   | Description   |
|------------|--------|---------------|
| `resort-id` | Long  | Resort ID     |

#### Request Parts

`images` — a JSON array of image items, each with:

| Field       | Type            | Required | Description                          |
|-------------|-----------------|----------|--------------------------------------|
| `image`     | MultipartFile   | Yes      | The image file to upload             |
| `caption`   | String          | No       | Alt text or display caption          |
| `is_default`| Boolean         | No       | Whether this is the resort's default image (default: `false`) |
| `sort_order`| Integer         | No       | Display order (default: `0`)         |

#### Response — `201 Created`

```json
{
  "success": true,
  "id": null
}
```

#### Errors

| Status | Reason |
|--------|--------|
| `404`  | Resort not found |
| `404`  | No active image storage config for this resort |
| `400`  | Config is missing required keys for the provider |
| `500`  | Upload failed at the hosting provider (S3/Cloudinary error) |

---

### 2. Get Single Image

**`GET /api/v1/resorts/{resort-id}/images/{id}`**

Returns metadata for a single resort image.

#### Path Parameters

| Parameter   | Type | Description  |
|-------------|------|--------------|
| `resort-id` | Long | Resort ID    |
| `id`        | Long | Image ID     |

#### Response — `200 OK`

```json
{
  "data": {
    "id": 1,
    "resort_id": 42,
    "image_url": "https://bucket.s3.us-east-1.amazonaws.com/uuid-photo.jpg",
    "public_id": "uuid-photo.jpg",
    "caption": "Poolside view",
    "is_default": true,
    "sort_order": 0
  }
}
```

#### `ResortImageDto` Fields

| Field        | Type    | Description                              |
|--------------|---------|------------------------------------------|
| `id`         | Long    | Image record ID                          |
| `resort_id`  | Long    | Owning resort ID                         |
| `image_url`  | String  | Public URL of the uploaded image         |
| `public_id`  | String  | Provider-assigned identifier (S3 key or Cloudinary public_id) |
| `caption`    | String  | Display caption                          |
| `is_default` | Boolean | Whether this is the resort's default image |
| `sort_order` | Integer | Display ordering position                |

#### Errors

| Status | Reason |
|--------|--------|
| `404`  | Image not found or does not belong to this resort |

---

### 3. List All Images (Paginated)

**`GET /api/v1/resorts/{resort-id}/images`**

Returns a paginated list of active images for the resort.

#### Path Parameters

| Parameter   | Type | Description |
|-------------|------|-------------|
| `resort-id` | Long | Resort ID   |

#### Query Parameters (PaginatedRequest)

| Parameter  | Type    | Default | Description                                      |
|------------|---------|---------|--------------------------------------------------|
| `page`     | Integer | `0`     | Zero-based page number                           |
| `size`     | Integer | `10`    | Page size                                        |
| `sort_by`  | String  | `id`    | Sort field — allowed: `id`, `sortOrder`, `isDefault` |
| `sort_dir` | String  | `asc`   | Sort direction: `asc` or `desc`                  |

#### Response — `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort_id": 42,
      "image_url": "https://bucket.s3.us-east-1.amazonaws.com/uuid-photo.jpg",
      "public_id": "uuid-photo.jpg",
      "caption": "Poolside view",
      "is_default": true,
      "sort_order": 0
    }
  ],
  "page": 0,
  "size": 10,
  "total_elements": 1,
  "total_pages": 1
}
```

---

### 4. Update Image Metadata

**`PUT /api/v1/resorts/{resort-id}/images/{id}`**

Updates editable metadata for an existing image. Does not re-upload the image file.

#### Path Parameters

| Parameter   | Type | Description |
|-------------|------|-------------|
| `resort-id` | Long | Resort ID   |
| `id`        | Long | Image ID    |

#### Request Body

```json
{
  "caption": "New caption",
  "is_default": false,
  "sort_order": 2
}
```

| Field        | Type    | Required | Description                      |
|--------------|---------|----------|----------------------------------|
| `caption`    | String  | No       | Updated caption (null = no change) |
| `is_default` | Boolean | No       | Updated default flag (null = no change) |
| `sort_order` | Integer | No       | Updated sort order (null = no change) |

All fields are optional — only non-null fields are applied.

#### Response — `200 OK`

```json
{
  "data": {
    "id": 1,
    "resort_id": 42,
    "image_url": "https://bucket.s3.us-east-1.amazonaws.com/uuid-photo.jpg",
    "public_id": "uuid-photo.jpg",
    "caption": "New caption",
    "is_default": false,
    "sort_order": 2
  }
}
```

#### Errors

| Status | Reason |
|--------|--------|
| `404`  | Image not found or does not belong to this resort |

---

### 5. Delete Image

**`DELETE /api/v1/resorts/{resort-id}/images/{id}`**

Soft-deletes a resort image (sets `is_deleted = true`, `is_active = false`).

#### Path Parameters

| Parameter   | Type | Description |
|-------------|------|-------------|
| `resort-id` | Long | Resort ID   |
| `id`        | Long | Image ID    |

> **Note:** The image file is **not** deleted from the hosting provider (S3/Cloudinary). Only the database record is soft-deleted.

#### Response — `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

#### Errors

| Status | Reason |
|--------|--------|
| `404`  | Image not found or does not belong to this resort |

---

## Database Schema

```sql
create table if not exists resort_images (
    id               bigserial    primary key,
    resort_id        bigint       not null references resorts(id),
    image_url        text         not null,
    public_id        varchar(255),
    caption          varchar(255),
    is_default       boolean      not null default false,
    sort_order       integer      not null default 0,

    -- audit fields
    created_by       bigint       not null,
    created_at       timestamptz  not null default current_timestamp,
    updated_by       bigint       not null,
    updated_at       timestamptz  not null default current_timestamp,
    version          bigint       not null default 0,

    -- soft-delete fields
    is_active        boolean      not null default true,
    is_deleted       boolean      not null default false,
    deleted_by       bigint,
    deleted_at       timestamptz
);
```

---

## Prerequisites

Before uploading images, the resort must have an active image storage configuration. See [Resort Image Storage Config API](./resort-image-storage-config.md) for how to create and activate a config.

The upload endpoint will return `404` if no active config exists for the resort.

---

## Related Documentation

- [Resort Image Storage Config API](./resort-image-storage-config.md) — Configure S3 or Cloudinary credentials per resort
- [Image Hosting Flow](./image-hosting-flow.md) — End-to-end architecture of the upload pipeline
