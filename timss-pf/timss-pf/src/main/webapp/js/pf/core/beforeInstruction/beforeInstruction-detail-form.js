//表单、标题的id
var beforeInstructionInit = "beforeInstructionForm";
var beforeInstructionDetailText = "beforeInstructionDetailList";

var attach = {tabDiv: "#attachFormWrapper", tabText: "#attachFormList", tabInit: "#attachForm",}
var attachMap = businessAttachmentList;

var trainingEffectEle = {
    tabDiv: "#trainingEffectWrapper",
    tabText: "#trainingEffectList",
    tabInit: "#trainingEffectTable",
    tabAddbut: "#add-trainingEffect",
    tabAddshow: "#addtrainingEffect"
}

var initParams = {
    namePrefix: titleName,
    //增删改查等按钮的地址
    url: {
        del: basePath + "pf/beforeInstruction/delete.do",//required删除
        query: basePath + "pf/beforeInstruction/getDetail.do", //required 查询
        create: basePath + "pf/beforeInstruction/insertBeforeInstruction.do",//required 创建
        update: basePath + "pf/beforeInstruction/updateBeforeInstruction.do",//required  更改
    },
    //初始化表单的各个字段
    form: {
        id: "beforeInstructionForm",
        opts: {
            labelFixWidth: 150
        },
        fields: [
            {title: " ", id: "trainingId", type: "hidden"},
            // {id: 'shipName', title: '船名', rules: {required: true}},//combox
            {
                title: "船名",
                id: "deptShip",
                type: "combobox",
                data: mgmtShipInfoMap,
                rules: {
                    required: true
                }
            },
            {id: 'duties', title: '职务', rules: {required: true, maxlength: 20}},
            {id: 'nameId', title: '姓名Id', type: "hidden"},
            {id: 'name', title: '姓名', rules: {required: true}},//iHint,查找所有船员数据
            {id: 'dutiesDate', title: '任职日期', type: 'date', rules: {required: true}},
            {
                title: "开航前完成内容",
                id: "content",
                type: "textarea",
                linebreak: true,
                wrapXsWidth: 120,
                wrapMdWidth: 60,
                height: 200,
                rules: {maxlength: 400, required: true}
            },
            {
                title: "一周内完成内容",
                id: "attribute1",
                type: "textarea",
                linebreak: true,
                wrapXsWidth: 120,
                wrapMdWidth: 60,
                height: 200,
                rules: {maxlength: 400, required: true}
            }
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

var trainingEffectFields = [[
    {field: 'personId', title: '人员ID', hidden: true},
    {
        field: 'trainingItem', title: '培训范围', width: 150,
        editor: {type: "text", options: {rules: {maxlength: 200}}}
    },
    {
        field: 'trainingEffect', title: '培训效果', width: 150,
        editor: {type: "text", options: {rules: {maxlength: 200}}}
    },
    {
        field: 'endDate', title: '完成时间', width: 80,
        editor: {type: 'datebox', options: {dataType: "date", minView: 2, format: "yyyy-mm-dd"}},
        formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {
        field: 'tester', title: '考核人', width: 80,
        editor: {type: "text", options: {rules: {maxlength: 30}}}
    },
    garbageColunms,
]];

//初始化表单
function initPager() {
    $("#" + beforeInstructionDetailText).iFold("show");
    $("#" + beforeInstructionDetailText).iFold("init");
    initAttach();
    initTrainingEffect();
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

//初始化培训效果
function initTrainingEffect() {
    var data = trainingEffectList;
    if (!initTrainingEffect.dataGrid) {
        //初始化表格或者表单的标题
        $(trainingEffectEle.tabText).iFold();
        initTrainingEffect.dataGrid = $(trainingEffectEle.tabInit).datagrid({
            fitColumns: true,
            scrollbarSize: 0,
            data: data,
            columns: trainingEffectFields,
            onClickCell: function (rowIndex, field, value) {//表格的单行删除事件
                deleteGarbageColumnFunction(rowIndex, field, value, "code", initTrainingEffect.dataGrid);
            }
        });
    }
}

//设置详细信息的值
function setFormDetailVal() {
    $("#" + beforeInstructionInit).iForm("setVal", bean);
}