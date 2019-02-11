package com.timss.pf.service;


import com.timss.pf.bean.PersonCertificate;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.service.abstr.AbstractService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 证件service
 */
@Transactional
public interface PersonCertificateService extends AbstractService<PersonCertificate> {
    int deletePersonCertificateById(String id);

    List<PersonCertificate> queryPersonCertificateByPersonId(String id);


    List<PersonCertificate> getNewBeanList(SecureUser var2);

    int deletePersonCertificateByPersonId(String businessId);
}