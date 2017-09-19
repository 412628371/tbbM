package com.xinguang.tubobo.impl.merchant.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * disconf配置.
 */
@Configuration
@Scope("singleton")
@DisconfFile(filename = "merchantBiz.properties")
public class Config {

    private static  final Logger logger = LoggerFactory.getLogger(Config.class);
        private Double dispatchRadiusKiloMiles;//派单半径,km
    private Integer taskGrabExpiredMilSeconds;//过期毫秒数
    private Integer taskPostOrderMilSeconds;//驿站订单过期毫秒数
    private Integer payExpiredMilSeconds;//过期毫秒数

    private Integer consignorPayExpiredMilliSeconds;//货主支付超时过期毫秒数
    private Integer consignorTaskExpiredMilliSeconds;//货主接单超时过期毫秒数
    private Integer maxDeliveryMills;
    private Long aliPushAppKey;
    private String noticeGrabedTemplate;
    private String noticeGrabedTimeoutTemplate;
    private String noticeGrabedTitle;
    private String noticeGrabedTimeoutTitle;
    private String noticeFinishedTemplate;
    private String noticeFinishedTitle;
    private String consignorNoticeGrabTimeoutTemplate;//大件订单超时提醒

    private String noticeOrderCanceledTemplate;
    private String noticeOrderCanceledTitle;
    private String noticeRiderOrderCanceledTitle;
    private String noticeRiderOrderCanceledTemplate;


    private String noticeAuditSuccessTitle;
    private String noticeAuditSuccessTemplate;
    private String noticeAuditFailTitle;
    private String noticeAuditFailTemplate;

    private String beginWorkTime;
    private String endWorkTime;
    private String consignorBeginWorkTime;
    private String consignorEndWorkTime;


    private Double nonConfidentialPaymentLimit;

   /* 1.6版本稳定后删除,该配置改为从数据库中获取
   private Double firstLevelInitPrice;
    private Double firstLevelPricePerKM;
    private Double secondLevelDistance;
    private Double secondLevelPricePerKM;*/
    private Long payPwdMaxErrorTimes;
    private String distributionLimitationExpression;

//    private String gdKey;
    private Integer autoRateDays;
    private String transportType;
    private int thirdOrderRemainHours;
    private String iosMusic;

    @DisconfFileItem(name = "merchant.notice.riderCancel.title", associateField = "noticeRiderOrderCanceledTitle")
    public String getNoticeRiderOrderCanceledTitle() {
        return noticeRiderOrderCanceledTitle;
    }

    public void setNoticeRiderOrderCanceledTitle(String noticeRiderOrderCanceledTitle) {
        this.noticeRiderOrderCanceledTitle = noticeRiderOrderCanceledTitle;
    }
    @DisconfFileItem(name = "merchant.notice.riderCancel.template", associateField = "noticeRiderOrderCanceledTemplate")
    public String getNoticeRiderOrderCanceledTemplate() {
        return noticeRiderOrderCanceledTemplate;
    }

    public void setNoticeRiderOrderCanceledTemplate(String noticeRiderOrderCanceledTemplate) {
        this.noticeRiderOrderCanceledTemplate = noticeRiderOrderCanceledTemplate;
    }


    @DisconfFileItem(name = "iosMusic", associateField = "iosMusic")
    public String getIosMusic() {
        return iosMusic;
    }

    public void setIosMusic(String iosMusic) {
        this.iosMusic = iosMusic;
    }

    @DisconfFileItem(name = "thirdOrderRemainHours", associateField = "thirdOrderRemainHours")
    public int getThirdOrderRemainHours() {
        return thirdOrderRemainHours;
    }

    public void setThirdOrderRemainHours(int thirdOrderRemainHours) {
        this.thirdOrderRemainHours = thirdOrderRemainHours;
    }

    @DisconfFileItem(name = "consignorNoticeGrabTimeoutTemplate", associateField = "consignorNoticeGrabTimeoutTemplate")
    public String getConsignorNoticeGrabTimeoutTemplate() {
        return consignorNoticeGrabTimeoutTemplate;
    }

    public void setConsignorNoticeGrabTimeoutTemplate(String consignorNoticeGrabTimeoutTemplate) {
        this.consignorNoticeGrabTimeoutTemplate = consignorNoticeGrabTimeoutTemplate;
    }

    @DisconfFileItem(name = "consignorTaskExpiredMilliSeconds", associateField = "consignorTaskExpiredMilliSeconds")
    public Integer getConsignorTaskExpiredMilliSeconds() {
        return consignorTaskExpiredMilliSeconds;
    }

    public void setConsignorTaskExpiredMilliSeconds(Integer consignorTaskExpiredMilliSeconds) {
        this.consignorTaskExpiredMilliSeconds = consignorTaskExpiredMilliSeconds;
    }

