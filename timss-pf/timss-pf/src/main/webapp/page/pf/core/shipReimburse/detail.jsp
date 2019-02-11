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

    <title>付款审批详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        _useLoadingMask = true;
    </script>
    <!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->

    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <script type="text/javascript" src="${basePath}js/common/pageItem.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageGrid.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageForm.js?ver=${iVersion}"></script>

    <%--<script type="text/javascript" src="${basePath}js/wf_new/workflow2.js?ver=${iVersion}"></script>--%>
    <script type="text/javascript" src='${basePath}/js/pf/core/shipReimburse/workflow2.js?ver=${iVersion}'></script>
    <script type="text/javascript" src="${basePath}js/common/utils.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageDetail3.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageMode.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageAttach.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageUIFix.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageModule.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/detailPageStyleFix.js?ver=${iVersion}"></script>
    <%--<script type="text/javascript" src='${basePath}/js/pf/core/common/DatePicker.js?ver=${iVersion}'></script>--%>
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

        /**
         * 在站点页面上重写方法或修改全局参数来对站点页面做个性化
         */
        $(document).ready(function () {
            PageDetail.init($.extend(true, PageModule.initParams(module
                , {
                    useOnChange: true,
                    isMainFormStoreInItem: false,
                    isGridFieldCodeWithFormCode: false,//列表的字段编码是否带表单编码
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
            if(mode!="create"){
                $("#mainForm").iForm("setVal",{reimburseDate:new Date(parseInt($("#mainForm").iForm("getVal").reimburseDate.replace(/-/g, "/"))).format("yyyy-MM")});
            }

            $("#f_reimburseDate").parent().iDate("init",{inputOpts: {Width: 135},datepickerOpts:{format: "yyyy-mm",startView:3,minView:3}});
            $("#f_reimburseDate").bind("focus",function () {
                this.blur();
            })
            $("#f_reimburseDate").next().hide();
        });

        // $("#f_reimburseDate").bind('input propertychange', function() {
        //     $("#mainForm").iForm("setVal",{reimburseDate:new Date(parseInt($("#mainForm").iForm("getVal").reimburseDate.replace(/-/g, "/"))).format("yyyy-MM")});
        // })

        //重写保存方法，费用明细不能为空。
        StyleFix.objs.btns.commit.onclick = function (target) {
            console.log("写保存方法，费用明细不能为空。。")
            var list = $("#itemFormDatagrid").datagrid("getRows");
            var reimburseDates=$("#mainForm").iForm("getVal").reimburseDate;
            reimburseDates+="-01";
            reimburseDates=reimburseDates.slice(0,10);
            $("#mainForm").iForm("setVal",{reimburseDate:reimburseDates});
            console.log(list)
            if (list.length < 1) {
                FW.error("费用明细不可为空!");
                return false;
            }
            StyleFix.toCommit(target);//兼容之前的代码，新加的按钮不需另写执行的方法
        }
        //从写审批提交方法。
        StyleFix.objs.btns.audit.onclick = function (target) {
            var list = $("#itemFormDatagrid").datagrid("getRows");
            var reimburseDates=$("#mainForm").iForm("getVal").reimburseDate;
            reimburseDates+="-01";
            reimburseDates=reimburseDates.slice(0,10);
            $("#mainForm").iForm("setVal",{reimburseDate:reimburseDates});
            var wfStatus = $("#mainForm").iForm("getVal").wfStatus;
            console.log(list);
            if (mode == "view" && wfStatus != "草稿") {
                if (list[0].repliedAmount != undefined) {
                    for (var i = 0; i < list.length; i++) {
                        var val = list[i].repliedAmount;
                        if (!val) {
                            FW.error("审核金额不可为空！");
                            return false;
                        }
                    }
                } else {
                    for (var i = 0; i < list.length; i++) {
                        var val = $("input[name='repliedAmount_" + i + "']").val();
                        // var val1 = $("input[name='attribute2_" + i + "']").val();
                        // $("#itemFormDatagrid").datagrid("updateRow",{index:i,row:{
                        //         repliedAmount:val,
                        //         attribute2:val1,
                        //     }});
                        if (!val) {
                            FW.error("审核金额不可为空！");
                            return false;
                        }
                    }
                }

            }
            StyleFix.toAudit(target);
        }

        function dateFormat(millionsTime, pattern){
            var d = new Date();
            d.setTime(millionsTime);
            var date = {
                "M+": d.getMonth() + 1,
                "d+": d.getDate(),
                "h+": d.getHours(),
                "m+": d.getMinutes(),
                "s+": d.getSeconds(),
                "q+": Math.floor((d.getMonth() + 3) / 3),
                "S+": d.getMilliseconds()
            };
            if (/(y+)/i.test(pattern)) {
                pattern = pattern.replace(RegExp.$1, (d.getFullYear() + '').substr(4 - RegExp.$1.length));
            }
            for (var k in date) {
                if (new RegExp("(" + k + ")").test(pattern)) {
                    pattern = pattern.replace(RegExp.$1, RegExp.$1.length == 1
                        ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
                }
            }
            return pattern;
        }
        function isEmptys(obj){
            return (obj == null || obj == "" || obj == "undefined")
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
    付款审批111
</div>
</body>
</html>
