package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.PersonRelation;
import com.timss.pf.dao.PersonRelationDao;
import com.timss.pf.service.PersonRelationService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonRelationServiceImpl extends AbstractServiceImpl<PersonRelation> implements PersonRelationService {

    protected PersonRelationDao dao;

    @Autowired
    protected void setAtdPersonRelationDao(PersonRelationDao dao) {
        this.dao = dao;
        setDao(dao);
    }

    @Override
    public List<PersonRelation> queryPersonRelationByPersonId(String id) {
        return dao.queryPersonRelationByPersonId(id);
    }

    @Override
    public int deletePersonRelationById(String id) {
        return dao.deletePersonRelationById(id);
    }

    @Override
    public List<PersonRelation> getNewBeanList(SecureUser var2) {
        return dao.getNewBeanList(var2);
    }

    @Override
    public int deletePersonRelationByPersonId(String id) {
        return dao.deletePersonRelationByPersonId(id);
    }

    @Override
    public Integer insert(PersonRelation bean, SecureUser operator) throws Exception {
        bean.setBusinessId(UUIDGenerator.getUUID());
        return super.insert(bean, operator);
    }
}
