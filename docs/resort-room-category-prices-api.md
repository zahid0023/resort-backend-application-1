# Resort Room Category Prices API

Base URL: `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices`

A resort room category price row describes what a [Resort Room Category](resort-room-categories-api.md) costs
under one pricing rule — a `price_type` (`BAS`/`WKD`/`WKE`/`HOL`/`SPECIAL`, i.e. Base / Weekday / Weekend /
Holiday / Special), a billing `price_unit` (e.g. `PER_NIGHT`/`PER_DAY`/`PER_PERSON`), a `currency`, an `amount`,
and — for Weekday/Weekend rules only — the set of `days` (of week) the rule applies to. A room category
typically has several rows: one `BAS` row per currency as the default rate, optional `WKD`/`WKE` overrides, and
any number of `HOL`/`SPECIAL` rows for date-bound promotions/surcharges.

Resort room category prices are always reached nested under their owning resort room category; there is no
top-level `/api/v1/resort-room-category-prices` route. Every endpoint below also validates the
`{resort-id}`/`{room-category-id}` pair first — an unknown resort, an unknown room category, or a room category
that exists but belongs to a different resort all return `404 ENTITY_NOT_FOUND`. `{id}` on the single-row
endpoints is additionally scoped to `{room-category-id}` — a price `id` that exists but belongs to a different
room category behaves the same as an unknown `id`.

**Creation is split across three purpose-built endpoints, one per price shape — there is no generic "create a
price of any type" endpoint, and none of the three take a `price_type_id` in the request body.** Each endpoint
resolves its own price type(s) server-side instead:

