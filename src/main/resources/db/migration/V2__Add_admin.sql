insert into usr (id, username, password, active, email, blocked, is_online)
values (1,
        'admin',
        '$2a$06$UMzvW8jD1ucd7ffoBGFhY.VsFaixBHS59j.5i8vKrl9Fg9EN6OtMW',
        true,
        'guravskiy79@gmail.com',
        false,
        false
        );

insert into user_role (user_id, roles)
values (1, 'USER'),
       (1, 'ADMIN');

-- update usr set password = '$2a$08$B0Zih/A07ceAsvpJe9fxMeHleHGwGvrco3y9pRYdpbvO0cx0OKgkS'
-- password = 11
-- https://www.devglan.com/online-tools/bcrypt-hash-generator