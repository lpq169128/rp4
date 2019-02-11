package com.timss.pf.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.WFBean;

import java.util.Date;

public class ShipReimburse extends WFBean {
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
     * 报销单名称
     */
    private String reimburseName;

    /**
     * 船舶Id
     */
    private String shipId;

    /**
     * 报销日期
     */
    private Date reimburseDate;

    /**
     * 描述
     */
    private String remark;

    /**
     * 船舶名称
     */
    private String shipName;

    public ShipReimburse() {
        this.setBusinessType("paymentApprovalDet");
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

    public String getReimburseName() {
        return reimburseName;
    }

    public void setReimburseName(String reimburseName) {
        this.reimburseName = reimburseName;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public Date getReimburseDate() {
        return reimburseDate;
    }

    public void setReimburseDate(Date reimburseDate) {
        this.reimburseDate = reimburseDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }
}
