package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.CalCulateUtil;
import com.xinguang.taskcenter.api.OverTimeRuleInterface;
import com.xinguang.taskcenter.api.dto.OverTimeRuleDto;
import com.xinguang.tubobo.impl.merchant.amap.RoutePlanning;
import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.common.CodeGenerator;
import com.xinguang.tubobo.impl.merchant.dao.MerchantOrderDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */
@Service
@Transactional(readOnly = true)
public class OrderService extends BaseService {
    @Autowired
    private MerchantOrderDao merchantOrderDao;
    @Autowired
    private CodeGenerator codeGenerator;
    @Autowired
    private DeliveryFeeService deliveryFeeService;
    @Autowired
    RoutePlanning routePlanning;
    @Autowired
    OverTimeRuleInterface overTimeRuleService;

//    public MerchantOrderEntity get(String id) {
//        return merchantOrderDao.get(id);
//    }

    //    @Cacheable(value= RedisCache.MERCHANT,key="'merchantOrder_'+#orderNo+'_'+#orderStatus")
    public MerchantOrderEntity findByOrderNo(String orderNo) {
        return merchantOrderDao.findByOrderNo(orderNo);
    }

    //    @Cacheable(value= RedisCache.MERCHANT,key="'merchantOrder_'+#orderNo+'_'+#orderStatus")
    public MerchantOrderEntity findByOrderNoAndStatus(String orderNo, String orderStatus) {
        return merchantOrderDao.findByOrderNoAndStatus(orderNo, orderStatus);
    }

    @Cacheable(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_'+#orderNo")
    public MerchantOrderEntity findByMerchantIdAndOrderNo(String merchantId, String orderNo) {
        return merchantOrderDao.findByMerchantIdAndOrderNo(merchantId, orderNo);
    }

    /**
     * 商家提交订单
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#userId+'_*'")
    @Transactional(readOnly = false)
    public String order(String userId, MerchantOrderEntity entity) throws MerchantClientException {
        double distance;
        //保存配送距离 从1.41版本保存为我们之前传给客户端的距离值,同时向下兼容
        Double deliveryDistance = entity.getDeliveryDistance();
        //1.41版本之前所展示的实时值
        if (EnumOrderType.BIGORDER.getValue().equals(entity.getOrderType())) {
            distance = deliveryFeeService.sumDeliveryDistanceChePei(entity.getSenderLongitude(), entity.getSenderLatitude(),
                    entity.getReceiverLongitude(), entity.getReceiverLatitude());
        } else {
            distance = deliveryFeeService.sumDeliveryDistanceMerchant(userId, entity.getReceiverLatitude(), entity.getReceiverLongitude());
        }
        //此处向下兼容
        distance = deliveryDistance == null ? distance : deliveryDistance;
        entity.setDeliveryDistance(distance);
        String orderNo = codeGenerator.nextCustomerCode(entity.getOrderType());
        entity.setOrderNo(orderNo);

        if (entity.getDeliveryFee() != null) {
            if (entity.getTipFee() == null) {
                entity.setPayAmount(entity.getDeliveryFee());
            } else {
                entity.setPayAmount(entity.getDeliveryFee() + entity.getTipFee());
            }
        }
        if (entity.getPeekOverFee() != null) {
            entity.setPayAmount(entity.getPayAmount() + entity.getPeekOverFee());
        }
        if (entity.getWeatherOverFee() != null) {
            entity.setPayAmount(entity.getPayAmount() + entity.getWeatherOverFee());
        }
        entity.setOrderStatus(EnumMerchantOrderStatus.INIT.getValue());
        merchantOrderDao.save(entity);
        //将订单加入支付超时队列
        return orderNo;
    }

    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int merchantPay(String merchantId, String orderNo, long payId, Date payDate) {
        int count = merchantOrderDao.merchantPay(merchantId, orderNo, payId, payDate);
        return count;
    }

    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public boolean merchantCancel(String merchantId, String orderNo, String cancelReason) {
        return merchantOrderDao.merchantCancel(merchantId, orderNo, cancelReason);
    }

    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public boolean adminCancel(String merchantId, String orderNo, String cancelReason) {
        return merchantOrderDao.adminCancel(orderNo, cancelReason);
    }

    /**
     * 商家删除订单
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int deleteOrder(String merchantId, String orderNo) {
        int count = merchantOrderDao.deleteOrder(merchantId, orderNo);
        return count;
    }

    /**
     * 骑手抢单
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int riderGrabOrder(String merchantId, String riderId, String riderName, String riderPhone, String orderNo,
                              Date grabOrderTime, Date expectFinishTime, String riderCarNo, String riderCarType) {
        //v1.41预计送达时间改为从规则表中获得
        //Date expectFinishTimeDueRule = getExpectFinishTimeDueRule(orderNo,grabOrderTime);
        return merchantOrderDao.riderGrabOrder(riderId, riderName, riderPhone, orderNo, grabOrderTime, expectFinishTime, riderCarNo, riderCarType);
    }

    /**
     * 骑手取货
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int riderGrabItem(String merchantId, String orderNo, Date grabItemTime) {
        return merchantOrderDao.riderGrabItem(orderNo, grabItemTime);
    }

    /**
     * 骑手完成订单
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int riderFinishOrder(String merchantId, String orderNo, Date finishOrderTime) {

        double orderOverTime = getOrderOverTime(finishOrderTime, orderNo);
        if (0.0 == orderOverTime) {
            //订单未超时
            return merchantOrderDao.riderFinishOrder(orderNo, finishOrderTime);
        } else {
            //订单超时
            return merchantOrderDao.riderFinishExpiredOrder(orderNo, finishOrderTime, orderOverTime);
        }
    }

    /**
     * 支付超时
     */
//    @CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public void payExpired(String merchantId, String orderNo) {
        merchantOrderDao.payExpire(orderNo);
    }

