# Rooms API Documentation

## Overview

Base path: `/api/v1/resorts/{resort-id}/resort-room-categories/{resort-room-category-id}/rooms`

All requests require a JWT bearer token:

```
Authorization: Bearer <token>
```

All request and response bodies use **snake_case** JSON field names.

---

## Endpoints

### POST `/`

Create a room under a resort room category.

**Request Body**

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `name` | string | yes | |
| `description` | string | no | |
| `room_number` | string | no | |
| `floor` | integer | no | |
| `max_adults` | integer | yes | |
| `max_children` | integer | no | Defaults to `0` |
| `base_price` | decimal | yes | |

> `max_occupancy` is computed automatically as `max_adults + max_children`.

**Example Request**

```json
{
  "name": "Room 101",
  "description": "A cozy double room with ocean view.",
  "room_number": "101",
  "floor": 1,
  "max_adults": 2,
  "max_children": 1,
  "base_price": "150.00"
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

Retrieve a single room by ID.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| `resort-id` | long | ID of the resort |
| `resort-room-category-id` | long | ID of the resort room category |
| `id` | long | ID of the room |

**Response** `200 OK`

```json
{
  "data": {
    "id": 1,
    "resort_room_category_id": 1,
    "name": "Room 101",
    "description": "A cozy double room with ocean view.",
    "room_number": "101",
    "floor": 1,
    "max_adults": 2,
    "max_children": 1,
    "max_occupancy": 3,
    "base_price": "150.00"
  }
}
```

---

### GET `/`

List all rooms in a resort room category.

**Query Parameters**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | integer | `0` | Zero-based page number |
| `size` | integer | `10` | Items per page (max `50`) |
| `sort_by` | string | `id` | Field to sort by (see allowed values below) |
| `sort_dir` | string | `ASC` | Sort direction: `ASC` or `DESC` |

**Allowed `sort_by` values:** `id`, `name`, `roomNumber`, `floor`, `basePrice`

**Response** `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort_room_category_id": 1,
      "name": "Room 101",
      "description": "A cozy double room with ocean view.",
      "room_number": "101",
      "floor": 1,
      "max_adults": 2,
      "max_children": 1,
      "max_occupancy": 3,
      "base_price": "150.00"
    }
  ],
  "current_page": 0,
  "total_pages": 3,
  "total_elements": 25,
  "page_size": 10,
  "has_next": true,
  "has_previous": false
}
```

---

### PUT `/{id}`

Update a room. Only provided fields are applied. `max_occupancy` is recomputed automatically if `max_adults` or `max_children` changes.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| `resort-id` | long | ID of the resort |
| `resort-room-category-id` | long | ID of the resort room category |
| `id` | long | ID of the room |

**Request Body** — all fields optional

| Field | Type |
|-------|------|
| `name` | string |
| `description` | string |
| `room_number` | string |
| `floor` | integer |
| `max_adults` | integer |
| `max_children` | integer |
| `base_price` | decimal |

**Example Request**

```json
{
  "base_price": "175.00",
  "max_adults": 3
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

Soft delete a room. The record is retained in the database with `is_deleted = true` and `is_active = false`.

**Path Parameters**

| Parameter | Type | Description |
|-----------|------|-------------|
| `resort-id` | long | ID of the resort |
| `resort-room-category-id` | long | ID of the resort room category |
| `id` | long | ID of the room |

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
| `404 Not Found` | Room not found for the given resort room category |
