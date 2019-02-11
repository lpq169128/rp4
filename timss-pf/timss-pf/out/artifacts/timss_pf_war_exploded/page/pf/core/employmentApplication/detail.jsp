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

    <title>聘用申请</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        _useLoadingMask = false;
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

        var recruitType = ${params.applyType};
        var recruitVal = FW.getEnumMap("ATD_SAILOR_TYPE")[recruitType];
        var dutiesID = " ";

        //获取总吨、国际、公司
        function getShipVal(shipKey) {
            if (!shipKey) {
                return false;
            }
            $.ajax({
                url: "pf/employmentApplication/queryMgmtShipInfoById.do?shipId=" + shipKey,
                type: "post",
                complete: function (rowData) {
                    console.log(rowData);
                    var data = {
                        "nationality": rowData.responseJSON.nationality != null ? rowData.responseJSON.nationality : "",
                        "grossTon": rowData.responseJSON.grossTon != null ? rowData.responseJSON.grossTon : "",
                        "siteName": rowData.responseJSON.siteName != null ? rowData.responseJSON.siteName : "",
                    };
                    $('#mainForm').iForm('setVal', data);
                }

            })
        }

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
                    blankBeanSourceKey: "${param.createSource}",
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
                },

            }));

            var dutiesVal = $('#mainForm').iForm('getVal').duties;


            //如果是新建页面，可以设置对象类型，职务联动 和 性别的
            if (mode == "create") {
                $("#formFormitemForm").iForm("setVal", {sex: " "});
                $("#mainform").iForm("setVal", {grossTon:"",nationality:"",siteName:""});
            } else { //如果是编辑页面，获取页面的招聘对象类型值
                // recruitType = $('#mainForm').iForm('getVal').recruitType;
            }
            $("#formFormitemForm").iForm("endEdit", "sex");
            $("#mainForm").iForm("endEdit", "recruitType");
            //初始化招聘对象类型，给该对象增加onChange方法  设置职务的联动
            // $("#f_recruitType").iCombo('init', {
            //     data: FW.getEnumMap("ATD_SAILOR_TYPE"), onChange: function (val) {
            //         setDuties(val);
            //
            //         //初始化职务联动
            //         if(recruitType){
            //             recruitType = "";
            //             $('#mainForm').iForm('setVal', {duties: dutiesVal});
            //         }
            //     },
            // });


            //把列表页面 或者编辑页面带来的招聘类型对象，设置在表单里面
            if (mode == "create") {
                $('#mainForm').iForm('setVal', {recruitType: recruitType});
                if(recruitType=="cz"){
                    $('#mainForm').iForm('setVal', {duties: "cz"});
                }
            }

            //给人员名单的人员设置搜索输入框
            searchCiHint("userId", "#itemForm_name");
            //给交班人的人员设置搜索输入框
            handoverUserCiHint("userId", '#f_handoverUser');

        });

        //上下联 iHint 通过名称模糊搜索 人员
        function searchCiHint(inputId, inputName) {
            var $firstPartyInput = $(inputName);
            var url = basePath + "pf/employmentApplication/person.do";
            var firstPartyInit = {
                datasource: url,
                clickEvent: function (id, name) {
                    $("#f_" + inputId).val(id);
                    var userId = id;
                    var arr = name.split("/");
                    $firstPartyInput.val(arr[0]);

                    //通过选取的人员id获取详细信息
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
            var url = basePath + "pf/employmentApplication/queryPersonInfoByUserId.do?userId=" + id;
            $.ajax({
                url: url,
                type: 'post',
                dataType: "json",
                success: function (rowData) {
                    var data = {
                        "businessId": rowData.businessId,
                        // "userId": rowData.userId,
                        "sex": rowData.sex != null ? rowData.sex : "",
                        "idCard": rowData.idCard != null ? rowData.idCard : "",
                        "mobile": rowData.mobile != null ? rowData.mobile : "",
                        "graduateSchool": rowData.graduateSchool != null ? rowData.graduateSchool : "",
                        "highestEducation": rowData.highestEducation != null ? rowData.highestEducation : ""
                    };
                    $("#formFormitemForm").iForm("setVal", data);
                }
            });
        }

        //根据招聘对象类型，设置职务的联动
        function setDuties(recruitType) {
            var recruitVal = FW.getEnumMap("ATD_SAILOR_TYPE")[recruitType];

            //判断不同的对象类型，设置不同的职务选项
            if (recruitVal == "船长") {
                $('#f_duties').iCombo('loadData', FW.getEnumMap("ATD_CZ_DUTIES"));
                $("#mainform").iForm("setVal",{duties:"cz"})
            } else if (recruitVal == "普通船员") {
                $('#f_duties').iCombo('loadData', FW.getEnumMap("ATD_PTCY_DUTIES"));
            } else if (recruitVal == "高级船员") {
                $('#f_duties').iCombo('loadData', FW.getEnumMap("ATD_GJCY_DUTIES"));
            }
            if (mode == "create") {
                $('#mainForm').iForm("setVal", {duties: dutiesID});
            }
        }

        function changeDuties(dutiesKey) {
            dutiesID = dutiesKey;
        }

        //上下联 iHint 通过名称模糊搜索  交班人
        function handoverUserCiHint(inputId, inputName) {
            var $firstPartyInput = $(inputName);
            var url = basePath + "pf/employmentApplication/handoverUser.do";
            var firstPartyInit = {
                datasource: url,
                clickEvent: function (id, name) {
                    $("#f_" + inputId).val(id);
                    var userId = id;
                    var arr = name.split("/");
                    $firstPartyInput.val(arr[0]);

                    //通过选取的人员id获取详细信息
                    $("#mainForm").iForm("setVal", {handoverUser: arr[0]});
                }, maxItemCount: 100
            };

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
    出差申请111
</div>
<%--<form id="mainForm" class="margin-form-title margin-form-foldable">--%>

<%--</form>--%>
<%--<div class="margin-group"></div>--%>
<%--<div id="persoinDiv" class="margin-group-bottom" grouptitle="人员名单">--%>
<%--<form id="persoinForm"  class="margin-form-title margin-form-foldable">--%>
<%--</form>--%>
<%--</div>--%>
<%--<!-- 附件 -->--%>
<%--<div id="attachDiv" grouptitle="附件">--%>
<%--<form id="attachForm" style="width:100%">--%>
<%--</form>--%>
<%--</div>--%>
<%--<button type="button" class="audit-btn" id="btn-commit" onclick="test(this)">确定</button>--%>
</body>
</html>
