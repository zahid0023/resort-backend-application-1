# Resort Basic Info API

Base URL: `/api/v1/resort-basic-info`

Resort Basic Info holds the core identity and location data for the resort. It is a **singleton resource** — only one
record can ever exist. Locale-specific content (name, tagline, description, address) is stored in the locales
sub-resource. All locale records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                    | Description                      |
|--------|-----------------------------------------|----------------------------------|
| POST   | `/api/v1/resort-basic-info`             | Create resort basic info         |
| GET    | `/api/v1/resort-basic-info`             | Get resort basic info            |
| PUT    | `/api/v1/resort-basic-info`             | Update resort basic info         |
| POST   | `/api/v1/resort-basic-info/locales`     | Create a resort basic info locale |
| PUT    | `/api/v1/resort-basic-info/locales/{id}` | Update a resort basic info locale |
| DELETE | `/api/v1/resort-basic-info/locales/{id}` | Delete a resort basic info locale |

---

## Data Model

### Resort Basic Info

| Field        | Type    | Required | Constraints               | Description                                                   |
|--------------|---------|----------|---------------------------|---------------------------------------------------------------|
| `id`         | Long    | —        | read-only, always `1`     | Fixed singleton identifier                                    |
| `code`       | String  | Yes      | max 50 chars, unique      | Short code identifying the resort; not updatable after create |
| `sort_order` | Integer | Yes      | not null, default `0`     | Display order                                                 |
| `estd`       | Short   | Yes      | not null                  | Year the resort was established                               |
| `country_id` | Long    | Yes      | must exist                | ID of the country where the resort is located                 |
| `city_id`    | Long    | Yes      | must exist                | ID of the city where the resort is located                    |
| `logo_url`   | String  | No       | max 500 chars             | URL of the resort logo; omitted if null                       |
| `lat`        | Double  | No       | —                         | Latitude coordinate; omitted if null                          |
| `lon`        | Double  | No       | —                         | Longitude coordinate; omitted if null                         |
| `locales`    | Array   | —        | read-only                 | All locale translations for this resort                       |

### Resort Basic Info Locale

| Field               | Type    | Required | Constraints               | Description                                         |
|---------------------|---------|----------|---------------------------|-----------------------------------------------------|
| `id`                | Long    | —        | read-only                 | Auto-generated identifier                           |
| `locale_id`         | Long    | Yes      | must exist, not updatable | ID of an existing active locale                     |
| `sort_order`        | Integer | Yes      | not null, default `0`     | Display order for this locale entry                 |
| `name`              | String  | Yes      | max 255 chars             | Localized name of the resort                        |
| `tagline`           | String  | Yes      | not blank                 | Localized tagline or slogan                         |
| `short_description` | String  | No       | max 1024 chars            | Localized short description; omitted if null        |
| `address`           | String  | No       | unlimited                 | Localized address text; omitted if null             |

---

## Create Resort Basic Info

`POST /api/v1/resort-basic-info`

Creates the singleton resort basic info record along with its locale translations. Can only be called once — subsequent
calls will fail with a constraint violation. All provided `locale_id` values must reference existing, active locales.

### Request Body

```json
{
  "code": "SUNRISE",
  "sort_order": 1,
  "estd": 1998,
  "country_id": 1,
  "city_id": 3,
  "logo_url": "https://cdn.example.com/logo.png",
  "lat": 21.4272,
  "lon": 92.0058,
  "locales": [
    {
      "locale_id": 1,
      "sort_order": 1,
      "name": "Sunrise Beach Resort",
      "tagline": "Where the sea meets serenity",
      "short_description": "A luxury beachfront resort on the coast of Cox's Bazar.",
      "address": "Marine Drive Road, Cox's Bazar, Bangladesh"
    },
    {
      "locale_id": 2,
      "sort_order": 2,
      "name": "সানরাইজ বিচ রিসোর্ট",
      "tagline": "যেখানে সমুদ্র মিলে শান্তিতে",
      "short_description": "কক্সবাজার উপকূলে একটি বিলাসবহুল সমুদ্রসৈকত রিসোর্ট।",
      "address": "মেরিন ড্রাইভ রোড, কক্সবাজার, বাংলাদেশ"
    }
  ]
}
```

### Request Fields

| Field        | Type    | Required | Validation               |
|--------------|---------|----------|--------------------------|
| `code`       | String  | Yes      | Not blank, max 50 chars  |
| `sort_order` | Integer | Yes      | Not null                 |
| `estd`       | Short   | Yes      | Not null                 |
| `country_id` | Long    | Yes      | Not null, must exist     |
| `city_id`    | Long    | Yes      | Not null, must exist     |
| `logo_url`   | String  | No       | max 500 chars            |
| `lat`        | Double  | No       | —                        |
| `lon`        | Double  | No       | —                        |
| `locales`    | Array   | No       | See locale fields below  |

**Locale fields (`locales[]`):**

| Field               | Type    | Required | Validation               |
|---------------------|---------|----------|--------------------------|
| `locale_id`         | Long    | Yes      | Not null, must exist     |
| `sort_order`        | Integer | Yes      | Not null                 |
| `name`              | String  | Yes      | Not blank, max 255 chars |
| `tagline`           | String  | Yes      | Not blank                |
| `short_description` | String  | No       | max 1024 chars           |
| `address`           | String  | No       | —                        |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Resort Basic Info

