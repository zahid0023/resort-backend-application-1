# Resort Room Category Prices API

Base URL: `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`

A resort room category price defines a pricing rule for a specific resort room category. Each rule combines a price
type (e.g. Base, Promotional, Corporate), a price unit (e.g. Per Night, Per Person), an amount, an optional validity
period, and the days of the week on which the rule applies. Multiple pricing rules can exist per room category,
resolved at runtime by priority. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                                | Description               |
|--------|-------------------------------------------------------------------------------------|---------------------------|
| POST   | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`      | Create a price rule       |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`      | List / filter price rules |
| GET    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}` | Get a price rule          |
| PUT    | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}` | Update a price rule       |
| DELETE | `/api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}` | Delete a price rule       |

---

## Data Model

### Resort Room Category Price

| Field                     | Type       | Required | Constraints                                    | Description                                                                            |
|---------------------------|------------|----------|------------------------------------------------|----------------------------------------------------------------------------------------|
| `id`                      | Long       | —        | read-only                                      | Auto-generated identifier                                                              |
| `resort_room_category_id` | Long       | —        | read-only                                      | ID of the parent resort room category                                                  |
| `price_type_id`           | Long       | Yes      | not null, must exist                           | ID of the price type (e.g. Base, Promotional, Corporate)                               |
| `price_unit_id`           | Long       | Yes      | not null, must exist                           | ID of the price unit (e.g. Per Night, Per Person)                                      |
| `amount`                  | BigDecimal | Yes      | not null, >= 0.00, precision 12 scale 2        | Price amount in the resort's currency                                                  |
| `priority`                | Integer    | Yes      | not null, default `0`                          | Rule priority; higher value wins when multiple rules match the same booking conditions |
| `valid_from`              | String     | No       | date (`YYYY-MM-DD`), nullable                  | Start date of the validity period. Omitted from response if null.                      |
| `valid_to`                | String     | No       | date (`YYYY-MM-DD`), nullable; >= `valid_from` | End date of the validity period. Omitted from response if null.                        |
| `monday`                  | Boolean    | Yes      | not null, default `false`                      | Whether this rule applies on Mondays                                                   |
| `tuesday`                 | Boolean    | Yes      | not null, default `false`                      | Whether this rule applies on Tuesdays                                                  |
| `wednesday`               | Boolean    | Yes      | not null, default `false`                      | Whether this rule applies on Wednesdays                                                |
| `thursday`                | Boolean    | Yes      | not null, default `false`                      | Whether this rule applies on Thursdays                                                 |
| `friday`                  | Boolean    | Yes      | not null, default `false`                      | Whether this rule applies on Fridays                                                   |
| `saturday`                | Boolean    | Yes      | not null, default `false`                      | Whether this rule applies on Saturdays                                                 |
| `sunday`                  | Boolean    | Yes      | not null, default `false`                      | Whether this rule applies on Sundays                                                   |

---

## Database Constraints

Three check constraints enforce data integrity at the database level:

1. **Non-negative amount** — `amount` must be >= 0. Returns `409 DATA_INTEGRITY_VIOLATION` if violated.

2. **Valid date range** — When both `valid_from` and `valid_to` are provided, `valid_from` must be <= `valid_to`.
   Returns `409 DATA_INTEGRITY_VIOLATION` if violated.

3. **At least one day** — At least one day-of-week flag (`monday` through `sunday`) must be `true`. A price rule that
   applies to no day is rejected. Returns `409 DATA_INTEGRITY_VIOLATION` if violated.

---

## Create Price Rule

`POST /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`

Creates a new pricing rule for the specified resort room category. The resort and resort room category must exist and
be active. Multiple rules can be created for the same room category and will be resolved by `priority` at runtime.

### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |

### Request Fields

