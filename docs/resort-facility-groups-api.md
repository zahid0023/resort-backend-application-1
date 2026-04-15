# Resort Facility Groups API

Base URL: `/api/v1/resorts/{resort-id}/resort-facility-groups`

Links a facility group to a specific resort with a custom name, description, and sort order. All records support soft-delete.

---

## Endpoints

| Method | Path                                                             | Description                        |
|--------|------------------------------------------------------------------|------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/resort-facility-groups`            | Create a resort facility group     |
| GET    | `/api/v1/resorts/{resort-id}/resort-facility-groups`            | List all resort facility groups    |
| GET    | `/api/v1/resorts/{resort-id}/resort-facility-groups/{id}`       | Get a resort facility group        |
| PUT    | `/api/v1/resorts/{resort-id}/resort-facility-groups/{id}`       | Update a resort facility group     |
| DELETE | `/api/v1/resorts/{resort-id}/resort-facility-groups/{id}`       | Delete a resort facility group     |

---

## Data Model

| Field               | Type    | Required | Description                                      |
|---------------------|---------|----------|--------------------------------------------------|
| `facility_group_id` | Long    | Yes      | ID of the facility group to link                 |
| `name`              | String  | Yes      | Display name (max 255 chars)                     |
| `description`       | String  | Yes      | Description (unlimited)                          |
| `sort_order`        | Integer | Yes      | Display order                                    |

---

## Create Resort Facility Group

`POST /api/v1/resorts/{resort-id}/resort-facility-groups`

### Path Parameters

| Parameter   | Type | Description    |
|-------------|------|----------------|
| `resort-id` | Long | ID of the resort |

### Request Body

```json
{
  "facility_group_id": 2,
  "name": "Dining & Beverages",
  "description": "All dining outlets at this resort.",
  "sort_order": 1
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 10
}
```

---

## Get Resort Facility Group

`GET /api/v1/resorts/{resort-id}/resort-facility-groups/{id}`

### Path Parameters

| Parameter   | Type | Description                    |
|-------------|------|--------------------------------|
| `resort-id` | Long | ID of the resort               |
| `id`        | Long | ID of the resort facility group |

### Response `200 OK`

```json
{
  "data": {
    "id": 10,
    "resort_id": 1,
    "facility_group_id": 2,
    "name": "Dining & Beverages",
    "description": "All dining outlets at this resort.",
    "sort_order": 1
  }
}
```

---

## List Resort Facility Groups

`GET /api/v1/resorts/{resort-id}/resort-facility-groups`

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Query Parameters

| Parameter  | Type   | Default | Constraints                  | Description              |
|------------|--------|---------|------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                         | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                       | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `name`, `sortOrder`    | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 10,
      "resort_id": 1,
      "facility_group_id": 2,
      "name": "Dining & Beverages",
      "description": "All dining outlets at this resort.",
      "sort_order": 1
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

## Update Resort Facility Group

`PUT /api/v1/resorts/{resort-id}/resort-facility-groups/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter   | Type | Description                     |
|-------------|------|---------------------------------|
| `resort-id` | Long | ID of the resort                |
| `id`        | Long | ID of the resort facility group |

### Request Body

```json
{
  "name": "Food & Beverage",
  "sort_order": 2
}
```

| Field         | Type    | Required |
|---------------|---------|----------|
| `name`        | String  | No       |
| `description` | String  | No       |
| `sort_order`  | Integer | No       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 10
}
```

---

## Delete Resort Facility Group

`DELETE /api/v1/resorts/{resort-id}/resort-facility-groups/{id}`

Soft-deletes the record.

### Path Parameters

| Parameter   | Type | Description                     |
|-------------|------|---------------------------------|
| `resort-id` | Long | ID of the resort                |
| `id`        | Long | ID of the resort facility group |

### Response `200 OK`

```json
{
  "success": true,
  "id": 10
}
```

---

## Error Responses

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "Resort Facility Group with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                             |
|-------------|----------------------------|---------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data            |
| 404         | `ENTITY_NOT_FOUND`         | Resort, facility group, or record not found       |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                              |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                           |
