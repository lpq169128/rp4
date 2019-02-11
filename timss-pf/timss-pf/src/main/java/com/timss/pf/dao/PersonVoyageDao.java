package com.timss.pf.dao;

import com.timss.pf.bean.PersonVoyage;
import com.yudean.mvc.dao.abstr.AbstractDao;

import java.util.List;

public interface PersonVoyageDao extends AbstractDao<PersonVoyage> {
    int deletePersonVoyageById(String id);

    List<PersonVoyage> queryPersonVoyageByPersonId(String id);

    List<PersonVoyage> queryPersonVoyageAsCreateTimeByPersonId(String id);

    int deletePersonVoyageByPersonId(String id);
}
