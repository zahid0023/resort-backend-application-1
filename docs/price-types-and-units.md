# Price Types & Price Units — Concepts and Design

This document explains what `price_types` and `price_units` are, why both exist, and how they work
together to support flexible pricing across different kinds of bookable resources (hotel rooms,
sports fields, activity slots, etc.).

---

## The Core Problem

A resort charges different prices for different things in different ways.

- A **hotel room** costs $200 per night, but $300 per night on weekends.
- A **football field** costs $30 per hour on weekdays and $50 per hour on weekends.
- A **conference room** costs $150 per day for regular use and $200 per day during holidays.

Two separate dimensions are at play:

| Dimension      | Question it answers                 | Examples                                 |
|----------------|-------------------------------------|------------------------------------------|
| **Price Type** | *What pricing tier/period applies?* | Weekday, Weekend, Holiday, High Season   |
| **Price Unit** | *What is the price measured in?*    | Per Night, Per Hour, Per Day, Per Person |

**Price Type** and **Price Unit** are independent. A weekday price can be per-night or per-hour
depending on what you are booking. This is why they are two separate tables — combining them would
produce a combinatorial explosion of records and make it impossible to reuse either axis cleanly.

---

## Price Type — *What tier/period applies?*

A price type classifies **why** a particular price is in effect. It represents a named pricing
tier that the business defines and attaches to a price period.

### Examples

| Code          | Meaning                                     |
|---------------|---------------------------------------------|
| `BASE`        | Default price, no special condition         |
| `WEEKDAY`     | Applies Monday – Friday                     |
| `WEEKEND`     | Applies Saturday and Sunday                 |
| `HOLIDAY`     | Applies on public holidays                  |
| `HIGH_SEASON` | Peak demand period (e.g. summer, Christmas) |
| `LOW_SEASON`  | Off-peak period with reduced rates          |

### What it does NOT tell you

A price type says nothing about *how* the price is counted. Whether the $50 WEEKEND price is
per hour or per night is determined entirely by the **price unit** assigned to that resource.

### Extra locale fields

Because price types serve as labels visible to administrators, their locale records carry two
additional explanatory fields beyond `name` and `description`:

| Field           | Purpose                                                                             | Example                                                                    |
|-----------------|-------------------------------------------------------------------------------------|----------------------------------------------------------------------------|
| `purpose`       | Explains the business role of this tier                                             | `"Allows higher pricing during weekends when demand is elevated."`         |
| `usage_example` | A concrete scenario showing when this type is used, written for admin understanding | `"Football Ground A costs $50/hour on weekdays and $70/hour on weekends."` |

These fields exist purely to help administrators understand and configure pricing correctly. They
are not used in booking calculations.

---

## Price Unit — *What is the price measured in?*

A price unit defines the **billing granularity**: the quantity that the customer's price is
multiplied by to arrive at a total charge.

### Examples

| Code         | Meaning                                  | Total formula     |
|--------------|------------------------------------------|-------------------|
| `PER_NIGHT`  | One charge per night of stay             | `price × nights`  |
| `PER_DAY`    | One charge per calendar day              | `price × days`    |
| `PER_HOUR`   | One charge per hour of use               | `price × hours`   |
| `PER_PERSON` | One charge per guest per stay or session | `price × persons` |

### What it does NOT tell you

A price unit says nothing about *when* the price applies or whether it is a base price, a weekend
premium, or a holiday surcharge. That is determined by the **price type** attached to the price
period.

### Extra locale fields

Price unit locale records carry two additional explanatory fields:

| Field            | Purpose                                                         | Example                                       |
|------------------|-----------------------------------------------------------------|-----------------------------------------------|
| `billing_method` | Describes the mathematical formula used to calculate the charge | `"Total amount = booked hours × unit price."` |
| `usage_example`  | A concrete calculation shown to administrators                  | `"$30 × 3 hours = $90"`                       |

These fields exist so that administrators configuring price periods understand exactly what they
are setting up before saving.

---

## How They Work Together

Every price period stored in the system is stamped with:

- A **price type** → which tier/period this price belongs to
- A **price unit** → how to multiply the price into a customer total
- A **price amount** → the numeric rate

### Worked Examples

#### Hotel Room — Deluxe, Summer Weekend

| Field        | Value          |
|--------------|----------------|
| Price amount | $300           |
| Price type   | `WEEKEND`      |
| Price unit   | `PER_NIGHT`    |
| Date range   | Jun 1 – Aug 31 |

Customer books 3 weekend nights → **$300 × 3 = $900**

---

#### Football Field — Weekday Booking

| Field        | Value      |
|--------------|------------|
| Price amount | $30        |
| Price type   | `WEEKDAY`  |
| Price unit   | `PER_HOUR` |
| Date range   | always     |

Customer books 3 hours on a Tuesday → **$30 × 3 = $90**

---

#### Football Field — Weekend Booking

| Field        | Value      |
|--------------|------------|
| Price amount | $50        |
| Price type   | `WEEKEND`  |
| Price unit   | `PER_HOUR` |
| Date range   | always     |

Customer books 2 hours on a Saturday → **$50 × 2 = $100**

---

#### Conference Room — Holiday

| Field        | Value           |
|--------------|-----------------|
| Price amount | $200            |
| Price type   | `HOLIDAY`       |
| Price unit   | `PER_DAY`       |
| Date range   | public holidays |

Customer books 1 holiday day → **$200 × 1 = $200**

---

## Why Two Tables Instead of One?

A single combined table (e.g. `WEEKEND_PER_HOUR`, `WEEKDAY_PER_NIGHT`) would:

1. Require a new row for every combination, making the list grow rapidly as you add resources.
2. Make it impossible to change the name or description of "Weekend" without touching every
   combined record.
3. Force administrators to search through a long undifferentiated list when they only care about
   one axis.

Keeping them separate means:

- You define `WEEKEND` once and reuse it across hotel rooms, sports fields, and activity slots.
- You define `PER_HOUR` once and reuse it wherever hourly billing is needed.
- The combination is expressed on the **price period record**, not in a lookup table.

---

## Summary

| Concept        | Answers              | Stored separately because                                                         |
|----------------|----------------------|-----------------------------------------------------------------------------------|
| **Price Type** | *What tier applies?* | The same tier (e.g. WEEKEND) is used across many resource types and billing units |
| **Price Unit** | *Billed per what?*   | The same unit (e.g. PER_HOUR) is used across many resource types and price tiers  |

A price period record joins both: `price_amount` + `price_type_id` + `price_unit_id` = a complete,
unambiguous pricing rule.
