package com.timss.pf.service;



import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.bean.module.ModuleData;
import com.yudean.mvc.service.abstr.WfAbstractService;

import java.util.Map;


/**
 * 反事故演习service
 *
 */
public interface SecurityActivitiesService extends WfAbstractService<ModuleData> {
    ModuleData getNewBean(String var1, SecureUser var2) throws Exception;

    Page<ModuleData> queryListOnlyMain(Page<ModuleData> var1) throws Exception;

    Integer deleteByParams(Map<String, Object> var1, Boolean var2, SecureUser var3) throws Exception;
}