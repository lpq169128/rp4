//点击编辑，开器编辑模式
function editBean() {
    //开启编辑
    PageDetail.toEdit();

    $("#personinfo-save").show();
    $("#personinfo-edit").hide();
    $("#" + personinfoInit).iForm("beginEdit");
    $("#" + personinfoDetailInit).iForm("beginEdit");
    $(attach.tabInit).iForm('beginEdit');
    $(attach.tabDiv).show();
    //设置表格权限
    convertDataGridEdit(initFamily.dataGrid, familyEle.tabAddshow, familyEle.tabDiv);
    convertDataGridEdit(initCertificates.dataGrid, certiEle.tabAddshow, certiEle.tabDiv);
    convertDataGridEdit(initCredentials.dataGrid, credEle.tabAddshow, credEle.tabDiv);
    convertDataGridEdit(initExperinence.dataGrid, experEle.tabAddshow, experEle.tabDiv);


    convertDataGridReadOnlyOrEdit(initVoyage.dataGrid, voyage.tabAddshow, voyage.tabDiv);

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

var repeatState = "N";

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
    var personinfoList = $("#" + personinfoInit).iForm('getVal');
    var personinfoDetailList = $("#" + personinfoDetailInit).iForm('getVal');
    var businessData = {};
    $.fn.extend(true, businessData, personinfoList, personinfoDetailList);

    //获取家庭、证件、证书、工作经历、海上经历等表格的值并存入data中
    var data = {PersonInfo: FW.stringify(businessData)};
    data.PersonRelation = FW.stringify(getTableData(initFamily.dataGrid));
    data.PersonCertificate = FW.stringify(getTableData(initCertificates.dataGrid));
    data.PersonCredentials = FW.stringify(getTableData(initCredentials.dataGrid));
    data.PersonExperinence = FW.stringify(getTableData(initExperinence.dataGrid));
    data.PersonVoyage = FW.stringify(getTableData(initVoyage.dataGrid));
    data.AttachList = $(attach.tabInit).iForm('getVal');
    data.repeatState = repeatState;

    $.ajax({
        url: url,
        data: data,
        type: "post",
        complete: function (data) {
            var flag = data && data.responseJSON && data.responseJSON.flag;
            if (flag && flag == "success") {
                if (mode == "create") {
                    closeTab();
                } else {
                    window.location.reload();
                }
                repeatState = "N";
                FW.success("保存船员档案信息成功");
            } else {
                if (data.responseJSON.data) {
                    FW.confirm("确认更新船员信息？", {
                        onConfirm: function () {
                            repeatState = "Y";
                            save(_this);
                        },
                        onCancel: function () {
                            repeatState = "N";
                        }
                    });
                } else {
                    FW.error(data.responseJSON.msg);
                }
                $saveBtn.button('reset');
            }
        }

    })
}

//对页面进行校验，基本信息和家庭、证件等每个表单表格都要进行校验
function valid() {
    if (!$("#" + personinfoInit).valid()) {
        return false;
    }
    if (!$("#" + personinfoDetailInit).valid()) {
        return false;
    }
    if (!$(familyEle.tabText).valid()) {
        return false;
    }
    if (!$(certiEle.tabText).valid()) {
        return false;
    }
    if (!$(credEle.tabText).valid()) {
        return false;
    }
    if (!$(experEle.tabText).valid()) {
        return false;
    }
    if (!$(voyage.tabText).valid()) {
        return false;
    }

    var certlistLen = $("#certificatesTable").datagrid('getRows').length;
    var certHash = {};
    for (var i = 0; i < certlistLen; i++) {
        var valThis = $("input[name='cardType_" + i + "']").val();
        if (certHash[valThis]) {
            FW.error("存在相同的证件！在证件表的第" + (i + 1) + "行（" + FW.getEnumMap("ATD_CARD_TYPE")[valThis] + "）重复");
            return false;
        }
        certHash[valThis] = true;
    }

    var credlistLen = $("#credentialsTable").datagrid('getRows').length;
    var credHash = {};
    for (var i = 0; i < credlistLen; i++) {
        var valThis = $("input[name='credentialsType_" + i + "']").val();
        if (credHash[valThis]) {
            FW.error("存在相同的证书！在证书表的第" + (i + 1) + "行（" + FW.getEnumMap("ATD_CERTIFICATE_TYPE")[valThis] + "）重复");
            return false;
        }
        credHash[valThis] = true;
    }
    return true;
}

function getAppsChanged(labelName) {//获得应用变更的情况
    var rows = $("#" + labelName).datagrid("getRows");
    return rows;
}



