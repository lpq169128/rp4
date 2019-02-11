package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.TrainingEffect;
import com.timss.pf.dao.TrainingEffectDao;
import com.timss.pf.service.TrainingEffectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingEffectServiceImpl extends AbstractServiceImpl<TrainingEffect> implements TrainingEffectService {
    protected TrainingEffectDao dao;

    @Autowired
    protected void setCrewFileDao(TrainingEffectDao dao) {
        this.dao = dao;
        setDao(dao);
    }

    @Override
    public List<TrainingEffect> getTrainingEffectList(String trainingId) {
        return dao.getTrainingEffectList(trainingId);
    }

    @Override
    public List<TrainingEffect> querTrainingEffectByTrainingId(String id) {
        return dao.querTrainingEffectByTrainingId(id);
    }

    @Override
    public int deleteTrainingEffectByTrainingId(String id) {
        return dao.deleteTrainingEffectByTrainingId(id);
    }
}
