//点击编辑，开器编辑模式
function editBean() {
    //开启编辑
    PageDetail.toEdit();

    $("#trainingInfo-save").show();
    $("#trainingInfo-edit").hide();
    $(attach.tabInit).iForm('beginEdit');
    $(attach.tabDiv).show();

    FW.fixToolbar("#toolbar");
}

//删除按钮
function del() {
    FW.confirm("确定删除吗？删除后数据不能恢复", function () {
        $.post(PageDetail.objs.url.del, {"id": id}, function (data) {
            if (data.result == "success") {
                FW.success("删除成功");
                FW.deleteTabById(FW.getCurrentTabId());
            }
        });
    });

}

/**
 * 关闭选项卡
 */
function closeTab() {
    FW.deleteTabById(FW.getCurrentTabId());
}

function save(_this) {
    //表单校验 必填和长度
    if (!valid()) {
        return false;
    }


    var $saveBtn = $(_this).button('loading');

    //判断是新增还是修改
    var url = PageDetail.objs.url.create;
    if (id && id != "" && id != null) {
        url = PageDetail.objs.url.update;
    }

    //获取基本信息和详细信息页面的值
    var trainingInfoList = $("#" + trainingInfoInit).iForm('getVal');
    var businessData = {};
    $.fn.extend(true, businessData, trainingInfoList);
    businessData.handler = businessData.handlerId;//负责人存ID，不是名字

    //获取参与人员表格的值并存入data中
    var data = {trainingInfo: FW.stringify(businessData)};
    data.TrainingPerson = FW.stringify(getTableData(initTrainingPerson.dataGrid));
    if (data.TrainingPerson == "[]") {
        FW.error("参与人员不能为空");
        $saveBtn.button('reset');
        return false;
    }
    data.AttachList = $(attach.tabInit).iForm('getVal');
    console.log($(attach.tabInit).iForm('getVal'))
    console.log(data.AttachList + "附件")
    console.log("----data----save")
    console.log(data)

    // $saveBtn.button('reset');
    // return false;
    $.ajax({
        url: url,
        data: data,
        type: "post",
        complete: function (data) {
            console.log("data-----------******")
            console.log(data);
            var flag = data && data.responseJSON && data.responseJSON.flag;
            if (flag && flag == "success") {
                if (mode == "create") {
                    closeTab();
                } else {
                    window.location.reload();
                }
                FW.success("保存培训记录信息成功");
            } else {
                FW.error(data.responseJSON.msg);
                $saveBtn.button('reset');
            }
        }
    })
}

//对页面进行校验，基本信息和家庭、证件等每个表单表格都要进行校验
function valid() {
    if (!$("#" + trainingInfoInit).valid()) {
        return false;
    }
    return true;
}

function getAppsChanged(labelName) {//获得应用变更的情况
    var rows = $("#" + labelName).datagrid("getRows");
    console.log('----简洁的方法-55-556--' + labelName)
    console.log(rows)
    return rows;
}

function addTrainingPersonButton() {
    var src = basePath + "pf/trainingPerson/trainingPersonList.do"
    var btnOpts = [{
        "name": "取消",
        "float": "right",
        "style": "btn-default",
        "onclick": function () {
            return true;
        }
    },
        {
            "name": "确定",
            "float": "right",
            "style": "btn-success",
            "onclick": function () {
                var conWin = _parent().window.document.getElementById("itcDlgTrainingPersonContent").contentWindow;
                var listData = conWin.$("#tabList").datagrid("getSelections");
                if (listData <= 0) {
                    FW.error("请选择一个或者多个人员");
                    return false;
                }
                addInvMatApplyItem(listData);
                _parent().$("#itcDlgTrainingPerson").dialog("close");
            }
        }];
    var dlgOpts = {width: 800, height: 400, title: "培训人员", idSuffix: "TrainingPerson"};
    FW.dialog("init", {"src": src, "dlgOpts": dlgOpts, "btnOpts": btnOpts});
}

//添加物资领料表单的物资明细
function addInvMatApplyItem(data) {
    for (var i = 0; i < data.length; i++) {
        var trainingPersonRows = $("#trainingPersonTable").datagrid("getRows");
        var equalFlg = false;
        for (var j = 0; j < trainingPersonRows.length; j++) {
            if (data[i]["personId"] == trainingPersonRows[j].personId) {
                equalFlg = true;
            }
        }
        if (!equalFlg) {
            var row = {};
            row["tmpid"] = i + 1;
            row["personId"] = data[i]["personId"];
            row["name"] = data[i]["name"];
            row["sex"] = data[i]["sex"];
            row["idCard"] = data[i]["idCard"];
            row["mobile"] = data[i]["mobile"];
            row["graduateSchool"] = data[i]["graduateSchool"];
            row["highestEducation"] = data[i]["highestEducation"];
            $("#trainingPersonTable").datagrid("appendRow", row);
        }
    }
    $("#trainingPersonTable").datagrid("resize");
}


