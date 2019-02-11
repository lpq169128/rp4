package com.timss.pf.service;

import com.timss.pf.bean.PersonInfo;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.service.abstr.AbstractService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 船员信息service
 */
@Transactional
public interface PersonInfoService extends AbstractService<PersonInfo> {

    void updateJobStatus(PersonInfo personInfo);

    String insertPersonInfoAndSecureUser(PersonInfo personInfo, UserInfo userInfo) throws Exception;

    PersonInfo queryPersonInfoByUserId(String var1);

    List<PersonInfo> queryAllPersonInfo(String var1);

    List<PersonInfo> queryPersoninfoIdcard(String idCard);

    void updatePersonInfoAndSecureUser(PersonInfo personInfo, UserInfo userInfo) throws Exception;
}