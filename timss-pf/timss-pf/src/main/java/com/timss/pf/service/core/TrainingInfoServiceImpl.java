package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.TrainingInfo;
import com.timss.pf.dao.TrainingInfoDao;
import com.timss.pf.service.TrainingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingInfoServiceImpl extends AbstractServiceImpl<TrainingInfo> implements TrainingInfoService {
    protected TrainingInfoDao dao;

    @Autowired
    protected void setCrewFileDao(TrainingInfoDao dao) {
        this.dao = dao;
        setDao(dao);
    }
}