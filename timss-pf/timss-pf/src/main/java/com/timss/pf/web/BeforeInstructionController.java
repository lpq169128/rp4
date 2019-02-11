package com.timss.pf.web;

import com.timss.facade.util.CreateReturnMapUtil;
import com.timss.facade.web.abstr.AbstractController;
import com.timss.pf.bean.*;
import com.timss.pf.dao.PersonInfoDao;
import com.timss.pf.dao.PersonSailorFileDao;
import com.timss.pf.service.BeforeInstructionService;
import com.timss.pf.service.TrainingEffectService;
import com.timss.pf.service.TrainingPersonService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.support.BusinessAttachmentService;
import com.yudean.itc.util.Constant;
import com.yudean.itc.util.FileUploadUtil;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping(value = "pf/beforeInstruction")
public class BeforeInstructionController extends AbstractController<BeforeInstruction> {
    private static final Logger LOG = Logger.getLogger(TrainingInfo.class);

    @Override
    protected String getPrevPath() {
        return "beforeInstruction";
    }

    BeforeInstructionService service;

    @Autowired
    AttachmentMapper attachmentMapper;
    @Autowired
    BusinessAttachmentService businessAttachmentService;
    @Autowired
    private TrainingPersonService trainingPersonService;
    @Autowired
    private TrainingEffectService trainingEffectService;
    @Autowired
    private PersonSailorFileDao personSailorFileDao;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Autowired
    protected void setBeforeInstructionService(BeforeInstructionService service) throws Exception {
        this.service = service;
        setService(service);
    }

    /**
     * 开航前指令进入列表页面
     *
     * @return
     */
    @RequestMapping({"/listPageJsp"})
    @ReturnEnumsBind("ATD_TRAINING_TYPE")
    public ModelAndViewPage listPageJsp() throws Exception {
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
        List<PersonSailorFile> personSailorFileList = this.personSailorFileDao.queryShip(userInfo.getSecureUser());
        for (int i = 0; i < personSailorFileList.size(); i++) {
            result.put(personSailorFileList.get(i).getIdm(), personSailorFileList.get(i).getShipName());
        }
        JSONObject json = JSONObject.fromObject(result);
        return this.itcMvcService.Pages(this.getPrevPath() + "/list.jsp", "params", json);
    }

    /**
     * 开航前指令进入详情，编辑，查看页面
     *
     * @return
     */
    @RequestMapping({"/detailPage"})
    @ReturnEnumsBind("ATD_TRAINING_TYPE")
    public ModelAndViewPage detailPage(@ModelAttribute("id") String id, @ModelAttribute("mode") String mode) throws Exception {
        Map<String, Object> result = new HashMap();
        SecureUser secureUser = this.itcMvcService.getUserInfoScopeDatas().getSecureUser();
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        List<TrainingEffect> trainingEffectList = new ArrayList<TrainingEffect>();
        List<Map<String, Object>> businessAttachmentList = new ArrayList();
        BeforeInstruction bean;
        String titleName;
        LinkedHashMap<String, String> mgmtShipInfoMap = new LinkedHashMap<String, String>();
        List<PersonSailorFile> personSailorFileList = this.personSailorFileDao.queryShip(userInfo.getSecureUser());
        for (int i = 0; i < personSailorFileList.size(); i++) {
            mgmtShipInfoMap.put(personSailorFileList.get(i).getIdm(), personSailorFileList.get(i).getShipName());
        }
        if (StringUtils.isNotBlank(id)) {
            //基本信息
            bean = this.service.queryById(id);
            titleName = bean.getShipName() + "-开航前指令";
            //培训效果
            trainingEffectList = trainingEffectService.querTrainingEffectByTrainingId(id);
            //附件
            List<String> attachmentIds = businessAttachmentService.queryBusinessAttachmentById(id, "beforeInstruction", null, null);
            businessAttachmentList = FileUploadUtil.getJsonFileList(Constant.basePath, attachmentIds);
        } else {
            bean = this.service.getNewBean(userInfo.getSecureUser());
            titleName = "开航前指令";
            TrainingEffect one = new TrainingEffect();
            one.setTrainingItem("(1)至(8)项内容");
            one.setTrainingEffect("经过培训，已熟悉上述内容");
            trainingEffectList.add(one);
            TrainingEffect two = new TrainingEffect();
            two.setTrainingItem("(9)至(10)项内容");
            two.setTrainingEffect("经过培训，已熟悉上述内容");
            trainingEffectList.add(two);
            TrainingEffect three = new TrainingEffect();
            three.setTrainingItem("第二、第三部分内容培训");
            three.setTrainingEffect("经过培训，已熟悉上述内容");
            trainingEffectList.add(three);
            bean.setContent(content);
            bean.setAttribute1(attribute1);
        }
        result.put("id", id);
        result.put("titleName", titleName);
        result.put("valKey", FileUploadUtil.getValidateStr(secureUser, 2));
        result.put("sessionid", this.itcMvcService.getUserInfoScopeDatas().getSession().getId());
        result.put("bean", JsonHelper.toJsonString(bean));
        result.put("mgmtShipInfoMap", JsonHelper.toJsonString(mgmtShipInfoMap));
        result.put("TrainingEffectList", JsonHelper.toJsonString(trainingEffectList));
        result.put("BusinessAttachmentList", JsonHelper.toJsonString(businessAttachmentList));
        Map<String, Object> pageParams = this.service.getPageParams(bean, userInfo.getSecureUser());
        result.put("page", JsonHelper.toJsonString(pageParams));
        return this.itcMvcService.Pages(this.getPrevPath() + "/detail.jsp", "params", result);
    }

