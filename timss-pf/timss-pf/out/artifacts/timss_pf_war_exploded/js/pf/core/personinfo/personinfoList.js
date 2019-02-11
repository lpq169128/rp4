var initColumns = [[
    {field: 'isCheck', title: '', checkbox: true, width: 10,},
    {field: 'userId', title: '工号', hidden: true},
    {field: 'name', title: '姓名', width: 100, fixed: true, sortable: true,},
    {
        field: 'sex', title: '性别', width: 80, fixed: true, sortable: true,
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_PERSON_SEX")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("ATD_PERSON_SEX", _enum)
            }
        }
    },
    {field: 'idCard', title: '身份证', width: 165, fixed: true, sortable: true,},
    {field: 'mobile', title: '个人电话', width: 115, fixed: true, sortable: true,},
    {field: 'graduateSchool', title: '毕业院校', width: 160},
    {field: 'highestEducation', title: '文化程度', width: 100, fixed: true, sortable: true,},
    {field: 'createUserName', title: '创建人', width: 110, fixed: true, sortable: true,},
    {
        field: 'createdate', title: '创建时间', width: 100, fixed: true, sortable: true, formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {
        field: 'inshipStatus', title: '上船状态', width: 80, fixed: true, sortable: true,
        formatter: function (value, row, index) {
            return FW.getEnumMap("INSHIP_STATUS")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("INSHIP_STATUS", _enum)
            }
        }
    },
    {
        field: 'jobStatus', title: '聘用状态', width: 110, fixed: true, sortable: true,
        formatter: function (value, row, index) {
            return FW.getEnumMap("RECRUIT_STATUS")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("RECRUIT_STATUS", _enum)
            }
        }
    }
]];

function goEmbark() {
    var codeArr = [];
    var listData = $("#tabList").datagrid("getSelections");

    if (listData <= 0) {
        FW.error("请选择一个或者多个船员");
        return false;
    }
    for (var i = 0; i < listData.length; i++) {
        if (listData[i].jobStatus == "N") {
            FW.error("存在未完成聘用申请的船员，无法登记上船");
            return false;
        }
        if (listData[i].inshipStatus == "IN") {
            FW.error("存在已在船船员，无需登记上船");
            return false;
        }
        codeArr.push(listData[i].businessId);
    }

    var src = basePath + "pf/personinfo/register.do";
    var btnOpts = [
        {
            "name": "关闭",
            "onclick": function () {
                return true;
            }
        },
        {
            "name": "确定",
            "style": "btn-success",
            "onclick": function () {
                var conWin = _parent().window.document.getElementById("itcDlginshipContent").contentWindow;
                if (!conWin.$("#baseform").valid()) {
                    return false;
                }
                var formData = conWin.$("#baseform").iForm("getVal");//获取主表单数据

                $.ajax({
                    type: "post",
                    url: basePath + "pf/personinfo/saveRegister.do",
                    async: false,
                    data: {
                        personIds: FW.stringify(codeArr),
                        registerInfo: FW.stringify(formData),
                    },
                    success: function (data) {
                        if (data.flag == "success") {
                            $("#tabList").datagrid("reload");
                            FW.success("上船登记成功");
                        }
                        else {
                            FW.error("上船登记失败");
                        }
                    },
                    error: function (data) {
                        FW.error("系统运行错误");
                    }
                });
                _parent().$("#itcDlginship").dialog("close");
            }
        }
    ];
    var dlgOpts = {width: 500, height: 180, title: "上船登记", idSuffix: "inship", modal: true};
    FW.dialog("init", {"src": src, "dlgOpts": dlgOpts, "btnOpts": btnOpts});
};

function goShiftOperation() {
    var codeArr = [];
    var listData = $("#tabList").datagrid("getSelections");
    console.log("listData-----")
    console.log(listData)

    if (listData <= 0) {
        FW.error("请选择一个或者多个船员");
        return false;
    }
    for (var i = 0; i < listData.length; i++) {
        if (listData[i].inshipStatus == "OUT") {
            FW.error("存在已离船船员，无需进行交班操作");
            return false;
        }
        if (listData[i].jobStatus == "N") {
            FW.error("存在未完成聘用申请的船员，无法登记上船");
            return false;
        }
        codeArr.push(listData[i].businessId);
    }
    console.log(codeArr)

    var src = basePath + "pf/personinfo/shiftOperation.do";
    var btnOpts = [
        {
            "name": "关闭",
            "onclick": function () {
                return true;
            }
        },
        {
            "name": "确定",
            "style": "btn-success",
            "onclick": function () {
                var conWin = _parent().window.document.getElementById("itcDlgshiftOperationContent").contentWindow;
                if (!conWin.$("#baseform").valid()) {
                    return false;
                }
                var formData = conWin.$("#baseform").iForm("getVal");//获取主表单数据
                console.log("交班操作--------")
                console.log(formData)

                $.ajax({
                    type: "post",
                    url: basePath + "pf/personinfo/saveShift.do",
                    async: false,
                    data: {
                        personIds: FW.stringify(codeArr),
                        shiftInfo: FW.stringify(formData),
                    },
                    success: function (data) {
                        console.log(data)
                        if (data.flag == "success") {
                            $("#tabList").datagrid("reload");
                            FW.success("交班操作成功");
                        }
                        else {
                            FW.error(data.msg);
                        }
                    },
                    error: function (data) {
                        FW.error("系统运行错误");
                    }
                });
                _parent().$("#itcDlgshiftOperation").dialog("close");
            }
        }
    ];
    var dlgOpts = {width: 700, height: 200, title: "交班操作", idSuffix: "shiftOperation"};
    FW.dialog("init", {"src": src, "dlgOpts": dlgOpts, "btnOpts": btnOpts});
};

