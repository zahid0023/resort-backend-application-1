CREATE TABLE IF NOT EXISTS facilities
(
    id                bigserial PRIMARY KEY,

    code              varchar(100)             NOT NULL,
    sort_order        integer                  NOT NULL DEFAULT 1,

    facility_group_id bigint                   NOT NULL REFERENCES facility_groups (id),

    icon_type         varchar(100)             NOT NULL,
    icon_value        text,
    icon_meta         jsonb,

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

CREATE TABLE IF NOT EXISTS facility_locales
(
    id          bigserial PRIMARY KEY,

    facility_id bigint                   NOT NULL REFERENCES facilities (id),
    locale_id   bigint                   NOT NULL REFERENCES locales (id),

    name        varchar(255)             NOT NULL,
    description text                     NOT NULL DEFAULT '',
    sort_order  integer                  NOT NULL DEFAULT 1,

    created_by  bigint                   NOT NULL REFERENCES users (id),
    created_at  timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  bigint                   NOT NULL REFERENCES users (id),
    updated_at  timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version     bigint                   NOT NULL DEFAULT 0,
    is_active   boolean                  NOT NULL DEFAULT true,
    is_deleted  boolean                  NOT NULL DEFAULT false,
    deleted_by  bigint,
    deleted_at  timestamp with time zone
);

-- Seed: facilities
INSERT INTO facilities (code, sort_order, facility_group_id, icon_type, icon_value, icon_meta, created_by, updated_by)
SELECT t.code,
       t.sort_order,
       fg.id,
       t.icon_type::varchar,
       t.icon_value,
       t.icon_meta::jsonb,
       1,
       1
FROM (VALUES ('RESTAURANT', 1, 'DINING', 'LUCIDE', 'UtensilsCrossed', '{"size": 24, "color": "#f59e0b"}'),
             ('POOL_BAR', 2, 'DINING', 'LUCIDE', 'GlassWater', '{"size": 24, "color": "#3b82f6"}'),
             ('SPA', 1, 'WELLNESS', 'LUCIDE', 'Sparkles', '{"size": 24, "color": "#8b5cf6"}'),
             ('FITNESS', 2, 'WELLNESS', 'LUCIDE', 'Dumbbell', '{"size": 24, "color": "#ec4899"}'),
             ('SWIMMING_POOL', 1, 'RECREATION', 'LUCIDE', 'Waves', '{"size": 24, "color": "#06b6d4"}'),
             ('TENNIS', 2, 'RECREATION', 'LUCIDE', 'CircleDot', '{"size": 24, "color": "#84cc16"}'),
             ('ROOM_SERVICE', 1, 'ACCOMMODATION', 'LUCIDE', 'BellRing', '{"size": 24, "color": "#f97316"}'),
             ('CONCIERGE', 2, 'ACCOMMODATION', 'LUCIDE', 'ConciergeBell',
              '{"size": 24, "color": "#14b8a6"}')) AS t(code, sort_order, group_code, icon_type, icon_value, icon_meta)
         JOIN facility_groups fg ON fg.code = t.group_code;

-- Seed: facility locales (English)
INSERT INTO facility_locales (facility_id, locale_id, name, description, sort_order, created_by, updated_by)
SELECT f.id, (SELECT id FROM locales WHERE code = 'en'), t.name_en, t.desc_en, t.sort_order, 1, 1
FROM (VALUES ('RESTAURANT', 'Main Restaurant', 'Full-service restaurant with buffet and à la carte options.', 1),
             ('POOL_BAR', 'Pool Bar', 'Outdoor bar serving refreshments by the pool.', 2),
             ('SPA', 'Spa & Treatment', 'Relaxing spa treatments and beauty therapies.', 1),
             ('FITNESS', 'Fitness Center', 'Fully equipped gym with cardio and strength training.', 2),
             ('SWIMMING_POOL', 'Swimming Pool', 'Outdoor infinity pool with sun deck and loungers.', 1),
             ('TENNIS', 'Tennis Court', 'Floodlit tennis courts available for day and evening play.', 2),
             ('ROOM_SERVICE', 'Room Service', '24-hour in-room dining available for all guests.', 1),
             ('CONCIERGE', 'Concierge', 'Dedicated concierge team for reservations and local guidance.',
              2)) AS t(code, name_en, desc_en, sort_order)
         JOIN facilities f ON f.code = t.code;

-- Seed: facility locales (Bangla)
INSERT INTO facility_locales (facility_id, locale_id, name, description, sort_order, created_by, updated_by)
SELECT f.id, (SELECT id FROM locales WHERE code = 'bn'), t.name_bn, t.desc_bn, t.sort_order, 1, 1
FROM (VALUES ('RESTAURANT', 'মূল রেস্তোরাঁ', 'বুফে এবং আ লা কার্ট অপশন সহ ফুল-সার্ভিস রেস্তোরাঁ।', 1),
             ('POOL_BAR', 'পুল বার', 'পুলের পাশে রিফ্রেশমেন্ট পরিবেশনকারী আউটডোর বার।', 2),
             ('SPA', 'স্পা ও ট্রিটমেন্ট', 'রিল্যাক্সিং স্পা ট্রিটমেন্ট এবং বিউটি থেরাপি।', 1),
             ('FITNESS', 'ফিটনেস সেন্টার', 'কার্ডিও এবং স্ট্রেংথ ট্রেনিং সহ সম্পূর্ণ সজ্জিত জিম।', 2),
             ('SWIMMING_POOL', 'সুইমিং পুল', 'সান ডেক ও লাউঞ্জার সহ আউটডোর ইনফিনিটি পুল।', 1),
             ('TENNIS', 'টেনিস কোর্ট', 'দিন ও সন্ধ্যায় খেলার জন্য ফ্লাডলিট টেনিস কোর্ট।', 2),
             ('ROOM_SERVICE', 'রুম সার্ভিস', 'সকল অতিথিদের জন্য ২৪ ঘণ্টা ইন-রুম ডাইনিং সুবিধা।', 1),
             ('CONCIERGE', 'কনসিয়ার্জ', 'রিজার্ভেশন ও স্থানীয় গাইডেন্সের জন্য ডেডিকেটেড কনসিয়ার্জ টিম।',
              2)) AS t(code, name_bn, desc_bn, sort_order)
         JOIN facilities f ON f.code = t.code;