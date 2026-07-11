# Resort Contacts API

Base URL: `/api/v1/resorts/{resort-id}/contacts`

Resort contacts represent the individual contact entries for a resort — the specific values (phone numbers, email
addresses, URLs, etc.) through which guests can reach the resort for a given purpose and channel. Each contact entry
links a resort to a contact type (the purpose, e.g. `RESERVATION`) and a communication channel (the medium, e.g.
`PHONE`), and stores the actual contact value (e.g. `+8801712345678`).

Every response embeds the full `contact_type` and `communication_channel` objects, including their locale
translations, so no additional lookups are needed on the client side.

All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                        | Description                   |
|--------|---------------------------------------------|-------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/contacts`      | Create a resort contact       |
| GET    | `/api/v1/resorts/{resort-id}/contacts`      | List / filter resort contacts |
| GET    | `/api/v1/resorts/{resort-id}/contacts/{id}` | Get a resort contact          |
| PUT    | `/api/v1/resorts/{resort-id}/contacts/{id}` | Update a resort contact       |
| DELETE | `/api/v1/resorts/{resort-id}/contacts/{id}` | Delete a resort contact       |

---

## Data Model

### Resort Contact

| Field                   | Type    | Required | Constraints               | Description                                                                                      |
|-------------------------|---------|----------|---------------------------|--------------------------------------------------------------------------------------------------|
| `id`                    | Long    | —        | read-only                 | Auto-generated identifier                                                                        |
| `resort_id`             | Long    | —        | read-only                 | ID of the owning resort                                                                          |
| `contact_type`          | Object  | —        | read-only                 | Full contact type object (id, code, sort_order, locales); set on create, not updatable           |
| `communication_channel` | Object  | —        | read-only                 | Full communication channel object (id, code, flags, locales); set on create, not updatable       |
| `contact_value`         | String  | Yes      | not blank, unlimited      | The actual contact value — a phone number, email address, URL, username, etc.                    |
| `is_primary`            | Boolean | Yes      | not null, default `false` | `true` if this is the preferred contact for the same resort + contact type + channel combination |
| `sort_order`            | Integer | Yes      | >= 0, default `0`         | Display order                                                                                    |

### Embedded: Contact Type

The `contact_type` object embedded in every response is the full contact type as returned by the Contact Types API.

| Field        | Type    | Description                                                                  |
|--------------|---------|------------------------------------------------------------------------------|
| `id`         | Long    | Contact type identifier                                                      |
| `code`       | String  | Machine-readable code (e.g. `RESERVATION`)                                   |
| `sort_order` | Integer | Display order of the contact type                                            |
| `locales`    | Array   | Locale translations (`id`, `locale_id`, `name`, `description`, `sort_order`) |

### Embedded: Communication Channel

The `communication_channel` object embedded in every response is the full communication channel as returned by the
Communication Channels API.

| Field          | Type    | Description                                                                  |
|----------------|---------|------------------------------------------------------------------------------|
| `id`           | Long    | Communication channel identifier                                             |
| `code`         | String  | Machine-readable code (e.g. `PHONE`, `EMAIL`)                                |
| `sort_order`   | Integer | Display order of the channel                                                 |
| `is_url`       | Boolean | `true` if the contact value is expected to be a URL                          |
| `is_phone`     | Boolean | `true` if the contact value is expected to be a phone number                 |
| `is_email`     | Boolean | `true` if the contact value is expected to be an email address               |
| `is_clickable` | Boolean | `true` if the value should be rendered as a clickable link                   |
| `locales`      | Array   | Locale translations (`id`, `locale_id`, `name`, `description`, `sort_order`) |

---

## Uniqueness Constraints

Two database constraints enforce data integrity:

1. **Duplicate contact** — The combination of `(resort_id, contact_type_id, communication_channel_id, contact_value)`
   is unique. You cannot register the same phone number, email, or URL for the same purpose and channel twice on the
   same resort. Returns `409 DATA_INTEGRITY_VIOLATION`.

2. **Duplicate primary** — Only one contact entry may be marked `is_primary = true` for a given
   `(resort_id, contact_type_id, communication_channel_id)` combination. If you set `is_primary: true` and a primary
   already exists for that combination, the database will reject it. Returns `409 DATA_INTEGRITY_VIOLATION`.

---

## Create Resort Contact

`POST /api/v1/resorts/{resort-id}/contacts`

Creates a new contact entry for the resort. The `contact_type_id` and `communication_channel_id` are resolved at
creation time and embedded in the response. They cannot be changed after creation. To change the contact type or
channel, delete the entry and create a new one.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Request Fields

| Field                      | Type    | Required | Validation           |
|----------------------------|---------|----------|----------------------|
| `contact_type_id`          | Long    | Yes      | Not null, must exist |
| `communication_channel_id` | Long    | Yes      | Not null, must exist |
| `contact_value`            | String  | Yes      | Not blank            |
| `is_primary`               | Boolean | Yes      | Not null             |
| `sort_order`               | Integer | Yes      | Not null, >= 0       |

### Request Body

```json
{
  "contact_type_id": 2,
  "communication_channel_id": 1,
  "contact_value": "+8801712345678",
  "is_primary": true,
  "sort_order": 1
}
```

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

Returns a single active (non-deleted) contact entry belonging to the specified resort, with the full embedded
`contact_type` and `communication_channel` objects.

### Path Parameters

| Parameter   | Type | Description       |
|-------------|------|-------------------|
| `resort-id` | Long | ID of the resort  |
| `id`        | Long | ID of the contact |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "resort_id": 10,
    "contact_type": {
      "id": 2,
      "code": "RESERVATION",
      "sort_order": 2,
      "locales": [
        {
          "id": 2,
          "locale_id": 1,
          "name": "Reservation",
          "description": "Contact for room bookings, availability checks, and reservation changes.",
          "sort_order": 2
        }
      ]
    },
    "communication_channel": {
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
    "contact_value": "+8801712345678",
    "is_primary": true,
    "sort_order": 1
  }
}
```

