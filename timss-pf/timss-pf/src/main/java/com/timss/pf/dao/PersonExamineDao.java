package com.timss.pf.dao;


import com.timss.pf.bean.PersonExamine;
import com.yudean.mvc.dao.abstr.WfAbstractDao;

import java.util.List;

public interface PersonExamineDao extends WfAbstractDao<PersonExamine> {
    List<PersonExamine> queryPersonExamineById(String personId);
}