    @DisconfFileItem(name = "consignorPayExpiredMilliSeconds", associateField = "consignorPayExpiredMilliSeconds")
    public Integer getConsignorPayExpiredMilliSeconds() {
        return consignorPayExpiredMilliSeconds;
    }

    public void setConsignorPayExpiredMilliSeconds(Integer consignorPayExpiredMilliSeconds) {
        this.consignorPayExpiredMilliSeconds = consignorPayExpiredMilliSeconds;
    }

    @DisconfFileItem(name = "consignorBeginWorkTime", associateField = "consignorBeginWorkTime")
    public String getConsignorBeginWorkTime() {
        return consignorBeginWorkTime;
    }

    public void setConsignorBeginWorkTime(String consignorBeginWorkTime) {
        this.consignorBeginWorkTime = consignorBeginWorkTime;
    }
    @DisconfFileItem(name = "consignorEndWorkTime", associateField = "consignorEndWorkTime")
    public String getConsignorEndWorkTime() {
        return consignorEndWorkTime;
    }

    public void setConsignorEndWorkTime(String consignorEndWorkTime) {
        this.consignorEndWorkTime = consignorEndWorkTime;
    }

    @DisconfFileItem(name = "transportType", associateField = "transportType")
    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    @DisconfFileItem(name = "autoRateDays", associateField = "autoRateDays")
    public Integer getAutoRateDays() {
        return autoRateDays;
    }

    public void setAutoRateDays(Integer autoRateDays) {
        this.autoRateDays = autoRateDays;
    }


    @DisconfFileItem(name = "distributionLimitationExpression", associateField = "distributionLimitationExpression")
    public String getDistributionLimitationExpression() {
        return distributionLimitationExpression;
    }

    public void setDistributionLimitationExpression(String distributionLimitationExpression) {
        this.distributionLimitationExpression = distributionLimitationExpression;
    }

    @DisconfFileItem(name = "payPwdMaxErrorTimes", associateField = "payPwdMaxErrorTimes")
    public Long getPayPwdMaxErrorTimes() {
        return payPwdMaxErrorTimes;
    }

    public void setPayPwdMaxErrorTimes(Long payPwdMaxErrorTimes) {
        this.payPwdMaxErrorTimes = payPwdMaxErrorTimes;
    }

   /* @DisconfFileItem(name = "firstLevelInitPrice", associateField = "firstLevelInitPrice")
    public Double getFirstLevelInitPrice() {
        return firstLevelInitPrice;
    }

    public void setFirstLevelInitPrice(Double firstLevelInitPrice) {
        this.firstLevelInitPrice = firstLevelInitPrice;
    }

    @DisconfFileItem(name = "firstLevelPricePerKM", associateField = "firstLevelPricePerKM")
    public Double getFirstLevelPricePerKM() {
        return firstLevelPricePerKM;
    }

    public void setFirstLevelPricePerKM(Double firstLevelPricePerKM) {
        this.firstLevelPricePerKM = firstLevelPricePerKM;
    }

    @DisconfFileItem(name = "secondLevelDistance", associateField = "secondLevelDistance")
    public Double getSecondLevelDistance() {
        return secondLevelDistance;
    }

    public void setSecondLevelDistance(Double secondLevelDistance) {
        this.secondLevelDistance = secondLevelDistance;
    }
    @DisconfFileItem(name = "secondLevelPricePerKM", associateField = "secondLevelPricePerKM")
    public Double getSecondLevelPricePerKM() {
        return secondLevelPricePerKM;
    }

    public void setSecondLevelPricePerKM(Double secondLevelPricePerKM) {
        this.secondLevelPricePerKM = secondLevelPricePerKM;
    }*/

    @DisconfFileItem(name = "nonConfidentialPaymentLimit", associateField = "nonConfidentialPaymentLimit")
    public Double getNonConfidentialPaymentLimit() {
        return nonConfidentialPaymentLimit;
    }

    public void setNonConfidentialPaymentLimit(Double nonConfidentialPaymentLimit) {
        this.nonConfidentialPaymentLimit = nonConfidentialPaymentLimit;
    }


    @DisconfFileItem(name = "beginWorkTime", associateField = "beginWorkTime")
    public String getBeginWorkTime() {
        logger.info("beginWorkTime:{}",beginWorkTime);
        return beginWorkTime;
    }

    @DisconfFileItem(name = "endWorkTime", associateField = "endWorkTime")
    public String getEndWorkTime() {
        logger.info("endWorkTime:{}",endWorkTime);
        return endWorkTime;
    }

