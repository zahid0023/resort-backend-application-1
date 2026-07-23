# Facility Price Types API

Base URL: `/api/v1/facility-price-types`

Facility price types define how a facility is priced for guests (e.g. `FREE`, `INCLUDED`, `PAID`, `CONTACT`). Common
types are pre-seeded by the database migration but new types can be created and existing ones soft-deleted as needed.
Only `sort_order` is updatable on the root resource. Locale translations (including `purpose` and `usage_example`) are
managed via the locale sub-resource endpoints and are included in every response via the `locales` array. All records
support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                 | Description                        |
|--------|----------------------------------------------------------------------|------------------------------------|
| POST   | `/api/v1/facility-price-types`                                       | Create a facility price type       |
| GET    | `/api/v1/facility-price-types`                                       | List / search facility price types |
| GET    | `/api/v1/facility-price-types/{id}`                                  | Get a facility price type          |
| PUT    | `/api/v1/facility-price-types/{id}`                                  | Update a facility price type       |
| DELETE | `/api/v1/facility-price-types/{id}`                                  | Delete a facility price type       |
| POST   | `/api/v1/facility-price-types/{facility-price-type-id}/locales`      | Add a locale translation           |
| PUT    | `/api/v1/facility-price-types/{facility-price-type-id}/locales/{id}` | Update a locale translation        |
| DELETE | `/api/v1/facility-price-types/{facility-price-type-id}/locales/{id}` | Delete a locale translation        |

---

## Data Model

### Facility Price Type

| Field        | Type    | Required | Constraints  | Description                                                              |
|--------------|---------|----------|--------------|--------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only    | Auto-generated identifier                                                |
| `code`       | String  | Yes      | max 50 chars | Stable internal code (e.g. `FREE`, `PAID`). Not updatable after creation |
| `sort_order` | Integer | Yes      | >= 0         | Display order                                                            |
| `locales`    | Array   | —        | read-only    | All active locale translations                                           |

### Facility Price Type Locale

| Field           | Type    | Required | Constraints              | Description                                    |
|-----------------|---------|----------|--------------------------|------------------------------------------------|
| `id`            | Long    | —        | read-only                | Auto-generated identifier                      |
| `locale_id`     | Long    | Yes      | must exist               | ID of an existing active locale; not updatable |
| `name`          | String  | Yes      | not blank, max 100 chars | Localized display name                         |
| `description`   | String  | No       | —                        | Short UI description; omitted if null          |
| `sort_order`    | Integer | Yes      | not null                 | Display order for this locale entry            |
| `purpose`       | String  | No       | —                        | Explains when this price type should be used   |
| `usage_example` | String  | No       | —                        | Example scenario shown to administrators       |

---

## Create Facility Price Type

`POST /api/v1/facility-price-types`

Creates a new facility price type. Locale translations are added separately via the locale endpoints.

### Request Body

```json
{
  "code": "NEGOTIABLE",
  "sort_order": 5
}
```

### Request Fields

| Field        | Type    | Required | Validation              |
|--------------|---------|----------|-------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars |
| `sort_order` | Integer | Yes      | Not null, >= 0          |

### Response `201 Created`

```json
{
  "success": true,
  "id": 5
}
```

---

## Get Facility Price Type

`GET /api/v1/facility-price-types/{id}`

Returns a single facility price type with all its active locale translations.

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the facility price type |

### Response `200 OK`

```json
{
  "data": {
    "id": 3,
    "code": "PAID",
    "sort_order": 3,
    "locales": [
      {
        "id": 3,
        "locale_id": 1,
        "name": "Paid",
        "description": "The facility requires a separate fee at the time of booking or use.",
        "sort_order": 3,
        "purpose": "Used for premium amenities or services that are charged individually.",
        "usage_example": "A spa treatment costs $80 per session and must be booked separately."
      }
    ]
  }
}
```

---

## List / Search Facility Price Types

`GET /api/v1/facility-price-types`

Returns a paginated, filterable list of active (non-deleted) facility price types including their locale translations.
All filter parameters are optional; omitting them returns all types. Filtering performs a case-insensitive partial
match.

### Query Parameters

