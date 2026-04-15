# Resorts API

Base URL: `/api/v1/resorts`

Resorts are the core entities of the application. Each resort belongs to a country and optionally a city. All records
support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                   | Description      |
|--------|------------------------|------------------|
| POST   | `/api/v1/resorts`      | Create a resort  |
| GET    | `/api/v1/resorts`      | List all resorts |
| GET    | `/api/v1/resorts/{id}` | Get a resort     |
| PUT    | `/api/v1/resorts/{id}` | Update a resort  |
| DELETE | `/api/v1/resorts/{id}` | Delete a resort  |

---

## Data Modely

| Field           | Type   | Required | Max Length | Description                    |
|-----------------|--------|----------|------------|--------------------------------|
| `name`          | String | Yes      | 255        | Display name of the resort     |
| `description`   | String | Yes      | unlimited  | Full description of the resort |
| `address`       | String | No       | unlimited  | Physical address               |
| `country_id`    | Long   | Yes      | —          | ID of the associated country   |
| `city_id`       | Long   | Yes      | —          | ID of the associated city      |
| `contact_email` | String | No       | 255        | Contact email address          |
| `contact_phone` | String | No       | 50         | Contact phone number           |

---

## Create Resort

`POST /api/v1/resorts`

### Request Body

```json
{
  "name": "Sunset Beach Resort",
  "description": "A beautiful beachfront resort with stunning ocean views.",
  "address": "123 Ocean Drive, Boracay",
  "country_id": 1,
  "city_id": 5,
  "contact_email": "info@sunsetbeach.com",
  "contact_phone": "+63-912-345-6789"
}
```

| Field           | Type   | Required |
|-----------------|--------|----------|
| `name`          | String | Yes      |
| `description`   | String | Yes      |
| `address`       | String | No       |
| `country_id`    | Long   | Yes      |
| `city_id`       | Long   | No       |
| `contact_email` | String | No       |
| `contact_phone` | String | No       |

### Response `201 Created`

```json
{
  "id": 1,
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Sunset Beach Resort",
  "description": "A beautiful beachfront resort with stunning ocean views.",
  "address": "123 Ocean Drive, Boracay",
  "country_id": 1,
  "city_id": 5,
  "contact_email": "info@sunsetbeach.com",
  "contact_phone": "+63-912-345-6789"
}
```

---

## Get Resort

`GET /api/v1/resorts/{id}`

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the resort |

### Response `200 OK`

```json
{
  "id": 1,
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Sunset Beach Resort",
  "description": "A beautiful beachfront resort with stunning ocean views.",
  "address": "123 Ocean Drive, Boracay",
  "country_id": 1,
  "city_id": 5,
  "contact_email": "info@sunsetbeach.com",
  "contact_phone": "+63-912-345-6789"
}
```

---

## List All Resorts

`GET /api/v1/resorts`

Returns a paginated list of active (non-deleted) resorts.

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
      "uuid": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Sunset Beach Resort",
      "description": "A beautiful beachfront resort with stunning ocean views.",
      "address": "123 Ocean Drive, Boracay",
      "country_id": 1,
      "city_id": 5,
      "contact_email": "info@sunsetbeach.com",
      "contact_phone": "+63-912-345-6789"
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Resort

`PUT /api/v1/resorts/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the resort |

### Request Body

```json
{
  "name": "Sunset Beach Resort & Spa",
  "description": "A luxurious beachfront resort and spa.",
  "contact_email": "contact@sunsetbeachspa.com"
}
```

| Field           | Type   | Required |
|-----------------|--------|----------|
| `name`          | String | No       |
| `description`   | String | No       |
| `address`       | String | No       |
| `country_id`    | Long   | No       |
| `city_id`       | Long   | No       |
| `contact_email` | String | No       |
| `contact_phone` | String | No       |

### Response `200 OK`

```json
{
  "id": 1,
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Sunset Beach Resort & Spa",
  "description": "A luxurious beachfront resort and spa.",
  "address": "123 Ocean Drive, Boracay",
  "country_id": 1,
  "city_id": 5,
  "contact_email": "contact@sunsetbeachspa.com",
  "contact_phone": "+63-912-345-6789"
}
```

---

## Delete Resort

`DELETE /api/v1/resorts/{id}`

Soft-deletes the resort. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the resort |

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
  "message": "Resort with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                                  |
|-------------|----------------------------|--------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data                 |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found, or referenced country/city not found |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                                   |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                |