| Field           | Type       | Required | Validation                                                                  |
|-----------------|------------|----------|-----------------------------------------------------------------------------|
| `price_type_id` | Long       | Yes      | Not null, must reference an existing active price type                      |
| `price_unit_id` | Long       | Yes      | Not null, must reference an existing active price unit                      |
| `amount`        | BigDecimal | Yes      | Not null, >= 0.00                                                           |
| `priority`      | Integer    | Yes      | Not null                                                                    |
| `valid_from`    | String     | No       | Date in `YYYY-MM-DD` format                                                 |
| `valid_to`      | String     | No       | Date in `YYYY-MM-DD` format; must be >= `valid_from` when both are provided |
| `monday`        | Boolean    | Yes      | Not null                                                                    |
| `tuesday`       | Boolean    | Yes      | Not null                                                                    |
| `wednesday`     | Boolean    | Yes      | Not null                                                                    |
| `thursday`      | Boolean    | Yes      | Not null                                                                    |
| `friday`        | Boolean    | Yes      | Not null                                                                    |
| `saturday`      | Boolean    | Yes      | Not null                                                                    |
| `sunday`        | Boolean    | Yes      | Not null; at least one day-of-week flag must be `true`                      |

### Request Body

```json
{
  "price_type_id": 1,
  "price_unit_id": 2,
  "amount": 150.00,
  "priority": 10,
  "valid_from": "2026-01-01",
  "valid_to": "2026-03-31",
  "monday": true,
  "tuesday": true,
  "wednesday": true,
  "thursday": true,
  "friday": true,
  "saturday": false,
  "sunday": false
}
```

### Response `201 Created`

```json
{
  "success": true,
  "id": 42
}
```

---

