# Resort Facility Groups API

Base URL: `/api/v1/resorts/{resort-id}/facility-groups`

A resort facility group links a platform-level facility group to a specific resort, allowing per-resort presentation
overrides (sort order, icon, localized names). It can also be a fully custom group with no platform-level counterpart
when `facility_group_id` is omitted. Locale-specific names and descriptions are embedded in every response via the
`locales` array and are managed separately via the locale sub-resource endpoints. All records support soft-delete —
deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                                                                  | Description                    |
|--------|---------------------------------------------------------------------------------------|--------------------------------|
| POST   | `/api/v1/resorts/{resort-id}/facility-groups`                                         | Create a resort facility group |
| GET    | `/api/v1/resorts/{resort-id}/facility-groups`                                         | List resort facility groups    |
| GET    | `/api/v1/resorts/{resort-id}/facility-groups/{id}`                                    | Get a resort facility group    |
| PUT    | `/api/v1/resorts/{resort-id}/facility-groups/{id}`                                    | Update a resort facility group |
| DELETE | `/api/v1/resorts/{resort-id}/facility-groups/{id}`                                    | Delete a resort facility group |
| POST   | `/api/v1/resorts/{resort-id}/facility-groups/{resort-facility-group-id}/locales`      | Create a locale translation    |
| PUT    | `/api/v1/resorts/{resort-id}/facility-groups/{resort-facility-group-id}/locales/{id}` | Update a locale translation    |
| DELETE | `/api/v1/resorts/{resort-id}/facility-groups/{resort-facility-group-id}/locales/{id}` | Delete a locale translation    |

---

## Data Model

### Resort Facility Group

| Field               | Type    | Required | Constraints                                                  | Description                                                                                                                                                                                     |
|---------------------|---------|----------|--------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`                | Long    | —        | read-only                                                    | Auto-generated identifier                                                                                                                                                                       |
| `resort_id`         | Long    | —        | read-only                                                    | ID of the parent resort                                                                                                                                                                         |
| `facility_group_id` | Long    | No       | must exist if given; unique per resort (active records only) | ID of the platform facility group to link. Omit for a fully custom resort-defined group. Only one active, non-deleted record may link the same `facility_group_id` to a given resort at a time. |
| `sort_order`        | Integer | Yes      | not null, >= 0                                               | Display order of this group within the resort                                                                                                                                                   |
| `icon_type`         | String  | No       | enum                                                         | Icon type override. Allowed values: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`. Omitted from response if null.                                                                                        |
| `icon_value`        | String  | No       | max 2000 chars                                               | Icon value override — icon name, URL, or SVG markup. Omitted from response if null.                                                                                                             |
| `icon_meta`         | Object  | No       | any JSON object                                              | Optional rendering hints (e.g. `size`, `color`, `alt`). Omitted from response if null.                                                                                                          |
| `locales`           | Array   | —        | read-only here                                               | All locale translations for this resort facility group                                                                                                                                          |

> When `facility_group_id` is provided, `icon_type`, `icon_value`, and `icon_meta` act as overrides for the platform
> group's icon. When omitted (custom group), these fields become the primary icon source.

### Resort Facility Group Locale

| Field         | Type    | Required | Constraints        | Description                                     |
|---------------|---------|----------|--------------------|-------------------------------------------------|
| `id`          | Long    | —        | read-only          | Auto-generated identifier                       |
| `locale_id`   | Long    | Yes      | must exist         | ID of an existing active locale. Not updatable. |
| `name`        | String  | Yes      | not blank, max 255 | Localized display name for this facility group  |
| `description` | String  | No       | unlimited          | Localized description; defaults to empty string |
| `sort_order`  | Integer | Yes      | not null           | Display order for this locale entry             |

---

## Create Resort Facility Group

`POST /api/v1/resorts/{resort-id}/facility-groups`

Creates a resort facility group, optionally linking it to a platform facility group and optionally seeding locale
translations in the same request. All provided `locale_id` values must reference existing, active locales.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Request Body

**Linked group (overriding a platform facility group):**

```json
{
  "facility_group_id": 3,
  "sort_order": 1,
  "icon_type": "LUCIDE",
  "icon_value": "UtensilsCrossed",
  "icon_meta": {
    "size": 24,
    "color": "#f59e0b"
  },
  "locales": [
    {
      "locale_id": 1,
      "name": "Dining & Beverages",
      "description": "All dining outlets available at this resort.",
      "sort_order": 1
    },
    {
      "locale_id": 2,
      "name": "ডাইনিং ও পানীয়",
      "description": "এই রিসোর্টে উপলব্ধ সকল ডাইনিং আউটলেট।",
      "sort_order": 2
    }
  ]
}
```

**Custom group (no platform counterpart):**

