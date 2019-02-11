package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.TrainingPerson;
import com.timss.pf.dao.TrainingPersonDao;
import com.timss.pf.service.TrainingPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingPersonServiceImpl extends AbstractServiceImpl<TrainingPerson> implements TrainingPersonService {

    protected TrainingPersonDao dao;

    @Autowired
    protected void setTrainingPersonDao(TrainingPersonDao dao) {
        this.dao = dao;
        setDao(dao);
    }

    @Autowired
    protected void setCrewFileDao(TrainingPersonDao dao) {
        this.dao = dao;
        setDao(dao);
    }

    @Override
    public List<TrainingPerson> getTrainingPersonList(String trainingId) {
        return dao.getTrainingPersonList(trainingId);
    }

    @Override
    public List<TrainingPerson> querTrainingPersonByTrainingId(String id) {
        return dao.querTrainingPersonByTrainingId(id);
    }

    @Override
    public int deleteTrainingPersonByTrainingId(String id) {
        return dao.deleteTrainingPersonByTrainingId(id);
    }
}
