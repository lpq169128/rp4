package com.timss.pf.dao;

import com.timss.pf.bean.TrainingEffect;
import com.yudean.mvc.dao.abstr.WfAbstractDao;

import java.util.List;

public interface TrainingEffectDao extends WfAbstractDao<TrainingEffect> {
    int deleteTrainingEffectByTrainingId(String id);

    List<TrainingEffect> getTrainingEffectList(String trainingId);

    List<TrainingEffect> querTrainingEffectByTrainingId(String id);


}
