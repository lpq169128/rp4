package com.timss.pf.web;

import com.timss.facade.util.CreateReturnMapUtil;
import com.timss.facade.web.abstr.AbstractController;
import com.timss.management.bean.MgmtShipInfo;
import com.timss.management.service.MgmtShipInfoService;
import com.timss.pf.bean.*;
import com.timss.pf.dao.*;
import com.timss.pf.service.*;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dao.sec.SecureUserMapper;
import com.yudean.itc.dao.support.AppEnumMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.support.BusinessAttachmentService;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
import com.yudean.itc.util.UUIDGenerator;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.util.ViewUtil;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "pf/personinfo")
public class PersonInfoController extends AbstractController<PersonInfo> {

    protected PersonInfoService service;
    @Autowired
    private PersonRelationService personRelationService;
    @Autowired
    private PersonCertificateService personCertificateService;
    @Autowired
    private PersonCredentialsService personCredentialsService;
    @Autowired
    private PersonExperinenceService personExperinenceService;
    @Autowired
    private PersonVoyageService personVoyageService;
    @Autowired
    private PersonSailorFileService personSailorFileService;
    @Autowired
    private PersonWorkService personWorkService;
    @Autowired
    private SecureUserMapper secUserMapper;
    @Autowired
    private MgmtShipInfoService mgmtShipInfoService;
    @Autowired
    private BusinessAttachmentService businessAttachmentService;
    @Autowired
    private HirePersonInfoDao hirePersonInfoDao;
    @Autowired
    private HireInfoDao hireInfoDao;
    @Autowired
    private PersonExamineDao personExamineDao;
    @Autowired
    private PersonSailorFileDao personSailorFileDao;
    @Autowired
    private TrainingInfoDao trainingInfoDao;
    @Autowired
    private TrainingPersonDao trainingPersonDao;
    @Autowired
    private AppEnumMapper appEnumMapper;

    @Autowired
    protected void setPersonInfoService(PersonInfoService service) throws Exception {
        this.service = service;
        setService(service);
    }

    @Override
    protected String getPrevPath() {
        return "personinfo";
    }


    /**
     * 船员档案进入列表页面
     *
     * @return
     */
    @RequestMapping({"/listPage"})
    @ReturnEnumsBind("ATD_PERSON_SEX,RECRUIT_STATUS,INSHIP_STATUS,ATD_ DUTIES_TYPE")
    public String listPage() throws Exception {
        return this.getPrevPath() + "/list.jsp";
    }

    /**
     * 船员档案上船登记
     *
     * @return
     */
    @RequestMapping({"/register"})
    @ReturnEnumsBind("ATD_ DUTIES_TYPE")
    public ModelAndViewPage register() throws Exception {
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
        List<PersonSailorFile> personSailorFileList = this.personSailorFileDao.queryShip(userInfo.getSecureUser());
        for (int i = 0; i < personSailorFileList.size(); i++) {
            result.put(personSailorFileList.get(i).getIdm(), personSailorFileList.get(i).getShipName());
        }
        JSONObject json = JSONObject.fromObject(result);
        return this.itcMvcService.Pages(this.getPrevPath() + "/scheduling.jsp", "params", json);
    }

    /**
     * 船员档案交班操作
     *
     * @return
     */
    @RequestMapping({"/shiftOperation"})
    public String shiftOperation() throws Exception {
        return this.getPrevPath() + "/shiftOperation.jsp";
    }


    /**
     * 船员档案上船登记保存
     */
    @RequestMapping({"/saveRegister"})
    public ModelAndViewAjax saveRegister() {
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        Map resultMap;
        try {
            String personIds = userInfo.getParam("personIds");
            String registerInfo = userInfo.getParam("registerInfo");
            JSONObject json = JSONObject.fromObject(registerInfo);
            String shipId = json.getString("shipId");
            String inShipDate = json.getString("successionDate");
            String shipName = "";
            String grossTon = "";
            String nationality = "";
            String siteName = "";
            String sailArea = "";
            List<PersonSailorFile> personSailorFileList = this.personSailorFileService.queryShip(userInfo.getSecureUser());
            for (PersonSailorFile personSailorFile : personSailorFileList) {
                if (personSailorFile.getIdm().equals(shipId)) {
                    shipName = personSailorFile.getShipName();
                    grossTon = personSailorFile.getGrossTon();
                    nationality = personSailorFile.getNationalityPerson();
                    siteName = personSailorFile.getSiteName();
                    sailArea = personSailorFile.getSailArea();
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(inShipDate);
            String personId = personIds.substring(1, personIds.length() - 1);
            String[] personIdItem = personId.split(",");
            for (int i = 0; i < personIdItem.length; i++) {
                PersonSailorFile personSailorFile = this.personSailorFileService.queryPersonSailorFileByPersonId(personIdItem[i].substring(1, personIdItem[i].length() - 1));
                PersonVoyage personVoyage = new PersonVoyage();
                personVoyage.setSoilorId(personSailorFile.getBusinessId());
                personVoyage.setInShipDate(date);
                personVoyage.setShipName(shipName);
                personVoyage.setPower(grossTon);
                personVoyage.setNationality(nationality);
                personVoyage.setCompany(siteName);
                personVoyage.setBusinessId(UUIDGenerator.getUUID());
                personVoyage.setCompany(userInfo.getSecureUser().getCurrSiteName());
                personVoyage.setDifferent("auto");
                personVoyage.setArea(sailArea);
                //从聘用申请表中查找职务
                List<HirePersonInfo> hirePersonInfoList = hirePersonInfoDao.queryHirePersonInfoByPersonId(personIdItem[i].substring(1, personIdItem[i].length() - 1));
                HirePersonInfo hirePersonInfo = hirePersonInfoList.get(0);
                HireInfo hireInfo = hireInfoDao.queryById(hirePersonInfo.getRecruitId());
                personVoyage.setDuties(hireInfo.getDuties());
                //保存到近五年海上资历表中
                this.personVoyageService.insert(personVoyage, userInfo.getSecureUser());
                personSailorFile.setInshipStatus("IN");
                personSailorFile.setShipId(shipId);
                //更新离船状态为在船状态
                this.personSailorFileService.update(personSailorFile, userInfo.getSecureUser());
                PersonWork personWork = new PersonWork();
                personWork.setBusinessId(UUIDGenerator.getUUID());
                personWork.setPersonId(personIdItem[i].substring(1, personIdItem[i].length() - 1));
                personWork.setSuccessionDate(date);
                personWork.setShipId(shipId);
                //数据插入到工作交接表中
                this.personWorkService.insert(personWork, userInfo.getSecureUser());
            }
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "船员登记保存成功", "");
        } catch (Exception e) {
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, e.getMessage(), "");
        }
        return ViewUtil.Json(resultMap);
    }

