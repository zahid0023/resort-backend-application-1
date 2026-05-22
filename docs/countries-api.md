# Countries API

Base URL: `/api/v1/countries`

Countries represent geographic countries used across the platform. Country names and descriptions are locale-specific and are embedded in every response via the `locales` array. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                              | Description               |
|--------|---------------------------------------------------|---------------------------|
| POST   | `/api/v1/countries`                               | Create a country          |
| GET    | `/api/v1/countries`                               | List all countries        |
| GET    | `/api/v1/countries/{id}`                          | Get a country             |
| PUT    | `/api/v1/countries/{id}`                          | Update a country          |
| DELETE | `/api/v1/countries/{id}`                          | Delete a country          |
| POST   | `/api/v1/countries/{country-id}/locales`          | Create a country locale   |
| PUT    | `/api/v1/countries/{country-id}/locales/{id}`     | Update a country locale   |
| DELETE | `/api/v1/countries/{country-id}/locales/{id}`     | Delete a country locale   |

---

## Data Model

### Country

| Field        | Type    | Required | Constraints           | Description                                    |
|--------------|---------|----------|-----------------------|------------------------------------------------|
| `id`         | Long    | —        | read-only             | Auto-generated identifier                      |
| `code`       | String  | Yes      | max 10 chars, unique  | ISO 3166-1 alpha-2 code (e.g., `BD`, `US`)     |
| `iso3_code`  | String  | No       | max 10 chars          | ISO 3166-1 alpha-3 code (e.g., `BGD`, `USA`)   |
| `phone_code` | String  | No       | max 10 chars          | International dialing code (e.g., `+880`)      |
| `sort_order` | Integer | Yes      | not null, default `0` | Display order                                  |
| `locales`    | Array   | —        | read-only             | All locale translations for this country       |

### Country Locale

| Field         | Type    | Required | Constraints           | Description                             |
|---------------|---------|----------|-----------------------|-----------------------------------------|
| `id`          | Long    | —        | read-only             | Auto-generated identifier               |
| `locale_id`   | Long    | Yes      | must exist            | ID of an existing active locale         |
| `name`        | String  | Yes      | max 255 chars         | Localized name of the country           |
| `description` | String  | No       | unlimited             | Localized description                   |
| `sort_order`  | Integer | Yes      | not null              | Display order for this locale entry     |

---

## Create Country

`POST /api/v1/countries`

Creates a country along with its locale-specific translations in one request. All provided `locale_id` values must reference existing, active locales.

### Request Body

```json
{
  "code": "BD",
  "iso3_code": "BGD",
  "phone_code": "+880",
  "sort_order": 1,
  "locales": [
    {
      "locale_id": 1,
      "name": "Bangladesh",
      "description": "A country in South Asia.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "বাংলাদেশ",
      "description": "দক্ষিণ এশিয়ার একটি দেশ।",
      "sort_order": 2
    }
  ]
}
```

### Request Fields

| Field        | Type    | Required | Validation              |
|--------------|---------|----------|-------------------------|
| `code`       | String  | Yes      | Not blank, max 10 chars |
| `iso3_code`  | String  | No       | max 10 chars            |
| `phone_code` | String  | No       | max 10 chars            |
| `sort_order` | Integer | Yes      | Not null                |
| `locales`    | Array   | No       | See locale fields below |

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

## Get Country

`GET /api/v1/countries/{id}`

Returns a single country with all its locale translations.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the country  |

### Response `200 OK`

```json
{
  "country": {
    "id": 1,
    "code": "BD",
    "iso3_code": "BGD",
    "phone_code": "+880",
    "sort_order": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Bangladesh",
        "description": "A country in South Asia.",
        "sort_order": 1
      },
      {
        "id": 2,
        "locale_id": 2,
        "name": "বাংলাদেশ",
        "description": "দক্ষিণ এশিয়ার একটি দেশ।",
        "sort_order": 2
      }
    ]
  }
}
```

---

## List All Countries

`GET /api/v1/countries`

Returns a paginated list of active (non-deleted) countries. Each item includes all locale translations.

### Query Parameters

| Parameter  | Type   | Default | Constraints                                           | Description              |
|------------|--------|---------|-------------------------------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                                                  | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                                                | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `name`, `sortOrder`, `createdAt`        | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                                         | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "BD",
      "iso3_code": "BGD",
      "phone_code": "+880",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Bangladesh",
          "description": "A country in South Asia.",
          "sort_order": 1
        },
        {
          "id": 2,
          "locale_id": 2,
          "name": "বাংলাদেশ",
          "description": "দক্ষিণ এশিয়ার একটি দেশ।",
          "sort_order": 2
        }
      ]
    },
    {
      "id": 2,
      "code": "US",
      "iso3_code": "USA",
      "phone_code": "+1",
      "sort_order": 2,
      "locales": [
        {
          "id": 3,
          "locale_id": 1,
          "name": "United States",
          "description": "A country in North America.",
          "sort_order": 1
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

## Update Country

`PUT /api/v1/countries/{id}`

Updates `iso3_code`, `phone_code`, and `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed via the country locale endpoints.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the country  |

### Request Body

```json
{
  "iso3_code": "BGD",
  "phone_code": "+880",
  "sort_order": 1
}
```

### Request Fields

| Field        | Type    | Required | Validation   |
|--------------|---------|----------|--------------|
| `iso3_code`  | String  | No       | max 10 chars |
| `phone_code` | String  | No       | max 10 chars |
| `sort_order` | Integer | Yes      | Not null     |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Country

`DELETE /api/v1/countries/{id}`

Soft-deletes the country. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the country  |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Country Locales

Country locale endpoints manage per-locale translations for a country. The `{country-id}` path parameter must reference an existing, active country.

---

### Create Country Locale

`POST /api/v1/countries/{country-id}/locales`

Adds a new locale translation to an existing country.

#### Path Parameters

| Parameter    | Type | Description         |
|--------------|------|---------------------|
| `country-id` | Long | ID of the country   |

#### Request Body

```json
{
  "locale_id": 1,
  "name": "Bangladesh",
  "description": "A country in South Asia.",
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
  "id": 3
}
```

---

### Update Country Locale

`PUT /api/v1/countries/{country-id}/locales/{id}`

Updates an existing locale translation for a country.

#### Path Parameters

| Parameter    | Type | Description                |
|--------------|------|----------------------------|
| `country-id` | Long | ID of the country          |
| `id`         | Long | ID of the country locale   |

#### Request Body

```json
{
  "name": "Bangladesh",
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
  "id": 3
}
```

---

### Delete Country Locale

`DELETE /api/v1/countries/{country-id}/locales/{id}`

Soft-deletes a country locale. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter    | Type | Description                |
|--------------|------|----------------------------|
| `country-id` | Long | ID of the country          |
| `id`         | Long | ID of the country locale   |

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
  "message": "Country not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                       |
|-------------|----------------------------|-----------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field                               |
| 404         | `ENTITY_NOT_FOUND`         | Country, locale, or country locale not found, or already deleted            |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)                                  |
