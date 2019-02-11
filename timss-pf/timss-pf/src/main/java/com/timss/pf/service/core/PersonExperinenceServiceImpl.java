package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.PersonExperinence;
import com.timss.pf.dao.PersonExperinenceDao;
import com.timss.pf.service.PersonExperinenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonExperinenceServiceImpl extends AbstractServiceImpl<PersonExperinence> implements PersonExperinenceService {

    protected PersonExperinenceDao dao;

    @Autowired
    protected void setAtdPersonExperinenceDao(PersonExperinenceDao dao) {
        this.dao = dao;
        setDao(dao);
    }

    @Override
    public List<PersonExperinence> queryPersonExperinenceByPersonId(String id) {
        return dao.queryPersonExperinenceByPersonId(id);
    }

    @Override
    public int deletePersonExperinenceByPersonId(String id) {return dao.deletePersonExperinenceByPersonId(id);
    }

    @Override
    public int deletePersonExperinenceById(String id) {
        return dao.deletePersonExperinenceById(id);
    }

}
