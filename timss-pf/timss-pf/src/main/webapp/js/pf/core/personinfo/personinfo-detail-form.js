//表单、标题的id
var personinfoInit = "personinfoForm";
var personinfoDetailInit = "personinfoDetailForm";
var personinfoDetailText = "personinfoDetailList";

//初始化需要的各个表格模块里，表单、标题-、添加按钮的id
var familyEle = {
    tabDiv: "#familyWrapper",
    tabText: "#familyList",
    tabInit: "#familyTable",
    tabAddbut: "#add-family",
    tabAddshow: "#addfamily"
}
var certiEle = {
    tabDiv: "#certificatesWrapper",
    tabText: "#certificatesList",
    tabInit: "#certificatesTable",
    tabAddbut: "#add-certificates",
    tabAddshow: "#addcertificates"
};
var credEle = {
    tabDiv: "#credentialsWrapper",
    tabText: "#credentialsList",
    tabInit: "#credentialsTable",
    tabAddbut: "#add-credentials",
    tabAddshow: "#addcredentials"
};
var experEle = {
    tabDiv: "#experinenceWrapper",
    tabText: "#experinenceList",
    tabInit: "#experinenceTable",
    tabAddbut: "#add-experinence",
    tabAddshow: "#addexperinence"
};
var voyage = {
    tabDiv: "#voyageWrapper",
    tabText: "#voyageList",
    tabInit: "#voyageTable",
    tabAddbut: "#add-voyage",
    tabAddshow: "#addvoyage"
};
var examineEle = {
    tabDiv: "#examineWrapper",
    tabText: "#examineList",
    tabInit: "#examineTable",
};
var trainingEle = {
    tabDiv: "#trainingWrapper",
    tabText: "#trainingList",
    tabInit: "#trainingTable",
};
var hireinfoEle = {
    tabDiv: "#hireinfoWrapper",
    tabText: "#hireinfoList",
    tabInit: "#hireinfoTable",
}
var attach = {tabDiv: "#attachFormWrapper", tabText: "#attachFormList", tabInit: "#attachForm",}
var initParams = {
    namePrefix: "人员档案",
    //增删改查等按钮的地址
    url: {
        del: basePath + "pf/personinfo/delete.do",//required删除
        query: basePath + "pf/personinfo/getDetail.do", //required 查询
        create: basePath + "pf/personinfo/insertPersonInfo.do",//required 创建
        update: basePath + "pf/personinfo/updatePersonInfo.do",//required  更改
    },
};

//初始化页面需要用到的fields 字段信息、校验标准、宽度等信息
var mainFields = [
    {title: " ", id: "businessId", type: "hidden"},
    {title: "工号", id: "userId", type: "hidden"},
    {id: 'name', title: '姓名', rules: {required: true, maxlength: 20}},
    {id: 'pinyin', title: '拼音', rules: {maxlength: 50}},
    {
        id: 'sex', title: '性别', type: 'combobox', rules: {required: true},
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_PERSON_SEX")[value];
        },
        options: {
            "data": FW.parseEnumData("ATD_PERSON_SEX", _enum),

        }
    },
    {
        id: 'birthday', title: '出生年月', type: 'date', rules: {required: true},
        options: {"endDate": new Date()}
    },
    {id: 'mobile', title: '个人电话', rules: {maxlength: 20, regex: /^[0-9-]{1,}$/}},
    {
        id: 'arrivalDate', title: '加入时间', type: 'date', options: {"endDate": new Date()},
        rules: {required: true, greaterEqualThan: "#f_birthday"}
    },
];

var detailFields = [
    {id: 'idCard', title: '身份证', rules: {required: true, maxlength: 20, regex: /^[0-9a-zA-Z_]{1,}$/}},
    {id: 'politicalFace', title: '政治面目', rules: {maxlength: 20}},
    {id: 'nationality', title: '籍贯/民族', rules: {maxlength: 20}},
    {id: 'graduateSchool', title: '毕业院校', wrapXsWidth: 8, wrapMdWidth: 8, rules: {maxlength: 100}},
    {id: 'highestEducation', title: '文化程度', rules: {maxlength: 20}},
    {id: 'originalWorkUnit', title: '原工作单位', wrapXsWidth: 8, wrapMdWidth: 8, rules: {maxlength: 100}},
    {id: 'workDate', title: '参加工作时间', type: 'date'},
    {id: 'address', title: '通信地址', wrapXsWidth: 8, wrapMdWidth: 8, rules: {maxlength: 100}},
    {id: 'postalCode', title: '邮政编码', rules: {maxlength: 20}},
    {id: 'health', title: '健康状况', rules: {maxlength: 20}},
    {id: 'familyHistory', title: '家庭病史', rules: {maxlength: 20}},
    {id: 'history', title: '既往病史', rules: {maxlength: 20},}
];

