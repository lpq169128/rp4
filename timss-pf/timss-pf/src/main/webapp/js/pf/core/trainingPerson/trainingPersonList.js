var initColumns = [[
    {field: 'isCheck', title: '', checkbox: true, width: 10,},
    {field: 'personId', title: '人员ID', hidden: true},
    {field: 'name', title: '姓名', width: 150},
    {
        field: 'sex', title: '性别', width: 80,
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_PERSON_SEX")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("ATD_PERSON_SEX", _enum)
            }
        }
    },
    {field: 'idCard', title: '身份证', width: 100,},
    {field: 'mobile', title: '个人电话', width: 80,},
    {field: 'graduateSchool', title: '毕业院校', width: 80,},
    {field: 'highestEducation', title: '文化程度', width: 80,}
]];