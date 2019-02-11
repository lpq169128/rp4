package com.timss.pf.service;

import com.timss.pf.bean.BeforeInstruction;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.service.abstr.AbstractService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 开航前指令service
 */
@Transactional
public interface BeforeInstructionService extends AbstractService<BeforeInstruction> {
}
