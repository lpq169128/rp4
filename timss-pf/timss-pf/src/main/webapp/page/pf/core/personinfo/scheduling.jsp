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
    <title>上船登记</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        _useLoadingMask = false;
        _dialogEmmbed = true;
    </script>
    <!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/utils.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageMode.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageDetail.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageDetailFix.js?ver=${iVersion}"></script>
    <style type="text/css">
        .datagrid-header-rownumber {
            width: 40px;
        }

        .datagrid-cell-rownumber {
            width: 40px;
        }
    </style>
    <script>
        var inship = ${params};
        var fields = [
            {
                title: "船舶", id: "shipId", type: "combobox", rules: {required: true},
                data: inship, wrapXsWidth: 12, wrapMdWidth: 12
            },
            {
                title: "接班时间",
                id: "successionDate",
                type: 'date',
                rules: {required: true},
                options: {"endDate": new Date()}, wrapXsWidth: 12, wrapMdWidth: 12
            },
        ];


        $(document).ready(function () {
            //初始化表单
            $("#baseform").iForm("init", {"fields": fields, "options": {validate: true}});
        });
    </script>
</head>
<body style="margin-top:10px;">

<form id="baseform">
</form>

</body>
</html>
