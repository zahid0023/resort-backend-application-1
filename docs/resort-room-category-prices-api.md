# Resort Room Category Prices API

Base URL: `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`

A resort room category price defines a pricing rule for a specific room category within a resort. Five price types are
supported — Base, Weekday, Weekend, Holiday, and Special — each with distinct validation rules, priority behaviour, and
field requirements. Multiple rules can coexist per room category per currency, resolved at booking time by priority.
All records support soft-delete — deleted records are hidden from all responses.

---

## Price Types

| Code  | Name    | Priority   | Day Records | Date Range  | Description                                                                                                         |
|-------|---------|------------|-------------|-------------|---------------------------------------------------------------------------------------------------------------------|
| `BAS` | Base    | 0 (fixed)  | Not allowed | Not allowed | Default rack rate. Used when no other rule applies.                                                                 |
| `WKD` | Weekday | 10 (fixed) | Required    | Not allowed | Applied on the configured weekdays. Must be ≤ base price.                                                           |
| `WKE` | Weekend | 20 (fixed) | Required    | Not allowed | Applied on the configured weekend days. Must be ≤ base price.                                                       |
| `HOL` | Holiday | ≥ 100      | Optional    | Required    | Applied on public or regional holidays. If `day_of_week_ids` is set, only matches on those days. Any price allowed. |
| `SPE` | Special | ≥ 200      | Optional    | Required    | Promotional or event-specific price. If `day_of_week_ids` is set, only matches on those days. Any price allowed.    |

### Pricing Priority

When determining the price for a given date, the highest-priority matching rule wins:

```
Special  (≥ 200)
   ↓
Holiday  (≥ 100)
   ↓
Weekend / Weekday  (20 / 10)
   ↓
Base  (0)
```

### Rules Per Price Type

| Rule                                           | BAS | WKD | WKE | HOL | SPE |
|------------------------------------------------|-----|-----|-----|-----|-----|
| `day_of_week_ids` required                     | ✗   | ✓   | ✓   | ✗   | ✗   |
| `day_of_week_ids` optional                     | ✗   | ✗   | ✗   | ✓   | ✓   |
| `day_of_week_ids` forbidden                    | ✓   | ✗   | ✗   | ✗   | ✗   |
| `valid_from` / `valid_to` required             | ✗   | ✗   | ✗   | ✓   | ✓   |
| `valid_from` / `valid_to` forbidden            | ✓   | ✓   | ✓   | ✗   | ✗   |
| Only one allowed per room category + currency  | ✓   | ✓   | ✓   | ✗   | ✗   |
| `priority` editable by owner                   | ✗   | ✗   | ✗   | ✓   | ✓   |
| `price` must be ≤ base price for same currency | —   | ✓   | ✓   | ✗   | ✗   |

### Discount Calculation

When the `price` of a Weekday, Weekend, Holiday, or Special rule is less than the active Base price for the same
currency, the response includes two derived fields:

- `discount_amount` — the absolute saving (`base_price − price`), rounded to 2 decimal places
- `discount_percentage` — the relative saving as a percentage of the base price, rounded to 2 decimal places

These fields are omitted when the price is ≥ the base price.

---

## Endpoints

