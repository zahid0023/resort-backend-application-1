# Resort Room Facilities API

Base URL: `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities`

A resort room facility is a resort-room-scoped amenity (in-suite spa, private pool, ...) — the same concept as
a [Resort Room Category Facility](resort-room-category-facilities-api.md), one level down the hierarchy (per
individual room instead of per room category). Every resort room facility belongs to exactly one [Resort Room
Facility Group](resort-room-facility-groups-api.md) via `resort_room_facility_group_id`, set at creation and
immutable afterward — to move a facility to a different group, delete it and create a new one. At creation
time, the owner either:

- **picks a platform facility** (see [facilities-api.md](facilities-api.md)) via `facility_id` — this is purely
  a creation-time convenience so the owner can click an existing platform facility instead of typing everything
  from scratch; its icon fields are copied onto the new resort room facility as starting values (still
  overridable via `icon_type`/`icon_value`/`icon_meta`), or
- **creates a fully custom facility** — omits `facility_id`, and supplies `icon_type`/`icon_value`/`icon_meta`/
  the locale's `name`/`description` as the facility's own values.

**`facility_id` is write-only** — it is accepted on [Create](#create-resort-room-facility) but never returned
in any response; a resort room facility's own copied/overridden `icon_type`/`icon_value`/`icon_meta` and locale
`name`/`description` are all a client needs afterward; there is no ongoing link back to the platform facility.
It is set at creation and immutable afterward — to base a facility on a different platform facility, delete it
and create a new one. A resort room may link to the same platform facility at most once — attempting to link a
second time returns `409 CONFLICT` (see [Create Resort Room Facility](#create-resort-room-facility) below);
this constraint does not apply to custom facilities, and (like [Resort Room Category
Facilities](resort-room-category-facilities-api.md)) has no backing database constraint, only an
application-level check.

Resort room facilities are always reached nested under their owning resort, resort room category, and resort
room; there is no top-level `/api/v1/resort-room-facilities` route. `GET`/`PUT`/`DELETE` by `id` are scoped to
the `{resort-room-id}` in the path — an `id` that exists but belongs to a different resort room returns
`404 ENTITY_NOT_FOUND`, the same as an unknown `id`, since a resort room facility has no meaning outside its
owning resort room.

A resort room facility's display name/description/notes are locale-specific and managed through a companion
sub-resource — Resort Room Facility Locales — reached via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales`.
All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is actually
used to shape the response:

- **`GET /{id}` (Get Resort Room Facility)** and **`GET` (List/Search Resort Room Facilities)** — the header's
  value selects exactly one locale translation for the `locale` field: an exact match if one exists, otherwise
  `en`, otherwise `null`.
- **`GET .../locales` (List Resort Room Facility Locales)** — the header must be present, but its value has no
  effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a single
  Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                                                                                                             | Description                                |
|--------|------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities`                                             | Create a resort room facility               |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities`                                             | List / search a resort room's facilities    |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{id}`                                        | Get a resort room facility                  |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{id}`                                        | Update a resort room facility               |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{id}`                                        | Delete a resort room facility               |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales`           | List a resort room facility's locales       |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales/count`     | Count a resort room facility's used platform locales |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales`           | Create a resort room facility locale        |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales/{id}`      | Update a resort room facility locale        |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales/{id}`      | Delete a resort room facility locale        |

> Like [Resort Room Category Facilities](resort-room-category-facilities-api.md), resort room facilities have
> no operating-hours or price sub-resources — the underlying schema (`resort_room_facilities`) has no matching
> tables for either.

---

## Data Model

### ResortRoomFacility

| Field            | Type    | Required | Constraints                              | Description                                                                                                                                              |
|-------------------|---------|----------|--------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`             | Long    | —        | read-only                                 | Auto-generated identifier                                                                                                                                |
| `code`           | String  | Yes      | not blank, max 100 chars, immutable       | Resort-room-scoped identifier, unique per resort room                                                                                                    |
| `sort_order`     | Integer | Yes      | default 1                                 | Display order among the room's facilities                                                                                                                 |
| `is_highlighted` | Boolean | Yes      | default `false`                           | Marks the facility as featured/promoted                                                                                                                   |
| `icon_type`      | String  | —        | nullable, max 100 chars                   | Icon system identifier (e.g. `LUCIDE`); copied from the platform facility at creation if linked, then independently editable                              |
| `icon_value`     | String  | —        | nullable                                  | Icon value (e.g. an icon name); copied from the platform facility at creation if linked, then independently editable                                      |
| `icon_meta`      | Object  | —        | nullable, arbitrary JSON                  | Icon metadata (size, color, stroke width, ...); copied from the platform facility at creation if linked, then independently editable                      |
| `locale`         | Object  | —        | nullable; see ResortRoomFacilityLocale below | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the facility has no translations at all) |

> **Note:** `resort_room_facility_group_id` (the owning facility group) and `facility_id` (the platform
> facility picked at creation, if any) are write-only inputs — see [Create](#create-resort-room-facility) and
> [Update](#update-resort-room-facility) — and do not appear on this data model because they are never returned
> in any response.

**Uniqueness:** when `facility_id` is supplied at creation, the combination of `(resort_room, facility)` must
be unique among active records — creating a duplicate returns `409 CONFLICT` (see
[Create Resort Room Facility](#create-resort-room-facility)). This is checked at the application level only —
there is no database-level unique constraint backing it. No such constraint applies to custom facilities.
`code` must be unique within its owning resort room (a DB-level unique index on `(resort_room_id, code)`);
different resort rooms may reuse the same `code`.

### ResortRoomFacilityLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                       |
|---------------|---------|----------|-----------------------------------------------------|--------------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                         |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`)    |
| `name`        | String  | Yes      | max 255 chars                                    | Localized facility name (override if linked, actual name if custom)               |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized facility description (override if linked, actual description if custom) |
| `notes`       | String  | —        | not null (defaults to `""`)                      | Free-form notes about the facility (e.g. internal remarks, guest-facing caveats)   |
| `sort_order`  | Integer | Yes      | default 1                                        | Display order among locale entries                                                |

---

## Create Resort Room Facility

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities`

Creates a new facility for the resort room, together with exactly **one** initial locale translation.
`resort_room_facility_group_id` is required and must reference an existing, active resort room facility group
belonging to the same resort room — an unknown or cross-room group id returns `404 ENTITY_NOT_FOUND`.
`facility_id` is optional — supply it to base the new facility on a platform facility (its icon fields are
copied onto the new facility as starting values), or omit/`null` it to create a fully custom facility. When
`facility_id` is supplied, it must reference an existing, active platform facility, and the resort room must
not already have an active facility linked to that same platform facility — attempting to link a second time
returns `409 CONFLICT`. **`resort_room_facility_group_id` and `facility_id` are write-only, set at creation,
immutable afterward, and are not echoed back in the response** — see the note in [Data Model](#data-model)
above.

**The initial translation is always attached to the `en` locale, resolved by the server — the request carries
no `locale_id` at all.** There is no option to submit multiple locales at creation time. Additional languages
are added afterward via the Resort Room Facility Locales sub-resource below.

### Path Parameters

| Parameter                | Type | Description                            |
|---------------------------|------|-------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                    |
| `resort-room-category-id` | Long | ID of the owning resort room category      |
| `resort-room-id`          | Long | ID of the owning resort room               |

### Request Body

```json
{
  "resort_room_facility_group_id": 1,
  "facility_id": 1,
  "code": "ROOM_SPA",
  "sort_order": 1,
  "is_highlighted": false,
  "icon_type": "LUCIDE",
  "icon_value": "Sparkles",
  "icon_meta": {
    "size": 24,
    "color": "#a855f7",
    "stroke_width": 1.5
  },
  "locale": {
    "name": "In-Room Spa Treatment",
    "description": "Private spa treatments delivered directly to the room.",
    "notes": "Book at least 24 hours in advance.",
    "sort_order": 1
  }
}
```

### Request Fields

| Field                            | Type    | Required | Validation                                                                                                                            |
|------------------------------------|---------|----------|-------------------------------------------------------------------------------------------------------------------------------------------|
| `resort_room_facility_group_id`  | Long    | Yes      | Not null; must reference an existing, active resort room facility group belonging to this resort room; immutable after creation          |
| `facility_id`                    | Long    | —        | Nullable; if present, must reference an existing, active facility; immutable after creation                                              |
| `code`                           | String  | Yes      | Not blank, max 100 chars; must be unique within this resort room; immutable after creation                                               |
| `sort_order`                     | Integer | Yes      | Not null                                                                                                                                    |
| `is_highlighted`                 | Boolean | Yes      | Not null                                                                                                                                    |
| `icon_type`                      | String  | —        | Nullable, max 100 chars                                                                                                                     |
| `icon_value`                     | String  | —        | Nullable                                                                                                                                     |
| `icon_meta`                      | Object  | —        | Nullable, arbitrary JSON                                                                                                                     |
| `locale`                         | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale                                                 |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|----------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | —        | —                         |
| `notes`       | String  | —        | —                         |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Resort Room Facility

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{id}`

Returns a single active resort room facility, scoped to its owning resort room — an `id` that exists but
belongs to a different resort room returns `404 ENTITY_NOT_FOUND`, the same as an unknown `id`. `locale` is the
one translation matching the request's `Accept-Language` header (falls back to `en`, then `null` if the
facility has no translations at all). To fetch every translation, use
[List Resort Room Facility Locales](#list-resort-room-facility-locales) below.

### Path Parameters

| Parameter                | Type | Description                            |
|---------------------------|------|-------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                    |
| `resort-room-category-id` | Long | ID of the owning resort room category      |
| `resort-room-id`          | Long | ID of the owning resort room               |
| `id`                      | Long | ID of the resort room facility             |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "ROOM_SPA",
    "sort_order": 1,
    "is_highlighted": false,
    "icon_type": "LUCIDE",
    "icon_value": "Sparkles",
    "icon_meta": {
      "size": 24,
      "color": "#a855f7",
      "stroke_width": 1.5
    },
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "In-Room Spa Treatment",
      "description": "Private spa treatments delivered directly to the room.",
      "notes": "Book at least 24 hours in advance.",
      "sort_order": 1
    }
  }
}
```

---

## List / Search Resort Room Facilities

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities`

