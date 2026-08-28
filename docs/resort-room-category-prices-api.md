# Resort Room Category Prices API

Base URL: `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`

Resort room category pricing is split across two concepts, each with its own table and its own request/
response shape — there is no generic "any price type" table or endpoint:

- **Main** — a room category's normal per-currency rate structure: `base_price`, `weekday_price`, and
  `weekend_price`, together as **one row per currency**. `base_price` is the default rack rate;
  `weekday_price`/`weekend_price` override it on weekday/weekend dates and can never exceed it.
- **Special** — any number of date-ranged rules per currency, each with its **own** `weekday_price`/
  `weekend_price` pair for that date range — no cap vs. `base_price`. There is no separate holiday concept: a
  holiday (Eid, Christmas, ...) is just a Special row whose `name`/`description` say so, exactly like a
  promotion or surcharge not tied to a public holiday (e.g. a New Year's Eve surcharge, a summer promotion).

**Which days of week count as WEEKDAY vs. WEEKEND is not part of this API at all — it's a property of the
*resort*, shared by every room category and every currency at that resort, and lives on the separate [Resort
Weekly Schedule API](resort-weekly-schedule-api.md).** A resort's weekly schedule must be set via
`PUT /resorts/{resort-id}/weekly-schedule` **before** any of its room categories can be given an active price —
every row on both tables always carries both a weekday and a weekend price, so both day sets must exist
first (see [Error Responses](#error-responses)). Every row in responses below embeds `weekday_days`/
`weekend_days` — always the resort's one shared schedule, not anything set per price row — so a client can tell
which calendar dates within a Special range get which of its two prices (e.g. Eid Jun 16–20: Jun 16–18
use `weekday_price`, Jun 19–20 use `weekend_price`).

**Resolving a price for a given date** follows this precedence: a **Special** row covering that date wins;
otherwise **Weekday**/**Weekend** (from Main, based on the resort's schedule); otherwise **Base**. If more than
one Special row covers the same date, `priority` (higher wins) breaks the tie — Special date ranges are
allowed to overlap.

Resort room category prices are always reached nested under their owning resort room category; there is no
top-level `/api/v1/resort-room-category-prices` route. Every endpoint below also validates the
`{resort-id}`/`{resort-room-category-id}` pair first — an unknown resort, an unknown room category, or a room category
that exists but belongs to a different resort all return `404 ENTITY_NOT_FOUND`. `{id}` on the Special
single-row endpoints is additionally scoped to `{resort-room-category-id}` — a price `id` that exists but belongs to a
different room category behaves the same as an unknown `id`.

**Main can only ever be created
via [Create Resort Room Category](resort-room-categories-api.md#create-resort-room-category)
(the room category's first currency)
or [Create Resort Room Category Main Price](#create-resort-room-category-main-price)
(every currency after that).** There is no way to delete just a currency's main price while leaving its
Special rows behind — those rows require an active main price to exist (see
[Create Resort Room Category Special Price](#create-resort-room-category-special-price)), so
[Delete Resort Room Category Prices By Currency](#delete-resort-room-category-prices-by-currency) removes both
at once instead of orphaning them. Special rows can be created any number of times (one per date
range/promotion/holiday), edited in place, and deleted individually.

`price_unit_id` and `currency_id` are supplied only at creation and are never fields on a response object —
responses always embed the resolved `price_unit`/`currency` instead (see [Data Model](#data-model)).
`currency_id` is additionally **immutable after creation**: it is never accepted on any Update request. For
Main, `price_unit_id` can be changed via [Update](#update-resort-room-category-main-price), in place — Main is
a single row per currency, so Update edits it directly rather than replacing it. For Special,
`price_unit_id` can likewise be changed via Update, but `currency_id` cannot (delete and recreate instead).

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). This entity has no `locale` field of its own, but the header's value still
shapes the response: it selects the locale-matched translation embedded on `resort_room_category.locale`,
`price_unit.locale`, `currency.locale`, and each day's `day_of_week.locale`.

---

## Endpoints

| Method | Path                                                                                         | Description                                                            |
|--------|----------------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/main`          | Create a resort room category's main price for a currency              |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/specials`      | Create a resort room category special price                            |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`               | Get a resort room category's prices, grouped by type, for one currency |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/count`         | Count the currencies for which the room category has a main price      |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/main`          | Update a resort room category's main price for a currency, in place    |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/specials/{id}` | Update a resort room category special price                            |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/specials/{id}` | Delete a resort room category special price                            |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`               | Delete every price (main + special) for one currency                   |

There is no `GET /{id}` — a single row is only ever seen embedded inside [List Resort Room Category
Prices](#list-resort-room-category-prices)' grouped response, since every row is always looked at in the
context of its currency's full main/special set.

---

## Data Model

### ResortRoomCategoryMainPrice

| Field                  | Type    | Required | Constraints                                                                        | Description                                                      |
|------------------------|---------|----------|------------------------------------------------------------------------------------|------------------------------------------------------------------|
| `id`                   | Long    | —        | read-only                                                                          | Auto-generated identifier                                        |
| `resort_room_category` | Object  | —        | read-only; see [ResortRoomCategory](resort-room-categories-api.md)                 | The room category this price belongs to                          |
| `price_unit`           | Object  | —        | read-only; see [PriceUnit](price-units-api.md); resolved from `price_unit_id`      | Billing unit shared by all three prices below (`PER_NIGHT`, ...) |
| `currency`             | Object  | —        | read-only; see [Currency](currencies-api.md); resolved from `currency_id`          | Currency of the three prices below. Immutable after creation     |
| `base_price`           | Decimal | Yes      | not null; >= 0; at most 10 integer/2 fraction digits (`numeric(12,2)`)             | Default rack rate                                                |
| `weekday_price`        | Decimal | Yes      | not null; >= 0; at most 10 integer/2 fraction digits; cannot exceed `base_price`   | Applies on weekday dates                                         |
| `weekend_price`        | Decimal | Yes      | not null; >= 0; at most 10 integer/2 fraction digits; cannot exceed `base_price`   | Applies on weekend dates                                         |
| `weekday_days`         | Array   | —        | read-only; see [ResortWeeklyScheduleDay](resort-weekly-schedule-api.md#data-model) | The resort's shared weekly-schedule days classified as weekday   |
| `weekend_days`         | Array   | —        | read-only; see [ResortWeeklyScheduleDay](resort-weekly-schedule-api.md#data-model) | The resort's shared weekly-schedule days classified as weekend   |

### ResortRoomCategorySpecialPrice

A date-ranged rule with its own weekday/weekend price pair. There is no separate holiday concept — a holiday
is just a Special row whose `name` says so (e.g. `Eid-ul-Fitr`).

| Field                  | Type    | Required | Constraints                                                                        | Description                                                                        |
|------------------------|---------|----------|------------------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| `id`                   | Long    | —        | read-only                                                                          | Auto-generated identifier                                                          |
| `resort_room_category` | Object  | —        | read-only; see [ResortRoomCategory](resort-room-categories-api.md)                 | The room category this price belongs to                                            |
| `price_unit`           | Object  | —        | read-only; see [PriceUnit](price-units-api.md); resolved from `price_unit_id`      | Billing unit shared by the two prices below                                        |
| `currency`             | Object  | —        | read-only; see [Currency](currencies-api.md); resolved from `currency_id`          | Currency of the two prices below. Immutable after creation                         |
| `name`                 | String  | Yes      | not blank, max 200 chars                                                           | Display name, e.g. `Eid-ul-Fitr`, `New Year's Eve`                                 |
| `description`          | String  | —        | nullable                                                                           | Optional description                                                               |
| `valid_from`           | Date    | Yes      | not null; must be <= `valid_to`                                                    | Start of the date range this rule applies to                                       |
| `valid_to`             | Date    | Yes      | not null                                                                           | End of the date range this rule applies to                                         |
| `weekday_price`        | Decimal | Yes      | not null; >= 0; at most 10 integer/2 fraction digits; **no cap vs. `base_price`**  | Applies on weekday dates within [`valid_from`, `valid_to`]                         |
| `weekend_price`        | Decimal | Yes      | not null; >= 0; at most 10 integer/2 fraction digits; **no cap vs. `base_price`**  | Applies on weekend dates within [`valid_from`, `valid_to`]                         |
| `priority`             | Integer | —        | nullable, defaults to `0`                                                          | Tie-breaker when multiple Special rules could apply to the same date — higher wins |
| `weekday_days`         | Array   | —        | read-only; see [ResortWeeklyScheduleDay](resort-weekly-schedule-api.md#data-model) | The resort's shared weekly-schedule days classified as weekday                     |
| `weekend_days`         | Array   | —        | read-only; see [ResortWeeklyScheduleDay](resort-weekly-schedule-api.md#data-model) | The resort's shared weekly-schedule days classified as weekend                     |

> **Note:** `price_unit_id` and `currency_id` are write-only inputs and never appear on either data model, since
> responses always return the resolved `price_unit`/`currency` objects instead. Each embeds only `id`, `code`,
> `sort_order`, and the Accept-Language-matched `locale` (plus, for `currency`, `numeric_code`/`symbol`/
> `decimal_places`/`is_default`) — nested collections are never embedded here. `weekday_days`/`weekend_days` are
> likewise read-only — see the [Resort Weekly Schedule API](resort-weekly-schedule-api.md) to change them.

### Price unit rules

**Not every price unit is valid here** — a price unit is only usable for a resort room category price if it's
assigned to the `ROOM_CATEGORY` [price scope](price-scopes-api.md). This is enforced at the database level on
every write (see [Error Responses](#error-responses)); build the picker correctly by fetching the allowed set
first via `GET /api/v1/price-units?priceScopeCodes=ROOM_CATEGORY` and only offering those ids.

A room category may have at most one **active** main price per currency — creating a second one for the same
currency returns `409 CONFLICT`, backed by a database-level partial unique index
(`uq_resort_room_category_main_price_active`) so even two
concurrent [Create Main](#create-resort-room-category-main-price)
requests for the same room category/currency cannot both succeed (see [Error Responses](#error-responses)).
Special has no such limit — a room category/currency may have any number of active Special rows at once,
including ones with overlapping date ranges (`priority` breaks ties).

---

## Create Resort Room Category Main Price

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/main`

Adds a new currency's main price to an already-existing resort room category — the same row [Create Resort Room
Category](resort-room-categories-api.md#create-resort-room-category) creates for the room category's first
currency, exposed here so additional currencies can be added afterward (e.g. the room category was created
priced in BDT; call this endpoint to also price it in USD). `currency_id` must not already have an active main
price for this room category — attempting to add a currency it already has one for returns `409 CONFLICT`.
`weekday_price`/`weekend_price` cannot exceed `base_price` (`400 INVALID_ARGUMENT` otherwise, mirroring the
same rule on [Create Resort Room Category](resort-room-categories-api.md#create-resort-room-category)).
`price_unit_id` must reference an existing, active [Price Unit](price-units-api.md); `currency_id` an existing,
active [Currency](currencies-api.md).

**The resort must already have an active weekly schedule** (see [Resort Weekly Schedule
API](resort-weekly-schedule-api.md)) before this call can succeed — every main price row carries both a weekday
and a weekend price, so the DB rejects an active row for a resort with no schedule for either type (`500
INTERNAL_SERVER_ERROR` currently — see [Error Responses](#error-responses)). Set the resort's schedule via
`PUT /resorts/{resort-id}/weekly-schedule` first if this is the resort's first room category price.

### Path Parameters

| Parameter          | Type | Description                    |
|--------------------|------|--------------------------------|
| `resort-id`        | Long | ID of the owning resort        |
| `room-category-id` | Long | ID of the resort room category |

### Request Body

```json
{
  "currency_id": 2,
  "price_unit_id": 1,
  "base_price": 220.00,
  "weekday_price": 200.00,
  "weekend_price": 260.00
}
```

### Request Fields

| Field           | Type    | Required | Validation                                                                                                               |
|-----------------|---------|----------|--------------------------------------------------------------------------------------------------------------------------|
| `currency_id`   | Long    | Yes      | Not null; must reference an existing, active currency; must not already have an active main price for this room category |
| `price_unit_id` | Long    | Yes      | Not null; must reference an existing, active price unit                                                                  |
| `base_price`    | Decimal | Yes      | Not null; >= 0; at most 10 integer/2 fraction digits                                                                     |
| `weekday_price` | Decimal | Yes      | Not null; >= 0; at most 10 integer/2 fraction digits; cannot exceed `base_price`                                         |
| `weekend_price` | Decimal | Yes      | Not null; >= 0; at most 10 integer/2 fraction digits; cannot exceed `base_price`                                         |

### Response `201 Created`

```json
{
  "success": true,
  "id": 20
}
```

---

## Create Resort Room Category Special Price

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/specials`

Creates a Special pricing rule for the room category — a date-ranged rule with its own weekday/weekend price
pair, used both for holidays (a surcharge for Eid-ul-Fitr, Christmas, or another public holiday) and for
promotions/surcharges not tied to a public holiday (e.g. a New Year's Eve surcharge, a summer promotion). There
is no separate holiday endpoint — `name`/`description` say what the rule is for. `price_unit_id` must reference
an existing, active [Price Unit](price-units-api.md); `currency_id` an existing, active
[Currency](currencies-api.md). **`currency_id` must already have an active main price for this room category**
— see [Create Resort Room Category Main Price](#create-resort-room-category-main-price) — otherwise the call
fails with `404 ENTITY_NOT_FOUND`; a currency with no main price cannot be given a special price first.
Overlapping date ranges with another active Special row (for the same room category/currency) are allowed —
`priority` breaks the tie when resolving a price for a date both cover.

### Path Parameters

| Parameter          | Type | Description                    |
|--------------------|------|--------------------------------|
| `resort-id`        | Long | ID of the owning resort        |
| `room-category-id` | Long | ID of the resort room category |

### Request Body

```json
{
  "currency_id": 1,
  "price_unit_id": 1,
  "name": "Eid-ul-Fitr",
  "description": "Holiday surcharge.",
  "valid_from": "2026-06-16",
  "valid_to": "2026-06-20",
  "weekday_price": 120.00,
  "weekend_price": 140.00,
  "priority": 100
}
```

### Request Fields

| Field           | Type    | Required | Validation                                                                                              |
|-----------------|---------|----------|---------------------------------------------------------------------------------------------------------|
| `currency_id`   | Long    | Yes      | Not null; must reference an existing, active currency                                                   |
| `price_unit_id` | Long    | Yes      | Not null; must reference an existing, active price unit                                                 |
| `name`          | String  | Yes      | Not blank, max 200 chars                                                                                |
| `description`   | String  | —        | Nullable                                                                                                |
| `valid_from`    | Date    | Yes      | Not null; must be <= `valid_to` (database-trigger-enforced, pre-validated at the application layer too) |
| `valid_to`      | Date    | Yes      | Not null                                                                                                |
| `weekday_price` | Decimal | Yes      | Not null; >= 0; at most 10 integer/2 fraction digits; **no cap vs. `base_price`**                       |
| `weekend_price` | Decimal | Yes      | Not null; >= 0; at most 10 integer/2 fraction digits; **no cap vs. `base_price`**                       |
| `priority`      | Integer | —        | Nullable (defaults to `0`); higher wins when multiple Special rules could apply to the same date        |

### Response `201 Created`

```json
{
  "success": true,
  "id": 24
}
```

---

## List Resort Room Category Prices

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`

Returns every active price the room category has in one currency, bucketed by type instead of as a flat list:
`main` is a single object (or `null` if the room category has no active main price in that currency), while
`specials` is an array, since a room category can have any number of active Special rows per
currency (e.g. one per holiday, one per promotion). There is no pagination. **Every entry includes
`weekday_days`/`weekend_days`**, unlike list endpoints on other entities in this codebase — useful here since
weekday/weekend pricing is defined by which calendar days they apply to, on Main *and* within a Special
range. Both always reflect the *resort's* current weekly schedule (see [Resort Weekly Schedule
API](resort-weekly-schedule-api.md)) — identical across `main`/every `specials` entry, since it's
shared, not per-row. This is also the only way to read a single row's full detail, since there is no `GET /{id}`.

### Path Parameters

| Parameter          | Type | Description                           |
|--------------------|------|---------------------------------------|
| `resort-id`        | Long | ID of the owning resort               |
| `room-category-id` | Long | ID of the owning resort room category |

### Query Parameters

| Parameter     | Type | Required | Description                           |
|---------------|------|----------|---------------------------------------|
| `currency-id` | Long | Yes      | ID of the currency to group prices by |

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
    "main": {
      "id": 10,
      "resort_room_category": {
        "id": 10,
        "code": "DLX-SEA",
        "sort_order": 1,
        "locale": {
          "...": "..."
        }
      },
      "price_unit": {
        "id": 1,
        "code": "PER_NIGHT",
        "sort_order": 1,
        "locale": {
          "...": "..."
        }
      },
      "currency": {
        "id": 1,
        "code": "USD",
        "...": "..."
      },
      "base_price": 200.00,
      "weekday_price": 180.00,
      "weekend_price": 200.00,
      "weekday_days": [
        {
          "id": 28,
          "day_of_week": {
            "id": 1,
            "code": "SUNDAY",
            "sort_order": 1,
            "locale": {
              "...": "..."
            }
          }
        }
      ],
      "weekend_days": [
        {
          "id": 30,
          "day_of_week": {
            "id": 5,
            "code": "FRIDAY",
            "sort_order": 5,
            "locale": {
              "...": "..."
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
              "...": "..."
            }
          }
        }
      ]
    },
    "specials": [
      {
        "id": 23,
        "resort_room_category": {
          "id": 10,
          "code": "DLX-SEA",
          "sort_order": 1,
          "locale": {
            "...": "..."
          }
        },
        "price_unit": {
          "id": 1,
          "code": "PER_NIGHT",
          "sort_order": 1,
          "locale": {
            "...": "..."
          }
        },
        "currency": {
          "id": 1,
          "code": "USD",
          "...": "..."
        },
        "name": "Eid-ul-Fitr",
        "description": "Holiday surcharge.",
        "valid_from": "2026-06-16",
        "valid_to": "2026-06-20",
        "weekday_price": 120.00,
        "weekend_price": 140.00,
        "priority": 100,
        "weekday_days": [
          "..."
        ],
        "weekend_days": [
          "..."
        ]
      }
    ]
  }
}
```

---

## Count Resort Room Category Main Prices

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/count`

Returns how many currencies, and which ones, the room category has an active main price for. Special
rows are not considered — this only reflects the main price set.

### Path Parameters

| Parameter          | Type | Description                    |
|--------------------|------|--------------------------------|
| `resort-id`        | Long | ID of the owning resort        |
| `room-category-id` | Long | ID of the resort room category |

### Response `200 OK`

```json
{
  "count": 2,
  "codes": [
    "USD",
    "BDT"
  ]
}
```

---

## Update Resort Room Category Main Price

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/main?currency-id={currency-id}`

**Updates one currency's main price row in place** — Main is a single row per currency, so this is a plain
field update, not a soft-delete-and-recreate (unlike the pre-split design). `currency-id` must already have an
active main price for this room category — otherwise the call fails with `404 ENTITY_NOT_FOUND`; use
[Create Resort Room Category Main Price](#create-resort-room-category-main-price) instead for a currency that
has never been priced. `weekday_price`/`weekend_price` cannot exceed `base_price`, the same rule as Create. The
row keeps its `id`.

`currency_id` is **not** a body field on this endpoint — the currency being updated is identified by the
`currency-id` query parameter, since `currency_id` is immutable (there is nothing to "change the currency to").

### Path Parameters

| Parameter          | Type | Description                    |
|--------------------|------|--------------------------------|
| `resort-id`        | Long | ID of the owning resort        |
| `room-category-id` | Long | ID of the resort room category |

### Query Parameters

| Parameter     | Type | Required | Description                                          |
|---------------|------|----------|------------------------------------------------------|
| `currency-id` | Long | Yes      | ID of the currency whose main price is being updated |

### Request Body

```json
{
  "price_unit_id": 1,
  "base_price": 230.00,
  "weekday_price": 210.00,
  "weekend_price": 270.00
}
```

### Request Fields

Same shape as [Create Resort Room Category Main Price](#create-resort-room-category-main-price) — see the
Request Fields table there — except there is no `currency_id` field (see `currency-id` under [Query
Parameters](#query-parameters-2) above).

### Response `200 OK`

```json
{
  "success": true,
  "id": 10
}
```

---

## Update Resort Room Category Special Price

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/specials/{id}`

Updates `price_unit_id`, `name`, `description`, `valid_from`, `valid_to`, `weekday_price`, `weekend_price`, and
`priority` on an existing Special row, in place — the row keeps its `id`. `currency_id` cannot be changed; to
reclassify a row's currency, delete it via [Delete](#delete-resort-room-category-special-price) and create
a new one via [Create Resort Room Category Special Price](#create-resort-room-category-special-price) instead.

### Path Parameters

| Parameter          | Type | Description                                  |
|--------------------|------|----------------------------------------------|
| `resort-id`        | Long | ID of the owning resort                      |
| `room-category-id` | Long | ID of the owning resort room category        |
| `id`               | Long | ID of the resort room category special price |

### Request Body

```json
{
  "price_unit_id": 1,
  "name": "Eid-ul-Fitr",
  "description": "Holiday surcharge, extended.",
  "valid_from": "2026-06-15",
  "valid_to": "2026-06-21",
  "weekday_price": 125.00,
  "weekend_price": 145.00,
  "priority": 100
}
```

### Request Fields

Same shape as [Create Resort Room Category Special Price](#create-resort-room-category-special-price) — see
the Request Fields table there — except there is no `currency_id` field.

### Response `200 OK`

```json
{
  "success": true,
  "id": 24
}
```

---

## Delete Resort Room Category Special Price

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/specials/{id}`

Soft-deletes the Special row. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter          | Type | Description                                  |
|--------------------|------|----------------------------------------------|
| `resort-id`        | Long | ID of the owning resort                      |
| `room-category-id` | Long | ID of the owning resort room category        |
| `id`               | Long | ID of the resort room category special price |

### Response `200 OK`

```json
{
  "success": true,
  "id": 24
}
```

---

## Delete Resort Room Category Prices By Currency

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices?currency-id={currency-id}`

Soft-deletes **every** active price for one currency in a single call — main, plus any Special rows —
atomically, in one transaction. This is the only way to remove a currency's main price; the type-specific
single-row delete endpoint only ever operates on Special rows, because deleting just the main price
would leave that currency's Special rows pointing at a currency with no base rate — this endpoint takes
all of a currency's rows together instead, so that situation can never occur.

**Every resort room category must keep at least one currency's prices.** If `currency-id` is the room
category's only currency with an active main price, the call is rejected with `409 CONFLICT` — to remove
pricing entirely, delete the room category itself instead (see [Delete Resort Room
Category](resort-room-categories-api.md#delete-resort-room-category)). If `currency-id` has no active prices
for this room category at all (already deleted, or never priced in that currency), the call fails with `404
ENTITY_NOT_FOUND`.

### Path Parameters

| Parameter          | Type | Description                    |
|--------------------|------|--------------------------------|
| `resort-id`        | Long | ID of the owning resort        |
| `room-category-id` | Long | ID of the resort room category |

### Query Parameters

| Parameter     | Type | Required | Description                                       |
|---------------|------|----------|---------------------------------------------------|
| `currency-id` | Long | Yes      | ID of the currency whose prices are being deleted |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

`id` is `currency-id` — there is no single row id to return, since this deletes a whole set of rows at once.

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "ResortRoomCategorySpecialPrice not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
|-------------|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing `currency-id` query parameter on [List](#list-resort-room-category-prices)/[Update Main](#update-resort-room-category-main-price)/[Delete By Currency](#delete-resort-room-category-prices-by-currency); missing/invalid required fields on any create/update endpoint (`currency_id`/`price_unit_id`/`name`/prices null, a price with more than 10 integer or 2 fraction digits, `valid_from`/`valid_to` null or `valid_from` after `valid_to` on Special); `weekday_price`/`weekend_price` greater than `base_price` on [Create](#create-resort-room-category-main-price)/[Update](#update-resort-room-category-main-price) Main (checked at the application layer before the write)                                                                                                                                                                                                                                                                                        |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found; resort room category not found for the given `resort-id`/`room-category-id` pair (including a `room-category-id` that belongs to a different resort); price not found for the given `room-category-id`/`id` pair (including an `id` that belongs to a different room category) on [Update Special](#update-resort-room-category-special-price)/[Delete Special](#delete-resort-room-category-special-price); no active main price found for the given `currency-id` on [Update Main](#update-resort-room-category-main-price); `currency_id` has no active main price yet on [Create Special](#create-resort-room-category-special-price) (checked at the application layer before the write); `currency-id` has no active prices at all for this room category on [Delete By Currency](#delete-resort-room-category-prices-by-currency); the price unit or currency not found; `currency-id` not found on [List](#list-resort-room-category-prices)/[Update Main](#update-resort-room-category-main-price)/[Delete By Currency](#delete-resort-room-category-prices-by-currency) |
| 409         | `CONFLICT`                 | On [Create Main](#create-resort-room-category-main-price): the room category already has an active main price for the given `currency_id` — this can also surface as a `409 DATA_INTEGRITY_VIOLATION` (see below) instead, if two concurrent requests for the same room category/currency both pass the initial check and race to insert. On [Delete By Currency](#delete-resort-room-category-prices-by-currency): `currency-id` is the room category's only remaining currency with an active main price. All checked at the application layer before the write except the concurrent-insert case                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| 409         | `DATA_INTEGRITY_VIOLATION` | A foreign key (`price_unit_id`, `currency_id`, `resort_room_category_id`) or the underlying unique constraint somehow references/duplicates a row unexpectedly — should not normally be reachable, since each is resolved and validated before the write. One case *is* reachable in practice: two concurrent [Create Main](#create-resort-room-category-main-price) calls for the same room category/currency can both pass the initial `409 CONFLICT` check before either commits — the database's `uq_resort_room_category_main_price_active` partial unique index is the actual backstop here                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| 500         | `INTERNAL_SERVER_ERROR`    | The `price_unit_id` used by the write isn't assigned to the `ROOM_CATEGORY` price scope, see [Price unit rules](#price-unit-rules); or the resort has no active weekly schedule for `WKD`/`WKE` yet, on any Create/Update endpoint, see [Resort Weekly Schedule API](resort-weekly-schedule-api.md). Neither shape rule is pre-validated on any endpoint here; in both cases the database trigger's raised exception isn't a recognized constraint-violation type, so it falls through to the generic error handler as a `500` rather than a `400`/`404`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
