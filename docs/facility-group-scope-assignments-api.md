# Facility Group Scope Assignments API

Base URL: `/api/v1/facility-scopes/{facility-scope-id}/group-assignments`

Facility group scope assignments record which facility groups (e.g. `DINING`, `WELLNESS`) apply at which
facility scope (e.g. `RESORT`, `ROOM_CATEGORY`, `ROOM`). This lets a resort owner fetch only the facility
groups relevant to the scope they're currently working in — a resort, or a room category — instead of being
overwhelmed with every facility group in the system.

This is a pure membership/assignment resource: there is no locale sub-resource, no updatable fields, and no
filtering — an assignment either exists or doesn't. To change an assignment, unassign it and assign a new
one. All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions**, per this platform's global
rule — a request missing (or with a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT`
before it reaches any endpoint (see [Error Responses](#error-responses)). Its value has no effect on any of
these endpoints — `GET` (list) still resolves each embedded `facility_group.locale` using
`Accept-Language` the same way `GET /facility-groups` does, but the header's *presence* is what's enforced
here, not a value this resource itself branches on.

---

## Endpoints

| Method | Path                                                                | Description                                  |
|--------|-----------------------------------------------------------------------|-------------------------------------------------|
| GET    | `/api/v1/facility-scopes/{facility-scope-id}/group-assignments`       | List facility groups assigned to a scope         |
| POST   | `/api/v1/facility-scopes/{facility-scope-id}/group-assignments`       | Assign a facility group to a scope               |
| DELETE | `/api/v1/facility-scopes/{facility-scope-id}/group-assignments/{id}`  | Unassign a facility group from a scope           |

---

## Data Model

### FacilityGroupScopeAssignment

| Field           | Type   | Required | Constraints | Description                                                              |
|------------------|--------|----------|---------------|-----------------------------------------------------------------------------|
| `id`             | Long   | —        | read-only     | Auto-generated identifier of the assignment row                             |
| `facility_group` | Object | —        | read-only     | The assigned facility group — same shape as [Facility Groups](facility-groups-api.md)'s `FacilityGroup` data model, including its own `locale`-matched translation |

The parent `facility_scope` is never re-embedded on each row — it's already known from the
`{facility-scope-id}` path segment.

---

## List Facility Group Scope Assignments

`GET /api/v1/facility-scopes/{facility-scope-id}/group-assignments`

Returns a paginated list of every facility group assigned to a given facility scope. This is the primary way
a client discovers "which facility groups apply here" for a resort (`RESORT` scope) or a room category
(`ROOM_CATEGORY` scope), without fetching every facility group in the system.

### Path Parameters

| Parameter            | Type | Description               |
|------------------------|------|-------------------------------|
| `facility-scope-id`    | Long | ID of the facility scope     |

### Query Parameters

| Parameter | Type | Default | Constraints | Description               |
|-----------|------|---------|---------------|-------------------------------|
| `page`    | int  | `0`     | >= 0          | Zero-based page index         |
| `size`    | int  | `10`    | 1 – 50        | Number of items per page      |

> **Note:** `sortBy`/`sortDir` are accepted on the request object but there are no sortable fields
> registered for this endpoint — passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default
> (sorted by `id` ascending).

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "facility_group": {
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
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Dining",
          "description": "All food and beverage outlets including restaurants, bars, and room service.",
          "sort_order": 1
        }
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": null,
  "searchable_fields": null
}
```

---

## Assign Facility Group

`POST /api/v1/facility-scopes/{facility-scope-id}/group-assignments`

Assigns a facility group to a facility scope. Both must reference existing, active records — an unknown
`facility_group_id` returns `404 ENTITY_NOT_FOUND`. The combination of facility scope and facility group must
be unique — assigning a group that's already assigned to this scope returns `409 CONFLICT`. This check is
application-level only; there is no DB-level unique constraint on `(facility_scope_id, facility_group_id)`.

### Path Parameters

| Parameter            | Type | Description               |
|------------------------|------|-------------------------------|
| `facility-scope-id`    | Long | ID of the facility scope     |

### Request Body

```json
{
  "facility_group_id": 1
}
```

### Request Fields

| Field                | Type | Required | Validation                                    |
|-----------------------|------|----------|---------------------------------------------------|
| `facility_group_id`   | Long | Yes      | Not null; must reference an existing facility group |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Unassign Facility Group

`DELETE /api/v1/facility-scopes/{facility-scope-id}/group-assignments/{id}`

Soft-deletes an assignment. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter            | Type | Description               |
|------------------------|------|-------------------------------|
| `facility-scope-id`    | Long | ID of the facility scope     |
| `id`                   | Long | ID of the assignment row     |

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
  "message": "FacilityGroupScopeAssignment not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                     |
|-------------|-----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs); an unsupported `sortBy` query value; or `facility_group_id` missing/null |
| 404         | `ENTITY_NOT_FOUND`         | Facility scope not found, the facility group referenced by `facility_group_id` not found, or assignment not found                          |
| 409         | `CONFLICT`                 | The facility group is already assigned to this facility scope (pre-checked at the application level)                                         |
