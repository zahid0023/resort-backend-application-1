CREATE TABLE IF NOT EXISTS facility_groups
(
    id         bigserial PRIMARY KEY,

    code       varchar(100)             NOT NULL,
    sort_order integer                  NOT NULL DEFAULT 1,

    icon_type  varchar(100)             NOT NULL,
    icon_value text,
    icon_meta  jsonb,

    created_by bigint                   NOT NULL REFERENCES users (id),
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint                   NOT NULL REFERENCES users (id),
    updated_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version    bigint                   NOT NULL DEFAULT 0,
    is_active  boolean                  NOT NULL DEFAULT true,
    is_deleted boolean                  NOT NULL DEFAULT false,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

CREATE TABLE IF NOT EXISTS facility_group_locales
(
    id                bigserial PRIMARY KEY,

    facility_group_id bigint                   NOT NULL REFERENCES facility_groups (id),
    locale_id         bigint                   NOT NULL REFERENCES locales (id),

    name              varchar(255)             NOT NULL,
    description       text                     NOT NULL DEFAULT '',
    sort_order        integer                  NOT NULL DEFAULT 1,

    created_by        bigint                   NOT NULL REFERENCES users (id),
    created_at        timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        bigint                   NOT NULL REFERENCES users (id),
    updated_at        timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version           bigint                   NOT NULL DEFAULT 0,
    is_active         boolean                  NOT NULL DEFAULT true,
    is_deleted        boolean                  NOT NULL DEFAULT false,
    deleted_by        bigint,
    deleted_at        timestamp with time zone
);

-- Seed: facility groups
INSERT INTO facility_groups (code, sort_order, icon_type, icon_value, icon_meta, created_by, updated_by)
VALUES ('DINING', 1, 'LUCIDE', 'UtensilsCrossed', '{
  "size": 24,
  "color": "#f59e0b",
  "stroke_width": 1.5
}', 1, 1),
       ('WELLNESS', 2, 'LUCIDE', 'Spa', '{
         "size": 24,
         "color": "#8b5cf6",
         "stroke_width": 1.5
       }', 1, 1),
       ('RECREATION', 3, 'LUCIDE', 'Waves', '{
         "size": 24,
         "color": "#3b82f6",
         "stroke_width": 1.5
       }', 1, 1),
       ('ACCOMMODATION', 4, 'LUCIDE', 'BedDouble', '{
         "size": 24,
         "color": "#10b981",
         "stroke_width": 1.5
       }', 1, 1);

-- Seed: facility group locales (English)
INSERT INTO facility_group_locales (facility_group_id, locale_id, name, description, sort_order, created_by, updated_by)
SELECT fg.id, (SELECT id FROM locales WHERE code = 'en'), t.name_en, t.desc_en, t.sort_order, 1, 1
FROM (VALUES ('DINING', 'Dining', 'All food and beverage outlets including restaurants, bars, and room service.', 1),
             ('WELLNESS', 'Wellness', 'Spa, fitness center, and wellness treatment facilities.', 2),
             ('RECREATION', 'Recreation', 'Swimming pools, sports courts, and outdoor leisure activities.', 3),
             ('ACCOMMODATION', 'Accommodation', 'Room types, suite options, and lodging facilities.',
              4)) AS t(code, name_en, desc_en, sort_order)
         JOIN facility_groups fg ON fg.code = t.code;

-- Seed: facility group locales (Bangla)
INSERT INTO facility_group_locales (facility_group_id, locale_id, name, description, sort_order, created_by, updated_by)
SELECT fg.id, (SELECT id FROM locales WHERE code = 'bn'), t.name_bn, t.desc_bn, t.sort_order, 1, 1
FROM (VALUES ('DINING', 'ডাইনিং', 'রেস্তোরাঁ, বার এবং রুম সার্ভিস সহ সকল খাবার ও পানীয় আউটলেট।', 1),
             ('WELLNESS', 'ওয়েলনেস', 'স্পা, ফিটনেস সেন্টার এবং ওয়েলনেস ট্রিটমেন্ট সুবিধা।', 2),
             ('RECREATION', 'বিনোদন', 'সুইমিং পুল, স্পোর্টস কোর্ট এবং আউটডোর বিনোদন কার্যক্রম।', 3),
             ('ACCOMMODATION', 'আবাসন', 'রুমের ধরন, স্যুট অপশন এবং আবাসন সুবিধা।',
              4)) AS t(code, name_bn, desc_bn, sort_order)
         JOIN facility_groups fg ON fg.code = t.code;