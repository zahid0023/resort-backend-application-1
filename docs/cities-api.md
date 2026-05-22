# Cities API

Base URL: `/api/v1/countries/{country-id}/cities`

Cities are a sub-resource of countries. All city endpoints require a valid `country-id` in the path. City names and descriptions are locale-specific and embedded in every response via the `locales` array. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                       | Description             |
|--------|----------------------------------------------------------------------------|-------------------------|
| POST   | `/api/v1/countries/{country-id}/cities`                                    | Create a city           |
| GET    | `/api/v1/countries/{country-id}/cities`                                    | List all cities         |
| GET    | `/api/v1/countries/{country-id}/cities/{id}`                               | Get a city              |
| PUT    | `/api/v1/countries/{country-id}/cities/{id}`                               | Update a city           |
| DELETE | `/api/v1/countries/{country-id}/cities/{id}`                               | Delete a city           |
| POST   | `/api/v1/countries/{country-id}/cities/{city-id}/locales`                  | Create a city locale    |
| PUT    | `/api/v1/countries/{country-id}/cities/{city-id}/locales/{id}`             | Update a city locale    |
| DELETE | `/api/v1/countries/{country-id}/cities/{city-id}/locales/{id}`             | Delete a city locale    |

---

## Data Model

### City

| Field        | Type    | Required | Constraints           | Description                            |
|--------------|---------|----------|-----------------------|----------------------------------------|
| `id`         | Long    | —        | read-only             | Auto-generated identifier              |
| `country_id` | Long    | —        | set via path param    | ID of the country this city belongs to |
| `code`       | String  | No       | max 50 chars          | Short city code (e.g., `DHAKA`, `CTG`) |
| `sort_order` | Integer | Yes      | not null, default `0` | Display order                          |
| `locales`    | Array   | —        | read-only             | All locale translations for this city  |

### City Locale

| Field         | Type    | Required | Constraints           | Description                         |
|---------------|---------|----------|-----------------------|-------------------------------------|
| `id`          | Long    | —        | read-only             | Auto-generated identifier           |
| `locale_id`   | Long    | Yes      | must exist            | ID of an existing active locale     |
| `name`        | String  | Yes      | max 255 chars         | Localized name of the city          |
| `description` | String  | No       | —                     | Localized description               |
| `sort_order`  | Integer | Yes      | not null              | Display order for this locale entry |

---

## Create City

`POST /api/v1/countries/{country-id}/cities`

Creates a city along with its locale-specific translations in one request. All provided `locale_id` values must reference existing, active locales.

### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |

### Request Body

```json
{
  "code": "DHAKA",
  "sort_order": 1,
  "locales": [
    {
      "locale_id": 1,
      "name": "Dhaka",
      "description": "Capital city of Bangladesh.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "ঢাকা",
      "description": "বাংলাদেশের রাজধানী শহর।",
      "sort_order": 2
    }
  ]
}
```

### Request Fields

| Field        | Type    | Required | Validation              |
|--------------|---------|----------|-------------------------|
| `code`       | String  | No       | max 50 chars            |
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

## Get City

`GET /api/v1/countries/{country-id}/cities/{id}`

Returns a single city with all its locale translations.

### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |
| `id`         | Long | ID of the city    |

### Response `200 OK`

```json
{
  "city": {
    "id": 1,
    "country_id": 1,
    "code": "DHAKA",
    "sort_order": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Dhaka",
        "description": "Capital city of Bangladesh.",
        "sort_order": 1
      },
      {
        "id": 2,
        "locale_id": 2,
        "name": "ঢাকা",
        "description": "বাংলাদেশের রাজধানী শহর।",
        "sort_order": 2
      }
    ]
  }
}
```

---

## List All Cities

`GET /api/v1/countries/{country-id}/cities`

Returns a paginated list of active (non-deleted) cities for the given country. Each item includes all locale translations.

### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |

### Query Parameters

| Parameter  | Type   | Default | Constraints                            | Description              |
|------------|--------|---------|----------------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                                   | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                                 | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                          | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "DHAKA",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Dhaka",
          "description": "Capital city of Bangladesh.",
          "sort_order": 1
        },
        {
          "id": 2,
          "locale_id": 2,
          "name": "ঢাকা",
          "description": "বাংলাদেশের রাজধানী শহর।",
          "sort_order": 2
        }
      ]
    },
    {
      "id": 2,
      "code": "CTG",
      "sort_order": 2,
      "locales": [
        {
          "id": 3,
          "locale_id": 1,
          "name": "Chittagong",
          "description": "Port city of Bangladesh.",
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

## Update City

`PUT /api/v1/countries/{country-id}/cities/{id}`

Updates `sort_order` of a city. The `country_id` and `code` are fixed at creation and cannot be changed. Locale translations are managed via the city locale endpoints.

### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |
| `id`         | Long | ID of the city    |

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

## Delete City

`DELETE /api/v1/countries/{country-id}/cities/{id}`

Soft-deletes the city. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |
| `id`         | Long | ID of the city    |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## City Locales

City locale endpoints manage per-locale translations for a city. Both `{country-id}` and `{city-id}` must reference existing, active records.

---

### Create City Locale

`POST /api/v1/countries/{country-id}/cities/{city-id}/locales`

Adds a new locale translation to an existing city.

#### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |
| `city-id`    | Long | ID of the city    |

#### Request Body

```json
{
  "locale_id": 1,
  "name": "Dhaka",
  "description": "Capital city of Bangladesh.",
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

### Update City Locale

`PUT /api/v1/countries/{country-id}/cities/{city-id}/locales/{id}`

Updates an existing locale translation for a city.

#### Path Parameters

| Parameter    | Type | Description             |
|--------------|------|-------------------------|
| `country-id` | Long | ID of the country       |
| `city-id`    | Long | ID of the city          |
| `id`         | Long | ID of the city locale   |

#### Request Body

```json
{
  "name": "Dhaka",
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

### Delete City Locale

`DELETE /api/v1/countries/{country-id}/cities/{city-id}/locales/{id}`

Soft-deletes a city locale. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter    | Type | Description             |
|--------------|------|-------------------------|
| `country-id` | Long | ID of the country       |
| `city-id`    | Long | ID of the city          |
| `id`         | Long | ID of the city locale   |

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
  "message": "City not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                        |
|-------------|----------------------------|------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field                                |
| 404         | `ENTITY_NOT_FOUND`         | Country, city, locale, or city locale not found, or already deleted          |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate city-locale pair)                       |
