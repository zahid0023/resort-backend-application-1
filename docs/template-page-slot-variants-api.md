# Template Page Slot Variants API

Base URL: `/api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{template-page-slot-id}/template-page-slot-variants`

Template Page Slot Variants define which UI block definitions are available for a given slot. Each variant belongs to a template page slot and references a specific UI block definition. One variant may be marked as the default. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                                   | Description                          |
|--------|----------------------------------------------------------------------------------------|--------------------------------------|
| POST   | `.../template-page-slot-variants`       | Create a template page slot variant  |
| GET    | `.../template-page-slot-variants`       | List all template page slot variants |
| GET    | `.../template-page-slot-variants/{id}`  | Get a template page slot variant     |
| PUT    | `.../template-page-slot-variants/{id}`  | Update a template page slot variant  |
| DELETE | `.../template-page-slot-variants/{id}`  | Delete a template page slot variant  |

---

## Path Parameters

| Parameter               | Type | Required | Description                                                   |
|-------------------------|------|----------|---------------------------------------------------------------|
| `template-id`           | Long | Yes      | ID of the parent template (all endpoints)                     |
| `template-page-id`      | Long | Yes      | ID of the parent template page (all endpoints)                |
| `template-page-slot-id` | Long | Yes      | ID of the parent template page slot (all endpoints)           |
| `id`                    | Long | Yes      | ID of the template page slot variant (except list/create)     |

---

## Data Model

| Field                    | Type    | Required | Description                                                       |
|--------------------------|---------|----------|-------------------------------------------------------------------|
| `ui_block_definition_id` | Long    | Yes      | ID of the UI block definition available for this slot             |
| `display_order`          | Integer | No       | Display order of this variant within the slot                     |
| `is_default`             | Boolean | No       | Whether this variant is the default selection. Default: `false`   |

> `template_page_slot_id` is derived from the `{template-page-slot-id}` path parameter — it is not accepted in the request body.

---

## Create Template Page Slot Variant

`POST /api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{template-page-slot-id}/template-page-slot-variants`

### Request Body

```json
{
  "ui_block_definition_id": 5,
  "display_order": 1,
  "is_default": true
}
```

| Field                    | Type    | Required |
|--------------------------|---------|----------|
| `ui_block_definition_id` | Long    | Yes      |
| `display_order`          | Integer | No       |
| `is_default`             | Boolean | No       |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Template Page Slot Variant

`GET /api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{template-page-slot-id}/template-page-slot-variants/{id}`

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "template_page_slot_id": 3,
    "ui_block_definition_id": 5,
    "display_order": 1,
    "is_default": true
  }
}
```

---

## List All Template Page Slot Variants

`GET /api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{template-page-slot-id}/template-page-slot-variants`

Returns a paginated list of active (non-deleted) variants for the given slot.

### Query Parameters

| Parameter  | Type   | Default | Constraints              | Description              |
|------------|--------|---------|--------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                     | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                   | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `displayOrder`     | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`            | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "template_page_slot_id": 3,
      "ui_block_definition_id": 5,
      "display_order": 1,
      "is_default": true
    },
    {
      "id": 2,
      "template_page_slot_id": 3,
      "ui_block_definition_id": 7,
      "display_order": 2,
      "is_default": false
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

## Update Template Page Slot Variant

`PUT /api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{template-page-slot-id}/template-page-slot-variants/{id}`

All fields are optional — only provided fields are updated.

### Request Body

```json
{
  "display_order": 0,
  "is_default": false
}
```

| Field                    | Type    | Required |
|--------------------------|---------|----------|
| `ui_block_definition_id` | Long    | No       |
| `display_order`          | Integer | No       |
| `is_default`             | Boolean | No       |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Template Page Slot Variant

`DELETE /api/v1/templates/{template-id}/template-pages/{template-page-id}/template-page-slots/{template-page-slot-id}/template-page-slot-variants/{id}`

Soft-deletes the template page slot variant. The record is not removed from the database but will no longer appear in any response.

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
  "message": "Template Page Slot Variant with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                                                        |
|-------------|----------------------------|------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data                                       |
| 404         | `ENTITY_NOT_FOUND`         | Template, page, slot, or variant not found or already deleted                |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                                                         |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                                      |
