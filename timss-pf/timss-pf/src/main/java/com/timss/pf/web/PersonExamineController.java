package com.timss.pf.web;

import com.timss.facade.web.abstr.WfAbstractController;
import com.timss.management.bean.MgmtShipInfo;
import com.timss.management.service.MgmtShipInfoService;
import com.timss.pf.bean.*;
import com.timss.pf.dao.HireInfoDao;
import com.timss.pf.dao.HirePersonInfoDao;
import com.timss.pf.dao.PersonSailorFileDao;
import com.timss.pf.service.PersonExamineService;
import com.timss.pf.service.PersonInfoService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.view.ModelAndViewAjax;
import com.yudean.mvc.view.ModelAndViewPage;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

import org.apache.log4j.Logger;

@Controller
@RequestMapping(value = "pf/personExamine")
public class PersonExamineController extends WfAbstractController<PersonExamine> {
    private static final Logger LOG = Logger.getLogger(PersonExamine.class);
    @Autowired
    PersonInfoService atdPersonInfoService;
    @Autowired
    private MgmtShipInfoService mgmtShipInfoService;
    @Autowired
    private HirePersonInfoDao hirePersonInfoDao;
    @Autowired
    private HireInfoDao hireInfoDao;
    @Autowired
    private PersonSailorFileDao personSailorFileDao;

    @Override
    protected String getPrevPath() {
        return "personExamine";
    }

    @ReturnEnumsBind("ATD_PERSON_SEX,ATD_SAILOR_TYPE,ATD_ DUTIES_TYPE,ATD_EXAMINE_LEVEL")
    @RequestMapping({"/listPageJsp"})
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

    @RequestMapping({"/detailPage"})
    @ReturnEnumsBind("ATD_PERSON_SEX,ATD_SAILOR_TYPE,ATD_ DUTIES_TYPE,ATD_EXAMINE_LEVEL")
    public ModelAndViewPage detailPage(@ModelAttribute("id") String id, @ModelAttribute("mode") String mode) throws Exception {
        Map<String, Object> result = new HashMap();
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        PersonExamine bean;
        if (StringUtils.isNotBlank(id)) {
            bean = this.service.queryById(id);
            bean = this.service.queryDetail(bean, userInfo.getSecureUser());
        } else {
            bean = this.service.getNewBean(userInfo.getSecureUser());
        }

        result.put("bean", JsonHelper.toJsonString(bean));
        Map<String, Object> pageParams = this.service.getPageParams(bean, userInfo.getSecureUser());
        result.put("page", JsonHelper.toJsonString(pageParams));
        return this.itcMvcService.Pages(this.getPrevPath() + "/detail.jsp", "params", result);
    }

    @Autowired
    protected void setHireInfoService(PersonExamineService service) throws Exception {
        this.service = service;
        setService(service);
    }

    @RequestMapping({"/person"})
    public ModelAndViewAjax person(String kw) {
        LOG.debug("[person]传入参数 kw = " + kw);
        if (StringUtils.isNotBlank(kw)) {
            kw = kw.trim().toUpperCase();
        }
        List<PersonInfo> atdPersonInfoList = this.atdPersonInfoService.queryAllPersonInfo(kw);
        List<Map<String, Object>> resultMap = new ArrayList();
        Iterator var5 = atdPersonInfoList.iterator();
        while (var5.hasNext()) {
            PersonInfo bean = (PersonInfo) var5.next();
            String value = bean.getName()+ "/"  + bean.getUserId();
            Map<String, Object> map = new HashMap();
            map.put("id", bean.getUserId());
            map.put("name", value);
            resultMap.add(map);
        }
        return this.itcMvcService.jsons(resultMap);
    }

    @RequestMapping({"/queryPersonInfoByUserId"})
    public PersonInfo queryPersonInfoByUserId(String userId) {
        PersonInfo personInfo = this.atdPersonInfoService.queryPersonInfoByUserId(userId);
        List<HirePersonInfo> hirePersonInfoList = hirePersonInfoDao.queryHirePersonInfoByPersonId(personInfo.getBusinessId());
        if (hirePersonInfoList.size() > 0) {
            try {
                HireInfo hireInfo = hireInfoDao.queryById(hirePersonInfoList.get(0).getRecruitId());
                personInfo.setShipId(hireInfo.getShipId());
                personInfo.setDuties(hireInfo.getDuties());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return personInfo;
    }

    /**
     * 船员考核列表
     *
     * @return
     */
    @RequestMapping({"/getList"})
    public Page<PersonExamine> getList() throws Exception {
        UserInfoScope userInfoScope = this.itcMvcService.getUserInfoScopeDatas();
        Page<PersonExamine> page = userInfoScope.getPage();
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
            if (((Map) fuzzyParams).containsKey("birthday")) {
                ((Map) fuzzyParams).put("to_char(birthday,'yyyy-MM-dd')", ((Map) fuzzyParams).get("birthday"));
                ((Map) fuzzyParams).remove("birthday");
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
}
