# Resort Basic Info API

Base URL: `/api/v1/resorts/{resort-id}/basic-info`

Resort Basic Info holds the core identity and location data for a resort, including its establishment year,
geographic placement, optional logo, and localized content such as name, tagline, description, and address.

It is a **per-resort singleton** — the record is created automatically when the resort is created and always exists for
every active resort. It cannot be created or deleted independently; the resort owner can only view and update it.
Locale-specific content is managed via the locales sub-resource, and each basic info record may have multiple locale
entries — one per language.

---

## Endpoints

| Method | Path                                                  | Description                       |
|--------|-------------------------------------------------------|-----------------------------------|
| GET    | `/api/v1/resorts/{resort-id}/basic-info`              | Get resort basic info             |
| PUT    | `/api/v1/resorts/{resort-id}/basic-info`              | Update resort basic info          |
| POST   | `/api/v1/resorts/{resort-id}/basic-info/logo`         | Upload resort logo                |
| POST   | `/api/v1/resorts/{resort-id}/basic-info/locales`      | Create a resort basic info locale |
| PUT    | `/api/v1/resorts/{resort-id}/basic-info/locales/{id}` | Update a resort basic info locale |
| DELETE | `/api/v1/resorts/{resort-id}/basic-info/locales/{id}` | Delete a resort basic info locale |

---

## Data Model

### Resort Basic Info

| Field        | Type    | Required | Constraints                     | Description                                                                                               |
|--------------|---------|----------|---------------------------------|-----------------------------------------------------------------------------------------------------------|
| `id`         | Long    | —        | read-only                       | Auto-generated identifier                                                                                 |
| `code`       | String  | —        | read-only, max 50 chars, unique | Short alphanumeric code identifying the resort; set at resort creation time, never updatable              |
| `sort_order` | Integer | Yes      | not null, default `0`           | Controls display ordering of resorts in list views; lower values appear first                             |
| `estd`       | Short   | Yes      | not null                        | Year the resort was established (e.g. `1998`)                                                             |
| `country`    | Object  | —        | read-only                       | Country where the resort is located; see Country Object below                                             |
| `city`       | Object  | —        | read-only                       | City where the resort is located; see City Object below                                                   |
| `logo_url`   | String  | No       | max 500 chars                   | URL of the resort's logo image; typically set via the Upload Logo endpoint; omitted from response if null |
| `lat`        | Double  | No       | —                               | Latitude coordinate of the resort's location; omitted from response if null                               |
| `lon`        | Double  | No       | —                               | Longitude coordinate of the resort's location; omitted from response if null                              |
| `locales`    | Array   | —        | read-only                       | All locale translations associated with this resort basic info record                                     |

### Resort Basic Info Locale

| Field               | Type    | Required | Constraints              | Description                                                              |
|---------------------|---------|----------|--------------------------|--------------------------------------------------------------------------|
| `id`                | Long    | —        | read-only                | Auto-generated identifier                                                |
| `locale`            | Object  | —        | read-only                | Locale (language) for this translation; see Locale Object below          |
| `sort_order`        | Integer | Yes      | not null, default `0`    | Controls display ordering of locale entries                              |
| `name`              | String  | Yes      | not blank, max 255 chars | Localized name of the resort in this language                            |
| `tagline`           | String  | Yes      | not blank                | Localized short tagline or slogan for the resort                         |
| `short_description` | String  | No       | max 1024 chars           | Localized short description of the resort; omitted from response if null |
| `address`           | String  | No       | unlimited length         | Localized postal address of the resort; omitted from response if null    |

### Country Object

Embedded in the `country` field. Cities are not included.

| Field        | Type    | Description                              |
|--------------|---------|------------------------------------------|
| `id`         | Long    | Auto-generated identifier                |
| `code`       | String  | Short country code (e.g. `BD`)           |
| `iso_3_code` | String  | ISO 3166-1 alpha-3 code (e.g. `BGD`)     |
| `phone_code` | String  | International dialing code (e.g. `+880`) |
| `sort_order` | Integer | Display ordering                         |
| `locales`    | Array   | Locale translations for this country     |
| `cities`     | Array   | Always empty in this context             |

### City Object

Embedded in the `city` field. Country is not included.

| Field        | Type    | Description                       |
|--------------|---------|-----------------------------------|
| `id`         | Long    | Auto-generated identifier         |
| `code`       | String  | Short city code (e.g. `COX`)      |
| `sort_order` | Integer | Display ordering                  |
| `locales`    | Array   | Locale translations for this city |

### Locale Object

Embedded in each locale entry's `locale` field.

