<%--
  Created by IntelliJ IDEA.
  User: HuangYong
  Date: 2018/10/23
  Time: 15:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" style="height:99%" xml:lang="en">

<head>
    <title>船员考核列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
    <script>_useLoadingMask = true;</script>
    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <script type="text/javascript" src="${basePath}/js/finance/common/eventTab.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageList.js?ver=${iVersion}"></script>

    <script>
        var shipType = ${params};
        var shipArr = [];
        var index = 0;
        for (var key in shipType) {
            shipArr[index] = [key, shipType[key]];
            index++;
        }
        //第一次打开页面
        var modelFlag = false;
        $(document).ready(function () {
            dataGrid = $("#mainGrid").iDatagrid("init", {
                pageSize: pageSize,//pageSize为全局变量，自动获取的
                url: basePath + "pf/personExamine/getList.do",	//basePath为全局变量，自动获取的
                singleSelect: true,
                onLoadSuccess: function (data) {
                    //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
                    if (data && data.total == 0) {
                        $("#noSearchResult").show();
                    } else {
                        $("#noSearchResult").hide();
                    }
                },
                onDblClickRow: function (rowIndex, rowData) {//双击单行跳转查看页面
                    var id = rowData.businessId;
                    var newId = "personExamine";
                    var pageName = "船员考核"
                    var pageUrl = basePath + "pf/personExamine/detailPage.do?mode=view&id=" + id;
                    var oldId = FW.getCurrentTabId();
                    addEventTab(newId + id, pageName, pageUrl, oldId);
                },
                columns: [[
                    {field: 'businessId', hidden: true},
                    {field: 'personId', hidden: true},
                    {field: 'name', title: '姓名', align: 'left', width: 100, fixed: true, sortable: true,},
                    {
                        field: 'sex', title: '性别', width: 80, fixed: true, sortable: true,
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
                    {
                        field: 'birthday', title: '出生日期', type: 'date', width: 115, sortable: true, fixed: true,
                        formatter: function (value) {
                            return FW.long2date(value);
                        }
                    },
                    {
                        field: 'shipId', title: '船名', align: 'left', width: 120, fixed: true, sortable: true,
                        formatter: function (value, row, index) {
                            return shipType[value];
                        },
                        "editor": {
                            "type": "combobox",
                            "options": {
                                "data": shipArr,
                            }
                        }
                    },
                    {
                        field: 'duties', title: '职务', align: 'left', width: 100, fixed: true, sortable: true,
                        formatter: function (value, row, index) {
                            return FW.getEnumMap("ATD_ DUTIES_TYPE")[value];
                            return value;
                        },
                        "editor": {
                            "type": "combobox",
                            "options": {
                                "data": FW.parseEnumData("ATD_ DUTIES_TYPE", _enum)
                            }
                        }
                    },
                    {
                        field: 'isFinishContract',
                        title: '完成合同期',
                        align: 'left',
                        width: 100,
                        fixed: true,
                        sortable: true,
                        //editor:{
                        //     "type" : "radio",
                        //     "options" : {
                        //         data:[["Y","是",true],["N","否"]]
                        //     }
                        // }
                        formatter: function (value, row, index) {
                            if (value == "是") {
                                return "是";
                            } else {
                                return "否";
                            }
                        }
                    },
                    {
                        field: 'testLevel', title: '考核结果', align: 'left', width: 120, fixed: true, sortable: true,
                        formatter: function (value, row, index) {
                            return FW.getEnumMap("ATD_EXAMINE_LEVEL")[value];
                        },
                        "editor": {
                            "type": "combobox",
                            "options": {
                                "data": FW.parseEnumData("ATD_EXAMINE_LEVEL", _enum)
                            }
                        }
                    },
                    {field: 'createUserName', title: '创建人', align: 'left', width: 140, fixed: true, sortable: true,hidden:true},
                    {
                        field: 'createdate',
                        title: '创建日期',
                        align: 'left',
                        width: 120,
                        fixed: true,
                        sortable: true,
                        hidden: true,
                        formatter: function (value) {
                            return FW.long2date(value);
                        }
                    },
                    {
                        field: 'wfStatus', title: '状态', align: 'left', width: 210, fixed: true, sortable: true,
                        // formatter: function (value, row, index) {
                        //     var flowStatus = row["flowStatus"];
                        //     var status = row["wfStatus"];
                        //     value = getStatus(status, flowStatus);
                        //     return value;
                        // }
                    },
                    {field: 'seize', title: '', width: 1}
                ]]

            });

            //表头搜索相关的
            $("#btn_advSearch").click(function () {
                if ($(this).hasClass("active")) {
                    $("#mainGrid").iDatagrid("endSearch");
                }
                else {

                    $("#mainGrid").iDatagrid("beginSearch", {
                        "remoteSearch": true, "onParseArgs": function (args) {
                            isSearchMode = true;
                            //强烈建议转为一个string传给后台，方便转Bean或hashMap
                            return {"search": FW.stringify(args)};
                        }
                    });
                }
            });
        });

        function refreshFinancePage() {
            dataGrid.datagrid('reload');
        }

        //流程状态
        //流程状态
        function getStatus(status, flowStatus) {
            // console.log(status + '---'+flowStatus)
            var value = "";
            if (status == "草稿") {
                value = "草稿";
            } else if (status == 'hangyunbushenpi') {
                value = "航运部经理审批";
            } else if (status == 'jiwubushenpi') {
                value = "机务部经理审核";
            } else if (status == 'chuanyuanbushenpi') {
                value = "船员部经理审核";
            } else if (status == 'fenguanlingdaoshenpi') {
                value = "分管领导审批";
            } else if (status == 'chuanyuanzhuguanshenpi') {
                value = "船员主管审核";
            } else if (status == 'zhidaochuanzhangshenpi') {
                value = "指导船长审核";
            } else if (status == 'zhidaolunjizhangshenpi') {
                value = "指导轮机长审核";
            } else if (status == "已归档") {
                value = "已归档";
            } else if (status == "作废") {
                value = "作废";
            } else {
                value = "审批中"
            }
            return value;
        }

        //新建跳转
        function addNewBidResultTab() {
            var url = basePath + "pf/personExamine/detailPage.do?mode=create";
            var newId = "personExamine";
            var pageName = "船员考核";
            var oldId = FW.getCurrentTabId();
            addEventTab(newId, pageName, url, oldId);
        }
    </script>
</head>
<body style="height: 100%; min-width:850px" class="bbox list-page">
<div id="main_content">
    <div class="bbox toolbar-with-pager" id="toolbar_wrap">
        <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
        <div id="toolbar1" class="btn-toolbar ">
            <div class="btn-group btn-group-sm" id="btnCreate">
                <button class="btn-success btn priv" privilege="VIRTUAL-NEW" onclick="addNewBidResultTab();">新建</button>
            </div>

            <div class="btn-group btn-group-sm">
                <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
            </div>

        </div>
        <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
        <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">
        </div>
    </div>

    <!--这里要清掉分页器的右浮动效果-->
    <div style="clear:both"></div>
    <div id="grid1_wrap" style="width:100%">
        <table id="mainGrid" pager="#pagination_1" class="eu-datagrid">

        </table>
    </div>
    <!-- 下页器部分-->
    <div id="bottomPager" style="width:100%;margin-top:6px">
    </div>
</div>
<div id="noSearchResult" style="display:none;width:100%;">
    <div style="height:100%;display:table;width:100%">
        <div style="display:table-cell;vertical-align:middle;text-align:center">
            <div style="font-size:14px">没有找到符合条件的结果</div>
        </div>
    </div>
</div>
<!-- 错误信息-->
<div class="row" id="grid1_error" style="display:none">
    无法从服务器获取数据，请检查网络是否正常
</div>
<!-- 无数据 -->
<div id="grid1_empty" style="display:none;width:100%;height:62%;">
    <div style="height:100%;display:table;width:100%">
        <div style="display:table-cell;vertical-align:middle;text-align:center">
            <div style="font-size:14px">没有考核信息</div>
            <div class="btn-group btn-group-pf margin-element">
                <button class="btn-success btn priv" privilege="VIRTUAL-NEW" onclick="addNewBidResultTab();">新建</button>
            </div>
            <%--<div class="btn-group btn-group-sm margin-element">--%>
            <%--<button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown" onclick="PageList.toCreate()">--%>
            <%--新建--%>
            <%--<span class="caret"></span> <!-- 添加"新建"文字右边的"下箭头" -->--%>
            <%--</button>--%>
            <%--<ul id="newButton" class="dropdown-menu"></ul>--%>
            <%--</div>--%>
        </div>
    </div>
</div>
</body>
</html>
