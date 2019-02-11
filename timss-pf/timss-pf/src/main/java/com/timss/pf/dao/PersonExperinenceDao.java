package com.timss.pf.dao;

import com.timss.pf.bean.PersonExperinence;
import com.yudean.mvc.dao.abstr.AbstractDao;

import java.util.List;

public interface PersonExperinenceDao extends AbstractDao<PersonExperinence> {
    int deletePersonExperinenceById(String id);

    List<PersonExperinence> queryPersonExperinenceByPersonId(String id);

    int deletePersonExperinenceByPersonId(String id);
}
