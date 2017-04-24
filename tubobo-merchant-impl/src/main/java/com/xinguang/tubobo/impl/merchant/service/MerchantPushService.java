package com.xinguang.tubobo.impl.merchant.service;

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

    public void pushToUser(String userId,String content,String title){
        List<String> list = new ArrayList<>(1);
        list.add(userId);
        Options options = Options.builder()
                .setTargetType(Constance.TargetType.ACCOUNT)
                .setDeviceType(Constance.DeviceType.ALL)
                .setTitle(title)
                .build();
        Long pushAppKey = config.getAliPushAppKey();
        pushService.push(content,pushAppKey,list,options);
    }

    /**
     * 通知商家
     * @param userId
     */
    public void noticeGrab(String userId){
        MerchantSettingsEntity entity = settingsService.findBuUserId(userId);
        if (entity == null || !entity.getPushMsgOrderGrabed())
            return;
        pushToUser(userId,config.getNoticeGrabedTemplate(),config.getNoticeGrabedTitle());
        logger.info("订单被接单，通知商家。userId: {}, content: {}",userId,config.getNoticeGrabedTemplate());
    }

    /**
     * 通知商家订单配送完成
     * @param userId
     */
    public void noticeFinished(String userId){
        MerchantSettingsEntity entity = settingsService.findBuUserId(userId);
        if (entity == null || !entity.getPushMsgOrderFinished())
            return;
        pushToUser(userId,config.getNoticeFinishedTemplate(),config.getNoticeFinishedTitle());
        logger.info("订单配送完成，通知商家。userId: {}, content: {}",userId,config.getNoticeFinishedTemplate());
    }

    /**
     * 通知商家超时无人抢单
     * @param userId
     */
    public void noticeGrabTimeout(String userId){
        MerchantSettingsEntity entity = settingsService.findBuUserId(userId);
        if (entity == null || !entity.getPushMsgOrderExpired())
            return;
        pushToUser(userId,config.getNoticeGrabedTimeoutTemplate(),config.getNoticeGrabedTimeoutTitle());
        logger.info("订单超时未接单，通知商家。userId: {}, content: {}",userId,config.getNoticeGrabedTimeoutTemplate());
    }
}
