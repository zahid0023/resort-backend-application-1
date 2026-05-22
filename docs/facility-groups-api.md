# Facility Groups API

Base URL: `/api/v1/facility-groups`

Facility groups categorize amenities and facilities offered by a resort (e.g., Dining, Recreation, Wellness). Each group
carries an icon that the frontend uses to render a visual indicator. Display names and descriptions are managed per
locale via the locale sub-resource. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                       | Description                      |
|--------|------------------------------------------------------------|----------------------------------|
| POST   | `/api/v1/facility-groups`                                  | Create a facility group          |
| GET    | `/api/v1/facility-groups`                                  | List all facility groups         |
| GET    | `/api/v1/facility-groups/{id}`                             | Get a facility group             |
| GET    | `/api/v1/facility-groups/icon-types`                       | List all icon types              |
| PUT    | `/api/v1/facility-groups/{id}`                             | Update a facility group          |
| DELETE | `/api/v1/facility-groups/{id}`                             | Delete a facility group          |
| POST   | `/api/v1/facility-groups/{facility-group-id}/locales`      | Add a locale to a facility group |
| PUT    | `/api/v1/facility-groups/{facility-group-id}/locales/{id}` | Update a facility group locale   |
| DELETE | `/api/v1/facility-groups/{facility-group-id}/locales/{id}` | Delete a facility group locale   |

---

## Data Model

### Facility Group

| Field        | Type    | Required | Constraints         | Description                                                             |
|--------------|---------|----------|---------------------|-------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only           | Auto-generated identifier                                               |
| `code`       | String  | Yes      | max 100 chars       | Short unique identifier (e.g., `DINING`). Not updatable after creation. |
| `sort_order` | Integer | No       | >= 0, default `1`   | Display order                                                           |
| `icon_type`  | Enum    | Yes      | see values below    | Determines how `icon_value` is interpreted                              |
| `icon_value` | String  | No       | max 2000 chars      | Icon name or URL                                                        |
| `icon_meta`  | Object  | No       | any key-value pairs | Optional rendering hints (size, color, etc.)                            |
| `locales`    | Array   | No       | —                   | Locale entries (included in all responses)                              |

### Facility Group Locale

| Field         | Type    | Required | Constraints   | Description                                       |
|---------------|---------|----------|---------------|---------------------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier                         |
| `locale_id`   | Long    | Yes      | must exist    | ID of the locale. Not updatable after creation.   |
| `name`        | String  | Yes      | max 255 chars | Display name of the facility group in this locale |
| `description` | String  | No       | unlimited     | Full description in this locale                   |
| `sort_order`  | Integer | Yes      | —             | Display order for this locale entry               |

### `icon_type` values

| Value    | `icon_value` meaning                             | Typical `icon_meta` keys        |
|----------|--------------------------------------------------|---------------------------------|
| `LUCIDE` | Lucide React icon name, e.g. `"Waves"`, `"Wifi"` | `size`, `color`, `stroke_width` |
| `IMAGE`  | Image URL or storage path                        | `alt`, `width`, `height`        |

---

## Get All Icon Types

`GET /api/v1/facility-groups/icon-types`

Returns all supported icon type values. Use this to populate dropdowns or validate `icon_type` on the client side.

### Response `200 OK`

```json
[
  "LUCIDE",
  "IMAGE"
]
```

---

## Create Facility Group

`POST /api/v1/facility-groups`

### Request Body

```json
{
  "code": "DINING",
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "UtensilsCrossed",
  "icon_meta": {
    "size": 24,
    "color": "#f59e0b",
    "stroke_width": 1.5
  },
  "locales": [
    {
      "locale_id": 1,
      "name": "Dining",
      "description": "All food and beverage outlets including restaurants, bars, and room service.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "ডাইনিং",
      "description": "রেস্তোরাঁ, বার এবং রুম সার্ভিস সহ সকল খাবার ও পানীয় আউটলেট।",
      "sort_order": 2
    }
  ]
}
```

### Request Fields

| Field        | Type    | Required | Validation                |
|--------------|---------|----------|---------------------------|
| `code`       | String  | Yes      | Not blank, max 100 chars  |
| `sort_order` | Integer | No       | >= 0                      |
| `icon_type`  | Enum    | Yes      | One of: `LUCIDE`, `IMAGE` |
| `icon_value` | String  | No       | Max 2000 chars            |
| `icon_meta`  | Object  | No       | Any JSON object           |
| `locales`    | Array   | No       | See locale fields below   |

