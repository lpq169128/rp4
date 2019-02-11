package com.timss.pf.dao;

import com.timss.pf.bean.TrainingPerson;
import com.yudean.mvc.dao.abstr.WfAbstractDao;

import java.util.List;

public interface TrainingPersonDao extends WfAbstractDao<TrainingPerson> {
    int deleteTrainingPersonByTrainingId(String id);

    List<TrainingPerson> getTrainingPersonList(String trainingId);

    List<TrainingPerson> querTrainingPersonByTrainingId(String id);


    List<TrainingPerson> queryTrainingPersonById(String id);

}