    @DisconfFileItem(name = "dispatch.radius.kiloMiles", associateField = "dispatchRadiusKiloMiles")
    public Double getDispatchRadiusKiloMiles() {
        if (null == dispatchRadiusKiloMiles){
            logger.info("dispatchRadiusKiloMiles配置为空，使用默认值：{}",MerchantConstants.DISPATCH_RADIUS_BY_KiloMILLS);
            return MerchantConstants.DISPATCH_RADIUS_BY_KiloMILLS;
        }
        logger.info("dispatchRadiusKiloMiles:{}",dispatchRadiusKiloMiles);
        return dispatchRadiusKiloMiles;
    }
    public void setTaskPostOrderMilSeconds(Integer taskPostOrderMilSeconds) {
        this.taskPostOrderMilSeconds = taskPostOrderMilSeconds;
    }
    @DisconfFileItem(name = "task.post.order.milseconds", associateField = "getTaskPostOrderMilSeconds")
    public Integer getTaskPostOrderGrabExpiredMilSeconds() {
        if (null == taskPostOrderMilSeconds){
            logger.info("getTaskPostOrderMilSeconds，使用默认值：{}",MerchantConstants.POST_ORDER_GRABEXPIRED_MilSECONDS);
            return MerchantConstants.POST_ORDER_GRABEXPIRED_MilSECONDS;
        }
        logger.info("taskPostOrderMilSeconds:{}",taskPostOrderMilSeconds);
        return taskPostOrderMilSeconds;
    }
    @DisconfFileItem(name = "task.grab.expires.milseconds", associateField = "taskGrabExpiredMilSeconds")
    public Integer getTaskGrabExpiredMilSeconds() {
        if (null == taskGrabExpiredMilSeconds){
            logger.info("taskGrabExpiredMilSeconds，使用默认值：{}",MerchantConstants.GRAB_EXPIRED_TIME_BY_MilSECONDS);
            return MerchantConstants.GRAB_EXPIRED_TIME_BY_MilSECONDS;
        }
        logger.info("taskGrabExpiredMilSeconds:{}",taskGrabExpiredMilSeconds);
        return taskGrabExpiredMilSeconds;
    }

    @DisconfFileItem(name = "merchant.notice.grabed.template", associateField = "noticeGrabedTemplate")
    public String getNoticeGrabedTemplate() {
        return noticeGrabedTemplate;
    }

    public void setNoticeGrabedTemplate(String noticeGrabedTemplate) {
        this.noticeGrabedTemplate = noticeGrabedTemplate;
    }


    @DisconfFileItem(name = "merchant.notice.grabTimeout.template", associateField = "noticeGrabedTimeoutTemplate")
    public String getNoticeGrabedTimeoutTemplate() {
        return noticeGrabedTimeoutTemplate;
    }

    public void setNoticeGrabedTimeoutTemplate(String noticeGrabedTimeoutTemplate) {
        this.noticeGrabedTimeoutTemplate = noticeGrabedTimeoutTemplate;
    }


    public void setEndWorkTime(String endWorkTime) {
        this.endWorkTime = endWorkTime;
    }

    public void setTaskGrabExpiredMilSeconds(Integer taskGrabExpiredMilSeconds) {
        this.taskGrabExpiredMilSeconds = taskGrabExpiredMilSeconds;
    }

    public void setDispatchRadiusKiloMiles(Double dispatchRadiusKiloMiles) {
        this.dispatchRadiusKiloMiles = dispatchRadiusKiloMiles;
    }

//    public void setInitDistanceByMiles(Double initDistanceByMiles) {
//        this.initDistanceByMiles = initDistanceByMiles;
//    }
//
//    public void setInitPrice(Double initPrice) {
//        this.initPrice = initPrice;
//    }
//
//    public void setPricePerKiloMiles(Double pricePerKiloMiles) {
//        this.pricePerKiloMiles = pricePerKiloMiles;
//    }

    @DisconfFileItem(name = "merchant.notice.finished.template", associateField = "noticeFinishedTemplate")
    public String getNoticeFinishedTemplate() {
        return noticeFinishedTemplate;
    }
    public void setNoticeFinishedTemplate(String noticeFinishedTemplate) {
        this.noticeFinishedTemplate = noticeFinishedTemplate;
    }

    @DisconfFileItem(name = "alipush.appkey", associateField = "aliPushAppKey")
    public Long getAliPushAppKey() {
        return aliPushAppKey;
    }

    @DisconfFileItem(name = "merchant.pay.expired.milliseconds", associateField = "payExpiredMilSeconds")
    public Integer getPayExpiredMilSeconds() {
        if (null == payExpiredMilSeconds){
            logger.info("payExpiredMilSeconds配置为空，使用默认值：{}",MerchantConstants.PAY_EXPIRED_TIME_BY_MilSECONDS);
            return MerchantConstants.PAY_EXPIRED_TIME_BY_MilSECONDS;
        }
        logger.info("payExpiredMilSeconds:{}",payExpiredMilSeconds);
        return payExpiredMilSeconds;
    }

