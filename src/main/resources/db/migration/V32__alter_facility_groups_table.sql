-- Rename to match entity @Table(name = "facility_groups")
alter table if exists facility_group rename to facility_groups;

-- Widen code column to match entity (varchar 50 -> 100) and add unique constraint
alter table facility_groups alter column code type varchar(100);
alter table facility_groups add constraint uq_facility_groups_code unique (code);

-- Set description not null with empty-string default to match entity @ColumnDefault("''")
update facility_groups set description = '' where description is null;
alter table facility_groups alter column description set not null;
alter table facility_groups alter column description set default '';

-- Add icon columns
alter table facility_groups add column if not exists icon_type  varchar(100) not null default 'LUCIDE';
alter table facility_groups add column if not exists icon_value text;
alter table facility_groups add column if not exists icon_meta  jsonb;
