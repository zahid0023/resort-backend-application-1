create table if not exists resort_images (
    id          bigserial     primary key,
    resort_id   bigint        not null references resorts(id),
    image_url   text          not null,
    public_id   varchar(255),
    caption     varchar(255),
    is_default  boolean       not null default false,
    sort_order  integer       not null default 0,
    created_by  bigint        not null,
    created_at  timestamptz   not null default current_timestamp,
    updated_by  bigint        not null,
    updated_at  timestamptz   not null default current_timestamp,
    version     bigint        not null default 0,
    is_active   boolean       not null default true,
    is_deleted  boolean       not null default false,
    deleted_by  bigint,
    deleted_at  timestamptz
);
