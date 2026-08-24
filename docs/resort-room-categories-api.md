# Resort Room Categories API

Base URL: `/api/v1/resorts/{resort-id}/room-categories`

A Resort Room Category links a platform-defined Room Category to a specific resort, giving it a
resort-scoped `code` and display order. A resort room category's display name and description are
locale-specific and are managed through a companion sub-resource — Resort Room Category Locales — reached via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales`. Its occupancy, room-detail,
and booking-rule settings live in a **1:1 Meta sub-resource**, created automatically together with the resort
room category and updated separately via `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/meta`.
Its bed configuration (one row per bed type, e.g. 1 king + 1 sofa bed) lives in a **1:N Beds sub-resource** —
[Resort Room Category Beds](resort-room-category-beds-api.md) — reached via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/beds`. At least one bed is required at
creation time (see `beds` below); further beds are added afterward through that sub-resource. Unlike locale
and meta, a room category's bed rows are embedded directly as the `beds` array on this entity's own `GET`
responses (see [Data Model](#data-model) below) in addition to being manageable through the sub-resource.

Its pricing lives in a **1:N Prices sub-resource** — [Resort Room Category
Prices](resort-room-category-prices-api.md) — reached via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`. **At least one currency's full
BASE/WEEKDAY/WEEKEND price set is required at creation time** (see `prices` below) — every resort room category
must be created already knowing what it costs, in at least one currency, for every day of the week. Additional
currencies, and additional HOLIDAY/SPECIAL prices, are added afterward through that sub-resource. **Unlike
`beds`, `prices` is not embedded on this entity's `GET` responses** — fetch [List Resort Room Category
Prices](resort-room-category-prices-api.md#list-resort-room-category-prices) separately to see them.

All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Resort Room Category)** and **`GET` (List Resort Room Categories)** — the header's
  value selects exactly one locale translation for the resort room category's `locale` field (and for the
  nested `room_category.locale` field): an exact match if a translation exists, otherwise `en`, otherwise
  `null`.
- **`GET .../locales` (List Resort Room Category Locales)** — the header must be present, but its value has
  no effect; this endpoint returns every translation (optionally filtered by `localeCode`), not a single
  Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** (including the Meta sub-resource's `PUT`) — the header must be present but its
  value has no effect at all.

---

## Endpoints

| Method | Path                                                                                 | Description                           |
|--------|--------------------------------------------------------------------------------------|---------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories`                                        | Create a resort room category         |
| GET    | `/api/v1/resorts/{resort-id}/room-categories`                                        | List resort room categories           |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{id}`                                   | Get a resort room category            |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{id}`                                   | Update a resort room category         |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{id}`                                   | Delete a resort room category         |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/meta`         | Update a resort room category's meta  |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales`      | List a resort room category's locales |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales`      | Create a resort room category locale  |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales/{id}` | Update a resort room category locale  |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales/{id}` | Delete a resort room category locale  |

Resort room category beds have their own full endpoint set — see the [Resort Room Category Beds
API](resort-room-category-beds-api.md) — reached via
`/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/beds`. Resort room category prices
likewise have their own full endpoint set — see the [Resort Room Category Prices
API](resort-room-category-prices-api.md) — reached via
`/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices`.

---

## Data Model

### ResortRoomCategory

| Field           | Type    | Required | Constraints                                                                              | Description                                                                                                                           |
|-----------------|---------|----------|------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| `id`            | Long    | —        | read-only                                                                                | Auto-generated identifier                                                                                                             |
| `code`          | String  | Yes      | max 50 chars, unique per resort among active records; set at creation, immutable         | Resort-scoped identifier for this room category (e.g. `DLX-SEA`)                                                                      |
| `sort_order`    | Integer | Yes      | default 0                                                                                | Display order                                                                                                                         |
| `resort`        | Object  | —        | read-only                                                                                | The parent resort (`id`, `code`)                                                                                                      |
| `room_category` | Object  | —        | read-only                                                                                | The linked platform room category (`id`, `code`, `sort_order`, `locale`)                                                              |
| `locale`        | Object  | —        | nullable; see ResortRoomCategoryLocale below                                             | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if there are no translations at all) |
| `meta`          | Object  | —        | read-only here; see ResortRoomCategoryMeta below                                         | Occupancy, room-detail, and booking-rule settings — always present, created automatically with the resort room category               |
| `beds`          | Array   | —        | read-only here; see [ResortRoomCategoryBed](resort-room-category-beds-api.md#data-model) | Bed configuration rows (one per bed type) — always at least one, created together with the resort room category                       |

### ResortRoomCategoryLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|--------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 150 chars                                    | Localized room category name (e.g. "Deluxe Sea View Room")                     |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                          |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                             |

### ResortRoomCategoryMeta

| Field                 | Type    | Required | Constraints                                                         | Description                                                                                                                |
|-----------------------|---------|----------|---------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| `id`                  | Long    | —        | read-only                                                           | Auto-generated identifier                                                                                                  |
| `max_adults`          | Integer | Yes      | default 2                                                           | Maximum number of adults                                                                                                   |
| `max_children`        | Integer | Yes      | default 0                                                           | Maximum number of children                                                                                                 |
| `max_infants`         | Integer | Yes      | default 0                                                           | Maximum number of infants                                                                                                  |
| `max_occupancy`       | Integer | Yes      | default 2; must be >= `max_adults` + `max_children` + `max_infants` | Maximum total occupancy                                                                                                    |
| `room_size`           | Decimal | —        | nullable; > 0 if not null                                           | Room size, in the unit given by `room_size_unit`                                                                           |
| `room_size_unit`      | Object  | —        | read-only; nullable; see [Unit](units-api.md#data-model)            | The unit `room_size` is measured in (e.g. sqm, sqft); set at write time via `room_size_unit_id` (see Request Fields below) |
| `bedroom_count`       | Integer | Yes      | default 1; > 0                                                      | Number of bedrooms                                                                                                         |
| `bathroom_count`      | Integer | Yes      | default 1; > 0                                                      | Number of bathrooms                                                                                                        |
| `minimum_stay_nights` | Integer | Yes      | default 1; > 0                                                      | Minimum stay length, in nights                                                                                             |
| `maximum_stay_nights` | Integer | —        | nullable; if not null, must be >= `minimum_stay_nights`             | Maximum stay length, in nights                                                                                             |

`ResortRoomCategoryBed` (the shape of each entry in `beds`) is documented in full in the [Resort Room Category
Beds API](resort-room-category-beds-api.md#data-model), including its own dedicated endpoints.

---

## Create Resort Room Category

`POST /api/v1/resorts/{resort-id}/room-categories`

Creates a new resort room category, linking `room_category_id` to the resort, together with exactly **one**
initial locale translation and its **meta** settings. `code` must be unique among active, non-deleted resort
room categories for this resort — attempting to reuse an existing code returns `409 CONFLICT`. Linking a room
category the resort already has an active row for also returns `409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time. Additional
languages are added afterward via the Resort Room Category Locales sub-resource below.

**`meta` is required** — every resort room category always has meta settings, created in the same
transaction. There is no separate `POST` for meta; afterward it is only ever changed via
[Update Resort Room Category Meta](#update-resort-room-category-meta) below.

**`beds` is required and must contain at least one entry** — every resort room category must be created with
at least one bed configuration row. `bed_type_id` must be unique across the entries in a single request
(duplicates are rejected) and each must reference an existing, active bed type. Each entry has the same shape
as [Create Resort Room Category Bed](resort-room-category-beds-api.md#create-resort-room-category-bed), minus
the resort room category (resolved from the URL path there instead). Additional beds can be added afterward
via that same endpoint.

**`prices` is required and must contain at least one entry, one per currency** — `currency_id` must be unique
across the entries in a single request (duplicates are rejected). Each entry creates exactly three [Resort Room
Category Price](resort-room-category-prices-api.md) rows for that currency — `BAS` (named `"Base Price"`),
`WKD` (`"Weekday Price"`), and `WKE` (`"Weekend Price"`) — there is no way to create only one or two of the
three through this endpoint. `weekday_price`/`weekend_price` cannot exceed `base_price` for the same entry
(`400 INVALID_ARGUMENT` otherwise, mirroring the same rule on the standalone [Create Resort Room Category
Price](resort-room-category-prices-api.md#create-resort-room-category-price) endpoint). Every price unit id
(`base_price_unit_id`/`weekday_price_unit_id`/`weekend_price_unit_id`) must reference an existing, active price
unit — each of the three can be different. `weekday_day_of_week_ids`/`weekend_day_of_week_ids` must each be
non-empty and every id must reference an existing, active [Day of Week](days-of-week-api.md). `HOL`/`SPECIAL`
prices cannot be created here — add them afterward via `POST .../prices`.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Request Body

```json
{
  "room_category_id": 2,
  "code": "DLX-SEA",
  "sort_order": 1,
  "locale": {
    "name": "Deluxe Sea View Room",
    "description": "Deluxe room with a private balcony overlooking the sea.",
    "sort_order": 1
  },
  "meta": {
    "max_adults": 2,
    "max_children": 1,
    "max_infants": 0,
    "max_occupancy": 3,
    "room_size": 45.50,
    "room_size_unit_id": 1,
    "bedroom_count": 1,
    "bathroom_count": 1,
    "minimum_stay_nights": 1,
    "maximum_stay_nights": 14
  },
  "beds": [
    {
      "bed_type_id": 3,
      "quantity": 1,
      "is_extra_bed_allowed": true,
      "max_extra_beds": 1
    }
  ],
  "prices": [
    {
      "currency_id": 1,
      "base_price_unit_id": 1,
      "base_price": 100.00,
      "weekday_price_unit_id": 1,
      "weekday_price": 90.00,
      "weekday_day_of_week_ids": [
        1,
        2,
        3,
        4
      ],
      "weekend_price_unit_id": 1,
      "weekend_price": 130.00,
      "weekend_day_of_week_ids": [
        5,
        6
      ]
    }
  ]
}
```

### Request Fields

| Field              | Type    | Required | Validation                                                                                 |
|--------------------|---------|----------|--------------------------------------------------------------------------------------------|
| `room_category_id` | Long    | Yes      | Not null; must reference an existing, active room category                                 |
| `code`             | String  | Yes      | Not blank, max 50 chars, unique among this resort's active records                         |
| `sort_order`       | Integer | Yes      | Not null                                                                                   |
| `locale`           | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale |
| `meta`             | Object  | Yes      | Not null; validated (see below)                                                            |
| `beds`             | Array   | Yes      | Not empty; each entry validated (see below); `bed_type_id` must be unique within the array |
| `prices`           | Array   | Yes      | Not empty; each entry validated (see below); `currency_id` must be unique within the array |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 150 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

**Meta entry (`meta`):**

| Field                 | Type    | Required | Validation                                              |
|-----------------------|---------|----------|---------------------------------------------------------|
| `max_adults`          | Integer | Yes      | Not null                                                |
| `max_children`        | Integer | Yes      | Not null                                                |
| `max_infants`         | Integer | Yes      | Not null                                                |
| `max_occupancy`       | Integer | Yes      | Not null                                                |
| `room_size`           | Decimal | —        | Nullable; > 0 if supplied                               |
| `room_size_unit_id`   | Long    | —        | Nullable; must reference an existing unit if supplied   |
| `bedroom_count`       | Integer | Yes      | Not null; > 0                                           |
| `bathroom_count`      | Integer | Yes      | Not null; > 0                                           |
| `minimum_stay_nights` | Integer | Yes      | Not null; > 0                                           |
| `maximum_stay_nights` | Integer | —        | Nullable; if supplied, must be >= `minimum_stay_nights` |

**Bed entry (each item in `beds`):**

| Field                  | Type    | Required | Validation                                    |
|------------------------|---------|----------|-----------------------------------------------|
| `bed_type_id`          | Long    | Yes      | Not null; must reference an existing bed type |
| `quantity`             | Integer | Yes      | Not null; >= 1                                |
| `is_extra_bed_allowed` | Boolean | Yes      | Not null                                      |
| `max_extra_beds`       | Integer | Yes      | Not null; >= 0                                |

**Price group entry (each item in `prices`) — one currency's BASE/WEEKDAY/WEEKEND set:**

| Field                     | Type    | Required | Validation                                                                     |
|---------------------------|---------|----------|--------------------------------------------------------------------------------|
| `currency_id`             | Long    | Yes      | Not null; must reference an existing, active currency; unique within the array |
| `base_price_unit_id`      | Long    | Yes      | Not null; must reference an existing, active price unit                        |
| `base_price`              | Decimal | Yes      | Not null; >= 0                                                                 |
| `weekday_price_unit_id`   | Long    | Yes      | Not null; must reference an existing, active price unit                        |
| `weekday_price`           | Decimal | Yes      | Not null; >= 0; cannot exceed `base_price`                                     |
| `weekday_day_of_week_ids` | Long[]  | Yes      | Not empty; each id must reference an existing, active day of week              |
| `weekend_price_unit_id`   | Long    | Yes      | Not null; must reference an existing, active price unit                        |
| `weekend_price`           | Decimal | Yes      | Not null; >= 0; cannot exceed `base_price`                                     |
| `weekend_day_of_week_ids` | Long[]  | Yes      | Not empty; each id must reference an existing, active day of week              |

### Response `201 Created`

```json
{
  "success": true,
  "id": 10
}
```

---

## Get Resort Room Category

`GET /api/v1/resorts/{resort-id}/room-categories/{id}`

Returns a single active resort room category by its ID. `locale` is the one translation matching the
request's `Accept-Language` header (falls back to `en`, then `null` if there are no translations at all). To
fetch every translation, use [List Resort Room Category Locales](#list-resort-room-category-locales) below.

### Path Parameters

| Parameter   | Type | Description                    |
|-------------|------|--------------------------------|
| `resort-id` | Long | ID of the resort               |
| `id`        | Long | ID of the resort room category |

### Response `200 OK`

```json
{
  "data": {
    "id": 10,
    "code": "DLX-SEA",
    "sort_order": 1,
    "resort": {
      "id": 5,
      "code": "SUNSET-BAY"
    },
    "room_category": {
      "id": 2,
      "code": "DLX",
      "sort_order": 2,
      "locale": {
        "id": 3,
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
    },
    "locale": {
      "id": 7,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Deluxe Sea View Room",
      "description": "Deluxe room with a private balcony overlooking the sea.",
      "sort_order": 1
    },
    "meta": {
      "id": 4,
      "max_adults": 2,
      "max_children": 1,
      "max_infants": 0,
      "max_occupancy": 3,
      "room_size": 45.50,
      "room_size_unit": {
        "id": 1,
        "code": "SQM",
        "symbol": "m²",
        "is_base_unit": true,
        "conversion_factor": 1,
        "sort_order": 0,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Square Meter",
          "plural_name": "Square Meters",
          "description": "Metric unit of area",
          "sort_order": 1
        }
      },
      "bedroom_count": 1,
      "bathroom_count": 1,
      "minimum_stay_nights": 1,
      "maximum_stay_nights": 14
    },
    "beds": [
      {
        "id": 6,
        "bed_type": {
          "id": 3,
          "code": "KING",
          "sort_order": 1,
          "locale": {
            "id": 5,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "King Bed",
            "description": "",
            "sort_order": 1
          }
        },
        "quantity": 1,
        "is_extra_bed_allowed": true,
        "max_extra_beds": 1
      }
    ]
  }
}
```

---

## List Resort Room Categories

`GET /api/v1/resorts/{resort-id}/room-categories`

Returns a paginated list of active (non-deleted) resort room categories belonging to the given resort.
`Accept-Language` selects each row's `locale` field (and the nested `room_category.locale` field) the same
way as `GET /{id}` (exact match, falls back to `en`, then `null`). Each row also embeds its full `meta`
object and `beds` array, same as `GET /{id}`.

> **Note:** no field is currently registered as filterable or sortable beyond the default. `sortBy` accepts
> only `createdAt` — passing any other value throws `400 INVALID_ARGUMENT: Invalid sort field: <value>`.
> Omitting `sortBy` entirely sorts by `id` (implicit).

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Query Parameters

| Parameter | Type   | Default         | Constraints      | Description              |
|-----------|--------|-----------------|------------------|--------------------------|
| `page`    | int    | `0`             | >= 0             | Zero-based page index    |
| `size`    | int    | `10`            | 1 – 50           | Number of items per page |
| `sortBy`  | String | `id` (implicit) | `createdAt` only | Field to sort by         |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`    | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 10,
      "code": "DLX-SEA",
      "sort_order": 1,
      "resort": {
        "id": 5,
        "code": "SUNSET-BAY"
      },
      "room_category": {
        "id": 2,
        "code": "DLX",
        "sort_order": 2,
        "locale": {
          "id": 3,
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
      },
      "locale": {
        "id": 7,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Deluxe Sea View Room",
        "description": "Deluxe room with a private balcony overlooking the sea.",
        "sort_order": 1
      },
      "meta": {
        "id": 4,
        "max_adults": 2,
        "max_children": 1,
        "max_infants": 0,
        "max_occupancy": 3,
        "room_size": 45.50,
        "room_size_unit": {
          "id": 1,
          "code": "SQM",
          "symbol": "m²",
          "is_base_unit": true,
          "conversion_factor": 1,
          "sort_order": 0,
          "locale": {
            "id": 1,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "Square Meter",
            "plural_name": "Square Meters",
            "description": "Metric unit of area",
            "sort_order": 1
          }
        },
        "bedroom_count": 1,
        "bathroom_count": 1,
        "minimum_stay_nights": 1,
        "maximum_stay_nights": 14
      },
      "beds": [
        {
          "id": 6,
          "bed_type": {
            "id": 3,
            "code": "KING",
            "sort_order": 1,
            "locale": {
              "id": 5,
              "locale": {
                "id": 1,
                "code": "en",
                "name": "English",
                "sort_order": 1
              },
              "name": "King Bed",
              "description": "",
              "sort_order": 1
            }
          },
          "quantity": 1,
          "is_extra_bed_allowed": true,
          "max_extra_beds": 1
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [
    "createdAt"
  ],
  "searchable_fields": []
}
```

---

## Update Resort Room Category

`PUT /api/v1/resorts/{resort-id}/room-categories/{id}`

Updates `sort_order` only. `code` and `room_category_id` are set at creation and cannot be changed. Locale
translations and meta settings are managed separately, via the sub-resource endpoints below, not through this
endpoint.

### Path Parameters

| Parameter   | Type | Description                    |
|-------------|------|--------------------------------|
| `resort-id` | Long | ID of the resort               |
| `id`        | Long | ID of the resort room category |

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
  "id": 10
}
```

---

## Delete Resort Room Category

`DELETE /api/v1/resorts/{resort-id}/room-categories/{id}`

Soft-deletes the resort room category (and cascades the soft-delete to its own locale translations, its
meta row, and its bed rows). The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter   | Type | Description                    |
|-------------|------|--------------------------------|
| `resort-id` | Long | ID of the resort               |
| `id`        | Long | ID of the resort room category |

### Response `200 OK`

```json
{
  "success": true,
  "id": 10
}
```

---

## Resort Room Category Meta

The Meta sub-resource holds occupancy, room-detail, and booking-rule settings for a resort room category. It
is a strict **1:1** relationship — every resort room category has exactly one meta row, created automatically
by [Create Resort Room Category](#create-resort-room-category) above. There is no `POST`, standalone `GET`,
or `DELETE` for meta — it is only ever read as the embedded `meta` field on
[Get](#get-resort-room-category)/[List](#list-resort-room-categories) Resort Room Category, and only ever
changed via the `PUT` below.

---

### Update Resort Room Category Meta

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/meta`

Updates every field of a resort room category's meta settings (full replace, not a partial patch — omitted
fields are rejected by validation the same as on create, except the two nullable fields).

#### Path Parameters

| Parameter                 | Type | Description                           |
|---------------------------|------|---------------------------------------|
| `resort-id`               | Long | ID of the parent resort               |
| `resort-room-category-id` | Long | ID of the parent resort room category |

#### Request Body

```json
{
  "max_adults": 2,
  "max_children": 2,
  "max_infants": 1,
  "max_occupancy": 4,
  "room_size": 52.00,
  "room_size_unit_id": 1,
  "bedroom_count": 1,
  "bathroom_count": 1,
  "minimum_stay_nights": 2,
  "maximum_stay_nights": 21
}
```

#### Request Fields

Same shape as the `meta` entry on [Create Resort Room Category](#create-resort-room-category) — see the
Meta entry table there.

#### Response `200 OK`

```json
{
  "success": true,
  "id": 4
}
```

`id` is the meta row's own id, not the resort room category's.

---

## Resort Room Category Locales

Resort Room Category Locale endpoints manage locale-specific name/description translations for a resort
room category. The `{resort-id}` and `{resort-room-category-id}` path parameters must reference an
existing, active resort and resort room category respectively.

---

### List Resort Room Category Locales

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales`

Returns a paginated list of every locale translation belonging to a resort room category — this is the only
way to see more than the single Accept-Language-matched translation returned by
`GET .../room-categories/{id}` and `GET .../room-categories`. Optionally filtered to locales whose `code`
contains a given substring.

#### Path Parameters

| Parameter                 | Type | Description                           |
|---------------------------|------|---------------------------------------|
| `resort-id`               | Long | ID of the parent resort               |
| `resort-room-category-id` | Long | ID of the parent resort room category |

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
      "id": 7,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "Deluxe Sea View Room",
      "description": "Deluxe room with a private balcony overlooking the sea.",
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
      "name": "ডিলাক্স সি ভিউ রুম",
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

### Create Resort Room Category Locale

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales`

Adds a new locale translation to an existing resort room category. `locale_id` must reference an existing,
active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of resort room
category and locale must be unique — adding a locale it already has a translation for returns
`409 CONFLICT`.

#### Path Parameters

| Parameter                 | Type | Description                           |
|---------------------------|------|---------------------------------------|
| `resort-id`               | Long | ID of the parent resort               |
| `resort-room-category-id` | Long | ID of the parent resort room category |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "ডিলাক্স সি ভিউ রুম",
  "description": "",
  "sort_order": 2
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
  "id": 8
}
```

---

### Update Resort Room Category Locale

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing resort room category locale translation.
The associated resort room category and locale cannot be changed after creation.

#### Path Parameters

| Parameter                 | Type | Description                           |
|---------------------------|------|---------------------------------------|
| `resort-id`               | Long | ID of the parent resort               |
| `resort-room-category-id` | Long | ID of the parent resort room category |
| `id`                      | Long | ID of the resort room category locale |

#### Request Body

```json
{
  "name": "ডিলাক্স সি ভিউ রুম",
  "description": "",
  "sort_order": 2
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
  "id": 8
}
```

---

### Delete Resort Room Category Locale

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales/{id}`

Soft-deletes a resort room category locale. The record is not removed from the database but will no longer
appear in any response.

#### Path Parameters

| Parameter                 | Type | Description                           |
|---------------------------|------|---------------------------------------|
| `resort-id`               | Long | ID of the parent resort               |
| `resort-room-category-id` | Long | ID of the parent resort room category |
| `id`                      | Long | ID of the resort room category locale |

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
  "message": "ResortRoomCategory not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                                                                                             |
|-------------|----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields (including an empty `beds`/`prices` array, a duplicate `bed_type_id` within `beds`, or a duplicate `currency_id` within `prices` at create); a `weekday_price`/`weekend_price` greater than `base_price` for the same `prices` entry; or an unsupported `sortBy` query value                        |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found, room category not found (`room_category_id` at create), unit not found (`room_size_unit_id` if supplied), bed type not found (any `bed_type_id` in `beds` at create), currency/price unit/day of week not found (any id in `prices` at create), resort room category not found, resort room category meta not found, resort room category locale not found, or the locale referenced by `locale_id` not found (locale creation) |
| 409         | `CONFLICT`                 | `code` already in use by another active resort room category for this resort (`create`); the resort already has this room category linked (`create`); or the resort room category already has a translation for the given `locale_id` (`create` locale, pre-checked at the application level)                                                                                                                                                     |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraints on `(resort_id, code)`, `(resort_id, room_category_id)`, `resort_room_category_id` (meta), `(resort_room_category_id, locale_id)`, or the `prices`/day-of-week backstops described in the [Resort Room Category Prices API](resort-room-category-prices-api.md#error-responses) — should not normally be reachable now that duplicates are pre-checked at the application level                           |

See the [Resort Room Category Beds API](resort-room-category-beds-api.md#error-responses) for errors specific
to the standalone beds sub-resource endpoints (e.g. the `409 CONFLICT` when a bed type is already in use), and
the [Resort Room Category Prices API](resort-room-category-prices-api.md#error-responses) for errors specific
to the standalone prices sub-resource endpoints (including the `500 INTERNAL_SERVER_ERROR` caveat for
DB-trigger-only rules that this creation flow's own `weekday`/`weekend`-vs-`base` check does not fully replace,
e.g. price scope assignment).