var familyFields = [[
    {field: 'businessId', title: '', hidden: true},
    {
        field: 'kinName', title: '亲属姓名', width: 160, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {required: true, maxlength: 20}}}
    },
    {
        field: 'relation', title: '关系', width: 320, fixed: true,
        editor: {type: "text", options: {rules: {required: true, maxlength: 20}}}
    },
    {
        field: 'phone', title: '亲属电话', width: 200, fixed: true,
        editor: {type: "text", options: {rules: {maxlength: 20, regex: /^[0-9-]{1,}$/}}}
    },
    garbageColunms,
    {field: "seize", title: "", width: 1}
]];

var CertificateFields = [[
    {field: 'businessId', title: '', hidden: true},
    {
        field: 'cardType', title: '证件类别', width: 160, align: 'left', fixed: true,
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_CARD_TYPE")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("ATD_CARD_TYPE", _enum),
                "rules": {
                    required: true,
                },

            },
        }
    },
    {
        field: 'cardNo', title: '证件号码', width: 320, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {required: true, maxlength: 50}}}
    },

    {
        field: 'cardDate', title: '领证日期', width: 220, align: 'left', fixed: true,
        editor: {
            type: 'datebox',
            options: {dataType: "date", minView: 2, format: "yyyy-mm-dd", rules: {required: true},}
        },
        formatter: function (value) {
            //时间转date的string，还有long2date(value)方法
            return FW.long2date(value);
        }
    },
    {
        field: 'cardLevel', title: '级别', width: 140, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {maxlength: 20}}}
    },
    {
        field: 'validDate', title: '有效期', width: 140, align: 'left', fixed: true,
        editor: {
            type: 'datebox',
            options: {
                dataType: "date", minView: 2, format: "yyyy-mm-dd",
                rules: {
                    required: true, greaterEqualThan: '%cardDate',
                    messages: {
                        greaterEqualThan: "有效日期必须大于领证日期"
                    }
                }
            }
        },
        formatter: function (value) {
            //时间转date的string，还有long2date(value)方法
            return FW.long2date(value);
        }
    },
    garbageColunms,
    {field: "seize", title: "", width: 1}
]];

var credentialsFields = [[
    {field: 'businessId', title: '', hidden: true},
    {
        field: 'credentialsType', title: '类别', width: 160, align: 'left', fixed: true,
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_CERTIFICATE_TYPE")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("ATD_CERTIFICATE_TYPE", _enum),
                "rules": {required: true,}
            },
        }
    },
    {
        field: 'credentialsNo', title: '编号', width: 320, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {required: true, maxlength: 50}}}
    },

    {
        field: 'issuer', title: '签发机关', width: 220, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {maxlength: 100}}}
    },
    {
        field: 'dateType', title: '日期类型', width: 140, align: 'left', fixed: true,
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_CERTIFICATE_DATETYPE")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("ATD_CERTIFICATE_DATETYPE", _enum),
                "rules": {required: true,}
            }
        }
    },
    {
        field: 'credentialsDate', title: '日期', width: 140, align: 'left', fixed: true,
        editor: {
            type: 'datebox',
            options: {dataType: "date", minView: 2, format: "yyyy-mm-dd", "rules": {required: true,}}
        },
        formatter: function (value) {
            //时间转date的string，还有long2date(value)方法
            return FW.long2date(value);
        }
    },
    garbageColunms,
    {field: "seize", title: "", width: 1}
]];

