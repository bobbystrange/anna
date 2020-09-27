use anna_relaxed;

/*
Student<number, name, person_code, room_id>
MealCard<student_id, number, type, canteen_id>
Canteen<location, level>
Agency<meal_card_type, manager>
Person<code, age, gender>
Suits<person_id, number>
Suit<person_id, suits_id, maker_id, price, color>
Maker<name, title>
Room<name, area, door_code>
Desk<room_name, height, supplier_id>
Chair<desk_id, seq, weight, supplier_id>
Maintenance<seq, standard>
SuitMaintenanceRelation<suit_id, maintenance_seq>
ChairMaintenanceRelation<chair_seq, maintenance_id>
Supplier<name>
Door<code, size, supplier_id>


                           Student
                /          /                            \
    MealCard          Person                           Room
    /    \             /                            /        \
Canteen  Agency     Suits                         Desk         Door
                    /                           /      \           \
                Suit                           Chair     Supplier   Supplier
            /     \                             |    \
        Maker SuitMaintenanceRelation           |     \
                       \       ChairMaintenanceRelation Supplier
                        \        /
                        Maintenance

create view Information from Student at number(
    staff,
    comment,
    name = $name,
    meal_card_number = $MealCard.number,
    canteen_location = $MealCard.Canteen.location,
    agency_manager = $MealCard.Agency.manager,
    person_age = $Person.age,
    suits_number = $Person.Suits.number,
    suit_color = $Person.Suits.Suit.color,
    maker_name = $Person.Suits.Suit.Maker.name,
    suit_maintenance_seq = $Person.Suits.Suit.SuitMaintenanceRelation.Maintenance.seq,
    chair_maintenance_standard = $Room.Desk.Chair.ChairMaintenanceRelation.Maintenance.standard,
    chair_supplier_name = $Room.Desk.Chair.Supplier.name,
    door_size = $Room.Door.size,
    door_supplier_name = $Room.Door.Supplier.name,
);
*/
select *
from `table_def`;
select id, name, source
from `column_def`
where table_id = 2;

-- information
insert into `table_def`(id, tenant_id, name, display_name, source_table, source_column)
values (2, 0, 'information', 'Information of Student', 'student', 'number');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag)
values (2000, 0, 2, 'number', 'column staff', 1, 7);
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag)
values (2001, 0, 2, 'staff', 'column staff', 1, 0);
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag)
values (2002, 0, 2, 'comment', 'column comment', 1, 0);
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2003, 0, 2, 'name', 'column name', 0, 0, 'name');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2004, 0, 2, 'meal_card_number', 'column meal card number', 0, 0, 'MealCard.number');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2005, 0, 2, 'canteen_location', 'column canteen location', 0, 0, 'MealCard.Canteen.location');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2006, 0, 2, 'agency_manager', 'column agency manager', 0, 0, 'MealCard.Agency.manager');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2007, 0, 2, 'person_age', 'column person age', 0, 0, 'Person.age');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2008, 0, 2, 'suits_number', 'column suits number', 0, 0, 'Person.Suits.number');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2009, 0, 2, 'suit_color', 'column suit color', 0, 0, 'Person.Suits.Suit.color');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2010, 0, 2, 'maker_name', 'column maker name', 0, 0, 'Person.Suits.Suit.Maker.name');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2011, 0, 2, 'suit_maintenance_seq', 'column suit maintenance seq', 0, 0,
        'Person.Suits.Suit.SuitMaintenanceRelation.Maintenance.seq');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2012, 0, 2, 'chair_maintenance_standard', 'column maintenance standard', 0, 0,
        'Room.Desk.Chair.ChairMaintenanceRelation.Maintenance.standard');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2013, 0, 2, 'chair_supplier_name', 'column chair supplier name', 0, 0, 'Room.Desk.Chair.Supplier.name');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2014, 0, 2, 'door_size', 'column door size', 0, 0, 'Room.Door.size');
