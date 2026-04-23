# Facilities API

Base URL: `/api/v1/facilities`

Facilities are individual amenities belonging to a facility group (e.g., "Outdoor Pool" under the "Recreation" group). Each facility carries an optional icon — the same icon system used by facility groups (`LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`). All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                            | Description           |
|--------|---------------------------------|-----------------------|
| POST   | `/api/v1/facilities`            | Create a facility     |
| GET    | `/api/v1/facilities`            | List all facilities   |
| GET    | `/api/v1/facilities/{id}`       | Get a facility        |
| GET    | `/api/v1/facilities/icon-types` | List all icon types   |
| PUT    | `/api/v1/facilities/{id}`       | Update a facility     |
| DELETE | `/api/v1/facilities/{id}`       | Delete a facility     |

---

## Data Model

| Field               | Type    | Required | Constraints       | Description                                                    |
|---------------------|---------|----------|-------------------|----------------------------------------------------------------|
| `id`                | Long    | —        | read-only         | Auto-generated identifier                                      |
| `facility_group_id` | Long    | Yes      | —                 | ID of the parent facility group                                |
| `code`              | String  | Yes      | max 100 chars, unique | Short identifier (e.g., `POOL_OUTDOOR`)                   |
| `name`              | String  | Yes      | max 255 chars     | Display name of the facility                                   |
| `description`       | String  | No       | unlimited         | Full description of the facility                               |
| `icon_type`         | Enum    | Yes      | see values below  | Determines how `icon_value` is interpreted                     |
| `icon_value`        | String  | Yes*     | max 2000 chars    | Icon name, URL, or SVG content — required when `icon_type` is set |
| `icon_meta`         | Object  | No       | any key-value pairs | Optional rendering hints (size, color, alt, etc.)            |
| `sort_order`        | Integer | No       | >= 0              | Display order                                                  |

### `icon_type` values

| Value      | `icon_value` meaning                              | Typical `icon_meta` keys        |
|------------|---------------------------------------------------|---------------------------------|
| `LUCIDE`   | Lucide React icon name, e.g. `"Waves"`, `"Wifi"` | `size`, `color`, `stroke_width` |
| `IMAGE`    | Image URL or storage path                         | `alt`, `width`, `height`        |
| `SVG`      | Raw SVG string or SVG file URL                    | `viewBox`, `fill`               |
| `EXTERNAL` | Any external icon ID or URL                       | any custom metadata             |

---

## Get All Icon Types

`GET /api/v1/facilities/icon-types`

Returns all supported icon type values. Use this to populate dropdowns or validate `icon_type` on the client side.

### Response `200 OK`

```json
["LUCIDE", "IMAGE", "SVG", "EXTERNAL"]
```

---

## Create Facility

`POST /api/v1/facilities`

### Request Body

All four icon types are shown as separate examples.

**Lucide icon**
```json
{
  "facility_group_id": 1,
  "code": "POOL_OUTDOOR",
  "name": "Outdoor Pool",
  "description": "A large outdoor swimming pool with sun loungers and a poolside bar.",
  "icon_type": "LUCIDE",
  "icon_value": "Waves",
  "icon_meta": {
    "size": 24,
    "color": "#3b82f6",
    "stroke_width": 1.5
  },
  "sort_order": 1
}
```

**Image URL**
```json
{
  "facility_group_id": 2,
  "code": "SPA_TREATMENT",
  "name": "Spa Treatment Room",
  "description": "Private treatment room for massages and body therapies.",
  "icon_type": "IMAGE",
  "icon_value": "https://cdn.example.com/icons/spa.png",
  "icon_meta": {
    "alt": "Spa icon",
    "width": 48,
    "height": 48
  },
  "sort_order": 2
}
```

**Raw SVG**
```json
{
  "facility_group_id": 3,
  "code": "TENNIS_COURT",
  "name": "Tennis Court",
  "icon_type": "SVG",
  "icon_value": "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'><path d='M12 2a10 10 0 1 0 0 20A10 10 0 0 0 12 2z'/></svg>",
  "icon_meta": {
    "viewBox": "0 0 24 24",
    "fill": "currentColor"
  },
  "sort_order": 3
}
```

**External icon**
```json
{
  "facility_group_id": 4,
  "code": "TRANSPORT_SHUTTLE",
  "name": "Airport Shuttle",
  "icon_type": "EXTERNAL",
  "icon_value": "fontawesome:van-shuttle",
  "icon_meta": {
    "library": "fontawesome",
    "version": "6"
  },
  "sort_order": 4
}
```

