package com.timss.pf.dao;

import com.timss.pf.bean.PersonRelation;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.dao.abstr.AbstractDao;

import java.util.List;

public interface PersonRelationDao extends AbstractDao<PersonRelation> {
    int deletePersonRelationById(String id);

    List<PersonRelation> queryPersonRelationByPersonId(String id);


    List<PersonRelation> getNewBeanList(SecureUser var2);

    int deletePersonRelationByPersonId(String id);
}
