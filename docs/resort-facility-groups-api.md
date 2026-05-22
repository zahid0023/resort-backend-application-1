# Resort Facility Groups API

Base URL: `/api/v1/resorts/{resort-id}/resort-facility-groups`

Links a facility group to a specific resort with a custom name, description, sort order, and optional icon override. All records support soft-delete.

---

## Endpoints

| Method | Path                                                              | Description                     |
|--------|-------------------------------------------------------------------|---------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/resort-facility-groups`             | Create a resort facility group  |
| GET    | `/api/v1/resorts/{resort-id}/resort-facility-groups`             | List all resort facility groups |
| GET    | `/api/v1/resorts/{resort-id}/resort-facility-groups/{id}`        | Get a resort facility group     |
| PUT    | `/api/v1/resorts/{resort-id}/resort-facility-groups/{id}`        | Update a resort facility group  |
| DELETE | `/api/v1/resorts/{resort-id}/resort-facility-groups/{id}`        | Delete a resort facility group  |

---

## Data Model

| Field               | Type    | Required | Constraints       | Description                                                      |
|---------------------|---------|----------|-------------------|------------------------------------------------------------------|
| `id`                | Long    | —        | read-only         | Auto-generated identifier                                        |
| `resort_id`         | Long    | —        | read-only         | ID of the parent resort                                          |
| `facility_group_id` | Long    | Yes      | —                 | ID of the facility group to link                                 |
| `name`              | String  | Yes      | max 255 chars     | Display name for this resort's version of the group              |
| `description`       | String  | Yes      | unlimited         | Description of this group for this resort                        |
| `sort_order`        | Integer | Yes      | >= 0              | Display order                                                    |
| `icon_type`         | String  | No       | max 100 chars     | Icon type override (e.g. `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`)   |
| `icon_value`        | String  | No       | max 2000 chars    | Icon value override — name, URL, or SVG content                  |
| `icon_meta`         | Object  | No       | any key-value pairs | Optional rendering hints (size, color, alt, etc.)              |

> `icon_type`, `icon_value`, and `icon_meta` are optional overrides. If omitted, the frontend falls back to the icon defined on the base facility group.

---

## Create Resort Facility Group

`POST /api/v1/resorts/{resort-id}/resort-facility-groups`

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Request Body

```json
{
  "facility_group_id": 2,
  "name": "Dining & Beverages",
  "description": "All dining outlets at this resort.",
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "UtensilsCrossed",
  "icon_meta": {
    "size": 24,
    "color": "#f59e0b"
  }
}
```

### Request Fields

| Field               | Type    | Required | Validation                        |
|---------------------|---------|----------|-----------------------------------|
| `facility_group_id` | Long    | Yes      | Must reference an existing group  |
| `name`              | String  | Yes      | Not blank, max 255 chars          |
| `description`       | String  | Yes      | Not null                          |
| `sort_order`        | Integer | Yes      | Not null, >= 0                    |
| `icon_type`         | String  | No       | Max 100 chars                     |
| `icon_value`        | String  | No       | Max 2000 chars                    |
| `icon_meta`         | Object  | No       | Any JSON object                   |

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

| Parameter   | Type | Description                     |
|-------------|------|---------------------------------|
| `resort-id` | Long | ID of the resort                |
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
    "sort_order": 1,
    "icon_type": "LUCIDE",
    "icon_value": "UtensilsCrossed",
    "icon_meta": {
      "size": 24,
      "color": "#f59e0b"
    }
  }
}
```

> Fields with `null` values are omitted from the response (`icon_type`, `icon_value`, `icon_meta` will be absent if not set).

---

## List Resort Facility Groups

`GET /api/v1/resorts/{resort-id}/resort-facility-groups`

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Query Parameters

| Parameter  | Type   | Default | Constraints               | Description              |
|------------|--------|---------|---------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                      | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                    | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `name`, `sortOrder` | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`             | Sort direction           |

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
      "sort_order": 1,
      "icon_type": "LUCIDE",
      "icon_value": "UtensilsCrossed",
      "icon_meta": {
        "size": 24,
        "color": "#f59e0b"
      }
    },
    {
      "id": 11,
      "resort_id": 1,
      "facility_group_id": 3,
      "name": "Wellness & Spa",
      "description": "Spa and wellness facilities.",
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

## Update Resort Facility Group

`PUT /api/v1/resorts/{resort-id}/resort-facility-groups/{id}`

All fields are optional — only provided (non-null) fields are updated.

### Path Parameters

| Parameter   | Type | Description                     |
|-------------|------|---------------------------------|
| `resort-id` | Long | ID of the resort                |
| `id`        | Long | ID of the resort facility group |

### Request Body

```json
{
  "name": "Food & Beverage",
  "sort_order": 2,
  "icon_type": "LUCIDE",
  "icon_value": "ChefHat",
  "icon_meta": {
    "size": 20,
    "color": "#ef4444"
  }
}
```

### Request Fields

| Field         | Type    | Required | Validation            |
|---------------|---------|----------|-----------------------|
| `name`        | String  | No       | Max 255 chars         |
| `description` | String  | No       | —                     |
| `sort_order`  | Integer | No       | >= 0                  |
| `icon_type`   | String  | No       | Max 100 chars         |
| `icon_value`  | String  | No       | Max 2000 chars        |
| `icon_meta`   | Object  | No       | Any JSON object       |

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

Soft-deletes the record. The record is not removed from the database but will no longer appear in any response.

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

| HTTP Status | Error Code                 | Cause                                                        |
|-------------|----------------------------|--------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations             |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request                      |
| 404         | `ENTITY_NOT_FOUND`         | Resort, facility group, or resort facility group not found   |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                                         |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                      |
