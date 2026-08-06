# Contact Types API

Base URL: `/api/v1/contact-types`

Contact types represent the categories of contact information a resort can expose (e.g. `GENERAL` for general
enquiries, `RESERVATION` for bookings, `SALES`, `SUPPORT`, `EMERGENCY`, `ACCOUNTING`). Each contact type is
identified by a unique `code`. A contact type's display name and description are locale-specific and are
managed through a companion sub-resource — Contact Type Locales — reached via
`/api/v1/contact-types/{contact-type-id}/locales`. All records support soft-delete — deleted records are
hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Contact Type)** and **`GET` (List/Search Contact Types)** — the header's value selects
  exactly one locale translation for the contact type's `locale` field: an exact match if the contact type
  has one, otherwise `en`, otherwise `null`.
- **`GET /{contact-type-id}/locales` (List Contact Type Locales)** — the header must be present, but its
  value has no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a
  single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                  | Description                    |
|--------|--------------------------------------------------------|-------------------------------------|
| POST   | `/api/v1/contact-types`                                 | Create a contact type               |
| GET    | `/api/v1/contact-types`                                 | List / search contact types         |
| GET    | `/api/v1/contact-types/{id}`                            | Get a contact type                  |
| PUT    | `/api/v1/contact-types/{id}`                            | Update a contact type               |
| DELETE | `/api/v1/contact-types/{id}`                            | Delete a contact type               |
| GET    | `/api/v1/contact-types/{contact-type-id}/locales`       | List a contact type's locales       |
| GET    | `/api/v1/contact-types/{contact-type-id}/locales/count` | Count a contact type's locales      |
| POST   | `/api/v1/contact-types/{contact-type-id}/locales`       | Create a contact type locale        |
| PUT    | `/api/v1/contact-types/{contact-type-id}/locales/{id}`  | Update a contact type locale        |
| DELETE | `/api/v1/contact-types/{contact-type-id}/locales/{id}`  | Delete a contact type locale        |

---

## Data Model

### ContactType

| Field        | Type    | Required | Constraints                                                            | Description                                                                                                                                    |
|--------------|---------|----------|--------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                                 | Auto-generated identifier                                                                                                                            |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable    | Internal code (e.g. `GENERAL`, `RESERVATION`, `SALES`, `SUPPORT`, `EMERGENCY`, `ACCOUNTING`)                                                          |
| `sort_order` | Integer | Yes      | default 0                                                                 | Display order                                                                                                                                         |
| `locale`     | Object  | —        | nullable; see ContactTypeLocale below                                    | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the contact type has no translations at all)    |

### ContactTypeLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|---------------------------------------------------|----------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                        |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`)   |
| `name`        | String  | Yes      | max 100 chars                                    | Localized display name of the contact type                                       |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                            |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                               |

---

## Create Contact Type

`POST /api/v1/contact-types`

Creates a new contact type together with exactly **one** initial locale translation. `code` must be unique
among active, non-deleted contact types — attempting to reuse an existing code returns `409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Contact Type Locales sub-resource below.

### Request Body

```json
{
  "code": "GENERAL",
  "sort_order": 1,
  "locale": {
    "name": "General",
    "description": "Main contact for general enquiries about the resort.",
    "sort_order": 1
  }
}
```

### Request Fields

| Field        | Type    | Required | Validation                                                                             |
|--------------|---------|----------|------------------------------------------------------------------------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars, unique among active records                                     |
| `sort_order` | Integer | Yes      | Not null                                                                                   |
| `locale`     | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Contact Type

`GET /api/v1/contact-types/{id}`

Returns a single active contact type by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the contact type has no translations at all).
To fetch every translation a contact type has, use
[List Contact Type Locales](#list-contact-type-locales) below.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|------------------------|
| `id`      | Long | ID of the contact type |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "GENERAL",
    "sort_order": 1,
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "General",
      "description": "Main contact for general enquiries about the resort.",
      "sort_order": 1
    }
  }
}
```

---

## List / Search Contact Types

`GET /api/v1/contact-types`

