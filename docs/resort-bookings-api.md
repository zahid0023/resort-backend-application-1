# Resort Bookings API

Base URL: `/api/v1/resorts/{resort-id}/bookings` (create/list bookings).

A **booking** is the single entry point for reserving one or more rooms in a resort in one transaction —
possibly across different room categories, since a booking is not scoped to one category's URL. Booking-level
fields (`booking_source_id`, `email`/`phone_number`, `notes`, `currency_id`) are owned exclusively by the
booking and are never duplicated per room; every room in a booking is charged in the same currency. Each entry
in `rooms` picks its own room, dates, occupancy, guests, and `reservation_status_id` — rooms in the same
booking are **not** required to share a stay window or even a reservation status.

The customer is resolved by a find-or-create on username, where the username is either the email or the phone
number: if `email` is present in the request, it is used as the username to look up (or, if none exists yet,
register) the customer; otherwise `phone_number` is used. A newly registered customer gets a random,
system-generated password — the booker never types one in, and it's never included in any API response either
(see [password-reset-api.md](password-reset-api.md) for how the customer eventually sets a real one).

Each room in the booking becomes its own **ResortRoomReservation** row, owned by the booking. A reservation has
**no endpoints of its own** — it's an internal building block, not a public resource: `resort_room_id` +
`check_in`/`check_out` availability is checked by the resort's availability API, and the only way to read a
reservation back over HTTP is nested inside its parent booking via **`GET .../bookings`**, which returns every
booking's full detail (reference code, customer, booking source, booking-level notes, and the full nested list
of that booking's reservations — room, category, dates, per-room notes, nightly price breakdown, guest list,
and price — plus the booking's total price). A reservation always belongs to exactly one booking, even a lone
single-room booking (a "group of one") — reachable from a reservation via its `booking_id`/`customer_id`
fields. There is no `GET .../bookings/{id}` either — a single booking is found by paging/sorting through the
list endpoint.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). Its value has no effect on any endpoint's response shape — none of
booking/reservation carries any locale-specific field of its own (nested objects like `resort_room` /
`reservation_status` / `currency` / `price_unit` still resolve their own single-locale fields the normal way).

---

## Endpoints

