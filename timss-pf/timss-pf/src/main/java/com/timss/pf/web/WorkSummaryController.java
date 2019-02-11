package com.timss.pf.web;

import com.timss.facade.web.module.ModuleDataController;
import com.timss.pf.dao.HireInfoDao;
import com.timss.pf.service.WorkSummaryService;
import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.module.service.ModuleService;
import com.yudean.mvc.bean.module.ModuleData;
import com.yudean.mvc.bean.module.ModuleList;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.view.ModelAndViewPage;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "pf/workSummary")
public class WorkSummaryController extends ModuleDataController {
    protected WorkSummaryService service;
    @Autowired
    ModuleService moduleService;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private HireInfoDao hireInfoDao;
    @Autowired
    protected void setWorkSummaryService(WorkSummaryService service) throws Exception {
        this.service = service;
        this.setService(service);
    }
    public WorkSummaryController() {
    }
    /*
     * 重新设定页面资源所在的路径前缀
     **/
    protected String getPrevPath() {
        return "workSummary";
    }


    /**
     * 列表页面
     *
     * @param moduleCode
     * @return
     * @throws Exception
     */
    @RequestMapping({"/moduleDataListPage"})
    public ModelAndViewPage listPage(@ModelAttribute("moduleCode") String moduleCode) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        /*查找用户相关信息*/
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        /*通过模板code，站点查找配置的模板数据*/
        ModuleList moduleList = this.moduleService.queryModuleList(moduleCode, userInfo.getSiteId());
       /*查询本站点下所有的班组信息*/
        List<Organization> organizationList=hireInfoDao.queryTeamBySiteId(userInfo.getSiteId());
        LinkedHashMap<String,String> teamMap=new LinkedHashMap<String, String>();
        for (Organization organization:organizationList){
            teamMap.put(organization.getCode(), organization.getName());
        }
        result.put("moduleList", JsonHelper.toJsonString(moduleList));
        JSONObject MapJSON = JSONObject.fromObject(teamMap);
        result.put("teamMap", JsonHelper.toJsonString(MapJSON));
        return this.itcMvcService.Pages(this.getPrevPath() + "/list.jsp", "params", result);
    }

    @RequestMapping({"/moduleDataDetailPage"})
    public ModelAndViewPage detailPage(@ModelAttribute("moduleCode") String moduleCode, @ModelAttribute("id") String id, @ModelAttribute("mode") String mode) throws Exception {
        Map<String, Object> result = new HashMap();
        LinkedHashMap<String, String> defaultMap = new LinkedHashMap<String, String>();
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        ModuleData bean = null;
        if (StringUtils.isNotBlank(id)) {
            bean = this.service.queryById(id);
            bean = this.service.queryDetail(bean, userInfo.getSecureUser());
        } else {
            bean = this.service.getNewBean(moduleCode, userInfo.getSecureUser());
            /*得到登陆人的默认部门信息*/
            defaultMap=getDefaultOrganization(userInfo);
        }
        //传部门下的所有单位
        LinkedHashMap<String,LinkedHashMap> mapMap=new LinkedHashMap<String, LinkedHashMap>();
        mapMap=getOrganization(userInfo);
        //初始化用户默认值
        JSONObject userResponsibilityJSON = JSONObject.fromObject(defaultMap);
        result.put("defaultMap", JsonHelper.toJsonString(userResponsibilityJSON));
        //传所有的部门数据
        JSONObject MapJSON = JSONObject.fromObject(mapMap);
        result.put("totalDep", JsonHelper.toJsonString(MapJSON));
        Map<String, Object> pageParams = this.service.getPageParams(bean, userInfo.getSecureUser());
        result.put("page", JsonHelper.toJsonString(pageParams));
        result.put("bean", JsonHelper.toJsonString(bean));
        return this.itcMvcService.Pages(this.getPrevPath() + "/detail.jsp", "params", result);
    }

    /**
     * 得到登陆用户的信息
     * @param userInfo
     * @return
     */
    public LinkedHashMap<String,String> getDefaultOrganization( UserInfoScope userInfo){
        /*查询登陆用户的部门信息*/
        LinkedHashMap<String, String> defaultMap = new LinkedHashMap<String, String>();
        List<Organization> organizationList1 = organizationMapper.selectOrgUserBelongsTo(userInfo.getUserId());
        /*当且只有一个部门的时候（设置班组用户的权限，只有班组用户方可登陆新建）*/
        if (organizationList1.size() == 1) {
            //分部编码
            String userBranchCode = organizationList1.get(0).getParentCode();
            Organization organization = organizationMapper.selectOrgByID(userBranchCode);
            //责任部门编码
            String userResponsibilityCode = organization.getParentCode();
            defaultMap.put("userResponsibilityMap",userResponsibilityCode);
            defaultMap.put("userBranchMap",userBranchCode);
            defaultMap.put("userTeamMap",organizationList1.get(0).getCode());
        }
        return defaultMap;
    }

    /**
     * 得到当前部们的所有值
     * @param userInfo
     * @return
     */
    public LinkedHashMap<String,LinkedHashMap> getOrganization( UserInfoScope userInfo){
        List<Organization> organizationList2 = organizationMapper.selectOrgBySiteId(userInfo.getSiteId());
        LinkedHashMap<String, LinkedHashMap<String, String>> secondMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        LinkedHashMap<String, LinkedHashMap<String, String>> thirdMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        LinkedHashMap<String, String> firstMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, LinkedHashMap> mapMap = new LinkedHashMap<String, LinkedHashMap>();
        String first = "";
        for (Organization organization : organizationList2) {
            if (organization.getParentCode().equals("1")) {
                //查询公司下面的所有的组织信息
                List<Organization> organizationList = hireInfoDao.selectWokeChildrenByParentCode(organization.getCode());
                //责任部门
                for (Organization organization1 : organizationList) {
                    if (organization1.getParentCode().equals(organization.getCode())) {
                        firstMap.put(organization1.getCode(), organization1.getName());
                        first = first + organization1.getCode() + ",";
                    }
                }
                //分部
                String[] firstStr = first.split(",");
                String second = "";
                for (int i = 0; i < firstStr.length; i++) {
                    String firsts = firstStr[i];
                    LinkedHashMap<String, String> map1 = new LinkedHashMap<String, String>();
                    for (Organization organization2 : organizationList) {
                        String ParentName = organization2.getParentCode();
                        if (firsts.equals(ParentName)) {
                            map1.put(organization2.getCode(), organization2.getName());
                            second = second + organization2.getCode() + ",";
                        }
                    }
                    secondMap.put(firsts, map1);
                }

                //班组
                String[] secondStr = second.split(",");
                for (int i = 0; i < secondStr.length; i++) {
                    String seconds = secondStr[i];
                    LinkedHashMap<String, String> map2 = new LinkedHashMap<String, String>();
                    for (Organization organization3 : organizationList) {
                        String ParentName = organization3.getParentCode();
                        if (seconds.equals(ParentName)) {
                            map2.put(organization3.getCode(), organization3.getName());
                        }
                    }
                    thirdMap.put(seconds,map2);
                }
            }

        }
        mapMap.put("responsibilityMap", firstMap);
        mapMap.put("branchMap", secondMap);
        mapMap.put("teamMap", thirdMap);
        return mapMap;
    }



}