Returns a paginated, filterable list of active (non-deleted) contact types. All filter parameters are
optional; omitting them returns all contact types. Multiple filters are combined with AND. Each `LIKE`-type
filter performs a case-insensitive partial match. `Accept-Language` selects each contact type's `locale`
field the same way as `GET /{id}` (exact match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `ContactTypeFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter | Type   | Default         | Constraints                                | Description                                                                                 |
|-----------|--------|-----------------|-----------------------------------------------|--------------------------------------------------------------------------------------------------|
| `code`    | String | —               | —                                              | Filter by code (partial, case-insensitive)                                                       |
| `name`    | String | —               | —                                              | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale        |
| `page`    | int    | `0`             | >= 0                                           | Zero-based page index                                                                             |
| `size`    | int    | `10`            | 1 – 50                                         | Number of items per page                                                                          |
| `sortBy`  | String | `id` (implicit) | `createdAt`, `code`, `name` (`id` NOT selectable) | Field to sort by                                                                              |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                                  | Sort direction                                                                                    |

> **Note:** `sort_order` is not filterable or sortable — only `code` and locale `name` are wired into the
> search/sort infrastructure for this endpoint.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "GENERAL",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "General",
        "description": "Main contact for general enquiries about the resort.",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "RESERVATION",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Reservation",
        "description": "Contact for room bookings, availability checks, and reservation changes.",
        "sort_order": 2
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 2,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [
    "createdAt",
    "code",
    "name"
  ],
  "searchable_fields": [
    "code",
    "name"
  ]
}
```

---

## Update Contact Type

`PUT /api/v1/contact-types/{id}`

Updates `sort_order`. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Contact Type Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|------------------------|
| `id`      | Long | ID of the contact type |

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

## Delete Contact Type

`DELETE /api/v1/contact-types/{id}`

Soft-deletes the contact type. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter | Type | Description           |
|-----------|------|------------------------|
| `id`      | Long | ID of the contact type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Contact Type Locales

Contact Type Locale endpoints manage locale-specific name/description translations for a contact type. The
`{contact-type-id}` path parameter must reference an existing, active contact type.

---

### List Contact Type Locales

`GET /api/v1/contact-types/{contact-type-id}/locales`

Returns a paginated list of every locale translation belonging to a contact type — this is the only way to
see more than the single Accept-Language-matched translation returned by `GET /contact-types/{id}` and
`GET /contact-types`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter         | Type | Description                    |
|--------------------|------|----------------------------------|
| `contact-type-id`  | Long | ID of the parent contact type    |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|--------------|--------|---------|-------------|-----------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn`     |
| `page`       | int    | `0`     | >= 0        | Zero-based page index                                                                                |
| `size`       | int    | `10`    | 1 – 50      | Number of items per page                                                                             |

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
      "name": "General",
      "description": "Main contact for general enquiries about the resort.",
      "sort_order": 1
    },
    {
      "id": 7,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "সাধারণ",
      "description": "",
      "sort_order": 1
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

### Count Contact Type Locales

`GET /api/v1/contact-types/{contact-type-id}/locales/count`

Returns how many active locale translations a contact type currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active
locale codes) to determine which languages the contact type is still missing and can add a translation
for via [Create Contact Type Locale](#create-contact-type-locale) — e.g. if the platform has `en`, `bn`,
`es` and this endpoint returns `en`, `bn` for the contact type, `es` is still available; if it returns all
three, every platform locale already has a translation and `POST .../locales` for any of them will fail
with `409 CONFLICT`.

#### Path Parameters

| Parameter         | Type | Description                    |
|--------------------|------|----------------------------------|
| `contact-type-id`  | Long | ID of the parent contact type    |

#### Response `200 OK`

```json
{
  "count": 2,
  "codes": ["en", "bn"]
}
```

---

### Create Contact Type Locale

`POST /api/v1/contact-types/{contact-type-id}/locales`

Adds a new locale translation to an existing contact type. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of contact type and locale
must be unique — adding a locale the contact type already has a translation for returns `409 CONFLICT`,
pre-checked at the application level before any write (backed by a DB-level unique constraint on
`(contact_type_id, locale_id)` as a last-resort guard).

#### Path Parameters

| Parameter         | Type | Description                    |
|--------------------|------|----------------------------------|
| `contact-type-id`  | Long | ID of the parent contact type    |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "সাধারণ",
  "description": "রিসোর্ট সম্পর্কিত সাধারণ অনুসন্ধানের জন্য মূল যোগাযোগ।",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|-----------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale    |
| `name`        | String  | Yes      | Not blank, max 100 chars                       |
| `description` | String  | Yes      | Not null                                        |
| `sort_order`  | Integer | Yes      | Not null                                        |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 7
}
```

---

### Update Contact Type Locale

`PUT /api/v1/contact-types/{contact-type-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing contact type locale translation. The
associated contact type and locale cannot be changed after creation.

#### Path Parameters

| Parameter         | Type | Description                    |
|--------------------|------|----------------------------------|
| `contact-type-id`  | Long | ID of the parent contact type    |
| `id`               | Long | ID of the contact type locale    |

#### Request Body

```json
{
  "name": "সাধারণ",
  "description": "রিসোর্ট সম্পর্কিত সাধারণ অনুসন্ধানের জন্য মূল যোগাযোগ।",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 7
}
```

---

### Delete Contact Type Locale

`DELETE /api/v1/contact-types/{contact-type-id}/locales/{id}`

Soft-deletes a contact type locale. The record is not removed from the database but will no longer appear
in any response.

#### Path Parameters

| Parameter         | Type | Description                    |
|--------------------|------|----------------------------------|
| `contact-type-id`  | Long | ID of the parent contact type    |
| `id`               | Long | ID of the contact type locale    |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 7
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
  "message": "ContactType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                     |
|-------------|-----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value |
| 404         | `ENTITY_NOT_FOUND`         | Contact type not found, contact type locale not found, or the locale referenced by `locale_id` not found (locale creation)                                |
| 409         | `CONFLICT`                 | `code` already in use by another active contact type (`create`); or the contact type already has a translation for the given `locale_id` (`create` locale, pre-checked at the application level) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `contact_type_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level |