insert into `column_def`(id, tenant_id, table_id, name, display_name, type, flag, source)
values (2015, 0, 2, 'door_supplier_name', 'column door supplier name', 0, 0, 'Room.Door.Supplier.name');

-- other tables
select *
from student;
select *
from meal_card;
select *
from canteen;
select *
from agency;
select *
from person;
select *
from suits;
select *
from suit;
select *
from maker;
select *
from desk;
select *
from chair;
select *
from maintenance;
select *
from supplier;
select *
from door;

-- Student
create table student (
    id          bigint unsigned primary key,
    number      varchar(255),
    name        varchar(255),
    person_code varchar(255),
    room_id     bigint unsigned
);
insert into student
values (1, 'student_number_1', 'student_name_1', 'person_code_3', 2);
insert into student
values (2, 'student_number_2', 'student_name_2', 'person_code_1', 3);
insert into student
values (3, 'student_number_3', 'student_name_3', 'person_code_2', 1);

-- MealCard
create table meal_card (
    id         bigint unsigned primary key,
    number     varchar(255),
    type       smallint unsigned,
    student_id bigint unsigned,
    canteen_id bigint unsigned
);
insert into meal_card
values (1, 'meal_card_number_1', 3, 2, 3);
insert into meal_card
values (2, 'meal_card_number_2', 2, 1, 1);
insert into meal_card
values (3, 'meal_card_number_3', 1, 1, 2);
insert into meal_card
values (4, 'meal_card_number_4', 2, 3, 1);
insert into meal_card
values (5, 'meal_card_number_5', 3, 1, 2);
insert into meal_card
values (6, 'meal_card_number_6', 3, 2, 4);

-- Canteen
create table canteen (
    id       bigint unsigned primary key,
    location varchar(255),
    level    smallint unsigned
);
insert into canteen
values (1, 'east', 1);
insert into canteen
values (2, 'west', 2);
insert into canteen
values (3, 'north', 3);
insert into canteen
values (4, 'south', 4);

-- Agency
create table agency (
    id             bigint unsigned primary key,
    meal_card_type smallint unsigned,
    manager        varchar(255)
);
insert into agency
values (1, 1, 'agency_1_1');
insert into agency
values (2, 1, 'agency_1_2');
insert into agency
values (3, 1, 'agency_1_3');
insert into agency
values (4, 2, 'agency_2_1');
insert into agency
values (5, 2, 'agency_2_2');
insert into agency
values (6, 3, 'agency_3');

-- Person
create table person (
    id     bigint unsigned primary key,
    code   varchar(255),
    age    smallint unsigned,
    gender tinyint unsigned
);
insert into person
values (1, 'person_code_1', 6, 0);
insert into person
values (2, 'person_code_2', 24, 1);
insert into person
values (3, 'person_code_3', 80, 0);

-- Suits
create table suits (
    id        bigint unsigned primary key,
    person_id bigint unsigned,
    number    varchar(255)
);
insert into suits
values (1, 1, 'suits-1');
insert into suits
values (2, 1, 'suits-2');
insert into suits
values (3, 2, 'suits-3');
insert into suits
values (4, 3, 'suits-4');
insert into suits
values (5, 2, 'suits-5');
insert into suits
values (6, 2, 'suits-6');

-- Suits
create table suit (
    id        bigint unsigned primary key,
    suits_id  bigint unsigned,
    person_id bigint unsigned,
    maker_id  bigint unsigned,
    price     bigint unsigned,
    color     varchar(255)
);
insert into suit
values (1, 1, 1, 1, 100, 'red');
insert into suit
values (2, 1, 1, 2, 50, 'green');
insert into suit
values (3, 2, 1, 2, 10, 'black');
insert into suit
values (4, 2, 1, 1, 75, 'green');
insert into suit
values (5, 2, 1, 3, 110, 'red');
insert into suit
values (6, 2, 1, 2, 130, 'red');
insert into suit
values (7, 3, 2, 2, 680, 'white');
insert into suit
values (8, 3, 2, 3, 15, 'blue');
insert into suit
values (9, 4, 3, 2, 36, 'white');
insert into suit
values (10, 5, 2, 3, 1600, 'blue');
insert into suit
values (11, 5, 2, 1, 300, 'grey');
insert into suit
values (12, 6, 2, 1, 250, 'yellow');