| Method | Path                                                                                | Description          |
|--------|-------------------------------------------------------------------------------------|----------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`      | Create a price rule  |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`      | List / filter prices |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}` | Get a price rule     |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}` | Update a price rule  |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}` | Delete a price rule  |

---

## Data Model

### Resort Room Category Price

| Field                 | Type       | Required | Constraints                                                                            | Description                                                                                                                                                               |
|-----------------------|------------|----------|----------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`                  | Long       | —        | read-only                                                                              | Auto-generated identifier                                                                                                                                                 |
| `price_type`          | Object     | —        | read-only, see PriceTypeDto below                                                      | Nested price type object                                                                                                                                                  |
| `price_unit`          | Object     | —        | read-only, see PriceUnitDto below                                                      | Nested price unit object                                                                                                                                                  |
| `currency`            | Object     | —        | read-only, see CurrencyDto below                                                       | Nested currency object                                                                                                                                                    |
| `name`                | String     | Yes      | not blank, max 200 chars                                                               | Display name (e.g. `Base Price`, `Eid-ul-Fitr`, `Summer Sale`)                                                                                                            |
| `description`         | String     | No       | optional, free text                                                                    | Optional description. Omitted from response if null.                                                                                                                      |
| `price`               | BigDecimal | Yes      | not null, >= 0.00, precision 12 scale 2                                                | Price amount in the specified currency                                                                                                                                    |
| `day_of_week_ids`     | Array      | WKD/WKE  | at least one valid day-of-week ID for WKD/WKE; optional for HOL/SPE; forbidden for BAS | IDs of the days of the week this rule applies to. When set on HOL/SPE, the rule only applies if the date also falls on one of these days. Omitted from response if empty. |
| `valid_from`          | String     | HOL/SPE  | date (`YYYY-MM-DD`); forbidden for BAS, WKD, WKE                                       | Start of the validity period. Omitted from response if null.                                                                                                              |
| `valid_to`            | String     | HOL/SPE  | date (`YYYY-MM-DD`), must be >= `valid_from`; forbidden for BAS, WKD, WKE              | End of the validity period. Omitted from response if null.                                                                                                                |
| `priority`            | Integer    | HOL/SPE  | fixed `0`/`10`/`20` for BAS/WKD/WKE; >= `100` for HOL; >= `200` for SPE                | Rule priority — higher value overrides lower when multiple rules match the same date.                                                                                     |
| `discount_amount`     | BigDecimal | —        | read-only, derived                                                                     | Absolute saving vs. base price. Present only when `price` < base price for the same currency.                                                                             |
| `discount_percentage` | BigDecimal | —        | read-only, derived                                                                     | Percentage saving vs. base price. Present only when `price` < base price for the same currency.                                                                           |

### PriceTypeDto

| Field        | Type    | Description                          |
|--------------|---------|--------------------------------------|
| `id`         | Long    | Price type identifier                |
| `code`       | String  | Price type code (`BAS`, `WKD`, etc.) |
| `sort_order` | Integer | Display sort order                   |
| `locales`    | Array   | Locale translations (see below)      |

### PriceTypeLocaleDto

| Field           | Type    | Description                                        |
|-----------------|---------|----------------------------------------------------|
| `id`            | Long    | Locale entry identifier                            |
| `locale`        | Object  | Locale object (`id`, `code`, `name`, `sort_order`) |
| `name`          | String  | Translated name of the price type                  |
| `description`   | String  | Translated description                             |
| `sort_order`    | Integer | Display sort order                                 |
| `purpose`       | String  | Intended use of this price type                    |
| `usage_example` | String  | Example of how this price type is applied          |

### PriceUnitDto

| Field        | Type    | Description                        |
|--------------|---------|------------------------------------|
| `id`         | Long    | Price unit identifier              |
| `code`       | String  | Price unit code (e.g. `PER_NIGHT`) |
| `sort_order` | Integer | Display sort order                 |
| `locales`    | Array   | Locale translations (see below)    |

### PriceUnitLocaleDto

| Field                | Type    | Description                                        |
|----------------------|---------|----------------------------------------------------|
| `id`                 | Long    | Locale entry identifier                            |
| `locale`             | Object  | Locale object (`id`, `code`, `name`, `sort_order`) |
| `name`               | String  | Translated name of the price unit                  |
| `description`        | String  | Translated description                             |
| `sort_order`         | Integer | Display sort order                                 |
| `calculation_method` | String  | How the price is calculated using this unit        |
| `usage_example`      | String  | Example of how this price unit is applied          |

### CurrencyDto

| Field            | Type    | Description                                |
|------------------|---------|--------------------------------------------|
| `id`             | Long    | Currency identifier                        |
| `code`           | String  | ISO 4217 alphabetic code (e.g. `USD`)      |
| `numeric_code`   | String  | ISO 4217 numeric code (e.g. `840`)         |
| `symbol`         | String  | Currency symbol (e.g. `$`)                 |
| `decimal_places` | Integer | Number of decimal places                   |
| `is_default`     | Boolean | Whether this is the default currency       |
| `sort_order`     | Integer | Display sort order                         |
| `country_id`     | Long    | ID of the country this currency belongs to |
| `locales`        | Array   | Locale translations (see below)            |

