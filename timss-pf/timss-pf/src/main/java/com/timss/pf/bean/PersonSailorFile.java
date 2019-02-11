package com.timss.pf.bean;

import com.timss.management.bean.MgmtShipInfo;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;

import java.util.List;

/**
 * 船员档案之船员档案
 */
public class PersonSailorFile extends ItcBusinessBean {

    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;

    /**
     * 船员信息ID
     */
    private String personId;

    /**
     * 在船状态
     */
    private String inshipStatus;
    /**
     * 在船ID
     */
    private String shipId;
    /**
     * 船舶信息ID
     */
    private String idm;
    /**
     * 船舶信息名称
     */
    private String shipName;
    /**
     * 船舶信息发动机吨位
     */
    private Double load;
    /**
     * 船舶信息吨位
     */
    private String grossTon;
    /**
     * 船舶信息国籍
     */
    private String nationalityPerson;
    /**
     * 船舶信息公司
     */
    private String siteName;
    /**
     * 船舶信息公司
     */
    private String sailArea;

    /**
     * 跟船舶信息关联(一对一)
     */
    private MgmtShipInfo mgmtShipInfo;
    /**
     * 与海上资历表一对多关联
     */
    private List<PersonVoyage> personVoyageList;

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

    public String getInshipStatus() {
        return inshipStatus;
    }

    public void setInshipStatus(String inshipStatus) {
        this.inshipStatus = inshipStatus;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public Double getLoad() {
        return load;
    }

    public void setLoad(Double load) {
        this.load = load;
    }

    public List<PersonVoyage> getPersonVoyageList() {
        return personVoyageList;
    }

    public void setPersonVoyageList(List<PersonVoyage> personVoyageList) {
        this.personVoyageList = personVoyageList;
    }

    public MgmtShipInfo getMgmtShipInfo() {
        return mgmtShipInfo;
    }

    public void setMgmtShipInfo(MgmtShipInfo mgmtShipInfo) {
        this.mgmtShipInfo = mgmtShipInfo;
    }

    public String getIdm() {
        return idm;
    }

    public void setIdm(String idm) {
        this.idm = idm;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getGrossTon() {
        return grossTon;
    }

    public void setGrossTon(String grossTon) {
        this.grossTon = grossTon;
    }

    public String getNationalityPerson() {
        return nationalityPerson;
    }

    public void setNationalityPerson(String nationalityPerson) {
        this.nationalityPerson = nationalityPerson;
    }

    @Override
    public String getSiteName() {
        return siteName;
    }

    @Override
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSailArea() {
        return sailArea;
    }

    public void setSailArea(String sailArea) {
        this.sailArea = sailArea;
    }
}
