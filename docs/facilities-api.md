# Facilities API

Base URL: `/api/v1/facilities`

Facilities are individual amenities belonging to a facility group (e.g., a "Restaurant" under the "Dining" group). Each facility has a type and an optional icon. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                        | Description          |
|--------|-----------------------------|----------------------|
| POST   | `/api/v1/facilities`        | Create a facility    |
| GET    | `/api/v1/facilities`        | List all facilities  |
| GET    | `/api/v1/facilities/{id}`   | Get a facility       |
| PUT    | `/api/v1/facilities/{id}`   | Update a facility    |
| DELETE | `/api/v1/facilities/{id}`   | Delete a facility    |

---

## Data Model

| Field               | Type    | Required | Max Length | Description                                      |
|---------------------|---------|----------|------------|--------------------------------------------------|
| `facility_group_id` | Long    | Yes      | —          | ID of the parent facility group                  |
| `code`              | String  | Yes      | 50         | Short identifier code (e.g., `POOL_OUTDOOR`)     |
| `name`              | String  | Yes      | 255        | Display name of the facility                     |
| `description`       | String  | No       | unlimited  | Full description of the facility                 |
| `type`              | String  | Yes      | 30         | Category type (e.g., `INDOOR`, `OUTDOOR`)        |
| `icon`              | String  | No       | 255        | Icon identifier or URL for the facility          |

---

## Create Facility

`POST /api/v1/facilities`

### Request Body

```json
{
  "facility_group_id": 1,
  "code": "POOL_OUTDOOR",
  "name": "Outdoor Pool",
  "description": "A large outdoor swimming pool with sun loungers and a poolside bar.",
  "type": "OUTDOOR",
  "icon": "icon-pool"
}
```

| Field               | Type    | Required |
|---------------------|---------|----------|
| `facility_group_id` | Long    | Yes      |
| `code`              | String  | Yes      |
| `name`              | String  | Yes      |
| `description`       | String  | No       |
| `type`              | String  | Yes      |
| `icon`              | String  | No       |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Facility

`GET /api/v1/facilities/{id}`

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the facility   |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "facility_group_id": 1,
    "code": "POOL_OUTDOOR",
    "name": "Outdoor Pool",
    "description": "A large outdoor swimming pool with sun loungers and a poolside bar.",
    "type": "OUTDOOR",
    "icon": "icon-pool"
  }
}
```

---

## List All Facilities

`GET /api/v1/facilities`

Returns a paginated list of active (non-deleted) facilities.

### Query Parameters

| Parameter  | Type   | Default | Constraints           | Description              |
|------------|--------|---------|-----------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                  | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `name`  | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`         | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "facility_group_id": 1,
      "code": "POOL_OUTDOOR",
      "name": "Outdoor Pool",
      "description": "A large outdoor swimming pool with sun loungers and a poolside bar.",
      "type": "OUTDOOR",
      "icon": "icon-pool"
    },
    {
      "id": 2,
      "facility_group_id": 1,
      "code": "POOL_INDOOR",
      "name": "Indoor Pool",
      "description": "A heated indoor pool available year-round.",
      "type": "INDOOR",
      "icon": "icon-pool-indoor"
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

## Update Facility

`PUT /api/v1/facilities/{id}`

All fields are optional — only provided fields are updated. If `facility_group_id` is omitted, the existing group is kept.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the facility   |

### Request Body

```json
{
  "name": "Outdoor Infinity Pool",
  "icon": "icon-infinity-pool"
}
```

| Field               | Type    | Required |
|---------------------|---------|----------|
| `facility_group_id` | Long    | No       |
| `code`              | String  | No       |
| `name`              | String  | No       |
| `description`       | String  | No       |
| `type`              | String  | No       |
| `icon`              | String  | No       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Facility

`DELETE /api/v1/facilities/{id}`

Soft-deletes the facility. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the facility   |

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
  "message": "Facility with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                           |
|-------------|----------------------------|-------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data          |
| 404         | `ENTITY_NOT_FOUND`         | Facility or facility group not found            |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)      |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                         |
