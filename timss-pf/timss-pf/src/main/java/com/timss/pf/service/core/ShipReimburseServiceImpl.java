package com.timss.pf.service.core;


import com.timss.facade.service.abstr.WfAbstractServiceImpl;
import com.timss.pf.bean.ReimburseCost;
import com.timss.pf.bean.ShipReimburse;
import com.timss.pf.dao.ReimburseCostDao;
import com.timss.pf.dao.ShipReimburseDao;
import com.timss.pf.service.ReimburseCostService;
import com.timss.pf.service.ShipReimburseService;
import com.yudean.itc.annotation.CUDTarget;
import com.yudean.itc.annotation.Operator;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.util.data.DataFormatUtil;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ShipReimburseServiceImpl extends WfAbstractServiceImpl<ShipReimburse> implements ShipReimburseService {

    protected ShipReimburseDao shipReimburseDao;
    @Autowired
    private ReimburseCostService reimburseCostService;
    @Autowired
    private ReimburseCostDao reimburseCostDao;

    @Autowired
    protected void setShipReimburseDao(ShipReimburseDao dao) {
        this.shipReimburseDao = dao;
        this.setDao(dao);
    }

    /**
     * 重写WfAbstractServiceImpl的方法
     *
     * @param bean
     * @param operator
     * @throws Exception
     */
    public void fixWFBean(@CUDTarget ShipReimburse bean, @Operator SecureUser operator) throws Exception {
        if (org.apache.commons.lang3.StringUtils.isBlank(bean.getBusinessUrl())) {
            bean.setBusinessUrl("pf/shipReimburse/detailPage.do?mode=view&id=" + bean.getBusinessId());
        }
        String desc = DataFormatUtil.substr("船舶报销", 200);
        if (!desc.equals(bean.getBusinessDesc())) {
            bean.setBusinessDesc(desc);
        }
    }


    /**
     * 重写保存子项目
     *
     * @param bean
     * @param operator
     * @throws Exception
     */
    public void saveItem(@CUDTarget ShipReimburse bean, @Operator SecureUser operator) throws Exception {
        JSONArray itemForm = JSONArray.fromObject(bean.getInsertItemMap().get("itemForm"));
        if (itemForm.size() > 0) {
            for (int i = 0; i < itemForm.size(); i++) {
                UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
                JSONObject JsonItemFrom = itemForm.getJSONObject(i);
                ReimburseCost reimburseCost = new ReimburseCost();
                if (!JsonItemFrom.getString("amount").equals("")) {
                    reimburseCost.setAmount(Double.parseDouble(JsonItemFrom.getString("amount")));
                }
                reimburseCost.setCostType(JsonItemFrom.getString("costType"));
                reimburseCost.setDescription(JsonItemFrom.getString("description"));
                reimburseCost.setReimburseId(bean.getBusinessId());
                if (!JsonItemFrom.getString("attribute1").equals("")) {
                    reimburseCost.setAttribute1(Double.parseDouble(JsonItemFrom.getString("attribute1")));
                }
                if (!JsonItemFrom.getString("attribute2").equals("")) {
                    reimburseCost.setAttribute2(Double.parseDouble(JsonItemFrom.getString("attribute2")));
                }
                reimburseCostService.insert(reimburseCost, userInfo.getSecureUser());
            }

        }
        JSONArray itemFormUpdate = JSONArray.fromObject(bean.getUpdateItemMap().get("itemForm"));
        if (itemFormUpdate.size() > 0) {
            for (int i = 0; i < itemFormUpdate.size(); i++) {
                UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
                JSONObject JsonItemFrom = itemFormUpdate.getJSONObject(i);
                ReimburseCost reimburseCost = new ReimburseCost();
                if (!JsonItemFrom.getString("amount").equals("")) {
                    reimburseCost.setAmount(Double.parseDouble(JsonItemFrom.getString("amount")));
                }
                reimburseCost.setCostType(JsonItemFrom.getString("costType"));
                reimburseCost.setDescription(JsonItemFrom.getString("description"));
                reimburseCost.setReimburseId(bean.getBusinessId());
                if (!JsonItemFrom.getString("attribute1").equals("")) {
                    reimburseCost.setAttribute1(Double.parseDouble(JsonItemFrom.getString("attribute1")));
                }
                if (!JsonItemFrom.getString("attribute2").equals("")) {
                    reimburseCost.setAttribute2(Double.parseDouble(JsonItemFrom.getString("attribute2")));
                }
                if (!JsonItemFrom.getString("repliedAmount").equals("")) {
                    reimburseCost.setRepliedAmount(Double.parseDouble(JsonItemFrom.getString("repliedAmount")));
                }
                reimburseCost.setBusinessId(JsonItemFrom.getString("businessId"));
                reimburseCostService.update(reimburseCost, userInfo.getSecureUser());
            }

        }

        JSONArray itemFormDelete = JSONArray.fromObject(bean.getDeleteItemMap().get("itemForm"));
        if (itemFormDelete.size() > 0) {
            for (int i = 0; i < itemFormDelete.size(); i++) {
                UserInfoScope userInfo = this.itcMvcService.getUserInfoScopeDatas();
                JSONObject JsonItemFrom = itemFormDelete.getJSONObject(i);
                if (JsonItemFrom.size() > 0) {
                    reimburseCostService.delete(JsonItemFrom.getString("businessId"), userInfo.getSecureUser());
                }

            }
        }
    }

    /**
     * 重写AbstractServiceImpl中的setItem方法，重新放置付款审批子项目信息
     *
     * @param bean
     * @param operator
     * @throws Exception
     */
    public void setItem(@CUDTarget ShipReimburse bean, @Operator SecureUser operator) throws Exception {
        List<ReimburseCost> reimburseCostList = reimburseCostDao.queryReimburseCostByReimburseId(bean.getBusinessId());
        if (reimburseCostList.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (ReimburseCost reimburseCost : reimburseCostList) {
                Map<String, String> result = new HashMap<String, String>();
                result.put("businessId", reimburseCost.getBusinessId());
                if (reimburseCost.getCostType() != null) {
                    result.put("costType", reimburseCost.getCostType());
                }
                if (reimburseCost.getDescription() != null) {
                    result.put("description", reimburseCost.getDescription());
                }
                if (reimburseCost.getAmount() != 0) {
                    result.put("amount", String.valueOf(reimburseCost.getAmount()));
                }
                if (reimburseCost.getAttribute1() != 0) {
                    result.put("attribute1", String.valueOf(reimburseCost.getAttribute1()));
                }
                if (reimburseCost.getAttribute2() != 0) {
                    result.put("attribute2", String.valueOf(reimburseCost.getAttribute2()));
                }
                if (reimburseCost.getRepliedAmount() != 0) {
                    result.put("repliedAmount", String.valueOf(reimburseCost.getRepliedAmount()));
                }
                jsonArray.add(result);
            }
            bean.setItemMap("itemForm", jsonArray);
        }
    }
}
