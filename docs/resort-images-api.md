# Resort Images API

Base URL: `/api/v1/resorts/{resort-id}/images`

Resort images are media files uploaded to a cloud provider (Cloudinary or S3) and associated with both a resort and
an image hosting config. Each image stores the remote URL, the provider's asset identifier (`external_id`), an
optional caption, and a display order. The `resort_id` is set automatically from the hosting config at upload time.
On delete, the file is removed from the cloud provider before the record is soft-deleted.

---

## Endpoints

| Method | Path                                      | Description           |
|--------|-------------------------------------------|-----------------------|
| POST   | `/api/v1/resorts/{resort-id}/images`      | Upload resort images  |
| GET    | `/api/v1/resorts/{resort-id}/images`      | List resort images    |
| GET    | `/api/v1/resorts/{resort-id}/images/{id}` | Get a resort image    |
| PUT    | `/api/v1/resorts/{resort-id}/images/{id}` | Update a resort image |
| DELETE | `/api/v1/resorts/{resort-id}/images/{id}` | Delete a resort image |

---

## Data Model

### Resort Image

| Field         | Type    | Required | Constraints           | Description                                                                             |
|---------------|---------|----------|-----------------------|-----------------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only             | Auto-generated identifier                                                               |
| `resort_id`   | Long    | —        | read-only             | ID of the resort this image belongs to (derived from the hosting config)                |
| `config_id`   | Long    | —        | read-only             | ID of the image hosting config used to upload this image; set at upload time, immutable |
| `external_id` | String  | No       | max 255 chars         | Provider asset identifier (Cloudinary `public_id` or S3 object key); used for deletion  |
| `url`         | String  | Yes      | unique                | Full CDN/S3 URL of the stored image                                                     |
| `caption`     | String  | No       | max 255 chars         | Optional display caption; omitted from response if null                                 |
| `sort_order`  | Integer | Yes      | not null, default `0` | Display order                                                                           |

---

## Upload Resort Images

`POST /api/v1/resorts/{resort-id}/images`

Uploads one or more images to the cloud provider configured by `config_id`, then persists each image record. The
request is `multipart/form-data` with two parts: `images` (the binary files) and `meta` (a JSON array describing each
file). Each meta entry is matched to an image by the file's original filename (`client_image_id`).

If any upload fails after some files have already been uploaded successfully, the already-uploaded files are
automatically deleted from the provider before the error is returned.

### Path Parameters

| Parameter   | Type | Required | Description      |
|-------------|------|----------|------------------|
| `resort-id` | Long | Yes      | ID of the resort |

### Form Parts

| Part        | Type                  | Required | Description                                                        |
|-------------|-----------------------|----------|--------------------------------------------------------------------|
| `images`    | `List<MultipartFile>` | Yes      | The image files to upload                                          |
| `meta`      | JSON array            | Yes      | Metadata for each image; see Meta fields below                     |
| `config_id` | Long (query param)    | Yes      | ID of the image hosting config to use (must belong to this resort) |

### Meta Fields (`meta[]`)

| Field             | Type    | Required | Description                                                    |
|-------------------|---------|----------|----------------------------------------------------------------|
| `client_image_id` | String  | Yes      | Must match the `originalFilename` of one of the uploaded files |
| `caption`         | String  | No       | Optional caption for this image                                |
| `is_default`      | Boolean | No       | Default `false`                                                |
| `sort_order`      | Integer | No       | Default `0`                                                    |

### Example Request (multipart)

```
POST /api/v1/resorts/1/images?config_id=1
Content-Type: multipart/form-data

--boundary
Content-Disposition: form-data; name="meta"
Content-Type: application/json

[
  {
    "client_image_id": "pool.jpg",
    "caption": "Main swimming pool",
    "is_default": true,
    "sort_order": 1
  },
  {
    "client_image_id": "lobby.jpg",
    "caption": "Grand lobby entrance",
    "sort_order": 2
  }
]

--boundary
Content-Disposition: form-data; name="images"; filename="pool.jpg"
Content-Type: image/jpeg

<binary data>

--boundary
Content-Disposition: form-data; name="images"; filename="lobby.jpg"
Content-Type: image/jpeg

<binary data>
--boundary--
```

### Response `201 Created`

Returns a list of all created image DTOs.

