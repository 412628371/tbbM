package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.dao.MerchantThirdBindDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantThirdBindEntity;
import com.xinguang.tubobo.takeout.TakeoutNotifyConstant;
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
    public void bindMt(TakeoutNotifyConstant.PlatformCode platformCode,String userId, String mtAuthToken){
        switch (platformCode){
            case MT:
                thirdBindDao.updateMtBindInfo(userId,true,mtAuthToken);
                break;
            case ELE:
                thirdBindDao.updateEleBindInfo(userId,true,mtAuthToken);
                break;
            default:
                break;
        }

    }
    @Transactional(readOnly = false)
    public void unbindMt(TakeoutNotifyConstant.PlatformCode platformCode,String userId){
        switch (platformCode){
            case MT:
                thirdBindDao.updateMtBindInfo(userId,false,"");
                break;
            case ELE:
                thirdBindDao.updateEleBindInfo(userId,true,"");
                break;
            default:
                break;
        }

    }
    @Transactional(readOnly = true)
    public MerchantThirdBindEntity getByUserId(String userId){
        return thirdBindDao.findByUserId(userId);
    }
}