| Field        | Type    | Description                   |
|--------------|---------|-------------------------------|
| `id`         | Long    | Auto-generated identifier     |
| `code`       | String  | Locale code (e.g. `en`)       |
| `name`       | String  | Display name (e.g. `English`) |
| `sort_order` | Integer | Display ordering              |

---

## Get Resort Basic Info

`GET /api/v1/resorts/{resort-id}/basic-info`

Returns the full resort basic info record for the given resort, including all locale translations. This endpoint is
accessible to the resort owner.

### Path Parameters

| Parameter   | Type | Required | Description                              |
|-------------|------|----------|------------------------------------------|
| `resort-id` | Long | Yes      | ID of the resort whose basic info to get |

### Response `200 OK`

Optional fields (`logo_url`, `lat`, `lon`, locale `short_description`, locale `address`) are omitted from the response
when their value is null.

```json
{
  "resort_basic_info": {
    "id": 1,
    "code": "SUNRISE",
    "sort_order": 1,
    "estd": 1998,
    "country": {
      "id": 1,
      "code": "BD",
      "iso_3_code": "BGD",
      "phone_code": "+880",
      "sort_order": 1,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Bangladesh",
          "sort_order": 1
        }
      ],
      "cities": []
    },
    "city": {
      "id": 3,
      "code": "COX",
      "sort_order": 2,
      "locales": [
        {
          "id": 1,
          "locale_id": 1,
          "name": "Cox's Bazar",
          "sort_order": 1
        }
      ]
    },
    "logo_url": "https://cdn.example.com/logo.png",
    "lat": 21.4272,
    "lon": 92.0058,
    "locales": [
      {
        "id": 1,
        "locale": {
          "id": 1,
          "code": "en",
          "name": "English",
          "sort_order": 1
        },
        "sort_order": 1,
        "name": "Sunrise Beach Resort",
        "tagline": "Where the sea meets serenity",
        "short_description": "A luxury beachfront resort on the coast of Cox's Bazar.",
        "address": "Marine Drive Road, Cox's Bazar, Bangladesh"
      },
      {
        "id": 2,
        "locale": {
          "id": 2,
          "code": "bn",
          "name": "Bengali",
          "sort_order": 2
        },
        "sort_order": 2,
        "name": "সানরাইজ বিচ রিসোর্ট",
        "tagline": "যেখানে সমুদ্র মিলে শান্তিতে",
        "short_description": "কক্সবাজার উপকূলে একটি বিলাসবহুল সমুদ্রসৈকত রিসোর্ট।",
        "address": "মেরিন ড্রাইভ রোড, কক্সবাজার, বাংলাদেশ"
      }
    ]
  }
}
```

---

## Update Resort Basic Info

`PUT /api/v1/resorts/{resort-id}/basic-info`

Updates the mutable fields of the resort basic info record. The `code` field is immutable — it is set at resort
creation time and cannot be changed via this endpoint. Locale translations are managed separately via the locale
endpoints below. The resort owner is the only user permitted to update this record.

### Path Parameters

| Parameter   | Type | Required | Description                                 |
|-------------|------|----------|---------------------------------------------|
| `resort-id` | Long | Yes      | ID of the resort whose basic info to update |

### Request Fields

| Field        | Type    | Required | Validation                             | Description                                                                             |
|--------------|---------|----------|----------------------------------------|-----------------------------------------------------------------------------------------|
| `sort_order` | Integer | Yes      | Not null                               | Display order for this resort                                                           |
| `estd`       | Short   | Yes      | Not null                               | Year the resort was established                                                         |
| `country_id` | Long    | Yes      | Not null, must reference active record | ID of the country where the resort is located                                           |
| `city_id`    | Long    | Yes      | Not null, must reference active record | ID of the city where the resort is located                                              |
| `logo_url`   | String  | No       | Max 500 chars                          | Direct URL of the resort logo; use the Upload Logo endpoint instead for managed uploads |
| `lat`        | Double  | No       | —                                      | Latitude coordinate of the resort's location                                            |
| `lon`        | Double  | No       | —                                      | Longitude coordinate of the resort's location                                           |

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

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Upload Resort Logo

`POST /api/v1/resorts/{resort-id}/basic-info/logo`

Uploads a logo image file to the specified image hosting provider (Cloudinary or S3) and stores the resulting public
URL in the `logo_url` field of the resort basic info record. Uploading a new logo overwrites the previously stored
`logo_url`. This endpoint uses `multipart/form-data`.

### Path Parameters

| Parameter   | Type | Required | Description                           |
|-------------|------|----------|---------------------------------------|
| `resort-id` | Long | Yes      | ID of the resort to upload a logo for |

### Form Parameters

