# Booking Sources API

Base URL: `/api/v1/booking-sources`

A booking source represents the channel a booking originated from (e.g. `MANUAL`, `WEBSITE`, `PHONE`,
`WHATSAPP`, `FACEBOOK`, `INSTAGRAM`, `WALK_IN`, `OTA`, `API`), identified by a unique `code`. It is owned
exclusively by a booking (`resort_bookings.booking_source_id`) — a room reservation never stores its own
source, it resolves the channel by reaching through its booking. A booking source's display name and
description are locale-specific and are managed through a companion sub-resource — Booking Source Locales —
reached via `/api/v1/booking-sources/{booking-source-id}/locales`. All records support soft-delete — deleted
records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is actually
used to shape the response:

- **`GET /{id}` (Get Booking Source)** and **`GET` (List Booking Sources)** — the header's value selects
  exactly one locale translation for the `locale` field: an exact match if the booking source has one,
  otherwise `en`, otherwise `null`. Unlike some other entities in this platform, `GET /{id}` does **not**
  return every translation — use [List Booking Source Locales](#list-booking-source-locales) for that.
- **`GET /{booking-source-id}/locales` (List Booking Source Locales)** — the header must be present, but its
  value has no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a
  single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                        | Description                      |
|--------|-------------------------------------------------------------|----------------------------------|
| POST   | `/api/v1/booking-sources`                                   | Create a booking source          |
| GET    | `/api/v1/booking-sources`                                   | List / paginate booking sources  |
| GET    | `/api/v1/booking-sources/{id}`                              | Get a booking source             |
| PUT    | `/api/v1/booking-sources/{id}`                              | Update a booking source          |
| DELETE | `/api/v1/booking-sources/{id}`                              | Delete a booking source          |
| GET    | `/api/v1/booking-sources/{booking-source-id}/locales`       | List a booking source's locales  |
| GET    | `/api/v1/booking-sources/{booking-source-id}/locales/count` | Count a booking source's locales |
| POST   | `/api/v1/booking-sources/{booking-source-id}/locales`       | Create a booking source locale   |
| PUT    | `/api/v1/booking-sources/{booking-source-id}/locales/{id}`  | Update a booking source locale   |
| DELETE | `/api/v1/booking-sources/{booking-source-id}/locales/{id}`  | Delete a booking source locale   |

---

## Data Model

### BookingSource

| Field        | Type    | Required | Constraints                                                           | Description                                                                                               |
|--------------|---------|----------|-----------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                 |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal channel code (e.g. `WHATSAPP`)                                                                   |
| `sort_order` | Integer | Yes      | default 0                                                             | Display order in admin UI                                                                                 |
| `locale`     | Object  | —        | nullable; see BookingSourceLocale below                               | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if none) |

### BookingSourceLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|--------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 150 chars                                    | Localized display name (e.g. "WhatsApp")                                       |
| `description` | String  | Yes      | not null                                         | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                             |

---

## Create Booking Source

`POST /api/v1/booking-sources`

Creates a new booking source together with exactly **one** initial locale translation. `code` must be unique
among active, non-deleted booking sources — attempting to reuse an existing code returns `409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request carries
no `locale_id` at all.** There is no option to submit multiple locales at creation time. Additional languages
are added afterward via the Booking Source Locales sub-resource below.

### Request Body

```json
{
  "code": "WHATSAPP",
  "sort_order": 4,
  "locale": {
    "name": "WhatsApp",
    "description": "Booking requested over WhatsApp.",
    "sort_order": 4
  }
}
```

### Request Fields

| Field        | Type    | Required | Validation                                                                                 |
|--------------|---------|----------|--------------------------------------------------------------------------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars, unique among active records                                       |
| `sort_order` | Integer | Yes      | Not null                                                                                   |
| `locale`     | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 150 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 4
}
```

---

## Get Booking Source

`GET /api/v1/booking-sources/{id}`

