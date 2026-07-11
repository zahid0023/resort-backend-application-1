# Resorts API

Base URL: `/api/v1/resorts`

Resorts are the top-level entities of the platform. Creating a resort automatically assigns the authenticated user as
its owner with full permissions. Optional `basic_info` and `contacts` can be provided at creation time and will be
persisted atomically in the same transaction. Two list endpoints are provided: one for admins (all resorts) and one for
resort users (only resorts they have been granted access to). All records support soft-delete — deleted records are
hidden from all responses.

---

## Endpoints

| Method | Path                         | Description                                 | Access        |
|--------|------------------------------|---------------------------------------------|---------------|
| POST   | `/api/v1/resorts`            | Create a resort                             | Authenticated |
| GET    | `/api/v1/resorts/{id}`       | Get a resort                                | Authenticated |
| GET    | `/api/v1/resorts`            | List all resorts (paginated, filtered)      | Admin         |
| GET    | `/api/v1/resorts/my-resorts` | List resorts the current user has access to | Resort User   |
| PUT    | `/api/v1/resorts/{id}`       | Update a resort                             | Authenticated |
| DELETE | `/api/v1/resorts/{id}`       | Delete a resort                             | Authenticated |

---

## Data Model

### Resort

| Field        | Type   | Required | Constraints                      | Description                            |
|--------------|--------|----------|----------------------------------|----------------------------------------|
| `id`         | Long   | —        | read-only                        | Auto-generated identifier              |
| `code`       | String | Yes      | max 100 chars, unique, immutable | Short unique identifier for the resort |

### Resort Basic Info (nested in create)

| Field        | Type    | Required | Constraints              | Description                                                   |
|--------------|---------|----------|--------------------------|---------------------------------------------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars  | Short code for the basic info record; not updatable           |
| `sort_order` | Integer | Yes      | Not null                 | Display order                                                 |
| `estd`       | Short   | Yes      | Not null                 | Year the resort was established                               |
| `country_id` | Long    | Yes      | Not null, must exist     | ID of the country where the resort is located                 |
| `city_id`    | Long    | Yes      | Not null, must exist     | ID of the city where the resort is located                    |
| `logo_url`   | String  | No       | max 500 chars            | URL of the resort logo                                        |
| `lat`        | Double  | No       | —                        | Latitude coordinate                                           |
| `lon`        | Double  | No       | —                        | Longitude coordinate                                          |
| `locales`    | Array   | No       | See locale fields below  | Locale translations for the basic info                        |

**Basic info locale fields (`basic_info.locales[]`):**

| Field               | Type    | Required | Constraints              | Description                          |
|---------------------|---------|----------|--------------------------|--------------------------------------|
| `locale_id`         | Long    | Yes      | Not null, must exist     | ID of an existing active locale      |
| `sort_order`        | Integer | Yes      | Not null                 | Display order for this locale entry  |
| `name`              | String  | Yes      | Not blank, max 255 chars | Localized name of the resort         |
| `tagline`           | String  | Yes      | Not blank                | Localized tagline or slogan          |
| `short_description` | String  | No       | max 1024 chars           | Localized short description          |
| `address`           | String  | No       | —                        | Localized address text               |

### Resort Contact (nested in create)

| Field                    | Type    | Required | Constraints          | Description                                        |
|--------------------------|---------|----------|----------------------|----------------------------------------------------|
| `contact_type_id`        | Long    | Yes      | Not null, must exist | ID of an existing active contact type              |
| `communication_channel_id` | Long  | Yes      | Not null, must exist | ID of an existing active communication channel     |
| `contact_value`          | String  | Yes      | Not blank            | The actual contact value (e.g. phone number, email)|
| `is_primary`             | Boolean | Yes      | Not null             | Whether this is the primary contact of its type    |
| `sort_order`             | Integer | Yes      | Not null, >= 0       | Display order                                      |

---

## Create Resort

`POST /api/v1/resorts`

Creates a new resort. The authenticated user is automatically assigned as owner with all permissions granted. The
`code` field is set at creation time and cannot be changed. Optionally, `basic_info` and a list of `contacts` can be
provided and will be created atomically — if any part fails, the entire operation is rolled back.

### Request Fields

| Field        | Type   | Required | Validation                        |
|--------------|--------|----------|-----------------------------------|
| `code`       | String | Yes      | Not blank, max 100 chars          |
| `basic_info` | Object | No       | See Resort Basic Info fields above |
| `contacts`   | Array  | No       | See Resort Contact fields above   |

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

Returns a paginated, filterable list of all active (non-deleted) resorts across the platform. Intended for admin use.

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
    { "id": 1, "code": "SUNRISE" },
    { "id": 2, "code": "OCEAN-VIEW" }
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

## List My Resorts (Resort User)

`GET /api/v1/resorts/my-resorts`

Returns a paginated, filterable list of active (non-deleted) resorts that the authenticated user has been granted
access to (as owner, staff, or any other access type). Only active, non-deleted resort memberships are considered.

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
    { "id": 1, "code": "SUNRISE" }
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

| HTTP Status | Error Code                 | Cause                                                                                                             |
|-------------|----------------------------|-------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field                                                                     |
| 403         | `ACCESS_DENIED`            | User is not a member of the resort (delete)                                                                       |
| 404         | `ENTITY_NOT_FOUND`         | Resort, country, city, locale, contact type, or communication channel not found / deleted                         |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate resort `code`, duplicate basic info `code`, or basic info already exists for the resort                 |
