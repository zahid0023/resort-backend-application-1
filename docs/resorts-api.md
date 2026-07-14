# Resorts API

Base URL: `/api/v1/resorts`

Resorts are the top-level entities of the platform. Creating a resort automatically assigns the authenticated user as
its owner with full permissions. `basic_info` and `contacts` are required at creation time and will be persisted
atomically in the same transaction. Each resort has exactly one `basic_info` record. All records support soft-delete —
deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                  | Description                                 | Access        |
|--------|-----------------------------------------------------------------------|---------------------------------------------|---------------|
| POST   | `/api/v1/resorts`                                                     | Create a resort                             | Authenticated |
| GET    | `/api/v1/resorts/{id}`                                                | Get a resort                                | Authenticated |
| GET    | `/api/v1/resorts`                                                     | List all resorts (paginated, filtered)      | Admin         |
| GET    | `/api/v1/resorts/my-resorts`                                          | List resorts the current user has access to | Resort User   |
| PUT    | `/api/v1/resorts/{id}`                                                | Update a resort                             | Authenticated |
| DELETE | `/api/v1/resorts/{id}`                                                | Delete a resort                             | Authenticated |
| POST   | `/api/v1/resorts/{resort-id}/users`                                   | Add a user to a resort                      | Authenticated |
| GET    | `/api/v1/resorts/{resort-id}/users`                                   | List resort users (paginated)               | Authenticated |
| GET    | `/api/v1/resorts/{resort-id}/users/{id}`                              | Get a resort user                           | Authenticated |
| PUT    | `/api/v1/resorts/{resort-id}/users/{id}`                              | Update a resort user's access type          | Authenticated |
| DELETE | `/api/v1/resorts/{resort-id}/users/{id}`                              | Remove a user from a resort                 | Authenticated |
| POST   | `/api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions`      | Assign permissions to a resort user         | Authenticated |
| GET    | `/api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions`      | List resort user permissions (paginated)    | Authenticated |
| GET    | `/api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions/{id}` | Get a resort user permission                | Authenticated |
| PUT    | `/api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions/{id}` | Update a resort user permission             | Authenticated |
| DELETE | `/api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions/{id}` | Remove a resort user permission             | Authenticated |

---

## Data Model

### Resort

| Field  | Type   | Required | Constraints                      | Description                            |
|--------|--------|----------|----------------------------------|----------------------------------------|
| `id`   | Long   | —        | read-only                        | Auto-generated identifier              |
| `code` | String | Yes      | max 100 chars, unique, immutable | Short unique identifier for the resort |

### Resort Basic Info (nested in create, returned in list responses)

| Field        | Type    | Required | Constraints             | Description                                         |
|--------------|---------|----------|-------------------------|-----------------------------------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars | Short code for the basic info record; not updatable |
| `sort_order` | Integer | Yes      | Not null                | Display order                                       |
| `estd`       | Short   | Yes      | Not null                | Year the resort was established                     |
| `country_id` | Long    | Yes      | Not null, must exist    | ID of the country where the resort is located       |
| `city_id`    | Long    | Yes      | Not null, must exist    | ID of the city where the resort is located          |
| `logo_url`   | String  | No       | max 500 chars           | URL of the resort logo                              |
| `lat`        | Double  | No       | —                       | Latitude coordinate                                 |
| `lon`        | Double  | No       | —                       | Longitude coordinate                                |
| `locales`    | Array   | No       | See locale fields below | Locale translations for the basic info              |

**Basic info locale fields (`basic_info.locales[]`):**

| Field               | Type    | Required | Constraints              | Description                         |
|---------------------|---------|----------|--------------------------|-------------------------------------|
| `locale_id`         | Long    | Yes      | Not null, must exist     | ID of an existing active locale     |
| `sort_order`        | Integer | Yes      | Not null                 | Display order for this locale entry |
| `name`              | String  | Yes      | Not blank, max 255 chars | Localized name of the resort        |
| `tagline`           | String  | Yes      | Not blank                | Localized tagline or slogan         |
| `short_description` | String  | No       | max 1024 chars           | Localized short description         |
| `address`           | String  | No       | —                        | Localized address text              |

