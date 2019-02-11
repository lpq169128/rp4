var initColumns = [[
    // {field: 'isCheck', title: '', checkbox: true, width: 10,},
    {field: 'trainingId', title: '培训ID', hidden: true},
    {
        field: 'trainingType', title: '培训类型', width: 130, fixed: true, sortable: true,
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_TRAINING_TYPE")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("ATD_TRAINING_TYPE", _enum)
            }
        }
    },
    {
        field: 'deptShip', title: '船名', width: 120, fixed: true, align: 'left', sortable: true,
        formatter: function (value) {
            return shipJson[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": shipArr
            }
        }
    },
    {field: 'duties', title: '职务', width: 100, fixed: true, sortable: true,},
    {field: 'name', title: '姓名', width: 100, fixed: true, sortable: true,},
    {
        field: 'dutiesDate', title: '任职日期', width: 100, fixed: true, sortable: true, formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {field: 'createUserName', title: '创建人', width: 140, fixed: true, sortable: true,},
    {
        field: 'createdate', title: '创建日期', width: 100, fixed: true, sortable: true, formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {field: 'seize', title: '', width: 1,}
]];