package com.timss.pf.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;

import java.util.Date;

/**
 * 船员档案之海上资历表
 */
public class PersonVoyage extends ItcBusinessBean {

    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;

    /**
     * 船员档案ID
     */
    private String soilorId;

    /**
     * 船名
     */
    private String shipName;
    /**
     * 职务
     */
    private String duties;
    /**
     * 总吨/马力
     */
    private String power;
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 公司
     */
    private String company;

    /**
     * 上船时间
     */
    private Date inShipDate;

    /**
     * 下船时间
     */
    private Date outShipDate;

    /**
     * 航行区域
     */
    private String area;
    /**
     * 描述
     */
    private String discription;

    /**
     * 区分字段
     */
    private String different;

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getSoilorId() {
        return soilorId;
    }

    public void setSoilorId(String soilorId) {
        this.soilorId = soilorId;
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

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getInShipDate() {
        return inShipDate;
    }

    public void setInShipDate(Date inShipDate) {
        this.inShipDate = inShipDate;
    }

    public Date getOutShipDate() {
        return outShipDate;
    }

    public void setOutShipDate(Date outShipDate) {
        this.outShipDate = outShipDate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getDifferent() {
        return different;
    }

    public void setDifferent(String different) {
        this.different = different;
    }
}
