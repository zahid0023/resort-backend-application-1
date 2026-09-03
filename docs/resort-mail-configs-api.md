# Resort Mail Configs API

Base URL: `/api/v1/resorts/{resort-id}/mail-configs`

A resort mail config is a resort's own set of mail-sending credentials, plugged into one of the platform's
[mail providers](mail-providers-api.md) (e.g. `GMAIL`, `REPLIT`, `CUSTOM_SMTP`). Where a platform-level
[Mail Provider Config](mail-providers-api.md#configs) is shared across the whole platform (e.g. for system
notification emails), a resort mail config is scoped to exactly one resort — e.g. so "Sunrise Resort" can
send booking confirmations from its own inbox while "Palm Beach Resort" sends from a different one, both
potentially through the same provider. Resort mail configs are always reached nested under their owning
resort; there is no top-level `/api/v1/resort-mail-configs` route. Resort mail configs have no locale
sub-resource — `name` and `config` are not translatable. All records support soft-delete — deleted records
are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). Its value never shapes any response on this resource — `ResortMailConfig`
has no locale-specific fields of its own, though its embedded `resort` and `mail_provider` objects each carry
their own locale-shaped fields per their own resources ([resorts-api.md](resorts-api.md) /
[mail-providers-api.md](mail-providers-api.md)).

---

## Endpoints

| Method | Path                                              | Description                          |
|--------|-----------------------------------------------------|-----------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/mail-configs`          | Create a resort mail config             |
| GET    | `/api/v1/resorts/{resort-id}/mail-configs`          | List / search a resort's mail configs   |
| GET    | `/api/v1/resorts/{resort-id}/mail-configs/{id}`     | Get a resort mail config                |
| PUT    | `/api/v1/resorts/{resort-id}/mail-configs/{id}`     | Update a resort mail config             |
| DELETE | `/api/v1/resorts/{resort-id}/mail-configs/{id}`     | Delete a resort mail config             |

---

## Data Model

### ResortMailConfig

| Field           | Type          | Required | Constraints                                                                        | Description                                                                 |
|-----------------|---------------|----------|---------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `id`            | Long          | —        | read-only                                                                              | Auto-generated identifier                                                       |
| `resort`        | Resort        | —        | see [resorts-api.md](resorts-api.md); set at creation from the `{resort-id}` path segment, immutable | The owning resort                                                               |
| `mail_provider` | MailProvider  | —        | see [mail-providers-api.md](mail-providers-api.md); set at creation via `mail_provider_id`, immutable | The mail provider this config sends through                                    |
| `name`          | String        | Yes      | max 100 chars; unique among the resort's active mail configs; set at creation         | Human-readable label for this config (e.g., `Bookings Inbox`, `Support Inbox`) |
| `config`        | Object (JSON) | Yes      | not null; stored as `jsonb`, arbitrary shape                                           | The actual configuration/credential values for this instance                    |

> **Note:** `mail_provider_id` is a create-time-only field — it is not part of the update payload, and there
> is no way to move an existing config to a different provider afterward. Delete and recreate instead.

---

## Create Resort Mail Config

`POST /api/v1/resorts/{resort-id}/mail-configs`

Creates a new mail config for the given resort against the given mail provider. `mail_provider_id` must
reference an existing, active mail provider — an unknown value returns `404 ENTITY_NOT_FOUND`. `name` must be
unique among the resort's active mail configs — reusing a name already used by another active config **of
the same resort** returns `409 CONFLICT`; the same `name` is allowed across different resorts.

### Path Parameters

| Parameter   | Type | Description       |
|-------------|------|--------------------|
| `resort-id` | Long | ID of the resort   |

### Request Body

```json
{
  "mail_provider_id": 1,
  "name": "Bookings Inbox",
  "config": {
    "host": "smtp.gmail.com",
    "port": 587,
    "username": "bookings@sunriseresort.com",
    "password": "app-password",
    "from_name": "Sunrise Resort Bookings",
    "from_email": "bookings@sunriseresort.com",
    "use_tls": true
  }
}
```

### Request Fields

| Field              | Type   | Required | Validation                                                          |
|---------------------|--------|----------|------------------------------------------------------------------------|
| `mail_provider_id`  | Long   | Yes      | Not null; must reference an existing, active mail provider            |
| `name`              | String | Yes      | Not blank, max 100 chars; unique among the resort's active mail configs |
| `config`            | Object | Yes      | Not null; arbitrary JSON object                                        |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Resort Mail Config

`GET /api/v1/resorts/{resort-id}/mail-configs/{id}`

Returns a single active mail config by its ID. The config must belong to the resort named in the path —
passing a valid config `id` that belongs to a *different* resort returns `404 ENTITY_NOT_FOUND`, the same as
an unknown `id`.

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|---------------------------|
| `resort-id` | Long | ID of the resort          |
| `id`        | Long | ID of the mail config     |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "resort": {
      "id": 10,
      "code": "SUNRISE",
      "name": "Sunrise Resort"
    },
    "mail_provider": {
      "id": 1,
      "code": "GMAIL",
      "name": "Gmail",
      "description": "",
      "sort_order": 1
    },
    "name": "Bookings Inbox",
    "config": {
      "host": "smtp.gmail.com",
      "port": 587,
      "username": "bookings@sunriseresort.com",
      "password": "app-password",
      "from_name": "Sunrise Resort Bookings",
      "from_email": "bookings@sunriseresort.com",
      "use_tls": true
    }
  }
}
```

