# Countries API

Base URL: `/api/v1/countries`

Countries represent the list of countries available in the resort application. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                        | Description        |
|--------|-----------------------------|--------------------|
| POST   | `/api/v1/countries`         | Create a country   |
| GET    | `/api/v1/countries`         | List all countries |
| GET    | `/api/v1/countries/{id}`    | Get a country      |
| PUT    | `/api/v1/countries/{id}`    | Update a country   |
| DELETE | `/api/v1/countries/{id}`    | Delete a country   |

---

## Data Model

| Field  | Type   | Required | Max Length | Description               |
|--------|--------|----------|------------|---------------------------|
| `code` | String | No       | 10         | Short country code        |
| `name` | String | No       | 100        | Display name of the country |

---

## Create Country

`POST /api/v1/countries`

### Request Body

```json
{
  "code": "PH",
  "name": "Philippines"
}
```

| Field  | Type   | Required |
|--------|--------|----------|
| `code` | String | No       |
| `name` | String | No       |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Country

`GET /api/v1/countries/{id}`

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the country  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "PH",
    "name": "Philippines"
  }
}
```

---

## List All Countries

`GET /api/v1/countries`

Returns a paginated list of active (non-deleted) countries.

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
      "code": "PH",
      "name": "Philippines"
    },
    {
      "id": 2,
      "code": "US",
      "name": "United States"
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

## Update Country

`PUT /api/v1/countries/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the country  |

### Request Body

```json
{
  "code": "PHL",
  "name": "Republic of the Philippines"
}
```

| Field  | Type   | Required |
|--------|--------|----------|
| `code` | String | No       |
| `name` | String | No       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Country

`DELETE /api/v1/countries/{id}`

Soft-deletes the country. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the country  |

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
  "message": "Country with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                       |
|-------------|----------------------------|---------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data      |
| 404         | `ENTITY_NOT_FOUND`         | Country not found or already deleted        |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)  |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                     |
