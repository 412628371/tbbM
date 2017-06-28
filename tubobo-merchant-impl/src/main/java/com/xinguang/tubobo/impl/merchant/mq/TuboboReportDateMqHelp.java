package com.xinguang.tubobo.impl.merchant.mq;

import com.alibaba.fastjson.JSONObject;
import com.xinguang.taskcenter.api.request.TaskCreateDTO;
import com.xinguang.tubobo.api.MqMsgDTO.*;
import com.xinguang.tubobo.api.enums.EnumMqMsgType;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 同步报表信息
 * Created by ou_young on 2017/06/26.
 */
@Service
public class TuboboReportDateMqHelp {

    private static Logger logger = LoggerFactory.getLogger(TuboboReportDateMqHelp.class);

    //兔波波报表队列
    private static String REPORTDATA_MQ_QUEUE = "reportDataMqQueue";

    //默认所属区域
    private static String BELONG_AREA = "杭州";

    @Autowired
    private RabbitTemplate reportDataMqTemplate;

    public void merchantRegister(MerchantInfoEntity entity) {
        MerchantRegisterDTO dto = new MerchantRegisterDTO();
        dto.setMsgType(EnumMqMsgType.MERCHANT_REGISTER.getValue());
        dto.setMerchantId(entity.getUserId());
        dto.setAccountId(entity.getAccountId() != null ? entity.getAccountId().toString():"");
        dto.setGoodOwnerStatus(entity.getConsignorStatus());
        dto.setMerchantStatus(entity.getMerchantStatus());
        dto.setName(entity.getRealName());
        dto.setPhone(entity.getPhone());
        dto.setIdCard(entity.getIdCardNo());
        dto.setShopName(entity.getMerchantName());
        dto.setRegisterTime(entity.getCreateDate());
        dto.setBelongArea(BELONG_AREA);
        sendToMq(dto);
    }

    public void merchantStatusVerify(String userId, String riderStatus) {
        MerchantUpdateDTO dto = new MerchantUpdateDTO();
        dto.setMsgType(EnumMqMsgType.MERCHANT_UPDATE.getValue());
        dto.setMerchantId(userId);
        dto.setMerchantStatus(riderStatus);
        dto.setMerchantAuthTime(new Date());
        sendToMq(dto);
    }

    public void goodOwnerStatusVerify(String userId, String driverStatus) {
        MerchantUpdateDTO dto = new MerchantUpdateDTO();
        dto.setMsgType(EnumMqMsgType.MERCHANT_UPDATE.getValue());
        dto.setMerchantId(userId);
        dto.setGoodOwnerStatus(driverStatus);
        dto.setGoodOwnerAuthTime(new Date());
        sendToMq(dto);
    }

    public void updateBD(String userId, String bd) {
        MerchantUpdateDTO dto = new MerchantUpdateDTO();
        dto.setMsgType(EnumMqMsgType.MERCHANT_UPDATE.getValue());
        dto.setMerchantId(userId);
        dto.setBelongBDId(bd);
        sendToMq(dto);
    }

    public void merchantWithdraw(String userId, Long amount) {
        MerchantWithdrawDTO dto = new MerchantWithdrawDTO();
        dto.setMsgType(EnumMqMsgType.MERCHANT_WITHDRAW.getValue());
        dto.setMerchantId(userId);
        dto.setWithdrawCash(Long.valueOf(amount).intValue());
        sendToMq(dto);
    }

    public void merchantOrder(TaskCreateDTO task) {
        MerchantOrderDTO dto = new MerchantOrderDTO();
        dto.setMsgType(EnumMqMsgType.MERCHANT_ORDER.getValue());
        dto.setId(task.getOrderNo());
        dto.setMerchantName(task.getSenderName());
        dto.setMerchantId(task.getUserId());
        dto.setDeliveryDistance(task.getDeliveryDistance());
        dto.setSendBillTime(task.getOrderTime());
        dto.setOrderAmount(task.getPayAmount()*100);
        dto.setBenefitAmount(task.getWeatherOverFee()*100+task.getPeekOverFee()*100);
        sendToMq(dto);
    }

    public void orderCancelBySys(String orderNo) {
        OrderCancelDTO dto = new OrderCancelDTO();
        dto.setMsgType(EnumMqMsgType.SYS_CANCEL_ORDER.getValue());
        dto.setId(orderNo);
        sendToMq(dto);
    }

    public void rateRider(String riderId,int timeScore, int serviceScore) {
        RateRiderDTO dto = new RateRiderDTO();
        dto.setMsgType(EnumMqMsgType.RATE_RIDER.getValue());
        dto.setRiderId(riderId);
        dto.setTimeScore(timeScore);
        dto.setServiceScore(serviceScore);
        sendToMq(dto);
    }

    private void sendToMq(Object obj){
        reportDataMqTemplate.convertAndSend(REPORTDATA_MQ_QUEUE, JSONObject.toJSONString(obj));
    }

}
