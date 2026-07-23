# Resort Facilities API

Base URL: `/api/v1/resorts/{resort-id}/facilities`

A resort facility links a platform-level facility to a specific resort facility group, allowing per-resort presentation
overrides (sort order, icon, localized names). It can also be a fully custom facility with no platform-level counterpart
when `platform_facility` is omitted. Locale-specific names and descriptions are embedded in every response via the
`locales` array and are managed separately via the locale sub-resource endpoints. All records support soft-delete —
deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                       | Description                       |
|--------|----------------------------------------------------------------------------|-----------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/facilities`                                   | Create a resort facility          |
| GET    | `/api/v1/resorts/{resort-id}/facilities`                                   | List resort facilities            |
| GET    | `/api/v1/resorts/{resort-id}/facilities/{id}`                              | Get a resort facility             |
| PUT    | `/api/v1/resorts/{resort-id}/facilities/{id}`                              | Update a resort facility          |
| DELETE | `/api/v1/resorts/{resort-id}/facilities/{id}`                              | Delete a resort facility          |
| PUT    | `/api/v1/resorts/{resort-id}/facilities/highlights`                        | Set highlighted resort facilities |
| POST   | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales`      | Create a locale translation       |
| PUT    | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales/{id}` | Update a locale translation       |
| DELETE | `/api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales/{id}` | Delete a locale translation       |

---

## Data Model

### Resort Facility

| Field                   | Type    | Required     | Constraints                                     | Description                                                                                                                                                  |
|-------------------------|---------|--------------|-------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`                    | Long    | —            | read-only                                       | Auto-generated identifier                                                                                                                                    |
| `resort_id`             | Long    | —            | read-only                                       | ID of the parent resort                                                                                                                                      |
| `resort_facility_group` | Object  | Yes (create) | not null, must exist, must belong to the resort | The resort facility group this facility belongs to. Must belong to the same resort as the `{resort-id}` path parameter. Set at creation time, not updatable. |
| `platform_facility`     | Object  | No           | must exist if given                             | The platform facility linked to this resort facility. Omitted from response if null (custom facility).                                                       |
| `facility_price_type`   | Object  | Yes          | not null, must exist                            | The facility price type (e.g. `FREE`, `PAID`). Required on create and update.                                                                                |
| `sort_order`            | Integer | Yes          | not null, >= 0                                  | Display order of this facility within its group                                                                                                              |
| `is_highlighted`        | Boolean | No           | default `false`                                 | Whether this facility is featured as a highlight. Must have 4–9 highlighted facilities per resort. Use the dedicated highlights endpoint to manage this.     |
| `icon_type`             | String  | No           | enum                                            | Icon type override. Allowed values: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`. Omitted from response if null.                                                     |
| `icon_value`            | String  | No           | max 2000 chars                                  | Icon value override — icon name, URL, or SVG markup. Omitted from response if null.                                                                          |
| `icon_meta`             | Object  | No           | any JSON object                                 | Optional rendering hints (e.g. `size`, `color`, `alt`). Omitted from response if null.                                                                       |
| `locales`               | Array   | —            | read-only here                                  | All locale translations for this resort facility                                                                                                             |
| `prices`                | Array   | —            | read-only here                                  | Active price entries for this facility. Only present when `facility_price_type.code` is `PAID`.                                                              |

> When `platform_facility` is provided, `icon_type`, `icon_value`, and `icon_meta` act as overrides for the platform
> facility's icon. When omitted (custom facility), these fields become the primary icon source.

### Resort Facility Price

Included in the `prices` array only when the facility price type is `PAID`.

| Field                | Type       | Required | Constraints       | Description                            |
|----------------------|------------|----------|-------------------|----------------------------------------|
| `id`                 | Long       | —        | read-only         | Auto-generated identifier              |
| `resort_facility_id` | Long       | —        | read-only         | ID of the parent resort facility       |
| `price_unit`         | Object     | Yes      | not null          | The unit of measurement for this price |
| `currency`           | Object     | Yes      | not null          | The currency for this price            |
| `amount`             | BigDecimal | Yes      | not null, >= 0.00 | The price amount                       |
| `notes`              | String     | No       | —                 | Optional notes about this price entry  |
| `sort_order`         | Integer    | Yes      | not null, >= 0    | Display order for this price entry     |

### Resort Facility Locale

| Field         | Type    | Required | Constraints        | Description                                     |
|---------------|---------|----------|--------------------|-------------------------------------------------|
| `id`          | Long    | —        | read-only          | Auto-generated identifier                       |
| `locale_id`   | Long    | Yes      | must exist         | ID of an existing active locale. Not updatable. |
| `name`        | String  | Yes      | not blank, max 255 | Localized display name for this facility        |
| `description` | String  | No       | unlimited          | Localized description; defaults to empty string |
| `sort_order`  | Integer | Yes      | not null           | Display order for this locale entry             |

---

## Create Resort Facility

`POST /api/v1/resorts/{resort-id}/facilities`

Creates a resort facility, optionally linking it to a platform facility and optionally seeding locale translations in
the same request. All provided `locale_id` values must reference existing, active locales. When
`facility_price_type_id` refers to a `PAID` type, the `resort_facility_price` object is required. For any other price
type, `resort_facility_price` must be omitted.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Request Body

**Linked facility with PAID price type:**

```json
{
  "resort_facility_group_id": 12,
  "facility_id": 7,
  "facility_price_type_id": 3,
  "sort_order": 1,
  "is_highlighted": true,
  "icon_type": "LUCIDE",
  "icon_value": "Waves",
  "icon_meta": {
    "size": 24,
    "color": "#3b82f6"
  },
  "resort_facility_price": {
    "price_unit_id": 2,
    "currency_id": 1,
    "amount": 25.00,
    "notes": "Per person per session",
    "sort_order": 1
  },
  "locales": [
    {
      "locale_id": 1,
      "name": "Outdoor Pool",
      "description": "Heated outdoor pool open year-round.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "আউটডোর পুল",
      "description": "সারা বছর খোলা উত্তপ্ত আউটডোর পুল।",
      "sort_order": 2
    }
  ]
}
```

**Custom facility with FREE price type (no platform counterpart, no price):**

```json
{
  "resort_facility_group_id": 12,
  "facility_price_type_id": 1,
  "sort_order": 4,
  "is_highlighted": false,
  "icon_type": "IMAGE",
  "icon_value": "https://cdn.example.com/icons/private-beach.png",
  "locales": [
    {
      "locale_id": 1,
      "name": "Private Beach Access",
      "sort_order": 1
    }
  ]
}
```

### Request Fields

| Field                      | Type    | Required                      | Validation                                                                         |
|----------------------------|---------|-------------------------------|------------------------------------------------------------------------------------|
| `resort_facility_group_id` | Long    | Yes                           | Not null, must reference an existing active group that belongs to the given resort |
| `facility_id`              | Long    | No                            | Must reference an existing active facility                                         |
| `facility_price_type_id`   | Long    | Yes                           | Not null, must reference an existing active facility price type                    |
| `sort_order`               | Integer | Yes                           | Not null, >= 0                                                                     |
| `is_highlighted`           | Boolean | No                            | Defaults to `false`. Prefer the dedicated highlights endpoint for bulk control     |
| `icon_type`                | String  | No                            | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`                                       |
| `icon_value`               | String  | No                            | Max 2000 chars                                                                     |
| `icon_meta`                | Object  | No                            | Any JSON object                                                                    |
| `resort_facility_price`    | Object  | Yes (if price type is `PAID`) | Required when price type is `PAID`; must be omitted otherwise                      |
| `locales`                  | Array   | No                            | See locale fields below                                                            |

