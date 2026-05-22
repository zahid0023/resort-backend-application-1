CREATE TABLE IF NOT EXISTS cities
(
    id
    bigserial
    PRIMARY
    KEY,

    country_id
    bigint
    NOT
    NULL
    REFERENCES
    countries
(
    id
) ON DELETE RESTRICT,
    code varchar
(
    50
),
    sort_order int NOT NULL DEFAULT 0,

    created_by bigint NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint NOT NULL,
    updated_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version bigint NOT NULL DEFAULT 0,
    is_active boolean NOT NULL DEFAULT true,
    is_deleted boolean NOT NULL DEFAULT false,
    deleted_by bigint,
    deleted_at timestamp
  with time zone
      );

CREATE TABLE IF NOT EXISTS city_locales
(
    id
    bigserial
    PRIMARY
    KEY,

    city_id
    bigint
    NOT
    NULL
    REFERENCES
    cities
(
    id
) ON DELETE CASCADE,
    locale_id bigint NOT NULL REFERENCES locales
(
    id
)
  ON DELETE RESTRICT,
    name varchar
(
    255
) NOT NULL,
    description text NOT NULL DEFAULT '',
    sort_order int NOT NULL DEFAULT 0,

    created_by bigint NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint NOT NULL,
    updated_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version bigint NOT NULL DEFAULT 0,
    is_active boolean NOT NULL DEFAULT true,
    is_deleted boolean NOT NULL DEFAULT false,
    deleted_by bigint,
    deleted_at timestamp
  with time zone,
      UNIQUE (city_id, locale_id)
    );

INSERT INTO cities (country_id,
                    code,
                    sort_order,
                    created_by,
                    updated_by)
VALUES ((SELECT id FROM countries WHERE code = 'BD'), 'DHAKA', 1, 1, 1),
       ((SELECT id FROM countries WHERE code = 'BD'), 'CTG', 2, 1, 1),
       ((SELECT id FROM countries WHERE code = 'BD'), 'SYLHET', 3, 1, 1),
       ((SELECT id FROM countries WHERE code = 'BD'), 'RAJSHAHI', 4, 1, 1),
       ((SELECT id FROM countries WHERE code = 'BD'), 'KHULNA', 5, 1, 1),
       ((SELECT id FROM countries WHERE code = 'BD'), 'BARISAL', 6, 1, 1),
       ((SELECT id FROM countries WHERE code = 'BD'), 'RANGPUR', 7, 1, 1),
       ((SELECT id FROM countries WHERE code = 'BD'), 'MYMENSINGH', 8, 1, 1);

INSERT INTO city_locales (city_id,
                          locale_id,
                          name,
                          description,
                          sort_order,
                          created_by,
                          updated_by)
VALUES ((SELECT id FROM cities WHERE code = 'DHAKA'),
        (SELECT id FROM locales WHERE code = 'bn'),
        'ঢাকা',
        'বাংলাদেশের রাজধানী শহর',
        1, 1, 1),

       ((SELECT id FROM cities WHERE code = 'CTG'),
        (SELECT id FROM locales WHERE code = 'bn'),
        'চট্টগ্রাম',
        'বাংলাদেশের প্রধান সমুদ্র বন্দর শহর',
        1, 1, 1),

       ((SELECT id FROM cities WHERE code = 'SYLHET'),
        (SELECT id FROM locales WHERE code = 'bn'),
        'সিলেট',
        'চা বাগান ও পাহাড়ি অঞ্চল',
        1, 1, 1),

       ((SELECT id FROM cities WHERE code = 'RAJSHAHI'),
        (SELECT id FROM locales WHERE code = 'bn'),
        'রাজশাহী',
        'রেশম ও আমের শহর',
        1, 1, 1),

       ((SELECT id FROM cities WHERE code = 'KHULNA'),
        (SELECT id FROM locales WHERE code = 'bn'),
        'খুলনা',
        'সুন্দরবনের প্রবেশদ্বার',
        1, 1, 1),

       ((SELECT id FROM cities WHERE code = 'BARISAL'),
        (SELECT id FROM locales WHERE code = 'bn'),
        'বরিশাল',
        'নদীমাতৃক শহর',
        1, 1, 1),

       ((SELECT id FROM cities WHERE code = 'RANGPUR'),
        (SELECT id FROM locales WHERE code = 'bn'),
        'রংপুর',
        'উত্তরাঞ্চলের প্রশাসনিক শহর',
        1, 1, 1),

       ((SELECT id FROM cities WHERE code = 'MYMENSINGH'),
        (SELECT id FROM locales WHERE code = 'bn'),
        'ময়মনসিংহ',
        'শিক্ষা ও সংস্কৃতির শহর',
        1, 1, 1);