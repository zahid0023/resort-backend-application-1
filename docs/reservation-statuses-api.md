# Reservation Statuses API

Base URL: `/api/v1/reservation-statuses`

A reservation status represents a step in a reservation's lifecycle (`PENDING`, `CONFIRMED`, `CHECKED_IN`,
`CHECKED_OUT`, `CANCELLED`, `NO_SHOW`). Each status is identified by a unique `code`. A status's display name
and description are locale-specific and are managed through a companion sub-resource — Reservation Status
Locales — reached via `/api/v1/reservation-statuses/{reservation-status-id}/locales`. All records support
soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Reservation Status)** and **`GET` (List Reservation Statuses)** — the header's value
  selects exactly one locale translation for the status's `locale` field: an exact match if the status has
  one, otherwise `en`, otherwise `null`.
- **`GET /{reservation-status-id}/locales` (List Reservation Status Locales)** — the header must be present,
  but its value has no effect; this endpoint returns every translation (optionally filtered by
  `localeCode`), not a single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                                 | Description                          |
|--------|----------------------------------------------------------------------|--------------------------------------|
| POST   | `/api/v1/reservation-statuses`                                       | Create a reservation status          |
| GET    | `/api/v1/reservation-statuses`                                       | List reservation statuses            |
| GET    | `/api/v1/reservation-statuses/{id}`                                  | Get a reservation status             |
| PUT    | `/api/v1/reservation-statuses/{id}`                                  | Update a reservation status          |
| DELETE | `/api/v1/reservation-statuses/{id}`                                  | Delete a reservation status          |
| GET    | `/api/v1/reservation-statuses/{reservation-status-id}/locales`       | List a reservation status's locales  |
| GET    | `/api/v1/reservation-statuses/{reservation-status-id}/locales/count` | Count a reservation status's locales |
| POST   | `/api/v1/reservation-statuses/{reservation-status-id}/locales`       | Create a reservation status locale   |
| PUT    | `/api/v1/reservation-statuses/{reservation-status-id}/locales/{id}`  | Update a reservation status locale   |
| DELETE | `/api/v1/reservation-statuses/{reservation-status-id}/locales/{id}`  | Delete a reservation status locale   |

---

## Data Model

### ReservationStatus

| Field        | Type    | Required | Constraints                                                           | Description                                                                                                                                |
|--------------|---------|----------|-----------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                                  |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal code (e.g. `PENDING`, `CONFIRMED`, `CHECKED_IN`, `CHECKED_OUT`, `CANCELLED`, `NO_SHOW`)                                           |
| `sort_order` | Integer | Yes      | default 0                                                             | Display order                                                                                                                              |
| `locale`     | Object  | —        | nullable; see ReservationStatusLocale below                           | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the status has no translations at all) |

### ReservationStatusLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|--------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 150 chars                                    | Localized display name                                                         |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                             |

---

## Create Reservation Status

`POST /api/v1/reservation-statuses`

