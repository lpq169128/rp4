PageDetail.afterChangeShow = function () {
    if (PageDetail.isMode("create")) {
        PageDetail.objs.form.obj.iForm("hide", ["createdate", "businessNo", "wfStatus"]);
        $("#btn-printer").hide();
    } else {
        PageDetail.objs.form.obj.iForm("hide", ["wfStatus"]);
    }

    if (PageDetail.objs.form.bean.wfStatus == "提交申请") {
        $("#btn-printer").hide();
    }
};

