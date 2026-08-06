# Facility Group Facility Assignments API

Base URL: `/api/v1/facility-groups/{facility-group-id}/facility-assignments`

Facility group facility assignments record which facilities (e.g. `RESTAURANT`, `SPA`) belong to which
facility group (e.g. `DINING`, `WELLNESS`). A facility must belong to at least one facility group at creation
time (see [Facilities API](facilities-api.md)); this sub-resource is how a facility is attached to additional
groups afterward, or detached from one it no longer belongs to.

This is a pure membership/assignment resource: there is no locale sub-resource, no updatable fields, and no
filtering — an assignment either exists or doesn't. To change an assignment, unassign it and assign a new
one. All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions**, per this platform's global
rule — a request missing (or with a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT`
before it reaches any endpoint (see [Error Responses](#error-responses)). Its value has no effect on any of
these endpoints — `GET` (list) still resolves the embedded `facility.locale` using `Accept-Language` the
same way `GET /facilities` does, but the header's *presence* is what's enforced here, not a value this
resource itself branches on.

---

## Endpoints

| Method | Path                                                                     | Description                                    |
|--------|---------------------------------------------------------------------------|-----------------------------------------------------|
| GET    | `/api/v1/facility-groups/{facility-group-id}/facility-assignments`        | List facilities assigned to a facility group         |
| POST   | `/api/v1/facility-groups/{facility-group-id}/facility-assignments`        | Assign a facility to a facility group                |
| DELETE | `/api/v1/facility-groups/{facility-group-id}/facility-assignments/{id}`   | Unassign a facility from a facility group             |

---

## Data Model

### FacilityGroupFacilityAssignment

| Field      | Type   | Required | Constraints | Description                                                                                                                                                        |
|------------|--------|----------|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`       | Long   | —        | read-only   | Auto-generated identifier of the assignment row                                                                                                                     |
| `facility` | Object | —        | read-only   | The assigned facility — same shape as [Facilities](facilities-api.md)'s `Facility` data model, including its own `locale`-matched translation. Its `facility_groups` and `facility_scopes` are not re-embedded here. |

The parent `facility_group` is never re-embedded on each row — it's already known from the
`{facility-group-id}` path segment.

---

## List Facility Group Facility Assignments

`GET /api/v1/facility-groups/{facility-group-id}/facility-assignments`

Returns a paginated list of every facility currently assigned to a given facility group. This is the primary
way a client discovers "which facilities belong to this group" without fetching every facility in the system
and filtering client-side — equivalently, `GET /facilities?facilityGroupId={id}` returns the same set from the
facility side (see [List / Search Facilities](facilities-api.md#list--search-facilities)).

### Path Parameters

| Parameter          | Type | Description               |
|---------------------|------|--------------------------------|
| `facility-group-id` | Long | ID of the facility group       |

### Query Parameters

| Parameter | Type | Default | Constraints | Description               |
|-----------|------|---------|-------------|--------------------------------|
| `page`    | int  | `0`     | >= 0        | Zero-based page index          |
| `size`    | int  | `10`    | 1 – 50      | Number of items per page       |

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
      "facility": {
        "id": 1,
        "code": "RESTAURANT",
        "sort_order": 1,
        "icon_type": "LUCIDE",
        "icon_value": "UtensilsCrossed",
        "icon_meta": {
          "size": 24,
          "color": "#f59e0b"
        },
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Main Restaurant",
          "description": "Full-service restaurant with buffet and à la carte options.",
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

## Assign Facility

`POST /api/v1/facility-groups/{facility-group-id}/facility-assignments`

Assigns a facility to a facility group. Both must reference existing, active records — an unknown
`facility_id` returns `404 ENTITY_NOT_FOUND`. The combination of facility group and facility must be
unique — assigning a facility that's already assigned to this group returns `409 CONFLICT`. This check is
application-level only; there is no DB-level unique constraint on `(facility_group_id, facility_id)`.

### Path Parameters

| Parameter          | Type | Description               |
|---------------------|------|--------------------------------|
| `facility-group-id` | Long | ID of the facility group       |

### Request Body

```json
{
  "facility_id": 1
}
```

### Request Fields

| Field         | Type | Required | Validation                                     |
|---------------|------|----------|-------------------------------------------------|
| `facility_id` | Long | Yes      | Not null; must reference an existing facility    |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Unassign Facility

`DELETE /api/v1/facility-groups/{facility-group-id}/facility-assignments/{id}`

Soft-deletes an assignment. The record is not removed from the database but will no longer appear in any
response. Unassigning a facility's only remaining group does **not** automatically delete the facility or
block the operation — a facility can end up with zero groups this way.

### Path Parameters

| Parameter          | Type | Description               |
|---------------------|------|--------------------------------|
| `facility-group-id` | Long | ID of the facility group       |
| `id`                | Long | ID of the assignment row       |

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
  "message": "FacilityGroupFacilityAssignment not found with id: 99"
}
```

| HTTP Status | Error Code          | Cause                                                                                                                                                        |
|-------------|----------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`  | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs); an unsupported `sortBy` query value; or `facility_id` missing/null      |
| 404         | `ENTITY_NOT_FOUND`  | Facility group not found, the facility referenced by `facility_id` not found, or assignment not found                                                          |
| 409         | `CONFLICT`          | The facility is already assigned to this facility group (pre-checked at the application level)                                                                 |
