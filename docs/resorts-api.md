# Resorts API

Base URL: `/api/v1/resorts`

A resort is a top-level property identified by a unique `code`. Every resort is created together with exactly
one **ResortBasicInfo** (identity/branding fields) and exactly one **ResortAddress** (location fields) — both
are mandatory one-to-one details of the resort, created in the same transaction as the resort itself and
soft-deleted together with it when the resort is deleted. Each of them additionally has its own locale
sub-resource for translated fields (name/tagline/description for basic info, the street address text for
address).

**Every resort is also created together with its weekly schedule** — the [Resort Weekly Schedule
API](resort-weekly-schedule-api.md)'s `weekday_day_of_week_ids`/`weekend_day_of_week_ids` lists, required on
`POST /api/v1/resorts` via the `weekly_schedule` field, created in the same transaction as the resort. There is
no way to create a resort without one and no separate "create" step for it afterward — once the resort exists,
the schedule can only be *replaced as a whole* via
[`PUT /api/v1/resorts/{resort-id}/weekly-schedule`](resort-weekly-schedule-api.md#update-weekly-schedule), never
edited one day at a time.

**Creating a resort automatically makes the authenticated user its owner.** `POST /api/v1/resorts` resolves
the caller from the request's JWT, then — in the same transaction as the resort, its basic info, and its
address — creates a `resort_users` row linking that user to the new resort with the `OWNER` resort role, and a
`resort_user_permissions` row granting that membership the `ALL_PERMISSIONS` permission (`is_allowed = true`).
There is no request field to choose a different owner or role; it is always the caller, always `OWNER`, always
`ALL_PERMISSIONS`. Membership and permission management have no API surface yet — the underlying
`resort_users`/`resort_user_permissions` tables exist only to support this create-time assignment.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is actually
used:

- **`GET /api/v1/resorts/{id}`** — the header's value selects exactly one locale translation for
  `basic_info.locale` and `address.locale` (and, inside `address`, for the embedded `country.locale`/
  `city.locale` too): an exact match if a translation exists, otherwise `en`, otherwise `null`.
- **`GET /api/v1/resorts/{resort-id}/address`** — same single-locale-matching behavior as above, scoped to
  just the address.
- **`GET .../basic-info/locales`, `GET .../address/locales`** — the header must be present, but its value has
  no effect; these return every translation (optionally filtered by `localeCode`), not a single
  Accept-Language-matched one.
- **`GET /api/v1/resorts`, `GET /api/v1/resorts/my-resorts`** — list rows never include `basic_info`/`address`
  at all (see [List / Search Resorts](#list--search-resorts)), so the header's value has no effect on them.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                   | Description                                          |
|--------|--------------------------------------------------------|------------------------------------------------------|
| POST   | `/api/v1/resorts`                                      | Create a resort (with its basic info and address)    |
| GET    | `/api/v1/resorts`                                      | List / search resorts                                |
| GET    | `/api/v1/resorts/my-resorts`                           | List resorts the authenticated user has access to    |
| GET    | `/api/v1/resorts/{id}`                                 | Get a resort (with its basic info and address)       |
| PUT    | `/api/v1/resorts/{id}`                                 | Update a resort                                      |
| DELETE | `/api/v1/resorts/{id}`                                 | Delete a resort (cascades to basic info and address) |
| PUT    | `/api/v1/resorts/{resort-id}/basic-info`               | Update a resort's basic info                         |
| GET    | `/api/v1/resorts/{resort-id}/basic-info/locales`       | List a resort basic info's locales                   |
| GET    | `/api/v1/resorts/{resort-id}/basic-info/locales/count` | Count a resort basic info's used platform locales    |
| POST   | `/api/v1/resorts/{resort-id}/basic-info/locales`       | Create a resort basic info locale                    |
| PUT    | `/api/v1/resorts/{resort-id}/basic-info/locales/{id}`  | Update a resort basic info locale                    |
| DELETE | `/api/v1/resorts/{resort-id}/basic-info/locales/{id}`  | Delete a resort basic info locale                    |
| GET    | `/api/v1/resorts/{resort-id}/address`                  | Get a resort's address                               |
| PUT    | `/api/v1/resorts/{resort-id}/address`                  | Update a resort's address                            |
| GET    | `/api/v1/resorts/{resort-id}/address/locales`          | List a resort address's locales                      |
| GET    | `/api/v1/resorts/{resort-id}/address/locales/count`    | Count a resort address's used platform locales       |
| POST   | `/api/v1/resorts/{resort-id}/address/locales`          | Create a resort address locale                       |
| PUT    | `/api/v1/resorts/{resort-id}/address/locales/{id}`     | Update a resort address locale                       |
| DELETE | `/api/v1/resorts/{resort-id}/address/locales/{id}`     | Delete a resort address locale                       |

There is **no standalone create/get/delete** for basic info or address — they only exist tied to a resort's own
lifecycle (see the intro above). `GET /api/v1/resorts/{resort-id}/basic-info` does not exist at all; basic info
is only ever read embedded in `GET /api/v1/resorts/{id}`.

---

## Data Model

### Resort

| Field        | Type   | Required | Constraints                                                            | Description                                 |
|--------------|--------|----------|------------------------------------------------------------------------|---------------------------------------------|
| `id`         | Long   | —        | read-only                                                              | Auto-generated identifier                   |
| `code`       | String | Yes      | max 100 chars, unique among active records; set at creation, immutable | Internal unique code identifying the resort |
| `basic_info` | Object | —        | see ResortBasicInfo below; only present on `GET /{id}`                 | The resort's basic info                     |
| `address`    | Object | —        | see ResortAddress below; only present on `GET /{id}`                   | The resort's address                        |

### ResortBasicInfo

| Field      | Type   | Required | Constraints                               | Description                                                                                       |
|------------|--------|----------|-------------------------------------------|---------------------------------------------------------------------------------------------------|
| `id`       | Long   | —        | read-only                                 | Auto-generated identifier                                                                         |
| `estd`     | Short  | Yes      | not null                                  | Year the resort was established                                                                   |
| `logo_url` | String | —        | nullable                                  | URL of the resort's logo                                                                          |
| `locale`   | Object | —        | nullable; see ResortBasicInfoLocale below | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null`) |

### ResortBasicInfoLocale

| Field               | Type    | Required | Constraints                                      | Description                                                                    |
|---------------------|---------|----------|--------------------------------------------------|--------------------------------------------------------------------------------|
| `id`                | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`            | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `sort_order`        | Integer | Yes      | default 0                                        | Display order among locale entries                                             |
| `name`              | String  | Yes      | max 255 chars                                    | Localized resort name                                                          |
| `tagline`           | String  | Yes      | not blank                                        | Short localized marketing tagline                                              |
| `short_description` | String  | —        | nullable, max 1024 chars                         | Localized short description                                                    |

### ResortAddress

| Field         | Type    | Required | Constraints                              | Description                                                                                       |
|---------------|---------|----------|------------------------------------------|---------------------------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                | Auto-generated identifier                                                                         |
| `country`     | Country | —        | see [countries-api.md](countries-api.md) | The resort's country (single-locale embedded, per its own Accept-Language matching)               |
| `city`        | City    | —        | see [countries-api.md](countries-api.md) | The resort's city (single-locale embedded, per its own Accept-Language matching)                  |
| `postal_code` | String  | —        | nullable, max 50 chars                   | Postal / ZIP code                                                                                 |
| `lat`         | Double  | —        | nullable                                 | Latitude                                                                                          |
| `lon`         | Double  | —        | nullable                                 | Longitude                                                                                         |
| `locale`      | Object  | —        | nullable; see ResortAddressLocale below  | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null`) |

### ResortAddressLocale

| Field        | Type    | Required | Constraints                                      | Description                                                                    |
|--------------|---------|----------|--------------------------------------------------|--------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                                        | Auto-generated identifier                                                      |
| `locale`     | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`) |
| `address`    | String  | Yes      | not blank                                        | Localized street address text                                                  |
| `sort_order` | Integer | Yes      | default 0                                        | Display order among locale entries                                             |

---

## Create Resort

`POST /api/v1/resorts`

Creates a new resort, its basic info, its address, and its weekly schedule in one transaction, and assigns the
authenticated caller as the resort's `OWNER` with `ALL_PERMISSIONS` (see the intro above). `code` must be
unique among active, non-deleted resorts — attempting to reuse an existing code returns `409 CONFLICT`.

**Each of `basic_info.locale` and `address.locale` is always attached to the `en` locale, resolved by the
server — neither carries a `locale_id`.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the respective locale sub-resource endpoints below.

### Request Body

```json
{
  "code": "SUNSET_BAY",
  "basic_info": {
    "estd": 1998,
    "logo_url": "https://cdn.example.com/logo.png",
    "locale": {
      "sort_order": 1,
      "name": "Sunset Bay Resort",
      "tagline": "Where the sun meets the sea",
      "short_description": "A beachfront escape with world-class amenities."
    }
  },
  "address": {
    "country_id": 1,
    "city_id": 5,
    "postal_code": "4700",
    "lat": 21.4272,
    "lon": 92.0058,
    "locale": {
      "address": "Marine Drive, Cox's Bazar",
      "sort_order": 1
    }
  },
  "weekly_schedule": {
    "weekday_day_of_week_ids": [
      1,
      2,
      3,
      4
    ],
    "weekend_day_of_week_ids": [
      5,
      6
    ]
  }
}
```

### Request Fields

| Field             | Type   | Required | Validation                                            |
|-------------------|--------|----------|-------------------------------------------------------|
| `code`            | String | Yes      | Not blank, max 100 chars, unique among active records |
| `basic_info`      | Object | Yes      | Not null; validated (see below)                       |
| `address`         | Object | Yes      | Not null; validated (see below)                       |
| `weekly_schedule` | Object | Yes      | Not null; validated (see below)                       |

**`basic_info`:**

| Field                      | Type    | Required | Validation                             |
|----------------------------|---------|----------|----------------------------------------|
| `estd`                     | Short   | Yes      | Not null                               |
| `logo_url`                 | String  | —        | —                                      |
| `locale`                   | Object  | Yes      | Not null; no `locale_id` — always `en` |
| `locale.sort_order`        | Integer | Yes      | Not null                               |
| `locale.name`              | String  | Yes      | Not blank, max 255 chars               |
| `locale.tagline`           | String  | Yes      | Not blank                              |
| `locale.short_description` | String  | —        | Max 1024 chars                         |

**`address`:**

| Field               | Type    | Required | Validation                                   |
|---------------------|---------|----------|----------------------------------------------|
| `country_id`        | Long    | Yes      | Not null; must reference an existing country |
| `city_id`           | Long    | Yes      | Not null; must reference an existing city    |
| `postal_code`       | String  | —        | Max 50 chars                                 |
| `lat`               | Double  | —        | —                                            |
| `lon`               | Double  | —        | —                                            |
| `locale`            | Object  | Yes      | Not null; no `locale_id` — always `en`       |
| `locale.address`    | String  | Yes      | Not blank                                    |
| `locale.sort_order` | Integer | Yes      | Not null                                     |

**`weekly_schedule`:** same shape and validation as [Update Weekly
Schedule](resort-weekly-schedule-api.md#update-weekly-schedule)'s request body — this initial schedule is
created in the same transaction as the resort.

| Field                     | Type   | Required | Validation                                                                                                                    |
|---------------------------|--------|----------|-------------------------------------------------------------------------------------------------------------------------------|
| `weekday_day_of_week_ids` | Long[] | Yes      | Not empty; no duplicates; no id shared with `weekend_day_of_week_ids`; each id must reference an existing, active day of week |
| `weekend_day_of_week_ids` | Long[] | Yes      | Not empty; no duplicates; no id shared with `weekday_day_of_week_ids`; each id must reference an existing, active day of week |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

`id` is the created resort's own id.

---

## Get Resort

`GET /api/v1/resorts/{id}`

Returns a single active resort by its ID, with its `basic_info` and `address` embedded in full (each with its
single Accept-Language-matched `locale`; `address.country`/`address.city` are likewise embedded with their own
single-locale-matched `locale`). To fetch every translation basic info or address has, use their respective
locale sub-resource list endpoints below.

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the resort |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "SUNSET_BAY",
    "basic_info": {
      "id": 1,
      "estd": 1998,
      "logo_url": "https://cdn.example.com/logo.png",
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "sort_order": 1,
        "name": "Sunset Bay Resort",
        "tagline": "Where the sun meets the sea",
        "short_description": "A beachfront escape with world-class amenities."
      }
    },
    "address": {
      "id": 1,
      "country": {
        "id": 1,
        "code": "BD",
        "iso3_code": "BGD",
        "phone_code": "880",
        "flag_url": "",
        "sort_order": 1,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Bangladesh",
          "description": "People's Republic of Bangladesh",
          "sort_order": 1
        }
      },
      "city": {
        "id": 5,
        "code": "CXB",
        "sort_order": 1,
        "locale": {
          "id": 5,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Cox's Bazar",
          "description": "",
          "sort_order": 1
        }
      },
      "postal_code": "4700",
      "lat": 21.4272,
      "lon": 92.0058,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "address": "Marine Drive, Cox's Bazar",
        "sort_order": 1
      }
    }
  }
}
```

---

## List / Search Resorts

`GET /api/v1/resorts`

Returns a paginated, filterable list of active (non-deleted) resorts. All filter parameters are optional;
omitting them returns all resorts. Each `LIKE`-type filter performs a case-insensitive partial match.

**List rows never include `basic_info`/`address`** — those two fields are only ever populated by
[Get Resort](#get-resort). This keeps the list endpoint cheap regardless of how much basic-info/address data
a resort has accumulated.

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `ResortFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies. Jackson's `@JsonNaming` (which
> produces snake_case) only applies to `@RequestBody`/`@ResponseBody`; `@ModelAttribute`/`@ParameterObject`
> query-string binding goes through Spring's plain `DataBinder` instead, which matches the exact property
> name.