    /**
     * 开航前指令新建保存
     *
     * @return
     */
    @RequestMapping({"/insertBeforeInstruction"})
    public ModelAndViewAjax insertBeforeInstruction() {
        UserInfo userInfo = this.itcMvcService.getUserInfoScopeDatas();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String projectString = "";
        Map resultMap;
        BeforeInstruction crewBaseInfo;
        try {
            //开航前指令保存
            projectString = userInfoScope.getParam("beforeInstruction");
            crewBaseInfo = this.service.convertBean(projectString);
            crewBaseInfo.setTrainingType("before_vayage");
            this.service.insert(crewBaseInfo, userInfo.getSecureUser());
            //培训人员表数据保存
            insertTrainPerson(crewBaseInfo, userInfo);
            //培训效果保存
            saveTrainingEffect(crewBaseInfo, userInfoScope, userInfo);
            //附件保存
            SaveorUpdateAttachment(crewBaseInfo, userInfoScope, userInfo);
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "插入成功", crewBaseInfo.getBusinessId());
        } catch (Exception e) {
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "保存失败", "");
        }
        return ViewUtil.Json(resultMap);
    }

    /**
     * 开航前指令更新保存
     *
     * @return
     */
    @RequestMapping({"/updateBeforeInstruction"})
    public ModelAndViewAjax updateBeforeInstruction() {
        UserInfo userInfo = this.itcMvcService.getUserInfoScopeDatas();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String projectString = "";
        Map resultMap;
        BeforeInstruction crewBaseInfo;
        try {
            //开航前指令更新
            projectString = userInfoScope.getParam("beforeInstruction");
            crewBaseInfo = this.service.convertBean(projectString);
            this.service.update(crewBaseInfo, userInfo.getSecureUser());
            //培训人员表数据更新
            updateTrainPerson(crewBaseInfo, userInfo);
            //参与人员更新
            updateTrainingEffect(crewBaseInfo, userInfoScope, userInfo);
            //附件更新
            SaveorUpdateAttachment(crewBaseInfo, userInfoScope, userInfo);
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "更新成功", crewBaseInfo.getBusinessId());
        } catch (Exception e) {
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, "更新失败", "");
        }
        return ViewUtil.Json(resultMap);
    }

    public void updateTrainPerson(BeforeInstruction crewBaseInfo, UserInfo userInfo) throws Exception {
        trainingPersonService.deleteTrainingPersonByTrainingId(crewBaseInfo.getTrainingId());
        TrainingPerson tp = new TrainingPerson();
        tp.setTrainingId(crewBaseInfo.getTrainingId());
        tp.setPersonId(crewBaseInfo.getName());
        tp.setDuties(crewBaseInfo.getDuties());
        tp.setDutiesDate(crewBaseInfo.getDutiesDate());
        trainingPersonService.insert(tp, userInfo.getSecureUser());
    }

    public void insertTrainPerson(BeforeInstruction crewBaseInfo, UserInfo userInfo) throws Exception {
        TrainingPerson tp = new TrainingPerson();
        tp.setTrainingId(crewBaseInfo.getTrainingId());
        tp.setPersonId(crewBaseInfo.getName());
        tp.setDuties(crewBaseInfo.getDuties());
        tp.setDutiesDate(crewBaseInfo.getDutiesDate());
        trainingPersonService.insert(tp, userInfo.getSecureUser());
    }

    /**
     * 附件保存
     */
    public void SaveorUpdateAttachment(BeforeInstruction crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String attachList = userInfoScope.getParam("AttachList[attach]");
        if (StringUtils.isNotBlank(attachList)) {
            List<String> attachIdList = Collections.singletonList(attachList);
            this.businessAttachmentService.insertOrUpdateBusinessAttachment("beforeInstruction", crewBaseInfo.getTrainingId(), null, null, attachIdList, userInfo.getSecureUser());
        }
    }

    public void saveTrainingEffect(BeforeInstruction crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String trainingEffect = userInfoScope.getParam("TrainingEffect");
        if (trainingEffect != null && !trainingEffect.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(trainingEffect);
            TrainingEffect personCertificate;
            for (int i = 0; i < jsonArray.size(); ++i) {
                String personcertificates = jsonArray.getString(i);
                personCertificate = trainingEffectService.convertBean(personcertificates);
                personCertificate.setTrainingId(crewBaseInfo.getTrainingId());
                trainingEffectService.insert(personCertificate, userInfo.getSecureUser());
            }
        }
    }

    public void updateTrainingEffect(BeforeInstruction crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personrelation = null;
        personrelation = userInfoScope.getParam("TrainingEffect");
        if (personrelation != null && !personrelation.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personrelation);
            TrainingEffect personRelation;
            //先删除
            trainingEffectService.deleteTrainingEffectByTrainingId(crewBaseInfo.getTrainingId());
            for (int i = 0; i < jsonArray.size(); ++i) {
                String personrelations = jsonArray.getString(i);
                personRelation = this.trainingEffectService.convertBean(personrelations);
                personRelation.setTrainingId(crewBaseInfo.getTrainingId());
                trainingEffectService.insert(personRelation, userInfo.getSecureUser());
            }
        }
    }

    @RequestMapping({"/getList"})
    public Page<BeforeInstruction> getList() throws Exception {
        UserInfoScope userInfoScope = this.itcMvcService.getUserInfoScopeDatas();
        Page<BeforeInstruction> page = userInfoScope.getPage();
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

            if (((Map) fuzzyParams).containsKey("dutiesDate")) {
                ((Map) fuzzyParams).put("to_char(dutiesDate,'yyyy-MM-dd')", ((Map) fuzzyParams).get("dutiesDate"));
                ((Map) fuzzyParams).remove("dutiesDate");
            }
        }

        this.fixFuzzyParams((Map) fuzzyParams);
        if (!((Map) fuzzyParams).isEmpty()) {
            page.setFuzzyParams((Map) fuzzyParams);
        }

        String sort = userInfoScope.getParam("sort");
        if (StringUtils.isBlank(sort)) {
            sort = "createdate";
        }

        page.setSortKey(sort);
        String order = userInfoScope.getParam("order");
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }

        page.setSortOrder(order);
        this.fixPageParams(userInfoScope, page);
        page = this.service.queryList(page);
        return page;
    }

    /**
     * 模糊查询人员根据输入信息（已登记）
     *
     * @param kw
     * @return
     */
    @RequestMapping({"/person"})
    public ModelAndViewAjax person(String kw) {
        LOG.debug("[person]传入参数 kw = " + kw);
        if (StringUtils.isNotBlank(kw)) {
            kw = kw.trim().toUpperCase();
        }
        List<PersonInfo> atdPersonInfoList = this.personInfoDao.queryPersonInfoByJobStatus(kw);
        List<Map<String, Object>> resultMap = new ArrayList();
        Iterator var5 = atdPersonInfoList.iterator();
        while (var5.hasNext()) {
            PersonInfo bean = (PersonInfo) var5.next();
            String value = bean.getUserId() + "/" + bean.getName();
            Map<String, Object> map = new HashMap();
            map.put("id", bean.getUserId());//person_info.id
            map.put("name", value);
            resultMap.add(map);
        }
        return this.itcMvcService.jsons(resultMap);
    }

    String content = "(1)公司的安全和环境保护方针\n" +
            "(2)指定人员及其联系方法\n" +
            "(3)公司安全管理体系的组织机构和文件结构\n" +
            "(4)船长的责任和权利、岗位职责\n" +
            "(5)船舶紧急情况反应程序(船舶15种紧急情况，公司应急领导小组及联系方法)\n" +
            "(6)交接班要求(交接记录、证书情况、设备情况、图纸资料、操作规程、修理记录、所辖零备件及存放位置)\n" +
            "(7)开航前和抵港靠泊前检查要求\n" +
            "(8)安全与防污染设备的操作训练和试验要求\n" +
            "(9)紧急情况中的职责(应急设备的布置、应急操作职责、应急器材存放位置、所属艇筏位置、撤离线路)\n" +
            "(10)主管设备与系统的操作(包括应急操作)";

    String attribute1 = "(1)船舶和设备的维护程序(预防检修制度、循环检修、计划修理、报表要求)\n" +
            "(2)船员管理制度(值班制度、纪律要求、防毒走私、酒精管理)、\n" +
            "(3)安全综合管理检查规定、\n" +
            "(4)不符合规定情况、事故和险情的处理程序、\n" +
            "(5)船舶供应规定(尤其是油水)\n" +
            "(6)加装/驳燃润油操作规定\n" +
            "(7)船舶货物装载要求\n" +
            "(8)船舶压载水操作要求\n" +
            "(9)船舶报告制度要求\n" +
            "(10)制定航次安全计划要求\n" +
            "(11)船舶通信要求";
}
