# A tiny implement for Metadata Driven Design

### Features
- Custom-defined data table and its CURD
- Data view includes expressions

> To see the expressions, please have a look at the module **anna-relaxed-test**

### Example
##### Steps
- execute SQL `anna-relaxed/devops/migarte/anna_relaxed.ddl.sql`
 and `anna-relaxed-test/devops/test/information.sql`
- execute the module **anna-relaxed-test**

##### Result
```shell script
curl -v -H 'Content-Type: application/json' http://localhost:8080/api/v1/view/query/map -d '
{
  "view": "information",
  "value": "student_number_1"
}'
```
```json
{
  "code": 0,
  "data": {
    "number": "student_number_1",
    "MealCard": [
      {
        "Canteen": {
          "location": "east"
        },
        "number": "meal_card_number_2",
        "Agency": [
          {
            "manager": "agency_2_1"
          },
          {
            "manager": "agency_2_2"
          }
        ]
      },
      {
        "Canteen": {
          "location": "west"
        },
        "number": "meal_card_number_3",
        "Agency": {
          "manager": "agency_3"
        }
      },
      {
        "Canteen": {
          "location": "west"
        },
        "number": "meal_card_number_5"
      }
    ],
    "name": "student_name_1",
    "Room": {
      "Desk": {
        "Chair": [
          {
            "ChairMaintenanceRelation": [
              {
                "Maintenance": {
                  "standard": "maintenance_standard_1"
                }
              },
              {
                "Maintenance": {
                  "standard": "maintenance_standard_2"
                }
              },
              {
                "Maintenance": {
                  "standard": "maintenance_standard_4"
                }
              },
              {
                "Maintenance": {
                  "standard": "maintenance_standard_6"
                }
              }
            ],
            "Supplier": {
              "name": "supplier_name_4"
            }
          },
          {
            "ChairMaintenanceRelation": [
              {
                "Maintenance": {
                  "standard": "maintenance_standard_5"
                }
              },
              {
                "Maintenance": {
                  "standard": "maintenance_standard_7"
                }
              }
            ],
            "Supplier": {
              "name": "supplier_name_4"
            }
          }
        ]
      },
      "Door": {
        "size": "12x20",
        "Supplier": {
          "name": "supplier_name_1"
        }
      }
    },
    "Person": {
      "Suits": {
        "number": "suits-4",
        "Suit": {
          "color": "white",
          "SuitMaintenanceRelation": [
            {
              "Maintenance": [
                {
                  "standard": "maintenance_standard_3",
                  "seq": "maintenance_seq_2"
                },
                {
                  "standard": "maintenance_standard_4",
                  "seq": "maintenance_seq_2"
                },
                {
                  "standard": "maintenance_standard_5",
                  "seq": "maintenance_seq_2"
                }
              ]
            },
            {
              "Maintenance": [
                {
                  "standard": "maintenance_standard_7",
                  "seq": "maintenance_seq_4"
                },
                {
                  "standard": "maintenance_standard_8",
                  "seq": "maintenance_seq_4"
                }
              ]
            },
            {},
            {}
          ],
          "Maker": {
            "name": "maker_name_2"
          }
        }
      },
      "age": 80
    }
  },
  "success": true
}
```
### Todo
- Anna Query Language 
```aql
create view Information from Student at number(
    name = $name,
    number = $number,
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
) where ($MealCard.number in ?
    and $MealCard.Agency.manager = ?
    and $Person.age between ? and ?
    and $Person.Suits.number <> ?) or length($Room.Door.code) > ?;
```
