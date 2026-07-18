# Price Types API

Base URL: `/api/v1/price-types`

Price types define the pricing categories used to classify room price periods (e.g., Base, Weekday, Weekend, Holiday).
Display names, descriptions, purpose, and usage examples are locale-specific and are embedded in every response via
the `locales` array. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                               | Description                |
|--------|----------------------------------------------------|----------------------------|
| POST   | `/api/v1/price-types`                              | Create a price type        |
| GET    | `/api/v1/price-types`                              | List / search price types  |
| GET    | `/api/v1/price-types/{id}`                         | Get a price type           |
| PUT    | `/api/v1/price-types/{id}`                         | Update a price type        |
| DELETE | `/api/v1/price-types/{id}`                         | Delete a price type        |
| POST   | `/api/v1/price-types/{price-type-id}/locales`      | Create a price type locale |
| PUT    | `/api/v1/price-types/{price-type-id}/locales/{id}` | Update a price type locale |
| DELETE | `/api/v1/price-types/{price-type-id}/locales/{id}` | Delete a price type locale |

---

## Data Model

### Price Type

| Field        | Type    | Required | Constraints           | Description                                    |
|--------------|---------|----------|-----------------------|------------------------------------------------|
| `id`         | Long    | —        | read-only             | Auto-generated identifier                      |
| `code`       | String  | Yes      | max 50 chars, unique  | Stable business code (e.g., `BASE`, `WEEKEND`) |
| `sort_order` | Integer | Yes      | not null, default `0` | Display order in administrative interfaces     |
| `locales`    | Array   | —        | read-only here        | All locale translations for this price type    |

### Price Type Locale

| Field           | Type    | Required | Constraints           | Description                                                 |
|-----------------|---------|----------|-----------------------|-------------------------------------------------------------|
| `id`            | Long    | —        | read-only             | Auto-generated identifier                                   |
| `locale`        | Object  | —        | read-only in response | Embedded locale object (`id`, `code`, `name`, `sort_order`) |
| `locale_id`     | Long    | Yes      | not null, must exist  | ID of an existing active locale (request only)              |
| `name`          | String  | Yes      | max 100 chars         | Localized display name (e.g., `"Weekend Price"`)            |
| `description`   | String  | No       | unlimited             | Short explanation shown in the UI                           |
| `sort_order`    | Integer | Yes      | not null              | Display order for this locale entry                         |
| `purpose`       | String  | No       | unlimited             | Business purpose of this price type                         |
| `usage_example` | String  | No       | unlimited             | Example scenario shown to administrators                    |

---

## Create Price Type

`POST /api/v1/price-types`

Creates a price type along with its locale-specific translations in one request. All provided `locale_id` values must
reference existing, active locales. The `code` is set at creation time and cannot be changed.

### Request Fields

| Field        | Type    | Required | Validation              |
|--------------|---------|----------|-------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars |
| `sort_order` | Integer | Yes      | Not null                |
| `locales`    | Array   | No       | See locale fields below |

**Locale fields (`locales[]`):**

| Field           | Type    | Required | Validation               |
|-----------------|---------|----------|--------------------------|
| `locale_id`     | Long    | Yes      | Not null, must exist     |
| `name`          | String  | Yes      | Not blank, max 100 chars |
| `description`   | String  | No       | —                        |
| `sort_order`    | Integer | Yes      | Not null                 |
| `purpose`       | String  | No       | —                        |
| `usage_example` | String  | No       | —                        |

### Request Body

