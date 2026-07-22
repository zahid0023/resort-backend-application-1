# Resort Room Categories API

Base URL: `/api/v1/resorts/{resort-id}/room-categories`

A resort room category links a platform-level room category (e.g. Deluxe, Suite, Villa) to a specific resort and
enriches it with resort-specific configuration. Detailed room metadata (occupancy, room size, policies, stay rules)
lives in a dedicated `meta` sub-resource. Bed configurations are managed individually via the `beds` sub-resource.
Locale-specific names and descriptions are managed via the `locales` sub-resource.

Each resort room category is uniquely identified within the resort by both its linked `room_category_id` and its
`code`. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                                 | Description                          |
|--------|--------------------------------------------------------------------------------------|--------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories`                                        | Create a resort room category        |
| GET    | `/api/v1/resorts/{resort-id}/room-categories`                                        | List / filter resort room categories |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{id}`                                   | Get a resort room category           |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{id}`                                   | Update a resort room category        |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{id}`                                   | Delete a resort room category        |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/meta`         | Update room category meta            |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/beds`         | Add a bed                            |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/beds/{id}`    | Update a bed                         |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/beds/{id}`    | Remove a bed                         |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales`      | Create a locale translation          |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales/{id}` | Update a locale translation          |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales/{id}` | Delete a locale translation          |

---

## Data Model

### Resort Room Category

| Field           | Type    | Required | Constraints                                | Description                                                                                                                        |
|-----------------|---------|----------|--------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| `id`            | Long    | —        | read-only                                  | Auto-generated identifier                                                                                                          |
| `resort_id`     | Long    | —        | read-only                                  | ID of the parent resort                                                                                                            |
| `room_category` | Object  | —        | read-only in response                      | Full platform room category object (`id`, `code`, `sort_order`). Linked on create via `room_category_id`; not updatable.           |
| `code`          | String  | Yes      | not blank, max 50 chars; unique per resort | Resort-specific machine-readable code (e.g. `DLX-KING`). Set on create, not updatable.                                             |
| `sort_order`    | Integer | Yes      | not null, >= 0, default `0`                | Display order of this room category within the resort                                                                              |
| `meta`          | Object  | —        | read-only; see Meta sub-resource           | Room configuration details (occupancy, size, policies, stay rules). Created with the room category, updated via the meta endpoint. |
| `beds`          | Array   | —        | read-only; see Beds sub-resource           | Bed configurations for this room category. May be seeded inline on create; managed individually afterward via the beds endpoint.   |
| `locales`       | Array   | —        | read-only                                  | Locale translations for this resort room category                                                                                  |

### Resort Room Category Meta

| Field                    | Type    | Required | Constraints                 | Description                                                                                                             |
|--------------------------|---------|----------|-----------------------------|-------------------------------------------------------------------------------------------------------------------------|
| `id`                     | Long    | —        | read-only                   | Auto-generated identifier                                                                                               |
| `max_adults`             | Integer | Yes      | not null, >= 1, default `2` | Maximum number of adults the room can accommodate                                                                       |
| `max_children`           | Integer | Yes      | not null, >= 0, default `0` | Maximum number of children the room can accommodate                                                                     |
| `max_infants`            | Integer | Yes      | not null, >= 0, default `0` | Maximum number of infants the room can accommodate                                                                      |
| `max_occupancy`          | Integer | Yes      | not null, >= 1, default `2` | Overall occupancy cap including adults, children, and infants                                                           |
| `room_size`              | Decimal | No       | > 0, nullable               | Floor area of the room. Omitted from response if null.                                                                  |
| `room_size_unit`         | Object  | No       | nullable, must exist        | Full unit object for `room_size` (e.g., m²). Linked via `room_size_unit_id` in requests. Omitted from response if null. |
| `bedroom_count`          | Integer | Yes      | not null, >= 1, default `1` | Number of bedrooms                                                                                                      |
| `bathroom_count`         | Integer | Yes      | not null, >= 1, default `1` | Number of bathrooms                                                                                                     |
| `default_check_in_time`  | String  | No       | time (`HH:mm:ss`), nullable | Default check-in time (e.g. `14:00:00`). Omitted from response if null.                                                 |
| `default_check_out_time` | String  | No       | time (`HH:mm:ss`), nullable | Default check-out time (e.g. `12:00:00`). Omitted from response if null.                                                |
| `is_extra_bed_allowed`   | Boolean | Yes      | not null, default `false`   | Whether an extra bed can be added                                                                                       |
| `max_extra_beds`         | Integer | Yes      | not null, >= 0, default `0` | Maximum number of extra beds. Relevant only when `is_extra_bed_allowed` is `true`.                                      |
| `is_smoking_allowed`     | Boolean | Yes      | not null, default `false`   | Whether smoking is permitted                                                                                            |
| `is_pets_allowed`        | Boolean | Yes      | not null, default `false`   | Whether pets are allowed                                                                                                |
| `minimum_stay_nights`    | Integer | Yes      | not null, >= 1, default `1` | Minimum number of nights required per booking                                                                           |
| `maximum_stay_nights`    | Integer | No       | >= 1, nullable              | Maximum number of nights allowed per booking. Omitted from response if null.                                            |

### Resort Room Category Bed

| Field      | Type    | Required | Constraints                 | Description                                                                                                                               |
|------------|---------|----------|-----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| `id`       | Long    | —        | read-only                   | Auto-generated identifier                                                                                                                 |
| `bed_type` | Object  | —        | read-only in response       | Full bed type object (`id`, `code`, `sort_order`, `locales`). Linked on create/add via `bed_type_id` in requests; immutable after create. |
| `quantity` | Integer | Yes      | not null, >= 1, default `1` | Number of beds of this type in the room                                                                                                   |

### Resort Room Category Locale

| Field         | Type    | Required | Constraints              | Description                                     |
|---------------|---------|----------|--------------------------|-------------------------------------------------|
| `id`          | Long    | —        | read-only                | Auto-generated identifier                       |
| `locale_id`   | Long    | Yes      | not null, must exist     | ID of an existing active locale. Not updatable. |
| `name`        | String  | Yes      | not blank, max 150 chars | Localized display name for this room category   |
| `description` | String  | No       | unlimited                | Localized description; defaults to empty string |
| `sort_order`  | Integer | Yes      | not null                 | Display order for this locale entry             |

---

## Uniqueness Constraints

Two database constraints enforce data integrity:

1. **Duplicate room category link** — The combination of `(resort_id, room_category_id)` is unique. You cannot link the
   same platform room category to the same resort more than once. Returns `409 DATA_INTEGRITY_VIOLATION`.

2. **Duplicate code** — The combination of `(resort_id, code)` is unique. Two resort room categories under the same
   resort cannot share the same code. Returns `409 DATA_INTEGRITY_VIOLATION`.

---

## Create Resort Room Category

`POST /api/v1/resorts/{resort-id}/room-categories`

Creates a new resort room category. The `room_category_id` and `code` are fixed at creation time and cannot be changed.
The `meta` object is required and is created atomically with the room category. Bed configurations may be seeded inline
via the `beds` array or added individually afterward via the beds sub-resource. Locale translations may be seeded inline
via the `locales` array or added individually afterward.

### Path Parameters

| Parameter   | Type | Required | Description      |
|-------------|------|----------|------------------|
| `resort-id` | Long | Yes      | ID of the resort |

### Request Fields

| Field              | Type    | Required | Validation                                                                   |
|--------------------|---------|----------|------------------------------------------------------------------------------|
| `room_category_id` | Long    | Yes      | Not null, must reference an existing active room category; unique per resort |
| `code`             | String  | Yes      | Not blank, max 50 chars; must be unique within the resort                    |
| `sort_order`       | Integer | Yes      | Not null, >= 0                                                               |
| `meta`             | Object  | Yes      | Not null; see meta fields below                                              |
| `beds`             | Array   | No       | See bed fields below; seeded inline, managed via beds endpoint afterward     |
| `locales`          | Array   | No       | See locale fields below                                                      |

> The response returns `room_category` as a full object. On create the input is `room_category_id` (Long). Similarly,
> each entry in `beds` returns `bed_type` as a full object in responses; the input is `bed_type_id` (Long).

**Meta fields (`meta`):**

| Field                    | Type    | Required | Validation                       |
|--------------------------|---------|----------|----------------------------------|
| `max_adults`             | Integer | Yes      | Not null, >= 1                   |
| `max_children`           | Integer | Yes      | Not null, >= 0                   |
| `max_infants`            | Integer | Yes      | Not null, >= 0                   |
| `max_occupancy`          | Integer | Yes      | Not null, >= 1                   |
| `room_size`              | Decimal | No       | Must be > 0 if provided          |
| `room_size_unit_id`      | Long    | No       | Must reference an existing unit  |
| `bedroom_count`          | Integer | Yes      | Not null, >= 1                   |
| `bathroom_count`         | Integer | Yes      | Not null, >= 1                   |
| `default_check_in_time`  | String  | No       | Time string in `HH:mm:ss` format |
| `default_check_out_time` | String  | No       | Time string in `HH:mm:ss` format |
| `is_extra_bed_allowed`   | Boolean | Yes      | Not null                         |
| `max_extra_beds`         | Integer | Yes      | Not null, >= 0                   |
| `is_smoking_allowed`     | Boolean | Yes      | Not null                         |
| `is_pets_allowed`        | Boolean | Yes      | Not null                         |
| `minimum_stay_nights`    | Integer | Yes      | Not null, >= 1                   |
| `maximum_stay_nights`    | Integer | No       | >= 1 if provided                 |

**Bed fields (`beds[]`):**

| Field         | Type    | Required | Validation           |
|---------------|---------|----------|----------------------|
| `bed_type_id` | Long    | Yes      | Not null, must exist |
| `quantity`    | Integer | Yes      | Not null, >= 1       |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 150 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Request Body

```json
{
  "room_category_id": 3,
  "code": "DLX-KING",
  "sort_order": 1,
  "meta": {
    "max_adults": 2,
    "max_children": 1,
    "max_infants": 1,
    "max_occupancy": 4,
    "room_size": 42.5,
    "room_size_unit_id": 11,
    "bedroom_count": 1,
    "bathroom_count": 1,
    "default_check_in_time": "14:00:00",
    "default_check_out_time": "12:00:00",
    "is_extra_bed_allowed": true,
    "max_extra_beds": 1,
    "is_smoking_allowed": false,
    "is_pets_allowed": false,
    "minimum_stay_nights": 1,
    "maximum_stay_nights": 30
  },
  "beds": [
    {
      "bed_type_id": 5,
      "quantity": 1
    }
  ],
  "locales": [
    {
      "locale_id": 1,
      "name": "Deluxe King Room",
      "description": "A spacious room with a king-sized bed and resort garden view.",
      "sort_order": 1
    }
  ]
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 7
}
```

---

## Get Resort Room Category

`GET /api/v1/resorts/{resort-id}/room-categories/{id}`

Returns a single active resort room category with its `meta`, `beds`, and all locale translations. Optional fields
(`room_size`, `room_size_unit`, `default_check_in_time`, `default_check_out_time`, `maximum_stay_nights`) are
omitted from the response when null.

### Path Parameters

| Parameter   | Type | Required | Description                    |
|-------------|------|----------|--------------------------------|
| `resort-id` | Long | Yes      | ID of the resort               |
| `id`        | Long | Yes      | ID of the resort room category |

### Response `200 OK`

```json
{
  "data": {
    "id": 7,
    "resort_id": 1,
    "room_category": {
      "id": 3,
      "code": "DELUXE",
      "sort_order": 1
    },
    "code": "DLX-KING",
    "sort_order": 1,
    "meta": {
      "id": 7,
      "max_adults": 2,
      "max_children": 1,
      "max_infants": 1,
      "max_occupancy": 4,
      "room_size": 42.5,
      "room_size_unit": {
        "id": 11,
        "unit_type_id": 2,
        "code": "SQM",
        "symbol": "m²",
        "is_base_unit": true,
        "conversion_factor": 1.00000000,
        "sort_order": 1
      },
      "bedroom_count": 1,
      "bathroom_count": 1,
      "default_check_in_time": "14:00:00",
      "default_check_out_time": "12:00:00",
      "is_extra_bed_allowed": true,
      "max_extra_beds": 1,
      "is_smoking_allowed": false,
      "is_pets_allowed": false,
      "minimum_stay_nights": 1,
      "maximum_stay_nights": 30
    },
    "beds": [
      {
        "id": 3,
        "bed_type": {
          "id": 5,
          "code": "KING",
          "sort_order": 1,
          "locales": [
            {
              "id": 1,
              "locale": {
                "id": 1,
                "code": "en"
              },
              "name": "King Bed",
              "description": "A large king-sized bed.",
              "sort_order": 1
            }
          ]
        },
        "quantity": 1
      }
    ],
    "locales": [
      {
        "id": 11,
        "locale_id": 1,
        "name": "Deluxe King Room",
        "description": "A spacious room with a king-sized bed and resort garden view.",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List / Filter Resort Room Categories

`GET /api/v1/resorts/{resort-id}/room-categories`

Returns a paginated, filterable list of active resort room categories for the specified resort. All filter parameters
are optional. Multiple filters are combined with AND. The `code` filter performs a case-insensitive partial match;
the policy filters (`isExtraBedAllowed`, `isSmokingAllowed`, `isPetsAllowed`) perform exact matches against the
room category's meta. Each item includes the full `meta`, `beds`, and `locales` sub-resources.

If the resort does not exist or is deleted, a `404` is returned before executing the query.

### Path Parameters

| Parameter   | Type | Required | Description      |
|-------------|------|----------|------------------|
| `resort-id` | Long | Yes      | ID of the resort |

### Query Parameters

| Parameter           | Type    | Default | Constraints                            | Description                                |
|---------------------|---------|---------|----------------------------------------|--------------------------------------------|
| `code`              | String  | —       | —                                      | Filter by code (partial, case-insensitive) |
| `roomCategoryId`    | Long    | —       | —                                      | Filter by linked platform room category ID |
| `isExtraBedAllowed` | Boolean | —       | `true` or `false`                      | Filter by extra-bed policy (from meta)     |
| `isSmokingAllowed`  | Boolean | —       | `true` or `false`                      | Filter by smoking policy (from meta)       |
| `isPetsAllowed`     | Boolean | —       | `true` or `false`                      | Filter by pet policy (from meta)           |
| `page`              | int     | `0`     | >= 0                                   | Zero-based page index                      |
| `size`              | int     | `10`    | 1 – 50                                 | Number of items per page                   |
| `sort_by`           | String  | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by                           |
| `sort_dir`          | String  | `ASC`   | `ASC`, `DESC`                          | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 7,
      "resort_id": 1,
      "room_category": {
        "id": 3,
        "code": "DELUXE",
        "sort_order": 1
      },
      "code": "DLX-KING",
      "sort_order": 1,
      "meta": {
        "id": 7,
        "max_adults": 2,
        "max_children": 1,
        "max_infants": 1,
        "max_occupancy": 4,
        "room_size": 42.5,
        "room_size_unit": {
          "id": 11,
          "unit_type_id": 2,
          "code": "SQM",
          "symbol": "m²",
          "is_base_unit": true,
          "conversion_factor": 1.00000000,
          "sort_order": 1
        },
        "bedroom_count": 1,
        "bathroom_count": 1,
        "default_check_in_time": "14:00:00",
        "default_check_out_time": "12:00:00",
        "is_extra_bed_allowed": true,
        "max_extra_beds": 1,
        "is_smoking_allowed": false,
        "is_pets_allowed": false,
        "minimum_stay_nights": 1,
        "maximum_stay_nights": 30
      },
      "beds": [
        {
          "id": 3,
          "bed_type": {
            "id": 5,
            "code": "KING",
            "sort_order": 1,
            "locales": [
              {
                "id": 1,
                "locale": {
                  "id": 1,
                  "code": "en"
                },
                "name": "King Bed",
                "description": "A large king-sized bed.",
                "sort_order": 1
              }
            ]
          },
          "quantity": 1
        }
      ],
      "locales": [
        {
          "id": 11,
          "locale_id": 1,
          "name": "Deluxe King Room",
          "description": "A spacious room with a king-sized bed and resort garden view.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 8,
      "resort_id": 1,
      "room_category": {
        "id": 5,
        "code": "SUITE",
        "sort_order": 2
      },
      "code": "SUITE-JR",
      "sort_order": 2,
      "meta": {
        "id": 8,
        "max_adults": 3,
        "max_children": 2,
        "max_infants": 1,
        "max_occupancy": 6,
        "bedroom_count": 1,
        "bathroom_count": 1,
        "is_extra_bed_allowed": false,
        "max_extra_beds": 0,
        "is_smoking_allowed": false,
        "is_pets_allowed": false,
        "minimum_stay_nights": 2
      },
      "beds": [
        {
          "id": 5,
          "bed_type": {
            "id": 6,
            "code": "TWIN",
            "sort_order": 2,
            "locales": [
              {
                "id": 2,
                "locale": {
                  "id": 1,
                  "code": "en"
                },
                "name": "Twin Beds",
                "description": "Two single beds.",
                "sort_order": 1
              }
            ]
          },
          "quantity": 2
        }
      ],
      "locales": [
        {
          "id": 14,
          "locale_id": 1,
          "name": "Junior Suite",
          "description": "A spacious junior suite with twin beds.",
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

## Update Resort Room Category

`PUT /api/v1/resorts/{resort-id}/room-categories/{id}`

Updates `sort_order` only. `room_category_id` and `code` are immutable. Room metadata is updated via the meta
endpoint. Bed configurations are managed via the beds endpoint. Locale translations are managed via the locale
endpoint.

### Path Parameters

| Parameter   | Type | Required | Description                    |
|-------------|------|----------|--------------------------------|
| `resort-id` | Long | Yes      | ID of the resort               |
| `id`        | Long | Yes      | ID of the resort room category |

### Request Fields

| Field        | Type    | Required | Validation     |
|--------------|---------|----------|----------------|
| `sort_order` | Integer | Yes      | Not null, >= 0 |

### Request Body

```json
{
  "sort_order": 2
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 7
}
```

---

## Delete Resort Room Category

`DELETE /api/v1/resorts/{resort-id}/room-categories/{id}`

Soft-deletes the resort room category. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter   | Type | Required | Description                    |
|-------------|------|----------|--------------------------------|
| `resort-id` | Long | Yes      | ID of the resort               |
| `id`        | Long | Yes      | ID of the resort room category |

### Response `200 OK`

```json
{
  "success": true,
  "id": 7
}
```

---

## Resort Room Category Meta

The meta sub-resource holds detailed room configuration for a resort room category. Meta is created automatically
when the room category is created and cannot be deleted independently. It is updated via the dedicated PUT endpoint.

The `{resort-room-category-id}` path parameter must reference an existing, active resort room category belonging
to the specified resort.

---

### Update Resort Room Category Meta

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/meta`

Replaces all meta configuration fields for the resort room category.

#### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |

#### Request Fields

| Field                    | Type    | Required | Validation                       |
|--------------------------|---------|----------|----------------------------------|
| `max_adults`             | Integer | Yes      | Not null, >= 1                   |
| `max_children`           | Integer | Yes      | Not null, >= 0                   |
| `max_infants`            | Integer | Yes      | Not null, >= 0                   |
| `max_occupancy`          | Integer | Yes      | Not null, >= 1                   |
| `room_size`              | Decimal | No       | Must be > 0 if provided          |
| `room_size_unit_id`      | Long    | No       | Must reference an existing unit  |
| `bedroom_count`          | Integer | Yes      | Not null, >= 1                   |
| `bathroom_count`         | Integer | Yes      | Not null, >= 1                   |
| `default_check_in_time`  | String  | No       | Time string in `HH:mm:ss` format |
| `default_check_out_time` | String  | No       | Time string in `HH:mm:ss` format |
| `is_extra_bed_allowed`   | Boolean | Yes      | Not null                         |
| `max_extra_beds`         | Integer | Yes      | Not null, >= 0                   |
| `is_smoking_allowed`     | Boolean | Yes      | Not null                         |
| `is_pets_allowed`        | Boolean | Yes      | Not null                         |
| `minimum_stay_nights`    | Integer | Yes      | Not null, >= 1                   |
| `maximum_stay_nights`    | Integer | No       | >= 1 if provided                 |

#### Request Body

```json
{
  "max_adults": 2,
  "max_children": 2,
  "max_infants": 1,
  "max_occupancy": 5,
  "room_size": 42.5,
  "room_size_unit_id": 11,
  "bedroom_count": 1,
  "bathroom_count": 1,
  "default_check_in_time": "15:00:00",
  "default_check_out_time": "11:00:00",
  "is_extra_bed_allowed": true,
  "max_extra_beds": 2,
  "is_smoking_allowed": false,
  "is_pets_allowed": true,
  "minimum_stay_nights": 2,
  "maximum_stay_nights": 14
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 7
}
```

---

## Resort Room Category Beds

Bed endpoints manage individual bed configurations within a room category. A room category may have multiple bed
entries, each representing a distinct bed type and its quantity. The combination of `(resort_room_category_id,
bed_type_id)` must be unique.

The `{resort-room-category-id}` path parameter must reference an existing, active resort room category belonging
to the specified resort. Beds in the response of the Get / List endpoints reflect only active (non-deleted) entries.

---

### Add Bed

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/beds`

Adds a new bed configuration to the room category. Each `bed_type_id` may only be used once per room category.

#### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |

#### Request Fields

| Field         | Type    | Required | Validation           |
|---------------|---------|----------|----------------------|
| `bed_type_id` | Long    | Yes      | Not null, must exist |
| `quantity`    | Integer | Yes      | Not null, >= 1       |

#### Request Body

```json
{
  "bed_type_id": 2,
  "quantity": 2
}
```

#### Response `201 Created`

```json
{
  "success": true,
  "id": 4
}
```

---

### Update Bed

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/beds/{id}`

Updates the `quantity` of an existing bed entry. `bed_type_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |
| `id`                      | Long | Yes      | ID of the bed entry            |

#### Request Fields

| Field      | Type    | Required | Validation     |
|------------|---------|----------|----------------|
| `quantity` | Integer | Yes      | Not null, >= 1 |

#### Request Body

```json
{
  "quantity": 3
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 4
}
```

---

### Remove Bed

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/beds/{id}`

Soft-deletes a bed entry. The record is not removed from the database but will no longer appear in any response.

#### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |
| `id`                      | Long | Yes      | ID of the bed entry            |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 4
}
```

---

## Resort Room Category Locales

Locale endpoints manage per-locale translations (name, description) for a resort room category. The
`{resort-room-category-id}` path parameter must reference an existing, active resort room category belonging to
the specified resort.

---

### Create Resort Room Category Locale

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales`

Adds a new locale translation to an existing resort room category. Each `locale_id` may only be used once per
resort room category.

#### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 150 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "Deluxe-Doppelzimmer",
  "description": "Ein geräumiges Zimmer mit Kingsize-Bett und Blick auf den Resortgarten.",
  "sort_order": 2
}
```

#### Response `201 Created`

```json
{
  "success": true,
  "id": 15
}
```

---

### Update Resort Room Category Locale

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales/{id}`

Updates an existing locale translation. `locale_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter                 | Type | Required | Description                           |
|---------------------------|------|----------|---------------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort                      |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category        |
| `id`                      | Long | Yes      | ID of the resort room category locale |

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 150 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Request Body

```json
{
  "name": "Deluxe King Room",
  "description": "A spacious room with a king-sized bed and resort garden view.",
  "sort_order": 1
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 15
}
```

---

### Delete Resort Room Category Locale

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales/{id}`

Soft-deletes a locale translation. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter                 | Type | Required | Description                           |
|---------------------------|------|----------|---------------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort                      |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category        |
| `id`                      | Long | Yes      | ID of the resort room category locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 15
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
  "message": "ResortRoomCategory not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                  |
|-------------|----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations (e.g. `meta` null on create, `sort_order` missing, `max_adults` less than 1, `quantity` less than 1)  |
| 400         | `INVALID_ARGUMENT`         | Invalid `sort_by` field or invalid time format                                                                                                         |
| 404         | `ENTITY_NOT_FOUND`         | Resort, room category, resort room category, bed type, locale, bed entry, or locale translation not found, or already deleted                          |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `(resort_id, room_category_id)`, duplicate `(resort_id, code)`, duplicate `bed_type_id` for the same room category, or duplicate `locale_id` |
