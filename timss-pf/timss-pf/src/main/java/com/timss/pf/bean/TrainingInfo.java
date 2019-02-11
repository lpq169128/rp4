package com.timss.pf.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;

import java.util.Date;

/**
 * 培训记录之培训表&开航前指令共用
 */
public class TrainingInfo extends ItcBusinessBean {
    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;
    /**
     * 培训ID
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
     * 培训名称
     */
    private String trainingName;
    /**
     * 负责人Id
     */
    private String handlerId;
    /**
     * 负责人姓名
     */
    private String handler;
    /**
     * 培训日期
     */
    private Date startDate;
    /**
     * 地点
     */
    private String address;
    /**
     * 部门/船舶
     */
    private String deptShip;
    /**
     * 授课人
     */
    private String teacher;
    /**
     * 课时
     */
    private String hour;
    /**
     * 分类
     */
    private String trainingCategory;
    /**
     * 类型
     */
    private String trainingType;
    /**
     * 费用
     */
    private String cost;
    /**
     * 培训效果
     */
    private String trainingEffect;
    /**
     * 授课主要内容
     */
    private String content;
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

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public String getTrainingNo() {
        return trainingNo;
    }

    public void setTrainingNo(String trainingNo) {
        this.trainingNo = trainingNo;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeptShip() {
        return deptShip;
    }

    public void setDeptShip(String deptShip) {
        this.deptShip = deptShip;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTrainingEffect() {
        return trainingEffect;
    }

    public void setTrainingEffect(String trainingEffect) {
        this.trainingEffect = trainingEffect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTrainingCategory() {
        return trainingCategory;
    }

    public void setTrainingCategory(String trainingCategory) {
        this.trainingCategory = trainingCategory;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }
}
