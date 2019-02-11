package com.timss.pf.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;

import java.util.Date;

/**
 * 船员档案之工作经历
 */
public class PersonExperinence extends ItcBusinessBean {

    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;

    /**
     * 船员信息ID
     */
    private String personId;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 公司名称
     */
    private String company;
    /**
     * 职务
     */
    private String duties;
    /**
     * 备注
     */
    private String remark;

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