var experinenceFields = [[
    {field: 'businessId', title: '', hidden: true},
    {
        field: 'company', title: '工作单位', width: 480, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {required: true, maxlength: 100}}}
    },

    {
        field: 'duties', title: '职位', width: 220, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {maxlength: 40}}}
    },

    {
        field: 'startDate', title: '开始日期', width: 140, align: 'left', fixed: true,
        editor: {type: 'datebox', options: {dataType: "date", minView: 2, format: "yyyy-mm-dd"}},
        formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {
        field: 'endDate', title: '结束日期', width: 140, align: 'left', fixed: true,
        editor: {
            type: 'datebox',
            options: {
                dataType: "date", minView: 2, format: "yyyy-mm-dd",
                rules: {
                    greaterEqualThan: '%startDate',
                    messages: {
                        greaterEqualThan: "结束日期必须大于开始日期"
                    }
                }
            }

        },
        formatter: function (value) {
            return FW.long2date(value);
        }
    },
    garbageColunms,
    {field: "seize", title: "", width: 1}
]];

var voyageFields = [[
    {field: 'businessId', title: '', hidden: true},
    {field: 'different', title: '', hidden: true},
    {
        field: 'shipName', title: '船名', width: 160, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {required: true, maxlength: 40}}}
    },

    {
        field: 'duties', title: '职务', width: 100, align: 'left', fixed: true,
        formatter: function (value) {
            return FW.getEnumMap("ATD_ DUTIES_TYPE")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("ATD_ DUTIES_TYPE", _enum),
                "rules": {required: true,}
            },

        }
    },
    {
        field: 'power', title: '总吨/马力', width: 100, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {maxlength: 20}}}
    },
    {
        field: 'nationality', title: '国籍', width: 140, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {maxlength: 20}}}
    },
    {
        field: 'company', title: '公司', width: 200, align: 'left', fixed: true,
        editor: {type: "text", options: {rules: {maxlength: 40}}}
    },
    {
        field: 'inShipDate', title: '上船日期', width: 140, align: 'left', fixed: true,
        editor: {type: 'datebox', options: {dataType: "date", minView: 2, format: "yyyy-mm-dd"},"rules": {required: true}},
        formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {
        field: 'outShipDate', title: '下船日期', width: 140, align: 'left', fixed: true,
        editor: {
            type: 'datebox',
            options: {
                dataType: "date", minView: 2, format: "yyyy-mm-dd",
                rules: {
                    greaterEqualThan: '%inShipDate',
                    messages: {
                        greaterEqualThan: "下船日期必须大于上船日期"
                    }
                }
            }
        },
        formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {
        field: 'area', title: '航行区域', width: 120, align: 'left', fixed: true,
        formatter: function (value) {
            return FW.getEnumMap("SAIL_AREA")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("SAIL_AREA", _enum),
            },

        }
    },
    garbageColunms,
    {field: "seize", title: "", width: 1}
]];

var examineFields = [[
    {field: 'businessId', title: '', hidden: true},
    {
        field: 'shipId', title: '船名', width: 160, align: 'left', fixed: true,
        formatter: function (value, row, index) {
            return mgmtShipMap[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": mgmtShipMap,
            },
        }
    },

    {
        field: 'duties', title: '职务', width: 320, align: 'left', fixed: true,
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_ DUTIES_TYPE")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("ATD_ DUTIES_TYPE", _enum),
            },
        }
    },
    {
        field: 'onJobDate', title: '任职时间', width: 220, align: 'left', fixed: true,
        editor: {type: 'datebox', options: {dataType: "date", format: "yyyy-mm-dd"}},
        formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {
        field: 'isFinishContract', title: '完成合同期', width: 140, align: 'left', fixed: true,
        formatter: function (value, row, index) {
            return {Y: "是", N: "否"}[value];
        },
    },
    {
        field: 'testLevel', title: '考核级别', width: 140, align: 'left',
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_EXAMINE_LEVEL")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("ATD_EXAMINE_LEVEL", _enum),
            },
        }
    },
]];

