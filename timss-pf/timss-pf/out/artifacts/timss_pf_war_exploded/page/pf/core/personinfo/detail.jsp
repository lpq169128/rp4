<%--<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.yudean.itc.util.FileUploadUtil" %>
<%@page import="com.yudean.itc.util.Constant" %>
<%@page import="com.yudean.itc.dto.sec.SecureUser" %>
<%
    SecureUser operator = (SecureUser) session.getAttribute(Constant.secUser);
    String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
    String sessId = request.getSession().getId();
//	String mode = (String)request.getAttribute("mode");
//	String pmType = (String)request.getAttribute("pmType");
%>

<!DOCTYPE html>
<html>
<head>

    <title>人员档案详情</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        _useLoadingMask = true;
    </script>
    <script>
        var id = "${id}";
        var session = "${params.sessionid}";
        var valKey = "${params.valKey}";
        // var loginUserId = ItcMvcService.user.getUserId();
        var mode = "${mode}";
        var bean =${params.bean};
        var personRelations = ${params.PersonRelationList};
        var personCertificateList = ${params.PersonCertificateList};
        var personcredentialsList = ${params.PersonCredentialsList};
        var personexperinenceList = ${params.PersonExperinenceList};
        var personvoyageList = ${params.PersonVoyageList};
        var businessAttachmentList = ${params.BusinessAttachmentList};
        var hireInfoList = ${params.hireInfoList};
        var personExamineList = ${params.personExamineList};
        var trainingInfoList = ${params.trainingInfoList};

        var mgmtShipMap = ${params.mgmtShipMap};

    </script>
    <!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <link rel="stylesheet" type="text/css" href="${basePath}css/common.css" media="all"/>
    <script src="${basePath}/js/pf/core/personinfo/jQuery.Hz2Py-min.js?ver=${iVersion}"></script>
    <script src="${basePath}/js/pf/core/personinfo/personinfo-detail-common.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/utils.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageMode.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageDetail3.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/detailPageStyleFix.js?ver=${iVersion}"></script>
    <script src="${basePath}/js/pf/core/personinfo/personinfo-detail-form.js?ver=${iVersion}"></script>
    <script src="${basePath}/js/pf/core/personinfo/personinfo-detail-button.js?ver=${iVersion}"></script>
    <script src="${basePath}/js/pf/core/personinfo/personinfo-detail-priv.js?ver=${iVersion}"></script>


    <script type="text/javascript">
        /**
         //
         //在站点页面上重写方法或修改全局参数来对站点页面做个性化
         //
         **/
        PageDetail.fixStyle = function () {//新样式修正
            if (typeof(StyleFix) == "object") {//如果引入了新样式修正
                StyleFix.refresh({
                    selector: {},
                    bean: PageDetail.objs.form.bean,
                    module: {withWorkflow: "N"}
                });
            }
        }
        $(document).ready(function () {
            PageDetail.init($.extend(true, initParams, {
                mode: mode,//默认，可选view/create/edit
                form: {
                    beanId: id,
                    bean: bean,
                },
                withWorkFlow: false,
                withAttach: false,
            }));
            initPager();
            initPriv();

            //设置拼音的联动事件
            $("#f_name").on("change", function (val) {
                $("#personinfoForm").iForm("setVal", {pinyin: $(this).toPinyin()})
            })

        });


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
    <div class="btn-toolbar" role="toolbar" id="toolbar">
        <div class="btn-group btn-group-sm">
            <button type="button" class="btn btn-default" onclick="closeTab();">关闭</button>
        </div>
        <div class="btn-group btn-group-sm">
            <button type="button" class="btn btn-default priv" id="personinfo-save" privilege="virsual-personinfo-save"
                    onclick="save($(this));">保存
            </button>
        </div>
        <div class="btn-group btn-group-sm">
            <button type="button" class="btn btn-default priv" id="personinfo-edit" privilege="virsual-personinfo-edit"
                    onclick="editBean();">编辑
            </button>
        </div>
        <div class="btn-group btn-group-sm">
            <button type="button" class="btn btn-default priv" id="personinfo-del" privilege="virsual-personinfo-del"
                    onclick="del();">删除
            </button>

        </div>

    </div>
