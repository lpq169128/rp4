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

    <title>船员考核</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        _useLoadingMask = true;
    </script>
    <!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->

    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <script type="text/javascript" src="${basePath}js/common/pageItem.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageGrid.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageForm.js?ver=${iVersion}"></script>

    <script type="text/javascript" src="${basePath}js/wf_new/workflow2.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/utils.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageDetail3.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageMode.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageAttach.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageUIFix.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageModule.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/detailPageStyleFix.js?ver=${iVersion}"></script>

    <script type="text/javascript" src='${basePath}/js/pf/core/crewApproval.js?ver=${iVersion}'></script>
    <script type="text/javascript">
        var page = ${params.page};
        var module = page.module;
        var mode = "${mode}";
        var id = "${id}";
        var bean =${params.bean};
        wfBean = bean;
        var sessId = "<%=sessId%>";
        var valKey = "<%=valKey%>";
        var taskId = wfBean.currentTaskId;
        var startDate = wfBean.startDate;
        var endDate = wfBean.endDate;


        console.log(module.formList)

        /**
         * 在站点页面上重写方法或修改全局参数来对站点页面做个性化
         */
        $(document).ready(function () {
            PageDetail.init($.extend(true, PageModule.initParams(module
                , {
                    useOnChange: true,
                    isMainFormStoreInItem: false
                }
            ), {
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
                },
                print: {
                    url: fineReportServicePath + "?reportlet=timss/attendance/management_cost/finance_apply/TIMSS2_FIN_FMA_001.cpt&format=pdf&__embed__=true" + "&id=" +
                        wfBean.businessId + "&siteid=ITC&proc_inst_id=" + wfBean.processInstId + "&author=" + ItcMvcService.user.getUserId(),//非空会覆盖id配置的url
                    id: "PXSQ_001",//指定id拼装打印url
                    format: "pdf"//打印的格式
                }
            }));
            $("#f_onJobDate").attr("placeholder","yyyy/MM/dd-yyyy/MM/dd");
            if (mode == "create") {
                $("#mainform").iForm("setVal", {sex: " ", nationality: "", shipId: " ", duties: " ", testLevel: "cz"});
            }
            if(mode != "create"){
                if($("#mainform").iForm("getVal").sex!=null){
                    $("#mainform").iForm("setVal",{sex:FW.getEnumMap("ATD_PERSON_SEX")[$("#mainform").iForm("getVal").sex]});
                }
            }

            // $("#mainform").iForm("hide", "description");
            $("#mainform").iForm("endEdit", "sex");
            $("#mainform").iForm("endEdit", "shipId");
            $("#mainform").iForm("endEdit", "duties");
            $("div[fieldid='description']").hide();
            searchCiHint("userId", "name");
        });

        //上下联 iHint 通过名称模糊搜索
        function searchCiHint(inputId, inputName) {
            var $firstPartyInput = $('#f_' + inputName);
            var url = basePath + "pf/personExamine/person.do";
            var firstPartyInit = {
                datasource: url,
                clickEvent: function (id, name) {
                    $("#f_" + inputId).val(id);
                    var userId = id;
                    var arr = name.split("/");
                    $firstPartyInput.val(arr[0]);
                    queryPersonInfo(userId);
                }, maxItemCount: 100
            };

            $firstPartyInput.change(function () {
                var val = $.trim($firstPartyInput.val());
                if (val == "") {
                    $("#f_" + inputId).val("");
                }
            });
            $firstPartyInput.iHint('init', firstPartyInit);
        }

        //通过userID加载人员信息
        function queryPersonInfo(id) {
            var url = basePath + "pf/personExamine/queryPersonInfoByUserId.do?userId=" + id;
            $.ajax({
                url: url,
                type: 'post',
                dataType: "json",
                success: function (rowData) {
                    console.log(rowData);
                    var data = {
                        "personId": rowData.businessId,
                        "sex": rowData.sex != null ? rowData.sex : "",
                        "birthday": rowData.birthday != null ? rowData.birthday : "",
                        "nationality": rowData.nationality != null ? rowData.nationality : "",
                        "shipId": rowData.shipId != null ? rowData.shipId : "",
                        "duties": rowData.duties != null ?rowData.duties: "",
                    };
                    $("#mainform").iForm("setVal", data);
                }
            });
        }


    </script>
    <style type="text/css">
        input::-webkit-input-placeholder { /* WebKit browsers */

            font-size:13px;

        }

        input:-moz-placeholder { /* Mozilla Firefox 4 to 18 */

            font-size:13px;

        }

        input::-moz-placeholder { /* Mozilla Firefox 19+ */

            font-size:13px;

        }

        input:-ms-input-placeholder { /* Internet Explorer 10+ */

            font-size:13px;

        }
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
        <div class="btn-group btn-group-sm " style="display: none">
            <button type="button" id="btn-printer" class="btn btn-default" onclick="PageDetail.toPrint()">打印</button>
        </div>

    </div>
</div>

<div class="inner-title" id="pageTitle">
    船员考核
</div>
</body>
</html>
