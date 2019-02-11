package com.timss.pf.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.mvc.bean.ItcBusinessBean;

/**
 * 船员档案之家庭成员
 */
public class PersonRelation extends ItcBusinessBean {

    @UUIDGen(requireType = UUIDGen.GenerationType.REQUIRED_NULL)
    private String businessId;

    /**
     * 船员信息ID
     */
    private String personId;

    /**
     * 配偶或亲属
     * 姓名
     */
    private String kinName;

    /**
     * 关系
     */
    private String relation;
    /**
     * 联系电话
     */
    private String phone;

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

    public String getKinName() {
        return kinName;
    }

    public void setKinName(String kinName) {
        this.kinName = kinName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