/**
Maintenance<seq, standard>
SuitMaintenanceRelation<suit_id, maintenance_seq>
 */
create table suit_maintenance_relation (
    id              bigint unsigned primary key auto_increment,
    suit_id         bigint unsigned,
    maintenance_seq varchar(255)
);
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (1, 'maintenance_seq_1');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (1, 'maintenance_seq_2');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (1, 'maintenance_seq_4');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (1, 'maintenance_seq_6');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (1, 'maintenance_seq_8');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (2, 'maintenance_seq_2');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (2, 'maintenance_seq_3');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (2, 'maintenance_seq_4');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (2, 'maintenance_seq_6');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (3, 'maintenance_seq_3');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (3, 'maintenance_seq_5');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (3, 'maintenance_seq_6');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (3, 'maintenance_seq_7');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (4, 'maintenance_seq_5');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (4, 'maintenance_seq_6');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (4, 'maintenance_seq_8');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (5, 'maintenance_seq_6');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (5, 'maintenance_seq_7');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (5, 'maintenance_seq_8');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (6, 'maintenance_seq_7');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (7, 'maintenance_seq_3');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (7, 'maintenance_seq_5');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (7, 'maintenance_seq_7');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (7, 'maintenance_seq_8');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (8, 'maintenance_seq_1');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (8, 'maintenance_seq_3');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (8, 'maintenance_seq_6');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (8, 'maintenance_seq_8');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (9, 'maintenance_seq_2');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (9, 'maintenance_seq_4');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (9, 'maintenance_seq_6');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (9, 'maintenance_seq_8');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (10, 'maintenance_seq_1');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (10, 'maintenance_seq_3');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (10, 'maintenance_seq_5');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (10, 'maintenance_seq_7');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (11, 'maintenance_seq_4');
insert into suit_maintenance_relation(suit_id, maintenance_seq)
values (12, 'maintenance_seq_1');

-- Maker
create table maker (
    id    bigint unsigned primary key,
    name  varchar(255),
    title varchar(255)
);
insert into maker
values (1, 'maker_name_1', 'maker_title_1');
insert into maker
values (2, 'maker_name_2', 'maker_title_2');
insert into maker
values (3, 'maker_name_3', 'maker_title_3');

-- Room
create table room (
    id        bigint unsigned primary key,
    name      varchar(255),
    area      int unsigned,
    door_code varchar(255)
);
insert into room
values (1, 'room_name_1', 16, 'door_code_1');
insert into room
values (2, 'room_name_2', 98, 'door_code_2');
insert into room
values (3, 'room_name_3', 55, 'door_code_3');

-- Desk
create table desk (
    id          bigint unsigned primary key,
    room_name   varchar(255),
    height      int unsigned,
    supplier_id bigint unsigned
);
insert into desk
values (1, 'room_name_1', 180, 1);
insert into desk
values (2, 'room_name_1', 175, 2);
insert into desk
values (3, 'room_name_1', 165, 2);
insert into desk
values (4, 'room_name_2', 120, 2);
insert into desk
values (5, 'room_name_3', 110, 3);
insert into desk
values (6, 'room_name_3', 125, 3);
insert into desk
values (7, 'room_name_3', 115, 4);
insert into desk
values (8, 'room_name_3', 105, 4);

