# Resort Contacts API

Base URL: `/api/v1/resorts/{resort-id}/contacts`

A resort contact is one piece of contact information exposed by a resort — a phone number, email address,
website, social profile, etc. Each contact is classified by a **contact type** (its purpose — `GENERAL`,
`RESERVATION`, `SALES`, `SUPPORT`, `EMERGENCY`, ...; see [contact-types-api.md](contact-types-api.md)) and a
**communication channel** (how it's reached — `PHONE`, `EMAIL`, `WHATSAPP`, `WEBSITE`, ...; see
[communication-channels-api.md](communication-channels-api.md)). A resort may have any number of contacts, but
only one **primary** contact per `(resort, contact type, communication channel)` combination — see
[Create Resort Contact](#create-resort-contact) below for how that's enforced. Resort contacts are always
reached nested under their owning resort; there is no top-level `/api/v1/resort-contacts` route. Resort
contacts have no locale sub-resource — `contact_value` is not translatable. All records support soft-delete —
deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). Unlike most other resources, **the header's value never shapes any
response on this resource** — `ResortContact` has no locale-specific fields of its own, and while its embedded
`resort`/`contact_type`/`communication_channel` objects each carry their own `locale` field, that's a property
of those resources, not of the resort contact itself (see [contact-types-api.md](contact-types-api.md) /
[communication-channels-api.md](communication-channels-api.md) / [resorts-api.md](resorts-api.md) for how their
own `locale` fields are resolved).

---

## Endpoints

| Method | Path                                        | Description                       |
|--------|---------------------------------------------|-----------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/contacts`      | Create a resort contact           |
| GET    | `/api/v1/resorts/{resort-id}/contacts`      | List / search a resort's contacts |
| GET    | `/api/v1/resorts/{resort-id}/contacts/{id}` | Get a resort contact              |
| PUT    | `/api/v1/resorts/{resort-id}/contacts/{id}` | Update a resort contact           |
| DELETE | `/api/v1/resorts/{resort-id}/contacts/{id}` | Delete a resort contact           |

---

## Data Model

### ResortContact

| Field                   | Type                 | Required | Constraints                                                                                                          | Description                                                                 |
|-------------------------|----------------------|----------|----------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| `id`                    | Long                 | —        | read-only                                                                                                            | Auto-generated identifier                                                   |
| `resort`                | Resort               | —        | see [resorts-api.md](resorts-api.md); embedded without `basic_info`/`address`                                        | The owning resort                                                           |
| `contact_type`          | ContactType          | —        | see [contact-types-api.md](contact-types-api.md); set at creation, immutable                                         | The contact's purpose (e.g. `GENERAL`, `RESERVATION`)                       |
| `communication_channel` | CommunicationChannel | —        | see [communication-channels-api.md](communication-channels-api.md); set at creation, immutable                       | The contact's communication channel (e.g. `PHONE`, `EMAIL`)                 |
| `contact_value`         | String               | Yes      | not blank                                                                                                            | The actual contact value (e.g. `+8801712345678`, `info@resort.com`, a URL)  |
| `is_primary`            | Boolean              | Yes      | default `false`; at most one active `true` row per `(resort, contact_type, communication_channel)` — see notes below | Whether this is the preferred contact for its purpose + channel combination |
| `sort_order`            | Integer              | Yes      | default 0                                                                                                            | Display order                                                               |

**Uniqueness:** the combination of `(resort, contact_type, communication_channel, contact_value)` must be
unique among active records — creating a duplicate returns `409 CONFLICT` (see
[Create Resort Contact](#create-resort-contact)).

---

## Create Resort Contact

`POST /api/v1/resorts/{resort-id}/contacts`

Creates a new contact for the resort. `contact_type_id` and `communication_channel_id` must reference existing,
active records and cannot be changed afterward — to reclassify a contact, delete it and create a new one
instead. The combination of `(resort, contact_type, communication_channel, contact_value)` must be unique among
active records — attempting to reuse one returns `409 CONFLICT`.

**`is_primary`:** if `true`, any existing active contact for the same `(resort, contact_type,
communication_channel)` that is currently primary is automatically flipped to `is_primary: false` in the same
transaction — the request is never rejected for this reason, and at most one contact stays primary per
combination.

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

### Request Body

```json
{
  "contact_type_id": 1,
  "communication_channel_id": 1,
  "contact_value": "+8801712345678",
  "is_primary": true,
  "sort_order": 1
}
```

### Request Fields

| Field                      | Type    | Required | Validation                                                         |
|----------------------------|---------|----------|--------------------------------------------------------------------|
| `contact_type_id`          | Long    | Yes      | Not null; must reference an existing, active contact type          |
| `communication_channel_id` | Long    | Yes      | Not null; must reference an existing, active communication channel |
| `contact_value`            | String  | Yes      | Not blank                                                          |
| `is_primary`               | Boolean | Yes      | Not null                                                           |
| `sort_order`               | Integer | Yes      | Not null                                                           |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Resort Contact

`GET /api/v1/resorts/{resort-id}/contacts/{id}`

Returns a single active resort contact, scoped to its owning resort — an `id` that exists but belongs to a
different resort returns `404 ENTITY_NOT_FOUND`, the same as an unknown `id`.

### Path Parameters

| Parameter   | Type | Description              |
|-------------|------|--------------------------|
| `resort-id` | Long | ID of the owning resort  |
| `id`        | Long | ID of the resort contact |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "resort": {
      "id": 1,
      "code": "SUNSET_BAY"
    },
    "contact_type": {
      "id": 1,
      "code": "RESERVATION",
      "sort_order": 1,
      "locale": {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "name": "Reservations",
        "description": "",
        "sort_order": 1
      }
    },
    "communication_channel": {
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
        "description": "",
        "sort_order": 1
      }
    },
    "contact_value": "+8801712345678",
    "is_primary": true,
    "sort_order": 1
  }
}
```

