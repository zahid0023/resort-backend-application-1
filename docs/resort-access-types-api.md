# Resort Access Types API

Base URL: `/api/v1/resort-access-types`

Resort Access Types define the different access classifications available at the resort (e.g. day pass, overnight, member). All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                  | Description                  |
|--------|---------------------------------------|------------------------------|
| POST   | `/api/v1/resort-access-types`         | Create a resort access type  |
| GET    | `/api/v1/resort-access-types`         | List all resort access types |
| GET    | `/api/v1/resort-access-types/{id}`    | Get a resort access type     |
| PUT    | `/api/v1/resort-access-types/{id}`    | Update a resort access type  |
| DELETE | `/api/v1/resort-access-types/{id}`    | Delete a resort access type  |

---

## Data Model

| Field         | Type   | Required | Max Length | Description                          |
|---------------|--------|----------|------------|--------------------------------------|
| `code`        | String | Yes      | 50         | Short identifier code for the type   |
| `name`        | String | Yes      | 100        | Display name                         |
| `description` | String | Yes      | unlimited  | Description of the access type       |

---

## Create Resort Access Type

`POST /api/v1/resort-access-types`

### Request Body

```json
{
  "code": "DAY_PASS",
  "name": "Day Pass",
  "description": "Single-day access to resort facilities"
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

## Get Resort Access Type

`GET /api/v1/resort-access-types/{id}`

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the resort access type  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "DAY_PASS",
    "name": "Day Pass",
    "description": "Single-day access to resort facilities"
  }
}
```

---

## List All Resort Access Types

`GET /api/v1/resort-access-types`

Returns a paginated list of active (non-deleted) resort access types.

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
      "code": "DAY_PASS",
      "name": "Day Pass",
      "description": "Single-day access to resort facilities"
    },
    {
      "id": 2,
      "code": "OVERNIGHT",
      "name": "Overnight Stay",
      "description": "Access including overnight accommodation"
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

## Update Resort Access Type

`PUT /api/v1/resort-access-types/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the resort access type  |

### Request Body

```json
{
  "code": "DAY_PASS_V2",
  "name": "Day Pass V2",
  "description": "Updated description for day pass"
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
  "data": {
    "id": 1,
    "code": "DAY_PASS_V2",
    "name": "Day Pass V2",
    "description": "Updated description for day pass"
  }
}
```

---

## Delete Resort Access Type

`DELETE /api/v1/resort-access-types/{id}`

Soft-deletes the resort access type. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the resort access type  |

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
  "message": "Resort Access Type with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                      |
|-------------|----------------------------|--------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data     |
| 404         | `ENTITY_NOT_FOUND`         | Resort access type not found or deleted    |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code) |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                    |
