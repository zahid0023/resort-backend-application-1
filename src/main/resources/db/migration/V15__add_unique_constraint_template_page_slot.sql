create unique index uq_template_page_slot_active_template_page_id_ui_block_category_id
    on template_page_slot (template_page_id, ui_block_category_id)
    where is_active = true and is_deleted = false;
