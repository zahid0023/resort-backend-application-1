# Communication Channels API

Base URL: `/api/v1/communication-channels`

Communication channels represent the mediums through which a resort can be contacted (e.g. Phone, Email, WhatsApp,
Instagram). They are master data pre-seeded with 12 channels and can be extended by administrators. Names and
descriptions are locale-specific and embedded in every response via the `locales` array. Boolean flags (`is_phone`,
`is_email`, `is_url`, `is_clickable`) allow the UI to render and validate contact values appropriately. All records
support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                       | Description                          |
|--------|------------------------------------------------------------|--------------------------------------|
| POST   | `/api/v1/communication-channels`                           | Create a communication channel       |
| GET    | `/api/v1/communication-channels`                           | List / search communication channels |
| GET    | `/api/v1/communication-channels/{id}`                      | Get a communication channel          |
| PUT    | `/api/v1/communication-channels/{id}`                      | Update a communication channel       |
| DELETE | `/api/v1/communication-channels/{id}`                      | Delete a communication channel       |
| POST   | `/api/v1/communication-channels/{channel-id}/locales`      | Create a channel locale              |
| PUT    | `/api/v1/communication-channels/{channel-id}/locales/{id}` | Update a channel locale              |
| DELETE | `/api/v1/communication-channels/{channel-id}/locales/{id}` | Delete a channel locale              |

---

## Data Model

### Communication Channel

| Field          | Type    | Required | Constraints              | Description                                                                 |
|----------------|---------|----------|--------------------------|-----------------------------------------------------------------------------|
| `id`           | Long    | —        | read-only                | Auto-generated identifier                                                   |
| `code`         | String  | Yes      | max 50 chars, unique     | Machine-readable code (e.g. `PHONE`, `EMAIL`); set on create, not updatable |
| `sort_order`   | Integer | Yes      | >= 0, default `0`        | Display order                                                               |
| `is_url`       | Boolean | Yes      | not null                 | `true` if the contact value is a URL (e.g. website, social media profile)   |
| `is_phone`     | Boolean | Yes      | not null                 | `true` if the contact value is a phone number (e.g. PHONE, MOBILE, FAX)     |
| `is_email`     | Boolean | Yes      | not null                 | `true` if the contact value is an email address                             |
| `is_clickable` | Boolean | Yes      | not null, default `true` | `true` if the value should be rendered as a clickable link in the UI        |
| `locales`      | Array   | —        | read-only                | All active locale translations for this channel                             |

### Communication Channel Locale

| Field         | Type    | Required | Constraints           | Description                                                        |
|---------------|---------|----------|-----------------------|--------------------------------------------------------------------|
| `id`          | Long    | —        | read-only             | Auto-generated identifier                                          |
| `locale_id`   | Long    | Yes      | must exist            | ID of an existing active locale; set on create only, not updatable |
| `name`        | String  | Yes      | max 100 chars         | Localized display name of the channel                              |
| `description` | String  | No       | unlimited             | Localized description; omitted from response if null               |
| `sort_order`  | Integer | Yes      | not null, default `0` | Display order for this locale entry                                |

> Each locale may only be added once per channel. The pair `(channel_id, locale_id)` is unique — attempting to
> add the same locale twice will return `409 DATA_INTEGRITY_VIOLATION`.

---

## Pre-seeded Communication Channels

The following 12 channels are seeded automatically (English locale only). Additional locale translations can be
added via the locale endpoints after seeding.

