package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.BeforeInstruction;
import com.timss.pf.dao.BeforeInstructionDao;
import com.timss.pf.service.BeforeInstructionService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.sec.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeforeInstructionServiceImpl extends AbstractServiceImpl<BeforeInstruction> implements BeforeInstructionService {

    protected BeforeInstructionDao dao;

    @Autowired
    protected void setCrewFileDao(BeforeInstructionDao dao) {
        this.dao = dao;
        setDao(dao);
    }
}
