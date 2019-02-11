package com.timss.pf.web;

import com.timss.facade.util.CreateReturnMapUtil;
import com.timss.facade.web.abstr.AbstractController;
import com.timss.pf.bean.PersonInfo;
import com.timss.pf.bean.TrainingInfo;
import com.timss.pf.bean.TrainingPerson;
import com.timss.pf.dao.PersonInfoDao;
import com.timss.pf.service.PersonInfoService;
import com.timss.pf.service.TrainingInfoService;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping(value = "pf/trainingInfo")
public class TrainingInfoController extends AbstractController<TrainingInfo> {
    private static final Logger LOG = Logger.getLogger(TrainingInfo.class);

    protected TrainingInfoService service;

    @Autowired
    AttachmentMapper attachmentMapper;
    @Autowired
    BusinessAttachmentService businessAttachmentService;
    @Autowired
    TrainingPersonService trainingPersonService;
    @Autowired
    PersonInfoService personInfoService;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Autowired
    protected void setTrainingInfoService(TrainingInfoService service) throws Exception {
        this.service = service;
        setService(service);
    }

    @Override
    protected String getPrevPath() {
        return "trainingInfo";
    }

    /**
     * 培训记录进入列表页面
     *
     * @return
     */
    @RequestMapping({"/listPage"})
    @ReturnEnumsBind("ATD_TRAINING_CATEGORY,ATD_PERSON_SEX")
    public String listPage() throws Exception {
        return this.getPrevPath() + "/list.jsp";
    }

    /**
     * 培训记录进入详情，编辑，查看页面
     *
     * @return
     */
    @RequestMapping({"/detailPage"})
    @ReturnEnumsBind("ATD_TRAINING_CATEGORY,ATD_PERSON_SEX")
    public ModelAndViewPage detailPage(@ModelAttribute("id") String id, @ModelAttribute("mode") String mode) throws Exception {
        Map<String, Object> result = new HashMap();
        SecureUser secureUser = this.itcMvcService.getUserInfoScopeDatas().getSecureUser();
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        List<Map<String, Object>> businessAttachmentList = new ArrayList();
        TrainingInfo bean;
        String titleName;
        List<TrainingPerson> trainingPersonList = new ArrayList<TrainingPerson>();
        if (StringUtils.isNotBlank(id)) {
            //基本信息
            bean = this.service.queryById(id);
            titleName = bean.getTrainingName();
            //参与人员
            trainingPersonList = this.trainingPersonService.querTrainingPersonByTrainingId(id);
            //附件
            List<String> attachmentIds = businessAttachmentService.queryBusinessAttachmentById(id, "trainingInfo", null, null);
            businessAttachmentList = FileUploadUtil.getJsonFileList(Constant.basePath, attachmentIds);
        } else {
            bean = this.service.getNewBean(userInfo.getSecureUser());
            titleName = "培训详情";
            bean.setContent(content);//详细设计7.5中要求设定初始值
        }
        result.put("id", id);
        result.put("titleName", titleName);
        result.put("valKey", FileUploadUtil.getValidateStr(secureUser, 2));
        result.put("sessionid", this.itcMvcService.getUserInfoScopeDatas().getSession().getId());
        result.put("bean", JsonHelper.toJsonString(bean));
        result.put("TrainingPersonList", JsonHelper.toJsonString(trainingPersonList));
        result.put("BusinessAttachmentList", JsonHelper.toJsonString(businessAttachmentList));
        Map<String, Object> pageParams = this.service.getPageParams(bean, userInfo.getSecureUser());
        result.put("page", JsonHelper.toJsonString(pageParams));
        return this.itcMvcService.Pages(this.getPrevPath() + "/detail.jsp", "params", result);
    }

