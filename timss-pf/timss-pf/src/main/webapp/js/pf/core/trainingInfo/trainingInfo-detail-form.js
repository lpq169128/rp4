//表单、标题的id
var trainingInfoInit = "trainingInfoForm";
var trainingInfoDetailText = "trainingInfoDetailList";

var attach = {tabDiv: "#attachFormWrapper", tabText: "#attachFormList", tabInit: "#attachForm",}
var attachMap = businessAttachmentList;

var trainingPersonEle = {
    tabDiv: "#trainingPersonWrapper",
    tabText: "#trainingPersonList",
    tabInit: "#trainingPersonTable",
    tabAddbut: "#add-trainingPerson",
    tabAddshow: "#addtrainingPerson"
}

var initParams = {
    namePrefix: titleName,
    //增删改查等按钮的地址
    url: {
        del: basePath + "pf/trainingInfo/delete.do",//required删除
        query: basePath + "pf/trainingInfo/getDetail.do", //required 查询
        create: basePath + "pf/trainingInfo/insertTrainingInfo.do",//required 创建
        update: basePath + "pf/trainingInfo/updateTrainingInfo.do",//required  更改
    },
    //初始化表单的各个字段
    form: {
        id: "trainingInfoForm",
        opts: {
            labelFixWidth: 150
        },
        fields: [
            {title: " ", id: "trainingId", type: "hidden"},
            {id: 'trainingName', title: '培训名称', rules: {required: true, maxlength: 15}},
            {id: 'handlerId', title: '负责人Id', type: "hidden"},
            {id: 'handler', title: '负责人', rules: {required: true}},
            {id: 'startDate', title: '日期', type: 'date', rules: {required: true}},
            {id: 'address', title: '地点', rules: {maxlength: 15}},
            {id: 'deptShip', title: '部门/船舶', rules: {maxlength: 15}},
            {id: 'teacher', title: '授课人', rules: {required: true, maxlength: 15}},
            {id: 'hour', title: '课时', rules: {number: true,maxlength: 15}},
            {
                id: 'trainingCategory', title: '类型', type: 'combobox', rules: {required: true},
                formatter: function (value, row, index) {
                    return FW.getEnumMap("ATD_TRAINING_CATEGORY")[value];
                },
                options: {
                    "data": FW.parseEnumData("ATD_TRAINING_CATEGORY", _enum)
                }
            },
            {id: 'cost', title: '费用', rules: {number: true, min: 0}},
            {id: 'trainingEffect', wrapXsWidth: 8, wrapMdWidth: 8, title: '培训效果', rules: {maxlength: 50}},
            {
                title: "授课主要内容",
                id: "content",
                type: "textarea",
                linebreak: true,
                wrapXsWidth: 120,
                wrapMdWidth: 60,
                height: 300,
                rules: {required: true}
            },
            {id: 'remark', wrapXsWidth: 8, wrapMdWidth: 8, title: '备注', rules: {maxlength: 50}},
        ],
        //主键id
        idField: "trainingId",
    },
};

var attachFormFields = [
    {
        title: " ",
        id: "attach",
        linebreak: true,
        type: "fileupload",
        wrapXsWidth: 12,
        wrapMdWidth: 12,
        options: {
            "uploader": basePath + "upload?method=uploadFile&jsessionid=" + session,
            "delFileUrl": basePath + "upload?method=delFile&key=" + valKey,
            "downloadFileUrl": basePath + "upload?method=downloadFile",
            "swf": basePath + "itcui/js/uploadify.swf",
            "fileSizeLimit": 10 * 1024,
            "initFiles": attachMap,
            "delFileAfterPost": true
        }
    }
];

var trainingPersonFields = [[
    {field: 'personId', title: '人员ID', hidden: true},
    {field: 'name', title: '姓名', width: 80},
    {
        field: 'sex', title: '性别', width: 80,
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
    {field: 'idCard', title: '身份证', width: 100},
    {field: 'mobile', title: '个人电话', width: 100},
    {field: 'graduateSchool', title: '毕业院校', width: 100},
    {field: 'highestEducation', title: '文化程度', width: 100},
    garbageColunms,
]];

//初始化表单
function initPager() {
    $("#" + trainingInfoDetailText).iFold("show");
    $("#" + trainingInfoDetailText).iFold("init");
    initAttach();
    initTrainingPerson();
    //编辑进入后 设置表单值
    if (id && id != "" && id != null) {
        setFormDetailVal();
    }
}

//初始化附件
function initAttach() {
    $(attach.tabInit).iForm('init', {
        "fields": attachFormFields, "options": {
            labelFixWidth: 1,
            labelColon: false,
        }
    });
    $(attach.tabText).iFold();
    console.log("初始化------------");
    console.log($(attach.tabInit).iForm('getVal'))
}

//初始化参与人员
function initTrainingPerson() {
    var data = trainingPersonList;
    if (!initTrainingPerson.dataGrid) {
        //初始化表格或者表单的标题
        $(trainingPersonEle.tabText).iFold();
        initTrainingPerson.dataGrid = $(trainingPersonEle.tabInit).datagrid({
            fitColumns: true,
            scrollbarSize: 0,
            data: data,
            columns: trainingPersonFields,
            onClickCell: function (rowIndex, field, value) {//表格的单行删除事件
                deleteGarbageColumnFunction(rowIndex, field, value, "code", initTrainingPerson.dataGrid);
            }
        });
    }
}

//设置详细信息的值
function setFormDetailVal() {
    $("#" + trainingInfoInit).iForm("setVal", bean);
}