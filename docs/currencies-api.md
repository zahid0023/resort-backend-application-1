# Currencies API

Base URL: `/api/v1/currencies`

Currencies represent ISO 4217 monetary units used across the platform. Each currency belongs to a country and may have
multiple locale-specific translations managed via the locale sub-resource endpoints. Locale translations are included in
every `getById` response via the `locales` array. All records support soft-delete — deleted records are hidden from all
responses.

---

## Endpoints

| Method | Path                                            | Description              |
|--------|-------------------------------------------------|--------------------------|
| POST   | `/api/v1/currencies`                            | Create a currency        |
| GET    | `/api/v1/currencies`                            | List / search currencies |
| GET    | `/api/v1/currencies/{id}`                       | Get a currency           |
| PUT    | `/api/v1/currencies/{id}`                       | Update a currency        |
| DELETE | `/api/v1/currencies/{id}`                       | Delete a currency        |
| POST   | `/api/v1/currencies/{currency-id}/locales`      | Create a currency locale |
| PUT    | `/api/v1/currencies/{currency-id}/locales/{id}` | Update a currency locale |
| DELETE | `/api/v1/currencies/{currency-id}/locales/{id}` | Delete a currency locale |

---

## Data Model

### Currency

| Field            | Type    | Required | Constraints               | Description                                                |
|------------------|---------|----------|---------------------------|------------------------------------------------------------|
| `id`             | Long    | —        | read-only                 | Auto-generated identifier                                  |
| `code`           | String  | Yes      | max 3 chars, unique       | ISO 4217 alphabetic code (e.g. `BDT`, `USD`, `EUR`)        |
| `numeric_code`   | String  | No       | max 3 chars, unique       | ISO 4217 numeric code (e.g. `050`, `840`); omitted if null |
| `symbol`         | String  | Yes      | max 10 chars              | Currency symbol (e.g. `৳`, `$`, `€`)                       |
| `decimal_places` | Integer | Yes      | not null, default `2`     | Number of minor unit decimal places (e.g. `0` for JPY)     |
| `is_default`     | Boolean | Yes      | not null, default `false` | Whether this is the platform default currency              |
| `sort_order`     | Integer | Yes      | not null, default `0`     | Display order                                              |
| `country_id`     | Long    | Yes      | must exist, not updatable | ID of the country this currency belongs to                 |
| `locales`        | Array   | —        | read-only                 | All active locale translations for this currency           |

### Currency Locale

| Field        | Type    | Required | Constraints               | Description                                                   |
|--------------|---------|----------|---------------------------|---------------------------------------------------------------|
| `id`         | Long    | —        | read-only                 | Auto-generated identifier                                     |
| `locale_id`  | Long    | Yes      | must exist, not updatable | ID of an existing active locale                               |
| `name`       | String  | Yes      | max 200 chars             | Localized full name (e.g. `Bangladeshi Taka`, `US Dollar`)    |
| `short_name` | String  | No       | max 100 chars             | Localized short name (e.g. `Taka`, `Dollar`); omitted if null |
| `sort_order` | Integer | Yes      | not null                  | Display order for this locale entry                           |

---

## Create Currency

`POST /api/v1/currencies`

Creates a currency along with its optional locale-specific translations in one request. The `country_id` must reference
an existing, active country. All provided `locale_id` values must reference existing, active locales.

### Request Body

```json
{
  "code": "BDT",
  "numeric_code": "050",
  "symbol": "৳",
  "decimal_places": 2,
  "is_default": true,
  "sort_order": 1,
  "country_id": 1,
  "locales": [
    {
      "locale_id": 1,
      "name": "Bangladeshi Taka",
      "short_name": "Taka",
      "sort_order": 1
    }
  ]
}
```

### Request Fields

| Field            | Type    | Required | Validation              |
|------------------|---------|----------|-------------------------|
| `code`           | String  | Yes      | Not blank, max 3 chars  |
| `numeric_code`   | String  | No       | max 3 chars             |
| `symbol`         | String  | Yes      | Not blank, max 10 chars |
| `decimal_places` | Integer | Yes      | Not null                |
| `is_default`     | Boolean | Yes      | Not null                |
| `sort_order`     | Integer | Yes      | Not null                |
| `country_id`     | Long    | Yes      | Not null, must exist    |
| `locales`        | Array   | No       | See locale fields below |

**Locale fields (`locales[]`):**

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `locale_id`  | Long    | Yes      | Not null, must exist     |
| `name`       | String  | Yes      | Not blank, max 200 chars |
| `short_name` | String  | No       | max 100 chars            |
| `sort_order` | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Currency

`GET /api/v1/currencies/{id}`

Returns a single currency with all its active locale translations.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the currency |

### Response `200 OK`

Optional fields (`numeric_code`, locale `short_name`) are omitted from the response when not set.

