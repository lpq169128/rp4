package com.timss.pf.service;

import com.timss.pf.bean.PersonRelation;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.service.abstr.AbstractService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 亲友service
 */
@Transactional
public interface PersonRelationService extends AbstractService<PersonRelation> {
    int deletePersonRelationById(String id);

    List<PersonRelation> queryPersonRelationByPersonId(String id);

    List<PersonRelation> getNewBeanList(SecureUser var2);

    int deletePersonRelationByPersonId(String id);
}