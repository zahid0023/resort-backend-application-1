-- V17 seeded price_unit_scope_assignments for the ROOM_CATEGORY scope only, leaving the ROOM scope
-- (created alongside it in V15) with zero assigned price units. Since fn_validate_resort_room_price_unit_scope()
-- (V40) requires resort_room_main_prices/resort_room_special_prices.price_unit_id to be assigned to the ROOM
-- scope, every insert/update into those tables was rejected regardless of price_unit_id. Mirror the same
-- price units already assigned to ROOM_CATEGORY.
insert into price_unit_scope_assignments (price_scope_id, price_unit_id, created_by, updated_by)
select (select id from price_scopes where code = 'ROOM'),
       pu.id,
       (select id from users where username = 'system'),
       (select id from users where username = 'system')
from price_units pu
where pu.code in ('PER_NIGHT', 'PER_DAY', 'PER_HOUR', 'PER_PERSON', 'PER_ROOM', 'PER_BOOKING')
  and not exists (select 1
                  from price_unit_scope_assignments pusa
                           join price_scopes ps on ps.id = pusa.price_scope_id
                  where pusa.price_unit_id = pu.id
                    and ps.code = 'ROOM');