```json
[
  {
    "id": 10,
    "resort_id": 1,
    "config_id": 1,
    "external_id": "uuid_pool.jpg",
    "url": "https://my-resort-assets.s3.ap-southeast-1.amazonaws.com/uuid_pool.jpg",
    "caption": "Main swimming pool",
    "sort_order": 1
  },
  {
    "id": 11,
    "resort_id": 1,
    "config_id": 1,
    "external_id": "uuid_lobby.jpg",
    "url": "https://my-resort-assets.s3.ap-southeast-1.amazonaws.com/uuid_lobby.jpg",
    "caption": "Grand lobby entrance",
    "sort_order": 2
  }
]
```

---

## List Resort Images

`GET /api/v1/resorts/{resort-id}/images`

Returns a paginated list of active (non-deleted) resort images for the specified resort. The list response uses the
`ResortImageSummary` projection, which omits `config_id`. Use the get-by-id endpoint to retrieve the full record
including `config_id`.

### Path Parameters

| Parameter   | Type | Required | Description      |
|-------------|------|----------|------------------|
| `resort-id` | Long | Yes      | ID of the resort |

### Query Parameters

| Parameter  | Type   | Default | Constraints                    | Description           |
|------------|--------|---------|--------------------------------|-----------------------|
| `page`     | int    | `0`     | >= 0                           | Zero-based page index |
| `size`     | int    | `10`    | 1 – 50                         | Items per page        |
| `sort_by`  | String | `id`    | `id`, `sortOrder`, `createdAt` | Field to sort by      |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                  | Sort direction        |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 10,
      "resort_id": 1,
      "external_id": "uuid_pool.jpg",
      "url": "https://my-resort-assets.s3.ap-southeast-1.amazonaws.com/uuid_pool.jpg",
      "caption": "Main swimming pool",
      "sort_order": 1
    },
    {
      "id": 11,
      "resort_id": 1,
      "external_id": "uuid_lobby.jpg",
      "url": "https://my-resort-assets.s3.ap-southeast-1.amazonaws.com/uuid_lobby.jpg",
      "caption": "Grand lobby entrance",
      "sort_order": 2
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

---

## Get Resort Image

`GET /api/v1/resorts/{resort-id}/images/{id}`

Returns a single resort image with all fields, including `config_id`.

### Path Parameters

| Parameter   | Type | Required | Description            |
|-------------|------|----------|------------------------|
| `resort-id` | Long | Yes      | ID of the resort       |
| `id`        | Long | Yes      | ID of the resort image |

### Response `200 OK`

```json
{
  "resort_image": {
    "id": 10,
    "resort_id": 1,
    "config_id": 1,
    "external_id": "uuid_pool.jpg",
    "url": "https://my-resort-assets.s3.ap-southeast-1.amazonaws.com/uuid_pool.jpg",
    "caption": "Main swimming pool",
    "sort_order": 1
  }
}
```

---

## Update Resort Image

`PUT /api/v1/resorts/{resort-id}/images/{id}`

Updates the `caption` and `sort_order` of an existing resort image. The `url`, `external_id`, and `config_id` are
set at upload time and cannot be changed.

### Path Parameters

| Parameter   | Type | Required | Description            |
|-------------|------|----------|------------------------|
| `resort-id` | Long | Yes      | ID of the resort       |
| `id`        | Long | Yes      | ID of the resort image |

### Request Fields

| Field        | Type    | Required | Validation    | Description                            |
|--------------|---------|----------|---------------|----------------------------------------|
| `caption`    | String  | No       | Max 255 chars | Optional display caption for the image |
| `sort_order` | Integer | Yes      | Not null      | Display order                          |

### Request Body

```json
{
  "caption": "Updated pool caption",
  "sort_order": 3
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 10
}
```

---

## Delete Resort Image

`DELETE /api/v1/resorts/{resort-id}/images/{id}`

Deletes the image from the cloud provider (using the stored `external_id`) and then soft-deletes the database record.
If `external_id` is null, the cloud deletion step is skipped. The record is not permanently removed from the database
but will no longer appear in any response.

### Path Parameters

| Parameter   | Type | Required | Description            |
|-------------|------|----------|------------------------|
| `resort-id` | Long | Yes      | ID of the resort       |
| `id`        | Long | Yes      | ID of the resort image |

### Response `200 OK`

```json
{
  "success": true,
  "id": 10
}
```

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "Resort image not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                  |
|-------------|----------------------------|----------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields, invalid sort field, or no metadata found for an uploaded file |
| 404         | `ENTITY_NOT_FOUND`         | Image hosting config not found, or resort image not found / already deleted            |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate URL constraint violation                                                     |
| 500         | `INTERNAL_SERVER_ERROR`    | Upload to or deletion from Cloudinary / S3 failed                                      |
