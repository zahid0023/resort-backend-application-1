# Facility Groups API

Base URL: `/api/v1/facility-groups`

Facility groups categorize amenities and facilities offered by a resort (e.g., Dining, Recreation, Wellness). Each group carries an icon that the frontend uses to render a visual indicator — the icon can be a Lucide React icon name, an image URL, a raw SVG, or any external icon identifier. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                        | Description               |
|--------|---------------------------------------------|---------------------------|
| POST   | `/api/v1/facility-groups`                   | Create a facility group   |
| GET    | `/api/v1/facility-groups`                   | List all facility groups  |
| GET    | `/api/v1/facility-groups/{id}`              | Get a facility group      |
| GET    | `/api/v1/facility-groups/icon-types`        | List all icon types       |
| PUT    | `/api/v1/facility-groups/{id}`              | Update a facility group   |
| DELETE | `/api/v1/facility-groups/{id}`              | Delete a facility group   |

---

## Data Model

| Field         | Type                                      | Required | Constraints        | Description                                              |
|---------------|-------------------------------------------|----------|--------------------|----------------------------------------------------------|
| `id`          | Long                                      | —        | read-only          | Auto-generated identifier                                |
| `code`        | String                                    | Yes      | max 100 chars      | Short unique identifier (e.g., `DINING`)                 |
| `name`        | String                                    | Yes      | max 255 chars      | Display name of the group                                |
| `description` | String                                    | No       | unlimited          | Full description of the facility group                   |
| `sort_order`  | Integer                                   | No       | >= 0, default `0`  | Display order                                            |
| `icon_type`   | Enum                                      | Yes      | see values below   | Determines how `icon_value` is interpreted               |
| `icon_value`  | String                                    | Yes*     | max 2000 chars     | Icon name, URL, or SVG content — required if `icon_type` is set |
| `icon_meta`   | Object                                    | No       | any key-value pairs | Optional rendering hints (size, color, alt, etc.)       |

### `icon_type` values

| Value      | `icon_value` meaning                            | Typical `icon_meta` keys              |
|------------|-------------------------------------------------|---------------------------------------|
| `LUCIDE`   | Lucide React icon name, e.g. `"Waves"`, `"Wifi"` | `size`, `color`, `stroke_width`       |
| `IMAGE`    | Image URL or storage path                       | `alt`, `width`, `height`              |
| `SVG`      | Raw SVG string or SVG file URL                  | `viewBox`, `fill`                     |
| `EXTERNAL` | Any external icon ID or URL                     | any custom metadata                   |

---

## Get All Icon Types

`GET /api/v1/facility-groups/icon-types`

Returns all supported icon type values. Use this to populate dropdowns or validate `icon_type` on the client side.

### Response `200 OK`

```json
["LUCIDE", "IMAGE", "SVG", "EXTERNAL"]
```

---

## Create Facility Group

`POST /api/v1/facility-groups`

### Request Body

All four icon types are shown as separate examples.

**Lucide icon**
```json
{
  "code": "DINING",
  "name": "Dining",
  "description": "All food and beverage outlets including restaurants, bars, and room service.",
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "UtensilsCrossed",
  "icon_meta": {
    "size": 24,
    "color": "#f59e0b",
    "stroke_width": 1.5
  }
}
```

**Image URL**
```json
{
  "code": "WELLNESS",
  "name": "Wellness",
  "description": "Spa, fitness center, and wellness treatment facilities.",
  "sort_order": 2,
  "icon_type": "IMAGE",
  "icon_value": "https://cdn.example.com/icons/wellness.png",
  "icon_meta": {
    "alt": "Wellness icon",
    "width": 48,
    "height": 48
  }
}
```

**Raw SVG**
```json
{
  "code": "RECREATION",
  "name": "Recreation",
  "sort_order": 3,
  "icon_type": "SVG",
  "icon_value": "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'><path d='M12 2a10 10 0 1 0 0 20A10 10 0 0 0 12 2z'/></svg>",
  "icon_meta": {
    "viewBox": "0 0 24 24",
    "fill": "currentColor"
  }
}
```

**External icon**
```json
{
  "code": "TRANSPORT",
  "name": "Transport",
  "sort_order": 4,
  "icon_type": "EXTERNAL",
  "icon_value": "fontawesome:car",
  "icon_meta": {
    "library": "fontawesome",
    "version": "6"
  }
}
```

### Request Fields