```json
{
  "sort_order": 5,
  "icon_type": "IMAGE",
  "icon_value": "https://cdn.example.com/icons/special.png",
  "locales": [
    {
      "locale_id": 1,
      "name": "Special Services",
      "sort_order": 1
    }
  ]
}
```

### Request Fields

| Field               | Type    | Required | Validation                                                                                                          |
|---------------------|---------|----------|---------------------------------------------------------------------------------------------------------------------|
| `facility_group_id` | Long    | No       | Must reference an existing active facility group; must not already be assigned to this resort (active records only) |
| `sort_order`        | Integer | Yes      | Not null, >= 0                                                                                                      |
| `icon_type`         | String  | No       | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`                                                                        |
| `icon_value`        | String  | No       | Max 2000 chars                                                                                                      |
| `icon_meta`         | Object  | No       | Any JSON object                                                                                                     |
| `locales`           | Array   | No       | See locale fields below                                                                                             |

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
  "id": 12
}
```

---

## Get Resort Facility Group

`GET /api/v1/resorts/{resort-id}/facility-groups/{id}`

Returns a single resort facility group with all its locale translations.

### Path Parameters

| Parameter   | Type | Description                     |
|-------------|------|---------------------------------|
| `resort-id` | Long | ID of the resort                |
| `id`        | Long | ID of the resort facility group |

### Response `200 OK`

Null-valued optional fields (`facility_group_id`, `icon_type`, `icon_value`, `icon_meta`) are omitted from the response.

```json
{
  "data": {
    "id": 12,
    "resort_id": 1,
    "facility_group_id": 3,
    "sort_order": 1,
    "icon_type": "LUCIDE",
    "icon_value": "UtensilsCrossed",
    "icon_meta": {
      "size": 24,
      "color": "#f59e0b"
    },
    "locales": [
      {
        "id": 21,
        "locale_id": 1,
        "name": "Dining & Beverages",
        "description": "All dining outlets available at this resort.",
        "sort_order": 1
      },
      {
        "id": 22,
        "locale_id": 2,
        "name": "ডাইনিং ও পানীয়",
        "description": "এই রিসোর্টে উপলব্ধ সকল ডাইনিং আউটলেট।",
        "sort_order": 2
      }
    ]
  }
}
```

**Custom group (no linked platform group):**

