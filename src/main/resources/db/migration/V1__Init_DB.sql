 create table chat (id bigint not null, creation_date datetime, last_message longblob, updated_date datetime, primary key (id)) engine=MyISAM;
 create table chat_members (chats_id bigint not null, members_id bigint not null) engine=MyISAM;
 create table hibernate_sequence (next_val bigint) engine=MyISAM;
 insert into hibernate_sequence values ( 1 );
 insert into hibernate_sequence values ( 1 );
 insert into hibernate_sequence values ( 1 );
 insert into hibernate_sequence values ( 1 );
 insert into hibernate_sequence values ( 1 );
 insert into hibernate_sequence values ( 1 );
 create table image (id bigint not null, hide bit not null, main bit not null, name varchar(2520), tested bit not null, url_link varchar(2520), user_id bigint, primary key (id)) engine=MyISAM;
 create table message (id bigint not null, content longblob, creation_date datetime, delivered bit, updated_date datetime, viewed bit, viewed_page bit, author_id bigint, chat_id bigint, primary key (id)) engine=MyISAM;
 create table profile_detail (id bigint not null, hobby varchar(255), look tinyblob, personal_information varchar(255), target varchar(255), text_about varchar(2520), user_id bigint, primary key (id)) engine=MyISAM;
 create table user_role (user_id bigint not null, roles varchar(255)) engine=MyISAM;
 create table user_like (owner_id bigint not null, user_like_id bigint not null, time_like datetime, primary key (owner_id, user_like_id)) engine=MyISAM;
 create table user_view (id bigint not null, last_visit_user date, owner_id bigint, user_view_id bigint, primary key (id)) engine=MyISAM;
 create table usr (id bigint not null, activation_code varchar(255), active bit not null, birth_date date, blocked bit not null, city varchar(255), country varchar(255), date_of_registration datetime, email varchar(255), gender varchar(255), is_online bit not null, last_visit datetime, like_new integer, new_email varchar(255), new_password varchar(255), password varchar(255), region varchar(255), username varchar(255), userpic varchar(2520), warning integer, primary key (id)) engine=MyISAM;
 create table usr_chats_deleted (user_id bigint not null, chats_deleted_id bigint not null) engine=MyISAM;
 create table usr_users_block (user_id bigint not null, users_block_id bigint not null) engine=MyISAM;
 alter table usr_users_block add constraint UK_ddvyhwyccsogqhgd127ibrle9 unique (users_block_id);
 alter table chat_members add constraint FKdg2442b9m5j1i2g3q42ie68yr foreign key (members_id) references usr (id);
 alter table chat_members add constraint FKo2knhn9cl27criwsgduquk6ge foreign key (chats_id) references chat (id);
 alter table image add constraint FKvf5ssrlgrnny2hg946lc9m9y foreign key (user_id) references usr (id);
 alter table message add constraint FKqhhiq2fjqs0a1cgrg9bueu7ab foreign key (author_id) references usr (id);
 alter table message add constraint FKmejd0ykokrbuekwwgd5a5xt8a foreign key (chat_id) references chat (id);
 alter table profile_detail add constraint FKam9mvawqgysvnbaxgaut0lhqp foreign key (user_id) references usr (id);
 alter table user_role add constraint FKfpm8swft53ulq2hl11yplpr5 foreign key (user_id) references usr (id);
 alter table user_like add constraint FK9xpcf94t8w3jritucqmfx0lba foreign key (user_like_id) references usr (id);
 alter table user_like add constraint FKg82jvr6focvcmjsgj62vqpntt foreign key (owner_id) references usr (id);
 alter table user_view add constraint FKj2r6ub7q8d3l893qpe0sju7g1 foreign key (owner_id) references usr (id);
 alter table user_view add constraint FKt3bsgfqn8ra51onabn7us906s foreign key (user_view_id) references usr (id);
 alter table usr_chats_deleted add constraint FK1u6kdxwehh53jg867pnel62ia foreign key (chats_deleted_id) references chat (id);
 alter table usr_chats_deleted add constraint FKg1h0uildoed7uyt44lxnkucs7 foreign key (user_id) references usr (id);
 alter table usr_users_block add constraint FK9ckcjbfw7xdujkqcv2iot82yt foreign key (users_block_id) references usr (id);
 alter table usr_users_block add constraint FK1602egwe8ry03bv2lrqglphqu foreign key (user_id) references usr (id);




