# Resorts API

Base URL: `/api/v1/resorts`

Resorts are the core entities of the application. Each resort belongs to a country and optionally a city. All records
support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                   | Description      |
|--------|------------------------|------------------|
| POST   | `/api/v1/resorts`      | Create a resort  |
| GET    | `/api/v1/resorts`      | List all resorts |
| GET    | `/api/v1/resorts/{id}` | Get a resort     |
| PUT    | `/api/v1/resorts/{id}` | Update a resort  |
| DELETE | `/api/v1/resorts/{id}` | Delete a resort  |

---

## Data Model

| Field           | Type   | Required | Max Length | Description                                 |
|-----------------|--------|----------|------------|---------------------------------------------|
| `name`          | String | Yes      | 255        | Display name of the resort                  |
| `description`   | String | Yes      | unlimited  | Full description (defaults to empty string) |
| `address`       | String | Yes      | unlimited  | Physical address                            |
| `country_id`    | Long   | Yes      | —          | ID of the associated country                |
| `city_id`       | Long   | Yes      | —          | ID of the associated city                   |
| `contact_email` | String | Yes      | 255        | Contact email address                       |
| `contact_phone` | String | Yes      | 50         | Contact phone number                        |

---

## Create Resort

`POST /api/v1/resorts`
`Content-Type: multipart/form-data`
`Authorization: Bearer <token>` — requires `USER` role

Creates a resort and, in a single request, configures its image storage provider and uploads
its initial images. The request uses two multipart parts:

| Part     | Content-Type       | Description                                       |
|----------|--------------------|---------------------------------------------------|
| `data`   | `application/json` | JSON object with resort fields, storage config, and image metadata |
| `images` | binary files       | One or more image files to upload                 |

The controller processes the parts in this order:

1. Create the resort record
2. For each image: look up its metadata from `data.images` by matching `client_image_id` to the file's original filename, upload the file via the configured storage provider, then persist the image record.

### `data` Part — JSON Schema

```json
{
  "name":          "string  (required)",
  "description":   "string  (required)",
  "address":       "string  (required)",
  "country_id":    "long    (required)",
  "city_id":       "long    (required)",
  "contact_email": "string  (required)",
  "contact_phone": "string  (required)",

  "config_request": {
    "provider": "S3 | CLOUDINARY  (required)",
    "config":   { "key": "value", "..." }
  },

  "images": [
    {
      "client_image_id": "string  — must match the uploaded file's original filename (required)",
      "caption":         "string  (optional)",
      "is_default":      "boolean (optional, default: false)",
      "sort_order":      "integer (optional, default: 0)"
    }
  ]
}
```

#### `config_request.config` keys by provider

**S3**

| Key          | Required | Description                       |
|--------------|----------|-----------------------------------|
| `bucket`     | Yes      | S3 bucket name                    |
| `region`     | Yes      | AWS region (e.g. `ap-southeast-1`)|
| `access_key` | Yes      | IAM access key ID                 |
| `secret_key` | Yes      | IAM secret access key             |

**CLOUDINARY**

| Key          | Required | Description                  |
|--------------|----------|------------------------------|
| `cloud_name` | Yes      | Cloudinary cloud identifier  |
| `api_key`    | Yes      | Cloudinary API key           |
| `api_secret` | Yes      | Cloudinary API secret        |

See [Resort Image Storage Configs API](resort-image-storage-configs-api.md) for full provider details.

### `images` Part

One or more binary image files. Each file's **original filename** must match the `client_image_id`
of exactly one entry in `data.images`. If no matching metadata entry is found the request fails
with `400 Bad Request`.

### Example — Resort + S3 storage config + two images

```
POST /api/v1/resorts
Content-Type: multipart/form-data; boundary=----FormBoundary

------FormBoundary
Content-Disposition: form-data; name="data"
Content-Type: application/json

{
  "name": "Sunset Beach Resort",
  "description": "A beautiful beachfront resort.",
  "address": "123 Ocean Drive, Boracay",
  "country_id": 1,
  "city_id": 5,
  "contact_email": "info@sunsetbeach.com",
  "contact_phone": "+63-912-345-6789",
  "config_request": {
    "provider": "S3",
    "config": {
      "bucket": "my-resort-images",
      "region": "ap-southeast-1",
      "access_key": "AKIAIOSFODNN7EXAMPLE",
      "secret_key": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
    }
  },
  "images": [
    { "client_image_id": "pool.jpg",  "caption": "Infinity pool at sunset", "is_default": true,  "sort_order": 0 },
    { "client_image_id": "lobby.jpg", "caption": "Grand lobby entrance",    "is_default": false, "sort_order": 1 }
  ]
}
------FormBoundary
Content-Disposition: form-data; name="images"; filename="pool.jpg"
Content-Type: image/jpeg

<binary image data>
------FormBoundary
Content-Disposition: form-data; name="images"; filename="lobby.jpg"
Content-Type: image/jpeg

<binary image data>
------FormBoundary--
```

