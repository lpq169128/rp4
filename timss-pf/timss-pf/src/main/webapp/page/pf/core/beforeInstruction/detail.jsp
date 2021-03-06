<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

    <title>开航前指令</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        _useLoadingMask = true;
    </script>
    <script>
        var id = "${id}";
        var session = "${params.sessionid}";
        var valKey = "${params.valKey}";
        var mode = "${mode}";
        var bean =${params.bean};
        var titleName = "${params.titleName}";
        var mgmtShipInfoMap = ${params.mgmtShipInfoMap};
        var trainingEffectList = ${params.TrainingEffectList};
        var businessAttachmentList = ${params.BusinessAttachmentList};
    </script>
    <!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <link rel="stylesheet" type="text/css" href="${basePath}css/common.css" media="all"/>

    <script src="${basePath}/js/pf/core/beforeInstruction/beforeInstruction-detail-common.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/utils.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageMode.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageDetail3.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/detailPageStyleFix.js?ver=${iVersion}"></script>
    <script src="${basePath}/js/pf/core/beforeInstruction/beforeInstruction-detail-form.js?ver=${iVersion}"></script>
    <script src="${basePath}/js/pf/core/beforeInstruction/beforeInstruction-detail-button.js?ver=${iVersion}"></script>
    <script src="${basePath}/js/pf/core/beforeInstruction/beforeInstruction-detail-priv.js?ver=${iVersion}"></script>

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
            //给姓名设置搜索输入框
            searchCiHint("nameId", "name", basePath + "pf/beforeInstruction/person.do");
        });

        //上下联 iHint 通过名称模糊搜索
        function searchCiHint(inputId, inputName, url) {
            var $firstPartyInput = $('#f_' + inputName);
            var firstPartyInit = {
                datasource: url,
                clickEvent: function (id, name) {
                    console.log("点击事件---")
                    $("#f_" + inputId).val(id);
                    var arr = name.split("/");
                    $firstPartyInput.val(arr[1]);
                }, maxItemCount: 100
            };

            $firstPartyInput.change(function () {
                console.log("input值改变了----")
                var val = $.trim($firstPartyInput.val());
                if (val == "") {
                    $("#f_" + inputId).val("");
                }
            });
            $firstPartyInput.iHint('init', firstPartyInit);
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
    <div class="btn-toolbar" role="toolbar" id="toolbar">
        <div class="btn-group btn-group-sm">
            <button type="button" class="btn btn-default" onclick="closeTab();">关闭</button>
        </div>
        <div class="btn-group btn-group-sm">
            <button type="button" class="btn btn-default priv" id="beforeInstruction-save"
                    privilege="virsual-beforeInstruction-save"
                    onclick="save($(this));">保存
            </button>
        </div>
        <div class="btn-group btn-group-sm">
            <button type="button" class="btn btn-default priv" id="beforeInstruction-edit"
                    privilege="virsual-beforeInstruction-edit"
                    onclick="editBean();">编辑
            </button>
        </div>
        <div class="btn-group btn-group-sm">
            <button type="button" class="btn btn-default priv" id="beforeInstruction-del"
                    privilege="virsual-beforeInstruction-del"
                    onclick="del();">删除
            </button>

        </div>

    </div>
</div>
<div style="clear:both"></div>

<div class="inner-title" id="pageTitle">
    开航前指令
</div>
<form id="beforeInstructionForm" class="margin-form-title margin-form-foldable"></form>

<div id="trainingEffectWrapper" class="margin-group-bottom">
    <form id="trainingEffectList" grouptitle="培训效果" class="margin-title-table">
        <table id="trainingEffectTable" class="eu-datagrid">
        </table>
    </form>
    <div class="btn-toolbar margin-foldable-button" id="addtrainingEffectDiv">
        <div class="btn-group btn-group-xs" id="addtrainingEffect">
            <button type="button" class="btn btn-success" onclick="addTabTr('trainingEffectTable')"
                    id="add-trainingEffect">添加培训效果
            </button>
        </div>
    </div>
</div>

<div id="attachFormWrapper" class="margin-group-bottom">
    <div id="attachFormList" grouptitle="附件">
        <form id="attachForm" class="margin-form-title margin-form-foldable"></form>
    </div>
</div>
</body>
</html>
