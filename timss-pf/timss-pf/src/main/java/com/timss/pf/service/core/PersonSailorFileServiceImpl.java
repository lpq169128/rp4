package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.PersonSailorFile;
import com.timss.pf.dao.PersonSailorFileDao;
import com.timss.pf.service.PersonSailorFileService;
import com.yudean.itc.dto.sec.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PersonSailorFileServiceImpl extends AbstractServiceImpl<PersonSailorFile> implements PersonSailorFileService {

    protected PersonSailorFileDao dao;

    @Autowired
    protected void setAtdPersonSailorFileDao(PersonSailorFileDao dao) {
        this.dao = dao;
        setDao(dao);
    }


    @Override
    public List<PersonSailorFile> queryShip(SecureUser var) {
        return dao.queryShip(var);
    }

    @Override
    public PersonSailorFile queryPersonSailorFileByPersonId(String id) {
        return dao.queryPersonSailorFileByPersonId(id);
    }

}