    /**
     * 订单超时
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int orderExpire(String merchantId, String orderNo, Date expireTime) {
        int count = merchantOrderDao.orderExpire(orderNo, expireTime);
        return count;
    }

    /**
     * 商家查询订单分页（缓存）
     */
    @Cacheable(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#entity.getUserId()+'_'+#entity.getOrderType()+'_'+#entity.getOrderStatus()+'_'+#pageNo+'_'+#pageSize")
    public Page<MerchantOrderEntity> merchantQueryOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity) {
        return merchantOrderDao.findMerchantOrderPageToApp(pageNo, pageSize, entity);
    }

    /**
     * 后台查询分页（不缓存）
     */
    public Page<MerchantOrderEntity> adminQueryOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity) {
        return merchantOrderDao.findMerchantOrderPageToAdmin(pageNo, pageSize, entity);
    }

    /**
     * 获取
     *
     * @param overTimeMilSeconds
     * @return
     */
    @Transactional(readOnly = true)
    public List<String> getUnCanceledGrabOvertimeOrderNoList(Integer overTimeMilSeconds) {
        return merchantOrderDao.getUnCanceledGrabOvertimeOrderNoList(overTimeMilSeconds);
    }

    /**
     * 计算当天已完成的订单
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public Long getTodayFinishOrderNum(String userId) {
        return merchantOrderDao.getTodayFinishOrderNum(userId);
    }

    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public boolean rateOrder(String merchantId, String orderNo) {
        int count = merchantOrderDao.rateOrder(orderNo);
        logger.info("更新订单：{}的状态为已评价，result:{}", orderNo, count);
        return count == 1;
    }

    @Transactional(readOnly = true)
    public List<String> getUnRatedOrderNos(int days) {
        return merchantOrderDao.getUnRatedOrderNos(days);
    }

    /**
     * 清除redis缓存，防止有的没有清除掉，造成订单状态展示不及时
     *
     * @param userId
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#userId+'_*'")
    public void clearRedisCache(String userId) {

    }

    /**
     * 获得骑手完成送达时的超时间分钟 0为未超时
     *
     * @param compareDate 传入时间, DeliverDistance 传入
     * @param orderNo     订单编号(用于获取配送距离)
     */
    public double getOrderOverTime(Date compareDate, String orderNo) {
        List<OverTimeRuleDto> list = overTimeRuleService.findAllOverTimeRule();
        double returnOverMinute = 0.0;
        if (list != null && list.size() > 0) {
            double initDistance = 10000;
            double initMinute = 10000;
            double raiseMinute = 10000;
            for (OverTimeRuleDto overTimeRuleDto : list) {
                initDistance = Double.parseDouble(overTimeRuleDto.getInitDistance());
                initMinute = Double.parseDouble(overTimeRuleDto.getInitMinute());
                raiseMinute = Double.parseDouble(overTimeRuleDto.getRaiseMinute());
            }
            MerchantOrderEntity order = findByOrderNo(orderNo);
            double deliveryDistance = order.getDeliveryDistance() / 1000;
            Date payTime = order.getGrabOrderTime();
            long timeSub = compareDate.getTime() - payTime.getTime();
            double spendMinute = CalCulateUtil.div(timeSub, 60000, 2);
            //获得送达花费时间
            spendMinute = Math.ceil(spendMinute);
            //计算规则内允许送达最长时间分钟
            double maxAllowMinute;
            if (deliveryDistance <= initDistance) {
                maxAllowMinute = initMinute;
            } else {
                double subDistance = Math.ceil(deliveryDistance - initDistance);
                double etraMinute = CalCulateUtil.mul(subDistance, raiseMinute);
                maxAllowMinute = initMinute + etraMinute;
            }
            if (maxAllowMinute < spendMinute) {
                returnOverMinute = CalCulateUtil.sub(spendMinute, maxAllowMinute);
            }
        }
        logger.info("订单超时:{}订单编号,{}超时时间", orderNo, returnOverMinute);
        return returnOverMinute;

    }


}
