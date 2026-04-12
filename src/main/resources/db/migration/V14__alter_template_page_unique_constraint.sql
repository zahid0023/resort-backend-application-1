alter table template_page
    drop constraint if exists uq_template_page_template_id_page_type_id;

create unique index uq_template_page_active_template_id_page_type_id
    on template_page (template_id, page_type_id)
    where is_active = true and is_deleted = false;