-- Chair
create table chair (
    id          bigint unsigned primary key,
    desk_id     bigint unsigned,
    seq         varchar(255),
    weight      int unsigned,
    supplier_id bigint unsigned
);
insert into chair
values (1, 1, 'chair_seq_1', 30, 1);
insert into chair
values (2, 1, 'chair_seq_1', 10, 2);
insert into chair
values (3, 1, 'chair_seq_2', 20, 3);
insert into chair
values (4, 2, 'chair_seq_2', 100, 3);
insert into chair
values (5, 2, 'chair_seq_2', 70, 3);
insert into chair
values (6, 3, 'chair_seq_3', 150, 2);
insert into chair
values (7, 4, 'chair_seq_1', 110, 4);
insert into chair
values (8, 4, 'chair_seq_4', 120, 4);
insert into chair
values (9, 5, 'chair_seq_3', 70, 4);
insert into chair
values (10, 6, 'chair_seq_1', 90, 2);
insert into chair
values (11, 7, 'chair_seq_2', 10, 1);
insert into chair
values (12, 7, 'chair_seq_4', 340, 1);
insert into chair
values (13, 7, 'chair_seq_3', 160, 2);
insert into chair
values (14, 8, 'chair_seq_3', 710, 1);
insert into chair
values (15, 3, 'chair_seq_1', 330, 3);
insert into chair
values (16, 5, 'chair_seq_4', 900, 4);
insert into chair
values (17, 1, 'chair_seq_2', 200, 4);

-- ChairMaintenanceRelation
create table chair_maintenance_relation (
    id             bigint unsigned primary key,
    chair_seq      varchar(255),
    maintenance_id bigint unsigned
);
insert into chair_maintenance_relation
values (1, 'chair_seq_1', 1);
insert into chair_maintenance_relation
values (2, 'chair_seq_1', 2);
insert into chair_maintenance_relation
values (3, 'chair_seq_1', 4);
insert into chair_maintenance_relation
values (4, 'chair_seq_1', 6);
insert into chair_maintenance_relation
values (5, 'chair_seq_2', 1);
insert into chair_maintenance_relation
values (6, 'chair_seq_2', 3);
insert into chair_maintenance_relation
values (7, 'chair_seq_2', 4);
insert into chair_maintenance_relation
values (8, 'chair_seq_3', 1);
insert into chair_maintenance_relation
values (9, 'chair_seq_3', 2);
insert into chair_maintenance_relation
values (10, 'chair_seq_3', 5);
insert into chair_maintenance_relation
values (11, 'chair_seq_3', 7);
insert into chair_maintenance_relation
values (12, 'chair_seq_3', 8);
insert into chair_maintenance_relation
values (13, 'chair_seq_4', 5);
insert into chair_maintenance_relation
values (14, 'chair_seq_4', 7);

-- Maintenance
create table maintenance (
    id       bigint unsigned primary key,
    seq      varchar(255),
    standard varchar(255)
);
insert into maintenance
values (1, 'maintenance_seq_1', 'maintenance_standard_1');
insert into maintenance
values (2, 'maintenance_seq_1', 'maintenance_standard_2');
insert into maintenance
values (3, 'maintenance_seq_2', 'maintenance_standard_3');
insert into maintenance
values (4, 'maintenance_seq_2', 'maintenance_standard_4');
insert into maintenance
values (5, 'maintenance_seq_2', 'maintenance_standard_5');
insert into maintenance
values (6, 'maintenance_seq_3', 'maintenance_standard_6');
insert into maintenance
values (7, 'maintenance_seq_4', 'maintenance_standard_7');
insert into maintenance
values (8, 'maintenance_seq_4', 'maintenance_standard_8');

-- Supplier
create table supplier (
    id   bigint unsigned primary key,
    name varchar(255)
);
insert into supplier
values (1, 'supplier_name_1');
insert into supplier
values (2, 'supplier_name_2');
insert into supplier
values (3, 'supplier_name_3');
insert into supplier
values (4, 'supplier_name_4');


-- Door
create table door (
    id          bigint unsigned primary key,
    code        varchar(255),
    supplier_id bigint unsigned,
    size        varchar(255)
);
insert into door
values (1, 'door_code_1', 2, '16x16');
insert into door
values (2, 'door_code_2', 1, '12x20');
insert into door
values (3, 'door_code_3', 4, '15x18');
