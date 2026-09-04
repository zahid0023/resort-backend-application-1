# Payment Methods API

Base URL: `/api/v1/payment-methods`

Payment methods represent how a booking payment was actually settled (e.g. `CASH`, `CARD`,
`BANK_TRANSFER`, `MOBILE_BANKING`, `ONLINE_GATEWAY`, `OTHER`) — consumed by `resort_booking_payments
.payment_method_id`, which does not yet have its own CRUD API. Each method is identified by a unique
`code`. A method's display name and description are
locale-specific and are managed through a companion sub-resource — Payment Method Locales — reached via
`/api/v1/payment-methods/{payment-method-id}/locales`. The platform ships with six seeded methods (`CASH`,
`CARD`, `BANK_TRANSFER`, `MOBILE_BANKING`, `ONLINE_GATEWAY`, `OTHER`), but this list is **not read-only** —
additional methods can be created, and existing ones updated or deleted, through this API. All records
support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Payment Method)** and **`GET` (List Payment Methods)** — the header's value selects
  exactly one locale translation for the method's `locale` field: an exact match if the method has one,
  otherwise `en`, otherwise `null`.
- **`GET /{payment-method-id}/locales` (List Payment Method Locales)** — the header must be present, but
  its value has no effect; this endpoint returns every translation (optionally filtered by `localeCode`),
  not a single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                       | Description                  |
|--------|-------------------------------------------------------------|-------------------------------|
| POST   | `/api/v1/payment-methods`                                    | Create a payment method       |
| GET    | `/api/v1/payment-methods`                                    | List payment methods          |
| GET    | `/api/v1/payment-methods/{id}`                               | Get a payment method          |
| PUT    | `/api/v1/payment-methods/{id}`                               | Update a payment method       |
| DELETE | `/api/v1/payment-methods/{id}`                               | Delete a payment method       |
| GET    | `/api/v1/payment-methods/{payment-method-id}/locales`        | List a method's locales       |
| GET    | `/api/v1/payment-methods/{payment-method-id}/locales/count`  | Count a method's locales      |
| POST   | `/api/v1/payment-methods/{payment-method-id}/locales`        | Create a method locale        |
| PUT    | `/api/v1/payment-methods/{payment-method-id}/locales/{id}`   | Update a method locale        |
| DELETE | `/api/v1/payment-methods/{payment-method-id}/locales/{id}`   | Delete a method locale        |

---

## Data Model

### PaymentMethod

| Field        | Type    | Required | Constraints                                                           | Description                                                                                                                                |
|--------------|---------|----------|-------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                                             | Auto-generated identifier                                                                                                                  |
| `code`       | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Internal code, e.g. `CASH`, `CARD`, `BANK_TRANSFER`, `MOBILE_BANKING`, `ONLINE_GATEWAY`, `OTHER`                                            |
| `sort_order` | Integer | Yes      | default 0                                                             | Display order                                                                                                                              |
| `locale`     | Object  | —        | nullable; see PaymentMethodLocale below                              | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the method has no translations at all) |

### PaymentMethodLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|---------------------------------------------------|--------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `name`        | String  | Yes      | max 150 chars                                    | Localized display name, e.g. `Mobile Banking`                                 |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                         |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                            |

---

## Create Payment Method

`POST /api/v1/payment-methods`

Creates a new payment method together with exactly **one** initial locale translation. `code` must be
unique among active, non-deleted methods — attempting to reuse an existing code returns `409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Payment Method Locales sub-resource below.

### Request Body

```json
{
  "code": "WALLET",
  "sort_order": 7,
  "locale": {
    "name": "Digital Wallet",
    "description": "Payment settled through a standalone digital wallet app.",
    "sort_order": 1
  }
}
```

### Request Fields

| Field        | Type    | Required | Validation                                                                                 |
|--------------|---------|----------|----------------------------------------------------------------------------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars, unique among active records                                       |
| `sort_order` | Integer | Yes      | Not null                                                                                    |
| `locale`     | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale  |

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
  "id": 7
}
```

---

## Get Payment Method

`GET /api/v1/payment-methods/{id}`