---

## List / Search Resort Contacts

`GET /api/v1/resorts/{resort-id}/contacts`

Returns a paginated, filterable list of a resort's active (non-deleted) contacts. All filter parameters are
optional; omitting them returns every contact belonging to the resort. Multiple filters are combined with AND.
`contact_value` performs a case-insensitive partial match; `contact_type_id`, `communication_channel_id`, and
`is_primary` are exact matches. Unlike [List / Search Resorts](resorts-api.md#list--search-resorts), list rows
here **do** include the embedded `resort`/`contact_type`/`communication_channel` objects, same as
[Get Resort Contact](#get-resort-contact).

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is omitted
> entirely.

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

### Query Parameters

> **Note:** Query parameters bind directly onto `ResortContactFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies. Jackson's `@JsonNaming` (which
> produces snake_case) only applies to `@RequestBody`/`@ResponseBody`; `@ModelAttribute`/`@ParameterObject`
> query-string binding goes through Spring's plain `DataBinder` instead, which matches the exact property name.

| Parameter                | Type    | Default         | Constraints                                                                                                             | Description                                         |
|--------------------------|---------|-----------------|-------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------|
| `contactTypeId`          | Long    | —               | —                                                                                                                       | Filter by exact contact type id                     |
| `communicationChannelId` | Long    | —               | —                                                                                                                       | Filter by exact communication channel id            |
| `contactValue`           | String  | —               | —                                                                                                                       | Filter by contact value (partial, case-insensitive) |
| `isPrimary`              | Boolean | —               | —                                                                                                                       | Filter by exact `is_primary` value                  |
| `page`                   | int     | `0`             | >= 0                                                                                                                    | Zero-based page index                               |
| `size`                   | int     | `10`            | 1 – 50                                                                                                                  | Number of items per page                            |
| `sortBy`                 | String  | `id` (implicit) | `createdAt`, `contactTypeEntity.id`, `communicationChannelEntity.id`, `contactValue`, `isPrimary` (`id` NOT selectable) | Field to sort by                                    |
| `sortDir`                | String  | `ASC`           | `ASC`, `DESC`                                                                                                           | Sort direction                                      |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort": {
        "id": 1,
        "code": "SUNSET_BAY"
      },
      "contact_type": {
        "id": 1,
        "code": "RESERVATION",
        "sort_order": 1,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Reservations",
          "description": "",
          "sort_order": 1
        }
      },
      "communication_channel": {
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
          "description": "",
          "sort_order": 1
        }
      },
      "contact_value": "+8801712345678",
      "is_primary": true,
      "sort_order": 1
    },
    {
      "id": 2,
      "resort": {
        "id": 1,
        "code": "SUNSET_BAY"
      },
      "contact_type": {
        "id": 1,
        "code": "RESERVATION",
        "sort_order": 1,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Reservations",
          "description": "",
          "sort_order": 1
        }
      },
      "communication_channel": {
        "id": 3,
        "code": "EMAIL",
        "sort_order": 3,
        "is_url": false,
        "is_phone": false,
        "is_email": true,
        "is_clickable": true,
        "locale": {
          "id": 3,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Email",
          "description": "",
          "sort_order": 1
        }
      },
      "contact_value": "reservations@sunsetbay.example.com",
      "is_primary": true,
      "sort_order": 2
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
    "contactTypeEntity.id",
    "communicationChannelEntity.id",
    "contactValue",
    "isPrimary"
  ],
  "searchable_fields": [
    "contactValue"
  ]
}
```

---

## Update Resort Contact

`PUT /api/v1/resorts/{resort-id}/contacts/{id}`

Updates `contact_value`, `is_primary`, and `sort_order`. `contact_type_id` and `communication_channel_id` are
set at creation and cannot be changed — to reclassify a contact, delete it and create a new one instead.

**`is_primary`:** the same auto-unset behavior as [Create Resort Contact](#create-resort-contact) applies —
flipping this contact's `is_primary` from `false` to `true` automatically flips any other currently-primary
contact for the same `(resort, contact_type, communication_channel)` to `false` in the same transaction.

### Path Parameters

| Parameter   | Type | Description              |
|-------------|------|--------------------------|
| `resort-id` | Long | ID of the owning resort  |
| `id`        | Long | ID of the resort contact |

### Request Body

```json
{
  "contact_value": "+8801712345679",
  "is_primary": true,
  "sort_order": 1
}
```

### Request Fields

| Field           | Type    | Required | Validation |
|-----------------|---------|----------|------------|
| `contact_value` | String  | Yes      | Not blank  |
| `is_primary`    | Boolean | Yes      | Not null   |
| `sort_order`    | Integer | Yes      | Not null   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Resort Contact

`DELETE /api/v1/resorts/{resort-id}/contacts/{id}`

Soft-deletes the resort contact. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter   | Type | Description              |
|-------------|------|--------------------------|
| `resort-id` | Long | ID of the owning resort  |
| `id`        | Long | ID of the resort contact |

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
  "message": "ResortContact not found with id: 99"
}
```

| HTTP Status | Error Code         | Cause                                                                                                                                                                                                                    |
|-------------|--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT` | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; an unsupported `sortBy` query value                                       |
| 404         | `ENTITY_NOT_FOUND` | Resort not found; resort contact not found for the given `resort-id`/`id` pair; the contact type referenced by `contact_type_id` not found; the communication channel referenced by `communication_channel_id` not found |
| 409         | `CONFLICT`         | The combination of `(resort, contact_type, communication_channel, contact_value)` already exists among active records (`create`)                                                                                         |
