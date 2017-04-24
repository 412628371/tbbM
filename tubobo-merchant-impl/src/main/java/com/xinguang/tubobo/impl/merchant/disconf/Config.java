package com.xinguang.tubobo.impl.merchant.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created by shade on 2017/4/6.
 */
@Configuration
@Scope("singleton")
@DisconfFile(filename = "common.properties")
public class Config {

    private Double dispatchRadiusMils;//派单半径米
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


    @DisconfFileItem(name = "dispatch.radius.mils", associateField = "dispatchRadiusMils")
    public Double getDispatchRadiusMils() {
        if (null == dispatchRadiusMils){
            return MerchantConstants.DISPATCH_RADIUS_BY_MILLS;
        }
        return dispatchRadiusMils;
    }

    public void setDispatchRadiusMils(Double dispatchRadiusMils) {
        this.dispatchRadiusMils = dispatchRadiusMils;
    }

    public void setTaskGrabExpiredMilSeconds(Integer taskGrabExpiredMilSeconds) {
        this.taskGrabExpiredMilSeconds = taskGrabExpiredMilSeconds;
    }

    @DisconfFileItem(name = "task.grab.expires.milseconds", associateField = "taskGrabExpiredMilSeconds")
    public Integer getTaskGrabExpiredMilSeconds() {
        if (null == taskGrabExpiredMilSeconds){
            return MerchantConstants.GRAB_EXPIRED_TIME_BY_MilSECONDS;
        }
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
            return MerchantConstants.PAY_EXPIRED_TIME_BY_MilSECONDS;
        }
        return payExpiredMilSeconds;
    }

    public void setPayExpiredMilSeconds(Integer payExpiredMilSeconds) {
        this.payExpiredMilSeconds = payExpiredMilSeconds;
    }

    @DisconfFileItem(name = "merchant.deliveryMills.max", associateField = "maxDeliveryMills")
    public Integer getMaxDeliveryMills() {
        if (maxDeliveryMills == null){
            return 4000;
        }
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
