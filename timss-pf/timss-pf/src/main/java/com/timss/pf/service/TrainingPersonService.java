package com.timss.pf.service;

import com.timss.pf.bean.TrainingPerson;
import com.yudean.mvc.service.abstr.AbstractService;

import java.util.List;

public interface TrainingPersonService extends AbstractService<TrainingPerson> {
    int deleteTrainingPersonByTrainingId(String id);

    List<TrainingPerson> getTrainingPersonList(String trainingId);

    List<TrainingPerson> querTrainingPersonByTrainingId(String id);


}