**Locale fields (each item in `locales`):**

| Field         | Type    | Required | Validation                   |
|---------------|---------|----------|------------------------------|
| `locale_id`   | Long    | Yes      | Must refer to a valid locale |
| `name`        | String  | Yes      | Not blank, max 255 chars     |
| `description` | String  | No       | —                            |
| `sort_order`  | Integer | Yes      | —                            |

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

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility group |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "DINING",
    "sort_order": 1,
    "icon_type": "LUCIDE",
    "icon_value": "UtensilsCrossed",
    "icon_meta": {
      "size": 24,
      "color": "#f59e0b",
      "stroke_width": 1.5
    },
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Dining",
        "description": "All food and beverage outlets including restaurants, bars, and room service.",
        "sort_order": 1
      },
      {
        "id": 2,
        "locale_id": 2,
        "name": "ডাইনিং",
        "description": "রেস্তোরাঁ, বার এবং রুম সার্ভিস সহ সকল খাবার ও পানীয় আউটলেট।",
        "sort_order": 2
      }
    ]
  }
}
```

> Fields with `null` values are omitted from the response (`icon_meta` will be absent if not set).

---

## List All Facility Groups

`GET /api/v1/facility-groups`

Returns a paginated list of active (non-deleted) facility groups including their locales.

### Query Parameters

| Parameter  | Type   | Default | Constraints                              | Description              |
|------------|--------|---------|------------------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                                     | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                                   | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `sort_order`, `created_at` | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                            | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "DINING",
      "sort_order": 1,
      "icon_type": "LUCIDE",
      "icon_value": "UtensilsCrossed",
      "icon_meta": {
        "size": 24,
        "color": "#f59e0b"
      },
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Dining",
          "description": "All food and beverage outlets including restaurants, bars, and room service.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "code": "WELLNESS",
      "sort_order": 2,
      "icon_type": "IMAGE",
      "icon_value": "https://cdn.example.com/icons/wellness.png",
      "icon_meta": {
        "alt": "Wellness icon",
        "width": 48,
        "height": 48
      },
      "locales": [
        {
          "id": 3,
          "locale_id": 1,
          "name": "Wellness",
          "description": "Spa, fitness center, and wellness treatment facilities.",
          "sort_order": 1
        }
      ]
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

Updates the facility group. `code` is not updatable. Use the locale sub-resource to manage `name` and `description`.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility group |

### Request Body

```json
{
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "ChefHat",
  "icon_meta": {
    "size": 20,
    "color": "#ef4444"
  }
}
```

### Request Fields

| Field        | Type    | Required | Validation                |
|--------------|---------|----------|---------------------------|
| `sort_order` | Integer | No       | >= 0 if provided          |
| `icon_type`  | Enum    | Yes      | One of: `LUCIDE`, `IMAGE` |
| `icon_value` | String  | No       | Max 2000 chars            |
| `icon_meta`  | Object  | No       | Any JSON object           |

> **Note:** `code` is immutable and cannot be changed after creation.

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

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility group |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Add Locale

`POST /api/v1/facility-groups/{facility-group-id}/locales`

Adds a locale entry to an existing facility group.

### Path Parameters

| Parameter           | Type | Description              |
|---------------------|------|--------------------------|
| `facility-group-id` | Long | ID of the facility group |

### Request Body

```json
{
  "locale_id": 1,
  "name": "Dining",
  "description": "All food and beverage outlets including restaurants, bars, and room service.",
  "sort_order": 1
}
```

### Request Fields

| Field         | Type    | Required | Validation                   |
|---------------|---------|----------|------------------------------|
| `locale_id`   | Long    | Yes      | Must refer to a valid locale |
| `name`        | String  | Yes      | Not blank, max 255 chars     |
| `description` | String  | No       | —                            |
| `sort_order`  | Integer | Yes      | —                            |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Update Locale

`PUT /api/v1/facility-groups/{facility-group-id}/locales/{id}`

Updates an existing locale entry. `locale_id` is not updatable.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the facility group        |
| `id`                | Long | ID of the facility group locale |

### Request Body

```json
{
  "name": "Food & Beverage",
  "description": "Updated description.",
  "sort_order": 1
}
```

### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | —                        |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Locale

`DELETE /api/v1/facility-groups/{facility-group-id}/locales/{id}`

Soft-deletes the locale entry. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the facility group        |
| `id`                | Long | ID of the facility group locale |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Error Responses

| HTTP Status | Error Code                 | Cause                                            |
|-------------|----------------------------|--------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request          |
| 404         | `ENTITY_NOT_FOUND`         | Facility group or locale not found / deleted     |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)       |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                          |

---

---

# Facilities API

Base URL: `/api/v1/facility-group/{facility-group-id}/facilities`

Facilities are individual amenities or services that belong to a facility group (e.g., "Pool Bar" under "Dining", "
Sauna" under "Wellness"). Like facility groups, each facility carries an icon and supports soft-delete.

---

## Endpoints

| Method | Path                                                               | Description         |
|--------|--------------------------------------------------------------------|---------------------|
| POST   | `/api/v1/facility-group/{facility-group-id}/facilities`            | Create a facility   |
| GET    | `/api/v1/facility-group/{facility-group-id}/facilities`            | List all facilities |
| GET    | `/api/v1/facility-group/{facility-group-id}/facilities/{id}`       | Get a facility      |
| GET    | `/api/v1/facility-group/{facility-group-id}/facilities/icon-types` | List all icon types |
| PUT    | `/api/v1/facility-group/{facility-group-id}/facilities/{id}`       | Update a facility   |
| DELETE | `/api/v1/facility-group/{facility-group-id}/facilities/{id}`       | Delete a facility   |

---

## Data Model

| Field               | Type    | Required | Constraints         | Description                                                       |
|---------------------|---------|----------|---------------------|-------------------------------------------------------------------|
| `id`                | Long    | —        | read-only           | Auto-generated identifier                                         |
| `facility_group_id` | Long    | —        | read-only           | ID of the parent facility group                                   |
| `code`              | String  | Yes      | max 100 chars       | Short unique identifier (e.g., `POOL_BAR`)                        |
| `name`              | String  | Yes      | max 255 chars       | Display name of the facility                                      |
| `description`       | String  | No       | unlimited           | Full description of the facility                                  |
| `sort_order`        | Integer | No       | >= 0, default `1`   | Display order                                                     |
| `icon_type`         | Enum    | Yes      | see values below    | Determines how `icon_value` is interpreted                        |
| `icon_value`        | String  | Yes*     | max 2000 chars      | Icon name, URL, or SVG content — required when `icon_type` is set |
| `icon_meta`         | Object  | No       | any key-value pairs | Optional rendering hints (size, color, alt, etc.)                 |

### `icon_type` values

| Value      | `icon_value` meaning                             | Typical `icon_meta` keys        |
|------------|--------------------------------------------------|---------------------------------|
| `LUCIDE`   | Lucide React icon name, e.g. `"Waves"`, `"Wifi"` | `size`, `color`, `stroke_width` |
| `IMAGE`    | Image URL or storage path                        | `alt`, `width`, `height`        |
| `SVG`      | Raw SVG string or SVG file URL                   | `viewBox`, `fill`               |
| `EXTERNAL` | Any external icon ID or URL                      | any custom metadata             |

---

## Get All Icon Types

`GET /api/v1/facility-group/{facility-group-id}/facilities/icon-types`

Returns all supported icon type values.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |

### Response `200 OK`

```json
[
  "LUCIDE",
  "IMAGE",
  "SVG",
  "EXTERNAL"
]
```

---

## Create Facility

`POST /api/v1/facility-group/{facility-group-id}/facilities`

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |

### Request Body

```json
{
  "code": "POOL_BAR",
  "name": "Pool Bar",
  "description": "Outdoor bar serving refreshments by the pool.",
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "GlassWater",
  "icon_meta": {
    "size": 24,
    "color": "#3b82f6",
    "stroke_width": 1.5
  }
}
```

### Request Fields

| Field         | Type    | Required | Validation                                       |
|---------------|---------|----------|--------------------------------------------------|
| `code`        | String  | Yes      | Not blank, max 100 chars                         |
| `name`        | String  | Yes      | Not blank, max 255 chars                         |
| `description` | String  | No       | —                                                |
| `sort_order`  | Integer | No       | >= 0; defaults to `1` if omitted                 |
| `icon_type`   | Enum    | Yes      | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`     |
| `icon_value`  | String  | Yes*     | Required when `icon_type` is set, max 2000 chars |
| `icon_meta`   | Object  | No       | Any JSON object                                  |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Facility

