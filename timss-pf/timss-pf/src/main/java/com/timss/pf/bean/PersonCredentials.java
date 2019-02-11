package com.timss.pf.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;

import java.util.Date;

/**
 * 船员档案之证书
 */
public class PersonCredentials extends ItcBusinessBean {

    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;

    /**
     * 船员信息ID
     */
    private String personId;

    /**
     * 证书类型
     */
    private String credentialsType;

    /**
     * 证书编号
     */
    private String credentialsNo;
    /**
     * 签发机关
     */
    private String issuer;
    /**
     * 日期类型
     */
    private String dateType;
    /**
     * 日期
     */
    private Date credentialsDate;

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

    public String getCredentialsType() {
        return credentialsType;
    }

    public void setCredentialsType(String credentialsType) {
        this.credentialsType = credentialsType;
    }

    public String getCredentialsNo() {
        return credentialsNo;
    }

    public void setCredentialsNo(String credentialsNo) {
        this.credentialsNo = credentialsNo;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public Date getCredentialsDate() {
        return credentialsDate;
    }

    public void setCredentialsDate(Date credentialsDate) {
        this.credentialsDate = credentialsDate;
    }
}