---

## List / Filter Resort Contacts

`GET /api/v1/resorts/{resort-id}/contacts`

Returns a paginated, filterable list of active (non-deleted) contacts for the specified resort. All filter parameters
are optional; omitting them returns all contacts for the resort. Multiple filters are combined with AND. The
`contact_value` filter performs a case-insensitive partial match; the remaining filters perform exact matches.

If the resort does not exist or is deleted, a `404` is returned before executing the query.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Query Parameters

| Parameter                | Type    | Default | Constraints                                    | Description                                         |
|--------------------------|---------|---------|------------------------------------------------|-----------------------------------------------------|
| `contactValue`           | String  | —       | —                                              | Filter by contact value (partial, case-insensitive) |
| `contactTypeId`          | Long    | —       | —                                              | Filter by contact type ID (exact match)             |
| `communicationChannelId` | Long    | —       | —                                              | Filter by communication channel ID (exact match)    |
| `isPrimary`              | Boolean | —       | `true` or `false`                              | Filter by primary flag (exact match)                |
| `page`                   | int     | `0`     | >= 0                                           | Zero-based page index                               |
| `size`                   | int     | `10`    | 1 – 50                                         | Number of items per page                            |
| `sort_by`                | String  | `id`    | `id`, `sortOrder`, `contactValue`, `createdAt` | Field to sort by                                    |
| `sort_dir`               | String  | `ASC`   | `ASC`, `DESC`                                  | Sort direction                                      |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort_id": 10,
      "contact_type": {
        "id": 2,
        "code": "RESERVATION",
        "sort_order": 2,
        "locales": [
          {
            "id": 2,
            "locale_id": 1,
            "name": "Reservation",
            "description": "Contact for room bookings, availability checks, and reservation changes.",
            "sort_order": 2
          }
        ]
      },
      "communication_channel": {
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
      "contact_value": "+8801712345678",
      "is_primary": true,
      "sort_order": 1
    },
    {
      "id": 2,
      "resort_id": 10,
      "contact_type": {
        "id": 1,
        "code": "GENERAL",
        "sort_order": 1,
        "locales": [
          {
            "id": 1,
            "locale_id": 1,
            "name": "General",
            "description": "Main contact for general enquiries about the resort.",
            "sort_order": 1
          }
        ]
      },
      "communication_channel": {
        "id": 4,
        "code": "EMAIL",
        "sort_order": 4,
        "is_url": false,
        "is_phone": false,
        "is_email": true,
        "is_clickable": true,
        "locales": [
          {
            "id": 4,
            "locale_id": 1,
            "name": "Email",
            "description": "Email address for written correspondence.",
            "sort_order": 4
          }
        ]
      },
      "contact_value": "info@myresort.com",
      "is_primary": true,
      "sort_order": 2
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 2,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Resort Contact

`PUT /api/v1/resorts/{resort-id}/contacts/{id}`

Updates `contact_value`, `is_primary`, and `sort_order`. The `contact_type` and `communication_channel` are set at
creation time and cannot be changed. To change the contact type or channel, delete the entry and create a new one.

> If `is_primary: true` is submitted and another contact already holds the primary flag for the same
> `(resort_id, contact_type_id, communication_channel_id)` combination, the request will be rejected with
> `409 DATA_INTEGRITY_VIOLATION`.

### Path Parameters

| Parameter   | Type | Description       |
|-------------|------|-------------------|
| `resort-id` | Long | ID of the resort  |
| `id`        | Long | ID of the contact |

### Request Fields

| Field           | Type    | Required | Validation     |
|-----------------|---------|----------|----------------|
| `contact_value` | String  | Yes      | Not blank      |
| `is_primary`    | Boolean | Yes      | Not null       |
| `sort_order`    | Integer | Yes      | Not null, >= 0 |

### Request Body

```json
{
  "contact_value": "+8801799999999",
  "is_primary": true,
  "sort_order": 1
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

## Delete Resort Contact

`DELETE /api/v1/resorts/{resort-id}/contacts/{id}`

Soft-deletes the resort contact. The record is not removed from the database but will no longer appear in any
response. Once a primary contact is deleted, no other contact automatically becomes primary — a new primary must be
explicitly designated via an update.

### Path Parameters

| Parameter   | Type | Description       |
|-------------|------|-------------------|
| `resort-id` | Long | ID of the resort  |
| `id`        | Long | ID of the contact |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Usage Examples

### Recording all contacts for a resort

A typical resort might have entries like the following across different contact types and channels:

| Contact Type  | Channel    | contact_value               | is_primary |
|---------------|------------|-----------------------------|------------|
| `GENERAL`     | `PHONE`    | `+8801712345678`            | true       |
| `GENERAL`     | `EMAIL`    | `info@myresort.com`         | true       |
| `GENERAL`     | `WEBSITE`  | `https://www.myresort.com`  | true       |
| `RESERVATION` | `PHONE`    | `+8801787654321`            | true       |
| `RESERVATION` | `WHATSAPP` | `+8801787654321`            | true       |
| `RESERVATION` | `EMAIL`    | `reservations@myresort.com` | true       |
| `EMERGENCY`   | `PHONE`    | `+8801700000000`            | true       |

### Fetching only primary contacts

```
GET /api/v1/resorts/10/contacts?isPrimary=true
```

### Fetching all phone numbers for a resort

```
GET /api/v1/resorts/10/contacts?communicationChannelId=1
```

### Fetching all reservation contacts

```
GET /api/v1/resorts/10/contacts?contactTypeId=2
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

| HTTP Status | Error Code                 | Cause                                                                                                                           |
|-------------|----------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields, invalid sort field, or `sort_order` less than 0                                                        |
| 404         | `ENTITY_NOT_FOUND`         | Resort, contact type, communication channel, or contact not found, or already deleted                                           |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `(resort_id, contact_type_id, communication_channel_id, contact_value)`, or a second primary for the same combination |