Returns a single active booking source by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the booking source has no translations at all).
To fetch every translation a booking source has, use
[List Booking Source Locales](#list-booking-source-locales) below.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the booking source |

### Response `200 OK`

```json
{
  "data": {
    "id": 4,
    "code": "WHATSAPP",
    "sort_order": 4,
    "locale": {
      "id": 4,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "WhatsApp",
      "description": "Booking requested over WhatsApp.",
      "sort_order": 4
    }
  }
}
```

---

## List Booking Sources

`GET /api/v1/booking-sources`

Returns a paginated list of active (non-deleted) booking sources. `Accept-Language` selects each booking
source's `locale` field the same way as `GET /{id}` (exact match, falls back to `en`, then `null`).

> **Note:** no field on `BookingSource` was classified as filterable or sortable, so `sortBy` has no valid
> value to pass — any non-null `sortBy` throws `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit
> `sortBy` entirely to get the default (sorted by `id` ascending). There is no `code`/`name` filter parameter
> at all — this endpoint only paginates.

### Query Parameters

| Parameter | Type   | Default         | Constraints   | Description                 |
|-----------|--------|-----------------|---------------|-----------------------------|
| `page`    | int    | `0`             | >= 0          | Zero-based page index       |
| `size`    | int    | `10`            | 1 – 50        | Number of items per page    |
| `sortBy`  | String | `id` (implicit) | none valid    | Not usable — see note above |
| `sortDir` | String | `ASC`           | `ASC`, `DESC` | Sort direction              |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "MANUAL",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Manual",
        "description": "Booking entered manually by staff without a specific channel.",
        "sort_order": 1
      }
    },
    {
      "id": 4,
      "code": "WHATSAPP",
      "sort_order": 4,
      "locale": {
        "id": 4,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "WhatsApp",
        "description": "Booking requested over WhatsApp.",
        "sort_order": 4
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 2,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [],
  "searchable_fields": []
}
```

---

## Update Booking Source

`PUT /api/v1/booking-sources/{id}`

Updates `sort_order` only. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Booking Source Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the booking source |

### Request Body

```json
{
  "sort_order": 5
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
  "id": 4
}
```

---

## Delete Booking Source

`DELETE /api/v1/booking-sources/{id}`

Soft-deletes the booking source (and, along with it, every one of its locale translations). The record is
not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the booking source |

### Response `200 OK`

```json
{
  "success": true,
  "id": 4
}
```

---

## Booking Source Locales

Booking Source Locale endpoints manage locale-specific name/description translations for a booking source.
The `{booking-source-id}` path parameter must reference an existing, active booking source.

---

### List Booking Source Locales

`GET /api/v1/booking-sources/{booking-source-id}/locales`

Returns a paginated list of every locale translation belonging to a booking source — this is the only way to
see more than the single Accept-Language-matched translation returned by `GET /booking-sources/{id}` and
`GET /booking-sources`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `booking-source-id` | Long | ID of the parent booking source |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|--------------|--------|---------|-------------|-------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn` |
| `page`       | int    | `0`     | >= 0        | Zero-based page index                                                                           |
| `size`       | int    | `10`    | 1 – 50      | Number of items per page                                                                        |

> **Note:** `sortBy`/`sortDir` are accepted on the request object but there are no sortable fields registered
> for this endpoint — passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default (sorted by
> `id` ascending).

#### Response `200 OK`

```json
{
  "data": [
    {
      "id": 4,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "WhatsApp",
      "description": "Booking requested over WhatsApp.",
      "sort_order": 4
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": null,
  "searchable_fields": null
}
```

---

### Count Booking Source Locales

`GET /api/v1/booking-sources/{booking-source-id}/locales/count`

Returns how many active locale translations a booking source currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active locale
codes) to determine which languages the booking source is still missing and can add a translation for via
[Create Booking Source Locale](#create-booking-source-locale) — e.g. if the platform has `en`, `bn`, `es` and
this endpoint returns `en` for the booking source, `bn` and `es` are still available; if it returns all
three, every platform locale already has a translation and `POST .../locales` for any of them will fail with
`409 CONFLICT`.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `booking-source-id` | Long | ID of the parent booking source |

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

### Create Booking Source Locale

`POST /api/v1/booking-sources/{booking-source-id}/locales`

Adds a new locale translation to an existing booking source. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of booking source and locale
must be unique — adding a locale the booking source already has a translation for returns `409 CONFLICT`,
pre-checked at the application level before any write (backed by a DB-level unique constraint on
`(booking_source_id, locale_id)` as a last-resort guard).

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `booking-source-id` | Long | ID of the parent booking source |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "হোয়াটসঅ্যাপ",
  "description": "হোয়াটসঅ্যাপের মাধ্যমে অনুরোধ করা বুকিং।",
  "sort_order": 4
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|---------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale |
| `name`        | String  | Yes      | Not blank, max 150 chars                    |
| `description` | String  | Yes      | Not null                                    |
| `sort_order`  | Integer | Yes      | Not null                                    |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 5
}
```

---

### Update Booking Source Locale

`PUT /api/v1/booking-sources/{booking-source-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing booking source locale translation. The
associated booking source and locale cannot be changed after creation.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `booking-source-id` | Long | ID of the parent booking source |
| `id`                | Long | ID of the booking source locale |

#### Request Body

```json
{
  "name": "হোয়াটসঅ্যাপ",
  "description": "হোয়াটসঅ্যাপের মাধ্যমে অনুরোধ করা বুকিং।",
  "sort_order": 4
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 150 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 5
}
```

---

### Delete Booking Source Locale

`DELETE /api/v1/booking-sources/{booking-source-id}/locales/{id}`

Soft-deletes a booking source locale. The record is not removed from the database but will no longer appear
in any response.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `booking-source-id` | Long | ID of the parent booking source |
| `id`                | Long | ID of the booking source locale |

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
  "message": "BookingSource not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                   |
|-------------|----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; an unsupported `sortBy` query value (every `sortBy` is unsupported on this entity — see the notes above) |
| 404         | `ENTITY_NOT_FOUND`         | Booking source not found, booking source locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                                                                                          |
| 409         | `CONFLICT`                 | `code` already in use by another active booking source (`create`); the booking source already has a translation for the given `locale_id` (`create` booking source locale, pre-checked at the application level)                                        |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `booking_source_id` + `locale_id`, or on `code`, should not normally be reachable now that both are pre-checked at the application level                                                                      |
