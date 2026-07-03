# UI Block Sections API

Base URL: `/api/v1/ui-block-sections`

UI Block Sections define the layout areas within a page where UI blocks can be placed (e.g., Hero, Header, Gallery,
Footer). Display names and descriptions are locale-specific and are embedded in every response via the `locales` array.
All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                           | Description                      |
|--------|----------------------------------------------------------------|----------------------------------|
| POST   | `/api/v1/ui-block-sections`                                    | Create a UI block section        |
| GET    | `/api/v1/ui-block-sections`                                    | List / search UI block sections  |
| GET    | `/api/v1/ui-block-sections/{id}`                               | Get a UI block section           |
| PUT    | `/api/v1/ui-block-sections/{id}`                               | Update a UI block section        |
| DELETE | `/api/v1/ui-block-sections/{id}`                               | Delete a UI block section        |
| POST   | `/api/v1/ui-block-sections/{ui-block-section-id}/locales`      | Create a UI block section locale |
| PUT    | `/api/v1/ui-block-sections/{ui-block-section-id}/locales/{id}` | Update a UI block section locale |
| DELETE | `/api/v1/ui-block-sections/{ui-block-section-id}/locales/{id}` | Delete a UI block section locale |

---

## Data Model

### UI Block Section

| Field        | Type    | Required | Constraints           | Description                                       |
|--------------|---------|----------|-----------------------|---------------------------------------------------|
| `id`         | Long    | —        | read-only             | Auto-generated identifier                         |
| `code`       | String  | Yes      | max 100 chars, unique | Stable business code (e.g., `HRO`, `HDR`, `FTR`)  |
| `sort_order` | Integer | Yes      | not null, default `1` | Display order in administrative interfaces        |
| `locales`    | Array   | —        | read-only             | All locale translations for this UI block section |

### UI Block Section Locale

| Field         | Type    | Required | Constraints   | Description                                         |
|---------------|---------|----------|---------------|-----------------------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier                           |
| `locale_id`   | Long    | Yes      | must exist    | ID of an existing active locale                     |
| `name`        | String  | Yes      | max 255 chars | Localized display name (e.g., `"Hero"`, `"Footer"`) |
| `description` | String  | No       | unlimited     | Short explanation of the section shown in the UI    |
| `sort_order`  | Integer | Yes      | not null      | Display order for this locale entry                 |

---

## Create UI Block Section

`POST /api/v1/ui-block-sections`

Creates a UI block section along with its locale-specific translations in one request. All provided `locale_id` values
must reference existing, active locales.

### Request Body

```json
{
  "code": "HRO",
  "sort_order": 1,
  "locales": [
    {
      "locale_id": 1,
      "name": "Hero",
      "description": "Full-width hero banner displayed at the top of a page.",
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
  "id": 1
}
```

---

## Get UI Block Section

`GET /api/v1/ui-block-sections/{id}`

Returns a single UI block section with all its locale translations.

### Path Parameters

| Parameter | Type | Description                |
|-----------|------|----------------------------|
| `id`      | Long | ID of the UI block section |

### Response `200 OK`

```json
{
  "ui_block_section": {
    "id": 1,
    "code": "HRO",
    "sort_order": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Hero",
        "description": "Full-width hero banner displayed at the top of a page.",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List / Search UI Block Sections

`GET /api/v1/ui-block-sections`

Returns a paginated, filterable list of active (non-deleted) UI block sections. Each item includes all locale
translations. All filter parameters are optional; omitting them returns all sections. Filters perform a
case-insensitive partial match.

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
      "code": "HRO",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Hero",
          "description": "Full-width hero banner displayed at the top of a page.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "code": "HDR",
      "sort_order": 2,
      "locales": [
        {
          "id": 2,
          "locale_id": 1,
          "name": "Header",
          "description": "Page header area containing navigation and branding elements.",
          "sort_order": 1
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 10,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update UI Block Section

`PUT /api/v1/ui-block-sections/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the UI block section locale endpoints.

### Path Parameters

| Parameter | Type | Description                |
|-----------|------|----------------------------|
| `id`      | Long | ID of the UI block section |

### Request Body

```json
{
  "sort_order": 2
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
  "id": 1
}
```

---

## Delete UI Block Section

`DELETE /api/v1/ui-block-sections/{id}`

Soft-deletes the UI block section. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter | Type | Description                |
|-----------|------|----------------------------|
| `id`      | Long | ID of the UI block section |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## UI Block Section Locales

UI block section locale endpoints manage per-locale translations for a UI block section. The
`{ui-block-section-id}` path parameter must reference an existing, active UI block section.

---

### Create UI Block Section Locale

`POST /api/v1/ui-block-sections/{ui-block-section-id}/locales`

Adds a new locale translation to an existing UI block section.

#### Path Parameters

| Parameter             | Type | Description                |
|-----------------------|------|----------------------------|
| `ui-block-section-id` | Long | ID of the UI block section |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "Héroe",
  "description": "Banner de héroe a pantalla completa en la parte superior de la página.",
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
  "id": 11
}
```

---

### Update UI Block Section Locale

`PUT /api/v1/ui-block-sections/{ui-block-section-id}/locales/{id}`

Updates an existing locale translation for a UI block section. The `locale_id` is set at creation time and cannot be
changed.

#### Path Parameters

| Parameter             | Type | Description                       |
|-----------------------|------|-----------------------------------|
| `ui-block-section-id` | Long | ID of the UI block section        |
| `id`                  | Long | ID of the UI block section locale |

#### Request Body

```json
{
  "name": "Hero",
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
  "id": 11
}
```

---

### Delete UI Block Section Locale

`DELETE /api/v1/ui-block-sections/{ui-block-section-id}/locales/{id}`

Soft-deletes a UI block section locale. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter             | Type | Description                       |
|-----------------------|------|-----------------------------------|
| `ui-block-section-id` | Long | ID of the UI block section        |
| `id`                  | Long | ID of the UI block section locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 11
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
  "message": "UiBlockSection not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                      |
|-------------|----------------------------|----------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field                              |
| 404         | `ENTITY_NOT_FOUND`         | UI block section, locale, or section locale not found, or already deleted  |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code or duplicate locale for section) |