```json
{
  "code": "WEEKEND",
  "sort_order": 3,
  "locales": [
    {
      "locale_id": 1,
      "name": "Weekend Price",
      "description": "Premium rate applied on Saturdays and Sundays.",
      "sort_order": 1,
      "purpose": "Captures higher demand during weekend stays.",
      "usage_example": "A Deluxe Room priced at $100/night on weekdays rises to $150/night under the WEEKEND rate."
    }
  ]
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

## Get Price Type

`GET /api/v1/price-types/{id}`

Returns a single price type with all its locale translations. Each locale entry embeds the full locale object.
Optional locale fields (`description`, `purpose`, `usage_example`) are omitted from the response when not set.

### Path Parameters

| Parameter | Type | Required | Description          |
|-----------|------|----------|----------------------|
| `id`      | Long | Yes      | ID of the price type |

### Response `200 OK`

```json
{
  "price_type": {
    "id": 3,
    "code": "WEEKEND",
    "sort_order": 3,
    "locales": [
      {
        "id": 3,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Weekend Price",
        "description": "Premium rate applied on Saturdays and Sundays.",
        "sort_order": 1,
        "purpose": "Captures higher demand during weekend stays.",
        "usage_example": "A Deluxe Room priced at $100/night on weekdays rises to $150/night under the WEEKEND rate."
      }
    ]
  }
}
```

---

## List / Search Price Types

`GET /api/v1/price-types`

Returns a paginated, filterable list of active (non-deleted) price types. Each item includes all locale translations.
All filter parameters are optional; omitting them returns all price types. Each filter performs a case-insensitive
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

Optional locale fields (`description`, `purpose`, `usage_example`) are omitted when not set.

```json
{
  "data": [
    {
      "id": 1,
      "code": "BASE",
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
          "name": "Base Price",
          "description": "Standard rack rate applied by default when no other pricing rule is active.",
          "sort_order": 1,
          "purpose": "Serves as the fallback price for all room bookings.",
          "usage_example": "A Deluxe Room is listed at $100/night. With no active pricing rule in effect, the BASE rate of $100 applies."
        }
      ]
    },
    {
      "id": 2,
      "code": "WEEKDAY",
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
          "name": "Weekday Price",
          "description": "Rate applied on Monday through Friday.",
          "sort_order": 1,
          "purpose": "Encourages mid-week bookings by offering competitive weekday pricing.",
          "usage_example": "A Standard Room costs $80/night on weekdays (Mon–Fri) versus $120/night on weekends."
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 4,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Price Type

`PUT /api/v1/price-types/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the price type locale endpoints.

### Path Parameters

| Parameter | Type | Required | Description          |
|-----------|------|----------|----------------------|
| `id`      | Long | Yes      | ID of the price type |

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
  "id": 3
}
```

---

## Delete Price Type

`DELETE /api/v1/price-types/{id}`

Soft-deletes the price type. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Required | Description          |
|-----------|------|----------|----------------------|
| `id`      | Long | Yes      | ID of the price type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

## Price Type Locales

Price type locale endpoints manage per-locale translations for a price type. The `{price-type-id}` path parameter must
reference an existing, active price type.

---

### Create Price Type Locale

`POST /api/v1/price-types/{price-type-id}/locales`

Adds a new locale translation to an existing price type. Each `locale_id` may only be used once per price type.

#### Path Parameters

| Parameter       | Type | Required | Description          |
|-----------------|------|----------|----------------------|
| `price-type-id` | Long | Yes      | ID of the price type |

#### Request Fields

| Field           | Type    | Required | Validation               |
|-----------------|---------|----------|--------------------------|
| `locale_id`     | Long    | Yes      | Not null, must exist     |
| `name`          | String  | Yes      | Not blank, max 100 chars |
| `description`   | String  | No       | —                        |
| `sort_order`    | Integer | Yes      | Not null                 |
| `purpose`       | String  | No       | —                        |
| `usage_example` | String  | No       | —                        |

#### Request Body

```json
{
  "locale_id": 1,
  "name": "Holiday Price",
  "description": "Premium rate applied on public holidays and peak festive periods.",
  "sort_order": 1,
  "purpose": "Maximises revenue during high-demand holiday seasons.",
  "usage_example": "During the New Year period, a Suite is priced at $250/night under the HOLIDAY rate instead of the regular $180/night."
}
```

#### Response `201 Created`

```json
{
  "success": true,
  "id": 5
}
```

---

### Update Price Type Locale

`PUT /api/v1/price-types/{price-type-id}/locales/{id}`

Updates an existing locale translation for a price type. `locale_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter       | Type | Required | Description                 |
|-----------------|------|----------|-----------------------------|
| `price-type-id` | Long | Yes      | ID of the price type        |
| `id`            | Long | Yes      | ID of the price type locale |

#### Request Fields

| Field           | Type    | Required | Validation               |
|-----------------|---------|----------|--------------------------|
| `name`          | String  | Yes      | Not blank, max 100 chars |
| `description`   | String  | No       | —                        |
| `sort_order`    | Integer | Yes      | Not null                 |
| `purpose`       | String  | No       | —                        |
| `usage_example` | String  | No       | —                        |

#### Request Body

```json
{
  "name": "Holiday Price",
  "description": "Updated description.",
  "sort_order": 1,
  "purpose": "Updated purpose.",
  "usage_example": "Updated usage example."
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

### Delete Price Type Locale

`DELETE /api/v1/price-types/{price-type-id}/locales/{id}`

Soft-deletes a price type locale. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter       | Type | Required | Description                 |
|-----------------|------|----------|-----------------------------|
| `price-type-id` | Long | Yes      | ID of the price type        |
| `id`            | Long | Yes      | ID of the price type locale |

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
  "message": "PriceType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                   |
|-------------|----------------------------|-----------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations (e.g. `name` blank, `sort_order` null) |
| 400         | `INVALID_ARGUMENT`         | Invalid `sort_by` field                                                                 |
| 404         | `ENTITY_NOT_FOUND`         | Price type, locale, or price type locale not found, or already deleted                  |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `code` or duplicate `locale_id` for the same price type                       |