    /**
     * 船员档案交班操作保存
     */
    @RequestMapping({"/saveShift"})
    public ModelAndViewAjax saveShift() {
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        Map resultMap=null;
        try {
            String personIds = userInfo.getParam("personIds");
            String shiftInfo = userInfo.getParam("shiftInfo");
            JSONObject json = JSONObject.fromObject(shiftInfo);
            String reason = json.getString("reason");
            String shiftDate = json.getString("shiftDate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(shiftDate);
            String personId = personIds.substring(1, personIds.length() - 1);
            String[] personIdItem = personId.split(",");
            Integer index=0;
            for (int i = 0; i < personIdItem.length; i++) {
                //更改为船员为未聘用状态
                PersonInfo personInfo = this.service.queryById(personIdItem[i].substring(1, personIdItem[i].length() - 1));
                List<PersonWork> personWorkList = personWorkService.queryPersonWorkByPersonId(personIdItem[i].substring(1, personIdItem[i].length() - 1));
                PersonWork personWork = personWorkList.get(0);
                Date successionDate=personWork.getSuccessionDate();
                if(successionDate.getTime()>date.getTime()){
                    resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "存在船员上船日期大于交班日期，请重新确认！", "");
                    break;
                }
                personInfo.setJobStatus("N");
                this.service.updateJobStatus(personInfo);
                //更改在船状态为离船状态
                PersonSailorFile personSailorFile = this.personSailorFileService.queryPersonSailorFileByPersonId(personIdItem[i].substring(1, personIdItem[i].length() - 1));
                personSailorFile.setInshipStatus("OUT");
                this.personSailorFileService.update(personSailorFile, userInfo.getSecureUser());
                //插入交班信息
//                List<PersonWork> personWorkList = personWorkService.queryPersonWorkByPersonId(personIdItem[i].substring(1, personIdItem[i].length() - 1));
//                PersonWork personWork = personWorkList.get(0);
                personWork.setShiftDate(date);
                personWork.setReason(reason);
                this.personWorkService.update(personWork, userInfo.getSecureUser());
                //插入到近五年海上工作经历表中(按照降序排列第一个)
                List<PersonVoyage> personVoyageList = personVoyageService.queryPersonVoyageAsCreateTimeByPersonId(personSailorFile.getBusinessId());
                if (personVoyageList.size() > 0) {
                    PersonVoyage personVoyage = personVoyageList.get(0);
                    personVoyage.setOutShipDate(date);
                    personVoyage.setDiscription(reason);
                    //同步跟新到近五年海上工作经历
                    this.personVoyageService.update(personVoyage, userInfo.getSecureUser());
                }
                index++;
            }
            if(index==personIdItem.length){
                resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "交班操作跟新成功", "");
            }
        } catch (Exception e) {
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, e.getMessage(), "");
        }
        return ViewUtil.Json(resultMap);
    }

    /**
     * 船员档案进入详情，编辑，查看页面
     *
     * @return
     */
    @RequestMapping({"/detailPage"})
    @ReturnEnumsBind("ATD_PERSON_SEX,ATD_CARD_TYPE,ATD_CERTIFICATE_TYPE,ATD_CERTIFICATE_DATETYPE,ATD_ DUTIES_TYPE,ATD_EXAMINE_LEVEL,ATD_SAILOR_TYPE,SAIL_AREA")
    public ModelAndViewPage detailPage(@ModelAttribute("id") String id, @ModelAttribute("mode") String mode) throws Exception {
        Map<String, Object> result = new HashMap();
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        PersonInfo bean;
        List<PersonRelation> personRelationList = new ArrayList<PersonRelation>();
        List<PersonCertificate> personCertificateList = new ArrayList<PersonCertificate>();
        List<PersonCredentials> personCredentialsList = new ArrayList<PersonCredentials>();
        List<PersonExperinence> personExperinenceList = new ArrayList<PersonExperinence>();
        List<PersonVoyage> personVoyageList = new ArrayList<PersonVoyage>();
        List<Map<String, Object>> businessAttachmentList = new ArrayList();
        List<PersonExamine> personExamineList = new ArrayList<PersonExamine>();
        List<TrainingInfo> trainingInfoList = new ArrayList<TrainingInfo>();
        List<HireInfo> hireInfoList = new ArrayList<HireInfo>();
        Map<String, String> mgmtShipMap = new HashMap<String, String>();
        List<HirePersonInfo> hirePersonInfoList = hirePersonInfoDao.queryHirePersonInfoByPersonId(id);
        if (StringUtils.isNotBlank(id)) {
            //基本信息
            bean = this.service.queryById(id);
            bean = this.service.queryDetail(bean, userInfo.getSecureUser());
        } else {
            bean = this.service.getNewBean(userInfo.getSecureUser());
        }
        if (StringUtils.isNotBlank(id)) {
            //家庭成员信息
            personRelationList = this.personRelationService.queryPersonRelationByPersonId(id);
            //证件信息
            personCertificateList = this.personCertificateService.queryPersonCertificateByPersonId(id);
            //证书信息
            personCredentialsList = this.personCredentialsService.queryPersonCredentialsByPersonId(id);
            //人员工作经历
            personExperinenceList = this.personExperinenceService.queryPersonExperinenceByPersonId(id);
            //海上资历
            PersonSailorFile personSailorFile = this.personSailorFileService.queryPersonSailorFileByPersonId(id);
            if (personSailorFile != null) {
                personVoyageList = this.personVoyageService.queryPersonVoyageByPersonId(personSailorFile.getBusinessId());
            }
            //聘用申请
            if (hirePersonInfoList.size() > 0) {
                for (HirePersonInfo hirePersonInfo : hirePersonInfoList) {
                    HireInfo hireInfo = hireInfoDao.queryById(hirePersonInfo.getRecruitId());
                    hireInfoList.add(hireInfo);
                }
            }
            //考核记录
            personExamineList = personExamineDao.queryPersonExamineById(id);
            //培训记录
            List<TrainingPerson> trainingPersonList = trainingPersonDao.queryTrainingPersonById(id);
            if (trainingPersonList.size() > 0) {
                for (TrainingPerson trainingPerson : trainingPersonList) {
                    List<TrainingInfo> trainingPersonList1 = trainingInfoDao.queryTrainingInfoById(trainingPerson.getTrainingId());
                    trainingInfoList.addAll(trainingPersonList1);
                }
            }
            //船舶信息
            List<MgmtShipInfo> mgmtShipInfoList = this.mgmtShipInfoService.queryAllShipInfo("Y", "Y");
            for (MgmtShipInfo mgmtShipInfo : mgmtShipInfoList) {
                mgmtShipMap.put(mgmtShipInfo.getBusinessId(), mgmtShipInfo.getShipName());
            }
            //附件
            List<String> attachmentIds = businessAttachmentService.queryBusinessAttachmentById(id, "personInfo", null, null);
            businessAttachmentList = FileUploadUtil.getJsonFileList(Constant.basePath, attachmentIds);
        } else {
            personRelationList = this.personRelationService.getNewBeanList(userInfo.getSecureUser());
            personCertificateList = this.personCertificateService.getNewBeanList(userInfo.getSecureUser());
        }
        SecureUser secureUser = this.itcMvcService.getUserInfoScopeDatas().getSecureUser();
        result.put("id", id);
        result.put("valKey", FileUploadUtil.getValidateStr(secureUser, 2));
        result.put("sessionid", this.itcMvcService.getUserInfoScopeDatas().getSession().getId());
        result.put("bean", JsonHelper.toJsonString(bean));
        result.put("PersonRelationList", JsonHelper.toJsonString(personRelationList));
        result.put("PersonCertificateList", JsonHelper.toJsonString(personCertificateList));
        result.put("PersonCredentialsList", JsonHelper.toJsonString(personCredentialsList));
        result.put("PersonExperinenceList", JsonHelper.toJsonString(personExperinenceList));
        result.put("PersonVoyageList", JsonHelper.toJsonString(personVoyageList));
        result.put("hireInfoList", JsonHelper.toJsonString(hireInfoList));
        result.put("mgmtShipMap", JsonHelper.toJsonString(mgmtShipMap));
        result.put("personExamineList", JsonHelper.toJsonString(personExamineList));
        result.put("BusinessAttachmentList", JsonHelper.toJsonString(businessAttachmentList));
        result.put("trainingInfoList", JsonHelper.toJsonString(trainingInfoList));
        Map<String, Object> pageParams = this.service.getPageParams(bean, userInfo.getSecureUser());
        result.put("page", JsonHelper.toJsonString(pageParams));
        return this.itcMvcService.Pages(this.getPrevPath() + "/detail.jsp", "params", result);
    }

    /**
     * 船员档案新建保存
     *
     * @return
     */
    @RequestMapping({"/insertPersonInfo"})
    public ModelAndViewAjax insertPersonInfo() {
        UserInfo userInfo = this.itcMvcService.getUserInfoScopeDatas();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String projectString = "";
        Map resultMap;
        PersonInfo crewBaseInfo;
        try {
            String repeatState = userInfoScope.getParam("repeatState");
            //基本信息保存
            projectString = userInfoScope.getParam("PersonInfo");
            crewBaseInfo = this.service.convertBean(projectString);
            crewBaseInfo.setUserId(crewBaseInfo.getIdCard());
            crewBaseInfo.setJobStatus("N");
            //判断身份证号重复的问题
            if (repeatState.equals("N")) {
                List<PersonInfo> personInfoList = this.service.queryPersoninfoIdcard(crewBaseInfo.getIdCard());
                if (personInfoList.size() > 0) {
                    resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "身份证号重复", crewBaseInfo.getIdCard());
                } else {
                    //先删除用户表中已存在的用户
                    SecureUser secureUsers = this.secUserMapper.selectUser(crewBaseInfo.getUserId());
                    if (secureUsers != null) {
                        resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "工号重复", "");
                    } else {
                        this.service.insert(crewBaseInfo, userInfo.getSecureUser());
                        SecureUser secureUser = new SecureUser();
                        JSONObject json_test = JSONObject.fromObject(projectString);
                        secureUser.setId(json_test.getString("idCard"));
                        secureUser.setName(json_test.getString("name"));
                        secureUser.setMobile(json_test.getString("mobile"));
                        secureUser.setArrivalDate(json_test.getString("arrivalDate"));
                        this.secUserMapper.insertUser(secureUser);
                        //设置上船状态为离船状态
                        PersonSailorFile personSailorFile = new PersonSailorFile();
                        personSailorFile.setPersonId(crewBaseInfo.getBusinessId());
                        personSailorFile.setInshipStatus("OUT");
                        personSailorFile.setBusinessId(UUIDGenerator.getUUID());
                        this.personSailorFileService.insert(personSailorFile, userInfo.getSecureUser());
                        //家庭成员信息保存
                        savePersonRelation(crewBaseInfo, userInfoScope, userInfo);
                        //船员证件信息保存
                        savePersonCertificate(crewBaseInfo, userInfoScope, userInfo);
                        //船员证书信息保存
                        savePersonCredentials(crewBaseInfo, userInfoScope, userInfo);
                        //人员工作经历保存
                        savePersonExperinence(crewBaseInfo, userInfoScope, userInfo);
                        //海上资历保存
                        savePersonVoyage(crewBaseInfo, userInfoScope, userInfo);
                        //附件保存
                        saveOrUpdateAttachment(crewBaseInfo, userInfoScope, userInfo);
                        resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "插入成功", crewBaseInfo.getBusinessId());
                    }

                }
            } else {
                //先删除用户表中已存在的用户
                SecureUser secureUsers = this.secUserMapper.selectUser(crewBaseInfo.getUserId());
                if (secureUsers != null) {
                    resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "工号重复", "");
                } else {
                    //删除身份证相同的数据
                    List<PersonInfo> personInfoList = this.service.queryPersoninfoIdcard(crewBaseInfo.getIdCard());
                    if (personInfoList.size() > 0) {
                        this.service.delete(personInfoList.get(0).getBusinessId(), userInfo.getSecureUser());
                    }
                    this.service.insert(crewBaseInfo, userInfo.getSecureUser());
                    SecureUser secureUser = new SecureUser();
                    JSONObject json_test = JSONObject.fromObject(projectString);
                    secureUser.setId(json_test.getString("idCard"));
                    secureUser.setName(json_test.getString("name"));
                    secureUser.setMobile(json_test.getString("mobile"));
                    secureUser.setArrivalDate(json_test.getString("arrivalDate"));
                    this.secUserMapper.insertUser(secureUser);
                    //设置上船状态为离船状态
                    PersonSailorFile personSailorFile = new PersonSailorFile();
                    personSailorFile.setPersonId(crewBaseInfo.getBusinessId());
                    personSailorFile.setInshipStatus("OUT");
                    personSailorFile.setBusinessId(UUIDGenerator.getUUID());
                    this.personSailorFileService.insert(personSailorFile, userInfo.getSecureUser());
                    //家庭成员信息保存
                    savePersonRelation(crewBaseInfo, userInfoScope, userInfo);
                    //船员证件信息保存
                    savePersonCertificate(crewBaseInfo, userInfoScope, userInfo);
                    //船员证书信息保存
                    savePersonCredentials(crewBaseInfo, userInfoScope, userInfo);
                    //人员工作经历保存
                    savePersonExperinence(crewBaseInfo, userInfoScope, userInfo);
                    //海上资历保存
                    savePersonVoyage(crewBaseInfo, userInfoScope, userInfo);
                    //附件保存
                    saveOrUpdateAttachment(crewBaseInfo, userInfoScope, userInfo);
                    resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "插入成功", crewBaseInfo.getBusinessId());
                }

            }
        } catch (Exception e) {
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "系统运行异常", "");
        }
        return ViewUtil.Json(resultMap);
    }

    /**
     * 船员档案更新保存
     *
     * @return
     */
    @RequestMapping({"/updatePersonInfo"})
    public ModelAndViewAjax updatePersonInfo() {
        UserInfo userInfo = this.itcMvcService.getUserInfoScopeDatas();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String projectString = "";
        Map resultMap;
        PersonInfo crewBaseInfo;
        try {
            String repeatState = userInfoScope.getParam("repeatState");
            //基本信息更新保存
            projectString = userInfoScope.getParam("PersonInfo");
            crewBaseInfo = this.service.convertBean(projectString);
            //查找工号
            PersonInfo personInfo = this.service.queryById(crewBaseInfo.getBusinessId());
            //判断身份证号重复的问题
            if (repeatState.equals("N")) {
                List<PersonInfo> personInfoList = this.service.queryPersoninfoIdcard(crewBaseInfo.getIdCard());
                if (personInfoList.size() > 0) {
                    resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "身份证号重复", crewBaseInfo.getIdCard());
                } else {
                    this.secUserMapper.deleteUser(personInfo.getUserId());
                    SecureUser secureUser = new SecureUser();
                    JSONObject json_test = JSONObject.fromObject(projectString);
                    secureUser.setId(personInfo.getUserId());
                    secureUser.setName(json_test.getString("name"));
                    secureUser.setMobile(json_test.getString("mobile"));
                    secureUser.setArrivalDate(json_test.getString("arrivalDate"));
                    this.secUserMapper.insertUser(secureUser);
                    this.service.update(crewBaseInfo, userInfo.getSecureUser());
                    //家庭成员信息更新保存
                    updatePersonRelation(crewBaseInfo, userInfoScope, userInfo);
                    //证件信息更新保存
                    updatePersonCertificate(crewBaseInfo, userInfoScope, userInfo);
                    //证书信息的更新保存
                    updatePersonCredentials(crewBaseInfo, userInfoScope, userInfo);
                    //人员工作经历的更新保存
                    updatePersonExperinence(crewBaseInfo, userInfoScope, userInfo);
                    //人员海上经历的更新保存
                    updatePersonVoyage(crewBaseInfo, userInfoScope, userInfo);
                    //更新附件
                    saveOrUpdateAttachment(crewBaseInfo, userInfoScope, userInfo);
                    resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "更新成功", crewBaseInfo.getBusinessId());
                }

            } else {
                this.secUserMapper.deleteUser(personInfo.getUserId());
                SecureUser secureUser = new SecureUser();
                JSONObject json_test = JSONObject.fromObject(projectString);
                secureUser.setId(personInfo.getUserId());
                secureUser.setName(json_test.getString("name"));
                secureUser.setMobile(json_test.getString("mobile"));
                secureUser.setArrivalDate(json_test.getString("arrivalDate"));
                this.secUserMapper.insertUser(secureUser);
                this.service.update(crewBaseInfo, userInfo.getSecureUser());
                //家庭成员信息更新保存
                updatePersonRelation(crewBaseInfo, userInfoScope, userInfo);
                //证件信息更新保存
                updatePersonCertificate(crewBaseInfo, userInfoScope, userInfo);
                //证书信息的更新保存
                updatePersonCredentials(crewBaseInfo, userInfoScope, userInfo);
                //人员工作经历的更新保存
                updatePersonExperinence(crewBaseInfo, userInfoScope, userInfo);
                //人员海上经历的更新保存
                updatePersonVoyage(crewBaseInfo, userInfoScope, userInfo);
                //更新附件
                saveOrUpdateAttachment(crewBaseInfo, userInfoScope, userInfo);
                resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "更新成功", crewBaseInfo.getBusinessId());
            }
        } catch (Exception e) {
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "系统运行异常", "");
        }
        return ViewUtil.Json(resultMap);
    }

    public void updatePersonRelation(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personrelation = null;
        personrelation = userInfoScope.getParam("PersonRelation");
        if (personrelation != null && !personrelation.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personrelation);
            PersonRelation personRelation;
            //先删除
            List<PersonRelation> personRelationList = personRelationService.queryPersonRelationByPersonId(crewBaseInfo.getBusinessId());
            if (personRelationList.size() > 0) {
                for (PersonRelation personRelation1 : personRelationList) {
                    personRelationService.deletePersonRelationById(personRelation1.getBusinessId());
                }
            }
            for (int i = 0; i < jsonArray.size(); ++i) {
                String personrelations = jsonArray.getString(i);
                personRelation = this.personRelationService.convertBean(personrelations);
                personRelation.setBusinessId(null);
                personRelation.setPersonId(crewBaseInfo.getBusinessId());
                personRelationService.insert(personRelation, userInfo.getSecureUser());
            }
        }
    }


    public void updatePersonCertificate(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personcredentials = null;
        personcredentials = userInfoScope.getParam("PersonCredentials");
        if (personcredentials != null && !personcredentials.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personcredentials);
            PersonCredentials personCredentials;
            //先删除
            List<PersonCredentials> personCredentialsList = personCredentialsService.queryPersonCredentialsByPersonId(crewBaseInfo.getBusinessId());
            if (personCredentialsList.size() > 0) {
                for (PersonCredentials personCredentials1 : personCredentialsList) {
                    personCredentialsService.deletePersonCredentialsById(personCredentials1.getBusinessId());
                }
            }
            for (int i = 0; i < jsonArray.size(); ++i) {
                String personcertificatess = jsonArray.getString(i);
                personCredentials = this.personCredentialsService.convertBean(personcertificatess);
                personCredentials.setBusinessId(null);
                personCredentials.setPersonId(crewBaseInfo.getBusinessId());
                personCredentialsService.insert(personCredentials, userInfo.getSecureUser());
            }
        }
    }

    public void updatePersonCredentials(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personcertificate = null;
        personcertificate = userInfoScope.getParam("PersonCertificate");
        if (personcertificate != null && !personcertificate.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personcertificate);
            PersonCertificate personCertificate;
            //先删除
            List<PersonCertificate> personCertificateList = personCertificateService.queryPersonCertificateByPersonId(crewBaseInfo.getBusinessId());
            if (personCertificateList.size() > 0) {
                for (PersonCertificate personCertificate1 : personCertificateList) {
                    personCertificateService.deletePersonCertificateById(personCertificate1.getBusinessId());
                }
            }
            for (int i = 0; i < jsonArray.size(); ++i) {
                String personcertificates = jsonArray.getString(i);
                personCertificate = this.personCertificateService.convertBean(personcertificates);
                personCertificate.setBusinessId(null);
                personCertificate.setPersonId(crewBaseInfo.getBusinessId());
                personCertificateService.insert(personCertificate, userInfo.getSecureUser());
            }
        }
    }


    public void updatePersonVoyage(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personvoyage = null;
        personvoyage = userInfoScope.getParam("PersonVoyage");
        if (personvoyage != null && !personvoyage.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personvoyage);
            PersonSailorFile personSailorFile = this.personSailorFileService.queryPersonSailorFileByPersonId(crewBaseInfo.getBusinessId());
            PersonVoyage personVoyage;
            //先删除
            List<PersonVoyage> personVoyageList = personVoyageService.queryPersonVoyageByPersonId(personSailorFile.getBusinessId());
            if (personVoyageList.size() > 0) {
                for (PersonVoyage personVoyage1 : personVoyageList) {
                    personVoyageService.deletePersonVoyageById(personVoyage1.getBusinessId());
                }
            }
            for (int i = 0; i < jsonArray.size(); ++i) {
                String personvoyages = jsonArray.getString(i);
                personVoyage = this.personVoyageService.convertBean(personvoyages);
                personVoyage.setBusinessId(null);
                personVoyage.setSoilorId(personSailorFile.getBusinessId());
                personVoyageService.insert(personVoyage, userInfo.getSecureUser());
            }
        }
    }

    public void updatePersonExperinence(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personexperinence = null;
        personexperinence = userInfoScope.getParam("PersonExperinence");
        if (personexperinence != null && !personexperinence.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personexperinence);
            PersonExperinence personExperinence;
            //先删除
            List<PersonExperinence> personExperinenceList = personExperinenceService.queryPersonExperinenceByPersonId(crewBaseInfo.getBusinessId());
            if (personExperinenceList.size() > 0) {
                for (PersonExperinence personExperinence1 : personExperinenceList) {
                    personExperinenceService.deletePersonExperinenceById(personExperinence1.getBusinessId());
                }
            }
            for (int i = 0; i < jsonArray.size(); ++i) {
                String personcertificates = jsonArray.getString(i);
                personExperinence = this.personExperinenceService.convertBean(personcertificates);
                personExperinence.setBusinessId(null);
                personExperinence.setPersonId(crewBaseInfo.getBusinessId());
                personExperinenceService.insert(personExperinence, userInfo.getSecureUser());
            }
        }
    }

    public void savePersonRelation(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personrelation = userInfoScope.getParam("PersonRelation");
        if (personrelation != null && !personrelation.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personrelation);
            PersonRelation personRelation;

            for (int i = 0; i < jsonArray.size(); ++i) {
                String personrelations = jsonArray.getString(i);
                personRelation = this.personRelationService.convertBean(personrelations);
                personRelation.setPersonId(crewBaseInfo.getBusinessId());
                personRelationService.insert(personRelation, userInfo.getSecureUser());
            }
        }
    }

    public void savePersonCertificate(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personcertificate = userInfoScope.getParam("PersonCertificate");
        if (personcertificate != null && !personcertificate.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personcertificate);
            PersonCertificate personCertificate;

            for (int i = 0; i < jsonArray.size(); ++i) {
                String personcertificates = jsonArray.getString(i);
                personCertificate = this.personCertificateService.convertBean(personcertificates);
                personCertificate.setPersonId(crewBaseInfo.getBusinessId());
                personCertificate.setBusinessId(UUIDGenerator.getUUID());
                personCertificateService.insert(personCertificate, userInfo.getSecureUser());
            }
        }
    }

    public void savePersonCredentials(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personcredentials = userInfoScope.getParam("PersonCredentials");
        if (personcredentials != null && !personcredentials.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personcredentials);
            PersonCredentials personCredentials;

            for (int i = 0; i < jsonArray.size(); ++i) {
                String personcertificatess = jsonArray.getString(i);
                personCredentials = this.personCredentialsService.convertBean(personcertificatess);
                personCredentials.setPersonId(crewBaseInfo.getBusinessId());
                personCredentials.setBusinessId(UUIDGenerator.getUUID());
                personCredentialsService.insert(personCredentials, userInfo.getSecureUser());
            }
        }
    }

    public void savePersonExperinence(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personexperinence = userInfoScope.getParam("PersonExperinence");
        if (personexperinence != null && !personexperinence.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personexperinence);
            PersonExperinence personExperinence;

            for (int i = 0; i < jsonArray.size(); ++i) {
                String PersonExperinences = jsonArray.getString(i);
                personExperinence = this.personExperinenceService.convertBean(PersonExperinences);
                personExperinence.setPersonId(crewBaseInfo.getBusinessId());
                personExperinence.setBusinessId(UUIDGenerator.getUUID());
                personExperinenceService.insert(personExperinence, userInfo.getSecureUser());
            }
        }
    }

    public void savePersonVoyage(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personvoyage = userInfoScope.getParam("PersonVoyage");
        PersonSailorFile personSailorFile = this.personSailorFileService.queryPersonSailorFileByPersonId(crewBaseInfo.getBusinessId());
        if (personvoyage != null && !personvoyage.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personvoyage);
            PersonVoyage personVoyage;

            for (int i = 0; i < jsonArray.size(); ++i) {
                String personvoyages = jsonArray.getString(i);
                personVoyage = this.personVoyageService.convertBean(personvoyages);
                personVoyage.setSoilorId(personSailorFile.getBusinessId());
                personVoyage.setBusinessId(UUIDGenerator.getUUID());
                personVoyage.setDifferent("hand");
                personVoyageService.insert(personVoyage, userInfo.getSecureUser());
            }
        }
    }

    /**
     * 附件保存
     */
    public void saveOrUpdateAttachment(PersonInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String attachList = userInfoScope.getParam("AttachList[attach]");
        List<String> attachIdList = Collections.singletonList(attachList);
        this.businessAttachmentService.insertOrUpdateBusinessAttachment("personInfo", crewBaseInfo.getBusinessId(), null, null, attachIdList, userInfo.getSecureUser());
    }

    @RequestMapping({"/getList"})
    public Page<PersonInfo> getList() throws Exception {
        UserInfoScope userInfoScope = this.itcMvcService.getUserInfoScopeDatas();
        Page<PersonInfo> page = userInfoScope.getPage();
        Map<String, Object> fuzzyParams = new HashMap();
        String fuzzySearchParams = userInfoScope.getParam("search");
        if (StringUtils.isNotBlank(fuzzySearchParams)) {
            fuzzyParams = MapHelper.jsonToHashMap(fuzzySearchParams);
            if (((Map) fuzzyParams).containsKey("createdate")) {
                ((Map) fuzzyParams).put("to_char(createdate,'yyyy-MM-dd')", ((Map) fuzzyParams).get("createdate"));
                ((Map) fuzzyParams).remove("createdate");
            }

            if (((Map) fuzzyParams).containsKey("modifydate")) {
                ((Map) fuzzyParams).put("to_char(modifydate,'yyyy-MM-dd')", ((Map) fuzzyParams).get("modifydate"));
                ((Map) fuzzyParams).remove("modifydate");
            }
        }
        String sort = userInfoScope.getParam("sort");
        page.setSortKey(sort);
        String order = userInfoScope.getParam("order");
        page.setSortOrder(order);
        this.fixFuzzyParams((Map) fuzzyParams);
        if (!((Map) fuzzyParams).isEmpty()) {
            page.setFuzzyParams((Map) fuzzyParams);
        }
        this.fixPageParams(userInfoScope, page);
        page = this.service.queryList(page);
        return page;
    }

    @RequestMapping("/importPersonInfo.do")
    @ResponseBody
    public ModelAndViewAjax importPersonInfo(MultipartFile file, HttpServletRequest request, @ModelAttribute("replaceType") boolean replaceType) {
        Map res = new HashMap();
        Workbook wb = readExcel(file);
        res = importPersonInfoByWb(wb, res, replaceType);
        return ViewUtil.Json(res);
    }

    public Map importPersonInfoByWb(Workbook wb, Map res, boolean replaceType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String errorWords = "";
        if (wb == null) {
            res.put("errorWords", "表格无数据！");
        } else {
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(2);
            PersonInfo personInfo = new PersonInfo();
            String idCard = "";
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(5)))) {
                idCard = (String) getCellFormatValue(row.getCell(5));
                List<PersonInfo> personInfoList = this.service.queryPersoninfoIdcard(idCard);
                if (replaceType) {
                    //确定覆盖（replaceType=true）时，查询personInfo
                    if(personInfoList.size()==1){
                        personInfo = personInfoList.get(0);
                    }else{
                        res.put("status", "error");//ok成功,check确认是否覆盖,error错误
                        if(personInfoList.size()==0){
                            res.put("errorWords", "身份证信息不存在，请联系管理员");//ok成功,check确认是否覆盖,error错误
                        }else{
                            res.put("errorWords", "身份证信息存在多条，请联系管理员");//ok成功,check确认是否覆盖,error错误
                        }
                        return res;
                    }
                }else {
                    //未确定覆盖（replaceType=false）时，身份证查询，如果重复则直接提示
                    if (personInfoList.size() > 0) {
                        res.put("status", "check");//ok成功,check确认是否覆盖,error错误
                        res.put("errorWords", "身份证信息重复，身份证为：" + idCard + "，是否覆盖？");//ok成功,check确认是否覆盖,error错误
                        return res;
                    }
                }
                personInfo.setIdCard((String) getCellFormatValue(row.getCell(5)));
            } else {
                res.put("status", "error");//ok成功,check确认是否覆盖,error错误
                res.put("errorWords", "身份证不能为空！");//ok成功,check确认是否覆盖,error错误
                return res;
            }

            PersonRelation personRelation = new PersonRelation();
            List<PersonCredentials> personCredentialsList = new ArrayList<PersonCredentials>();
            List<PersonCertificate> personCertificateList = new ArrayList<PersonCertificate>();
            List<PersonExperinence> personExperinenceList = new ArrayList<PersonExperinence>();
            List<PersonVoyage> personVoyageList = new ArrayList<PersonVoyage>();
            row = sheet.getRow(1);//姓名  性别  出生年月
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(1)))) {
                personInfo.setName((String) getCellFormatValue(row.getCell(1)));
            } else {
                errorWords = errorWords + "姓名、";
            }
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(3)))) {
                String str = (String) getCellFormatValue(row.getCell(3));
                if ("男".equals(str)) {
                    personInfo.setSex("male");
                } else if ("女".equals(str)) {
                    personInfo.setSex("female");
                } else {
                    errorWords = errorWords + "性别、";
                }
            } else {
                errorWords = errorWords + "性别、";
            }
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(5)))) {
                String str = (String) getCellFormatValue(row.getCell(5));
                String[] arr = str.split("[.]");
                try {
                    Date date = sdf.parse(arr[0] + arr[1] + arr[2]);
                    personInfo.setBirthday(date);
                } catch (Exception e) {
                    errorWords = errorWords + "生日、";
                }
            } else {
                errorWords = errorWords + "生日、";
            }
            row = sheet.getRow(2);//拼音  籍贯/民族   身份证
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(1)))) {
                personInfo.setPinyin((String) getCellFormatValue(row.getCell(1)));
            } else {
                errorWords = errorWords + "拼音、";
            }
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(3)))) {
                personInfo.setNationality((String) getCellFormatValue(row.getCell(3)));
            } else {
                errorWords = errorWords + "籍贯/民族、";
            }
