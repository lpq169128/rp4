package com.timss.pf.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.WFBean;

import java.util.Date;

public class HireInfo extends WFBean {
    private static final long serialVersionUID = 1978015345173641791L;
    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NEW)
    private String businessId;
    /**
     * 流水号
     */
    @AutoGen(value = "DF_SHEET_NO_SEQ",
            requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW)
    private String businessNo;
    /**
     * 招聘对象类型
     */
    private String recruitType;
    /**
     * 原因
     */
    private String remark;
    /**
     * 职务
     */
    private String duties;
    /**
     * 交班人
     */
    private String handoverUser;
    /**
     * 调往船舶
     */
    private String shipId;
    /**
     * 上船日期
     */
    private Date inShipDate;

    /**
     * 删除标志
     */
    private String delInd;
    /**
     * 流程状态
     */
    private String wfStatus;

    public HireInfo() {
        this.setBusinessType("EmploymentTest");
    }

    private HirePersonInfo hirePersonInfo;

    public HirePersonInfo getHirePersonInfo() {
        return hirePersonInfo;
    }

    public void setHirePersonInfo(HirePersonInfo hirePersonInfo) {
        this.hirePersonInfo = hirePersonInfo;
    }

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getRecruitType() {
        return recruitType;
    }

    public void setRecruitType(String recruitType) {
        this.recruitType = recruitType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getHandoverUser() {
        return handoverUser;
    }

    public void setHandoverUser(String handoverUser) {
        this.handoverUser = handoverUser;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public Date getInShipDate() {
        return inShipDate;
    }

    public void setInShipDate(Date inShipDate) {
        this.inShipDate = inShipDate;
    }

    @Override
    public String getBusinessNo() {
        return businessNo;
    }

    @Override
    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getDelInd() {
        return delInd;
    }

    public void setDelInd(String delInd) {
        this.delInd = delInd;
    }

    @Override
    public String getWfStatus() {
        return wfStatus;
    }

    @Override
    public void setWfStatus(String wfStatus) {
        this.wfStatus = wfStatus;
    }
}
