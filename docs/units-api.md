# Units API

Base URL: `/api/v1/units`

Units represent specific measurements within a unit type (e.g., Kilogram within Weight, Liter within Volume). Each
unit belongs to a unit type and carries a conversion factor relative to the base unit of its type. Display names and
plural forms are locale-specific and are embedded in every response via the `locales` array. All records support
soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                   | Description          |
|--------|----------------------------------------|----------------------|
| POST   | `/api/v1/units`                        | Create a unit        |
| GET    | `/api/v1/units`                        | List / search units  |
| GET    | `/api/v1/units/{id}`                   | Get a unit           |
| PUT    | `/api/v1/units/{id}`                   | Update a unit        |
| DELETE | `/api/v1/units/{id}`                   | Delete a unit        |
| POST   | `/api/v1/units/{unit-id}/locales`      | Create a unit locale |
| PUT    | `/api/v1/units/{unit-id}/locales/{id}` | Update a unit locale |
| DELETE | `/api/v1/units/{unit-id}/locales/{id}` | Delete a unit locale |

---

## Seeded Codes

The following unit codes are pre-seeded by the system:

| Code    | Symbol | Unit Type   | Base Unit | Conversion Factor |
|---------|--------|-------------|-----------|-------------------|
| `G`     | g      | WEIGHT      | Yes       | 1                 |
| `KG`    | kg     | WEIGHT      | No        | 1,000             |
| `TON`   | t      | WEIGHT      | No        | 1,000,000         |
| `ML`    | mL     | VOLUME      | Yes       | 1                 |
| `L`     | L      | VOLUME      | No        | 1,000             |
| `MM`    | mm     | LENGTH      | Yes       | 1                 |
| `CM`    | cm     | LENGTH      | No        | 10                |
| `M`     | m      | LENGTH      | No        | 1,000             |
| `KM`    | km     | LENGTH      | No        | 1,000,000         |
| `SQ_CM` | cm²    | AREA        | No        | 0.0001            |
| `SQ_M`  | m²     | AREA        | Yes       | 1                 |
| `SQ_KM` | km²    | AREA        | No        | 1,000,000         |
| `PIECE` | pc     | COUNT       | Yes       | 1                 |
| `DOZEN` | doz    | COUNT       | No        | 12                |
| `SEC`   | s      | TIME        | Yes       | 1                 |
| `MIN`   | min    | TIME        | No        | 60                |
| `HR`    | h      | TIME        | No        | 3,600             |
| `DAY`   | d      | TIME        | No        | 86,400            |
| `CEL`   | °C     | TEMPERATURE | Yes       | 1                 |
| `FAH`   | °F     | TEMPERATURE | No        | 1 (non-linear)    |
| `KEL`   | K      | TEMPERATURE | No        | 1 (non-linear)    |
| `J`     | J      | ENERGY      | Yes       | 1                 |
| `KJ`    | kJ     | ENERGY      | No        | 1,000             |
| `CAL`   | cal    | ENERGY      | No        | 4.184             |
| `KCAL`  | kcal   | ENERGY      | No        | 4,184             |
| `PA`    | Pa     | PRESSURE    | Yes       | 1                 |
| `KPA`   | kPa    | PRESSURE    | No        | 1,000             |
| `BAR`   | bar    | PRESSURE    | No        | 100,000           |
| `PSI`   | psi    | PRESSURE    | No        | 6,894.757         |
| `UNIT`  | unit   | OTHER       | Yes       | 1                 |

---

## Data Model

### Unit

| Field               | Type    | Required | Constraints           | Description                                                                   |
|---------------------|---------|----------|-----------------------|-------------------------------------------------------------------------------|
| `id`                | Long    | —        | read-only             | Auto-generated identifier                                                     |
| `unit_type_id`      | Long    | Yes      | not null, must exist  | ID of the parent unit type. Immutable after create.                           |
| `code`              | String  | Yes      | max 50 chars, unique  | Stable business code (e.g., `KG`, `L`). Immutable after create.               |
| `symbol`            | String  | Yes      | max 20 chars, unique  | Display symbol (e.g., `kg`, `L`). Immutable after create.                     |
| `is_base_unit`      | Boolean | Yes      | not null              | Whether this is the base unit of its type. Immutable after create.            |
| `conversion_factor` | Decimal | Yes      | > 0                   | Number of base units represented by one of this unit. Immutable after create. |
| `sort_order`        | Integer | Yes      | not null, default `0` | Display order within the unit type.                                           |
| `locales`           | Array   | —        | read-only here        | All locale translations for this unit.                                        |