| Parameter | Type   | Default         | Constraints         | Description                                |
|-----------|--------|-----------------|---------------------|--------------------------------------------|
| `code`    | String | —               | —                   | Filter by code (partial, case-insensitive) |
| `page`    | int    | `0`             | >= 0                | Zero-based page index                      |
| `size`    | int    | `10`            | 1 – 50              | Number of items per page                   |
| `sortBy`  | String | `id` (implicit) | `createdAt`, `code` | Field to sort by                           |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`       | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "SUNSET_BAY"
    },
    {
      "id": 2,
      "code": "MOUNTAIN_VIEW"
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
    "code"
  ],
  "searchable_fields": [
    "code"
  ]
}
```

---

## My Resorts

`GET /api/v1/resorts/my-resorts`

Returns a paginated list of active resorts where the authenticated caller (resolved from the JWT) has an
active, non-deleted `resort_users` membership — i.e. every resort they were made `OWNER` of at creation time,
or were otherwise added to. There is no `code` search filter on this endpoint, only pagination and sorting.
Same as [List / Search Resorts](#list--search-resorts), rows never include `basic_info`/`address`.

### Query Parameters

| Parameter | Type   | Default         | Constraints         | Description              |
|-----------|--------|-----------------|---------------------|--------------------------|
| `page`    | int    | `0`             | >= 0                | Zero-based page index    |
| `size`    | int    | `10`            | 1 – 50              | Number of items per page |
| `sortBy`  | String | `id` (implicit) | `createdAt`, `code` | Field to sort by         |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`       | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "SUNSET_BAY"
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [
    "createdAt",
    "code"
  ],
  "searchable_fields": []
}
```

---

## Update Resort

`PUT /api/v1/resorts/{id}`

Resort has no updatable fields — `code` is set at creation and cannot be changed. This endpoint exists for
API-shape consistency and accepts an empty body; it does not modify any resort field. To update basic info or
address, use their own endpoints below.

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the resort |

### Request Body

```json
{}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Resort

`DELETE /api/v1/resorts/{id}`

Soft-deletes the resort, and cascades the same soft-delete to its ResortBasicInfo and ResortAddress in the
same transaction. The records are not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the resort |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Update Resort Basic Info

`PUT /api/v1/resorts/{resort-id}/basic-info`

Updates `estd` and `logo_url`. Basic info has no standalone create/get/delete — it's created together with the
resort and only ever read embedded in [Get Resort](#get-resort) (see the intro above).

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

### Request Body

```json
{
  "estd": 1998,
  "logo_url": "https://cdn.example.com/logo-v2.png"
}
```

### Request Fields

| Field      | Type   | Required | Validation |
|------------|--------|----------|------------|
| `estd`     | Short  | Yes      | Not null   |
| `logo_url` | String | —        | —          |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

`id` is the ResortBasicInfo's own id (not the resort's id).

---

## Resort Basic Info Locales

Manage locale-specific name/tagline/description translations for a resort's basic info. The `{resort-id}` path
parameter must reference an existing, active resort (with an existing basic info — always true once the resort
was created).

### List Resort Basic Info Locales

`GET /api/v1/resorts/{resort-id}/basic-info/locales`

Returns a paginated list of every locale translation the basic info has — the only way to see more than the
single Accept-Language-matched translation returned by [Get Resort](#get-resort). Optionally filtered to
locales whose `code` contains a given substring.

#### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|--------------|--------|---------|-------------|-------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn` |
| `page`       | int    | `0`     | >= 0        | Zero-based page index                                                                           |
| `size`       | int    | `10`    | 1 – 50      | Number of items per page                                                                        |

> **Note:** `sortBy`/`sortDir` are accepted but there are no sortable fields registered for this endpoint —
> passing any non-null `sortBy` throws `400 INVALID_ARGUMENT: Invalid sort field: <value>`.

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
      "sort_order": 1,
      "name": "Sunset Bay Resort",
      "tagline": "Where the sun meets the sea",
      "short_description": "A beachfront escape with world-class amenities."
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

---

### Count Resort Basic Info Locales

`GET /api/v1/resorts/{resort-id}/basic-info/locales/count`

Returns how many active, non-deleted platform [Locale](locales-api.md) codes the resort's basic info already
has an active translation for, together with each one's `code`. Matched via `locale_id`. `count` is always
`codes.length`. Use this to gray out/disable locales already present in `codes` when building the picker for
[Create Resort Basic Info Locale](#create-resort-basic-info-locale) — `locale_id` must not already have a
translation for this resort's basic info, or the create call returns `409 CONFLICT`.

#### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

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

### Create Resort Basic Info Locale

`POST /api/v1/resorts/{resort-id}/basic-info/locales`

Adds a new locale translation to a resort's basic info. `locale_id` must reference an existing, active locale
— an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of basic info and locale must be
unique — adding a locale it already has a translation for returns `409 CONFLICT`.

#### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

#### Request Body

```json
{
  "locale_id": 2,
  "sort_order": 2,
  "name": "সানসেট বে রিসোর্ট",
  "tagline": "যেখানে সূর্য সাগরের সাথে মিলিত হয়",
  "short_description": "বিশ্বমানের সুযোগ-সুবিধা সহ একটি সৈকত পলায়ন।"
}
```

#### Request Fields

| Field               | Type    | Required | Validation                                  |
|---------------------|---------|----------|---------------------------------------------|
| `locale_id`         | Long    | Yes      | Not null; must reference an existing locale |
| `sort_order`        | Integer | Yes      | Not null                                    |
| `name`              | String  | Yes      | Not blank, max 255 chars                    |
| `tagline`           | String  | Yes      | Not blank                                   |
| `short_description` | String  | —        | Max 1024 chars                              |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 2
}
```

---

### Update Resort Basic Info Locale

`PUT /api/v1/resorts/{resort-id}/basic-info/locales/{id}`

Updates `sort_order`, `name`, `tagline`, and `short_description`. The associated basic info and locale cannot
be changed after creation.

#### Path Parameters

| Parameter   | Type | Description                        |
|-------------|------|------------------------------------|
| `resort-id` | Long | ID of the owning resort            |
| `id`        | Long | ID of the resort basic info locale |

#### Request Body

```json
{
  "sort_order": 2,
  "name": "সানসেট বে রিসোর্ট",
  "tagline": "যেখানে সূর্য সাগরের সাথে মিলিত হয়",
  "short_description": "বিশ্বমানের সুযোগ-সুবিধা সহ একটি সৈকত পলায়ন।"
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

### Delete Resort Basic Info Locale

`DELETE /api/v1/resorts/{resort-id}/basic-info/locales/{id}`

Soft-deletes a resort basic info locale.

#### Path Parameters

| Parameter   | Type | Description                        |
|-------------|------|------------------------------------|
| `resort-id` | Long | ID of the owning resort            |
| `id`        | Long | ID of the resort basic info locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

## Get Resort Address

`GET /api/v1/resorts/{resort-id}/address`

Returns the resort's address, with `country`/`city` embedded (each single-locale-matched) and its own
`locale` field single-Accept-Language-matched. Open to any authenticated user — not permission-gated.

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "country": {
      "id": 1,
      "code": "BD",
      "iso3_code": "BGD",
      "phone_code": "880",
      "flag_url": "",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Bangladesh",
        "description": "People's Republic of Bangladesh",
        "sort_order": 1
      }
    },
    "city": {
      "id": 5,
      "code": "CXB",
      "sort_order": 1,
      "locale": {
        "id": 5,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Cox's Bazar",
        "description": "",
        "sort_order": 1
      }
    },
    "postal_code": "4700",
    "lat": 21.4272,
    "lon": 92.0058,
    "locale": {
      "id": 1,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "address": "Marine Drive, Cox's Bazar",
      "sort_order": 1
    }
  }
}
```

---

## Update Resort Address

`PUT /api/v1/resorts/{resort-id}/address`

Updates `country_id`, `city_id`, `postal_code`, `lat`, and `lon` — unlike basic info, the address's country and
city **are** updatable after creation. Address has no standalone create/delete — it's created together with
the resort (see the intro above).

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

### Request Body

```json
{
  "country_id": 1,
  "city_id": 5,
  "postal_code": "4700",
  "lat": 21.4272,
  "lon": 92.0058
}
```

### Request Fields

| Field         | Type   | Required | Validation                                   |
|---------------|--------|----------|----------------------------------------------|
| `country_id`  | Long   | Yes      | Not null; must reference an existing country |
| `city_id`     | Long   | Yes      | Not null; must reference an existing city    |
| `postal_code` | String | —        | Max 50 chars                                 |
| `lat`         | Double | —        | —                                            |
| `lon`         | Double | —        | —                                            |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

`id` is the ResortAddress's own id (not the resort's id).

---

## Resort Address Locales

Manage locale-specific street-address translations for a resort's address. The `{resort-id}` path parameter
must reference an existing, active resort (with an existing address — always true once the resort was
created).

### List Resort Address Locales

`GET /api/v1/resorts/{resort-id}/address/locales`

Returns a paginated list of every locale translation the address has — the only way to see more than the
single Accept-Language-matched translation returned by [Get Resort Address](#get-resort-address) /
[Get Resort](#get-resort). Optionally filtered to locales whose `code` contains a given substring.

#### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|--------------|--------|---------|-------------|-------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn` |
| `page`       | int    | `0`     | >= 0        | Zero-based page index                                                                           |
| `size`       | int    | `10`    | 1 – 50      | Number of items per page                                                                        |

