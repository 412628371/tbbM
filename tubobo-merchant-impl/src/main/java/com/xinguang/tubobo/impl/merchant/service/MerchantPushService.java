package com.xinguang.tubobo.impl.merchant.service;

import com.alibaba.fastjson.JSON;
import com.xinguang.tubobo.impl.merchant.alipush.NoticeParamVo;
import com.xinguang.tubobo.impl.merchant.alipush.NoticePushVo;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import com.xinguang.tubobo.push.Constance;
import com.xinguang.tubobo.push.Options;
import com.xinguang.tubobo.push.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/4/19.
 */
@Service
public class MerchantPushService {
    Logger logger = LoggerFactory.getLogger(MerchantPushService.class);
    @Autowired
    PushService pushService;
    @Autowired
    Config config;
    @Autowired
    MerchantSettingsService settingsService;

    public void pushToUser(String userId,String content,String title,String extraJson){
        List<String> list = new ArrayList<>(1);
        list.add(userId);
        Options options = Options.builder()
                .setTargetType(Constance.TargetType.ACCOUNT)
                .setDeviceType(Constance.DeviceType.ALL)
                .setTitle(title)
                .setiOSBadge(0)
                .setExtParameters(extraJson)
                .build();
        Long pushAppKey = config.getAliPushAppKey();
        try{
            pushService.push(content,pushAppKey,list,options);
            logger.info("状态通知已发送给userId:{}",userId);
        }catch (Exception e){
            logger.error("push to userId:{},content:{},title:{}.异常：",userId,content,title,e);
        }
    }


    /**
     * 通知商家
     * @param userId
     */
    public void noticeGrab(String userId,String orderNo,String type){
        MerchantSettingsEntity entity = settingsService.findBuUserId(userId);
        if (entity == null || !entity.getPushMsgOrderGrabed())
            return;

        pushToUser(userId,config.getNoticeGrabedTemplate(),config.getNoticeGrabedTitle(),generateExtraParam(orderNo,type));
        logger.info("订单被接单，通知商家。userId: {}, content: {}",userId,config.getNoticeGrabedTemplate());
    }
//    public void noticeGrab(String userId,String orderNo){
//        noticeGrab(userId,orderNo, MerchantConstants.PUSH_ORDER_TYPE_SMALL);
//    }

    /**
     * 通知商家订单配送完成
     * @param userId
     */
    public void noticeFinished(String userId,String orderNo,String type){
        MerchantSettingsEntity entity = settingsService.findBuUserId(userId);
        if (entity == null || !entity.getPushMsgOrderFinished())
            return;
        pushToUser(userId,config.getNoticeFinishedTemplate(),config.getNoticeFinishedTitle(),generateExtraParam(orderNo,type));
        logger.info("订单配送完成，通知商家。userId: {}, content: {}",userId,config.getNoticeFinishedTemplate());
    }
//    public void noticeFinished(String userId,String orderNo){
//        noticeFinished(userId,orderNo,MerchantConstants.PUSH_ORDER_TYPE_SMALL);
//    }

    /**
     * 通知商家超时无人抢单
     * @param userId
     */
    public void noticeGrabTimeout(String userId,String orderNo,String type){
        MerchantSettingsEntity entity = settingsService.findBuUserId(userId);
        if (entity == null || !entity.getPushMsgOrderExpired())
            return;
        String grabTimeoutTemplete = config.getNoticeGrabedTimeoutTitle();
        if (MerchantConstants.PUSH_ORDER_TYPE_SMALL.equals(type)){
            grabTimeoutTemplete = config.getConsignorNoticeGrabTimeoutTemplate();
        }
        pushToUser(userId,config.getNoticeGrabedTimeoutTemplate(),grabTimeoutTemplete,generateExtraParam(orderNo,type));
        logger.info("订单超时未接单，通知商家。userId: {}, content: {}",userId,config.getNoticeGrabedTimeoutTemplate());
    }
//    public void noticeGrabTimeout(String userId,String orderNo){
//        noticeGrabTimeout(userId,orderNo,MerchantConstants.PUSH_ORDER_TYPE_SMALL);
//    }

    private static String  generateExtraParam(String orderNo,String type){
        NoticeParamVo paramVo = new NoticeParamVo();
        paramVo.setOrderNo(orderNo);
        NoticePushVo noticePushVo = new NoticePushVo();
        noticePushVo.setType(type);
        noticePushVo.setParams(paramVo);
        String s = JSON.toJSONString(noticePushVo);
        return s;
    }
    public static void main(String[] args) {
        String s = generateExtraParam("aaa","orderDetail-big");
        System.out.println(s);
    }
}
