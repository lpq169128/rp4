package com.timss.pf.dao;

import com.timss.pf.bean.ReimburseCost;
import com.yudean.mvc.dao.abstr.AbstractDao;

import java.util.List;


public interface ReimburseCostDao extends AbstractDao<ReimburseCost> {

    List<ReimburseCost> queryReimburseCostByReimburseId(String reimburseId);

}
