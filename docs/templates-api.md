# Templates API

Base URL: `/api/v1/templates`

Templates represent the top-level structure for resort pages. A template contains one or more pages, each with slots and slot variants. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                       | Description        |
|--------|----------------------------|--------------------|
| POST   | `/api/v1/templates`        | Create a template  |
| GET    | `/api/v1/templates`        | List all templates |
| GET    | `/api/v1/templates/{id}`   | Get a template     |
| PUT    | `/api/v1/templates/{id}`   | Update a template  |
| DELETE | `/api/v1/templates/{id}`   | Delete a template  |

---

## Data Model

| Field         | Type   | Required | Max Length | Description                                         |
|---------------|--------|----------|------------|-----------------------------------------------------|
| `key`         | String | Yes      | 100        | Unique identifier key for the template              |
| `name`        | String | Yes      | 150        | Display name                                        |
| `description` | String | No       | unlimited  | Optional description                                |
| `status`      | String | No       | —          | Template status. Default: `draft`. e.g. `draft`, `published` |

---

## Create Template

`POST /api/v1/templates`

### Request Body

```json
{
  "key": "RESORT_MAIN",
  "name": "Resort Main Template",
  "description": "Primary template for the resort homepage",
  "status": "draft"
}
```

| Field         | Type   | Required |
|---------------|--------|----------|
| `key`         | String | Yes      |
| `name`        | String | Yes      |
| `description` | String | No       |
| `status`      | String | No       |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Template

`GET /api/v1/templates/{id}`

Returns the template along with its nested pages, slots, and slot variants.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the template  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "key": "RESORT_MAIN",
    "name": "Resort Main Template",
    "description": "Primary template for the resort homepage",
    "status": "draft",
    "template_pages": [
      {
        "id": 1,
        "template_id": 1,
        "page_type_id": 2,
        "page_name": "Home",
        "page_slug": "home",
        "page_order": 1,
        "template_page_slots": []
      }
    ]
  }
}
```

---

## List All Templates

`GET /api/v1/templates`

Returns a paginated list of active (non-deleted) templates. The list response omits `description` and nested relations (summary projection).

### Query Parameters

| Parameter  | Type   | Default | Constraints              | Description              |
|------------|--------|---------|--------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                     | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                   | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `key`, `name`      | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`            | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "key": "RESORT_MAIN",
      "name": "Resort Main Template",
      "status": "draft"
    },
    {
      "id": 2,
      "key": "RESORT_EVENTS",
      "name": "Resort Events Template",
      "status": "published"
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

## Update Template

`PUT /api/v1/templates/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the template  |

### Request Body

```json
{
  "name": "Resort Main Template V2",
  "status": "published"
}
```

| Field         | Type   | Required |
|---------------|--------|----------|
| `key`         | String | No       |
| `name`        | String | No       |
| `description` | String | No       |
| `status`      | String | No       |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "key": "RESORT_MAIN",
    "name": "Resort Main Template V2",
    "description": "Primary template for the resort homepage",
    "status": "published"
  }
}
```

---

## Delete Template

`DELETE /api/v1/templates/{id}`

Soft-deletes the template. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the template  |

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
  "message": "Template with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                      |
|-------------|----------------------------|--------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data     |
| 404         | `ENTITY_NOT_FOUND`         | Template not found or already deleted      |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate key)  |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                    |
