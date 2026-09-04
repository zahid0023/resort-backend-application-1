# Payment Providers API

Base URL: `/api/v1/payment-providers`

Payment providers represent the specific provider used to settle a payment under a given payment method
(e.g. `BKASH`, `NAGAD`, `ROCKET`, `UPAY` under `MOBILE_BANKING`) — consumed by
`resort_booking_payments.payment_provider_id`, which does not yet have its own CRUD API. Each provider
belongs to exactly one payment method and is identified by a `code` unique within that method (the same
code, e.g. `OTHER`, may legitimately exist under more than one method). A provider's display name and
description are locale-specific and are managed through a companion sub-resource — Payment Provider Locales
— reached via `/api/v1/payment-providers/{payment-provider-id}/locales`. Each provider response embeds a
summary of its parent payment method, using the same single-locale shape the
[Payment Methods API](payment-methods-api.md) itself returns. The platform ships with five seeded providers
under `MOBILE_BANKING` (`BKASH`, `NAGAD`, `ROCKET`, `UPAY`, `OTHER`), but this list is **not read-only** —
additional providers can be created, and existing ones updated or deleted, through this API. All records
support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Payment Provider)** and **`GET` (List Payment Providers)** — the header's value
  selects exactly one locale translation for the provider's own `locale` field, and separately for the
  embedded parent payment method's `locale` field: an exact match if one exists, otherwise `en`, otherwise
  `null`.
- **`GET /{payment-provider-id}/locales` (List Payment Provider Locales)** — the header must be present,
  but its value has no effect; this endpoint returns every translation the provider has (optionally
  filtered by `localeCode`), not a single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                           | Description                    |
|--------|-------------------------------------------------------------------|----------------------------------|
| POST   | `/api/v1/payment-providers`                                        | Create a payment provider        |
| GET    | `/api/v1/payment-providers`                                        | List payment providers           |
| GET    | `/api/v1/payment-providers/{id}`                                   | Get a payment provider           |
| PUT    | `/api/v1/payment-providers/{id}`                                   | Update a payment provider        |
| DELETE | `/api/v1/payment-providers/{id}`                                   | Delete a payment provider        |
| GET    | `/api/v1/payment-providers/{payment-provider-id}/locales`          | List a provider's locales        |
| GET    | `/api/v1/payment-providers/{payment-provider-id}/locales/count`    | Count a provider's locales       |
| POST   | `/api/v1/payment-providers/{payment-provider-id}/locales`          | Create a provider locale         |
| PUT    | `/api/v1/payment-providers/{payment-provider-id}/locales/{id}`     | Update a provider locale         |
| DELETE | `/api/v1/payment-providers/{payment-provider-id}/locales/{id}`     | Delete a provider locale         |

---

## Data Model

### PaymentProvider

| Field            | Type          | Required | Constraints                                                                        | Description                                                                                                                                  |
|------------------|---------------|----------|--------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`             | Long          | —        | read-only                                                                          | Auto-generated identifier                                                                                                                     |
| `payment_method` | PaymentMethod | —        | read-only; embedded parent summary (single Accept-Language-matched `locale`, same shape as `GET /payment-methods/{id}`) | The parent payment method this provider belongs to                                                                                            |
| `code`           | String        | Yes      | max 50 chars, unique among active records **within the same payment method**; set at creation, immutable | Internal code, e.g. `BKASH`, `NAGAD`, `ROCKET`, `UPAY`, `OTHER`                                                                                |
| `sort_order`     | Integer       | Yes      | default 0                                                                          | Display order                                                                                                                                  |
| `locale`         | Object        | —        | nullable; see PaymentProviderLocale below                                         | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the provider has no translations at all)  |

### PaymentProviderLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|---------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 150 chars                                    | Localized display name, e.g. `bKash`                                          |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                         |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                            |

---

## Create Payment Provider

`POST /api/v1/payment-providers`

Creates a new payment provider under an existing payment method, together with exactly **one** initial
locale translation. `payment_method_id` must reference an existing, active payment method — an unknown
`payment_method_id` returns `404 ENTITY_NOT_FOUND`. `code` must be unique among active, non-deleted
providers **within that same payment method** — attempting to reuse an existing code under the same method
returns `409 CONFLICT` (the same code is allowed again under a different method).

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Payment Provider Locales sub-resource below.

### Request Body

```json
{
  "code": "STRIPE",
  "payment_method_id": 5,
  "sort_order": 1,
  "locale": {
    "name": "Stripe",
    "description": "Payment settled through the Stripe online gateway.",
    "sort_order": 1
  }
}
```

### Request Fields

| Field               | Type    | Required | Validation                                                                                 |
|---------------------|---------|----------|----------------------------------------------------------------------------------------------|
| `code`              | String  | Yes      | Not blank, max 50 chars, unique among active records within the same payment method        |
| `payment_method_id` | Long    | Yes      | Not null; must reference an existing, active payment method                                |
| `sort_order`        | Integer | Yes      | Not null                                                                                    |
| `locale`            | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale  |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 150 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 6
}
```

