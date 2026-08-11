# Price Unit Scope Assignments API

Base URL: `/api/v1/price-units/{price-unit-id}/scope-assignments`

Price unit scope assignments record which [price scopes](price-scopes-api.md) (e.g. `ROOM_CATEGORY`, `ROOM`,
`RESORT_FACILITY`) a given price unit (e.g. `PER_NIGHT`, `PER_DAY`, `PER_HOUR`, `PER_PERSON`, `PER_ROOM`,
`PER_BOOKING`) applies at. A price unit can be assigned to more than one scope — for example, a per-night
price unit may apply to both room category and room scopes, while a per-hour price unit may apply only to
the resort facility scope.

This is a pure membership/assignment resource: there is no locale sub-resource, no updatable fields, and no
filtering — an assignment either exists or doesn't. To change an assignment, unassign it and assign a new
one. All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions**, per this platform's global
rule — a request missing (or with a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT`
before it reaches any endpoint (see [Error Responses](#error-responses)). Its value has no effect on either
endpoint below — the header's *presence* is what's enforced here, not a value this resource itself branches
on. To see which price scopes a price unit currently has assigned, use `GET /price-units/{id}` (see
[Price Units](price-units-api.md)), which embeds the full list.

---

## Endpoints

| Method | Path                                                                     | Description                        |
|--------|---------------------------------------------------------------------------|-------------------------------------|
| POST   | `/api/v1/price-units/{price-unit-id}/scope-assignments`                  | Assign a scope to a price unit     |
| DELETE | `/api/v1/price-units/{price-unit-id}/scope-assignments/{price-scope-id}` | Unassign a scope from a price unit |

---

## Assign Scope

`POST /api/v1/price-units/{price-unit-id}/scope-assignments`

Assigns a price scope to a price unit. Both must reference existing, active records — an unknown
`price_scope_id` returns `404 ENTITY_NOT_FOUND`. The combination of price unit and price scope must be
unique — assigning a scope that's already assigned to this price unit returns `409 CONFLICT`. This check is
application-level only; there is no DB-level unique constraint on `(price_unit_id, price_scope_id)`.

### Path Parameters

| Parameter       | Type | Description          |
|-----------------|------|-----------------------|
| `price-unit-id` | Long | ID of the price unit |

### Request Body

```json
{
  "price_scope_id": 1
}
```

### Request Fields

| Field            | Type | Required | Validation                                       |
|------------------|------|----------|----------------------------------------------------|
| `price_scope_id` | Long | Yes      | Not null; must reference an existing price scope |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Unassign Scope

`DELETE /api/v1/price-units/{price-unit-id}/scope-assignments/{price-scope-id}`

Soft-deletes the assignment between this price unit and the given price scope. Identified by the price
scope's own id rather than the assignment row's id — since a price unit can have at most one active,
non-deleted assignment to a given price scope (enforced at the application level on
[Assign Scope](#assign-scope)), `(price_unit_id, price_scope_id)` is always enough to uniquely identify it.
The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter        | Type | Description            |
|-------------------|------|-------------------------|
| `price-unit-id`   | Long | ID of the price unit   |
| `price-scope-id`  | Long | ID of the price scope  |

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
  "message": "PriceUnitScopeAssignment not found for PriceUnit '3' and PriceScope '99'"
}
```

| HTTP Status | Error Code         | Cause                                                                                                                                                                    |
|-------------|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT` | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs); or `price_scope_id` missing/null                                               |
| 404         | `ENTITY_NOT_FOUND` | Price unit not found, the price scope referenced by `price_scope_id` not found (assign), or no active assignment exists between this price unit and price scope (unassign) |
| 409         | `CONFLICT`         | The price scope is already assigned to this price unit (pre-checked at the application level)                                                                          |
