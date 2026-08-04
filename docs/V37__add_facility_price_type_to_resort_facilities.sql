ALTER TABLE resort_facilities
    ADD COLUMN IF NOT EXISTS facility_price_type_id bigint NOT NULL REFERENCES facility_price_types (id);

CREATE INDEX IF NOT EXISTS idx_resort_facilities_price_type
    ON resort_facilities (facility_price_type_id);
