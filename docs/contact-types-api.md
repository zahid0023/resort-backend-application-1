# Contact Types API

Base URL: `/api/v1/contact-types`

Contact types represent the categories of resort contacts (e.g. General, Reservation, Sales). They are master data
pre-seeded with 6 types and can be extended by administrators. Names and descriptions are locale-specific and are
embedded in every response via the `locales` array. All records support soft-delete — deleted records are hidden from
all responses.

---

## Endpoints

| Method | Path                                                   | Description                  |
|--------|--------------------------------------------------------|------------------------------|
| POST   | `/api/v1/contact-types`                                | Create a contact type        |
| GET    | `/api/v1/contact-types`                                | List / search contact types  |
| GET    | `/api/v1/contact-types/{id}`                           | Get a contact type           |
| PUT    | `/api/v1/contact-types/{id}`                           | Update a contact type        |
| DELETE | `/api/v1/contact-types/{id}`                           | Delete a contact type        |
| POST   | `/api/v1/contact-types/{contact-type-id}/locales`      | Create a contact type locale |
| PUT    | `/api/v1/contact-types/{contact-type-id}/locales/{id}` | Update a contact type locale |
| DELETE | `/api/v1/contact-types/{contact-type-id}/locales/{id}` | Delete a contact type locale |

---

## Data Model

### Contact Type

| Field        | Type    | Required | Constraints          | Description                                                          |
|--------------|---------|----------|----------------------|----------------------------------------------------------------------|
| `id`         | Long    | —        | read-only            | Auto-generated identifier                                            |
| `code`       | String  | Yes      | max 50 chars, unique | Machine-readable code (e.g. `GENERAL`, `RESERVATION`); not updatable |
| `sort_order` | Integer | Yes      | >= 0, default `0`    | Display order                                                        |
| `locales`    | Array   | —        | read-only            | All locale translations for this contact type                        |

### Contact Type Locale

| Field         | Type    | Required | Constraints   | Description                                          |
|---------------|---------|----------|---------------|------------------------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier                            |
| `locale_id`   | Long    | Yes      | must exist    | ID of an existing active locale; set on create only  |
| `name`        | String  | Yes      | max 100 chars | Localized name of the contact type                   |
| `description` | String  | No       | unlimited     | Localized description; omitted from response if null |
| `sort_order`  | Integer | Yes      | not null      | Display order for this locale entry                  |

---

## Pre-seeded Contact Types

The following contact types are seeded automatically:

| Code          | Name        | Description                              |
|---------------|-------------|------------------------------------------|
| `GENERAL`     | General     | Main contact for general inquiries       |
| `RESERVATION` | Reservation | Contact for booking and reservations     |
| `SALES`       | Sales       | Contact for sales and pricing inquiries  |
| `SUPPORT`     | Support     | Contact for guest support and assistance |
| `EMERGENCY`   | Emergency   | Emergency contact available 24/7         |
| `ACCOUNTING`  | Accounting  | Contact for billing and accounting       |

---

## Create Contact Type

`POST /api/v1/contact-types`

Creates a contact type along with its locale-specific translations in one request. All provided `locale_id` values must
reference existing, active locales.

### Request Body

```json
{
  "code": "CONCIERGE",
  "sort_order": 7,
  "locales": [
    {
      "locale_id": 1,
      "name": "Concierge",
      "description": "Contact for concierge services.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "কনসিয়ার্জ",
      "description": "কনসিয়ার্জ সেবার জন্য যোগাযোগ।",
      "sort_order": 2
    }
  ]
}
```

### Request Fields

| Field        | Type    | Required | Validation              |
|--------------|---------|----------|-------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars |
| `sort_order` | Integer | Yes      | Not null, >= 0          |
| `locales`    | Array   | No       | See locale fields below |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 7
}
```

---

## Get Contact Type

`GET /api/v1/contact-types/{id}`

Returns a single contact type with all its locale translations.

### Path Parameters

| Parameter | Type | Description            |
|-----------|------|------------------------|
| `id`      | Long | ID of the contact type |

### Response `200 OK`

Optional fields (locale `description`) are omitted from the response when not set.

```json
{
  "contact_type": {
    "id": 1,
    "code": "GENERAL",
    "sort_order": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "General",
        "description": "Main contact for general inquiries.",
        "sort_order": 1
      },
      {
        "id": 2,
        "locale_id": 2,
        "name": "সাধারণ",
        "sort_order": 2
      }
    ]
  }
}
```

---

## List / Search Contact Types

`GET /api/v1/contact-types`

Returns a paginated, filterable list of active (non-deleted) contact types. Each item includes all locale translations.
All filter parameters are optional; omitting them returns all contact types. The `code` filter performs a
case-insensitive partial match.

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
      "code": "GENERAL",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "General",
          "description": "Main contact for general inquiries.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "code": "RESERVATION",
      "sort_order": 2,
      "locales": [
        {
          "id": 2,
          "locale_id": 1,
          "name": "Reservation",
          "description": "Contact for booking and reservations.",
          "sort_order": 1
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 6,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Contact Type

`PUT /api/v1/contact-types/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the contact type locale endpoints.

### Path Parameters

| Parameter | Type | Description            |
|-----------|------|------------------------|
| `id`      | Long | ID of the contact type |

### Request Body

```json
{
  "sort_order": 1
}
```

### Request Fields

| Field        | Type    | Required | Validation     |
|--------------|---------|----------|----------------|
| `sort_order` | Integer | Yes      | Not null, >= 0 |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Contact Type

`DELETE /api/v1/contact-types/{id}`

Soft-deletes the contact type. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description            |
|-----------|------|------------------------|
| `id`      | Long | ID of the contact type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Contact Type Locales

Contact type locale endpoints manage per-locale translations for a contact type. The `{contact-type-id}` path parameter
must reference an existing, active contact type. All write operations on locale endpoints require the `ADMIN` role.

---

### Create Contact Type Locale

`POST /api/v1/contact-types/{contact-type-id}/locales`

Adds a new locale translation to an existing contact type. Requires `ADMIN` role.

#### Path Parameters

| Parameter         | Type | Description            |
|-------------------|------|------------------------|
| `contact-type-id` | Long | ID of the contact type |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "সাধারণ",
  "description": "সাধারণ অনুসন্ধানের জন্য প্রধান যোগাযোগ।",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 13
}
```

---

### Update Contact Type Locale

`PUT /api/v1/contact-types/{contact-type-id}/locales/{id}`

Updates an existing locale translation for a contact type. The `locale_id` is set on creation and cannot be changed.
Requires `ADMIN` role.

#### Path Parameters

| Parameter         | Type | Description                   |
|-------------------|------|-------------------------------|
| `contact-type-id` | Long | ID of the contact type        |
| `id`              | Long | ID of the contact type locale |

#### Request Body

```json
{
  "name": "General",
  "description": "Updated description.",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

### Delete Contact Type Locale

`DELETE /api/v1/contact-types/{contact-type-id}/locales/{id}`

Soft-deletes a contact type locale. The record is not removed from the database but will no longer appear in any
response. Requires `ADMIN` role.

#### Path Parameters

| Parameter         | Type | Description                   |
|-------------------|------|-------------------------------|
| `contact-type-id` | Long | ID of the contact type        |
| `id`              | Long | ID of the contact type locale |

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
  "message": "ContactType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                    |
|-------------|----------------------------|--------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields, invalid sort field, or `sort_order` less than 0 |
| 403         | `FORBIDDEN`                | Locale write operations attempted without `ADMIN` role                   |
| 404         | `ENTITY_NOT_FOUND`         | Contact type or locale not found, or already deleted                     |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate `code`)                             |
