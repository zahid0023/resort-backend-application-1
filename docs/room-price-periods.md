# Room Price Periods API

Base URL: `/api/v1/resorts/{resort-id}/resort-room-categories/{resort-room-category-id}/rooms/{room-id}/room-price-periods`

Room price periods define pricing for a specific room over a date range, categorized by a price type. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                                                                                          | Description                  |
|--------|-----------------------------------------------------------------------------------------------------------------------------------------------|------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/resort-room-categories/{resort-room-category-id}/rooms/{room-id}/room-price-periods`                             | Create a room price period   |
| GET    | `/api/v1/resorts/{resort-id}/resort-room-categories/{resort-room-category-id}/rooms/{room-id}/room-price-periods`                             | List all room price periods  |
| GET    | `/api/v1/resorts/{resort-id}/resort-room-categories/{resort-room-category-id}/rooms/{room-id}/room-price-periods/{id}`                        | Get a room price period      |
| PUT    | `/api/v1/resorts/{resort-id}/resort-room-categories/{resort-room-category-id}/rooms/{room-id}/room-price-periods/{id}`                        | Update a room price period   |
| DELETE | `/api/v1/resorts/{resort-id}/resort-room-categories/{resort-room-category-id}/rooms/{room-id}/room-price-periods/{id}`                        | Delete a room price period   |

---

## Path Parameters

All endpoints share the following path parameters:

| Parameter                  | Type | Description                             |
|----------------------------|------|-----------------------------------------|
| `resort-id`                | Long | ID of the resort                        |
| `resort-room-category-id`  | Long | ID of the resort room category          |
| `room-id`                  | Long | ID of the room                          |

---

## Data Model

| Field            | Type       | Required | Description                                              |
|------------------|------------|----------|----------------------------------------------------------|
| `start_date`     | Date       | Yes      | Start date of the pricing period (`YYYY-MM-DD`)         |
| `end_date`       | Date       | Yes      | End date of the pricing period (`YYYY-MM-DD`)           |
| `price`          | Decimal    | Yes      | Price for the period (up to 12 digits, 2 decimal places) |
| `priority`       | Integer    | No       | Conflict resolution priority (defaults to `1`)          |
| `price_type_id`  | Long       | Yes      | ID of the price type to associate                       |

---

## Create Room Price Period

`POST .../room-price-periods`

### Request Body

```json
{
  "start_date": "2025-06-01",
  "end_date": "2025-08-31",
  "price": 350.00,
  "priority": 1,
  "price_type_id": 1
}
```

| Field           | Type    | Required |
|-----------------|---------|----------|
| `start_date`    | Date    | Yes      |
| `end_date`      | Date    | Yes      |
| `price`         | Decimal | Yes      |
| `priority`      | Integer | No       |
| `price_type_id` | Long    | Yes      |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Room Price Period

`GET .../room-price-periods/{id}`

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the room price period  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "room_id": 5,
    "start_date": "2025-06-01",
    "end_date": "2025-08-31",
    "price": 350.00,
    "priority": 1,
    "price_type_id": 1,
    "price_type_code": "HIGH_SEASON",
    "price_type_name": "High Season"
  }
}
```

---

## List All Room Price Periods

`GET .../room-price-periods`

Returns a paginated list of active (non-deleted) price periods for the given room.

### Query Parameters

| Parameter  | Type   | Default | Constraints                                        | Description              |
|------------|--------|---------|----------------------------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                                               | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                                             | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `startDate`, `endDate`, `price`, `priority`  | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                                      | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "room_id": 5,
      "start_date": "2025-06-01",
      "end_date": "2025-08-31",
      "price": 350.00,
      "priority": 1,
      "price_type_id": 1,
      "price_type_code": "HIGH_SEASON",
      "price_type_name": "High Season"
    },
    {
      "id": 2,
      "room_id": 5,
      "start_date": "2025-09-01",
      "end_date": "2025-11-30",
      "price": 200.00,
      "priority": 1,
      "price_type_id": 2,
      "price_type_code": "LOW_SEASON",
      "price_type_name": "Low Season"
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

## Update Room Price Period

`PUT .../room-price-periods/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the room price period  |

### Request Body

```json
{
  "price": 375.00,
  "priority": 2
}
```

| Field           | Type    | Required |
|-----------------|---------|----------|
| `start_date`    | Date    | No       |
| `end_date`      | Date    | No       |
| `price`         | Decimal | No       |
| `priority`      | Integer | No       |
| `price_type_id` | Long    | No       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Room Price Period

`DELETE .../room-price-periods/{id}`

Soft-deletes the room price period. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the room price period  |

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
  "message": "Room Price Period with id: 99 was not found."
}
```

| HTTP Status | Error Code         | Cause                                                           |
|-------------|--------------------|-----------------------------------------------------------------|
| `400`       | `BAD_REQUEST`      | Missing required fields or invalid values                       |
| `401`       | `UNAUTHORIZED`     | Missing or invalid authentication token                         |
| `404`       | `ENTITY_NOT_FOUND` | Room, price type, or price period not found or has been deleted |
