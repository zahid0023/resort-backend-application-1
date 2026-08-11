# Days of Week API

Base URL: `/api/v1/days-of-week`

Days of week are a fixed, platform-seeded lookup list (`MONDAY` through `SUNDAY`) used elsewhere in the
system (e.g. resort/room scheduling). **The seven records themselves are read-only through this API** —
there is no create, update, or delete endpoint for a day of week; only `GET` endpoints exist. Each day's
display name and short name are locale-specific and are managed through a companion sub-resource — Day of
Week Locales — reached via `/api/v1/days-of-week/{day-of-week-id}/locales`, which does support full CRUD.
All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Day of Week)** and **`GET` (List/Search Days of Week)** — the header's value selects
  exactly one locale translation for the day's `locale` field: an exact match if the day has one, otherwise
  `en`, otherwise `null`.
- **`GET /{day-of-week-id}/locales` (List Day of Week Locales)** — the header must be present, but its
  value has no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a
  single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** (locale sub-resource) — the header must be present but its value has no effect
  at all.

---

## Endpoints

| Method | Path                                                  | Description                   |
|--------|-------------------------------------------------------|-------------------------------|
| GET    | `/api/v1/days-of-week`                                | List / search days of week    |
| GET    | `/api/v1/days-of-week/{id}`                           | Get a day of week             |
| GET    | `/api/v1/days-of-week/{day-of-week-id}/locales`       | List a day of week's locales  |
| GET    | `/api/v1/days-of-week/{day-of-week-id}/locales/count` | Count a day of week's locales |
| POST   | `/api/v1/days-of-week/{day-of-week-id}/locales`       | Create a day of week locale   |
| PUT    | `/api/v1/days-of-week/{day-of-week-id}/locales/{id}`  | Update a day of week locale   |
| DELETE | `/api/v1/days-of-week/{day-of-week-id}/locales/{id}`  | Delete a day of week locale   |

---

## Data Model

### DayOfWeek

| Field        | Type    | Required | Constraints                             | Description                                                                                                                             |
|--------------|---------|----------|-----------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                               | Auto-generated identifier                                                                                                               |
| `code`       | String  | —        | max 50 chars, unique; seeded, read-only | Internal code, e.g. `MONDAY`, `TUESDAY`, ... `SUNDAY`                                                                                   |
| `sort_order` | Integer | —        | seeded, read-only                       | Display order (1 = Monday ... 7 = Sunday)                                                                                               |
| `locale`     | Object  | —        | nullable; see DayOfWeekLocale below     | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the day has no translations at all) |

### DayOfWeekLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|--------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 100 chars                                    | Localized full name, e.g. `Monday`                                             |
| `short_name`  | String  | Yes      | not null (defaults to `""`), max 20 chars        | Localized abbreviation, e.g. `Mon`                                             |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                             |

---

## Get Day of Week

`GET /api/v1/days-of-week/{id}`

Returns a single active day of week by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the day has no translations at all). To fetch
every translation a day has, use [List Day of Week Locales](#list-day-of-week-locales) below.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|-----------------------|
| `id`      | Long | ID of the day of week |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "MONDAY",
    "sort_order": 1,
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Monday",
      "short_name": "Mon",
      "description": "",
      "sort_order": 1
    }
  }
}
```

---

## List / Search Days of Week

`GET /api/v1/days-of-week`

Returns a paginated, filterable list of active (non-deleted) days of week. All filter parameters are
optional; omitting them returns all seven days. Multiple filters are combined with AND. Each `LIKE`-type
filter performs a case-insensitive partial match. `Accept-Language` selects each day's `locale` field the
same way as `GET /{id}` (exact match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `DayOfWeekFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies. Jackson's `@JsonNaming`
> (which produces snake_case) only applies to `@RequestBody`/`@ResponseBody`; `@ModelAttribute` /
> `@ParameterObject` query-string binding goes through Spring's plain `DataBinder` instead, which
> matches the exact property name.