- [Create Resort Room Category Main Price](#create-resort-room-category-main-price) (`POST .../prices/main`)
  creates a currency's `BAS`+`WKD`+`WKE` set together, in one call, on an already-existing resort room
  category — the same three-row bundle
  [Create Resort Room Category](resort-room-categories-api.md#create-resort-room-category) creates for the
  room category's first currency, exposed here so additional currencies can be added afterward (e.g. the room
  category was created priced in BDT; call this endpoint to also price it in USD).
- [Create Resort Room Category Holiday Price](#create-resort-room-category-holiday-price)
  (`POST .../prices/holidays`) creates a single date-bound `HOL` row.
- [Create Resort Room Category Special Price](#create-resort-room-category-special-price)
  (`POST .../prices/specials`) creates a single date-bound `SPECIAL` row.

**`BAS`/`WKD`/`WKE` rows can only ever be created via [Create Resort Room
Category](resort-room-categories-api.md#create-resort-room-category) (the room category's first currency) or
[Create Resort Room Category Main Price](#create-resort-room-category-main-price) (every currency after
that) — they can never be deleted afterward, only updated, and [Delete](#delete-resort-room-category-price)
below rejects them with `400 INVALID_ARGUMENT` (see [Error Responses](#error-responses)).** `HOL`/`SPECIAL`
rows can be created any number of times (one per date range/promotion) and can also be deleted.
[Update](#update-resort-room-category-price) has no type restriction at all — every price type, including
`BAS`/`WKD`/`WKE`, can always be updated (amount, days, etc.) through this sub-resource.

`price_unit_id` and `currency_id` are **immutable after creation** — [Update Resort Room Category
Price](#update-resort-room-category-price) never changes them; to reclassify a `HOL`/`SPECIAL` price, change
its unit, or change its currency, delete it and create a new one (this escape hatch does not exist for
`BAS`/`WKD`/`WKE` — see above). A row's `price_type` is likewise permanent, though it's never submitted as an
id at all — it's implied entirely by which of the three create endpoints was called. All records support
soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). This entity has no `locale` field of its own, but the header's value still
shapes the response: it selects the locale-matched translation embedded on `resort_room_category.locale`,
`price_type.locale`, `price_unit.locale`, `currency.locale`, and each day's `day_of_week.locale`.

---

## Endpoints

| Method | Path                                                                              | Description                                                                    |
|--------|------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/main`     | Create a resort room category's BASE/WEEKDAY/WEEKEND price set for a currency |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/holidays` | Create a resort room category holiday price                                   |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/specials` | Create a resort room category special price                                   |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices`          | Get a resort room category's prices, grouped by price type, for one currency  |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/{id}`     | Get a resort room category price                                              |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/{id}`     | Update a resort room category price                                           |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/{id}`     | Delete a resort room category price                                           |

---

## Data Model

### ResortRoomCategoryPrice

| Field                  | Type    | Required      | Constraints                                                                                      | Description                                                                                                |
|------------------------|---------|---------------|----------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| `id`                   | Long    | —             | read-only                                                                                        | Auto-generated identifier                                                                                  |
| `resort_room_category` | Object  | —             | read-only; see [ResortRoomCategory](resort-room-categories-api.md)                               | The room category this price belongs to. Resolved from the URL path, never a request body field            |
| `price_type`           | Object  | —             | read-only; see [PriceType](price-types-api.md); implied by which create endpoint was called      | Pricing rule classification (`BAS`/`WKD`/`WKE`/`HOL`/`SPECIAL`). Permanent after creation                  |
| `price_unit`           | Object  | —             | read-only; see [PriceUnit](price-units-api.md); resolved from `price_unit_id`                    | Billing unit (`PER_NIGHT`/`PER_DAY`/`PER_PERSON`, ...). Immutable after creation                           |
| `currency`             | Object  | —             | read-only; see [Currency](currencies-api.md); resolved from `currency_id`                        | Currency of `price`. Immutable after creation                                                              |
| `name`                 | String  | Yes           | not blank, max 200 chars                                                                         | Display name, e.g. `Base Price`, `Weekend Price`, `Eid-ul-Fitr`                                            |
| `description`          | String  | —             | nullable                                                                                         | Optional description                                                                                       |
| `price`                | Decimal | Yes           | not null; numeric(12,2); >= 0; `WKD`/`WKE` additionally capped at the active `BAS` price         | Price amount                                                                                               |
| `valid_from`           | Date    | conditionally | required for `HOL`/`SPECIAL`, forbidden for `BAS`/`WKD`/`WKE`; must be <= `valid_to` if both set | Start of the date range the price applies to                                                               |
| `valid_to`             | Date    | conditionally | required for `HOL`/`SPECIAL`, forbidden for `BAS`/`WKD`/`WKE`                                    | End of the date range the price applies to                                                                 |
| `priority`             | Integer | —             | forced to `0` for `BAS`/`WKD`/`WKE`; free-form (default `0`) for `HOL`/`SPECIAL`                 | Tie-breaker when multiple rules could apply to the same date — higher wins (e.g. `HOL`=100, `SPECIAL`=200) |
| `days`                 | Array   | —             | see ResortRoomCategoryPriceDay below; only present on [Get](#get-resort-room-category-price)     | The days of week this rule applies to — required (non-empty) for `WKD`/`WKE`, forbidden for other types    |

> **Note:** `price_unit_id` and `currency_id` (used to resolve `price_unit` and `currency`) are write-only
> inputs, supplied only at creation — see [Create Resort Room Category Main
> Price](#create-resort-room-category-main-price), [Create Resort Room Category Holiday
> Price](#create-resort-room-category-holiday-price), and [Create Resort Room Category Special
> Price](#create-resort-room-category-special-price) — and do not appear on this data model because the
> response always returns the resolved objects instead. `price_type` itself is never submitted by the caller at
> all — each creation endpoint resolves it server-side (`BAS`/`WKD`/`WKE` for Main, `HOL` for Holiday,
> `SPECIAL` for Special). Each embeds only `id`, `code`, `sort_order`, and the Accept-Language-matched `locale`
> (plus, for `currency`, `numeric_code`/`symbol`/`decimal_places`/`is_default`) — nested collections such as a
> price type's/unit's own `price_scopes`, or a currency's `country`, are never embedded here.

### ResortRoomCategoryPriceDay

| Field         | Type   | Required | Constraints                                                                               | Description                          |
|---------------|--------|----------|---------------------------------------------------------------------------------------------|-----------------------------------------|
| `id`          | Long   | —        | read-only                                                                                 | Auto-generated identifier            |
| `day_of_week` | Object | —        | read-only; see [DayOfWeek](days-of-week-api.md); resolved from an id in `day_of_week_ids` | The day of week this rule applies to |

### Price type rules

**Not every price type/price unit is valid here** — a price type or price unit is only usable for a resort room
category price if it's assigned to the `ROOM_CATEGORY` [price scope](price-scopes-api.md). This is enforced at
the database level on every write (see [Error Responses](#error-responses)); build the picker correctly by
fetching the allowed sets first via `GET /api/v1/price-types?priceScopeCodes=ROOM_CATEGORY` and
`GET /api/v1/price-units?priceScopeCodes=ROOM_CATEGORY` and only offering those ids.

Each `price_type` code additionally has its own shape requirements, ultimately enforced by database triggers.
For [Create Resort Room Category Main Price](#create-resort-room-category-main-price), the `WKD`/`WKE`
"required days"/"forbidden dates" rules are effectively guaranteed by the request shape itself (there's no
`valid_from`/`valid_to` field to submit, and `weekday_day_of_week_ids`/`weekend_day_of_week_ids` are bean-
validated `@NotEmpty`). For [Create Resort Room Category Holiday
Price](#create-resort-room-category-holiday-price) and [Create Resort Room Category Special
Price](#create-resort-room-category-special-price), `valid_from`/`valid_to` are bean-validated `@NotNull`
directly on the request, so a request missing either fails with a normal `400 INVALID_ARGUMENT` rather than
reaching the database trigger. The one shape rule none of the three create endpoints pre-validate is that
`price_type_id`/`price_unit_id` must be assigned to the `ROOM_CATEGORY` [price
scope](price-scopes-api.md) — see the note under [Error Responses](#error-responses). **[Update Resort Room
Category Price](#update-resort-room-category-price), however, is still fully generic across every price type
and does not bean-validate any of the shape rules below** — an update that violates one of them reaches the
database trigger unvalidated, same as before this endpoint split:

| `price_type` code | `valid_from` / `valid_to`       | `days`                          | `priority`              | `price` cap                                                              |
|--------------------|----------------------------------|-----------------------------------|--------------------------|-----------------------------------------------------------------------------|
| `BAS` (Base)      | Forbidden (must be null)        | Forbidden (must be empty)       | Forced to `0`           | None                                                                     |
| `WKD` (Weekday)   | Forbidden (must be null)        | **Required** (at least one day) | Forced to `0`           | Cannot exceed the active `BAS` price for the same room category/currency |
| `WKE` (Weekend)   | Forbidden (must be null)        | **Required** (at least one day) | Forced to `0`           | Cannot exceed the active `BAS` price for the same room category/currency |
| `HOL` (Holiday)   | **Required** (both must be set) | Forbidden (must be empty)       | Free-form (default `0`) | None                                                                     |
| `SPECIAL`         | **Required** (both must be set) | Forbidden (must be empty)       | Free-form (default `0`) | None                                                                     |

A room category may have at most one **active** price per exact `(price_type, price_unit, currency)`
combination — creating a second one returns `409 CONFLICT`.

---

## Create Resort Room Category Main Price

`POST /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/main`

Adds a new currency's full `BAS`/`WKD`/`WKE` price set to an already-existing resort room category — the same
three-row bundle [Create Resort Room Category](resort-room-categories-api.md#create-resort-room-category)
creates for the room category's first currency, exposed here so additional currencies can be added afterward
(e.g. the room category was created priced in BDT; call this endpoint to also price it in USD). `currency_id`
must not already have an active `BAS` price for this room category — attempting to add a currency it already
has a price group for returns `409 CONFLICT`. `weekday_price`/`weekend_price` cannot exceed `base_price`
(`400 INVALID_ARGUMENT` otherwise, mirroring the same rule on
[Create Resort Room Category](resort-room-categories-api.md#create-resort-room-category)). Every price unit id
(`base_price_unit_id`/`weekday_price_unit_id`/`weekend_price_unit_id`) must reference an existing, active
[Price Unit](price-units-api.md) — each of the three can be different.
`weekday_day_of_week_ids`/`weekend_day_of_week_ids` must each be non-empty and every id must reference an
existing, active [Day of Week](days-of-week-api.md). `currency_id` must reference an existing, active
[Currency](currencies-api.md).

### Path Parameters

| Parameter          | Type | Description                    |
|---------------------|------|----------------------------------|
| `resort-id`        | Long | ID of the owning resort        |
| `room-category-id` | Long | ID of the resort room category |

### Request Body

```json
{
  "currency_id": 2,
  "base_price_unit_id": 1,
  "base_price": 220.00,
  "weekday_price_unit_id": 1,
  "weekday_price": 200.00,
  "weekday_day_of_week_ids": [
    1,
    2,
    3,
    4
  ],
  "weekend_price_unit_id": 1,
  "weekend_price": 260.00,
  "weekend_day_of_week_ids": [
    5,
    6
  ]
}
```

### Request Fields

| Field                    | Type    | Required | Validation                                                                                                                |
|---------------------------|---------|----------|-------------------------------------------------------------------------------------------------------------------------------|
| `currency_id`             | Long    | Yes      | Not null; must reference an existing, active currency; must not already have an active `BAS` price for this room category |
| `base_price_unit_id`      | Long    | Yes      | Not null; must reference an existing, active price unit                                                                   |
| `base_price`              | Decimal | Yes      | Not null; numeric(12,2); >= 0                                                                                              |
| `weekday_price_unit_id`   | Long    | Yes      | Not null; must reference an existing, active price unit                                                                   |
| `weekday_price`           | Decimal | Yes      | Not null; numeric(12,2); >= 0; cannot exceed `base_price`                                                                  |
| `weekday_day_of_week_ids` | Long[]  | Yes      | Not empty; each id must reference an existing, active day of week                                                          |
| `weekend_price_unit_id`   | Long    | Yes      | Not null; must reference an existing, active price unit                                                                   |
| `weekend_price`           | Decimal | Yes      | Not null; numeric(12,2); >= 0; cannot exceed `base_price`                                                                  |
| `weekend_day_of_week_ids` | Long[]  | Yes      | Not empty; each id must reference an existing, active day of week                                                          |

### Response `201 Created`

```json
{
  "success": true,
  "base_price_id": 20,
  "weekday_price_id": 21,
  "weekend_price_id": 22
}
```

---

## Create Resort Room Category Holiday Price

`POST /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/holidays`

Creates a single `HOL` (Holiday) pricing rule for the room category — e.g. a surcharge for Eid-ul-Fitr,
Christmas, or another public holiday. `price_unit_id` must reference an existing, active
[Price Unit](price-units-api.md); `currency_id` an existing, active [Currency](currencies-api.md). The room
category may have at most one active `HOL` price per exact `(price_unit, currency)` combination — creating a
second one for the same pair returns `409 CONFLICT`; use a different `price_unit_id`, or
[update](#update-resort-room-category-price)/[delete](#delete-resort-room-category-price) the existing row
first, to change a holiday's price.

### Path Parameters

| Parameter          | Type | Description                    |
|---------------------|------|----------------------------------|
| `resort-id`        | Long | ID of the owning resort        |
| `room-category-id` | Long | ID of the resort room category |

### Request Body

```json
{
  "currency_id": 1,
  "price_unit_id": 1,
  "name": "Eid-ul-Fitr",
  "description": "Holiday surcharge.",
  "price": 250.00,
  "valid_from": "2026-03-20",
  "valid_to": "2026-03-22",
  "priority": 100
}
```

### Request Fields

| Field           | Type    | Required | Validation                                                                |
|------------------|---------|----------|-------------------------------------------------------------------------------|
| `currency_id`   | Long    | Yes      | Not null; must reference an existing, active currency                     |
| `price_unit_id` | Long    | Yes      | Not null; must reference an existing, active price unit                   |
| `name`          | String  | Yes      | Not blank, max 200 chars                                                   |
| `description`   | String  | —        | Nullable                                                                   |
| `price`         | Decimal | Yes      | Not null; numeric(12,2); >= 0                                              |
| `valid_from`    | Date    | Yes      | Not null; must be <= `valid_to` if both set (database-trigger-enforced)   |
| `valid_to`      | Date    | Yes      | Not null                                                                   |
| `priority`      | Integer | —        | Nullable (defaults to `0`); free-form — higher wins when multiple `HOL`/`SPECIAL` rules could apply to the same date |

### Response `201 Created`

```json
{
  "success": true,
  "id": 23
}
```

---

## Create Resort Room Category Special Price

`POST /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/specials`

Identical request shape and rules to [Create Resort Room Category Holiday
Price](#create-resort-room-category-holiday-price) above, except it creates a `SPECIAL` row instead of `HOL`.
Use this for date-bound promotions/surcharges not tied to a public holiday (e.g. a New Year's Eve surcharge, a
long-stay discount window). The room category may have at most one active `SPECIAL` price per exact
`(price_unit, currency)` combination — creating a second one for the same pair returns `409 CONFLICT`.

### Path Parameters

| Parameter          | Type | Description                    |
|---------------------|------|----------------------------------|
| `resort-id`        | Long | ID of the owning resort        |
| `room-category-id` | Long | ID of the resort room category |

### Request Body

```json
{
  "currency_id": 1,
  "price_unit_id": 1,
  "name": "New Year's Eve",
  "description": "Peak surcharge.",
  "price": 400.00,
  "valid_from": "2026-12-31",
  "valid_to": "2026-12-31",
  "priority": 200
}
```

### Request Fields

Same shape as [Create Resort Room Category Holiday Price](#create-resort-room-category-holiday-price) — see
the Request Fields table there.

### Response `201 Created`

```json
{
  "success": true,
  "id": 24
}
```

---

## Get Resort Room Category Price

`GET /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/{id}`

Returns a single active resort room category price, scoped to its owning room category — an `id` that exists
but belongs to a different room category (or a room category that belongs to a different resort) returns `404
ENTITY_NOT_FOUND`, the same as an unknown `id`. This is the only endpoint that returns `days`.

### Path Parameters

| Parameter          | Type | Description                           |
|---------------------|------|-----------------------------------------|
| `resort-id`        | Long | ID of the owning resort               |
| `room-category-id` | Long | ID of the owning resort room category |
| `id`               | Long | ID of the resort room category price  |

### Response `200 OK`

```json
{
  "data": {
    "id": 12,
    "resort_room_category": {
      "id": 10,
      "code": "DLX-SEA",
      "sort_order": 1,
      "locale": {
        "id": 4,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Deluxe Sea View",
        "description": "",
        "sort_order": 1
      }
    },
    "price_type": {
      "id": 2,
      "code": "WKD",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Weekday",
        "description": "",
        "sort_order": 2
      }
    },
    "price_unit": {
      "id": 1,
      "code": "PER_NIGHT",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Per Night",
        "description": "",
        "sort_order": 1
      }
    },
    "currency": {
      "id": 1,
      "code": "USD",
      "numeric_code": "840",
      "symbol": "$",
      "decimal_places": 2,
      "is_default": true,
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "US Dollar",
        "sort_order": 1
      }
    },
    "name": "Weekend Price",
    "description": "Friday and Saturday nights.",
    "price": 150.00,
    "valid_from": null,
    "valid_to": null,
    "priority": 0,
    "days": [
      {
        "id": 30,
        "day_of_week": {
          "id": 5,
          "code": "FRIDAY",
          "sort_order": 5,
          "locale": {
            "id": 5,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "Friday",
            "short_name": "Fri",
            "description": "",
            "sort_order": 5
          }
        }
      },
      {
        "id": 31,
        "day_of_week": {
          "id": 6,
          "code": "SATURDAY",
          "sort_order": 6,
          "locale": {
            "id": 6,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "Saturday",
            "short_name": "Sat",
            "description": "",
            "sort_order": 6
          }
        }
      }
    ]
  }
}
```

---

## List Resort Room Category Prices

`GET /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices`

Returns every active price the room category has in one currency, bucketed by `price_type` instead of as a flat
list: `base` and `weekday`/`weekend` are each at most one object (or `null` if the room category has none active
in that currency — normally only possible for `weekday`/`weekend`, since `base` always exists per currency),
while `holidays`/`specials` are arrays, since a room category can have any number of active `HOL`/`SPECIAL` rows
per currency (e.g. one per holiday, one per promotion). There is no pagination. **Every entry includes its
`days`**, unlike list endpoints on other entities in this codebase — useful here since `weekday`/`weekend` are
defined by which days they apply to.

### Path Parameters

| Parameter          | Type | Description                           |
|---------------------|------|-----------------------------------------|
| `resort-id`        | Long | ID of the owning resort               |
| `room-category-id` | Long | ID of the owning resort room category |

### Query Parameters

| Parameter     | Type | Required | Description                            |
|----------------|------|----------|-----------------------------------------|
| `currency-id` | Long | Yes      | ID of the currency to group prices by  |

### Response `200 OK`

```json
{
  "data": {
    "currency": {
      "id": 1,
      "code": "USD",
      "numeric_code": "840",
      "symbol": "$",
      "decimal_places": 2,
      "is_default": true,
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "US Dollar",
        "sort_order": 1
      }
    },
    "base": {
      "id": 10,
      "resort_room_category": { "id": 10, "code": "DLX-SEA", "sort_order": 1, "locale": { "...": "..." } },
      "price_type": { "id": 1, "code": "BAS", "sort_order": 1, "locale": { "...": "..." } },
      "price_unit": { "id": 1, "code": "PER_NIGHT", "sort_order": 1, "locale": { "...": "..." } },
      "currency": { "id": 1, "code": "USD", "...": "..." },
      "name": "Base Price",
      "description": null,
      "price": 200.00,
      "valid_from": null,
      "valid_to": null,
      "priority": 0,
      "days": []
    },
    "weekday": null,
    "weekend": {
      "id": 12,
      "resort_room_category": { "id": 10, "code": "DLX-SEA", "sort_order": 1, "locale": { "...": "..." } },
      "price_type": { "id": 3, "code": "WKE", "sort_order": 3, "locale": { "...": "..." } },
      "price_unit": { "id": 1, "code": "PER_NIGHT", "sort_order": 1, "locale": { "...": "..." } },
      "currency": { "id": 1, "code": "USD", "...": "..." },
      "name": "Weekend Price",
      "description": "Friday and Saturday nights.",
      "price": 150.00,
      "valid_from": null,
      "valid_to": null,
      "priority": 0,
      "days": [
        { "id": 30, "day_of_week": { "id": 5, "code": "FRIDAY", "sort_order": 5, "locale": { "...": "..." } } },
        { "id": 31, "day_of_week": { "id": 6, "code": "SATURDAY", "sort_order": 6, "locale": { "...": "..." } } }
      ]
    },
    "holidays": [],
    "specials": []
  }
}
```

---

## Update Resort Room Category Price

`PUT /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/{id}`

Updates `name`, `description`, `price`, `valid_from`, `valid_to`, `priority`, and `day_of_week_ids`. **Works for
every price type, including `BAS`/`WKD`/`WKE`** — unlike Create and Delete above, Update has no price-type
restriction. `price_unit_id` and `currency_id` are set at creation and cannot be changed — for a `HOL`/`SPECIAL`
row, reclassifying it, changing its unit, or changing its currency means deleting it and creating a new one
instead; `BAS`/`WKD`/`WKE` rows cannot be deleted or re-created at all, so their `price_type`/`price_unit_id`/
`currency_id` are effectively permanent.

**`day_of_week_ids` is a full replacement, not a merge/patch** — every existing day row is removed first and the
request's `day_of_week_ids` (if any) are attached fresh. Omitting `day_of_week_ids` (or sending an empty array)
on a `WKD`/`WKE` price clears all of its days, which then fails the "at least one day" rule from [Price type
rules](#price-type-rules) — see the note under [Error Responses](#error-responses). Always resend the full
current day list (plus any changes) on every update.

### Path Parameters

| Parameter          | Type | Description                           |
|---------------------|------|-----------------------------------------|
| `resort-id`        | Long | ID of the owning resort               |
| `room-category-id` | Long | ID of the owning resort room category |
| `id`               | Long | ID of the resort room category price  |

### Request Body

```json
{
  "name": "Weekend Price",
  "description": "Friday and Saturday nights, peak season.",
  "price": 175.00,
  "priority": null,
  "day_of_week_ids": [
    5,
    6
  ]
}
```

### Request Fields

| Field             | Type    | Required | Validation                                                                                                            |
|--------------------|---------|----------|----------------------------------------------------------------------------------------------------------------------------|
| `name`            | String  | Yes      | Not blank, max 200 chars                                                                                              |
| `description`     | String  | —        | Nullable                                                                                                              |
| `price`           | Decimal | Yes      | Not null; numeric(12,2); >= 0                                                                                         |
| `valid_from`      | Date    | —        | See [Price type rules](#price-type-rules)                                                                             |
| `valid_to`        | Date    | —        | See [Price type rules](#price-type-rules)                                                                             |
| `priority`        | Integer | —        | Nullable (defaults to `0`); forced to `0` server-side for `BAS`/`WKD`/`WKE`                                           |
| `day_of_week_ids` | Long[]  | —        | Full replacement of the price's day list — see the note above; each id must reference an existing, active day of week |

### Response `200 OK`

```json
{
  "success": true,
  "id": 12
}
```

---

## Delete Resort Room Category Price

`DELETE /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/{id}`

Soft-deletes the resort room category price (and, via cascade, its day rows). The record is not removed from
the database but will no longer appear in any response. **Only `HOL`/`SPECIAL` rows can be deleted** — a
`BAS`/`WKD`/`WKE` row (identified by the target `id`'s own `price_type`) is rejected with `400
INVALID_ARGUMENT`, since a room category must always keep exactly the BASE/WEEKDAY/WEEKEND rows it was created
with (see the intro above).

### Path Parameters

| Parameter          | Type | Description                           |
|---------------------|------|-----------------------------------------|
| `resort-id`        | Long | ID of the owning resort               |
| `room-category-id` | Long | ID of the owning resort room category |
| `id`               | Long | ID of the resort room category price  |

### Response `200 OK`

```json
{
  "success": true,
  "id": 12
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
  "message": "ResortRoomCategoryPrice not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
|-------------|------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing `currency-id` query parameter on [List](#list-resort-room-category-prices); missing/invalid required fields on any of the three create endpoints or on [Update](#update-resort-room-category-price) (`currency_id`/`price_unit_id`/`name`/`price` null, or — for [Holiday](#create-resort-room-category-holiday-price)/[Special](#create-resort-room-category-special-price) — `valid_from`/`valid_to` null); `weekday_price`/`weekend_price` greater than `base_price` on [Main](#create-resort-room-category-main-price) (checked at the application layer before the write); the target row's `price_type` being `BAS`/`WKD`/`WKE` on [Delete](#delete-resort-room-category-price) (checked at the application layer before the write) |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found; resort room category not found for the given `resort-id`/`room-category-id` pair (including a `room-category-id` that belongs to a different resort); price not found for the given `room-category-id`/`id` pair (including an `id` that belongs to a different room category); the price unit, currency, or any day-of-week id not found; `currency-id` not found on [List Resort Room Category Prices](#list-resort-room-category-prices)                                                                                                                                                                                                     |
| 409         | `CONFLICT`                 | On [Main](#create-resort-room-category-main-price): the room category already has an active `BAS` price for the given `currency_id`. On [Holiday](#create-resort-room-category-holiday-price)/[Special](#create-resort-room-category-special-price): the room category already has an active price with the same price type/`price_unit_id`/`currency_id` combination. Both checked at the application layer before the write                                                                                                                                                                                                                                     |
| 409         | `DATA_INTEGRITY_VIOLATION` | A foreign key (`price_type_id`, `price_unit_id`, `currency_id`, `resort_room_category_id`, `day_of_week_id`) or the underlying unique constraints somehow reference/duplicate a row unexpectedly — should not normally be reachable, since each is resolved and validated before the write                                                                                                                                                                                                                                                                                                                                                                            |
| 500         | `INTERNAL_SERVER_ERROR`    | The `price_type_id`/`price_unit_id` pair used by the creation endpoint isn't assigned to the `ROOM_CATEGORY` price scope (all three create endpoints — this shape rule is never pre-validated, see [Price type rules](#price-type-rules)); or, on [Update](#update-resort-room-category-price) only, any of the [Price type rules](#price-type-rules) shape checks (e.g. `valid_from`/`valid_to` missing for `HOL`/`SPECIAL`, present for `BAS`/`WKD`/`WKE`; no day rows left for a `WKD`/`WKE` price; a day row attached to a non-`WKD`/`WKE` price) — Update remains fully generic and un-bean-validated for these. In every case the database trigger's raised exception isn't a recognized constraint-violation type, so it falls through to the generic error handler as a `500` rather than a `400`/`409` |