//            if(StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(5)))){
//                //身份证查询，如果重复则直接提示，放在最前
//                personInfo.setIdCard((String) getCellFormatValue(row.getCell(5)));
//            }else{
//                errorWords = errorWords+"身份证、";
//            }
            row = sheet.getRow(3);//政治面目    加入时间    个人电话
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(1)))) {
                personInfo.setPoliticalFace((String) getCellFormatValue(row.getCell(1)));
            } else {
                errorWords = errorWords + "政治面目、";
            }
            try {
                if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(3)))) {
                    personInfo.setArrivalDate(dateToStamp((String) getCellFormatValue(row.getCell(3))));
                } else {
                    errorWords = errorWords + "加入时间、";
                }
            } catch (Exception e) {
                errorWords = errorWords + "加入时间、";
            }

            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(5)))) {
                personInfo.setMobile((String) getCellFormatValue(row.getCell(5)));
            } else {
                errorWords = errorWords + "个人电话、";
            }
            row = sheet.getRow(4);//毕业院校、时间、专业    文化程度
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(1)))) {
                personInfo.setGraduateSchool((String) getCellFormatValue(row.getCell(1)));
            } else {
                errorWords = errorWords + "毕业院校、";
            }
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(6)))) {
                personInfo.setHighestEducation((String) getCellFormatValue(row.getCell(6)));
            } else {
                errorWords = errorWords + "文化程度、";
            }
            row = sheet.getRow(6);//原工作单位   参加工作时间
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(1)))) {
                personInfo.setOriginalWorkUnit((String) getCellFormatValue(row.getCell(1)));
            } else {
                errorWords = errorWords + "原工作单位、";
            }
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(6)))) {
                String str = (String) getCellFormatValue(row.getCell(6));
                String[] arr = str.split("[.]");
                String year = arr[0];
                String month = "01";
                if (arr.length > 1) month = arr[1];
                String day = "01";
                if (arr.length > 2) day = arr[2];
                try {
                    Date date = sdf.parse(year + month + day);
                    personInfo.setWorkDate(date);
                } catch (Exception e) {
                    errorWords = errorWords + "参加工作时间、";
                }
            } else {
                errorWords = errorWords + "参加工作时间、";
            }
            row = sheet.getRow(7);//健康情况    家族病史    既往病史
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(1)))) {
                personInfo.setHealth((String) getCellFormatValue(row.getCell(1)));
            } else {
                errorWords = errorWords + "健康情况、";
            }
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(3)))) {
                personInfo.setFamilyHistory((String) getCellFormatValue(row.getCell(3)));
            } else {
                errorWords = errorWords + "家族病史、";
            }
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(6)))) {
                personInfo.setHistory((String) getCellFormatValue(row.getCell(6)));
            } else {
                errorWords = errorWords + "既往病史、";
            }
            row = sheet.getRow(8);//通信地址    邮政编码
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(2)))) {
                personInfo.setAddress((String) getCellFormatValue(row.getCell(2)));
            } else {
                errorWords = errorWords + "通信地址、";
            }
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(7)))) {
                personInfo.setPostalCode((String) getCellFormatValue(row.getCell(7)));
            } else {
                errorWords = errorWords + "邮政编码、";
            }
            row = sheet.getRow(9);//配偶或亲属姓名 关系  亲属电话    PersonRelation
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(2)))) {
                personRelation.setKinName((String) getCellFormatValue(row.getCell(2)));
            } else {
                errorWords = errorWords + "配偶或亲属姓名、";
            }
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(5)))) {
                personRelation.setRelation((String) getCellFormatValue(row.getCell(5)));
            } else {
                errorWords = errorWords + "关系、";
            }
            if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(7)))) {
                personRelation.setPhone((String) getCellFormatValue(row.getCell(7)));
            } else {
                errorWords = errorWords + "亲属电话、";
            }
            row = sheet.getRow(10);//海员证编号	签发机关    日期类型	有效期
            errorWords = getPersonCredentialsByData("hyz", row, personCredentialsList, errorWords);
            row = sheet.getRow(11);//适任证编号 	签发机关    日期类型	有效期
            errorWords = getPersonCredentialsByData("srz", row, personCredentialsList, errorWords);
            row = sheet.getRow(12);//服务簿编号 	签发机关    日期类型	有效期
            errorWords = getPersonCredentialsByData("fwb", row, personCredentialsList, errorWords);
            row = sheet.getRow(13);//专业培训合格证编号 	签发机关    日期类型	有效期
            errorWords = getPersonCredentialsByData("zypxhgz", row, personCredentialsList, errorWords);
            row = sheet.getRow(14);//GMDSS证书编号 	签发机关    日期类型	有效期
            errorWords = getPersonCredentialsByData("gmddsz", row, personCredentialsList, errorWords);
