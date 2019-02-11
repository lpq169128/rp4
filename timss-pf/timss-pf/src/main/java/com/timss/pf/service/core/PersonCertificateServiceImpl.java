package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.PersonCertificate;
import com.timss.pf.dao.PersonCertificateDao;
import com.timss.pf.service.PersonCertificateService;
import com.yudean.itc.dto.sec.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonCertificateServiceImpl extends AbstractServiceImpl<PersonCertificate> implements PersonCertificateService {

    protected PersonCertificateDao dao;

    @Autowired
    protected void setAtdPersonCertificateDao(PersonCertificateDao dao) {
        this.dao = dao;
        setDao(dao);
    }

    @Override
    public List<PersonCertificate> queryPersonCertificateByPersonId(String id) {
        return dao.queryPersonCertificateByPersonId(id);
    }

    @Override
    public int deletePersonCertificateById(String id) {
        return dao.deletePersonCertificateById(id);
    }

    @Override
    public List<PersonCertificate> getNewBeanList(SecureUser var2) {
        return dao.getNewBeanList(var2);
    }

    @Override
    public int deletePersonCertificateByPersonId(String id) {return dao.deletePersonCertificateByPersonId(id);}
}
