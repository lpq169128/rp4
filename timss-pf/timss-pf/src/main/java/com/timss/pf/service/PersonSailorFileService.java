package com.timss.pf.service;


import com.timss.pf.bean.PersonSailorFile;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.service.abstr.AbstractService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 船员档案service
 */
@Transactional
public interface PersonSailorFileService extends AbstractService<PersonSailorFile> {
    PersonSailorFile queryPersonSailorFileByPersonId(String id);

    List<PersonSailorFile> queryShip(SecureUser var);


}