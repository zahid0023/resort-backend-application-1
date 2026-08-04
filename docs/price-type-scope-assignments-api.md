# Price Type Scope Assignments API

Base URL: `/api/v1/price-type-scopes/{price-type-scope-id}/price-type-assignments`

Price type scope assignments record which price types (e.g. `BAS`, `WKD`, `WKE`, `HOL`, `SPECIAL`) apply at
which price type scope (e.g. `ROOM_CATEGORY`, `ROOM`, `RESORT_FACILITY`). A price type can be assigned to
more than one scope — for example, a per-day price type may apply to both room category and room scopes,
while a free/included price type may apply only to the resort facility scope.

This is a pure membership/assignment resource: there is no locale sub-resource, no updatable fields, and no
filtering — an assignment either exists or doesn't. To change an assignment, unassign it and assign a new
one. All records support soft-delete — deleted records are hidden from all responses.

**`Accept-Language` is required on every endpoint below, with no exceptions**, per this platform's global
rule — a request missing (or with a blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT`
before it reaches any endpoint (see [Error Responses](#error-responses)). Its value has no effect on any of
these endpoints — `GET` (list) still resolves each embedded `price_type.locale` using `Accept-Language` the
same way `GET /price-types` does, but the header's *presence* is what's enforced here, not a value this
resource itself branches on.

---

## Endpoints

| Method | Path                                                                        | Description                          |
|--------|----------------------------------------------------------------------------------|-------------------------------------------|
| GET    | `/api/v1/price-type-scopes/{price-type-scope-id}/price-type-assignments`         | List price types assigned to a scope       |
| POST   | `/api/v1/price-type-scopes/{price-type-scope-id}/price-type-assignments`         | Assign a price type to a scope             |
| DELETE | `/api/v1/price-type-scopes/{price-type-scope-id}/price-type-assignments/{id}`    | Unassign a price type from a scope         |

---

## Data Model

### PriceTypeScopeAssignment

| Field         | Type   | Required | Constraints | Description                                                              |
|----------------|--------|----------|---------------|--------------------------------------------------------------------------------|
| `id`           | Long   | —        | read-only     | Auto-generated identifier of the assignment row                                |
| `price_type`   | Object | —        | read-only     | The assigned price type — same shape as [Price Types](price-types-api.md)'s `PriceType` data model, including its own `locale`-matched translation |

The parent `price_type_scope` is never re-embedded on each row — it's already known from the
`{price-type-scope-id}` path segment.

---

## List Price Type Scope Assignments

`GET /api/v1/price-type-scopes/{price-type-scope-id}/price-type-assignments`

Returns a paginated list of every price type assigned to a given price type scope. This is the primary way
a client discovers "which price types apply here" for a room category, a room, or a resort facility, without
fetching every price type in the system.

### Path Parameters

| Parameter               | Type | Description                     |
|----------------------------|------|--------------------------------------|
| `price-type-scope-id`      | Long | ID of the price type scope          |

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
      "price_type": {
        "id": 2,
        "code": "WKD",
        "sort_order": 2,
        "locale": {
          "id": 2,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Weekday Price",
          "description": "Price applied to bookings made on weekdays (Monday through Friday).",
          "sort_order": 2,
          "purpose": "Allows lower pricing during off-peak weekday periods.",
          "usage_example": "A room costs $90/night on weekdays compared to $130/night on weekends."
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

## Assign Price Type

`POST /api/v1/price-type-scopes/{price-type-scope-id}/price-type-assignments`

Assigns a price type to a price type scope. Both must reference existing, active records — an unknown
`price_type_id` returns `404 ENTITY_NOT_FOUND`. The combination of price type scope and price type must be
unique — assigning a price type that's already assigned to this scope returns `409 CONFLICT`. This check is
application-level only; there is no DB-level unique constraint on `(price_type_scope_id, price_type_id)`.

### Path Parameters

| Parameter               | Type | Description                     |
|----------------------------|------|--------------------------------------|
| `price-type-scope-id`      | Long | ID of the price type scope          |

### Request Body

```json
{
  "price_type_id": 2
}
```

### Request Fields

| Field             | Type | Required | Validation                                     |
|--------------------|------|----------|-----------------------------------------------------|
| `price_type_id`    | Long | Yes      | Not null; must reference an existing price type      |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Unassign Price Type

`DELETE /api/v1/price-type-scopes/{price-type-scope-id}/price-type-assignments/{id}`

Soft-deletes an assignment. The record is not removed from the database but will no longer appear in any
response.

### Path Parameters

| Parameter               | Type | Description                     |
|----------------------------|------|--------------------------------------|
| `price-type-scope-id`      | Long | ID of the price type scope          |
| `id`                       | Long | ID of the assignment row            |

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
  "message": "PriceTypeScopeAssignment not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                          |
|-------------|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs); an unsupported `sortBy` query value; or `price_type_id` missing/null |
| 404         | `ENTITY_NOT_FOUND`         | Price type scope not found, the price type referenced by `price_type_id` not found, or assignment not found                                     |
| 409         | `CONFLICT`                 | The price type is already assigned to this price type scope (pre-checked at the application level)                                              |
