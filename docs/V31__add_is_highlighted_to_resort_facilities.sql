ALTER TABLE resort_facilities
    ADD COLUMN IF NOT EXISTS is_highlighted boolean NOT NULL DEFAULT false;
