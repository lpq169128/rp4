package com.timss.pf.web;

import com.timss.facade.web.abstr.WfAbstractController;

import com.timss.pf.bean.PersonSailorFile;
import com.timss.pf.bean.ShipReimburse;
import com.timss.pf.dao.PersonSailorFileDao;
import com.timss.pf.service.ShipReimburseService;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.view.ModelAndViewPage;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "pf/shipReimburse")
public class ShipReimburseController extends WfAbstractController<ShipReimburse> {

    protected ShipReimburseService service;
    @Autowired
    private PersonSailorFileDao personSailorFileDao;

    @Autowired
    protected void setShipReimburseService(ShipReimburseService service) throws Exception {
        this.service = service;
        setService(service);
    }

    /*
     * 重新设定页面资源所在的路径前缀
     **/
    @Override
    protected String getPrevPath() {
        return "shipReimburse";
    }

    /**
     * 付款审批列表页面
     *
     * @return
     */
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

    @RequestMapping({"/getList"})
    public Page<ShipReimburse> getList() throws Exception {
        UserInfoScope userInfoScope = this.itcMvcService.getUserInfoScopeDatas();
        Page<ShipReimburse> page = userInfoScope.getPage();
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
            if (((Map) fuzzyParams).containsKey("reimburseDate")) {
                ((Map) fuzzyParams).put("to_char(reimburseDate,'yyyy-MM-dd')", ((Map) fuzzyParams).get("reimburseDate"));
                ((Map) fuzzyParams).remove("reimburseDate");
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
