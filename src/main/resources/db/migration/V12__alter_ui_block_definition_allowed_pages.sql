alter table ui_block_definition
    add column if not exists allowed_pages_new text[];

update ui_block_definition
set allowed_pages_new = allowed_pages
where allowed_pages is not null;

alter table ui_block_definition
    drop column if exists allowed_pages;

alter table ui_block_definition
    rename column allowed_pages_new to allowed_pages;
