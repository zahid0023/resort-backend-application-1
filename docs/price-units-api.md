# Price Units API

Base URL: `/api/v1/price-units`

Price units define the unit of measurement applied to a price period (e.g., Per Night, Per Person, Per Week).
Display names, descriptions, calculation methods, and usage examples are locale-specific and are embedded in every
response via the `locales` array. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                               | Description                |
|--------|----------------------------------------------------|----------------------------|
| POST   | `/api/v1/price-units`                              | Create a price unit        |
| GET    | `/api/v1/price-units`                              | List / search price units  |
| GET    | `/api/v1/price-units/{id}`                         | Get a price unit           |
| PUT    | `/api/v1/price-units/{id}`                         | Update a price unit        |
| DELETE | `/api/v1/price-units/{id}`                         | Delete a price unit        |
| POST   | `/api/v1/price-units/{price-unit-id}/locales`      | Create a price unit locale |
| PUT    | `/api/v1/price-units/{price-unit-id}/locales/{id}` | Update a price unit locale |
| DELETE | `/api/v1/price-units/{price-unit-id}/locales/{id}` | Delete a price unit locale |

---

## Data Model

### Price Unit

| Field        | Type    | Required | Constraints           | Description                                            |
|--------------|---------|----------|-----------------------|--------------------------------------------------------|
| `id`         | Long    | —        | read-only             | Auto-generated identifier                              |
| `code`       | String  | Yes      | max 50 chars, unique  | Stable business code (e.g., `PER_NIGHT`, `PER_PERSON`) |
| `sort_order` | Integer | Yes      | not null, default `0` | Display order in administrative interfaces             |
| `locales`    | Array   | —        | read-only here        | All locale translations for this price unit            |

### Price Unit Locale

| Field                | Type    | Required | Constraints           | Description                                                  |
|----------------------|---------|----------|-----------------------|--------------------------------------------------------------|
| `id`                 | Long    | —        | read-only             | Auto-generated identifier                                    |
| `locale`             | Object  | —        | read-only in response | Embedded locale object (`id`, `code`, `name`, `sort_order`)  |
| `locale_id`          | Long    | Yes      | not null, must exist  | ID of an existing active locale (request only)               |
| `name`               | String  | Yes      | max 100 chars         | Localized display name (e.g., `"Per Night"`)                 |
| `description`        | String  | No       | unlimited             | Short explanation shown in the UI                            |
| `sort_order`         | Integer | Yes      | not null              | Display order for this locale entry                          |
| `calculation_method` | String  | No       | unlimited             | How the unit is applied when calculating a price             |
| `usage_example`      | String  | No       | unlimited             | Example scenario shown to administrators                     |

---

## Create Price Unit

`POST /api/v1/price-units`

Creates a price unit along with its locale-specific translations in one request. All provided `locale_id` values must
reference existing, active locales. The `code` is set at creation time and cannot be changed.

### Request Fields

| Field        | Type    | Required | Validation              |
|--------------|---------|----------|-------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars |
| `sort_order` | Integer | Yes      | Not null                |
| `locales`    | Array   | No       | See locale fields below |

**Locale fields (`locales[]`):**

| Field                | Type    | Required | Validation               |
|----------------------|---------|----------|--------------------------|
| `locale_id`          | Long    | Yes      | Not null, must exist     |
| `name`               | String  | Yes      | Not blank, max 100 chars |
| `description`        | String  | No       | —                        |
| `sort_order`         | Integer | Yes      | Not null                 |
| `calculation_method` | String  | No       | —                        |
| `usage_example`      | String  | No       | —                        |

### Request Body

