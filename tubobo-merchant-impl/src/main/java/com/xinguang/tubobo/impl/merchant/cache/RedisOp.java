package com.xinguang.tubobo.impl.merchant.cache;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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
    @Autowired
    private CacheManager cacheManager;
    public void evictCache(String key){
        cacheManager.getCache(RedisCache.MERCHANT).evict(key);
    }
    private void increment(String opType,String userId,long step){
        stringRedisTemplate.opsForValue().increment(generateKey(opType,userId),step);
    }
    private void initZero(String key){
        stringRedisTemplate.opsForValue().set(key,"0");
    }

    public void pwdWrong(String type,String userId) throws MerchantClientException {
        increment(type,userId,1);
        mindAvailablePwdWrongTimes(type,userId);
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
            throw new MerchantClientException(EnumRespCode.ACCOUNT_PWD_ERROR_TOO_MUCH,
                    String.valueOf(config.getPayPwdMaxErrorTimes()));
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

    /**
     * 提醒剩余错误次数
     * @throws MerchantClientException
     */
    private void mindAvailablePwdWrongTimes(String type,String userId) throws MerchantClientException {
        //获取剩余可用次数
        long availableWrongTimes = getAvailableWrongTimes(type,userId);
        if (availableWrongTimes <= 0){
            throw new MerchantClientException(EnumRespCode.ACCOUNT_PWD_ERROR_TOO_MUCH,
                    String.valueOf(config.getPayPwdMaxErrorTimes()));
        }
        throw  new MerchantClientException(EnumRespCode.ACCOUNT_PWD_ERROR,
                String.valueOf(availableWrongTimes));
    }
    private String generateKey(String opType,String userId){
        return opType+"_"+userId;
    }
}