Returns a paginated, filterable list of a resort room's active (non-deleted) facilities. All filter parameters
are optional; omitting them returns every facility belonging to the resort room. Multiple filters are combined
with AND. `name` and `code` perform a case-insensitive partial match (`code` against the raw column, `name`
against the resolved locale); `resortRoomFacilityGroupId` and `facilityId` are exact matches. `Accept-Language`
selects each row's `locale` field the same way as `GET /{id}`.

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is omitted
> entirely.

### Path Parameters

| Parameter                | Type | Description                            |
|---------------------------|------|-------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                    |
| `resort-room-category-id` | Long | ID of the owning resort room category      |
| `resort-room-id`          | Long | ID of the owning resort room               |

### Query Parameters

> **Note:** Query parameters bind directly onto `ResortRoomFacilityFilterRequest`'s Java field names, so they
> are **camelCase** — not the snake_case used in JSON request/response bodies. Jackson's `@JsonNaming` (which
> produces snake_case) only applies to `@RequestBody`/`@ResponseBody`; `@ModelAttribute`/`@ParameterObject`
> query-string binding goes through Spring's plain `DataBinder` instead, which matches the exact property name.

| Parameter                    | Type    | Default         | Constraints                                                                                                       | Description                                                                               |
|--------------------------------|---------|-----------------|-----------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| `resortRoomFacilityGroupId`  | Long    | —               | —                                                                                                                    | Filter by exact resort room facility group id                                                  |
| `facilityId`                 | Long    | —               | —                                                                                                                    | Filter by exact linked platform facility id                                                    |
| `code`                        | String  | —               | —                                                                                                                    | Filter by code (partial, case-insensitive)                                                     |
| `isHighlighted`               | Boolean | —               | —                                                                                                                    | Filter by exact highlighted flag                                                                |
| `name`                         | String  | —               | —                                                                                                                    | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale       |
| `page`                         | int     | `0`             | >= 0                                                                                                                  | Zero-based page index                                                                             |
| `size`                          | int     | `10`            | 1 – 50                                                                                                                | Number of items per page                                                                           |
| `sortBy`                       | String  | `id` (implicit) | `createdAt`, `resortRoomFacilityGroupEntity.id`, `facilityEntity.id`, `code`, `isHighlighted`, `name` (`id` NOT selectable) | Field to sort by                                                                                    |
| `sortDir`                      | String  | `ASC`           | `ASC`, `DESC`                                                                                                        | Sort direction                                                                                       |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "ROOM_SPA",
      "sort_order": 1,
      "is_highlighted": false,
      "icon_type": "LUCIDE",
      "icon_value": "Sparkles",
      "icon_meta": {
        "size": 24,
        "color": "#a855f7",
        "stroke_width": 1.5
      },
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "In-Room Spa Treatment",
        "description": "Private spa treatments delivered directly to the room.",
        "notes": "Book at least 24 hours in advance.",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "PRIVATE_POOL",
      "sort_order": 2,
      "is_highlighted": true,
      "icon_type": "LUCIDE",
      "icon_value": "Waves",
      "icon_meta": {
        "size": 24,
        "color": "#06b6d4"
      },
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Private Plunge Pool",
        "description": "Dedicated plunge pool on the room's terrace.",
        "notes": "",
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
    "resortRoomFacilityGroupEntity.id",
    "facilityEntity.id",
    "code",
    "isHighlighted",
    "name"
  ],
  "searchable_fields": [
    "code",
    "name"
  ]
}
```

---

## Update Resort Room Facility

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{id}`

