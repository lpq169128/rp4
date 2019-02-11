package com.timss.pf.dao;

import com.timss.pf.bean.HirePersonInfo;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.dao.abstr.AbstractDao;

import java.util.List;

public interface HirePersonInfoDao extends AbstractDao<HirePersonInfo> {
    HirePersonInfo queryByRecruitId(String recruId);

    List<HirePersonInfo> getHirePersonInfoList(SecureUser var2);


    List<HirePersonInfo> queryHirePersonInfoByPersonId(String id);
}
