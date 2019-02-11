package com.timss.pf.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;


public class ReimburseCost extends ItcBusinessBean {
    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;
    /**
     * 报销ID
     */
    private String reimburseId;
    /**
     * 费用类型
     */
    private String costType;
    /**
     * 描述
     */
    private String description;

    /**
     * 申请金额
     */
    private double amount;
    /**
     * 批准金额
     */
    private double repliedAmount;
    /**
     * 申请美元
     */
    private double attribute1;
    /**
     * 批准美元
     */
    private double attribute2;

    public ReimburseCost() {
        setBusinessType("ReimburseCost");
        setBusinessServiceName("ReimburseCostServiceImpl");
    }

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getReimburseId() {
        return reimburseId;
    }

    public void setReimburseId(String reimburseId) {
        this.reimburseId = reimburseId;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRepliedAmount() {
        return repliedAmount;
    }

    public void setRepliedAmount(double repliedAmount) {
        this.repliedAmount = repliedAmount;
    }

    public double getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(double attribute1) {
        this.attribute1 = attribute1;
    }

    public double getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(double attribute2) {
        this.attribute2 = attribute2;
    }
}