### CurrencyLocaleDto

| Field        | Type    | Description                   |
|--------------|---------|-------------------------------|
| `id`         | Long    | Locale entry identifier       |
| `locale_id`  | Long    | ID of the locale              |
| `name`       | String  | Full translated currency name |
| `short_name` | String  | Short translated name         |
| `sort_order` | Integer | Display sort order            |

---

## Create Price Rule

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`

Creates a new pricing rule for the specified resort room category. The `price_type_id` and `currency_id` are fixed at
creation time and cannot be changed. The resort and resort room category must exist and be active.

Only one Base, Weekday, or Weekend rule is allowed per room category per currency. Multiple Holiday and Special rules
are allowed, each with a unique user-defined priority.

### Prerequisites

Price rules must be created in order. Each type requires the following rules to already exist for the **same room
category and currency** before it can be created:

| Price Type | Required Before Creating |
|------------|--------------------------|
| `BAS`      | None                     |
| `WKD`      | `BAS`                    |
| `WKE`      | `BAS`, `WKD`             |
| `HOL`      | `BAS`, `WKD`, `WKE`      |
| `SPE`      | `BAS`, `WKD`, `WKE`      |

### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |

### Request Fields

| Field             | Type       | Required | Validation                                                                                                                                              |
|-------------------|------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| `price_type_id`   | Long       | Yes      | Not null, must reference an existing active price type                                                                                                  |
| `currency_id`     | Long       | Yes      | Not null, must reference an existing active currency                                                                                                    |
| `price_unit_id`   | Long       | Yes      | Not null, must reference an existing active price unit                                                                                                  |
| `name`            | String     | Yes      | Not blank, max 200 chars                                                                                                                                |
| `description`     | String     | No       | Free text                                                                                                                                               |
| `price`           | BigDecimal | Yes      | Not null, >= 0.00; for WKD/WKE must be ≤ the active base price for the currency                                                                         |
| `day_of_week_ids` | Array      | WKD/WKE  | At least one valid day-of-week ID for WKD/WKE; optional for HOL/SPE (restricts rule to those days within the date range); must be omitted/empty for BAS |
| `valid_from`      | String     | HOL/SPE  | `YYYY-MM-DD`; must be omitted/null for BAS, WKD, WKE                                                                                                    |
| `valid_to`        | String     | HOL/SPE  | `YYYY-MM-DD`, must be >= `valid_from`; must be omitted/null for BAS, WKD, WKE                                                                           |
| `priority`        | Integer    | HOL/SPE  | Ignored for BAS/WKD/WKE (auto-set to 0/10/20). >= 100 for HOL; >= 200 for SPE                                                                           |

---

### Example — Create Base Price

```json
{
  "price_type_id": 1,
  "currency_id": 1,
  "price_unit_id": 1,
  "name": "Base Price",
  "description": "Standard rack rate for this room category.",
  "price": 200.00
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 10
}
```

---

### Example — Create Weekday Price

Weekday and Weekend rules require at least one `day_of_week_id`. The price must not exceed the active Base price for the
same currency. Because weekday/weekend days differ by country, the owner configures which days apply via
`day_of_week_ids`.

```json
{
  "price_type_id": 2,
  "currency_id": 1,
  "price_unit_id": 1,
  "name": "Weekday Price",
  "day_of_week_ids": [
    1,
    2,
    3,
    4,
    5
  ],
  "price": 110.00
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 11
}
```

---

### Example — Create Weekend Price

```json
{
  "price_type_id": 3,
  "currency_id": 1,
  "price_unit_id": 1,
  "name": "Weekend Price",
  "day_of_week_ids": [
    6,
    7
  ],
  "price": 140.00
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 12
}
```

---

### Example — Create Holiday Price

Holiday and Special rules require both `valid_from` and `valid_to`. The price may be higher, equal to, or lower than
the base price. If the price is lower, `discount_amount` and `discount_percentage` are returned on read.

```json
{
  "price_type_id": 4,
  "currency_id": 1,
  "price_unit_id": 1,
  "name": "Eid-ul-Fitr",
  "description": "Premium rate applied during the Eid-ul-Fitr holiday period.",
  "price": 170.00,
  "valid_from": "2026-03-29",
  "valid_to": "2026-04-02",
  "priority": 100
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 13
}
```

---

### Example — Create Special Price (all days)

```json
{
  "price_type_id": 5,
  "currency_id": 1,
  "price_unit_id": 1,
  "name": "Summer Flash Sale",
  "description": "Limited-time promotional rate for the summer season.",
  "price": 90.00,
  "valid_from": "2026-06-01",
  "valid_to": "2026-08-31",
  "priority": 200
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 14
}
```

---

### Example — Create Special Price (weekdays only within a date range)

When `day_of_week_ids` is provided on a Special (or Holiday) rule, the rule only applies if the booking date falls
within the date range **and** on one of the specified days of the week. Days not listed fall back to the next matching
rule (e.g. Weekend or Base).

```json
{
  "price_type_id": 5,
  "currency_id": 1,
  "price_unit_id": 1,
  "name": "New Year Weekday Promo",
  "description": "Discounted weekday rate over the New Year period. Weekends retain the standard weekend price.",
  "price": 150.00,
  "valid_from": "2026-12-28",
  "valid_to": "2027-01-03",
  "day_of_week_ids": [
    1,
    2,
    3,
    4,
    5
  ],
  "priority": 200
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 15
}
```

---

## Get Price Rule

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}`

