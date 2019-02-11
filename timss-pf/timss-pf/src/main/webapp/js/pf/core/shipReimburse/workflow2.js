var wfOpts={//工作流初始化的配置
    selector:{
        toolBar:"#buttonToolBar",//按钮栏选择器
        wfBtnsClass:"wf-btn",//标记工作流按钮的css类
        form:"#baseform",//标记表单
    },
    url:basePath+"todo/operate.do",//流程操作的url
    fn:{
        save:"toSave",
        commit:"toCommit",
        execute:"toExecute",
        invalid:"toInvalid",
        "delete":"toDelete",
        withdraw:"toWithdraw",
        rollback:"toRollback",
        showAuditInfo:"toShowAuditInfo",
        delegate:"toDelegate",//代办
        help:"toHelp"//协办
    },
    operatingPostfix:"中...",
    closeAfterExecute:true,//审批后直接关闭页面，否则刷页面
    DEBUG:false//DEBUG模式，有默认按钮，不提交后台
};
var wfBean;//页面用WFBean赋值，在WorkFlow初始化之前赋值，或调用wf2.refresh刷新

var WorkFlow = function(opts,wfBean){//由页面调用初始化
    $.extend(this,{
        opts:{},
        wfBean:{},
        page:{},//页面渲染数据
        formObj:null
    });

    if(this.checkEnv()){
        this.refresh(opts,wfBean);
    }
};