//                row = sheet.getRow(17);//护照	    证书号码    领证日期    级别  有效期
//                row = sheet.getRow(18);//健康证（海事）	    证书号码    领证日期    级别  有效期
//                row = sheet.getRow(19);//健康证（卫检）	    证书号码    领证日期    级别  有效期
//                row = sheet.getRow(20);//防疫针（黄热）	    证书号码    领证日期    级别  有效期
//                row = sheet.getRow(21);//防疫针（霍乱）	    证书号码    领证日期    级别  有效期
            int i = 17;
            boolean zsCheck = true;
            boolean zsError = false;
            while (zsCheck) {
                row = sheet.getRow(i++);
                if (zsCheck) {
                    String str = (String) getCellFormatValue(row.getCell(0));
                    if (StringUtils.isNotBlank(str)) {
                        if (!"个人简历".equals(StringUtils.deleteWhitespace(str))) {
                            if (getPersonCertificateByData(personCertificateList, row)) zsError = true;
                        } else {
                            zsCheck = false;
                        }
                    }
                }
            }
            if (zsError) errorWords = errorWords + "其他证书记载列表、";

            i++;
            boolean jlCheck = true;
            boolean jlError = false;
            while (jlCheck) {
                row = sheet.getRow(i++);
                if (jlCheck) {
                    String str = (String) getCellFormatValue(row.getCell(0));
                    if (StringUtils.isNotBlank(str)) {
                        if (!"近五年海上资历".equals(StringUtils.deleteWhitespace(str))) {
                            if (getPersonExperinenceByData(personExperinenceList, row)) jlError = true;
                        } else {
                            jlCheck = false;
                        }
                    }
                }
            }
            if (jlError) errorWords = errorWords + "个人简历列表、";

            boolean wnhsCheck = true;
            boolean wnhsError = true;
            while (wnhsCheck) {
                row = sheet.getRow(i++);
                if (row == null) {
                    wnhsCheck = false;
                } else if (wnhsCheck) {
                    String str = (String) getCellFormatValue(row.getCell(4));
                    if (StringUtils.isNotBlank(str)) {
                        if (!"填表日期".equals(StringUtils.deleteWhitespace(str))) {
                            if (getPersonVoyageListByData(personVoyageList, row)) wnhsError = true;
                        } else {
                            wnhsCheck = false;
                        }
                    }
                }
            }
            if (wnhsError) errorWords = errorWords + "个人简历列表、";
            if (StringUtils.isNotBlank(errorWords)) {
                errorWords = "以下数据有问题：" + errorWords.substring(0, errorWords.length() - 1) + "。\r\n";
            }
