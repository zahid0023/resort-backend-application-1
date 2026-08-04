# Room Categories API

Base URL: `/api/v1/room-categories`

Room categories represent the tier of accommodation offered by a resort (e.g. `STD`, `DLX`, `STE`), each
identified by a unique `code`. A room category's display name and description are locale-specific and are
managed through a companion sub-resource — Room Category Locales — reached via
`/api/v1/room-categories/{room-category-id}/locales`. All records support soft-delete — deleted records are
hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Room Category)** and **`GET` (List/Search Room Categories)** — the header's value
  selects exactly one locale translation for the room category's `locale` field: an exact match if the room
  category has one, otherwise `en`, otherwise `null`.
- **`GET /{room-category-id}/locales` (List Room Category Locales)** — the header must be present, but its
  value has no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a
  single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                       | Description                     |
|--------|-------------------------------------------------------------|---------------------------------|
| POST   | `/api/v1/room-categories`                                    | Create a room category           |
| GET    | `/api/v1/room-categories`                                    | List / search room categories    |
| GET    | `/api/v1/room-categories/{id}`                               | Get a room category              |
| PUT    | `/api/v1/room-categories/{id}`                               | Update a room category           |
| DELETE | `/api/v1/room-categories/{id}`                               | Delete a room category           |
| GET    | `/api/v1/room-categories/{room-category-id}/locales`         | List a room category's locales   |
| POST   | `/api/v1/room-categories/{room-category-id}/locales`         | Create a room category locale    |
| PUT    | `/api/v1/room-categories/{room-category-id}/locales/{id}`    | Update a room category locale    |
| DELETE | `/api/v1/room-categories/{room-category-id}/locales/{id}`    | Delete a room category locale    |

---

## Data Model

### Room Category

| Field        | Type    | Required | Constraints                                                            | Description                                                                                                                                       |
|--------------|---------|----------|----------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                                 | Auto-generated identifier                                                                                                                          |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable    | Internal code (e.g. `STD`, `DLX`, `STE`)                                                                                                            |
| `sort_order` | Integer | Yes      | default 0                                                                 | Display order                                                                                                                                       |
| `locale`     | Object  | —        | nullable; see RoomCategoryLocale below                                   | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the room category has no translations at all)  |

### RoomCategoryLocale

| Field         | Type    | Required | Constraints                                                          | Description                                                                    |
|---------------|---------|----------|--------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                                               | Auto-generated identifier                                                          |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation                      | The locale this translation is written in (`id`, `code`, `name`, `sort_order`)     |
| `name`        | String  | Yes      | max 100 chars, unique among active translations for the same locale   | Localized name of the room category                                                |
| `description` | String  | Yes      | not null (defaults to `""`)                                            | Localized description                                                              |
| `sort_order`  | Integer | Yes      | default 0                                                               | Display order among locale entries                                                 |

---

## Create Room Category

`POST /api/v1/room-categories`

Creates a new room category together with exactly **one** initial locale translation. `code` must be unique
among active, non-deleted room categories — attempting to reuse an existing code returns `409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Room Category Locales sub-resource below.

### Request Body

```json
{
  "code": "STD",
  "sort_order": 1,
  "locale": {
    "name": "Standard Room",
    "description": "Comfortable room with essential amenities for everyday stays.",
    "sort_order": 1
  }
}
```

### Request Fields

| Field        | Type    | Required | Validation                                                                                 |
|--------------|---------|----------|-----------------------------------------------------------------------------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars, unique among active records                                          |
| `sort_order` | Integer | Yes      | Not null                                                                                        |
| `locale`     | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale     |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation                                                          |
|---------------|---------|----------|-----------------------------------------------------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars, unique among active translations for `en`   |
| `description` | String  | Yes      | Not null                                                               |
| `sort_order`  | Integer | Yes      | Not null                                                               |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Room Category

`GET /api/v1/room-categories/{id}`

Returns a single active room category by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the room category has no translations at all).
To fetch every translation a room category has, use [List Room Category Locales](#list-room-category-locales)
below.

### Path Parameters

| Parameter | Type | Description             |
|-----------|------|-----------------------------|
| `id`      | Long | ID of the room category     |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "STD",
    "sort_order": 1,
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Standard Room",
      "description": "Comfortable room with essential amenities for everyday stays.",
      "sort_order": 1
    }
  }
}
```

---

## List / Search Room Categories

`GET /api/v1/room-categories`

