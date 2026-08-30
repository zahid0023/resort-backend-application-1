# Reservation Sources API

Base URL: `/api/v1/reservation-sources`

A reservation source records **where a reservation originated** — the communication/booking channel (e.g.
`WHATSAPP`, `PHONE`, `WEBSITE`, `OTA`) — independent of who/what created it in the system (that's the
`created_by` audit field on the reservation itself). Each source is identified by a unique `code`. A source's
display name and description are locale-specific and are managed through a companion sub-resource —
Reservation Source Locales — reached via `/api/v1/reservation-sources/{reservation-source-id}/locales`. All
records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Reservation Source)** and **`GET` (List Reservation Sources)** — the header's value
  selects exactly one locale translation for the source's `locale` field: an exact match if the source has
  one, otherwise `en`, otherwise `null`.
- **`GET /{reservation-source-id}/locales` (List Reservation Source Locales)** — the header must be present,
  but its value has no effect; this endpoint returns every translation (optionally filtered by
  `localeCode`), not a single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                                | Description                          |
|--------|---------------------------------------------------------------------|--------------------------------------|
| POST   | `/api/v1/reservation-sources`                                       | Create a reservation source          |
| GET    | `/api/v1/reservation-sources`                                       | List reservation sources             |
| GET    | `/api/v1/reservation-sources/{id}`                                  | Get a reservation source             |
| PUT    | `/api/v1/reservation-sources/{id}`                                  | Update a reservation source          |
| DELETE | `/api/v1/reservation-sources/{id}`                                  | Delete a reservation source          |
| GET    | `/api/v1/reservation-sources/{reservation-source-id}/locales`       | List a reservation source's locales  |
| GET    | `/api/v1/reservation-sources/{reservation-source-id}/locales/count` | Count a reservation source's locales |
| POST   | `/api/v1/reservation-sources/{reservation-source-id}/locales`       | Create a reservation source locale   |
| PUT    | `/api/v1/reservation-sources/{reservation-source-id}/locales/{id}`  | Update a reservation source locale   |
| DELETE | `/api/v1/reservation-sources/{reservation-source-id}/locales/{id}`  | Delete a reservation source locale   |

---

## Data Model

### ReservationSource

| Field        | Type    | Required | Constraints                                                           | Description                                                                                                                                |
|--------------|---------|----------|-----------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                                  |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal code (e.g. `MANUAL`, `WEBSITE`, `PHONE`, `WHATSAPP`, `FACEBOOK`, `INSTAGRAM`, `WALK_IN`, `OTA`, `API`)                            |
| `sort_order` | Integer | Yes      | default 0                                                             | Display order                                                                                                                              |
| `locale`     | Object  | —        | nullable; see ReservationSourceLocale below                           | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the source has no translations at all) |

### ReservationSourceLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|--------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 150 chars                                    | Localized display name                                                         |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                             |

---

## Create Reservation Source

`POST /api/v1/reservation-sources`

