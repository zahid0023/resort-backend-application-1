CREATE TABLE IF NOT EXISTS contact_types
(
    id         bigserial PRIMARY KEY,

    code       varchar(50)                  NOT NULL UNIQUE, -- GENERAL, RESERVATION, SALES, SUPPORT, EMERGENCY, ACCOUNTING
    sort_order integer                      NOT NULL DEFAULT 0,

    created_by bigint references users (id) NOT NULL,
    created_at timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint references users (id) NOT NULL,
    updated_at timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version    bigint                                DEFAULT 0 NOT NULL,
    is_active  boolean                      NOT NULL DEFAULT true,
    is_deleted boolean                      NOT NULL DEFAULT false,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

CREATE TABLE IF NOT EXISTS contact_type_locales
(
    id              bigserial PRIMARY KEY,

    contact_type_id bigint                       NOT NULL REFERENCES contact_types (id),
    locale_id       bigint                       NOT NULL REFERENCES locales (id),

    name            varchar(100)                 NOT NULL,
    description     text,
    sort_order      integer                      NOT NULL DEFAULT 0,

    created_by      bigint references users (id) NOT NULL,
    created_at      timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      bigint references users (id) NOT NULL,
    updated_at      timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version         bigint                                DEFAULT 0 NOT NULL,
    is_active       boolean                      NOT NULL DEFAULT true,
    is_deleted      boolean                      NOT NULL DEFAULT false,
    deleted_by      bigint,
    deleted_at      timestamp with time zone,
    UNIQUE (contact_type_id, locale_id)
);

-- ============================================================
-- Seed data
-- ============================================================

DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        INSERT INTO contact_types (code, sort_order, created_by, updated_by)
        VALUES ('GENERAL',     1, sys_id, sys_id),
               ('RESERVATION', 2, sys_id, sys_id),
               ('SALES',       3, sys_id, sys_id),
               ('SUPPORT',     4, sys_id, sys_id),
               ('EMERGENCY',   5, sys_id, sys_id),
               ('ACCOUNTING',  6, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        INSERT INTO contact_type_locales (contact_type_id, locale_id, name, description, sort_order,
                                          created_by, updated_by)
        SELECT t.id, l.id, v.name, v.description, t.sort_order, sys_id, sys_id
        FROM contact_types t
                 JOIN (VALUES ('GENERAL',
                               'General',
                               'Main contact for general enquiries about the resort.'),
                              ('RESERVATION',
                               'Reservation',
                               'Contact for room bookings, availability checks, and reservation changes.'),
                              ('SALES',
                               'Sales',
                               'Contact for group bookings, corporate deals, and promotional inquiries.'),
                              ('SUPPORT',
                               'Support',
                               'Contact for guest assistance, complaints, and on-site support during stay.'),
                              ('EMERGENCY',
                               'Emergency',
                               '24/7 emergency contact for urgent situations requiring immediate assistance.'),
                              ('ACCOUNTING',
                               'Accounting',
                               'Contact for billing, invoices, and payment-related queries.'))
                          v(code, name, description)
                      ON t.code = v.code
                 JOIN locales l ON l.code = 'en'
        ON CONFLICT (contact_type_id, locale_id) DO NOTHING;
    END
$$;