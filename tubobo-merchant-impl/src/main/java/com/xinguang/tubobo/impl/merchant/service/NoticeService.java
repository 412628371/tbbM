package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.dao.PushNoticeDao;
import com.xinguang.tubobo.impl.merchant.entity.PushNoticeEntity;
import com.xinguang.tubobo.merchant.api.dto.NoticeDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumNoticeType;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderNoticeType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuqinghua on 2017/7/14.
 */
@Service
public class NoticeService {
    @Autowired
    PushNoticeDao pushNoticeDao;
    @Autowired MerchantPushService merchantPushService;

    public void pushMoney(NoticeDTO dto) {
        PushNoticeEntity entity = new PushNoticeEntity();
        BeanUtils.copyProperties(dto,entity);
        pushNoticeDao.saveEntity(entity);
        merchantPushService.pushMoneyNotice(dto);
    }

    public void pushAudit(NoticeDTO dto){
        PushNoticeEntity entity = new PushNoticeEntity();
        BeanUtils.copyProperties(dto,entity);
        pushNoticeDao.saveEntity(entity);
        merchantPushService.pushAuditNotice(entity);
    }
    public void pushOrder(NoticeDTO dto){
        PushNoticeEntity entity = new PushNoticeEntity();
        BeanUtils.copyProperties(dto,entity);
        if (EnumOrderNoticeType.ADMIN_CANCEL.getValue().equals(dto.getOrderOperateType())){
            entity.setContent(String.format(dto.getContent(),dto.getOrderNo()));
            pushNoticeDao.saveEntity(entity);
        }
        merchantPushService.pushOrderNotice(entity);
    }
    public void pushSystem(NoticeDTO dto){
        merchantPushService.pushSystemNotice(dto);
    }

    /**
     * 查找全部通知，包括已读和未读
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<PushNoticeEntity> findNoticePage(String userId, int pageNo, int pageSize){
        return pushNoticeDao.findNoticePage(userId,0,pageNo,pageSize);
    }
    public void processMsgsRead(String userId, List<Long> ids){
        if (null == ids || ids.size() ==0)
            return;
        for (Long id :ids){
            if (id != null && StringUtils.isNotBlank(userId)){
                pushNoticeDao.processNotice(userId,id);
            }
        }
    }
    public void deleteMsgs(String userId, List<Long> ids){
        if (null == ids || ids.size() ==0)
            return;
        for (Long id :ids){
            pushNoticeDao.deleteNotice(userId,id);
        }
    }

    public Long getUnProcessedCount(String userId){
        return pushNoticeDao.getUnProcessedCount(userId);
    }
}
