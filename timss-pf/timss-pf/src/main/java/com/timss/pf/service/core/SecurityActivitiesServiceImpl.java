package com.timss.pf.service.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.timss.facade.service.abstr.WfAbstractServiceImpl;
import com.timss.pf.service.SecurityActivitiesService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.annotation.workflow.ProcessDetail;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.support.ISequenceManager;
import com.yudean.itc.util.data.DataFormatUtil;
import com.yudean.module.dao.ModuleDataDao;
import com.yudean.module.service.FormDataService;
import com.yudean.module.service.FormFieldService;
import com.yudean.module.service.FormService;
import com.yudean.mvc.bean.module.*;
import com.yudean.mvc.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SecurityActivitiesServiceImpl extends WfAbstractServiceImpl<ModuleData> implements SecurityActivitiesService {
    public void fixWFBean(ModuleData bean, SecureUser operator) throws Exception {
        if (StringUtils.isBlank(bean.getBusinessUrl())) {
            bean.setBusinessUrl("pf/securActivities/moduleDataDetailPage.do?mode=view&moduleCode=" + bean.getBusinessType() + "&id=" + bean.getBusinessId());
        }
//        String desc = DataFormatUtil.substr(this.serviceUtil.fixExpressionFromBean(bean.getModule().getDescExpression(), bean, operator), Integer.valueOf(200));
        String desc = DataFormatUtil.substr("安全活动记录", 200);
        if (!desc.equals(bean.getBusinessDesc())) {
            bean.setBusinessDesc(desc);
        }
    }

    protected ModuleDataDao dao;
    @Autowired
    FormDataService formDataService;
    @Autowired
    FormService formService;
    @Autowired
    FormFieldService formFieldService;
    @Autowired
    private ISequenceManager sequenceManager;

    public SecurityActivitiesServiceImpl() {
    }

    @Autowired
    protected void setModuleDataDao(ModuleDataDao dao) {
        this.dao = dao;
        this.setDao(dao);
    }

    public ModuleData queryById(String id) throws Exception {
        String[] params = id.split("_");
        return this.dao.queryModuleDataById(params[0], params[1], params[2]);
    }

    @Transactional(
            propagation = Propagation.REQUIRED
    )
    public Integer insert(@CUDTarget ModuleData bean, @Operator SecureUser operator) throws Exception {
        logger.debug("start ModuleDataServiceImpl.insert:" + (new Date()).getTime());
        this.serviceUtil.beforeInsert(bean, operator);
        this.selfService.fixBean(bean, operator);
        if (StringUtils.isBlank(bean.getBusinessNo())) {
            bean.setBusinessNo(this.sequenceManager.getGeneratedId(bean.getModule().getNoSeq()));
        }

        Integer result = this.dao.insert(bean);
        bean.setBusinessId(bean.getBusinessType() + "_" + bean.getSiteid() + "_" + bean.getBusinessId());
        if (result.intValue() > 0) {
            this.selfService.saveAttachment(bean, operator);
            this.selfService.saveItem(bean, operator);
        }

        logger.debug("finish ModuleDataServiceImpl.insert:" + (new Date()).getTime());
        return result;
    }

    @Transactional(
            propagation = Propagation.REQUIRED
    )
    public Integer update(@CUDTarget ModuleData bean, @Operator SecureUser operator) throws Exception {
        this.selfService.fixBean(bean, operator);
        bean.setBusinessId(bean.getBusinessId().split("_")[2]);
        this.serviceUtil.beforeUpdate(bean, operator);
        Integer result = this.dao.update(bean);
        bean.setBusinessId(bean.getBusinessType() + "_" + bean.getSiteid() + "_" + bean.getBusinessId());
        if (result.intValue() > 0) {
            this.selfService.saveAttachment(bean, operator);
            this.selfService.saveItem(bean, operator);
        }

        return result;
    }

    @Transactional(
            propagation = Propagation.REQUIRED
    )
    public Integer delete(String id, @Operator SecureUser operator) throws Exception {
        String[] params = id.split("_");
        return this.dao.deleteModuleData(params[0], params[1], params[2], operator.getId(), new Date());
    }

    @Transactional(
            propagation = Propagation.REQUIRED
    )
    public Integer updateAuditStatus(String businessId, String processInstId, String wfStatus, @Operator SecureUser operator) throws Exception {
        if (StringUtils.isBlank(wfStatus)) {
            wfStatus = "已归档";
        }

        String[] params = businessId.split("_");
        Integer result = this.dao.updateModuleDataAuditStatus(params[0], params[1], params[2], processInstId, wfStatus, operator.getId(), new Date());
        return result;
    }

    public Page<ModuleData> queryList(Page<ModuleData> page) throws Exception {
        String[] formFieldCodeList = this.initFormFieldCodeList(page);
        List<ModuleData> result = this.dao.queryList(page);
        Iterator var5 = result.iterator();

        while(var5.hasNext()) {
            ModuleData moduleData = (ModuleData)var5.next();
            String[] var9 = formFieldCodeList;
            int var8 = formFieldCodeList.length;

            for(int var7 = 0; var7 < var8; ++var7) {
                String formFieldCode = var9[var7];
                Object data = moduleData.remove(formFieldCode);
                moduleData.put(formFieldCode, data);
            }
        }

        page.setResults(result);
        return page;
    }

    public void setItem(@CUDTarget ModuleData bean, @Operator SecureUser operator) throws Exception {
        String[] params = bean.getBusinessId().split("_");
        List<Form> formList = this.formService.queryByModuleId(bean.getBusinessTypeId(), true, operator);
        Iterator var6 = formList.iterator();

        while(true) {
            while(var6.hasNext()) {
                Form form = (Form)var6.next();
                List formData;
                if ("form".equals(form.getType())) {
                    formData = this.formDataService.queryFormDataDetail(params[2], form, (String[])null);
                    bean.setItemMap(form.getFormCode(), formData != null && formData.size() > 0 ? (FormData)formData.get(0) : null);
                } else if ("grid".equals(form.getType())) {
                    formData = this.formDataService.queryFormDataList(params[2], form, (String[])null);
                    bean.setItemMap(form.getFormCode(), formData);
                }
            }

            return;
        }
    }

    private FormData getFormDataFromJsonObject(Form form, JSONObject obj) throws Exception {
        FormData result = new FormData();
        result.setBusinessId(obj.getString("businessId"));
        Iterator var5 = form.getFormFieldList().iterator();

        while(var5.hasNext()) {
            FormField field = (FormField)var5.next();
            Object valObj = "grid".equals(form.getType()) ? obj.get(field.getFormCode() + "_" + field.getFieldCode()) : obj.get(field.getFieldCode());
            if (valObj != null) {
                String val = String.valueOf(valObj);
                if (StringUtils.isNotBlank(val)) {
                    result.put(field.getFieldCode(), val);
                }
            }
        }

        return result;
    }

    private List<FormData> getFormDataFormJsonArray(Form form, JSONArray array) throws Exception {
        List<FormData> result = new ArrayList();

        for(int i = 0; i < array.size(); ++i) {
            result.add(this.getFormDataFromJsonObject(form, array.getJSONObject(i)));
        }

        return result;
    }

    private Boolean checkItemIsNotNull(Object obj) throws Exception {
        return obj == null || (!(obj instanceof Map) || ((Map)obj).isEmpty()) && (!(obj instanceof List) || ((List)obj).isEmpty()) ? false : true;
    }

    @Transactional(
            propagation = Propagation.REQUIRED
    )
    public void saveItem(@CUDTarget ModuleData bean, @Operator SecureUser operator) throws Exception {
        logger.debug("start ModuleDataServiceImpl.saveItem:" + (new Date()).getTime());
        String[] params = bean.getBusinessId().split("_");
        if (bean.getModule() == null) {
            Module module = this.moduleService.queryByCode(bean.getBusinessType(), bean.getSiteid());
            if (module == null) {
                throw new ValidationException("module of " + bean.getBusinessType() + ":" + bean.getSiteid() + " is not exist");
            }

            bean.setModule(module);
        }

        List<Form> formList = this.formService.queryByModuleId(bean.getModule().getBusinessId(), true, operator);
        Iterator var6 = formList.iterator();

        while(var6.hasNext()) {
            Form form = (Form)var6.next();
            ArrayList formDataList;
            if (this.checkItemIsNotNull(bean.getInsertItemMap(form.getFormCode())).booleanValue()) {
                formDataList = new ArrayList();
                if ("form".equals(form.getType())) {
                    if (bean.getInsertItemMap(form.getFormCode()) instanceof JSONObject) {
                        formDataList.add(this.getFormDataFromJsonObject(form, (JSONObject)bean.getInsertItemMap(form.getFormCode())));
                    } else {
                        formDataList.add((FormData)bean.getInsertItemMap(form.getFormCode()));
                    }
                } else if ("grid".equals(form.getType())) {
                    if (bean.getInsertItemMap(form.getFormCode()) instanceof JSONArray) {
                        formDataList.addAll(this.getFormDataFormJsonArray(form, (JSONArray)bean.getInsertItemMap(form.getFormCode())));
                    } else {
                        formDataList.addAll((List)bean.getInsertItemMap(form.getFormCode()));
                    }
                }

                this.formDataService.batchInsert(params[2], form, formDataList, operator);
            }

            if (this.checkItemIsNotNull(bean.getUpdateItemMap(form.getFormCode())).booleanValue()) {
                formDataList = new ArrayList();
                if ("form".equals(form.getType())) {
                    if (bean.getUpdateItemMap(form.getFormCode()) instanceof JSONObject) {
                        formDataList.add(this.getFormDataFromJsonObject(form, (JSONObject)bean.getUpdateItemMap(form.getFormCode())));
                    } else {
                        formDataList.add((FormData)bean.getUpdateItemMap(form.getFormCode()));
                    }
                } else if ("grid".equals(form.getType())) {
                    if (bean.getUpdateItemMap(form.getFormCode()) instanceof JSONArray) {
                        formDataList.addAll(this.getFormDataFormJsonArray(form, (JSONArray)bean.getUpdateItemMap(form.getFormCode())));
                    } else {
                        formDataList.addAll((List)bean.getUpdateItemMap(form.getFormCode()));
                    }
                }

                this.formDataService.batchUpdate(params[2], form, formDataList, operator);
            }

            if (this.checkItemIsNotNull(bean.getDeleteItemMap(form.getFormCode())).booleanValue()) {
                formDataList = new ArrayList();
                if ("form".equals(form.getType())) {
                    if (bean.getDeleteItemMap(form.getFormCode()) instanceof JSONObject) {
                        formDataList.add(this.getFormDataFromJsonObject(form, (JSONObject)bean.getDeleteItemMap(form.getFormCode())));
                    } else {
                        formDataList.add((FormData)bean.getDeleteItemMap(form.getFormCode()));
                    }
                } else if ("grid".equals(form.getType())) {
                    if (bean.getDeleteItemMap(form.getFormCode()) instanceof JSONArray) {
                        formDataList.addAll(this.getFormDataFormJsonArray(form, (JSONArray)bean.getDeleteItemMap(form.getFormCode())));
                    } else {
                        formDataList.addAll((List)bean.getDeleteItemMap(form.getFormCode()));
                    }
                }

                this.formDataService.batchDelete(params[2], form, formDataList, operator);
            }
        }

        logger.debug("finish ModuleDataServiceImpl.saveItem:" + (new Date()).getTime());
    }

    @ProcessDetail
    public ModuleData getNewBean(String moduleCode, SecureUser operator) throws Exception {
        ModuleData result = (ModuleData)this.getNewBean(operator);
        result.setBusinessType(moduleCode);
        return result;
    }

    public void saveAttachment(ModuleData bean, @Operator SecureUser operator) throws Exception {
        if (!bean.getAttachmentIdListMap().isEmpty()) {
            Iterator var4 = bean.getAttachmentIdListMap().keySet().iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                List<String> attachmentIdList = (List)bean.getAttachmentIdListMap().get(key);
                this.businessAttachmentService.insertOrUpdateBusinessAttachment(bean.getBusinessType(), bean.getBusinessId().split("_")[2], key, bean.getWfStatus(), attachmentIdList, operator);
            }
        }

    }

    @ProcessDetail
    public ModuleData queryDetail(@CUDTarget ModuleData bean, @Operator SecureUser operator) throws Exception {
        if (bean != null) {
            bean.setAttachmentListMap(this.selfService.getAttachment(bean.getBusinessType(), bean.getBusinessId().split("_")[2], (String)null, (String)null));
            this.selfService.setItem(bean, operator);
        }

        return bean;
    }

    private String[] initFormFieldCodeList(Page<ModuleData> page) throws Exception {
        String[] formFieldCodeList = (String[])page.getParams().get("formFieldCodeList");
        if (formFieldCodeList == null || formFieldCodeList.length == 0) {
            String moduleCode = (String)page.getParams().get("moduleCode");
            String siteId = (String)page.getParams().get("siteId");
            List<FormField> formFields = this.formFieldService.queryByModuleCodeWithAttr(moduleCode, true, siteId);
            if (formFields != null && formFields.size() > 0) {
                int formFieldSize = formFields.size();
                formFieldCodeList = new String[formFieldSize];

                for(int i = 0; i < formFieldSize; ++i) {
                    formFieldCodeList[i] = ((FormField)formFields.get(i)).getFormCode() + "_" + ((FormField)formFields.get(i)).getFieldCode();
                }
            }

            page.setParameter("formFieldCodeList", formFieldCodeList);
        }

        return formFieldCodeList;
    }

    public Page<ModuleData> queryListOnlyMain(Page<ModuleData> page) throws Exception {
        List<ModuleData> result = this.dao.queryListOnlyMain(page);
        page.setResults(result);
        return page;
    }

    @Transactional(
            propagation = Propagation.REQUIRED
    )
    public Integer deleteByParams(Map<String, Object> params, Boolean logicDelete, SecureUser operator) throws Exception {
        return this.dao.deleteByParams(params, logicDelete, operator.getId(), new Date());
    }
}