### Example — Resort + Cloudinary storage config

```json
{
  "name": "Palm Cove Resort",
  "description": "Nestled among tropical palms.",
  "address": "88 Palm Lane",
  "country_id": 1,
  "city_id": 3,
  "contact_email": "hello@palmcove.com",
  "contact_phone": "+1-800-555-0199",
  "config_request": {
    "provider": "CLOUDINARY",
    "config": {
      "cloud_name": "my-resort-cloud",
      "api_key": "123456789012345",
      "api_secret": "abcDEFghiJKLmnoPQR"
    }
  },
  "images": [
    { "client_image_id": "beach.jpg", "is_default": true, "sort_order": 0 }
  ]
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 7
}
```

### Error Cases

| Status | Scenario                                                              |
|--------|-----------------------------------------------------------------------|
| `400`  | `countryId` not provided                                              |
| `400`  | `storageConfig.provider` is null when `storageConfig` fields are sent |
| `400`  | Required storage config key is missing or blank for the provider      |
| `400`  | Images provided without a `storageConfig`                             |
| `401`  | Missing or invalid JWT token                                          |
| `403`  | Authenticated user does not have `USER` role                          |
| `404`  | Country or city not found                                             |

---

## Get Resort

`GET /api/v1/resorts/{id}`

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the resort |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "uuid": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Sunset Beach Resort",
    "description": "A beautiful beachfront resort with stunning ocean views.",
    "address": "123 Ocean Drive, Boracay",
    "country_id": 1,
    "city_id": 5,
    "contact_email": "info@sunsetbeach.com",
    "contact_phone": "+63-912-345-6789"
  }
}
```

### Error Cases

| Status | Scenario         |
|--------|------------------|
| `404`  | Resort not found |

---

## List All Resorts

`GET /api/v1/resorts`
`Authorization: Bearer <token>` — requires `ADMIN` or `USER` role

Returns a paginated list of active (non-deleted) resorts. Results are scoped by role:

- **ADMIN** — sees all resorts in the system.
- **USER** — sees only resorts they have access to (i.e. resorts they own or have been granted access to).

### Query Parameters

| Parameter  | Type   | Default | Constraints   | Description              |
|------------|--------|---------|---------------|--------------------------|
| `page`     | int    | `0`     | >= 0          | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50        | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `name`  | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC` | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "uuid": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Sunset Beach Resort",
      "description": "A beautiful beachfront resort with stunning ocean views.",
      "address": "123 Ocean Drive, Boracay",
      "country_id": 1,
      "city_id": 5,
      "contact_email": "info@sunsetbeach.com",
      "contact_phone": "+63-912-345-6789"
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Resort

`PUT /api/v1/resorts/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the resort |

### Request Body

```json
{
  "name": "Sunset Beach Resort & Spa",
  "description": "A luxurious beachfront resort and spa.",
  "contact_email": "contact@sunsetbeachspa.com"
}
```

| Field           | Type   | Required |
|-----------------|--------|----------|
| `name`          | String | No       |
| `description`   | String | No       |
| `address`       | String | No       |
| `country_id`    | Long   | No       |
| `city_id`       | Long   | No       |
| `contact_email` | String | No       |
| `contact_phone` | String | No       |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "uuid": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Sunset Beach Resort & Spa",
    "description": "A luxurious beachfront resort and spa.",
    "address": "123 Ocean Drive, Boracay",
    "country_id": 1,
    "city_id": 5,
    "contact_email": "contact@sunsetbeachspa.com",
    "contact_phone": "+63-912-345-6789"
  }
}
```

### Error Cases

| Status | Scenario                           |
|--------|------------------------------------|
| `404`  | Resort, country, or city not found |

---

## Delete Resort

`DELETE /api/v1/resorts/{id}`

Soft-deletes the resort. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the resort |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
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
  "message": "Resort with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                                  |
|-------------|----------------------------|--------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data                 |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found, or referenced country/city not found |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                                   |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                |
