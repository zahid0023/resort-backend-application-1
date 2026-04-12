# UI Block Categories API

Base URL: `/api/v1/ui-block-categories`

UI Block Categories are used to classify and group UI blocks within the resort application. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                  | Description                  |
|--------|---------------------------------------|------------------------------|
| POST   | `/api/v1/ui-block-categories`         | Create a UI block category   |
| GET    | `/api/v1/ui-block-categories`         | List all UI block categories |
| GET    | `/api/v1/ui-block-categories/{id}`    | Get a UI block category      |
| PUT    | `/api/v1/ui-block-categories/{id}`    | Update a UI block category   |
| DELETE | `/api/v1/ui-block-categories/{id}`    | Delete a UI block category   |

---

## Data Model

| Field         | Type   | Required | Max Length | Description                        |
|---------------|--------|----------|------------|------------------------------------|
| `key`         | String | Yes      | 100        | Unique identifier key for the category |
| `name`        | String | Yes      | 100        | Display name                       |
| `description` | String | No       | unlimited  | Optional description               |

---

## Create UI Block Category

`POST /api/v1/ui-block-categories`

### Request Body

```json
{
  "key": "HERO",
  "name": "Hero Block",
  "description": "Full-width hero banner blocks"
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

## Get UI Block Category

`GET /api/v1/ui-block-categories/{id}`

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the UI block category   |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "key": "HERO",
    "name": "Hero Block",
    "description": "Full-width hero banner blocks"
  }
}
```

---

## List All UI Block Categories

`GET /api/v1/ui-block-categories`

Returns a paginated list of active (non-deleted) UI block categories including all fields.

### Query Parameters

| Parameter  | Type   | Default | Constraints          | Description              |
|------------|--------|---------|----------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                 | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50               | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `key`, `name`  | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`        | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "key": "HERO",
      "name": "Hero Block",
      "description": "Full-width hero banner blocks"
    },
    {
      "id": 2,
      "key": "GALLERY",
      "name": "Gallery Block",
      "description": "Image gallery blocks"
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

## Update UI Block Category

`PUT /api/v1/ui-block-categories/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the UI block category   |

### Request Body

```json
{
  "key": "HERO_V2",
  "name": "Hero Block V2",
  "description": "Updated hero banner blocks"
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
    "key": "HERO_V2",
    "name": "Hero Block V2",
    "description": "Updated hero banner blocks"
  }
}
```

---

## Delete UI Block Category

`DELETE /api/v1/ui-block-categories/{id}`

Soft-deletes the UI block category. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the UI block category   |

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
  "message": "UI Block Category with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                      |
|-------------|----------------------------|--------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data     |
| 404         | `ENTITY_NOT_FOUND`         | Category not found or already deleted      |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate key)  |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                    |
