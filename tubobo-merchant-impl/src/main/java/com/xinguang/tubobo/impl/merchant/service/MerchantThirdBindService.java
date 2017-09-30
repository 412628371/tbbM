package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantThirdBindEntity;
import com.xinguang.tubobo.impl.merchant.repository.MerchantThirdBindRepository;
import com.xinguang.tubobo.takeout.TakeoutNotifyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by xuqinghua on 2017/6/29.
 */
@Service
@Transactional(readOnly = true)
public class MerchantThirdBindService {
    @Autowired private MerchantThirdBindRepository thirdBindDao;

    @Transactional(readOnly = false)
    public void bindMt(TakeoutNotifyConstant.PlatformCode platformCode,String userId, String mtAuthToken){
        MerchantThirdBindEntity entity = thirdBindDao.findByUserIdAndDelFlag(userId, BaseMerchantEntity.DEL_FLAG_NORMAL);
        if(null==entity){
            entity=new MerchantThirdBindEntity();
            entity.setUserId(userId);
            entity.setCreateDate(new Date());
        }
        switch (platformCode){

            case MT:
                entity.setMtBound(true);
                entity.setMtAuthToken(mtAuthToken);
                entity.setUpdateDate(new Date());
                thirdBindDao.save(entity);
                break;
            case ELE:
                entity.setEleBound(true);
                entity.setUpdateDate(new Date());
                thirdBindDao.save(entity);
                break;
            case YZ:
                entity.setYzBound(true);
                entity.setUpdateDate(new Date());
                thirdBindDao.save(entity);
                break;
            default:
                break;
        }

    }
    @Transactional(readOnly = false)
    public void unbindMt(TakeoutNotifyConstant.PlatformCode platformCode,String userId){
        MerchantThirdBindEntity entity = thirdBindDao.findByUserIdAndDelFlag(userId, BaseMerchantEntity.DEL_FLAG_NORMAL);
        if(null==entity){
            entity=new MerchantThirdBindEntity();
            entity.setUserId(userId);
            entity.setCreateDate(new Date());
        }
        switch (platformCode){
            case MT:
                entity.setMtBound(false);
                entity.setMtAuthToken("");
                entity.setUpdateDate(new Date());
                thirdBindDao.save(entity);
                break;
            case ELE:
                entity.setEleBound(false);
                entity.setUpdateDate(new Date());
                thirdBindDao.save(entity);
                break;
            case YZ:
                entity.setYzBound(false);
                entity.setUpdateDate(new Date());
                thirdBindDao.save(entity);
                break;
            default:
                break;
        }

    }
    @Transactional(readOnly = true)
    public MerchantThirdBindEntity getByUserId(String userId){
        return thirdBindDao.findByUserIdAndDelFlag(userId, BaseMerchantEntity.DEL_FLAG_NORMAL);
    }
}
