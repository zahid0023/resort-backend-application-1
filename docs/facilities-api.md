# Facilities API

Base URL: `/api/v1/facilities`

Facilities are individual amenities or services that belong to a facility group (e.g., "Outdoor Pool" under "
Recreation",
"Sauna" under "Wellness"). Each facility carries an optional icon using the same icon system as facility groups. Display
names and descriptions are locale-specific and are embedded in every response via the `locales` array. All records
support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                            | Description              |
|--------|-------------------------------------------------|--------------------------|
| POST   | `/api/v1/facilities`                            | Create a facility        |
| GET    | `/api/v1/facilities`                            | List / search facilities |
| GET    | `/api/v1/facilities/{id}`                       | Get a facility           |
| PUT    | `/api/v1/facilities/{id}`                       | Update a facility        |
| DELETE | `/api/v1/facilities/{id}`                       | Delete a facility        |
| POST   | `/api/v1/facilities/{facility-id}/locales`      | Add a locale             |
| PUT    | `/api/v1/facilities/{facility-id}/locales/{id}` | Update a locale          |
| DELETE | `/api/v1/facilities/{facility-id}/locales/{id}` | Delete a locale          |

---

## Data Model

### Facility

| Field               | Type    | Required | Constraints         | Description                                                                   |
|---------------------|---------|----------|---------------------|-------------------------------------------------------------------------------|
| `id`                | Long    | —        | read-only           | Auto-generated identifier                                                     |
| `facility_group_id` | Long    | Yes      | must exist          | ID of the parent facility group. Not updatable after creation.                |
| `code`              | String  | Yes      | max 100 chars       | Short unique identifier (e.g., `POOL_OUTDOOR`). Not updatable after creation. |
| `sort_order`        | Integer | No       | >= 0, default `1`   | Display order                                                                 |
| `icon_type`         | Enum    | Yes      | see values below    | Determines how `icon_value` is interpreted                                    |
| `icon_value`        | String  | No       | max 2000 chars      | Icon name or URL; omitted from response if not set                            |
| `icon_meta`         | Object  | No       | any key-value pairs | Optional rendering hints (size, color, etc.); omitted if not set              |
| `locales`           | Array   | No       | —                   | Locale entries (included in all responses)                                    |

### Facility Locale

| Field         | Type    | Required | Constraints   | Description                                      |
|---------------|---------|----------|---------------|--------------------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier                        |
| `locale_id`   | Long    | Yes      | must exist    | ID of the locale. Not updatable after creation.  |
| `name`        | String  | Yes      | max 255 chars | Display name of the facility in this locale      |
| `description` | String  | No       | unlimited     | Full description in this locale; omitted if null |
| `sort_order`  | Integer | Yes      | —             | Display order for this locale entry              |

### `icon_type` values

| Value      | `icon_value` meaning                             | Typical `icon_meta` keys        |
|------------|--------------------------------------------------|---------------------------------|
| `LUCIDE`   | Lucide React icon name, e.g. `"Waves"`, `"Wifi"` | `size`, `color`, `stroke_width` |
| `IMAGE`    | Image URL or storage path                        | `alt`, `width`, `height`        |
| `SVG`      | Raw SVG string or SVG file URL                   | `viewBox`, `fill`               |
| `EXTERNAL` | Any external icon ID or URL                      | any custom metadata             |

---

## Create Facility

`POST /api/v1/facilities`

Creates a facility along with its locale-specific translations in one request. All provided `locale_id` values must
reference existing, active locales.

### Request Body

```json
{
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

| Field               | Type    | Required | Validation                                     |
|---------------------|---------|----------|------------------------------------------------|
| `facility_group_id` | Long    | Yes      | Not null, must refer to a valid facility group |
| `code`              | String  | Yes      | Not blank, max 100 chars                       |
| `sort_order`        | Integer | No       | >= 0                                           |
| `icon_type`         | Enum    | Yes      | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`   |
| `icon_value`        | String  | No       | Max 2000 chars                                 |
| `icon_meta`         | Object  | No       | Any JSON object                                |
| `locales`           | Array   | No       | See locale fields below                        |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

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

Returns a single facility with all its locale translations.

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

---

## List / Search Facilities

`GET /api/v1/facilities`

Returns a paginated, filterable list of active (non-deleted) facilities including their locales. All filter parameters
are optional; omitting them returns all facilities. Multiple filters are combined with AND. String filters perform a
case-insensitive partial match. To scope the list to a specific facility group, use the `facilityGroupId` query
parameter or the dedicated sub-resource endpoint
`GET /api/v1/facility-groups/{facility-group-id}/facilities` (see Facility Groups API).

### Query Parameters

| Parameter         | Type   | Default | Constraints                            | Description                                |
|-------------------|--------|---------|----------------------------------------|--------------------------------------------|
| `code`            | String | —       | —                                      | Filter by code (partial, case-insensitive) |
| `facilityGroupId` | Long   | —       | —                                      | Filter by facility group (exact match)     |
| `page`            | int    | `0`     | >= 0                                   | Zero-based page index                      |
| `size`            | int    | `10`    | 1 – 50                                 | Number of items per page                   |
| `sort_by`         | String | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by                           |
| `sort_dir`        | String | `ASC`   | `ASC`, `DESC`                          | Sort direction                             |

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

`PUT /api/v1/facilities/{id}`

Updates `sort_order`, `icon_type`, `icon_value`, and `icon_meta`. The `code` and `facility_group_id` fields are set at
creation time and cannot be changed. Locale translations are managed via the facility locale endpoints.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the facility |

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

> **Note:** `code` and `facility_group_id` are immutable and cannot be changed after creation.

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

## Facility Locales

Facility locale endpoints manage per-locale translations for a facility. The `{facility-id}` path parameter must
reference an existing, active facility.

---

### Add Locale

`POST /api/v1/facilities/{facility-id}/locales`

Adds a new locale translation to an existing facility.

#### Path Parameters

| Parameter     | Type | Description        |
|---------------|------|--------------------|
| `facility-id` | Long | ID of the facility |

#### Request Body

```json
{
  "locale_id": 1,
  "name": "Outdoor Pool",
  "description": "A large outdoor swimming pool with sun loungers and a poolside bar.",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

### Update Locale

`PUT /api/v1/facilities/{facility-id}/locales/{id}`

Updates an existing locale translation for a facility. `locale_id` is not updatable.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `facility-id` | Long | ID of the facility        |
| `id`          | Long | ID of the facility locale |

#### Request Body

```json
{
  "name": "Outdoor Infinity Pool",
  "description": "Updated description.",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

### Delete Locale

`DELETE /api/v1/facilities/{facility-id}/locales/{id}`

Soft-deletes the locale entry. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `facility-id` | Long | ID of the facility        |
| `id`          | Long | ID of the facility locale |

#### Response `200 OK`

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
  "message": "Facility not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                   |
|-------------|----------------------------|---------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations        |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request                 |
| 404         | `ENTITY_NOT_FOUND`         | Facility, facility group, or locale not found / deleted |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code)              |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                 |
