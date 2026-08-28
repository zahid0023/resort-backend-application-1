# Resort Facility Operating Hours API

Base URL: `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/operating-hours`

A resort facility operating hours row describes a single open window for a [Resort
Facility](resort-facilities-api.md) on a single [Day of Week](days-of-week-api.md) (e.g. a swimming pool open
06:00–21:00 on Mondays). A facility may have **multiple rows for the same day** to represent a break in
service — e.g. a restaurant open `09:00:00`–`14:00:00` and again `17:00:00`–`23:00:00` on the same day, with a
gap in between where it's closed. There is no locale sub-resource for this entity itself; `day_of_week` embeds
the day's own locale-matched translation instead (see [Days of Week API](days-of-week-api.md)). All records
support soft-delete — deleted records are hidden from all responses.

**There is no per-row endpoint at all — a facility's schedule is always read and written as a complete week.**
Operating hours are created for the first time as part of [Create Resort
Facility](resort-facilities-api.md#create-resort-facility) (the `operating_hours` field there), and edited
afterward only through [Set Weekly Schedule](#set-weekly-schedule) below, which atomically replaces every day at
once. Reading a facility's schedule means [List Operating Hours](#list-operating-hours) — this API exposes only
that list endpoint plus the one write endpoint; there is no `POST`, no `GET /{id}`, no `PUT /{id}`, and no
`DELETE /{id}`. See [Why no per-row endpoints](#why-no-per-row-endpoints).

Operating hours are always reached nested under their owning resort facility; there is no top-level
`/api/v1/resort-facility-operating-hours` route. Every endpoint below also validates the `{resort-id}`/
`{resort-facility-id}` pair first — an unknown resort, an unknown facility, or a facility that exists but belongs to
a different resort all return `404 ENTITY_NOT_FOUND`.

**`is_closed` and `is_twenty_four_hours` govern `opens_at`/`closes_at`, enforced on every write:** exactly one
of three states applies —

- `is_closed=true, is_twenty_four_hours=false` — both `opens_at`/`closes_at` must be omitted/`null` (closed
  all day).
- `is_closed=false, is_twenty_four_hours=true` — both `opens_at`/`closes_at` must also be omitted/`null` (open
  the entire day, no specific window).
- `is_closed=false, is_twenty_four_hours=false` — both `opens_at`/`closes_at` are required (open for a
  specific window).

`is_closed` and `is_twenty_four_hours` can never both be `true`. Violating any of this returns
`400 INVALID_ARGUMENT` (checked at the application level before any write; a database check constraint backs
it as a last-resort guard — see [Error Responses](#error-responses)).

**Overnight windows are supported with no special flag, and they're validated against the *next calendar
day's* rows too.** `opens_at`/`closes_at` are plain clock times with no "closes after opens" ordering check
anywhere (application or database) — an entry with `opens_at="16:00:00"` and `closes_at="01:00:00"` saves and
returns exactly as given, meaning the facility is open Monday 16:00 through Tuesday 01:00. The API stores the
two times as-is on the day the window *starts* (Monday in this example) — there is no second row created for
Tuesday. A `closes_at <= opens_at` value is what signals the rollover. Because the window's last hour (Tuesday
00:00–01:00 in this example) is real open time on the next calendar day, it's checked for overlap against
Tuesday's own rows exactly as if it were one of Tuesday's windows — see
[Overlap and Cross-Day Validation](#overlap-and-cross-day-validation) below. The reverse direction is checked
too: a window starting early on Tuesday is checked against Monday's overnight spillover. Since every write
covers the whole week in one request, both directions are always validated together, including the wraparound
from Sunday back to Monday.

**Multiple windows per day (breaks) are supported, with overlap validation — same-day and cross-day — on every
write.** A day can hold any number of specific-window entries (`is_closed=false, is_twenty_four_hours=false`)
as long as no two of them overlap in time. A window that overlaps another window on the same day returns
`409 CONFLICT` — and so does one whose overnight spillover overlaps the *next* day's windows, or that follows
immediately after the *previous* day's overnight spillover into an overlapping window. `is_closed=true` or
`is_twenty_four_hours=true` describes the *entire* day and is mutually exclusive with any other entry
(specific-window or otherwise) for that day, in both the same-day and cross-day sense — e.g. Tuesday cannot be
`is_closed=true` while Monday's overnight window is still spilling into Tuesday's early hours. See
[Overlap and Cross-Day Validation](#overlap-and-cross-day-validation) and
[Error Responses](#error-responses).

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). This entity has no `locale` field of its own, but the header's value
still shapes the response: it selects the locale-matched translation embedded on `resort_facility.locale` and
`day_of_week.locale` (exact match, falls back to `en`, then `null`), the same as `GET` on either of those
resources directly.

---

## Endpoints

| Method | Path                                                                            | Description                                          |
|--------|---------------------------------------------------------------------------------|------------------------------------------------------|
| PUT    | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/operating-hours/schedule` | Set the facility's entire weekly schedule atomically |
| GET    | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/operating-hours`          | List a facility's operating hours                    |

**A facility's schedule is all-or-nothing: if any day is set, every day must be set.** There is no way to have
just Monday set and the other six days unconfigured — [Set Weekly Schedule](#set-weekly-schedule) (and
[Create Resort Facility](resort-facilities-api.md#create-resort-facility)'s embedded `operating_hours`) are the
only ways to write a schedule, and both require exactly one entry per active day of week (`CLOSED`,
`OPEN_24_HOURS`, or one-or-more custom windows).

---

## Data Model

### ResortFacilityOperatingHours

| Field                  | Type    | Required      | Constraints                                                                                                                   | Description                                                                                                                                                        |
|------------------------|---------|---------------|-------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`                   | Long    | —             | read-only                                                                                                                     | Auto-generated identifier                                                                                                                                          |
| `resort_facility`      | Object  | —             | read-only; see [ResortFacility](resort-facilities-api.md)                                                                     | The facility this row belongs to. Resolved from the URL path, never a request body field.                                                                          |
| `day_of_week`          | Object  | —             | read-only; see [DayOfWeek](days-of-week-api.md); resolved from `day_of_week_id`                                               | The day of week this row applies to (`id`, `code`, `sort_order`, `locale`)                                                                                         |
| `opens_at`             | String  | conditionally | `HH:mm:ss` (SQL `time`); required only when both `is_closed` and `is_twenty_four_hours` are `false`, otherwise must be `null` | Opening time. May be later than `closes_at` to represent an overnight window (e.g. `23:00:00`–`03:00:00`) — not validated against `closes_at` in either direction. |
| `closes_at`            | String  | conditionally | `HH:mm:ss` (SQL `time`); same rule as `opens_at`                                                                              | Closing time                                                                                                                                                       |
| `is_closed`            | Boolean | Yes           | default `false`; cannot be `true` together with `is_twenty_four_hours`                                                        | `true` = the facility is fully closed on this day                                                                                                                  |
| `is_twenty_four_hours` | Boolean | Yes           | default `false`; cannot be `true` together with `is_closed`                                                                   | `true` = the facility is open the entire day with no specific window (`opens_at`/`closes_at` are `null`)                                                           |

> **Note:** `day_of_week_id` (used to resolve `day_of_week`) is a write-only input, supplied per entry in
> [Set Weekly Schedule](#set-weekly-schedule)'s `days` — it does not appear on this data model because the
> response always returns the resolved `day_of_week` object instead.

### Overlap and Cross-Day Validation

Multiple rows may share the same `day_of_week_id` — a day can have several windows (breaks) — but on every
write (both [Set Weekly Schedule](#set-weekly-schedule) and [Create Resort
Facility](resort-facilities-api.md#create-resort-facility)'s embedded `operating_hours`), the following are all
checked and return `409 CONFLICT` on violation:

1. **Same-day overlap.** Two windows on the same day must not have overlapping `opens_at`/`closes_at` ranges.
2. **Same-day whole-day exclusivity.** A `is_closed=true`/`is_twenty_four_hours=true` entry cannot coexist with
   any other entry (whole-day or specific-window) for that same day.
3. **Next-day spillover overlap.** If a window's `closes_at <= opens_at` (an overnight window), the portion
   that lands on the next calendar day (`00:00` through `closes_at`) is checked for overlap against that next
   day's own windows — and against `is_closed` on the next day (an overnight window can never spill into a day
   marked fully closed). It's compatible with the next day being `is_twenty_four_hours=true` (redundant, not
   contradictory).
4. **Previous-day spillover overlap.** The same check in the other direction — a window (or an `is_closed`
   day) is checked against the *previous* day's overnight spillover, if any. This wraps: the last day of the
   week is checked against the first, and vice versa.

This is checked at the application level; the DB-level `chk_facility_operating_hours` check constraint remains
as a last-resort guard for the per-row `opens_at`/`closes_at`/`is_closed`/`is_twenty_four_hours` consistency
rule only (it does not — and cannot — enforce cross-row or cross-day overlap).

---

## Set Weekly Schedule

`PUT /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/operating-hours/schedule`

Replaces the facility's **entire** operating-hours schedule in one atomic write: every currently active row for
the facility (across all days) is soft-deleted, and every row described in the request is created fresh. This
is the only way to edit a schedule once the facility exists — it enforces the all-or-nothing completeness rule
(every active day of week must be covered) and validates cross-day overnight spillover across the whole week in
one pass, including the wraparound from the last day of the week back to the first.

`days` must contain **exactly one entry per active day of week** — omitting a day, or including an unknown or
duplicate `day_of_week_id`, is rejected before anything is written (see [Request Fields](#request-fields) and
[Error Responses](#error-responses)). Each entry is one of three shapes:

- `is_closed=true` — closed all day; `windows` must be empty.
- `is_twenty_four_hours=true` — open all day, no specific window; `windows` must be empty.
- `is_closed=false, is_twenty_four_hours=false` — `windows` must contain one or more `{opens_at, closes_at}`
  windows (a single window, or several for a day with a break). Windows within the same day must not overlap.

Cross-day overnight spillover (see [Overlap and Cross-Day Validation](#overlap-and-cross-day-validation)) is
checked for every consecutive pair of days in the request, wrapping from the last day back to the first.

**Editing a single day still means resubmitting all seven** — fetch the current schedule (`GET` the list
endpoint below), change the one day's `windows`/`is_closed`/`is_twenty_four_hours` client-side, and `PUT` the
whole `days` array back. See [Why no per-row write endpoints](#why-no-per-row-write-endpoints).

### Path Parameters

| Parameter     | Type | Description               |
|---------------|------|---------------------------|
| `resort-id`   | Long | ID of the owning resort   |
| `facility-id` | Long | ID of the resort facility |

### Request Body

A full week: Monday has a lunch/dinner break, Tuesday is closed, Wednesday is open 24 hours, and Sunday's
overnight window (`22:00:00`–`02:00:00`) spills into Monday's early hours without conflict because Monday's
first window doesn't start until `09:00:00`:

```json
{
  "days": [
    {
      "day_of_week_id": 1,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "09:00:00",
          "closes_at": "14:00:00"
        },
        {
          "opens_at": "17:00:00",
          "closes_at": "23:00:00"
        }
      ]
    },
    {
      "day_of_week_id": 2,
      "is_closed": true,
      "is_twenty_four_hours": false,
      "windows": []
    },
    {
      "day_of_week_id": 3,
      "is_closed": false,
      "is_twenty_four_hours": true,
      "windows": []
    },
    {
      "day_of_week_id": 4,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "09:00:00",
          "closes_at": "23:00:00"
        }
      ]
    },
    {
      "day_of_week_id": 5,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "09:00:00",
          "closes_at": "23:00:00"
        }
      ]
    },
    {
      "day_of_week_id": 6,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "16:00:00",
          "closes_at": "01:00:00"
        }
      ]
    },
    {
      "day_of_week_id": 7,
      "is_closed": false,
      "is_twenty_four_hours": false,
      "windows": [
        {
          "opens_at": "22:00:00",
          "closes_at": "02:00:00"
        }
      ]
    }
  ]
}
```

### Request Fields

| Field                         | Type    | Required | Validation                                                                                                                |
|-------------------------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------|
| `days`                        | Array   | Yes      | Not empty; exactly one entry per active day of week, no duplicates, no unknown ids                                        |
| `days[].day_of_week_id`       | Long    | Yes      | Not null; must reference an existing, active day of week                                                                  |
| `days[].is_closed`            | Boolean | Yes      | Not null; cannot be `true` together with `is_twenty_four_hours`                                                           |
| `days[].is_twenty_four_hours` | Boolean | Yes      | Not null; cannot be `true` together with `is_closed`                                                                      |
| `days[].windows`              | Array   | —        | Must be empty when `is_closed`/`is_twenty_four_hours` is `true`; at least one entry otherwise; no two entries may overlap |
| `days[].windows[].opens_at`   | String  | Yes      | `HH:mm:ss`                                                                                                                |
| `days[].windows[].closes_at`  | String  | Yes      | `HH:mm:ss`; `<= opens_at` means the window rolls past midnight into the next calendar day                                 |

### Response `200 OK`

Returns every row created by the replace, across all seven days (`day_of_week` embeds the locale-matched
translation the same way as [List Operating Hours](#list-operating-hours)):

```json
{
  "data": [
    {
      "id": 11,
      "day_of_week": {
        "id": 1,
        "code": "MONDAY",
        "sort_order": 1,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Monday",
          "short_name": "Mon",
          "description": "",
          "sort_order": 1
        }
      },
      "opens_at": "09:00:00",
      "closes_at": "14:00:00",
      "is_closed": false,
      "is_twenty_four_hours": false
    },
    {
      "id": 12,
      "day_of_week": {
        "id": 1,
        "code": "MONDAY",
        "sort_order": 1,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Monday",
          "short_name": "Mon",
          "description": "",
          "sort_order": 1
        }
      },
      "opens_at": "17:00:00",
      "closes_at": "23:00:00",
      "is_closed": false,
      "is_twenty_four_hours": false
    }
  ]
}
```

> **Note:** the response is not paginated — it always returns every row from the replace in one array. `resort_facility`
> is omitted from each row here (unlike [List](#list-operating-hours)) since it's identical on every row and already known
> from the URL path. Every `id` in the response is a *new* row id —
> see [Why no per-row endpoints](#why-no-per-row-endpoints) on id churn.

---

## List Operating Hours

`GET /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/operating-hours`

Returns a paginated list of every active operating-hours row belonging to the facility (typically at most
seven — one per day of week — but more if any day has a break, since each window is its own row). There is no
filtering. This is the endpoint to call before editing a schedule via [Set Weekly
Schedule](#set-weekly-schedule) — fetch the current rows, group them by `day_of_week`, change what you need,
and resubmit the full week.

> **Note:** `sortBy`/`sortDir` are accepted on the request object but there are no sortable fields registered
> for this endpoint — passing any non-null `sortBy` value throws
> `400 INVALID_ARGUMENT: Invalid sort field: <value>`. Omit `sortBy` entirely to get the default (sorted by
> `id` ascending).

### Path Parameters

| Parameter     | Type | Description                      |
|---------------|------|----------------------------------|
| `resort-id`   | Long | ID of the owning resort          |
| `facility-id` | Long | ID of the owning resort facility |

### Query Parameters

| Parameter | Type | Default | Constraints | Description              |
|-----------|------|---------|-------------|--------------------------|
| `page`    | int  | `0`     | >= 0        | Zero-based page index    |
| `size`    | int  | `10`    | 1 – 50      | Number of items per page |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "resort_facility": {
        "id": 2,
        "code": "SWIMMING_POOL",
        "sort_order": 2,
        "is_highlighted": false,
        "icon_type": "LUCIDE",
        "icon_value": "Waves",
        "icon_meta": {
          "size": 24,
          "color": "#06b6d4"
        },
        "locale": {
          "id": 2,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Swimming Pool",
          "description": "Outdoor infinity pool with sun deck and loungers.",
          "sort_order": 2
        }
      },
      "day_of_week": {
        "id": 1,
        "code": "MONDAY",
        "sort_order": 1,
        "locale": {
          "id": 1,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Monday",
          "short_name": "Mon",
          "description": "",
          "sort_order": 1
        }
      },
      "opens_at": "06:00:00",
      "closes_at": "21:00:00",
      "is_closed": false,
      "is_twenty_four_hours": false
    },
    {
      "id": 2,
      "resort_facility": {
        "id": 2,
        "code": "SWIMMING_POOL",
        "sort_order": 2,
        "is_highlighted": false,
        "icon_type": "LUCIDE",
        "icon_value": "Waves",
        "icon_meta": {
          "size": 24,
          "color": "#06b6d4"
        },
        "locale": {
          "id": 2,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Swimming Pool",
          "description": "Outdoor infinity pool with sun deck and loungers.",
          "sort_order": 2
        }
      },
      "day_of_week": {
        "id": 2,
        "code": "TUESDAY",
        "sort_order": 2,
        "locale": {
          "id": 2,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Tuesday",
          "short_name": "Tue",
          "description": "",
          "sort_order": 2
        }
      },
      "opens_at": null,
      "closes_at": null,
      "is_closed": true,
      "is_twenty_four_hours": false
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 2,
  "page_size": 10,
  "has_next": false,
  "has_previous": false,
  "sortable_fields": null,
  "searchable_fields": null
}
```