```json
{
  "code": "PER_NIGHT",
  "sort_order": 1,
  "locales": [
    {
      "locale_id": 1,
      "name": "Per Night",
      "description": "Price charged once for each night of the stay.",
      "sort_order": 1,
      "calculation_method": "Multiply the nightly rate by the number of nights.",
      "usage_example": "A room priced at $100 PER_NIGHT for a 3-night stay totals $300."
    }
  ]
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Price Unit

`GET /api/v1/price-units/{id}`

Returns a single price unit with all its locale translations. Each locale entry embeds the full locale object.
Optional locale fields (`description`, `calculation_method`, `usage_example`) are omitted from the response when not
set.

### Path Parameters

| Parameter | Type | Required | Description          |
|-----------|------|----------|----------------------|
| `id`      | Long | Yes      | ID of the price unit |

### Response `200 OK`

```json
{
  "price_unit": {
    "id": 1,
    "code": "PER_NIGHT",
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
        "name": "Per Night",
        "description": "Price charged once for each night of the stay.",
        "sort_order": 1,
        "calculation_method": "Multiply the nightly rate by the number of nights.",
        "usage_example": "A room priced at $100 PER_NIGHT for a 3-night stay totals $300."
      }
    ]
  }
}
```

---

## List / Search Price Units

`GET /api/v1/price-units`

Returns a paginated, filterable list of active (non-deleted) price units. Each item includes all locale translations.
All filter parameters are optional; omitting them returns all price units. Each filter performs a case-insensitive
partial match.

### Query Parameters

| Parameter  | Type   | Default | Constraints                            | Description                                |
|------------|--------|---------|----------------------------------------|--------------------------------------------|
| `code`     | String | —       | —                                      | Filter by code (partial, case-insensitive) |
| `page`     | int    | `0`     | >= 0                                   | Zero-based page index                      |
| `size`     | int    | `10`    | 1 – 50                                 | Number of items per page                   |
| `sort_by`  | String | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by                           |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                          | Sort direction                             |

### Response `200 OK`

Optional locale fields (`description`, `calculation_method`, `usage_example`) are omitted when not set.

```json
{
  "data": [
    {
      "id": 1,
      "code": "PER_NIGHT",
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
          "name": "Per Night",
          "description": "Price charged once for each night of the stay.",
          "sort_order": 1,
          "calculation_method": "Multiply the nightly rate by the number of nights.",
          "usage_example": "A room priced at $100 PER_NIGHT for a 3-night stay totals $300."
        }
      ]
    },
    {
      "id": 2,
      "code": "PER_PERSON",
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
          "name": "Per Person",
          "description": "Price charged per guest per night.",
          "sort_order": 1,
          "calculation_method": "Multiply the per-person rate by the number of guests and nights.",
          "usage_example": "A villa priced at $50 PER_PERSON for 2 guests over 3 nights totals $300."
        }
      ]
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

## Update Price Unit

`PUT /api/v1/price-units/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the price unit locale endpoints.

### Path Parameters

| Parameter | Type | Required | Description          |
|-----------|------|----------|----------------------|
| `id`      | Long | Yes      | ID of the price unit |

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

## Delete Price Unit

`DELETE /api/v1/price-units/{id}`

Soft-deletes the price unit. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Required | Description          |
|-----------|------|----------|----------------------|
| `id`      | Long | Yes      | ID of the price unit |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Price Unit Locales

Price unit locale endpoints manage per-locale translations for a price unit. The `{price-unit-id}` path parameter must
reference an existing, active price unit.

---

### Create Price Unit Locale

`POST /api/v1/price-units/{price-unit-id}/locales`

Adds a new locale translation to an existing price unit. Each `locale_id` may only be used once per price unit.

#### Path Parameters

| Parameter       | Type | Required | Description          |
|-----------------|------|----------|----------------------|
| `price-unit-id` | Long | Yes      | ID of the price unit |

#### Request Fields

| Field                | Type    | Required | Validation               |
|----------------------|---------|----------|--------------------------|
| `locale_id`          | Long    | Yes      | Not null, must exist     |
| `name`               | String  | Yes      | Not blank, max 100 chars |
| `description`        | String  | No       | —                        |
| `sort_order`         | Integer | Yes      | Not null                 |
| `calculation_method` | String  | No       | —                        |
| `usage_example`      | String  | No       | —                        |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "Por Noche",
  "description": "Precio cobrado por cada noche de estancia.",
  "sort_order": 1,
  "calculation_method": "Multiplicar la tarifa nocturna por el número de noches.",
  "usage_example": "Una habitación a $100 POR_NOCHE durante 3 noches totaliza $300."
}
```

#### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

### Update Price Unit Locale

`PUT /api/v1/price-units/{price-unit-id}/locales/{id}`

Updates an existing locale translation for a price unit. `locale_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter       | Type | Required | Description                 |
|-----------------|------|----------|-----------------------------|
| `price-unit-id` | Long | Yes      | ID of the price unit        |
| `id`            | Long | Yes      | ID of the price unit locale |

#### Request Fields

| Field                | Type    | Required | Validation               |
|----------------------|---------|----------|--------------------------|
| `name`               | String  | Yes      | Not blank, max 100 chars |
| `description`        | String  | No       | —                        |
| `sort_order`         | Integer | Yes      | Not null                 |
| `calculation_method` | String  | No       | —                        |
| `usage_example`      | String  | No       | —                        |

#### Request Body

```json
{
  "name": "Per Night",
  "description": "Updated description.",
  "sort_order": 1,
  "calculation_method": "Updated calculation method.",
  "usage_example": "Updated usage example."
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

### Delete Price Unit Locale

`DELETE /api/v1/price-units/{price-unit-id}/locales/{id}`

Soft-deletes a price unit locale. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter       | Type | Required | Description                 |
|-----------------|------|----------|-----------------------------|
| `price-unit-id` | Long | Yes      | ID of the price unit        |
| `id`            | Long | Yes      | ID of the price unit locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 3
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
  "message": "PriceUnit not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                   |
|-------------|----------------------------|-----------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations (e.g. `name` blank, `sort_order` null) |
| 400         | `INVALID_ARGUMENT`         | Invalid `sort_by` field                                                                 |
| 404         | `ENTITY_NOT_FOUND`         | Price unit, locale, or price unit locale not found, or already deleted                  |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `code` or duplicate `locale_id` for the same price unit                       |
