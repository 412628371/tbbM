package com.xinguang.tubobo.impl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.utils.RedisUtil;
import com.hzmux.hzcms.common.utils.StringUtils;

/**
 * 手机号联想服务
 * <p>使用redis set 的方式实现</p>
 * @author hanyong
 *
 */
@Service
public class AssociateMobileNumService {
    
    private static final String key_prefix = "storeMobileNumSet_";
    
    /**
     * 
     * @param storeId
     * @param mobileNumber
     */
    public void addMobileNumber(String storeId, String mobileNumber) {
        String key = genKey(storeId, mobileNumber);
        if(key == null) return ;
        
//        RedisUtil.getJedis().sadd(key, mobileNumber);
        RedisUtil.sadd(key, mobileNumber);
    }
    /**
     * 
     * @param storeId 所属门店
     * @param numSuffix 手机号后5位
     * @return
     */
    public List<String> associateMobileNum(String storeId, String numSuffix) {
        String key = genKey(storeId, numSuffix);
        if(key == null) return Collections.emptyList();
        
//        Set<String> set = RedisUtil.getJedis().smembers(key);
        Set<String> set = RedisUtil.smembers(key);
        
        if(null ==set) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(set);
        }
    }
    
    private String genKey(String storeId, String inputNum) {
        if(StringUtils.isBlank(storeId)
                || StringUtils.isBlank(inputNum)
                || !NumberUtils.isDigits(inputNum)) {
            return null;
        }
        StringBuffer sb = (new StringBuffer(key_prefix)).append(storeId).append("_"); 
        if(inputNum.length() == 11) {
            return sb.append(inputNum.substring(6)).toString();
        } else if(inputNum.length() == 5) {
            return sb.append(inputNum).toString();
        } else {
            return null;
        }
    }
    
}
