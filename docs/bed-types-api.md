# Bed Types API

Base URL: `/api/v1/bed-types`

Bed types represent the kinds of beds available in resort room categories (e.g., King Bed, Bunk Bed, Sofa Bed).
Display names and descriptions are locale-specific and are embedded in every response via the `locales` array.
All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                           | Description              |
|--------|------------------------------------------------|--------------------------|
| POST   | `/api/v1/bed-types`                            | Create a bed type        |
| GET    | `/api/v1/bed-types`                            | List / search bed types  |
| GET    | `/api/v1/bed-types/{id}`                       | Get a bed type           |
| PUT    | `/api/v1/bed-types/{id}`                       | Update a bed type        |
| DELETE | `/api/v1/bed-types/{id}`                       | Delete a bed type        |
| POST   | `/api/v1/bed-types/{bed-type-id}/locales`      | Create a bed type locale |
| PUT    | `/api/v1/bed-types/{bed-type-id}/locales/{id}` | Update a bed type locale |
| DELETE | `/api/v1/bed-types/{bed-type-id}/locales/{id}` | Delete a bed type locale |

---

## Seeded Codes

The following bed type codes are pre-seeded by the system and cannot be duplicated:

| Code     | Name       | Description                                                                 |
|----------|------------|-----------------------------------------------------------------------------|
| `SINGLE` | Single Bed | A bed designed for one person, typically 90 × 190 cm.                       |
| `TWIN`   | Twin Bed   | Two separate single beds in the same room, each typically 90 × 190 cm.      |
| `DOUBLE` | Double Bed | A bed wide enough for two people, typically 135 × 190 cm.                   |
| `QUEEN`  | Queen Bed  | A large bed suitable for two people, typically 150 × 200 cm.                |
| `KING`   | King Bed   | The largest standard bed size, typically 180 × 200 cm.                      |
| `SOFA`   | Sofa Bed   | A sofa that folds out into a sleeping surface, ideal for flexible use.      |
| `BUNK`   | Bunk Bed   | Two beds stacked vertically, commonly used in dormitories or family rooms.  |
| `FUTON`  | Futon      | A low Japanese-style bed or mattress that can be rolled up when not in use. |
| `MURPHY` | Murphy Bed | A wall bed that folds into the wall to save floor space when not in use.    |

---

## Data Model

### Bed Type

| Field        | Type    | Required | Constraints           | Description                                                          |
|--------------|---------|----------|-----------------------|----------------------------------------------------------------------|
| `id`         | Long    | —        | read-only             | Auto-generated identifier                                            |
| `code`       | String  | Yes      | max 50 chars, unique  | Stable business code (e.g., `KING`, `BUNK`). Immutable after create. |
| `sort_order` | Integer | Yes      | not null, default `0` | Display order in administrative interfaces                           |
| `locales`    | Array   | —        | read-only here        | All locale translations for this bed type                            |

### Bed Type Locale

| Field         | Type    | Required | Constraints           | Description                                                 |
|---------------|---------|----------|-----------------------|-------------------------------------------------------------|
| `id`          | Long    | —        | read-only             | Auto-generated identifier                                   |
| `locale`      | Object  | —        | read-only in response | Embedded locale object (`id`, `code`, `name`, `sort_order`) |
| `locale_id`   | Long    | Yes      | not null, must exist  | ID of an existing active locale (request only)              |
| `name`        | String  | Yes      | max 100 chars         | Localized display name (e.g., `"King Bed"`)                 |
| `description` | String  | No       | unlimited             | Short explanation shown in the UI                           |
| `sort_order`  | Integer | Yes      | not null              | Display order for this locale entry                         |

---

## Create Bed Type

`POST /api/v1/bed-types`

Creates a bed type along with its locale-specific translations in one request. All provided `locale_id` values must
reference existing, active locales. The `code` is set at creation time and cannot be changed.

### Request Fields

| Field        | Type    | Required | Validation              |
|--------------|---------|----------|-------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars |
| `sort_order` | Integer | Yes      | Not null                |
| `locales`    | Array   | No       | See locale fields below |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Request Body

