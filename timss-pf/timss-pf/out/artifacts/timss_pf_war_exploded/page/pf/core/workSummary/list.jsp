<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html style='height:99%'>
<head>
    <title>工作总结列表</title>
    <script type="text/javascript">
        _useLoadingMask = true;
    </script>
    <jsp:include page="/page/mvc/mvc_include.jsp" flush="false"/>
    <script type="text/javascript" src="${basePath}js/common/utils.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageList.js?ver=${iVersion}"></script>
    <script type="text/javascript" src="${basePath}js/common/pageModuleList.js?ver=${iVersion}"></script>


    <script>
        var moduleCode = "${moduleCode}";
        var moduleList =${params.moduleList};
        var teamList = ${params.teamMap};
        var teamArr = [];
        var index = 0;
        for(var key in teamList){
            teamArr[index] = [key,teamList[key]];
            index++;
        }

        var month = [[1,1],[2,2],[3,3],[4,4],[5,5],[6,6],[7,7],[8,8],[9,9],[10,10],[11,11],[12,12]];
        var initColumns = [[
            {field: 'businessId', hidden: true},
            {
                field: 'mainForm_team', title: '录入班组', width: 150, align: 'left',fixed: true,
                formatter: function (value) {
                    return teamList[value];
                },
                "editor": {
                    "type": "combobox",
                    "options": {
                        "data": teamArr
                    }
                }
            },
            // {
            //     field: 'mainForm_year', title: '年', width: 140, align: 'left',
            //     formatter: function (value) {
            //         return value;
            //     },
            //     "editor": {
            //         "type": "text",
            //     }
            // },
            // {
            //     field: 'mainForm_month', title: '月', width: 140, align: 'left', fixed: true,
            //     formatter: function (value) {
            //         return value;
            //     },
            //     "editor": {
            //         "type": "combobox",
            //         "options": {
            //             "data": month
            //         }
            //     }
            // },
            {
                field:'mainForm_planName',title:'标题', width: 500, align: 'left',
                formatter:function (value) {
                  return value;
                }
            },
            {
                field: 'createUserName', title: '创建人', width: 140, align: 'left',fixed: true,
                formatter: function (value) {
                   return value;
                },
                "editor": {
                    "type": "text",
                }
            },
            {
                field: 'createdate', title: '创建时间', width: 160, align: 'left',fixed: true,
                formatter: function (value) {
                    return FW.long2time(value);
                }
            },
            {
                field: 'wfStatus', title: '状态', width: 210, align: 'left',fixed: true,
                // formatter: function (value, row, index) {
                    // console.log(value)
                    // var flowStatus = row["flowStatus"];
                    // var status = row["wfStatus"];
                    // value = getStatus(status, flowStatus);
                    // return value;
                // }
            },

        ]];

        $(document).ready(function () {
            PageList.init($.extend(true, PageModuleList.initParams(moduleList,{
                isGridFieldCodeWithFormCode:false,//列表的字段编码是否带表单编码
            }), {
                datagrid: {
                    id: "moduleDataList",//required
                    detailPage: {
                        url: basePath + "pf/workSummary/moduleDataDetailPage.do?mode=view&moduleCode=" + moduleList.businessType + "&id=",//required
                        createUrl: basePath + "pf/workSummary/moduleDataDetailPage.do?mode=create&moduleCode=" + moduleList.businessType + "&id="//required
                    },
                    params: {
                        idField: "businessId",//required
                        url: basePath + "facade/moduledata/getList.do",//required
                        columns: initColumns,//required
                    }
                }
            }));
            // initPriv();
        });

        //设置新建权限
        function initPriv() {
            Priv.map("isEditable()", "worksummary_create");
            Priv.apply();
        }

        function isEditable() {
            var privMapping = _parent().privMapping;
            return !!privMapping["worksummary_list_create"];
        }


    </script>
    <style type="text/css">
    </style>
</head>
<body style="height: 100%;" class="bbox list-page">
<div class="toolbar-with-pager bbox">
    <div id="toolbar" class="btn-toolbar ">
        <div class="btn-group btn-group-sm" id="btnCreate">
            <button class="btn-success btn priv" privilege="worksummary_create" onclick="PageList.toCreate()">新建</button>
        </div>
        <div class="btn-group btn-group-sm" id="btnSearch">
            <button onclick="PageList.toShowSearchLine()" class="btn btn-default">查询</button>
        </div>
    </div>
    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager">
    </div>
</div>

<!--这里要清掉分页器的右浮动效果-->
<div style="clear:both"></div>
<div id="grid_wrap" style="width:100%">
    <table id="moduleDataList" pager="#pagination" class="eu-datagrid">
    </table>
</div>
<div id="noColumns" style="display:none;width:100%;height:62%">
    <div style="height:100%;display:table;width:100%">
        <div style="display:table-cell;vertical-align:middle;text-align:center">
            <div style="font-size:14px"><span class="business-type-name"></span>没有可显示的字段</div>
        </div>
    </div>
</div>
<div id="noSearchResult" style="display:none;width:100%;">
    <div style="height:100%;display:table;width:100%">
        <div style="display:table-cell;vertical-align:middle;text-align:center">
            <div style="font-size:14px">没有查询到相关<span class="business-type-name"></span></div>
        </div>
    </div>
</div>
<!-- 无数据 -->
<div id="grid_empty" style="display:none;width:100%;height:62%">
    <div style="height:100%;display:table;width:100%">
        <div style="display:table-cell;vertical-align:middle;text-align:center">
            <div style="font-size:14px">没有查询到相关数据</div>
            <div class="btn-group btn-group-sm margin-element">
                <button class="btn-success btn priv" privilege="VIRTUAL-NEW" onclick="PageList.toCreate()">新建</button>
            </div>
        </div>
    </div>
</div>
<!--大表需要加下分页器-->
<div id="bottomPager" style="width:100%">
</div>
</body>
</html>