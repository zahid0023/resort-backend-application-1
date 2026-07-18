# Resort Room Categories API

Base URL: `/api/v1/resorts/{resort-id}/room-categories`

A resort room category links a platform-level room category (e.g. Deluxe, Suite, Villa) to a specific resort and
enriches it with resort-specific configuration: occupancy limits, extra-bed policy, smoking and pet rules, and
default check-in / check-out times. Each resort room category is uniquely identified within the resort by both its
linked `room_category_id` and its `code`.

Locale-specific names and descriptions are managed separately via the locale sub-resource endpoints and are embedded
in every response via the `locales` array. All records support soft-delete — deleted records are hidden from all
responses.

---

## Endpoints

| Method | Path                                                                                 | Description                          |
|--------|--------------------------------------------------------------------------------------|--------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories`                                        | Create a resort room category        |
| GET    | `/api/v1/resorts/{resort-id}/room-categories`                                        | List / filter resort room categories |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{id}`                                   | Get a resort room category           |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{id}`                                   | Update a resort room category        |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{id}`                                   | Delete a resort room category        |
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales`      | Create a locale translation          |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales/{id}` | Update a locale translation          |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales/{id}` | Delete a locale translation          |

---

## Data Model

### Resort Room Category

| Field                    | Type    | Required | Constraints                                          | Description                                                                                                                               |
|--------------------------|---------|----------|------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| `id`                     | Long    | —        | read-only                                            | Auto-generated identifier                                                                                                                 |
| `resort_id`              | Long    | —        | read-only                                            | ID of the parent resort                                                                                                                   |
| `room_category_id`       | Long    | Yes      | not null, must exist; unique per resort              | ID of the platform room category. Set on create, not updatable. Only one active record may link the same room category to a given resort. |
| `code`                   | String  | Yes      | not blank, max 50 chars; unique per resort           | Resort-specific machine-readable code (e.g. `DLX-KING`). Set on create, not updatable.                                                    |
| `sort_order`             | Integer | Yes      | not null, >= 0, default `0`                          | Display order of this room category within the resort                                                                                     |
| `max_adults`             | Integer | Yes      | not null, >= 1, default `2`                          | Maximum number of adults the room can accommodate                                                                                         |
| `max_children`           | Integer | Yes      | not null, >= 0, default `0`                          | Maximum number of children the room can accommodate                                                                                       |
| `max_occupancy`          | Integer | Yes      | not null, >= 1; must be >= max_adults + max_children | Overall occupancy cap including adults and children                                                                                       |
| `default_check_in_time`  | String  | No       | time (`HH:mm:ss`), nullable                          | Default check-in time for this room category (e.g. `14:00:00`). Omitted from response if null.                                            |
| `default_check_out_time` | String  | No       | time (`HH:mm:ss`), nullable                          | Default check-out time for this room category (e.g. `12:00:00`). Omitted from response if null.                                           |
| `is_extra_bed_allowed`   | Boolean | Yes      | not null, default `false`                            | Whether an extra bed can be added to this room category                                                                                   |
| `max_extra_beds`         | Integer | Yes      | not null, >= 0, default `0`                          | Maximum number of extra beds that can be added. Relevant only when `is_extra_bed_allowed` is `true`.                                      |
| `is_smoking_allowed`     | Boolean | Yes      | not null, default `false`                            | Whether smoking is permitted in this room category                                                                                        |
| `is_pets_allowed`        | Boolean | Yes      | not null, default `false`                            | Whether pets are allowed in this room category                                                                                            |
| `locales`                | Array   | —        | read-only here                                       | Locale translations for this resort room category                                                                                         |

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

A database check constraint also enforces that `max_occupancy >= max_adults + max_children`. Violating this at the
database level returns `409 DATA_INTEGRITY_VIOLATION`; client-side validation should enforce this before submitting.

---

## Create Resort Room Category

`POST /api/v1/resorts/{resort-id}/room-categories`

Creates a new resort room category, linking a platform room category to the specified resort. The `room_category_id`
and `code` are fixed at creation time and cannot be changed. Locale translations may be seeded inline via the `locales`
array or added individually afterward via the locale sub-resource endpoints.

### Path Parameters

| Parameter   | Type | Required | Description      |
|-------------|------|----------|------------------|
| `resort-id` | Long | Yes      | ID of the resort |

### Request Fields

| Field                    | Type    | Required | Validation                                                                   |
|--------------------------|---------|----------|------------------------------------------------------------------------------|
| `room_category_id`       | Long    | Yes      | Not null, must reference an existing active room category; unique per resort |
| `code`                   | String  | Yes      | Not blank, max 50 chars; must be unique within the resort                    |
| `sort_order`             | Integer | Yes      | Not null, >= 0                                                               |
| `max_adults`             | Integer | Yes      | Not null, >= 1                                                               |
| `max_children`           | Integer | Yes      | Not null, >= 0                                                               |
| `max_occupancy`          | Integer | Yes      | Not null, >= 1; must satisfy `max_occupancy >= max_adults + max_children`    |
| `default_check_in_time`  | String  | No       | Time string in `HH:mm:ss` format                                             |
| `default_check_out_time` | String  | No       | Time string in `HH:mm:ss` format                                             |
| `is_extra_bed_allowed`   | Boolean | Yes      | Not null                                                                     |
| `max_extra_beds`         | Integer | Yes      | Not null, >= 0                                                               |
| `is_smoking_allowed`     | Boolean | Yes      | Not null                                                                     |
| `is_pets_allowed`        | Boolean | Yes      | Not null                                                                     |
| `locales`                | Array   | No       | See locale fields below                                                      |

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
  "max_adults": 2,
  "max_children": 1,
  "max_occupancy": 3,
  "default_check_in_time": "14:00:00",
  "default_check_out_time": "12:00:00",
  "is_extra_bed_allowed": true,
  "max_extra_beds": 1,
  "is_smoking_allowed": false,
  "is_pets_allowed": false,
  "locales": [
    {
      "locale_id": 1,
      "name": "Deluxe King Room",
      "description": "A spacious room with a king-sized bed and resort garden view.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "ডিলাক্স কিং রুম",
      "description": "কিং সাইজ বেড এবং রিসোর্ট গার্ডেন ভিউসহ একটি প্রশস্ত কক্ষ।",
      "sort_order": 2
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

Returns a single active (non-deleted) resort room category belonging to the specified resort, including all locale
translations. Optional time fields (`default_check_in_time`, `default_check_out_time`) are omitted from the response
if null.

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
    "room_category_id": 3,
    "code": "DLX-KING",
    "sort_order": 1,
    "max_adults": 2,
    "max_children": 1,
    "max_occupancy": 3,
    "default_check_in_time": "14:00:00",
    "default_check_out_time": "12:00:00",
    "is_extra_bed_allowed": true,
    "max_extra_beds": 1,
    "is_smoking_allowed": false,
    "is_pets_allowed": false,
    "locales": [
      {
        "id": 11,
        "locale_id": 1,
        "name": "Deluxe King Room",
        "description": "A spacious room with a king-sized bed and resort garden view.",
        "sort_order": 1
      },
      {
        "id": 12,
        "locale_id": 2,
        "name": "ডিলাক্স কিং রুম",
        "description": "কিং সাইজ বেড এবং রিসোর্ট গার্ডেন ভিউসহ একটি প্রশস্ত কক্ষ।",
        "sort_order": 2
      }
    ]
  }
}
```

