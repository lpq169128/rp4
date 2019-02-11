<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html style='height:99%'>
<head>
    <title>付款审批列表</title>
    <script type="text/javascript">
        _useLoadingMask = true;
    </script>
    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <script type="text/javascript" src="${basePath}js/common/utils.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageList.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageModuleList.js?ver=${iVersion}"></script>


    <script>
        var shipJson = ${params};

        var shipArr = [];
        var index = 0;
        for (var key in shipJson) {
            shipArr[index] = [key, shipJson[key]];
            index++;
        }

        var initColumns = [[
            {field: 'businessId', hidden: true},
            {
                field: 'reimburseName', title: '报销单名称', width: 200, align: 'left', sortable: true
            },
            {
                field: 'shipId', title: '船舶', width: 100, align: 'left', sortable: true, fixed: true,
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
            {
                field: 'reimburseDate', title: '发生月份', width: 100, align: 'left', fixed: true, sortable: true,
                formatter: function (value) {
                    return  new Date(value).format("yyyy-MM");
                }
            },
            {
                field: 'createUserName', title: '经办人', width: 140, align: 'left', sortable: true, fixed: true,
            },
            {
                field: 'createdate', title: '创建时间', width: 100, align: 'left', fixed: true, sortable: true,
                formatter: function (value) {
                    return FW.long2date(value);
                }
            },
            {
                field: 'wfStatus', title: '状态', width: 210, align: 'left', sortable: true, fixed: true,
            },

        ]];

        $(document).ready(function () {
            PageList.init($.extend(true, {
                datagrid: {
                    id: "moduleDataList",//required
                    detailPage: {
                        url: basePath + "pf/shipReimburse/detailPage.do?mode=view&id=",//required
                        createUrl: basePath + "pf/shipReimburse/detailPage.do?mode=create&id=",//required
                        idPrefix: "businessId",//详情页面id前缀，后面加_详情项id
                        namePrefix: "付款审批"//详情页面名称前缀，后面加详情项名称
                    },
                    params: {
                        idField: "businessId",//required
                        url: basePath + "pf/shipReimburse/getList.do",//required
                        columns: initColumns,//required
                    }
                }
            }));

        });

    </script>
    <style type="text/css">
    </style>
</head>
<body style="height: 100%;" class="bbox list-page">
<div class="toolbar-with-pager bbox">
    <div id="toolbar" class="btn-toolbar ">
        <div class="btn-group btn-group-sm" id="btnCreate">
            <button class="btn-success btn priv" privilege="VIRTUAL-NEW" onclick="PageList.toCreate()">新建</button>
        </div>
        <div class="btn-group btn-group-sm" id="btnSearch">
            <button onclick="PageList.toShowSearchLine()" class="btn btn-default">查询</button>
        </div>
    </div>
    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager">
    </div>
</div>

<!--这里要清掉分页器的右浮动效果-->
<div style="clear:both"></div>
<div id="grid_wrap" style="width:100%">
    <table id="moduleDataList" pager="#pagination" class="eu-datagrid">
    </table>
</div>
<div id="noColumns" style="display:none;width:100%;height:62%">
    <div style="height:100%;display:table;width:100%">
        <div style="display:table-cell;vertical-align:middle;text-align:center">
            <div style="font-size:14px"><span class="business-type-name"></span>没有可显示的字段</div>
        </div>
    </div>
</div>
<div id="noSearchResult" style="display:none;width:100%;">
    <div style="height:100%;display:table;width:100%">
        <div style="display:table-cell;vertical-align:middle;text-align:center">
            <div style="font-size:14px">没有找到符合条件的结果<span class="business-type-name"></span></div>
        </div>
    </div>
</div>
<!-- 无数据 -->
<div id="grid_empty" style="display:none;width:100%;height:62%">
    <div style="height:100%;display:table;width:100%">
        <div style="display:table-cell;vertical-align:middle;text-align:center">
            <div style="font-size:14px">没有查询到相关数据</div>
            <div class="btn-group btn-group-sm margin-element">
                <button class="btn-success btn priv" privilege="VIRTUAL-NEW" onclick="PageList.toCreate()">新建</button>
            </div>
        </div>
    </div>
</div>
<!--大表需要加下分页器-->
<div id="bottomPager" style="width:100%">
</div>
</body>
</html>