`GET /api/v1/facility-group/{facility-group-id}/facilities/{id}`

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |
| `id`                | Long | ID of the facility              |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "facility_group_id": 3,
    "code": "POOL_BAR",
    "name": "Pool Bar",
    "description": "Outdoor bar serving refreshments by the pool.",
    "sort_order": 1,
    "icon_type": "LUCIDE",
    "icon_value": "GlassWater",
    "icon_meta": {
      "size": 24,
      "color": "#3b82f6",
      "stroke_width": 1.5
    }
  }
}
```

> Fields with `null` values are omitted from the response (`icon_meta` will be absent if not set).

---

## List All Facilities

`GET /api/v1/facility-group/{facility-group-id}/facilities`

Returns a paginated list of active (non-deleted) facilities belonging to the given facility group.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |

### Query Parameters

| Parameter  | Type   | Default | Constraints                        | Description              |
|------------|--------|---------|------------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                               | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                             | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `name`, `sort_order` | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                      | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "facility_group_id": 3,
      "code": "POOL_BAR",
      "name": "Pool Bar",
      "description": "Outdoor bar serving refreshments by the pool.",
      "sort_order": 1,
      "icon_type": "LUCIDE",
      "icon_value": "GlassWater",
      "icon_meta": {
        "size": 24,
        "color": "#3b82f6"
      }
    },
    {
      "id": 2,
      "facility_group_id": 3,
      "code": "RESTAURANT",
      "name": "Main Restaurant",
      "description": "Full-service restaurant with buffet and à la carte options.",
      "sort_order": 2,
      "icon_type": "LUCIDE",
      "icon_value": "UtensilsCrossed",
      "icon_meta": {
        "size": 24,
        "color": "#f59e0b"
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

## Update Facility

`PUT /api/v1/facility-group/{facility-group-id}/facilities/{id}`

All fields are optional — only provided (non-null) fields are updated.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |
| `id`                | Long | ID of the facility              |

### Request Body

```json
{
  "name": "Poolside Bar",
  "icon_type": "LUCIDE",
  "icon_value": "Beer",
  "icon_meta": {
    "size": 20,
    "color": "#f97316"
  }
}
```

### Request Fields

| Field         | Type    | Required | Validation                                            |
|---------------|---------|----------|-------------------------------------------------------|
| `code`        | String  | No       | Max 100 chars if provided                             |
| `name`        | String  | No       | Max 255 chars if provided                             |
| `description` | String  | No       | —                                                     |
| `sort_order`  | Integer | No       | >= 0 if provided                                      |
| `icon_type`   | Enum    | No       | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`          |
| `icon_value`  | String  | No*      | Required when `icon_type` is included, max 2000 chars |
| `icon_meta`   | Object  | No       | Any JSON object                                       |

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

`DELETE /api/v1/facility-group/{facility-group-id}/facilities/{id}`

Soft-deletes the facility. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |
| `id`                | Long | ID of the facility              |

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

| Field        | Create   | Update      | Rule                                                 |
|--------------|----------|-------------|------------------------------------------------------|
| `code`       | Required | Optional    | Not blank; max 100 chars                             |
| `name`       | Required | Optional    | Not blank; max 255 chars                             |
| `sort_order` | Optional | Optional    | Must be >= 0; defaults to `1` on create              |
| `icon_type`  | Required | Optional    | Must be a valid enum value                           |
| `icon_value` | Required | Conditional | Required when `icon_type` is present; max 2000 chars |
| `icon_meta`  | Optional | Optional    | Any valid JSON object                                |

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "Facility id: 99 not found."
}
```

| HTTP Status | Error Code                 | Cause                                            |
|-------------|----------------------------|--------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request          |
| 404         | `ENTITY_NOT_FOUND`         | Facility or facility group not found / deleted   |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)       |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                          |