## Get Price Rule

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}`

Returns a single active (non-deleted) pricing rule belonging to the specified resort room category. The `valid_from`
and `valid_to` fields are omitted from the response when null.

### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |
| `id`                      | Long | Yes      | ID of the price rule           |

### Response `200 OK`

```json
{
  "data": {
    "id": 42,
    "resort_room_category_id": 7,
    "price_type_id": 1,
    "price_unit_id": 2,
    "amount": 150.00,
    "priority": 10,
    "valid_from": "2026-01-01",
    "valid_to": "2026-03-31",
    "monday": true,
    "tuesday": true,
    "wednesday": true,
    "thursday": true,
    "friday": true,
    "saturday": false,
    "sunday": false
  }
}
```

---

## List / Filter Price Rules

`GET /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices`

Returns a paginated, filterable list of active (non-deleted) pricing rules for the specified resort room category. All
filter parameters are optional; omitting them returns all rules for the room category. Multiple filters are combined
with AND.

The `valid_from` filter returns rules whose `valid_from` is on or after the given date. The `valid_to` filter returns
rules whose `valid_to` is on or before the given date. Day-of-week filters perform exact boolean matches.

If the resort or resort room category does not exist or is deleted, a `404` is returned before executing the query.

### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |

### Query Parameters

| Parameter     | Type    | Default | Constraints                                                     | Description                                   |
|---------------|---------|---------|-----------------------------------------------------------------|-----------------------------------------------|
| `priceTypeId` | Long    | —       | —                                                               | Filter by price type ID (exact match)         |
| `priceUnitId` | Long    | —       | —                                                               | Filter by price unit ID (exact match)         |
| `validFrom`   | String  | —       | `YYYY-MM-DD`                                                    | Filter rules valid from this date or later    |
| `validTo`     | String  | —       | `YYYY-MM-DD`                                                    | Filter rules valid until this date or earlier |
| `monday`      | Boolean | —       | `true` or `false`                                               | Filter by Monday applicability                |
| `tuesday`     | Boolean | —       | `true` or `false`                                               | Filter by Tuesday applicability               |
| `wednesday`   | Boolean | —       | `true` or `false`                                               | Filter by Wednesday applicability             |
| `thursday`    | Boolean | —       | `true` or `false`                                               | Filter by Thursday applicability              |
| `friday`      | Boolean | —       | `true` or `false`                                               | Filter by Friday applicability                |
| `saturday`    | Boolean | —       | `true` or `false`                                               | Filter by Saturday applicability              |
| `sunday`      | Boolean | —       | `true` or `false`                                               | Filter by Sunday applicability                |
| `page`        | int     | `0`     | >= 0                                                            | Zero-based page index                         |
| `size`        | int     | `10`    | 1 – 50                                                          | Number of items per page                      |
| `sort_by`     | String  | `id`    | `id`, `amount`, `priority`, `validFrom`, `validTo`, `createdAt` | Field to sort by                              |
| `sort_dir`    | String  | `ASC`   | `ASC`, `DESC`                                                   | Sort direction                                |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 42,
      "resort_room_category_id": 7,
      "price_type_id": 1,
      "price_unit_id": 2,
      "amount": 150.00,
      "priority": 10,
      "valid_from": "2026-01-01",
      "valid_to": "2026-03-31",
      "monday": true,
      "tuesday": true,
      "wednesday": true,
      "thursday": true,
      "friday": true,
      "saturday": false,
      "sunday": false
    },
    {
      "id": 43,
      "resort_room_category_id": 7,
      "price_type_id": 2,
      "price_unit_id": 2,
      "amount": 120.00,
      "priority": 5,
      "monday": false,
      "tuesday": false,
      "wednesday": false,
      "thursday": false,
      "friday": false,
      "saturday": true,
      "sunday": true
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

## Update Price Rule

`PUT /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}`

Replaces all fields of an existing pricing rule. All fields are fully mutable — including `price_type_id` and
`price_unit_id`. The same database constraints on amount, date range, and day-of-week apply as on create.

### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |
| `id`                      | Long | Yes      | ID of the price rule           |

### Request Fields

| Field           | Type       | Required | Validation                                                               |
|-----------------|------------|----------|--------------------------------------------------------------------------|
| `price_type_id` | Long       | Yes      | Not null, must reference an existing active price type                   |
| `price_unit_id` | Long       | Yes      | Not null, must reference an existing active price unit                   |
| `amount`        | BigDecimal | Yes      | Not null, >= 0.00                                                        |
| `priority`      | Integer    | Yes      | Not null                                                                 |
| `valid_from`    | String     | No       | Date in `YYYY-MM-DD` format, or `null` to clear                          |
| `valid_to`      | String     | No       | Date in `YYYY-MM-DD` format, or `null` to clear; must be >= `valid_from` |
| `monday`        | Boolean    | Yes      | Not null                                                                 |
| `tuesday`       | Boolean    | Yes      | Not null                                                                 |
| `wednesday`     | Boolean    | Yes      | Not null                                                                 |
| `thursday`      | Boolean    | Yes      | Not null                                                                 |
| `friday`        | Boolean    | Yes      | Not null                                                                 |
| `saturday`      | Boolean    | Yes      | Not null                                                                 |
| `sunday`        | Boolean    | Yes      | Not null; at least one day-of-week flag must be `true`                   |

### Request Body

```json
{
  "price_type_id": 1,
  "price_unit_id": 2,
  "amount": 175.00,
  "priority": 10,
  "valid_from": "2026-04-01",
  "valid_to": "2026-06-30",
  "monday": true,
  "tuesday": true,
  "wednesday": true,
  "thursday": true,
  "friday": true,
  "saturday": true,
  "sunday": true
}
```

### Response `200 OK`

```json
{
  "success": true,
  "id": 42
}
```

---

## Delete Price Rule

`DELETE /api/v1/resorts/{resort-id}/room-categories/{resort-room-category-id}/prices/{id}`

Soft-deletes the pricing rule. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter                 | Type | Required | Description                    |
|---------------------------|------|----------|--------------------------------|
| `resort-id`               | Long | Yes      | ID of the resort               |
| `resort-room-category-id` | Long | Yes      | ID of the resort room category |
| `id`                      | Long | Yes      | ID of the price rule           |

### Response `200 OK`

```json
{
  "success": true,
  "id": 42
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
  "message": "ResortRoomCategoryPrice not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                              |
|-------------|----------------------------|--------------------------------------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations (e.g. `amount` null, `price_type_id` null, day flags all missing) |
| 400         | `INVALID_ARGUMENT`         | Invalid `sort_by` field or invalid date format                                                                     |
| 404         | `ENTITY_NOT_FOUND`         | Resort, resort room category, price rule, price type, or price unit not found, or already deleted                  |
| 409         | `DATA_INTEGRITY_VIOLATION` | `amount` < 0, `valid_from` > `valid_to`, or no day-of-week flag set to `true`                                      |
