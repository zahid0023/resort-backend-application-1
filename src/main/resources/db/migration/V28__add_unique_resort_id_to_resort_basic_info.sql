ALTER TABLE resort_basic_info
    ADD CONSTRAINT uq_resort_basic_info_resort_id UNIQUE (resort_id);