`GET /api/v1/resort-basic-info`

Returns the singleton resort basic info record with all locale translations.

### Response `200 OK`

Optional fields (`logo_url`, `lat`, `lon`, locale `short_description`, locale `address`) are omitted when not set.

```json
{
  "resort_basic_info": {
    "id": 1,
    "code": "SUNRISE",
    "sort_order": 1,
    "estd": 1998,
    "country_id": 1,
    "city_id": 3,
    "logo_url": "https://cdn.example.com/logo.png",
    "lat": 21.4272,
    "lon": 92.0058,
    "locales": [
      {
        "id": 1,
        "locale_id": 1,
        "sort_order": 1,
        "name": "Sunrise Beach Resort",
        "tagline": "Where the sea meets serenity",
        "short_description": "A luxury beachfront resort on the coast of Cox's Bazar.",
        "address": "Marine Drive Road, Cox's Bazar, Bangladesh"
      },
      {
        "id": 2,
        "locale_id": 2,
        "sort_order": 2,
        "name": "সানরাইজ বিচ রিসোর্ট",
        "tagline": "যেখানে সমুদ্র মিলে শান্তিতে"
      }
    ]
  }
}
```

---

## Update Resort Basic Info

`PUT /api/v1/resort-basic-info`

Updates the resort basic info record. The `code` field is set at creation time and cannot be changed. Locale
translations are managed via the locale endpoints below.

### Request Body

```json
{
  "sort_order": 1,
  "estd": 1998,
  "country_id": 1,
  "city_id": 3,
  "logo_url": "https://cdn.example.com/logo-v2.png",
  "lat": 21.4272,
  "lon": 92.0058
}
```

### Request Fields

| Field        | Type    | Required | Validation           |
|--------------|---------|----------|----------------------|
| `sort_order` | Integer | Yes      | Not null             |
| `estd`       | Short   | Yes      | Not null             |
| `country_id` | Long    | Yes      | Not null, must exist |
| `city_id`    | Long    | Yes      | Not null, must exist |
| `logo_url`   | String  | No       | max 500 chars        |
| `lat`        | Double  | No       | —                    |
| `lon`        | Double  | No       | —                    |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Resort Basic Info Locales

Locale endpoints manage per-locale translations. The resort basic info record must already exist before locales can be
created.

---

### Create Resort Basic Info Locale

`POST /api/v1/resort-basic-info/locales`

Adds a new locale translation. The `locale_id` must reference an existing, active locale and cannot be changed after
creation.

#### Request Body

```json
{
  "locale_id": 1,
  "sort_order": 1,
  "name": "Sunrise Beach Resort",
  "tagline": "Where the sea meets serenity",
  "short_description": "A luxury beachfront resort on the coast of Cox's Bazar.",
  "address": "Marine Drive Road, Cox's Bazar, Bangladesh"
}
```

#### Request Fields

| Field               | Type    | Required | Validation               |
|---------------------|---------|----------|--------------------------|
| `locale_id`         | Long    | Yes      | Not null, must exist     |
| `sort_order`        | Integer | Yes      | Not null                 |
| `name`              | String  | Yes      | Not blank, max 255 chars |
| `tagline`           | String  | Yes      | Not blank                |
| `short_description` | String  | No       | max 1024 chars           |
| `address`           | String  | No       | —                        |

#### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

### Update Resort Basic Info Locale

`PUT /api/v1/resort-basic-info/locales/{id}`

Updates an existing locale translation. The `locale_id` cannot be changed.

#### Path Parameters

| Parameter | Type | Description                      |
|-----------|------|----------------------------------|
| `id`      | Long | ID of the resort basic info locale |

#### Request Body

```json
{
  "sort_order": 1,
  "name": "Sunrise Beach Resort",
  "tagline": "Where the sea meets serenity",
  "short_description": "Updated description.",
  "address": "Marine Drive Road, Cox's Bazar, Bangladesh"
}
```

#### Request Fields

| Field               | Type    | Required | Validation               |
|---------------------|---------|----------|--------------------------|
| `sort_order`        | Integer | Yes      | Not null                 |
| `name`              | String  | Yes      | Not blank, max 255 chars |
| `tagline`           | String  | Yes      | Not blank                |
| `short_description` | String  | No       | max 1024 chars           |
| `address`           | String  | No       | —                        |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

### Delete Resort Basic Info Locale

`DELETE /api/v1/resort-basic-info/locales/{id}`

Soft-deletes a locale translation. The record is not removed from the database but will no longer appear in any
response.

#### Path Parameters

| Parameter | Type | Description                        |
|-----------|------|------------------------------------|
| `id`      | Long | ID of the resort basic info locale |

#### Response `200 OK`

```json
{
  "success": true,
  "id": 3
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
  "message": "ResortBasicInfo not found"
}
```

| HTTP Status | Error Code                 | Cause                                                                          |
|-------------|----------------------------|--------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or failed validation                                   |
| 404         | `ENTITY_NOT_FOUND`         | Resort basic info not yet created, or locale not found / already deleted       |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. creating resort basic info when one already exists, duplicate locale_id) |
