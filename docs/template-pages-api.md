# Template Pages API

Base URL: `/api/v1/templates/{template-id}/template-pages`

Template Pages represent individual pages within a template (e.g. Home, Rooms, Events). Every template page belongs to a specific template, identified by `{template-id}` in the path. Each page is also linked to a page type. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                              | Description             |
|--------|-------------------------------------------------------------------|-------------------------|
| POST   | `/api/v1/templates/{template-id}/template-pages`                  | Create a template page  |
| GET    | `/api/v1/templates/{template-id}/template-pages`                  | List all template pages |
| GET    | `/api/v1/templates/{template-id}/template-pages/{id}`             | Get a template page     |
| PUT    | `/api/v1/templates/{template-id}/template-pages/{id}`             | Update a template page  |
| DELETE | `/api/v1/templates/{template-id}/template-pages/{id}`             | Delete a template page  |

---

## Path Parameters

| Parameter     | Type | Required | Description                               |
|---------------|------|----------|-------------------------------------------|
| `template-id` | Long | Yes      | ID of the parent template (all endpoints) |
| `id`          | Long | Yes      | ID of the template page (except list/create) |

---

## Data Model

| Field          | Type    | Required | Description                                       |
|----------------|---------|----------|---------------------------------------------------|
| `page_type_id` | Long    | Yes      | ID of the page type                               |
| `page_name`    | String  | Yes      | Display name of the page (max 150)                |
| `page_slug`    | String  | Yes      | URL-safe slug for the page, e.g. `home`, `rooms` (max 150) |
| `page_order`   | Integer | Yes      | Display order of the page within the template     |

> `template_id` is derived from the `{template-id}` path parameter — it is not accepted in the request body.

---

## Create Template Page

`POST /api/v1/templates/{template-id}/template-pages`

### Request Body

```json
{
  "page_type_id": 2,
  "page_name": "Home",
  "page_slug": "home",
  "page_order": 1
}
```

| Field          | Type    | Required |
|----------------|---------|----------|
| `page_type_id` | Long    | Yes      |
| `page_name`    | String  | Yes      |
| `page_slug`    | String  | Yes      |
| `page_order`   | Integer | Yes      |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Template Page

`GET /api/v1/templates/{template-id}/template-pages/{id}`

Returns the template page along with its nested slots and slot variants.

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "template_id": 1,
    "page_type_id": 2,
    "page_name": "Home",
    "page_slug": "home",
    "page_order": 1,
    "template_page_slots": [
      {
        "id": 1,
        "template_page_id": 1,
        "ui_block_category_id": 3,
        "slot_name": "Hero Banner",
        "is_required": true,
        "slot_order": 1,
        "template_page_slot_variants": []
      }
    ]
  }
}
```

---

## List All Template Pages

`GET /api/v1/templates/{template-id}/template-pages`

Returns a paginated list of active (non-deleted) template pages. Nested relations are omitted (summary projection).

### Query Parameters

| Parameter  | Type   | Default      | Constraints                                 | Description              |
|------------|--------|--------------|---------------------------------------------|--------------------------|
| `page`     | int    | `0`          | >= 0                                        | Zero-based page index    |
| `size`     | int    | `10`         | 1 – 50                                      | Number of items per page |
| `sort_by`  | String | `id`         | `id`, `pageName`, `pageSlug`, `pageOrder`   | Field to sort by         |
| `sort_dir` | String | `ASC`        | `ASC`, `DESC`                               | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "template_id": 1,
      "page_type_id": 2,
      "page_name": "Home",
      "page_slug": "home",
      "page_order": 1
    },
    {
      "id": 2,
      "template_id": 1,
      "page_type_id": 3,
      "page_name": "Rooms",
      "page_slug": "rooms",
      "page_order": 2
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

## Update Template Page

`PUT /api/v1/templates/{template-id}/template-pages/{id}`

All fields are optional — only provided fields are updated.

### Request Body

```json
{
  "page_name": "Home V2",
  "page_order": 0
}
```

| Field          | Type    | Required |
|----------------|---------|----------|
| `page_type_id` | Long    | No       |
| `page_name`    | String  | No       |
| `page_slug`    | String  | No       |
| `page_order`   | Integer | No       |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "template_id": 1,
    "page_type_id": 2,
    "page_name": "Home V2",
    "page_slug": "home",
    "page_order": 0
  }
}
```

---

## Delete Template Page

`DELETE /api/v1/templates/{template-id}/template-pages/{id}`

Soft-deletes the template page. The record is not removed from the database but will no longer appear in any response.

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
  "message": "Template Page with id: 99 was not found."
}
```

| HTTP Status | Error Code                 | Cause                                          |
|-------------|----------------------------|------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Invalid sort field or bad request data         |
| 404         | `ENTITY_NOT_FOUND`         | Template or template page not found / deleted  |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate slug)     |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                        |
