package com.timss.pf.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;

import java.util.Date;

/**
 * 船员档案之工作交接
 */
public class PersonWork extends ItcBusinessBean {

    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;

    /**
     * 船员信息ID
     */
    private String personId;
    /**
     * 在船ID
     */
    private String shipId;

    /**
     * 上船日期/接班日期
     */
    private Date successionDate;
    /**
     * 下船日期/交班日期
     */
    private Date shiftDate;

    /**
     * 原因
     */
    private String reason;

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

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public Date getSuccessionDate() {
        return successionDate;
    }

    public void setSuccessionDate(Date successionDate) {
        this.successionDate = successionDate;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