Creates a new reservation status together with exactly **one** initial locale translation. `code` must be
unique among active, non-deleted reservation statuses — attempting to reuse an existing code returns
`409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time. Additional
languages are added afterward via the Reservation Status Locales sub-resource below.

### Request Body

```json
{
  "code": "CHECKED_IN",
  "sort_order": 3,
  "locale": {
    "name": "Checked In",
    "description": "Guest has arrived and checked into the room.",
    "sort_order": 3
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
  "id": 3
}
```

---

## Get Reservation Status

`GET /api/v1/reservation-statuses/{id}`

Returns a single active reservation status by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the status has no translations at all). To
fetch every translation a status has, use
[List Reservation Status Locales](#list-reservation-status-locales) below.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the reservation status |

### Response `200 OK`

```json
{
  "data": {
    "id": 3,
    "code": "CHECKED_IN",
    "sort_order": 3,
    "locale": {
      "id": 3,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Checked In",
      "description": "Guest has arrived and checked into the room.",
      "sort_order": 3
    }
  }
}
```

---

## List Reservation Statuses

`GET /api/v1/reservation-statuses`

Returns a paginated list of active (non-deleted) reservation statuses. `Accept-Language` selects each
status's `locale` field the same way as `GET /{id}` (exact match, falls back to `en`, then `null`).

> **Note:** unlike most entities in this API, **no field on ReservationStatus is filterable or sortable** —
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
      "code": "PENDING",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Pending",
        "description": "Reservation has been created but is not yet confirmed.",
        "sort_order": 1
      }
    },
    {
      "id": 3,
      "code": "CHECKED_IN",
      "sort_order": 3,
      "locale": {
        "id": 3,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Checked In",
        "description": "Guest has arrived and checked into the room.",
        "sort_order": 3
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

## Update Reservation Status

`PUT /api/v1/reservation-statuses/{id}`

Updates `sort_order` only. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Reservation Status Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the reservation status |

### Request Body

```json
{
  "sort_order": 4
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
  "id": 3
}
```

---

## Delete Reservation Status

`DELETE /api/v1/reservation-statuses/{id}`

Soft-deletes the reservation status (and cascades the soft-delete to all of its locale translations). The
record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the reservation status |

### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

## Reservation Status Locales

Reservation Status Locale endpoints manage locale-specific name/description translations for a reservation
status. The `{reservation-status-id}` path parameter must reference an existing, active reservation status.

---

### List Reservation Status Locales

`GET /api/v1/reservation-statuses/{reservation-status-id}/locales`

Returns a paginated list of every locale translation belonging to a reservation status — this is the only
way to see more than the single Accept-Language-matched translation returned by
`GET /reservation-statuses/{id}` and `GET /reservation-statuses`. Optionally filtered to locales whose
`code` contains a given substring.

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `reservation-status-id` | Long | ID of the parent reservation status |

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
      "id": 3,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Checked In",
      "description": "Guest has arrived and checked into the room.",
      "sort_order": 3
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

### Count Reservation Status Locales

`GET /api/v1/reservation-statuses/{reservation-status-id}/locales/count`

Returns how many active locale translations a reservation status currently has, plus the `code` of each
one. Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the status is still missing and can add a translation for via
[Create Reservation Status Locale](#create-reservation-status-locale).

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `reservation-status-id` | Long | ID of the parent reservation status |

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

### Create Reservation Status Locale

`POST /api/v1/reservation-statuses/{reservation-status-id}/locales`

Adds a new locale translation to an existing reservation status. `locale_id` must reference an existing,
active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of reservation
status and locale must be unique — adding a locale the status already has a translation for returns
`409 CONFLICT`, pre-checked at the application level before any write (backed by a DB-level unique
constraint on `(reservation_status_id, locale_id)` as a last-resort guard).

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `reservation-status-id` | Long | ID of the parent reservation status |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "চেক-ইন সম্পন্ন",
  "description": "অতিথি এসে পৌঁছেছেন এবং রুমে চেক-ইন করেছেন।",
  "sort_order": 3
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
  "id": 4
}
```

---

### Update Reservation Status Locale

`PUT /api/v1/reservation-statuses/{reservation-status-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing reservation status locale translation. The
associated reservation status and locale cannot be changed after creation.

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `reservation-status-id` | Long | ID of the parent reservation status |
| `id`                    | Long | ID of the reservation status locale |

#### Request Body

```json
{
  "name": "চেক-ইন সম্পন্ন",
  "description": "অতিথি এসে পৌঁছেছেন এবং রুমে চেক-ইন করেছেন।",
  "sort_order": 3
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
  "id": 4
}
```

---

### Delete Reservation Status Locale

`DELETE /api/v1/reservation-statuses/{reservation-status-id}/locales/{id}`

Soft-deletes a reservation status locale. The record is not removed from the database but will no longer
appear in any response.

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `reservation-status-id` | Long | ID of the parent reservation status |
| `id`                    | Long | ID of the reservation status locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 4
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
  "message": "ReservationStatus not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                            |
|-------------|----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value            |
| 404         | `ENTITY_NOT_FOUND`         | Reservation status not found, reservation status locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                           |
| 409         | `CONFLICT`                 | `code` already in use by another active reservation status (`create`); or the status already has a translation for the given `locale_id` (`create` locale, pre-checked at the application level) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `reservation_status_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level                 |
