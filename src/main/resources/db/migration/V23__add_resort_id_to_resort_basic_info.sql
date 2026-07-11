-- Resort basic info is redesigned from a global singleton to a per-resort record.
-- Existing singleton data is cleared to allow the schema change.
TRUNCATE TABLE resort_basic_info CASCADE;

-- Remove the singleton CHECK constraint (id = 1).
ALTER TABLE resort_basic_info DROP CONSTRAINT IF EXISTS resort_basic_info_id_check;

-- Make id auto-generated via a sequence.
CREATE SEQUENCE IF NOT EXISTS resort_basic_info_id_seq START WITH 1;
ALTER TABLE resort_basic_info ALTER COLUMN id SET DEFAULT nextval('resort_basic_info_id_seq');

-- Add resort_id FK — one basic info record per resort.
ALTER TABLE resort_basic_info
    ADD COLUMN resort_id bigint NOT NULL REFERENCES resorts(id);

ALTER TABLE resort_basic_info
    ADD CONSTRAINT uq_resort_basic_info_resort UNIQUE (resort_id);
