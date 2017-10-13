package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.entity.PushNoticeEntity;
import com.xinguang.tubobo.impl.merchant.repository.PushNoticeRepository;
import com.xinguang.tubobo.merchant.api.dto.NoticeDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderNoticeType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqinghua on 2017/7/14.
 */
@Service
public class NoticeService {
    @Autowired
    PushNoticeRepository pushNoticeDao;
    @Autowired MerchantPushService merchantPushService;

    public void pushMoney(NoticeDTO dto) {
        PushNoticeEntity entity = new PushNoticeEntity();
        BeanUtils.copyProperties(dto,entity);
        entity.setProcessed(false);
        entity.setDelFlag(BaseMerchantEntity.DEL_FLAG_NORMAL);
        pushNoticeDao.save(entity);
        merchantPushService.pushMoneyNotice(dto);
    }

    public void pushAudit(NoticeDTO dto){
        PushNoticeEntity entity = new PushNoticeEntity();
        BeanUtils.copyProperties(dto,entity);
        entity.setProcessed(false);
        entity.setDelFlag(BaseMerchantEntity.DEL_FLAG_NORMAL);
        pushNoticeDao.save(entity);
        merchantPushService.pushAuditNotice(entity);
    }
    public void pushOrder(NoticeDTO dto){
        PushNoticeEntity entity = new PushNoticeEntity();
        BeanUtils.copyProperties(dto,entity);
        if (EnumOrderNoticeType.ADMIN_CANCEL.getValue().equals(dto.getOrderOperateType())){
            entity.setContent(String.format(dto.getContent(),dto.getOrderNo()));
            entity.setProcessed(false);
            entity.setDelFlag(BaseMerchantEntity.DEL_FLAG_NORMAL);
            pushNoticeDao.save(entity);
        }
        merchantPushService.pushOrderNotice(entity);
    }
    public void pushSystem(NoticeDTO dto){
        merchantPushService.pushSystemNotice(dto);
    }

    public void pushBind(NoticeDTO dto){
        //PushNoticeEntity entity = new PushNoticeEntity();
        //BeanUtils.copyProperties(dto, entity);
        //pushNoticeDao.saveEntity(entity);
        merchantPushService.pushBindNotice(dto);
    }

    public void pushUnbind(NoticeDTO dto){
        PushNoticeEntity entity = new PushNoticeEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setProcessed(false);
        entity.setDelFlag(BaseMerchantEntity.DEL_FLAG_NORMAL);
        pushNoticeDao.save(entity);
        merchantPushService.pushUnbindNotice(dto);
    }

    /**
     * 查找全部通知，包括已读和未读
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<PushNoticeEntity> findNoticePage(String userId, int pageNo, int pageSize){
        PageRequest pageRequest = new PageRequest(pageNo-1, pageSize,  new Sort(Sort.Direction.DESC, "createDate"));
        Page<PushNoticeEntity> page = pushNoticeDao.findAll(where(userId,0), pageRequest);
        return page;


       // return pushNoticeDao.findNoticePage(userId,0,pageNo,pageSize);

    }


    private Specification<PushNoticeEntity> where(final String userId,final int processStatus){
        return new Specification<PushNoticeEntity>() {
            @Override
            public Predicate toPredicate(Root<PushNoticeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("delFlag").as(String.class), BaseMerchantEntity.DEL_FLAG_NORMAL));
                if (org.apache.commons.lang.StringUtils.isNotBlank(userId)) {
                    list.add(cb.and(cb.equal(root.get("userId").as(String.class), userId)));
                }
                boolean processed = false;
                if (processStatus > 0) {
                    processed = false;
                    if (processStatus == 1) {
                        processed = true;
                    }
                    list.add(cb.and(cb.equal(root.get("processed").as(Boolean.class), processed)));
                }

                return criteriaQuery.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        };
        }








    public void processMsgsRead(String userId, List<Long> ids){
        if (null == ids || ids.size() ==0)
            return;
        for (Long id :ids){
            if (id != null && StringUtils.isNotBlank(userId)){
                pushNoticeDao.processNotice(true,id,userId,BaseMerchantEntity.DEL_FLAG_NORMAL);
            }
        }
    }
    public void deleteMsgs(String userId, List<Long> ids){
        if (null == ids || ids.size() ==0)
            return;
        for (Long id :ids){
            pushNoticeDao.deleteNotice(BaseMerchantEntity.DEL_FLAG_DELETE,id,userId,BaseMerchantEntity.DEL_FLAG_NORMAL);
        }
    }

    public Long getUnProcessedCount(String userId){
        return pushNoticeDao.getUnProcessedCount(false,userId,BaseMerchantEntity.DEL_FLAG_NORMAL);
    }
}
