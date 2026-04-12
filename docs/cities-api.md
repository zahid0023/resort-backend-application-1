# Cities API

Base URL: `/api/v1/cities`

Cities represent geographic city records linked to a country. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                     | Description     |
|--------|--------------------------|-----------------|
| POST   | `/api/v1/cities`         | Create a city   |
| GET    | `/api/v1/cities`         | List all cities |
| GET    | `/api/v1/cities/{id}`    | Get a city      |
| PUT    | `/api/v1/cities/{id}`    | Update a city   |
| DELETE | `/api/v1/cities/{id}`    | Delete a city   |

---

## Data Model

| Field        | Type   | Required | Max Length | Description                      |
|--------------|--------|----------|------------|----------------------------------|
| `name`       | String | Yes      | 150        | Name of the city                 |
| `country_id` | Long   | Yes      | —          | ID of the country this city belongs to |

---

## Create City

`POST /api/v1/cities`

### Request Body

```json
{
  "name": "Manila",
  "country_id": 1
}
```

| Field        | Type   | Required |
|--------------|--------|----------|
| `name`       | String | Yes      |
| `country_id` | Long   | Yes      |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get City

`GET /api/v1/cities/{id}`

### Path Parameters

| Parameter | Type | Description     |
|-----------|------|-----------------|
| `id`      | Long | ID of the city  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "name": "Manila",
    "country_id": 1
  }
}
```

---

## List All Cities

`GET /api/v1/cities`

Returns a paginated list of active (non-deleted) cities.

### Query Parameters

| Parameter  | Type   | Default | Constraints   | Description              |
|------------|--------|---------|---------------|--------------------------|
| `page`     | int    | `0`     | >= 0          | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50        | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `name`  | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC` | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "name": "Manila",
      "country_id": 1
    },
    {
      "id": 2,
      "name": "Cebu City",
      "country_id": 1
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

## Update City

`PUT /api/v1/cities/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description     |
|-----------|------|-----------------|
| `id`      | Long | ID of the city  |

### Request Body

```json
{
  "name": "Metro Manila",
  "country_id": 1
}
```

| Field        | Type   | Required |
|--------------|--------|----------|
| `name`       | String | No       |
| `country_id` | Long   | No       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete City

`DELETE /api/v1/cities/{id}`

Soft-deletes the city. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description     |
|-----------|------|-----------------|
| `id`      | Long | ID of the city  |

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
  "message": "City with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                           |
|-------------|----------------------------|-------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data          |
| 404         | `ENTITY_NOT_FOUND`         | City or referenced country not found or deleted |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                            |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                         |
