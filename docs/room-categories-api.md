# Room Categories API

Base URL: `/api/v1/room-categories`

Room categories classify rooms by tier or type (e.g., Standard, Deluxe, Suite). Category names and descriptions are
locale-specific and are embedded in every response via the `locales` array. All records support soft-delete — deleted
records are hidden from all responses.

---

## Endpoints

| Method | Path                                                      | Description                   |
|--------|-----------------------------------------------------------|-------------------------------|
| POST   | `/api/v1/room-categories`                                 | Create a room category        |
| GET    | `/api/v1/room-categories`                                 | List all room categories      |
| GET    | `/api/v1/room-categories/{id}`                            | Get a room category           |
| PUT    | `/api/v1/room-categories/{id}`                            | Update a room category        |
| DELETE | `/api/v1/room-categories/{id}`                            | Delete a room category        |
| POST   | `/api/v1/room-categories/{room-category-id}/locales`      | Create a room category locale |
| PUT    | `/api/v1/room-categories/{room-category-id}/locales/{id}` | Update a room category locale |
| DELETE | `/api/v1/room-categories/{room-category-id}/locales/{id}` | Delete a room category locale |

---

## Data Model

### Room Category

| Field        | Type    | Required | Constraints           | Description                               |
|--------------|---------|----------|-----------------------|-------------------------------------------|
| `id`         | Long    | —        | read-only             | Auto-generated identifier                 |
| `code`       | String  | Yes      | max 50 chars, unique  | Short identifier code (e.g., `DELUXE`)    |
| `sort_order` | Integer | Yes      | not null, default `0` | Display order                             |
| `locales`    | Array   | —        | read-only             | All locale translations for this category |

### Room Category Locale

| Field         | Type    | Required | Constraints   | Description                         |
|---------------|---------|----------|---------------|-------------------------------------|
| `id`          | Long    | —        | read-only     | Auto-generated identifier           |
| `locale_id`   | Long    | Yes      | must exist    | ID of an existing active locale     |
| `name`        | String  | Yes      | max 100 chars | Localized name of the category      |
| `description` | String  | No       | unlimited     | Localized description               |
| `sort_order`  | Integer | Yes      | not null      | Display order for this locale entry |

---

## Create Room Category

`POST /api/v1/room-categories`

Creates a room category along with its locale-specific translations in one request. All provided `locale_id` values must
reference existing, active locales.

### Request Body

```json
{
  "code": "DELUXE",
  "sort_order": 2,
  "locales": [
    {
      "locale_id": 1,
      "name": "Deluxe Room",
      "description": "Spacious room with upgraded interior and additional facilities.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "ডিলাক্স রুম",
      "description": "উন্নত ইন্টেরিয়র ও অতিরিক্ত সুবিধাসহ প্রশস্ত রুম।",
      "sort_order": 2
    }
  ]
}
```

### Request Fields

| Field        | Type    | Required | Validation              |
|--------------|---------|----------|-------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars |
| `sort_order` | Integer | Yes      | Not null                |
| `locales`    | Array   | No       | See locale fields below |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
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

## Get Room Category

`GET /api/v1/room-categories/{id}`

Returns a single room category with all its locale translations.

### Path Parameters

| Parameter | Type | Description             |
|-----------|------|-------------------------|
| `id`      | Long | ID of the room category |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "DELUXE",
    "sort_order": 2,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Deluxe Room",
        "description": "Spacious room with upgraded interior and additional facilities.",
        "sort_order": 1
      },
      {
        "id": 2,
        "locale_id": 2,
        "name": "ডিলাক্স রুম",
        "description": "উন্নত ইন্টেরিয়র ও অতিরিক্ত সুবিধাসহ প্রশস্ত রুম।",
        "sort_order": 2
      }
    ]
  }
}
```

---

## List All Room Categories

`GET /api/v1/room-categories`

Returns a paginated list of active (non-deleted) room categories. Each item includes all locale translations.

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
      "code": "STANDARD",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Standard Room",
          "description": "Comfortable room with essential amenities for everyday stays.",
          "sort_order": 1
        },
        {
          "id": 2,
          "locale_id": 2,
          "name": "স্ট্যান্ডার্ড রুম",
          "description": "দৈনন্দিন থাকার জন্য প্রয়োজনীয় সুবিধাসহ আরামদায়ক রুম।",
          "sort_order": 2
        }
      ]
    },
    {
      "id": 2,
      "code": "DELUXE",
      "sort_order": 2,
      "locales": [
        {
          "id": 3,
          "locale_id": 1,
          "name": "Deluxe Room",
          "description": "Spacious room with upgraded interior and additional facilities.",
          "sort_order": 1
        },
        {
          "id": 4,
          "locale_id": 2,
          "name": "ডিলাক্স রুম",
          "description": "উন্নত ইন্টেরিয়র ও অতিরিক্ত সুবিধাসহ প্রশস্ত রুম।",
          "sort_order": 2
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

## Update Room Category

`PUT /api/v1/room-categories/{id}`

Updates `sort_order`. The `code` field is set at creation time and cannot be changed. Locale translations are managed
via the room category locale endpoints.

### Path Parameters

| Parameter | Type | Description             |
|-----------|------|-------------------------|
| `id`      | Long | ID of the room category |

### Request Body

```json
{
  "sort_order": 3
}
```

### Request Fields

| Field        | Type    | Required | Validation |
|--------------|---------|----------|------------|
| `sort_order` | Integer | Yes      | Not null   |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Room Category

`DELETE /api/v1/room-categories/{id}`

Soft-deletes the room category. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description             |
|-----------|------|-------------------------|
| `id`      | Long | ID of the room category |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Room Category Locales

Room category locale endpoints manage per-locale translations for a room category. The `{room-category-id}` path
parameter must reference an existing, active room category.

---

### Create Room Category Locale

`POST /api/v1/room-categories/{room-category-id}/locales`

Adds a new locale translation to an existing room category.

#### Path Parameters

| Parameter          | Type | Description             |
|--------------------|------|-------------------------|
| `room-category-id` | Long | ID of the room category |

#### Request Body

```json
{
  "locale_id": 1,
  "name": "Deluxe Room",
  "description": "Spacious room with upgraded interior and additional facilities.",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

### Update Room Category Locale

`PUT /api/v1/room-categories/{room-category-id}/locales/{id}`

Updates an existing locale translation for a room category.

#### Path Parameters

| Parameter          | Type | Description                    |
|--------------------|------|--------------------------------|
| `room-category-id` | Long | ID of the room category        |
| `id`               | Long | ID of the room category locale |

#### Request Body

```json
{
  "name": "Deluxe Room",
  "description": "Updated description.",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

### Delete Room Category Locale

`DELETE /api/v1/room-categories/{room-category-id}/locales/{id}`

Soft-deletes a room category locale. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter          | Type | Description                    |
|--------------------|------|--------------------------------|
| `room-category-id` | Long | ID of the room category        |
| `id`               | Long | ID of the room category locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 3
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
  "message": "RoomCategory not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                            |
|-------------|----------------------------|----------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations                                 |
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or malformed request                                          |
| 404         | `ENTITY_NOT_FOUND`         | Room category, locale, or room category locale not found, or already deleted     |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate code or duplicate locale for same category) |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                                          |
