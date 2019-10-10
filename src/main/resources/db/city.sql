# insert into country(id, name) values (0, 'Россия');
# insert into country(id, name) values (1, 'Украина');
# insert into country(id, name) values (2, 'Абхазия');
# insert into country(id, name) values (3, 'Австралия');
# insert into country(id, name) values (4, 'Австрия');
# insert into country(id, name) values (5, 'Азербайджан');
create table hibernate_sequence
(
    next_val bigint
) engine = MyISAM;
insert into hibernate_sequence
values (1);
insert into hibernate_sequence
values (1);
insert into hibernate_sequence
values (1);
insert into hibernate_sequence
values (1);


create table city
(
    id        bigint not null,
    name      varchar(255),
    region_id bigint,
    primary key (id)
) engine = MyISAM;

create table country
(
    id   bigint not null,
    name varchar(255),
    primary key (id)
) engine = MyISAM;
create table region
(
    id         bigint not null,
    name       varchar(255),
    country_id bigint,
    primary key (id)
) engine = MyISAM;
create table usr
(
    id                   bigint not null,
    active               bit    not null,
    date_of_registration datetime,
    email                varchar(255),
    gender               varchar(255),
    last_visit           datetime,
    locale               varchar(255),
    password             varchar(255),
    role                 integer,
    username             varchar(255),
    userpic              varchar(255),
    primary key (id)
) engine = MyISAM;
alter table city
    add constraint FKsi0dkm9kk6dyuedmc0j18t770 foreign key (region_id) references region (id)
alter table region
    add constraint FK7vb2cqcnkr9391hfn72louxkq foreign key (country_id) references country (id)