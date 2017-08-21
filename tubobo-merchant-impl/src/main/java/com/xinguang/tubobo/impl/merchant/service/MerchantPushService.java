package com.xinguang.tubobo.impl.merchant.service;

import com.alibaba.fastjson.JSON;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.alipush.NoticeParamVo;
import com.xinguang.tubobo.impl.merchant.alipush.NoticePushVo;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import com.xinguang.tubobo.impl.merchant.entity.PushNoticeEntity;
import com.xinguang.tubobo.merchant.api.dto.NoticeDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumNoticeType;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderNoticeType;
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
    private final String androidActivity="com.toobob.www.PopupPushActivity";

    public void pushToUser(String userId,String content,String title,String extraJson){
        List<String> list = new ArrayList<>(1);
        list.add(userId);
        Options options = Options.builder()
                .setTargetType(Constance.TargetType.ACCOUNT)
                .setDeviceType(Constance.DeviceType.ALL)
                .setTitle(title)
                .setiOSBadge(0)
              // .setAndroidOpenType(Constance.AndroidOpenType.NONE)
                .setExtParameters(extraJson)
                .setAndroidPopupBody(androidActivity)
                .build();
        Long pushAppKey = config.getAliPushAppKey();
        try{
            pushService.push(content,pushAppKey,list,options);
            logger.info("状态通知已发送给userId:{}",userId);
            logger.info("发送的data为:{}",extraJson);
        }catch (Exception e){
            logger.error("push to userId:{},content:{},title:{}.异常：",userId,content,title,e);
        }
    }
    public void pushToUserWithSound(String userId,String content,String title,String extraJson){
        List<String> list = new ArrayList<>(1);
        list.add(userId);
        Options options = Options.builder()
                .setTargetType(Constance.TargetType.ACCOUNT)
                .setDeviceType(Constance.DeviceType.ALL)
                .setTitle(title)
                .setiOSBadge(0)
                .setAndroidOpenType(Constance.AndroidOpenType.NONE)
                .setExtParameters(extraJson)
                .setiOSMusic(config.getIosMusic())
                .build();
        Long pushAppKey = config.getAliPushAppKey();
        try{
            pushService.push(content,pushAppKey,list,options);
            logger.info("状态通知已发送给userId:{}",userId);
            logger.info("发送的data为:{}",extraJson);
        }catch (Exception e){
            logger.error("push to userId:{},content:{},title:{}.异常：",userId,content,title,e);
        }
    }

    public void pushToAll(String content,String title,String extraJson){
        List<String> list = new ArrayList<>(1);
        list.add("1");
        Options options = Options.builder()
                .setTargetType(Constance.TargetType.ALL)
                .setDeviceType(Constance.DeviceType.ALL)
                .setTitle(title)
                .setiOSBadge(0)
                .setAndroidOpenType(Constance.AndroidOpenType.NONE)
                .setExtParameters(extraJson)
                .setAndroidPopupBody(androidActivity)
                .build();
        Long pushAppKey = config.getAliPushAppKey();
        try{
            pushService.push(content,pushAppKey,list,options);
            logger.info("状态通知已发送给所有人,title:{}",title);
        }catch (Exception e){
            logger.error("push to all user,content:{},title:{}.异常：",content,title,e);
        }
    }

