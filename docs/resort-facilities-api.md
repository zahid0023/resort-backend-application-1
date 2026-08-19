# Resort Facilities API

Base URL: `/api/v1/resorts/{resort-id}/facilities`

A resort facility is a resort-scoped amenity (restaurant, pool, spa, ...). Every resort facility belongs to
exactly one [Resort Facility Group](resort-facility-groups-api.md) — `resort_facility_group_id` is required at
creation and immutable afterward (to move a facility to a different group, delete it and create a new one). At
creation time, the owner either:

- **picks a platform facility** (see [facilities-api.md](facilities-api.md)) via `facility_id` — this is purely
  a creation-time convenience so the owner can click an existing platform facility instead of typing everything
  from scratch; its icon fields are copied onto the new resort facility as starting values (still overridable
  via `icon_type`/`icon_value`/`icon_meta`), or
- **creates a fully custom facility** — omits `facility_id`, and supplies `icon_type`/`icon_value`/`icon_meta`/
  the locale's `name`/`description` as the facility's own values.

**`facility_id` is write-only** — it is accepted on [Create](#create-resort-facility) but never returned in any
response; a resort facility's own copied/overridden `icon_type`/`icon_value`/`icon_meta` and locale
`name`/`description` are all a client needs afterward; there is no ongoing link back to the platform facility.
It is set at creation and immutable afterward — to base a facility on a different platform facility, delete it
and create a new one. A resort may link to the same platform facility at most once — attempting to link a
second time returns `409 CONFLICT` (see [Create Resort Facility](#create-resort-facility) below); this
constraint does not apply to custom facilities, and (unlike [Resort Facility Groups](resort-facility-groups-api.md))
has no backing database constraint, only an application-level check.

Resort facilities are always reached nested under their owning resort; there is no top-level
`/api/v1/resort-facilities` route. `GET`/`PUT`/`DELETE` by `id` are scoped to the `{resort-id}` in the path —
an `id` that exists but belongs to a different resort returns `404 ENTITY_NOT_FOUND`, the same as an unknown
`id`, since a resort facility has no meaning outside its owning resort.

A resort facility's display name/description are locale-specific and managed through a companion sub-resource
— Resort Facility Locales — reached via `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales`.
All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is actually
used to shape the response:

- **`GET /{id}` (Get Resort Facility)** and **`GET` (List/Search Resort Facilities)** — the header's value
  selects exactly one locale translation for the `locale` field: an exact match if one exists, otherwise `en`,
  otherwise `null`.
- **`GET .../locales` (List Resort Facility Locales)** — the header must be present, but its value has no
  effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a single
  Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                                       | Description                         |
|--------|----------------------------------------------------------------------------|-------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/facilities`                                   | Create a resort facility            |
| GET    | `/api/v1/resorts/{resort-id}/facilities`                                   | List / search a resort's facilities |
| GET    | `/api/v1/resorts/{resort-id}/facilities/{id}`                              | Get a resort facility               |
| PUT    | `/api/v1/resorts/{resort-id}/facilities/{id}`                              | Update a resort facility            |
| DELETE | `/api/v1/resorts/{resort-id}/facilities/{id}`                              | Delete a resort facility            |
| GET    | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales`      | List a resort facility's locales    |
| POST   | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales`      | Create a resort facility locale     |
| PUT    | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales/{id}` | Update a resort facility locale     |
| DELETE | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales/{id}` | Delete a resort facility locale     |

---

## Data Model

### ResortFacility

| Field            | Type    | Required | Constraints                              | Description                                                                                                                                  |
|------------------|---------|----------|------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| `id`             | Long    | —        | read-only                                | Auto-generated identifier                                                                                                                    |
| `code`           | String  | Yes      | not blank, max 100 chars, immutable      | Resort-scoped identifier, unique per resort                                                                                                  |
| `sort_order`     | Integer | Yes      | default 1                                | Display order among the resort's facilities                                                                                                  |
| `is_highlighted` | Boolean | Yes      | default `false`                          | Marks the facility as featured/promoted                                                                                                      |
| `icon_type`      | String  | —        | nullable, max 100 chars                  | Icon system identifier (e.g. `LUCIDE`); copied from the platform facility at creation if linked, then independently editable                 |
| `icon_value`     | String  | —        | nullable                                 | Icon value (e.g. an icon name); copied from the platform facility at creation if linked, then independently editable                         |
| `icon_meta`      | Object  | —        | nullable, arbitrary JSON                 | Icon metadata (size, color, stroke width, ...); copied from the platform facility at creation if linked, then independently editable         |
| `locale`         | Object  | —        | nullable; see ResortFacilityLocale below | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the facility has no translations at all) |

> **Note:** `resort_facility_group_id` (the owning resort facility group, required) and `facility_id` (the
> platform facility picked at creation, if any) are write-only inputs — see
> [Create Resort Facility](#create-resort-facility) — and do not appear on this data model because they are
> never returned in any response.

**Uniqueness:** when `facility_id` is supplied at creation, the combination of `(resort, facility)` must be
unique among active records — creating a duplicate returns `409 CONFLICT` (see
[Create Resort Facility](#create-resort-facility)). This is checked at the application level only — there is
no database-level unique constraint backing it. No such constraint applies to custom facilities. `code` must be
unique within its owning resort (a DB-level unique index on `(resort_id, code)`); different resorts may reuse
the same `code`.

### ResortFacilityLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                       |
|---------------|---------|----------|--------------------------------------------------|-----------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                         |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`)    |
| `name`        | String  | Yes      | max 255 chars                                    | Localized facility name (override if linked, actual name if custom)               |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized facility description (override if linked, actual description if custom) |
| `notes`       | String  | —        | not null (defaults to `""`)                      | Free-form notes about the facility (e.g. internal remarks, guest-facing caveats)  |
| `sort_order`  | Integer | Yes      | default 1                                        | Display order among locale entries                                                |

---

## Create Resort Facility

`POST /api/v1/resorts/{resort-id}/facilities`

Creates a new facility for the resort, together with exactly **one** initial locale translation and its
**entire weekly operating-hours schedule**.
`resort_facility_group_id` is required and must reference an existing, active resort facility group belonging
to the same resort — an unknown or cross-resort group id returns `404 ENTITY_NOT_FOUND`. `facility_id` is
optional — supply it to base the new facility on a platform facility (its icon fields are copied onto the new
facility as starting values), or omit/`null` it to create a fully custom facility. When `facility_id` is
supplied, it must reference an existing, active platform facility, and the resort must not already have an
active facility linked to that same platform facility — attempting to link a second time returns
`409 CONFLICT`. **`resort_facility_group_id` and `facility_id` are write-only and are not echoed back in the
response** — see the note in [Data Model](#data-model) above.

**The initial translation is always attached to the `en` locale, resolved by the server — the request carries
no `locale_id` at all.** There is no option to submit multiple locales at creation time. Additional languages
are added afterward via the Resort Facility Locales sub-resource below.

**`operating_hours` is required and must cover every active day of week exactly once** — same shape and
validation as [Set Weekly Schedule](resort-facility-operating-hours-api.md#set-weekly-schedule) on the
[Resort Facility Operating Hours API](resort-facility-operating-hours-api.md): a facility is never created with
a partial schedule. Each entry is `CLOSED` (`is_closed=true`), `OPEN_24_HOURS` (`is_twenty_four_hours=true`), or
one-or-more custom `windows` (a day with a break, e.g. lunch/dinner). Same-day and cross-day overlap validation
(including overnight windows spilling into the next day, wrapping Sunday back to Monday) runs exactly as
described there — see that document for the full rule set and worked examples. Any violation aborts the whole
`POST` before the facility itself is created — `400 INVALID_ARGUMENT` for shape/completeness errors,
`409 CONFLICT` for overlap errors, `404 ENTITY_NOT_FOUND` for an unknown `day_of_week_id`. The created rows are
not returned from this endpoint — fetch them afterward via `GET .../facilities/{id}/operating-hours`.

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

### Request Body

```json
{
  "resort_facility_group_id": 1,
  "facility_id": 1,
  "code": "RESTAURANT",
  "sort_order": 1,
  "is_highlighted": false,
  "icon_type": "LUCIDE",
  "icon_value": "UtensilsCrossed",
  "icon_meta": {
    "size": 24,
    "color": "#f59e0b",
    "stroke_width": 1.5
  },
  "locale": {
    "name": "Main Restaurant",
    "description": "Full-service restaurant with buffet and à la carte options.",
    "notes": "Reservations recommended on weekends.",
    "sort_order": 1
  },
  "operating_hours": [
    {
      "day_of_week_id": 1,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "09:00:00",
          "closes_at": "14:00:00"
        },
        {
          "opens_at": "17:00:00",
          "closes_at": "23:00:00"
        }
      ]
    },
    {
      "day_of_week_id": 2,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "09:00:00",
          "closes_at": "23:00:00"
        }
      ]
    },
    {
      "day_of_week_id": 3,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "09:00:00",
          "closes_at": "23:00:00"
        }
      ]
    },
    {
      "day_of_week_id": 4,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "09:00:00",
          "closes_at": "23:00:00"
        }
      ]
    },
    {
      "day_of_week_id": 5,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "09:00:00",
          "closes_at": "23:00:00"
        }
      ]
    },
    {
      "day_of_week_id": 6,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "09:00:00",
          "closes_at": "23:00:00"
        }
      ]
    },
    {
      "day_of_week_id": 7,
      "is_closed": true,
      "is_twenty_four_hours": false,
      "windows": []
    }
  ]
}
```

### Request Fields

| Field                      | Type    | Required | Validation                                                                                     |
|----------------------------|---------|----------|------------------------------------------------------------------------------------------------|
| `resort_facility_group_id` | Long    | Yes      | Not null; must reference an existing, active resort facility group belonging to this resort    |
| `facility_id`              | Long    | —        | Nullable; if present, must reference an existing, active facility                              |
| `code`                     | String  | Yes      | Not blank, max 100 chars; must be unique within this resort; immutable after creation          |
| `sort_order`               | Integer | Yes      | Not null                                                                                       |
| `is_highlighted`           | Boolean | Yes      | Not null                                                                                       |
| `icon_type`                | String  | —        | Nullable, max 100 chars                                                                        |
| `icon_value`               | String  | —        | Nullable                                                                                       |
| `icon_meta`                | Object  | —        | Nullable, arbitrary JSON                                                                       |
| `locale`                   | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale     |
| `operating_hours`          | Array   | Yes      | Not empty; exactly one entry per active day of week, no duplicates, no unknown ids — see below |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | —        | —                        |
| `notes`       | String  | —        | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

**Operating hours entries (`operating_hours[]`)** — identical shape to [Set Weekly
Schedule](resort-facility-operating-hours-api.md#set-weekly-schedule)'s `days[]`:

| Field                                    | Type    | Required | Validation                                                                                                                |
|------------------------------------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------|
| `operating_hours[].day_of_week_id`       | Long    | Yes      | Not null; must reference an existing, active day of week                                                                  |
| `operating_hours[].is_closed`            | Boolean | Yes      | Not null; cannot be `true` together with `is_twenty_four_hours`                                                           |
| `operating_hours[].is_twenty_four_hours` | Boolean | Yes      | Not null; cannot be `true` together with `is_closed`                                                                      |
| `operating_hours[].windows`              | Array   | —        | Must be empty when `is_closed`/`is_twenty_four_hours` is `true`; at least one entry otherwise; no two entries may overlap |
| `operating_hours[].windows[].opens_at`   | String  | Yes      | `HH:mm:ss`                                                                                                                |
| `operating_hours[].windows[].closes_at`  | String  | Yes      | `HH:mm:ss`; `<= opens_at` means the window rolls past midnight into the next calendar day                                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Resort Facility

`GET /api/v1/resorts/{resort-id}/facilities/{id}`

Returns a single active resort facility, scoped to its owning resort — an `id` that exists but belongs to a
different resort returns `404 ENTITY_NOT_FOUND`, the same as an unknown `id`. `locale` is the one translation
matching the request's `Accept-Language` header (falls back to `en`, then `null` if the facility has no
translations at all). To fetch every translation, use
[List Resort Facility Locales](#list-resort-facility-locales) below.

### Path Parameters

| Parameter   | Type | Description               |
|-------------|------|---------------------------|
| `resort-id` | Long | ID of the owning resort   |
| `id`        | Long | ID of the resort facility |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "RESTAURANT",
    "sort_order": 1,
    "is_highlighted": false,
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
      "name": "Main Restaurant",
      "description": "Full-service restaurant with buffet and à la carte options.",
      "notes": "Reservations recommended on weekends.",
      "sort_order": 1
    }
  }
}
```

---

## List / Search Resort Facilities

`GET /api/v1/resorts/{resort-id}/facilities`

Returns a paginated, filterable list of a resort's active (non-deleted) facilities. All filter parameters are
optional; omitting them returns every facility belonging to the resort. Multiple filters are combined with
AND. `name` and `code` perform a case-insensitive partial match (`code` against the raw column, `name` against
the resolved locale); `resortFacilityGroupId` and `facilityId` are exact matches. `Accept-Language` selects
each row's `locale` field the same way as `GET /{id}`.

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is omitted
> entirely.

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

### Query Parameters

> **Note:** Query parameters bind directly onto `ResortFacilityFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies. Jackson's `@JsonNaming` (which
> produces snake_case) only applies to `@RequestBody`/`@ResponseBody`; `@ModelAttribute`/`@ParameterObject`
> query-string binding goes through Spring's plain `DataBinder` instead, which matches the exact property name.

| Parameter               | Type   | Default         | Constraints                                                                                            | Description                                                                               |
|-------------------------|--------|-----------------|--------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| `resortFacilityGroupId` | Long   | —               | —                                                                                                      | Filter by exact resort facility group id                                                  |
| `facilityId`            | Long   | —               | —                                                                                                      | Filter by exact linked platform facility id                                               |
| `code`                  | String | —               | —                                                                                                      | Filter by code (partial, case-insensitive)                                                |
| `name`                  | String | —               | —                                                                                                      | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale |
| `page`                  | int    | `0`             | >= 0                                                                                                   | Zero-based page index                                                                     |
| `size`                  | int    | `10`            | 1 – 50                                                                                                 | Number of items per page                                                                  |
| `sortBy`                | String | `id` (implicit) | `createdAt`, `resortFacilityGroupEntity.id`, `facilityEntity.id`, `code`, `name` (`id` NOT selectable) | Field to sort by                                                                          |
| `sortDir`               | String | `ASC`           | `ASC`, `DESC`                                                                                          | Sort direction                                                                            |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "RESTAURANT",
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
        "name": "Main Restaurant",
        "description": "Full-service restaurant with buffet and à la carte options.",
        "notes": "Reservations recommended on weekends.",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "code": "SWIMMING_POOL",
      "sort_order": 2,
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
        "name": "Swimming Pool",
        "description": "Outdoor infinity pool with sun deck and loungers.",
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
    "resortFacilityGroupEntity.id",
    "facilityEntity.id",
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

## Update Resort Facility

`PUT /api/v1/resorts/{resort-id}/facilities/{id}`

Updates `sort_order`, `icon_type`, `icon_value`, and `icon_meta`. `resort_facility_group_id`, `facility_id`, and
`code` are set at creation and cannot be changed — to move a facility to a different group, base it on a
different platform facility, or change its code, delete it and create a new one instead. Locale translations
are managed separately via the Resort Facility Locales sub-resource endpoints below, not through this
endpoint.

### Path Parameters

| Parameter   | Type | Description               |
|-------------|------|---------------------------|
| `resort-id` | Long | ID of the owning resort   |
| `id`        | Long | ID of the resort facility |

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

## Delete Resort Facility

`DELETE /api/v1/resorts/{resort-id}/facilities/{id}`

Soft-deletes the resort facility. The record is not removed from the database but will no longer appear in
any response.

### Path Parameters

| Parameter   | Type | Description               |
|-------------|------|---------------------------|
| `resort-id` | Long | ID of the owning resort   |
| `id`        | Long | ID of the resort facility |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Resort Facility Locales

Resort Facility Locale endpoints manage locale-specific name/description/notes translations for a resort
facility.
The `{resort-id}`/`{resort-facility-id}` path parameters must reference an existing, active resort and resort
facility respectively (a `resort-facility-id` belonging to a different resort behaves the same as an unknown
one — `404 ENTITY_NOT_FOUND`).

---

### List Resort Facility Locales

`GET /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales`

Returns a paginated list of every locale translation belonging to a resort facility — this is the only way to
see more than the single Accept-Language-matched translation returned by
[Get Resort Facility](#get-resort-facility) and
[List / Search Resort Facilities](#list--search-resort-facilities). Optionally filtered to locales whose
`code` contains a given substring.

#### Path Parameters

| Parameter            | Type | Description                      |
|----------------------|------|----------------------------------|
| `resort-id`          | Long | ID of the owning resort          |
| `resort-facility-id` | Long | ID of the parent resort facility |

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
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Main Restaurant",
      "description": "Full-service restaurant with buffet and à la carte options.",
      "notes": "Reservations recommended on weekends.",
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
      "name": "প্রধান রেস্তোরাঁ",
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

### Create Resort Facility Locale

`POST /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales`

Adds a new locale translation to an existing resort facility. `locale_id` must reference an existing, active
locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of resort facility and locale
must be unique — adding a locale the facility already has a translation for returns `409 CONFLICT`,
pre-checked at the application level before any write (backed by a DB-level unique constraint on
`(resort_facility_id, locale_id)` as a last-resort guard).

#### Path Parameters

| Parameter            | Type | Description                      |
|----------------------|------|----------------------------------|
| `resort-id`          | Long | ID of the owning resort          |
| `resort-facility-id` | Long | ID of the parent resort facility |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "প্রধান রেস্তোরাঁ",
  "description": "বুফে এবং আ লা কার্ট বিকল্প সহ পূর্ণ-পরিষেবা রেস্তোরাঁ।",
  "notes": "সাপ্তাহিক ছুটির দিনে রিজার্ভেশন সুপারিশ করা হয়।",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|---------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale |
| `name`        | String  | Yes      | Not blank, max 255 chars                    |
| `description` | String  | —        | —                                           |
| `notes`       | String  | —        | —                                           |
| `sort_order`  | Integer | Yes      | Not null                                    |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 2
}
```

---

### Update Resort Facility Locale

`PUT /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales/{id}`

Updates `name`, `description`, `notes`, and `sort_order` for an existing resort facility locale translation. The
associated resort facility and locale cannot be changed after creation.

#### Path Parameters

| Parameter            | Type | Description                      |
|----------------------|------|----------------------------------|
| `resort-id`          | Long | ID of the owning resort          |
| `resort-facility-id` | Long | ID of the parent resort facility |
| `id`                 | Long | ID of the resort facility locale |

#### Request Body

```json
{
  "name": "প্রধান রেস্তোরাঁ",
  "description": "বুফে এবং আ লা কার্ট বিকল্প সহ পূর্ণ-পরিষেবা রেস্তোরাঁ।",
  "notes": "সাপ্তাহিক ছুটির দিনে রিজার্ভেশন সুপারিশ করা হয়।",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | —        | —                        |
| `notes`       | String  | —        | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

### Delete Resort Facility Locale

`DELETE /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales/{id}`

Soft-deletes a resort facility locale. The record is not removed from the database but will no longer appear
in any response.

#### Path Parameters

| Parameter            | Type | Description                      |
|----------------------|------|----------------------------------|
| `resort-id`          | Long | ID of the owning resort          |
| `resort-facility-id` | Long | ID of the parent resort facility |
| `id`                 | Long | ID of the resort facility locale |

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
  "message": "ResortFacility not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
|-------------|----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; an unsupported `sortBy` query value; `create` only — `operating_hours` shape/completeness errors (duplicate or missing `day_of_week_id`, `is_closed`/`is_twenty_four_hours` both `true`, `windows` inconsistent with them) — see [Resort Facility Operating Hours API](resort-facility-operating-hours-api.md#error-responses) |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found; resort facility not found for the given `resort-id`/`id` pair (including an `id` that belongs to a different resort); the resort facility group referenced by `resort_facility_group_id` not found (or belongs to a different resort); the platform facility referenced by `facility_id` not found; resort facility locale not found; the locale referenced by `locale_id` not found; `create` only — an unknown `day_of_week_id` in `operating_hours`      |
| 409         | `CONFLICT`                 | The resort already has an active facility linked to the given `facility_id` (`create`, application-level check only — no DB constraint backs it); the resort facility already has a translation for the given `locale_id` (`create` locale, pre-checked at the application level); `create` only — same-day or cross-day overlap in `operating_hours` (see [Resort Facility Operating Hours API](resort-facility-operating-hours-api.md#overlap-and-cross-day-validation))    |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `(resort_facility_id, locale_id)`, should not normally be reachable now that the duplicate is pre-checked at the application level; a duplicate `code` within the same resort (backed by the `(resort_id, code)` unique index — not pre-checked at the application level)                                                                                                                                                           |
