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

    <title>moduleData详情</title>

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
            if (mode == "create") {
                $("#mainForm").iForm("setVal", {week: " ", weather: " "})
            }

            if (mode != "create") {
                userResponsibilityID = $("#mainForm").iForm("getVal").depart;
                userBranchID = $("#mainForm").iForm("getVal").subDepart;
                userTeamID = $("#mainForm").iForm("getVal").team;
            }

            $("#f_partiPerson").parent().click(function () {
                var personList = {partiPerson: "", partiPersonUser: "",}
                getSucUser("mainForm", "partiPerson", "partiPersonUser", personList);
            })
            $("#f_absentee").parent().click(function () {
                var personList = {absentee: "", absenteeUser: "",}
                getSucUser("mainForm", "absentee", "absenteeUser", personList);
            })

            //给暂存按钮增加点击事件，更改默认值。
            $("button[data-action='save']").on("click", function () {
                if (wfStatus == "草稿") {
                    userResponsibilityID = $("#mainForm").iForm("getVal").depart;
                    userBranchID = $("#mainForm").iForm("getVal").subDepart;
                    userTeamID = $("#mainForm").iForm("getVal").team;
                }
            })

        });

        //落实人点击事件，点击打开选人树
        function getSucUser(formId, personField, personID, personList) {
            setSucUserForDialog(formId, personField, personID);
            FW.dialog("init", {
                "src": basePath + "page/pf/core/common/select_role_user.jsp",
                "dlgOpts": {width: 700, height: 300, title: "新增人员", idSuffix: "sucUser"},
                "btnOpts": [
                    {
                        "name": "关闭", "onclick": function () {
                            return true;
                        }
                    },
                    {
                        "name": "确定",
                        "style": "btn-success",
                        "onclick": function () {
                            var conWin = _parent().window.document.getElementById("itcDlgsucUserContent").contentWindow;
                            g.users = conWin.getChecked();

                            var count = 0;
                            for (var i in g.users) {
                                if (g.users[i] != null) {
                                    count += 1;
                                }
                            }
                            if (count > 20) {
                                FW.error("最多只能选择20人!");
                                return;
                            }
                            saveSucUserForDialogToFrom(formId, personField, personID, personList);
                            _parent().$("#itcDlgsucUser").dialog("close");
                        }
                    }
                ]
            });
        }

        //为选人弹出框初始化准备数据
        function setSucUserForDialog(formId, personField, personID) {
            var data = $('#' + formId).iForm('getVal');

            var participantIds = "";
            var participantName = "";
            if (typeof(data[personID]) != "undefined" && data[personID] != null && data[personID] != "") {
                participantIds = data[personID].split(",");
                participantName = data[personField].split(",");
            }
            var schedulePerson = {};
            if (participantIds.length > 0) {
                for (var i = 0; i < participantIds.length; i++) {
                    schedulePerson[participantIds[i]] = participantName[i];
                }
            }
            g.users = schedulePerson;
        }

        //保存修改的落实人
        function saveSucUserForDialogToFrom(formId, personField, personID, personList) {
            var strId = "";
            var strName = "";
            for (var k in g.users) {
                strId += k + ","
                strName += g.users[k] + ",";
            }
            if (strId == "") {
                strId = "";
                strName = "";
            } else {
                strId = strId.substring(0, strId.length - 1);
                strName = strName.substring(0, strName.length - 1);
            }
            personList[personField] = strName;
            personList[personID] = strId;

            $("#" + formId).iForm("setVal", personList);
        }

        //重写保存方法，计划任务不能为空。
        StyleFix.objs.btns.commit.onclick = function (target) {
            if (wfStatus == "草稿") {
                userResponsibilityID = $("#mainForm").iForm("getVal").depart;
                userBranchID = $("#mainForm").iForm("getVal").subDepart;
                userTeamID = $("#mainForm").iForm("getVal").team;
            }
            StyleFix.toCommit(target);//兼容之前的代码，新加的按钮不需另写执行的方法
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
</html>
