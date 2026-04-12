# Page Types API

Base URL: `/api/v1/page-types`

Page Types represent categories/classifications for pages in the resort application. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                    | Description          |
|--------|-------------------------|----------------------|
| POST   | `/api/v1/page-types`    | Create a page type   |
| GET    | `/api/v1/page-types`    | List all page types  |
| GET    | `/api/v1/page-types/{id}` | Get a page type    |
| PUT    | `/api/v1/page-types/{id}` | Update a page type |
| DELETE | `/api/v1/page-types/{id}` | Delete a page type |

---

## Data Model

| Field         | Type   | Required | Max Length | Description                        |
|---------------|--------|----------|------------|------------------------------------|
| `key`         | String | Yes      | 100        | Unique identifier key for the type |
| `name`        | String | Yes      | 100        | Display name                       |
| `description` | String | No       | unlimited  | Optional description               |

---

## Create Page Type

`POST /api/v1/page-types`

### Request Body

```json
{
  "key": "LANDING",
  "name": "Landing Page",
  "description": "Main landing page of the resort"
}
```

| Field         | Type   | Required |
|---------------|--------|----------|
| `key`         | String | Yes      |
| `name`        | String | Yes      |
| `description` | String | No       |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Page Type

`GET /api/v1/page-types/{id}`

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the page type  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "key": "LANDING",
    "name": "Landing Page",
    "description": "Main landing page of the resort"
  }
}
```

---

## List All Page Types

`GET /api/v1/page-types`

Returns a paginated list of active (non-deleted) page types. The list response omits `description` (summary projection).

### Query Parameters

| Parameter  | Type   | Default | Constraints       | Description                          |
|------------|--------|---------|-------------------|--------------------------------------|
| `page`     | int    | `0`     | >= 0              | Zero-based page index                |
| `size`     | int    | `10`    | 1 – 50            | Number of items per page             |
| `sort_by`  | String | `id`    | `id`, `key`, `name` | Field to sort by                  |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`     | Sort direction                       |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "key": "LANDING",
      "name": "Landing Page"
    },
    {
      "id": 2,
      "key": "ROOMS",
      "name": "Rooms Page"
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

## Update Page Type

`PUT /api/v1/page-types/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the page type  |

### Request Body

```json
{
  "key": "LANDING_V2",
  "name": "Landing Page V2",
  "description": "Updated description"
}
```

| Field         | Type   | Required |
|---------------|--------|----------|
| `key`         | String | No       |
| `name`        | String | No       |
| `description` | String | No       |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "key": "LANDING_V2",
    "name": "Landing Page V2",
    "description": "Updated description"
  }
}
```

---

## Delete Page Type

`DELETE /api/v1/page-types/{id}`

Soft-deletes the page type. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the page type  |

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
  "message": "Page Type with id: 99 was not found."
}
```

| HTTP Status | Error Code               | Cause                                     |
|-------------|--------------------------|-------------------------------------------|
| 400         | `INVALID_ARGUMENT`       | Invalid sort field or bad request data    |
| 404         | `ENTITY_NOT_FOUND`       | Page type not found or already deleted    |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate key) |
| 500         | `INTERNAL_SERVER_ERROR`  | Unexpected server error                   |