```json
{
  "code": "DAYBED",
  "sort_order": 10,
  "locales": [
    {
      "locale_id": 1,
      "name": "Day Bed",
      "description": "A narrow bed suitable for daytime resting, often used as a sofa.",
      "sort_order": 1
    }
  ]
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 10
}
```

---

## Get Bed Type

`GET /api/v1/bed-types/{id}`

Returns a single bed type with all its locale translations. Each locale entry embeds the full locale object.
The optional `description` field is omitted from the response when not set.

### Path Parameters

| Parameter | Type | Required | Description        |
|-----------|------|----------|--------------------|
| `id`      | Long | Yes      | ID of the bed type |

### Response `200 OK`

```json
{
  "bed_type": {
    "id": 5,
    "code": "KING",
    "sort_order": 5,
    "locales": [
      {
        "id": 5,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "King Bed",
        "description": "The largest standard bed size, typically 180 × 200 cm.",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List / Search Bed Types

`GET /api/v1/bed-types`

Returns a paginated, searchable list of active (non-deleted) bed types. Each item includes all locale translations.
All filter parameters are optional; omitting them returns all bed types. The `code` filter performs a
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

The optional `description` field is omitted per item when not set.

```json
{
  "data": [
    {
      "id": 1,
      "code": "SINGLE",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Single Bed",
          "description": "A bed designed for one person, typically 90 × 190 cm.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "code": "TWIN",
      "sort_order": 2,
      "locales": [
        {
          "id": 2,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Twin Bed",
          "description": "Two separate single beds in the same room, each typically 90 × 190 cm.",
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

## Update Bed Type

`PUT /api/v1/bed-types/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the bed type locale endpoints.

### Path Parameters

| Parameter | Type | Required | Description        |
|-----------|------|----------|--------------------|
| `id`      | Long | Yes      | ID of the bed type |

### Request Fields

| Field        | Type    | Required | Validation |
|--------------|---------|----------|------------|
| `sort_order` | Integer | Yes      | Not null   |

### Request Body

```json
{
  "sort_order": 2
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Bed Type

`DELETE /api/v1/bed-types/{id}`

Soft-deletes the bed type. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Required | Description        |
|-----------|------|----------|--------------------|
| `id`      | Long | Yes      | ID of the bed type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Bed Type Locales

Bed type locale endpoints manage per-locale translations for a bed type. The `{bed-type-id}` path parameter must
reference an existing, active bed type.

---

### Create Bed Type Locale

`POST /api/v1/bed-types/{bed-type-id}/locales`

Adds a new locale translation to an existing bed type. Each `locale_id` may only be used once per bed type.

#### Path Parameters

| Parameter     | Type | Required | Description        |
|---------------|------|----------|--------------------|
| `bed-type-id` | Long | Yes      | ID of the bed type |

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "Kingbett",
  "description": "Das größte Standardbettformat, typischerweise 180 × 200 cm.",
  "sort_order": 1
}
```

#### Response `201 Created`

```json
{
  "success": true,
  "id": 10
}
```

---

### Update Bed Type Locale

`PUT /api/v1/bed-types/{bed-type-id}/locales/{id}`

Updates an existing locale translation for a bed type. `locale_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter     | Type | Required | Description               |
|---------------|------|----------|---------------------------|
| `bed-type-id` | Long | Yes      | ID of the bed type        |
| `id`          | Long | Yes      | ID of the bed type locale |

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Request Body

```json
{
  "name": "King Bed",
  "description": "The largest standard bed size, typically 180 × 200 cm.",
  "sort_order": 1
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 5
}
```

---

### Delete Bed Type Locale

`DELETE /api/v1/bed-types/{bed-type-id}/locales/{id}`

Soft-deletes a bed type locale. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter     | Type | Required | Description               |
|---------------|------|----------|---------------------------|
| `bed-type-id` | Long | Yes      | ID of the bed type        |
| `id`          | Long | Yes      | ID of the bed type locale |

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
  "message": "BedType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                   |
|-------------|----------------------------|-----------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations (e.g. `name` blank, `sort_order` null) |
| 400         | `INVALID_ARGUMENT`         | Invalid `sort_by` field                                                                 |
| 404         | `ENTITY_NOT_FOUND`         | Bed type, locale, or bed type locale not found, or already deleted                      |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `code` or duplicate `locale_id` for the same bed type                         |
