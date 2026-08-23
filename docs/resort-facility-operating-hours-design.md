# Resort Facility Operating Hours — System Design

This document explains how `ResortFacilityOperatingHours` models a facility's weekly schedule internally —
the data model, why the schema looks the way it does, multi-window (break) support, and same-day/cross-day
overlap validation. It's an internal engineering reference, not an API contract — see
`docs/resort-facility-operating-hours-api.md` for the request/response contract.

---

## 1. The problem this solves

A [Resort Facility](resort-facilities-api.md) needs an open/closed schedule per [Day of
Week](days-of-week-api.md) — e.g. a pool open 06:00–21:00 on Mondays, a spa closed on Tuesdays, a 24-hour gym.
Real-world facilities also close for a midday break and reopen later the same day (a restaurant open for lunch
and again for dinner), and often stay open past midnight (a bar open Friday 20:00 through Saturday 02:00). The
model has to represent all of that with one row shape, without a `type` discriminator column, while also
guaranteeing a facility's schedule is never "half filled in" — either every day of the week has a state, or
none do. That last guarantee is strong enough that it shapes the write API itself — see §6.

---

## 2. Data model

`resort_facility_operating_hours` (`src/main/resources/db/migration/V30__create_resort_facility_operating_hours.sql`):

| Column                                             | Type                             | Notes                                                                             |
|----------------------------------------------------|----------------------------------|-----------------------------------------------------------------------------------|
| `resort_facility_id`                               | `bigint` FK, not null            | owning facility                                                                   |
| `day_of_week_id`                                   | `bigint` FK, not null            | the day this row applies to                                                       |
| `opens_at` / `closes_at`                           | `time`, nullable                 | populated only for a specific-window row (see §4)                                 |
| `is_closed`                                        | `boolean not null default false` | `true` = facility fully closed this day                                           |
| `is_twenty_four_hours`                             | `boolean not null default false` | `true` = open the entire day, no specific window                                  |
| `chk_facility_operating_hours`                     | check constraint                 | enforces the three-state shape below, per row                                     |
| `idx_resort_facility_operating_hours_facility_day` | index (not unique)               | speeds up the facility-scoped lookups in §6; does **not** enforce one-row-per-day |

`ResortFacilityOperatingHoursEntity` (`resort/model/entity/`) mirrors this 1:1 — `@Getter @Setter` only, no
builder, extends `AuditableEntity` for soft-delete/audit columns, per the codebase-wide entity convention.

### Per-row three-state shape

Every row is in exactly one of three states, enforced twice — at the application level
(`ResortFacilityOperatingHoursScheduleValidator#validateDayShape`, §5) and, as a last-resort guard, by
`chk_facility_operating_hours`:

| `is_closed` | `is_twenty_four_hours` | `opens_at`/`closes_at` | Meaning                    |
|-------------|------------------------|------------------------|----------------------------|
| `true`      | `false`                | both `null`            | closed all day             |
| `false`     | `true`                 | both `null`            | open all day, no window    |
| `false`     | `false`                | both required          | open for a specific window |

`is_closed` and `is_twenty_four_hours` can never both be `true` — both describe the *whole day* and are
mutually exclusive with each other, and (per §4) with any other row for that day.

---

## 3. Ownership and cascading

`ResortFacilityOperatingHoursEntity` has two `@ManyToOne` parents — `ResortFacilityEntity` and
`DayOfWeekEntity` — wired through `commons/model/entity/EntityRelationshipHelper.java`'s `addChild`/`removeChild`
pattern rather than a plain `@OneToMany(mappedBy=...)`:

```java
resortFacilityEntity.addResortFacilityOperatingHoursEntity(entity);
dayOfWeekEntity.

addResortFacilityOperatingHoursEntity(entity);
```

