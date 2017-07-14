package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.dao.PushNoticeDao;
import com.xinguang.tubobo.impl.merchant.entity.PushNoticeEntity;
import com.xinguang.tubobo.merchant.api.dto.NoticeDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuqinghua on 2017/7/14.
 */
@Service
public class NoticeService {
    @Autowired
    PushNoticeDao pushNoticeDao;
    @Autowired MerchantPushService merchantPushService;

    public void pushAudit(NoticeDTO dto){
        PushNoticeEntity entity = new PushNoticeEntity();
        BeanUtils.copyProperties(dto,entity);
        pushNoticeDao.saveEntity(entity);
        //TODO 阿里推送
    }
}
