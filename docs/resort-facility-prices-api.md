# Resort Facility Prices API

Base URL: `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices`

A resort facility price row describes what a [Resort Facility](resort-facilities-api.md) costs — a generic
pricing *classification* (`price_type`, e.g. `FREE`/`INCLUDED`/`FIXED`/`VARIABLE`) plus an optional billing
*unit* (`price_unit`, e.g. `PER_PERSON`/`PER_NIGHT`) and an optional monetary `amount` in a given `currency`.
**Most facilities have no price at all — a price is the exception, not the rule.** A facility's first price is
usually created together with the facility itself, via the optional `price` field on [Create Resort
Facility](resort-facilities-api.md#create-resort-facility) — the same single-object shape as
[Create Resort Facility Price](#create-resort-facility-price) below, with the resort facility resolved from the
URL path rather than the request body. Additional prices, if ever needed, are added afterward through this
sub-resource. There is no database-level constraint limiting a facility to one active price row (the entity
holds prices in a plain collection), but in practice a facility is expected to have at most one.

Resort facility prices are always reached nested under their owning resort facility; there is no top-level
`/api/v1/resort-facility-prices` route. Every endpoint below also validates the `{resort-id}`/`{resort-facility-id}`
pair first — an unknown resort, an unknown facility, or a facility that exists but belongs to a different
resort all return `404 ENTITY_NOT_FOUND`. `{id}` on the single-row endpoints is additionally scoped to
`{resort-facility-id}` — a price `id` that exists but belongs to a different facility behaves the same as an unknown
`id`.

`price_type_id`, `price_unit_id`, and `currency_id` are all **immutable after creation** — [Update Resort
Facility Price](#update-resort-facility-price) only changes `amount`/`note`; to change the classification, unit,
or currency, delete the price and create a new one. All records support soft-delete — deleted records are
hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). This entity has no `locale` field of its own, but the header's value still
shapes the response: it selects the locale-matched translation embedded on `price_type.locale`,
`price_unit.locale`, and `currency.locale` (exact match, falls back to `en`, then `null`), the same as `GET` on
each of those resources directly.

---

## Endpoints

| Method | Path                                                               | Description                     |
|--------|--------------------------------------------------------------------|---------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices`      | Create a resort facility price  |
| GET    | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices`      | List a resort facility's prices |
| GET    | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices/{id}` | Get a resort facility price     |
| PUT    | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices/{id}` | Update a resort facility price  |
| DELETE | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices/{id}` | Delete a resort facility price  |

---

## Data Model

### ResortFacilityPrice

| Field             | Type    | Required      | Constraints                                                                             | Description                                                                                                           |
|-------------------|---------|---------------|-----------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| `id`              | Long    | —             | read-only                                                                               | Auto-generated identifier                                                                                             |
| `resort_facility` | Object  | —             | read-only; see [ResortFacility](resort-facilities-api.md)                               | The facility this price belongs to. Resolved from the URL path, never a request body field                            |
| `price_type`      | Object  | —             | read-only; see [FacilityPriceType](facility-price-types-api.md); resolved from `price_type_id` | Pricing classification (`FREE`/`INCLUDED`/`FIXED`/`VARIABLE`). Immutable after creation                          |
| `price_unit`      | Object  | —             | nullable; read-only; see [PriceUnit](price-units-api.md); resolved from `price_unit_id` | Billing unit (`PER_PERSON`/`PER_ROOM`/`PER_NIGHT`, ...). Null for `FREE`/`INCLUDED` pricing. Immutable after creation |
| `currency`        | Object  | —             | read-only; see [Currency](currencies-api.md); resolved from `currency_id`               | Currency of `amount`. Immutable after creation                                                                        |
| `amount`          | Decimal | conditionally | nullable; numeric(19,4)                                                                 | Monetary amount. Null for `FREE`/`INCLUDED` pricing types                                                             |
| `note`            | String  | —             | not null (defaults to `""`)                                                             | Free-form note about the price (e.g. conditions, caveats)                                                             |

> **Note:** `price_type_id`, `price_unit_id`, and `currency_id` (used to resolve `price_type`, `price_unit`, and
> `currency`) are write-only inputs, supplied only at creation — see [Create Resort Facility
> Price](#create-resort-facility-price) — and do not appear on this data model because the response always
> returns the resolved objects instead. `price_type`/`price_unit`/`currency` each embed only `id`, `code`,
> `sort_order`, and the Accept-Language-matched `locale` — nested collections such as a price unit's own
> `price_scopes`, or a currency's `country`, are never embedded here.

**Consistency between `price_type`, `amount`, and `price_unit` is not enforced by this API at any level** —
there is no application check or database constraint requiring `amount`/`price_unit_id` to be null for
`FREE`/`INCLUDED` pricing, or non-null for `FIXED`/`VARIABLE`. The columns are nullable to accommodate the
common case; callers are expected to leave `amount`/`price_unit_id` unset for `FREE`/`INCLUDED` prices by
convention.

---

## Create Resort Facility Price

`POST /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices`

Creates a new price row for the facility. `price_type_id` must reference an existing, active [Resort Facility
Price Type](facility-price-types-api.md) (`FREE`/`INCLUDED`/`FIXED`/`VARIABLE`) — an unknown id returns
`404 ENTITY_NOT_FOUND`. Unlike `price_unit_id`, `price_type_id` is not scope-filtered — resort facility price
types are a dedicated, single-purpose classification with nothing else to disambiguate. `price_unit_id` is
optional; when supplied, it must reference an existing, active [Price Unit](price-units-api.md). `currency_id`
must reference an existing, active [Currency](currencies-api.md). `price_type_id`, `price_unit_id`, and
`currency_id` are all immutable after creation — see [Update Resort Facility
Price](#update-resort-facility-price).

**Not every price unit is valid here** — a price unit is only usable for a resort facility if it's assigned
to the `RESORT_FACILITY` [price scope](price-scopes-api.md). This isn't enforced by `POST` itself (no
application-level or database check ties `price_unit_id` to a scope), so building the picker correctly is up
to the caller: fetch the allowed set first via [List / Search Price
Units](price-units-api.md#list--search-price-units) filtered to that scope —
`GET /api/v1/price-units?priceScopeCodes=RESORT_FACILITY` — and only offer those ids.

### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `resort-id`   | Long | ID of the owning resort   |
| `facility-id` | Long | ID of the resort facility |

### Request Body

```json
{
  "price_type_id": 3,
  "price_unit_id": 1,
  "currency_id": 1,
  "amount": 25.0000,
  "note": "Per adult, children under 12 free."
}
```

### Request Fields

| Field           | Type    | Required | Validation                                                                                    |
|-----------------|---------|----------|-----------------------------------------------------------------------------------------------|
| `price_type_id` | Long    | Yes      | Not null; must reference an existing, active price type; immutable after creation             |
| `price_unit_id` | Long    | —        | Nullable; if present, must reference an existing, active price unit; immutable after creation |
| `currency_id`   | Long    | Yes      | Not null; must reference an existing, active currency; immutable after creation               |
| `amount`        | Decimal | —        | Nullable; numeric(19,4). Omit/null for `FREE`/`INCLUDED` pricing types                        |
| `note`          | String  | —        | Nullable (defaults to `""`)                                                                   |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Resort Facility Price

`GET /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices/{id}`

Returns a single active resort facility price, scoped to its owning facility — an `id` that exists but belongs
to a different facility (or a facility that belongs to a different resort) returns `404 ENTITY_NOT_FOUND`, the
same as an unknown `id`.

### Path Parameters

| Parameter     | Type | Description                      |
|---------------|------|----------------------------------|
| `resort-id`   | Long | ID of the owning resort          |
| `facility-id` | Long | ID of the owning resort facility |
| `id`          | Long | ID of the resort facility price  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "resort_facility": {
      "id": 2,
      "code": "SWIMMING_POOL",
      "sort_order": 2,
      "is_highlighted": false,
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
        "sort_order": 2
      }
    },
    "price_type": {
      "id": 3,
      "code": "FIXED",
      "sort_order": 3,
      "locale": {
        "id": 3,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Fixed",
        "description": "",
        "sort_order": 3
      }
    },
    "price_unit": {
      "id": 1,
      "code": "PER_PERSON",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Per Person",
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
    "amount": 25.0000,
    "note": "Per adult, children under 12 free."
  }
}
```

---

## List Resort Facility Prices

`GET /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices`

Returns a paginated list of every active price belonging to the facility. There is no filtering and no
sortable field other than the default (`id` ascending).

> **Note:** `sortBy`/`sortDir` are accepted on the request object but there are no sortable fields registered
> for this endpoint — passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default (sorted by `id`
> ascending).

### Path Parameters

| Parameter     | Type | Description                      |
|---------------|------|----------------------------------|
| `resort-id`   | Long | ID of the owning resort          |
| `facility-id` | Long | ID of the owning resort facility |

### Query Parameters

| Parameter | Type | Default | Constraints | Description              |
|-----------|------|---------|-------------|--------------------------|
| `page`    | int  | `0`     | >= 0        | Zero-based page index    |
| `size`    | int  | `10`    | 1 – 50      | Number of items per page |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort_facility": null,
      "price_type": {
        "id": 3,
        "code": "FIXED",
        "sort_order": 3,
        "locale": {
          "id": 3,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Fixed",
          "description": "",
          "sort_order": 3
        }
      },
      "price_unit": {
        "id": 1,
        "code": "PER_PERSON",
        "sort_order": 1,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Per Person",
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
      "amount": 25.0000,
      "note": "Per adult, children under 12 free."
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": null,
  "searchable_fields": null
}
```

> **Note:** unlike [Get Resort Facility Price](#get-resort-facility-price), list rows omit `resort_facility`
> entirely (`null`, dropped by `JsonInclude.NON_NULL`) — it's identical on every row and already known from the
> URL path.

---

## Update Resort Facility Price

`PUT /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices/{id}`

Updates `amount` and `note` only. `price_type_id`, `price_unit_id`, and `currency_id` are set at creation and
cannot be changed — to reclassify a price, change its unit, or change its currency, delete it and create a new
one instead.

### Path Parameters

| Parameter     | Type | Description                      |
|---------------|------|----------------------------------|
| `resort-id`   | Long | ID of the owning resort          |
| `facility-id` | Long | ID of the owning resort facility |
| `id`          | Long | ID of the resort facility price  |

### Request Body

```json
{
  "amount": 30.0000,
  "note": "Per adult, children under 12 free. Updated for peak season."
}
```

### Request Fields

| Field    | Type    | Required | Validation                  |
|----------|---------|----------|-----------------------------|
| `amount` | Decimal | —        | Nullable; numeric(19,4)     |
| `note`   | String  | —        | Nullable (defaults to `""`) |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Resort Facility Price

`DELETE /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/prices/{id}`

Soft-deletes the resort facility price. The record is not removed from the database but will no longer appear
in any response.

### Path Parameters

| Parameter     | Type | Description                      |
|---------------|------|----------------------------------|
| `resort-id`   | Long | ID of the owning resort          |
| `facility-id` | Long | ID of the owning resort facility |
| `id`          | Long | ID of the resort facility price  |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
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
  "message": "ResortFacilityPrice not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|-------------|----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields (`price_type_id`/`currency_id` null on create); an unsupported `sortBy` query value                                                                                                                                                                                                                           |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found; resort facility not found for the given `resort-id`/`facility-id` pair (including a `facility-id` that belongs to a different resort); resort facility price not found for the given `facility-id`/`id` pair (including an `id` that belongs to a different facility); the price type referenced by `price_type_id` not found; the price unit referenced by `price_unit_id` not found; the currency referenced by `currency_id` not found |
| 409         | `DATA_INTEGRITY_VIOLATION` | A foreign key (`price_type_id`, `price_unit_id`, `currency_id`, `resort_facility_id`) somehow references a row that no longer exists — should not normally be reachable, since each is resolved and validated before the write                                                                                                                                                                                                                              |
