# Room Categories API

Base URL: `/api/v1/room-categories`

Room categories classify rooms by tier or type (e.g., Standard, Deluxe, Suite). All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                              | Description             |
|--------|-----------------------------------|-------------------------|
| POST   | `/api/v1/room-categories`         | Create a room category  |
| GET    | `/api/v1/room-categories`         | List all room categories|
| GET    | `/api/v1/room-categories/{id}`    | Get a room category     |
| PUT    | `/api/v1/room-categories/{id}`    | Update a room category  |
| DELETE | `/api/v1/room-categories/{id}`    | Delete a room category  |

---

## Data Model

| Field         | Type    | Required | Max Length | Description                              |
|---------------|---------|----------|------------|------------------------------------------|
| `code`        | String  | Yes      | 50         | Short identifier code (e.g., `DELUXE`)   |
| `name`        | String  | Yes      | 100        | Display name of the category             |
| `description` | String  | Yes      | unlimited  | Full description of the category         |
| `sort_order`  | Integer | No       | —          | Display order (defaults to `0`)          |

---

## Create Room Category

`POST /api/v1/room-categories`

### Request Body

```json
{
  "code": "DELUXE",
  "name": "Deluxe Room",
  "description": "Spacious room with ocean view and premium amenities.",
  "sort_order": 2
}
```

| Field         | Type    | Required |
|---------------|---------|----------|
| `code`        | String  | Yes      |
| `name`        | String  | Yes      |
| `description` | String  | Yes      |
| `sort_order`  | Integer | No       |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Room Category

`GET /api/v1/room-categories/{id}`

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the room category  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "DELUXE",
    "name": "Deluxe Room",
    "description": "Spacious room with ocean view and premium amenities.",
    "sort_order": 2
  }
}
```

---

## List All Room Categories

`GET /api/v1/room-categories`

Returns a paginated list of active (non-deleted) room categories.

### Query Parameters

| Parameter  | Type   | Default | Constraints           | Description              |
|------------|--------|---------|-----------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                  | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `name`  | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`         | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "STANDARD",
      "name": "Standard Room",
      "description": "Comfortable room with essential amenities.",
      "sort_order": 1
    },
    {
      "id": 2,
      "code": "DELUXE",
      "name": "Deluxe Room",
      "description": "Spacious room with ocean view and premium amenities.",
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

## Update Room Category

`PUT /api/v1/room-categories/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the room category  |

### Request Body

```json
{
  "name": "Deluxe Ocean View Room",
  "sort_order": 3
}
```

| Field         | Type    | Required |
|---------------|---------|----------|
| `code`        | String  | No       |
| `name`        | String  | No       |
| `description` | String  | No       |
| `sort_order`  | Integer | No       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Room Category

`DELETE /api/v1/room-categories/{id}`

Soft-deletes the room category. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the room category  |

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
  "message": "Room Category with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                        |
|-------------|----------------------------|----------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data       |
| 404         | `ENTITY_NOT_FOUND`         | Room category not found or already deleted   |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)   |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                      |
