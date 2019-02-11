package com.timss.pf.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;

import java.util.Date;

public class BeforeInstruction extends ItcBusinessBean {
    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;
    /**
     * ID
     */
    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NEW)
    private String trainingId;
    /**
     * 流水号
     */
    @AutoGen(value = "DF_SHEET_NO_SEQ",
            requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NEW)
    private String trainingNo;
    /**
     * 培训类型（大类）
     */
    private String trainingType;
    /**
     * 船名Id
     */
    private String deptShip;
    /**
     * 船名
     */
    private String shipName;
    /**
     * 职务
     */
    private String duties;
    /**
     * 姓名
     */
    private String nameId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 任职日期
     */
    private Date dutiesDate;
    /**
     * 授课主要内容
     */
    private String content;
    /**
     * 一周内完成内容
     */
    private String attribute1;

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDutiesDate() {
        return dutiesDate;
    }

    public void setDutiesDate(Date dutiesDate) {
        this.dutiesDate = dutiesDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getDeptShip() {
        return deptShip;
    }

    public void setDeptShip(String deptShip) {
        this.deptShip = deptShip;
    }

    public String getTrainingNo() {
        return trainingNo;
    }

    public void setTrainingNo(String trainingNo) {
        this.trainingNo = trainingNo;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }
}
