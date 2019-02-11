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
    <title>聘用申请列表</title>
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
            //新建页面按钮的
            appendBtn();

            //初始化聘用申请列表页面
            dataGrid = $("#mainGrid").iDatagrid("init", {
                pageSize: pageSize,//pageSize为全局变量，自动获取的
                url: basePath + "pf/employmentApplication/getList.do",	//basePath为全局变量，自动获取的
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
                    var newId = "finEditFMATab" + id;
                    var pageName = FW.getEnumMap("ATD_SAILOR_TYPE")[rowData.recruitType];
                    var pageUrl = basePath + "pf/employmentApplication/detailPage.do?mode=view&id=" + id;
                    var oldId = FW.getCurrentTabId();
                    addEventTab(newId, pageName, pageUrl, oldId);
                },
                columns: [[
                    {field: 'businessId', hidden: true},
                    {
                        field: 'recruitType', title: '招聘对象类型', width: 120, align: 'left', fixed: true, sortable: true,
                        formatter: function (value, row, index) {
                            return FW.getEnumMap("ATD_SAILOR_TYPE")[value];
                        },
                        "editor": {
                            "type": "combobox",
                            "options": {
                                "data": FW.parseEnumData("ATD_SAILOR_TYPE", _enum)
                            }
                        }
                    },
                    {
                        field: 'shipId', title: '调往船舶', width: 120, align: 'left', fixed: true, sortable: true,
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
                    {field: 'remark', title: '原因', align: 'left', width: 105, sortable: true,},
                    {
                        field: 'inShipDate', title: '上船日期', width: 100, align: 'left', fixed: true, sortable: true,
                        formatter: function (value) {
                            return FW.long2date(value);
                        }
                    },
                    {field: 'createUserName', title: '创建人', width: 110, align: 'left', fixed: true, sortable: true,},
                    {
                        field: 'createdate', title: '创建时间', width: 100, align: 'left', fixed: true, sortable: true,
                        formatter: function (value) {
                            return FW.long2date(value);
                        }
                    },
                    {
                        field: 'wfStatus', title: '状态', width: 140, align: 'left', fixed: true, sortable: true,
                        // formatter: function (value, row, index) {
                        //     // console.log(value)
                        //     // console.log(row)
                        //     var flowStatus = row["flowStatus"];
                        //     var status = row["wfStatus"];
                        //     value = getStatus(status, flowStatus);
                        //     return value;
                        // }
                    }]]
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
        function getStatus(status, flowStatus) {
            // console.log(status + '---'+flowStatus)
            var value = "";
            if (status == "草稿") {
                value = "草稿";
            } else if (status == 'guideCaptain') {
                value = "指导船长审核";
            } else if (status == 'aircraftManager') {
                value = "机务部经理审核";
            } else if (status == 'ShippingManager') {
                value = "航运部经理审核";
            } else if (status == 'CrewManager') {
                value = "船员部经理审核";
            } else if (status == 'chargeLeadership') {
                value = "分管领导审批";
            } else if (status == 'crewDirector') {
                value = "船员主管审核";
            } else if (status == 'guideCaptain1') {
                value = "指导船长审核";
            } else if (status == 'guideChiefEngineer') {
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

        /**
         *动态添加新建按钮下的招聘对象类型
         */
        function appendBtn() {
            var applyTypeEnums = FW.getEnumMap("ATD_SAILOR_TYPE");
            for (var key in applyTypeEnums) {
                var appendHtml = '<li><a onclick=newFinApplyPage(' + "'" + key + "'" + ')>' + applyTypeEnums[key] + '</a></li>';
                $("#newCreateBut").append(appendHtml);
                $("#newButton").append(appendHtml);
            }
        }

        function newFinApplyPage(applyType) {
            var applyTypeEnums = FW.getEnumMap("ATD_SAILOR_TYPE");
            var newId = "finNewApplyTab" + applyType;
            var pageName = "";
            for (var key in applyTypeEnums) {
                if (key == applyType) {
                    pageName = pageName + applyTypeEnums[key];
                }
            }
            var pageUrl = basePath + "pf/employmentApplication/detailPage.do?mode=create&applyType=" + applyType;
            var oldId = FW.getCurrentTabId();
            addEventTab(newId, pageName, pageUrl, oldId);
        }
    </script>
</head>
<body style="height: 100%; min-width:850px" class="bbox list-page">
<div id="main_content">
    <div class="bbox toolbar-with-pager" id="toolbar_wrap">
        <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
        <div id="toolbar1" class="btn-toolbar ">
            <!-- <div class="btn-group btn-group-sm " >
                <button type="button" class="btn btn-success pms-privilege" id="pms-b-br-add" onclick="addNewBidResultTab();">新建</button>
            </div> -->
            <div class="btn-group btn-group-sm">
                <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
                    新建
                    <span class="caret"></span> <!-- 添加"新建"文字右边的"下箭头" -->
                </button>
                <ul id="newCreateBut" class="dropdown-menu"></ul>
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
            <div style="font-size:14px">没有申请信息</div>
            <!-- <div class="btn-group btn-group-sm margin-element">
                     <button type="button" class="btn btn-success" onclick="addNewBidResultTab();">新建1</button>
            </div> -->
            <div class="btn-group btn-group-sm margin-element">
                <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
                    新建
                    <span class="caret"></span> <!-- 添加"新建"文字右边的"下箭头" -->
                </button>
                <ul id="newButton" class="dropdown-menu"></ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>
