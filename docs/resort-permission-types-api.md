# Resort Permission Types API

Base URL: `/api/v1/resort-permission-types`

Resort permission types define the granular actions a user is allowed to perform within a specific resort (e.g.,
`VIEW_BOOKING`, `CREATE_BOOKING`, `MANAGE_STAFF`). This is platform-managed master data — only administrators can
create, update, or delete permission types. Resort owners reference these types when granting specific capabilities
to other users on their resort (e.g., a Booker user may be granted `VIEW_BOOKING` and `CREATE_BOOKING` but not
`MANAGE_STAFF`).

Display names and descriptions are locale-specific and are embedded in every response via the `locales` array. All
records support soft-delete — deleted records are hidden from all responses.

The following permission types are seeded by the platform on first run:

| Code             | Description                                                   |
|------------------|---------------------------------------------------------------|
| `VIEW_BOOKING`   | Can view all reservations and booking details for the resort. |
| `CREATE_BOOKING` | Can create new reservations on behalf of guests.              |
| `CANCEL_BOOKING` | Can cancel existing reservations.                             |
| `MANAGE_ROOMS`   | Can manage room availability, pricing, and configuration.     |
| `VIEW_REPORTS`   | Can access occupancy, revenue, and operational reports.       |
| `MANAGE_STAFF`   | Can assign and revoke resort access for other users.          |

---

## Endpoints

| Method | Path                                                                       | Access | Description                              |
|--------|----------------------------------------------------------------------------|--------|------------------------------------------|
| POST   | `/api/v1/resort-permission-types`                                          | Admin  | Create a resort permission type          |
| GET    | `/api/v1/resort-permission-types`                                          | Any    | List / search resort permission types    |
| GET    | `/api/v1/resort-permission-types/{id}`                                     | Any    | Get a resort permission type             |
| PUT    | `/api/v1/resort-permission-types/{id}`                                     | Admin  | Update a resort permission type          |
| DELETE | `/api/v1/resort-permission-types/{id}`                                     | Admin  | Delete a resort permission type          |
| POST   | `/api/v1/resort-permission-types/{resort-permission-type-id}/locales`      | Admin  | Add a locale to a resort permission type |
| PUT    | `/api/v1/resort-permission-types/{resort-permission-type-id}/locales/{id}` | Admin  | Update a resort permission type locale   |
| DELETE | `/api/v1/resort-permission-types/{resort-permission-type-id}/locales/{id}` | Admin  | Delete a resort permission type locale   |

> **Access control:** All write endpoints (`POST`, `PUT`, `DELETE`) require the `ADMIN` role. Read endpoints (`GET`)
> require any valid authenticated session.

---

## Data Model

### Resort Permission Type

| Field        | Type    | Required | Constraints       | Description                                                                                |
|--------------|---------|----------|-------------------|--------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only         | Auto-generated identifier                                                                  |
| `code`       | String  | Yes      | max 150 chars     | Unique identifier for the permission (e.g., `VIEW_BOOKING`). Not updatable after creation. |
| `sort_order` | Integer | Yes      | >= 1, default `1` | Display order                                                                              |
| `locales`    | Array   | No       | —                 | Locale entries (included in all responses)                                                 |

### Resort Permission Type Locale

| Field         | Type    | Required | Constraints   | Description                                        |
|---------------|---------|----------|---------------|----------------------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier                          |
| `locale_id`   | Long    | Yes      | must exist    | ID of the locale. Not updatable after creation.    |
| `name`        | String  | Yes      | max 255 chars | Display name of the permission type in this locale |
| `description` | String  | No       | unlimited     | Full description in this locale; omitted if null   |
| `sort_order`  | Integer | Yes      | >= 1          | Display order for this locale entry                |

---

## Create Resort Permission Type

`POST /api/v1/resort-permission-types`

Creates a resort permission type along with its locale-specific translations in one request. All provided `locale_id`
values must reference existing, active locales. Requires the `ADMIN` role.

### Request Body

```json
{
  "code": "MANAGE_PRICING",
  "sort_order": 7,
  "locales": [
    {
      "locale_id": 1,
      "name": "Manage Pricing",
      "description": "Can update room rates, seasonal pricing, and discount configurations.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "মূল্য নির্ধারণ পরিচালনা",
      "description": "রুমের হার, মৌসুমী মূল্য নির্ধারণ এবং ছাড়ের কনফিগারেশন আপডেট করতে পারেন।",
      "sort_order": 2
    }
  ]
}
```

### Request Fields

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `code`       | String  | Yes      | Not blank, max 150 chars |
| `sort_order` | Integer | Yes      | Not null, >= 1           |
| `locales`    | Array   | No       | See locale fields below  |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null, >= 1           |

### Response `201 Created`

```json
{
  "success": true,
  "id": 7
}
```

---

## Get Resort Permission Type