### Resort Contact (nested in create)

| Field                      | Type    | Required | Constraints          | Description                                         |
|----------------------------|---------|----------|----------------------|-----------------------------------------------------|
| `contact_type_id`          | Long    | Yes      | Not null, must exist | ID of an existing active contact type               |
| `communication_channel_id` | Long    | Yes      | Not null, must exist | ID of an existing active communication channel      |
| `contact_value`            | String  | Yes      | Not blank            | The actual contact value (e.g. phone number, email) |
| `is_primary`               | Boolean | Yes      | Not null             | Whether this is the primary contact of its type     |
| `sort_order`               | Integer | Yes      | Not null, >= 0       | Display order                                       |

### Resort User

| Field                   | Type    | Required | Constraints | Description                                     |
|-------------------------|---------|----------|-------------|-------------------------------------------------|
| `id`                    | Long    | —        | read-only   | Auto-generated identifier                       |
| `resort_id`             | Long    | —        | read-only   | ID of the resort                                |
| `user_id`               | Long    | Yes      | must exist  | ID of the user to add                           |
| `resort_access_type_id` | Long    | Yes      | must exist  | ID of the access type to assign                 |
| `joined_at`             | Instant | —        | read-only   | Timestamp when the user was added to the resort |

### Resort User Permission

| Field                       | Type    | Required | Constraints | Description                         |
|-----------------------------|---------|----------|-------------|-------------------------------------|
| `id`                        | Long    | —        | read-only   | Auto-generated identifier           |
| `resort_user_id`            | Long    | —        | read-only   | ID of the resort user               |
| `resort_permission_type_id` | Long    | Yes      | must exist  | ID of the permission type to assign |
| `is_allowed`                | Boolean | Yes      | Not null    | Whether the permission is granted   |

---

## Create Resort

`POST /api/v1/resorts`

Creates a new resort. The authenticated user is automatically assigned as owner with all permissions granted. The
`code` field is set at creation time and cannot be changed. `basic_info` and `contacts` are required and created
atomically — if any part fails, the entire operation is rolled back. Each resort can have only one `basic_info`.

### Request Fields

| Field        | Type   | Required | Validation                                 |
|--------------|--------|----------|--------------------------------------------|
| `code`       | String | Yes      | Not blank, max 100 chars                   |
| `basic_info` | Object | Yes      | See Resort Basic Info fields above         |
| `contacts`   | Array  | Yes      | Not empty, see Resort Contact fields above |

### Request Body

```json
{
  "code": "SUNRISE",
  "basic_info": {
    "code": "SUNRISE-INFO",
    "sort_order": 1,
    "estd": 1998,
    "country_id": 1,
    "city_id": 3,
    "logo_url": "https://cdn.example.com/logo.png",
    "lat": 21.4272,
    "lon": 92.0058,
    "locales": [
      {
        "locale_id": 1,
        "sort_order": 1,
        "name": "Sunrise Beach Resort",
        "tagline": "Where the sea meets serenity",
        "short_description": "A luxury beachfront resort on the coast of Cox's Bazar.",
        "address": "Marine Drive Road, Cox's Bazar, Bangladesh"
      }
    ]
  },
  "contacts": [
    {
      "contact_type_id": 1,
      "communication_channel_id": 2,
      "contact_value": "+8801700000000",
      "is_primary": true,
      "sort_order": 1
    },
    {
      "contact_type_id": 2,
      "communication_channel_id": 3,
      "contact_value": "info@sunriseresort.com",
      "is_primary": true,
      "sort_order": 2
    }
  ]
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

## Get Resort

`GET /api/v1/resorts/{id}`

Returns a single resort by ID.

### Path Parameters

| Parameter | Type | Description      |
|-----------|------|------------------|
| `id`      | Long | ID of the resort |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "SUNRISE"
  }
}
```

---

## List All Resorts (Admin)

`GET /api/v1/resorts`

Returns a paginated, filterable list of all active (non-deleted) resorts across the platform. Each item includes a
`resort_basic_info` summary. Intended for admin use.

