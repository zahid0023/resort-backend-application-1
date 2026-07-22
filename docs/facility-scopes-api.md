# Facility Scopes API

Base URL: `/api/v1/facility-scopes`

Facility scopes are pre-seeded master data that define where a facility can be applied (e.g. `RESORT`, `ROOM_CATEGORY`,
`ROOM`). They cannot be created or deleted — the full set is inserted by the database migration. Only `sort_order` is
updatable. Locale translations are managed via the locale sub-resource endpoints and are included in every `getById`
response via the `locales` array. Locale translations support soft-delete — deleted locales are hidden from all
responses.

---

## Endpoints

| Method | Path                                                       | Description                    |
|--------|------------------------------------------------------------|--------------------------------|
| GET    | `/api/v1/facility-scopes`                                  | List / search facility scopes  |
| GET    | `/api/v1/facility-scopes/{id}`                             | Get a facility scope           |
| PUT    | `/api/v1/facility-scopes/{id}`                             | Update a facility scope        |
| POST   | `/api/v1/facility-scopes/{facility-scope-id}/locales`      | Create a facility scope locale |
| PUT    | `/api/v1/facility-scopes/{facility-scope-id}/locales/{id}` | Update a facility scope locale |
| DELETE | `/api/v1/facility-scopes/{facility-scope-id}/locales/{id}` | Delete a facility scope locale |

---

## Data Model

### Facility Scope

| Field        | Type    | Required | Constraints             | Description                                            |
|--------------|---------|----------|-------------------------|--------------------------------------------------------|
| `id`         | Long    | —        | read-only               | Auto-generated identifier                              |
| `code`       | String  | —        | read-only, max 50 chars | Internal code (e.g. `RESORT`, `ROOM_CATEGORY`, `ROOM`) |
| `sort_order` | Integer | Yes      | >= 0                    | Display order                                          |
| `locales`    | Array   | —        | read-only               | All active locale translations for this scope          |

### Facility Scope Locale

| Field         | Type    | Required | Constraints              | Description                                    |
|---------------|---------|----------|--------------------------|------------------------------------------------|
| `id`          | Long    | —        | read-only                | Auto-generated identifier                      |
| `locale_id`   | Long    | Yes      | must exist               | ID of an existing active locale; not updatable |
| `name`        | String  | Yes      | not blank, max 100 chars | Localized name of the scope                    |
| `description` | String  | No       | —                        | Localized description; omitted if null         |
| `sort_order`  | Integer | Yes      | not null                 | Display order for this locale entry            |

---

## Get Facility Scope

`GET /api/v1/facility-scopes/{id}`

Returns a single facility scope with all its active locale translations.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility scope |

### Response `200 OK`

`description` is omitted from locale entries when not set.

```json
{
  "data": {
    "id": 1,
    "code": "RESORT",
    "sort_order": 1,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "name": "Resort",
        "sort_order": 1
      }
    ]
  }
}
```

---

## List / Search Facility Scopes

`GET /api/v1/facility-scopes`

Returns a paginated, filterable list of facility scopes. Each item includes all active locale translations. All filter
parameters are optional; omitting them returns all scopes. Filtering performs a case-insensitive partial match.

### Query Parameters

| Parameter  | Type   | Default | Constraints                            | Description                                |
|------------|--------|---------|----------------------------------------|--------------------------------------------|
| `code`     | String | —       | —                                      | Filter by code (partial, case-insensitive) |
| `page`     | int    | `0`     | >= 0                                   | Zero-based page index                      |
| `size`     | int    | `10`    | 1 – 50                                 | Number of items per page                   |
| `sort_by`  | String | `id`    | `id`, `code`, `sortOrder`, `createdAt` | Field to sort by                           |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                          | Sort direction                             |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "RESORT",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Resort",
          "sort_order": 1
        }
      ]
    },
    {
      "id": 2,
      "code": "ROOM_CATEGORY",
      "sort_order": 2,
      "locales": [
        {
          "id": 2,
          "locale_id": 1,
          "name": "Room Category",
          "sort_order": 2
        }
      ]
    },
    {
      "id": 3,
      "code": "ROOM",
      "sort_order": 3,
      "locales": [
        {
          "id": 3,
          "locale_id": 1,
          "name": "Room",
          "sort_order": 3
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

## Update Facility Scope

`PUT /api/v1/facility-scopes/{id}`

Updates `sort_order`. The `code` field is set by the database migration and cannot be changed. Locale translations are
managed via the facility scope locale endpoints.

### Path Parameters

| Parameter | Type | Description              |
|-----------|------|--------------------------|
| `id`      | Long | ID of the facility scope |

### Request Body

```json
{
  "sort_order": 1
}
```

### Request Fields

| Field        | Type    | Required | Validation     |
|--------------|---------|----------|----------------|
| `sort_order` | Integer | Yes      | Not null, >= 0 |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Facility Scope Locales

Facility scope locale endpoints manage per-locale translations for a facility scope. The `{facility-scope-id}` path
parameter must reference an existing facility scope.

---

### Create Facility Scope Locale

`POST /api/v1/facility-scopes/{facility-scope-id}/locales`

Adds a new locale translation to an existing facility scope.

#### Path Parameters

| Parameter           | Type | Description              |
|---------------------|------|--------------------------|
| `facility-scope-id` | Long | ID of the facility scope |

#### Request Body

```json
{
  "locale_id": 2,
  "name": "রিসোর্ট",
  "description": "রিসোর্টের সুযোগ-সুবিধা",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `locale_id`   | Long    | Yes      | Not null, must exist     |
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 4
}
```

---

### Update Facility Scope Locale

`PUT /api/v1/facility-scopes/{facility-scope-id}/locales/{id}`

Updates `name`, `description`, and `sort_order` for an existing locale translation. `locale_id` is set at creation time
and cannot be changed.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-scope-id` | Long | ID of the facility scope        |
| `id`                | Long | ID of the facility scope locale |

#### Request Body

```json
{
  "name": "রিসোর্ট",
  "description": "রিসোর্টের সুযোগ-সুবিধা",
  "sort_order": 1
}
```

#### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `name`        | String  | Yes      | Not blank, max 100 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 4
}
```

---

### Delete Facility Scope Locale

`DELETE /api/v1/facility-scopes/{facility-scope-id}/locales/{id}`

Soft-deletes a facility scope locale. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter           | Type | Description                     |
|---------------------|------|---------------------------------|
| `facility-scope-id` | Long | ID of the facility scope        |
| `id`                | Long | ID of the facility scope locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 4
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
  "message": "FacilityScope not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                  |
|-------------|----------------------------|------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field                          |
| 404         | `ENTITY_NOT_FOUND`         | Facility scope or locale not found, or locale already deleted          |
| 409         | `DATA_INTEGRITY_VIOLATION` | Duplicate locale for the same scope (unique constraint on `locale_id`) |