//    /**
//     * 通知商家
//     * @param userId
//     */
//    public void noticeGrab(String userId,String orderNo,String type){
//        MerchantSettingsEntity entity = settingsService.findBuUserId(userId);
//        if (entity == null || !entity.getPushMsgOrderGrabed())
//            return;
//
//        pushToUser(userId,config.getNoticeGrabedTemplate(),config.getNoticeGrabedTitle(),
//                generateExtraParam(userId,orderNo,type));
//        logger.info("订单被接单，通知商家。userId: {}, content: {}",userId,config.getNoticeGrabedTemplate());
//    }
//
//    /**
//     * 通知商家订单配送完成
//     * @param userId
//     */
//    public void noticeFinished(String userId,String orderNo,String type){
//        MerchantSettingsEntity entity = settingsService.findBuUserId(userId);
//        if (entity == null || !entity.getPushMsgOrderFinished())
//            return;
//        pushToUser(userId,config.getNoticeFinishedTemplate(),config.getNoticeFinishedTitle(),
//                generateExtraParam(userId,orderNo,type));
//        logger.info("订单配送完成，通知商家。userId: {}, content: {}",userId,config.getNoticeFinishedTemplate());
//    }
//
//    /**
//     * 通知商家超时无人抢单
//     * @param userId
//     */
//    public void noticeGrabTimeout(String userId,String orderNo,String type){
//        MerchantSettingsEntity entity = settingsService.findBuUserId(userId);
//        if (entity == null || !entity.getPushMsgOrderExpired())
//            return;
//        String grabTimeoutTemplete = config.getNoticeGrabedTimeoutTemplate();
//        if (MerchantConstants.PUSH_ORDER_TYPE_BIG.equals(type)){
//            grabTimeoutTemplete = config.getConsignorNoticeGrabTimeoutTemplate();
//        }
//        pushToUser(userId,grabTimeoutTemplete,config.getNoticeGrabedTimeoutTitle(),
//                generateExtraParam(userId,orderNo,type));
//        logger.info("订单超时未接单，通知商家。userId: {}, content: {}",userId,grabTimeoutTemplete);
//    }



    private static String  generateExtraParam(String userId, String orderNo, String type){
        NoticeParamVo paramVo = new NoticeParamVo();
        paramVo.setOrderNo(orderNo);
        NoticePushVo noticePushVo = new NoticePushVo();
        noticePushVo.setType(type);
        noticePushVo.setParams(paramVo);
        noticePushVo.setUserId(userId);
        String s = JSON.toJSONString(noticePushVo);
        return s;
    }
    private static String  generateCommonPushParam(String userId, EnumNoticeType enumNoticeType,String id){
        NoticeParamVo paramVo = new NoticeParamVo();
        paramVo.setId(id);
        NoticePushVo noticePushVo = new NoticePushVo();
        noticePushVo.setNoticeType(enumNoticeType.getValue());
        noticePushVo.setParams(paramVo);
        noticePushVo.setUserId(userId);
        String s = JSON.toJSONString(noticePushVo);
        return s;
    }
    private static String  generateAuditPushParam(String userId, EnumNoticeType enumNoticeType,String id,String reason,String verifyResult){
        NoticeParamVo paramVo = new NoticeParamVo();
        paramVo.setId(id);
        paramVo.setReason(reason);
        paramVo.setVerifyResult(verifyResult);
        NoticePushVo noticePushVo = new NoticePushVo();
        noticePushVo.setNoticeType(enumNoticeType.getValue());
        noticePushVo.setParams(paramVo);
        noticePushVo.setUserId(userId);
        String s = JSON.toJSONString(noticePushVo);
        return s;
    }
    private static String  generateOrderPushParam(String userId,
                                                  Long id,String orderType,String orderNo){
        NoticeParamVo paramVo = new NoticeParamVo();
        paramVo.setId(String.valueOf(id));
        paramVo.setOrderNo(orderNo);
        paramVo.setOrderType(orderType);
        NoticePushVo noticePushVo = new NoticePushVo();
        noticePushVo.setNoticeType(EnumNoticeType.ORDER.getValue());
        noticePushVo.setParams(paramVo);
        noticePushVo.setUserId(userId);
        noticePushVo.setType(MerchantConstants.getPushParamByOrderType(orderType));
        String s = JSON.toJSONString(noticePushVo);
        return s;
    }
    /**
     * 推送审核结果通知
     * @param entity
     */
    public void pushAuditNotice(PushNoticeEntity entity){
        if (entity == null)
            return;
        String data = generateAuditPushParam(entity.getUserId(),EnumNoticeType.AUDIT,String.valueOf(entity.getId()),entity.getReason(),entity.getIdentifyType()+"_"+entity.getIdentifyStatus());
        pushToUser(entity.getUserId(),entity.getContent(),entity.getTitle(), data);
    }
    /**
     * 推送系统公告
     * @param dto
     */
    public void pushSystemNotice(NoticeDTO dto){
        if (dto == null)
            return;
        //TODO 通知的标题和内容待处理
        String data = generateCommonPushParam(dto.getUserId(),EnumNoticeType.SYSTEM,dto.getId());
        pushToAll(dto.getSummary(),dto.getTitle(),data);
    }

    /**
     * 推送订单通知
     * @param entity
     */
    public void pushOrderNotice(PushNoticeEntity entity){
        if (entity == null)
            return;
        boolean isOpen = false;
        if (EnumOrderNoticeType.ADMIN_CANCEL.getValue().equals(entity.getOrderOperateType())){
            isOpen = true;
        }else {
            MerchantSettingsEntity settingsEntity = settingsService.findBuUserId(entity.getUserId());
            if (null == settingsEntity)
                return;
            if (settingsEntity.getPushMsgOrderExpired() &&
                    EnumOrderNoticeType.GRAB_EXPIRED.getValue().equals(entity.getOrderOperateType())){
                isOpen = true;
                if (settingsEntity.getPushMsgVoiceOpen()){
                    //订单超时 语音提醒 TODO param 修改适配andriod
                    String data = generateOrderPushParam(entity.getUserId(),
                            entity.getId(),entity.getOrderType(),entity.getOrderNo());
                    pushToUserWithSound(entity.getUserId(),entity.getContent(),entity.getTitle(), data);
                    return;
                }
            }
            if (settingsEntity.getPushMsgOrderFinished() &&
                    EnumOrderNoticeType.FINISH.getValue().equals(entity.getOrderOperateType())){
                isOpen = true;

            }
            if (settingsEntity.getPushMsgOrderGrabed() &&
                    EnumOrderNoticeType.ACCEPTED.getValue().equals(entity.getOrderOperateType())){
                isOpen = true;
            }
        }
        if (isOpen){
            String data = generateOrderPushParam(entity.getUserId(),
                    entity.getId(),entity.getOrderType(),entity.getOrderNo());
            pushToUser(entity.getUserId(),entity.getContent(),entity.getTitle(), data);
        }
    }
    public static void main(String[] args) {
//        String s = generateCommonPushParam("30126",EnumNoticeType.AUDIT,12L);
        String s = generateOrderPushParam("30126",13L,"smallOrder","20140000001");
        System.out.println(s);
    }
}