---

## Why no per-row endpoints

Earlier versions of this API had `POST`, `GET /{id}`, `PUT /{id}`, and `DELETE /{id}` for
creating/reading/editing/deleting a single day's row in isolation. The three writes were removed once [Set
Weekly Schedule](#set-weekly-schedule) existed, for two reasons:

1. **A single-row write can never be fully self-contained.** [Overlap and Cross-Day
   Validation](#overlap-and-cross-day-validation) means changing one day can affect (and be affected by) its
   previous and next day. A per-row endpoint has to reach outside the row it's editing to validate correctly
   anyway — at that point it's not meaningfully simpler than resubmitting the week, and it can't offer the same
   atomicity: several sequential per-row calls can leave the schedule briefly inconsistent if one fails partway
   through, and can't guarantee the all-or-nothing completeness rule the way one atomic replace can.
2. **One write path is simpler to reason about and test than two.** With per-row endpoints gone, every write —
   at creation ([Create Resort Facility](resort-facilities-api.md#create-resort-facility)) and at any point
   after ([Set Weekly Schedule](#set-weekly-schedule)) — goes through the exact same validation code and the
   exact same request shape.

`GET /{id}` was removed separately, for a narrower reason: because row `id`s churn on every `PUT .../schedule`
call (see below), a client essentially never has a usable `id` to look up *without* already having fetched it
via [List Operating Hours](#list-operating-hours) first — so a single-row `GET` mostly just duplicated what the
list endpoint already returns, for a marginal convenience.

The tradeoff: **editing a single day still means sending all seven, and reading one row means fetching the
whole list.** The client (frontend) is expected to `GET` the current schedule, mutate the one day it cares
about, and `PUT` the whole `days` array back to [Set Weekly Schedule](#set-weekly-schedule). This also means row
`id`s are not stable across edits — every `PUT .../schedule` soft-deletes the previous rows and creates new
ones (even for days whose content didn't change), so a previously-fetched `id` should not be cached or relied
on after any schedule write.

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "ResortFacility not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                                                                                                                                                         |
|-------------|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); missing/invalid required fields; an unsupported `sortBy` query value; `is_closed` and `is_twenty_four_hours` both `true`; `opens_at`/`closes_at` inconsistent with them; a duplicate `day_of_week_id` in `days`; or `days` missing one or more active days of week              |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found; resort facility not found for the given `resort-id`/`facility-id` pair (including a `facility-id` that belongs to a different resort); an unknown `day_of_week_id` in `days`                                                                                                                                                                                                |
| 409         | `CONFLICT`                 | Same-day overlap; same-day whole-day/specific-window exclusivity; an overnight window's spillover overlapping the next day's rows (or the next day being `is_closed`); or a window/`is_closed` day conflicting with the previous day's overnight spillover — see [Overlap and Cross-Day Validation](#overlap-and-cross-day-validation). Pre-checked at the application level before any write |
| 409         | `DATA_INTEGRITY_VIOLATION` | Last-resort DB check constraint (`chk_facility_operating_hours`) if an `opens_at`/`closes_at`/`is_closed`/`is_twenty_four_hours` inconsistency somehow bypassed the application-level check. Cross-row and cross-day overlap are only enforced at the application level — there is no DB constraint for either.                                                                               |
