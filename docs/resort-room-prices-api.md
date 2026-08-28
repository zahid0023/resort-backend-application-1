# Resort Room Prices API

Base URL: `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/rooms/{resort-room-id}/prices`

Resort room pricing mirrors [Resort Room Category Prices](resort-room-category-prices-api.md) — split the
same way across two concepts (Main, Special), each with its own table and its own request/response
shape. **The difference is that every row here is an optional, per-currency override, not an independently
required price.** A resort room only needs a row for a currency it wants to price differently than its
category; for any currency with no override, the room's price for that currency is **inherited wholesale**
(Main + Special together, as one bundle) from its
[resort room category's prices](resort-room-category-prices-api.md).

- **Main** — a room's overridden per-currency rate structure: `base_price`, `weekday_price`, and
  `weekend_price`, one row per currency the room overrides. Same shape and rules as the category's Main.
- **Special** — any number of date-ranged rules per overridden currency. Requires the room to already have its
  own active Main override for that currency (see [Create Resort Room Special
  Price](#create-resort-room-special-price)) — a room can't override a special rate without first overriding
  the base rate it layers on top of. There is no separate holiday concept: a holiday is just a Special row
  whose `name`/`description` say so, exactly like a promotion or surcharge.

**Which days of week count as WEEKDAY vs. WEEKEND is not part of this API** — same shared, resort-level
weekly schedule the category API uses. See [Resort Room Category Prices](resort-room-category-prices-api.md)
for the full precedence rules (Special > Weekday/Weekend > Base) — they apply identically here,
just resolved against whichever bundle (the room's own, or its category's) `inherited` on
[List](#list-resort-room-prices) indicates is in effect for that currency.

**`inherited` on the [List](#list-resort-room-prices) response is the key field**: `true` means the room has no
override for the requested currency and every field below it (`main`/`specials`) is the room's
*category's* bundle, verbatim; `false` means every field is the room's own override rows. A `main`/`specials`
entry from an inherited bundle has `resort_room: null`, since it isn't actually one of this room's own rows.

Resort room prices are always reached nested under their owning resort room (which is itself nested under its
resort room category); there is no top-level `/api/v1/resort-room-prices` route. Every endpoint validates the
`{resort-id}`/`{resort-room-category-id}`/`{resort-room-id}` triple first — an unknown resort, room category, or room
(or one that exists but belongs to a different parent) returns `404 ENTITY_NOT_FOUND`.

**Main can only ever be created via [Create Resort Room Main
Price](#create-resort-room-main-price)** — unlike the category level, there is no "first currency created
alongside the parent" path, since a room needs no price of its own at creation time (it simply inherits).
There is no way to delete just a currency's main override while leaving its Special overrides behind;
[Delete Resort Room Prices By Currency](#delete-resort-room-prices-by-currency) removes both at once, and
reverts the room back to inheriting that currency's price from its category. **Unlike the category level,
there is no "at least one currency must remain" restriction** — a room is allowed to have zero overrides (every
currency fully inherited); deleting a room's last override just means the room goes back to being 100%
inherited, not an error.

`price_unit_id` and `currency_id` are supplied only at creation and are never fields on a response object —
responses always embed the resolved `price_unit`/`currency` instead. `currency_id` is immutable after
creation, same as the category level.

**`Accept-Language` is required on every endpoint below, with no exceptions** — same global rule as everywhere
else in this API (see [Error Responses](#error-responses)).

---

## Endpoints

| Method | Path                                                                                                             | Description                                                    |
|--------|--------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| POST   | `.../rooms/{resort-room-id}/prices/main`          | Create a resort room's main price override for a currency              |
| POST   | `.../rooms/{resort-room-id}/prices/specials`      | Create a resort room special price override                            |
| GET    | `.../rooms/{resort-room-id}/prices`               | Get a resort room's prices for one currency — own override, or its category's, whichever applies |
| GET    | `.../rooms/{resort-room-id}/prices/count`         | Count the currencies for which the room has its own main price override |
| PUT    | `.../rooms/{resort-room-id}/prices/main`          | Update a resort room's main price override for a currency, in place    |
| PUT    | `.../rooms/{resort-room-id}/prices/specials/{id}` | Update a resort room special price override                            |
| DELETE | `.../rooms/{resort-room-id}/prices/specials/{id}` | Delete a resort room special price override                            |
| DELETE | `.../rooms/{resort-room-id}/prices`               | Delete every override (main + special) for one currency — reverts to inherited |

Paths above are relative to the Base URL. There is no `GET /{id}` — same reasoning as the category level, see
[List Resort Room Prices](#list-resort-room-prices).

---

## Data Model

### ResortRoomMainPrice

Same shape as [ResortRoomCategoryMainPrice](resort-room-category-prices-api.md#data-model), with `resort_room`
in place of `resort_room_category`. When a bundle is inherited (`inherited: true` on the parent [List
response](#list-resort-room-prices)), `resort_room` is `null`, since the row is really the category's, not the
room's own.

| Field           | Type    | Required | Constraints                                                                        | Description                                                      |
|-----------------|---------|----------|-------------------------------------------------------------------------------------|--------------------------------------------------------------------|
| `id`            | Long    | —        | read-only                                                                          | Auto-generated identifier                                        |
| `resort_room`   | Object  | —        | read-only; see [ResortRoom](resort-rooms-api.md); `null` when this bundle is inherited from the category | The room this price override belongs to |
| `price_unit`    | Object  | —        | read-only; see [PriceUnit](price-units-api.md); resolved from `price_unit_id`      | Billing unit shared by all three prices below                    |
| `currency`      | Object  | —        | read-only; see [Currency](currencies-api.md); resolved from `currency_id`          | Currency of the three prices below. Immutable after creation     |
| `base_price`    | Decimal | Yes      | not null; >= 0; at most 10 integer/2 fraction digits (`numeric(12,2)`)             | Default rack rate                                                 |
| `weekday_price` | Decimal | Yes      | not null; >= 0; at most 10 integer/2 fraction digits; cannot exceed `base_price`   | Applies on weekday dates                                          |
| `weekend_price` | Decimal | Yes      | not null; >= 0; at most 10 integer/2 fraction digits; cannot exceed `base_price`   | Applies on weekend dates                                          |
| `weekday_days`  | Array   | —        | read-only; see [ResortWeeklyScheduleDay](resort-weekly-schedule-api.md#data-model) | The resort's shared weekly-schedule days classified as weekday    |
| `weekend_days`  | Array   | —        | read-only; see [ResortWeeklyScheduleDay](resort-weekly-schedule-api.md#data-model) | The resort's shared weekly-schedule days classified as weekend    |

### ResortRoomSpecialPrice

Mirrors [ResortRoomCategorySpecialPrice](resort-room-category-prices-api.md#data-model) with `resort_room` in
place of `resort_room_category` (same `null`-when-inherited rule as Main above). There is no separate holiday
concept — a holiday is just a Special row whose `name` says so (e.g. `Eid-ul-Fitr`).

| Field           | Type    | Required | Constraints                                                                        | Description                                                                                |
|-----------------|---------|----------|-------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|
| `id`            | Long    | —        | read-only                                                                          | Auto-generated identifier                                                                    |
| `resort_room`   | Object  | —        | read-only; see [ResortRoom](resort-rooms-api.md); `null` when this bundle is inherited from the category | The room this price override belongs to |
| `price_unit`    | Object  | —        | read-only; see [PriceUnit](price-units-api.md); resolved from `price_unit_id`      | Billing unit shared by the two prices below                                                  |
| `currency`      | Object  | —        | read-only; see [Currency](currencies-api.md); resolved from `currency_id`          | Currency of the two prices below. Immutable after creation                                   |
| `name`          | String  | Yes      | not blank, max 200 chars                                                          | Display name, e.g. `Eid-ul-Fitr`, `New Year's Eve`                                           |
| `description`   | String  | —        | nullable                                                                           | Optional description                                                                         |
| `valid_from`    | Date    | Yes      | not null; must be <= `valid_to`                                                    | Start of the date range this rule applies to                                                 |
| `valid_to`      | Date    | Yes      | not null                                                                           | End of the date range this rule applies to                                                   |
| `weekday_price` | Decimal | Yes      | not null; >= 0; at most 10 integer/2 fraction digits; **no cap vs. `base_price`**  | Applies on weekday dates within [`valid_from`, `valid_to`]                                   |
| `weekend_price` | Decimal | Yes      | not null; >= 0; at most 10 integer/2 fraction digits; **no cap vs. `base_price`**  | Applies on weekend dates within [`valid_from`, `valid_to`]                                   |
| `priority`      | Integer | —        | nullable, defaults to `0`                                                          | Tie-breaker when multiple Special rules could apply to the same date — higher wins           |
| `weekday_days`  | Array   | —        | read-only; see [ResortWeeklyScheduleDay](resort-weekly-schedule-api.md#data-model) | The resort's shared weekly-schedule days classified as weekday                               |
| `weekend_days`  | Array   | —        | read-only; see [ResortWeeklyScheduleDay](resort-weekly-schedule-api.md#data-model) | The resort's shared weekly-schedule days classified as weekend                               |

### Price unit rules

**Not every price unit is valid here** — a price unit is only usable for a resort room price override if it's
assigned to the `ROOM` [price scope](price-scopes-api.md) — note this is a **different** scope than the
category level's `ROOM_CATEGORY`. Build the picker correctly by fetching the allowed set first via
`GET /api/v1/price-units?priceScopeCodes=ROOM` and only offering those ids.

A room may have at most one **active** main price override per currency — creating a second one for the same
currency returns `409 CONFLICT`, backed by a database-level partial unique index
(`uq_resort_room_main_price_active`).

---

## Create Resort Room Main Price

`POST .../rooms/{resort-room-id}/prices/main`

Adds a currency's main price override to a resort room — from this point on, the room uses this row instead of
its category's price for this currency. `currency_id` must not already have an active override for this room.
`weekday_price`/`weekend_price` cannot exceed `base_price`. `price_unit_id` must be assigned to the `ROOM`
price scope (see [Price unit rules](#price-unit-rules)); `currency_id` an existing, active
[Currency](currencies-api.md).

**The resort must already have an active weekly schedule** — same requirement as [Create Resort Room Category
Main Price](resort-room-category-prices-api.md#create-resort-room-category-main-price).

### Path Parameters

| Parameter                    | Type | Description                    |
|-------------------------------|------|--------------------------------|
| `resort-id`                   | Long | ID of the owning resort        |
| `resort-room-category-id`     | Long | ID of the owning resort room category |
| `room-id`                     | Long | ID of the resort room          |

### Request Body

```json
{
  "currency_id": 2,
  "price_unit_id": 4,
  "base_price": 260.00,
  "weekday_price": 240.00,
  "weekend_price": 300.00
}
```

### Request Fields

| Field           | Type    | Required | Validation                                                                                          |
|-----------------|---------|----------|-------------------------------------------------------------------------------------------------------|
| `currency_id`   | Long    | Yes      | Not null; must reference an existing, active currency; must not already have an active override for this room |
| `price_unit_id` | Long    | Yes      | Not null; must reference an existing, active price unit assigned to the `ROOM` price scope           |
| `base_price`    | Decimal | Yes      | Not null; >= 0; at most 10 integer/2 fraction digits                                                  |
| `weekday_price` | Decimal | Yes      | Not null; >= 0; at most 10 integer/2 fraction digits; cannot exceed `base_price`                      |
| `weekend_price` | Decimal | Yes      | Not null; >= 0; at most 10 integer/2 fraction digits; cannot exceed `base_price`                      |

### Response `201 Created`

```json
{
  "success": true,
  "id": 40
}
```

---

## Create Resort Room Special Price

`POST .../rooms/{resort-room-id}/prices/specials`

Creates a Special override for the room — a date-ranged rule with its own weekday/weekend price pair, used
both for holidays and for promotions/surcharges. Same shape and rules as [Create Resort Room Category Special
Price](resort-room-category-prices-api.md#create-resort-room-category-special-price). **`currency_id` must
already have an active room-level main price override** (see [Create Resort Room Main
Price](#create-resort-room-main-price)) — otherwise the call fails with `404 ENTITY_NOT_FOUND`. This checks the
room's *own* main override, never the category's — a room inheriting a currency entirely cannot be given a
special override for it without first overriding that currency's main price too.

### Path Parameters

| Parameter                | Type | Description                            |
|---------------------------|------|-----------------------------------------|
| `resort-id`                | Long | ID of the owning resort                 |
| `resort-room-category-id`  | Long | ID of the owning resort room category   |
| `room-id`                  | Long | ID of the resort room                   |

### Request Body

```json
{
  "currency_id": 1,
  "price_unit_id": 4,
  "name": "Eid-ul-Fitr",
  "description": "Holiday surcharge for this specific room.",
  "valid_from": "2026-06-16",
  "valid_to": "2026-06-20",
  "weekday_price": 150.00,
  "weekend_price": 175.00,
  "priority": 100
}
```

### Request Fields

Same shape as [Create Resort Room Category Special
Price](resort-room-category-prices-api.md#create-resort-room-category-special-price).

### Response `201 Created`

```json
{
  "success": true,
  "id": 13
}
```

---

## List Resort Room Prices

`GET .../rooms/{resort-room-id}/prices?currency-id={currency-id}`

Returns the room's active prices for one currency — its own override bundle if it has one, otherwise its
category's bundle. Same bucketed-by-type shape as [List Resort Room Category
Prices](resort-room-category-prices-api.md#list-resort-room-category-prices), plus the `inherited` flag.

### Path Parameters

| Parameter                  | Type | Description                            |
|------------------------------|------|-----------------------------------------|
| `resort-id`                  | Long | ID of the owning resort                 |
| `resort-room-category-id`    | Long | ID of the owning resort room category   |
| `room-id`                    | Long | ID of the resort room                   |

### Query Parameters

| Parameter     | Type | Required | Description                           |
|----------------|------|----------|-----------------------------------------|
| `currency-id`  | Long | Yes      | ID of the currency to return prices for |

### Response `200 OK` — room has its own override

```json
{
  "data": {
    "currency": { "id": 1, "code": "USD", "...": "..." },
    "inherited": false,
    "main": {
      "id": 40,
      "resort_room": { "id": 5, "code": "DLX-101", "...": "..." },
      "price_unit": { "id": 4, "code": "PER_NIGHT", "...": "..." },
      "currency": { "id": 1, "code": "USD", "...": "..." },
      "base_price": 260.00,
      "weekday_price": 240.00,
      "weekend_price": 300.00,
      "weekday_days": ["..."],
      "weekend_days": ["..."]
    },
    "specials": []
  }
}
```

### Response `200 OK` — room has no override, inherited from category

```json
{
  "data": {
    "currency": { "id": 1, "code": "USD", "...": "..." },
    "inherited": true,
    "main": {
      "id": 10,
      "resort_room": null,
      "price_unit": { "id": 1, "code": "PER_NIGHT", "...": "..." },
      "currency": { "id": 1, "code": "USD", "...": "..." },
      "base_price": 200.00,
      "weekday_price": 180.00,
      "weekend_price": 200.00,
      "weekday_days": ["..."],
      "weekend_days": ["..."]
    },
    "specials": []
  }
}
```

---

## Count Resort Room Main Price Overrides

`GET .../rooms/{resort-room-id}/prices/count`

Returns how many currencies, and which ones, the room has its **own** active main price override for.
Inherited currencies are not counted — this only reflects the room's own overrides.

### Path Parameters

Same as [List Resort Room Prices](#list-resort-room-prices).

### Response `200 OK`

```json
{
  "count": 1,
  "codes": [
    "USD"
  ]
}
```

---

## Update Resort Room Main Price

`PUT .../rooms/{resort-room-id}/prices/main?currency-id={currency-id}`

Updates one currency's main price override in place. `currency-id` must already have an active override for
this room — otherwise `404 ENTITY_NOT_FOUND`; use [Create Resort Room Main
Price](#create-resort-room-main-price) instead for a currency this room doesn't yet override.

### Path Parameters

Same as [List Resort Room Prices](#list-resort-room-prices).

### Query Parameters

| Parameter     | Type | Required | Description                                          |
|----------------|------|----------|---------------------------------------------------------|
| `currency-id`  | Long | Yes      | ID of the currency whose override is being updated     |

### Request Body

```json
{
  "price_unit_id": 4,
  "base_price": 270.00,
  "weekday_price": 250.00,
  "weekend_price": 310.00
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 40
}
```

---

## Update Resort Room Special Price

`PUT .../rooms/{resort-room-id}/prices/specials/{id}`

Same fields and rules as [Update Resort Room Category Special
Price](resort-room-category-prices-api.md#update-resort-room-category-special-price).

### Path Parameters

| Parameter                  | Type | Description                            |
|------------------------------|------|-----------------------------------------|
| `resort-id`                  | Long | ID of the owning resort                 |
| `resort-room-category-id`    | Long | ID of the owning resort room category   |
| `room-id`                    | Long | ID of the resort room                   |
| `id`                          | Long | ID of the resort room special price     |

### Response `200 OK`

```json
{
  "success": true,
  "id": 13
}
```

---

## Delete Resort Room Special Price

`DELETE .../rooms/{resort-room-id}/prices/specials/{id}`

Soft-deletes the Special override row.

### Response `200 OK`

```json
{
  "success": true,
  "id": 13
}
```

---

## Delete Resort Room Prices By Currency

`DELETE .../rooms/{resort-room-id}/prices?currency-id={currency-id}`

Soft-deletes every active override for one currency — main plus any special — atomically. **Reverts
the room to inheriting that currency's price from its category.** Unlike the category-level equivalent, there
is no minimum-currency restriction — a room may end up with zero overrides. Fails with `404 ENTITY_NOT_FOUND`
if `currency-id` has no active override for this room.

### Query Parameters

| Parameter     | Type | Required | Description                                       |
|----------------|------|----------|------------------------------------------------------|
| `currency-id`  | Long | Yes      | ID of the currency whose overrides are being deleted |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

`id` is `currency-id`.

---

## Error Responses

Same structure as every other endpoint in this API:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "ResortRoomSpecialPrice not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                              |
|-------------|-----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`          | Missing or blank `Accept-Language` header; missing `currency-id` query parameter on [List](#list-resort-room-prices)/[Update Main](#update-resort-room-main-price)/[Delete By Currency](#delete-resort-room-prices-by-currency); missing/invalid required fields on any create/update endpoint; `weekday_price`/`weekend_price` greater than `base_price` on Main create/update |
| 404         | `ENTITY_NOT_FOUND`          | Resort/room category/room not found for the given path triple; price not found for the given `room-id`/`id` pair on Update/Delete Special; no active override found for the given `currency-id` on [Update Main](#update-resort-room-main-price)/[Delete By Currency](#delete-resort-room-prices-by-currency); `currency_id` has no active room-level main override yet on [Create Special](#create-resort-room-special-price); the price unit or currency not found |
| 409         | `CONFLICT`                  | On [Create Main](#create-resort-room-main-price): the room already has an active override for the given `currency_id` — can also surface as `409 DATA_INTEGRITY_VIOLATION` on a concurrent-insert race                                                                                                                                                                            |
| 409         | `DATA_INTEGRITY_VIOLATION`  | A foreign key or the underlying unique constraint (`uq_resort_room_main_price_active`) somehow references/duplicates a row unexpectedly — normally only reachable via the same concurrent-insert race as the category level                                                                                                                                                      |
| 500         | `INTERNAL_SERVER_ERROR`     | The `price_unit_id` used isn't assigned to the `ROOM` price scope (see [Price unit rules](#price-unit-rules)); or the resort has no active weekly schedule for `WKD`/`WKE` yet                                                                                                                                                                                                     |