| Method | Path                                       | Description                                    |
|--------|--------------------------------------------|------------------------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/bookings/pos` | Create a booking (POS/booker channel)          |
| GET    | `/api/v1/resorts/{resort-id}/bookings`     | List / paginate bookings, grouped, full detail |

---

## Data Model

### ResortBooking

| Field            | Type    | Description                                                                              |
|------------------|---------|------------------------------------------------------------------------------------------|
| `id`             | Long    | Auto-generated identifier                                                                |
| `reference_code` | String  | Human-readable code (e.g. `BK00000123`) the customer can quote back over phone/WhatsApp  |
| `customer`       | Object  | The booking's customer — `id` and `username` only (never a password or other credential) |
| `booking_source` | Object  | Which channel the booking originated from (e.g. `WHATSAPP`, `PHONE`, `WEBSITE`)          |
| `notes`          | String  | Booking-level notes; not null (defaults to `""`)                                         |
| `reservations`   | Array   | One entry per room in the booking — full `ResortRoomReservation` objects, see below      |
| `total_price`    | Decimal | Sum of every reservation's own `total_price`                                             |
| `currency`       | Object  | Shared by every reservation in the booking                                               |

### ResortRoomReservation

| Field                                 | Type    | Description                                                                                                              |
|---------------------------------------|---------|--------------------------------------------------------------------------------------------------------------------------|
| `id`                                  | Long    | Auto-generated identifier                                                                                                |
| `customer_id`                         | Long    | The booking's customer (`resort_booking.user_entity.id`) — not duplicated on this row                                    |
| `booking_id`                          | Long    | The booking this reservation belongs to — every reservation has one, even a "group of one"                               |
| `resort_room`                         | Object  | The specific room booked (id, code, category, room status, etc.)                                                         |
| `reservation_status`                  | Object  | Current status (e.g. `PENDING`, `CONFIRMED`, `CHECKED_IN`, `CHECKED_OUT`, `CANCELLED`, `NO_SHOW`)                        |
| `check_in`                            | Date    | Stay start date (`yyyy-MM-dd`)                                                                                           |
| `check_out`                           | Date    | Stay end date (`yyyy-MM-dd`); must be after `check_in`                                                                   |
| `guests`                              | Array   | Every occupant of this room (name + `guest_type`) — see ResortRoomReservationGuest below                                 |
| `previous_resort_room_reservation_id` | Long    | Nullable; set when this row supersedes an earlier status-history row for the same stay                                   |
| `adult_count`                         | Integer | >= 1                                                                                                                     |
| `child_count`                         | Integer | >= 0                                                                                                                     |
| `currency`                            | Object  | Currency this reservation is priced in — shared by every room in the booking                                             |
| `price_unit`                          | Object  | The price unit resolved for this room at booking time                                                                    |
| `nights`                              | Array   | One entry per night of the stay, frozen at booking time — see ResortRoomReservationNightlyPrice below                    |
| `total_price`                         | Decimal | Sum of `nights[].price`; frozen at booking time, unaffected by later price changes                                       |
| `notes`                               | String  | Not null (defaults to `""`)                                                                                              |
| `cancellation_reason`                 | String  | Not null (defaults to `""`); set only when the current status was an explained transition (e.g. `CANCELLED`/`NO_SHOW`)   |
| `blocks_availability`                 | Boolean | `true` while this row occupies the room (kept in sync by a DB trigger off `reservation_status`); never set by the client |

### ResortRoomReservationGuest

| Field        | Type   | Description                                             |
|--------------|--------|---------------------------------------------------------|
| `name`       | String | Guest's name (free text — no platform account required) |
| `guest_type` | String | `ADULT` or `CHILD`                                      |

### ResortRoomReservationNightlyPrice

| Field       | Type    | Description                                           |
|-------------|---------|-------------------------------------------------------|
| `date`      | Date    | The night this price applies to                       |
| `price`     | Decimal | Price resolved for that night, frozen at booking time |
| `rate_type` | String  | `WEEKDAY`, `WEEKEND`, or `SPECIAL`                    |

---

## Create Booking

`POST /api/v1/resorts/{resort-id}/bookings/pos`

Creates a booking for one or more rooms on behalf of a customer — the entry point for the POS/booker channel
(a staff member booking over WhatsApp/phone/walk-in/etc.). `resort_room_id` on each room entry must belong to
`{resort-id}`; a room from another resort returns `404 ENTITY_NOT_FOUND`. Overlapping dates for the same room
across two active reservations are rejected atomically at the database level with `409 CONFLICT`.

**Pricing and the nightly breakdown are resolved entirely server-side** — there is no `price_unit_id`,
`total_price`, or per-night price field on the request; the effective price (a room's own override, else its
category's) is looked up for each night of the stay and frozen onto the created reservation.

**The customer is found-or-created, never passed as an id.** The username used to look the customer up is
`email` if present in the request, otherwise `phone_number` (`phone_number` is always required; `email` is
optional). If no user exists yet for that username, one is registered on the fly with a random,
system-generated password.

**On success, the customer is sent a best-effort notification** pointing them at the customer portal and
telling them to use "forgot password" to set their own — by email (via the platform's configured
`CREATE_USER_EMAIL_NOTIFICATIONS` mail provider, see [mail-providers-api.md](mail-providers-api.md)) when
`email` was used as the username, or by WhatsApp when `phone_number` was used and that phone is already marked
WhatsApp-reachable. Sent for every booking, new customer or returning. A missing mail provider config, a send
failure, or a phone that isn't WhatsApp-reachable is logged and skipped rather than failing the booking — the
notification never appears in the response body either way. The password itself is never sent in this or any
other notification (see [password-reset-api.md](password-reset-api.md) for how the customer eventually sets
one).

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Request Body

```json
{
  "email": "jane.doe@example.com",
  "phone_number": "+15551234567",
  "booking_source_id": 4,
  "currency_id": 1,
  "notes": "Anniversary trip, late check-in requested",
  "rooms": [
    {
      "resort_room_id": 12,
      "reservation_status_id": 1,
      "check_in": "2026-09-10",
      "check_out": "2026-09-12",
      "adult_count": 2,
      "child_count": 0,
      "notes": "",
      "guests": [
        {
          "name": "Jane Doe",
          "guest_type": "ADULT",
          "sort_order": 0
        },
        {
          "name": "John Doe",
          "guest_type": "ADULT",
          "sort_order": 1
        }
      ]
    }
  ]
}
```

### Request Fields

| Field               | Type   | Required | Validation                                                                            |
|---------------------|--------|----------|---------------------------------------------------------------------------------------|
| `email`             | String | —        | Nullable; when present, used as the username to find-or-create the customer           |
| `phone_number`      | String | Yes      | Not blank; used as the username to find-or-create the customer when `email` is absent |
| `booking_source_id` | Long   | Yes      | Not null; must reference an existing, active booking source                           |
| `currency_id`       | Long   | Yes      | Not null; must reference an existing, active currency                                 |
| `notes`             | String | —        | Nullable (stored as `""` if omitted)                                                  |
| `rooms`             | Array  | Yes      | Not empty; at least one room entry                                                    |

**Each entry in `rooms`:**

| Field                   | Type    | Required | Validation                                              |
|-------------------------|---------|----------|---------------------------------------------------------|
| `resort_room_id`        | Long    | Yes      | Not null; must belong to `{resort-id}`                  |
| `reservation_status_id` | Long    | Yes      | Not null; must reference an existing reservation status |
| `check_in`              | Date    | Yes      | Not null                                                |
| `check_out`             | Date    | Yes      | Not null; must be after `check_in`                      |
| `adult_count`           | Integer | Yes      | Not null, >= 1                                          |
| `child_count`           | Integer | Yes      | Not null, >= 0                                          |
| `notes`                 | String  | —        | Nullable (stored as `""` if omitted)                    |
| `guests`                | Array   | Yes      | Not empty; at least one guest entry                     |

**Each entry in `rooms[].guests`:**

| Field        | Type    | Required | Validation                   |
|--------------|---------|----------|------------------------------|
| `name`       | String  | Yes      | Not blank                    |
| `guest_type` | String  | Yes      | Not null; `ADULT` or `CHILD` |
| `sort_order` | Integer | Yes      | Not null                     |

### Response `201 Created`

```json
{
  "success": true,
  "id": 7
}
```

`id` is the new booking's id — not a reservation id. There is no per-booking `GET .../bookings/{id}`; follow up
with [List Bookings](#list-bookings) for the full detailed view.

---

## List Bookings

`GET /api/v1/resorts/{resort-id}/bookings`

Returns a paginated, detailed, resort-wide list of active (non-deleted) bookings — one row per booking, each
carrying its full array of room reservations (room, category, dates, per-room notes, nightly price breakdown,
guest list, per-room price) plus the booking's own reference code, customer, source, notes, and total price.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Query Parameters

| Parameter | Type   | Default         | Constraints                       | Description              |
|-----------|--------|-----------------|-----------------------------------|--------------------------|
| `page`    | int    | `0`             | >= 0                              | Zero-based page index    |
| `size`    | int    | `10`            | 1 – 50                            | Number of items per page |
| `sortBy`  | String | `id` (implicit) | `createdAt` (only sortable field) | Field to sort by         |
| `sortDir` | String | `ASC`           | `ASC`, `DESC`                     | Sort direction           |

> **Note:** there are no filter parameters — a booking is reached via this resort-wide finder rather than the
> generic filterable-specification framework used by other list endpoints.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 7,
      "reference_code": "BK1A2B3C4D5E",
      "customer": {
        "id": 9,
        "username": "jane.doe@example.com"
      },
      "booking_source": {
        "id": 4,
        "code": "WHATSAPP",
        "sort_order": 1
      },
      "notes": "Anniversary trip, late check-in requested",
      "reservations": [
        {
          "id": 15,
          "customer_id": 9,
          "booking_id": 7,
          "resort_room": {
            "id": 12,
            "code": "101",
            "sort_order": 1
          },
          "reservation_status": {
            "id": 1,
            "code": "PENDING",
            "sort_order": 1
          },
          "check_in": "2026-09-10",
          "check_out": "2026-09-12",
          "guests": [
            {
              "name": "Jane Doe",
              "guest_type": "ADULT"
            },
            {
              "name": "John Doe",
              "guest_type": "ADULT"
            }
          ],
          "adult_count": 2,
          "child_count": 0,
          "currency": {
            "id": 1,
            "code": "USD",
            "symbol": "$"
          },
          "price_unit": {
            "id": 1,
            "code": "PER_NIGHT"
          },
          "nights": [
            {
              "date": "2026-09-10",
              "price": 120.00,
              "rate_type": "WEEKDAY"
            },
            {
              "date": "2026-09-11",
              "price": 150.00,
              "rate_type": "WEEKEND"
            }
          ],
          "total_price": 270.00,
          "notes": "",
          "cancellation_reason": null,
          "blocks_availability": true
        }
      ],
      "total_price": 270.00,
      "currency": {
        "id": 1,
        "code": "USD",
        "symbol": "$"
      }
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 1,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": [
    "createdAt"
  ],
  "searchable_fields": []
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
  "message": "ResortRoom not found with id: 12"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                      |
|-------------|----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header; missing/invalid required fields (e.g. empty `rooms`/`guests`, `check_out` not after `check_in`, `adult_count < 1`); an unsupported `sortBy` query value on the list endpoint                                                    |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found; `booking_source_id` not found; `currency_id` not found; `reservation_status_id` not found; `resort_room_id` not found (or found but belongs to a different resort) — note the customer is find-or-create, so a missing `email`/`phone_number` never 404s |
| 409         | `DATA_INTEGRITY_VIOLATION` | The room is already booked for an overlapping date range (`excl_resort_room_reservations_no_overlap`) — choose different dates or a different room                                                                                                                         |
