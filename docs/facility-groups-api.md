# Facility Groups API

Base URL: `/api/v1/facility-groups`

Facility groups categorize amenities and facilities offered by a resort (e.g., Dining, Recreation, Wellness). Each
group carries an icon that the frontend uses to render a visual indicator. Display names and descriptions are
locale-specific and are embedded in every response via the `locales` array. Each group must be assigned at least one
facility scope at creation time; scopes can also be managed individually after creation. All records support
soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                                | Description                        |
|--------|-------------------------------------------------------------------------------------|------------------------------------|
| POST   | `/api/v1/facility-groups`                                                           | Create a facility group            |
| GET    | `/api/v1/facility-groups`                                                           | List / search facility groups      |
| GET    | `/api/v1/facility-groups/{id}`                                                      | Get a facility group               |
| PUT    | `/api/v1/facility-groups/{id}`                                                      | Update a facility group            |
| DELETE | `/api/v1/facility-groups/{id}`                                                      | Delete a facility group            |
| POST   | `/api/v1/facility-groups/{facility-group-id}/locales`                               | Add a locale to a facility group   |
| PUT    | `/api/v1/facility-groups/{facility-group-id}/locales/{id}`                          | Update a facility group locale     |
| DELETE | `/api/v1/facility-groups/{facility-group-id}/locales/{id}`                          | Delete a facility group locale     |
| POST   | `/api/v1/facility-groups/{facility-group-id}/scope-assignments`                     | Assign a scope to a facility group |
| DELETE | `/api/v1/facility-groups/{facility-group-id}/scope-assignments/{facility-scope-id}` | Unassign a scope                   |
| GET    | `/api/v1/facility-groups/{facility-group-id}/scope-assignments`                     | List scope assignments             |

---

## Data Model

### Facility Group

| Field               | Type    | Required | Constraints         | Description                                                             |
|---------------------|---------|----------|---------------------|-------------------------------------------------------------------------|
| `id`                | Long    | —        | read-only           | Auto-generated identifier                                               |
| `code`              | String  | Yes      | max 100 chars       | Short unique identifier (e.g., `DINING`). Not updatable after creation. |
| `sort_order`        | Integer | No       | >= 0, default `1`   | Display order                                                           |
| `icon_type`         | Enum    | Yes      | see values below    | Determines how `icon_value` is interpreted                              |
| `icon_value`        | String  | No       | max 2000 chars      | Icon name or URL; omitted from response if not set                      |
| `icon_meta`         | Object  | No       | any key-value pairs | Optional rendering hints (size, color, etc.); omitted if not set        |
| `locales`           | Array   | No       | —                   | Locale entries (included in all responses)                              |
| `scope_assignments` | Array   | —        | read-only           | Active scope assignments (included in all responses)                    |

### Facility Group Locale

| Field         | Type    | Required | Constraints   | Description                                       |
|---------------|---------|----------|---------------|---------------------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier                         |
| `locale_id`   | Long    | Yes      | must exist    | ID of the locale. Not updatable after creation.   |
| `name`        | String  | Yes      | max 255 chars | Display name of the facility group in this locale |
| `description` | String  | No       | unlimited     | Full description in this locale; omitted if null  |
| `sort_order`  | Integer | Yes      | —             | Display order for this locale entry               |

### Facility Group Scope Assignment

| Field               | Type    | Required | Constraints | Description                                |
|---------------------|---------|----------|-------------|--------------------------------------------|
| `facility_scope_id` | Long    | —        | read-only   | ID of the assigned facility scope          |
| `code`              | String  | —        | read-only   | Code of the scope (e.g., `RESORT`, `ROOM`) |
| `sort_order`        | Integer | —        | read-only   | Sort order of the scope                    |
| `locales`           | Array   | —        | read-only   | Locale translations of the scope           |

### `icon_type` values

| Value    | `icon_value` meaning                             | Typical `icon_meta` keys        |
|----------|--------------------------------------------------|---------------------------------|
| `LUCIDE` | Lucide React icon name, e.g. `"Waves"`, `"Wifi"` | `size`, `color`, `stroke_width` |
| `IMAGE`  | Image URL or storage path                        | `alt`, `width`, `height`        |

---

## Create Facility Group

`POST /api/v1/facility-groups`

Creates a facility group along with its locale-specific translations and scope assignments in one request. At least one
`scope_id` is required. All provided `locale_id` and `scope_id` values must reference existing, active records.

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
  "scope_ids": [
    1,
    2
  ],
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

| Field        | Type    | Required | Validation                    |
|--------------|---------|----------|-------------------------------|
| `code`       | String  | Yes      | Not blank, max 100 chars      |
| `sort_order` | Integer | No       | >= 0                          |
| `icon_type`  | Enum    | Yes      | One of: `LUCIDE`, `IMAGE`     |
| `icon_value` | String  | No       | Max 2000 chars                |
| `icon_meta`  | Object  | No       | Any JSON object               |
| `scope_ids`  | Array   | Yes      | Not empty; each ID must exist |
| `locales`    | Array   | No       | See locale fields below       |

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

## Get Facility Group

`GET /api/v1/facility-groups/{id}`

