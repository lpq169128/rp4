package com.timss.pf.service.core;

import com.timss.asset.util.UserPrivUtil;
import com.timss.facade.service.abstr.WfAbstractServiceImpl;
import com.timss.pf.bean.HireInfo;
import com.timss.pf.bean.HirePersonInfo;
import com.timss.pf.bean.PersonInfo;
import com.timss.pf.dao.HireInfoDao;
import com.timss.pf.dao.HirePersonInfoDao;
import com.timss.pf.dao.PersonInfoDao;
import com.timss.pf.service.HireInfoService;
import com.timss.pf.service.HirePersonInfoService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dao.support.AppEnumMapper;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.itc.util.data.DataFormatUtil;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HireInfoServiceImpl extends WfAbstractServiceImpl<HireInfo> implements HireInfoService {
    private UserPrivUtil privUtil;
    protected HireInfoDao hireInfoDao;

    @Autowired
    private HirePersonInfoDao hirePersonInfoDao;
    @Autowired
    private HirePersonInfoService hirePersonInfoService;
    @Autowired
    private PersonInfoDao personInfoDao;
    @Autowired
    private AppEnumMapper appEnumMapper;

    @Autowired
    protected void setHireInfoDao(HireInfoDao dao) {
        this.hireInfoDao = dao;
        this.setDao(dao);
    }


    /**
     * 重写WfAbstractServiceImpl的方法
     *
     * @param bean
     * @param operator
     * @throws Exception
     */
    public void fixWFBean(@CUDTarget HireInfo bean, @Operator SecureUser operator) throws Exception {
        if (org.apache.commons.lang3.StringUtils.isBlank(bean.getBusinessUrl())) {
            bean.setBusinessUrl("pf/employmentApplication/detailPage.do?mode=view&id=" + bean.getBusinessId());
        }
        List<AppEnum> appEnumList = appEnumMapper.selectEnumsByCat("ATD_ DUTIES_TYPE");
        String EnumName = "";
        for (AppEnum appEnum : appEnumList) {
            if (appEnum.getCode().equals(bean.getDuties())) {
                EnumName = appEnum.getLabel();
            }
        }
        String desc = DataFormatUtil.substr(EnumName + "聘用申请", 200);
        if (!desc.equals(bean.getBusinessDesc())) {
            bean.setBusinessDesc(desc);
        }
    }

    /**
     * 重写保存子项目
     *
     * @param bean
     * @param operator
     * @throws Exception
     */
    public void saveItem(@CUDTarget HireInfo bean, @Operator SecureUser operator) throws Exception {
        if (bean.getNextTaskDefKey() != null) {
            if (bean.getNextTaskDefKey().equals("end")) {
                HirePersonInfo hirePersonInfo = hirePersonInfoDao.queryByRecruitId(bean.getBusinessId());
                PersonInfo personInfo = personInfoDao.queryById(hirePersonInfo.getPersonId());
                if (personInfo != null) {
                    personInfo.setJobStatus("Y");
                }
                personInfoDao.updateJobStatus(personInfo);
            }
        }

        JSONObject itemForm = JSONObject.fromObject(bean.getUpdateItemMap().get("itemForm"));
        String personId = itemForm.getString("businessId");
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        List<HirePersonInfo> hirePersonInfoList = hirePersonInfoDao.getHirePersonInfoList(userInfo.getSecureUser());
        String recruitIdStr = "";
        if (hirePersonInfoList.size() > 0) {
            for (HirePersonInfo hirePersonInfo : hirePersonInfoList) {
                recruitIdStr = recruitIdStr + "," + hirePersonInfo.getRecruitId();
            }
            if (recruitIdStr.indexOf(bean.getBusinessId()) != -1) {
                HirePersonInfo hirePersonInfo1 = hirePersonInfoDao.queryByRecruitId(bean.getBusinessId());
                hirePersonInfo1.setPersonId(personId);
                hirePersonInfo1.setRecruitId(bean.getBusinessId());
                hirePersonInfoDao.update(hirePersonInfo1);
            } else {
                HirePersonInfo hirePersonInfo = new HirePersonInfo();
                hirePersonInfo.setBusinessId(UUIDGenerator.getUUID());
                hirePersonInfo.setPersonId(personId);
                hirePersonInfo.setRecruitId(bean.getBusinessId());
                hirePersonInfoDao.insert(hirePersonInfo);
            }
        } else {
            HirePersonInfo hirePersonInfo = new HirePersonInfo();
            hirePersonInfo.setBusinessId(UUIDGenerator.getUUID());
            hirePersonInfo.setPersonId(personId);
            hirePersonInfo.setRecruitId(bean.getBusinessId());
            hirePersonInfoDao.insert(hirePersonInfo);
        }
    }

    /**
     * 重写AbstractServiceImpl中的setItem方法，重新放置人员名单信息
     *
     * @param bean
     * @param operator
     * @throws Exception
     */
    public void setItem(@CUDTarget HireInfo bean, @Operator SecureUser operator) throws Exception {
        UserInfo userInfo = this.itcMvcService.getUserInfoScopeDatas();
        HirePersonInfo hirePersonInfo1 = hirePersonInfoDao.queryByRecruitId(bean.getBusinessId());
        PersonInfo personInfo = personInfoDao.queryById(hirePersonInfo1.getPersonId());
        List<PersonInfo> personInfoList = personInfoDao.queryPersonInfoByUserId(personInfo.getUserId(), userInfo.getSiteId());
        PersonInfo personInfo1 = new PersonInfo();
        if (personInfoList.size() > 0) {
            personInfo1 = personInfoList.get(0);
        }
        Map<String, String> result = new HashMap<String, String>();
        result.put("businessId", personInfo1.getBusinessId());
        result.put("name", personInfo1.getName());
        result.put("sex", personInfo1.getSex());
        result.put("userId", personInfo1.getUserId());
        if (personInfo1.getMobile() == null) {
            result.put("mobile", "");
        } else {
            result.put("mobile", personInfo1.getMobile());
        }
        if (personInfo1.getGraduateSchool() == null) {
            result.put("graduateSchool", "");
        } else {
            result.put("graduateSchool", personInfo1.getGraduateSchool());
        }
        if (personInfo1.getHighestEducation() == null) {
            result.put("highestEducation", "");
        } else {
            result.put("highestEducation", personInfo1.getHighestEducation());
        }
        if (personInfo1.getIdCard() == null) {
            result.put("idCard", "");
        } else {
            result.put("idCard", personInfo1.getIdCard());
        }
        JSONObject jsonObject = JSONObject.fromObject(result);
        bean.setItemMap("itemForm", jsonObject);
    }
}