---

## Get Payment Provider

`GET /api/v1/payment-providers/{id}`

Returns a single active payment provider by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the provider has no translations at all). The
embedded `payment_method` resolves its own `locale` field the same way, independently. To fetch every
translation a provider has, use [List Payment Provider Locales](#list-payment-provider-locales) below.

### Path Parameters

| Parameter | Type | Description                |
|-----------|------|------------------------------|
| `id`      | Long | ID of the payment provider   |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "payment_method": {
      "id": 4,
      "code": "MOBILE_BANKING",
      "sort_order": 4,
      "locale": {
        "id": 4,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Mobile Banking",
        "description": "Payment settled through a mobile financial service (e.g. bKash, Nagad).",
        "sort_order": 4
      }
    },
    "code": "BKASH",
    "sort_order": 1,
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "bKash",
      "description": "Payment settled through the bKash mobile financial service.",
      "sort_order": 1
    }
  }
}
```

> **Note:** the embedded `payment_method` shows exactly the same single Accept-Language-matched `locale`
> (falls back to `en`, then `null`) that `GET /payment-methods/{id}` itself would return — not every
> translation the method has. To see every translation of the method itself, use the
> [Payment Methods API](payment-methods-api.md)'s locale sub-resource.

---

## List Payment Providers

`GET /api/v1/payment-providers`

Returns a paginated list of active (non-deleted) payment providers. `Accept-Language` selects each
provider's `locale` field the same way as `GET /{id}` (exact match, falls back to `en`, then `null`) — and
does the same for each provider's embedded `payment_method.locale`.

> **Note:** no field on `PaymentProvider` is registered as filterable or sortable beyond the implicit `id`
> default — `searchable_fields` is always `[]` and `sortable_fields` is always `["createdAt"]` in the
> response envelope below. There is no `payment_method_id` or `code` filter for this entity (unlike, e.g.,
> [Currencies](currencies-api.md), which does filter by `countryId`/`code`).

### Query Parameters

> **Note:** Query parameters bind directly onto `PaymentProviderFilterRequest`'s Java field names, so they
> are **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter | Type   | Default         | Constraints           | Description             |
|-----------|--------|-----------------|------------------------|---------------------------|
| `page`    | int    | `0`             | >= 0                   | Zero-based page index     |
| `size`    | int    | `10`            | 1 – 50                 | Number of items per page  |
| `sortBy`  | String | `id` (implicit) | `createdAt` only       | Field to sort by          |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`          | Sort direction             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "payment_method": {
        "id": 4,
        "code": "MOBILE_BANKING",
        "sort_order": 4,
        "locale": {
          "id": 4,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Mobile Banking",
          "description": "Payment settled through a mobile financial service (e.g. bKash, Nagad).",
          "sort_order": 4
        }
      },
      "code": "BKASH",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "bKash",
        "description": "Payment settled through the bKash mobile financial service.",
        "sort_order": 1
      }
    },
    {
      "id": 2,
      "payment_method": {
        "id": 4,
        "code": "MOBILE_BANKING",
        "sort_order": 4,
        "locale": {
          "id": 4,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Mobile Banking",
          "description": "Payment settled through a mobile financial service (e.g. bKash, Nagad).",
          "sort_order": 4
        }
      },
      "code": "NAGAD",
      "sort_order": 2,
      "locale": {
        "id": 2,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Nagad",
        "description": "Payment settled through the Nagad mobile financial service.",
        "sort_order": 2
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 5,
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

## Update Payment Provider

`PUT /api/v1/payment-providers/{id}`

Updates `sort_order` only. `code` and `payment_method_id` are set at creation and cannot be changed. Locale
translations are managed separately via the Payment Provider Locales sub-resource endpoints below, not
through this endpoint.

### Path Parameters

| Parameter | Type | Description                |
|-----------|------|------------------------------|
| `id`      | Long | ID of the payment provider   |

### Request Body

```json
{
  "sort_order": 3
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
  "id": 1
}
```

---

## Delete Payment Provider

`DELETE /api/v1/payment-providers/{id}`

Soft-deletes the payment provider. The record is not removed from the database but will no longer appear in
any response.

### Path Parameters

| Parameter | Type | Description                |
|-----------|------|------------------------------|
| `id`      | Long | ID of the payment provider   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Payment Provider Locales

Payment Provider Locale endpoints manage locale-specific name/description translations for a payment
provider. The `{payment-provider-id}` path parameter must reference an existing, active provider.

---

### List Payment Provider Locales

`GET /api/v1/payment-providers/{payment-provider-id}/locales`

Returns a paginated list of every locale translation belonging to a payment provider — this is the only way
to see more than the single Accept-Language-matched translation returned by
`GET /payment-providers/{id}` and `GET /payment-providers`. Optionally filtered to locales whose `code`
contains a given substring.

#### Path Parameters

| Parameter              | Type | Description                       |
|--------------------------|------|-------------------------------------|
| `payment-provider-id`  | Long | ID of the parent payment provider   |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|--------------|--------|---------|-------------|---------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn` |
| `page`       | int    | `0`     | >= 0        | Zero-based page index                                                                             |
| `size`       | int    | `10`    | 1 – 50      | Number of items per page                                                                           |

> **Note:** `sortBy`/`sortDir` are accepted on the request object but there are no sortable fields
> registered for this endpoint — passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default
> (sorted by `id` ascending).

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
      "name": "bKash",
      "description": "Payment settled through the bKash mobile financial service.",
      "sort_order": 1
    },
    {
      "id": 6,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "বিকাশ",
      "description": "",
      "sort_order": 1
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

### Count Payment Provider Locales

`GET /api/v1/payment-providers/{payment-provider-id}/locales/count`

Returns how many active locale translations a payment provider currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active locale
codes) to determine which languages the provider is still missing and can add a translation for via
[Create Payment Provider Locale](#create-payment-provider-locale) — e.g. if the platform has `en`, `bn`,
`es` and this endpoint returns `en` for the provider, `bn` and `es` are still available; if it returns all
three, every platform locale already has a translation and `POST .../locales` for any of them will fail
with `409 CONFLICT`.

#### Path Parameters

| Parameter              | Type | Description                       |
|--------------------------|------|-------------------------------------|
| `payment-provider-id`  | Long | ID of the parent payment provider   |

#### Response `200 OK`

```json
{
  "count": 1,
  "codes": [
    "en"
  ]
}
```

---

### Create Payment Provider Locale

`POST /api/v1/payment-providers/{payment-provider-id}/locales`

Adds a new locale translation to an existing payment provider. `locale_id` must reference an existing,
active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of provider and
locale must be unique — adding a locale the provider already has a translation for returns `409 CONFLICT`,
pre-checked at the application level before any write (backed by a DB-level unique constraint
(`uq_payment_provider_locale` on `payment_provider_id` + `locale_id`) as a last-resort guard).

#### Path Parameters

| Parameter              | Type | Description                       |
|--------------------------|------|-------------------------------------|
| `payment-provider-id`  | Long | ID of the parent payment provider   |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "বিকাশ",
  "description": "",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|----------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale |
| `name`        | String  | Yes      | Not blank, max 150 chars                    |
| `description` | String  | Yes      | Not null                                     |
| `sort_order`  | Integer | Yes      | Not null                                     |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 6
}
```

---

### Update Payment Provider Locale

`PUT /api/v1/payment-providers/{payment-provider-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing payment provider locale translation. The
associated provider and locale cannot be changed after creation.

#### Path Parameters

| Parameter              | Type | Description                       |
|--------------------------|------|-------------------------------------|
| `payment-provider-id`  | Long | ID of the parent payment provider   |
| `id`                   | Long | ID of the payment provider locale   |

#### Request Body

```json
{
  "name": "বিকাশ",
  "description": "",
  "sort_order": 1
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
  "id": 6
}
```

---

### Delete Payment Provider Locale

`DELETE /api/v1/payment-providers/{payment-provider-id}/locales/{id}`

Soft-deletes a payment provider locale. The record is not removed from the database but will no longer
appear in any response.

#### Path Parameters

| Parameter              | Type | Description                       |
|--------------------------|------|-------------------------------------|
| `payment-provider-id`  | Long | ID of the parent payment provider   |
| `id`                   | Long | ID of the payment provider locale   |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 6
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
  "message": "PaymentProvider not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                    |
|-------------|-----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value                                                    |
| 404         | `ENTITY_NOT_FOUND`         | Payment provider not found, payment provider locale not found, payment method referenced by `payment_method_id` not found (provider creation), or the locale referenced by `locale_id` not found (locale creation)                       |
| 409         | `CONFLICT`                 | `code` already in use by another active provider under the same `payment_method_id` (`create`); or the provider already has a translation for the given `locale_id` (`create` provider locale, pre-checked at the application level)     |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint (`uq_payment_provider_locale`) on `payment_provider_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level                             |