$.extend(true,WorkFlow.prototype,{//工作流类的扩展方法
    /**
     * 检查依存，对应的分别为jQuery、ITC基础组件、模板引擎、Promise Polyfill
     */
    checkEnv:function(){
        var env = ["$", "FW", "template", "Promise"];
        var i, k;
        for(i=0; i<env.length; i++){
            k = env[i];
            if(!window[k]){
                alert("无法找到依存" + k + "，工作流无法正常运行！");
                return false;
            }
        }
        return true;
    },

    /**
     * 根据工作流参数刷新工作流
     */
    refresh:function(opts,wfBeanParam){
        var self=this;
        $.extend(true,self.opts,opts||wfOpts);
        self.wfBean=wfBeanParam||wfBean;
        self.formObj=$(self.opts.selector.form);
        self.refreshBtns();
        self.refreshForm();
    },
    refreshForm:function(){
        var self=this;
        var map={
            r:[],
            w:[],
            h:[]
        }
        if(self.wfBean.nodeConf&&self.wfBean.nodeConf.nodeFieldList&&self.wfBean.nodeConf.nodeFieldList.length>0){
            $.each(self.wfBean.nodeConf.nodeFieldList,function(i,wfField){
                if(wfField.showType){
                    map[wfField.showType].push(wfField.fieldCode);
                }
            });
        }
        if(map.h&&map.h.length>0){
            self.formObj.iForm("hide",map.h);
        }
        if(map.r&&map.r.length>0){
            self.formObj.iForm("endEdit",map.r);
        }
        if(map.w&&map.w.length>0&&self.wfBean.hasAuditPermisson){
            self.formObj.iForm("show",map.w);//字段可能被隐藏，先显示
            self.formObj.iForm("beginEdit",map.w);
        }
    },
    refreshBtns:function(){
        var self=this;
        //清空所有工作流按钮
        $(self.getBtnSelector()).parent().remove();
        if(self.wfBean.isCommited){//已提交流程，根据流程配置显示按钮
            if(self.wfBean.hasAuditPermisson){
                if(self.wfBean.nodeConf&&self.wfBean.nodeConf.nodeButtonList&&self.wfBean.nodeConf.nodeButtonList.length>0){
                    $.each(self.wfBean.nodeConf.nodeButtonList,function(i,button){
                        if(button.buttonCode=="rollback"&&self.wfBean.currentTaskStatus=="help"){//协办不给退回
                            return true;
                        }
                        self.addBtn([{code:button.buttonCode,name:button.buttonName,fn:button.event,dialogTitle:button.title,desc:button.remark}]);
                    });
                }else{
                    if(self.opts.DEBUG){
                        self.addBtn([{code:"execute",name:"默认审批"},{code:"rollback",name:"默认退回"},{code:"invalid",name:"默认作废"}]);
                    }
                }
            }
            if(self.wfBean.hasRetracePermission&&!self.wfBean.isEnd){
                self.addBtn([{code:"withdraw",name:"撤回"}]);
            }
        }else if(self.wfBean.isApplicant){//显示默认按钮save/commit/delete
            self.addBtn([{code:"save",name:"暂存"},{code:"commit",name:"提交"}]);
            if(self.wfBean.businessId){//有id可删除
                self.addBtn([{code:"delete",name:"删除"}]);
            }
        }
        self.addBtn([{code:"showAuditInfo",name:"审批信息",loadingText:"审批信息"}]);
    },
    addBtn:function(btnGroup){//[{code,name,fn,title,desc,loadingText}]
        var self=this;
        if(btnGroup&&btnGroup.length>0){
            $(self.opts.selector.toolBar).append('<div class="btn-group btn-group-sm"></div>');
            var obj=$(self.opts.selector.toolBar).children("div.btn-group:last");
            $.each(btnGroup,function(i,btn){
                obj.append('<button type="button" class="btn-default btn '+self.opts.selector.wfBtnsClass+'" i-show="'+btn.code+'" data-action="'+btn.code+'" data-loading-text="'+(btn.loadingText||(btn.name+self.opts.operatingPostfix))+'" dialog-title="'+(btn.title||btn.name)+'" title="'+(btn.desc||btn.name)+'">'+btn.name+'</button>');
                var fn=btn.fn||self.opts.fn[btn.code];
                if(fn&&typeof(fn)==='string'){//如果是字符串则从工作流中找方法，如果找不到则解析成js对象
                    fn=self[fn]||eval(fn);
                }
                if(fn&&typeof(fn)==='function'){//如果找到了方法则绑定
                    $(self.getBtnSelector(btn.code)).on("click",function(){
                        fn.bind(self,this)();
                    });
                }
            });
        }
    },
    getBtnSelector:function(btnCode){
        var self=this;
        return self.opts.selector.toolBar+" ."+self.opts.selector.wfBtnsClass+(btnCode?"[data-action="+btnCode+"]":"");
    },

    getWfDataForOperate:function(opt){//一般根据操作类型指定operation(必须)、submitType、destTask、participants、auditMsg
        var self=this;
        return $.extend(true,{
            operation:null,//required
            submitType:null,
            businessId:self.wfBean.businessId,
            businessNo:self.wfBean.businessNo,
            businessType:self.wfBean.businessType,
            businessSiteId:self.wfBean.siteid,
            currentTaskId:self.wfBean.currentTaskId,
            processInstId:self.wfBean.processInstId,
            status:self.wfBean.wfStatus,
            destTask:null,
            participants:null,
            auditMsg:null,
            relatedData:FW.stringify(self.getProcessVariableForSubmit())
        },opt);
    },
    getFormDataForSubmit:function(){
        var self=this;
        var formData=self.formObj.iForm("getVal");
        var submitData=formData;/**{};
         if(self.wfBean.isCommited){//已提交流程，根据流程配置获取数据
    		if(self.wfBean.wfFieldList&&self.wfBean.wfFieldList.length>0){
                $.each(self.wfBean.wfFieldList,function(i,wfField){
                    if(wfField.type=="w"){
                        submitData[wfField.fieldCode]=formData[wfField.fieldCode];
                    }
                });
            }
    	}else{//默认提交全部数据
    		submitData=formData;
    	}**/
        return {businessData:FW.stringify(submitData)};
    },
    getProcessVariableForSubmit:function(){
        var self=this;
        var submitData={};
        if(self.wfBean.nodeConf&&self.wfBean.nodeConf.nodeVariableList&&self.wfBean.nodeConf.nodeVariableList.length>0){
            var formData=self.formObj.iForm("getVal");
            $.each(self.wfBean.nodeConf.nodeVariableList,function(i,wfVariable){
                var val=formData[wfVariable.fieldCode];
                if(val){

                }else if(wfVariable.variableVal){
                    val=wfVariable.variableVal;
                }else if(wfVariable.expression){
                    val=eval(wfVariable.expression);
                }
                if(val){
                    if($.inArray(wfVariable.dataType,["Int","Long","Short"])>-1){
                        val=parseInt(val);
                    }else if($.inArray(wfVariable.dataType,["Float","Double"])>-1){
                        val=parseFloat(val);
                    }
                }
                submitData[wfVariable.variableCode]=val;
            });
        }
        return submitData;
    },

    close:function(){
        FW.deleteTabById(FW.getCurrentTabId());
    },
    valid:function(){
        return true;
    },

    toSave:function(target){//向外暴露
        var self=this;
        self.beforeSave(target)
            .then(function(){
                self.save(target);
            });
    },
    beforeSave:function(target){
        var self=this;
        return new Promise(function(resolve,reject){
            if(self.valid()){
                self.changeBtns(target, false);
                return resolve();
            }else{
                return reject();
            }
        });
    },
    save:function(target){
        var self=this;
        self.doOperation({
            operation:"save"
        })
            .then(function(data){
                self.successSave(data,target);
            },function(data){
                self.failSave(data,target);
            });
    },
    successSave:function(data,target){
        var self=this;
        self.refresh();
    },
    failSave:function(data,target){
        var self=this;
        self.changeBtns(target, true);
    },

    toCommit:function(target){//向外暴露
        var self=this;
        if(self.wfBean.processInstId){//有流程了
            self.toExecute(target);
        }else{
            self.beforeCommit(target)
                .then(function(){
                    self.commit(target);
                });
        }
    },
    beforeCommit:function(target){
        var self=this;
        return new Promise(function(resolve,reject){
            if(self.valid()){
                self.changeBtns(target, false);
                return resolve();
            }else{
                return reject();
            }
        });
    },
    commit:function(target){
        var self=this;
        self.doOperation({
            operation:"commit"
        })
            .then(function(data){
                self.successCommit(data,target);
            },function(data){
                self.failCommit(data,target);
            });
    },
    successCommit:function(data,target){
        var self=this;
        self.refresh();
        self.changeBtns(null, false);
        self.showAudit(target,function(form){
            self.execute(target,form);
        },function(){
            self.changeBtns(null, true);
            return true;
        },function(){
            self.changeBtns(null, true);
        });
    },
    failCommit:function(data,target){
        var self=this;
        self.changeBtns(target, true);
    },

    toExecute:function(target){//向外暴露
        var self=this;
        self.beforeExecute(target)
            .then(function(){
                self.showAudit(target,function(form){
                    self.doAudit(target, form);
                },null,function(){
                    //self.refresh();//不能重刷页面，会导致用户未保存的数据丢失
                    self.changeBtns(target, true);
                });
            });
    },
    toDelegate:function (target) {
        var self=this;
        self.beforeDelegate(target)
            .then(function(){
                self.showAudit(target,function(form){
                    self.doAudit(target, form);
                },null,function(){
                    self.changeBtns(target, true);
                },"delegate");
            });
    },
    toHelp:function (target) {
        var self=this;
        self.beforeHelp(target)
            .then(function(){
                self.showAudit(target,function(form){
                    self.doAudit(target, form);
                },null,function(){
                    self.changeBtns(target, true);
                },"help");
            });
    },
    beforeExecute:function(target){
        var self=this;
        return new Promise(function(resolve,reject){
            if(self.valid()){
                self.changeBtns(target, false);
                return resolve();
            }else{
                return reject();
            }
        });
    },
    beforeDelegate:function (target) {
        var self=this;
        return new Promise(function(resolve,reject){
            if(self.valid()){
                self.changeBtns(target, false);
                return resolve();
            }else{
                return reject();
            }
        });
    },

    beforeHelp:function (target) {
        var self=this;
        return new Promise(function(resolve,reject){
            if(self.valid()){
                self.changeBtns(target, false);
                return resolve();
            }else{
                return reject();
            }
        });
    },
    execute:function(target,params){
        var self=this;
        self.doOperation({
            operation:"execute",
            submitType:params.type,
            destTask:params.nextStep,
            participants:params.participants,
            auditMsg:params.msg
        })
            .then(function(data){
                self.successExecute(data,target);
            },function(data){
                self.failExecute(data,target);
            });
    },
    successExecute:function(data,target){
        var self=this;
        self.closeDialog();
        if(self.opts.closeAfterExecute){
            self.close();//直接关页面
        }else{
            self.refresh();//刷页面
        }
    },
    failExecute:function(data,target){
        var self=this;
        self.changeAuditBtns(true);
    },

    toInvalid:function(target){//向外暴露
        var self=this;
        Notice.confirm("确认作废|是否确定要作废"+self.wfBean.businessTypeName+" "+self.wfBean.businessNo+"？该操作无法撤销。",function(){
            self.beforeInvalid(target)
                .then(function(){
                    self.invalid(target);
                });
        });
    },
    beforeInvalid:function(target){
        var self=this;
        return new Promise(function(resolve,reject){
            self.changeBtns(target, false);
            return resolve();
        });
    },
    invalid:function(target){
        var self=this;
        self.doOperation({
            operation:"invalid",
            auditMsg:"作废"
        })
            .then(function(data){
                self.successInvalid(data,target);
            },function(data){
                self.failInvalid(data,target);
            });
    },
    successInvalid:function(data,target){
        var self=this;
        self.closeDialog();
        //self.refresh();
        self.close();//直接关页面
    },
    failInvalid:function(data,target){
        var self=this;
        self.changeBtns(target, true);
    },

    toDelete:function(target){//向外暴露
        var self=this;
        Notice.confirm("确认删除|是否确定要删除"+self.wfBean.businessTypeName+(self.wfBean.businessNo?(" "+self.wfBean.businessNo):"")+"？该操作无法撤销。",function(){
            self.beforeDelete(target)
                .then(function(){
                    self.delete(target);
                });
        });
    },
    beforeDelete:function(target){
        var self=this;
        return new Promise(function(resolve,reject){
            self.changeBtns(target, false);
            return resolve();
        });
    },
    "delete":function(target){
        var self=this;
        self.doOperation({
            operation:"delete"
        })
            .then(function(data){
                self.successDelete(data,target);
            },function(data){
                self.failDelete(data,target);
            });
    },
    successDelete:function(data,target){
        var self=this;
        self.close();
    },
    failDelete:function(data,target){
        var self=this;
        self.changeBtns(target, true);
    },

    toWithdraw:function(target){//向外暴露
        var self=this;
        Notice.confirm("确认撤回|是否确定要撤回"+self.wfBean.businessTypeName+" "+self.wfBean.businessNo+"？该操作无法撤销。",function(){
            self.beforeWithdraw(target)
                .then(function(){
                    self.withdraw(target);
                });
        });
    },
    beforeWithdraw:function(target){
        var self=this;
        return new Promise(function(resolve,reject){
            self.changeBtns(target, false);
            return resolve();
        });
    },
    withdraw:function(target){
        var self=this;
        self.doOperation({
            operation:"withdraw",
            auditMsg:"撤回"
        })
            .then(function(data){
                self.successWithdraw(data,target);
            },function(data){
                self.failWithdraw(data,target);
            });
    },
    successWithdraw:function(data,target){
        var self=this;
        self.refresh();
    },
    failWithdraw:function(data,target){
        var self=this;
        self.changeBtns(target, true);
    },

    toRollback:function(target){//向外暴露
        var self=this;
        self.beforeRollback(target)
            .then(function(){
                self.showAudit(target,function(form){
                    self.doAudit(target, form);
                },null,function(){
                    self.changeBtns(target, true);
                },"rollback");
            });
    },
    beforeRollback:function(target){
        var self=this;
        return new Promise(function(resolve,reject){
            self.changeBtns(target, false);
            return resolve();
        });
    },
    rollback:function(target,params){
        var self=this;
        self.doOperation({
            operation:"rollback",
            destTask:params.nextStep,
            auditMsg:params.msg
        })
            .then(function(data){
                self.successRollback(data,target);
            },function(data){
                self.failRollback(data,target);
            });
    },
    successRollback:function(data,target){
        var self=this;
        self.closeDialog();
        //self.refresh();
        self.close();//直接关页面
    },
    failRollback:function(data,target){
        var self=this;
        self.changeAuditBtns(true);
    },

    toShowAuditInfo:function(target){
        var self=this;
        self.beforeShowAuditInfo(target)
            .then(function(){
                self.showAuditInfo(target,function(){
                    self.changeBtns(target, true);
                });
            });
    },
    beforeShowAuditInfo:function(target){
        var self=this;
        return new Promise(function(resolve,reject){
            self.changeBtns(target, false);
            return resolve();
        });
    },
    showAuditInfo:function(target,closeCallback){
        var self=this;

        FW.set("wfBean", self.wfBean);

        var btns=[{
            name: "关闭",
            float: "right",
            style: "btn-deafult",
            onclick: function(){
                return true;
            }
        }];
        if(self.wfBean.isCommited){//已提交流程，根据流程配置显示按钮
            if(self.wfBean.hasRetracePermission&&!self.wfBean.isEnd){
                btns.push(self.getAuditInfoBtnOpts({code:"withdraw",name:"撤回"}));
            }
            if(self.wfBean.hasAuditPermisson){
                if(self.wfBean.nodeConf&&self.wfBean.nodeConf.nodeButtonList&&self.wfBean.nodeConf.nodeButtonList.length>0){
                    for(var i=self.wfBean.nodeConf.nodeButtonList.length-1;i>-1;i--){//倒序放置
                        var button=self.wfBean.nodeConf.nodeButtonList[i];
                        if(button.buttonCode=="rollback"&&self.wfBean.currentTaskStatus=="help"){//协办不给退回
                            continue;
                        }
                        btns.push(self.getAuditInfoBtnOpts({code:button.buttonCode,name:button.buttonName}));
                    }
                }else{
                    if(self.opts.DEBUG){
                        btns.push(self.getAuditInfoBtnOpts({code:"invalid",name:"默认作废"}));
                        btns.push(self.getAuditInfoBtnOpts({code:"rollback",name:"默认退回"}));
                        btns.push(self.getAuditInfoBtnOpts({code:"execute",name:"默认审批",style:"btn-success"}));
                    }
                }
            }
        }

        FW.dialog("init", {
            src: basePath + "page/wf_new/audit_info.jsp",
            dlgOpts: {
                width: 852,
                height: 484,
                title: $(target).attr("dialog-title")||"审批信息",
                idSuffix: "AuditInfo",
                onClose:function(){
                    if(closeCallback&&typeof(closeCallback)==='function'){
                        closeCallback();
                    }
                }
            },
            btnOpts: btns
        });

        _parent().$("#itcDlgAuditInfo").parent(".panel.window").css("visibility","hidden");
        _parent().$("#itcDlgAuditInfo").parent(".panel.window").next(".window-shadow").css("visibility","hidden");
    },
    getAuditInfoBtnOpts:function(btn){
        var self=this;
        return {
            name: btn.name,
            float: "right",
            style: btn.style||"btn-deafult",
            onclick: function(){
                var win = _parent().window.document.getElementById("itcDlgAuditInfoContent").contentWindow;
                $(self.getBtnSelector(btn.code)).click();
            }
        };
    },

    showAudit:function(target,okCallback,cancelCallback,closeCallback,operate){//operate取值next/rollback/delegate/help
        var self=this;

        var procData={
            isDraft:!self.wfBean.isCommited,
            actionMask:"",
            taskId:self.wfBean.currentTaskId,
            operate:operate||"next",
            taskStatus:self.wfBean.currentTaskStatus
        };
        if(self.wfBean.nodeConf){
            $.extend(procData,{
                auditMsg:self.wfBean.nodeConf.approvalOpinion
            });
        }
        FW.set("procData", procData);

        FW.set("relatedDataJson",FW.stringify(self.getProcessVariableForSubmit()));

        FW.dialog("init", {
            src: basePath + "page/wf_new/audit.jsp",
            dlgOpts: {
                width: 480,
                height: 290,
                title: $(target).attr("dialog-title")||"审批",
                idSuffix: "Audit",
                onClose:function(){
                    if(closeCallback&&typeof(closeCallback)==='function'){
                        closeCallback();
                    }
                }
            },
            btnOpts: [{
                name: "取消",
                float: "right",
                style: "btn-deafult",
                onclick: function(){
                    if(cancelCallback&&typeof(cancelCallback)==='function'){
                        return cancelCallback();
                    }
                    return true;
                }
            },{
                name: "确定",
                float: "right",
                style: "btn-success",
                onclick: function(){
                    self.changeAuditBtns(false);
                    var win = _parent().window.document.getElementById("itcDlgAuditContent").contentWindow;
                    win.WFAudit.beforeExecute().then(function(form){
                        if(okCallback&&typeof(okCallback)==='function'){
                            okCallback(form);
                        }
                    }).catch(function(err) {
                        FW.error(err.message);
                        self.changeAuditBtns(true);
                    });
                }
            }]
        });

        _parent().$("#itcDlgAudit").parent(".panel.window").css("visibility","hidden");
        _parent().$("#itcDlgAudit").parent(".panel.window").next(".window-shadow").css("visibility","hidden");
    },
    doAudit:function(target,form){//执行审批选择的操作
        var self=this;
        if(form.operate=="next"){
            if(self.valid()){
                self.execute(target,form);
            }
        }else if(form.operate=="rollback"){
            self.rollback(target,form);
        }else if(form.operate=="delegate"){
            form.nextStep = wfBean.currentTaskDefKey;
            self.execute(target,form);
        }else if(form.operate=="help"){
            form.nextStep = wfBean.currentTaskDefKey;
            self.execute(target,form);
        }
    },
    changeAuditBtns:function(enable){
        var self=this;
        var auditDialogId="Audit";
        var id="#itcDlg"+auditDialogId+"Btn_1";
        var btnObj=_parent().$(id+" button");
        if(enable){
            btnObj.removeClass("disabled");
            btnObj.html("确定");
        }else{
            btnObj.addClass("disabled");
            btnObj.html("处理中...");
        }
    },
    changeBtns:function(target,enable){
        var self=this;
        if(enable){
            if(target){
                $(target).button("reset");
            }
            $(self.getBtnSelector()).attr("disabled",null);
        }else{
            $(self.getBtnSelector()).attr("disabled","disabled");
            if(target){
                $(target).button("loading");
            }
        }
    },
    closeDialog:function(dialogId){
        try{
            if(dialogId){
                _parent().$(dialogId).dialog("close");
            }else{
                _parent().$("#itcDlgAudit").dialog("close");
                _parent().$("#itcDlgAuditInfo").dialog("close");
            }
        }catch(e){
            console.error(e);
        }
    },
    doOperation:function(operationOpt){
        var self=this;
        return new Promise(function(resolve,reject){
            operationOpt=self.getWfDataForOperate(operationOpt);
            if(!operationOpt||!operationOpt.operation){
                FW.error("未指定执行的工作流操作");
                return reject();
            }
            var postData=$.extend({
                operateData:FW.stringify(operationOpt)
            },self.getFormDataForSubmit());
            if(self.opts.DEBUG){
                return resolve(postData);
            }
            $.ajax({
                type:"post",
                dataType:"json",
                url:self.opts.url,
                data:postData,
                success:function(data){
                    if(data.bean){
                        var reimburseDate=data.bean.reimburseDate;
                        reimburseDate=dateFormat(reimburseDate,"yyyy-MM-dd");
                        data.bean.reimburseDate=reimburseDate;
                        FW.success("操作成功");
                        self.wfBean=wfBean=data.bean;
                        self.page=data.page;
                        return resolve(data);
                    }else{
                        FW.error("操作异常，没有返回结果");
                        return reject(data);
                    }
                },
                error: function (data) {
                    if(data&&data.responseJSON){
                        FW.error(data.responseJSON.extData?data.responseJSON.extData:data.responseJSON.msg);
                    }else{
                        FW.error("操作失败");
                    }
                    return reject(data);
                }
            });
        });
    }
});

function isStyleFix(){
    return typeof(StyleFix)=="object"&&StyleFix.objs.isInited;
}