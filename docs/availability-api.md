# Resort Availability API

Base URL: `/api/v1/resorts/{resort-id}/availability`

Availability answers one question — **"which rooms in this resort are actually bookable for these dates,
right now?"** — as a single, channel-independent read. It is not part of the Room Reservations API (not yet
documented as its own `docs/*.md` — see `resort/roomreservation/` in the codebase); it exists so that every
booking channel (today's POS/booker flow, and later the resort website, mobile app, or an OTA integration)
asks the exact same question the exact same way, instead of each channel growing its own "is this room free"
logic that can quietly drift out of sync with the others.

A room counts as available only when **both** of the following hold for the whole `[check_in, check_out)`
range:

1. Its `room_status` is `AVAILABLE` — not `MAINTENANCE`, `OUT_OF_ORDER`, or `RENOVATION` (see the [Room
   Statuses API](room-statuses-api.md)). A room out of service is unavailable regardless of reservations.
2. It has no active reservation (`blocks_availability = true`, not soft-deleted) whose own `[check_in,
   check_out)` overlaps the requested range.

Only rooms that pass both checks are returned at all — there is no `available: false` entry anywhere in the
response.

## Scenario: why this endpoint exists

The platform is POS-first — the first booking interface is a resort **booker** working a manual channel, not
a customer self-serving on a website. A typical flow looks like this:

> A customer messages the resort directly — over WhatsApp, a phone call, Facebook, Instagram, or by walking
> in — asking: *"Do you have a Standard room from September 10 to September 12?"* The customer is not
> interacting with the platform at all; a **booker** (a staff member with an `ACTIVE` `BOOKER` membership on
> that resort) is. The booker tells the customer "let me check" and, inside the platform (already logged in,
> already working within that specific resort), calls:
>
> `GET /api/v1/resorts/{resort-id}/availability?checkIn=2026-09-10&checkOut=2026-09-12&currencyId=1`
>
> The response is a flat list of every room that is actually available, each entry pairing the **full room
> record** (same shape, same `meta`/`beds` inheritance resolution, as [Get Resort
> Room](resort-rooms-api.md#get-resort-room) / [List Resort Rooms](resort-rooms-api.md#list-resort-rooms)) with
> its **price for that exact stay**, resolved night by night (Weekday/Weekend/Special) in the requested
> currency — the booker doesn't just see "Standard room, STA-101 is free," they see the priced quote to read
> straight back to the customer. E.g. `STA-101` and `STA-102` come back (each embedding
> `resort_room_category.code == "STANDARD"`), `STA-103` does not (it already has an overlapping reservation, so
> it's simply omitted, not returned with a false flag). The booker relays this back to the customer over the
> same channel ("yes, we have two Standard rooms free those dates — STA-101 is 8,000 BDT total, STA-102 is
> 7,500 BDT total" or "sorry, nothing available for those dates"), the customer picks one, and the booker
> proceeds to create the reservation via `POST /api/v1/resorts/{resort-id}/bookings` (see `ResortBookingController`,
> with a `rooms` list of size 1 for a single-room booking) with `source = WHATSAPP` / `PHONE` / `FACEBOOK` /
> `WALK_IN` / etc. and `created_by` set to the booker.

**Why this can't just be "check the reservations table for gaps" ad hoc, per caller:** the same question gets
asked again later by very different callers — a future `Booking API` behind the resort website (`source =
WEBSITE`, `created_by = null`), and eventually OTA channel integrations (`source = BOOKING_COM`, etc., `created_by
= null`). All of them need the identical availability rule (room status **and** reservation overlap, not just
one or the other) applied identically. Centralizing it here is what makes that guarantee possible — see
`AvailabilityServiceImpl`, which every future booking entry point is expected to call rather than reimplement.

**Why this alone doesn't prevent overbooking:** this endpoint is a **point-in-time snapshot** — nothing is
locked or reserved by calling it. Two bookers (or a booker and a website customer) could both see `STA-101` as
available and both attempt to create a reservation for overlapping dates a moment later. The actual
overbooking guard is not here — it's the `excl_resort_room_reservations_no_overlap` GiST exclusion constraint on the
`room_reservations` table itself (see `V46__create_room_reservations_table.sql`), which makes the *second* conflicting
insert/status-transition fail atomically at the database level, race-free, regardless of what this endpoint
returned moments earlier. This endpoint exists to make the search fast and shared, not to hand out a guarantee
— always expect (and handle) the create step being rejected even after a room showed as available here.

**`Accept-Language` is required, with no exceptions** — a request missing (or with a blank) `Accept-Language`
header is rejected with `400 INVALID_ARGUMENT` before it reaches this endpoint (see [Error
Responses](#error-responses)). Its value selects exactly one locale translation for each room's own `locale`
field, its nested `room_status.locale` field, its embedded `resort_room_category.locale` field, and (via the
same resolution `GET .../rooms` uses) each bed row's nested `bed_type.locale` field.

---

## Endpoints

| Method | Path                                       | Description                                           |
|--------|--------------------------------------------|-------------------------------------------------------|
| GET    | `/api/v1/resorts/{resort-id}/availability` | Search available rooms for a resort over a date range |

---

## Data Model

### Available room entry

Each entry in `data` pairs a `room` object with its resolved `price` for the requested stay — see **Price**
below. Only rooms that are both available (per the two rules above) **and** priceable for the requested
`currency_id` are ever returned — a room with no active Main price (own or its category's) for that currency
is silently excluded, the same way an already-booked room is excluded.

| Field   | Type   | Description                                                                 |
|---------|--------|-----------------------------------------------------------------------------|
| `room`  | Object | The full resort room record — see **ResortRoom** below                      |
| `price` | Object | The room's resolved price for `[check_in, check_out)` — see **Price** below |

### ResortRoom

Each entry is the **exact same `ResortRoom` shape `GET .../rooms` (List Resort Rooms) returns** — see the full
field-by-field breakdown on the [Resort Rooms API](resort-rooms-api.md#data-model). It's assembled via the
same `ResortRoomService#buildDto` every list/get room endpoint uses (own-vs-inherited `meta`, own-vs-inherited
`beds`, embedded `resort_room_category` and `room_status`), not a separate availability-specific shape — so a
frontend can render an available room with the identical component it already uses elsewhere. There is no
`available` boolean field anywhere, since every entry in the response is, by construction, available.

| Field                  | Type    | Description                                                                                                                       |
|------------------------|---------|-----------------------------------------------------------------------------------------------------------------------------------|
| `id`                   | Long    | The resort room's id                                                                                                              |
| `code`                 | String  | The resort room's resort-scoped code (e.g. `STA-101`)                                                                             |
| `sort_order`           | Integer | Display order                                                                                                                     |
| `floor_number`         | Integer | Nullable                                                                                                                          |
| `building`             | String  | Nullable                                                                                                                          |
| `resort_room_category` | Object  | The room's category (`id`, `code`, `sort_order`, `locale` only — see [Resort Room Categories API](resort-room-categories-api.md)) |
| `room_status`          | Object  | The room's current operational status — always `AVAILABLE` here (see [Room Statuses API](room-statuses-api.md))                   |
| `locale`               | Object  | The room's own single Accept-Language-matched translation (falls back to `en`, then `null`)                                       |
| `meta`                 | Object  | Occupancy/room-detail/booking-rule settings — the room's own override if it has one, else its category's (`inherited` flag)       |
| `beds`                 | Array   | Bed configuration rows — the room's own rows if it has any, else its category's (`inherited` on each entry)                       |

### Price

Resolved by `RoomPricingResolver` — the exact same resolver `ResortBookingController` uses to
compute a reservation's `total_price`, so the quote shown here is exactly what booking the room would charge,
not an approximation. Main and Special prices are resolved independently (the room's own override if it has
one, else its category's — see [Resort Room Prices API](resort-room-prices-api.md)); each night in `nights` is
priced on its own, with a Special price window (highest `priority` wins if more than one window covers that
night) beating the resolved Main row's Weekday/Weekend rate for that specific night.

| Field        | Type    | Description                                                                     |
|--------------|---------|---------------------------------------------------------------------------------|
| `currency`   | Object  | The requested `currency_id`, resolved (see [Currencies API](currencies-api.md)) |
| `price_unit` | Object  | The billing unit of the resolved Main price row (e.g. `PER_NIGHT`)              |
| `nights`     | Array   | One entry per night in `[check_in, check_out)`, in date order — see below       |
| `total`      | Decimal | Sum of every `nights[].price`                                                   |

**`nights[]` entry:**

| Field       | Type    | Description                                                                 |
|-------------|---------|-----------------------------------------------------------------------------|
| `date`      | Date    | The night being priced                                                      |
| `price`     | Decimal | The resolved rate for that night                                            |
| `rate_type` | String  | `WEEKDAY`, `WEEKEND`, or `SPECIAL` — which rule actually won for that night |

---

## Search Availability

`GET /api/v1/resorts/{resort-id}/availability`

Returns a flat list of every currently available room belonging to the resort — across every category, each
room embedding its own `resort_room_category` rather than rooms being grouped under their category. Rooms
appear in the order returned by the database (no explicit sort is applied).

> **Note:** like every query-parameter-bound request in this codebase, `checkIn`/`checkOut` are the request's
> **Java field names** (camelCase) — not the snake_case used in JSON bodies — since `@ParameterObject`
> query-string binding goes through Spring's plain `DataBinder`, which Jackson's snake_case naming strategy
> does not apply to.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Query Parameters

| Parameter    | Type      | Required | Constraints                                               | Description                                         |
|--------------|-----------|----------|-----------------------------------------------------------|-----------------------------------------------------|
| `checkIn`    | LocalDate | Yes      | ISO-8601 (`YYYY-MM-DD`)                                   | Start of the requested stay (inclusive)             |
| `checkOut`   | LocalDate | Yes      | ISO-8601 (`YYYY-MM-DD`); must be strictly after `checkIn` | End of the requested stay (exclusive)               |
| `currencyId` | Long      | Yes      | Must reference an existing currency                       | Currency every returned room's `price` is quoted in |

### Example Request

```bash
curl -X GET "http://localhost:8080/api/v1/resorts/1/availability?checkIn=2026-09-10&checkOut=2026-09-12&currencyId=1" \
  -H "Accept-Language: en"
```

### Response `200 OK`

```json
{
  "data": [
    {
      "room": {
        "id": 101,
        "code": "STA-101",
        "sort_order": 1,
        "floor_number": 1,
        "building": "Main Building",
        "resort_room_category": {
          "id": 10,
          "code": "STANDARD",
          "sort_order": 1,
          "locale": {
            "id": 20,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "Standard Room",
            "description": "",
            "sort_order": 1
          }
        },
        "room_status": {
          "id": 1,
          "code": "AVAILABLE",
          "sort_order": 1,
          "locale": {
            "id": 1,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "Available",
            "description": "",
            "sort_order": 1
          }
        },
        "locale": {
          "id": 30,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Room 101",
          "description": "Deluxe sea view room on the first floor.",
          "sort_order": 1
        },
        "meta": {
          "id": 15,
          "max_adults": 2,
          "max_children": 1,
          "max_infants": 0,
          "max_occupancy": 3,
          "room_size": 45.50,
          "room_size_unit": {
            "id": 1,
            "code": "SQM",
            "symbol": "m²",
            "sort_order": 0
          },
          "bedroom_count": 1,
          "bathroom_count": 1,
          "minimum_stay_nights": 1,
          "maximum_stay_nights": 14,
          "inherited": false
        },
        "beds": [
          {
            "id": 25,
            "bed_type": {
              "id": 3,
              "code": "KING",
              "sort_order": 1
            },
            "quantity": 1,
            "is_extra_bed_allowed": true,
            "max_extra_beds": 1,
            "inherited": false
          }
        ]
      },
      "price": {
        "currency": {
          "id": 1,
          "code": "BDT",
          "symbol": "৳",
          "sort_order": 1
        },
        "price_unit": {
          "id": 1,
          "code": "PER_NIGHT",
          "sort_order": 1
        },
        "nights": [
          {
            "date": "2026-09-10",
            "price": 2000.00,
            "rate_type": "WEEKEND"
          },
          {
            "date": "2026-09-11",
            "price": 1800.00,
            "rate_type": "WEEKDAY"
          }
        ],
        "total": 3800.00
      }
    },
    {
      "room": {
        "id": 102,
        "code": "STA-102",
        "sort_order": 2,
        "floor_number": 1,
        "building": "Main Building",
        "resort_room_category": {
          "id": 10,
          "code": "STANDARD",
          "sort_order": 1,
          "locale": {
            "id": 20,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "Standard Room",
            "description": "",
            "sort_order": 1
          }
        },
        "room_status": {
          "id": 1,
          "code": "AVAILABLE",
          "sort_order": 1,
          "locale": {
            "id": 1,
            "locale": {
              "id": 1,
              "code": "en",
              "name": "English",
              "sort_order": 1
            },
            "name": "Available",
            "description": "",
            "sort_order": 1
          }
        },
        "locale": {
          "id": 31,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Room 102",
          "description": "Deluxe sea view room on the first floor.",
          "sort_order": 1
        },
        "meta": {
          "id": 4,
          "max_adults": 2,
          "max_children": 1,
          "max_infants": 0,
          "max_occupancy": 3,
          "room_size": 45.50,
          "room_size_unit": {
            "id": 1,
            "code": "SQM",
            "symbol": "m²",
            "sort_order": 0
          },
          "bedroom_count": 1,
          "bathroom_count": 1,
          "minimum_stay_nights": 1,
          "maximum_stay_nights": 14,
          "inherited": true
        },
        "beds": [
          {
            "id": 25,
            "bed_type": {
              "id": 3,
              "code": "KING",
              "sort_order": 1
            },
            "quantity": 1,
            "is_extra_bed_allowed": true,
            "max_extra_beds": 1,
            "inherited": true
          }
        ]
      },
      "price": {
        "currency": {
          "id": 1,
          "code": "BDT",
          "symbol": "৳",
          "sort_order": 1
        },
        "price_unit": {
          "id": 1,
          "code": "PER_NIGHT",
          "sort_order": 1
        },
        "nights": [
          {
            "date": "2026-09-10",
            "price": 4500.00,
            "rate_type": "WEEKEND"
          },
          {
            "date": "2026-09-11",
            "price": 3800.00,
            "rate_type": "WEEKDAY"
          }
        ],
        "total": 8300.00
      }
    }
  ]
}
```

`STA-103` (already booked for an overlapping date range in this example) is simply absent from `data` — it is
not returned with `available: false` or any other marker; its exclusion from the list *is* the signal. A room
whose category has no active Main price for `currencyId=1` would be excluded the same way, even if it's
otherwise free. `STA-102` above shows `room.meta.inherited: true` and `room.beds[].inherited: true` — it has no
overrides of its own, so both are its category's data (same fallback concept as [Get Resort
Room](resort-rooms-api.md#get-resort-room)); `STA-101` has its own overrides for both (`inherited: false`).
`STA-101` and `STA-102` are priced independently — different Main rows (own or inherited), so their `nights`
and `total` differ even for the identical stay.

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "Resort not found with id: 99"
}
```

| HTTP Status | Error Code         | Cause                                                                                                                                                                          |
|-------------|--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT` | Missing or blank `Accept-Language` header (checked globally, before this endpoint runs); missing `checkIn`/`checkOut`/`currencyId`; or `checkOut` not strictly after `checkIn` |
| 404         | `ENTITY_NOT_FOUND` | Resort not found, or `currencyId` does not reference an existing currency                                                                                                      |

Note: a room that's available but has no active Main price for the requested `currencyId` does **not** cause an
error — it's simply excluded from `data`, exactly like an already-booked room (see [Data
Model](#data-model)).
