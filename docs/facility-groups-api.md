# Facility Groups API

Base URL: `/api/v1/facility-groups`

Facility groups categorize amenities and facilities offered by a resort (e.g., Dining, Recreation, Wellness). All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                               | Description               |
|--------|------------------------------------|---------------------------|
| POST   | `/api/v1/facility-groups`          | Create a facility group   |
| GET    | `/api/v1/facility-groups`          | List all facility groups  |
| GET    | `/api/v1/facility-groups/{id}`     | Get a facility group      |
| PUT    | `/api/v1/facility-groups/{id}`     | Update a facility group   |
| DELETE | `/api/v1/facility-groups/{id}`     | Delete a facility group   |

---

## Data Model

| Field         | Type    | Required | Max Length | Description                                   |
|---------------|---------|----------|------------|-----------------------------------------------|
| `code`        | String  | Yes      | 50         | Short identifier code (e.g., `DINING`)        |
| `name`        | String  | Yes      | 255        | Display name of the group                     |
| `description` | String  | No       | unlimited  | Full description of the facility group        |
| `sort_order`  | Integer | No       | —          | Display order (defaults to `0`)               |

---

## Create Facility Group

`POST /api/v1/facility-groups`

### Request Body

```json
{
  "code": "DINING",
  "name": "Dining",
  "description": "All food and beverage outlets including restaurants, bars, and room service.",
  "sort_order": 1
}
```

| Field         | Type    | Required |
|---------------|---------|----------|
| `code`        | String  | Yes      |
| `name`        | String  | Yes      |
| `description` | String  | No       |
| `sort_order`  | Integer | No       |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Facility Group

`GET /api/v1/facility-groups/{id}`

### Path Parameters

| Parameter | Type | Description               |
|-----------|------|---------------------------|
| `id`      | Long | ID of the facility group  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "DINING",
    "name": "Dining",
    "description": "All food and beverage outlets including restaurants, bars, and room service.",
    "sort_order": 1
  }
}
```

---

## List All Facility Groups

`GET /api/v1/facility-groups`

Returns a paginated list of active (non-deleted) facility groups.

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
      "code": "DINING",
      "name": "Dining",
      "description": "All food and beverage outlets including restaurants, bars, and room service.",
      "sort_order": 1
    },
    {
      "id": 2,
      "code": "WELLNESS",
      "name": "Wellness",
      "description": "Spa, fitness center, and wellness treatment facilities.",
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

## Update Facility Group

`PUT /api/v1/facility-groups/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description               |
|-----------|------|---------------------------|
| `id`      | Long | ID of the facility group  |

### Request Body

```json
{
  "name": "Food & Beverage",
  "sort_order": 2
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

## Delete Facility Group

`DELETE /api/v1/facility-groups/{id}`

Soft-deletes the facility group. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description               |
|-----------|------|---------------------------|
| `id`      | Long | ID of the facility group  |

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
  "message": "Facility Group with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                           |
|-------------|----------------------------|-------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data          |
| 404         | `ENTITY_NOT_FOUND`         | Facility group not found or already deleted     |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)      |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                         |
