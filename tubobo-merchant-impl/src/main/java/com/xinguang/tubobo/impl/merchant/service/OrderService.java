package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.CalCulateUtil;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.xinguang.taskcenter.api.OverTimeRuleInterface;
import com.xinguang.tubobo.impl.merchant.amap.RoutePlanning;
import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.common.CodeGenerator;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.dao.MerchantOrderDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageSettingsEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(OrderService.class);


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
    @Autowired
    MerchantMessageSettingsService merchantMessageSettingsService;

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

/*    public int abortConfirm(String orderNo,Boolean confirm,String message,String userId){
        return merchantOrderDao.abortConfirm(orderNo,confirm,message,userId);
    }*/
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
        //设置该单是否付短信通知收货人
        MerchantMessageSettingsEntity setting = merchantMessageSettingsService.findBuUserId(userId);
        entity.setShortMessage(setting.getMessageOpen());
        entity.setOrderNo(orderNo);
        Double textMesssgaeFee=setting.getMessageOpen()? MerchantConstants.MESSAGE_FEE:0.0;

        if (entity.getDeliveryFee() != null) {
            if (entity.getTipFee() == null) {
                entity.setPayAmount(CalCulateUtil.add(entity.getDeliveryFee(),textMesssgaeFee));
            } else {
                entity.setPayAmount(CalCulateUtil.add(CalCulateUtil.add(entity.getDeliveryFee(),entity.getTipFee()),textMesssgaeFee) );
            }
        }
        if (entity.getPeekOverFee() != null) {
            entity.setPayAmount(CalCulateUtil.add(entity.getPayAmount(),entity.getPeekOverFee()) );
        }
        if (entity.getWeatherOverFee() != null) {
            entity.setPayAmount(CalCulateUtil.add(entity.getPayAmount(),entity.getWeatherOverFee()) );
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
    public boolean merchantCancel(String merchantId, String orderNo, String cancelReason,String waitPickCancelType,Double punishFee,Double subsidyFee) {
        return merchantOrderDao.merchantCancel(merchantId, orderNo, cancelReason,waitPickCancelType,punishFee,subsidyFee);
    }

    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public boolean adminCancel(String merchantId, String orderNo, String cancelReason) {
        return merchantOrderDao.adminCancel(orderNo, cancelReason);
    }
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public boolean riderCancel(String orderNo, String cancelReason, Date now, Double subsidy,String merchantId) {
        return merchantOrderDao.riderCancel(orderNo, cancelReason,now,subsidy);
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
                              Date grabOrderTime, Date expectFinishTime, String riderCarNo, String riderCarType, Double pickupDistance) {
        //v1.41预计送达时间改为从规则表中获得
        //Date expectFinishTimeDueRule = getExpectFinishTimeDueRule(orderNo,grabOrderTime);
        return merchantOrderDao.riderGrabOrder(riderId, riderName, riderPhone, orderNo, grabOrderTime, expectFinishTime, riderCarNo, riderCarType,pickupDistance);
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
    public int riderFinishOrder(String merchantId, String orderNo, Date finishOrderTime, Double expiredMinute, Double expiredCompensation) {

        //double orderOverTime = getOrderOverTime(finishOrderTime, orderNo);
        if (0.0 == expiredMinute) {
            //订单未超时
            logger.info("保存未超时订单：orderNo:{},expiredMinute:{}",orderNo,expiredMinute);
            return merchantOrderDao.riderFinishOrder(orderNo, finishOrderTime);

        } else {
            //订单超时
            logger.info("保存超时订单：orderNo:{},expiredMinute:{},,赔付金额:{}",orderNo,expiredMinute,expiredCompensation);
            return merchantOrderDao.riderFinishExpiredOrder(orderNo, finishOrderTime, expiredMinute, expiredCompensation);
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
     * 重新发单
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int orderResend(String merchantId, String originOrderNo){
        int count = merchantOrderDao.orderResend(originOrderNo);
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
    /**
     * 计算当天已完成的订单(带有短信通知)
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public Long getTodayFinishOrderWithShortTextNum(String userId) {
        return merchantOrderDao.getTodayFinishOrderWithShortTextNum(userId);
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
    public double getOrderOverTime(Date compareDate, String orderNo)  {
        double returnOverMinute = 0.0;
        MerchantOrderEntity order = findByOrderNo(orderNo);
        Date expectFinishTime = order.getExpectFinishTime();
        returnOverMinute = DateUtils.getMinuteBetweenTwoDate(compareDate, expectFinishTime);
        logger.info("订单超时:{}订单编号,{}超时时间", orderNo, returnOverMinute);
        return returnOverMinute;

    }

    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int riderUnsettledOrder(String merchantId, String orderNo,String reason) {
        return merchantOrderDao.riderUnsettledOrder(orderNo,reason);
    }

    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int merchantHandlerUnsettledOrder(String merchantId, String orderNo,Date finishOrderTime) {
        return merchantOrderDao.merchantHandlerUnsettledOrder(orderNo,finishOrderTime);
    }
}
