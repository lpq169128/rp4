<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html style='height:99%'>
<head>
    <title>安全活动列表</title>
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
        console.log(${params.teamMap})
        var initColumns = [[
            {field: 'businessId', hidden: true},
            {
                field: 'mainForm_activityTime', title: '活动时间', width: 140, align: 'left', fixed: true,
                formatter: function (value) {
                    return FW.long2date(new Date(value));
                }
            },
            {
                field: 'mainForm_emcee', title: '主持人', width: 160, align: 'left', fixed: true,
                formatter:function (value) {
                    return value;
                }
            },
            {field: 'mainForm_content', title: '活动内容', width: 180, align: 'left',
            formatter:function (value) {
                return value;
            }},
            {field: 'createUserName', title: '创建人', width: 140, align: 'left', fixed: true,},
            {
                field: 'createdate', title: '创建时间', width: 150, align: 'left', fixed: true,
                formatter: function (value) {
                    return FW.long2time(value);
                }
            },
            {
                field: 'wfStatus', title: '状态', width: 140, align: 'left',
                // formatter:function(value,row,index){
                //     var flowStatus=row["flowStatus"];
                //     var status=row["wfStatus"];
                //     value=getStatus(status,flowStatus);
                //     return value;
                // }
            }
        ]];
        // PageModuleList.initParams(moduleList)
        $(document).ready(function () {
            PageList.init($.extend(true, PageModuleList.initParams(moduleList), {
                datagrid: {
                    id: "moduleDataList",//required
                    detailPage: {
                        url: basePath + "pf/securActivities/moduleDataDetailPage.do?mode=view&moduleCode=" + moduleList.businessType + "&id=",//required
                        createUrl: basePath + "pf/securActivities/moduleDataDetailPage.do?mode=create&moduleCode=" + moduleList.businessType + "&id="//required
                    },
                    params: {
                        idField: "businessId",//required
                        url: basePath + "facade/moduledata/getList.do",//required
                        columns: initColumns,//required
                        singleSelect: true,
                        onLoadSuccess: function (data) {
                            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
                            console.log("列表加载成功后----------");
                            if(data && data.total==0){
                                $("#noSearchResult").show();
                            }else{
                                $("#noSearchResult").hide();
                            }
                        },
                    }
                }
            }));
            // initPriv();
        });
        //设置新建权限
        function initPriv() {
            console.log(isEditable())
            console.log(_parent().privMapping)
            Priv.map("isEditable()", "securActivities_create");
            Priv.apply();
        }

        function isEditable() {
            var privMapping = _parent().privMapping;
            return !!privMapping["securActivities_list_create"];
        }
        //流程状态
        function getStatus(status, flowStatus) {
            // console.log(status + '---'+flowStatus)
            var value = "";
            if (status == "草稿") {
                value = "草稿";
            } else if (status == 'banzhangshenpi') {
                value = "班长审批";
            } else if (status == 'bumenfuzerenshenpi') {
                value = "部门负责人审批";
            }else if (status == 'anquanyuanshenpi') {
                value = "安全员审批";
            }else if (status == 'canyurenqueren') {
                value = "参与人确认";
            }else if (status == "已归档") {
                value = "已归档";
            } else if (status == "作废") {
                value = "作废";
            } else {
                value = "审批中"
            }
            return value;
        }

    </script>
    <style type="text/css">
    </style>
</head>
<body style="height: 100%;" class="bbox list-page">
<div class="toolbar-with-pager bbox">
    <div id="toolbar" class="btn-toolbar ">
        <div class="btn-group btn-group-sm" id="btnCreate">
            <button class="btn-success btn priv" privilege="securActivities_create" onclick="PageList.toCreate()">新建</button>
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
            <div style="font-size:14px">没有找到符合条件的结果<span class="business-type-name"></span></div>
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