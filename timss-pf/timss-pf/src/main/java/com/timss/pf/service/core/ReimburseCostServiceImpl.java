package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;

import com.timss.pf.bean.ReimburseCost;
import com.timss.pf.dao.ReimburseCostDao;
import com.timss.pf.service.ReimburseCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReimburseCostServiceImpl extends AbstractServiceImpl<ReimburseCost> implements ReimburseCostService {
    protected ReimburseCostDao reimburseCostDao;

    @Autowired
    protected void setReimburseCostDao(ReimburseCostDao dao) {
        this.reimburseCostDao = dao;
        this.setDao(dao);
    }

}
