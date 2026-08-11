# Facility Group Assignments API

Base URL: `/api/v1/facilities/{facility-id}/group-assignments`

Facility group assignments record which [facility groups](facility-groups-api.md) (e.g. `DINING`, `WELLNESS`)
a given facility (e.g. `RESTAURANT`, `SPA`) belongs to. A facility can belong to more than one facility group.

This is a pure membership/assignment resource: there is no locale sub-resource, no updatable fields, and no
filtering — an assignment either exists or doesn't. To change an assignment, unassign it and assign a new
one. All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions**, per this platform's global
rule — a request missing (or with a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT`
before it reaches any endpoint (see [Error Responses](#error-responses)). Its value has no effect on either
endpoint below — the header's *presence* is what's enforced here, not a value this resource itself branches
on. To see which facility groups a facility currently belongs to, use `GET /facilities/{id}` (see
[Facilities](facilities-api.md)), which embeds the full list.

---

## Endpoints

| Method | Path                                                                     | Description                               |
|--------|--------------------------------------------------------------------------|-------------------------------------------|
| POST   | `/api/v1/facilities/{facility-id}/group-assignments`                     | Assign a facility group to a facility     |
| DELETE | `/api/v1/facilities/{facility-id}/group-assignments/{facility-group-id}` | Unassign a facility group from a facility |

---

## Assign Facility Group

`POST /api/v1/facilities/{facility-id}/group-assignments`

Assigns a facility group to a facility. Both must reference existing, active records — an unknown
`facility_group_id` returns `404 ENTITY_NOT_FOUND`. The combination of facility and facility group must be
unique — assigning a group that's already assigned to this facility returns `409 CONFLICT`. This check is
application-level only; there is no DB-level unique constraint on `(facility_id, facility_group_id)`.

**The facility group must itself be scoped (see
[Facility Group Scope Assignments](facility-group-scope-assignments-api.md)) to every facility scope the
facility is currently assigned to.** For example, a facility assigned to the `RESORT` scope cannot be added
to a facility group that is only scoped to `ROOM_CATEGORY`/`ROOM`. Violating this also returns `409 CONFLICT`.

### Path Parameters

| Parameter     | Type | Description        |
|---------------|------|--------------------|
| `facility-id` | Long | ID of the facility |

### Request Body

```json
{
  "facility_group_id": 1
}
```

### Request Fields

| Field               | Type | Required | Validation                                          |
|---------------------|------|----------|-----------------------------------------------------|
| `facility_group_id` | Long | Yes      | Not null; must reference an existing facility group |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Unassign Facility Group

`DELETE /api/v1/facilities/{facility-id}/group-assignments/{facility-group-id}`

Soft-deletes the assignment between this facility and the given facility group. Identified by the facility
group's own id rather than the assignment row's id — since a facility can have at most one active,
non-deleted assignment to a given facility group (enforced at the application level on
[Assign Facility Group](#assign-facility-group)), `(facility_id, facility_group_id)` is always enough to
uniquely identify it. The record is not removed from the database but will no longer appear in any response.
Unassigning a facility's only remaining group does **not** automatically delete the facility or block the
operation — a facility can end up with zero groups this way.

### Path Parameters

| Parameter           | Type | Description              |
|---------------------|------|--------------------------|
| `facility-id`       | Long | ID of the facility       |
| `facility-group-id` | Long | ID of the facility group |

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
  "message": "FacilityFacilityGroupAssignment not found for Facility '3' and FacilityGroup '99'"
}
```

| HTTP Status | Error Code         | Cause                                                                                                                                                                                             |
|-------------|--------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT` | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs); or `facility_group_id` missing/null                                                                       |
| 404         | `ENTITY_NOT_FOUND` | Facility not found, the facility group referenced by `facility_group_id` not found (assign), or no active assignment exists between this facility and facility group (unassign)                   |
| 409         | `CONFLICT`         | The facility group is already assigned to this facility (pre-checked at the application level); or the facility group is not scoped to every facility scope the facility is currently assigned to |
