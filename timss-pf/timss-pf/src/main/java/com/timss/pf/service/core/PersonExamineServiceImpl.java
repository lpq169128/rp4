package com.timss.pf.service.core;


import com.timss.facade.service.abstr.WfAbstractServiceImpl;
import com.timss.pf.bean.HireInfo;
import com.timss.pf.bean.PersonExamine;
import com.timss.pf.dao.PersonExamineDao;
import com.timss.pf.service.PersonExamineService;
import com.yudean.homepage.facade.ITaskFacade;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.data.DataFormatUtil;
import com.yudean.workflow.service.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonExamineServiceImpl extends WfAbstractServiceImpl<PersonExamine> implements PersonExamineService {
    private Logger log = LoggerFactory.getLogger(PersonExamineServiceImpl.class);
    protected PersonExamineDao personExamineDao;

    public PersonExamineServiceImpl() {
    }

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ITaskFacade iTaskFacade;

    @Autowired
    protected void setPersonExamineDao(PersonExamineDao dao) {
        this.personExamineDao = dao;
        this.setDao(dao);
    }

    /**
     * 重写WfAbstractServiceImpl的方法
     *
     * @param bean
     * @param operator
     * @throws Exception
     */
    public void fixWFBean(@CUDTarget PersonExamine bean, @Operator SecureUser operator) throws Exception {
        if (org.apache.commons.lang3.StringUtils.isBlank(bean.getBusinessUrl())) {
            bean.setBusinessUrl("pf/personExamine/detailPage.do?mode=view&id=" + bean.getBusinessId());
        }
        String desc = DataFormatUtil.substr(bean.getName() + "考核评估", 200);
        if (!desc.equals(bean.getBusinessDesc())) {
            bean.setBusinessDesc(desc);
        }
    }

}
