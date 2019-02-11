package com.timss.pf.service;


import com.timss.pf.bean.PersonVoyage;
import com.yudean.mvc.service.abstr.AbstractService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 海上人员资历service
 */
@Transactional
public interface PersonVoyageService extends AbstractService<PersonVoyage> {
    int deletePersonVoyageById(String id);

    List<PersonVoyage> queryPersonVoyageByPersonId(String id);


    List<PersonVoyage> queryPersonVoyageAsCreateTimeByPersonId(String id);

    int deletePersonVoyageByPersonId(String businessId);
}