# Resort Room Category Prices API

Base URL: `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices`

A resort room category price row describes what a [Resort Room Category](resort-room-categories-api.md) costs
under one pricing rule — a `price_type` (`BAS`/`WKD`/`WKE`/`HOL`/`SPECIAL`, i.e. Base / Weekday / Weekend /
Holiday / Special), a billing `price_unit` (e.g. `PER_NIGHT`/`PER_DAY`/`PER_PERSON`), a `currency`, an `amount`,
and — for Weekday/Weekend rules only — the set of `days` (of week) the rule applies to. A room category
typically has several rows: one `BAS` row per currency as the default rate, `WKD`/`WKE` overrides created
together with it, and any number of `HOL`/`SPECIAL` rows for date-bound promotions/surcharges.

Resort room category prices are always reached nested under their owning resort room category; there is no
top-level `/api/v1/resort-room-category-prices` route. Every endpoint below also validates the
`{resort-id}`/`{room-category-id}` pair first — an unknown resort, an unknown room category, or a room category
that exists but belongs to a different resort all return `404 ENTITY_NOT_FOUND`. `{id}` on the Holiday/Special
single-row endpoints is additionally scoped to `{room-category-id}` — a price `id` that exists but belongs to a
different room category behaves the same as an unknown `id`.

**Every write is split across three purpose-built shapes — Main (`BAS`+`WKD`+`WKE` together), Holiday
(`HOL`), and Special (`SPECIAL`) — there is no generic "any price type" endpoint, and none of the three ever
take a `price_type_id` in the request body.** Each endpoint resolves its own price type(s) server-side instead:

- **Main** — [Create](#create-resort-room-category-main-price) (`POST .../prices/main`) creates a currency's
  `BAS`+`WKD`+`WKE` set together, in one call, on an already-existing resort room category — the same
  three-row bundle [Create Resort Room Category](resort-room-categories-api.md#create-resort-room-category)
  creates for the room category's first currency, exposed here so additional currencies can be added afterward
  (e.g. the room category was created priced in BDT; call this endpoint to also price it in USD).
  [Update](#update-resort-room-category-main-price) (`PUT .../prices/main?currency-id=`) **replaces** an
  existing currency's three rows in one call — see the note under that endpoint on why this is a
  soft-delete-and-recreate rather than an in-place edit.
- **Holiday** — [Create](#create-resort-room-category-holiday-price) (`POST .../prices/holidays`) creates a
  single date-bound `HOL` row; [Update](#update-resort-room-category-holiday-price)
  (`PUT .../prices/holidays/{id}`) edits it in place.
- **Special** — [Create](#create-resort-room-category-special-price) (`POST .../prices/specials`) creates a
  single date-bound `SPECIAL` row; [Update](#update-resort-room-category-special-price)
  (`PUT .../prices/specials/{id}`) edits it in place.

**`BAS`/`WKD`/`WKE` rows can only ever be created via [Create Resort Room
Category](resort-room-categories-api.md#create-resort-room-category) (the room category's first currency) or
[Create Resort Room Category Main Price](#create-resort-room-category-main-price) (every currency after
that) — they can never be deleted afterward, only replaced via [Update Resort Room Category Main
Price](#update-resort-room-category-main-price), and [Delete](#delete-resort-room-category-price) below rejects
them with `400 INVALID_ARGUMENT` (see [Error Responses](#error-responses)).** `HOL`/`SPECIAL` rows can be
created any number of times (one per date range/promotion), edited in place, and deleted.

`price_unit_id` and `currency_id` are supplied only at creation and are never fields on a response object —
responses always embed the resolved `price_unit`/`currency` instead (see [Data Model](#data-model)).
`currency_id` is additionally **immutable after creation**: it is never accepted on any Update request — for
Holiday/Special, `price_unit_id` *can* be changed via Update, but `currency_id` cannot (delete and recreate
instead); for Main, the whole point of [Update](#update-resort-room-category-main-price) is that a currency's
three rows are replaced wholesale, so there's no "change this row's currency" operation at all. A row's
`price_type` is likewise permanent, and is never submitted as an id at all — it's implied entirely by which of
the three shapes was used. All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). This entity has no `locale` field of its own, but the header's value still
shapes the response: it selects the locale-matched translation embedded on `resort_room_category.locale`,
`price_type.locale`, `price_unit.locale`, `currency.locale`, and each day's `day_of_week.locale`.

---

## Endpoints

| Method | Path                                                                                  | Description                                                                    |
|--------|---------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/main`          | Create a resort room category's BASE/WEEKDAY/WEEKEND price set for a currency  |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/holidays`      | Create a resort room category holiday price                                    |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/specials`      | Create a resort room category special price                                    |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices`               | Get a resort room category's prices, grouped by price type, for one currency   |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/count`         | Count the currencies for which the room category has a main price set          |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/main`          | Replace a resort room category's BASE/WEEKDAY/WEEKEND price set for a currency |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/holidays/{id}` | Update a resort room category holiday price                                    |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/specials/{id}` | Update a resort room category special price                                    |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/{id}`          | Delete a resort room category price                                            |

There is no `GET /{id}` — a single row is only ever seen embedded inside [List Resort Room Category
Prices](#list-resort-room-category-prices)' grouped response, since every row is always looked at in the
context of its currency's full BASE/WEEKDAY/WEEKEND/HOLIDAY/SPECIAL set.

---

## Data Model

### ResortRoomCategoryPrice

| Field                  | Type    | Required      | Constraints                                                                                      | Description                                                                                                |
|------------------------|---------|---------------|--------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| `id`                   | Long    | —             | read-only                                                                                        | Auto-generated identifier                                                                                  |
| `resort_room_category` | Object  | —             | read-only; see [ResortRoomCategory](resort-room-categories-api.md)                               | The room category this price belongs to. Resolved from the URL path, never a request body field            |
| `price_type`           | Object  | —             | read-only; see [PriceType](price-types-api.md); implied by which shape was used                  | Pricing rule classification (`BAS`/`WKD`/`WKE`/`HOL`/`SPECIAL`). Permanent after creation                  |
| `price_unit`           | Object  | —             | read-only; see [PriceUnit](price-units-api.md); resolved from `price_unit_id`                    | Billing unit (`PER_NIGHT`/`PER_DAY`/`PER_PERSON`, ...)                                                     |
| `currency`             | Object  | —             | read-only; see [Currency](currencies-api.md); resolved from `currency_id`                        | Currency of `price`. Immutable after creation                                                              |
| `name`                 | String  | Yes           | not blank, max 200 chars                                                                         | Display name, e.g. `Base Price`, `Weekend Price`, `Eid-ul-Fitr`                                            |
| `description`          | String  | —             | nullable                                                                                         | Optional description                                                                                       |
| `price`                | Decimal | Yes           | not null; numeric(12,2); >= 0; `WKD`/`WKE` additionally capped at the active `BAS` price         | Price amount                                                                                               |
| `valid_from`           | Date    | conditionally | required for `HOL`/`SPECIAL`, forbidden for `BAS`/`WKD`/`WKE`; must be <= `valid_to` if both set | Start of the date range the price applies to                                                               |
| `valid_to`             | Date    | conditionally | required for `HOL`/`SPECIAL`, forbidden for `BAS`/`WKD`/`WKE`                                    | End of the date range the price applies to                                                                 |
| `priority`             | Integer | —             | forced to `0` for `BAS`/`WKD`/`WKE`; free-form (default `0`) for `HOL`/`SPECIAL`                 | Tie-breaker when multiple rules could apply to the same date — higher wins (e.g. `HOL`=100, `SPECIAL`=200) |
| `days`                 | Array   | —             | see ResortRoomCategoryPriceDay below                                                             | The days of week this rule applies to — required (non-empty) for `WKD`/`WKE`, forbidden for other types    |

> **Note:** `price_unit_id` and `currency_id` (used to resolve `price_unit` and `currency`) are write-only
> inputs and never appear on this data model, because the response always returns the resolved objects instead.
> `price_type` itself is never submitted by the caller at all — each shape resolves it server-side (`BAS`/`WKD`/
> `WKE` for Main, `HOL` for Holiday, `SPECIAL` for Special). Each embeds only `id`, `code`, `sort_order`, and the
> Accept-Language-matched `locale` (plus, for `currency`, `numeric_code`/`symbol`/`decimal_places`/`is_default`)
> — nested collections such as a price type's/unit's own `price_scopes`, or a currency's `country`, are never
> embedded here.

### ResortRoomCategoryPriceDay

| Field         | Type   | Required | Constraints                                                                               | Description                          |
|---------------|--------|----------|-------------------------------------------------------------------------------------------|--------------------------------------|
| `id`          | Long   | —        | read-only                                                                                 | Auto-generated identifier            |
| `day_of_week` | Object | —        | read-only; see [DayOfWeek](days-of-week-api.md); resolved from an id in `day_of_week_ids` | The day of week this rule applies to |

### Request building blocks

Every create/update request is built from a small set of shared shapes, nested rather than flattened:

**PriceRequest** (embedded, never sent standalone — the common shape shared by all five leaf shapes below):

| Field           | Type    | Required | Validation                                              | Description               |
|-----------------|---------|----------|---------------------------------------------------------|---------------------------|
| `price_unit_id` | Long    | Yes      | Not null; must reference an existing, active price unit | Billing unit for this row |
| `name`          | String  | Yes      | Not blank, max 200 chars                                | Display name for this row |
| `description`   | String  | —        | Nullable                                                | Optional description      |
| `price`         | Decimal | Yes      | Not null; numeric(12,2); >= 0                           | Price amount              |

- **BasePriceRequest** — exactly the fields above, no more.
- **WeekdayPriceRequest** / **WeekendPriceRequest** — the fields above, plus `day_of_week_ids` (`Long[]`,
  `@NotEmpty`, each id must reference an existing, active day of week).
- **DateRangePriceRequest** (the shared shape behind Holiday/Special) — the fields above, plus `valid_from`
  (Date, `@NotNull`), `valid_to` (Date, `@NotNull`), and `priority` (Integer, nullable, defaults to `0`).

**MainPriceRequest** — the request body for both [Create](#create-resort-room-category-main-price) and
[Update](#update-resort-room-category-main-price) Main:

| Field                | Type   | Required | Validation                        | Description                                    |
|----------------------|--------|----------|-----------------------------------|------------------------------------------------|
| `base_price_request` | Object | Yes      | `@Valid`; see BasePriceRequest    | The `BAS` row's own fields                     |
| `weekday_price`      | Object | Yes      | `@Valid`; see WeekdayPriceRequest | The `WKD` row's own fields, including its days |
| `weekend_price`      | Object | Yes      | `@Valid`; see WeekendPriceRequest | The `WKE` row's own fields, including its days |

[Create](#create-resort-room-category-main-price) additionally requires `currency_id` (Long, `@NotNull`) at the
top level, alongside the three objects above. [Update](#update-resort-room-category-main-price) does **not**
accept `currency_id` in the body at all — the currency being replaced is identified by the `currency-id` query
parameter instead (see that endpoint).

Holiday/Special requests are simply `DateRangePriceRequest` plus, on Create only, a top-level `currency_id`
(Long, `@NotNull`) — Update never accepts `currency_id`.

### Price type rules

**Not every price type/price unit is valid here** — a price type or price unit is only usable for a resort room
category price if it's assigned to the `ROOM_CATEGORY` [price scope](price-scopes-api.md). This is enforced at
the database level on every write (see [Error Responses](#error-responses)); build the picker correctly by
fetching the allowed sets first via `GET /api/v1/price-types?priceScopeCodes=ROOM_CATEGORY` and
`GET /api/v1/price-units?priceScopeCodes=ROOM_CATEGORY` and only offering those ids.

Each `price_type` code additionally has its own shape requirements, ultimately enforced by database triggers —
**and, since Update now uses the same typed request shapes as Create (`WeekdayPriceRequest`/
`WeekendPriceRequest`'s `@NotEmpty day_of_week_ids`, `DateRangePriceRequest`'s `@NotNull valid_from`/
`valid_to`), every one of the "required"/"forbidden" rules below is bean-validated up front on both Create
*and* Update, not just Create.** The one shape rule no endpoint pre-validates is that `price_type_id`/
`price_unit_id` must be assigned to the `ROOM_CATEGORY` [price scope](price-scopes-api.md) — see the note under
[Error Responses](#error-responses).

| `price_type` code | `valid_from` / `valid_to`       | `days`                          | `priority`              | `price` cap                                                              |
|-------------------|---------------------------------|---------------------------------|-------------------------|--------------------------------------------------------------------------|
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
has a price group for returns `409 CONFLICT`. `weekday_price.price`/`weekend_price.price` cannot exceed
`base_price_request.price` (`400 INVALID_ARGUMENT` otherwise, mirroring the same rule on [Create Resort Room
Category](resort-room-categories-api.md#create-resort-room-category)). Every price unit id (one per nested
object) must reference an existing, active [Price Unit](price-units-api.md) — each of the three can be
different. Each nested object's `day_of_week_ids` (`weekday_price`/`weekend_price` only) must be non-empty and
every id must reference an existing, active [Day of Week](days-of-week-api.md). `currency_id` must reference an
existing, active [Currency](currencies-api.md).

### Path Parameters

| Parameter          | Type | Description                    |
|--------------------|------|--------------------------------|
| `resort-id`        | Long | ID of the owning resort        |
| `room-category-id` | Long | ID of the resort room category |

### Request Body

```json
{
  "currency_id": 2,
  "base_price_request": {
    "price_unit_id": 1,
    "name": "Base Price",
    "description": null,
    "price": 220.00
  },
  "weekday_price": {
    "price_unit_id": 1,
    "name": "Weekday Price",
    "description": null,
    "price": 200.00,
    "day_of_week_ids": [
      1,
      2,
      3,
      4
    ]
  },
  "weekend_price": {
    "price_unit_id": 1,
    "name": "Weekend Price",
    "description": null,
    "price": 260.00,
    "day_of_week_ids": [
      5,
      6
    ]
  }
}
```

### Request Fields

| Field                              | Type    | Required | Validation                                                                                                                |
|------------------------------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------|
| `currency_id`                      | Long    | Yes      | Not null; must reference an existing, active currency; must not already have an active `BAS` price for this room category |
| `base_price_request`               | Object  | Yes      | `@Valid`; see [BasePriceRequest](#request-building-blocks)                                                                |
| `base_price_request.price_unit_id` | Long    | Yes      | Not null; must reference an existing, active price unit                                                                   |
| `base_price_request.name`          | String  | Yes      | Not blank, max 200 chars                                                                                                  |
| `base_price_request.description`   | String  | —        | Nullable                                                                                                                  |
| `base_price_request.price`         | Decimal | Yes      | Not null; numeric(12,2); >= 0                                                                                             |
| `weekday_price`                    | Object  | Yes      | `@Valid`; see [WeekdayPriceRequest](#request-building-blocks)                                                             |
| `weekday_price.price_unit_id`      | Long    | Yes      | Not null; must reference an existing, active price unit                                                                   |
| `weekday_price.name`               | String  | Yes      | Not blank, max 200 chars                                                                                                  |
| `weekday_price.description`        | String  | —        | Nullable                                                                                                                  |
| `weekday_price.price`              | Decimal | Yes      | Not null; numeric(12,2); >= 0; cannot exceed `base_price_request.price`                                                   |
| `weekday_price.day_of_week_ids`    | Long[]  | Yes      | Not empty; each id must reference an existing, active day of week                                                         |
| `weekend_price`                    | Object  | Yes      | `@Valid`; see [WeekendPriceRequest](#request-building-blocks)                                                             |
| `weekend_price.price_unit_id`      | Long    | Yes      | Not null; must reference an existing, active price unit                                                                   |
| `weekend_price.name`               | String  | Yes      | Not blank, max 200 chars                                                                                                  |
| `weekend_price.description`        | String  | —        | Nullable                                                                                                                  |
| `weekend_price.price`              | Decimal | Yes      | Not null; numeric(12,2); >= 0; cannot exceed `base_price_request.price`                                                   |
| `weekend_price.day_of_week_ids`    | Long[]  | Yes      | Not empty; each id must reference an existing, active day of week                                                         |

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
[Price Unit](price-units-api.md); `currency_id` an existing, active [Currency](currencies-api.md). **`currency_id`
must already have an active main (`BAS`) price for this room category** — see [Create Resort Room Category Main
Price](#create-resort-room-category-main-price) — otherwise the call fails with `404 ENTITY_NOT_FOUND`; a
currency with no main price set cannot be given a holiday/special price first. The room
category may have at most one active `HOL` price per exact `(price_unit, currency)` combination — creating a
second one for the same pair returns `409 CONFLICT`; use a different `price_unit_id`, or
[update](#update-resort-room-category-holiday-price)/[delete](#delete-resort-room-category-price) the existing
row first, to change a holiday's price.

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
  "price": 250.00,
  "valid_from": "2026-03-20",
  "valid_to": "2026-03-22",
  "priority": 100
}
```

### Request Fields

| Field           | Type    | Required | Validation                                                                                                           |
|-----------------|---------|----------|----------------------------------------------------------------------------------------------------------------------|
| `currency_id`   | Long    | Yes      | Not null; must reference an existing, active currency                                                                |
| `price_unit_id` | Long    | Yes      | Not null; must reference an existing, active price unit                                                              |
| `name`          | String  | Yes      | Not blank, max 200 chars                                                                                             |
| `description`   | String  | —        | Nullable                                                                                                             |
| `price`         | Decimal | Yes      | Not null; numeric(12,2); >= 0                                                                                        |
| `valid_from`    | Date    | Yes      | Not null; must be <= `valid_to` if both set (database-trigger-enforced)                                              |
| `valid_to`      | Date    | Yes      | Not null                                                                                                             |
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
|--------------------|------|--------------------------------|
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

## List Resort Room Category Prices

`GET /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices`

Returns every active price the room category has in one currency, bucketed by `price_type` instead of as a flat
list: `main.base`, `main.weekday`, and `main.weekend` are each at most one object (or `null` if the room category
has none active in that currency — normally only possible for `weekday`/`weekend`, since `base` always exists per
currency), while `holidays`/`specials` are arrays, since a room category can have any number of active
`HOL`/`SPECIAL` rows per currency (e.g. one per holiday, one per promotion). There is no pagination. **Every
entry includes its `days`**, unlike list endpoints on other entities in this codebase — useful here since
`weekday`/`weekend` are defined by which days they apply to. This is also the only way to read a single row's
full detail, since there is no `GET /{id}`.

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
      "base": {
        "id": 10,
        "resort_room_category": {
          "id": 10,
          "code": "DLX-SEA",
          "sort_order": 1,
          "locale": {
            "...": "..."
          }
        },
        "price_type": {
          "id": 1,
          "code": "BAS",
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
        "resort_room_category": {
          "id": 10,
          "code": "DLX-SEA",
          "sort_order": 1,
          "locale": {
            "...": "..."
          }
        },
        "price_type": {
          "id": 3,
          "code": "WKE",
          "sort_order": 3,
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
      }
    },
    "holidays": [],
    "specials": []
  }
}
```

---

## Count Resort Room Category Main Prices

`GET /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/count`

Returns how many currencies, and which ones, the room category has an active main (`BAS`) price for.
`BAS`/`WKD`/`WKE` are always created and replaced together (see [Create](#create-resort-room-category-main-price)/
[Update](#update-resort-room-category-main-price) Main above), so the presence of an active `BAS` row is a
reliable signal that the room category is fully priced in that currency. `HOL`/`SPECIAL` rows are not
considered — this only reflects the main price set.

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

`PUT /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/main?currency-id={currency-id}`

**Replaces, rather than edits in place, one currency's entire `BAS`/`WKD`/`WKE` set** — the three existing
active rows for `currency-id` (found by `resort_room_category_id`/`price_type`/`currency_id`, the same lookup
[Create Resort Room Category Main Price](#create-resort-room-category-main-price) uses for its own duplicate
check) are all soft-deleted, and three brand-new rows are created in their place, mirroring [Set Weekly
Schedule](resort-facility-operating-hours-api.md#set-weekly-schedule) on the Resort Facility Operating Hours
API. `currency-id` must already have an active `BAS`/`WKD`/`WKE` set for this room category — if any of the
three is missing, the call fails with `404 ENTITY_NOT_FOUND` rather than partially creating what's missing; use
[Create Resort Room Category Main Price](#create-resort-room-category-main-price) instead for a currency that
has never been priced. `weekday_price.price`/`weekend_price.price` cannot exceed `base_price_request.price`,
the same rule as Create.

**Every row id churns on every call, including for the day-of-week rows on `weekday_price`/`weekend_price`** —
because the old rows are soft-deleted and new ones created, a previously-fetched price `id` should not be
cached or relied on after this endpoint is called; re-fetch via [List Resort Room Category
Prices](#list-resort-room-category-prices) afterward. Unlike [Set Weekly
Schedule](resort-facility-operating-hours-api.md#set-weekly-schedule), the response here only returns the new
`BAS` row's id — fetch the list endpoint to see the new `WKD`/`WKE` ids too.

`currency_id` is **not** a body field on this endpoint — the currency being replaced is identified by the
`currency-id` query parameter, since the whole point of this call is to replace that currency's set (there is
nothing to "change the currency to").

### Path Parameters

| Parameter          | Type | Description                    |
|--------------------|------|--------------------------------|
| `resort-id`        | Long | ID of the owning resort        |
| `room-category-id` | Long | ID of the resort room category |

### Query Parameters

| Parameter     | Type | Required | Description                                          |
|---------------|------|----------|------------------------------------------------------|
| `currency-id` | Long | Yes      | ID of the currency whose price set is being replaced |

### Request Body

```json
{
  "base_price_request": {
    "price_unit_id": 1,
    "name": "Base Price",
    "description": null,
    "price": 230.00
  },
  "weekday_price": {
    "price_unit_id": 1,
    "name": "Weekday Price",
    "description": null,
    "price": 210.00,
    "day_of_week_ids": [
      1,
      2,
      3,
      4
    ]
  },
  "weekend_price": {
    "price_unit_id": 1,
    "name": "Weekend Price",
    "description": null,
    "price": 270.00,
    "day_of_week_ids": [
      5,
      6
    ]
  }
}
```

### Request Fields

Same shape as [Create Resort Room Category Main Price](#create-resort-room-category-main-price) — see the
Request Fields table there — except there is no top-level `currency_id` field (see `currency-id` under [Query
Parameters](#query-parameters-2) above).

### Response `200 OK`

```json
{
  "success": true,
  "id": 25
}
```

`id` is the new `BAS` row's id.

---

## Update Resort Room Category Holiday Price

`PUT /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/holidays/{id}`

Updates `price_unit_id`, `name`, `description`, `price`, `valid_from`, `valid_to`, and `priority` on an
existing `HOL` row, in place — the row keeps its `id`. `currency_id` cannot be changed; to reclassify a
holiday's currency, delete it via [Delete](#delete-resort-room-category-price) and create a new one via
[Create Resort Room Category Holiday Price](#create-resort-room-category-holiday-price) instead.

### Path Parameters

| Parameter          | Type | Description                           |
|--------------------|------|---------------------------------------|
| `resort-id`        | Long | ID of the owning resort               |
| `room-category-id` | Long | ID of the owning resort room category |
| `id`               | Long | ID of the resort room category price  |

### Request Body

```json
{
  "price_unit_id": 1,
  "name": "Eid-ul-Fitr",
  "description": "Holiday surcharge, extended.",
  "price": 275.00,
  "valid_from": "2026-03-19",
  "valid_to": "2026-03-23",
  "priority": 100
}
```

### Request Fields

Same shape as [Create Resort Room Category Holiday Price](#create-resort-room-category-holiday-price) — see
the Request Fields table there — except there is no `currency_id` field.

### Response `200 OK`

```json
{
  "success": true,
  "id": 23
}
```

---

## Update Resort Room Category Special Price

`PUT /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/specials/{id}`

Identical shape and rules to [Update Resort Room Category Holiday
Price](#update-resort-room-category-holiday-price) above, for a `SPECIAL` row instead of `HOL`.

### Path Parameters

| Parameter          | Type | Description                           |
|--------------------|------|---------------------------------------|
| `resort-id`        | Long | ID of the owning resort               |
| `room-category-id` | Long | ID of the owning resort room category |
| `id`               | Long | ID of the resort room category price  |

### Request Body

```json
{
  "price_unit_id": 1,
  "name": "New Year's Eve",
  "description": "Peak surcharge, extended.",
  "price": 420.00,
  "valid_from": "2026-12-30",
  "valid_to": "2027-01-01",
  "priority": 200
}
```

### Request Fields

Same shape as [Update Resort Room Category Holiday Price](#update-resort-room-category-holiday-price) — see
the Request Fields table there.

### Response `200 OK`

```json
{
  "success": true,
  "id": 24
}
```

---

## Delete Resort Room Category Price

`DELETE /api/v1/resorts/{resort-id}/room-categories/{room-category-id}/prices/{id}`

Soft-deletes the resort room category price (and, via cascade, its day rows). The record is not removed from
the database but will no longer appear in any response. **Only `HOL`/`SPECIAL` rows can be deleted** — a
`BAS`/`WKD`/`WKE` row (identified by the target `id`'s own `price_type`) is rejected with `400
INVALID_ARGUMENT`, since a room category must always keep exactly one active BASE/WEEKDAY/WEEKEND set per
currency (replace it instead via [Update Resort Room Category Main
Price](#update-resort-room-category-main-price)).

### Path Parameters

| Parameter          | Type | Description                           |
|--------------------|------|---------------------------------------|
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

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
|-------------|----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing `currency-id` query parameter on [List](#list-resort-room-category-prices)/[Update Main](#update-resort-room-category-main-price); missing/invalid required fields on any create/update endpoint (`currency_id`/`price_unit_id`/`name`/`price` null, `day_of_week_ids` empty on `weekday_price`/`weekend_price`, or — for Holiday/Special — `valid_from`/`valid_to` null); `weekday_price.price`/`weekend_price.price` greater than `base_price_request.price` on [Create](#create-resort-room-category-main-price)/[Update](#update-resort-room-category-main-price) Main (checked at the application layer before the write); the target row's `price_type` being `BAS`/`WKD`/`WKE` on [Delete](#delete-resort-room-category-price) (checked at the application layer before the write)                                                                                                                              |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found; resort room category not found for the given `resort-id`/`room-category-id` pair (including a `room-category-id` that belongs to a different resort); price not found for the given `room-category-id`/`id` pair (including an `id` that belongs to a different room category) on [Update Holiday](#update-resort-room-category-holiday-price)/[Update Special](#update-resort-room-category-special-price)/[Delete](#delete-resort-room-category-price); the currency's `BAS`/`WKD`/`WKE` set not found (any of the three missing) on [Update Main](#update-resort-room-category-main-price); `currency_id` has no active main (`BAS`) price yet on [Create Holiday](#create-resort-room-category-holiday-price)/[Create Special](#create-resort-room-category-special-price) (checked at the application layer before the write); the price unit, currency, or any day-of-week id not found; `currency-id` not found on [List](#list-resort-room-category-prices)/[Update Main](#update-resort-room-category-main-price) |
| 409         | `CONFLICT`                 | On [Create Main](#create-resort-room-category-main-price): the room category already has an active `BAS` price for the given `currency_id`. On [Create Holiday](#create-resort-room-category-holiday-price)/[Create Special](#create-resort-room-category-special-price): the room category already has an active price with the same price type/`price_unit_id`/`currency_id` combination. Both checked at the application layer before the write                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| 409         | `DATA_INTEGRITY_VIOLATION` | A foreign key (`price_type_id`, `price_unit_id`, `currency_id`, `resort_room_category_id`, `day_of_week_id`) or the underlying unique constraints somehow reference/duplicate a row unexpectedly — should not normally be reachable, since each is resolved and validated before the write                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| 500         | `INTERNAL_SERVER_ERROR`    | The `price_type_id`/`price_unit_id` pair used by the write isn't assigned to the `ROOM_CATEGORY` price scope — this shape rule is never pre-validated on any endpoint, see [Price type rules](#price-type-rules). In this case the database trigger's raised exception isn't a recognized constraint-violation type, so it falls through to the generic error handler as a `500` rather than a `400`/`409`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