| Parameter  | Type   | Default | Constraints                            | Description                                |
|------------|--------|---------|----------------------------------------|--------------------------------------------|
| `code`     | String | —       | —                                      | Filter by code (partial, case-insensitive) |
| `page`     | int    | `0`     | >= 0                                   | Zero-based page index                      |
| `size`     | int    | `10`    | 1 – 50                                 | Number of items per page                   |
| `sort_by`  | String | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by                           |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                          | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "FREE",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Free",
          "description": "The facility is available at no charge to guests.",
          "sort_order": 1,
          "purpose": "Used for complimentary amenities included with the stay.",
          "usage_example": "The gym and outdoor pool are free for all guests."
        }
      ]
    },
    {
      "id": 2,
      "code": "INCLUDED",
      "sort_order": 2,
      "locales": [
        {
          "id": 2,
          "locale_id": 1,
          "name": "Included",
          "description": "The facility is bundled into the room or package price.",
          "sort_order": 2,
          "purpose": "Used when the cost is absorbed into an existing booking package.",
          "usage_example": "Breakfast and airport shuttle are included in the full-board package."
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 4,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Facility Price Type

`PUT /api/v1/facility-price-types/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the locale endpoints.

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the facility price type |

### Request Body

```json
{
  "sort_order": 3
}
```

### Request Fields

| Field        | Type    | Required | Validation     |
|--------------|---------|----------|----------------|
| `sort_order` | Integer | Yes      | Not null, >= 0 |

> **Note:** `code` is immutable and cannot be changed after creation.

### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

## Delete Facility Price Type

`DELETE /api/v1/facility-price-types/{id}`

Soft-deletes the facility price type. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter | Type | Description                   |
|-----------|------|-------------------------------|
| `id`      | Long | ID of the facility price type |

### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

## Facility Price Type Locales

Locale endpoints manage per-locale translations for a facility price type. The `{facility-price-type-id}` path
parameter must reference an existing, active facility price type.

---

### Add Locale

`POST /api/v1/facility-price-types/{facility-price-type-id}/locales`

Adds a new locale translation to an existing facility price type.

#### Path Parameters

| Parameter                | Type | Description                   |
|--------------------------|------|-------------------------------|
| `facility-price-type-id` | Long | ID of the facility price type |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "পেইড",
  "description": "বুকিং বা ব্যবহারের সময় আলাদা ফি প্রয়োজন।",
  "sort_order": 3,
  "purpose": "প্রিমিয়াম সুবিধার জন্য আলাদা চার্জ করা হয়।",
  "usage_example": "একটি স্পা ট্রিটমেন্টের জন্য প্রতি সেশনে $80 চার্জ করা হয়।"
}
```

#### Request Fields

| Field           | Type    | Required | Validation               |
|-----------------|---------|----------|--------------------------|
| `locale_id`     | Long    | Yes      | Not null, must exist     |
| `name`          | String  | Yes      | Not blank, max 100 chars |
| `description`   | String  | No       | —                        |
| `sort_order`    | Integer | Yes      | Not null                 |
| `purpose`       | String  | No       | —                        |
| `usage_example` | String  | No       | —                        |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 5
}
```

---

### Update Locale

`PUT /api/v1/facility-price-types/{facility-price-type-id}/locales/{id}`

Updates an existing locale translation. `locale_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter                | Type | Description                          |
|--------------------------|------|--------------------------------------|
| `facility-price-type-id` | Long | ID of the facility price type        |
| `id`                     | Long | ID of the facility price type locale |

#### Request Body

```json
{
  "name": "Paid",
  "description": "Updated description.",
  "sort_order": 3,
  "purpose": "Updated purpose.",
  "usage_example": "Updated example."
}
```

#### Request Fields

| Field           | Type    | Required | Validation               |
|-----------------|---------|----------|--------------------------|
| `name`          | String  | Yes      | Not blank, max 100 chars |
| `description`   | String  | No       | —                        |
| `sort_order`    | Integer | Yes      | Not null                 |
| `purpose`       | String  | No       | —                        |
| `usage_example` | String  | No       | —                        |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 5
}
```

---

### Delete Locale

`DELETE /api/v1/facility-price-types/{facility-price-type-id}/locales/{id}`

Soft-deletes a locale translation. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter                | Type | Description                          |
|--------------------------|------|--------------------------------------|
| `facility-price-type-id` | Long | ID of the facility price type        |
| `id`                     | Long | ID of the facility price type locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 5
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
  "message": "FacilityPriceType not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                 |
|-------------|----------------------------|-----------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations                      |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request                               |
| 404         | `ENTITY_NOT_FOUND`         | Facility price type or locale not found                               |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `code` or duplicate locale for the same facility price type |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                               |
