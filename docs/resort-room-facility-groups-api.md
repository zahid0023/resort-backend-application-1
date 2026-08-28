# Resort Room Facility Groups API

Base URL: `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups`

A resort room facility group is a resort-room-scoped grouping of amenities (dining, wellness, recreation, ...)
— the same concept as a [Resort Room Category Facility Group](resort-room-category-facility-groups-api.md), one
level down the hierarchy (per individual room instead of per room category). At creation time, the owner
either:

- **picks a platform facility group** (see [facility-groups-api.md](facility-groups-api.md)) via
  `facility_group_id` — this is purely a creation-time convenience so the owner can click an existing platform
  group instead of typing everything from scratch; its icon fields are copied onto the new resort room facility
  group as starting values (still overridable via `icon_type`/`icon_value`/`icon_meta`), or
- **creates a fully custom group** — omits `facility_group_id`, and supplies `icon_type`/`icon_value`/
  `icon_meta`/the locale's `name`/`description` as the group's own values.

**`facility_group_id` is set at creation and immutable afterward** — it is accepted on
[Create](#create-resort-room-facility-group) and echoed back read-only on every response afterward (`null` for
a fully custom group), so a client can use it to look up the linked platform group's own facilities via
`GET /api/v1/facilities?facilityGroupId={facility_group_id}`. To base a group on a different platform facility
group, delete it and create a new one. A resort room may link to the same platform facility group at most once
at creation time (see [Create Resort Room Facility Group](#create-resort-room-facility-group) below, enforced
server-side); this constraint does not apply to custom groups.

Resort room facility groups are always reached nested under their owning resort, resort room category, and
resort room; there is no top-level `/api/v1/resort-room-facility-groups` route. `GET`/`PUT`/`DELETE` by `id`
are scoped to the `{resort-room-id}` in the path — an `id` that exists but belongs to a different resort room
returns `404 ENTITY_NOT_FOUND`, the same as an unknown `id`, since a resort room facility group has no meaning
outside its owning resort room.

A resort room facility group's display name/description are locale-specific and managed through a companion
sub-resource — Resort Room Facility Group Locales — reached via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales`.
All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is actually
used to shape the response:

- **`GET /{id}` (Get Resort Room Facility Group)** and **`GET` (List/Search Resort Room Facility Groups)** —
  the header's value selects exactly one locale translation for the `locale` field: an exact match if one
  exists, otherwise `en`, otherwise `null`.
- **`GET .../locales` (List Resort Room Facility Group Locales)** — the header must be present, but its value
  has no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a single
  Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                                                                                                                       | Description                                     |
|--------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups`                                              | Create a resort room facility group             |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups`                                              | List / search a resort room's facility groups   |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{id}`                                         | Get a resort room facility group                |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{id}`                                         | Update a resort room facility group             |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{id}`                                         | Delete a resort room facility group             |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales`                  | List a resort room facility group's locales     |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales/count`            | Count a resort room facility group's used platform locales |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales`                  | Create a resort room facility group locale      |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales/{id}`             | Update a resort room facility group locale      |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales/{id}`             | Delete a resort room facility group locale      |

---

## Data Model

### ResortRoomFacilityGroup

| Field               | Type    | Required | Constraints                                                                                                                      | Description                                                                                                                                                                                                                                                                          |
|---------------------|---------|----------|-----------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`                | Long    | —        | read-only; never null                                                                                                            | Auto-generated identifier of this resort room facility group itself                                                                                                                                                                                                                 |
| `facility_group_id` | Long    | —        | read-only; set at creation, immutable; null when this facility group is custom (omitted from the response entirely in that case) | The linked **platform** facility group's id — present only when this facility group was created from a platform group, `null`/absent for a fully custom group; use it to fetch that platform group's own facilities via `GET /api/v1/facilities?facilityGroupId={facility_group_id}` |
| `code`              | String  | Yes      | not blank, max 100 chars, immutable                                                                                              | Resort-room-scoped identifier, unique per resort room                                                                                                                                                                                                                                |
| `sort_order`        | Integer | Yes      | default 1                                                                                                                        | Display order among the room's facility groups                                                                                                                                                                                                                                       |
| `icon_type`         | String  | —        | nullable, max 100 chars                                                                                                          | Icon system identifier (e.g. `LUCIDE`); copied from the platform group at creation if linked, then independently editable                                                                                                                                                           |
| `icon_value`        | String  | —        | nullable                                                                                                                         | Icon value (e.g. an icon name); copied from the platform group at creation if linked, then independently editable                                                                                                                                                                   |
| `icon_meta`         | Object  | —        | nullable, arbitrary JSON                                                                                                         | Icon metadata (size, color, stroke width, ...); copied from the platform group at creation if linked, then independently editable                                                                                                                                                   |
| `locale`            | Object  | —        | nullable; see ResortRoomFacilityGroupLocale below                                                                                | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the group has no translations at all)                                                                                                                                           |

**Uniqueness:** when `facility_group_id` is supplied at creation, the combination of `(resort_room,
facility_group)` must be unique among active records — creating a duplicate returns `409 CONFLICT` (see
[Create Resort Room Facility Group](#create-resort-room-facility-group)). No such constraint applies to custom
groups. `code` must be unique within its owning resort room (a DB-level unique index on
`(resort_room_id, code)`); different resort rooms may reuse the same `code`.

### ResortRoomFacilityGroupLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|---------------------------------------------------|----------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 255 chars                                    | Localized group name (override if linked, actual name if custom)               |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized group description (override if linked, actual description if custom) |
| `sort_order`  | Integer | Yes      | default 1                                        | Display order among locale entries                                             |

---

## Create Resort Room Facility Group

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups`

Creates a new facility group for the resort room, together with exactly **one** initial locale translation.
`facility_group_id` is optional — supply it to base the new group on a platform facility group (its icon
fields are copied onto the new group as starting values), or omit/`null` it to create a fully custom group.
When `facility_group_id` is supplied, it must reference an existing, active platform facility group, and the
resort room must not already have an active facility group linked to that same platform group — attempting to
link a second time returns `409 CONFLICT`. `facility_group_id` is set at creation and immutable afterward — it
is echoed back read-only on every response afterward (`null`/absent for a custom group) — see
[Data Model](#data-model) above.

**The initial translation is always attached to the `en` locale, resolved by the server — the request carries
no `locale_id` at all.** There is no option to submit multiple locales at creation time. Additional languages
are added afterward via the Resort Room Facility Group Locales sub-resource below.

### Path Parameters

| Parameter                | Type | Description                            |
|---------------------------|------|-------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                    |
| `resort-room-category-id` | Long | ID of the owning resort room category      |
| `resort-room-id`          | Long | ID of the owning resort room               |

### Request Body

```json
{
  "facility_group_id": 1,
  "code": "DINING",
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "UtensilsCrossed",
  "icon_meta": {
    "size": 24,
    "color": "#f59e0b",
    "stroke_width": 1.5
  },
  "locale": {
    "name": "Dining",
    "description": "All food and beverage outlets available in this room.",
    "sort_order": 1
  }
}
```

### Request Fields

| Field               | Type    | Required | Validation                                                                                  |
|---------------------|---------|----------|-----------------------------------------------------------------------------------------------|
| `facility_group_id` | Long    | —        | Nullable; if present, must reference an existing, active facility group                       |
| `code`              | String  | Yes      | Not blank, max 100 chars; must be unique within this resort room; immutable after creation     |
| `sort_order`        | Integer | Yes      | Not null                                                                                       |
| `icon_type`         | String  | —        | Nullable, max 100 chars                                                                        |
| `icon_value`        | String  | —        | Nullable                                                                                        |
| `icon_meta`         | Object  | —        | Nullable, arbitrary JSON                                                                        |
| `locale`            | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale     |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | —        | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Resort Room Facility Group

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{id}`

Returns a single active resort room facility group, scoped to its owning resort room — an `id` that exists but
belongs to a different resort room returns `404 ENTITY_NOT_FOUND`, the same as an unknown `id`. `locale` is the
one translation matching the request's `Accept-Language` header (falls back to `en`, then `null` if the group
has no translations at all). To fetch every translation, use
[List Resort Room Facility Group Locales](#list-resort-room-facility-group-locales) below.

### Path Parameters

| Parameter                | Type | Description                            |
|---------------------------|------|-------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                    |
| `resort-room-category-id` | Long | ID of the owning resort room category      |
| `resort-room-id`          | Long | ID of the owning resort room               |
| `id`                      | Long | ID of the resort room facility group       |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "facility_group_id": 1,
    "code": "DINING",
    "sort_order": 1,
    "icon_type": "LUCIDE",
    "icon_value": "UtensilsCrossed",
    "icon_meta": {
      "size": 24,
      "color": "#f59e0b",
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
      "name": "Dining",
      "description": "All food and beverage outlets available in this room.",
      "sort_order": 1
    }
  }
}
```

---

## List / Search Resort Room Facility Groups

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups`

Returns a paginated, filterable list of a resort room's active (non-deleted) facility groups. All filter
parameters are optional; omitting them returns every facility group belonging to the resort room. Multiple
filters are combined with AND. `name` and `code` perform a case-insensitive partial match (`code` against the
raw column, `name` against the resolved locale); `facilityGroupId` is an exact match. `Accept-Language` selects
each row's `locale` field the same way as `GET /{id}`.

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

> **Note:** Query parameters bind directly onto `ResortRoomFacilityGroupFilterRequest`'s Java field names, so
> they are **camelCase** — not the snake_case used in JSON request/response bodies. Jackson's `@JsonNaming`
> (which produces snake_case) only applies to `@RequestBody`/`@ResponseBody`; `@ModelAttribute`/
> `@ParameterObject` query-string binding goes through Spring's plain `DataBinder` instead, which matches the
> exact property name.

| Parameter         | Type   | Default         | Constraints                                                                 | Description                                                                               |
|-------------------|--------|-----------------|-------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| `facilityGroupId` | Long   | —               | —                                                                             | Filter by exact linked platform facility group id                                           |
| `code`            | String | —               | —                                                                             | Filter by code (partial, case-insensitive)                                                  |
| `name`            | String | —               | —                                                                             | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale   |
| `page`            | int    | `0`             | >= 0                                                                          | Zero-based page index                                                                        |
| `size`            | int    | `10`            | 1 – 50                                                                        | Number of items per page                                                                      |
| `sortBy`          | String | `id` (implicit) | `createdAt`, `facilityGroupEntity.id`, `code`, `name` (`id` NOT selectable)   | Field to sort by                                                                               |
| `sortDir`         | String | `ASC`           | `ASC`, `DESC`                                                                 | Sort direction                                                                                  |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "facility_group_id": 1,
      "code": "DINING",
      "sort_order": 1,
      "icon_type": "LUCIDE",
      "icon_value": "UtensilsCrossed",
      "icon_meta": {
        "size": 24,
        "color": "#f59e0b",
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
        "name": "Dining",
        "description": "All food and beverage outlets available in this room.",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "SPA",
      "sort_order": 2,
      "icon_type": "LUCIDE",
      "icon_value": "Sparkles",
      "icon_meta": {
        "size": 24,
        "color": "#a855f7"
      },
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "In-Room Spa",
        "description": "Spa treatments available in this room.",
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
    "facilityGroupEntity.id",
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

## Update Resort Room Facility Group

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{id}`

Updates `sort_order`, `icon_type`, `icon_value`, and `icon_meta`. `facility_group_id` and `code` are set at
creation and cannot be changed — to base a group on a different platform facility group or change its code,
delete it and create a new one instead. Locale translations are managed separately via the Resort Room
Facility Group Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter                | Type | Description                            |
|---------------------------|------|-------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                    |
| `resort-room-category-id` | Long | ID of the owning resort room category      |
| `resort-room-id`          | Long | ID of the owning resort room               |
| `id`                      | Long | ID of the resort room facility group       |

### Request Body

```json
{
  "sort_order": 2,
  "icon_type": "LUCIDE",
  "icon_value": "Coffee",
  "icon_meta": {
    "size": 24,
    "color": "#f59e0b",
    "stroke_width": 1.5
  }
}
```

### Request Fields

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `sort_order` | Integer | Yes      | Not null                 |
| `icon_type`  | String  | —        | Nullable, max 100 chars  |
| `icon_value` | String  | —        | Nullable                 |
| `icon_meta`  | Object  | —        | Nullable, arbitrary JSON |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Resort Room Facility Group

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{id}`

Soft-deletes the resort room facility group. The record is not removed from the database but will no longer
appear in any response.

### Path Parameters

| Parameter                | Type | Description                            |
|---------------------------|------|-------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                    |
| `resort-room-category-id` | Long | ID of the owning resort room category      |
| `resort-room-id`          | Long | ID of the owning resort room               |
| `id`                      | Long | ID of the resort room facility group       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Resort Room Facility Group Locales

Resort Room Facility Group Locale endpoints manage locale-specific name/description translations for a resort
room facility group. The `{resort-id}`/`{resort-room-category-id}`/`{resort-room-id}`/`{resort-facility-group-id}`
path parameters must reference an existing, active resort, resort room category, resort room, and resort room
facility group respectively (a `facility-group-id` belonging to a different resort room behaves the same as an
unknown one — `404 ENTITY_NOT_FOUND`).

---

### List Resort Room Facility Group Locales

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales`

Returns a paginated list of every locale translation belonging to a resort room facility group — this is the
only way to see more than the single Accept-Language-matched translation returned by
[Get Resort Room Facility Group](#get-resort-room-facility-group) and
[List / Search Resort Room Facility Groups](#list--search-resort-room-facility-groups). Optionally filtered to
locales whose `code` contains a given substring.

#### Path Parameters

| Parameter                | Type | Description                                     |
|---------------------------|------|------------------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                                |
| `resort-room-category-id` | Long | ID of the owning resort room category                  |
| `resort-room-id`          | Long | ID of the owning resort room                           |
| `facility-group-id`       | Long | ID of the parent resort room facility group             |

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
      "name": "Dining",
      "description": "All food and beverage outlets available in this room.",
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
      "name": "খাবার",
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

### Count Resort Room Facility Group Locales

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales/count`

Returns how many active, non-deleted platform [Locale](locales-api.md) codes this resort room facility group
already has an active translation for, together with each one's `code`. Matched via `locale_id`. `count` is
always `codes.length`. Use this to gray out/disable locales already present in `codes` when building the
picker for [Create Resort Room Facility Group Locale](#create-resort-room-facility-group-locale) — `locale_id`
must not already have a translation for this resort room facility group, or the create call returns
`409 CONFLICT`.

#### Path Parameters

| Parameter                | Type | Description                                     |
|---------------------------|------|------------------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                                |
| `resort-room-category-id` | Long | ID of the owning resort room category                  |
| `resort-room-id`          | Long | ID of the owning resort room                           |
| `facility-group-id`       | Long | ID of the parent resort room facility group             |

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

### Create Resort Room Facility Group Locale

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales`

Adds a new locale translation to an existing resort room facility group. `locale_id` must reference an
existing, active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of resort
room facility group and locale must be unique — adding a locale the group already has a translation for
returns `409 CONFLICT`, pre-checked at the application level before any write (backed by a DB-level unique
constraint on `(resort_room_facility_group_id, locale_id)` as a last-resort guard).

#### Path Parameters

| Parameter                | Type | Description                                     |
|---------------------------|------|------------------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                                |
| `resort-room-category-id` | Long | ID of the owning resort room category                  |
| `resort-room-id`          | Long | ID of the owning resort room                           |
| `facility-group-id`       | Long | ID of the parent resort room facility group             |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "খাবার",
  "description": "এই রুমের জন্য উপলব্ধ সমস্ত খাবার এবং পানীয় আউটলেট।",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|----------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale |
| `name`        | String  | Yes      | Not blank, max 255 chars                    |
| `description` | String  | —        | —                                            |
| `sort_order`  | Integer | Yes      | Not null                                    |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 2
}
```

---

### Update Resort Room Facility Group Locale

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing resort room facility group locale translation.
The associated resort room facility group and locale cannot be changed after creation.

#### Path Parameters

| Parameter                | Type | Description                                     |
|---------------------------|------|------------------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                                |
| `resort-room-category-id` | Long | ID of the owning resort room category                  |
| `resort-room-id`          | Long | ID of the owning resort room                           |
| `facility-group-id`       | Long | ID of the parent resort room facility group             |
| `id`                      | Long | ID of the resort room facility group locale             |

#### Request Body

```json
{
  "name": "খাবার",
  "description": "এই রুমের জন্য উপলব্ধ সমস্ত খাবার এবং পানীয় আউটলেট।",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | —        | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

### Delete Resort Room Facility Group Locale

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/facility-groups/{resort-facility-group-id}/locales/{id}`

Soft-deletes a resort room facility group locale. The record is not removed from the database but will no
longer appear in any response.

#### Path Parameters

| Parameter                | Type | Description                                     |
|---------------------------|------|------------------------------------------------------|
| `resort-id`               | Long | ID of the owning resort                                |
| `resort-room-category-id` | Long | ID of the owning resort room category                  |
| `resort-room-id`          | Long | ID of the owning resort room                           |
| `facility-group-id`       | Long | ID of the parent resort room facility group             |
| `id`                      | Long | ID of the resort room facility group locale             |

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
  "message": "ResortRoomFacilityGroup not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                              |
|-------------|-----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; an unsupported `sortBy` query value                                                                                                                                                                                |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found; resort room category not found; resort room not found; resort room facility group not found for the given `resort-room-id`/`id` pair (including an `id` that belongs to a different resort room); the platform facility group referenced by `facility_group_id` not found; resort room facility group locale not found; the locale referenced by `locale_id` not found |
| 409         | `CONFLICT`                 | The resort room already has an active facility group linked to the given `facility_group_id` (`create`); the resort room already has a facility group with the given `code` (`create`, application-level check); the resort room facility group already has a translation for the given `locale_id` (`create` locale, pre-checked at the application level)     |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraints on `(resort_room_facility_group_id, locale_id)` or the partial unique index on `(resort_room_id, facility_group_id)`/`(resort_room_id, code)`, should not normally be reachable now that duplicates are pre-checked at the application level                                                                            |