---

## List / Filter Resort Room Categories

`GET /api/v1/resorts/{resort-id}/room-categories`

Returns a paginated, filterable list of active (non-deleted) room categories for the specified resort. All filter
parameters are optional; omitting them returns all room categories for the resort. Multiple filters are combined with
AND. The `code` filter performs a case-insensitive partial match; the remaining filters perform exact matches.

If the resort does not exist or is deleted, a `404` is returned before executing the query.

### Path Parameters

| Parameter   | Type | Required | Description      |
|-------------|------|----------|------------------|
| `resort-id` | Long | Yes      | ID of the resort |

### Query Parameters

| Parameter           | Type    | Default | Constraints                                                         | Description                                |
|---------------------|---------|---------|---------------------------------------------------------------------|--------------------------------------------|
| `code`              | String  | —       | —                                                                   | Filter by code (partial, case-insensitive) |
| `roomCategoryId`    | Long    | —       | —                                                                   | Filter by linked platform room category ID |
| `isExtraBedAllowed` | Boolean | —       | `true` or `false`                                                   | Filter by extra-bed policy                 |
| `isSmokingAllowed`  | Boolean | —       | `true` or `false`                                                   | Filter by smoking policy                   |
| `isPetsAllowed`     | Boolean | —       | `true` or `false`                                                   | Filter by pet policy                       |
| `page`              | int     | `0`     | >= 0                                                                | Zero-based page index                      |
| `size`              | int     | `10`    | 1 – 50                                                              | Number of items per page                   |
| `sort_by`           | String  | `id`    | `id`, `code`, `sortOrder`, `maxAdults`, `maxOccupancy`, `createdAt` | Field to sort by                           |
| `sort_dir`          | String  | `ASC`   | `ASC`, `DESC`                                                       | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 7,
      "resort_id": 1,
      "room_category_id": 3,
      "code": "DLX-KING",
      "sort_order": 1,
      "max_adults": 2,
      "max_children": 1,
      "max_occupancy": 3,
      "default_check_in_time": "14:00:00",
      "default_check_out_time": "12:00:00",
      "is_extra_bed_allowed": true,
      "max_extra_beds": 1,
      "is_smoking_allowed": false,
      "is_pets_allowed": false,
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
      "room_category_id": 5,
      "code": "SUITE-JR",
      "sort_order": 2,
      "max_adults": 2,
      "max_children": 2,
      "max_occupancy": 4,
      "is_extra_bed_allowed": false,
      "max_extra_beds": 0,
      "is_smoking_allowed": false,
      "is_pets_allowed": true,
      "locales": [
        {
          "id": 13,
          "locale_id": 1,
          "name": "Junior Suite",
          "description": "A junior suite with a separate living area and ocean view.",
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

Updates the configuration fields of the resort room category. `room_category_id` and `code` are set at creation time
and cannot be changed. Locale translations are managed separately via the locale sub-resource endpoints.

### Path Parameters

| Parameter   | Type | Required | Description                    |
|-------------|------|----------|--------------------------------|
| `resort-id` | Long | Yes      | ID of the resort               |
| `id`        | Long | Yes      | ID of the resort room category |

### Request Fields

The `room_category_id` and `code` are not updatable. To change them, delete this entry and create a new one.

| Field                    | Type    | Required | Validation                                                                |
|--------------------------|---------|----------|---------------------------------------------------------------------------|
| `sort_order`             | Integer | Yes      | Not null, >= 0                                                            |
| `max_adults`             | Integer | Yes      | Not null, >= 1                                                            |
| `max_children`           | Integer | Yes      | Not null, >= 0                                                            |
| `max_occupancy`          | Integer | Yes      | Not null, >= 1; must satisfy `max_occupancy >= max_adults + max_children` |
| `default_check_in_time`  | String  | No       | Time string in `HH:mm:ss` format, or `null` to clear                      |
| `default_check_out_time` | String  | No       | Time string in `HH:mm:ss` format, or `null` to clear                      |
| `is_extra_bed_allowed`   | Boolean | Yes      | Not null                                                                  |
| `max_extra_beds`         | Integer | Yes      | Not null, >= 0                                                            |
| `is_smoking_allowed`     | Boolean | Yes      | Not null                                                                  |
| `is_pets_allowed`        | Boolean | Yes      | Not null                                                                  |

### Request Body

```json
{
  "sort_order": 1,
  "max_adults": 2,
  "max_children": 2,
  "max_occupancy": 4,
  "default_check_in_time": "15:00:00",
  "default_check_out_time": "11:00:00",
  "is_extra_bed_allowed": true,
  "max_extra_beds": 2,
  "is_smoking_allowed": false,
  "is_pets_allowed": true
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

## Resort Room Category Locales

Locale endpoints manage per-locale translations (name, description) for a resort room category. The
`{resort-room-category-id}` path parameter must reference an existing, active resort room category belonging to the
specified resort.

---

### Create Resort Room Category Locale

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/locales`

Adds a new locale translation to an existing resort room category. Each `locale_id` may only be used once per resort
room category.

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
  "locale_id": 3,
  "name": "Deluxe-Doppelzimmer",
  "description": "Ein geräumiges Zimmer mit Kingsize-Bett und Blick auf den Resortgarten.",
  "sort_order": 3
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
  "name": "Deluxe-Doppelzimmer (aktualisiert)",
  "description": "Aktualisierte Beschreibung.",
  "sort_order": 3
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

| HTTP Status | Error Code                 | Cause                                                                                                                                |
|-------------|----------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations (e.g. `name` blank, `sort_order` missing, `max_adults` less than 1)                 |
| 400         | `INVALID_ARGUMENT`         | Invalid `sort_by` field, invalid time format, or `max_occupancy` less than `max_adults + max_children`                               |
| 404         | `ENTITY_NOT_FOUND`         | Resort, room category, resort room category, locale, or locale translation not found, or already deleted                             |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `(resort_id, room_category_id)`, duplicate `(resort_id, code)`, or duplicate `locale_id` for the same resort room category |