Both sides keep a `Set<ResortFacilityOperatingHoursEntity>` (`LinkedHashSet`, no uniqueness enforced by the
collection itself — see §4 for why the *data* is still constrained). `resortFacilityEntity`'s side is
`cascade = CascadeType.ALL, orphanRemoval = true` — this is what lets both `ResortFacilityOperatingHoursServiceImpl`
(§6) and `ResortFacilityServiceImpl` (§7) build rows in memory and have them persisted purely by saving their
owning `ResortFacilityEntity`, with no direct repository/service call of their own.

---

## 4. Multi-window (break) support

**A facility+day is no longer capped at one row.** The original schema had `unique (resort_facility_id,
day_of_week_id)`; it was removed specifically so a facility can have several active specific-window rows for
the same day — e.g. `09:00:00–14:00:00` and `17:00:00–23:00:00` for a restaurant that closes for a break in
the afternoon. Nothing distinguishes "window 1" from "window 2" at the schema level; a day's full schedule is
just *every active row that shares that `resort_facility_id`/`day_of_week_id`*, and the client is expected to
sort them by `opens_at` for display.

This only works because whole-day states (`is_closed`/`is_twenty_four_hours`) are still exclusive per day — see
§5. Removing the unique constraint without that rule would let a row say "closed all day" while another row for
the same day says "open 09:00–14:00," which is meaningless.

---

## 5. Overlap validation — same-day and cross-day (`ResortFacilityOperatingHoursScheduleValidator`)

