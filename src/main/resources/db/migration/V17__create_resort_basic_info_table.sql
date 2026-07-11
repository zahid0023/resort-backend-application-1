CREATE TABLE IF NOT EXISTS resort_basic_info
(
    id         bigint PRIMARY KEY CHECK ( id = 1 ),

    code       varchar(50)                      NOT NULL UNIQUE,
    sort_order integer                          NOT NULL DEFAULT 0,

    estd       smallint                         NOT NULL,

    country_id bigint references countries (id) NOT NULL,
    city_id    bigint references cities (id)    NOT NULL,

    logo_url   text,

    lat        float,
    lon        float,

    created_by bigint references users (id)     NOT NULL,
    created_at timestamp with time zone         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint references users (id)     NOT NULL,
    updated_at timestamp with time zone         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version    bigint                                    DEFAULT 0 NOT NULL,
    is_active  boolean                          NOT NULL DEFAULT true,
    is_deleted boolean                          NOT NULL DEFAULT false,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

CREATE TABLE IF NOT EXISTS resort_basic_info_locales
(
    id                   bigserial PRIMARY KEY,

    resort_basic_info_id bigint                       NOT NULL REFERENCES resort_basic_info (id) ON DELETE CASCADE,
    locale_id            bigint                       NOT NULL REFERENCES locales (id) ON DELETE RESTRICT,
    sort_order           int                          NOT NULL DEFAULT 0,

    name                 varchar(255)                 NOT NULL,
    tagline              text                         NOT NULL,
    short_description    varchar(1024),
    address              text,

    created_by           bigint references users (id) NOT NULL,
    created_at           timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by           bigint references users (id) NOT NULL,
    updated_at           timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version              bigint                                DEFAULT 0 NOT NULL,
    is_active            boolean                      NOT NULL DEFAULT true,
    is_deleted           boolean                      NOT NULL DEFAULT false,
    deleted_by           bigint,
    deleted_at           timestamp with time zone,

    UNIQUE (resort_basic_info_id, locale_id)
);