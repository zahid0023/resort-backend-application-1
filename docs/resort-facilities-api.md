# Resort Facilities API

Base URL: `/api/v1/resorts/{resort-id}/resort-facility-groups/{resort-facility-group-id}/resort-facilities`

Links platform facilities to a specific resort facility group with optional name, description, sort order, and icon overrides. All records support soft-delete.

> `{resort-id}` is part of the URL for context — validation is scoped by `{resort-facility-group-id}`.

---

## Endpoints

| Method | Path                                                                                                                   | Description                |
|--------|------------------------------------------------------------------------------------------------------------------------|----------------------------|
| POST   | `.../resort-facilities`                                                                                                | Create a resort facility   |
| GET    | `.../resort-facilities`                                                                                                | List all resort facilities |
| GET    | `.../resort-facilities/{id}`                                                                                           | Get a resort facility      |
| PUT    | `.../resort-facilities/{id}`                                                                                           | Update a resort facility   |
| DELETE | `.../resort-facilities/{id}`                                                                                           | Delete a resort facility   |

---

## Data Model

| Field                     | Type    | Required | Constraints        | Description                                                        |
|---------------------------|---------|----------|--------------------|--------------------------------------------------------------------|
| `id`                      | Long    | —        | read-only          | Auto-generated identifier                                          |
| `resort_facility_group_id`| Long    | —        | read-only          | ID of the parent resort facility group                             |
| `facility_id`             | Long    | Yes      | —                  | ID of the platform facility to link                                |
| `name`                    | String  | No       | max 255 chars      | Custom display name override                                       |
| `description`             | String  | No       | unlimited          | Custom description override                                        |
| `sort_order`              | Integer | Yes      | >= 0, default `1`  | Display order                                                      |
| `icon_type`               | String  | No       | max 100 chars      | Icon type override (e.g. `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`)     |
| `icon_value`              | String  | No       | max 2000 chars     | Icon value override — name, URL, or SVG content                    |
| `icon_meta`               | Object  | No       | any key-value pairs| Optional rendering hints (size, color, alt, etc.)                  |

> `name`, `description`, `icon_type`, `icon_value`, and `icon_meta` are optional overrides. If omitted, the frontend falls back to the values defined on the base facility.

---

## Create Resort Facility

`POST .../resort-facilities`

### Path Parameters

| Parameter                   | Type | Description                     |
|-----------------------------|------|---------------------------------|
| `resort-id`                 | Long | ID of the resort                |
| `resort-facility-group-id`  | Long | ID of the resort facility group |

### Request Body

```json
{
  "facility_id": 3,
  "sort_order": 1,
  "name": "Outdoor Pool",
  "description": "Heated outdoor pool open year-round.",
  "icon_type": "LUCIDE",
  "icon_value": "Waves",
  "icon_meta": {
    "size": 24,
    "color": "#3b82f6"
  }
}
```

### Request Fields

| Field         | Type    | Required | Validation                                  |
|---------------|---------|----------|---------------------------------------------|
| `facility_id` | Long    | Yes      | Must reference an existing active facility  |
| `sort_order`  | Integer | Yes      | Not null, >= 0                              |
| `name`        | String  | No       | Max 255 chars                               |
| `description` | String  | No       | —                                           |
| `icon_type`   | String  | No       | Max 100 chars                               |
| `icon_value`  | String  | No       | Max 2000 chars                              |
| `icon_meta`   | Object  | No       | Any JSON object                             |

### Response `201 Created`

```json
{
  "success": true,
  "id": 21
}
```

---

## Get Resort Facility

`GET .../resort-facilities/{id}`

### Path Parameters

| Parameter                   | Type | Description                     |
|-----------------------------|------|---------------------------------|
| `resort-id`                 | Long | ID of the resort                |
| `resort-facility-group-id`  | Long | ID of the resort facility group |
| `id`                        | Long | ID of the resort facility       |

### Response `200 OK`

```json
{
  "data": {
    "id": 21,
    "resort_facility_group_id": 10,
    "facility_id": 3,
    "name": "Outdoor Pool",
    "description": "Heated outdoor pool open year-round.",
    "sort_order": 1,
    "icon_type": "LUCIDE",
    "icon_value": "Waves",
    "icon_meta": {
      "size": 24,
      "color": "#3b82f6"
    }
  }
}
```

> Fields with `null` values are omitted from the response (`name`, `description`, `icon_type`, `icon_value`, `icon_meta` will be absent if not set).

---

## List Resort Facilities

`GET .../resort-facilities`

### Path Parameters

| Parameter                   | Type | Description                     |
|-----------------------------|------|---------------------------------|
| `resort-id`                 | Long | ID of the resort                |
| `resort-facility-group-id`  | Long | ID of the resort facility group |

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
      "id": 21,
      "resort_facility_group_id": 10,
      "facility_id": 3,
      "name": "Outdoor Pool",
      "description": "Heated outdoor pool open year-round.",
      "sort_order": 1,
      "icon_type": "LUCIDE",
      "icon_value": "Waves",
      "icon_meta": {
        "size": 24,
        "color": "#3b82f6"
      }
    },
    {
      "id": 22,
      "resort_facility_group_id": 10,
      "facility_id": 4,
      "sort_order": 2
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

## Update Resort Facility

`PUT .../resort-facilities/{id}`

All fields are optional — only provided (non-null) fields are updated.

### Path Parameters

| Parameter                   | Type | Description                     |
|-----------------------------|------|---------------------------------|
| `resort-id`                 | Long | ID of the resort                |
| `resort-facility-group-id`  | Long | ID of the resort facility group |
| `id`                        | Long | ID of the resort facility       |

### Request Body

```json
{
  "name": "Heated Outdoor Pool",
  "sort_order": 2,
  "icon_type": "LUCIDE",
  "icon_value": "Droplets",
  "icon_meta": {
    "size": 20,
    "color": "#0ea5e9"
  }
}
```

### Request Fields

| Field         | Type    | Required | Validation      |
|---------------|---------|----------|-----------------|
| `name`        | String  | No       | Max 255 chars   |
| `description` | String  | No       | —               |
| `sort_order`  | Integer | No       | >= 0            |
| `icon_type`   | String  | No       | Max 100 chars   |
| `icon_value`  | String  | No       | Max 2000 chars  |
| `icon_meta`   | Object  | No       | Any JSON object |

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

Soft-deletes the record. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter                   | Type | Description                     |
|-----------------------------|------|---------------------------------|
| `resort-id`                 | Long | ID of the resort                |
| `resort-facility-group-id`  | Long | ID of the resort facility group |
| `id`                        | Long | ID of the resort facility       |

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
  "message": "Resort Facility with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                                          |
|-------------|----------------------------|----------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations               |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request                        |
| 404         | `ENTITY_NOT_FOUND`         | Resort facility group, facility, or resort facility not found  |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                                           |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                        |