**Price fields (`resort_facility_price`):**

| Field           | Type       | Required | Validation           |
|-----------------|------------|----------|----------------------|
| `price_unit_id` | Long       | Yes      | Not null, must exist |
| `currency_id`   | Long       | Yes      | Not null, must exist |
| `amount`        | BigDecimal | Yes      | Not null, >= 0.00    |
| `notes`         | String     | No       | —                    |
| `sort_order`    | Integer    | Yes      | Not null, >= 0       |

**Locale fields (`locales[]`):**

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 31
}
```

---

## Get Resort Facility

`GET /api/v1/resorts/{resort-id}/facilities/{id}`

Returns a single resort facility with all its locale translations and, when the price type is `PAID`, its active price
entries. Returns `404` if the facility does not exist or does not belong to the specified resort.

### Path Parameters

| Parameter   | Type | Description               |
|-------------|------|---------------------------|
| `resort-id` | Long | ID of the resort          |
| `id`        | Long | ID of the resort facility |

### Response `200 OK`

Null-valued optional fields (`platform_facility`, `icon_type`, `icon_value`, `icon_meta`) are omitted from the
response. `prices` is only present when `facility_price_type.code` is `PAID`.

**PAID facility (linked to platform facility):**

```json
{
  "data": {
    "id": 31,
    "resort_id": 1,
    "resort_facility_group": {
      "id": 12,
      "resort_id": 1,
      "facility_group_id": 3,
      "sort_order": 1,
      "locales": [
        {
          "id": 20,
          "locale_id": 1,
          "name": "Pools & Water Features",
          "sort_order": 1
        }
      ]
    },
    "platform_facility": {
      "id": 7,
      "facility_group_id": 3,
      "code": "OUTDOOR_POOL",
      "sort_order": 1,
      "locales": [
        {
          "id": 10,
          "locale_id": 1,
          "name": "Outdoor Pool",
          "sort_order": 1
        }
      ]
    },
    "facility_price_type": {
      "id": 3,
      "code": "PAID",
      "sort_order": 2,
      "locales": [
        {
          "id": 5,
          "locale_id": 1,
          "name": "Paid",
          "sort_order": 1
        }
      ]
    },
    "sort_order": 1,
    "is_highlighted": true,
    "icon_type": "LUCIDE",
    "icon_value": "Waves",
    "icon_meta": {
      "size": 24,
      "color": "#3b82f6"
    },
    "locales": [
      {
        "id": 55,
        "locale_id": 1,
        "name": "Outdoor Pool",
        "description": "Heated outdoor pool open year-round.",
        "sort_order": 1
      },
      {
        "id": 56,
        "locale_id": 2,
        "name": "আউটডোর পুল",
        "description": "সারা বছর খোলা উত্তপ্ত আউটডোর পুল।",
        "sort_order": 2
      }
    ],
    "prices": [
      {
        "id": 11,
        "resort_facility_id": 31,
        "price_unit": {
          "id": 2,
          "code": "PER_PERSON"
        },
        "currency": {
          "id": 1,
          "code": "USD"
        },
        "amount": 25.00,
        "notes": "Per person per session",
        "sort_order": 1
      }
    ]
  }
}
```

**FREE custom facility (no platform facility, no prices):**

```json
{
  "data": {
    "id": 34,
    "resort_id": 1,
    "resort_facility_group": {
      "id": 12,
      "resort_id": 1,
      "facility_group_id": 3,
      "sort_order": 1,
      "locales": [
        {
          "id": 20,
          "locale_id": 1,
          "name": "Pools & Water Features",
          "sort_order": 1
        }
      ]
    },
    "facility_price_type": {
      "id": 1,
      "code": "FREE",
      "sort_order": 1,
      "locales": [
        {
          "id": 3,
          "locale_id": 1,
          "name": "Free",
          "sort_order": 1
        }
      ]
    },
    "sort_order": 4,
    "is_highlighted": false,
    "icon_type": "IMAGE",
    "icon_value": "https://cdn.example.com/icons/private-beach.png",
    "locales": [
      {
        "id": 60,
        "locale_id": 1,
        "name": "Private Beach Access",
        "description": "",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List Resort Facilities

`GET /api/v1/resorts/{resort-id}/facilities`

Returns a paginated list of active (non-deleted) resort facilities. Each item includes all locale translations and,
when the price type is `PAID`, its active prices. Use `resortFacilityGroupId` to scope results to a specific group;
without it, all active facilities are returned regardless of group.

> **Note:** The `{resort-id}` path parameter is required by the URL structure but is not used to scope the list query.
> To retrieve facilities for a specific group (and by extension a specific resort), always provide
`resortFacilityGroupId`.

### Path Parameters

| Parameter   | Type | Description                                                 |
|-------------|------|-------------------------------------------------------------|
| `resort-id` | Long | ID of the resort (required in path; not used for filtering) |

### Query Parameters

| Parameter               | Type   | Default | Constraints                    | Description                                                                  |
|-------------------------|--------|---------|--------------------------------|------------------------------------------------------------------------------|
| `resortFacilityGroupId` | Long   | —       | optional                       | Scope results to this resort facility group. Recommended for all list calls. |
| `page`                  | int    | `0`     | >= 0                           | Zero-based page index                                                        |
| `size`                  | int    | `10`    | 1 – 50                         | Number of items per page                                                     |
| `sort_by`               | String | `id`    | `id`, `sortOrder`, `createdAt` | Field to sort by                                                             |
| `sort_dir`              | String | `ASC`   | `ASC`, `DESC`                  | Sort direction                                                               |

### Response `200 OK`

Null-valued optional fields are omitted per item. Example shows results filtered by `resortFacilityGroupId=12`.

```json
{
  "data": [
    {
      "id": 31,
      "resort_id": 1,
      "resort_facility_group": {
        "id": 12,
        "resort_id": 1,
        "facility_group_id": 3,
        "sort_order": 1,
        "locales": [
          {
            "id": 20,
            "locale_id": 1,
            "name": "Pools & Water Features",
            "sort_order": 1
          }
        ]
      },
      "platform_facility": {
        "id": 7,
        "facility_group_id": 3,
        "code": "OUTDOOR_POOL",
        "sort_order": 1,
        "locales": []
      },
      "facility_price_type": {
        "id": 3,
        "code": "PAID",
        "sort_order": 2,
        "locales": []
      },
      "sort_order": 1,
      "is_highlighted": true,
      "icon_type": "LUCIDE",
      "icon_value": "Waves",
      "icon_meta": {
        "size": 24,
        "color": "#3b82f6"
      },
      "locales": [
        {
          "id": 55,
          "locale_id": 1,
          "name": "Outdoor Pool",
          "description": "Heated outdoor pool open year-round.",
          "sort_order": 1
        }
      ],
      "prices": [
        {
          "id": 11,
          "resort_facility_id": 31,
          "price_unit": {
            "id": 2,
            "code": "PER_PERSON"
          },
          "currency": {
            "id": 1,
            "code": "USD"
          },
          "amount": 25.00,
          "notes": "Per person per session",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 34,
      "resort_id": 1,
      "resort_facility_group": {
        "id": 12,
        "resort_id": 1,
        "facility_group_id": 3,
        "sort_order": 1,
        "locales": []
      },
      "facility_price_type": {
        "id": 1,
        "code": "FREE",
        "sort_order": 1,
        "locales": []
      },
      "sort_order": 4,
      "is_highlighted": false,
      "icon_type": "IMAGE",
      "icon_value": "https://cdn.example.com/icons/private-beach.png",
      "locales": [
        {
          "id": 60,
          "locale_id": 1,
          "name": "Private Beach Access",
          "description": "",
          "sort_order": 1
        }
      ]
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

## Update Resort Facility

`PUT /api/v1/resorts/{resort-id}/facilities/{id}`

Updates the resort facility's presentation fields. Locale translations are managed separately via the locale
sub-resource endpoints. Prices are managed separately via the resort facility prices endpoints.
`platform_facility` can be changed or cleared (set `facility_id` to `null`) to convert between a linked and custom
facility. `resort_facility_group_id` is set at creation time and cannot be changed.

### Path Parameters

| Parameter   | Type | Description               |
|-------------|------|---------------------------|
| `resort-id` | Long | ID of the resort          |
| `id`        | Long | ID of the resort facility |

### Request Body

```json
{
  "facility_id": 8,
  "facility_price_type_id": 3,
  "sort_order": 2,
  "is_highlighted": true,
  "icon_type": "LUCIDE",
  "icon_value": "Droplets",
  "icon_meta": {
    "size": 20,
    "color": "#0ea5e9"
  }
}
```

**To clear the linked facility and icon override:**

```json
{
  "facility_id": null,
  "facility_price_type_id": 1,
  "sort_order": 2,
  "is_highlighted": false,
  "icon_type": null,
  "icon_value": null,
  "icon_meta": null
}
```

### Request Fields

| Field                    | Type    | Required | Validation                                                                     |
|--------------------------|---------|----------|--------------------------------------------------------------------------------|
| `facility_id`            | Long    | No       | Must reference an existing active facility                                     |
| `facility_price_type_id` | Long    | Yes      | Not null, must reference an existing active facility price type                |
| `sort_order`             | Integer | Yes      | Not null, >= 0                                                                 |
| `is_highlighted`         | Boolean | No       | Defaults to `false`. Prefer the dedicated highlights endpoint for bulk control |
| `icon_type`              | String  | No       | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`                                   |
| `icon_value`             | String  | No       | Max 2000 chars                                                                 |
| `icon_meta`              | Object  | No       | Any JSON object                                                                |

### Response `200 OK`

```json
{
  "success": true,
  "id": 31
}
```

---

## Delete Resort Facility

`DELETE /api/v1/resorts/{resort-id}/facilities/{id}`

Soft-deletes the resort facility and its locale translations. The records are not removed from the database but will no
longer appear in any response.

### Path Parameters

| Parameter   | Type | Description               |
|-------------|------|---------------------------|
| `resort-id` | Long | ID of the resort          |
| `id`        | Long | ID of the resort facility |

### Response `200 OK`

```json
{
  "success": true,
  "id": 31
}
```

---

## Set Resort Facility Highlights

`PUT /api/v1/resorts/{resort-id}/facilities/highlights`

Replaces the full set of highlighted facilities for a resort in a single operation. The owner provides 4 to 9 resort
facility IDs to highlight; all other active facilities for that resort are automatically unhighlighted. This is the
preferred way to manage `is_highlighted` — it guarantees the min/max constraint is always met and avoids partial state.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Request Body

```json
{
  "facility_ids": [
    31,
    32,
    33,
    34
  ]
}
```

### Request Fields

| Field          | Type          | Required | Validation                                                         |
|----------------|---------------|----------|--------------------------------------------------------------------|
| `facility_ids` | Array\<Long\> | Yes      | Not null, between 4 and 9 IDs, all must belong to the given resort |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

> The `id` in the response is the resort ID.

### Behavior

- All active facilities for the resort that are **not** in `facility_ids` will have `is_highlighted` set to `false`.
- All facilities **in** `facility_ids` will have `is_highlighted` set to `true`.
- If any ID in `facility_ids` does not belong to the given resort, the entire request is rejected with
  `400 VALIDATION_ERROR`.

---

## Resort Facility Locales

Locale endpoints manage per-locale translations (name, description) for a resort facility. The
`{resort-facility-id}` path parameter must reference an existing, active resort facility belonging to the specified
resort.

---

### Create Resort Facility Locale

`POST /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales`

Adds a new locale translation to an existing resort facility. Each `locale_id` may only be used once per resort
facility.

#### Path Parameters

| Parameter            | Type | Description               |
|----------------------|------|---------------------------|
| `resort-id`          | Long | ID of the resort          |
| `resort-facility-id` | Long | ID of the resort facility |

#### Request Body

```json
{
  "locale_id": 3,
  "name": "Außenpool",
  "description": "Beheizter Außenpool, ganzjährig geöffnet.",
  "sort_order": 3
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 61
}
```

---

### Update Resort Facility Locale

`PUT /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales/{id}`

Updates an existing locale translation. `locale_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter            | Type | Description                      |
|----------------------|------|----------------------------------|
| `resort-id`          | Long | ID of the resort                 |
| `resort-facility-id` | Long | ID of the resort facility        |
| `id`                 | Long | ID of the resort facility locale |

#### Request Body

```json
{
  "name": "Außenpool",
  "description": "Aktualisierte Beschreibung.",
  "sort_order": 3
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 61
}
```

---

### Delete Resort Facility Locale

`DELETE /api/v1/resorts/{resort-id}/facilities/{resort-facility-id}/locales/{id}`

Soft-deletes a locale translation. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter            | Type | Description                      |
|----------------------|------|----------------------------------|
| `resort-id`          | Long | ID of the resort                 |
| `resort-facility-id` | Long | ID of the resort facility        |
| `id`                 | Long | ID of the resort facility locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 61
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
  "message": "ResortFacility not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                                                                                      |
|-------------|----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations (e.g. `sort_order` missing, `name` blank)                                                                                                                                                                 |
| 400         | `INVALID_ARGUMENT`         | `resort_facility_price` provided but price type is not `PAID`                                                                                                                                                                                              |
| 400         | `INVALID_ARGUMENT`         | `resort_facility_price` missing but price type is `PAID`                                                                                                                                                                                                   |
| 400         | `VALIDATION_ERROR`         | `facility_ids` has fewer than 4 or more than 9 entries on the highlights endpoint                                                                                                                                                                          |
| 400         | `VALIDATION_ERROR`         | One or more IDs in `facility_ids` do not belong to the given resort                                                                                                                                                                                        |
| 400         | `INVALID_ARGUMENT`         | Invalid `sort_by` field, invalid `icon_type` enum value, or malformed request                                                                                                                                                                              |
| 404         | `ENTITY_NOT_FOUND`         | Resort, facility, facility price type, price unit, currency, locale, or locale translation not found; or resort facility group not found / does not belong to the specified resort; or resort facility not found / does not belong to the specified resort |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `locale_id` for the same resort facility                                                                                                                                                                                                         |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                                                                                                                                                                                                                    |