### Query Parameters

| Parameter  | Type   | Default | Constraints               | Description                                |
|------------|--------|---------|---------------------------|--------------------------------------------|
| `code`     | String | —       | —                         | Filter by code (partial, case-insensitive) |
| `page`     | int    | `0`     | >= 0                      | Zero-based page index                      |
| `size`     | int    | `10`    | 1 – 50                    | Number of items per page                   |
| `sort_by`  | String | `id`    | `id`, `code`, `createdAt` | Field to sort by                           |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`             | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "SUNRISE",
      "resort_basic_info": {
        "id": 1,
        "resort_id": 1,
        "code": "SUNRISE-INFO",
        "sort_order": 1,
        "estd": 1998,
        "country_id": 1,
        "city_id": 3,
        "logo_url": "https://cdn.example.com/logo.png",
        "lat": 21.4272,
        "lon": 92.0058
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## List My Resorts (Resort User)

`GET /api/v1/resorts/my-resorts`

Returns a paginated, filterable list of active (non-deleted) resorts that the authenticated user has been granted
access to (as owner, staff, or any other access type). Each item includes a `resort_basic_info` summary.

### Query Parameters

| Parameter  | Type   | Default | Constraints               | Description                                |
|------------|--------|---------|---------------------------|--------------------------------------------|
| `code`     | String | —       | —                         | Filter by code (partial, case-insensitive) |
| `page`     | int    | `0`     | >= 0                      | Zero-based page index                      |
| `size`     | int    | `10`    | 1 – 50                    | Number of items per page                   |
| `sort_by`  | String | `id`    | `id`, `code`, `createdAt` | Field to sort by                           |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`             | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "SUNRISE",
      "resort_basic_info": {
        "id": 1,
        "resort_id": 1,
        "code": "SUNRISE-INFO",
        "sort_order": 1,
        "estd": 1998,
        "country_id": 1,
        "city_id": 3,
        "logo_url": "https://cdn.example.com/logo.png",
        "lat": 21.4272,
        "lon": 92.0058
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Resort

`PUT /api/v1/resorts/{id}`

Updates a resort. The `code` field is set at creation time and cannot be changed. Basic info and contacts are managed
via their own sub-resource endpoints.

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

Soft-deletes the resort. The record is not removed from the database but will no longer appear in any response.
Only a member of the resort can delete it.

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

## Add Resort User

`POST /api/v1/resorts/{resort-id}/users`

Adds a user to a resort with the specified access type. If the user was previously removed (soft-deleted), they are
reactivated with the new access type.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Request Fields

| Field                   | Type | Required | Validation |
|-------------------------|------|----------|------------|
| `user_id`               | Long | Yes      | Must exist |
| `resort_access_type_id` | Long | Yes      | Must exist |

### Request Body

```json
{
  "user_id": 5,
  "resort_access_type_id": 2
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

## List Resort Users

`GET /api/v1/resorts/{resort-id}/users`

Returns a paginated list of active users belonging to the resort.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Query Parameters

| Parameter  | Type   | Default | Constraints                    | Description              |
|------------|--------|---------|--------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                           | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                         | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `joined_at`, `createdAt` | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                  | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort_id": 1,
      "user_id": 2,
      "resort_access_type_id": 1,
      "joined_at": "2024-01-15T10:30:00Z"
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Get Resort User

`GET /api/v1/resorts/{resort-id}/users/{id}`

Returns a single resort user by ID.

### Path Parameters

| Parameter   | Type | Description           |
|-------------|------|-----------------------|
| `resort-id` | Long | ID of the resort      |
| `id`        | Long | ID of the resort user |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "resort_id": 1,
    "user_id": 2,
    "resort_access_type_id": 1,
    "joined_at": "2024-01-15T10:30:00Z"
  }
}
```

---

## Update Resort User

`PUT /api/v1/resorts/{resort-id}/users/{id}`

Updates the access type of a resort user.

### Path Parameters

| Parameter   | Type | Description           |
|-------------|------|-----------------------|
| `resort-id` | Long | ID of the resort      |
| `id`        | Long | ID of the resort user |

### Request Fields

| Field                   | Type | Required | Validation |
|-------------------------|------|----------|------------|
| `resort_access_type_id` | Long | Yes      | Must exist |

### Request Body

```json
{
  "resort_access_type_id": 3
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

## Remove Resort User

`DELETE /api/v1/resorts/{resort-id}/users/{id}`

Soft-deletes the resort user. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter   | Type | Description           |
|-------------|------|-----------------------|
| `resort-id` | Long | ID of the resort      |
| `id`        | Long | ID of the resort user |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Assign Resort User Permissions

`POST /api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions`

Assigns one or more permissions to a resort user. Each permission is linked to a permission type with an `is_allowed`
flag. Submitting a permission type that is already assigned throws an error.

### Path Parameters

| Parameter        | Type | Description           |
|------------------|------|-----------------------|
| `resort-id`      | Long | ID of the resort      |
| `resort-user-id` | Long | ID of the resort user |

### Request Fields

| Field                                     | Type    | Required | Validation |
|-------------------------------------------|---------|----------|------------|
| `permissions`                             | Array   | Yes      | Not empty  |
| `permissions[].resort_permission_type_id` | Long    | Yes      | Must exist |
| `permissions[].is_allowed`                | Boolean | Yes      | Not null   |

### Request Body

```json
{
  "permissions": [
    {
      "resort_permission_type_id": 1,
      "is_allowed": true
    },
    {
      "resort_permission_type_id": 2,
      "is_allowed": false
    }
  ]
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

## List Resort User Permissions

`GET /api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions`

Returns a paginated list of active permissions for the resort user.

### Path Parameters

| Parameter        | Type | Description           |
|------------------|------|-----------------------|
| `resort-id`      | Long | ID of the resort      |
| `resort-user-id` | Long | ID of the resort user |

### Query Parameters

| Parameter  | Type   | Default | Constraints       | Description              |
|------------|--------|---------|-------------------|--------------------------|
| `page`     | int    | `0`     | >= 0              | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50            | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `createdAt` | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`     | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort_user_id": 3,
      "resort_permission_type_id": 1,
      "is_allowed": true
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Get Resort User Permission

`GET /api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions/{id}`

Returns a single resort user permission by ID.

### Path Parameters

| Parameter        | Type | Description                      |
|------------------|------|----------------------------------|
| `resort-id`      | Long | ID of the resort                 |
| `resort-user-id` | Long | ID of the resort user            |
| `id`             | Long | ID of the resort user permission |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "resort_user_id": 3,
    "resort_permission_type_id": 1,
    "is_allowed": true
  }
}
```

---

## Update Resort User Permission

`PUT /api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions/{id}`

Updates the `is_allowed` flag of a resort user permission.

### Path Parameters

| Parameter        | Type | Description                      |
|------------------|------|----------------------------------|
| `resort-id`      | Long | ID of the resort                 |
| `resort-user-id` | Long | ID of the resort user            |
| `id`             | Long | ID of the resort user permission |

### Request Fields

| Field        | Type    | Required | Validation |
|--------------|---------|----------|------------|
| `is_allowed` | Boolean | Yes      | Not null   |

### Request Body

```json
{
  "is_allowed": false
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

## Remove Resort User Permission

`DELETE /api/v1/resorts/{resort-id}/users/{resort-user-id}/permissions/{id}`

Soft-deletes the resort user permission. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter        | Type | Description                      |
|------------------|------|----------------------------------|
| `resort-id`      | Long | ID of the resort                 |
| `resort-user-id` | Long | ID of the resort user            |
| `id`             | Long | ID of the resort user permission |

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
  "message": "Resort not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                    |
|-------------|----------------------------|--------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields, invalid sort field, empty contacts list, or permission already assigned                         |
| 403         | `ACCESS_DENIED`            | User is not a member of the resort (delete)                                                                              |
| 404         | `ENTITY_NOT_FOUND`         | Resort, user, access type, permission type, country, city, locale, contact type, or communication channel not found      |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate resort `code`, duplicate basic info `code`, basic info already exists for the resort, or user already a member |
