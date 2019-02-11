package com.timss.pf.dao;

import com.timss.pf.bean.HireInfo;
import com.yudean.itc.annotation.RowFilter;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.mvc.dao.abstr.AbstractDao;
import com.yudean.mvc.dao.abstr.WfAbstractDao;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface HireInfoDao extends WfAbstractDao<HireInfo> {
    List<Organization> selectWokeChildrenByParentCode(@Param("orgCode") String orgCode);

    List<Organization> queryTeamBySiteId(@Param("siteId") String siteId);

}
