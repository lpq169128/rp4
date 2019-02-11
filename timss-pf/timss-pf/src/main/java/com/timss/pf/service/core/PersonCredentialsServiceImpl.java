package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.PersonCredentials;
import com.timss.pf.dao.PersonCredentialsDao;
import com.timss.pf.service.PersonCredentialsService;
import com.yudean.itc.dto.sec.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonCredentialsServiceImpl extends AbstractServiceImpl<PersonCredentials> implements PersonCredentialsService {

    protected PersonCredentialsDao dao;

    @Autowired
    protected void setAtdPersonCredentialsDao(PersonCredentialsDao dao) {
        this.dao = dao;
        setDao(dao);
    }

    @Override
    public List<PersonCredentials> queryPersonCredentialsByPersonId(String id) {
        return dao.queryPersonCredentialsByPersonId(id);
    }

    @Override
    public int deletePersonCredentialsById(String id) {
        return dao.deletePersonCredentialsById(id);
    }

    @Override
    public List<PersonCredentials> getNewBeanList(SecureUser var2) {
        return dao.getNewBeanList(var2);
    }

    @Override
    public int deletePersonCredentialsByPersonId(String id) {return dao.deletePersonCredentialsByPersonId(id);
    }
}
