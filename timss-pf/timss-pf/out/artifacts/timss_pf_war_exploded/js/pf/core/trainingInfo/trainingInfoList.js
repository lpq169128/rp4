var initColumns = [[
    // {field: 'isCheck', title: '', checkbox: true, width: 10,},
    {field: 'trainingId', title: '培训ID', hidden: true},
    {field: 'trainingName', title: '培训名称', width: 150, sortable: true},
    {field: 'handler', title: '负责人', width: 90, fixed: true, sortable: true},
    {
        field: 'startDate', title: '培训日期', width: 176, fixed: true, sortable: true, formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {field: 'address', title: '地点', width: 160, fixed: true, sortable: true},
    {field: 'hour', title: '课时', width: 90, fixed: true, sortable: true},
    {
        field: 'createdate', title: '创建日期', width: 176, fixed: true, sortable: true, formatter: function (value) {
            return FW.long2date(value);
        }
    }
]];