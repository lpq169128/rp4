package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.facade.service.abstr.WfAbstractServiceImpl;
import com.timss.pf.bean.HirePersonInfo;
import com.timss.pf.dao.HirePersonInfoDao;
import com.timss.pf.service.HirePersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HirePersonInfoServiceImpl extends AbstractServiceImpl<HirePersonInfo> implements HirePersonInfoService {

    protected HirePersonInfoDao dao;

    @Autowired
    protected void setHirePersonInfoDao(HirePersonInfoDao dao) {
        this.dao = dao;
        setDao(dao);
    }

}
