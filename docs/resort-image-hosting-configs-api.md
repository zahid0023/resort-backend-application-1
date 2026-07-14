# Resort Image Hosting Configs API

Base URL: `/api/v1/resorts/{resort-id}/resort-image-hosting-configs`

Resort image hosting configs store the credentials and provider settings used to upload and manage resort images.
Each config belongs to a specific resort and targets a cloud provider (Cloudinary or Amazon S3), holding the required
connection keys as a JSON object. A config's `provider` and `config` are immutable after creation —
only the display `name` can be updated. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                 | Description                    |
|--------|----------------------------------------------------------------------|--------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/resort-image-hosting-configs`           | Create an image hosting config |
| GET    | `/api/v1/resorts/{resort-id}/resort-image-hosting-configs`           | List image hosting configs     |
| GET    | `/api/v1/resorts/{resort-id}/resort-image-hosting-configs/{id}`      | Get an image hosting config    |
| GET    | `/api/v1/resorts/{resort-id}/resort-image-hosting-configs/providers` | List supported providers       |
| PUT    | `/api/v1/resorts/{resort-id}/resort-image-hosting-configs/{id}`      | Update an image hosting config |
| DELETE | `/api/v1/resorts/{resort-id}/resort-image-hosting-configs/{id}`      | Delete an image hosting config |

---

## Data Model

### Resort Image Hosting Config

| Field       | Type   | Required | Constraints                        | Description                                                                |
|-------------|--------|----------|------------------------------------|----------------------------------------------------------------------------|
| `id`        | Long   | —        | read-only                          | Auto-generated identifier                                                  |
| `resort_id` | Long   | —        | read-only                          | ID of the resort this config belongs to (derived from path)                |
| `name`      | String | Yes      | max 100 chars                      | Human-readable display name (e.g. `"Cloudinary – Main"`)                   |
| `provider`  | String | Yes      | `S3` or `CLOUDINARY`, immutable    | Cloud provider identifier; cannot be changed after creation                |
| `config`    | Object | Yes      | see provider keys below, immutable | Provider-specific credentials as a key/value map; immutable after creation |

### Provider Config Keys

Each provider requires a specific set of keys inside the `config` object.

**`S3`**

| Key          | Label             |
|--------------|-------------------|
| `bucket`     | Bucket Name       |
| `region`     | AWS Region        |
| `access_key` | Access Key ID     |
| `secret_key` | Secret Access Key |

**`CLOUDINARY`**

| Key          | Label      |
|--------------|------------|
| `cloud_name` | Cloud Name |
| `api_key`    | API Key    |
| `api_secret` | API Secret |

---

## Create Image Hosting Config

`POST /api/v1/resorts/{resort-id}/resort-image-hosting-configs`

Creates a new image hosting config for the resort identified by `{resort-id}`. All required keys for the chosen
provider must be present in `config` with non-blank values; missing or blank keys are rejected with `400`.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Request Fields

| Field      | Type   | Required | Validation                                                              |
|------------|--------|----------|-------------------------------------------------------------------------|
| `name`     | String | Yes      | Not null, max 100 chars                                                 |
| `provider` | String | Yes      | Not null, one of: `S3`, `CLOUDINARY`                                    |
| `config`   | Object | Yes      | Not null, must contain all provider-required keys with non-blank values |

### Request Body — Cloudinary example

```json
{
  "name": "Cloudinary – Main",
  "provider": "CLOUDINARY",
  "config": {
    "cloud_name": "my-cloud",
    "api_key": "123456789012345",
    "api_secret": "abcdefghijklmnopqrstuvwxyz"
  }
}
```

### Request Body — S3 example

```json
{
  "name": "S3 – Resort Assets",
  "provider": "S3",
  "config": {
    "bucket": "my-resort-assets",
    "region": "ap-southeast-1",
    "access_key": "AKIAIOSFODNN7EXAMPLE",
    "secret_key": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
  }
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Image Hosting Config

`GET /api/v1/resorts/{resort-id}/resort-image-hosting-configs/{id}`

Returns a single image hosting config with full credentials.

### Path Parameters

| Parameter   | Type | Description                    |
|-------------|------|--------------------------------|
| `resort-id` | Long | ID of the resort               |
| `id`        | Long | ID of the image hosting config |

### Response `200 OK`

```json
{
  "resort_image_hosting_config": {
    "id": 1,
    "resort_id": 1,
    "name": "Cloudinary – Main",
    "provider": "CLOUDINARY",
    "config": {
      "cloud_name": "my-cloud",
      "api_key": "123456789012345",
      "api_secret": "abcdefghijklmnopqrstuvwxyz"
    }
  }
}
```

---

## List Image Hosting Configs

`GET /api/v1/resorts/{resort-id}/resort-image-hosting-configs`

Returns a paginated list of active (non-deleted) image hosting configs for the given resort.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Query Parameters

| Parameter  | Type   | Default | Constraints                   | Description           |
|------------|--------|---------|-------------------------------|-----------------------|
| `page`     | int    | `0`     | >= 0                          | Zero-based page index |
| `size`     | int    | `10`    | 1 – 50                        | Items per page        |
| `sort_by`  | String | `id`    | `id`, `provider`, `createdAt` | Field to sort by      |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                 | Sort direction        |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort_id": 1,
      "name": "Cloudinary – Main",
      "provider": "CLOUDINARY",
      "config": {
        "cloud_name": "my-cloud",
        "api_key": "123456789012345",
        "api_secret": "abcdefghijklmnopqrstuvwxyz"
      }
    },
    {
      "id": 2,
      "resort_id": 1,
      "name": "S3 – Resort Assets",
      "provider": "S3",
      "config": {
        "bucket": "my-resort-assets",
        "region": "ap-southeast-1",
        "access_key": "AKIAIOSFODNN7EXAMPLE",
        "secret_key": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
      }
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

## List Supported Providers

`GET /api/v1/resorts/{resort-id}/resort-image-hosting-configs/providers`

Returns all supported image hosting providers along with their required config keys and display labels. Use this
endpoint to dynamically build config creation forms.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Response `200 OK`

```json
[
  {
    "provider": "S3",
    "label": "Amazon S3",
    "required_keys": [
      {
        "key": "bucket",
        "label": "Bucket Name"
      },
      {
        "key": "region",
        "label": "AWS Region"
      },
      {
        "key": "access_key",
        "label": "Access Key ID"
      },
      {
        "key": "secret_key",
        "label": "Secret Access Key"
      }
    ]
  },
  {
    "provider": "CLOUDINARY",
    "label": "Cloudinary",
    "required_keys": [
      {
        "key": "cloud_name",
        "label": "Cloud Name"
      },
      {
        "key": "api_key",
        "label": "API Key"
      },
      {
        "key": "api_secret",
        "label": "API Secret"
      }
    ]
  }
]
```

---

## Update Image Hosting Config

`PUT /api/v1/resorts/{resort-id}/resort-image-hosting-configs/{id}`

Updates the display `name` of an existing config. The `provider` and `config` fields are set at creation time and
cannot be changed.

### Path Parameters

| Parameter   | Type | Description                    |
|-------------|------|--------------------------------|
| `resort-id` | Long | ID of the resort               |
| `id`        | Long | ID of the image hosting config |

### Request Fields

| Field  | Type   | Required | Validation |
|--------|--------|----------|------------|
| `name` | String | Yes      | Not null   |

### Request Body

```json
{
  "name": "Cloudinary – Updated Name"
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Image Hosting Config

`DELETE /api/v1/resorts/{resort-id}/resort-image-hosting-configs/{id}`

Soft-deletes the image hosting config. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter   | Type | Description                    |
|-------------|------|--------------------------------|
| `resort-id` | Long | ID of the resort               |
| `id`        | Long | ID of the image hosting config |

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
  "message": "ResortImageHostingConfig not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                           |
|-------------|----------------------------|-----------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields, invalid sort field, or missing/blank provider config keys                              |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found, or image hosting config not found / already deleted / does not belong to the specified resort |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                                                                                            |
