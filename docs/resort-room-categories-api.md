# Resort Room Categories API Documentation

## Overview

Base path: `/api/v1/resorts/{resort-id}/resort-room-categories`

All requests require a JWT bearer token:

```
Authorization: Bearer <token>
```

All request and response bodies use **snake_case** JSON field names.

> A resort room category links a global [Room Category](/api/v1/room-categories) to a specific resort, allowing optional name, description, and sort order overrides.

---

## Endpoints

### POST `/`

Create a resort room category under a resort.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| `resort-id` | long | ID of the resort |

**Request Body**

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `room_category_id` | long | yes | ID of the global room category |
| `name` | string | no | Override name for this resort |
| `description` | string | no | |
| `sort_order` | integer | no | |

**Example Request**

```json
{
  "room_category_id": 2,
  "name": "Deluxe Ocean View",
  "description": "Spacious rooms with direct ocean view.",
  "sort_order": 1
}
```

**Response** `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

### GET `/{id}`

Retrieve a single resort room category by ID.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| `resort-id` | long | ID of the resort |
| `id` | long | ID of the resort room category |

**Response** `200 OK`

```json
{
  "data": {
    "id": 1,
    "resort_id": 1,
    "room_category_id": 2,
    "name": "Deluxe Ocean View",
    "description": "Spacious rooms with direct ocean view.",
    "sort_order": 1
  }
}
```

---

### GET `/`

List all resort room categories for a resort.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| `resort-id` | long | ID of the resort |

**Query Parameters**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | integer | `0` | Zero-based page number |
| `size` | integer | `10` | Items per page (max `50`) |
| `sort_by` | string | `id` | Field to sort by (see allowed values below) |
| `sort_dir` | string | `ASC` | Sort direction: `ASC` or `DESC` |

**Allowed `sort_by` values:** `id`, `name`, `sortOrder`

**Response** `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort_id": 1,
      "room_category_id": 2,
      "name": "Deluxe Ocean View",
      "description": "Spacious rooms with direct ocean view.",
      "sort_order": 1
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

### PUT `/{id}`

Update a resort room category. Only provided fields are applied.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| `resort-id` | long | ID of the resort |
| `id` | long | ID of the resort room category |

**Request Body** — all fields optional

| Field | Type |
|-------|------|
| `name` | string |
| `description` | string |
| `sort_order` | integer |

**Example Request**

```json
{
  "sort_order": 2
}
```

**Response** `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

### DELETE `/{id}`

Soft delete a resort room category. The record is retained in the database with `is_deleted = true` and `is_active = false`.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| `resort-id` | long | ID of the resort |
| `id` | long | ID of the resort room category |

**Response** `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Error Responses

| Status | Scenario |
|--------|----------|
| `400 Bad Request` | Invalid request body or invalid `sort_by` value |
| `401 Unauthorized` | Missing or invalid JWT token |
| `404 Not Found` | Resort or resort room category not found |
