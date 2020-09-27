# 一个基于元数据驱动设计的精简实现(Metadata Driven Design)

### 功能点
- 自定义租户隔离的数据表及对其的增删改查
- 创建基于**取值表达式**和已有数据表的视图

取值表达式的例子，参考 **anna-relaxed-test** 模块
```Anna Query Language 
# 创建一个基于Student实体的number字段的视图，取名为Information
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
```
```shell script
# 查询实体Student实体的number字段为 "student_number_1"的Information视图
curl -v -H 'Content-Type: application/json' http://localhost:8080/metadata/def/table/select -d '
{
  "table": "information",
  "value": "student_number_1"
}'
```
```json
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
```
