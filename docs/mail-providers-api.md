# Mail Providers API

Base URL: `/api/v1/mail-providers`

Mail Providers represent the SMTP-based sending backends available to the platform (e.g. Gmail, Replit Mail,
a custom SMTP server), each identified by a unique `code`. Because every mail provider speaks the same SMTP
protocol (unlike image hosting, where Cloudinary and S3 are genuinely different SDKs/APIs), there is no
per-provider send implementation anywhere in this module — a single `JavaMailSenderImpl`, built fresh from
whichever config values are supplied, is enough to send through any of them. Each provider defines its own
connection-configuration schema — the fields a caller must supply to configure it (`host`, `port`, `username`,
etc.) — through a companion sub-resource, Config Fields, reached via
`/api/v1/mail-providers/{mail-provider-id}/config-fields`. A config field describes a *field of the schema
itself* (its key, label, type, whether it's required), not an actual stored credential value. Actual
platform-level configured instances of a provider (e.g. "System Notifications", "Marketing") are managed
through a second sub-resource, Configs, reached via `/api/v1/mail-providers/{mail-provider-id}/configs` — see
[Configs](#configs) below. A **resort-scoped** config also exists as a separate resource — see
[resort-mail-configs-api.md](resort-mail-configs-api.md) — for when a resort needs its own mail credentials
rather than using a platform-level one. All records support soft-delete — deleted records are hidden from all
responses.

**None of these modules have a locale/translation concept** — `name`, `label`, `config`, and other text/JSON
fields are stored directly, once, in whatever the caller submits, never per-language. That said,
**`Accept-Language` is still required on every endpoint below, with no exceptions** — it's enforced globally
by `commons/filter/LocaleContextFilter.java`, before any endpoint in the application runs, regardless of
whether that endpoint's module has a locale concept of its own. Its value has **no effect** on the response
shape anywhere in this document — it's checked for presence only.

---

## Endpoints

| Method | Path                                                      | Description             |
|--------|-----------------------------------------------------------|-------------------------|
| POST   | `/api/v1/mail-providers`                                  | Create a provider       |
| GET    | `/api/v1/mail-providers`                                  | List / search providers |
| GET    | `/api/v1/mail-providers/{id}`                             | Get a provider          |
| PUT    | `/api/v1/mail-providers/{id}`                             | Update a provider       |
| DELETE | `/api/v1/mail-providers/{id}`                             | Delete a provider       |
| POST   | `/api/v1/mail-providers/{provider-id}/config-fields`      | Create a config field   |
| GET    | `/api/v1/mail-providers/{provider-id}/config-fields`      | List config fields      |
| PUT    | `/api/v1/mail-providers/{provider-id}/config-fields/{id}` | Update a config field   |
| DELETE | `/api/v1/mail-providers/{provider-id}/config-fields/{id}` | Delete a config field   |
| POST   | `/api/v1/mail-providers/{provider-id}/configs`            | Create a config         |
| GET    | `/api/v1/mail-providers/{provider-id}/configs`            | List / search configs   |
| PUT    | `/api/v1/mail-providers/{provider-id}/configs/{id}`       | Update a config         |
| DELETE | `/api/v1/mail-providers/{provider-id}/configs/{id}`       | Delete a config         |

---

## Data Model

### MailProvider

| Field         | Type    | Required | Constraints                                                           | Description                                                  |
|---------------|---------|----------|-----------------------------------------------------------------------|--------------------------------------------------------------|
| `id`          | Long    | —        | read-only                                                             | Auto-generated identifier                                    |
| `code`        | String  | Yes      | max 50 chars, unique among active records; set at creation, immutable | Short provider code (e.g., `GMAIL`, `REPLIT`, `CUSTOM_SMTP`) |
| `name`        | String  | Yes      | max 100 chars                                                         | Display name (e.g., `Gmail`)                                 |
| `description` | String  | Yes      | not null (defaults to `""`)                                           | Free-text description                                        |
| `sort_order`  | Integer | Yes      | default 0                                                             | Display order                                                |

> **Note:** `MailProvider` responses (both `GET /{id}` and `GET` list) never include the provider's config
> fields — there is no `config_fields` field on this DTO at all. To read a provider's
> connection-configuration schema, call [List Config Fields](#list-config-fields) separately.

### MailProviderConfigField

| Field           | Type    | Required | Constraints                                                                                                       | Description                                                     |
|-----------------|---------|----------|-------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------|
| `id`            | Long    | —        | read-only                                                                                                         | Auto-generated identifier                                       |
| `key`           | String  | Yes      | max 100 chars, unique among the provider's active fields; set at creation, immutable                              | Machine-readable field name (e.g., `host`, `port`, `password`)  |
| `label`         | String  | Yes      | max 100 chars                                                                                                     | Human-readable label (e.g., `SMTP Host`)                        |
| `field_type`    | String  | Yes      | max 30 chars, free text (convention: `TEXT`, `PASSWORD`, `NUMBER`, `BOOLEAN`, `URL` — not enforced by validation) | Input type hint for rendering the field                         |
| `placeholder`   | String  | Yes      | not null (defaults to `""`), max 255 chars                                                                        | Placeholder text for the input                                  |
| `default_value` | String  | Yes      | not null (defaults to `""`), max 500 chars                                                                        | Default value pre-filled for the field                          |
| `is_required`   | Boolean | Yes      | default `true`                                                                                                    | Whether a value must be supplied when configuring this provider |
| `sort_order`    | Integer | Yes      | default 0                                                                                                         | Display order among the provider's config fields                |

### MailProviderConfig

| Field    | Type          | Required | Constraints                                                                                                                     | Description                                                                                                                                                                                                                                         |
|----------|---------------|----------|---------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`     | Long          | —        | read-only                                                                                                                       | Auto-generated identifier                                                                                                                                                                                                                           |
| `name`   | String        | Yes      | max 100 chars; unique among the owning provider's active configs; set at creation                                               | Human-readable label for this configured instance (e.g., `System Notifications`)                                                                                                                                                                    |
| `code`   | String (enum) | No       | one of the known `MailProviderConfigCode` values (see below); unique among all active configs (regardless of provider) when set | Designates this config as the one a system flow sends through — e.g. `CREATE_USER_EMAIL_NOTIFICATIONS`/`PASSWORD_RESET_EMAIL_NOTIFICATIONS` are looked up by the POS booking and forgot-password flows respectively. Most configs leave this unset. |
| `config` | Object (JSON) | Yes      | not null; stored as `jsonb`, arbitrary shape                                                                                    | The actual configuration/credential values for this instance                                                                                                                                                                                        |

> **Note:** the response DTO never includes which provider a config belongs to — that's implicit in the
> `{provider-id}` path segment you called. See [Configs](#configs) below. Also note this is the
> **platform-level** config — it has no resort association. For a resort-scoped equivalent, see
> [resort-mail-configs-api.md](resort-mail-configs-api.md).
>
> **Known `code` values:** `CREATE_USER_EMAIL_NOTIFICATIONS` (looked up by the POS booking flow when it
> registers a new customer by email) and `PASSWORD_RESET_EMAIL_NOTIFICATIONS` (looked up by
> `POST /api/v1/auth/forgot-password` when delivering a reset code to an email username — see
> [password-reset-api.md](password-reset-api.md)). Submitting any other string for `code` returns
> `400 INVALID_ARGUMENT` — it's deserialized directly as a Java enum, so an unknown value fails before
> validation even runs.

---

## Create Provider

`POST /api/v1/mail-providers`

Creates a new mail provider together with its config fields — the connection-configuration schema must be
submitted **in this same request**; there is no separate "create empty provider, add fields later" path.
`code` must be unique among active, non-deleted providers — attempting to reuse an existing code returns
`409 CONFLICT`. `config_fields` must contain at least one entry, and no two entries in it may share the same
`key` — a duplicate `key` within the request returns `409 CONFLICT`. There is no locale/translation step —
`name` and `description` are submitted directly in this same request.

### Request Body

```json
{
  "code": "GMAIL",
  "name": "Gmail",
  "description": "",
  "sort_order": 1,
  "config_fields": [
    {
      "key": "host",
      "label": "SMTP Host",
      "field_type": "TEXT",
      "placeholder": "",
      "default_value": "smtp.gmail.com",
      "is_required": true,
      "sort_order": 1
    },
    {
      "key": "port",
      "label": "SMTP Port",
      "field_type": "NUMBER",
      "placeholder": "",
      "default_value": "587",
      "is_required": true,
      "sort_order": 2
    }
  ]
}
```

### Request Fields

| Field           | Type    | Required | Validation                                                                      |
|-----------------|---------|----------|---------------------------------------------------------------------------------|
| `code`          | String  | Yes      | Not blank, max 50 chars, unique among active records                            |
| `name`          | String  | Yes      | Not blank, max 100 chars                                                        |
| `description`   | String  | Yes      | Not null                                                                        |
| `sort_order`    | Integer | Yes      | Not null                                                                        |
| `config_fields` | Array   | Yes      | Not empty; no duplicate `key` within the list; each entry validated (see below) |

**Config field entry (`config_fields[]`):**

| Field           | Type    | Required | Validation               |
|-----------------|---------|----------|--------------------------|
| `key`           | String  | Yes      | Not blank, max 100 chars |
| `label`         | String  | Yes      | Not blank, max 100 chars |
| `field_type`    | String  | Yes      | Not blank, max 30 chars  |
| `placeholder`   | String  | Yes      | Not null, max 255 chars  |
| `default_value` | String  | Yes      | Not null, max 500 chars  |
| `is_required`   | Boolean | Yes      | Not null                 |
| `sort_order`    | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Provider

`GET /api/v1/mail-providers/{id}`

Returns a single active provider by its ID. Config fields are **not** included in this response — call
[List Config Fields](#list-config-fields) separately to read them.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the provider |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "GMAIL",
    "name": "Gmail",
    "description": "",
    "sort_order": 1
  }
}
```

---

## List / Search Providers

`GET /api/v1/mail-providers`

Returns a paginated, filterable list of active (non-deleted) providers. All filter parameters are optional;
omitting them returns all providers. Multiple filters are combined with AND. `code` and `name` both perform a
case-insensitive partial match. As with `GET /{id}`, rows never include config fields — call
[List Config Fields](#list-config-fields) per provider if you need them.

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely. Also note only `code` and `name` are sortable here.

### Query Parameters

> **Note:** Query parameters bind directly onto `MailProviderFilterRequest`'s Java field names, so they are
> **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter | Type   | Default         | Constraints                          | Description                                |
|-----------|--------|-----------------|--------------------------------------|--------------------------------------------|
| `code`    | String | —               | —                                    | Filter by code (partial, case-insensitive) |
| `name`    | String | —               | —                                    | Filter by name (partial, case-insensitive) |
| `page`    | int    | `0`             | >= 0                                 | Zero-based page index                      |
| `size`    | int    | `10`            | 1 – 50                               | Number of items per page                   |
| `sortBy`  | String | `id` (implicit) | `code`, `name` (`id` NOT selectable) | Field to sort by                           |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                        | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "GMAIL",
      "name": "Gmail",
      "description": "",
      "sort_order": 1
    },
    {
      "id": 2,
      "code": "REPLIT",
      "name": "Replit Mail",
      "description": "",
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

## Update Provider

`PUT /api/v1/mail-providers/{id}`

Updates `name`, `description`, and `sort_order`. `code` is set at creation and cannot be changed.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the provider |

### Request Body

```json
{
  "name": "Gmail",
  "description": "Primary transactional mail provider",
  "sort_order": 1
}
```

### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | Yes      | Not null                 |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Provider

`DELETE /api/v1/mail-providers/{id}`

Soft-deletes the provider. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the provider |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Config Fields

Config Field endpoints manage the connection-configuration schema for a provider — each entry describes one
field of that schema (e.g. "SMTP Host", "Password"), not an actual configured credential value. A provider's
initial config fields are submitted as part of [Create Provider](#create-provider) — the endpoints below are
for adding, changing, or removing fields on a provider that already exists. The `{provider-id}` path
parameter must reference an existing, active provider.

---

### Create Config Field

`POST /api/v1/mail-providers/{provider-id}/config-fields`

Adds a new config field to an existing provider. `key` must be unique among the provider's active config
fields — adding a key the provider already has returns `409 CONFLICT`, pre-checked at the application level
before any write (backed by a DB-level unique constraint on `(mail_provider_id, key)` as a last-resort
guard).

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `provider-id` | Long | ID of the parent provider |

#### Request Body

```json
{
  "key": "host",
  "label": "SMTP Host",
  "field_type": "TEXT",
  "placeholder": "smtp.gmail.com",
  "default_value": "",
  "is_required": true,
  "sort_order": 1
}
```

#### Request Fields

| Field           | Type    | Required | Validation               |
|-----------------|---------|----------|--------------------------|
| `key`           | String  | Yes      | Not blank, max 100 chars |
| `label`         | String  | Yes      | Not blank, max 100 chars |
| `field_type`    | String  | Yes      | Not blank, max 30 chars  |
| `placeholder`   | String  | Yes      | Not null, max 255 chars  |
| `default_value` | String  | Yes      | Not null, max 500 chars  |
| `is_required`   | Boolean | Yes      | Not null                 |
| `sort_order`    | Integer | Yes      | Not null                 |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 2
}
```

---

### List Config Fields

`GET /api/v1/mail-providers/{provider-id}/config-fields`

Returns every active config field belonging to the provider.

> **Note:** unlike every other list endpoint in this API set, this one is **not paginated** — the response
> body is a plain JSON array, with no `data` envelope and no `sortable_fields`/`searchable_fields`.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `provider-id` | Long | ID of the parent provider |

#### Response `200 OK`

```json
[
  {
    "id": 1,
    "key": "host",
    "label": "SMTP Host",
    "field_type": "TEXT",
    "placeholder": "",
    "default_value": "smtp.gmail.com",
    "is_required": true,
    "sort_order": 1
  },
  {
    "id": 2,
    "key": "port",
    "label": "SMTP Port",
    "field_type": "NUMBER",
    "placeholder": "",
    "default_value": "587",
    "is_required": true,
    "sort_order": 2
  }
]
```

---

### Update Config Field

`PUT /api/v1/mail-providers/{provider-id}/config-fields/{id}`

Updates `label`, `field_type`, `placeholder`, `default_value`, `is_required`, and `sort_order` for an
existing config field. The associated provider and `key` cannot be changed after creation.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `provider-id` | Long | ID of the parent provider |
| `id`          | Long | ID of the config field    |

#### Request Body

```json
{
  "label": "SMTP Host",
  "field_type": "TEXT",
  "placeholder": "smtp.gmail.com",
  "default_value": "",
  "is_required": true,
  "sort_order": 1
}
```

#### Request Fields

| Field           | Type    | Required | Validation               |
|-----------------|---------|----------|--------------------------|
| `label`         | String  | Yes      | Not blank, max 100 chars |
| `field_type`    | String  | Yes      | Not blank, max 30 chars  |
| `placeholder`   | String  | Yes      | Not null, max 255 chars  |
| `default_value` | String  | Yes      | Not null, max 500 chars  |
| `is_required`   | Boolean | Yes      | Not null                 |
| `sort_order`    | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

### Delete Config Field

`DELETE /api/v1/mail-providers/{provider-id}/config-fields/{id}`

Soft-deletes a config field. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `provider-id` | Long | ID of the parent provider |
| `id`          | Long | ID of the config field    |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 2
}
```

---

## Configs

Config endpoints manage actual **platform-level** configured instances of a provider — for example "System
Notifications" and "Marketing" could be two separate configs against the same `GMAIL` provider, each with its
own credentials/settings. Where a Config Field (above) describes one *field of the schema* (its key, label,
type), a Config stores the actual configured values for that schema as a single JSON payload. Every endpoint
below is nested under, and scoped to, a single provider — there is no top-level, cross-provider way to list or
address a config. The `{provider-id}` path parameter must reference an existing, active provider on every
endpoint below — an unknown value returns `404 ENTITY_NOT_FOUND`.

> A config created here has no resort association — it's intended for platform-wide sending (e.g. system
> account emails). If a resort needs its own dedicated mail credentials instead, use the separate,
> resort-scoped resource documented in [resort-mail-configs-api.md](resort-mail-configs-api.md).

---

### Create Config

`POST /api/v1/mail-providers/{provider-id}/configs`

Creates a new config under the given provider. `name` must be unique among that provider's active configs —
reusing a name already used by another active config **of the same provider** returns `409 CONFLICT`; the
same `name` is allowed across different providers. `code`, if supplied, must be unique among **all** active
configs regardless of provider — reusing a `code` already assigned to another active config returns
`409 CONFLICT`.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `provider-id` | Long | ID of the parent provider |

#### Request Body

```json
{
  "name": "System Notifications",
  "code": "CREATE_USER_EMAIL_NOTIFICATIONS",
  "config": {
    "host": "smtp.gmail.com",
    "port": 587,
    "username": "notifications@resort.com",
    "password": "app-password",
    "from_name": "Resort Notifications",
    "from_email": "notifications@resort.com",
    "use_tls": true
  }
}
```

#### Request Fields

| Field    | Type          | Required | Validation                                                                                                                            |
|----------|---------------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `name`   | String        | Yes      | Not blank, max 100 chars; unique among the provider's active configs                                                                  |
| `code`   | String (enum) | No       | Must be one of the known `MailProviderConfigCode` values (see [Data Model](#data-model)) if supplied; unique among all active configs |
| `config` | Object        | Yes      | Not null; arbitrary JSON object                                                                                                       |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

### List Configs

`GET /api/v1/mail-providers/{provider-id}/configs`

Returns a paginated, filterable list of the given provider's active (non-deleted) configs. All filter
parameters are optional; omitting them returns every config for that provider. `name` performs a
case-insensitive partial match.

> **Note:** unlike [List Config Fields](#list-config-fields), this list **is** paginated — the response
> follows the same `data`/`current_page`/`sortable_fields` shape as
> [List / Search Providers](#list--search-providers) above, not a plain array.

> **Note:** `id` is not a selectable `sortBy` value — passing `?sortBy=id` throws
> `400 INVALID_ARGUMENT: Invalid sort field: id`. It's used only as the implicit sort when `sortBy` is
> omitted entirely.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `provider-id` | Long | ID of the parent provider |

#### Query Parameters

> **Note:** Query parameters bind directly onto `MailProviderConfigFilterRequest`'s Java field names, so
> they are **camelCase** — not the snake_case used in JSON request/response bodies.

| Parameter | Type   | Default         | Constraints                  | Description                                |
|-----------|--------|-----------------|------------------------------|--------------------------------------------|
| `name`    | String | —               | —                            | Filter by name (partial, case-insensitive) |
| `page`    | int    | `0`             | >= 0                         | Zero-based page index                      |
| `size`    | int    | `10`            | 1 – 50                       | Number of items per page                   |
| `sortBy`  | String | `id` (implicit) | `name` (`id` NOT selectable) | Field to sort by                           |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                | Sort direction                             |

#### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "name": "System Notifications",
      "code": "CREATE_USER_EMAIL_NOTIFICATIONS",
      "config": {
        "host": "smtp.gmail.com",
        "port": 587,
        "username": "notifications@resort.com",
        "password": "app-password",
        "from_name": "Resort Notifications",
        "from_email": "notifications@resort.com",
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

### Update Config

`PUT /api/v1/mail-providers/{provider-id}/configs/{id}`

Updates `name`, `code`, and `config`. The config must belong to the provider named in the path — passing a
valid config `id` that belongs to a *different* provider returns `404 ENTITY_NOT_FOUND`, the same as an
unknown `id`. `name` must remain unique among the provider's active configs (excluding this record) — a
collision returns `409 CONFLICT`. `code`, if supplied, must remain unique among all active configs (excluding
this record) — a collision returns `409 CONFLICT`.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `provider-id` | Long | ID of the parent provider |
| `id`          | Long | ID of the config          |

#### Request Body

```json
{
  "name": "System Notifications",
  "code": "CREATE_USER_EMAIL_NOTIFICATIONS",
  "config": {
    "host": "smtp.gmail.com",
    "port": 587,
    "username": "notifications@resort.com",
    "password": "rotated-app-password",
    "from_name": "Resort Notifications",
    "from_email": "notifications@resort.com",
    "use_tls": true
  }
}
```

#### Request Fields

| Field    | Type          | Required | Validation                                                                                                                            |
|----------|---------------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `name`   | String        | Yes      | Not blank, max 100 chars; unique among the provider's active configs                                                                  |
| `code`   | String (enum) | No       | Must be one of the known `MailProviderConfigCode` values (see [Data Model](#data-model)) if supplied; unique among all active configs |
| `config` | Object        | Yes      | Not null; arbitrary JSON object                                                                                                       |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

### Delete Config

`DELETE /api/v1/mail-providers/{provider-id}/configs/{id}`

Soft-deletes the config. The config must belong to the provider named in the path — the same
provider-mismatch rule as Update Config applies. The record is not removed from the database but will no
longer appear in any response.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `provider-id` | Long | ID of the parent provider |
| `id`          | Long | ID of the config          |

#### Response `200 OK`

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
  "message": "MailProvider not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|-------------|----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; an unknown `code` value on `create`/`update` config (deserialization of `MailProviderConfigCode` fails before validation runs); or an unsupported `sortBy` query value on `GET /mail-providers` or `GET /configs`                                                                                                                                                                                                                            |
| 404         | `ENTITY_NOT_FOUND`         | Provider not found; config field not found; or config not found (unknown `id`, or an `id` that belongs to a different provider than the one in the path)                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| 409         | `CONFLICT`                 | `code` already in use by another active provider (`create` provider — note this is `MailProvider.code`, unrelated to `MailProviderConfig.code`); two entries in `config_fields` share the same `key` (`create` provider); the provider already has a config field for the given `key` (`create` config field, pre-checked at the application level); `name` already in use by another active config for the same provider (`create`/`update` config); or `code` already assigned to another active config, of any provider (`create`/`update` config, pre-checked at the application level) |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB-level unique constraint on `(mail_provider_id, key)` for config fields, or on `code` for configs (partial index, only enforced when `code` is set), should not normally be reachable now that both duplicates are pre-checked at the application level. **Config `name` has no equivalent DB-level constraint** — the migration for `mail_provider_configs` defines no `unique` constraint on `(mail_provider_id, name)`, so that `409 CONFLICT` above is enforced purely at the application level                                                                           |
