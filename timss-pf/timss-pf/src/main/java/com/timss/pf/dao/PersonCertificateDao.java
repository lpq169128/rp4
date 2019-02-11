package com.timss.pf.dao;

import com.timss.pf.bean.PersonCertificate;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.dao.abstr.AbstractDao;

import java.util.List;

public interface PersonCertificateDao extends AbstractDao<PersonCertificate> {
    int deletePersonCertificateById(String id);

    List<PersonCertificate> queryPersonCertificateByPersonId(String id);


    List<PersonCertificate> getNewBeanList(SecureUser var2);

    int deletePersonCertificateByPersonId(String id);
}
