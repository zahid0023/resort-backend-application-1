# Resort Weekly Schedule API

Base URL: `/api/v1/resorts/{resort-id}/weekly-schedule`

A resort's weekly schedule says which [days of week](days-of-week-api.md) count as WEEKDAY vs. WEEKEND **for
the whole resort** — shared by every [Resort Room Category](resort-room-categories-api.md) at that resort, and
by every currency's `WKD`/`WKE` price row on those room categories (see [Resort Room Category Prices
API](resort-room-category-prices-api.md)). It intentionally does **not** live per room category or per
currency: which days are "weekend" is a property of the resort's real-world location/calendar (e.g. Bangladesh's
weekend is Friday/Saturday, the US's is Saturday/Sunday), not of which currency a guest happens to be paying in
or which room they're looking at. An earlier version of the schema attached days to each currency's `WKD`/`WKE`
price row individually, which allowed the same resort to disagree with itself about its own weekend depending
on which currency was being viewed — this API replaces that.

**There is no per-day endpoint — the schedule is always read and written as two complete lists.** There is no
`POST`, no `GET /{id}`, no `PUT /{id}`, and no `DELETE /{id}`; only [Get Weekly
Schedule](#get-weekly-schedule) and [Update Weekly Schedule](#update-weekly-schedule), which atomically replaces
both lists at once, mirroring [Set Weekly
Schedule](resort-facility-operating-hours-api.md#set-weekly-schedule) on the Resort Facility Operating Hours
API. All records support soft-delete — deleted records are hidden from all responses.

**A resort's initial weekly schedule is set at resort creation, not afterward.** `POST /api/v1/resorts`
(see [Resorts API](resorts-api.md#create-resort)) requires a `weekly_schedule` field with the exact same
`weekday_day_of_week_ids`/`weekend_day_of_week_ids` shape used by [Update Weekly
Schedule](#update-weekly-schedule) below, and creates the schedule's rows in the same transaction as the
resort, its basic info, and its address.
There is no separate "create" step for the schedule and no way to skip it — a resort cannot exist without one.
The `PUT` endpoint on this page is exclusively how the schedule is **changed** after that, always by replacing
the whole thing, never by adding/removing one day at a time — mirroring the create shape rather than being a
partial-update endpoint.

**A resort must have a weekly schedule before any of its room categories can be given an active `WKD`/`WKE`
price** — guaranteed in practice since every resort now gets one at creation, but still enforced independently:
[Create Resort Room Category Main Price](resort-room-category-prices-api.md#create-resort-room-category-main-price)
and [Update Resort Room Category Main
Price](resort-room-category-prices-api.md#update-resort-room-category-main-price) both fail (currently
`500 INTERNAL_SERVER_ERROR` — a database trigger, not pre-validated at the application layer, see [Error
Responses](#error-responses)) if the resort has no active schedule day for that price type yet.

Resort weekly schedules are always reached nested under their owning resort; there is no top-level
`/api/v1/resort-weekly-schedules` route. Every endpoint below also validates `{resort-id}` first — an unknown
resort returns `404 ENTITY_NOT_FOUND`.

**`Accept-Language` is required on every endpoint below, with no exceptions** — a request missing (or with a
blank) `Accept-Language` header is rejected with `400 INVALID_ARGUMENT` before it reaches any endpoint (see
[Error Responses](#error-responses)). This entity has no `locale` field of its own, but the header's value
still shapes the response: it selects the locale-matched translation embedded on each entry's `day_of_week.locale`.

---

## Endpoints

| Method | Path                                          | Description                                           |
|--------|-----------------------------------------------|-------------------------------------------------------|
| PUT    | `/api/v1/resorts/{resort-id}/weekly-schedule` | Update the resort's entire weekly schedule atomically |
| GET    | `/api/v1/resorts/{resort-id}/weekly-schedule` | Get the resort's current weekly schedule              |

---

## Data Model

### ResortWeeklyScheduleDay

| Field         | Type   | Required | Constraints                                                                         | Description                                                                  |
|---------------|--------|----------|-------------------------------------------------------------------------------------|------------------------------------------------------------------------------|
| `id`          | Long   | —        | read-only                                                                           | Auto-generated identifier                                                    |
| `day_of_week` | Object | —        | read-only; see [DayOfWeek](days-of-week-api.md); resolved from an id in the request | The day of week this entry applies to (`id`, `code`, `sort_order`, `locale`) |

Every `ResortWeeklyScheduleDay` implicitly belongs to either the `weekday` or `weekend` list of the response
that returned it — the WEEKDAY/WEEKEND classification itself is never a field on the row, since which list
it's in already says which one it is.

> **Note:** `weekday_day_of_week_ids`/`weekend_day_of_week_ids` (used to resolve each `day_of_week`) are
> write-only inputs on [Update Weekly Schedule](#update-weekly-schedule) and never appear on this data model,
> because the response always returns the resolved `day_of_week` objects instead.

---

## Update Weekly Schedule

`PUT /api/v1/resorts/{resort-id}/weekly-schedule`

Replaces the resort's **entire** weekly schedule in one atomic write: every currently active schedule day for
the resort (both `weekday` and `weekend`) is soft-deleted, and a fresh set is created from
`weekday_day_of_week_ids`/`weekend_day_of_week_ids`. This is the only way to edit a resort's schedule once it
exists.

Both lists must be **non-empty** — a resort's schedule always classifies at least one day as WEEKDAY and at
least one as WEEKEND. Unlike [Set Weekly
Schedule](resort-facility-operating-hours-api.md#set-weekly-schedule) on the Resort Facility Operating Hours
API, the two lists together are **not** required to cover every day of the week — a day absent from both simply
gets no `WKD`/`WKE` override and falls back to the room category's `BAS` rate for every currency.

**Validation, checked at the application level before any write:**

- Neither list may contain a **duplicate** day of week id.
- The **same** day of week id cannot appear in both `weekday_day_of_week_ids` and `weekend_day_of_week_ids` —
  a day can't simultaneously be WEEKDAY and WEEKEND.
- Every id in either list must reference an existing, active [Day of Week](days-of-week-api.md).

**Every row id churns on every call** — because the old rows are soft-deleted and new ones created, a
previously-fetched schedule-day `id` should not be cached or relied on after this endpoint is called; re-fetch
via [Get Weekly Schedule](#get-weekly-schedule) afterward.

**Changing the schedule does not retroactively re-validate already-active `WKD`/`WKE` room category
prices** — the "resort must have a schedule" check only runs when a `WKD`/`WKE` price row is itself
created/updated, not when the schedule changes afterward, mirroring the same already-accepted limitation on
the WKD/WKE-cannot-exceed-BASE check in the [Resort Room Category Prices
API](resort-room-category-prices-api.md#price-type-rules).

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

### Request Body

```json
{
  "weekday_day_of_week_ids": [
    1,
    2,
    3,
    4
  ],
  "weekend_day_of_week_ids": [
    5,
    6
  ]
}
```

### Request Fields

| Field                     | Type   | Required | Validation                                                                                                                    |
|---------------------------|--------|----------|-------------------------------------------------------------------------------------------------------------------------------|
| `weekday_day_of_week_ids` | Long[] | Yes      | Not empty; no duplicates; no id shared with `weekend_day_of_week_ids`; each id must reference an existing, active day of week |
| `weekend_day_of_week_ids` | Long[] | Yes      | Not empty; no duplicates; no id shared with `weekday_day_of_week_ids`; each id must reference an existing, active day of week |

### Response `200 OK`

```json
{
  "weekday": [
    {
      "id": 21,
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
      }
    }
  ],
  "weekend": [
    {
      "id": 25,
      "day_of_week": {
        "id": 5,
        "code": "FRIDAY",
        "sort_order": 5,
        "locale": {
          "id": 5,
          "locale": {
            "id": 1,
            "code": "en",
            "name": "English",
            "sort_order": 1
          },
          "name": "Friday",
          "short_name": "Fri",
          "description": "",
          "sort_order": 5
        }
      }
    }
  ]
}
```

> Abbreviated above to one entry per list for readability — a real response has one entry per id submitted.

---

## Get Weekly Schedule

`GET /api/v1/resorts/{resort-id}/weekly-schedule`

Returns the resort's current active weekly schedule, split into `weekday`/`weekend` — the same shape [Update
Weekly Schedule](#update-weekly-schedule) returns. There is no pagination (bounded to at most 7 entries per
list). If the resort has no schedule set yet, both arrays are empty.

### Path Parameters

| Parameter   | Type | Description             |
|-------------|------|-------------------------|
| `resort-id` | Long | ID of the owning resort |

### Response `200 OK`

Same shape as [Update Weekly Schedule](#update-weekly-schedule)'s response above.

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

| HTTP Status | Error Code              | Cause                                                                                                                                                                                                                                                      |
|-------------|-------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`      | Missing or blank `Accept-Language` header (checked globally, before any endpoint runs — see the intro above); `weekday_day_of_week_ids`/`weekend_day_of_week_ids` missing or empty on [Update Weekly Schedule](#update-weekly-schedule)                    |
| 404         | `ENTITY_NOT_FOUND`      | Resort not found for the given `resort-id`; an unknown day of week id in either list on [Update Weekly Schedule](#update-weekly-schedule)                                                                                                                  |
| 409         | `CONFLICT`              | A duplicate day of week id within one list, or the same day of week id appearing in both `weekday_day_of_week_ids` and `weekend_day_of_week_ids`, on [Update Weekly Schedule](#update-weekly-schedule) — checked at the application layer before the write |
| 500         | `INTERNAL_SERVER_ERROR` | Not raised by this API directly — see the [Resort Room Category Prices API](resort-room-category-prices-api.md#error-responses) for the "resort has no weekly schedule yet" case that surfaces when creating/updating a `WKD`/`WKE` price                  |
