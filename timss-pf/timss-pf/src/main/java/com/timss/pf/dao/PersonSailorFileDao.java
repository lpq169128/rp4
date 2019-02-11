package com.timss.pf.dao;

import com.timss.pf.bean.PersonSailorFile;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.dao.abstr.AbstractDao;

import java.util.List;

public interface PersonSailorFileDao extends AbstractDao<PersonSailorFile> {
    PersonSailorFile queryPersonSailorFileByPersonId(String id);

    List<PersonSailorFile> queryShip(SecureUser var);

}
