# Resort Access Types API

Base URL: `/api/v1/resort-access-types`

Resort access types define the roles a user can hold in relation to a specific resort (e.g., `OWNER`, `BOOKER`). This
is platform-managed master data — only administrators can create, update, or delete access types. Resort owners
reference these types when assigning access to other users on their resort. Display names and descriptions are
locale-specific and are embedded in every response via the `locales` array. All records support soft-delete — deleted
records are hidden from all responses.

The following access types are seeded by the platform on first run:

| Code     | Description                                                          |
|----------|----------------------------------------------------------------------|
| `OWNER`  | Full control over the resort — can manage settings and bookings.     |
| `BOOKER` | Can browse resort details and make reservations on behalf of guests. |

---

## Endpoints

| Method | Path                                                               | Access | Description                          |
|--------|--------------------------------------------------------------------|--------|--------------------------------------|
| POST   | `/api/v1/resort-access-types`                                      | Admin  | Create a resort access type          |
| GET    | `/api/v1/resort-access-types`                                      | Any    | List / search resort access types    |
| GET    | `/api/v1/resort-access-types/{id}`                                 | Any    | Get a resort access type             |
| PUT    | `/api/v1/resort-access-types/{id}`                                 | Admin  | Update a resort access type          |
| DELETE | `/api/v1/resort-access-types/{id}`                                 | Admin  | Delete a resort access type          |
| POST   | `/api/v1/resort-access-types/{resort-access-type-id}/locales`      | Admin  | Add a locale to a resort access type |
| PUT    | `/api/v1/resort-access-types/{resort-access-type-id}/locales/{id}` | Admin  | Update a resort access type locale   |
| DELETE | `/api/v1/resort-access-types/{resort-access-type-id}/locales/{id}` | Admin  | Delete a resort access type locale   |

> **Access control:** All write endpoints (`POST`, `PUT`, `DELETE`) require the `ADMIN` role. Read endpoints (`GET`)
> require any valid authenticated session.

---

## Data Model

### Resort Access Type

| Field        | Type    | Required | Constraints       | Description                                                                      |
|--------------|---------|----------|-------------------|----------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only         | Auto-generated identifier                                                        |
| `code`       | String  | Yes      | max 100 chars     | Short unique identifier (e.g., `OWNER`, `BOOKER`). Not updatable after creation. |
| `sort_order` | Integer | Yes      | >= 0, default `1` | Display order                                                                    |
| `locales`    | Array   | No       | —                 | Locale entries (included in all responses)                                       |

### Resort Access Type Locale

| Field         | Type    | Required | Constraints   | Description                                      |
|---------------|---------|----------|---------------|--------------------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier                        |
| `locale_id`   | Long    | Yes      | must exist    | ID of the locale. Not updatable after creation.  |
| `name`        | String  | Yes      | max 255 chars | Display name of the access type in this locale   |
| `description` | String  | No       | unlimited     | Full description in this locale; omitted if null |
| `sort_order`  | Integer | Yes      | —             | Display order for this locale entry              |

---

## Create Resort Access Type

`POST /api/v1/resort-access-types`

Creates a resort access type along with its locale-specific translations in one request. All provided `locale_id`
values must reference existing, active locales. Requires the `ADMIN` role.

### Request Body

```json
{
  "code": "MANAGER",
  "sort_order": 3,
  "locales": [
    {
      "locale_id": 1,
      "name": "Manager",
      "description": "Can manage day-to-day resort operations and staff assignments.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "ম্যানেজার",
      "description": "দৈনন্দিন রিসোর্ট কার্যক্রম এবং কর্মী নিয়োগ পরিচালনা করতে পারেন।",
      "sort_order": 2
    }
  ]
}
```

### Request Fields

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `code`       | String  | Yes      | Not blank, max 100 chars |
| `sort_order` | Integer | Yes      | Not null, >= 0           |
| `locales`    | Array   | No       | See locale fields below  |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

## Get Resort Access Type

`GET /api/v1/resort-access-types/{id}`

