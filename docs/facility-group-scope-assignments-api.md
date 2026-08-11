# Facility Group Scope Assignments API

Base URL: `/api/v1/facility-groups/{facility-group-id}/scope-assignments`

Facility group scope assignments record which [facility scopes](facility-scopes-api.md) (e.g. `RESORT`,
`ROOM_CATEGORY`, `ROOM`) a given facility group (e.g. `DINING`, `WELLNESS`, `RECREATION`, `ACCOMMODATION`)
applies at. A facility group can be assigned to more than one scope — for example, `DINING` may apply at the
resort, room category, and room scopes, while `ACCOMMODATION` may apply only at the room category and room
scopes.

This is a pure membership/assignment resource: there is no locale sub-resource, no updatable fields, and no
filtering — an assignment either exists or doesn't. To change an assignment, unassign it and assign a new
one. All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions**, per this platform's global
rule — a request missing (or with a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT`
before it reaches any endpoint (see [Error Responses](#error-responses)). Its value has no effect on either
endpoint below — the header's *presence* is what's enforced here, not a value this resource itself branches
on. To see which facility scopes a facility group currently has assigned, use `GET /facility-groups/{id}`
(see [Facility Groups](facility-groups-api.md)), which embeds the full list.

---

## Endpoints

| Method | Path                                                                                | Description                            |
|--------|-------------------------------------------------------------------------------------|----------------------------------------|
| POST   | `/api/v1/facility-groups/{facility-group-id}/scope-assignments`                     | Assign a scope to a facility group     |
| DELETE | `/api/v1/facility-groups/{facility-group-id}/scope-assignments/{facility-scope-id}` | Unassign a scope from a facility group |

---

## Assign Scope

`POST /api/v1/facility-groups/{facility-group-id}/scope-assignments`

Assigns a facility scope to a facility group. Both must reference existing, active records — an unknown
`facility_scope_id` returns `404 ENTITY_NOT_FOUND`. The combination of facility group and facility scope must
be unique — assigning a scope that's already assigned to this facility group returns `409 CONFLICT`. This
check is application-level only; there is no DB-level unique constraint on `(facility_group_id, facility_scope_id)`.

### Path Parameters

| Parameter           | Type | Description              |
|---------------------|------|--------------------------|
| `facility-group-id` | Long | ID of the facility group |

### Request Body

```json
{
  "facility_scope_id": 1
}
```

### Request Fields

| Field               | Type | Required | Validation                                          |
|---------------------|------|----------|-----------------------------------------------------|
| `facility_scope_id` | Long | Yes      | Not null; must reference an existing facility scope |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Unassign Scope

`DELETE /api/v1/facility-groups/{facility-group-id}/scope-assignments/{facility-scope-id}`

Soft-deletes the assignment between this facility group and the given facility scope. Identified by the
facility scope's own id rather than the assignment row's id — since a facility group can have at most one
active, non-deleted assignment to a given facility scope (enforced at the application level on
[Assign Scope](#assign-scope)), `(facility_group_id, facility_scope_id)` is always enough to uniquely
identify it. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter           | Type | Description              |
|---------------------|------|--------------------------|
| `facility-group-id` | Long | ID of the facility group |
| `facility-scope-id` | Long | ID of the facility scope |

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
  "message": "FacilityGroupScopeAssignment not found for FacilityGroup '3' and FacilityScope '99'"
}
```

| HTTP Status | Error Code         | Cause                                                                                                                                                                                       |
|-------------|--------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT` | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs); or `facility_scope_id` missing/null                                                                 |
| 404         | `ENTITY_NOT_FOUND` | Facility group not found, the facility scope referenced by `facility_scope_id` not found (assign), or no active assignment exists between this facility group and facility scope (unassign) |
| 409         | `CONFLICT`         | The facility scope is already assigned to this facility group (pre-checked at the application level)                                                                                        |
