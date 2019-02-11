package com.timss.pf.web;

import com.timss.facade.web.abstr.AbstractController;
import com.timss.pf.bean.TrainingPerson;
import com.timss.pf.service.TrainingPersonService;
import com.yudean.itc.annotation.ReturnEnumsBind;
import com.yudean.itc.dao.support.AttachmentMapper;
import com.yudean.itc.dto.Page;
import com.yudean.itc.util.map.MapHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.view.ModelAndViewPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "pf/trainingPerson")
public class TrainingPersonController extends AbstractController<TrainingPerson> {
    protected TrainingPersonService service;
    @Autowired
    AttachmentMapper attachmentMapper;

    @Autowired
    protected void setTrainingPersonService(TrainingPersonService service) throws Exception {
        this.service = service;
        setService(service);
    }

    @Override
    protected String getPrevPath() {
        return "trainingPerson";
    }

    /**
     * 培训人员列表
     *
     * @return
     */
    @RequestMapping({"/trainingPersonList"})
    @ReturnEnumsBind("ATD_PERSON_SEX")
    public ModelAndView listPage(@ModelAttribute("id") String id) throws Exception {
        ModelAndView result = new ModelAndView(this.getPrevPath() + "/list.jsp");
        return result;
    }


    @RequestMapping({"/getList"})
    @ReturnEnumsBind("ATD_PERSON_SEX")
    public Page<TrainingPerson> getList() throws Exception {
        UserInfoScope userInfoScope = this.itcMvcService.getUserInfoScopeDatas();
        Page<TrainingPerson> page = userInfoScope.getPage();
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