package com.xinguang.tubobo.merchant.web.controller.data_fix;

import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.MerchantToTaskCenterServiceInterface;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.fix.ReqFixUncanceledGrabOvertime;
import com.xinguang.tubobo.merchant.web.response.fix.RespFixUncanceledGrabOvertime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */
@Controller
@RequestMapping("/fix/grabOvertime")
public class FixErrorDataController extends MerchantBaseController<ReqFixUncanceledGrabOvertime,RespFixUncanceledGrabOvertime> {

    private static final Logger logger = LoggerFactory.getLogger(FixErrorDataController.class);
    @Autowired
    MerchantToTaskCenterServiceInterface merchantToTaskCenterServiceInterface;
    @Autowired
    MerchantOrderService merchantOrderService;
    @Autowired
    Config config;

    @Override
    protected RespFixUncanceledGrabOvertime doService(String userId, ReqFixUncanceledGrabOvertime req) throws MerchantClientException {
        if (req == null || !"asdfzxc".equals(req.getPassword())){
            return new RespFixUncanceledGrabOvertime("forbidden");
        }
        Integer grabOverMilSeconds = config.getTaskGrabExpiredMilSeconds();
        List<String> orderNos = merchantOrderService.getUnCanceledGrabOvertimeOrderNoList(grabOverMilSeconds);
        if (orderNos == null || orderNos.size() == 0){
            return new RespFixUncanceledGrabOvertime("nothing to do, everything is ok");
        }
        logger.info("处理抢单超时，未取消的订单。发现异常订单数：{}", orderNos.size());
        for (String orderNo: orderNos){
            merchantOrderService.dealGrabOvertimeOrders(orderNo,new Date(),false);
            logger.info("处理抢单超时，未取消的异常订单。orderNo：{}", orderNo);
        }
        return new RespFixUncanceledGrabOvertime("deal "+orderNos.size()+" 条异常单");
    }


    @Override
    protected boolean needLogin() {
        return false;
    }
}
