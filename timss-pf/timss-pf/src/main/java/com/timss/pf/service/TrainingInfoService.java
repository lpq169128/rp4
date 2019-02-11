package com.timss.pf.service;

import com.timss.pf.bean.TrainingInfo;
import com.yudean.mvc.service.abstr.AbstractService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 培训记录service
 *
 */
@Transactional
public interface TrainingInfoService extends AbstractService<TrainingInfo>  {
}