Creates a new reservation source together with exactly **one** initial locale translation. `code` must be
unique among active, non-deleted reservation sources — attempting to reuse an existing code returns
`409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time. Additional
languages are added afterward via the Reservation Source Locales sub-resource below.

### Request Body

```json
{
  "code": "WHATSAPP",
  "sort_order": 4,
  "locale": {
    "name": "WhatsApp",
    "description": "Reservation requested over WhatsApp.",
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

## Get Reservation Source

`GET /api/v1/reservation-sources/{id}`

Returns a single active reservation source by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the source has no translations at all). To fetch
every translation a source has, use [List Reservation Source Locales](#list-reservation-source-locales)
below.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the reservation source |

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
      "description": "Reservation requested over WhatsApp.",
      "sort_order": 4
    }
  }
}
```

---

## List Reservation Sources

`GET /api/v1/reservation-sources`

Returns a paginated list of active (non-deleted) reservation sources. `Accept-Language` selects each source's
`locale` field the same way as `GET /{id}` (exact match, falls back to `en`, then `null`).

> **Note:** unlike most entities in this API, **no field on ReservationSource is filterable or sortable** —
> there is no `code`/`name` filter parameter, and `sortBy`/`sortDir` are accepted on the request object but
> have no registered sortable fields. Passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default (sorted by
> `id` ascending).

### Query Parameters

| Parameter | Type   | Default | Constraints   | Description                                          |
|-----------|--------|---------|---------------|------------------------------------------------------|
| `page`    | int    | `0`     | >= 0          | Zero-based page index                                |
| `size`    | int    | `10`    | 1 – 50        | Number of items per page                             |
| `sortBy`  | String | —       | none valid    | Not usable — see note above                          |
| `sortDir` | String | `ASC`   | `ASC`, `DESC` | Sort direction (no effect without a usable `sortBy`) |

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
        "description": "Reservation entered manually by staff without a specific channel.",
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
        "description": "Reservation requested over WhatsApp.",
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

## Update Reservation Source

`PUT /api/v1/reservation-sources/{id}`

Updates `sort_order` only. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Reservation Source Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the reservation source |

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

## Delete Reservation Source

`DELETE /api/v1/reservation-sources/{id}`

Soft-deletes the reservation source (and cascades the soft-delete to all of its locale translations). The
record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the reservation source |

### Response `200 OK`

```json
{
  "success": true,
  "id": 4
}
```

---

## Reservation Source Locales

Reservation Source Locale endpoints manage locale-specific name/description translations for a reservation
source. The `{reservation-source-id}` path parameter must reference an existing, active reservation source.

---

### List Reservation Source Locales

`GET /api/v1/reservation-sources/{reservation-source-id}/locales`

Returns a paginated list of every locale translation belonging to a reservation source — this is the only
way to see more than the single Accept-Language-matched translation returned by
`GET /reservation-sources/{id}` and `GET /reservation-sources`. Optionally filtered to locales whose `code`
contains a given substring.

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `reservation-source-id` | Long | ID of the parent reservation source |

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
      "id": 4,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "WhatsApp",
      "description": "Reservation requested over WhatsApp.",
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

### Count Reservation Source Locales

`GET /api/v1/reservation-sources/{reservation-source-id}/locales/count`

Returns how many active locale translations a reservation source currently has, plus the `code` of each
one. Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the source is still missing and can add a translation for via
[Create Reservation Source Locale](#create-reservation-source-locale).

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `reservation-source-id` | Long | ID of the parent reservation source |

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

### Create Reservation Source Locale

`POST /api/v1/reservation-sources/{reservation-source-id}/locales`

Adds a new locale translation to an existing reservation source. `locale_id` must reference an existing,
active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of reservation
source and locale must be unique — adding a locale the source already has a translation for returns
`409 CONFLICT`, pre-checked at the application level before any write (backed by a DB-level unique
constraint on `(reservation_source_id, locale_id)` as a last-resort guard).

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `reservation-source-id` | Long | ID of the parent reservation source |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "হোয়াটসঅ্যাপ",
  "description": "হোয়াটসঅ্যাপের মাধ্যমে অনুরোধকৃত রিজার্ভেশন।",
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

### Update Reservation Source Locale

`PUT /api/v1/reservation-sources/{reservation-source-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing reservation source locale translation. The
associated reservation source and locale cannot be changed after creation.

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `reservation-source-id` | Long | ID of the parent reservation source |
| `id`                    | Long | ID of the reservation source locale |

#### Request Body

```json
{
  "name": "হোয়াটসঅ্যাপ",
  "description": "হোয়াটসঅ্যাপের মাধ্যমে অনুরোধকৃত রিজার্ভেশন।",
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

### Delete Reservation Source Locale

`DELETE /api/v1/reservation-sources/{reservation-source-id}/locales/{id}`

Soft-deletes a reservation source locale. The record is not removed from the database but will no longer
appear in any response.

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `reservation-source-id` | Long | ID of the parent reservation source |
| `id`                    | Long | ID of the reservation source locale |

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
  "message": "ReservationSource not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                            |
|-------------|----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value            |
| 404         | `ENTITY_NOT_FOUND`         | Reservation source not found, reservation source locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                           |
| 409         | `CONFLICT`                 | `code` already in use by another active reservation source (`create`); or the source already has a translation for the given `locale_id` (`create` locale, pre-checked at the application level) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `reservation_source_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level                 |
