CREATE TABLE IF NOT EXISTS resort_contacts
(
    id                       bigserial PRIMARY KEY,

    -- Resort
    resort_id                bigint                       NOT NULL
        REFERENCES resorts (id),

    -- Contact purpose
    -- Examples:
    -- GENERAL
    -- RESERVATION
    -- SALES
    -- SUPPORT
    -- EMERGENCY
    contact_type_id          bigint                       NOT NULL
        REFERENCES contact_types (id),

    -- Communication channel
    -- Examples:
    -- PHONE
    -- MOBILE
    -- EMAIL
    -- WHATSAPP
    -- WEBSITE
    -- FACEBOOK
    communication_channel_id bigint                       NOT NULL
        REFERENCES communication_channels (id),

    -- Actual contact information.
    -- Examples:
    -- +8801712345678
    -- info@resort.com
    -- https://www.myresort.com
    -- https://facebook.com/myresort
    contact_value            text                         NOT NULL,

    -- Indicates the preferred contact for the same
    -- purpose and communication channel.
    is_primary               boolean                      NOT NULL DEFAULT FALSE,

    -- Display order.
    sort_order               integer                      NOT NULL DEFAULT 0,

    created_by               bigint references users (id) NOT NULL,
    created_at               timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by               bigint references users (id) NOT NULL,
    updated_at               timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version                  bigint                                DEFAULT 0 NOT NULL,
    is_active                boolean                      NOT NULL DEFAULT true,
    is_deleted               boolean                      NOT NULL DEFAULT false,
    deleted_by               bigint,
    deleted_at               timestamp with time zone,

    CONSTRAINT uq_resort_contact
        UNIQUE (
                resort_id,
                contact_type_id,
                communication_channel_id,
                contact_value
            )
);

-- Only one primary contact is allowed for each
-- resort + purpose + communication channel.
CREATE UNIQUE INDEX IF NOT EXISTS uq_resort_primary_contact
    ON resort_contacts
        (
         resort_id,
         contact_type_id,
         communication_channel_id
            )
    WHERE is_primary = TRUE;