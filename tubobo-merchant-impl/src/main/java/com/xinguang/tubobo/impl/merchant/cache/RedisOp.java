package com.xinguang.tubobo.impl.merchant.cache;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 * Created by Administrator on 2017/5/18.
 */
@Service
public class RedisOp {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private Config config;
    public void increment(String key,long step){
        stringRedisTemplate.opsForValue().increment(key,step);
    }
    public void initZero(String key){
        stringRedisTemplate.opsForValue().set(key,"0");
    }

    /**
     * 检查支付密码错误次数是否达到限制值
     * @param key
     * @throws MerchantClientException
     */
    public void checkPwdErrorTimes(String key) throws MerchantClientException {
        String timeStr = stringRedisTemplate.opsForValue().get(key);
        Long currentTimes = 0L;
        if (StringUtils.isNotBlank(timeStr)){
            currentTimes = Long.valueOf(timeStr);
        }
        if (currentTimes >= config.getPayPwdMaxErrorTimes()){
            throw new MerchantClientException(EnumRespCode.ACCOUNT_PWD_ERROR_TOO_MUCH);
        }
    }

    /**
     * 重置所有的支付密码错误计数
     */
    public void resetPwdErrorTimes(){
        initZero(MerchantConstants.KEY_PWD_WRONG_TIMES_FREE);
        initZero(MerchantConstants.KEY_PWD_WRONG_TIMES_MODIFY);
        initZero(MerchantConstants.KEY_PWD_WRONG_TIMES_PAY);
    }

    public Long getLongValue(String key){
        return Long.valueOf(stringRedisTemplate.opsForValue().get(key));
    }
}
