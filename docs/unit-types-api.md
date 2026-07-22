# Unit Types API

Base URL: `/api/v1/unit-types`

Unit types classify the kind of measurement a unit belongs to (e.g., Weight, Volume, Length). They serve as a
top-level grouping for units of measure used across the platform. Display names and descriptions are locale-specific
and are embedded in every response via the `locales` array. All records support soft-delete — deleted records are
hidden from all responses.

---

## Endpoints

| Method | Path                                             | Description               |
|--------|--------------------------------------------------|---------------------------|
| POST   | `/api/v1/unit-types`                             | Create a unit type        |
| GET    | `/api/v1/unit-types`                             | List / search unit types  |
| GET    | `/api/v1/unit-types/{id}`                        | Get a unit type           |
| PUT    | `/api/v1/unit-types/{id}`                        | Update a unit type        |
| DELETE | `/api/v1/unit-types/{id}`                        | Delete a unit type        |
| POST   | `/api/v1/unit-types/{unit-type-id}/locales`      | Create a unit type locale |
| PUT    | `/api/v1/unit-types/{unit-type-id}/locales/{id}` | Update a unit type locale |
| DELETE | `/api/v1/unit-types/{unit-type-id}/locales/{id}` | Delete a unit type locale |

---

## Seeded Codes

The following unit type codes are pre-seeded by the system and cannot be duplicated:

| Code          | Description                              |
|---------------|------------------------------------------|
| `WEIGHT`      | Units for measuring mass or weight       |
| `VOLUME`      | Units for measuring liquid or gas volume |
| `LENGTH`      | Units for measuring distance or length   |
| `AREA`        | Units for measuring surface area         |
| `COUNT`       | Units for counting discrete items        |
| `TIME`        | Units for measuring duration             |
| `TEMPERATURE` | Units for measuring temperature          |
| `ENERGY`      | Units for measuring energy or power      |
| `PRESSURE`    | Units for measuring pressure             |
| `OTHER`       | Other miscellaneous unit types           |

---

## Data Model

### Unit Type

| Field        | Type    | Required | Constraints           | Description                                                              |
|--------------|---------|----------|-----------------------|--------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only             | Auto-generated identifier                                                |
| `code`       | String  | Yes      | max 50 chars, unique  | Stable business code (e.g., `WEIGHT`, `VOLUME`). Immutable after create. |
| `sort_order` | Integer | Yes      | not null, default `0` | Display order in administrative interfaces                               |
| `locales`    | Array   | —        | read-only here        | All locale translations for this unit type                               |

### Unit Type Locale

| Field         | Type    | Required | Constraints           | Description                                                 |
|---------------|---------|----------|-----------------------|-------------------------------------------------------------|
| `id`          | Long    | —        | read-only             | Auto-generated identifier                                   |
| `locale`      | Object  | —        | read-only in response | Embedded locale object (`id`, `code`, `name`, `sort_order`) |
| `locale_id`   | Long    | Yes      | not null, must exist  | ID of an existing active locale (request only)              |
| `name`        | String  | Yes      | max 100 chars         | Localized display name (e.g., `"Weight"`)                   |
| `description` | String  | No       | unlimited             | Short explanation shown in the UI                           |
| `sort_order`  | Integer | Yes      | not null              | Display order for this locale entry                         |

---

## Create Unit Type

`POST /api/v1/unit-types`

Creates a unit type along with its locale-specific translations in one request. All provided `locale_id` values must
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
  "code": "SPEED",
  "sort_order": 11,
  "locales": [
    {
      "locale_id": 1,
      "name": "Speed",
      "description": "Units for measuring velocity or rate of movement.",
      "sort_order": 1
    }
  ]
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 11
}
```

---

## Get Unit Type

`GET /api/v1/unit-types/{id}`

Returns a single unit type with all its locale translations. Each locale entry embeds the full locale object.
The optional `description` field is omitted from the response when not set.

### Path Parameters

| Parameter | Type | Required | Description         |
|-----------|------|----------|---------------------|
| `id`      | Long | Yes      | ID of the unit type |

### Response `200 OK`

```json
{
  "unit_type": {
    "id": 1,
    "code": "WEIGHT",
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
        "name": "Weight",
        "description": "Units for measuring mass or weight.",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List / Search Unit Types

`GET /api/v1/unit-types`

Returns a paginated, searchable list of active (non-deleted) unit types. Each item includes all locale translations.
All filter parameters are optional; omitting them returns all unit types. The `code` filter performs a
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
      "code": "WEIGHT",
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
          "name": "Weight",
          "description": "Units for measuring mass or weight.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "code": "VOLUME",
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
          "name": "Volume",
          "description": "Units for measuring liquid or gas volume.",
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

## Update Unit Type

`PUT /api/v1/unit-types/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the unit type locale endpoints.

### Path Parameters

| Parameter | Type | Required | Description         |
|-----------|------|----------|---------------------|
| `id`      | Long | Yes      | ID of the unit type |

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

## Delete Unit Type

`DELETE /api/v1/unit-types/{id}`

Soft-deletes the unit type. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Required | Description         |
|-----------|------|----------|---------------------|
| `id`      | Long | Yes      | ID of the unit type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Unit Type Locales

Unit type locale endpoints manage per-locale translations for a unit type. The `{unit-type-id}` path parameter must
reference an existing, active unit type.

---

### Create Unit Type Locale

`POST /api/v1/unit-types/{unit-type-id}/locales`

Adds a new locale translation to an existing unit type. Each `locale_id` may only be used once per unit type.

#### Path Parameters

| Parameter      | Type | Required | Description         |
|----------------|------|----------|---------------------|
| `unit-type-id` | Long | Yes      | ID of the unit type |

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
  "name": "Gewicht",
  "description": "Einheiten zur Messung von Masse oder Gewicht.",
  "sort_order": 1
}
```

#### Response `201 Created`

```json
{
  "success": true,
  "id": 11
}
```

---

### Update Unit Type Locale

`PUT /api/v1/unit-types/{unit-type-id}/locales/{id}`

Updates an existing locale translation for a unit type. `locale_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter      | Type | Required | Description                |
|----------------|------|----------|----------------------------|
| `unit-type-id` | Long | Yes      | ID of the unit type        |
| `id`           | Long | Yes      | ID of the unit type locale |

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Request Body

```json
{
  "name": "Weight",
  "description": "Units for measuring mass or weight.",
  "sort_order": 1
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

### Delete Unit Type Locale

`DELETE /api/v1/unit-types/{unit-type-id}/locales/{id}`

Soft-deletes a unit type locale. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter      | Type | Required | Description                |
|----------------|------|----------|----------------------------|
| `unit-type-id` | Long | Yes      | ID of the unit type        |
| `id`           | Long | Yes      | ID of the unit type locale |

#### Response `200 OK`

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
  "message": "UnitType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                   |
|-------------|----------------------------|-----------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations (e.g. `name` blank, `sort_order` null) |
| 400         | `INVALID_ARGUMENT`         | Invalid `sort_by` field                                                                 |
| 404         | `ENTITY_NOT_FOUND`         | Unit type, locale, or unit type locale not found, or already deleted                    |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `code` or duplicate `locale_id` for the same unit type                        |
