# Facilities API

Base URL: `/api/v1/facility-groups/{facility-group-id}/facilities`

Facilities are individual amenities or services that belong to a facility group (e.g., "Outdoor Pool" under "
Recreation", "Sauna" under "Wellness"). Each facility carries an optional icon using the same icon system as facility
groups (`LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`). Display names and descriptions are managed per locale via the locale
sub-resource. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                                | Description                |
|--------|-------------------------------------------------------------------------------------|----------------------------|
| POST   | `/api/v1/facility-groups/{facility-group-id}/facilities`                            | Create a facility          |
| GET    | `/api/v1/facility-groups/{facility-group-id}/facilities`                            | List all facilities        |
| GET    | `/api/v1/facility-groups/{facility-group-id}/facilities/{id}`                       | Get a facility             |
| GET    | `/api/v1/facility-groups/{facility-group-id}/facilities/icon-types`                 | List all icon types        |
| PUT    | `/api/v1/facility-groups/{facility-group-id}/facilities/{id}`                       | Update a facility          |
| DELETE | `/api/v1/facility-groups/{facility-group-id}/facilities/{id}`                       | Delete a facility          |
| POST   | `/api/v1/facility-groups/{facility-group-id}/facilities/{facility-id}/locales`      | Add a locale to a facility |
| PUT    | `/api/v1/facility-groups/{facility-group-id}/facilities/{facility-id}/locales/{id}` | Update a facility locale   |
| DELETE | `/api/v1/facility-groups/{facility-group-id}/facilities/{facility-id}/locales/{id}` | Delete a facility locale   |

---

## Data Model

### Facility

| Field               | Type    | Required | Constraints         | Description                                                                   |
|---------------------|---------|----------|---------------------|-------------------------------------------------------------------------------|
| `id`                | Long    | —        | read-only           | Auto-generated identifier                                                     |
| `facility_group_id` | Long    | —        | read-only           | ID of the parent facility group                                               |
| `code`              | String  | Yes      | max 100 chars       | Short unique identifier (e.g., `POOL_OUTDOOR`). Not updatable after creation. |
| `sort_order`        | Integer | No       | >= 0, default `1`   | Display order                                                                 |
| `icon_type`         | Enum    | Yes      | see values below    | Determines how `icon_value` is interpreted                                    |
| `icon_value`        | String  | No       | max 2000 chars      | Icon name, URL, or SVG content                                                |
| `icon_meta`         | Object  | No       | any key-value pairs | Optional rendering hints (size, color, alt, etc.)                             |
| `locales`           | Array   | No       | —                   | Locale entries (included in all responses)                                    |

### Facility Locale

| Field         | Type    | Required | Constraints   | Description                                     |
|---------------|---------|----------|---------------|-------------------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier                       |
| `locale_id`   | Long    | Yes      | must exist    | ID of the locale. Not updatable after creation. |
| `name`        | String  | Yes      | max 255 chars | Display name of the facility in this locale     |
| `description` | String  | No       | unlimited     | Full description in this locale                 |
| `sort_order`  | Integer | Yes      | —             | Display order for this locale entry             |

### `icon_type` values

| Value      | `icon_value` meaning                             | Typical `icon_meta` keys        |
|------------|--------------------------------------------------|---------------------------------|
| `LUCIDE`   | Lucide React icon name, e.g. `"Waves"`, `"Wifi"` | `size`, `color`, `stroke_width` |
| `IMAGE`    | Image URL or storage path                        | `alt`, `width`, `height`        |
| `SVG`      | Raw SVG string or SVG file URL                   | `viewBox`, `fill`               |
| `EXTERNAL` | Any external icon ID or URL                      | any custom metadata             |

---

## Get All Icon Types

`GET /api/v1/facility-groups/{facility-group-id}/facilities/icon-types`

Returns all supported icon type values. Use this to populate dropdowns or validate `icon_type` on the client side.

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

`POST /api/v1/facility-groups/{facility-group-id}/facilities`

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |

### Request Body

```json
{
  "code": "POOL_OUTDOOR",
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "Waves",
  "icon_meta": {
    "size": 24,
    "color": "#3b82f6",
    "stroke_width": 1.5
  },
  "locales": [
    {
      "locale_id": 1,
      "name": "Outdoor Pool",
      "description": "A large outdoor swimming pool with sun loungers and a poolside bar.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "আউটডোর পুল",
      "description": "সানলাউঞ্জার ও পুলসাইড বার সহ একটি বড় আউটডোর সুইমিং পুল।",
      "sort_order": 2
    }
  ]
}
```

### Request Fields

| Field        | Type    | Required | Validation                                   |
|--------------|---------|----------|----------------------------------------------|
| `code`       | String  | Yes      | Not blank, max 100 chars                     |
| `sort_order` | Integer | No       | >= 0                                         |
| `icon_type`  | Enum    | Yes      | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL` |
| `icon_value` | String  | No       | Max 2000 chars                               |
| `icon_meta`  | Object  | No       | Any JSON object                              |
| `locales`    | Array   | No       | See locale fields below                      |

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

## Get Facility

`GET /api/v1/facility-groups/{facility-group-id}/facilities/{id}`

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
    "facility_group_id": 1,
    "code": "POOL_OUTDOOR",
    "sort_order": 1,
    "icon_type": "LUCIDE",
    "icon_value": "Waves",
    "icon_meta": {
      "size": 24,
      "color": "#3b82f6",
      "stroke_width": 1.5
    },
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Outdoor Pool",
        "description": "A large outdoor swimming pool with sun loungers and a poolside bar.",
        "sort_order": 1
      },
      {
        "id": 2,
        "locale_id": 2,
        "name": "আউটডোর পুল",
        "description": "সানলাউঞ্জার ও পুলসাইড বার সহ একটি বড় আউটডোর সুইমিং পুল।",
        "sort_order": 2
      }
    ]
  }
}
```

> Fields with `null` values are omitted from the response (`icon_meta` and `description` will be absent if not set).

---

## List All Facilities

`GET /api/v1/facility-groups/{facility-group-id}/facilities`

Returns a paginated list of active (non-deleted) facilities belonging to the given facility group, including their
locales.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |

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
      "facility_group_id": 1,
      "code": "POOL_OUTDOOR",
      "sort_order": 1,
      "icon_type": "LUCIDE",
      "icon_value": "Waves",
      "icon_meta": {
        "size": 24,
        "color": "#3b82f6"
      },
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Outdoor Pool",
          "description": "A large outdoor swimming pool with sun loungers and a poolside bar.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "facility_group_id": 1,
      "code": "POOL_INDOOR",
      "sort_order": 2,
      "icon_type": "LUCIDE",
      "icon_value": "Droplets",
      "locales": [
        {
          "id": 3,
          "locale_id": 1,
          "name": "Indoor Pool",
          "description": "A heated indoor pool available year-round.",
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

## Update Facility

`PUT /api/v1/facility-groups/{facility-group-id}/facilities/{id}`

Updates the facility. `code` is not updatable. Use the locale sub-resource to manage `name` and `description`.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |
| `id`                | Long | ID of the facility              |

### Request Body

```json
{
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "Waves",
  "icon_meta": {
    "size": 28,
    "color": "#06b6d4"
  }
}
```

### Request Fields

| Field        | Type    | Required | Validation                                   |
|--------------|---------|----------|----------------------------------------------|
| `sort_order` | Integer | No       | >= 0 if provided                             |
| `icon_type`  | Enum    | Yes      | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL` |
| `icon_value` | String  | No       | Max 2000 chars                               |
| `icon_meta`  | Object  | No       | Any JSON object                              |

> **Note:** `code` is immutable and cannot be changed after creation.

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Facility

`DELETE /api/v1/facility-groups/{facility-group-id}/facilities/{id}`

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

## Add Locale

`POST /api/v1/facility-groups/{facility-group-id}/facilities/{facility-id}/locales`

Adds a locale entry to an existing facility.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |
| `facility-id`       | Long | ID of the facility              |

### Request Body

```json
{
  "locale_id": 1,
  "name": "Outdoor Pool",
  "description": "A large outdoor swimming pool with sun loungers and a poolside bar.",
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

`PUT /api/v1/facility-groups/{facility-group-id}/facilities/{facility-id}/locales/{id}`

Updates an existing locale entry. `locale_id` is not updatable.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |
| `facility-id`       | Long | ID of the facility              |
| `id`                | Long | ID of the facility locale       |

### Request Body

```json
{
  "name": "Outdoor Infinity Pool",
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

`DELETE /api/v1/facility-groups/{facility-group-id}/facilities/{facility-id}/locales/{id}`

Soft-deletes the locale entry. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the parent facility group |
| `facility-id`       | Long | ID of the facility              |
| `id`                | Long | ID of the facility locale       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Error Responses

| HTTP Status | Error Code                 | Cause                                                   |
|-------------|----------------------------|---------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations        |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request                 |
| 404         | `ENTITY_NOT_FOUND`         | Facility, facility group, or locale not found / deleted |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)              |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                 |
