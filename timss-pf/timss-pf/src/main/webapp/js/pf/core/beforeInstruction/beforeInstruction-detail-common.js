function getTableData(dataGrid, notRestore) {
    var result = [];
    if (dataGrid) {
        result = getDatagridData(dataGrid, notRestore);
    }
    return result;
}

function getDatagridData(dataGrid, notRestore) {
    var res = null;
    if (dataGrid) {
        var rows = dataGrid.datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            dataGrid.datagrid('endEdit', i);
        }
        res = dataGrid.datagrid('getRows');
        if (!notRestore) {
            for (var i = 0; i < rows.length; i++) {
                dataGrid.datagrid('beginEdit', i);
            }
        }
    }
    return res;
}

//设置表格权限
function convertDataGridEdit(dataGrid, addButton, gridWrapper) {
    var gridData = dataGrid.datagrid('getRows');
    if (gridData && gridData.length) {
        $(addButton).show();
        dataGrid.datagrid('showColumn', 'garbage-colunms');
        for (var i = 0; i < gridData.length; i++) {
            dataGrid.datagrid('beginEdit', i);
        }
    } else {
        $(addButton).show();
        $(gridWrapper).show();
        dataGrid.datagrid('resize');
        dataGrid.datagrid('showColumn', 'garbage-colunms');
    }

}

//设置为只读状态
function convertDataGridReadOnly(dataGrid, addButton, gridWrapper) {
    var gridData = dataGrid.datagrid('getRows');
    if (gridData && gridData.length) {
        $(addButton).hide();
        dataGrid.datagrid('hideColumn', 'garbage-colunms');
        for (var i = 0; i < gridData.length; i++) {
            dataGrid.datagrid('endEdit', i);
        }
    } else {
        $(gridWrapper).hide();
    }
}

function addTabTr(tabList) {
    // var dataGrid=initFamily.dataGrid;
    var dataGrid = $("#" + tabList);
    var row = {};
    dataGrid.datagrid("appendRow", row);
    var rowindex = dataGrid.datagrid('getRowIndex', row);
    dataGrid.datagrid('beginEdit', rowindex);
    // $("#addBut").html('继续添加');
}

//删除按钮
var garbageColunms = {
    field: 'garbage-colunms',
    title: '',
    width: 30,
    align: 'center',
    fixed: true,
    formatter: function (value, row, index) {
        return '<img src="' + basePath + 'img/supplier/btn_garbage.gif" title="删除当前行" width="16" height="16" >';
    }
};

//表格单行删除
function deleteGarbageColumnFunction(rowIndex, field, value, id, grid) {
    if (field == 'garbage-colunms') {
        var key = grid.datagrid('getRows')[rowIndex][id];
        deleteGridRow(grid, rowIndex, key);
    }
}

//删除行
function deleteGridRow(dataGrid, index, id) {
    dataGrid.datagrid('deleteRow', index);
}