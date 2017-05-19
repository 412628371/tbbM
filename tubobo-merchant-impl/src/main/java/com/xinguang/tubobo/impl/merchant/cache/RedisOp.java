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
    public void increment(String opType,String userId,long step){
        stringRedisTemplate.opsForValue().increment(generateKey(opType,userId),step);
    }
    private void initZero(String key){
        stringRedisTemplate.opsForValue().set(key,"0");
    }

    /**
     * 检查支付密码错误次数是否达到限制值
     * @param opType
     * @throws MerchantClientException
     */
    public void checkPwdErrorTimes(String opType,String userId) throws MerchantClientException {
        String timeStr = stringRedisTemplate.opsForValue().get(generateKey(opType,userId));
        Long currentTimes = 0L;
        if (StringUtils.isNotBlank(timeStr)){
            currentTimes = Long.valueOf(timeStr);
        }
        if (currentTimes >= config.getPayPwdMaxErrorTimes()){
            throw new MerchantClientException(EnumRespCode.ACCOUNT_PWD_ERROR_TOO_MUCH);
        }
    }

    /**
     * 获取剩余错误次数
     * @param opType
     * @param userId
     * @return
     */
    public long getAvailableWrongTimes(String opType,String userId){
        String timeStr = stringRedisTemplate.opsForValue().get(generateKey(opType,userId));
        Long currentTimes = 0L;
        if (StringUtils.isNotBlank(timeStr)){
            currentTimes = Long.valueOf(timeStr);
        }
        return config.getPayPwdMaxErrorTimes()-currentTimes;
    }
    /**
     * 重置所有的支付密码错误计数
     */
    public void resetPwdErrorTimes(String userId){
        initZero(generateKey(MerchantConstants.KEY_PWD_WRONG_TIMES_FREE , userId));
        initZero(generateKey(MerchantConstants.KEY_PWD_WRONG_TIMES_MODIFY ,userId));
        initZero(generateKey(MerchantConstants.KEY_PWD_WRONG_TIMES_PAY , userId));
    }

    private String generateKey(String opType,String userId){
        return opType+"_"+userId;
    }
}
