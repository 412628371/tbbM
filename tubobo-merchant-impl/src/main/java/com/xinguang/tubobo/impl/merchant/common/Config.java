package com.xinguang.tubobo.impl.merchant.common;

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

    private Integer payExpiredSeconds;
    private Integer maxDeliveryMills;
    @DisconfFileItem(name = "merchant.pay.expires.seconds", associateField = "payExpiredSeconds")
    public Integer getPayExpiredSeconds() {
        return payExpiredSeconds;
    }

    public void setPayExpiredSeconds(Integer payExpiredSeconds) {
        this.payExpiredSeconds = payExpiredSeconds;
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
