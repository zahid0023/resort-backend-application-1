# Locales API

Base URL: `/api/v1/locales`

Locales represent language or regional settings used across the platform (e.g., English, French, Arabic). All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                      | Description       |
|--------|---------------------------|-------------------|
| POST   | `/api/v1/locales`         | Create a locale   |
| GET    | `/api/v1/locales`         | List all locales  |
| GET    | `/api/v1/locales/{id}`    | Get a locale      |
| PUT    | `/api/v1/locales/{id}`    | Update a locale   |
| DELETE | `/api/v1/locales/{id}`    | Delete a locale   |

---

## Data Model

| Field         | Type    | Required | Constraints              | Description                              |
|---------------|---------|----------|--------------------------|------------------------------------------|
| `id`          | Long    | —        | read-only                | Auto-generated identifier                |
| `code`        | String  | Yes      | max 50 chars, unique     | Short unique identifier (e.g., `en`, `fr`) |
| `name`        | String  | Yes      | max 255 chars            | Display name (e.g., `English`, `French`) |
| `description` | String  | No       | unlimited                | Full description of the locale           |
| `sort_order`  | Integer | Yes      | not null, default `0`    | Display order                            |

---

## Create Locale

`POST /api/v1/locales`

### Request Body

```json
{
  "code": "en",
  "name": "English",
  "description": "English language locale.",
  "sort_order": 1
}
```

### Request Fields

| Field         | Type    | Required | Validation                  |
|---------------|---------|----------|-----------------------------|
| `code`        | String  | Yes      | Not blank, max 50 chars     |
| `name`        | String  | Yes      | Not blank, max 255 chars    |
| `description` | String  | No       | —                           |
| `sort_order`  | Integer | Yes      | Not null                    |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Locale

`GET /api/v1/locales/{id}`

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the locale   |

### Response `200 OK`

```json
{
  "locale": {
    "id": 1,
    "code": "en",
    "name": "English",
    "description": "English language locale.",
    "sort_order": 1
  }
}
```

---

## List All Locales

`GET /api/v1/locales`

Returns a paginated list of active (non-deleted) locales.

### Query Parameters

| Parameter  | Type   | Default | Constraints                                    | Description              |
|------------|--------|---------|------------------------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                                           | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                                         | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `name`, `sortOrder`, `createdAt` | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                                  | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "en",
      "name": "English",
      "description": "English language locale.",
      "sort_order": 1
    },
    {
      "id": 2,
      "code": "fr",
      "name": "French",
      "description": "French language locale.",
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

## Update Locale

`PUT /api/v1/locales/{id}`

All fields are replaced — provide the full intended state of the resource.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the locale   |

### Request Body

```json
{
  "code": "en",
  "name": "English (US)",
  "description": "English language locale for United States.",
  "sort_order": 1
}
```

### Request Fields

| Field         | Type    | Required | Validation                  |
|---------------|---------|----------|-----------------------------|
| `code`        | String  | Yes      | Not blank, max 50 chars     |
| `name`        | String  | Yes      | Not blank, max 255 chars    |
| `description` | String  | No       | —                           |
| `sort_order`  | Integer | Yes      | Not null                    |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Locale

`DELETE /api/v1/locales/{id}`

Soft-deletes the locale. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the locale   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Validation Errors

When the request body fails validation, the API returns `400 Bad Request`:

```json
{
  "request_id": "abc-123",
  "status": 400,
  "error": "INVALID_ARGUMENT",
  "message": "code must not be blank"
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
  "message": "Locale not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                          |
|-------------|----------------------------|------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field  |
| 404         | `ENTITY_NOT_FOUND`         | Locale not found or already deleted            |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)     |
