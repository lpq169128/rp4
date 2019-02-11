var isResetPage = true;//是否把页面重置为新的详情页
var isChangeAll = false;//是否使用了统一加班时间，updateRows方法会使得getRows("update")拿不到被修改的行，提交时，把所有行都放到updateList中
var HireInfo = {
    objs: {
        formFields: [],
        fileFields: [],
        datagridFields: [],
        taskId: "",//任务Id
        processInstId: "",//流程实例ID
        applyFlag: "",//审批标志
        overtimeId: "",//加班单id
        status: "",//加班单状态
        userId: "",//当前登录人
        applicant: {},//申请人信息
        // flowDiagram:"atd_?_overtime",
        flowDiagram: "com.gdyd.itc.bps.tm.pysq",
        siteId: "",
        sessId: "",
        valKey: "",
        isPrint: false,//有打印的按钮
        isFileEndEdit: false,//文件编辑是否需要endEdit
        isShowAuditBtn: false,//是否在审批框中显示审批按钮
        isChangeSaved: true//修改是否已提交完成
    },
    loadData: function (data) {
        if (data) {
            HireInfo.mountData(data);
        } else {
            if (HireInfo.isMode("add")) {//如果是新建，加载新建的数据
                $("#autoform").iForm("setVal", {
                    userName: HireInfo.objs.applicant.userName + " / " + HireInfo.objs.applicant.deptName,
                    deptId: HireInfo.objs.applicant.deptId,
                    createBy: HireInfo.objs.applicant.userId
                });
                HireInfo.setOvertimeDatagrid(null);
                HireInfo.refreshModeShow();
                HireInfo.setFile(null);
            } else {//否则获取加载加班单的数据
                HireInfo.getData(HireInfo.objs.overtimeId);
            }
        }
    },
//装载数据
    mountData: function (data) {
        //任务ID
        HireInfo.objs.taskId = data.taskId;
        //审批状态
        HireInfo.objs.applyFlag = data.applyFlag;
        //当前登录人
        HireInfo.objs.userId = data.userId;

        var overtimeData = data.rowData;
        HireInfo.objs.processInstId = overtimeData.instantId;
        HireInfo.objs.overtimeId = overtimeData.id;
        HireInfo.objs.status = overtimeData.status;
        HireInfo.objs.applicant = {
            userId: overtimeData.createBy,
            userName: overtimeData.userName,
            deptId: overtimeData.deptId,
            deptName: overtimeData.deptName,
            siteId: HireInfo.objs.siteId
        }
        var formData = {
            "id": overtimeData.id,
            "userName": overtimeData.userName + " / " + overtimeData.deptName,
            deptId: HireInfo.objs.applicant.deptId,
            createBy: HireInfo.objs.applicant.userId,
            num: overtimeData.num,
            "overTimeReason": overtimeData.overTimeReason,
            "createDay": overtimeData.createDay
        };
        $("#autoform").iForm("setVal", formData);

        HireInfo.setOvertimeDatagrid(overtimeData.itemList);
        HireInfo.refreshModeShow();
        HireInfo.changeDatagridDefaultVal();
        HireInfo.getFile(overtimeData.id);
        StyleFix.checkIsCommited = function () {
            return overtimeData.status != "草稿";
        };
        if (overtimeData.status == "草稿") {//草稿状态的单，默认是当前审批人
            StyleFix.checkHasAuditPermisson = function () {
                return true;
            };
        }
        ;
        if (isResetPage && null != overtimeData.businessId) {
            isResetPage = false;
            StyleFix.init({
                selector: {
                    btnCommit: "#commitButton",//!*原来的提交按钮
                    btnAudit: "#approveBtn",//!*原来的审批按钮
                    btnRollback: "#approveBtn"//!*原来的退回按钮
                },
                getBeanFromQueryDetail: true,
                bean: overtimeData
            });
        }
    },
}

function submit(_this) {
    buttonLoading(_this);
    // var url=basePath+'sm/employmentApplication/insertApplyAndStartWorkflow.do'
    var dataform = $("#form1").iForm('getVal');
    var details = $("#fmaList").iForm('getVal');
    var fileIds = $("#attachForm").iForm("getVal").attach;
    var businessData = {};
    $.fn.extend(true, businessData, dataform, details);
    var data = {
        atdhireinfo: FW.stringify(dataform),
        hirePersonInfo: FW.stringify(details)
    };


    if (!$("#form1").valid() || !validDatagrid()) {
        return;
    }
    if (HireInfo.objs.processInstId != null && HireInfo.objs.processInstId != "") {//非新建的提交包含了退回的操作
        var workFlow = new WorkFlow();
        workFlow.showAudit(HireInfo.objs.taskId, JSON.stringify($.extend({}, {
            processInstId: HireInfo.objs.processInstId
        }, dataCommit)), closeTab, null, null, null, null, HireInfo.loadData);
    } else {//新建和启动流程
        $(this).button("loading");
        var url = basePath + "pf/employmentApplication/submitHireInfo.do";
        $.ajax({
            url: url,
            type: 'post',
            dataType: "json",
            data: data,
            success: function (data) {
                if (data.result == "success") {
                    FW.success("提交成功 ！");
                    HireInfo.loadData(data);
                    var taskId = data.taskId;
                    if (taskId != null) {
                        var workFlow = new WorkFlow();
                        dataCommit = HireInfo.getDataForSubmit();
                        workFlow.submitApply(taskId, JSON.stringify($.extend({}, {
                            processInstId: data.processInstId
                        }, businessData)), closeTab, null, 0);
                    }
                } else {
                    if (data.reason != null) {
                        FW.error(data.reason);
                    } else {
                        FW.error("提交失败 ！");
                    }
                }
                $("#commitButton").button("reset");
            }
        });
    }
}

function validDatagrid() {
    var details = $("#fmaList").iForm('getVal');
    if (details.length <= 0) {
        FW.error("请添加聘用人员 ！");
        return false;
    } else {
        return $("#fmaList").valid();
    }
}

//显示流程图
$("#flowDiagramBtn").click(function () {
    if (isNull(HireInfo.objs.processInstId)) {
        if (HireInfo.objs.flowDiagram) {
            showFlowDialog(HireInfo.objs.flowDiagram);
        } else {
            FW.error("没有流程图信息");
        }
    } else {
        var workFlow = new WorkFlow();
        workFlow.showAuditInfo(HireInfo.objs.processInstId, "", HireInfo.objs.opts.isShowAuditBtn, function () {
            $("#approveBtn").click();
        });
    }
})