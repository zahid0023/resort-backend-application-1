# UI Block Definitions API

Base URL: `/api/v1/ui-block-definitions`

UI Block Definitions describe the structure, schema, and default content of a UI block used within the resort application. Each definition is linked to a UI block category and a page type. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                     | Description                   |
|--------|------------------------------------------|-------------------------------|
| POST   | `/api/v1/ui-block-definitions`           | Create a UI block definition  |
| GET    | `/api/v1/ui-block-definitions`           | List all UI block definitions |
| GET    | `/api/v1/ui-block-definitions/{id}`      | Get a UI block definition     |
| PUT    | `/api/v1/ui-block-definitions/{id}`      | Update a UI block definition  |
| DELETE | `/api/v1/ui-block-definitions/{id}`      | Delete a UI block definition  |

---

## Data Model

| Field                  | Type              | Required | Default   | Max Length | Description                                              |
|------------------------|-------------------|----------|-----------|------------|----------------------------------------------------------|
| `ui_block_key`         | String            | Yes      | —         | 100        | Unique identifier key for the block                      |
| `name`                 | String            | Yes      | —         | 150        | Display name                                             |
| `description`          | String            | Yes      | —         | unlimited  | Detailed description of the block                        |
| `ui_block_version`     | String            | No       | `"1.0.0"` | 20         | Semantic version of the block definition                 |
| `ui_block_category_id` | Long              | Yes      | —         | —          | ID of the associated UI block category                   |
| `page_type_id`         | Long              | Yes      | —         | —          | ID of the associated page type                           |
| `editable_schema`      | Object (JSON)     | Yes      | —         | —          | JSON schema describing the editable fields of the block  |
| `default_content`      | Object (JSON)     | Yes      | —         | —          | Default content values for the block                     |
| `allowed_pages`        | Array of Strings  | No       | —         | —          | List of page slugs or identifiers where the block can be used |
| `is_accepted`          | Boolean           | Yes      | —         | —          | Acceptance flag; stored as `"Accepted"` or `"Rejected"` in `status` |
| `status` *(response)*  | String            | —        | —         | 20         | Derived from `is_accepted`: `"Accepted"` or `"Rejected"` |

---

## Create UI Block Definition

`POST /api/v1/ui-block-definitions`

### Request Body

```json
{
  "ui_block_key": "HERO_BANNER",
  "name": "Hero Banner",
  "description": "A full-width hero banner with a title, subtitle, and CTA button.",
  "ui_block_version": "1.0.0",
  "ui_block_category_id": 1,
  "page_type_id": 2,
  "editable_schema": {
    "title": { "type": "string", "maxLength": 100 },
    "subtitle": { "type": "string", "maxLength": 200 },
    "cta_label": { "type": "string", "maxLength": 50 }
  },
  "default_content": {
    "title": "Welcome to Paradise",
    "subtitle": "Experience luxury like never before.",
    "cta_label": "Book Now"
  },
  "allowed_pages": ["home", "landing"],
  "is_accepted": false
}
```

| Field                  | Type             | Required |
|------------------------|------------------|----------|
| `ui_block_key`         | String           | Yes      |
| `name`                 | String           | Yes      |
| `description`          | String           | Yes      |
| `ui_block_version`     | String           | No       |
| `ui_block_category_id` | Long             | Yes      |
| `page_type_id`         | Long             | Yes      |
| `editable_schema`      | Object (JSON)    | Yes      |
| `default_content`      | Object (JSON)    | Yes      |
| `allowed_pages`        | Array of Strings | No       |
| `is_accepted`          | Boolean          | Yes      |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get UI Block Definition

`GET /api/v1/ui-block-definitions/{id}`

### Path Parameters

| Parameter | Type | Description                      |
|-----------|------|----------------------------------|
| `id`      | Long | ID of the UI block definition    |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "ui_block_key": "HERO_BANNER",
    "name": "Hero Banner",
    "description": "A full-width hero banner with a title, subtitle, and CTA button.",
    "ui_block_version": "1.0.0",
    "ui_block_category_id": 1,
    "page_type_id": 2,
    "editable_schema": {
      "title": { "type": "string", "maxLength": 100 },
      "subtitle": { "type": "string", "maxLength": 200 },
      "cta_label": { "type": "string", "maxLength": 50 }
    },
    "default_content": {
      "title": "Welcome to Paradise",
      "subtitle": "Experience luxury like never before.",
      "cta_label": "Book Now"
    },
    "allowed_pages": ["home", "landing"],
    "status": "Rejected"
  }
}
```

---

## List All UI Block Definitions

`GET /api/v1/ui-block-definitions`

Returns a paginated list of active (non-deleted) UI block definitions.

### Query Parameters

| Parameter  | Type   | Default | Constraints                    | Description              |
|------------|--------|---------|--------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                           | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                         | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `uiBlockKey`, `name`     | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                  | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "ui_block_key": "HERO_BANNER",
      "name": "Hero Banner",
      "description": "A full-width hero banner with a title, subtitle, and CTA button.",
      "ui_block_version": "1.0.0",
      "ui_block_category_id": 1,
      "page_type_id": 2,
      "editable_schema": {
        "title": { "type": "string", "maxLength": 100 }
      },
      "default_content": {
        "title": "Welcome to Paradise"
      },
      "allowed_pages": ["home", "landing"],
      "status": "Rejected"
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update UI Block Definition

`PUT /api/v1/ui-block-definitions/{id}`

All fields are optional — only provided fields are updated.

### Path Parameters

| Parameter | Type | Description                      |
|-----------|------|----------------------------------|
| `id`      | Long | ID of the UI block definition    |

### Request Body

```json
{
  "name": "Hero Banner v2",
  "ui_block_version": "2.0.0",
  "is_accepted": true
}
```

| Field                  | Type             | Required |
|------------------------|------------------|----------|
| `ui_block_key`         | String           | No       |
| `name`                 | String           | No       |
| `description`          | String           | No       |
| `ui_block_version`     | String           | No       |
| `ui_block_category_id` | Long             | No       |
| `page_type_id`         | Long             | No       |
| `editable_schema`      | Object (JSON)    | No       |
| `default_content`      | Object (JSON)    | No       |
| `allowed_pages`        | Array of Strings | No       |
| `is_accepted`          | Boolean          | No       |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "ui_block_key": "HERO_BANNER",
    "name": "Hero Banner v2",
    "description": "A full-width hero banner with a title, subtitle, and CTA button.",
    "ui_block_version": "2.0.0",
    "ui_block_category_id": 1,
    "page_type_id": 2,
    "editable_schema": {
      "title": { "type": "string", "maxLength": 100 },
      "subtitle": { "type": "string", "maxLength": 200 },
      "cta_label": { "type": "string", "maxLength": 50 }
    },
    "default_content": {
      "title": "Welcome to Paradise",
      "subtitle": "Experience luxury like never before.",
      "cta_label": "Book Now"
    },
    "allowed_pages": ["home", "landing"],
    "status": "Accepted"
  }
}
```

---

## Delete UI Block Definition

`DELETE /api/v1/ui-block-definitions/{id}`

Soft-deletes the UI block definition. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description                      |
|-----------|------|----------------------------------|
| `id`      | Long | ID of the UI block definition    |

### Response `200 OK`

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
  "message": "UI Block Definition with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                                         |
|-------------|----------------------------|---------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data                        |
| 404         | `ENTITY_NOT_FOUND`         | Definition not found, or the referenced category/page type does not exist |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate `ui_block_key`)          |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                       |
