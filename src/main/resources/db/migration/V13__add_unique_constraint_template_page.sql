alter table template_page
    add constraint uq_template_page_template_id_page_type_id unique (template_id, page_type_id);
