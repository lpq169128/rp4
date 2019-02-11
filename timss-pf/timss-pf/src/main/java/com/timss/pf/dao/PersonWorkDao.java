package com.timss.pf.dao;

import com.timss.pf.bean.PersonWork;
import com.yudean.mvc.dao.abstr.AbstractDao;

import java.util.List;

public interface PersonWorkDao extends AbstractDao<PersonWork> {
    List<PersonWork> queryPersonWorkByPersonId(String id);
}