### Request Fields

| Field               | Type    | Required | Validation                                            |
|---------------------|---------|----------|-------------------------------------------------------|
| `facility_group_id` | Long    | Yes      | Not null                                              |
| `code`              | String  | Yes      | Not blank, max 100 chars                              |
| `name`              | String  | Yes      | Not blank, max 255 chars                              |
| `description`       | String  | No       | —                                                     |
| `icon_type`         | Enum    | Yes      | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`          |
| `icon_value`        | String  | Yes*     | Required when `icon_type` is set, max 2000 chars      |
| `icon_meta`         | Object  | No       | Any JSON object                                       |
| `sort_order`        | Integer | No       | >= 0                                                  |

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

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the facility |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "facility_group_id": 1,
    "code": "POOL_OUTDOOR",
    "name": "Outdoor Pool",
    "description": "A large outdoor swimming pool with sun loungers and a poolside bar.",
    "icon_type": "LUCIDE",
    "icon_value": "Waves",
    "icon_meta": {
      "size": 24,
      "color": "#3b82f6",
      "stroke_width": 1.5
    },
    "sort_order": 1
  }
}
```

> Fields with `null` values are omitted from the response (`icon_meta`, `description`, and `sort_order` will be absent if not set).

---

## List All Facilities

`GET /api/v1/facilities`

Returns a paginated list of active (non-deleted) facilities.

### Query Parameters

| Parameter  | Type   | Default | Constraints                          | Description              |
|------------|--------|---------|--------------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                                 | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                               | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `name`, `sort_order`   | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                        | Sort direction           |

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
      "icon_type": "LUCIDE",
      "icon_value": "Waves",
      "icon_meta": {
        "size": 24,
        "color": "#3b82f6"
      },
      "sort_order": 1
    },
    {
      "id": 2,
      "facility_group_id": 1,
      "code": "POOL_INDOOR",
      "name": "Indoor Pool",
      "description": "A heated indoor pool available year-round.",
      "icon_type": "LUCIDE",
      "icon_value": "Droplets",
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

## Update Facility

`PUT /api/v1/facilities/{id}`

All fields are optional — only provided (non-null) fields are updated.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the facility |

### Request Body

```json
{
  "name": "Outdoor Infinity Pool",
  "icon_type": "LUCIDE",
  "icon_value": "Waves",
  "icon_meta": {
    "size": 28,
    "color": "#06b6d4"
  }
}
```

### Request Fields

| Field               | Type    | Required | Validation                                            |
|---------------------|---------|----------|-------------------------------------------------------|
| `facility_group_id` | Long    | No       | —                                                     |
| `code`              | String  | No       | Max 100 chars if provided                             |
| `name`              | String  | No       | Max 255 chars if provided                             |
| `description`       | String  | No       | —                                                     |
| `icon_type`         | Enum    | No       | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`          |
| `icon_value`        | String  | No*      | Required when `icon_type` is included, max 2000 chars |
| `icon_meta`         | Object  | No       | Any JSON object                                       |
| `sort_order`        | Integer | No       | >= 0 if provided                                      |

> **Note:** If you update `icon_type`, you must also include `icon_value` in the same request.

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

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the facility |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Validation Errors

When the request body fails validation, the API returns `400 Bad Request` with the following structure:

```json
{
  "request_id": "abc-123",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "errors": [
    {
      "field": "name",
      "message": "name must not be blank"
    },
    {
      "field": "icon_value",
      "message": "icon_value must not be blank when icon_type is provided"
    }
  ]
}
```

### Validation Rules Summary

| Field               | Create      | Update      | Rule                                                   |
|---------------------|-------------|-------------|--------------------------------------------------------|
| `facility_group_id` | Required    | Optional    | Not null                                               |
| `code`              | Required    | Optional    | Not blank; max 100 chars                               |
| `name`              | Required    | Optional    | Not blank; max 255 chars                               |
| `description`       | Optional    | Optional    | —                                                      |
| `icon_type`         | Required    | Optional    | Must be a valid enum value                             |
| `icon_value`        | Required    | Conditional | Required when `icon_type` is present; max 2000 chars   |
| `icon_meta`         | Optional    | Optional    | Any valid JSON object                                  |
| `sort_order`        | Optional    | Optional    | Must be >= 0                                           |

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

| HTTP Status | Error Code                 | Cause                                            |
|-------------|----------------------------|--------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request          |
| 404         | `ENTITY_NOT_FOUND`         | Facility or facility group not found             |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                             |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                          |