Returns a single active payment method by its ID. `locale` is the one translation matching the request's
`Accept-Language` header (falls back to `en`, then `null` if the method has no translations at all). To
fetch every translation a method has, use [List Payment Method Locales](#list-payment-method-locales) below.

### Path Parameters

| Parameter | Type | Description               |
|-----------|------|----------------------------|
| `id`      | Long | ID of the payment method   |

### Response `200 OK`

```json
{
  "data": {
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
  }
}
```

---

## List Payment Methods

`GET /api/v1/payment-methods`

Returns a paginated list of active (non-deleted) payment methods.

> **Note:** no field on `PaymentMethod` is registered as filterable or sortable beyond the implicit `id`
> default — `searchable_fields` is always `[]` and `sortable_fields` is always `["createdAt"]` in the
> response envelope below. There is no `code`-based filter for this entity (unlike, e.g.,
> [Room Statuses](room-statuses-api.md), which does filter by `code`).

### Query Parameters

> **Note:** Query parameters bind directly onto `PaymentMethodFilterRequest`'s Java field names, so they
> are **camelCase** — not the snake_case used in JSON request/response bodies. Jackson's `@JsonNaming`
> (which produces snake_case) only applies to `@RequestBody`/`@ResponseBody`; `@ModelAttribute` /
> `@ParameterObject` query-string binding goes through Spring's plain `DataBinder` instead, which
> matches the exact property name.

| Parameter | Type   | Default         | Constraints           | Description            |
|-----------|--------|-----------------|------------------------|-------------------------|
| `page`    | int    | `0`             | >= 0                   | Zero-based page index   |
| `size`    | int    | `10`            | 1 – 50                 | Number of items per page |
| `sortBy`  | String | `id` (implicit) | `createdAt` only       | Field to sort by         |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`          | Sort direction            |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "CASH",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Cash",
        "description": "Payment settled in physical cash.",
        "sort_order": 1
      }
    },
    {
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
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 6,
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

## Update Payment Method

`PUT /api/v1/payment-methods/{id}`

Updates `sort_order` only. `code` is set at creation and cannot be changed. Locale translations are managed
separately via the Payment Method Locales sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|---------------------------|
| `id`      | Long | ID of the payment method  |

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
  "id": 4
}
```

---

## Delete Payment Method

`DELETE /api/v1/payment-methods/{id}`

Soft-deletes the payment method. The record is not removed from the database but will no longer appear in
any response.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|---------------------------|
| `id`      | Long | ID of the payment method  |

### Response `200 OK`

```json
{
  "success": true,
  "id": 4
}
```

---

## Payment Method Locales

Payment Method Locale endpoints manage locale-specific name/description translations for a payment method.
The `{payment-method-id}` path parameter must reference an existing, active method.

---

### List Payment Method Locales

`GET /api/v1/payment-methods/{payment-method-id}/locales`

Returns a paginated list of every locale translation belonging to a payment method — this is the only way
to see more than the single Accept-Language-matched translation returned by
`GET /payment-methods/{id}` and `GET /payment-methods`. Optionally filtered to locales whose `code`
contains a given substring.

#### Path Parameters

| Parameter           | Type | Description                     |
|----------------------|------|----------------------------------|
| `payment-method-id`  | Long | ID of the parent payment method  |

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
    },
    {
      "id": 11,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "মোবাইল ব্যাংকিং",
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

### Count Payment Method Locales

`GET /api/v1/payment-methods/{payment-method-id}/locales/count`

Returns how many active locale translations a payment method currently has, plus the `code` of each one.
Compare this against [`GET /api/v1/locales/count`](locales-api.md) (the platform-wide list of active locale
codes) to determine which languages the method is still missing and can add a translation for via
[Create Payment Method Locale](#create-payment-method-locale) — e.g. if the platform has `en`, `bn`, `es`
and this endpoint returns `en` for the method, `bn` and `es` are still available; if it returns all three,
every platform locale already has a translation and `POST .../locales` for any of them will fail with
`409 CONFLICT`.

#### Path Parameters

| Parameter           | Type | Description                     |
|----------------------|------|----------------------------------|
| `payment-method-id`  | Long | ID of the parent payment method  |

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

### Create Payment Method Locale

`POST /api/v1/payment-methods/{payment-method-id}/locales`

Adds a new locale translation to an existing payment method. `locale_id` must reference an existing,
active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of method and locale
must be unique — adding a locale the method already has a translation for returns `409 CONFLICT`,
pre-checked at the application level before any write (backed by a DB-level unique constraint
(`uq_payment_method_locale` on `payment_method_id` + `locale_id`) as a last-resort guard).

#### Path Parameters

| Parameter           | Type | Description                     |
|----------------------|------|----------------------------------|
| `payment-method-id`  | Long | ID of the parent payment method  |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "মোবাইল ব্যাংকিং",
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
  "id": 11
}
```

---

### Update Payment Method Locale

`PUT /api/v1/payment-methods/{payment-method-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing payment method locale translation. The
associated method and locale cannot be changed after creation.

#### Path Parameters

| Parameter           | Type | Description                     |
|----------------------|------|----------------------------------|
| `payment-method-id`  | Long | ID of the parent payment method  |
| `id`                 | Long | ID of the payment method locale  |

#### Request Body

```json
{
  "name": "মোবাইল ব্যাংকিং",
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
  "id": 11
}
```

---

### Delete Payment Method Locale

`DELETE /api/v1/payment-methods/{payment-method-id}/locales/{id}`

Soft-deletes a payment method locale. The record is not removed from the database but will no longer
appear in any response.

#### Path Parameters

| Parameter           | Type | Description                     |
|----------------------|------|----------------------------------|
| `payment-method-id`  | Long | ID of the parent payment method  |
| `id`                 | Long | ID of the payment method locale  |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 11
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
  "message": "PaymentMethod not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                        |
|-------------|-----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value                       |
| 404         | `ENTITY_NOT_FOUND`         | Payment method not found, payment method locale not found, or the locale referenced by `locale_id` not found (locale creation)                                                                              |
| 409         | `CONFLICT`                 | `code` already in use by another active payment method (`create`); or the method already has a translation for the given `locale_id` (`create` method locale, pre-checked at the application level)         |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint (`uq_payment_method_locale`) on `payment_method_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level    |
