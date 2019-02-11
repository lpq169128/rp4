<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html style='height:99%'>
<head>
    <title>人员列表</title>
    <%--页面刷新--%>
    <%--<script type="text/javascript">--%>
    <%--_useLoadingMask = true;--%>
    <%--</script>--%>
    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <script type="text/javascript" src="${basePath}js/common/utils.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageList.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageModuleList.js?ver=${iVersion}"></script>
    <script src="${basePath}/js/pf/core/trainingPerson/trainingPersonList.js?ver=${iVersion}"></script>

    <script>
        $(document).ready(function () {
            PageList.init({
                datagrid: {
                    id: "tabList",//required
                    params: {
                        idField: "personId",//required
                        nameField: "businessNo",//required
                        url: basePath + "pf/trainingPerson/getList.do",//required,
                        columns: initColumns,//required
                        singleSelect: false,
                        onDblClickRow: function (rowIndex, rowData) {//双击行时间覆盖
                            // PageList.toShow(rowData);//显示一行数据的详情
                        }
                    }
                }
            });

        });

    </script>
    <style type="text/css">
    </style>
</head>
<body style="height: 100%;" class="bbox list-page">
<div class="toolbar-with-pager bbox">
    <div id="toolbar" class="btn-toolbar ">
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
    <table id="tabList" pager="#pagination" class="eu-datagrid">
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
        </div>
    </div>
</div>
<!--大表需要加下分页器-->
<div id="bottomPager" style="width:100%">
</div>
</body>
</html>