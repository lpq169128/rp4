package com.timss.pf.service;


import com.timss.pf.bean.PersonCredentials;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.service.abstr.AbstractService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 证书ervice
 */
@Transactional
public interface PersonCredentialsService extends AbstractService<PersonCredentials> {
    int deletePersonCredentialsById(String id);

    List<PersonCredentials> queryPersonCredentialsByPersonId(String id);


    List<PersonCredentials> getNewBeanList(SecureUser var2);

    int deletePersonCredentialsByPersonId(String id);
}