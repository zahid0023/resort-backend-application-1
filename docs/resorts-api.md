# Resorts API

Base URL: `/api/v1/resorts`

Resorts are the top-level entities of the platform. Creating a resort automatically assigns the authenticated user as
its owner with full permissions. Two list endpoints are provided: one for admins (all resorts) and one for resort users
(only resorts they have been granted access to). All records support soft-delete — deleted records are hidden from all
responses.

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

| Field  | Type   | Required | Constraints                      | Description                            |
|--------|--------|----------|----------------------------------|----------------------------------------|
| `id`   | Long   | —        | read-only                        | Auto-generated identifier              |
| `code` | String | Yes      | max 100 chars, unique, immutable | Short unique identifier for the resort |

---

## Create Resort

`POST /api/v1/resorts`

Creates a new resort. The authenticated user is automatically assigned as owner with all permissions granted.
The `code` field is set at creation time and cannot be changed.

### Request Fields

| Field  | Type   | Required | Validation               |
|--------|--------|----------|--------------------------|
| `code` | String | Yes      | Not blank, max 100 chars |

### Request Body

```json
{
  "code": "SUNSET-RESORT"
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
    "code": "SUNSET-RESORT"
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
    {
      "id": 1,
      "code": "SUNSET-RESORT"
    },
    {
      "id": 2,
      "code": "OCEAN-VIEW"
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
    {
      "id": 1,
      "code": "SUNSET-RESORT"
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

Updates a resort. The `code` field is set at creation time and cannot be changed.

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

| HTTP Status | Error Code                 | Cause                                         |
|-------------|----------------------------|-----------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field |
| 403         | `ACCESS_DENIED`            | User is not a member of the resort (delete)   |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found or already deleted           |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `code`                              |