Returns a single resort access type with all its locale translations.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the resort access type |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "OWNER",
    "sort_order": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Owner",
        "description": "Full control over the resort — can manage settings, staff, and bookings.",
        "sort_order": 1
      },
      {
        "id": 2,
        "locale_id": 2,
        "name": "মালিক",
        "description": "রিসোর্টের উপর সম্পূর্ণ নিয়ন্ত্রণ — সেটিংস, কর্মী এবং বুকিং পরিচালনা করতে পারেন।",
        "sort_order": 2
      }
    ]
  }
}
```

---

## List / Search Resort Access Types

`GET /api/v1/resort-access-types`

Returns a paginated, filterable list of active (non-deleted) resort access types including their locales. All filter
parameters are optional; omitting them returns all access types. Filters perform a case-insensitive partial match.

### Query Parameters

| Parameter  | Type   | Default | Constraints                            | Description                                |
|------------|--------|---------|----------------------------------------|--------------------------------------------|
| `code`     | String | —       | —                                      | Filter by code (partial, case-insensitive) |
| `page`     | int    | `0`     | >= 0                                   | Zero-based page index                      |
| `size`     | int    | `10`    | 1 – 50                                 | Number of items per page                   |
| `sort_by`  | String | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by                           |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                          | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "OWNER",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Owner",
          "description": "Full control over the resort — can manage settings, staff, and bookings.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "code": "BOOKER",
      "sort_order": 2,
      "locales": [
        {
          "id": 3,
          "locale_id": 1,
          "name": "Booker",
          "description": "Can browse resort details and make reservations on behalf of guests.",
          "sort_order": 1
        }
      ]
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

## Update Resort Access Type

`PUT /api/v1/resort-access-types/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the resort access type locale endpoints. Requires the `ADMIN` role.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the resort access type |

### Request Body

```json
{
  "sort_order": 2
}
```

### Request Fields

| Field        | Type    | Required | Validation     |
|--------------|---------|----------|----------------|
| `sort_order` | Integer | Yes      | Not null, >= 0 |

> **Note:** `code` is immutable and cannot be changed after creation.

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Resort Access Type

`DELETE /api/v1/resort-access-types/{id}`

Soft-deletes the resort access type. The record is not removed from the database but will no longer appear in any
response. Requires the `ADMIN` role.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the resort access type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Resort Access Type Locales

Resort access type locale endpoints manage per-locale translations for an access type. The
`{resort-access-type-id}` path parameter must reference an existing, active resort access type. All locale endpoints
require the `ADMIN` role.

---

### Add Locale

`POST /api/v1/resort-access-types/{resort-access-type-id}/locales`

Adds a new locale translation to an existing resort access type.

#### Path Parameters

| Parameter               | Type | Description                  |
|-------------------------|------|------------------------------|
| `resort-access-type-id` | Long | ID of the resort access type |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "মালিক",
  "description": "রিসোর্টের উপর সম্পূর্ণ নিয়ন্ত্রণ — সেটিংস, কর্মী এবং বুকিং পরিচালনা করতে পারেন।",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 4
}
```

---

### Update Locale

`PUT /api/v1/resort-access-types/{resort-access-type-id}/locales/{id}`

Updates an existing locale translation for a resort access type. `locale_id` is not updatable.

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `resort-access-type-id` | Long | ID of the resort access type        |
| `id`                    | Long | ID of the resort access type locale |

#### Request Body

```json
{
  "name": "Property Owner",
  "description": "Full administrative control over the resort property.",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
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

### Delete Locale

`DELETE /api/v1/resort-access-types/{resort-access-type-id}/locales/{id}`

Soft-deletes the locale entry. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter               | Type | Description                         |
|-------------------------|------|-------------------------------------|
| `resort-access-type-id` | Long | ID of the resort access type        |
| `id`                    | Long | ID of the resort access type locale |

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
  "message": "ResortAccessType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                             |
|-------------|----------------------------|---------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations  |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request           |
| 401         | `UNAUTHORIZED`             | No valid authentication token provided            |
| 403         | `FORBIDDEN`                | Authenticated user does not have the `ADMIN` role |
| 404         | `ENTITY_NOT_FOUND`         | Resort access type or locale not found / deleted  |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate `code`)      |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                           |
