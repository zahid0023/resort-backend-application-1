CREATE TABLE IF NOT EXISTS resorts
(
    id         bigserial PRIMARY KEY,

    -- Internal unique code
    code       varchar(100)             NOT NULL,

    created_by bigint                   NOT NULL REFERENCES users (id),
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint                   NOT NULL REFERENCES users (id),
    updated_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version    bigint                   NOT NULL DEFAULT 0,
    is_active  boolean                  NOT NULL DEFAULT true,
    is_deleted boolean                  NOT NULL DEFAULT false,
    deleted_by bigint REFERENCES users (id),
    deleted_at timestamp with time zone,

    CONSTRAINT uq_resorts_code UNIQUE (code)
);

CREATE INDEX idx_resorts_active
    ON resorts (is_active);

CREATE INDEX idx_resorts_deleted
    ON resorts (is_deleted);

CREATE TABLE IF NOT EXISTS resort_users
(
    id                    bigserial PRIMARY KEY,

    resort_id             bigint                   NOT NULL REFERENCES resorts (id),
    user_id               bigint                   NOT NULL REFERENCES users (id),
    -- Default access type
    resort_access_type_id bigint                   NOT NULL REFERENCES resort_access_types (id),

    joined_at             timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_by            bigint                   NOT NULL REFERENCES users (id),
    created_at            timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by            bigint                   NOT NULL REFERENCES users (id),
    updated_at            timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version               bigint                   NOT NULL DEFAULT 0,
    is_active             boolean                  NOT NULL DEFAULT true,
    is_deleted            boolean                  NOT NULL DEFAULT false,
    deleted_by            bigint REFERENCES users (id),
    deleted_at            timestamp with time zone,

    CONSTRAINT uq_resort_user
        UNIQUE (resort_id, user_id)
);

CREATE INDEX idx_resort_users_resort
    ON resort_users (resort_id);

CREATE INDEX idx_resort_users_user
    ON resort_users (user_id);

CREATE INDEX idx_resort_users_access_type
    ON resort_users (resort_access_type_id);

CREATE TABLE IF NOT EXISTS resort_user_permissions
(
    id                        bigserial PRIMARY KEY,

    resort_user_id            bigint                   NOT NULL REFERENCES resort_users (id) ON DELETE CASCADE,
    resort_permission_type_id bigint                   NOT NULL REFERENCES resort_permission_types (id),

    -- true = explicitly allow
    -- false = explicitly deny
    is_allowed                boolean                  NOT NULL,

    created_by                bigint                   NOT NULL REFERENCES users (id),
    created_at                timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by                bigint                   NOT NULL REFERENCES users (id),
    updated_at                timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version                   bigint                   NOT NULL DEFAULT 0,
    is_active                 boolean                  NOT NULL DEFAULT true,
    is_deleted                boolean                  NOT NULL DEFAULT false,
    deleted_by                bigint REFERENCES users (id),
    deleted_at                timestamp with time zone,

    CONSTRAINT uq_resort_user_permission
        UNIQUE (resort_user_id, resort_permission_type_id)
);

CREATE INDEX idx_resort_user_permissions_user
    ON resort_user_permissions (resort_user_id);

CREATE INDEX idx_resort_user_permissions_permission
    ON resort_user_permissions (resort_permission_type_id);