| Code        | is_phone | is_email | is_url | is_clickable | English Name | English Description                              |
|-------------|----------|----------|--------|--------------|--------------|--------------------------------------------------|
| `PHONE`     | true     | false    | false  | true         | Phone        | Fixed-line telephone number for direct calls.    |
| `MOBILE`    | true     | false    | false  | true         | Mobile       | Mobile phone number for calls and SMS.           |
| `WHATSAPP`  | true     | false    | false  | true         | WhatsApp     | WhatsApp number for instant messaging and calls. |
| `EMAIL`     | false    | true     | false  | true         | Email        | Email address for written correspondence.        |
| `WEBSITE`   | false    | false    | true   | true         | Website      | Official website URL of the resort.              |
| `FACEBOOK`  | false    | false    | true   | true         | Facebook     | Facebook page or profile URL.                    |
| `INSTAGRAM` | false    | false    | true   | true         | Instagram    | Instagram profile URL.                           |
| `X`         | false    | false    | true   | true         | X (Twitter)  | X (formerly Twitter) profile URL.                |
| `LINKEDIN`  | false    | false    | true   | true         | LinkedIn     | LinkedIn company or profile URL.                 |
| `TELEGRAM`  | false    | false    | false  | true         | Telegram     | Telegram username or group for messaging.        |
| `WECHAT`    | false    | false    | false  | false        | WeChat       | WeChat ID for messaging.                         |
| `FAX`       | true     | false    | false  | false        | Fax          | Fax number for document transmission.            |

---

## Create Communication Channel

`POST /api/v1/communication-channels`

Creates a communication channel along with its locale-specific translations in one request. All provided `locale_id`
values must reference existing, active locales. The `code` value must be unique across all channels.

### Request Fields

| Field          | Type    | Required | Validation              |
|----------------|---------|----------|-------------------------|
| `code`         | String  | Yes      | Not blank, max 50 chars |
| `sort_order`   | Integer | Yes      | Not null, >= 0          |
| `is_url`       | Boolean | Yes      | Not null                |
| `is_phone`     | Boolean | Yes      | Not null                |
| `is_email`     | Boolean | Yes      | Not null                |
| `is_clickable` | Boolean | Yes      | Not null                |
| `locales`      | Array   | No       | See locale fields below |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Request Body

```json
{
  "code": "VIBER",
  "sort_order": 13,
  "is_url": false,
  "is_phone": true,
  "is_email": false,
  "is_clickable": true,
  "locales": [
    {
      "locale_id": 1,
      "name": "Viber",
      "description": "Viber number for messaging and calls.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "ভাইবার",
      "sort_order": 2
    }
  ]
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

## Get Communication Channel

`GET /api/v1/communication-channels/{id}`

Returns a single active (non-deleted) communication channel with all its active locale translations.

### Path Parameters

| Parameter | Type | Description                     |
|-----------|------|---------------------------------|
| `id`      | Long | ID of the communication channel |

### Response `200 OK`

Optional fields (locale `description`) are omitted from the response when not set.

```json
{
  "data": {
    "id": 1,
    "code": "PHONE",
    "sort_order": 1,
    "is_url": false,
    "is_phone": true,
    "is_email": false,
    "is_clickable": true,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Phone",
        "description": "Fixed-line telephone number for direct calls.",
        "sort_order": 1
      },
      {
        "id": 2,
        "locale_id": 2,
        "name": "ফোন",
        "sort_order": 2
      }
    ]
  }
}
```

---

## List / Search Communication Channels

`GET /api/v1/communication-channels`

Returns a paginated, filterable list of active (non-deleted) communication channels. Each item includes all active
locale translations. All filter parameters are optional; omitting them returns all channels. The `code` filter
performs a case-insensitive partial match.

### Query Parameters

| Parameter  | Type   | Default | Constraints                            | Description                                |
|------------|--------|---------|----------------------------------------|--------------------------------------------|
| `code`     | String | —       | —                                      | Filter by code (partial, case-insensitive) |
| `page`     | int    | `0`     | >= 0                                   | Zero-based page index                      |
| `size`     | int    | `10`    | 1 – 50                                 | Number of items per page                   |
| `sort_by`  | String | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by                           |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                          | Sort direction                             |

### Response `200 OK`

Optional fields (locale `description`) are omitted when not set.

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
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Phone",
          "description": "Fixed-line telephone number for direct calls.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "code": "MOBILE",
      "sort_order": 2,
      "is_url": false,
      "is_phone": true,
      "is_email": false,
      "is_clickable": true,
      "locales": [
        {
          "id": 2,
          "locale_id": 1,
          "name": "Mobile",
          "description": "Mobile phone number for calls and SMS.",
          "sort_order": 1
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 2,
  "total_elements": 12,
  "page_size": 10,
  "has_next": true,
  "has_previous": false
}
```

