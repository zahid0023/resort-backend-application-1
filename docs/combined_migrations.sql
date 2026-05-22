-- V5: create_page_types_table
create table if not exists page_types
(
    id
    bigserial
    primary
    key,
    key
    varchar
(
    100
) not null,
    name varchar
(
    100
) not null,
    description text,
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V6: create_ui_block_categories_table
create table if not exists ui_block_categories
(
    id
    bigserial
    primary
    key,
    key
    varchar
(
    100
) not null,
    name varchar
(
    100
) not null,
    description text,
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V7: create_ui_block_definition_table
create table if not exists ui_block_definition
(
    id
    bigserial
    primary
    key,
    ui_block_key
    varchar
(
    100
) not null,
    name varchar
(
    150
) not null,
    description text not null,
    ui_block_version varchar
(
    20
) default '1.0.0' not null,
    ui_block_category_id bigint not null references ui_block_categories
(
    id
),
    page_type_id bigint not null references page_types
(
    id
),
    editable_schema jsonb not null,
    default_content jsonb not null,
    allowed_pages jsonb,
    status varchar
(
    20
) default 'draft' not null,
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V8: create_template_table
create table if not exists template
(
    id
    bigserial
    primary
    key,
    key
    varchar
(
    100
) not null,
    name varchar
(
    150
) not null,
    description text,
    status varchar
(
    20
) default 'draft' not null,
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V9: create_template_page_table
create table if not exists template_page
(
    id
    bigserial
    primary
    key,
    template_id
    bigint
    not
    null
    references
    template
(
    id
),
    page_type_id bigint not null references page_types
(
    id
),
    page_name varchar
(
    150
) not null,
    page_slug varchar
(
    150
) not null,
    page_order integer not null,
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V10: create_template_page_slot_table
create table if not exists template_page_slot
(
    id
    bigserial
    primary
    key,
    template_page_id
    bigint
    not
    null
    references
    template_page
(
    id
),
    ui_block_category_id bigint not null references ui_block_categories
(
    id
),
    slot_name varchar
(
    150
) not null,
    is_required boolean default false not null,
    slot_order integer not null,
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V11: create_template_page_slot_variant_table
create table if not exists template_page_slot_variant
(
    id
    bigserial
    primary
    key,
    template_page_slot_id
    bigint
    not
    null
    references
    template_page_slot
(
    id
),
    ui_block_definition_id bigint not null references ui_block_definition
(
    id
),
    display_order integer,
    is_default boolean default false not null,
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V12: alter_ui_block_definition_allowed_pages (jsonb -> text[])
alter table ui_block_definition
    add column if not exists allowed_pages_new text[];

update ui_block_definition
set allowed_pages_new = allowed_pages
where allowed_pages is not null;

alter table ui_block_definition
drop
column if exists allowed_pages;

alter table ui_block_definition
    rename column allowed_pages_new to allowed_pages;

-- V13: add_unique_constraint_template_page
alter table template_page
    add constraint uq_template_page_template_id_page_type_id unique (template_id, page_type_id);

-- V14: alter_template_page_unique_constraint (replace with partial index)
alter table template_page
drop
constraint if exists uq_template_page_template_id_page_type_id;

create unique index uq_template_page_active_template_id_page_type_id
    on template_page (template_id, page_type_id) where is_active = true and is_deleted = false;

-- V15: add_unique_constraint_template_page_slot
create unique index uq_template_page_slot_active_template_page_id_ui_block_category_id
    on template_page_slot (template_page_id, ui_block_category_id) where is_active = true and is_deleted = false;

-- V16: create_resort_access_types_table
create table if not exists resort_access_types
(
    id
    bigserial
    primary
    key,
    code
    varchar
(
    50
) not null,
    name varchar
(
    100
) not null,
    description text not null,
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V17: create_countries_table
create table if not exists countries
(
    id
    bigserial
    primary
    key,
    code
    varchar
(
    10
),
    name varchar
(
    100
),
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V18: create_cities_table
create table if not exists cities
(
    id
    bigserial
    primary
    key,
    name
    varchar
(
    150
) not null,
    country_id bigint not null references countries
(
    id
),
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V19: create_resorts_table
create table if not exists resorts
(
    id
    bigserial
    primary
    key,
    uuid
    uuid
    not
    null,
    name
    varchar
(
    255
) not null,
    description text default '' not null,
    address text,
    country_id bigint not null references countries
(
    id
),
    city_id bigint references cities
(
    id
),
    contact_email varchar
(
    255
),
    contact_phone varchar
(
    50
),
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

-- V20: create_user_resort_access_table
create table if not exists user_resort_access
(
    id
    bigserial
    primary
    key,
    user_id
    bigint
    not
    null
    references
    users
(
    id
),
    resort_id bigint not null references resorts
(
    id
),
    access_type_id bigint not null references resort_access_types
(
    id
),
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone,
                             unique (user_id, resort_id)
    );

-- V21: create_room_categories_table
create table if not exists room_categories
(
    id
    bigserial
    primary
    key,
    code
    varchar
(
    50
) not null,
    name varchar
(
    100
) not null,
    description text not null,
    sort_order integer default 0 not null,
    created_by bigint not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version bigint default 0 not null,
    is_active boolean default true not null,
    is_deleted boolean default false not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
                             );

create table resorts
(
    id            bigserial primary key,
    uuid          uuid                                               not null unique,

    name          varchar(255)                                       not null,
    description   text                                               not null default '',

    address       text,
    country_id    bigint references countries (id)                   not null,
    city_id       bigint references cities (id),

    contact_email varchar(255),
    contact_phone varchar(50),

    created_by    bigint references users (id)                       not null,
    created_at    timestamp with time zone default current_timestamp not null,
    updated_by    bigint references users (id)                       not null,
    updated_at    timestamp with time zone default current_timestamp not null,
    version       bigint                   default 0                 not null,
    is_active     boolean                  default true              not null,
    is_deleted    boolean                  default false             not null,
    deleted_by    bigint,
    deleted_at    timestamp with time zone
);

create table resort_image_storage_configs
(
    id         bigserial primary key,

    resort_id  bigint                                             not null references resorts (id),

    provider   varchar(50)                                        not null,
    -- e.g. CLOUDINARY, S3

    config     jsonb                                              not null,

    created_by bigint references users (id)                       not null,
    created_at timestamp with time zone default current_timestamp not null,
    updated_by bigint references users (id)                       not null,
    updated_at timestamp with time zone default current_timestamp not null,
    version    bigint                   default 0                 not null,
    is_active  boolean                  default true              not null,
    is_deleted boolean                  default false             not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

create table resort_images
(
    id         bigserial primary key,

    resort_id  bigint                                             not null references resorts (id),

    image_url  text                                               not null,
    public_id  varchar(255),

    caption    varchar(255),

    is_default boolean                  default false             not null,
    sort_order int                      default 0                 not null,

    created_by bigint references users (id)                       not null,
    created_at timestamp with time zone default current_timestamp not null,
    updated_by bigint references users (id)                       not null,
    updated_at timestamp with time zone default current_timestamp not null,
    version    bigint                   default 0                 not null,
    is_active  boolean                  default true              not null,
    is_deleted boolean                  default false             not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
);