```json
{
  "currency": {
    "id": 1,
    "code": "BDT",
    "numeric_code": "050",
    "symbol": "৳",
    "decimal_places": 2,
    "is_default": true,
    "sort_order": 1,
    "country_id": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Bangladeshi Taka",
        "short_name": "Taka",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List / Search Currencies

`GET /api/v1/currencies`

Returns a paginated, filterable list of active (non-deleted) currencies. Each item includes all active locale
translations. All filter parameters are optional; omitting them returns all currencies. Multiple filters are combined
with AND. Each filter performs a case-insensitive partial match.

### Query Parameters

| Parameter      | Type   | Default | Constraints                            | Description                                         |
|----------------|--------|---------|----------------------------------------|-----------------------------------------------------|
| `code`         | String | —       | —                                      | Filter by ISO 4217 code (partial, case-insensitive) |
| `numeric_code` | String | —       | —                                      | Filter by numeric code (partial, case-insensitive)  |
| `symbol`       | String | —       | —                                      | Filter by symbol (partial, case-insensitive)        |
| `page`         | int    | `0`     | >= 0                                   | Zero-based page index                               |
| `size`         | int    | `10`    | 1 – 50                                 | Number of items per page                            |
| `sort_by`      | String | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by                                    |
| `sort_dir`     | String | `ASC`   | `ASC`, `DESC`                          | Sort direction                                      |

### Response `200 OK`

Optional fields (`numeric_code`, locale `short_name`) are omitted when not set.

```json
{
  "data": [
    {
      "id": 1,
      "code": "BDT",
      "numeric_code": "050",
      "symbol": "৳",
      "decimal_places": 2,
      "is_default": true,
      "sort_order": 1,
      "country_id": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Bangladeshi Taka",
          "short_name": "Taka",
          "sort_order": 1
        }
      ]
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

## Update Currency

`PUT /api/v1/currencies/{id}`

Updates `numeric_code`, `symbol`, `decimal_places`, `is_default`, and `sort_order`. The `code` and `country_id` fields
are set at creation time and cannot be changed. Locale translations are managed via the currency locale endpoints.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the currency |

### Request Body

```json
{
  "numeric_code": "050",
  "symbol": "৳",
  "decimal_places": 2,
  "is_default": true,
  "sort_order": 1
}
```

### Request Fields

| Field            | Type    | Required | Validation              |
|------------------|---------|----------|-------------------------|
| `numeric_code`   | String  | No       | max 3 chars             |
| `symbol`         | String  | Yes      | Not blank, max 10 chars |
| `decimal_places` | Integer | Yes      | Not null                |
| `is_default`     | Boolean | Yes      | Not null                |
| `sort_order`     | Integer | Yes      | Not null                |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Currency

`DELETE /api/v1/currencies/{id}`

Soft-deletes the currency. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the currency |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Currency Locales

Currency locale endpoints manage per-locale translations for a currency. The `{currency-id}` path parameter must
reference an existing, active currency.

---

### Create Currency Locale

`POST /api/v1/currencies/{currency-id}/locales`

Adds a new locale translation to an existing currency.

#### Path Parameters

| Parameter     | Type | Description        |
|---------------|------|--------------------|
| `currency-id` | Long | ID of the currency |

#### Request Body

```json
{
  "locale_id": 1,
  "name": "Bangladeshi Taka",
  "short_name": "Taka",
  "sort_order": 1
}
```

#### Request Fields

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `locale_id`  | Long    | Yes      | Not null, must exist     |
| `name`       | String  | Yes      | Not blank, max 200 chars |
| `short_name` | String  | No       | max 100 chars            |
| `sort_order` | Integer | Yes      | Not null                 |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 2
}
```

---

### Update Currency Locale

`PUT /api/v1/currencies/{currency-id}/locales/{id}`

Updates `name`, `short_name`, and `sort_order` for an existing locale translation. `locale_id` is set at creation time
and cannot be changed.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `currency-id` | Long | ID of the currency        |
| `id`          | Long | ID of the currency locale |

#### Request Body

```json
{
  "name": "Bangladeshi Taka",
  "short_name": "Taka",
  "sort_order": 1
}
```

#### Request Fields

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `name`       | String  | Yes      | Not blank, max 200 chars |
| `short_name` | String  | No       | max 100 chars            |
| `sort_order` | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

### Delete Currency Locale

`DELETE /api/v1/currencies/{currency-id}/locales/{id}`

Soft-deletes a currency locale. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `currency-id` | Long | ID of the currency        |
| `id`          | Long | ID of the currency locale |

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
  "message": "Currency not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                       |
|-------------|----------------------------|-----------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field                               |
| 404         | `ENTITY_NOT_FOUND`         | Currency, country, locale, or currency locale not found, or already deleted |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `code`, `numeric_code`, or duplicate locale for the same currency |
