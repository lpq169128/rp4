package com.timss.pf.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.WFBean;

import java.util.Date;

public class PersonExamine extends WFBean {
    private static final long serialVersionUID = 1978015345173641791L;
    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NEW)
    private String businessId;
    /**
     * 流水号
     */
    @AutoGen(value = "DF_SHEET_NO_SEQ",
            requireType = AutoGen.GenerationType.REQUIRED_NEW)
    private String businessNo;
    /**
     * 人员Id
     */
    private String personId;
    /**
     * 船舶Id
     */
    private String shipId;
    /**
     * 职务
     */
    private String duties;
    /**
     * 考核等级
     */
    private String testLevel;
    /**
     * 任职日期
     */
    private String onJobDate;
    /**
     * 是否完成合同
     */
    private String isFinishContract;

    /**
     * 中途解雇原因
     */
    private String description;
    /**
     * 删除标志
     */
    private String delInd;
    /**
     * 姓名
     */
    private String name;
    private String sex;
    private Date birthday;
    /**
     * 籍贯/民族
     */
    private String nationality;

    public PersonExamine() {
        this.setBusinessType("crewAssess");
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public String getBusinessNo() {
        return businessNo;
    }

    @Override
    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getTestLevel() {
        return testLevel;
    }

    public void setTestLevel(String testLevel) {
        this.testLevel = testLevel;
    }

    public String getOnJobDate() {
        return onJobDate;
    }

    public void setOnJobDate(String onJobDate) {
        this.onJobDate = onJobDate;
    }

    public String getIsFinishContract() {
        return isFinishContract;
    }

    public void setIsFinishContract(String isFinishContract) {
        this.isFinishContract = isFinishContract;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDelInd() {
        return delInd;
    }

    public void setDelInd(String delInd) {
        this.delInd = delInd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}


