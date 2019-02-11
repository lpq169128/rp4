var fmaFormFields = [
    {id: "id", type: "hidden"},
    {
        id: "recruitType", title: "招聘对象类型", type: "combobox", rules: {
            required: true
        },
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_SAILOR_TYPE")[value];
        },
        options: {
            "data": FW.parseEnumData("ATD_SAILOR_TYPE", _enum)
        }
    },
    {
        title: "职务",
        id: "duties",
        type: "combobox",
        dataType: "enum",
        enumCat: "ATD_ DUTIES_TYPE",
        rules: {
            required: true
        }
    },
    {
        title: "交班人",
        id: "handoverUser",
        rules: {
            required: true
        }
    },
    {
        title: "调往船舶",
        id: "shipId",
        type: "combobox",
        data: shipList1,
        rules: {
            required: true
        }
    },
    {
        title: "上船日期",
        id: "inShipDate",
        type: "date"
    },
    {
        title: "原因",
        id: "remark",
        linebreak: true,
        wrapXsWidth: 12,
        wrapMdWidth: 8,
        height: 36
    }

];
var fmaAttachMap = [];
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
            //"fileSizeLimit" : 10 * 1024,
            "initFiles": fmaAttachMap,
            "delFileAfterPost": true
        }
    }
];

//初始化附件
function initAttachForm(data, $form, $wrapper, readOnly) {
    var result = {
        data: data,
        $form: $form,
        $wrapper: $wrapper,
        attachMap: fmaAttachMap,
        attachFormFields: attachFormFields,
        readOnly: readOnly
    };
    initAttachFormTemplate(result);
}

var columns = [{id: "userId", type: "hidden"},
    {id: "name", title: "人员", rules: {required: true}},
    {id: 'sex', title: '性别',},
    {id: 'idCard', title: '身份证'},
    {id: 'mobile', title: '个人电话'},
    {id: 'graduateSchool', title: '毕业院校'},
    {id: 'highestEducation', title: '文化程度'},
]

// $("#name").iHint("init",ops);
// var ops = [
//     opts.scope = opts.scope || "site",
//     opts.showOn = opts.showOn || "input",
//     opts.datasource = "sm/employmentApplication/person.do",
//     opts.getDataOnKeyPress = true,
//     opts.highlight = true,
//     opts.formatter = function(id,name){
//         return name + " / " + id.split("_")[0];
//     }]


//
//
//
// function initHint(){
//     $("#overtimeItemFrom").ITCUI_HintList({
//         "datasource":basePath + "sm/employmentApplication/informatizationTypeHint.do",
//         "getDataOnKeyPress":true,
//         "clickEvent":function(id,name){
//             $("#overtimeItemFrom").val(name);
//             expandForHintById(id);
//         },
//         "showOn":"input",
//         "highlight":true
//     });
// }


// $("#shipId").iHint("init",options);
// $("#name").iHint("init",options)