package com.timss.pf.dao;

import com.timss.pf.bean.TrainingInfo;
import com.yudean.mvc.dao.abstr.WfAbstractDao;

import java.util.List;

public interface TrainingInfoDao extends WfAbstractDao<TrainingInfo> {
    List<TrainingInfo> queryTrainingInfoById(String id);
}