| Field         | Type    | Required | Validation                                           |
|---------------|---------|----------|------------------------------------------------------|
| `code`        | String  | Yes      | Not blank, max 100 chars                             |
| `name`        | String  | Yes      | Not blank, max 255 chars                             |
| `description` | String  | No       | —                                                    |
| `sort_order`  | Integer | No       | >= 0                                                 |
| `icon_type`   | Enum    | Yes      | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`         |
| `icon_value`  | String  | Yes*     | Required when `icon_type` is set, max 2000 chars     |
| `icon_meta`   | Object  | No       | Any JSON object                                      |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Facility Group

`GET /api/v1/facility-groups/{id}`

### Path Parameters

| Parameter | Type | Description               |
|-----------|------|---------------------------|
| `id`      | Long | ID of the facility group  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "DINING",
    "name": "Dining",
    "description": "All food and beverage outlets including restaurants, bars, and room service.",
    "sort_order": 1,
    "icon_type": "LUCIDE",
    "icon_value": "UtensilsCrossed",
    "icon_meta": {
      "size": 24,
      "color": "#f59e0b",
      "stroke_width": 1.5
    }
  }
}
```

> Fields with `null` values are omitted from the response (`icon_meta` will be absent if not set).

---

## List All Facility Groups

`GET /api/v1/facility-groups`

Returns a paginated list of active (non-deleted) facility groups.

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
      "code": "DINING",
      "name": "Dining",
      "description": "All food and beverage outlets including restaurants, bars, and room service.",
      "sort_order": 1,
      "icon_type": "LUCIDE",
      "icon_value": "UtensilsCrossed",
      "icon_meta": {
        "size": 24,
        "color": "#f59e0b"
      }
    },
    {
      "id": 2,
      "code": "WELLNESS",
      "name": "Wellness",
      "description": "Spa, fitness center, and wellness treatment facilities.",
      "sort_order": 2,
      "icon_type": "IMAGE",
      "icon_value": "https://cdn.example.com/icons/wellness.png",
      "icon_meta": {
        "alt": "Wellness icon",
        "width": 48,
        "height": 48
      }
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

## Update Facility Group

`PUT /api/v1/facility-groups/{id}`

All fields are optional — only provided (non-null) fields are updated.

### Path Parameters

| Parameter | Type | Description               |
|-----------|------|---------------------------|
| `id`      | Long | ID of the facility group  |

### Request Body

```json
{
  "name": "Food & Beverage",
  "icon_type": "LUCIDE",
  "icon_value": "ChefHat",
  "icon_meta": {
    "size": 20,
    "color": "#ef4444"
  }
}
```

### Request Fields

| Field         | Type    | Required | Validation                                           |
|---------------|---------|----------|------------------------------------------------------|
| `code`        | String  | No       | Max 100 chars if provided                            |
| `name`        | String  | No       | Max 255 chars if provided                            |
| `description` | String  | No       | —                                                    |
| `sort_order`  | Integer | No       | >= 0 if provided                                     |
| `icon_type`   | Enum    | No       | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`         |
| `icon_value`  | String  | No*      | Required when `icon_type` is included, max 2000 chars |
| `icon_meta`   | Object  | No       | Any JSON object                                      |

> **Note:** If you update `icon_type`, you must also include `icon_value` in the same request.

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Facility Group

`DELETE /api/v1/facility-groups/{id}`

Soft-deletes the facility group. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description               |
|-----------|------|---------------------------|
| `id`      | Long | ID of the facility group  |

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
      "field": "code",
      "message": "code must not be blank"
    },
    {
      "field": "icon_value",
      "message": "icon_value must not be blank when icon_type is provided"
    }
  ]
}
```

### Validation Rules Summary

| Field        | Create        | Update        | Rule                                                   |
|--------------|---------------|---------------|--------------------------------------------------------|
| `code`       | Required      | Optional      | Not blank; max 100 chars                               |
| `name`       | Required      | Optional      | Not blank; max 255 chars                               |
| `sort_order` | Optional      | Optional      | Must be >= 0                                           |
| `icon_type`  | Required      | Optional      | Must be a valid enum value                             |
| `icon_value` | Required      | Conditional   | Required when `icon_type` is present; max 2000 chars   |
| `icon_meta`  | Optional      | Optional      | Any valid JSON object                                  |

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "Facility Group with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                            |
|-------------|----------------------------|--------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request          |
| 404         | `ENTITY_NOT_FOUND`         | Facility group not found or already deleted      |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)       |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                          |
