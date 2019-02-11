package com.timss.pf.dao;

import com.timss.pf.bean.PersonCredentials;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.dao.abstr.AbstractDao;

import java.util.List;

public interface PersonCredentialsDao extends AbstractDao<PersonCredentials> {
    int deletePersonCredentialsById(String id);

    List<PersonCredentials> queryPersonCredentialsByPersonId(String id);


    List<PersonCredentials> getNewBeanList(SecureUser var2);

    int deletePersonCredentialsByPersonId(String id);
}
