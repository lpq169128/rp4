package com.timss.pf.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;

import java.util.Date;

public class TrainingEffect extends ItcBusinessBean {
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
     * 培训范围/培训项
     */
    private String trainingItem;
    /**
     * 培训效果
     */
    private String trainingEffect;
    /**
     * 完成日期
     */
    private Date endDate;
    /**
     * 考核人
     */
    private String tester;

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

    public String getTrainingItem() {
        return trainingItem;
    }

    public void setTrainingItem(String trainingItem) {
        this.trainingItem = trainingItem;
    }

    public String getTrainingEffect() {
        return trainingEffect;
    }

    public void setTrainingEffect(String trainingEffect) {
        this.trainingEffect = trainingEffect;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }
}
