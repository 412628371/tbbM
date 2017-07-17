package com.xinguang.tubobo.impl.merchant.mq;

import com.alibaba.fastjson.JSONObject;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.merchant.api.dto.NoticeDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumAuthentication;
import com.xinguang.tubobo.merchant.api.enums.EnumNoticeType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuqinghua on 2017/7/13.
 */
@Service
public class RmqNoticeProducer {
    @Autowired private Config config;
    @Autowired private RabbitTemplate baseMqTemplate;

    /**
     * 发送订单被接单通知
     * @param userId
     * @param orderNo
     * @param orderType
     * @param platformCode
     * @param originOrderViewId
     */
    public void sendGrabNotice(String userId, String orderNo, String orderType,String platformCode,String originOrderViewId){
        NoticeDTO noticeDTO = getBaseOrderNoticeDto(userId,orderNo,orderType,platformCode,originOrderViewId);
        noticeDTO.setTitle(config.getNoticeGrabedTitle());
        noticeDTO.setContent(config.getNoticeGrabedTemplate());
        sendNotice(noticeDTO);
    }

    /**
     * 发送订单超时未接单通知
     * @param userId
     * @param orderNo
     * @param orderType
     * @param platformCode
     * @param originOrderViewId
     */
    public void sendGrabTimeoutNotice(String userId, String orderNo, String orderType,String platformCode,String originOrderViewId){
        NoticeDTO noticeDTO = getBaseOrderNoticeDto(userId,orderNo,orderType,platformCode,originOrderViewId);
        noticeDTO.setTitle(config.getNoticeGrabedTimeoutTitle());
        noticeDTO.setContent(config.getNoticeGrabedTimeoutTemplate());
        sendNotice(noticeDTO);
    }

    /**
     * 发送订单完成通知
     * @param userId
     * @param orderNo
     * @param orderType
     * @param platformCode
     * @param originOrderViewId
     */
    public void sendOrderFinishNotice(String userId, String orderNo, String orderType,String platformCode,String originOrderViewId){
        NoticeDTO noticeDTO = getBaseOrderNoticeDto(userId,orderNo,orderType,platformCode,originOrderViewId);
        noticeDTO.setTitle(config.getNoticeFinishedTitle());
        noticeDTO.setContent(config.getNoticeFinishedTemplate());
        sendNotice(noticeDTO);
    }

    /**
     * 发送订单被取消通知
     * @param userId
     * @param orderNo
     * @param orderType
     * @param platformCode
     * @param originOrderViewId
     */
    public void sendOrderCancelNotice(String userId, String orderNo, String orderType,String platformCode,String originOrderViewId){
        NoticeDTO noticeDTO = getBaseOrderNoticeDto(userId,orderNo,orderType,platformCode,originOrderViewId);
        noticeDTO.setTitle(config.getNoticeOrderCanceledTitle());
        noticeDTO.setContent(config.getNoticeOrderCanceledTemplate());
        sendNotice(noticeDTO);
    }

    /**
     * 发送审核结果通知
     * @param userId
     * @param auditSuccess
     * @return
     */
    private void sendAuditNotice(String userId,boolean auditSuccess){
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setNoticeType(EnumNoticeType.AUDIT.getValue());
        noticeDTO.setUserId(userId);
        if (auditSuccess){
            noticeDTO.setTitle(config.getNoticeAuditSuccessTitle());
            noticeDTO.setContent(config.getNoticeAuditSuccessTemplate());
            noticeDTO.setIdentifyStatus(EnumAuthentication.SUCCESS.getValue());
        }else {
            noticeDTO.setTitle(config.getNoticeAuditFailTitle());
            noticeDTO.setContent(config.getNoticeAuditFailTemplate());
            noticeDTO.setIdentifyStatus(EnumAuthentication.FAIL.getValue());
        }
        sendNotice(noticeDTO);
    }
    /**
     * 组装订单类型推送的DTO
     * @param userId
     * @param orderNo
     * @param orderType
     * @param platformCode
     * @param originOrderViewId
     * @return
     */
    private NoticeDTO getBaseOrderNoticeDto(String userId, String orderNo, String orderType,String platformCode,String originOrderViewId){
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setNoticeType(EnumNoticeType.ORDER.getValue());
        noticeDTO.setOrderNo(orderNo);
        noticeDTO.setPlatformCode(platformCode);
        noticeDTO.setOriginOrderViewId(originOrderViewId);
        noticeDTO.setOrderType(orderType);
        noticeDTO.setUserId(userId);
        return noticeDTO;
    }
    private void sendNotice(NoticeDTO noticeDTO){
        baseMqTemplate.convertAndSend("tbb_notice_exchange","notice_bindingKey_merchant",
                JSONObject.toJSONString(noticeDTO));
    }
}
