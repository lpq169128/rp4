package com.timss.pf.web;

import com.timss.facade.web.module.ModuleDataController;
import com.timss.pf.dao.HireInfoDao;
import com.timss.pf.service.AntiAcciDrillService;
import com.yudean.itc.dao.sec.OrganizationMapper;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.module.service.ModuleService;
import com.yudean.mvc.bean.module.ModuleData;
import com.yudean.mvc.bean.module.ModuleList;
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
@RequestMapping(value = "pf/antiAcciDrill")
public class AntiAccidentDrillController extends ModuleDataController {
    protected AntiAcciDrillService service;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private HireInfoDao hireInfoDao;
    @Autowired
    ModuleService moduleService;
    @Autowired
    protected void setAntiAcciDrillService(AntiAcciDrillService service) throws Exception {
        this.service = service;
        this.setService(service);
    }
    public AntiAccidentDrillController() {
    }
    /*
     * 重新设定页面资源所在的路径前缀
     **/
    @Override
    protected String getPrevPath() {
        return "antiAcciDrill";
    }

    /**
     * 列表页面
     * @param moduleCode
     * @return
     * @throws Exception
     */
    @RequestMapping({"/moduleDataListPage"})
    public ModelAndViewPage listPage(@ModelAttribute("moduleCode") String moduleCode) throws Exception {
        Map<String,String> result=new HashMap<String, String>();
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        ModuleList moduleList = this.moduleService.queryModuleList(moduleCode, userInfo.getSiteId());
        List<Organization> organizationList=hireInfoDao.queryTeamBySiteId(userInfo.getSiteId());
        LinkedHashMap<String,String> teamMap=new LinkedHashMap<String, String>();
        for (Organization organization:organizationList){
            teamMap.put(organization.getCode(), organization.getName());
        }
        result.put("moduleList", JsonHelper.toJsonString(moduleList));
        JSONObject MapJSON = JSONObject.fromObject(teamMap);
        result.put("teamMap", JsonHelper.toJsonString(MapJSON));
        return this.itcMvcService.Pages(this.getPrevPath() + "/list.jsp","params",result);
    }
//    /**
//     * 添加计划任务
//     *
//     * @return
//     */
//    @RequestMapping({"/addPlanMission"})
//    public String addPlanMission() throws Exception {
//        return this.getPrevPath() + "/scheduling.jsp";
//    }

    @RequestMapping({"/moduleDataDetailPage"})
    public ModelAndViewPage detailPage(@ModelAttribute("moduleCode") String moduleCode, @ModelAttribute("id") String id, @ModelAttribute("mode") String mode) throws Exception {
        Map<String, Object> result = new HashMap();
        UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
        ModuleData bean = null;
        if (StringUtils.isNotBlank(id)) {
            bean = this.service.queryById(id);
            bean = this.service.queryDetail(bean, userInfo.getSecureUser());
        } else {
            bean = this.service.getNewBean(moduleCode, userInfo.getSecureUser());
        }
        //初始化用户默认部门
        Map<String, Map> defaultMap = new HashMap<String, Map>();
        Map<String, String> userResponsibilityMap = new HashMap<String, String>();
        Map<String, String> userBranchMap = new HashMap<String, String>();
        Map<String, String> userTeamMap = new HashMap<String, String>();
        result.put("bean", JsonHelper.toJsonString(bean));
        //班组名称
        List<Organization> organizationList1 = organizationMapper.selectOrgUserBelongsTo(userInfo.getUserId());
        if (organizationList1.size() == 1) {

            //分部编码
            String userBranchCode = organizationList1.get(0).getParentCode();
            Organization organization = organizationMapper.selectOrgByID(userBranchCode);

            //责任部门编码
            String userResponsibilityCode = organization.getParentCode();
            Organization organization1 = organizationMapper.selectOrgByID(userResponsibilityCode);
            if (organization1.getParentCode().equals("1")) {
                userBranchMap.put(organizationList1.get(0).getCode(),organizationList1.get(0).getName());
                userResponsibilityMap.put(organization1.getCode(), organization1.getName());

            } else {
                userTeamMap.put(organizationList1.get(0).getCode(), organizationList1.get(0).getName());
                userBranchMap.put(organization.getCode(), organization.getName());
                userResponsibilityMap.put(organization1.getCode(), organization1.getName());
            }

        }
        if (organizationList1.size() == 0) {
        }
        if (organizationList1.size() > 1) {
            userResponsibilityMap.put(organizationList1.get(0).getCode(), organizationList1.get(0).getName());
        }
        defaultMap.put("userResponsibilityMap", userResponsibilityMap);
        defaultMap.put("userBranchMap", userBranchMap);
        defaultMap.put("userTeamMap", userTeamMap);
        //传部门下的所有单位
        List<Organization> organizationList2 = organizationMapper.selectOrgBySiteId(userInfo.getSiteId());

        Map<String, Map<String, String>> secondMap = new HashMap<String, Map<String, String>>();
        Map<String, Map<String, String>> thirdMap = new HashMap<String, Map<String, String>>();
        Map<String, String> firstMap = new HashMap<String, String>();
        Map<String, Map> mapMap = new HashMap<String, Map>();
        String first = "";
        for (Organization organization : organizationList2) {
            if (organization.getParentCode().equals("1")) {
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
                    Map<String, String> map1 = new HashMap<String, String>();
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
                    Map<String, String> map2 = new HashMap<String, String>();
                    for (Organization organization3 : organizationList) {
                        String ParentName = organization3.getParentCode();
                        if (seconds.equals(ParentName)) {
                            map2.put(organization3.getCode(), organization3.getName());
                        }
                    }
                    thirdMap.put(seconds, map2);
                }
            }

        }
        mapMap.put("responsibilityMap", firstMap);
        mapMap.put("branchMap", secondMap);
        mapMap.put("teamMap", thirdMap);

        //初始化用户默认值
        JSONObject userResponsibilityJSON = JSONObject.fromObject(defaultMap);
        result.put("defaultMap", JsonHelper.toJsonString(userResponsibilityJSON));
        //传所有的部门数据
        JSONObject MapJSON = JSONObject.fromObject(mapMap);
        result.put("totalDep", JsonHelper.toJsonString(MapJSON));
        Map<String, Object> pageParams = this.service.getPageParams(bean, userInfo.getSecureUser());
        result.put("page", JsonHelper.toJsonString(pageParams));
        return this.itcMvcService.Pages(this.getPrevPath() + "/detail.jsp", "params", result);
    }
}