Returns a single facility group with all its locale translations and active scope assignments.

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
      }
    ],
    "scope_assignments": [
      {
        "facility_scope_id": 1,
        "code": "RESORT",
        "sort_order": 1,
        "locales": [
          {
            "id": 1,
            "locale_id": 1,
            "name": "Resort",
            "sort_order": 1
          }
        ]
      },
      {
        "facility_scope_id": 2,
        "code": "ROOM_CATEGORY",
        "sort_order": 2,
        "locales": [
          {
            "id": 2,
            "locale_id": 1,
            "name": "Room Category",
            "sort_order": 2
          }
        ]
      }
    ]
  }
}
```

---

## List / Search Facility Groups

`GET /api/v1/facility-groups`

Returns a paginated, filterable list of active (non-deleted) facility groups including their locales and active scope
assignments. All filter parameters are optional; omitting them returns all facility groups. String filters perform a
case-insensitive partial match.

### Query Parameters

| Parameter    | Type   | Default | Constraints                            | Description                                  |
|--------------|--------|---------|----------------------------------------|----------------------------------------------|
| `code`       | String | —       | —                                      | Filter by code (partial, case-insensitive)   |
| `scope-code` | Enum   | —       | `RESORT`, `ROOM_CATEGORY`, `ROOM`      | Filter to groups assigned to the given scope |
| `page`       | int    | `0`     | >= 0                                   | Zero-based page index                        |
| `size`       | int    | `10`    | 1 – 50                                 | Number of items per page                     |
| `sort_by`    | String | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by                             |
| `sort_dir`   | String | `ASC`   | `ASC`, `DESC`                          | Sort direction                               |

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
      ],
      "scope_assignments": [
        {
          "facility_scope_id": 1,
          "code": "RESORT",
          "sort_order": 1,
          "locales": [
            {
              "id": 1,
              "locale_id": 1,
              "name": "Resort",
              "sort_order": 1
            }
          ]
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
      ],
      "scope_assignments": [
        {
          "facility_scope_id": 1,
          "code": "RESORT",
          "sort_order": 1,
          "locales": [
            {
              "id": 1,
              "locale_id": 1,
              "name": "Resort",
              "sort_order": 1
            }
          ]
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

Updates `sort_order`, `icon_type`, `icon_value`, and `icon_meta`. The `code` field is set at creation time and cannot
be changed. Locale translations are managed via the locale endpoints. Scope assignments are managed via the
scope-assignment endpoints.

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

Soft-deletes the facility group and all of its facilities. The record is not removed from the database but will no
longer appear in any response.

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

## Facility Group Locales

Facility group locale endpoints manage per-locale translations for a facility group. The `{facility-group-id}` path
parameter must reference an existing, active facility group.

---

### Add Locale

`POST /api/v1/facility-groups/{facility-group-id}/locales`

Adds a new locale translation to an existing facility group.

#### Path Parameters

| Parameter           | Type | Description              |
|---------------------|------|--------------------------|
| `facility-group-id` | Long | ID of the facility group |

#### Request Body

```json
{
  "locale_id": 1,
  "name": "Dining",
  "description": "All food and beverage outlets including restaurants, bars, and room service.",
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

`PUT /api/v1/facility-groups/{facility-group-id}/locales/{id}`

Updates an existing locale translation for a facility group. `locale_id` is not updatable.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the facility group        |
| `id`                | Long | ID of the facility group locale |

#### Request Body

```json
{
  "name": "Food & Beverage",
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

`DELETE /api/v1/facility-groups/{facility-group-id}/locales/{id}`

Soft-deletes the locale entry. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-group-id` | Long | ID of the facility group        |
| `id`                | Long | ID of the facility group locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Facility Group Scope Assignments

Scope assignment endpoints manage which facility scopes (e.g., `RESORT`, `ROOM_CATEGORY`, `ROOM`) a facility group
applies to. The `{facility-group-id}` path parameter must reference an existing, active facility group.

Assigning a scope that was previously unassigned (soft-deleted) reactivates the existing assignment record.

---

### Assign Scope

`POST /api/v1/facility-groups/{facility-group-id}/scope-assignments`

Assigns a facility scope to the facility group.

#### Path Parameters

| Parameter           | Type | Description              |
|---------------------|------|--------------------------|
| `facility-group-id` | Long | ID of the facility group |

#### Request Body

```json
{
  "facility_scope_id": 3
}
```

#### Request Fields

| Field               | Type | Required | Validation           |
|---------------------|------|----------|----------------------|
| `facility_scope_id` | Long | Yes      | Not null, must exist |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

> `id` is the `facility_group_id`.

---

### Unassign Scope

`DELETE /api/v1/facility-groups/{facility-group-id}/scope-assignments/{facility-scope-id}`

Soft-removes the scope assignment. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter           | Type | Description              |
|---------------------|------|--------------------------|
| `facility-group-id` | Long | ID of the facility group |
| `facility-scope-id` | Long | ID of the facility scope |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

> `id` is the `facility_group_id`.

---

### List Scope Assignments

`GET /api/v1/facility-groups/{facility-group-id}/scope-assignments`

Returns all active scope assignments for the given facility group, including each scope's locales.

#### Path Parameters

| Parameter           | Type | Description              |
|---------------------|------|--------------------------|
| `facility-group-id` | Long | ID of the facility group |

#### Response `200 OK`

```json
[
  {
    "facility_scope_id": 1,
    "code": "RESORT",
    "sort_order": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Resort",
        "sort_order": 1
      }
    ]
  },
  {
    "facility_scope_id": 2,
    "code": "ROOM_CATEGORY",
    "sort_order": 2,
    "locales": [
      {
        "id": 2,
        "locale_id": 1,
        "name": "Room Category",
        "sort_order": 2
      }
    ]
  }
]
```

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "FacilityGroup not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                     |
|-------------|----------------------------|-----------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations          |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request                   |
| 404         | `ENTITY_NOT_FOUND`         | Facility group, locale, scope, or assignment not found    |
| 409         | `CONFLICT`                 | Scope is already actively assigned to this facility group |
| 409         | `DATA_INTEGRITY_VIOLATION` | DB constraint violation (e.g. duplicate code)             |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                   |
