# Facility Scope Assignments API

Base URL: `/api/v1/facilities/{facility-id}/scope-assignments`

Facility scope assignments link a facility to one or more facility scopes (e.g. `RESORT`, `ROOM_CATEGORY`, `ROOM`),
defining where that facility can be applied. At least one scope must be provided when creating a facility. Additional
scopes can be assigned or unassigned individually at any time.

Soft-unassigning a scope hides it from all responses without removing the database record. Re-assigning a previously
unassigned scope reactivates the existing record rather than inserting a new one.

The `scope_assignments` array is also embedded in the `GET /api/v1/facilities/{id}` response.

---

## Endpoints

| Method | Path                                                                         | Description                   |
|--------|------------------------------------------------------------------------------|-------------------------------|
| POST   | `/api/v1/facilities/{facility-id}/scope-assignments`                         | Assign a scope to a facility  |
| DELETE | `/api/v1/facilities/{facility-id}/scope-assignments/{facility-scope-id}`     | Unassign a scope              |
| GET    | `/api/v1/facilities/{facility-id}/scope-assignments`                         | List scope assignments        |

---

## Data Model

### Facility Scope Assignment (response)

| Field               | Type    | Description                                             |
|---------------------|---------|---------------------------------------------------------|
| `facility_scope_id` | Long    | ID of the assigned facility scope                       |
| `code`              | String  | Scope code (e.g. `RESORT`, `ROOM_CATEGORY`, `ROOM`)     |
| `sort_order`        | Integer | Display order of the scope                              |
| `locales`           | Array   | Active locale translations of the scope (see below)     |

### Scope Locale (nested)

| Field         | Type    | Description                                    |
|---------------|---------|------------------------------------------------|
| `id`          | Long    | ID of the locale entry                         |
| `locale_id`   | Long    | ID of the locale                               |
| `name`        | String  | Localized name of the scope                    |
| `description` | String  | Localized description; omitted if null         |
| `sort_order`  | Integer | Display order of this locale entry             |

---

## Assign Scope

`POST /api/v1/facilities/{facility-id}/scope-assignments`

Assigns a facility scope to the given facility. If the scope was previously unassigned (soft-deleted), the assignment is
reactivated. If the scope is already actively assigned, a `409 CONFLICT` error is returned.

### Path Parameters

| Parameter     | Type | Description        |
|---------------|------|--------------------|
| `facility-id` | Long | ID of the facility |

### Request Fields

| Field               | Type | Required | Validation           |
|---------------------|------|----------|----------------------|
| `facility_scope_id` | Long | Yes      | Not null, must exist |

### Request Body

```json
{
  "facility_scope_id": 2
}
```

### Response `201 Created`

Returns the `facility_id` as `id`.

```json
{
  "success": true,
  "id": 1
}
```

---

## Unassign Scope

`DELETE /api/v1/facilities/{facility-id}/scope-assignments/{facility-scope-id}`

Soft-removes the scope assignment. The record is not deleted from the database but will no longer appear in any
response.

### Path Parameters

| Parameter           | Type | Description              |
|---------------------|------|--------------------------|
| `facility-id`       | Long | ID of the facility       |
| `facility-scope-id` | Long | ID of the facility scope |

### Response `200 OK`

Returns the `facility_id` as `id`.

```json
{
  "success": true,
  "id": 1
}
```

---

## List Scope Assignments

`GET /api/v1/facilities/{facility-id}/scope-assignments`

Returns all active scope assignments for the given facility. Each entry includes the scope's code, sort order, and all
active locale translations.

### Path Parameters

| Parameter     | Type | Description        |
|---------------|------|--------------------|
| `facility-id` | Long | ID of the facility |

### Response `200 OK`

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
  },
  {
    "facility_scope_id": 3,
    "code": "ROOM",
    "sort_order": 3,
    "locales": [
      {
        "id": 3,
        "locale_id": 1,
        "name": "Room",
        "sort_order": 3
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
  "message": "FacilityScopeAssignment not found for facilityId: 1 and facilityScopeId: 99"
}
```

| HTTP Status | Error Code              | Cause                                                                         |
|-------------|-------------------------|-------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`      | Missing or null `facility_scope_id`                                           |
| 404         | `ENTITY_NOT_FOUND`      | Facility or facility scope not found / deleted                                |
| 404         | `ENTITY_NOT_FOUND`      | Assignment not found on unassign (not assigned or already unassigned)         |
| 409         | `CONFLICT`              | Scope is already actively assigned to this facility                           |
| 500         | `INTERNAL_SERVER_ERROR` | Unexpected server error                                                       |
