package com.timss.pf.service.core;

import com.timss.facade.service.abstr.AbstractServiceImpl;
import com.timss.pf.bean.PersonInfo;
import com.timss.pf.bean.PersonSailorFile;
import com.timss.pf.dao.PersonInfoDao;
import com.timss.pf.service.PersonInfoService;
import com.timss.pf.service.PersonSailorFileService;
import com.yudean.itc.dao.sec.SecureUserMapper;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.mvc.bean.userinfo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonInfoServiceImpl extends AbstractServiceImpl<PersonInfo> implements PersonInfoService {

    protected PersonInfoDao dao;
    @Autowired
    private SecureUserMapper secUserMapper;
    @Autowired
    private PersonSailorFileService personSailorFileService;

    @Autowired
    protected void setCrewFileDao(PersonInfoDao dao) {
        this.dao = dao;
        setDao(dao);
    }

    @Override
    public Integer insert(PersonInfo bean, SecureUser operator) throws Exception {
        bean.setDelind("N");//新建时默认为未删除
        bean.setBusinessId(UUIDGenerator.getUUID());
        return super.insert(bean, operator);
    }

    @Override
    public List<PersonInfo> queryAllPersonInfo(String inputStr) {
        return this.dao.queryAllPersonInfo(inputStr);
    }

    public PersonInfo queryPersonInfoByUserId(String userId) {
        PersonInfo pov = null;
        UserInfo userInfo = this.itcMvcService.getUserInfoScopeDatas();
        List<PersonInfo> personInfoList = this.dao.queryPersonInfoByUserId(userId, userInfo.getSiteId());
        if (personInfoList != null && !personInfoList.isEmpty()) {
            pov = personInfoList.get(0);
        }

        return pov;
    }

    @Override
    public List<PersonInfo> queryPersoninfoIdcard(String idCard) {
        return dao.queryPersoninfoIdcard(idCard);
    }

    @Override
    public void updatePersonInfoAndSecureUser (PersonInfo personInfo, UserInfo userInfo) throws Exception{
        this.secUserMapper.deleteUser(personInfo.getIdCard());
        SecureUser secureUser = new SecureUser();
        secureUser.setId(personInfo.getIdCard());
        secureUser.setName(personInfo.getName());
        secureUser.setMobile(personInfo.getMobile());
        secureUser.setArrivalDate(personInfo.getArrivalDate());
        this.secUserMapper.insertUser(secureUser);
        //更新出错，因此删除后直接插入
        /*if(secureUserCheck == null){
            SecureUser secureUser = new SecureUser();
            secureUser.setId(personInfo.getIdCard());
            secureUser.setName(personInfo.getName());
            secureUser.setMobile(personInfo.getMobile());
            secureUser.setArrivalDate(personInfo.getArrivalDate());
            this.secUserMapper.insertUser(secureUser);
        }else{
            secureUserCheck.setName(personInfo.getName());
            secureUserCheck.setMobile(personInfo.getMobile());
            secureUserCheck.setArrivalDate(personInfo.getArrivalDate());
            this.secUserMapper.updateUser(secureUserCheck);
        }*/
        update(personInfo, userInfo.getSecureUser());
    }

    @Override
    public void updateJobStatus(PersonInfo personInfo) {
        dao.updateJobStatus(personInfo);
    }

    @Override
    public String insertPersonInfoAndSecureUser(PersonInfo personInfo, UserInfo userInfo) throws Exception {
        SecureUser secureUserCheck = this.secUserMapper.selectUser(personInfo.getIdCard());
        if (secureUserCheck != null) return "SecureUserRepeat";
        personInfo.setJobStatus("N");
        insert(personInfo, userInfo.getSecureUser());
        SecureUser secureUser = new SecureUser();
        secureUser.setId(personInfo.getIdCard());
        secureUser.setName(personInfo.getName());
        secureUser.setMobile(personInfo.getMobile());
        secureUser.setArrivalDate(personInfo.getArrivalDate());
        this.secUserMapper.insertUser(secureUser);
        //设置上船状态为离船状态
        PersonSailorFile personSailorFile = new PersonSailorFile();
        personSailorFile.setPersonId(personInfo.getBusinessId());
        personSailorFile.setInshipStatus("OUT");
        personSailorFile.setBusinessId(UUIDGenerator.getUUID());
        this.personSailorFileService.insert(personSailorFile, userInfo.getSecureUser());
        return "ok";
    }
}
