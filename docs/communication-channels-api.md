# Communication Channels API

Base URL: `/api/v1/communication-channels`

Communication channels represent the kinds of contact methods a resort can expose (e.g. `PHONE`, `MOBILE`,
`EMAIL`, `WHATSAPP`, `WEBSITE`, `FACEBOOK`, `INSTAGRAM`, `X`, `LINKEDIN`, `TELEGRAM`, `WECHAT`, `FAX`). Each
channel is identified by a unique `code` and carries three boolean classification flags (`is_url`, `is_phone`,
`is_email`) plus a UI hint (`is_clickable`) describing how the value should be rendered/handled by clients. A
channel's display name and description are locale-specific and are managed through a companion sub-resource —
Communication Channel Locales — reached via `/api/v1/communication-channels/{communication-channel-id}/locales`.
All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with
a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint
(see [Error Responses](#error-responses)). What differs per endpoint is whether the header's *value* is
actually used to shape the response:

- **`GET /{id}` (Get Communication Channel)** and **`GET` (List/Search Communication Channels)** — the
  header's value selects exactly one locale translation for the channel's `locale` field: an exact match if
  the channel has one, otherwise `en`, otherwise `null`.
- **`GET /{communication-channel-id}/locales` (List Communication Channel Locales)** — the header must be
  present, but its value has no effect; this endpoint returns every translation (optionally filtered by
  `localeCode`), not a single Accept-Language-matched one.
- **`POST`/`PUT`/`DELETE`** — the header must be present but its value has no effect at all.

---

## Endpoints

| Method | Path                                                                    | Description                             |
|--------|----------------------------------------------------------------------------|----------------------------------------------|
| POST   | `/api/v1/communication-channels`                                            | Create a communication channel                |
| GET    | `/api/v1/communication-channels`                                            | List / search communication channels          |
| GET    | `/api/v1/communication-channels/{id}`                                       | Get a communication channel                   |
| PUT    | `/api/v1/communication-channels/{id}`                                       | Update a communication channel                |
| DELETE | `/api/v1/communication-channels/{id}`                                       | Delete a communication channel                |
| GET    | `/api/v1/communication-channels/{communication-channel-id}/locales`         | List a communication channel's locales        |
| POST   | `/api/v1/communication-channels/{communication-channel-id}/locales`         | Create a communication channel locale         |
| PUT    | `/api/v1/communication-channels/{communication-channel-id}/locales/{id}`    | Update a communication channel locale         |
| DELETE | `/api/v1/communication-channels/{communication-channel-id}/locales/{id}`    | Delete a communication channel locale         |

---

## Data Model

### CommunicationChannel

| Field          | Type    | Required | Constraints                                                             | Description                                                                                                                                        |
|----------------|---------|----------|------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`           | Long    | —        | read-only                                                                     | Auto-generated identifier                                                                                                                              |
| `code`         | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable        | Internal code (e.g. `PHONE`, `MOBILE`, `EMAIL`, `WHATSAPP`, `WEBSITE`, `FACEBOOK`, `INSTAGRAM`, `X`, `LINKEDIN`, `TELEGRAM`, `WECHAT`, `FAX`)          |
| `sort_order`   | Integer | Yes      | default 0                                                                     | Display order                                                                                                                                          |
| `is_url`       | Boolean | Yes      | default `false`                                                               | Whether this channel expects a URL value (e.g. `WEBSITE`, `FACEBOOK`, `INSTAGRAM`)                                                                     |
| `is_phone`     | Boolean | Yes      | default `false`                                                               | Whether this channel represents a phone number (e.g. `PHONE`, `MOBILE`, `WHATSAPP`)                                                                    |
| `is_email`     | Boolean | Yes      | default `false`                                                               | Whether this channel represents an email address (e.g. `EMAIL`)                                                                                        |
| `is_clickable` | Boolean | Yes      | default `true`                                                                | Whether the value should be rendered as a clickable link/action in the UI                                                                              |
| `locale`       | Object  | —        | nullable; see CommunicationChannelLocale below                               | The single translation matching the request's `Accept-Language` (falls back to `en`, then `null` if the channel has no translations at all)           |

### CommunicationChannelLocale

| Field         | Type    | Required | Constraints                                      | Description                                                                    |
|---------------|---------|----------|---------------------------------------------------|----------------------------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                        | Auto-generated identifier                                                        |
| `locale`      | Locale  | —        | read-only, resolved from `locale_id` at creation | The locale this translation is written in (`id`, `code`, `name`, `sort_order`)   |
| `name`        | String  | Yes      | max 100 chars                                    | Localized display name of the communication channel                              |
| `description` | String  | Yes      | not null (defaults to `""`)                      | Localized description                                                            |
| `sort_order`  | Integer | Yes      | default 0                                        | Display order among locale entries                                               |

---

## Create Communication Channel

`POST /api/v1/communication-channels`

Creates a new communication channel together with exactly **one** initial locale translation. `code` must be
unique among active, non-deleted communication channels — attempting to reuse an existing code returns
`409 CONFLICT`.

**The initial translation is always attached to the `en` locale, resolved by the server — the request
carries no `locale_id` at all.** There is no option to submit multiple locales at creation time.
Additional languages are added afterward via the Communication Channel Locales sub-resource below.

### Request Body

```json
{
  "code": "WHATSAPP",
  "sort_order": 3,
  "is_url": false,
  "is_phone": true,
  "is_email": false,
  "is_clickable": true,
  "locale": {
    "name": "WhatsApp",
    "description": "WhatsApp number for instant messaging and calls.",
    "sort_order": 3
  }
}
```

### Request Fields

| Field          | Type    | Required | Validation                                                                                 |
|----------------|---------|----------|------------------------------------------------------------------------------------------------|
| `code`         | String  | Yes      | Not blank, max 50 chars, unique among active records                                            |
| `sort_order`   | Integer | Yes      | Not null                                                                                          |
| `is_url`       | Boolean | Yes      | Not null                                                                                          |
| `is_phone`     | Boolean | Yes      | Not null                                                                                          |
| `is_email`     | Boolean | Yes      | Not null                                                                                          |
| `is_clickable` | Boolean | Yes      | Not null                                                                                          |
| `locale`       | Object  | Yes      | Not null; validated (see below) — no `locale_id` field; always resolved to the `en` locale        |

**Locale entry (`locale`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

## Get Communication Channel

`GET /api/v1/communication-channels/{id}`

Returns a single active communication channel by its ID. `locale` is the one translation matching the
request's `Accept-Language` header (falls back to `en`, then `null` if the channel has no translations at
all). To fetch every translation a channel has, use
[List Communication Channel Locales](#list-communication-channel-locales) below.

### Path Parameters

| Parameter | Type | Description                     |
|-----------|------|-----------------------------------|
| `id`      | Long | ID of the communication channel   |

### Response `200 OK`

```json
{
  "data": {
    "id": 3,
    "code": "WHATSAPP",
    "sort_order": 3,
    "is_url": false,
    "is_phone": true,
    "is_email": false,
    "is_clickable": true,
    "locale": {
      "id": 3,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "WhatsApp",
      "description": "WhatsApp number for instant messaging and calls.",
      "sort_order": 3
    }
  }
}
```

---

## List / Search Communication Channels

`GET /api/v1/communication-channels`

Returns a paginated, filterable list of active (non-deleted) communication channels. All filter parameters
are optional; omitting them returns all channels. Multiple filters are combined with AND. Each `LIKE`-type
filter performs a case-insensitive partial match. `Accept-Language` selects each channel's `locale` field the
same way as `GET /{id}` (exact match, falls back to `en`, then `null`).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Query Parameters

> **Note:** Query parameters bind directly onto `CommunicationChannelFilterRequest`'s Java field names, so
> they are **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter | Type   | Default         | Constraints                                | Description                                                                                 |
|-----------|--------|-----------------|-----------------------------------------------|--------------------------------------------------------------------------------------------------|
| `code`    | String | —               | —                                              | Filter by code (partial, case-insensitive)                                                       |
| `name`    | String | —               | —                                              | Filter by locale-specific name (partial, case-insensitive), scoped to the resolved locale        |
| `page`    | int    | `0`             | >= 0                                           | Zero-based page index                                                                             |
| `size`    | int    | `10`            | 1 – 50                                         | Number of items per page                                                                          |
| `sortBy`  | String | `id` (implicit) | `createdAt`, `code`, `name` (`id` NOT selectable) | Field to sort by                                                                              |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                                  | Sort direction                                                                                    |

> **Note:** `sort_order`, `is_url`, `is_phone`, `is_email`, and `is_clickable` are not filterable or
> sortable — only `code` and locale `name` are wired into the search/sort infrastructure for this endpoint.
> Boolean fields were deliberately left out of the filter: this codebase's shared search infrastructure
> (`SearchFieldSpec`/`SpecificationUtils`) currently only supports `String`-typed filter values, with no
> existing precedent for a boolean-typed filter field, so wiring one up would have required changes to
> shared, cross-entity infrastructure used by every other filterable resource.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "PHONE",
      "sort_order": 1,
      "is_url": false,
      "is_phone": true,
      "is_email": false,
      "is_clickable": true,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Phone",
        "description": "Fixed-line telephone number for direct calls.",
        "sort_order": 1
      }
    },
    {
      "id": 4,
      "code": "EMAIL",
      "sort_order": 4,
      "is_url": false,
      "is_phone": false,
      "is_email": true,
      "is_clickable": true,
      "locale": {
        "id": 4,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Email",
        "description": "Email address for written correspondence.",
        "sort_order": 4
      }
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
    "code",
    "name"
  ],
  "searchable_fields": [
    "code",
    "name"
  ]
}
```

---

## Update Communication Channel

`PUT /api/v1/communication-channels/{id}`

Updates `sort_order`, `is_url`, `is_phone`, `is_email`, and `is_clickable`. `code` is set at creation and
cannot be changed. Locale translations are managed separately via the Communication Channel Locales
sub-resource endpoints below, not through this endpoint.

### Path Parameters

| Parameter | Type | Description                     |
|-----------|------|-----------------------------------|
| `id`      | Long | ID of the communication channel   |

### Request Body

```json
{
  "sort_order": 3,
  "is_url": false,
  "is_phone": true,
  "is_email": false,
  "is_clickable": false
}
```

### Request Fields

| Field          | Type    | Required | Validation |
|----------------|---------|----------|------------|
| `sort_order`   | Integer | Yes      | Not null   |
| `is_url`       | Boolean | Yes      | Not null   |
| `is_phone`     | Boolean | Yes      | Not null   |
| `is_email`     | Boolean | Yes      | Not null   |
| `is_clickable` | Boolean | Yes      | Not null   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

## Delete Communication Channel

`DELETE /api/v1/communication-channels/{id}`

Soft-deletes the communication channel. The record is not removed from the database but will no longer
appear in any response.

### Path Parameters

| Parameter | Type | Description                     |
|-----------|------|-----------------------------------|
| `id`      | Long | ID of the communication channel   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

## Communication Channel Locales

Communication Channel Locale endpoints manage locale-specific name/description translations for a
communication channel. The `{communication-channel-id}` path parameter must reference an existing, active
communication channel.

---

### List Communication Channel Locales

`GET /api/v1/communication-channels/{communication-channel-id}/locales`

Returns a paginated list of every locale translation belonging to a communication channel — this is the
only way to see more than the single Accept-Language-matched translation returned by
`GET /communication-channels/{id}` and `GET /communication-channels`. Optionally filtered to locales whose
`code` contains a given substring.

#### Path Parameters

| Parameter                  | Type | Description                          |
|------------------------------|------|-----------------------------------------|
| `communication-channel-id`   | Long | ID of the parent communication channel  |

#### Query Parameters

| Parameter    | Type   | Default | Constraints | Description                                                                                     |
|--------------|--------|---------|-------------|-----------------------------------------------------------------------------------------------------|
| `localeCode` | String | —       | —           | Filter to locales whose `code` contains this value (partial, case-insensitive), e.g. `en`, `bn`     |
| `page`       | int    | `0`     | >= 0        | Zero-based page index                                                                                |
| `size`       | int    | `10`    | 1 – 50      | Number of items per page                                                                             |

> **Note:** `sortBy`/`sortDir` are accepted on the request object but there are no sortable fields
> registered for this endpoint — passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default
> (sorted by `id` ascending).

#### Response `200 OK`

```json
{
  "data": [
    {
      "id": 3,
      "locale": {
        "id": 1,
        "code": "en",
        "name": "English",
        "sort_order": 1
      },
      "name": "WhatsApp",
      "description": "WhatsApp number for instant messaging and calls.",
      "sort_order": 3
    },
    {
      "id": 14,
      "locale": {
        "id": 2,
        "code": "bn",
        "name": "Bengali",
        "sort_order": 2
      },
      "name": "হোয়াটসঅ্যাপ",
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

### Create Communication Channel Locale

`POST /api/v1/communication-channels/{communication-channel-id}/locales`

Adds a new locale translation to an existing communication channel. `locale_id` must reference an existing,
active locale — an unknown `locale_id` returns `404 ENTITY_NOT_FOUND`. The combination of communication
channel and locale must be unique — adding a locale the channel already has a translation for returns
`409 CONFLICT`, pre-checked at the application level before any write (backed by a DB-level unique constraint
on `(communication_channel_id, locale_id)` as a last-resort guard).

#### Path Parameters

| Parameter                  | Type | Description                          |
|------------------------------|------|-----------------------------------------|
| `communication-channel-id`   | Long | ID of the parent communication channel  |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "হোয়াটসঅ্যাপ",
  "description": "তাৎক্ষণিক বার্তা এবং কলের জন্য হোয়াটসঅ্যাপ নম্বর।",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|-----------------------------------------------|
| `locale_id`   | Long    | Yes      | Not null; must reference an existing locale    |
| `name`        | String  | Yes      | Not blank, max 100 chars                       |
| `description` | String  | Yes      | Not null                                        |
| `sort_order`  | Integer | Yes      | Not null                                        |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 14
}
```

---

### Update Communication Channel Locale

`PUT /api/v1/communication-channels/{communication-channel-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing communication channel locale translation.
The associated communication channel and locale cannot be changed after creation.

#### Path Parameters

| Parameter                  | Type | Description                           |
|------------------------------|------|------------------------------------------|
| `communication-channel-id`   | Long | ID of the parent communication channel   |
| `id`                        | Long | ID of the communication channel locale   |

#### Request Body

```json
{
  "name": "হোয়াটসঅ্যাপ",
  "description": "তাৎক্ষণিক বার্তা এবং কলের জন্য হোয়াটসঅ্যাপ নম্বর।",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 14
}
```

---

### Delete Communication Channel Locale

`DELETE /api/v1/communication-channels/{communication-channel-id}/locales/{id}`

Soft-deletes a communication channel locale. The record is not removed from the database but will no longer
appear in any response.

#### Path Parameters

| Parameter                  | Type | Description                           |
|------------------------------|------|------------------------------------------|
| `communication-channel-id`   | Long | ID of the parent communication channel   |
| `id`                        | Long | ID of the communication channel locale   |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 14
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
  "message": "CommunicationChannel not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                     |
|-------------|-----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value |
| 404         | `ENTITY_NOT_FOUND`         | Communication channel not found, communication channel locale not found, or the locale referenced by `locale_id` not found (locale creation)               |
| 409         | `CONFLICT`                 | `code` already in use by another active communication channel (`create`); or the communication channel already has a translation for the given `locale_id` (`create` locale, pre-checked at the application level) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `communication_channel_id` + `locale_id`, should not normally be reachable now that the duplicate is pre-checked at the application level |