---

## Update Communication Channel

`PUT /api/v1/communication-channels/{id}`

Updates `sort_order`, `is_url`, `is_phone`, `is_email`, and `is_clickable`. The `code` field is set at creation time
and cannot be changed. Locale translations are managed via the channel locale endpoints.

### Path Parameters

| Parameter | Type | Description                     |
|-----------|------|---------------------------------|
| `id`      | Long | ID of the communication channel |

### Request Fields

| Field          | Type    | Required | Validation     |
|----------------|---------|----------|----------------|
| `sort_order`   | Integer | Yes      | Not null, >= 0 |
| `is_url`       | Boolean | Yes      | Not null       |
| `is_phone`     | Boolean | Yes      | Not null       |
| `is_email`     | Boolean | Yes      | Not null       |
| `is_clickable` | Boolean | Yes      | Not null       |

### Request Body

```json
{
  "sort_order": 1,
  "is_url": false,
  "is_phone": true,
  "is_email": false,
  "is_clickable": true
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Communication Channel

`DELETE /api/v1/communication-channels/{id}`

Soft-deletes the communication channel. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter | Type | Description                     |
|-----------|------|---------------------------------|
| `id`      | Long | ID of the communication channel |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Communication Channel Locales

Channel locale endpoints manage per-locale translations for a communication channel. The `{channel-id}` path parameter
must reference an existing, active communication channel. All write operations on locale endpoints require the `ADMIN`
role.

---

### Create Channel Locale

`POST /api/v1/communication-channels/{channel-id}/locales`

Adds a new locale translation to an existing communication channel. The `locale_id` must not already be registered
for this channel. Requires `ADMIN` role.

#### Path Parameters

| Parameter    | Type | Description                     |
|--------------|------|---------------------------------|
| `channel-id` | Long | ID of the communication channel |

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "ফোন",
  "description": "সরাসরি কলের জন্য ফিক্সড-লাইন টেলিফোন নম্বর।",
  "sort_order": 2
}
```

#### Response `201 Created`

```json
{
  "success": true,
  "id": 25
}
```

---

### Update Channel Locale

`PUT /api/v1/communication-channels/{channel-id}/locales/{id}`

Updates an existing locale translation for a communication channel. The `locale_id` is set on creation and cannot be
changed. Requires `ADMIN` role.

#### Path Parameters

| Parameter    | Type | Description                     |
|--------------|------|---------------------------------|
| `channel-id` | Long | ID of the communication channel |
| `id`         | Long | ID of the channel locale        |

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Request Body

```json
{
  "name": "Phone",
  "description": "Updated description for the phone channel.",
  "sort_order": 1
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

### Delete Channel Locale

`DELETE /api/v1/communication-channels/{channel-id}/locales/{id}`

Soft-deletes a channel locale. The record is not removed from the database but will no longer appear in any response.
Requires `ADMIN` role.

#### Path Parameters

| Parameter    | Type | Description                     |
|--------------|------|---------------------------------|
| `channel-id` | Long | ID of the communication channel |
| `id`         | Long | ID of the channel locale        |

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
  "message": "CommunicationChannel not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                             |
|-------------|----------------------------|-----------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields, invalid sort field, or `sort_order` less than 0          |
| 403         | `FORBIDDEN`                | Locale write operations attempted without `ADMIN` role                            |
| 404         | `ENTITY_NOT_FOUND`         | Channel or locale not found, or already deleted                                   |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `code` on channel create, or duplicate `locale_id` for the same channel |