| Parameter   | Type          | Required | Description                                                       |
|-------------|---------------|----------|-------------------------------------------------------------------|
| `config_id` | Long          | Yes      | ID of the image hosting config to use; must belong to this resort |
| `file`      | MultipartFile | Yes      | The logo image file to upload (e.g. JPEG, PNG)                    |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Resort Basic Info Locales

Locale endpoints manage the per-language translations for a resort's basic info. Each basic info record may have
multiple locale entries — one per language. The `locale_id` uniquely identifies the language of each entry; only one
entry per locale is allowed per resort.

---

### Create Resort Basic Info Locale

`POST /api/v1/resorts/{resort-id}/basic-info/locales`

Adds a new locale translation to the resort basic info. The `locale_id` must reference an existing active locale
and uniquely identifies the language of this translation. The `locale_id` cannot be changed after the entry is
created.

#### Path Parameters

| Parameter   | Type | Required | Description                                 |
|-------------|------|----------|---------------------------------------------|
| `resort-id` | Long | Yes      | ID of the resort to add the locale entry to |

#### Request Fields

| Field               | Type    | Required | Validation                             | Description                                      |
|---------------------|---------|----------|----------------------------------------|--------------------------------------------------|
| `locale_id`         | Long    | Yes      | Not null, must reference active locale | ID of the locale (language) for this translation |
| `sort_order`        | Integer | Yes      | Not null                               | Display ordering for this locale entry           |
| `name`              | String  | Yes      | Not blank, max 255 chars               | Localized name of the resort in this language    |
| `tagline`           | String  | Yes      | Not blank                              | Localized tagline or slogan for the resort       |
| `short_description` | String  | No       | Max 1024 chars                         | Localized short description of the resort        |
| `address`           | String  | No       | Unlimited length                       | Localized postal address of the resort           |

#### Request Body

```json
{
  "locale_id": 3,
  "sort_order": 3,
  "name": "Sunrise Beach Resort",
  "tagline": "Where the sea meets serenity",
  "short_description": "A luxury beachfront resort on the coast of Cox's Bazar.",
  "address": "Marine Drive Road, Cox's Bazar, Bangladesh"
}
```

#### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

### Update Resort Basic Info Locale

`PUT /api/v1/resorts/{resort-id}/basic-info/locales/{id}`

Updates an existing locale translation. The `locale_id` (language) cannot be changed — to change the language,
delete this entry and create a new one. All other fields are fully updatable.

#### Path Parameters

| Parameter   | Type | Required | Description                      |
|-------------|------|----------|----------------------------------|
| `resort-id` | Long | Yes      | ID of the resort                 |
| `id`        | Long | Yes      | ID of the locale entry to update |

#### Request Fields

| Field               | Type    | Required | Validation               | Description                                   |
|---------------------|---------|----------|--------------------------|-----------------------------------------------|
| `sort_order`        | Integer | Yes      | Not null                 | Display ordering for this locale entry        |
| `name`              | String  | Yes      | Not blank, max 255 chars | Localized name of the resort in this language |
| `tagline`           | String  | Yes      | Not blank                | Localized tagline or slogan for the resort    |
| `short_description` | String  | No       | Max 1024 chars           | Localized short description of the resort     |
| `address`           | String  | No       | Unlimited length         | Localized postal address of the resort        |

#### Request Body

```json
{
  "sort_order": 1,
  "name": "Sunrise Beach Resort",
  "tagline": "Where the sea meets serenity",
  "short_description": "Updated description of the resort.",
  "address": "Marine Drive Road, Cox's Bazar, Bangladesh"
}
```

#### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

### Delete Resort Basic Info Locale

`DELETE /api/v1/resorts/{resort-id}/basic-info/locales/{id}`

Soft-deletes a locale translation. Sets `is_active` to `false` and `is_deleted` to `true` on the record. The record
is not physically removed from the database. To add back support for the same locale, create a new entry with the same
`locale_id`.

#### Path Parameters

| Parameter   | Type | Required | Description                      |
|-------------|------|----------|----------------------------------|
| `resort-id` | Long | Yes      | ID of the resort                 |
| `id`        | Long | Yes      | ID of the locale entry to delete |

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
  "message": "ResortBasicInfo not found for resort with id: 5"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                                |
|-------------|----------------------------|----------------------------------------------------------------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields, blank values where not allowed, or values exceeding max length constraints                  |
| 404         | `ENTITY_NOT_FOUND`         | Resort not found, resort basic info not found, image hosting config not found, or locale not found / already deleted |
| 409         | `DATA_INTEGRITY_VIOLATION` | A locale entry for the given `locale_id` already exists on this resort basic info record                             |
| 500         | `INTERNAL_SERVER_ERROR`    | Upload to the image hosting provider (Cloudinary or S3) failed                                                       |