Updates `sort_order`, `is_highlighted`, `icon_type`, `icon_value`, and `icon_meta`.
`resort_room_facility_group_id`, `facility_id`, and `code` are set at creation and cannot be changed — to move
a facility to a different group, base it on a different platform facility, or change its code, delete it and
create a new one instead. Locale translations are managed separately via the Resort Room Facility Locales
sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter                | Type | Description                            |
|---------------------------|------|-------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                    |
| `resort-room-category-id` | Long | ID of the owning resort room category      |
| `resort-room-id`          | Long | ID of the owning resort room               |
| `id`                      | Long | ID of the resort room facility             |

### Request Body

```json
{
  "sort_order": 2,
  "is_highlighted": true,
  "icon_type": "LUCIDE",
  "icon_value": "Sparkles",
  "icon_meta": {
    "size": 24,
    "color": "#a855f7",
    "stroke_width": 1.5
  }
}
```

### Request Fields

| Field             | Type    | Required | Validation               |
|--------------------|---------|----------|----------------------------|
| `sort_order`      | Integer | Yes      | Not null                 |
| `is_highlighted`  | Boolean | Yes      | Not null                 |
| `icon_type`       | String  | —        | Nullable, max 100 chars  |
| `icon_value`      | String  | —        | Nullable                 |
| `icon_meta`       | Object  | —        | Nullable, arbitrary JSON |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Resort Room Facility

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{id}`

Soft-deletes the resort room facility. The record is not removed from the database but will no longer appear
in any response.

### Path Parameters

| Parameter                | Type | Description                            |
|---------------------------|------|-------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                    |
| `resort-room-category-id` | Long | ID of the owning resort room category      |
| `resort-room-id`          | Long | ID of the owning resort room               |
| `id`                      | Long | ID of the resort room facility             |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Resort Room Facility Locales

Resort Room Facility Locale endpoints manage locale-specific name/description/notes translations for a resort
room facility. The `{resort-id}`/`{resort-room-category-id}`/`{resort-room-id}`/`{resort-room-facility-id}`
path parameters must reference an existing, active resort, resort room category, resort room, and resort room
facility respectively (a `resort-room-facility-id` belonging to a different resort room behaves the same as an
unknown one — `404 ENTITY_NOT_FOUND`).

---

### List Resort Room Facility Locales

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales`

