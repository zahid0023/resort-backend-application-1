# Days of Week API

Base URL: `/api/v1/days-of-week`

Days of week are pre-seeded master data representing the seven days of the week (Monday through Sunday). They cannot be
created or deleted — the full set is inserted by the database migration. Only `display_order` is updatable. Locale
translations are managed via the locale sub-resource endpoints and are included in every `getById` response via the
`locales` array. Locale translations support soft-delete — deleted locales are hidden from all responses.

---

## Endpoints

| Method | Path                                                  | Description                  |
|--------|-------------------------------------------------------|------------------------------|
| GET    | `/api/v1/days-of-week`                                | List / search days of week   |
| GET    | `/api/v1/days-of-week/{id}`                           | Get a day of week            |
| PUT    | `/api/v1/days-of-week/{id}`                           | Update a day of week         |
| POST   | `/api/v1/days-of-week/{day-of-week-id}/locales`       | Create a day of week locale  |
| PUT    | `/api/v1/days-of-week/{day-of-week-id}/locales/{id}`  | Update a day of week locale  |
| DELETE | `/api/v1/days-of-week/{day-of-week-id}/locales/{id}`  | Delete a day of week locale  |

---

## Data Model

### Day of Week

| Field           | Type    | Required | Constraints              | Description                                        |
|-----------------|---------|----------|--------------------------|----------------------------------------------------|
| `id`            | Long    | —        | read-only                | Auto-generated identifier                          |
| `code`          | String  | —        | read-only, max 50 chars  | Internal code (e.g. `MONDAY`, `TUESDAY`)           |
| `iso_day_number`| Integer | —        | read-only, 1–7           | ISO-8601 day number (Monday = 1, Sunday = 7)       |
| `display_order` | Integer | Yes      | not null                 | Display order                                      |
| `locales`       | Array   | —        | read-only                | All active locale translations for this day        |

### Day of Week Locale

| Field        | Type   | Required | Constraints   | Description                                        |
|--------------|--------|----------|---------------|----------------------------------------------------|
| `id`         | Long   | —        | read-only     | Auto-generated identifier                          |
| `locale_id`  | Long   | Yes      | must exist    | ID of an existing active locale; not updatable     |
| `name`       | String | Yes      | max 100 chars | Localized name of the day (e.g. `Monday`)          |
| `short_name` | String | No       | max 20 chars  | Localized short name (e.g. `Mon`); omitted if null |

---

## Get Day of Week

`GET /api/v1/days-of-week/{id}`

Returns a single day of week with all its active locale translations.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the day of week |

### Response `200 OK`

`short_name` is omitted from locale entries when not set.

```json
{
  "day_of_week": {
    "id": 1,
    "code": "MONDAY",
    "iso_day_number": 1,
    "display_order": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Monday",
        "short_name": "Mon"
      },
      {
        "id": 2,
        "locale_id": 2,
        "name": "সোমবার",
        "short_name": "সোম"
      }
    ]
  }
}
```

---

## List / Search Days of Week

`GET /api/v1/days-of-week`

Returns a paginated, filterable list of days of week. Each item includes all active locale translations. All filter
parameters are optional; omitting them returns all days. Filtering performs a case-insensitive partial match.

### Query Parameters

| Parameter    | Type   | Default | Constraints                                                   | Description                                         |
|--------------|--------|---------|---------------------------------------------------------------|-----------------------------------------------------|
| `code`       | String | —       | —                                                             | Filter by code (partial, case-insensitive)           |
| `page`       | int    | `0`     | >= 0                                                          | Zero-based page index                               |
| `size`       | int    | `10`    | 1 – 50                                                        | Number of items per page                            |
| `sort_by`    | String | `id`    | `id`, `code`, `isoDayNumber`, `displayOrder`, `createdAt`     | Field to sort by                                    |
| `sort_dir`   | String | `ASC`   | `ASC`, `DESC`                                                 | Sort direction                                      |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "MONDAY",
      "iso_day_number": 1,
      "display_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Monday",
          "short_name": "Mon"
        }
      ]
    },
    {
      "id": 2,
      "code": "TUESDAY",
      "iso_day_number": 2,
      "display_order": 2,
      "locales": [
        {
          "id": 3,
          "locale_id": 1,
          "name": "Tuesday",
          "short_name": "Tue"
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 7,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Day of Week

`PUT /api/v1/days-of-week/{id}`

Updates `display_order`. The `code` and `iso_day_number` fields are set by the database migration and cannot be changed.
Locale translations are managed via the day of week locale endpoints.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|-----------------------|
| `id`      | Long | ID of the day of week |

### Request Body

```json
{
  "display_order": 1
}
```

### Request Fields

| Field           | Type    | Required | Validation |
|-----------------|---------|----------|------------|
| `display_order` | Integer | Yes      | Not null   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Day of Week Locales

Day of week locale endpoints manage per-locale translations for a day of week. The `{day-of-week-id}` path parameter
must reference an existing day of week.

---

### Create Day of Week Locale

`POST /api/v1/days-of-week/{day-of-week-id}/locales`

Adds a new locale translation to an existing day of week.

#### Path Parameters

| Parameter        | Type | Description           |
|------------------|------|-----------------------|
| `day-of-week-id` | Long | ID of the day of week |

#### Request Body

```json
{
  "locale_id": 1,
  "name": "Monday",
  "short_name": "Mon"
}
```

#### Request Fields

| Field        | Type   | Required | Validation            |
|--------------|--------|----------|-----------------------|
| `locale_id`  | Long   | Yes      | Not null, must exist  |
| `name`       | String | Yes      | Not blank, max 100 chars |
| `short_name` | String | No       | max 20 chars          |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

### Update Day of Week Locale

`PUT /api/v1/days-of-week/{day-of-week-id}/locales/{id}`

Updates `name` and `short_name` for an existing locale translation. `locale_id` is set at creation time and cannot be
changed.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `day-of-week-id` | Long | ID of the day of week        |
| `id`             | Long | ID of the day of week locale |

#### Request Body

```json
{
  "name": "Monday",
  "short_name": "Mon"
}
```

#### Request Fields

| Field        | Type   | Required | Validation               |
|--------------|--------|----------|--------------------------|
| `name`       | String | Yes      | Not blank, max 100 chars |
| `short_name` | String | No       | max 20 chars             |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

### Delete Day of Week Locale

`DELETE /api/v1/days-of-week/{day-of-week-id}/locales/{id}`

Soft-deletes a day of week locale. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `day-of-week-id` | Long | ID of the day of week        |
| `id`             | Long | ID of the day of week locale |

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
  "message": "DayOfWeek not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                              |
|-------------|----------------------------|--------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field                      |
| 404         | `ENTITY_NOT_FOUND`         | Day of week or locale not found, or locale already deleted         |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate locale for the same day (unique constraint on locale_id) |