    @DisconfFileItem(name = "merchant.deliveryMills.max", associateField = "maxDeliveryMills")
    public Integer getMaxDeliveryMills() {
        if (maxDeliveryMills == null){
            logger.info("maxDeliveryMills配置为空，使用默认值：{}",4000);
            return 4000;
        }
        logger.info("maxDeliveryMills:{}",maxDeliveryMills);
        return maxDeliveryMills;
    }

    @DisconfFileItem(name = "merchant.notice.grabed.title", associateField = "noticeGrabedTitle")
    public String getNoticeGrabedTitle() {
        return noticeGrabedTitle;
    }

    @DisconfFileItem(name = "merchant.notice.grabTimeout.title", associateField = "noticeGrabedTimeoutTitle")
    public String getNoticeGrabedTimeoutTitle() {
        return noticeGrabedTimeoutTitle;
    }

    public void setPayExpiredMilSeconds(Integer payExpiredMilSeconds) {
        this.payExpiredMilSeconds = payExpiredMilSeconds;
    }

    @DisconfFileItem(name = "merchant.notice.finished.title", associateField = "noticeFinishedTitle")
    public String getNoticeFinishedTitle() {
        return noticeFinishedTitle;
    }

    public void setAliPushAppKey(Long aliPushAppKey) {
        this.aliPushAppKey = aliPushAppKey;
    }

    public void setNoticeGrabedTimeoutTitle(String noticeGrabedTimeoutTitle) {
        this.noticeGrabedTimeoutTitle = noticeGrabedTimeoutTitle;
    }

    public void setNoticeGrabedTitle(String noticeGrabedTitle) {
        this.noticeGrabedTitle = noticeGrabedTitle;
    }

    public void setMaxDeliveryMills(Integer maxDeliveryMills) {
        this.maxDeliveryMills = maxDeliveryMills;
    }

    public void setNoticeFinishedTitle(String noticeFinishedTitle) {
        this.noticeFinishedTitle = noticeFinishedTitle;
    }

    public void setBeginWorkTime(String beginWorkTime) {
        this.beginWorkTime = beginWorkTime;
    }

    @DisconfFileItem(name = "merchant.notice.orderCancel.template", associateField = "noticeOrderCanceledTemplate")
    public String getNoticeOrderCanceledTemplate() {
        return noticeOrderCanceledTemplate;
    }

    public void setNoticeOrderCanceledTemplate(String noticeOrderCanceledTemplate) {
        this.noticeOrderCanceledTemplate = noticeOrderCanceledTemplate;
    }

    @DisconfFileItem(name = "merchant.notice.orderCancel.title", associateField = "noticeOrderCanceledTitle")
    public String getNoticeOrderCanceledTitle() {
        return noticeOrderCanceledTitle;
    }

    public void setNoticeOrderCanceledTitle(String noticeOrderCanceledTitle) {
        this.noticeOrderCanceledTitle = noticeOrderCanceledTitle;
    }
    @DisconfFileItem(name = "merchant.notice.auditSuccess.title", associateField = "noticeAuditSuccessTitle")
    public String getNoticeAuditSuccessTitle() {
        return noticeAuditSuccessTitle;
    }

    public void setNoticeAuditSuccessTitle(String noticeAuditSuccessTitle) {
        this.noticeAuditSuccessTitle = noticeAuditSuccessTitle;
    }
    @DisconfFileItem(name = "merchant.notice.auditSuccess.template", associateField = "noticeAuditSuccessTemplate")
    public String getNoticeAuditSuccessTemplate() {
        return noticeAuditSuccessTemplate;
    }

    public void setNoticeAuditSuccessTemplate(String noticeAuditSuccessTemplate) {
        this.noticeAuditSuccessTemplate = noticeAuditSuccessTemplate;
    }
    @DisconfFileItem(name = "merchant.notice.auditFail.title", associateField = "noticeAuditFailTitle")
    public String getNoticeAuditFailTitle() {
        return noticeAuditFailTitle;
    }

    public void setNoticeAuditFailTitle(String noticeAuditFailTitle) {
        this.noticeAuditFailTitle = noticeAuditFailTitle;
    }
    @DisconfFileItem(name = "merchant.notice.auditFail.template", associateField = "noticeAuditFailTemplate")
    public String getNoticeAuditFailTemplate() {
        return noticeAuditFailTemplate;
    }

    public void setNoticeAuditFailTemplate(String noticeAuditFailTemplate) {
        this.noticeAuditFailTemplate = noticeAuditFailTemplate;
    }
}
