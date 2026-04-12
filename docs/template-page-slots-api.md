# Template Page Slots API

Base URL: `/api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots`

Template Page Slots represent content areas (slots) within a template page. Each slot belongs to a template page, is associated with a UI block category, and can have one or more slot variants defining which UI block definitions are available for that slot. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                                                                                          | Description                  |
|--------|-----------------------------------------------------------------------------------------------------------------------------------------------|------------------------------|
| POST   | `/api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots`                                                      | Create a template page slot  |
| GET    | `/api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots`                                                      | List all template page slots |
| GET    | `/api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{id}`                                                  | Get a template page slot     |
| PUT    | `/api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{id}`                                                  | Update a template page slot  |
| DELETE | `/api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{id}`                                                  | Delete a template page slot  |

---

## Path Parameters

| Parameter          | Type | Required | Description                                              |
|--------------------|------|----------|----------------------------------------------------------|
| `template-id`      | Long | Yes      | ID of the parent template (all endpoints)                |
| `template-page-id` | Long | Yes      | ID of the parent template page (all endpoints)           |
| `id`               | Long | Yes      | ID of the template page slot (except list/create)        |

---

## Data Model

| Field                  | Type    | Required | Description                                              |
|------------------------|---------|----------|----------------------------------------------------------|
| `ui_block_category_id` | Long    | Yes      | ID of the UI block category allowed for this slot        |
| `slot_name`            | String  | Yes      | Display name of the slot (max 150 characters)            |
| `is_required`          | Boolean | No       | Whether this slot must be filled. Default: `false`       |
| `slot_order`           | Integer | Yes      | Display order of the slot within the page                |

> `template_page_id` is derived from the `{template-page-id}` path parameter — it is not accepted in the request body.

---

## Create Template Page Slot

`POST /api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots`

### Request Body

```json
{
  "ui_block_category_id": 3,
  "slot_name": "Hero Banner",
  "is_required": true,
  "slot_order": 1
}
```

| Field                  | Type    | Required |
|------------------------|---------|----------|
| `ui_block_category_id` | Long    | Yes      |
| `slot_name`            | String  | Yes      |
| `is_required`          | Boolean | No       |
| `slot_order`           | Integer | Yes      |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Template Page Slot

`GET /api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{id}`

Returns the template page slot along with its nested slot variants.

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "template_page_id": 1,
    "ui_block_category_id": 3,
    "slot_name": "Hero Banner",
    "is_required": true,
    "slot_order": 1,
    "template_page_slot_variants": [
      {
        "id": 1,
        "template_page_slot_id": 1,
        "ui_block_definition_id": 5,
        "display_order": 1,
        "is_default": true
      }
    ]
  }
}
```

---

## List All Template Page Slots

`GET /api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots`

Returns a paginated list of active (non-deleted) template page slots for the given template page. Nested slot variants are omitted.

### Query Parameters

| Parameter  | Type   | Default | Constraints                       | Description              |
|------------|--------|---------|-----------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                              | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                            | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `slotName`, `slotOrder`     | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                     | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "template_page_id": 1,
      "ui_block_category_id": 3,
      "slot_name": "Hero Banner",
      "is_required": true,
      "slot_order": 1
    },
    {
      "id": 2,
      "template_page_id": 1,
      "ui_block_category_id": 4,
      "slot_name": "Featured Rooms",
      "is_required": false,
      "slot_order": 2
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

## Update Template Page Slot

`PUT /api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{id}`

All fields are optional — only provided fields are updated.

### Request Body

```json
{
  "slot_name": "Hero Banner V2",
  "slot_order": 0
}
```

| Field                  | Type    | Required |
|------------------------|---------|----------|
| `ui_block_category_id` | Long    | No       |
| `slot_name`            | String  | No       |
| `is_required`          | Boolean | No       |
| `slot_order`           | Integer | No       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Template Page Slot

`DELETE /api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{id}`

Soft-deletes the template page slot. The record is not removed from the database but will no longer appear in any response.

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
  "message": "TemplatePageSlotEntity not found"
}
```

| HTTP Status | Error Code                 | Cause                                                              |
|-------------|----------------------------|--------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data                             |
| 404         | `ENTITY_NOT_FOUND`         | Template, template page, or slot not found or already deleted      |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                                               |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                            |