> **Note:** `sortBy`/`sortDir` are accepted but there are no sortable fields registered for this endpoint —
> passing any non-null `sortBy` throws `400 INVALID_ARGUMENT: Invalid sort field: <value>`.

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
      "address": "Marine Drive, Cox's Bazar",
      "sort_order": 1
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

---

### Count Resort Address Locales

`GET /api/v1/resorts/{resort-id}/address/locales/count`

Returns how many active, non-deleted platform [Locale](locales-api.md) codes the resort's address already has
an active translation for, together with each one's `code`. Matched via `locale_id`. `count` is always
`codes.length`. Use this to gray out/disable locales already present in `codes` when building the picker for
[Create Resort Address Locale](#create-resort-address-locale) — `locale_id` must not already have a
translation for this resort's address, or the create call returns `409 CONFLICT`.

#### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

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

### Create Resort Address Locale

`POST /api/v1/resorts/{resort-id}/address/locales`

Adds a new locale translation to a resort's address. `locale_id` must reference an existing, active locale —
an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of address and locale must be unique —
adding a locale it already has a translation for returns `409 CONFLICT`.

#### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

#### Request Body

```json
{
  "locale_id": 2,
  "address": "মেরিন ড্রাইভ, কক্সবাজার",
  "sort_order": 2
}
```

#### Request Fields

| Field        | Type    | Required | Validation                                  |
|--------------|---------|----------|---------------------------------------------|
| `locale_id`  | Long    | Yes      | Not null; must reference an existing locale |
| `address`    | String  | Yes      | Not blank                                   |
| `sort_order` | Integer | Yes      | Not null                                    |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 2
}
```

---

### Update Resort Address Locale

`PUT /api/v1/resorts/{resort-id}/address/locales/{id}`

Updates `address` and `sort_order`. The associated address and locale cannot be changed after creation.

#### Path Parameters

| Parameter   | Type | Description                     |
|-------------|------|---------------------------------|
| `resort-id` | Long | ID of the owning resort         |
| `id`        | Long | ID of the resort address locale |

#### Request Body

```json
{
  "address": "মেরিন ড্রাইভ, কক্সবাজার",
  "sort_order": 2
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

### Delete Resort Address Locale

`DELETE /api/v1/resorts/{resort-id}/address/locales/{id}`

Soft-deletes a resort address locale.

#### Path Parameters

| Parameter   | Type | Description                     |
|-------------|------|---------------------------------|
| `resort-id` | Long | ID of the owning resort         |
| `id`        | Long | ID of the resort address locale |

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
  "message": "Resort not found with id: 99"
}
```

| HTTP Status | Error Code         | Cause                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
|-------------|--------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT` | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields, including an empty `weekly_schedule.weekday_day_of_week_ids`/`weekend_day_of_week_ids` on `create`; an unsupported `sortBy` query value                                                                                                                                                                                                                                                                            |
| 404         | `ENTITY_NOT_FOUND` | Resort not found; resort basic info / address not found for the resort id; resort basic info locale / address locale not found; the country/city referenced by `country_id`/`city_id` not found; the locale referenced by `locale_id` not found; an unknown day of week id in `weekly_schedule.weekday_day_of_week_ids`/`weekend_day_of_week_ids` (`create`); or, on `create` only, the `OWNER` resort role type or `ALL_PERMISSIONS` resort permission type is missing/inactive in the platform's seed data (should not occur in a correctly seeded environment) |
| 409         | `CONFLICT`         | `code` already in use by another active resort (`create`); a duplicate day of week id within one `weekly_schedule` list, or the same day of week id appearing in both `weekday_day_of_week_ids` and `weekend_day_of_week_ids` (`create`); the basic info / address already has a translation for the given `locale_id` (create locale sub-resource)                                                                                                                                                                                                               |