var trainingFields = [[
    {field: 'businessId', title: '', hidden: true},
    {
        field: 'trainingName', title: '培训名称', width: 200, align: 'left', fixed: true,
        editor: {type: "text",}
    },

    {
        field: 'handler', title: '负责人', width: 140, align: 'left', fixed: true,
        editor: {type: "text",}
    },
    {
        field: 'startDate', title: '培训日期', width: 140, align: 'left', fixed: true,
        editor: {type: 'datebox', options: {dataType: "date", minView: 2, format: "yyyy-mm-dd"}},
        formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {
        field: 'address', title: '地点', width: 220, align: 'left', fixed: true,
        editor: {type: "text",}
    },
    {
        field: 'hour', title: '课时', width: 140, align: 'left', fixed: true,
        editor: {type: "text",}
    },
    {
        field: 'createdate', title: '创建时间', width: 140, align: 'left',
        editor: {type: 'datebox', options: {dataType: "date", minView: 2, format: "yyyy-mm-dd"}},
        formatter: function (value) {
            return FW.long2date(value);
        }
    },
]];

var hireinfoFields = [[
    {field: 'businessId', title: '', hidden: true},
    {
        field: 'shipId', title: '调往船舶', width: 160, align: 'left', fixed: true,
        formatter: function (value, row, index) {
            return mgmtShipMap[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": mgmtShipMap,
            },

        }
    },
    {
        field: 'recruitType', title: '招聘对象类型', width: 180, align: 'left', fixed: true,
        formatter: function (value, row, index) {
            return FW.getEnumMap("ATD_SAILOR_TYPE")[value];
        },
        "editor": {
            "type": "combobox",
            "options": {
                "data": FW.parseEnumData("ATD_SAILOR_TYPE", _enum),
            },
        }
    },
    {
        field: 'inShipDate', title: '上船日期', width: 140, align: 'left', fixed: true,
        editor: {type: 'datebox', options: {dataType: "date", minView: 2, format: "yyyy-mm-dd"}},
        formatter: function (value) {
            return FW.long2date(value);
        }
    },
    {
        field: 'remark', title: '原因', width: 140, align: 'left',
        editor: {type: "text",}
    },
]];

var attachMap = businessAttachmentList;
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

//初始化表单
function initPager() {
    $("#" + personinfoDetailText).iFold("show");
    $("#" + personinfoDetailText).iFold("init");
    initDetail();//初始化详细信息
    //初始化表格数据  家庭
    initFamily();//家庭成员
    initCertificates();//证件类别
    initCredentials();//证书
    initExperinence();//工作经历
    initVoyage();//近五年海上经历
    initAttach();
    //编辑进入后 设置表单值
    if (id && id != "" && id != null) {
        setFormDetailVal();
        initExamine();//考核记录
        initTraining();//培训记录
        initHireinfo();//聘用记录
    }
}

//初始化基本信息详情
function initDetail() {
    var mainform = $("#" + personinfoInit);
    mainform.iForm("init", {"fields": mainFields, options: {validate: true}});
    var form = $("#" + personinfoDetailInit);
    form.iForm("init", {"fields": detailFields, options: {validate: true}});
}

//初始化家庭成员
function initFamily() {
    var data = personRelations;
    if (!initFamily.dataGrid) {
        //初始化表格或者表单的标题
        $(familyEle.tabText).iFold();

        initFamily.dataGrid = $(familyEle.tabInit).datagrid({
            fitColumns: true,
            scrollbarSize: 0,
            data: data,
            columns: familyFields,
            onClickCell: function (rowIndex, field, value) {//表格的单行删除事件
                deleteGarbageColumnFunction(rowIndex, field, value, "code", initFamily.dataGrid);
            }

        });
    }
}

//初始化证件
function initCertificates() {
    var data = personCertificateList;
    if (!initCertificates.dataGrid) {
        $(certiEle.tabText).iFold();

        initCertificates.dataGrid = $(certiEle.tabInit).datagrid({
            fitColumns: true,
            scrollbarSize: 0,
            singleSelect: true,
            data: data,
            columns: CertificateFields,
            onClickCell: function (rowIndex, field, value) {//表格的单行删除事件
                deleteGarbageColumnFunction(rowIndex, field, value, "code", initCertificates.dataGrid);
            },
            //判断是否过期，小于90天为快要过期，然后标红和标黄。
            rowStyler: function (index, row) {
                var time = parseInt((row.validDate - Date.parse(new Date())) / 1000 / 3600 / 24);
                if (time < 0) {
                    return 'color:red;';
                } else if (time < 90 && time >= 0) {
                    return 'color:#FFCC00;';
                }
            },

        });
    }
}

//初始化证书
function initCredentials() {
    var data = personcredentialsList;
    if (!initCredentials.dataGrid) {
        $(credEle.tabText).iFold();

        initCredentials.dataGrid = $(credEle.tabInit).datagrid({
            fitColumns: true,
            scrollbarSize: 0,
            data: data,
            columns: credentialsFields,
            onClickCell: function (rowIndex, field, value) {//表格的单行删除事件
                deleteGarbageColumnFunction(rowIndex, field, value, "code", initCredentials.dataGrid);
            },
            rowStyler: function (index, row) {
                //日期为空时，返回不判断
                if (!row.credentialsDate) {
                    return false;
                }
                var time = parseInt((row.credentialsDate - Date.parse(new Date())) / 1000 / 3600 / 24);
                var type = FW.getEnumMap("ATD_CERTIFICATE_DATETYPE")[row.dateType];
                if (time < 0 && type == "有效日期") {
                    return 'color:red;';
                } else if (time < 90 && time >= 0 && type == "有效日期") {
                    return 'color:#FFCC00;';
                }
            },

        });
    }
}

//初始化工作经历
function initExperinence() {
    var data = personexperinenceList;
    if (!initExperinence.dataGrid) {
        $(experEle.tabText).iFold();

        initExperinence.dataGrid = $(experEle.tabInit).datagrid({
            fitColumns: true,
            scrollbarSize: 0,
            data: data,
            columns: experinenceFields,
            onClickCell: function (rowIndex, field, value) {//表格的单行删除事件
                deleteGarbageColumnFunction(rowIndex, field, value, "code", initExperinence.dataGrid);
            }

        });

    }
}

//初始化近五年海上经历
function initVoyage() {
    var data = personvoyageList;
    if (!initVoyage.dataGrid) {
        $(voyage.tabText).iFold();

        initVoyage.dataGrid = $(voyage.tabInit).datagrid({
            fitColumns: true,
            scrollbarSize: 0,
            singleSelect: true,
            data: data,
            columns: voyageFields,
            onClickCell: function (rowIndex, field, value) {//表格的单行删除事件
                deleteGarbageColumnFunction(rowIndex, field, value, "code", initVoyage.dataGrid);
            }

        });

    }
}

//初始化考核记录
function initExamine() {
    var data = personExamineList;
    if (!initExamine.dataGrid) {
        $(examineEle.tabText).iFold();
        initExamine.dataGrid = $(examineEle.tabInit).datagrid({
            fitColumns: true,
            scrollbarSize: 0,
            singleSelect: true,
            data: data,
            columns: examineFields,
            onClickCell: function (rowIndex, field, value) {//表格的单行删除事件
                deleteGarbageColumnFunction(rowIndex, field, value, "code", initExamine.dataGrid);
            },
            onLoadSuccess: function () {
                //没有数据隐藏
                if (data.length < 1) {
                    $(examineEle.tabDiv).hide();
                }
            }
        });

    }
}

//初始化培训记录
function initTraining() {
    var data = trainingInfoList;
    if (!initTraining.dataGrid) {
        $(trainingEle.tabText).iFold();
        initTraining.dataGrid = $(trainingEle.tabInit).datagrid({
            fitColumns: true,
            scrollbarSize: 0,
            singleSelect: true,
            data: data,
            columns: trainingFields,
            onClickCell: function (rowIndex, field, value) {//表格的单行删除事件
                deleteGarbageColumnFunction(rowIndex, field, value, "code", initTraining.dataGrid);
            },
            onLoadSuccess: function () {
                //没有数据隐藏
                if (data.length < 1) {
                    $(trainingEle.tabDiv).hide();
                }
            }
        });

    }
}

//初始化聘用记录
function initHireinfo() {
    var data = hireInfoList;
    if (!initHireinfo.dataGrid) {
        $(hireinfoEle.tabText).iFold();
        initHireinfo.dataGrid = $(hireinfoEle.tabInit).datagrid({
            fitColumns: true,
            scrollbarSize: 0,
            singleSelect: true,
            data: data,
            columns: hireinfoFields,
            onClickCell: function (rowIndex, field, value) {//表格的单行删除事件
                deleteGarbageColumnFunction(rowIndex, field, value, "code", initHireinfo.dataGrid);
            },
            onLoadSuccess: function () {
                //没有数据隐藏
                if (data.length < 1) {
                    $(hireinfoEle.tabDiv).hide();
                }
            }

        });
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
}

//设置详细信息的值
function setFormDetailVal() {
    $("#" + personinfoInit).iForm("setVal", bean);
    $("#" + personinfoDetailInit).iForm("setVal", bean);
}