```json
{
  "data": {
    "id": 15,
    "resort_id": 1,
    "sort_order": 5,
    "icon_type": "IMAGE",
    "icon_value": "https://cdn.example.com/icons/special.png",
    "locales": [
      {
        "id": 27,
        "locale_id": 1,
        "name": "Special Services",
        "description": "",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List Resort Facility Groups

`GET /api/v1/resorts/{resort-id}/facility-groups`

Returns a paginated list of active (non-deleted) facility groups for the given resort. Each item includes all locale
translations. Results are scoped exclusively to the specified resort.

### Path Parameters

| Parameter   | Type | Description      |
|-------------|------|------------------|
| `resort-id` | Long | ID of the resort |

### Query Parameters

| Parameter  | Type   | Default | Constraints                    | Description              |
|------------|--------|---------|--------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                           | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                         | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `sortOrder`, `createdAt` | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                  | Sort direction           |

### Response `200 OK`

Null-valued optional fields are omitted per item.

```json
{
  "data": [
    {
      "id": 12,
      "resort_id": 1,
      "facility_group_id": 3,
      "sort_order": 1,
      "icon_type": "LUCIDE",
      "icon_value": "UtensilsCrossed",
      "icon_meta": {
        "size": 24,
        "color": "#f59e0b"
      },
      "locales": [
        {
          "id": 21,
          "locale_id": 1,
          "name": "Dining & Beverages",
          "description": "All dining outlets available at this resort.",
          "sort_order": 1
        },
        {
          "id": 22,
          "locale_id": 2,
          "name": "ডাইনিং ও পানীয়",
          "description": "এই রিসোর্টে উপলব্ধ সকল ডাইনিং আউটলেট।",
          "sort_order": 2
        }
      ]
    },
    {
      "id": 13,
      "resort_id": 1,
      "facility_group_id": 5,
      "sort_order": 2,
      "locales": [
        {
          "id": 23,
          "locale_id": 1,
          "name": "Wellness & Spa",
          "description": "Relaxation and spa services.",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 15,
      "resort_id": 1,
      "sort_order": 5,
      "icon_type": "IMAGE",
      "icon_value": "https://cdn.example.com/icons/special.png",
      "locales": [
        {
          "id": 27,
          "locale_id": 1,
          "name": "Special Services",
          "description": "",
          "sort_order": 1
        }
      ]
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 3,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

---

## Update Resort Facility Group

`PUT /api/v1/resorts/{resort-id}/facility-groups/{id}`

Updates the resort facility group's presentation fields. Locale translations are managed separately via the locale
sub-resource endpoints.

### Path Parameters

| Parameter   | Type | Description                     |
|-------------|------|---------------------------------|
| `resort-id` | Long | ID of the resort                |
| `id`        | Long | ID of the resort facility group |

### Request Body

```json
{
  "facility_group_id": 4,
  "sort_order": 2,
  "icon_type": "LUCIDE",
  "icon_value": "ChefHat",
  "icon_meta": {
    "size": 20,
    "color": "#ef4444"
  }
}
```

**To clear the icon override**, pass `null` for the icon fields:

```json
{
  "facility_group_id": 3,
  "sort_order": 1,
  "icon_type": null,
  "icon_value": null,
  "icon_meta": null
}
```

### Request Fields

| Field               | Type    | Required | Validation                                                                                                          |
|---------------------|---------|----------|---------------------------------------------------------------------------------------------------------------------|
| `facility_group_id` | Long    | No       | Must reference an existing active facility group; must not already be assigned to this resort (active records only) |
| `sort_order`        | Integer | Yes      | Not null, >= 0                                                                                                      |
| `icon_type`         | String  | No       | One of: `LUCIDE`, `IMAGE`, `SVG`, `EXTERNAL`                                                                        |
| `icon_value`        | String  | No       | Max 2000 chars                                                                                                      |
| `icon_meta`         | Object  | No       | Any JSON object                                                                                                     |

### Response `200 OK`

```json
{
  "success": true,
  "id": 12
}
```

---

## Delete Resort Facility Group

`DELETE /api/v1/resorts/{resort-id}/facility-groups/{id}`

Soft-deletes the resort facility group and its locale translations. The records are not removed from the database but
will no longer appear in any response.

### Path Parameters

| Parameter   | Type | Description                     |
|-------------|------|---------------------------------|
| `resort-id` | Long | ID of the resort                |
| `id`        | Long | ID of the resort facility group |

### Response `200 OK`

```json
{
  "success": true,
  "id": 12
}
```

---

## Resort Facility Group Locales

Locale endpoints manage per-locale translations (name, description) for a resort facility group. The
`{resort-facility-group-id}` path parameter must reference an existing, active resort facility group belonging to the
specified resort.

---

### Create Resort Facility Group Locale

`POST /api/v1/resorts/{resort-id}/facility-groups/{resort-facility-group-id}/locales`

Adds a new locale translation to an existing resort facility group. Each `locale_id` may only be used once per resort
facility group.

#### Path Parameters

| Parameter                  | Type | Description                     |
|----------------------------|------|---------------------------------|
| `resort-id`                | Long | ID of the resort                |
| `resort-facility-group-id` | Long | ID of the resort facility group |

#### Request Body

```json
{
  "locale_id": 3,
  "name": "Speisen & Getränke",
  "description": "Alle Speise- und Getränkeangebote dieses Resorts.",
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
  "id": 28
}
```

---

### Update Resort Facility Group Locale

`PUT /api/v1/resorts/{resort-id}/facility-groups/{resort-facility-group-id}/locales/{id}`

Updates an existing locale translation. `locale_id` is set at creation time and cannot be changed.

#### Path Parameters

| Parameter                  | Type | Description                            |
|----------------------------|------|----------------------------------------|
| `resort-id`                | Long | ID of the resort                       |
| `resort-facility-group-id` | Long | ID of the resort facility group        |
| `id`                       | Long | ID of the resort facility group locale |

#### Request Body

```json
{
  "name": "Speisen & Getränke",
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
  "id": 28
}
```

---

### Delete Resort Facility Group Locale

`DELETE /api/v1/resorts/{resort-id}/facility-groups/{resort-facility-group-id}/locales/{id}`

Soft-deletes a locale translation. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter                  | Type | Description                            |
|----------------------------|------|----------------------------------------|
| `resort-id`                | Long | ID of the resort                       |
| `resort-facility-group-id` | Long | ID of the resort facility group        |
| `id`                       | Long | ID of the resort facility group locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 28
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
  "message": "ResortFacilityGroup not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                                                                                   |
|-------------|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 400         | `VALIDATION_ERROR`         | Missing required fields or constraint violations (e.g. `sort_order` missing, `name` blank)                                                                                              |
| 400         | `INVALID_ARGUMENT`         | Invalid `sort_by` field, invalid `icon_type` enum value, malformed request, or `facility_group_id` is already assigned to this resort (message: "This facility group is already assigned to the resort.") |
| 404         | `ENTITY_NOT_FOUND`         | Resort, facility group, resort facility group, locale, or locale translation not found                                                                                                                    |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate `locale_id` for the same resort facility group                                                                                                                                                  |
| 500         | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                                                                                                                                                 |
