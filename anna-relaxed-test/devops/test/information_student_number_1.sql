select id, name, expression
from view_field_def
where view_id = 2
order by id;
/*
+----+----------------------------+----------------------------------------------------------------+
| id | name                       | expression                                                     |
+----+----------------------------+----------------------------------------------------------------+
|  1 | name                       | name                                                           |
|  2 | number                     | number                                                         |
|  3 | meal_card_number           | MealCard.number                                                |
|  4 | canteen_location           | MealCard.Canteen.location                                      |
|  5 | agency_manager             | MealCard.Agency.manager                                        |
|  6 | person_age                 | Person.age                                                     |
|  7 | suits_number               | Person.Suits.number                                            |
|  8 | suit_color                 | Person.Suits.Suit.color                                        |
|  9 | maker_name                 | Person.Suits.Suit.Maker.name                                   |
| 10 | suit_maintenance_seq       | Person.Suits.Suit.SuitMaintenanceRelation.Maintenance.seq      |
| 11 | suit_maintenance_standard  | Person.Suits.Suit.SuitMaintenanceRelation.Maintenance.standard |
| 12 | chair_maintenance_standard | Room.Desk.Chair.ChairMaintenanceRelation.Maintenance.standard  |
| 13 | chair_supplier_name        | Room.Desk.Chair.Supplier.name                                  |
| 14 | door_size                  | Room.Door.size                                                 |
| 15 | door_supplier_name         | Room.Door.Supplier.name                                        |
+----+----------------------------+----------------------------------------------------------------+
*/

-- name
select distinct name
from student
where number = 'student_number_1';

-- MealCard.number
select distinct number
from meal_card
where student_id in (
    select id
    from student
    where number = 'student_number_1'
);

-- MealCard.Canteen.location
select distinct location
from canteen
where id in (
    select canteen_id
    from meal_card
    where student_id in (
        select id
        from student
        where number = 'student_number_1'
    )
);

-- MealCard.Agency.manager
select distinct manager
from agency
where meal_card_type in (
    select type
    from meal_card
    where student_id in (
        select id
        from student
        where number = 'student_number_1'
    )
);

-- Person.age
select distinct age
from person
where code in (
    select person_code
    from student
    where number = 'student_number_1'
);

-- Person.Suits.number
select distinct number
from suits
where person_id in (
    select id
    from person
    where code in (
        select person_code
        from student
        where number = 'student_number_1'
    )
);
-- Person.Suit.color
select distinct color
from suit
where suits_id in (
    select id
    from suits
    where person_id in (
        select id
        from person
        where code in (
            select person_code
            from student
            where number = 'student_number_1'
        )
    )
);

-- Person.Suit.Maker.name
select distinct name
from maker
where id in (
    select maker_id
    from suit
    where suits_id in (
        select id
        from suits
        where person_id in (
            select id
            from person
            where code in (
                select person_code
                from student
                where number = 'student_number_1'
            )
        )
    )
);

-- Person.Suit.SuitMaintenanceRelation.Maintenance.seq
select distinct seq
from maintenance
where seq in (
    select `seq` as suit_maintenance_seq
    from suit_maintenance_relation
    where suit_id in (
        select id
        from suit
        where suits_id in (
            select id
            from suits
            where person_id in (
                select id
                from person
                where code in (
                    select person_code
                    from student
                    where number = 'student_number_1'
                )
            )
        )
    )
);

-- Room.Desk.Chair.ChairMaintenanceRelation.Maintenance.standard
select distinct standard
from maintenance
where id in (
    select maintenance_id
    from chair_maintenance_relation
    where chair_seq in (
        select seq
        from chair
        where desk_id in (
            select id
            from desk
            where room_name in (
                select name
                from room
                where id in (
                    select room_id
                    from student
                    where number = 'student_number_1'
                )
            )
        )
    )
);

-- Room.Desk.Chair.Supplier.name
select name
from supplier
where id in (
    select supplier_id
    from chair
    where desk_id in (
        select id
        from desk
        where room_name in (
            select name
            from room
            where id in (
                select room_id
                from student
                where number = 'student_number_1'
            )
        )
    )
);


-- Room.Door.code
select code
from door
where code in (
    select door_code
    from room
    where id in (
        select room_id
        from student
        where number = 'student_number_1'
    )
);

-- Room.Door.Supplier.name
select name
from supplier
where id in (
    select supplier_id
    from door
    where code in (
        select door_code
        from room
        where id in (
            select room_id
            from student
            where number = 'student_number_1'
        )
    )
);