Returns a paginated, filterable list of active (non-deleted) room categories. All filter parameters are
optional; omitting them returns all room categories. Each `LIKE`-type filter performs a case-insensitive
partial match. `Accept-Language` selects each room category's `locale` field the same way as `GET /{id}`
(exact match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `RoomCategoryFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter | Type   | Default         | Constraints                             | Description                                     |
|-----------|--------|-----------------|--------------------------------------------|-----------------------------------------------------|
| `code`    | String | —               | —                                           | Filter by code (partial, case-insensitive)          |
| `name`    | String | —               | —                                           | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale |
| `page`    | int    | `0`             | >= 0                                        | Zero-based page index                               |
| `size`    | int    | `10`            | 1 – 50                                      | Number of items per page                            |
| `sortBy`  | String | `id` (implicit) | `createdAt`, `code`, `name` (`id` NOT selectable) | Field to sort by                              |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                               | Sort direction                                      |

> **Note:** `sort_order` is not filterable or sortable — only `code` and locale `name` are wired into the
> search/sort infrastructure for this endpoint.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "STD",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Standard Room",
        "description": "Comfortable room with essential amenities for everyday stays.",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "DLX",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Deluxe Room",
        "description": "Spacious room with upgraded interior and additional facilities.",
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

## Update Room Category

`PUT /api/v1/room-categories/{id}`

Updates `sort_order`. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Room Category Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description             |
|-----------|------|-----------------------------|
| `id`      | Long | ID of the room category     |

### Request Body

```json
{
  "sort_order": 2
}
```

### Request Fields

| Field        | Type    | Required | Validation |
|--------------|---------|----------|--------------|
| `sort_order` | Integer | Yes      | Not null     |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Room Category

`DELETE /api/v1/room-categories/{id}`

Soft-deletes the room category. The record is not removed from the database but will no longer appear in
any response.

### Path Parameters

| Parameter | Type | Description             |
|-----------|------|-----------------------------|
| `id`      | Long | ID of the room category     |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Room Category Locales

Room Category Locale endpoints manage locale-specific name/description translations for a room category.
The `{room-category-id}` path parameter must reference an existing, active room category.

---

### List Room Category Locales

`GET /api/v1/room-categories/{room-category-id}/locales`

Returns a paginated list of every locale translation belonging to a room category — this is the only way to
see more than the single Accept-Language-matched translation returned by `GET /room-categories/{id}` and
`GET /room-categories`. Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter            | Type | Description                     |
|------------------------|------|--------------------------------------|
| `room-category-id`     | Long | ID of the parent room category      |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|--------------|--------|---------|-------------|-----------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn`   |
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
      "name": "Standard Room",
      "description": "Comfortable room with essential amenities for everyday stays.",
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
      "name": "স্ট্যান্ডার্ড রুম",
      "description": "প্রতিদিনের থাকার জন্য প্রয়োজনীয় সুযোগ-সুবিধাসহ আরামদায়ক রুম।",
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

### Create Room Category Locale

`POST /api/v1/room-categories/{room-category-id}/locales`

Adds a new locale translation to an existing room category. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of room category and locale
must be unique — adding a locale the room category already has a translation for returns `409 CONFLICT`,
backed by a DB-level unique constraint on `(room_category_id, locale_id)`. `name` must also be unique among
active translations for the same locale, regardless of which room category they belong to — reusing a name
already in use for that locale returns `409 CONFLICT`, pre-checked at the application level (no DB
constraint backs this one).

Unlike every other locale sub-resource in this API, both `room_category_id` and `locale_id` are declared
`ON DELETE CASCADE` at the database level — deleting the parent room category or the referenced locale
would cascade-delete this translation row at the SQL level. In practice this never triggers, since the
platform only ever soft-deletes.

#### Path Parameters

| Parameter            | Type | Description                     |
|------------------------|------|--------------------------------------|
| `room-category-id`     | Long | ID of the parent room category      |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "স্ট্যান্ডার্ড রুম",
  "description": "প্রতিদিনের থাকার জন্য প্রয়োজনীয় সুযোগ-সুবিধাসহ আরামদায়ক রুম।",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                                                |
|---------------|---------|----------|--------------------------------------------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale                                    |
| `name`        | String  | Yes      | Not blank, max 100 chars, unique among active translations for `locale_id`      |
| `description` | String  | Yes      | Not null                                                                        |
| `sort_order`  | Integer | Yes      | Not null                                                                        |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 7
}
```

---

### Update Room Category Locale

`PUT /api/v1/room-categories/{room-category-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing room category locale translation. The
associated room category and locale cannot be changed after creation. `name` is re-checked for uniqueness
among active translations for the same locale, excluding this translation itself — renaming it to a name
already used by another translation in the same locale returns `409 CONFLICT`.

#### Path Parameters

| Parameter            | Type | Description                     |
|------------------------|------|--------------------------------------|
| `room-category-id`     | Long | ID of the parent room category      |
| `id`                   | Long | ID of the room category locale      |

#### Request Body

```json
{
  "name": "স্ট্যান্ডার্ড রুম",
  "description": "প্রতিদিনের থাকার জন্য প্রয়োজনীয় সুযোগ-সুবিধাসহ আরামদায়ক রুম।",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                                                     |
|---------------|---------|----------|--------------------------------------------------------------------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars, unique among active translations for this locale             |
| `description` | String  | Yes      | Not null                                                                                |
| `sort_order`  | Integer | Yes      | Not null                                                                                |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 7
}
```

---

### Delete Room Category Locale

`DELETE /api/v1/room-categories/{room-category-id}/locales/{id}`

Soft-deletes a room category locale. The record is not removed from the database but will no longer appear
in any response.

#### Path Parameters

| Parameter            | Type | Description                     |
|------------------------|------|--------------------------------------|
| `room-category-id`     | Long | ID of the parent room category      |
| `id`                   | Long | ID of the room category locale      |

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
  "message": "RoomCategory not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                    |
|-------------|-----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value  |
| 404         | `ENTITY_NOT_FOUND`         | Room category not found, room category locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                            |
| 409         | `CONFLICT`                 | `code` already in use by another active room category (`create`); the room category already has a translation for the given `locale_id` (`create` locale); or `name` already in use by another active translation for the same locale (`create`/`update` locale, pre-checked) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `room_category_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level            |
