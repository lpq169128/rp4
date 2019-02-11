package com.timss.pf.web;

import com.timss.facade.web.abstr.WfAbstractController;
import com.timss.management.bean.MgmtShipInfo;
import com.timss.management.service.MgmtShipInfoService;
import com.timss.pf.bean.HireInfo;
import com.timss.pf.bean.PersonInfo;
import com.timss.pf.bean.PersonSailorFile;
import com.timss.pf.dao.PersonInfoDao;
import com.timss.pf.dao.PersonSailorFileDao;
import com.timss.pf.service.HireInfoService;
import com.timss.pf.service.PersonInfoService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.log4j.Logger;

import java.util.*;

@Controller
@RequestMapping(value = "pf/employmentApplication")
public class HireInfoController extends WfAbstractController<HireInfo> {
    private static final Logger LOG = Logger.getLogger(HireInfo.class);

    @Override
    protected String getPrevPath() {
        return "employmentApplication";
    }

    protected HireInfoService service;
    @Autowired
    private MgmtShipInfoService mgmtShipInfoService;
    @Autowired
    private PersonInfoService atdPersonInfoService;
    @Autowired
    protected ItcMvcService itcMvcService;
    @Autowired
    private PersonInfoDao personInfoDao;
    @Autowired
    private PersonSailorFileDao personSailorFileDao;

    @Autowired
    protected void setHireInfoService(HireInfoService service) throws Exception {
        this.service = service;
        setService(service);
    }

    @RequestMapping({"/listPageJsp"})
    @ReturnEnumsBind("ATD_SAILOR_TYPE")
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
     * 模糊查询根据输入信息
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
        List<PersonInfo> atdPersonInfoList = this.personInfoDao.queryAllPersonInfoByHire(kw);
        List<Map<String, Object>> resultMap = new ArrayList();
        Iterator var5 = atdPersonInfoList.iterator();
        while (var5.hasNext()) {
            PersonInfo bean = (PersonInfo) var5.next();
            String value =  bean.getName()+ "/" + bean.getUserId();
            Map<String, Object> map = new HashMap();
            map.put("id", bean.getUserId());
            map.put("name", value);
            resultMap.add(map);
        }
        return this.itcMvcService.jsons(resultMap);
    }

    /**
     * 交班人
     *
     * @param kw
     * @return
     */
    @RequestMapping({"/handoverUser"})
    public ModelAndViewAjax handoverUser(String kw) {
        LOG.debug("[person]传入参数 kw = " + kw);
        if (StringUtils.isNotBlank(kw)) {
            kw = kw.trim().toUpperCase();
        }
        List<PersonInfo> atdPersonInfoList = this.personInfoDao.queryAllPersonInfoByKw(kw);
        List<Map<String, Object>> resultMap = new ArrayList();
        Iterator var5 = atdPersonInfoList.iterator();
        while (var5.hasNext()) {
            PersonInfo bean = (PersonInfo) var5.next();
            String value =  bean.getName()+ "/" + bean.getUserId();
            Map<String, Object> map = new HashMap();
            map.put("id", bean.getUserId());
            map.put("name", value);
            resultMap.add(map);
        }
        return this.itcMvcService.jsons(resultMap);
    }

    /**
     * 根据人员名单中的人的工号来查找人员详情
     *
     * @param userId
     * @return
     */
    @RequestMapping({"/queryPersonInfoByUserId"})
    @ReturnEnumsBind("ATD_PERSON_SEX")
    public PersonInfo queryPersonInfoByUserId(String userId) {
        PersonInfo personInfo = this.atdPersonInfoService.queryPersonInfoByUserId(userId);
        return personInfo;
    }

    /**
     * 根据船舶信息获取详情
     *
     * @param shipId
     * @return
     */
    @RequestMapping({"/queryMgmtShipInfoById"})
    public ModelAndViewAjax queryMgmtShipInfoById(String shipId) {
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        Map<String, String> result = new HashMap<String, String>();
        List<PersonSailorFile> personSailorFileList = this.personSailorFileDao.queryShip(userInfo.getSecureUser());
        for (PersonSailorFile personSailorFile : personSailorFileList) {
            if (personSailorFile.getIdm().equals(shipId)) {
                result.put("grossTon", personSailorFile.getGrossTon());
                result.put("nationality", personSailorFile.getNationalityPerson());
                result.put("siteName", personSailorFile.getSiteName());
            }
        }
        return this.itcMvcService.jsons(result);
    }


    /**
     * 聘用申请详情
     *
     * @return
     */
    @RequestMapping({"/detailPage"})
    @ReturnEnumsBind("ATD_PERSON_SEX,ATD_SAILOR_TYPE,ATD_ DUTIES_TYPE,ATD_CZ_DUTIES,ATD_GJCY_DUTIES,ATD_PTCY_DUTIES")
    public ModelAndViewPage detailPage(@ModelAttribute("id") String id, @ModelAttribute("mode") String mode) throws Exception {
        Map<String, Object> result = new HashMap();
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        String applyType = userInfo.getParam("applyType");
        HireInfo bean;
        if (StringUtils.isNotBlank(id)) {
            bean = this.service.queryById(id);
            bean = this.service.queryDetail(bean, userInfo.getSecureUser());
        } else {
            bean = this.service.getNewBean(userInfo.getSecureUser());
        }

        result.put("bean", JsonHelper.toJsonString(bean));
        result.put("applyType", JsonHelper.toJsonString(applyType));
        Map<String, Object> pageParams = this.service.getPageParams(bean, userInfo.getSecureUser());
        result.put("page", JsonHelper.toJsonString(pageParams));
        return this.itcMvcService.Pages(this.getPrevPath() + "/detail.jsp", "params", result);
    }

    @RequestMapping({"/getList"})
    public Page<HireInfo> getList() throws Exception {
        UserInfoScope userInfoScope = this.itcMvcService.getUserInfoScopeDatas();
        Page<HireInfo> page = userInfoScope.getPage();
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
            if (((Map) fuzzyParams).containsKey("inShipDate")) {
                ((Map) fuzzyParams).put("to_char(inShipDate,'yyyy-MM-dd')", ((Map) fuzzyParams).get("inShipDate"));
                ((Map) fuzzyParams).remove("inShipDate");
            }
        }

        this.fixFuzzyParams((Map) fuzzyParams);
        if (!((Map) fuzzyParams).isEmpty()) {
            page.setFuzzyParams((Map) fuzzyParams);
        }

        String sort = userInfoScope.getParam("sort");
        page.setSortKey(sort);
        String order = userInfoScope.getParam("order");
        page.setSortOrder(order);
        this.fixPageParams(userInfoScope, page);
        page = this.service.queryList(page);
        return page;
    }

}
