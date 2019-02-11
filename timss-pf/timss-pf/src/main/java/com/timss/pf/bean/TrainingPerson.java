package com.timss.pf.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;

import java.util.Date;

public class TrainingPerson extends ItcBusinessBean {
    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;
    /**
     * ID
     */
    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NEW)
    private String id;
    /**
     * 培训ID
     */
    private String trainingId;

    /**
     * 人员ID
     */
    private String personId;

    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 毕业院校
     */
    private String graduateSchool;
    /**
     * 文化程度
     */
    private String highestEducation;
    /**
     * 个人电话
     */
    private String mobile;
    /**
     * 职务
     */
    private String duties;
    /**
     * 任职日期
     */
    private Date dutiesDate;

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public Date getDutiesDate() {
        return dutiesDate;
    }

    public void setDutiesDate(Date dutiesDate) {
        this.dutiesDate = dutiesDate;
    }
}
