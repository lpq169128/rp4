<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="com.yudean.itc.util.FileUploadUtil" %>
<%@page import="com.yudean.itc.util.Constant" %>
<%@page import="com.yudean.itc.dto.sec.SecureUser" %>
<%
    SecureUser operator = (SecureUser) session.getAttribute(Constant.secUser);
    String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
    String sessId = request.getSession().getId();
    String mode = (String) request.getAttribute("mode");
%>
<!DOCTYPE html>
<html>
<head>
    <title>工作日志</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        _useLoadingMask = false;
    </script>
    <!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/utils.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageMode.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageDetail.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageDetailFix.js?ver=${iVersion}"></script>
    <%--<script type="text/javascript" src="${basePath}js/common/pageDetail3.js?ver=${iVersion}"></script>--%>
    <style type="text/css">
        .datagrid-header-rownumber {
            width: 40px;
        }

        .datagrid-cell-rownumber {
            width: 40px;
        }
    </style>
    <script>
        // var g = {users: {}};
        var fields = [
            {
                title: "任务",
                id: "mission",
                type: "textarea",
                linebreak: true,
                wrapXsWidth: 12,
                wrapMdWidth: 12,
                rules: {required: true,maxlength: 400},
            },
            {id: "sucUser", type: "hidden"},//落实人员id
            {
                title: "落实人", id: "successionDate", wrapXsWidth: 12, wrapMdWidth: 12,
                options: {
                    disableEdit: true,
                    icon: "itcui_btn_mag",
                    onClickIcon: function (val) {
                        getSucUser("baseform");
                    },
                },

            },
        ];


        $(document).ready(function () {
            //初始化表单
            $("#baseform").iForm("init", {"fields": fields, "options": {validate: true}});
            $("#f_successionDate").parent().click(function () {
                getSucUser("baseform");
            })
        });

        //落实人点击事件，点击打开选人树
        function getSucUser(formId) {
            setSucUserForDialog(formId);
            FW.dialog("init", {
                "src": basePath + "page/pf/core/workPlan/select_role_user.jsp",
                "dlgOpts": {width: 700, height: 300, title: "落实人", idSuffix: "sucUser"},
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
                            FW.getFrame(FW.getCurrentTabId()).g.users = conWin.getChecked();
                            var g = FW.getFrame(FW.getCurrentTabId()).g;

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
                            saveSucUserForDialogToFrom(formId);
                            _parent().$("#itcDlgsucUser").dialog("close");
                        }
                    }
                ]
            });
        }

        //为选人弹出框初始化准备数据
        function setSucUserForDialog(formId) {
            var data = $('#' + formId).iForm('getVal');

            var participantIds = "";
            var participantName = "";
            if (typeof(data.sucUser) != "undefined" && data.sucUser != null && data.sucUser != "") {
                participantIds = data.sucUser.split(",");
                participantName = data.successionDate.split(",");
            }
            var schedulePerson = {};
            if (participantIds.length > 0) {
                for (var i = 0; i < participantIds.length; i++) {
                    schedulePerson[participantIds[i]] = participantName[i];
                }
            }
            FW.getFrame(FW.getCurrentTabId()).g.users = schedulePerson;
        }

        //保存修改的落实人
        function saveSucUserForDialogToFrom(formId) {
            var g = FW.getFrame(FW.getCurrentTabId()).g;
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
            $("#" + formId).iForm("setVal", {"successionDate": strName, "sucUser": strId});
        }

    </script>
</head>
<body style="margin-top:10px;">

<form id="baseform">
</form>

</body>
</html>