### Unit Locale

| Field         | Type    | Required | Constraints           | Description                                                 |
|---------------|---------|----------|-----------------------|-------------------------------------------------------------|
| `id`          | Long    | —        | read-only             | Auto-generated identifier                                   |
| `locale`      | Object  | —        | read-only in response | Embedded locale object (`id`, `code`, `name`, `sort_order`) |
| `locale_id`   | Long    | Yes      | not null, must exist  | ID of an existing active locale (request only)              |
| `name`        | String  | Yes      | max 100 chars         | Singular localized name (e.g., `"Kilogram"`)                |
| `plural_name` | String  | Yes      | max 100 chars         | Plural localized name (e.g., `"Kilograms"`)                 |
| `description` | String  | No       | unlimited             | Short explanation shown in the UI                           |
| `sort_order`  | Integer | Yes      | not null              | Display order for this locale entry                         |

---

## Create Unit

`POST /api/v1/units`

Creates a unit along with its locale-specific translations in one request. `unit_type_id` must reference an existing,
active unit type. `code` and `symbol` must each be globally unique. All provided `locale_id` values must reference
existing, active locales. `code`, `symbol`, `is_base_unit`, `conversion_factor`, and `unit_type_id` are set at
creation time and cannot be changed.

### Request Fields

| Field               | Type    | Required | Validation                      |
|---------------------|---------|----------|---------------------------------|
| `unit_type_id`      | Long    | Yes      | Not null, must exist            |
| `code`              | String  | Yes      | Not blank, max 50 chars, unique |
| `symbol`            | String  | Yes      | Not blank, max 20 chars, unique |
| `is_base_unit`      | Boolean | Yes      | Not null                        |
| `conversion_factor` | Decimal | Yes      | Not null, must be > 0           |
| `sort_order`        | Integer | Yes      | Not null                        |
| `locales`           | Array   | No       | See locale fields below         |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `plural_name` | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Request Body

```json
{
  "unit_type_id": 1,
  "code": "MG",
  "symbol": "mg",
  "is_base_unit": false,
  "conversion_factor": 0.001,
  "sort_order": 4,
  "locales": [
    {
      "locale_id": 1,
      "name": "Milligram",
      "plural_name": "Milligrams",
      "description": "Equal to 0.001 grams.",
      "sort_order": 1
    }
  ]
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 31
}
```

---

## Get Unit

`GET /api/v1/units/{id}`

Returns a single unit with all its locale translations. Each locale entry embeds the full locale object.
The optional `description` field is omitted from the response when not set.

### Path Parameters

| Parameter | Type | Required | Description    |
|-----------|------|----------|----------------|
| `id`      | Long | Yes      | ID of the unit |

### Response `200 OK`

