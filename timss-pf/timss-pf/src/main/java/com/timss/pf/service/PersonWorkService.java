package com.timss.pf.service;


import com.timss.pf.bean.PersonWork;
import com.yudean.mvc.service.abstr.AbstractService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 工作交接service
 *
 */
@Transactional
public interface PersonWorkService extends AbstractService<PersonWork> {
    List<PersonWork> queryPersonWorkByPersonId(String id);
}