</div>
<div style="clear:both"></div>

<div class="inner-title" id="pageTitle">
    人员档案
</div>
<form id="personinfoForm" class="margin-form-title margin-form-foldable"></form>
<div id="personinfoDetailList" grouptitle="详细信息">
    <form id="personinfoDetailForm" class="autoform">

    </form>
</div>

<div id="familyWrapper" class="margin-group-bottom">
    <form id="familyList" grouptitle="家庭成员" class="margin-title-table">
        <table id="familyTable" class="eu-datagrid">
        </table>
    </form>
    <div class="btn-toolbar margin-foldable-button" id="addfamilyDiv">
        <div class="btn-group btn-group-xs" id="addfamily">
            <button type="button" class="btn btn-success" onclick="addTabTr('familyTable')" id="add-family">添加家庭成员
            </button>
        </div>
    </div>
</div>

<div id="certificatesWrapper" class="margin-group-bottom">
    <form id="certificatesList" grouptitle="证件" class="margin-title-table">
        <table id="certificatesTable" class="eu-datagrid">
        </table>
    </form>
    <div class="btn-toolbar margin-foldable-button" id="certificatesDiv">
        <div class="btn-group btn-group-xs" id="addcertificates">
            <button type="button" class="btn btn-success" onclick="addTabTr('certificatesTable');"
                    id="add-certificates">添加证件
            </button>
        </div>
    </div>
</div>

<div id="credentialsWrapper" class="margin-group-bottom">
    <form id="credentialsList" grouptitle="证书" class="margin-title-table">
        <table id="credentialsTable" class="eu-datagrid">
        </table>
    </form>
    <div class="btn-toolbar margin-foldable-button" id="credentialsDiv">
        <div class="btn-group btn-group-xs" id="addcredentials">
            <button type="button" class="btn btn-success" onclick="addTabTr('credentialsTable');" id="add-credentials">
                添加证书
            </button>
        </div>
    </div>
</div>

<div id="experinenceWrapper" class="margin-group-bottom">
    <form id="experinenceList" grouptitle="工作经历" class="margin-title-table">
        <table id="experinenceTable" class="eu-datagrid">
        </table>
    </form>
    <div class="btn-toolbar margin-foldable-button" id="experinenceDiv">
        <div class="btn-group btn-group-xs" id="addexperinence">
            <button type="button" class="btn btn-success" onclick="addTabTr('experinenceTable');" id="add-experinence">
                添加工作经历
            </button>
        </div>
    </div>
</div>

<div id="voyageWrapper" class="margin-group-bottom">
    <form id="voyageList" grouptitle="近五年海上经历" class="margin-title-table">
        <table id="voyageTable" class="eu-datagrid">
        </table>
    </form>
    <div class="btn-toolbar margin-foldable-button" id="voyageDiv">
        <div class="btn-group btn-group-xs" id="addvoyage">
            <button type="button" class="btn btn-success" onclick="addTabTr('voyageTable');" id="add-voyage">添加近五年海上经历
            </button>
        </div>
    </div>
</div>

<div id="examineWrapper" class="margin-group-bottom">
    <form id="examineList" grouptitle="考核记录" class="margin-title-table">
        <table id="examineTable" class="eu-datagrid">
        </table>
    </form>
</div>

<div id="trainingWrapper" class="margin-group-bottom">
    <form id="trainingList" grouptitle="培训记录" class="margin-title-table">
        <table id="trainingTable" class="eu-datagrid">
        </table>
    </form>
</div>

<div id="hireinfoWrapper" class="margin-group-bottom">
    <form id="hireinfoList" grouptitle="聘用记录" class="margin-title-table">
        <table id="hireinfoTable" class="eu-datagrid">
        </table>
    </form>
</div>

<div id="attachFormWrapper" class="margin-group-bottom">
    <div id="attachFormList" grouptitle="附件">
        <form id="attachForm" class="margin-form-title margin-form-foldable"></form>
    </div>
</div>
</body>
</html>