| Parameter   | Type   | Default         | Constraints                                                                 | Description                                                                                     |
|-------------|--------|-----------------|-----------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| `code`      | String | —               | —                                                                           | Filter by internal code (partial, case-insensitive), e.g. `MON`                                 |
| `name`      | String | —               | —                                                                           | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale       |
| `shortName` | String | —               | —                                                                           | Filter by locale-specific short name (partial, case-insensitive), scoped to the resolved locale |
| `page`      | int    | `0`             | >= 0                                                                        | Zero-based page index                                                                           |
| `size`      | int    | `10`            | 1 – 50                                                                      | Number of items per page                                                                        |
| `sortBy`    | String | `id` (implicit) | `createdAt`, `code`, `sortOrder`, `name`, `shortName` (`id` NOT selectable) | Field to sort by                                                                                |
| `sortDir`   | String | `ASC`           | `ASC`, `DESC`                                                               | Sort direction                                                                                  |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "MONDAY",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Monday",
        "short_name": "Mon",
        "description": "",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "TUESDAY",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Tuesday",
        "short_name": "Tue",
        "description": "",
        "sort_order": 2
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 7,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [
    "createdAt",
    "code",
    "sortOrder",
    "name",
    "shortName"
  ],
  "searchable_fields": [
    "code",
    "name",
    "shortName"
  ]
}
```

---

## Day of Week Locales

Day of Week Locale endpoints manage locale-specific name/short-name/description translations for a day of
week. The `{day-of-week-id}` path parameter must reference an existing, active day of week. Unlike the
seven root records, locale translations are fully mutable through this sub-resource — this is the only way
to add or edit language translations for a day of week (e.g. adding `bn`/`fr` on top of the seeded `en`
translation).

---

### List Day of Week Locales

`GET /api/v1/days-of-week/{day-of-week-id}/locales`

Returns a paginated list of every locale translation belonging to a day of week — this is the only way to
see more than the single Accept-Language-matched translation returned by `GET /days-of-week/{id}` and
`GET /days-of-week`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `day-of-week-id` | Long | ID of the parent day of week |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|--------------|--------|---------|-------------|-------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn` |
| `page`       | int    | `0`     | >= 0        | Zero-based page index                                                                           |
| `size`       | int    | `10`    | 1 – 50      | Number of items per page                                                                        |

> **Note:** `sortBy`/`sortDir` are accepted on the request object but there are no sortable fields
> registered for this endpoint — passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default
> (sorted by `id` ascending).

#### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Monday",
      "short_name": "Mon",
      "description": "",
      "sort_order": 1
    },
    {
      "id": 8,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "সোমবার",
      "short_name": "সোম",
      "description": "",
      "sort_order": 2
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 2,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": null,
  "searchable_fields": null
}
```

---

### Count Day of Week Locales

`GET /api/v1/days-of-week/{day-of-week-id}/locales/count`

Returns how many active locale translations a day of week currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active locale
codes) to determine which languages the day of week is still missing and can add a translation for via
[Create Day of Week Locale](#create-day-of-week-locale) — e.g. if the platform has `en`, `bn`, `es` and this
endpoint returns `en` for the day of week, `bn` and `es` are still available; if it returns all three, every
platform locale already has a translation and `POST .../locales` for any of them will fail with
`409 CONFLICT`.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `day-of-week-id` | Long | ID of the parent day of week |

#### Response `200 OK`

```json
{
  "count": 1,
  "codes": [
    "en"
  ]
}
```

---

### Create Day of Week Locale

`POST /api/v1/days-of-week/{day-of-week-id}/locales`

Adds a new locale translation to an existing day of week. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of day of week and locale
must be unique — adding a locale the day already has a translation for returns `409 CONFLICT`, pre-checked
at the application level before any write (backed by a DB-level unique constraint
(`uq_days_of_week_locale` on `day_of_week_id` + `locale_id`) as a last-resort guard).

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `day-of-week-id` | Long | ID of the parent day of week |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "সোমবার",
  "short_name": "সোম",
  "description": "",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|---------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale |
| `name`        | String  | Yes      | Not blank, max 100 chars                    |
| `short_name`  | String  | Yes      | Not null, max 20 chars                      |
| `description` | String  | Yes      | Not null                                    |
| `sort_order`  | Integer | Yes      | Not null                                    |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 8
}
```

---

### Update Day of Week Locale

`PUT /api/v1/days-of-week/{day-of-week-id}/locales/{id}`

Updates `name`, `short_name`, `description`, and `sort_order` for an existing day of week locale
translation. The associated day of week and locale cannot be changed after creation.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `day-of-week-id` | Long | ID of the parent day of week |
| `id`             | Long | ID of the day of week locale |

#### Request Body

```json
{
  "name": "সোমবার",
  "short_name": "সোম",
  "description": "",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `short_name`  | String  | Yes      | Not null, max 20 chars   |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 8
}
```

---

### Delete Day of Week Locale

`DELETE /api/v1/days-of-week/{day-of-week-id}/locales/{id}`

Soft-deletes a day of week locale. The record is not removed from the database but will no longer appear
in any response.

#### Path Parameters

| Parameter        | Type | Description                  |
|------------------|------|------------------------------|
| `day-of-week-id` | Long | ID of the parent day of week |
| `id`             | Long | ID of the day of week locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 8
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

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                |
|-------------|----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value                |
| 404         | `ENTITY_NOT_FOUND`         | Day of week not found, day of week locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                                             |
| 409         | `CONFLICT`                 | The day of week already has a translation for the given `locale_id` (`create` day of week locale, pre-checked at the application level)                                                              |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint (`uq_days_of_week_locale`) on `day_of_week_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level |