> **Note:** the embedded `resort` object above is abbreviated for readability — see
> [resorts-api.md](resorts-api.md) for its actual full shape.

---

## List / Search Resort Mail Configs

`GET /api/v1/resorts/{resort-id}/mail-configs`

Returns a paginated, filterable list of the given resort's active (non-deleted) mail configs. All filter
parameters are optional; omitting them returns every mail config for that resort. `name` performs a
case-insensitive partial match.

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

### Path Parameters

| Parameter   | Type | Description       |
|-------------|------|--------------------|
| `resort-id` | Long | ID of the resort   |

### Query Parameters

> **Note:** Query parameters bind directly onto `ResortMailConfigFilterRequest`'s Java field names, so they
> are **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter        | Type   | Default         | Constraints                  | Description                                |
|-------------------|--------|-----------------|--------------------------------|-----------------------------------------------|
| `name`            | String | —               | —                              | Filter by name (partial, case-insensitive)   |
| `mailProviderId`  | Long   | —               | —                              | Filter by mail provider ID                    |
| `page`            | int    | `0`             | >= 0                           | Zero-based page index                         |
| `size`            | int    | `10`            | 1 – 50                         | Number of items per page                      |
| `sortBy`          | String | `id` (implicit) | `name` (`id` NOT selectable)   | Field to sort by                              |
| `sortDir`         | String | `ASC`           | `ASC`, `DESC`                  | Sort direction                                |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort": {
        "id": 10,
        "code": "SUNRISE",
        "name": "Sunrise Resort"
      },
      "mail_provider": {
        "id": 1,
        "code": "GMAIL",
        "name": "Gmail",
        "description": "",
        "sort_order": 1
      },
      "name": "Bookings Inbox",
      "config": {
        "host": "smtp.gmail.com",
        "port": 587,
        "username": "bookings@sunriseresort.com",
        "password": "app-password",
        "from_name": "Sunrise Resort Bookings",
        "from_email": "bookings@sunriseresort.com",
        "use_tls": true
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [
    "name",
    "mailProviderEntity.id"
  ],
  "searchable_fields": [
    "name"
  ]
}
```

---

## Update Resort Mail Config

`PUT /api/v1/resorts/{resort-id}/mail-configs/{id}`

Updates `name` and `config`. The mail provider association (`mail_provider_id`) is set at creation and cannot
be changed. `name` must remain unique among the resort's active mail configs (excluding this record) — a
collision returns `409 CONFLICT`.

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|---------------------------|
| `resort-id` | Long | ID of the resort          |
| `id`        | Long | ID of the mail config     |

### Request Body

```json
{
  "name": "Bookings Inbox",
  "config": {
    "host": "smtp.gmail.com",
    "port": 587,
    "username": "bookings@sunriseresort.com",
    "password": "rotated-app-password",
    "from_name": "Sunrise Resort Bookings",
    "from_email": "bookings@sunriseresort.com",
    "use_tls": true
  }
}
```

### Request Fields

| Field    | Type   | Required | Validation                                                              |
|----------|--------|----------|-----------------------------------------------------------------------------|
| `name`   | String | Yes      | Not blank, max 100 chars; unique among the resort's active mail configs   |
| `config` | Object | Yes      | Not null; arbitrary JSON object                                            |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Resort Mail Config

`DELETE /api/v1/resorts/{resort-id}/mail-configs/{id}`

Soft-deletes the mail config. The config must belong to the resort named in the path — the same
resort-mismatch rule as [Get Resort Mail Config](#get-resort-mail-config) applies. The record is not removed
from the database but will no longer appear in any response.

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|---------------------------|
| `resort-id` | Long | ID of the resort          |
| `id`        | Long | ID of the mail config     |

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
  "message": "ResortMailConfig not found with id: 99"
}
```

| HTTP Status | Error Code          | Cause                                                                                                                                                                       |
|-------------|-----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`    | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; or an unsupported `sortBy` query value |
| 404         | `ENTITY_NOT_FOUND`    | Resort not found; mail provider not found (`mail_provider_id` on create); or mail config not found (unknown `id`, or an `id` that belongs to a different resort than the one in the path) |
| 409         | `CONFLICT`            | `name` already in use by another active mail config for the same resort (`create`/`update`)                                                                                |