All overlap/completeness/shape validation lives in one stateless `@UtilityClass`,
`resort/validation/ResortFacilityOperatingHoursScheduleValidator.java` — not on either ServiceImpl that uses
it. It operates purely on request DTOs (`ResortFacilityOperatingHoursDayScheduleRequest`/
`ResortFacilityOperatingHoursWindowRequest`) and `DayOfWeekEntity`, never touching a repository itself. Two
callers run the identical validation chain against the identical `days`/`operating_hours` shape:
`ResortFacilityOperatingHoursServiceImpl#setWeeklySchedule` (§6, editing an existing facility's schedule) and
`ResortFacilityServiceImpl#create` (§7, a brand-new facility's initial schedule). See §8 for why there's no
third caller that validates a single row against rows already in the database.

### Same-day validation (`validateWindowsDoNotOverlap`, plus the whole-day check inside `validateDayShape`)

1. **Whole-day exclusivity.** `validateDayShape` (§6 step 2) already guarantees a day is either
   `is_closed`/`is_twenty_four_hours` (with `windows` empty) or a specific-window day (with `windows`
   non-empty) — never both, and never a mix. Because every day in the request is independently forced into one
   of those two shapes, whole-day-vs-specific-window conflict *within a day* can't arise in the first place;
   there's no separate "reject if a sibling is already whole-day" step the way a per-row endpoint checking
   against pre-existing DB rows would need (see §8's history note on why that approach was removed).
2. **Time overlap.** `validateWindowsDoNotOverlap` pairwise-checks every two entries in a day's own `windows`
   list with `windowsOverlap`; any overlap throws `IllegalStateException` (→ `409 CONFLICT`, see §9) naming
   both windows' times.

### Overnight windows and the interval helpers

`opens_at`/`closes_at` are plain clock times with no "closes after opens" ordering requirement anywhere
(matching the API doc's stated behavior) — a window can read `opens_at=23:00:00, closes_at=03:00:00` to mean
"open until 3am the next day." Overlap detection has to account for that wraparound, so each window is first
normalized into one or two half-open `[start, end)` intervals on a 0–1440-minutes-since-midnight timeline via
`toIntervals`:

```java
public List<int[]> toIntervals(LocalTime start, LocalTime end) {
    int startMinutes = start.toSecondOfDay() / 60;
    int endMinutes = end.toSecondOfDay() / 60;
    if (endMinutes > startMinutes) {
        return List.of(new int[]{startMinutes, endMinutes});          // same-day window
    }
    return List.of(new int[]{startMinutes, 1440}, new int[]{0, endMinutes}); // wraps past midnight
}
```

`windowsOverlap` does a standard pairwise interval-overlap test (`max(startA, startB) < min(endA, endB)`)
across every interval-pair from the two windows (1×1, 1×2, or 2×2 depending on whether either wraps) — this is
correct for **same-day** comparisons, where both windows genuinely live on the same 0–1440 timeline:

```java
public boolean windowsOverlap(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
    for (int[] a : toIntervals(aStart, aEnd)) {
        for (int[] b : toIntervals(bStart, bEnd)) {
            if (Math.max(a[0], b[0]) < Math.min(a[1], b[1])) {
                return true;
            }
        }
    }
    return false;
}
```

**Worked example** — a day submits `23:00:00–03:00:00` and `02:00:00–06:00:00` as two of its `windows`:

- First window → intervals `[1380, 1440)` and `[0, 180)`.
- Second window → single interval `[120, 360)`.
- `max(0, 120)=120 < min(180, 360)=180` → overlap detected against the `[0,180)` piece → `409 CONFLICT`.

### Cross-day spillover (`validateSpilloverAcrossWeek`)

An overnight window's wrapped tail (`toIntervals`'s `[0, endMinutes)` piece) is not just an artifact of the
math — those minutes are *real open hours on the next calendar day*. A window `16:00:00–01:00:00` on Monday
means the facility is physically open Tuesday `00:00`–`01:00`, so it has to be checked against Tuesday's own
`windows` the same way any of Tuesday's own windows would be. Reusing `windowsOverlap` directly for this would
be wrong, though: it would also compare Monday's *same-day* portion (`[960, 1440)`, i.e. Monday 16:00–24:00)
against Tuesday's timeline, which are not the same clock hours at all. A dedicated helper isolates only the
spillover piece:

```java
public boolean spilloverOverlapsWindow(LocalTime prevOpensAt, LocalTime prevClosesAt,
                                       LocalTime nextOpensAt, LocalTime nextClosesAt) {
    int spilloverEnd = prevClosesAt.toSecondOfDay() / 60; // minutes into the next day
    for (int[] b : toIntervals(nextOpensAt, nextClosesAt)) {
        if (Math.max(0, b[0]) < Math.min(spilloverEnd, b[1])) {
            return true;
        }
    }
    return false;
}
```

`hasSpillover(opensAt, closesAt)` (`closesAt <= opensAt`) gates whether a window spills at all.
`validateSpilloverAcrossWeek` walks the request's `days` in `allDaysOfWeek`'s `sort_order` sequence, wrapping
last→first, and for each consecutive pair (`current`, `next`):

- If `current` is whole-day, it has no windows to spill — skip.
- For each of `current`'s windows that has spillover: if `next` is `is_closed` → always a conflict (spillover
  can't be open during a day marked fully closed); if `next` is `is_twenty_four_hours` → fine (redundant, not
  contradictory); otherwise check `spilloverOverlapsWindow` against every one of `next`'s windows.

Because this walks the *whole* week in one pass against the *whole* request, both directions (a day's outgoing
spillover into the next day, and a day's incoming exposure to the previous day's spillover) are covered by the
same single loop — there's no separate "previous-day" pass the way an isolated single-row check would need.

**Worked example** — Monday's `windows` include `16:00:00–01:00:00` (spills into Tuesday `00:00–01:00`) and
Tuesday's `windows` include `00:30:00–08:00:00`. `validateSpilloverAcrossWeek` rejects this:
`spilloverOverlapsWindow(16:00, 01:00, 00:30, 08:00)` computes `spilloverEnd=60`, Tuesday's window is a single
interval `[30, 480)`, and `max(0,30)=30 < min(60,480)=60` → overlap. If Tuesday's window instead started at
`01:00:00` or later (e.g. `04:00:00–08:00:00`), it would be accepted — Monday's spillover interval `[0,60)` no
longer intersects `[240, 480)`.

`allDaysOfWeek` (every active `DayOfWeekEntity`, ordered by `sort_order`) is resolved once by the controller
(`DayOfWeekService.getAllActiveEntities()`, a new method added specifically to give both write paths the
canonical week sequence) and passed into the validator — see §8.

---

## 6. Weekly-schedule completeness (`setWeeklySchedule`)

**A facility's schedule is all-or-nothing.** `PUT .../operating-hours/schedule` is the *only* way to write a
schedule for a facility that already exists (§7 covers the other write path, at creation) — it's a single
atomic write that replaces the facility's *entire* schedule and requires the request to cover every active day
of week exactly once.

`SetResortFacilityOperatingHoursScheduleRequest` carries `days: List<ResortFacilityOperatingHoursDayScheduleRequest>`,
one entry per day (`day_of_week_id`, `is_closed`, `is_twenty_four_hours`, `windows: List<{opens_at,
closes_at}>`). `ResortFacilityOperatingHoursServiceImpl#setWeeklySchedule` runs, in order (steps 1–4 delegate to
`ResortFacilityOperatingHoursScheduleValidator`, §5):

1. **`validateWeekCompleteness`** — `days` must have no duplicate `day_of_week_id`, no id outside the active
   `allDaysOfWeek` set (→ `404 ENTITY_NOT_FOUND`), and no missing id (→ `400 INVALID_ARGUMENT`). This is the
   check that makes the endpoint's completeness guarantee real — every other validation step assumes it already
   passed.
2. **`validateDayShape`** per day — the three-state shape from §2.
3. **`validateWindowsDoNotOverlap`** per day — §5's same-day check.
4. **`validateSpilloverAcrossWeek`** — §5's cross-day check, across the whole week in one pass.
5. **Replace.** Every currently active row for the facility (`findAllByResortFacilityEntity_IdAndIsActiveAndIsDeleted`,
   across *all* days, not just the ones being touched) is soft-deleted, then one new row per day (whole-day) or
   per window (specific-window days) is built via `buildRow` (which calls
   `ResortFacilityOperatingHoursMapper.create(isClosed, isTwentyFourHours, opensAt, closesAt)` and attaches the
   result to both parents) and saved. All in one `@Transactional` method — a validation failure at any step 1–4
   leaves the existing schedule completely untouched.

The response returns every newly created row (`ResortFacilityOperatingHoursScheduleResponse`, an unpaginated
list) rather than a bare `SuccessResponse`, since a single call can create anywhere from 7 rows (one whole-day
state each) to many more (any day with a break contributes multiple rows). Row `id`s are **not** stable across
calls — every call soft-deletes the old rows and creates new ones, even for days whose content didn't change
(see §10).

---

## 7. Create Resort Facility's embedded schedule

**`POST /api/v1/resorts/{resort-id}/facilities` (Create Resort Facility) accepts an optional `operating_hours`
field with the exact same shape and rules as §6** (see `docs/resort-facilities-api.md#create-resort-facility`).
**Most facilities don't have a schedule at all** — `operating_hours` is nullable/omittable, and a `null` or
empty value creates the facility with zero operating-hours rows; a schedule can always be attached later via
`PUT .../operating-hours/schedule` (§6). When `operating_hours` *is* supplied, it's still all-or-nothing exactly
as before: `ResortFacilityServiceImpl#create` runs the identical four-step validation from §5 directly against
`request.getOperatingHours()`, then — instead of calling `setWeeklySchedule` against an already-persisted
facility — builds each `ResortFacilityOperatingHoursEntity` row itself (via the same
`ResortFacilityOperatingHoursMapper.create(isClosed, isTwentyFourHours, opensAt, closesAt)` overload `buildRow`
uses) and attaches it to the *in-memory, not-yet-saved* `ResortFacilityEntity` and its resolved
`DayOfWeekEntity` before the single `resortFacilityRepository.save(entity)` call. Because
`ResortFacilityEntity`'s `resortFacilityOperatingHoursEntities` collection is `cascade = CascadeType.ALL` (§3),
that one save cascades the facility, its locale, and every operating-hours row (if any) in one INSERT batch —
there's no separate call into `ResortFacilityOperatingHoursRepository` or `ResortFacilityOperatingHoursService`
at all. This mirrors how the facility's initial locale is already attached before save, and follows the
codebase-wide "cascade owned children through the parent's single save, not a child service call" convention.
The controller only fetches `DayOfWeekService.getAllActiveEntities()` (needed for §5's completeness and
cross-day checks) when `operating_hours` is actually present, since the common case supplies none.

Cross-day spillover validation here (`validateSpilloverAcrossWeek`) only ever runs against the request's own
seven days — a brand-new facility has no existing rows to conflict with, so unlike §6 there's no
`ResortFacilityOperatingHoursRepository` query involved on the operating-hours side at all; the only query is
`ResortFacilityRepository.existsByResortEntity_IdAndFacilityEntity_IdAndIsActiveAndIsDeleted` for the unrelated
platform-facility-link uniqueness check that already existed before this feature.

If a *supplied* `operating_hours` fails validation, the entire `POST` is rejected before the facility, its
locale, or anything else is created — see §10's note on the lack of a partial-success path. Omitting
`operating_hours` sidesteps this entirely, since there's then nothing to validate.

---

## 8. Request flow, and why there's no per-row endpoint

Following the codebase-wide layered shape (`CLAUDE.md` §"Per-entity layered shape"):

1. **`ResortFacilityOperatingHoursController`** resolves `ResortFacilityEntity` (via
   `ResortFacilityService.getEntityById(resortId, facilityId)`, which also validates the resort/facility path
   pair) and, for `setWeeklySchedule`, the full ordered active-day list (via
   `DayOfWeekService.getAllActiveEntities()`, needed for §5's cross-day and completeness checks). Both are
   resolved in the controller and passed into the service as entities, never as bare ids — the service layer
   never resolves foreign keys or queries another domain's repository itself. `ResortFacilityController` does
   the same for `create` (§7).
2. **`ResortFacilityOperatingHoursServiceImpl`** exposes exactly two methods: `getAll` and `setWeeklySchedule`
   — **there is no `create`, `update`, `delete`, or single-row `getById`/`getEntityById`.** `setWeeklySchedule`
   is `@Transactional`; its replace step is soft (`isDeleted=true, isActive=false`, then save — never a hard
   delete).
3. **`ResortFacilityOperatingHoursMapper`** (`@UtilityClass`) has one entity-building method,
   `create(isClosed, isTwentyFourHours, opensAt, closesAt)` (no request-DTO overload — there's no single-row
   request DTO left to map from), and `toDto`. It does not touch `resortFacility`/`dayOfWeek` DTO fields
   itself — callers fill those in separately via `ResortFacilityMapper.toDto(...)` / `DayOfWeekMapper.toDto(...)`,
   since this entity has no locale sub-resource of its own to gate an `includeX` flag around.
   `setWeeklySchedule`'s response omits `resortFacility` on every row (it's identical across the whole response
   and already known from the URL) but still fills in `dayOfWeek` per row.

### History: `POST`/`GET /{id}`/`PUT /{id}`/`DELETE /{id}` were removed

An earlier version of this API had per-row `create`/`getById`/`update`/`delete` alongside `setWeeklySchedule` —
a facility+day's row(s) could be read or written in isolation, with `create`/`update` fetching that day's
existing siblings plus the previous/next day's existing rows (`validateAgainstExistingRows`, `siblingsExcluding`,
`adjacentDay`, and per-row mirrors of §5's same-day/cross-day checks) to validate against. The three write
methods were deleted first, once every caller was required to go through a whole-week shape anyway (§6/§7): a
single-row write was never actually self-contained (§5's cross-day rule means editing one day can depend on,
and affect, its neighbors), so the per-row endpoints bought isolation they couldn't really offer while
duplicating validation logic against a second data shape (DB entities instead of request DTOs). `getById`/
`getEntityById` were deleted separately and afterward, for a narrower reason: once row `id`s stopped being
stable across writes (§6), there was rarely a case where a caller had a usable `id` in hand without having
already fetched it via `getAll` — so the single-row read mostly duplicated the list endpoint for a marginal
convenience, and was removed too. What's left is one read path (`getAll`), one write path
(`setWeeklySchedule`), one validated shape (`ResortFacilityOperatingHoursScheduleValidator`, §5), and one
atomicity guarantee end-to-end. The tradeoff is explicit: editing a single day now means fetching the current
schedule (`GET` the list endpoint) and resubmitting all seven days via `setWeeklySchedule`, and reading one row
means fetching the whole list — there is no lighter-weight option for either, by design.

---

## 9. Error mapping

| Java exception                    | HTTP status | Thrown when                                                                                                                                                                                       |
|-----------------------------------|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `IllegalArgumentException`        | 400         | `validateDayShape` — the three-state shape (§2) is violated; or `validateWeekCompleteness`'s completeness/duplicate-id check (§5), inside `setWeeklySchedule` (§6) or Create Resort Facility (§7) |
| `IllegalStateException`           | 409         | `validateWindowsDoNotOverlap`/`validateSpilloverAcrossWeek` — same-day or cross-day overlap (§5), inside `setWeeklySchedule` (§6) or Create Resort Facility (§7)                                  |
| `EntityNotFoundException`         | 404         | Facility/operating-hours row not found; or an unknown `day_of_week_id` inside `setWeeklySchedule`'s or Create Resort Facility's `days`/`operating_hours` (§5's `validateWeekCompleteness`)        |
| `DataIntegrityViolationException` | 409         | `chk_facility_operating_hours` violated at the DB level (last-resort guard only — no DB constraint backs the overlap rule itself, see §10)                                                        |

`GlobalExceptionHandler` (`commons/exception/`) handles all four centrally; no per-entity exception handling
exists in this module.

---

## 10. Known gaps

- **Overlap/spillover validation is application-level only — there is no DB constraint backing it.** Unlike
  the old `uq_resort_facility_operating_hours` unique constraint it replaced, nothing in Postgres itself
  prevents two overlapping rows (same-day or cross-day) from being inserted for the same facility. A
  `btree_gist` exclusion constraint would close the same-day gap but requires the `btree_gist` extension and a
  generated time-range column, and still couldn't express the cross-day spillover rule (which depends on a
  *different* row, for the adjacent `day_of_week_id`) — not implemented. In practice this means two concurrent
  `setWeeklySchedule` writes for the same facility could both pass the application-level check and both commit,
  producing an overlap that only the next validated write would catch.
- **`setWeeklySchedule` fully replaces the schedule even for days whose content didn't change.** There's no
  diffing — every active row is soft-deleted and every day's rows are recreated with new ids on *every* call,
  even a call that only actually changes one day. This is also why there's no lightweight "just change
  Tuesday" path (§8) — but it does mean `id` values are never stable across schedule edits, which callers
  should not rely on.
- **No explicit ordering column.** Multiple windows for the same day are ordered implicitly by `id` (insertion
  order) in list responses, not by `opens_at`. A client rendering "lunch then dinner" needs to sort client-side
  if a window was created out of chronological order (this is exactly why `setWeeklySchedule`'s request makes
  each day's `windows` order explicit in the request, even though the *response* still doesn't guarantee an
  order beyond insertion).
- **`is_active` is never toggled to `false` independently of `is_deleted`.** Both are always flipped together
  on `setWeeklySchedule`'s replace step (§6); there's currently no "temporarily deactivate a window without
  deleting it" flow.
- **Create Resort Facility has no partial-success path.** If `operating_hours` validation fails, the entire
  `POST` is rejected before the facility, its locale, or anything else is created (§7) — there is no way to
  create the facility first and attach a schedule afterward in the same call. A caller with an invalid schedule
  must fix `operating_hours` and resubmit the whole request, including `code`/`locale`/etc.