```json
{
  "unit": {
    "id": 2,
    "unit_type_id": 1,
    "code": "KG",
    "symbol": "kg",
    "is_base_unit": false,
    "conversion_factor": 1000.0,
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
        "name": "Kilogram",
        "plural_name": "Kilograms",
        "description": "Equal to 1,000 grams.",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List / Search Units

`GET /api/v1/units`

Returns a paginated, searchable list of active (non-deleted) units. Each item includes all locale translations.
All filter parameters are optional; omitting them returns all units. The `code` and `symbol` filters perform
case-insensitive partial matches.

### Query Parameters

| Parameter      | Type    | Default | Constraints                                      | Description                                  |
|----------------|---------|---------|--------------------------------------------------|----------------------------------------------|
| `code`         | String  | —       | —                                                | Filter by code (partial, case-insensitive)   |
| `symbol`       | String  | —       | —                                                | Filter by symbol (partial, case-insensitive) |
| `unit-type-id` | Long    | —       | —                                                | Filter by unit type                          |
| `is_base_unit` | Boolean | —       | —                                                | Filter by base unit flag                     |
| `page`         | int     | `0`     | >= 0                                             | Zero-based page index                        |
| `size`         | int     | `10`    | 1 – 50                                           | Number of items per page                     |
| `sort_by`      | String  | `id`    | `id`, `code`, `symbol`, `sortOrder`, `createdAt` | Field to sort by                             |
| `sort_dir`     | String  | `ASC`   | `ASC`, `DESC`                                    | Sort direction                               |

### Response `200 OK`

The optional `description` field is omitted per item when not set.

```json
{
  "data": [
    {
      "id": 1,
      "unit_type_id": 1,
      "code": "G",
      "symbol": "g",
      "is_base_unit": true,
      "conversion_factor": 1.0,
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
          "name": "Gram",
          "plural_name": "Grams",
          "description": "Base unit of weight.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "unit_type_id": 1,
      "code": "KG",
      "symbol": "kg",
      "is_base_unit": false,
      "conversion_factor": 1000.0,
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
          "name": "Kilogram",
          "plural_name": "Kilograms",
          "description": "Equal to 1,000 grams.",
          "sort_order": 1
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 3,
  "total_elements": 30,
  "page_size": 10,
  "has_next": true,
  "has_previous": false
}
```

---

## Update Unit

`PUT /api/v1/units/{id}`

Updates `sort_order`. The `code`, `symbol`, `is_base_unit`, `conversion_factor`, and `unit_type_id` fields are set
at creation time and cannot be changed. Locale translations are managed via the unit locale endpoints.

### Path Parameters

| Parameter | Type | Required | Description    |
|-----------|------|----------|----------------|
| `id`      | Long | Yes      | ID of the unit |

### Request Fields

| Field        | Type    | Required | Validation |
|--------------|---------|----------|------------|
| `sort_order` | Integer | Yes      | Not null   |

### Request Body

```json
{
  "sort_order": 3
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

## Delete Unit

`DELETE /api/v1/units/{id}`

Soft-deletes the unit. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Required | Description    |
|-----------|------|----------|----------------|
| `id`      | Long | Yes      | ID of the unit |

### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

## Unit Locales

Unit locale endpoints manage per-locale translations for a unit. The `{unit-id}` path parameter must reference an
existing, active unit.

---

### Create Unit Locale

`POST /api/v1/units/{unit-id}/locales`

Adds a new locale translation to an existing unit. Each `locale_id` may only be used once per unit.

#### Path Parameters

| Parameter | Type | Required | Description    |
|-----------|------|----------|----------------|
| `unit-id` | Long | Yes      | ID of the unit |

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `plural_name` | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "Kilogramm",
  "plural_name": "Kilogramm",
  "description": "Entspricht 1.000 Gramm.",
  "sort_order": 1
}
```

#### Response `201 Created`

```json
{
  "success": true,
  "id": 31
}
```

---

### Update Unit Locale

`PUT /api/v1/units/{unit-id}/locales/{id}`

Updates an existing locale translation for a unit. `locale_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter | Type | Required | Description           |
|-----------|------|----------|-----------------------|
| `unit-id` | Long | Yes      | ID of the unit        |
| `id`      | Long | Yes      | ID of the unit locale |

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `plural_name` | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Request Body

```json
{
  "name": "Kilogram",
  "plural_name": "Kilograms",
  "description": "Equal to 1,000 grams.",
  "sort_order": 1
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

### Delete Unit Locale

`DELETE /api/v1/units/{unit-id}/locales/{id}`

Soft-deletes a unit locale. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter | Type | Required | Description           |
|-----------|------|----------|-----------------------|
| `unit-id` | Long | Yes      | ID of the unit        |
| `id`      | Long | Yes      | ID of the unit locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
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
  "message": "Unit not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                          |
|-------------|----------------------------|------------------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations (e.g. `name` blank, `conversion_factor` <= 0) |
| 400         | `INVALID_ARGUMENT`         | Invalid `sort_by` field                                                                        |
| 404         | `ENTITY_NOT_FOUND`         | Unit, unit type, locale, or unit locale not found, or already deleted                          |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `code`, duplicate `symbol`, or duplicate `locale_id` for the same unit               |