//图片上传
var xhr;

function doImport() {
    document.getElementById("file").click();
}

//上传文件方法
function doImportExcel(type) {
    FW.showMask();//使用遮罩
    var fileObj = document.getElementById("file").files[0]; // js 获取文件对象
    var url = basePath + "pf/personinfo/importPersonInfo.do?replaceType=" + type; // 接收上传文件的后台地址

    var form = new FormData(); // FormData 对象
    form.append("file", fileObj); // 文件对象

    xhr = new XMLHttpRequest();  // XMLHttpRequest 对象
    xhr.open("post", url, true); //post方式，url为服务器请求地址，true 该参数规定请求是否异步处理。
    xhr.onload = uploadComplete; //请求完成
    xhr.onerror = uploadFailed; //请求失败

    xhr.upload.onloadstart = function () {//上传开始执行方法
        ot = new Date().getTime();   //设置上传开始时间
        oloaded = 0;//设置上传开始时，以上传的文件大小为0
    };

    xhr.send(form); //开始上传，发送form数据
}

//上传成功响应
function uploadComplete(evt) {
    //服务断接收完文件返回的结果
    var data = JSON.parse(evt.target.responseText);
    //errorWords为报错信息，暂未展示
    console.log("errorWords:" + data.errorWords)
    if (data.status == "ok") {
        if (data.errorWords.length > 0) {
            FW.error("上传成功！部分数据有问题，请检查");
        } else {
            FW.success("上传成功！");
        }
        window.location.reload();
    } else if (data.status == "check") {
        FW.confirm("身份证重复，确定导入吗？确定后数据将覆盖", function () {
            doImportExcel(true);
        });
    } else if (data.status == "error") {
        FW.error("上传失败，" + data.errorWords);
    } else {
        FW.error("上传失败！");
    }
    FW.removeMask();//移除遮罩
}

//上传失败
function uploadFailed(evt) {
    alert("上传失败！");
    FW.removeMask();//移除遮罩
}

//取消上传
function cancleUploadFile() {
    xhr.abort();
}


//上传进度实现方法，上传过程中会频繁调用该方法
function progressFunction(evt) {
    var progressBar = document.getElementById("progressBar");
    var percentageDiv = document.getElementById("percentage");
    // event.total是需要传输的总字节，event.loaded是已经传输的字节。如果event.lengthComputable不为真，则event.total等于0
    if (evt.lengthComputable) {//
        progressBar.max = evt.total;
        progressBar.value = evt.loaded;
        percentageDiv.innerHTML = Math.round(evt.loaded / evt.total * 100) + "%";
    }
    var time = document.getElementById("time");
    var nt = new Date().getTime();//获取当前时间
    var pertime = (nt - ot) / 1000; //计算出上次调用该方法时到现在的时间差，单位为s
    ot = new Date().getTime(); //重新赋值时间，用于下次计算
    var perload = evt.loaded - oloaded; //计算该分段上传的文件大小，单位b
    oloaded = evt.loaded;//重新赋值已上传文件大小，用以下次计算
    //上传速度计算
    var speed = perload / pertime;//单位b/s
    var bspeed = speed;
    var units = 'b/s';//单位名称
    if (speed / 1024 > 1) {
        speed = speed / 1024;
        units = 'k/s';
    }
    if (speed / 1024 > 1) {
        speed = speed / 1024;
        units = 'M/s';
    }
    speed = speed.toFixed(1);
    //剩余时间
    var resttime = ((evt.total - evt.loaded) / bspeed).toFixed(1);
    time.innerHTML = '，速度：' + speed + units + '，剩余时间：' + resttime + 's';
    if (bspeed == 0) time.innerHTML = '上传已取消';
}
