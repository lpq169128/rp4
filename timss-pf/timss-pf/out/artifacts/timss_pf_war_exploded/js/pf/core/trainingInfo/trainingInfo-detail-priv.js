function initPriv(data) {
    var privMapping = getPrivMapping();
    Priv.map("isSavetable()", "virsual-trainingInfo-save");
    Priv.map("isEditable()", "virsual-trainingInfo-edit");
    Priv.map("isDeltable()", "virsual-trainingInfo-del");
    // if(!id){
    //     $("#trainingInfo-save").show();
    // }
    Priv.apply();
    FW.fixToolbar("#toolbar");
    //如果需要设置为只读状态
    setPageReadOnlyIfNeed(privMapping);
}

//设置页面为只读状态，如果用户没有编辑权限
function setPageReadOnlyIfNeed() {
    //编辑状态，但是没有编辑权限
    if (id) {
        $("#" + trainingInfoInit).iForm("endEdit");
        convertDataGridReadOnly(initTrainingPerson.dataGrid, trainingPersonEle.tabAddshow, trainingPersonEle.tabDiv);
        //判断附件是否有值，有值为不可编辑状态，无值不显示
        var attachmentMap = $(attach.tabInit).iForm('getVal');
        if (attachmentMap.attach) {
            $(attach.tabInit).iForm('endEdit');
        } else {
            $(attach.tabDiv).hide();
        }
    }
}

function isSavetable() {
    if (id) {
        //编辑页面，同时具有编辑权限，显示编辑按钮
        return false;
    }
    return true;
}

function isEditable() {
    if (id) {
        //编辑页面，同时具有编辑权限，显示编辑按钮
        return trainingInfoPriv.getEditPriv(privMapping);
    }
    return false;
}

function isDeltable() {
    if (id) {
        //删除按钮，同时具有删除权限，显示删除按钮
        return trainingInfoPriv.getDelPriv(privMapping);
    }
    return false;
}


//获取供应商权限变量
var trainingInfoPriv = {
    getPriv: function (privName, privMapping) {
        privMapping = privMapping || getPrivMapping();
        return !!privMapping[privName];
    },
    getEditPriv: function (privMapping) {
        privMapping = privMapping || getPrivMapping();
        return trainingInfoPriv.getPriv("trainingInfo_detail_edit", privMapping);
    },
    getDelPriv: function (privMapping) {
        privMapping = privMapping || getPrivMapping();
        return trainingInfoPriv.getPriv("trainingInfo_detail_del", privMapping);
    },
}

//获取权限数据
function getPrivMapping() {
    return _parent().privMapping;
}


//设置表单和按钮权限
function setPriv() {
    console.log(id + '-----' + mode);
    //设置表单权限
    changeEditForm();
    //设置按钮权限
    changeEditBut();
    FW.fixToolbar("#toolbar");
}

//设置表单权限
function changeEditForm() {
    if (mode == "edit") {
        //设置表格权限
        $("#" + trainingInfoInit).iForm("beginEdit");
        convertDataGridEdit(initTrainingPerson.dataGrid, trainingPersonEle.tabAddshow, trainingPersonEle.tabDiv);
    } else if (mode == "view" || mode == "closeEdit") {
        $("#" + trainingInfoInit).iForm("endEdit");
        //设置表单为只读
        setPageReadOnlyIfNeed();
    }
}

//设置按钮权限
function changeEditBut() {
    console.log("------------按钮权限")
    if (id && (mode == "view" || mode == "closeEdit")) {
        console.log(1)
        $("#trainingInfo-save").hide();
        // $("#trainingInfo-edit").show();
    } else if (id && mode == "edit") {
        console.log(2)
        $("#trainingInfo-save").show();
        // $("#trainingInfo-edit").hide();
    } else {
        console.log(4)
        $("#trainingInfo-save").show();
        // $("#trainingInfo-edit").hide();
        // $("#trainingInfo-del").hide();
    }
}