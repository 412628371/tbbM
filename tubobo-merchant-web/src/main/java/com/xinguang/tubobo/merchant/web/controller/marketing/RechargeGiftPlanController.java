package com.xinguang.tubobo.merchant.web.controller.marketing;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.service.MerchantTypeService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.MerchantTypeDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.marketing.RechargeGiftPlanItem;
import com.xinguang.tubobo.merchant.web.response.marketing.RechargeGiftPlanResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
@Controller
@RequestMapping("/marketing/rechargeGiftPlan")
public class RechargeGiftPlanController extends MerchantBaseController<Object,RechargeGiftPlanResp> {
    private static final Logger logger = LoggerFactory.getLogger(RechargeGiftPlanController.class);

    @Autowired
    private TbbAccountService tbbAccountService;

    @Autowired
    MerchantInfoService merchantInfoService;
    @Autowired
    MerchantTypeService merchantTypeService;

    @Override
    protected RechargeGiftPlanResp doService(String userId, Object req) throws MerchantClientException {

        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        MerchantTypeDTO type = merchantTypeService.findById(infoEntity.getMerTypeId());
        String plan = tbbAccountService.currentRechargeGiftPlan(infoEntity.getAccountId(),type.getName());

        RechargeGiftPlanResp resp = new RechargeGiftPlanResp();
        List<RechargeGiftPlanItem> list;
        if (StringUtils.isNotBlank(plan)){
            String[] items= plan.split(",");
            list = new ArrayList<>(items.length);
            for (String item:items){
                String[] parts = item.split(":");
                RechargeGiftPlanItem giftPlanItem = new RechargeGiftPlanItem(parts[0],parts[1]);
                list.add(giftPlanItem);
            }
        }else {
            list = new ArrayList<>(0);
        }
        resp.setList(list);
        return resp;
    }

//    public static void main(String[] args) {
////        String plan = "100:30,200:50";
//        String plan = "";
//        List<RechargeGiftPlanItem> list;
//        if (StringUtils.isNotBlank(plan)){
//            String[] items= plan.split(",");
//            list = new ArrayList<>(items.length);
//            for (String item:items){
//                String[] parts = item.split(":");
//                RechargeGiftPlanItem giftPlanItem = new RechargeGiftPlanItem(parts[0],parts[1]);
//                list.add(giftPlanItem);
//            }
//        }else {
//            list = new ArrayList<>(0);
//        }
//        RechargeGiftPlanResp resp = new RechargeGiftPlanResp();
//        resp.setList(list);
//        logger.info(resp.toString());
//    }

    @Override
    protected boolean needLogin() {
        return true;
    }
}