Returns a single active (non-deleted) pricing rule belonging to the specified resort room category. Derived discount
fields are included when the price is lower than the active Base price for the same currency. Null fields
(`description`, `day_of_week_ids`, `valid_from`, `valid_to`, `discount_amount`, `discount_percentage`) are omitted
from the response.

### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |
| `id`                      | Long | Yes      | ID of the price rule           |

### Response `200 OK` — Base Price

```json
{
  "data": {
    "id": 10,
    "price_type": {
      "id": 1,
      "code": "BAS",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Base",
          "description": "Default rack rate applied when no other rule matches.",
          "sort_order": 1,
          "purpose": "Fallback pricing",
          "usage_example": "Standard room rate"
        }
      ]
    },
    "price_unit": {
      "id": 1,
      "code": "PER_NIGHT",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Per Night",
          "description": "Price charged once per night of stay.",
          "sort_order": 1,
          "calculation_method": "Multiply by number of nights",
          "usage_example": "200 USD per night"
        }
      ]
    },
    "currency": {
      "id": 1,
      "code": "USD",
      "numeric_code": "840",
      "symbol": "$",
      "decimal_places": 2,
      "is_default": true,
      "sort_order": 1,
      "country_id": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "US Dollar",
          "short_name": "Dollar",
          "sort_order": 1
        }
      ]
    },
    "name": "Base Price",
    "description": "Standard rack rate for this room category.",
    "price": 200.00,
    "priority": 0
  }
}
```

### Response `200 OK` — Weekday Price (with discount)

```json
{
  "data": {
    "id": 11,
    "price_type": {
      "id": 2,
      "code": "WKD",
      "sort_order": 2,
      "locales": [
        {
          "id": 2,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Weekday",
          "description": "Discounted rate applied on configured weekdays.",
          "sort_order": 1,
          "purpose": "Weekday pricing",
          "usage_example": "110 USD Mon–Fri"
        }
      ]
    },
    "price_unit": {
      "id": 1,
      "code": "PER_NIGHT",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Per Night",
          "description": "Price charged once per night of stay.",
          "sort_order": 1,
          "calculation_method": "Multiply by number of nights",
          "usage_example": "110 USD per night"
        }
      ]
    },
    "currency": {
      "id": 1,
      "code": "USD",
      "numeric_code": "840",
      "symbol": "$",
      "decimal_places": 2,
      "is_default": true,
      "sort_order": 1,
      "country_id": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "US Dollar",
          "short_name": "Dollar",
          "sort_order": 1
        }
      ]
    },
    "name": "Weekday Price",
    "price": 110.00,
    "priority": 10,
    "day_of_week_ids": [
      1,
      2,
      3,
      4,
      5
    ],
    "discount_amount": 90.00,
    "discount_percentage": 45.00
  }
}
```

