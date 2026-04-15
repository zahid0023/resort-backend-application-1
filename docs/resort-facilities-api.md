# Resort Facilities API

Base URL: `/api/v1/resorts/{resort-id}/resort-facility-groups/{resort-facility-group-id}/resort-facilities`

Links platform facilities to a specific resort facility group. Supports single and bulk creation.

---

## Endpoints

| Method | Path                                                                                                                              | Description                        |
|--------|-----------------------------------------------------------------------------------------------------------------------------------|------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/resort-facility-groups/{resort-facility-group-id}/resort-facilities`                                | Create a resort facility           |
| POST   | `/api/v1/resorts/{resort-id}/resort-facility-groups/{resort-facility-group-id}/resort-facilities/bulk`                           | Bulk create resort facilities      |
| GET    | `/api/v1/resorts/{resort-id}/resort-facility-groups/{resort-facility-group-id}/resort-facilities`                                | List all resort facilities         |
| GET    | `/api/v1/resorts/{resort-id}/resort-facility-groups/{resort-facility-group-id}/resort-facilities/{id}`                           | Get a resort facility              |
| PUT    | `/api/v1/resorts/{resort-id}/resort-facility-groups/{resort-facility-group-id}/resort-facilities/{id}`                           | Update a resort facility           |
| DELETE | `/api/v1/resorts/{resort-id}/resort-facility-groups/{resort-facility-group-id}/resort-facilities/{id}`                           | Delete a resort facility           |

> `{resort-id}` is part of the URL for context but validation is scoped by `{resort-facility-group-id}`.

---

## Data Model

| Field                     | Type    | Required | Description                                      |
|---------------------------|---------|----------|--------------------------------------------------|
| `facility_id`             | Long    | Yes      | ID of the platform facility to link              |
| `name`                    | String  | No       | Custom display name (max 255 chars)              |
| `description`             | String  | No       | Custom description                               |
| `icon`                    | String  | No       | Icon identifier or URL (max 255 chars)           |
| `value`                   | String  | No       | Custom value or label (max 255 chars)            |

---

## Create Resort Facility

`POST .../resort-facilities`

### Request Body

```json
{
  "facility_id": 3,
  "name": "Outdoor Pool",
  "description": "Heated outdoor pool open year-round.",
  "icon": "icon-pool",
  "value": "24/7"
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 21
}
```

---

## Bulk Create Resort Facilities

`POST .../resort-facilities/bulk`

All facilities are created under the same resort facility group. Fails if any `facility_id` is not found.

### Request Body

```json
{
  "facilities": [
    {
      "facility_id": 3,
      "name": "Outdoor Pool",
      "icon": "icon-pool"
    },
    {
      "facility_id": 4,
      "name": "Gym",
      "icon": "icon-gym"
    },
    {
      "facility_id": 5,
      "name": "Sauna",
      "icon": "icon-sauna"
    }
  ]
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 0
}
```

---

## Get Resort Facility

`GET .../resort-facilities/{id}`

### Response `200 OK`

```json
{
  "data": {
    "id": 21,
    "resort_facility_group_id": 10,
    "facility_id": 3,
    "name": "Outdoor Pool",
    "description": "Heated outdoor pool open year-round.",
    "icon": "icon-pool",
    "value": "24/7"
  }
}
```

---

## List Resort Facilities

`GET .../resort-facilities`

### Query Parameters

| Parameter  | Type   | Default | Constraints     | Description              |
|------------|--------|---------|-----------------|--------------------------|
| `page`     | int    | `0`     | >= 0            | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50          | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `name`    | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`   | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 21,
      "resort_facility_group_id": 10,
      "facility_id": 3,
      "name": "Outdoor Pool",
      "icon": "icon-pool",
      "value": "24/7"
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

## Update Resort Facility

`PUT .../resort-facilities/{id}`

All fields optional — only provided fields are updated.

### Request Body

```json
{
  "name": "Heated Outdoor Pool",
  "value": "6am - 10pm"
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 21
}
```

---

## Delete Resort Facility

`DELETE .../resort-facilities/{id}`

Soft-deletes the record.

### Response `200 OK`

```json
{
  "success": true,
  "id": 21
}
```

---

## Error Responses

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "Facilitys not found for ids: [99, 101]"
}
```

| HTTP Status | Error Code                 | Cause                                                  |
|-------------|----------------------------|--------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data                 |
| 404         | `ENTITY_NOT_FOUND`         | Resort facility group, facility, or record not found   |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                                   |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                |
