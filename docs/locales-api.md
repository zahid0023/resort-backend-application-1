# Locales API

Base URL: `/api/v1/locales`

Locales represent language or regional settings used across the platform (e.g., `en`, `fr`, `bn`). They are referenced
by country and city locale translations. All records support soft-delete — deleted records are hidden from all
responses.

---

## Endpoints

| Method | Path                   | Description           |
|--------|------------------------|-----------------------|
| POST   | `/api/v1/locales`      | Create a locale       |
| GET    | `/api/v1/locales/{id}` | Get a locale          |
| GET    | `/api/v1/locales`      | List / search locales |
| PUT    | `/api/v1/locales/{id}` | Update a locale       |
| DELETE | `/api/v1/locales/{id}` | Delete a locale       |

---

## Data Model

| Field        | Type    | Required | Constraints           | Description                               |
|--------------|---------|----------|-----------------------|-------------------------------------------|
| `id`         | Long    | —        | read-only             | Auto-generated identifier                 |
| `code`       | String  | Yes      | max 50 chars          | Short identifier (e.g., `en`, `fr`, `bn`) |
| `name`       | String  | Yes      | max 255 chars         | Display name (e.g., `English`, `French`)  |
| `sort_order` | Integer | Yes      | not null, default `0` | Display order                             |

---

## Create Locale

`POST /api/v1/locales`

### Request Body

```json
{
  "code": "en",
  "name": "English",
  "sort_order": 1
}
```

### Request Fields

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars  |
| `name`       | String  | Yes      | Not blank, max 255 chars |
| `sort_order` | Integer | Yes      | Not null                 |

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

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the locale |

### Response `200 OK`

```json
{
  "locale": {
    "id": 1,
    "code": "en",
    "name": "English",
    "sort_order": 1
  }
}
```

---

## List / Search Locales

`GET /api/v1/locales`

Returns a paginated, filterable list of active (non-deleted) locales. All filter parameters are optional. Multiple
filters are combined with AND. Each filter performs a case-insensitive partial match.

### Query Parameters

| Parameter  | Type   | Default | Constraints                                    | Description                                |
|------------|--------|---------|------------------------------------------------|--------------------------------------------|
| `code`     | String | —       | —                                              | Filter by code (partial, case-insensitive) |
| `name`     | String | —       | —                                              | Filter by name (partial, case-insensitive) |
| `page`     | int    | `0`     | >= 0                                           | Zero-based page index                      |
| `size`     | int    | `10`    | 1 – 50                                         | Number of items per page                   |
| `sort_by`  | String | `id`    | `id`, `code`, `name`, `sortOrder`, `createdAt` | Field to sort by                           |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                                  | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "en",
      "name": "English",
      "sort_order": 1
    },
    {
      "id": 2,
      "code": "fr",
      "name": "French",
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

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the locale |

### Request Body

```json
{
  "code": "en",
  "name": "English (US)",
  "sort_order": 1
}
```

### Request Fields

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars  |
| `name`       | String  | Yes      | Not blank, max 255 chars |
| `sort_order` | Integer | Yes      | Not null                 |

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

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the locale |

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
  "message": "Locale not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                         |
|-------------|----------------------------|-----------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field |
| 404         | `ENTITY_NOT_FOUND`         | Locale not found or already deleted           |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)    |
