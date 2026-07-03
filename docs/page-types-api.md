# Page Types API

Base URL: `/api/v1/page-types`

Page types define the structural classification of pages within a resort website template (e.g., Home, Rooms, Gallery).
Display names and descriptions are locale-specific and are embedded in every response via the `locales` array.
All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                             | Description               |
|--------|--------------------------------------------------|---------------------------|
| POST   | `/api/v1/page-types`                             | Create a page type        |
| GET    | `/api/v1/page-types`                             | List / search page types  |
| GET    | `/api/v1/page-types/{id}`                        | Get a page type           |
| PUT    | `/api/v1/page-types/{id}`                        | Update a page type        |
| DELETE | `/api/v1/page-types/{id}`                        | Delete a page type        |
| POST   | `/api/v1/page-types/{page-type-id}/locales`      | Create a page type locale |
| PUT    | `/api/v1/page-types/{page-type-id}/locales/{id}` | Update a page type locale |
| DELETE | `/api/v1/page-types/{page-type-id}/locales/{id}` | Delete a page type locale |

---

## Data Model

### Page Type

| Field        | Type    | Required | Constraints           | Description                                      |
|--------------|---------|----------|-----------------------|--------------------------------------------------|
| `id`         | Long    | —        | read-only             | Auto-generated identifier                        |
| `code`       | String  | Yes      | max 100 chars, unique | Stable business code (e.g., `HOM`, `RMS`, `GAL`) |
| `sort_order` | Integer | Yes      | not null, default `1` | Display order in administrative interfaces       |
| `locales`    | Array   | —        | read-only             | All locale translations for this page type       |

### Page Type Locale

| Field         | Type    | Required | Constraints   | Description                                        |
|---------------|---------|----------|---------------|----------------------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier                          |
| `locale_id`   | Long    | Yes      | must exist    | ID of an existing active locale                    |
| `name`        | String  | Yes      | max 255 chars | Localized display name (e.g., `"Home Page"`)       |
| `description` | String  | No       | unlimited     | Short explanation of the page type shown in the UI |
| `sort_order`  | Integer | Yes      | not null      | Display order for this locale entry                |

---

## Create Page Type

`POST /api/v1/page-types`

Creates a page type along with its locale-specific translations in one request. All provided `locale_id` values must
reference existing, active locales.

### Request Body

```json
{
  "code": "RMS",
  "sort_order": 2,
  "locales": [
    {
      "locale_id": 1,
      "name": "Rooms Page",
      "description": "Showcases available room types, categories, and pricing.",
      "sort_order": 1
    }
  ]
}
```

### Request Fields

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `code`       | String  | Yes      | Not blank, max 100 chars |
| `sort_order` | Integer | Yes      | Not null                 |
| `locales`    | Array   | No       | See locale fields below  |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 2
}
```

---

## Get Page Type

`GET /api/v1/page-types/{id}`

Returns a single page type with all its locale translations.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the page type |

### Response `200 OK`

```json
{
  "page_type": {
    "id": 2,
    "code": "RMS",
    "sort_order": 2,
    "locales": [
      {
        "id": 2,
        "locale_id": 1,
        "name": "Rooms Page",
        "description": "Showcases available room types, categories, and pricing.",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List / Search Page Types

`GET /api/v1/page-types`

Returns a paginated, filterable list of active (non-deleted) page types. Each item includes all locale translations.
All filter parameters are optional; omitting them returns all page types. Filters perform a case-insensitive partial
match.

### Query Parameters

| Parameter  | Type   | Default | Constraints                            | Description                                |
|------------|--------|---------|----------------------------------------|--------------------------------------------|
| `code`     | String | —       | —                                      | Filter by code (partial, case-insensitive) |
| `page`     | int    | `0`     | >= 0                                   | Zero-based page index                      |
| `size`     | int    | `10`    | 1 – 50                                 | Number of items per page                   |
| `sort_by`  | String | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by                           |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                          | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "HOM",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Home Page",
          "description": "Main home page of the resort website.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "code": "RMS",
      "sort_order": 2,
      "locales": [
        {
          "id": 2,
          "locale_id": 1,
          "name": "Rooms Page",
          "description": "Showcases available room types, categories, and pricing.",
          "sort_order": 1
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 9,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Page Type

`PUT /api/v1/page-types/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the page type locale endpoints.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the page type |

### Request Body

```json
{
  "sort_order": 3
}
```

### Request Fields

| Field        | Type    | Required | Validation |
|--------------|---------|----------|------------|
| `sort_order` | Integer | Yes      | Not null   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

## Delete Page Type

`DELETE /api/v1/page-types/{id}`

Soft-deletes the page type. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the page type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

## Page Type Locales

Page type locale endpoints manage per-locale translations for a page type. The `{page-type-id}` path parameter must
reference an existing, active page type.

---

### Create Page Type Locale

`POST /api/v1/page-types/{page-type-id}/locales`

Adds a new locale translation to an existing page type.

#### Path Parameters

| Parameter      | Type | Description         |
|----------------|------|---------------------|
| `page-type-id` | Long | ID of the page type |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "Página de Habitaciones",
  "description": "Muestra los tipos de habitaciones, categorías y precios disponibles.",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 5
}
```

---

### Update Page Type Locale

`PUT /api/v1/page-types/{page-type-id}/locales/{id}`

Updates an existing locale translation for a page type. The `locale_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter      | Type | Description                |
|----------------|------|----------------------------|
| `page-type-id` | Long | ID of the page type        |
| `id`           | Long | ID of the page type locale |

#### Request Body

```json
{
  "name": "Rooms Page",
  "description": "Updated description.",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 5
}
```

---

### Delete Page Type Locale

`DELETE /api/v1/page-types/{page-type-id}/locales/{id}`

Soft-deletes a page type locale. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter      | Type | Description                |
|----------------|------|----------------------------|
| `page-type-id` | Long | ID of the page type        |
| `id`           | Long | ID of the page type locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 5
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
  "message": "PageType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                        |
|-------------|----------------------------|------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field                                |
| 404         | `ENTITY_NOT_FOUND`         | Page type, locale, or page type locale not found, or already deleted         |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code or duplicate locale for page type) |
