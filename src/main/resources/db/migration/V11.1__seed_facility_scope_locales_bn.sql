insert into facility_scope_locales (facility_scope_id, locale_id, name, sort_order, created_by, updated_by)
values ((select id from facility_scopes where code = 'RESORT'), (select id from locales where code = 'bn'),
        'রিসোর্ট', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ((select id from facility_scopes where code = 'ROOM_CATEGORY'), (select id from locales where code = 'bn'),
        'রুম ক্যাটাগরি', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ((select id from facility_scopes where code = 'ROOM'), (select id from locales where code = 'bn'),
        'রুম', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system'));