    /**
     * 培训记录新建保存
     *
     * @return
     */
    @RequestMapping({"/insertTrainingInfo"})
    public ModelAndViewAjax insertTrainingInfo() {
        UserInfo userInfo = this.itcMvcService.getUserInfoScopeDatas();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String projectString = "";
        Map resultMap;
        TrainingInfo crewBaseInfo;
        try {
            //培训记录保存
            projectString = userInfoScope.getParam("trainingInfo");
            crewBaseInfo = this.service.convertBean(projectString);
            crewBaseInfo.setTrainingType("training");
            this.service.insert(crewBaseInfo, userInfo.getSecureUser());
            //参与人员保存
            saveTrainingPerson(crewBaseInfo, userInfoScope, userInfo);
            //附件保存
            SaveorUpdateAttachment(crewBaseInfo, userInfoScope, userInfo);
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "插入成功", crewBaseInfo.getBusinessId());

        } catch (Exception e) {
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, e.getMessage(), "");
        }
        return ViewUtil.Json(resultMap);
    }

    /**
     * 培训记录更新保存
     *
     * @return
     */
    @RequestMapping({"/updateTrainingInfo"})
    public ModelAndViewAjax updateTrainingInfo() {
        UserInfo userInfo = this.itcMvcService.getUserInfoScopeDatas();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String projectString = "";
        Map resultMap;
        TrainingInfo crewBaseInfo;
        try {
            //培训记录更新保存
            projectString = userInfoScope.getParam("trainingInfo");
            crewBaseInfo = this.service.convertBean(projectString);
            this.service.update(crewBaseInfo, userInfo.getSecureUser());
            //参与人员保存
            updateTrainingPerson(crewBaseInfo, userInfoScope, userInfo);
            //附件保存
            SaveorUpdateAttachment(crewBaseInfo, userInfoScope, userInfo);
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.SUCCESS_FLAG, "更新成功", crewBaseInfo.getBusinessId());

        } catch (Exception e) {
            resultMap = CreateReturnMapUtil.createMap(CreateReturnMapUtil.FAIL_FLAG, e.getMessage(), "");
        }
        return ViewUtil.Json(resultMap);
    }

    /**
     * 附件保存
     */
    public void SaveorUpdateAttachment(TrainingInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String attachList = userInfoScope.getParam("AttachList[attach]");
        if (StringUtils.isNotBlank(attachList)) {
            List<String> attachIdList = Collections.singletonList(attachList);
            this.businessAttachmentService.insertOrUpdateBusinessAttachment("trainingInfo", crewBaseInfo.getTrainingId(), null, null, attachIdList, userInfo.getSecureUser());
        }
    }

    public void saveTrainingPerson(TrainingInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String trainingPerson = userInfoScope.getParam("TrainingPerson");
        if (trainingPerson != null && !trainingPerson.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(trainingPerson);
            TrainingPerson personCertificate;

            for (int i = 0; i < jsonArray.size(); ++i) {
                String personcertificates = jsonArray.getString(i);
                personCertificate = trainingPersonService.convertBean(personcertificates);
                personCertificate.setTrainingId(crewBaseInfo.getTrainingId());
                trainingPersonService.insert(personCertificate, userInfo.getSecureUser());
            }
        }
    }

    public void updateTrainingPerson(TrainingInfo crewBaseInfo, UserInfoScope userInfoScope, UserInfo userInfo) throws Exception {
        String personrelation = null;
        personrelation = userInfoScope.getParam("TrainingPerson");
        if (personrelation != null && !personrelation.equals("")) {
            JSONArray jsonArray = JSONArray.fromObject(personrelation);
            TrainingPerson personRelation;
            //先删除
            trainingPersonService.deleteTrainingPersonByTrainingId(crewBaseInfo.getTrainingId());
            for (int i = 0; i < jsonArray.size(); ++i) {
                String personrelations = jsonArray.getString(i);
                personRelation = this.trainingPersonService.convertBean(personrelations);
                personRelation.setTrainingId(crewBaseInfo.getTrainingId());
                trainingPersonService.insert(personRelation, userInfo.getSecureUser());
            }
        }
    }

    /**
     * 模糊查询人员根据输入信息
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
        List<PersonInfo> atdPersonInfoList = this.personInfoDao.queryPersonInfoAll(kw);//目前是人员档案表
        List<Map<String, Object>> resultMap = new ArrayList();
        Iterator var5 = atdPersonInfoList.iterator();
        while (var5.hasNext()) {
            PersonInfo bean = (PersonInfo) var5.next();
            String value =  bean.getName()+ "/" +bean.getUserId() ;
            Map<String, Object> map = new HashMap();
            map.put("id", bean.getUserId());//person_info.id
            map.put("name", value);
            resultMap.add(map);
        }
        return this.itcMvcService.jsons(resultMap);
    }

    @RequestMapping({"/getList"})
    public Page<TrainingInfo> getList() throws Exception {
        UserInfoScope userInfoScope = this.itcMvcService.getUserInfoScopeDatas();
        Page<TrainingInfo> page = userInfoScope.getPage();
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

            if (((Map) fuzzyParams).containsKey("startDate")) {
                ((Map) fuzzyParams).put("to_char(startDate,'yyyy-MM-dd')", ((Map) fuzzyParams).get("startDate"));
                ((Map) fuzzyParams).remove("startDate");
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

    String content = "授课主要内容：\n" +
            "一.船舶概况：船型/尺寸/机械设备/船员情况\n" +
            "二.上船基本需求:\n" +
            "1.广东海运股份有限公司上船合同的条款内容;2.检查核对并带齐了所有证书;3.有关证书的特殊要求和特殊培训;4.船员考核要求;5.对船舶和船东特别熟悉的要求。\n" +
            "三.有关船上培训学习的要求:\n" +
            "驾驶员负责对船上中普船员的培训(英语、公约、体系文件等);船长、大副负责安排对驾驶员本级及升职培训。\n" +
            "四.安全管理体系:\n" +
            "1.ISM/NSM CODE的要素及公司安全管理体系文件;2.最近新生效的公约、法规;3.中国海区及港口对海上防污染方面的特别要求;4.事故和险情报告。\n" +
            "五.安全检查:\n" +
            "1.最近在PSC/FSC检查泄露的船舶及其他缺陷原因;2.公司安全检查程序、特点、标准；3.AMSA最近的检查重点(外贸船舶);4.深圳船籍港要求\n" +
            "六.其他内容\n" +
            "1.外贸航线的注意事项;2.结合现阶段的气候特点介绍船舶的季节性安全工作和预防措施;3煤炭、矿石和散粮的运载管理要求。";
}