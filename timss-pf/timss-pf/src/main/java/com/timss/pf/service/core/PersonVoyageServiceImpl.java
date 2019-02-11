package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.PersonVoyage;
import com.timss.pf.dao.PersonVoyageDao;
import com.timss.pf.service.PersonVoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonVoyageServiceImpl extends AbstractServiceImpl<PersonVoyage> implements PersonVoyageService {

    protected PersonVoyageDao dao;

    @Autowired
    protected void setAtdPersonVoyageDao(PersonVoyageDao dao) {
        this.dao = dao;
        setDao(dao);
    }

    @Override
    public List<PersonVoyage> queryPersonVoyageByPersonId(String id) {
        return dao.queryPersonVoyageByPersonId(id);
    }

    @Override
    public int deletePersonVoyageById(String id) {
        return dao.deletePersonVoyageById(id);
    }

    @Override
    public List<PersonVoyage> queryPersonVoyageAsCreateTimeByPersonId(String id) {
        return dao.queryPersonVoyageAsCreateTimeByPersonId(id);
    }

    @Override
    public int deletePersonVoyageByPersonId(String businessId) {
        return dao.deletePersonVoyageByPersonId(businessId);
    }

}
