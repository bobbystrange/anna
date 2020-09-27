select id, name, source
from column_def
where table_id = 2
order by id;
/*
+------+----------------------+---------------------------------------------------------------+
| id   | name                 | source                                                        |
+------+----------------------+---------------------------------------------------------------+
| 2001 | staff                | NULL                                                          |
| 2002 | comment              | NULL                                                          |
| 2003 | name                 | name                                                          |
| 2004 | meal_card_number     | MealCard.number                                               |
| 2005 | canteen_location     | MealCard.Canteen.location                                     |
| 2006 | agency_manager       | MealCard.Agency.manager                                       |
| 2007 | person_age           | Person.age                                                    |
| 2008 | suits_number         | Person.Suits.number                                           |
| 2009 | suit_color           | Person.Suits.Suit.color                                       |
| 2010 | maker_name           | Person.Suits.Suit.Maker.name                                  |
| 2011 | suit_maintenance_seq | Person.Suits.Suit.SuitMaintenanceRelation.Maintenance.seq     |
| 2012 | maintenance_standard | Room.Desk.Chair.ChairMaintenanceRelation.Maintenance.standard |
| 2013 | chair_supplier_name  | Room.Desk.Chair.Supplier.name                                 |
| 2014 | door_size            | Room.Door.size                                                |
| 2015 | door_supplier_name   | Room.Door.Supplier.name                                       |
+------+----------------------+---------------------------------------------------------------+

# Request:
curl -v -H 'Content-Type: application/json' http://localhost:8080/metadata/def/table/select -d '
{
  "table": "information",
  "value": "student_number_1"
}'

curl -v -H 'Content-Type: application/json' http://localhost:8080/metadata/def/table/insert -d '
{
    "name": "information",
    "columns": [
        {
            "name": "number",
            "value": "student_number_1"
        },
        {
            "name": "staff",
            "value": "bobby"
        }
    ]
}'

# Response:
{
    "code": 0,
    "data": {
        "columns": [
            {
                "name": "agency_manager",
                "value": [
                    "agency_1_1",
                    "agency_1_2",
                    "agency_1_3",
                    "agency_2_1",
                    "agency_2_2",
                    "agency_3"
                ]
            },
            {
                "name": "canteen_location",
                "value": [
                    "east",
                    "west"
                ]
            },
            {
                "name": "maintenance_standard",
                "value": [
                    "maintenance_standard_1",
                    "maintenance_standard_2",
                    "maintenance_standard_4",
                    "maintenance_standard_5",
                    "maintenance_standard_6",
                    "maintenance_standard_7"
                ]
            },
            {
                "name": "door_supplier_name",
                "value": "supplier_name_1"
            },
            {
                "name": "suits_number",
                "value": "suits-4"
            },
            {
                "name": "suit_color",
                "value": "white"
            },
            {
                "name": "staff",
                "value": null
            },
            {
                "name": "person_age",
                "value": 80
            },
            {
                "name": "suit_maintenance_seq",
                "value": [
                    "maintenance_seq_1",
                    "maintenance_seq_1",
                    "maintenance_seq_2",
                    "maintenance_seq_2",
                    "maintenance_seq_3",
                    "maintenance_seq_4"
                ]
            },
            {
                "name": "door_size",
                "value": "12x20"
            },
            {
                "name": "name",
                "value": "student_name_1"
            },
            {
                "name": "meal_card_number",
                "value": [
                    "meal_card_number_2",
                    "meal_card_number_3",
                    "meal_card_number_5"
                ]
            },
            {
                "name": "chair_supplier_name",
                "value": "supplier_name_1"
            },
            {
                "name": "comment",
                "value": null
            },
            {
                "name": "maker_name",
                "value": "maker_name_2"
            }
        ]
    },
    "success": true
}
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
