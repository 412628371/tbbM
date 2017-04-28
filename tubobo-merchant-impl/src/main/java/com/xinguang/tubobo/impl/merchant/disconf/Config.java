package com.xinguang.tubobo.impl.merchant.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created by shade on 2017/4/6.
 */
@Configuration
@Scope("singleton")
@DisconfFile(filename = "common.properties")
public class Config {

    private static  final Logger logger = LoggerFactory.getLogger(Config.class);
    private Double dispatchRadiusKiloMiles;//派单半径,km
    private Integer taskGrabExpiredMilSeconds;//过期毫秒数
    private Integer payExpiredMilSeconds;//过期毫秒数
    private Integer maxDeliveryMills;
    private Long aliPushAppKey;
    private String noticeGrabedTemplate;
    private String noticeGrabedTimeoutTemplate;
    private String noticeFinishedTemplate;
    private String noticeGrabedTitle;
    private String noticeGrabedTimeoutTitle;
    private String noticeFinishedTitle;

    private String beginWorkTime;
    private String endWorkTime;

    @DisconfFileItem(name = "beginWorkTime", associateField = "beginWorkTime")
    public String getBeginWorkTime() {
        logger.info("beginWorkTime:{}",beginWorkTime);
        return beginWorkTime;
    }

    public void setBeginWorkTime(String beginWorkTime) {
        this.beginWorkTime = beginWorkTime;
    }
    @DisconfFileItem(name = "endWorkTime", associateField = "endWorkTime")
    public String getEndWorkTime() {
        logger.info("endWorkTime:{}",endWorkTime);
        return endWorkTime;
    }

    public void setEndWorkTime(String endWorkTime) {
        this.endWorkTime = endWorkTime;
    }

    public void setDispatchRadiusKiloMiles(Double dispatchRadiusKiloMiles) {
        this.dispatchRadiusKiloMiles = dispatchRadiusKiloMiles;
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


    public void setTaskGrabExpiredMilSeconds(Integer taskGrabExpiredMilSeconds) {
        this.taskGrabExpiredMilSeconds = taskGrabExpiredMilSeconds;
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

    public void setAliPushAppKey(Long aliPushAppKey) {
        this.aliPushAppKey = aliPushAppKey;
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

    public void setPayExpiredMilSeconds(Integer payExpiredMilSeconds) {
        this.payExpiredMilSeconds = payExpiredMilSeconds;
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

    public void setMaxDeliveryMills(Integer maxDeliveryMills) {
        this.maxDeliveryMills = maxDeliveryMills;
    }

    @DisconfFileItem(name = "merchant.notice.grabed.title", associateField = "noticeGrabedTitle")
    public String getNoticeGrabedTitle() {
        return noticeGrabedTitle;
    }

    public void setNoticeGrabedTitle(String noticeGrabedTitle) {
        this.noticeGrabedTitle = noticeGrabedTitle;
    }

    @DisconfFileItem(name = "merchant.notice.grabTimeout.title", associateField = "noticeGrabedTimeoutTitle")
    public String getNoticeGrabedTimeoutTitle() {
        return noticeGrabedTimeoutTitle;
    }

    public void setNoticeGrabedTimeoutTitle(String noticeGrabedTimeoutTitle) {
        this.noticeGrabedTimeoutTitle = noticeGrabedTimeoutTitle;
    }

    @DisconfFileItem(name = "merchant.notice.finished.title", associateField = "noticeFinishedTitle")
    public String getNoticeFinishedTitle() {
        return noticeFinishedTitle;
    }

    public void setNoticeFinishedTitle(String noticeFinishedTitle) {
        this.noticeFinishedTitle = noticeFinishedTitle;
    }
}
