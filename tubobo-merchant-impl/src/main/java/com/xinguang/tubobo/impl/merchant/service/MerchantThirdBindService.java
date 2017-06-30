package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.dao.MerchantThirdBindDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by xuqinghua on 2017/6/29.
 */
@Service
@Transactional(readOnly = true)
public class MerchantThirdBindService {
    @Autowired private MerchantThirdBindDao thirdBindDao;

    @Transactional(readOnly = false)
    public void bindMt(String userId,String mtAuthToken){
        thirdBindDao.updateMtBindInfo(userId,true,mtAuthToken);
    }
    @Transactional(readOnly = false)
    public void unbindMt(String userId){
        thirdBindDao.updateMtBindInfo(userId,false,"");
    }
}
