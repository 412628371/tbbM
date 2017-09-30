package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.condition.ProcessQueryCondition;
import com.xinguang.tubobo.impl.merchant.repository.ThirdOrderRepository;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.entity.ThirdOrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuqinghua on 2017/7/1.
 */
@Service
@Transactional(readOnly = true)
public class ThirdOrderService {
    Logger logger = LoggerFactory.getLogger(ThirdOrderService.class);

    @Autowired private ThirdOrderRepository thirdOrderRepository;
    @Transactional()
    public void saveMtOrder(ThirdOrderEntity mtOrderEntity){
        ThirdOrderEntity existEntity = thirdOrderRepository.findByOriginOrderIdAndPlatformCode(mtOrderEntity.getOriginOrderId(),
                mtOrderEntity.getPlatformCode());
        if (null == existEntity){
            mtOrderEntity.setCreateDate(new Date());
            mtOrderEntity.setUpdateDate(new Date());
            mtOrderEntity.setDelFlag(BaseMerchantEntity.DEL_FLAG_NORMAL);
            thirdOrderRepository.save(mtOrderEntity);
        }
    }
    @Transactional()
    public void processOrder(String userId,String platformCode,String originOrderId){
        thirdOrderRepository.processOrder(true,new Date(),userId,platformCode,originOrderId);
    }

    public Page<ThirdOrderEntity> findUnProcessedPageByUserId(ProcessQueryCondition condition, @Min(1) int pageNo,@Min(1) int pageSize){

        PageRequest pageRequest = new PageRequest((pageNo-1), pageSize, new Sort(Sort.Direction.DESC, "create_date"));
        Page<ThirdOrderEntity> page = thirdOrderRepository.findAll(where(condition), pageRequest);
        return page;
    }

    private Specification<ThirdOrderEntity> where(final ProcessQueryCondition condition) {
        return new Specification<ThirdOrderEntity>() {
            @Override
            public Predicate toPredicate(Root<ThirdOrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(cb.equal(root.get("delFlag").as(String.class), "0"));
                list.add(cb.equal(root.get("processed").as(Boolean.class), false));
                if (StringUtils.isNotBlank(condition.getUserId())){
                    list.add(cb.equal(root.get("userId").as(String.class), condition.getUserId()));
                }
                if (StringUtils.isNotBlank(condition.getPlatformCode())){
                    list.add(cb.equal(root.get("platformCode").as(String.class), condition.getPlatformCode()));
                }
                if ("PHONE".equals(condition.getQueryType())){
                    if (StringUtils.isNotBlank(condition.getKeyword())){
                        list.add(cb.like(root.get("keyword").as(String.class), "%"+condition.getKeyword()));

                    }
                }else {
                    if (StringUtils.isNotBlank(condition.getKeyword())){
                        list.add(cb.like(root.get("keyword").as(String.class), "%"+condition.getKeyword()+"%"));
                    }
                }
                return query.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        };
    }

    /**
     * 逻辑删除第三方平台记录
     */
    @Transactional()
    public void delRecordsPastHours(int pastHours){
        Date lastValidDate = DateUtils.getHourAfterOfDate(new Date(),-pastHours);

        int count = thirdOrderRepository.delRecordsPastHours(lastValidDate);
        logger.info("删除 {} 条,{} 小时之前的第三方平台订单记录",count,pastHours);
    }
}