### Response `200 OK` — Holiday Price (with discount)

```json
{
  "data": {
    "id": 13,
    "price_type": {
      "id": 4,
      "code": "HOL",
      "sort_order": 4,
      "locales": [
        {
          "id": 4,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Holiday",
          "description": "Rate applied during public or regional holidays.",
          "sort_order": 1,
          "purpose": "Holiday pricing",
          "usage_example": "Eid, Christmas, national holidays"
        }
      ]
    },
    "price_unit": {
      "id": 1,
      "code": "PER_NIGHT",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Per Night",
          "description": "Price charged once per night of stay.",
          "sort_order": 1,
          "calculation_method": "Multiply by number of nights",
          "usage_example": "170 USD per night"
        }
      ]
    },
    "currency": {
      "id": 1,
      "code": "USD",
      "numeric_code": "840",
      "symbol": "$",
      "decimal_places": 2,
      "is_default": true,
      "sort_order": 1,
      "country_id": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "US Dollar",
          "short_name": "Dollar",
          "sort_order": 1
        }
      ]
    },
    "name": "Eid-ul-Fitr",
    "description": "Premium rate applied during the Eid-ul-Fitr holiday period.",
    "price": 170.00,
    "valid_from": "2026-03-29",
    "valid_to": "2026-04-02",
    "priority": 100,
    "discount_amount": 30.00,
    "discount_percentage": 15.00
  }
}
```

### Response `200 OK` — Special Price (below base — discount shown)

```json
{
  "data": {
    "id": 14,
    "price_type": {
      "id": 5,
      "code": "SPE",
      "sort_order": 5,
      "locales": [
        {
          "id": 5,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Special",
          "description": "Promotional or event-specific pricing.",
          "sort_order": 1,
          "purpose": "Promotional pricing",
          "usage_example": "Summer flash sale, event packages"
        }
      ]
    },
    "price_unit": {
      "id": 1,
      "code": "PER_NIGHT",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Per Night",
          "description": "Price charged once per night of stay.",
          "sort_order": 1,
          "calculation_method": "Multiply by number of nights",
          "usage_example": "90 USD per night"
        }
      ]
    },
    "currency": {
      "id": 1,
      "code": "USD",
      "numeric_code": "840",
      "symbol": "$",
      "decimal_places": 2,
      "is_default": true,
      "sort_order": 1,
      "country_id": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "US Dollar",
          "short_name": "Dollar",
          "sort_order": 1
        }
      ]
    },
    "name": "Summer Flash Sale",
    "description": "Limited-time promotional rate for the summer season.",
    "price": 90.00,
    "valid_from": "2026-06-01",
    "valid_to": "2026-08-31",
    "priority": 200,
    "discount_amount": 110.00,
    "discount_percentage": 55.00
  }
}
```

---

