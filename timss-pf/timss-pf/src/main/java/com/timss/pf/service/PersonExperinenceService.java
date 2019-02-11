package com.timss.pf.service;


import com.timss.pf.bean.PersonExperinence;
import com.yudean.mvc.service.abstr.AbstractService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 工作经历service
 */
@Transactional
public interface PersonExperinenceService extends AbstractService<PersonExperinence> {
    int deletePersonExperinenceById(String id);

    List<PersonExperinence> queryPersonExperinenceByPersonId(String id);

    int deletePersonExperinenceByPersonId(String id);
}