//            row = sheet.getRow(i);
//            String tbDate = (String) getCellFormatValue(row.getCell(5));
            //开始存储数据
            UserInfo userInfo = this.itcMvcService.getUserInfoScopeDatas();
            personInfo.setUserId(personInfo.getIdCard());
            try {
                if (replaceType) {
                    this.service.updatePersonInfoAndSecureUser(personInfo, userInfo);
                } else {
                    //personInfo、SecureUser如果没存进去，后面从表没必要存
                    String userCheckStr = this.service.insertPersonInfoAndSecureUser(personInfo, userInfo);
                    if ("SecureUserRepeat".equals(userCheckStr)) {
                        res.put("status", "error");
                        res.put("errorWords", "工号已存在，请更换身份证或联系管理员");
                        return res;
                    }
                }
            } catch (Exception e) {
                res.put("status", "error");
                res.put("errorWords", "插入人员档案出错，请联系管理员");
                return res;//此处打断返回目的是主表都没成功后面存了会出错
            }
            //家庭成员信息保存  personRelation
            try {
                personRelationService.deletePersonRelationByPersonId(personInfo.getBusinessId());
                personRelation.setPersonId(personInfo.getBusinessId());
                personRelationService.insert(personRelation, userInfo.getSecureUser());
            } catch (Exception e) {
                errorWords = errorWords + "配偶或亲属信息保存失败！";
            }
            //船员证件信息保存
            try {
                personCertificateService.deletePersonCertificateByPersonId(personInfo.getBusinessId());
                for (PersonCertificate p : personCertificateList) {
                    p.setPersonId(personInfo.getBusinessId());
                    p.setBusinessId(UUIDGenerator.getUUID());
                    personCertificateService.insert(p, userInfo.getSecureUser());
                }
            } catch (Exception e) {
                errorWords = errorWords + "船员证件信息保存失败！";
            }
            //船员证书信息保存
            try {
                personCredentialsService.deletePersonCredentialsByPersonId(personInfo.getBusinessId());
                for (PersonCredentials p : personCredentialsList) {
                    p.setPersonId(personInfo.getBusinessId());
                    p.setBusinessId(UUIDGenerator.getUUID());
                    personCredentialsService.insert(p, userInfo.getSecureUser());
                }
            } catch (Exception e) {
                errorWords = errorWords + "船员证书信息保存失败！";
            }
            //人员工作经历保存
            try {
                personExperinenceService.deletePersonExperinenceByPersonId(personInfo.getBusinessId());
                for (PersonExperinence p : personExperinenceList) {
                    p.setPersonId(personInfo.getBusinessId());
                    p.setBusinessId(UUIDGenerator.getUUID());
                    personExperinenceService.insert(p, userInfo.getSecureUser());
                }
            } catch (Exception e) {
                errorWords = errorWords + "工作经历信息保存失败！";
            }
            //近五年海上经历保存
            try {
                PersonSailorFile personSailorFile = this.personSailorFileService.queryPersonSailorFileByPersonId(personInfo.getBusinessId());
                personVoyageService.deletePersonVoyageByPersonId(personSailorFile.getBusinessId());
                for (PersonVoyage p : personVoyageList) {
                    p.setSoilorId(personSailorFile.getBusinessId());
                    p.setBusinessId(UUIDGenerator.getUUID());
                    p.setDifferent("hand");
                    personVoyageService.insert(p, userInfo.getSecureUser());
                }
            } catch (Exception e) {
                errorWords = errorWords + "近五年海上经历信息保存失败！";
            }
            res.put("status", "ok");
            res.put("errorWords", errorWords);
        }
        return res;
    }

    private String getPersonCredentialsByData(String credentialsType, Row row, List<PersonCredentials> list, String errorWords) {
        Map codeMap = getAppEnumMap("ATD_CERTIFICATE_DATETYPE");
        boolean haveDataCheck = false;
        String errStr = "";
        PersonCredentials p = new PersonCredentials();
        p.setCredentialsType(credentialsType);
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(2)))) {
            p.setCredentialsNo((String) getCellFormatValue(row.getCell(2)));
            haveDataCheck = true;
        } else {
            errStr = errStr + "证书编号、";
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(5)))) {
            p.setIssuer((String) getCellFormatValue(row.getCell(5)));
            haveDataCheck = true;
        } else {
            errStr = errStr + "签发机关、";
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(6)))) {
            String str = (String) codeMap.get(StringUtils.deleteWhitespace((String) getCellFormatValue(row.getCell(6))));
            if (StringUtils.isNotBlank(str)) {
                p.setDateType(str);
                haveDataCheck = true;
            } else {
                errStr = errStr + "日期类型、";
            }
        } else {
            errStr = errStr + "日期类型、";
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(7)))) {
            String str = (String) getCellFormatValue(row.getCell(7));
            String[] arr = str.split("[.]");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                String year = arr[0];
                String month = "01";
                if (arr.length > 1) month = arr[1];
                String day = "01";
                if (arr.length > 2) day = arr[2];
                Date date = sdf.parse(year + month + day);
                p.setCredentialsDate(date);
                haveDataCheck = true;
            } catch (Exception e) {
                errStr = errStr + "日期、";
            }
        } else {
            errStr = errStr + "日期、";
        }
        if (haveDataCheck) {
            list.add(p);
        }
        if (StringUtils.isNotBlank(errStr)) {
            errStr = errStr.substring(0, errStr.length() - 1);
            if ("hyz".equals(credentialsType)) {
                //海员证编号
                errorWords = errorWords + "海员证编号行（" + errStr + "）、";
            } else if ("srz".equals(credentialsType)) {
                //适任证编号
                errorWords = errorWords + "适任证编号行（" + errStr + "）、";
            } else if ("fwb".equals(credentialsType)) {
                //服务簿编号
                errorWords = errorWords + "服务簿编号行（" + errStr + "）、";
            } else if ("zypxhgz".equals(credentialsType)) {
                //专业培训合格证编号
                errorWords = errorWords + "专业培训合格证编号行（" + errStr + "）、";
            } else if ("gmddsz".equals(credentialsType)) {
                //GMDSS证书编号
                errorWords = errorWords + "GMDSS证书编号行（" + errStr + "）、";
            }
        }
        return errorWords;
    }

    private boolean getPersonCertificateByData(List<PersonCertificate> list, Row row) {
        Map codeMap = getAppEnumMap("ATD_CARD_TYPE");
        boolean haveDataCheck = false;
        boolean dataHaveProblem = false;
        PersonCertificate p = new PersonCertificate();
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(0)))) {
            String str = (String) codeMap.get(StringUtils.deleteWhitespace((String) getCellFormatValue(row.getCell(0))));
            if (StringUtils.isNotBlank(str)) {
                p.setCardType(str);
                haveDataCheck = true;
            } else {
                dataHaveProblem = true;
            }
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(2)))) {
            p.setCardNo((String) getCellFormatValue(row.getCell(2)));
            haveDataCheck = true;
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(4)))) {
            String str = (String) getCellFormatValue(row.getCell(4));
            String[] arr = str.split("[.]");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                String year = arr[0];
                String month = "01";
                if (arr.length > 1) month = arr[1];
                String day = "01";
                if (arr.length > 2) day = arr[2];
                Date date = sdf.parse(year + month + day);
                p.setCardDate(date);
                haveDataCheck = true;
            } catch (Exception e) {
                dataHaveProblem = true;
            }
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(6)))) {
            p.setCardLevel((String) getCellFormatValue(row.getCell(6)));
            haveDataCheck = true;
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(7)))) {
            String str = (String) getCellFormatValue(row.getCell(7));
            String[] arr = str.split("[.]");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                String year = arr[0];
                String month = "01";
                if (arr.length > 1) month = arr[1];
                String day = "01";
                if (arr.length > 2) day = arr[2];
                Date date = sdf.parse(year + month + day);
                p.setValidDate(date);
                haveDataCheck = true;
            } catch (Exception e) {
                dataHaveProblem = true;
            }
        } else {
            dataHaveProblem = true;
        }
        if (haveDataCheck) list.add(p);
        return dataHaveProblem;
    }

    private boolean getPersonExperinenceByData(List<PersonExperinence> list, Row row) {
        boolean haveDataCheck = false;
        boolean dataHaveProblem = false;
        PersonExperinence p = new PersonExperinence();
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(0)))) {
            String str = (String) getCellFormatValue(row.getCell(0));
            String[] arr = str.split("[.]");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                String year = arr[0];
                String month = "01";
                if (arr.length > 1) month = arr[1];
                String day = "01";
                if (arr.length > 2) day = arr[2];
                Date date = sdf.parse(year + month + day);
                p.setStartDate(date);
                haveDataCheck = true;
            } catch (Exception e) {
                dataHaveProblem = true;
            }
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(2)))) {
            String str = (String) getCellFormatValue(row.getCell(2));
            String[] arr = str.split("[.]");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                String year = arr[0];
                String month = "01";
                if (arr.length > 1) month = arr[1];
                String day = "01";
                if (arr.length > 2) day = arr[2];
                Date date = sdf.parse(year + month + day);
                p.setEndDate(date);
                haveDataCheck = true;
            } catch (Exception e) {
                dataHaveProblem = true;
            }
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(3)))) {
            p.setCompany((String) getCellFormatValue(row.getCell(3)));
            haveDataCheck = true;
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(7)))) {
            p.setDuties((String) getCellFormatValue(row.getCell(7)));
            haveDataCheck = true;
        } else {
            dataHaveProblem = true;
        }
        if (haveDataCheck) list.add(p);
        return dataHaveProblem;
    }

    private boolean getPersonVoyageListByData(List<PersonVoyage> list, Row row) {
        boolean dataHaveProblem = false;
        Map code1Map = getAppEnumMap("ATD_ DUTIES_TYPE");
        Map code2Map = getAppEnumMap("SAIL_AREA");
        boolean haveDataCheck = false;
        PersonVoyage p = new PersonVoyage();
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(0)))) {
            p.setShipName((String) getCellFormatValue(row.getCell(0)));
            haveDataCheck = true;
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(1)))) {
            String str = (String) code1Map.get(StringUtils.deleteWhitespace((String) getCellFormatValue(row.getCell(1))));
            if (StringUtils.isNotBlank(str)) {
                p.setDuties(str);
                haveDataCheck = true;
            } else {
                dataHaveProblem = true;
            }
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(2)))) {
            p.setPower((String) getCellFormatValue(row.getCell(2)));
            haveDataCheck = true;
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(3)))) {
            p.setNationality((String) getCellFormatValue(row.getCell(3)));
            haveDataCheck = true;
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(4)))) {
            p.setCompany((String) getCellFormatValue(row.getCell(4)));
            haveDataCheck = true;
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(5)))) {
            String str = (String) getCellFormatValue(row.getCell(5));
            String[] arr = str.split("[.]");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                String year = arr[0];
                String month = "01";
                if (arr.length > 1) month = arr[1];
                String day = "01";
                if (arr.length > 2) day = arr[2];
                Date date = sdf.parse(year + month + day);
                p.setInShipDate(date);
                haveDataCheck = true;
            } catch (Exception e) {
                dataHaveProblem = true;
            }
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(6)))) {
            String str = (String) getCellFormatValue(row.getCell(6));
            String[] arr = str.split("[.]");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                String year = arr[0];
                String month = "01";
                if (arr.length > 1) month = arr[1];
                String day = "01";
                if (arr.length > 2) day = arr[2];
                Date date = sdf.parse(year + month + day);
                p.setOutShipDate(date);
                haveDataCheck = true;
            } catch (Exception e) {
                dataHaveProblem = true;
            }
        } else {
            dataHaveProblem = true;
        }
        if (StringUtils.isNotBlank((String) getCellFormatValue(row.getCell(7)))) {
            String str = (String) code2Map.get(StringUtils.deleteWhitespace((String) getCellFormatValue(row.getCell(7))));
            if (StringUtils.isNotBlank(str)) {
                p.setArea(str);
                haveDataCheck = true;
            } else {
                dataHaveProblem = true;
            }
        } else {
            dataHaveProblem = true;
        }
        if (haveDataCheck) list.add(p);
        return dataHaveProblem;
    }

    //读取excel
    public static Workbook readExcel(MultipartFile file) {
        Workbook wb = null;
        if (file == null) {
            return null;
        }
        InputStream is = null;
        try {
            is = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String[] fileNameArr = fileName.split("\\.");
            if ("xls".equals(fileNameArr[fileNameArr.length - 1])) {
                wb = new HSSFWorkbook(is);
            } else if ("xlsx".equals(fileNameArr[fileNameArr.length - 1])) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case 0: {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                case 1: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                case 2: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

    public Map getAppEnumMap(String type) {
        List<AppEnum> list = appEnumMapper.selectEnumsByCat(type);
        Map map = new HashMap();
        for (AppEnum a : list) {
            map.put(a.getLabel(), a.getCode());
        }
        return map;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        String[] arr = s.split("[.]");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String year = arr[0];
        String month = "01";
        if (arr.length > 1) month = arr[1];
        String day = "01";
        if (arr.length > 2) day = arr[2];
        Date date = sdf.parse(year + month + day);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
}