`GET /api/v1/resort-permission-types/{id}`

Returns a single resort permission type with all its locale translations.

### Path Parameters

| Parameter | Type | Description                      |
|-----------|------|----------------------------------|
| `id`      | Long | ID of the resort permission type |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "VIEW_BOOKING",
    "sort_order": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "View Bookings",
        "description": "Can view all reservations and booking details for the resort.",
        "sort_order": 1
      },
      {
        "id": 2,
        "locale_id": 2,
        "name": "বুকিং দেখুন",
        "description": "রিসোর্টের সমস্ত রিজার্ভেশন এবং বুকিং বিবরণ দেখতে পারেন।",
        "sort_order": 2
      }
    ]
  }
}
```

---

## List / Search Resort Permission Types

`GET /api/v1/resort-permission-types`

Returns a paginated, filterable list of active (non-deleted) resort permission types including their locales. All
filter parameters are optional; omitting them returns all permission types. Filters perform a case-insensitive partial
match.

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
      "code": "VIEW_BOOKING",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "View Bookings",
          "description": "Can view all reservations and booking details for the resort.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "code": "CREATE_BOOKING",
      "sort_order": 2,
      "locales": [
        {
          "id": 3,
          "locale_id": 1,
          "name": "Create Bookings",
          "description": "Can create new reservations on behalf of guests.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 3,
      "code": "CANCEL_BOOKING",
      "sort_order": 3,
      "locales": [
        {
          "id": 5,
          "locale_id": 1,
          "name": "Cancel Bookings",
          "description": "Can cancel existing reservations.",
          "sort_order": 1
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 2,
  "total_elements": 6,
  "page_size": 3,
  "has_next": true,
  "has_previous": false
}
```

---

## Update Resort Permission Type

`PUT /api/v1/resort-permission-types/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the resort permission type locale endpoints. Requires the `ADMIN` role.

### Path Parameters

| Parameter | Type | Description                      |
|-----------|------|----------------------------------|
| `id`      | Long | ID of the resort permission type |

### Request Body

```json
{
  "sort_order": 2
}
```

### Request Fields

| Field        | Type    | Required | Validation     |
|--------------|---------|----------|----------------|
| `sort_order` | Integer | Yes      | Not null, >= 1 |

> **Note:** `code` is immutable and cannot be changed after creation.

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Resort Permission Type

`DELETE /api/v1/resort-permission-types/{id}`

Soft-deletes the resort permission type. The record is not removed from the database but will no longer appear in any
response. Requires the `ADMIN` role.

### Path Parameters

| Parameter | Type | Description                      |
|-----------|------|----------------------------------|
| `id`      | Long | ID of the resort permission type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Resort Permission Type Locales

Resort permission type locale endpoints manage per-locale translations for a permission type. The
`{resort-permission-type-id}` path parameter must reference an existing, active resort permission type. All locale
endpoints require the `ADMIN` role.

---

### Add Locale

`POST /api/v1/resort-permission-types/{resort-permission-type-id}/locales`

Adds a new locale translation to an existing resort permission type.

#### Path Parameters

| Parameter                   | Type | Description                      |
|-----------------------------|------|----------------------------------|
| `resort-permission-type-id` | Long | ID of the resort permission type |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "বুকিং দেখুন",
  "description": "রিসোর্টের সমস্ত রিজার্ভেশন এবং বুকিং বিবরণ দেখতে পারেন।",
  "sort_order": 2
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null, >= 1           |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 13
}
```

---

### Update Locale

`PUT /api/v1/resort-permission-types/{resort-permission-type-id}/locales/{id}`

Updates an existing locale translation for a resort permission type. `locale_id` is not updatable.

#### Path Parameters

| Parameter                   | Type | Description                             |
|-----------------------------|------|-----------------------------------------|
| `resort-permission-type-id` | Long | ID of the resort permission type        |
| `id`                        | Long | ID of the resort permission type locale |

#### Request Body

```json
{
  "name": "View Reservations",
  "description": "Read-only access to all reservation records.",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null, >= 1           |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

### Delete Locale

`DELETE /api/v1/resort-permission-types/{resort-permission-type-id}/locales/{id}`

Soft-deletes the locale entry. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter                   | Type | Description                             |
|-----------------------------|------|-----------------------------------------|
| `resort-permission-type-id` | Long | ID of the resort permission type        |
| `id`                        | Long | ID of the resort permission type locale |

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
  "message": "ResortPermissionType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                |
|-------------|----------------------------|------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations     |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request              |
| 401         | `UNAUTHORIZED`             | No valid authentication token provided               |
| 403         | `FORBIDDEN`                | Authenticated user does not have the `ADMIN` role    |
| 404         | `ENTITY_NOT_FOUND`         | Resort permission type or locale not found / deleted |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate `code`)         |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                              |
