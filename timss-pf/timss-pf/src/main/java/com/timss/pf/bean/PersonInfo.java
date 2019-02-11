package com.timss.pf.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.bean.ItcBusinessBean;

import java.util.Date;
import java.util.List;

/**
 * 船员档案
 */
public class PersonInfo extends ItcBusinessBean {

    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;

    /**
     * 工号
     */
    private String userId;
    /**
     * 拼音
     */
    private String pinyin;
    /**
     * 性别
     */
    private String sex;
    /**
     * 出生年月
     */
    private Date birthday;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 政治面目
     */
    private String politicalFace;
    /**
     * 籍贯/民族
     */
    private String nationality;
    /**
     * 毕业院校
     */
    private String graduateSchool;
    /**
     * 文化程度
     */
    private String highestEducation;
    /**
     * 参加工作时间
     */
    private Date workDate;
    /**
     * 通信地址
     */
    private String address;
    /**
     * 邮政编码
     */
    private String postalCode;
    /**
     * 健康情况
     */
    private String health;
    /**
     * 家庭病史
     */
    private String familyHistory;
    /**
     * 既往病史
     */
    private String history;
    /**
     * 原工作单位
     */
    private String originalWorkUnit;

    /**
     * 聘用状态
     */
    private String jobStatus;
    /**
     * 专业
     */
    private String specialty;
    /**
     * 婚姻状态
     */
    private String maritalStatus;
    /**
     * 户口地址
     */
    private String registerAddress;
    /**
     * 用工性质
     */
    private String workNature;
    /**
     * 户口性质
     */
    private String registerType;
    /**
     * 曾用名
     */
    private String historyName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 个人电话
     */
    private String mobile;
    /**
     * 加入时间
     */
    private String arrivalDate;
    /**
     * 在船状态
     */
    private String inshipStatus;
    /**
     * 船舶信息ID
     */
    private String shipId;
    /**
     * 职务
     */
    private String duties;

    /**
     * 删除标志
     */
    private String delind;
    /**
     * 与家庭成员表关联(一对多关联)
     */
    private List<PersonRelation> personRelationList;
    /**
     * 与证件信息表关联(一对多关联)
     */
    private List<PersonCertificate> personCertificateList;

    /**
     * 与用户表一对一关联
     */
    private SecureUser secureUser;
    /**
     * 与证书表表一对多关联
     */
    private List<PersonCredentials> personCredentialsList;
    /**
     * 与人员工作经历表一对多关联
     */
    private List<PersonExperinence> personExperinenceList;
    /**
     * 与船员档案表表一对一关联
     */
    private PersonSailorFile personSailorFile;

    public PersonInfo() {
        setBusinessType("PersonInfo");
        setBusinessServiceName("PersonInfoServiceImpl");
    }

    public SecureUser getSecureUser() {
        return secureUser;
    }

    public void setSecureUser(SecureUser secureUser) {
        this.secureUser = secureUser;
    }

    public List<PersonRelation> getPersonRelationList() {
        return personRelationList;
    }

    public void setPersonRelationList(List<PersonRelation> personRelationList) {
        this.personRelationList = personRelationList;
    }

    public List<PersonCertificate> getPersonCertificateList() {
        return personCertificateList;
    }

    public void setPersonCertificateList(List<PersonCertificate> personCertificateList) {
        this.personCertificateList = personCertificateList;
    }

    public List<PersonCredentials> getPersonCredentialsList() {
        return personCredentialsList;
    }

    public void setPersonCredentialsList(List<PersonCredentials> personCredentialsList) {
        this.personCredentialsList = personCredentialsList;
    }

    public List<PersonExperinence> getPersonExperinenceList() {
        return personExperinenceList;
    }

    public void setPersonExperinenceList(List<PersonExperinence> personExperinenceList) {
        this.personExperinenceList = personExperinenceList;
    }


    public PersonSailorFile getPersonSailorFile() {
        return personSailorFile;
    }

    public void setPersonSailorFile(PersonSailorFile personSailorFile) {
        this.personSailorFile = personSailorFile;
    }

    @Override
    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPoliticalFace() {
        return politicalFace;
    }

    public void setPoliticalFace(String politicalFace) {
        this.politicalFace = politicalFace;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getOriginalWorkUnit() {
        return originalWorkUnit;
    }

    public void setOriginalWorkUnit(String originalWorkUnit) {
        this.originalWorkUnit = originalWorkUnit;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getWorkNature() {
        return workNature;
    }

    public void setWorkNature(String workNature) {
        this.workNature = workNature;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public String getHistoryName() {
        return historyName;
    }

    public void setHistoryName(String historyName) {
        this.historyName = historyName;
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

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getDelind() {
        return delind;
    }

    public void setDelind(String delind) {
        this.delind = delind;
    }
}
