package com.timss.pf.service;

import com.timss.pf.bean.TrainingEffect;
import com.yudean.mvc.service.abstr.AbstractService;

import java.util.List;

/**
 * 培训效果service
 */
public interface TrainingEffectService extends AbstractService<TrainingEffect> {
    int deleteTrainingEffectByTrainingId(String id);

    List<TrainingEffect> getTrainingEffectList(String trainingId);

    List<TrainingEffect> querTrainingEffectByTrainingId(String id);


}