-- create table hibernate_sequence (
--     next_val bigint
-- ) engine=MyISAM;
-- insert into hibernate_sequence values ( 1 );
-- insert into hibernate_sequence values ( 1 );
-- insert into hibernate_sequence values ( 1 );
-- insert into hibernate_sequence values ( 1 );
-- insert into hibernate_sequence values ( 1 );
-- insert into hibernate_sequence values ( 1 );
-- insert into hibernate_sequence values ( 1 );
--
-- create table chat (
--           id bigint not null,
--           creation_date datetime,
--           last_message varchar(255),
--           updated_date datetime,
--           primary key (id)
--         ) engine=MyISAM;
--
--  create table chat_members (
--          chats_id bigint not null,
--          members_id bigint not null
--          ) engine=MyISAM;
--
--  create table content (
--         id bigint not null,
--         content varchar(2048) NOT NULL,
--         link varchar(255),
--         link_cover varchar(255),
--         link_description varchar(255),
--         link_title varchar(255),
--         message_id bigint,
--         primary key (id)
--         ) engine=MyISAM DEFAULT CHARSET=utf8mb4;
--
--  create table image (
--          id bigint not null,
--          hide bit not null,
--          main bit not null,
--          name varchar(2520),
--          tested bit not null,
--          url_link varchar(2520),
--          user_id bigint,
--          primary key (id)
--          ) engine=MyISAM;
--
--  create table message (
--          id bigint not null,
--          creation_date datetime,
--          delivered bit,
--          updated_date datetime,
--          viewed bit,
--          viewed_page bit,
--          author_id bigint,
--          chat_id bigint,
--          primary key (id)
--          ) engine=MyISAM;
--
--  create table profile_detail (
--          id bigint not null,
--          hobby varchar(255),
--          look tinyblob,
--          personal_information varchar(255),
--          target varchar(255),
--          text_about varchar(2520),
--          user_id bigint,
--          primary key (id)
--          ) engine=MyISAM;
--
--  create table user_role (
--          user_id bigint not null,
--          roles varchar(255)
--          ) engine=MyISAM;
--
--  create table user_like (
--          owner_id bigint not null,
--          user_like_id bigint not null,
--          time_like datetime,
--          primary key (owner_id, user_like_id)
--          ) engine=MyISAM;
--
--  create table user_view (
--         id bigint not null,
--         last_visit_user date,
--         owner_id bigint,
--         user_view_id bigint,
--         primary key (id)
--         ) engine=MyISAM;
--
--  create table usr (
--          id bigint not null,
--          activation_code varchar(255),
--          active bit not null,
--          birth_date date,
--          blocked bit not null,
--          city varchar(255),
--          country varchar(255),
--          date_of_registration datetime,
--          email varchar(255),
--          gender varchar(255),
--          is_online bit not null,
--          last_visit datetime,
--          like_new integer,
--          new_email varchar(255),
--          new_password varchar(255),
--          password varchar(255),
--          region varchar(255),
--          username varchar(255),
--          userpic varchar(2520),
--          warning integer,
--          primary key (id)
--          ) engine=MyISAM;
--
--  create table usr_chats_deleted (
--          user_id bigint not null,
--          chats_deleted_id bigint not null
--          ) engine=MyISAM;
--
--  create table usr_users_block (
--          user_id bigint not null,
--          users_block_id bigint not null
--          ) engine=MyISAM;
--
--  alter table usr_users_block add constraint UK_ddvyhwyccsogqhgd127ibrle9 unique (users_block_id);
--  alter table chat_members add constraint FKdg2442b9m5j1i2g3q42ie68yr foreign key (members_id) references usr (id);
--  alter table chat_members add constraint FKo2knhn9cl27criwsgduquk6ge foreign key (chats_id) references chat (id);
--  alter table content add constraint FK9bte53vcafscxg0ljm0fl95rk foreign key (message_id) references message (id);
--  alter table image add constraint FKvf5ssrlgrnny2hg946lc9m9y foreign key (user_id) references usr (id);
--  alter table message add constraint FKqhhiq2fjqs0a1cgrg9bueu7ab foreign key (author_id) references usr (id);
--  alter table message add constraint FKmejd0ykokrbuekwwgd5a5xt8a foreign key (chat_id) references chat (id);
--  alter table profile_detail add constraint FKam9mvawqgysvnbaxgaut0lhqp foreign key (user_id) references usr (id);
--  alter table user_role add constraint FKfpm8swft53ulq2hl11yplpr5 foreign key (user_id) references usr (id);
--  alter table user_like add constraint FK9xpcf94t8w3jritucqmfx0lba foreign key (user_like_id) references usr (id);
--  alter table user_like add constraint FKg82jvr6focvcmjsgj62vqpntt foreign key (owner_id) references usr (id);
--  alter table user_view add constraint FKj2r6ub7q8d3l893qpe0sju7g1 foreign key (owner_id) references usr (id);
--  alter table user_view add constraint FKt3bsgfqn8ra51onabn7us906s foreign key (user_view_id) references usr (id);
--  alter table usr_chats_deleted add constraint FK1u6kdxwehh53jg867pnel62ia foreign key (chats_deleted_id) references chat (id);
--  alter table usr_chats_deleted add constraint FKg1h0uildoed7uyt44lxnkucs7 foreign key (user_id) references usr (id);
--  alter table usr_users_block add constraint FK9ckcjbfw7xdujkqcv2iot82yt foreign key (users_block_id) references usr (id);
--  alter table usr_users_block add constraint FK1602egwe8ry03bv2lrqglphqu foreign key (user_id) references usr (id);
--
--
-- -- create table chat (id bigint not null, creation_date datetime, last_message varchar(255), updated_date datetime, primary key (id)) engine=MyISAM;
-- -- create table chat_members (chats_id bigint not null, members_id bigint not null) engine=MyISAM;
-- -- create table hibernate_sequence (next_val bigint) engine=MyISAM;
-- -- insert into hibernate_sequence values ( 1 );
-- -- insert into hibernate_sequence values ( 1 );
-- -- insert into hibernate_sequence values ( 1 );
-- -- insert into hibernate_sequence values ( 1 );
-- -- insert into hibernate_sequence values ( 1 );
-- -- insert into hibernate_sequence values ( 1 );
-- -- create table image (id bigint not null, hide bit not null, main bit not null, name varchar(2520), tested bit not null, url_link varchar(2520), user_id bigint, primary key (id)) engine=MyISAM;
-- -- create table message (id bigint not null, content LONGBLOB, creation_date datetime, delivered bit, updated_date datetime, viewed bit, viewed_page bit, author_id bigint, chat_id bigint, primary key (id)) engine=MyISAM;
-- -- create table profile_detail (id bigint not null, hobby varchar(255), look tinyblob, personal_information varchar(255), target varchar(255), text_about varchar(2520), user_id bigint, primary key (id)) engine=MyISAM;
-- -- create table user_role (user_id bigint not null, roles varchar(255)) engine=MyISAM;
-- -- create table user_like (owner_id bigint not null, user_like_id bigint not null, time_like datetime, primary key (owner_id, user_like_id)) engine=MyISAM;
-- -- create table user_view (id bigint not null, last_visit_user date, owner_id bigint, user_view_id bigint, primary key (id)) engine=MyISAM;
-- -- create table usr (id bigint not null, activation_code varchar(255), active bit not null, birth_date date, blocked bit not null, city varchar(255), country varchar(255), date_of_registration datetime, email varchar(255), gender varchar(255), is_online bit not null, last_visit datetime, like_new integer, new_email varchar(255), new_password varchar(255), password varchar(255), region varchar(255), username varchar(255), userpic varchar(2520), warning integer, primary key (id)) engine=MyISAM;
-- -- create table usr_chats_deleted (user_id bigint not null, chats_deleted_id bigint not null) engine=MyISAM;
-- -- create table usr_users_block (user_id bigint not null, users_block_id bigint not null) engine=MyISAM;
-- -- alter table usr_users_block add constraint UK_ddvyhwyccsogqhgd127ibrle9 unique (users_block_id);
-- -- alter table chat_members add constraint FKdg2442b9m5j1i2g3q42ie68yr foreign key (members_id) references usr (id);
-- -- alter table chat_members add constraint FKo2knhn9cl27criwsgduquk6ge foreign key (chats_id) references chat (id);
-- -- alter table image add constraint FKvf5ssrlgrnny2hg946lc9m9y foreign key (user_id) references usr (id);
-- -- alter table message add constraint FKqhhiq2fjqs0a1cgrg9bueu7ab foreign key (author_id) references usr (id);
-- -- alter table message add constraint FKmejd0ykokrbuekwwgd5a5xt8a foreign key (chat_id) references chat (id);
-- -- alter table profile_detail add constraint FKam9mvawqgysvnbaxgaut0lhqp foreign key (user_id) references usr (id);
-- -- alter table user_role add constraint FKfpm8swft53ulq2hl11yplpr5 foreign key (user_id) references usr (id);
-- -- alter table user_like add constraint FK9xpcf94t8w3jritucqmfx0lba foreign key (user_like_id) references usr (id);
-- -- alter table user_like add constraint FKg82jvr6focvcmjsgj62vqpntt foreign key (owner_id) references usr (id);
-- -- alter table user_view add constraint FKj2r6ub7q8d3l893qpe0sju7g1 foreign key (owner_id) references usr (id);
-- -- alter table user_view add constraint FKt3bsgfqn8ra51onabn7us906s foreign key (user_view_id) references usr (id);
-- -- alter table usr_chats_deleted add constraint FK1u6kdxwehh53jg867pnel62ia foreign key (chats_deleted_id) references chat (id);
-- -- alter table usr_chats_deleted add constraint FKg1h0uildoed7uyt44lxnkucs7 foreign key (user_id) references usr (id);
-- -- alter table usr_users_block add constraint FK9ckcjbfw7xdujkqcv2iot82yt foreign key (users_block_id) references usr (id);
-- -- alter table usr_users_block add constraint FK1602egwe8ry03bv2lrqglphqu foreign key (user_id) references usr (id);