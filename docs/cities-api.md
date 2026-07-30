# Cities API

Base URL: `/api/v1/cities`

Cities belong to a country and are locale-specific, following the same pattern as the Countries API. Each response
resolves locale content to a single entry via the `Accept-Language` request header (falling back to `en` if the
requested language isn't available). All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                    | Description          |
|--------|------------------------------------------|----------------------|
| POST   | `/api/v1/cities`                        | Create a city        |
| GET    | `/api/v1/cities/{id}`                   | Get a city           |
| GET    | `/api/v1/cities`                        | List / search cities |
| PUT    | `/api/v1/cities/{id}`                   | Update a city        |
| DELETE | `/api/v1/cities/{id}`                   | Delete a city        |
| POST   | `/api/v1/cities/{city-id}/locales`      | Create a city locale |
| PUT    | `/api/v1/cities/{city-id}/locales/{id}` | Update a city locale |
| DELETE | `/api/v1/cities/{city-id}/locales/{id}` | Delete a city locale |

---

## Data Model

### City

| Field        | Type    | Required | Constraints                    | Description                                          |
|--------------|---------|----------|---------------------------------|-------------------------------------------------------|
| `id`         | Long    | —        | read-only                      | Auto-generated identifier                              |
| `country`    | Country | —        | read-only                      | The country this city belongs to                       |
| `code`       | String  | Yes      | max 50 chars, unique            | Short city code (e.g., `DHK`, `CTG`)                   |
| `sort_order` | Integer | Yes      | not null, default `0`           | Display order                                          |
| `locales`    | Array   | —        | read-only                      | Locale translation resolved to the requester's locale (empty on list/getById if no match) |

### City Locale

| Field         | Type    | Required | Constraints   | Description                         |
|---------------|---------|----------|---------------|--------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier            |
| `locale_id`   | Long    | Yes      | must exist    | ID of an existing active locale      |
| `name`        | String  | Yes      | max 255 chars | Localized name of the city           |
| `description` | String  | No       | unlimited     | Localized description                |
| `sort_order`  | Integer | Yes      | not null      | Display order for this locale entry  |

---

## Create City

`POST /api/v1/cities`

Creates a city along with its locale-specific translations in one request. The `country_id` must reference an
existing, active country. All provided `locale_id` values must reference existing, active locales.

### Request Body

```json
{
  "country_id": 1,
  "code": "DHK",
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

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|---------------------------|
| `country_id` | Long    | Yes      | Not null, must exist      |
| `code`       | String  | Yes      | Not blank, max 50 chars, unique |
| `sort_order` | Integer | Yes      | Not null                  |
| `locales`    | Array   | Yes      | Not empty — see fields below |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|----------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist       |
| `name`        | String  | Yes      | Not blank, max 255 chars   |
| `description` | String  | No       | —                          |
| `sort_order`  | Integer | Yes      | Not null                   |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get City

`GET /api/v1/cities/{id}`

Returns a single city with its parent country and the locale translation resolved from the `Accept-Language` header
(falls back to `en`, then omits `locales` if neither is found).

### Path Parameters

| Parameter | Type | Description    |
|-----------|------|----------------|
| `id`      | Long | ID of the city |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
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
          "description": "Bangladesh is a South Asian country known for its rivers, culture, and hospitality.",
          "sort_order": 1
        }
      ]
    },
    "code": "DHK",
    "sort_order": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Dhaka",
        "description": "Capital city of Bangladesh.",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List / Search Cities

`GET /api/v1/cities`

Returns a paginated, filterable list of active (non-deleted) cities. Optionally filter by country, code, and/or
localized name. Multiple filters are combined with AND. Text filters perform a case-insensitive partial match.

### Query Parameters

| Parameter   | Type   | Default | Constraints                                    | Description                                              |
|-------------|--------|---------|--------------------------------------------------|-----------------------------------------------------------|
| `countryId` | Long   | —       | —                                                | Filter by country ID (exact match)                        |
| `code`      | String | —       | —                                                | Filter by city code (partial, case-insensitive)            |
| `name`      | String | —       | —                                                | Filter by localized name (partial, case-insensitive; matches the locale resolved from `Accept-Language`) |
| `page`      | int    | `0`     | >= 0                                              | Zero-based page index                                      |
| `size`      | int    | `10`    | 1 – 50                                            | Number of items per page                                   |
| `sort_by`   | String | `id`    | `id`, `code`, `name`, `sortOrder`, `createdAt`   | Field to sort by                                            |
| `sort_dir`  | String | `ASC`   | `ASC`, `DESC`                                    | Sort direction                                              |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "country": {
        "id": 1,
        "code": "BD",
        "sort_order": 1,
        "locales": [
          {
            "id": 1,
            "locale_id": 1,
            "name": "Bangladesh",
            "sort_order": 1
          }
        ]
      },
      "code": "DHK",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Dhaka",
          "description": "Capital city of Bangladesh.",
          "sort_order": 1
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 8,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update City

`PUT /api/v1/cities/{id}`

Updates `sort_order` of a city. The `country_id` and `code` are fixed at creation and cannot be changed. Locale
translations are managed via the city locale endpoints.

### Path Parameters

| Parameter | Type | Description    |
|-----------|------|----------------|
| `id`      | Long | ID of the city |

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

`DELETE /api/v1/cities/{id}`

Soft-deletes the city. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description    |
|-----------|------|----------------|
| `id`      | Long | ID of the city |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## City Locales

City locale endpoints manage per-locale translations for a city. The `{city-id}` path parameter must reference an
existing, active city.

---

### Create City Locale

`POST /api/v1/cities/{city-id}/locales`

Adds a new locale translation to an existing city.

#### Path Parameters

| Parameter | Type | Description    |
|-----------|------|----------------|
| `city-id` | Long | ID of the city |

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

`PUT /api/v1/cities/{city-id}/locales/{id}`

Updates an existing locale translation for a city. The `locale_id` is set at creation and cannot be changed.

#### Path Parameters

| Parameter | Type | Description           |
|-----------|------|------------------------|
| `city-id` | Long | ID of the city         |
| `id`      | Long | ID of the city locale  |

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

`DELETE /api/v1/cities/{city-id}/locales/{id}`

Soft-deletes a city locale. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter | Type | Description           |
|-----------|------|------------------------|
| `city-id` | Long | ID of the city         |
| `id`      | Long | ID of the city locale  |

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

| HTTP Status | Error Code                 | Cause                                                                |
|-------------|------------------------------|------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field                          |
| 404         | `ENTITY_NOT_FOUND`         | Country, city, locale, or city locale not found, or already deleted    |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate city code or duplicate city-locale pair) |