Returns a paginated list of every locale translation belonging to a resort room facility — this is the only
way to see more than the single Accept-Language-matched translation returned by
[Get Resort Room Facility](#get-resort-room-facility) and
[List / Search Resort Room Facilities](#list--search-resort-room-facilities). Optionally filtered to locales
whose `code` contains a given substring.

#### Path Parameters

| Parameter                    | Type | Description                                   |
|---------------------------------|------|----------------------------------------------------|
| `resort-id`                   | Long | ID of the owning resort                              |
| `resort-room-category-id`     | Long | ID of the owning resort room category                |
| `resort-room-id`              | Long | ID of the owning resort room                         |
| `resort-room-facility-id`     | Long | ID of the parent resort room facility                |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|---------------|--------|---------|-------------|-----------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn` |
| `page`       | int    | `0`     | >= 0        | Zero-based page index                                                                             |
| `size`       | int    | `10`    | 1 – 50      | Number of items per page                                                                          |

> **Note:** `sortBy`/`sortDir` are accepted on the request object but there are no sortable fields registered
> for this endpoint — passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default (sorted by
> `id` ascending).

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
      "name": "In-Room Spa Treatment",
      "description": "Private spa treatments delivered directly to the room.",
      "notes": "Book at least 24 hours in advance.",
      "sort_order": 1
    },
    {
      "id": 2,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "রুম স্পা ট্রিটমেন্ট",
      "description": "",
      "notes": "",
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

### Count Resort Room Facility Locales

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales/count`

Returns how many active, non-deleted platform [Locale](locales-api.md) codes this resort room facility already
has an active translation for, together with each one's `code`. Matched via `locale_id`. `count` is always
`codes.length`. Use this to gray out/disable locales already present in `codes` when building the picker for
[Create Resort Room Facility Locale](#create-resort-room-facility-locale) — `locale_id` must not already have a
translation for this resort room facility, or the create call returns `409 CONFLICT`.

#### Path Parameters

| Parameter                    | Type | Description                                   |
|---------------------------------|------|----------------------------------------------------|
| `resort-id`                   | Long | ID of the owning resort                              |
| `resort-room-category-id`     | Long | ID of the owning resort room category                |
| `resort-room-id`              | Long | ID of the owning resort room                         |
| `resort-room-facility-id`     | Long | ID of the parent resort room facility                |

#### Response `200 OK`

```json
{
  "count": 2,
  "codes": [
    "en",
    "bn"
  ]
}
```

---

### Create Resort Room Facility Locale

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales`

Adds a new locale translation to an existing resort room facility. `locale_id` must reference an existing,
active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of resort room facility
and locale must be unique — adding a locale the facility already has a translation for returns `409 CONFLICT`,
pre-checked at the application level before any write (backed by a DB-level unique constraint on
`(resort_room_facility_id, locale_id)` as a last-resort guard).

#### Path Parameters

| Parameter                    | Type | Description                                   |
|---------------------------------|------|----------------------------------------------------|
| `resort-id`                   | Long | ID of the owning resort                              |
| `resort-room-category-id`     | Long | ID of the owning resort room category                |
| `resort-room-id`              | Long | ID of the owning resort room                         |
| `resort-room-facility-id`     | Long | ID of the parent resort room facility                |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "রুম স্পা ট্রিটমেন্ট",
  "description": "রুমে সরাসরি সরবরাহ করা ব্যক্তিগত স্পা ট্রিটমেন্ট।",
  "notes": "কমপক্ষে ২৪ ঘণ্টা আগে বুক করুন।",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|------------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale |
| `name`        | String  | Yes      | Not blank, max 255 chars                    |
| `description` | String  | —        | —                                            |
| `notes`       | String  | —        | —                                            |
| `sort_order`  | Integer | Yes      | Not null                                    |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 2
}
```

---

### Update Resort Room Facility Locale

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales/{id}`

Updates `name`, `description`, `notes`, and `sort_order` for an existing resort room facility locale
translation. The associated resort room facility and locale cannot be changed after creation.

#### Path Parameters

| Parameter                    | Type | Description                                   |
|---------------------------------|------|----------------------------------------------------|
| `resort-id`                   | Long | ID of the owning resort                              |
| `resort-room-category-id`     | Long | ID of the owning resort room category                |
| `resort-room-id`              | Long | ID of the owning resort room                         |
| `resort-room-facility-id`     | Long | ID of the parent resort room facility                |
| `id`                          | Long | ID of the resort room facility locale                 |

#### Request Body

```json
{
  "name": "রুম স্পা ট্রিটমেন্ট",
  "description": "রুমে সরাসরি সরবরাহ করা ব্যক্তিগত স্পা ট্রিটমেন্ট।",
  "notes": "কমপক্ষে ২৪ ঘণ্টা আগে বুক করুন।",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|----------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | —        | —                         |
| `notes`       | String  | —        | —                         |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

### Delete Resort Room Facility Locale

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facilities/{resort-room-facility-id}/locales/{id}`

Soft-deletes a resort room facility locale. The record is not removed from the database but will no longer
appear in any response.

#### Path Parameters

| Parameter                    | Type | Description                                   |
|---------------------------------|------|----------------------------------------------------|
| `resort-id`                   | Long | ID of the owning resort                              |
| `resort-room-category-id`     | Long | ID of the owning resort room category                |
| `resort-room-id`              | Long | ID of the owning resort room                         |
| `resort-room-facility-id`     | Long | ID of the parent resort room facility                |
| `id`                          | Long | ID of the resort room facility locale                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
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
  "message": "ResortRoomFacility not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|-------------|-----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; an unsupported `sortBy` query value                                                                                                                                                                                                                                                                                                              |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found; resort room category not found; resort room not found; resort room facility not found for the given `resort-room-id`/`id` pair (including an `id` that belongs to a different resort room); the resort room facility group referenced by `resort_room_facility_group_id` not found (or belongs to a different resort room) (`create` only); the platform facility referenced by `facility_id` not found; resort room facility locale not found; the locale referenced by `locale_id` not found |
| 409         | `CONFLICT`                 | The resort room already has an active facility linked to the given `facility_id` (`create`, application-level check only — no DB constraint backs it); the resort room already has a facility with the given `code` (`create`, application-level check); the resort room facility already has a translation for the given `locale_id` (`create` locale, pre-checked at the application level)                                                                                                 |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `(resort_room_facility_id, locale_id)`, should not normally be reachable now that the duplicate is pre-checked at the application level; a duplicate `code` within the same resort room (backed by the `(resort_room_id, code)` unique index — not pre-checked at the application level, though the application-level check above should normally catch it first)                                                                                    |
