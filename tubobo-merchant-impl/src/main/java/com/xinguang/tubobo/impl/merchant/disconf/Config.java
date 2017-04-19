package com.xinguang.tubobo.impl.merchant.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
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

    private Integer payExpiredMilSeconds;//过期毫秒数
    private Integer maxDeliveryMills;
    private Long aliPushAppKey;
    private String noticeGrabedTemplate;
    private String noticeGrabedTimeoutTemplate;
    private String noticeFinishedTemplate;

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
        return payExpiredMilSeconds;
    }

    public void setPayExpiredMilSeconds(Integer payExpiredMilSeconds) {
        this.payExpiredMilSeconds = payExpiredMilSeconds;
    }

    @DisconfFileItem(name = "merchant.deliveryMills.max", associateField = "maxDeliveryMills")
    public Integer getMaxDeliveryMills() {
        if (maxDeliveryMills == null){
            return 50000;
        }
        return maxDeliveryMills;
    }

    public void setMaxDeliveryMills(Integer maxDeliveryMills) {
        this.maxDeliveryMills = maxDeliveryMills;
    }
}