## List / Filter Price Rules

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`

Returns a paginated, filterable list of active (non-deleted) pricing rules for the specified resort room category. All
filter parameters are optional; omitting them returns all rules. Multiple filters are combined with AND.

The `validFrom` filter returns rules whose `valid_from` is on or after the given date. The `validTo` filter returns
rules whose `valid_to` is on or before the given date. If the resort or resort room category does not exist or is
deleted, `404` is returned before the query executes.

### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |

### Query Parameters

| Parameter     | Type   | Default | Constraints                                                    | Description                                       |
|---------------|--------|---------|----------------------------------------------------------------|---------------------------------------------------|
| `priceTypeId` | Long   | —       | —                                                              | Filter by price type ID (exact match)             |
| `priceUnitId` | Long   | —       | —                                                              | Filter by price unit ID (exact match)             |
| `currencyId`  | Long   | —       | —                                                              | Filter by currency ID (exact match)               |
| `validFrom`   | String | —       | `YYYY-MM-DD`                                                   | Rules whose `valid_from` is on or after this date |
| `validTo`     | String | —       | `YYYY-MM-DD`                                                   | Rules whose `valid_to` is on or before this date  |
| `page`        | int    | `0`     | >= 0                                                           | Zero-based page index                             |
| `size`        | int    | `10`    | 1 – 50                                                         | Number of items per page                          |
| `sortBy`      | String | `id`    | `id`, `price`, `priority`, `validFrom`, `validTo`, `createdAt` | Field to sort by                                  |
| `sortDir`     | String | `ASC`   | `ASC`, `DESC`                                                  | Sort direction                                    |

### Response `200 OK`

Each item in `data` follows the same nested structure as the Get Price Rule response. Null optional fields
(`description`, `day_of_week_ids`, `valid_from`, `valid_to`, `discount_amount`, `discount_percentage`) are omitted per
item.

```json
{
  "data": [
    {
      "id": 10,
      "price_type": {
        "id": 1,
        "code": "BAS",
        "sort_order": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "price_unit": {
        "id": 1,
        "code": "PER_NIGHT",
        "sort_order": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "currency": {
        "id": 1,
        "code": "USD",
        "numeric_code": "840",
        "symbol": "$",
        "decimal_places": 2,
        "is_default": true,
        "sort_order": 1,
        "country_id": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "name": "Base Price",
      "description": "Standard rack rate for this room category.",
      "price": 200.00,
      "priority": 0
    },
    {
      "id": 11,
      "price_type": {
        "id": 2,
        "code": "WKD",
        "sort_order": 2,
        "locales": [
          {
            "..."
          }
        ]
      },
      "price_unit": {
        "id": 1,
        "code": "PER_NIGHT",
        "sort_order": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "currency": {
        "id": 1,
        "code": "USD",
        "numeric_code": "840",
        "symbol": "$",
        "decimal_places": 2,
        "is_default": true,
        "sort_order": 1,
        "country_id": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "name": "Weekday Price",
      "price": 110.00,
      "priority": 10,
      "day_of_week_ids": [
        1,
        2,
        3,
        4,
        5
      ],
      "discount_amount": 90.00,
      "discount_percentage": 45.00
    },
    {
      "id": 12,
      "price_type": {
        "id": 3,
        "code": "WKE",
        "sort_order": 3,
        "locales": [
          {
            "..."
          }
        ]
      },
      "price_unit": {
        "id": 1,
        "code": "PER_NIGHT",
        "sort_order": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "currency": {
        "id": 1,
        "code": "USD",
        "numeric_code": "840",
        "symbol": "$",
        "decimal_places": 2,
        "is_default": true,
        "sort_order": 1,
        "country_id": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "name": "Weekend Price",
      "price": 140.00,
      "priority": 20,
      "day_of_week_ids": [
        6,
        7
      ],
      "discount_amount": 60.00,
      "discount_percentage": 30.00
    },
    {
      "id": 13,
      "price_type": {
        "id": 4,
        "code": "HOL",
        "sort_order": 4,
        "locales": [
          {
            "..."
          }
        ]
      },
      "price_unit": {
        "id": 1,
        "code": "PER_NIGHT",
        "sort_order": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "currency": {
        "id": 1,
        "code": "USD",
        "numeric_code": "840",
        "symbol": "$",
        "decimal_places": 2,
        "is_default": true,
        "sort_order": 1,
        "country_id": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "name": "Eid-ul-Fitr",
      "description": "Premium rate applied during the Eid-ul-Fitr holiday period.",
      "price": 170.00,
      "valid_from": "2026-03-29",
      "valid_to": "2026-04-02",
      "priority": 100,
      "discount_amount": 30.00,
      "discount_percentage": 15.00
    },
    {
      "id": 14,
      "price_type": {
        "id": 5,
        "code": "SPE",
        "sort_order": 5,
        "locales": [
          {
            "..."
          }
        ]
      },
      "price_unit": {
        "id": 1,
        "code": "PER_NIGHT",
        "sort_order": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "currency": {
        "id": 1,
        "code": "USD",
        "numeric_code": "840",
        "symbol": "$",
        "decimal_places": 2,
        "is_default": true,
        "sort_order": 1,
        "country_id": 1,
        "locales": [
          {
            "..."
          }
        ]
      },
      "name": "Summer Flash Sale",
      "description": "Limited-time promotional rate for the summer season.",
      "price": 90.00,
      "valid_from": "2026-06-01",
      "valid_to": "2026-08-31",
      "priority": 200,
      "discount_amount": 110.00,
      "discount_percentage": 55.00
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 5,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Price Rule

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}`

Replaces the updatable fields of an existing pricing rule. `price_type_id` and `currency_id` are immutable — they
cannot be changed after creation. To assign a different price type or currency, delete the existing rule and create a
new one.

The same field requirements and business rules apply as on create (day records for WKD/WKE, date range for HOL/SPE,
price cap for WKD/WKE, priority constraints for HOL/SPE). Priority for BAS, WKD, and WKE is always overridden to its
fixed value regardless of what is submitted.

### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |
| `id`                      | Long | Yes      | ID of the price rule           |

### Request Fields

| Field             | Type       | Required | Validation                                                                                                                                              |
|-------------------|------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| `price_unit_id`   | Long       | Yes      | Not null, must reference an existing active price unit                                                                                                  |
| `name`            | String     | Yes      | Not blank, max 200 chars                                                                                                                                |
| `description`     | String     | No       | Free text, or `null` to clear                                                                                                                           |
| `price`           | BigDecimal | Yes      | Not null, >= 0.00; for WKD/WKE must be ≤ the active base price for the currency                                                                         |
| `day_of_week_ids` | Array      | WKD/WKE  | At least one valid day-of-week ID for WKD/WKE; optional for HOL/SPE (restricts rule to those days within the date range); must be omitted/empty for BAS |
| `valid_from`      | String     | HOL/SPE  | `YYYY-MM-DD`; must be omitted/null for BAS, WKD, WKE                                                                                                    |
| `valid_to`        | String     | HOL/SPE  | `YYYY-MM-DD`, must be >= `valid_from`; must be omitted/null for BAS, WKD, WKE                                                                           |
| `priority`        | Integer    | HOL/SPE  | Ignored for BAS/WKD/WKE (auto-set). >= 100 for HOL; >= 200 for SPE                                                                                      |

---

### Example — Update Base Price

```json
{
  "price_unit_id": 1,
  "name": "Base Price",
  "description": "Updated standard rack rate.",
  "price": 220.00
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 10
}
```

---

### Example — Update Weekday Price (change days and price)

```json
{
  "price_unit_id": 1,
  "name": "Weekday Price",
  "price": 120.00,
  "day_of_week_ids": [
    1,
    2,
    3,
    4
  ]
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 11
}
```

---

### Example — Update Holiday Price (adjust date range and priority)

```json
{
  "price_unit_id": 1,
  "name": "Eid-ul-Fitr 2026",
  "description": "Updated dates for Eid-ul-Fitr 2026.",
  "price": 180.00,
  "valid_from": "2026-03-30",
  "valid_to": "2026-04-03",
  "priority": 101
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 13
}
```

---

## Delete Price Rule

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}`

Soft-deletes the pricing rule. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |
| `id`                      | Long | Yes      | ID of the price rule           |

### Response `200 OK`

```json
{
  "success": true,
  "id": 10
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

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                 |
|-------------|----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields (`name`, `price`, `price_unit_id`, `price_type_id`, `currency_id`) or invalid format                                                                                                                                                                                          |
| 400         | `INVALID_ARGUMENT`         | `day_of_week_ids` missing for WKD/WKE; `day_of_week_ids` provided for BAS; `valid_from`/`valid_to` missing for HOL/SPE; `valid_from`/`valid_to` provided for BAS/WKD/WKE; `valid_from` after `valid_to`; WKD/WKE price exceeds base price; priority below minimum for HOL/SPE; invalid `sortBy` field |
| 404         | `ENTITY_NOT_FOUND`         | Resort, resort room category, price rule, price type, price unit, currency, or day-of-week ID not found or already deleted                                                                                                                                                                            |
| 409         | `CONFLICT`                 | A Base, Weekday, or Weekend rule already exists for this room category and currency; or a required prerequisite price type does not yet exist (e.g. creating WKD without BAS, WKE without WKD, or HOL/SPE without BAS+WKD+WKE)                                                                        |
| 409         | `DATA_INTEGRITY_VIOLATION` | Database-level constraint violated (e.g. `price` < 0, `valid_from` > `valid_to`)                                                                                                                                                                                                                      |
