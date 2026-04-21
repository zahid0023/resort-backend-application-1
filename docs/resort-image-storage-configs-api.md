# Resort Image Storage Configs API Documentation

## Overview

Base path: `/api/v1/resorts/{resort-id}/resort-image-storage-configs`

A **Resort Image Storage Config** stores which image hosting provider (AWS S3 or Cloudinary)
a resort uses, along with the credentials needed to connect to that provider. Each resort
maintains its own independent config, so different resorts can use different cloud accounts.

All requests require a JWT bearer token:

```
Authorization: Bearer <token>
```

All request and response bodies use **snake_case** JSON field names.

Records are soft-deleted — deleted configs remain in the database but are excluded from all responses.

---

## Endpoints

| Method | Path                                                    | Description                        |
|--------|---------------------------------------------------------|------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/resort-image-storage-configs`          | Create a storage config  |
| GET    | `/api/v1/resorts/{resort-id}/resort-image-storage-configs/{id}`     | Get a config by ID       |
| GET    | `/api/v1/resorts/{resort-id}/resort-image-storage-configs/active`   | Get the active config    |
| GET    | `/api/v1/resorts/{resort-id}/resort-image-storage-configs`          | List all configs         |
| PUT    | `/api/v1/resorts/{resort-id}/resort-image-storage-configs/{id}`     | Update a config          |
| DELETE | `/api/v1/resorts/{resort-id}/resort-image-storage-configs/{id}`     | Delete a config          |

---

## Providers & Required Config Keys

The `provider` field must be one of the values below. The `config` map must contain all
required keys for the chosen provider — missing or blank keys are rejected with `400`.

| Provider      | Required `config` keys                                 |
|---------------|--------------------------------------------------------|
| `S3`          | `bucket`, `region`, `access_key`, `secret_key`         |
| `CLOUDINARY`  | `cloud_name`, `api_key`, `api_secret`                  |

---

## Data Model

| Field       | Type              | Required | Description                                      |
|-------------|-------------------|----------|--------------------------------------------------|
| `provider`  | string (enum)     | Yes      | Image hosting provider: `S3` or `CLOUDINARY`     |
| `config`    | map\<string,string\> | Yes   | Provider credentials — see table above           |

---

## POST `/`

Create a new image storage config for a resort.

**Path Parameters**

| Parameter  | Type | Description      |
|------------|------|------------------|
| `resort-id` | long | ID of the resort |

**Request Body**

| Field      | Type              | Required |
|------------|-------------------|----------|
| `provider` | string (enum)     | Yes      |
| `config`   | map\<string,string\> | Yes   |

**Example Request — S3**

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

**Example Request — Cloudinary**

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

**Response** `201 Created`

```json
{
  "success": true,
  "id": 12
}
```

**Error Cases**

| Status | Scenario                                          |
|--------|---------------------------------------------------|
| `400`  | `provider` is null                                |
| `400`  | `config` is null                                  |
| `400`  | Required config key is missing or blank           |
| `404`  | `resort-id` not found                             |

---

## GET `/{id}`

Retrieve a single storage config by its ID.

**Path Parameters**

| Parameter  | Type | Description              |
|------------|------|--------------------------|
| `resort-id` | long | ID of the resort         |
| `id`        | long | ID of the storage config |

**Response** `200 OK`

```json
{
  "data": {
    "id": 12,
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

**Error Cases**

| Status | Scenario                                              |
|--------|-------------------------------------------------------|
| `404`  | Config not found, deleted, or belongs to another resort |

---

## GET `/active`

Retrieve the currently active storage config for a resort. Used internally by the image
upload flow to determine which provider and credentials to use.

**Path Parameters**

| Parameter  | Type | Description      |
|------------|------|------------------|
| `resort-id` | long | ID of the resort |

**Response** `200 OK`

```json
{
  "data": {
    "id": 12,
    "resort_id": 5,
    "provider": "CLOUDINARY",
    "config": {
      "cloud_name": "my-resort-cloud",
      "api_key":    "123456789012345",
      "api_secret": "abcDEFghiJKLmnoPQR"
    }
  }
}
```

**Error Cases**

| Status | Scenario                                             |
|--------|------------------------------------------------------|
| `404`  | No active config found for the resort                |

---

## GET `/`

List all active storage configs for a resort, paginated.

**Path Parameters**

| Parameter  | Type | Description      |
|------------|------|------------------|
| `resort-id` | long | ID of the resort |

**Query Parameters**

| Parameter  | Type    | Default | Description                                   |
|------------|---------|---------|-----------------------------------------------|
| `page`     | integer | `0`     | Zero-based page number                        |
| `size`     | integer | `10`    | Items per page (max `50`)                     |
| `sort_by`  | string  | `id`    | Field to sort by: `id`, `provider`            |
| `sort_dir` | string  | `ASC`   | Sort direction: `ASC` or `DESC`               |

**Response** `200 OK`

```json
{
  "data": [
    {
      "id": 12,
      "resort_id": 5,
      "provider": "S3",
      "config": {
        "bucket":     "my-resort-images",
        "region":     "ap-southeast-1",
        "access_key": "AKIAIOSFODNN7EXAMPLE",
        "secret_key": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
      }
    },
    {
      "id": 13,
      "resort_id": 5,
      "provider": "CLOUDINARY",
      "config": {
        "cloud_name": "my-resort-cloud",
        "api_key":    "123456789012345",
        "api_secret": "abcDEFghiJKLmnoPQR"
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

**Error Cases**

| Status | Scenario                      |
|--------|-------------------------------|
| `400`  | Invalid `sort_by` value       |

---

## PUT `/{id}`

Update an existing storage config. Partial updates are supported — only non-null fields
overwrite existing values.

> **Note:** `config` is stored and replaced as a complete map. If you only need to rotate
> one credential, you must re-supply all required keys for the provider.

**Path Parameters**

| Parameter  | Type | Description              |
|------------|------|--------------------------|
| `resort-id` | long | ID of the resort         |
| `id`        | long | ID of the storage config |

**Request Body** — all fields optional

| Field      | Type                  | Notes                                              |
|------------|-----------------------|----------------------------------------------------|
| `provider` | string (enum)         | Change the provider; must match the new `config`   |
| `config`   | map\<string,string\>  | Full replacement of the credentials map            |

**Example Request — switch provider**

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

**Example Request — rotate secret key only (keep existing provider)**

```json
{
  "provider": null,
  "config": {
    "bucket":     "my-resort-images",
    "region":     "ap-southeast-1",
    "access_key": "AKIAIOSFODNN7EXAMPLE",
    "secret_key": "NEW_ROTATED_SECRET_KEY"
  }
}
```

**Validation logic for updates:**

| `provider` | `config`     | Validation applied                                    |
|------------|--------------|-------------------------------------------------------|
| provided   | provided     | Validate `config` against new `provider`              |
| null       | provided     | Validate `config` against the existing stored provider |
| provided   | null         | No config validation (keys unchanged)                 |
| null       | null         | No-op                                                 |

**Response** `200 OK`

```json
{
  "success": true,
  "id": 12
}
```

**Error Cases**

| Status | Scenario                                                       |
|--------|----------------------------------------------------------------|
| `404`  | Config not found, deleted, or belongs to another resort        |
| `400`  | Required config key is missing or blank                        |
| `409`  | Concurrent modification conflict (optimistic locking)          |

---

## DELETE `/{id}`

Soft-delete a storage config. The record is retained in the database with
`is_deleted = true` and `is_active = false`, and is excluded from all subsequent responses.

**Path Parameters**

| Parameter  | Type | Description              |
|------------|------|--------------------------|
| `resort-id` | long | ID of the resort         |
| `id`        | long | ID of the storage config |

**Response** `200 OK`

```json
{
  "success": true,
  "id": 12
}
```

**Error Cases**

| Status | Scenario                                  |
|--------|-------------------------------------------|
| `404`  | Config not found or already deleted       |

---

## Error Responses

| Status | Scenario                                                                   |
|--------|----------------------------------------------------------------------------|
| `400`  | `provider` or `config` is null in request body                             |
| `400`  | Required config key is missing or blank for the chosen provider            |
| `400`  | Invalid `sort_by` value in pagination query                                |
| `401`  | Missing or invalid JWT token                                               |
| `404`  | Resort not found                                                           |
| `404`  | Storage config not found, deleted, or belongs to a different resort        |
| `409`  | Concurrent write conflict (optimistic lock — retry the request)            |
