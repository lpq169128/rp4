package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.PersonWork;
import com.timss.pf.dao.PersonWorkDao;
import com.timss.pf.service.PersonWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PersonWorkServiceImpl extends AbstractServiceImpl<PersonWork> implements PersonWorkService {

    protected PersonWorkDao dao;

    @Autowired
    protected void setAtdPersonWorkDao(PersonWorkDao dao) {
        this.dao = dao;
        setDao(dao);
    }


    @Override
    public List<PersonWork> queryPersonWorkByPersonId(String id) {
        return dao.queryPersonWorkByPersonId(id);
    }
}
