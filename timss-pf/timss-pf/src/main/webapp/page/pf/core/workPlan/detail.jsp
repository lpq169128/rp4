<%@page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="com.yudean.itc.util.FileUploadUtil" %>
<%@page import="com.yudean.itc.util.Constant" %>
<%@page import="com.yudean.itc.dto.sec.SecureUser" %>
<%
    SecureUser operator = (SecureUser) session.getAttribute(Constant.secUser);
    String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
    String sessId = request.getSession().getId();
%>
<!DOCTYPE html>
<html>
<head>

    <title>工作计划详情</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        _useLoadingMask = true;
    </script>
    <!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <link rel="stylesheet" type="text/css" href="${basePath}css/common.css" media="all"/>
    <script type="text/javascript" src="${basePath}js/wf_new/workflow2.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/utils.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageItem.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageGrid.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageForm.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageDetail3.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageMode.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageAttach.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageUIFix.js?ver=${iVersion}"></script>
    <script type="text/javascript" src='${basePath}js/common/pageModule.js?ver=${iVersion}'></script>
    <script type="text/javascript" src="${basePath}js/common/detailPageStyleFix.js?ver=${iVersion}"></script>

    <script type="text/javascript" src='${basePath}/js/pf/core/crewApproval.js?ver=${iVersion}'></script>

    <script type="text/javascript">
        var g = {users: {}};
        var moduleCode = "${moduleCode}";
        var page =${params.page};
        var module = page.module;
        var mode = "${mode}";
        var id = "${id}";
        var bean =${params.bean};
        wfBean = bean;
        var sessId = "<%=sessId%>";
        var valKey = "<%=valKey%>";

        //获取默认部门和下拉选项值
        var totalDep = ${params.totalDep};
        var responsibilityMap = totalDep.responsibilityMap;
        var branchMap = totalDep.branchMap;
        var teamMap = totalDep.teamMap;

        var depart = ${params.defaultMap};
        var userResponsibilityID = depart.userResponsibilityMap ? depart.userResponsibilityMap : "";
        var userBranchID = depart.userBranchMap ? depart.userBranchMap : "";
        var userTeamID = depart.userTeamMap ? depart.userTeamMap : "";
        var changeBranchMap = "";
        var changeTeamMap = "";

        var planNameVal = userTeamID != "" ? teamMap[userBranchID][userTeamID] + "组"
            : (userBranchID != "" ? branchMap[userResponsibilityID][userBranchID]
                : (userResponsibilityID != "" ? responsibilityMap[userResponsibilityID] : ""));

        //获取当前年月
        var year = new Date().getFullYear();
        var month = new Date().getMonth() + 1;
        if (month == 12) {
            year += 1;
            month = 1;
        }
        var planName = year + "年" + month + "月" + planNameVal + "工作计划";

        var wfStatus = "";

        /**
         //
         //在站点页面上重写方法或修改全局参数来对站点页面做个性化
         //
         **/
        $(document).ready(function () {
            PageDetail.init($.extend(true, PageModule.initParams(module), {
                page: page,
                mode: mode,//默认，可选view/create/edit
                form: {
                    beanId: id,
                    bean: bean,
                    blankBeanSourceKey: "${param.createSource}"
                },
                attach: {
                    sessId: sessId,
                    valKey: valKey
                }
            }));

            wfStatus = $("#mainForm").iForm("getVal").wfStatus;
            if (mode != "create") {
                userResponsibilityID = $("#mainForm").iForm("getVal").depart;
                userBranchID = $("#mainForm").iForm("getVal").subDepart;
                userTeamID = $("#mainForm").iForm("getVal").team;
            }

            //给暂存按钮增加点击事件，更改默认值。
            $("button[data-action='save']").on("click", function () {
                if (wfStatus == "草稿") {
                    userResponsibilityID = $("#mainForm").iForm("getVal").depart;
                    userBranchID = $("#mainForm").iForm("getVal").subDepart;
                    userTeamID = $("#mainForm").iForm("getVal").team;
                }
            })

        });
        //重写表格行删除方法，给表格排序
        PageGrid.prototype.toDelRow = function (rowIndex, field, value) {
            var self = this;
            var name = self.opts.namePrefix + (self.obj.datagrid("getRows")[rowIndex][self.opts.nameField] || "");
            FW.confirm("删除？<br/>确定删除该" + name + "吗？该操作无法撤销。", function () {
                self.obj.datagrid("deleteRow", rowIndex);
                self.refreshAddBtn();

                //删除某一行后，修改行的排序
                var list = $("#itemFormDatagrid").datagrid("getRows");
                $.each(list, function (i, listRow) {
                    $("#itemFormDatagrid").datagrid('updateRow', {index: i, row: {itemForm_num: i + 1}});
                })
            });

        }

        //重写表格添加行，给弹出框
        PageDetail.toAddItem = function () {
            FW.dialog("init", {
                "src": basePath + "pf/workPlan/addPlanMission.do",
                "dlgOpts": {width: 700, height: 230, title: "新增计划任务", idSuffix: "addPlanMission"},
                "btnOpts": [
                    {
                        "name": "关闭",
                        "onclick": function () {
                            return true;
                        }
                    },
                    {
                        "name": "确定",
                        "style": "btn-success",
                        "onclick": function () {
                            var conWin = _parent().window.document.getElementById("itcDlgaddPlanMissionContent").contentWindow;
                            if (!conWin.$("#baseform").valid()) {
                                return false;
                            }
                            var formData = conWin.$("#baseform").iForm("getVal");//获取主表单数据

                            var row = {
                                itemForm_num: $("#itemFormDatagrid").datagrid("getRows").length + 1,
                                itemForm_mission: formData.mission,
                                itemForm_person: formData.successionDate,
                            };

                            //把子页面获取到的值放入到表格中
                            var tab = $("#itemFormDatagrid").datagrid("appendRow", row);
                            //获取当前值的行数，并给当前值设为编辑状态，如果过不需要可以删除。
                            // var rowindex = $("#itemFormDatagrid").datagrid('getRowIndex', row);
                            // $("#itemFormDatagrid").datagrid('beginEdit', rowindex);

                            _parent().$("#itcDlgaddPlanMission").dialog("close");
                        }
                    }
                ]
            });
        }

        //重写保存方法，计划任务不能为空。
        StyleFix.objs.btns.commit.onclick = function (target) {
            var list = $("#itemFormDatagrid").datagrid("getRows");
            if (list.length < 1) {
                FW.error("计划任务不可为空!");
                return false;
            }
            if (wfStatus == "草稿") {
                userResponsibilityID = $("#mainForm").iForm("getVal").depart;
                userBranchID = $("#mainForm").iForm("getVal").subDepart;
                userTeamID = $("#mainForm").iForm("getVal").team;
            }
            StyleFix.toCommit(target);//兼容之前的代码，新加的按钮不需另写执行的方法
        }

        //给页面设置初始值的方法，在模板配置过，这里没有用，留作后面相似功能复制用的
        function setVals() {
            if (mode == "create") {
                $("#mainForm").iForm("setVal", {
                    year: year, month: month, depart: userResponsibilityID, planName: planName
                });
            }
        }

        //在年、月的值变更配置的，变更计划名称的，在模板配置过，这里没有用，留作后面相似功能复制用的
        function setPlanNameYear(val) {
            year = val;
            planName = year + "年" + month + "月" + planNameVal + "工作计划";
            $("#mainForm").iForm("setVal", {planName: planName});
        }

        function setPlanName(val) {
            month = val;
            planName = year + "年" + month + "月" + planNameVal + "工作计划";
            $("#mainForm").iForm("setVal", {planName: planName});
        }

        //责任部门初始化sql，在模板配置过，这里没有，留作后面相似功能复制用的
        <%--select k,v from(--%>
        <%--select r.ORG_CODE k,r."NAME" v from SEC_ORGANIZATION r--%>
        <%--WHERE r.PARENT_CODE= (SELECT S.ORG_CODE FROM  SEC_SITE_ORGANIZATION S where S.SITE_ID='${siteid}')--%>
        <%--)--%>

        //在责任部门，分部，组织的值变更配置的，设置分部的，在模板配置过，这里没有用，留作后面相似功能复制用的
        function setResponsibility(resKey) {
            if (mode == "create") {
                $("#mainForm").iForm("setVal", {subDepart: userBranchID})
            }
            if (mode == "create" || wfStatus == "草稿") {
                $("#mainForm").iForm("beginEdit", "team");
                $("#mainForm").iForm("beginEdit", "subDepart");
            }
            $('#f_subDepart').iCombo('loadData', branchMap[resKey]);
            changeBranchMap = branchMap[resKey];
            if (resKey != userResponsibilityID) {
                FW.error("你选择的不是自己所属部门");
            }
            if ($.isEmptyObject(branchMap[resKey])) {
                $('#f_team').iCombo('loadData', {});
                $("#mainForm").iForm("endEdit", "team");
                $("#mainForm").iForm("endEdit", "subDepart");
                //变更计划名称的，有需要写，没需要可以删除。
                planNameVal = responsibilityMap[resKey] ? responsibilityMap[resKey] : "";
                planName = year + "年" + month + "月" + planNameVal + "工作计划";
                $("#mainForm").iForm("setVal", {planName: planName});
            }
        }

        function setDepart(branchKey) {
            if (mode == "create") {
                $("#mainForm").iForm("setVal", {team: userTeamID})
            }
            if (mode == "create" || wfStatus == "草稿") {
                $("#mainForm").iForm("beginEdit", "team");
            }
            $('#f_team').iCombo('loadData', teamMap[branchKey]);
            changeTeamMap = teamMap[branchKey];
            if (branchKey != userBranchID) {
                FW.error("你选择的不是自己所属分部");
            }
            if ($.isEmptyObject(teamMap[branchKey])) {
                $('#f_team').iCombo('loadData', {});
                $("#mainForm").iForm("endEdit", "team");
            }
            //变更计划名称的，有需要写，没需要可以删除。
            if(branchKey){
                planNameVal = changeBranchMap[branchKey];
                planName = year + "年" + month + "月" + planNameVal + "工作计划";
                $("#mainForm").iForm("setVal", {planName: planName});
            }
        }

        function setTeam(teamKey) {
            if (teamKey != userTeamID) {
                FW.error("你选择的不是自己所属班组");
            }
            //变更计划名称的，有需要写，没需要可以删除。
            if(teamKey){
                planNameVal = changeTeamMap[teamKey];
                planName = year + "年" + month + "月" + planNameVal + "工作计划";
                $("#mainForm").iForm("setVal", {planName: planName});
            }
        }

    </script>
    <style type="text/css">
        .btn-garbage {
            cursor: pointer;
        }

        html {
            height: 95%
        }

        body {
            height: 100%
        }
    </style>
</head>

<body>
<div class="toolbar-with-pager bbox">
    <div class="btn-toolbar" role="toolbar" id="buttonToolBar">
        <div class="btn-group btn-group-sm">
            <button type="button" class="btn btn-default" id="closeButtonDiv" onclick="closeTab()">关闭</button>
        </div>
        <div class="btn-group btn-group-sm">
            <button type="button" class="btn-default btn" id="btnPrint" onclick="PageDetail.toPrint()">打印</button>
        </div>
    </div>
</div>
<!-- <div style="clear:both"></div> -->

<div class="inner-title" id="pageTitle">
    此处写页标题
</div>

</body>
<script type="text/javascript">

</script>
</html>
