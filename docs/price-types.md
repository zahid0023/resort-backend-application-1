# Price Types API

Base URL: `/api/v1/price-types`

Price types define categories used to classify room pricing periods (e.g., High Season, Low Season, Weekend). All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                            | Description           |
|--------|---------------------------------|-----------------------|
| POST   | `/api/v1/price-types`           | Create a price type   |
| GET    | `/api/v1/price-types`           | List all price types  |
| GET    | `/api/v1/price-types/{id}`      | Get a price type      |
| PUT    | `/api/v1/price-types/{id}`      | Update a price type   |
| DELETE | `/api/v1/price-types/{id}`      | Delete a price type   |

---

## Data Model

| Field         | Type   | Required | Max Length | Description                                     |
|---------------|--------|----------|------------|-------------------------------------------------|
| `code`        | String | Yes      | 30         | Short identifier code (e.g., `HIGH_SEASON`)     |
| `name`        | String | Yes      | 100        | Display name of the price type                  |
| `description` | String | Yes      | unlimited  | Full description of the price type              |

---

## Create Price Type

`POST /api/v1/price-types`

### Request Body

```json
{
  "code": "HIGH_SEASON",
  "name": "High Season",
  "description": "Peak season pricing applied from June to August and December to January."
}
```

| Field         | Type   | Required |
|---------------|--------|----------|
| `code`        | String | Yes      |
| `name`        | String | Yes      |
| `description` | String | Yes      |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Price Type

`GET /api/v1/price-types/{id}`

### Path Parameters

| Parameter | Type | Description            |
|-----------|------|------------------------|
| `id`      | Long | ID of the price type   |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "HIGH_SEASON",
    "name": "High Season",
    "description": "Peak season pricing applied from June to August and December to January."
  }
}
```

---

## List All Price Types

`GET /api/v1/price-types`

Returns a paginated list of active (non-deleted) price types.

### Query Parameters

| Parameter  | Type   | Default | Constraints          | Description              |
|------------|--------|---------|----------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                 | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50               | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `name` | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`        | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "HIGH_SEASON",
      "name": "High Season",
      "description": "Peak season pricing applied from June to August and December to January."
    },
    {
      "id": 2,
      "code": "LOW_SEASON",
      "name": "Low Season",
      "description": "Off-peak pricing applied from September to November and February to May."
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

## Update Price Type

`PUT /api/v1/price-types/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description            |
|-----------|------|------------------------|
| `id`      | Long | ID of the price type   |

### Request Body

```json
{
  "name": "Peak Season",
  "description": "Updated peak season pricing."
}
```

| Field         | Type   | Required |
|---------------|--------|----------|
| `code`        | String | No       |
| `name`        | String | No       |
| `description` | String | No       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Price Type

`DELETE /api/v1/price-types/{id}`

Soft-deletes the price type. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description            |
|-----------|------|------------------------|
| `id`      | Long | ID of the price type   |

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
  "message": "Price Type with id: 99 was not found."
}
```

| HTTP Status | Error Code         | Cause                                                           |
|-------------|--------------------|-----------------------------------------------------------------|
| `400`       | `BAD_REQUEST`      | Missing required fields or invalid values                       |
| `401`       | `UNAUTHORIZED`     | Missing or invalid authentication token                         |
| `404`       | `ENTITY_NOT_FOUND` | Price type with the given ID does not exist or has been deleted |
