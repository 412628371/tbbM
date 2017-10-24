package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.utils.CalCulateUtil;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.xinguang.taskcenter.api.OverTimeRuleInterface;
import com.xinguang.taskcenter.api.common.enums.PostOrderUnsettledStatusEnum;
import com.xinguang.tubobo.impl.merchant.amap.RoutePlanning;
import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.common.CodeGenerator;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.*;
import com.xinguang.tubobo.impl.merchant.repository.MerchantOrderDetailRepository;
import com.xinguang.tubobo.impl.merchant.repository.MerchantOrderRepository;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.MerchantTypeDTO;
import com.xinguang.tubobo.merchant.api.enums.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
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
    private MerchantOrderRepository merchantOrderDao;
    @Autowired
    private MerchantOrderDetailRepository merchantOrderDetailRepository;
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
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private MerchantTypeService merchantTypeService;



//    public MerchantOrderEntity get(String id) {
//        return merchantOrderDao.get(id);
//    }

    //    @Cacheable(value= RedisCache.MERCHANT,key="'merchantOrder_'+#orderNo+'_'+#orderStatus")
    public MerchantOrderEntity findByOrderNo(String orderNo) {

        OrderEntity orderEntity = merchantOrderDao.findByOrderNoAndDelFlag(orderNo, BaseMerchantEntity.DEL_FLAG_NORMAL);
        OrderDetailEntity orderDetail = merchantOrderDetailRepository.findByOrderNoAndDelFlag(orderNo, BaseMerchantEntity.DEL_FLAG_NORMAL);
        MerchantOrderEntity merchantOrderEntity = new MerchantOrderEntity();
        BeanUtils.copyProperties(orderEntity,merchantOrderEntity);
        BeanUtils.copyProperties(orderDetail,merchantOrderEntity);

        return merchantOrderEntity;
    }

    //    @Cacheable(value= RedisCache.MERCHANT,key="'merchantOrder_'+#orderNo+'_'+#orderStatus")
    public MerchantOrderEntity findByOrderNoAndStatus(String orderNo, String orderStatus) {
        OrderEntity orderEntity = merchantOrderDao.findByOrderNoAndOrderStatusAndDelFlag(orderNo,orderStatus, BaseMerchantEntity.DEL_FLAG_NORMAL);
        OrderDetailEntity orderDetail = merchantOrderDetailRepository.findByOrderNoAndDelFlag(orderNo, BaseMerchantEntity.DEL_FLAG_NORMAL);
        MerchantOrderEntity merchantOrderEntity = new MerchantOrderEntity();
        BeanUtils.copyProperties(orderEntity,merchantOrderEntity);
        BeanUtils.copyProperties(orderDetail,merchantOrderEntity);
        return merchantOrderEntity;
    }

    @Cacheable(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_'+#orderNo")
    public MerchantOrderEntity findByMerchantIdAndOrderNo(String merchantId, String orderNo) {
        OrderEntity orderEntity = merchantOrderDao.findByOrderNoAndUserIdAndDelFlag(orderNo,merchantId, BaseMerchantEntity.DEL_FLAG_NORMAL);
        OrderDetailEntity orderDetail = merchantOrderDetailRepository.findByOrderNoAndDelFlag(orderNo, BaseMerchantEntity.DEL_FLAG_NORMAL);
        MerchantOrderEntity merchantOrderEntity = new MerchantOrderEntity();
        BeanUtils.copyProperties(orderEntity,merchantOrderEntity);
        BeanUtils.copyProperties(orderDetail,merchantOrderEntity);
        return merchantOrderEntity;
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
        OrderEntity orderEntity;
        OrderDetailEntity detailEntity;
        double distance= entity.getDeliveryDistance();;
        MerchantTypeDTO merchantTypeDTO = null;
        MerchantInfoEntity merchantInfoEntity;
        double totalFee;
        double commissionRateDl = 0.0;
        double platformFee;
        double riderFee;
        Integer commissionRate=0;

       /* //保存配送距离 从1.41版本保存为我们之前传给客户端的距离值,同时向下兼容
        Double deliveryDistance = entity.getDeliveryDistance();
        //1.41版本之前所展示的实时值
        if (EnumOrderType.BIGORDER.getValue().equals(entity.getOrderType())) {
            distance = deliveryFeeService.sumDeliveryDistanceChePei(entity.getSenderLongitude(), entity.getSenderLatitude(),
                    entity.getReceiverLongitude(), entity.getReceiverLatitude());
        } else {
            distance = deliveryFeeService.sumDeliveryDistanceMerchant(userId, entity.getReceiverLatitude(), entity.getReceiverLongitude());
        }
        //此处向下兼容
        distance = deliveryDistance == null ? distance : deliveryDistance;*/
        entity.setDeliveryDistance(distance);
        String orderNo = codeGenerator.nextCustomerCode(entity.getOrderType());
        //设置该单是否付短信通知收货人
        MerchantMessageSettingsEntity setting = merchantMessageSettingsService.findByUserIdAndCreate(userId);
        if (setting == null){
            setting = new MerchantMessageSettingsEntity();
            setting.setMessageOpen(false);
        }
        entity.setShortMessage(setting.getMessageOpen());
        entity.setOrderNo(orderNo);
        Double textMesssgaeFee=setting.getMessageOpen()? MerchantConstants.MESSAGE_FEE:0.0;

        if (entity.getDeliveryFee() != null) {
            if (entity.getTipFee() == null) {
                entity.setPayAmount(entity.getDeliveryFee());
            } else {
                entity.setPayAmount(CalCulateUtil.add(entity.getDeliveryFee(),entity.getTipFee()));
            }
        }
        if (entity.getPeekOverFee() != null) {
            entity.setPayAmount(CalCulateUtil.add(entity.getPayAmount(),entity.getPeekOverFee()) );
        }
        if (entity.getWeatherOverFee() != null) {
            entity.setPayAmount(CalCulateUtil.add(entity.getPayAmount(),entity.getWeatherOverFee()) );
        }
        // 没加短信费前 计算佣金
        merchantInfoEntity = merchantInfoService.findByUserId(userId);
        if (merchantInfoEntity==null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        totalFee = entity.getPayAmount();
        Long merTypeId =  merchantInfoEntity.getMerTypeId();
        if (merTypeId!=null){
            merchantTypeDTO = merchantTypeService.findById(merTypeId);
        }
        if (merchantTypeDTO==null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_TYPEERROR);
        }
        commissionRate = merchantTypeDTO.getCommissionRate();
        commissionRate=commissionRate==null?0:commissionRate;
        if (commissionRate!=0){
            commissionRateDl = CalCulateUtil.div(commissionRate,100,2);
        }
        platformFee  = CalCulateUtil.mul(totalFee,commissionRateDl);
        platformFee = CalCulateUtil.round(platformFee,2);
        riderFee = CalCulateUtil.sub(totalFee,platformFee);
        riderFee =  CalCulateUtil.round(riderFee,2);

        entity.setRiderFee(riderFee);
        entity.setPlatformFee(platformFee);

        //加上短信费
        entity.setPayAmount(CalCulateUtil.add(entity.getPayAmount(),textMesssgaeFee));

        entity.setOrderStatus(EnumMerchantOrderStatus.INIT.getValue());
        orderEntity=new OrderEntity();
        detailEntity=new OrderDetailEntity();

        BeanUtils.copyProperties(entity,orderEntity);
        BeanUtils.copyProperties(entity,detailEntity);
        merchantOrderDao.save(orderEntity);
        merchantOrderDetailRepository.save(detailEntity);
        //将订单加入支付超时队列
        return orderNo;
    }


    /**
     * 直接保存order,orderDetail
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#userId+'_*'")
    @Transactional(readOnly = false)
    public String saveOrderOnly(String userId, OrderEntity orderEntity,OrderDetailEntity detailEntity) throws MerchantClientException {
        String orderNo = codeGenerator.nextCustomerCode(orderEntity.getOrderType());
        Integer commissionRate;
        //重新设置
        // 没加短信费前 计算佣金
        MerchantInfoEntity merchantInfoEntity = merchantInfoService.findByUserId(userId);
        if (merchantInfoEntity==null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        Double totalFee = orderEntity.getPayAmount();
        if (orderEntity.getShortMessage()){
            totalFee=CalCulateUtil.sub(totalFee,MerchantConstants.MESSAGE_FEE);
        }
        Long merTypeId =  merchantInfoEntity.getMerTypeId();
        MerchantTypeDTO   merchantTypeDTO=null;
        double commissionRateDl = 0.0;
        if (merTypeId!=null){
            merchantTypeDTO = merchantTypeService.findById(merTypeId);
        }
        if (merchantTypeDTO==null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_TYPEERROR);
        }
        commissionRate = merchantTypeDTO.getCommissionRate();
        commissionRate=commissionRate==null?0:commissionRate;
        if (commissionRate!=0){
            commissionRateDl = CalCulateUtil.div(commissionRate,100,2);
        }

        double platformFee  = CalCulateUtil.mul(totalFee,commissionRateDl);
        platformFee = CalCulateUtil.round(platformFee,2);
        double riderFee = CalCulateUtil.sub(totalFee,platformFee);
        riderFee =  CalCulateUtil.round(riderFee,2);

        detailEntity.setRiderFee(riderFee);
        detailEntity.setPlatformFee(platformFee);


        orderEntity.setOrderNo(orderNo);
        detailEntity.setOrderNo(orderNo);
        merchantOrderDao.save(orderEntity);
        merchantOrderDetailRepository.save(detailEntity);
        return orderNo;
    }



    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int merchantPay(String merchantId, String orderNo, long payId, Date payDate,String orderStatus) {
        int count = merchantOrderDao.merchantPay(orderStatus, EnumPayStatus.PAID.getValue(),payDate , payId,merchantId,
                orderNo,EnumMerchantOrderStatus.INIT.getValue(),BaseMerchantEntity.DEL_FLAG_NORMAL);
        logger.info("支付操作数据库：count:{},merchantId:{},orderNo:{},payId:{},payDate:{}",count,merchantId,orderNo,payId,payDate);
        return count;
    }

    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public boolean merchantCancel(String merchantId, String orderNo, String cancelReason,String waitPickCancelType,Double punishFee,Double subsidyFee) {
        List<String> orderStatusArr=new ArrayList<>();
        if (StringUtils.isBlank(waitPickCancelType)){
            orderStatusArr.add(EnumMerchantOrderStatus.INIT.getValue());
            orderStatusArr.add(EnumMerchantOrderStatus.WAITING_GRAB.getValue());
        }else{
            //不为空说明 处于带取货状态
            orderStatusArr.add(EnumMerchantOrderStatus.WAITING_PICK.getValue());
        }
        merchantOrderDao.orderCancel(orderNo, new Date(),EnumMerchantOrderStatus.CANCEL.getValue(),new Date(),orderStatusArr, BaseMerchantEntity.DEL_FLAG_NORMAL);
        return  merchantOrderDetailRepository.updateCancelReasonWithFine(new Date(), cancelReason,waitPickCancelType, punishFee,subsidyFee,orderNo,BaseMerchantEntity.DEL_FLAG_NORMAL)==1;
    }

    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public boolean adminCancel(String merchantId, String orderNo, String cancelReason) {

        merchantOrderDao.orderCancelIgnoreStatus(orderNo, new Date(),EnumMerchantOrderStatus.CANCEL.getValue(),new Date(), BaseMerchantEntity.DEL_FLAG_NORMAL);
        return   merchantOrderDetailRepository.updateCancelReason(orderNo, cancelReason,new Date(), BaseMerchantEntity.DEL_FLAG_NORMAL)==1;

    }
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public boolean riderCancel(String orderNo, String cancelReason, Date now, Double subsidy,String merchantId) {
        List<String> orderStatusArr=new ArrayList<>();
        orderStatusArr.add(EnumMerchantOrderStatus.WAITING_PICK.getValue());
        merchantOrderDao.orderCancel(orderNo, now,EnumMerchantOrderStatus.RESEND.getValue(),new Date(),orderStatusArr, BaseMerchantEntity.DEL_FLAG_NORMAL);
        return  merchantOrderDetailRepository.updateCancelReasonWithFine(new Date(), cancelReason,null, null,subsidy,orderNo,BaseMerchantEntity.DEL_FLAG_NORMAL)==1;


    }

    /**
     * 商家删除订单
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int deleteOrder(String merchantId, String orderNo) {
        List<String> orderStatusArr=new ArrayList<>();
        orderStatusArr.add(EnumMerchantOrderStatus.INIT.getValue());
        orderStatusArr.add(EnumMerchantOrderStatus.CANCEL_GRAB_OVERTIME.getValue());
        orderStatusArr.add(EnumMerchantOrderStatus.CANCEL.getValue());
        orderStatusArr.add(EnumMerchantOrderStatus.CANCEL_PAY_OVERTIME.getValue());
        int count = merchantOrderDao.deleteOrder(MerchantOrderEntity.DEL_FLAG_DELETE,merchantId, orderNo,orderStatusArr,MerchantOrderEntity.DEL_FLAG_NORMAL);
        return count;
    }

    /**
     * 骑手抢单
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int riderGrabOrder(String merchantId, String riderId, String riderName, String riderPhone, String orderNo,
                              Date grabOrderTime, Date expectFinishTime, String riderCarNo, String riderCarType, Double pickupDistance) {
        merchantOrderDao.riderGrabOrder(EnumMerchantOrderStatus.WAITING_PICK.getValue(),riderId, riderName, riderPhone, orderNo, grabOrderTime, expectFinishTime,BaseMerchantEntity.DEL_FLAG_NORMAL);

        return   merchantOrderDetailRepository.riderGrabOrder( pickupDistance,orderNo,BaseMerchantEntity.DEL_FLAG_NORMAL);

    }
    /**
     * 骑手抢单
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional()
    public int riderGrabOrderOfPost(String merchantId, String riderId, String riderName, String riderPhone, String orderNo,
                              Date grabOrderTime, Date expectFinishTime, Date pickTime,  Double pickupDistance) {
        merchantOrderDao.riderGrabOrderOfPost(EnumMerchantOrderStatus.DELIVERYING.getValue(),riderId, riderName, riderPhone, orderNo, grabOrderTime, expectFinishTime,BaseMerchantEntity.DEL_FLAG_NORMAL);

        return merchantOrderDetailRepository.riderGrabOrderOfPost(pickupDistance, orderNo,BaseMerchantEntity.DEL_FLAG_NORMAL);
    }
    /**
     * 骑手取货
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int riderGrabItem(String merchantId, String orderNo, Date grabItemTime) {
        return merchantOrderDao.riderGrabItem(EnumMerchantOrderStatus.DELIVERYING.getValue(), grabItemTime,orderNo,EnumMerchantOrderStatus.WAITING_PICK.getValue(),BaseMerchantEntity.DEL_FLAG_NORMAL);
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
            return merchantOrderDao.riderFinishOrder(EnumMerchantOrderStatus.FINISH.getValue(), finishOrderTime,orderNo,EnumMerchantOrderStatus.DELIVERYING.getValue(),BaseMerchantEntity.DEL_FLAG_NORMAL);
        } else {
            //订单超时
            logger.info("保存超时订单：orderNo:{},expiredMinute:{},,赔付金额:{}",orderNo,expiredMinute,expiredCompensation);
            merchantOrderDao.riderFinishOrder(EnumMerchantOrderStatus.FINISH.getValue(), finishOrderTime,orderNo,EnumMerchantOrderStatus.DELIVERYING.getValue(),BaseMerchantEntity.DEL_FLAG_NORMAL);
            return merchantOrderDetailRepository.riderFinishExpiredOrder(orderNo, expiredMinute, expiredCompensation,BaseMerchantEntity.DEL_FLAG_NORMAL);
        }
    }

    /**
     * 支付超时
     */
//    @CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public void payExpired(String merchantId, String orderNo) {
        List<String> orderStatusArr=new ArrayList<>();
        orderStatusArr.add(EnumMerchantOrderStatus.INIT.getValue());
        merchantOrderDao.orderCancel(orderNo, new Date(),EnumMerchantOrderStatus.CANCEL.getValue(),new Date(),orderStatusArr, BaseMerchantEntity.DEL_FLAG_NORMAL);
        merchantOrderDetailRepository.updateCancelReason(orderNo, EnumCancelReason.PAY_OVERTIME.getValue(),new Date(), BaseMerchantEntity.DEL_FLAG_NORMAL);
    }

    /**
     * 订单超时
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int orderExpire(String merchantId, String orderNo, Date expireTime,String orderStatus) {
        List<String> orderStatusArr=new ArrayList<>();
        orderStatusArr.add(orderStatus);
        int count = merchantOrderDao.orderCancel(orderNo, expireTime,EnumMerchantOrderStatus.CANCEL_GRAB_OVERTIME.getValue(),new Date(),orderStatusArr, BaseMerchantEntity.DEL_FLAG_NORMAL);
        count=merchantOrderDetailRepository.updateCancelReason(orderNo, EnumCancelReason.GRAB_OVERTIME.getValue(),new Date(), BaseMerchantEntity.DEL_FLAG_NORMAL);

        return count;
    }

    /**
     * 重新发单
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int orderResend(String merchantId, String originOrderNo){
        int count = merchantOrderDao.orderResend(originOrderNo,EnumMerchantOrderStatus.RESEND.getValue(),EnumMerchantOrderStatus.CANCEL.getValue(),new Date(),BaseMerchantEntity.DEL_FLAG_NORMAL);
        return count;
    }

    /**
     * 商家查询订单分页（缓存）
     */
    @Cacheable(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#entity.getUserId()+'_'+#entity.getOrderType()+'_'+#entity.getOrderStatus()+'_'+#pageNo+'_'+#pageSize")
    public Page<OrderEntity> merchantQueryOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity) {
        PageRequest pageRequest = new PageRequest(pageNo-1, pageSize,  new Sort(Sort.Direction.DESC, "createDate"));
        Page<OrderEntity> page = merchantOrderDao.findAll(where(entity), pageRequest);
        return page;

       // return merchantOrderDao.findMerchantOrderPageToApp(pageNo, pageSize, entity);
    }

    private Specification<OrderEntity> where(final MerchantOrderEntity entity){
        return new Specification<OrderEntity>() {
            @Override
            public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("delFlag").as(String.class), MerchantInfoEntity.DEL_FLAG_NORMAL));
                if(StringUtils.isNotBlank(entity.getUserId())){
                    list.add(cb.and(cb.equal(root.get("userId").as(String.class), entity.getUserId())));
                }
                if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                        MerchantConstants.ORDER_LIST_QUERY_CONDITION_UNHANDLE.equals(entity.getOrderStatus())){
                    list.add(cb.or(cb.equal(root.get("orderStatus").as(String.class), "CANCEL"),cb.equal(root.get("orderStatus").as(String.class), "INIT")));
                }else if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                        MerchantConstants.ORDER_LIST_QUERY_CONDITION_FINISH.equals(entity.getOrderStatus())){
                    list.add(cb.or(cb.equal(root.get("orderStatus").as(String.class), "RESEND"),cb.equal(root.get("orderStatus").as(String.class), "FINISH")));
                }else if (EnumMerchantOrderStatus.UNDELIVERED.getValue().equals(entity.getOrderStatus())){
                    //未妥投
                    list.add(cb.equal(root.get("unsettledStatus").as(String.class),PostOrderUnsettledStatusEnum.ING.getValue()));
                    list.add(cb.equal(root.get("orderStatus").as(String.class),EnumMerchantOrderStatus.DELIVERYING.getValue()));

                }else if(EnumMerchantOrderStatus.CONFIRM.getValue().equals(entity.getOrderStatus())){
                    //已经确认
                    list.add(cb.equal(root.get("unsettledStatus").as(String.class),PostOrderUnsettledStatusEnum.FINISH.getValue()));
                    list.add(cb.equal(root.get("orderStatus").as(String.class),EnumMerchantOrderStatus.FINISH.getValue()));

                } else if (EnumMerchantOrderStatus.DELIVERYING.getValue().equals(entity.getOrderStatus())){
                    //待配送
                    list.add(cb.isNull(root.get("unsettledStatus").as(String.class)));
                    list.add(cb.equal(root.get("orderStatus").as(String.class),entity.getOrderStatus()));
                }
                else if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                        !MerchantConstants.ORDER_LIST_QUERY_CONDITION_ALL.equals(entity.getOrderStatus())){
                    list.add(cb.equal(root.get("orderStatus").as(String.class),entity.getOrderStatus()));

                }
                if (StringUtils.isNotBlank(entity.getOrderType())){
                    if (entity.getOrderType().equalsIgnoreCase(EnumOrderType.POSTORDER.getValue())){
                        //驿站单 查询postOrder和postnormalOrder
                        list.add(cb.or(cb.equal(root.get("orderType").as(String.class), EnumOrderType.POSTORDER.getValue()),cb.equal(root.get("orderType").as(String.class), EnumOrderType.POST_NORMAL_ORDER.getValue())));
                    }
                    if (entity.getOrderType().equalsIgnoreCase(EnumOrderType.SMALLORDER.getValue())){
                        list.add(cb.equal(root.get("orderType").as(String.class),entity.getOrderType()));
                    }

                }
                return criteriaQuery.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        };
    }


    //@Cacheable(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#entity.getOrderType()+'_'+#entity.getOrderStatus()+'_'+#pageNo+'_'+#pageSize")
    public Page<OrderEntity> postHouseQueryOrderPage(int pageNo, int pageSize, String expectFinishTimeSort,
                                                         String orderTimeSort, MerchantOrderEntity entity) {

//        PageRequest pageRequest = new PageRequest(pageNo-1, pageSize,  new Sort(Sort.Direction.DESC, "createDate"));
        //Sort sort=new Sort(Sort.Direction.DESC, "createDate"));
        Sort sort=null;
        if(StringUtils.isNotBlank(expectFinishTimeSort)){
            if ("asc".equalsIgnoreCase(expectFinishTimeSort)){
                sort.and(new Sort(new Sort.Order(Sort.Direction.ASC, "expectFinishTime")));
            }
            if ("desc".equalsIgnoreCase(expectFinishTimeSort)){
                sort.and(new Sort(new Sort.Order(Sort.Direction.DESC, "expectFinishTime")));
            }
        }
        if(StringUtils.isNotBlank(orderTimeSort)){
            if ("asc".equalsIgnoreCase(expectFinishTimeSort)){
                sort.and(new Sort(new Sort.Order(Sort.Direction.ASC, "orderTime")));
            }
            if ("desc".equalsIgnoreCase(expectFinishTimeSort)){
                sort.and(new Sort(new Sort.Order(Sort.Direction.DESC, "orderTime")));
            }
        }


        if (null==sort){
            sort=new Sort(Sort.Direction.DESC, "createDate");
        }
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Page<OrderEntity> page = merchantOrderDao.findAll(wherePostHouseOrderList(entity,expectFinishTimeSort,expectFinishTimeSort), pageRequest);
        return page;


   //     return merchantOrderDao.findMerchantOrderPageToPostHouse(pageNo, pageSize, expectFinishTimeSort, orderTimeSort,entity);
    }

    private Specification<OrderEntity> wherePostHouseOrderList(final MerchantOrderEntity entity,final String expectFinishTimeSort,
                                                               final String orderTimeSort){
        return new Specification<OrderEntity>() {
            @Override
            public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                List<Order> listorder = new ArrayList<>();

                list.add(cb.equal(root.get("delFlag").as(String.class), MerchantInfoEntity.DEL_FLAG_NORMAL));

                if (null != entity.getProviderId()){
                    list.add(cb.equal(root.get("providerId").as(Long.class),entity.getProviderId()));
                }
                if (StringUtils.isNotBlank(entity.getOrderType())){
                    list.add(cb.equal(root.get("orderType").as(String.class),entity.getOrderType()));


                }
                if (StringUtils.isNotBlank(entity.getOrderStatus())){
                    list.add(cb.equal(root.get("orderStatus").as(String.class),entity.getOrderStatus()));

                    if (StringUtils.isNotBlank(entity.getUnsettledStatus())){
                        list.add(cb.equal(root.get("unsettledStatus").as(String.class),entity.getUnsettledStatus()));
                    }else{
                        list.add(cb.isNull(root.get("unsettledStatus").as(String.class)));
                    }
                }
                if (StringUtils.isNotBlank(entity.getOrderNo())){
                    list.add(cb.equal(root.get("orderNo").as(String.class),entity.getOrderNo()));
                }
                if (StringUtils.isNotBlank(entity.getReceiverPhone())){
                    list.add(cb.like(root.get("receiverPhone").as(String.class),entity.getReceiverPhone()+"%"));
                }
                if (StringUtils.isNotBlank(entity.getRiderId())){
                    list.add(cb.equal(root.get("riderId").as(String.class),entity.getRiderId()));
                }
                if (StringUtils.isNotBlank(entity.getRiderName())){
                    list.add(cb.like(root.get("riderName").as(String.class),entity.getRiderName()+"%"));
                }
                if (StringUtils.isNotBlank(entity.getSenderId())){
                    list.add(cb.equal(root.get("senderId").as(String.class),entity.getSenderId()));
                }
                if (StringUtils.isNotBlank(entity.getSenderName())){
                    list.add(cb.like(root.get("senderName").as(String.class),entity.getSenderName()+"%"));

                }
                if (null != entity.getCreateDate()){
                    //list.add(cb.greaterThanOrEqualTo(root.get("orderTime").as(Date.class),DateUtils.formatDateTime(entity.getCreateDate())));
                    list.add(cb.greaterThanOrEqualTo(root.get("orderTime").as(Date.class),entity.getCreateDate()));
                 /*   sb.append("and order_time >= :create_date ");
                    parameter.put("create_date", DateUtils.formatDateTime(entity.getCreateDate()));*/
                }
                if (null != entity.getUpdateDate()){
                    list.add(cb.lessThanOrEqualTo(root.get("orderTime").as(Date.class),entity.getUpdateDate()));
                 /*   sb.append("and order_time <= :update_date ");
                    parameter.put("update_date", DateUtils.formatDateTime(entity.getUpdateDate()));*/
                }
                return criteriaQuery.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        };
    }







    /**
     * 后台查询分页（不缓存）
     */
    public Page<OrderEntity> adminQueryOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity) {
        PageRequest pageRequest = new PageRequest(pageNo-1, pageSize,  new Sort(Sort.Direction.DESC, "createDate"));
        Page<OrderEntity> page = merchantOrderDao.findAll(whereOrderListAdmin(entity), pageRequest);
        return page;
    }


    private Specification<OrderEntity> whereOrderListAdmin(final MerchantOrderEntity entity){
        return new Specification<OrderEntity>() {
            @Override
            public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("delFlag").as(String.class), MerchantInfoEntity.DEL_FLAG_NORMAL));

                if(StringUtils.isNotBlank(entity.getUserId())){
                    list.add(cb.and(cb.equal(root.get("userId").as(String.class), entity.getUserId())));
                }

               /* if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                        MerchantConstants.ORDER_LIST_QUERY_CONDITION_UNHANDLE.equals(entity.getOrderStatus())){
                    list.add(cb.or(cb.equal(root.get("orderStatus").as(String.class), "CANCEL"),cb.equal(root.get("orderStatus").as(String.class), "INIT")));
                }else if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                        MerchantConstants.ORDER_LIST_QUERY_CONDITION_FINISH.equals(entity.getOrderStatus())){
                    list.add(cb.or(cb.equal(root.get("orderStatus").as(String.class), "RESEND"),cb.equal(root.get("orderStatus").as(String.class), "FINISH")));
                }else if (EnumMerchantOrderStatus.UNDELIVERED.getValue().equals(entity.getOrderStatus())){
                    //未妥投
                    list.add(cb.equal(root.get("unsettledStatus").as(String.class),PostOrderUnsettledStatusEnum.ING.getValue()));
                    list.add(cb.equal(root.get("orderStatus").as(String.class),EnumMerchantOrderStatus.DELIVERYING.getValue()));

                }else if(EnumMerchantOrderStatus.CONFIRM.getValue().equals(entity.getOrderStatus())){
                    //已经确认
                    list.add(cb.equal(root.get("unsettledStatus").as(String.class),PostOrderUnsettledStatusEnum.FINISH.getValue()));
                    list.add(cb.equal(root.get("orderStatus").as(String.class),EnumMerchantOrderStatus.FINISH.getValue()));

                } else if (EnumMerchantOrderStatus.DELIVERYING.getValue().equals(entity.getOrderStatus())){
                    //待配送
                    list.add(cb.isNull(root.get("unsettledStatus").as(String.class)));
                    list.add(cb.equal(root.get("orderStatus").as(String.class),entity.getOrderStatus()));
                }
                else if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                        !MerchantConstants.ORDER_LIST_QUERY_CONDITION_ALL.equals(entity.getOrderStatus())){
                    list.add(cb.equal(root.get("orderStatus").as(String.class),entity.getOrderStatus()));

                }*/





               if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                        !MerchantConstants.ORDER_LIST_QUERY_CONDITION_ALL.equals(entity.getOrderStatus())){

                    if (EnumMerchantOrderStatus.UNDELIVERED.getValue().equals(entity.getOrderStatus())){
                        //未妥投
                        list.add(cb.equal(root.get("unsettledStatus").as(String.class),PostOrderUnsettledStatusEnum.ING.getValue()));
                        list.add(cb.equal(root.get("orderStatus").as(String.class),EnumMerchantOrderStatus.DELIVERYING.getValue()));
                    }else if(EnumMerchantOrderStatus.CONFIRM.getValue().equals(entity.getOrderStatus())){
                        //已经确认
                        list.add(cb.equal(root.get("unsettledStatus").as(String.class),PostOrderUnsettledStatusEnum.FINISH.getValue()));
                        list.add(cb.equal(root.get("orderStatus").as(String.class),EnumMerchantOrderStatus.FINISH.getValue()));

                    } else {
                        //正常
                        list.add(cb.equal(root.get("orderStatus").as(String.class),entity.getOrderStatus()));
                        list.add(cb.isNull(root.get("unsettledStatus").as(String.class)));
                    }
                }
                if (StringUtils.isNotBlank(entity.getOrderType())){
                    if (entity.getOrderType().equalsIgnoreCase(EnumOrderType.POSTORDER.getValue())){
                        //驿站单 查询postOrder和postnormalOrder
                        list.add(cb.or(cb.equal(root.get("orderType").as(String.class), EnumOrderType.POSTORDER.getValue()),cb.equal(root.get("orderType").as(String.class), EnumOrderType.POST_NORMAL_ORDER.getValue())));
                    }
                    if (entity.getOrderType().equalsIgnoreCase(EnumOrderType.SMALLORDER.getValue())){
                        list.add(cb.equal(root.get("orderType").as(String.class),entity.getOrderType()));
                    }

                }
                if (StringUtils.isNotBlank(entity.getOrderNo())){
                    list.add(cb.equal(root.get("orderNo").as(String.class),entity.getOrderNo()));
                }
                if (StringUtils.isNotBlank(entity.getRiderPhone())){
                    list.add(cb.equal(root.get("riderPhone").as(String.class),entity.getRiderPhone()));
                }
                if (StringUtils.isNotBlank(entity.getReceiverPhone())){
                    list.add(cb.like(root.get("receiverPhone").as(String.class),entity.getReceiverPhone()));

                }
                if (StringUtils.isNotBlank(entity.getSenderName())){
                    list.add(cb.like(root.get("senderName").as(String.class),entity.getSenderName()));

                }
                if (null != entity.getCreateDate()){
                    list.add(cb.greaterThanOrEqualTo(root.get("createDate").as(Date.class),DateUtils.getDateStart(entity.getCreateDate())));

                }
                if (null != entity.getUpdateDate()){
                    list.add(cb.lessThanOrEqualTo(root.get("orderTime").as(Date.class),DateUtils.getDateEnd(entity.getUpdateDate())));
                }
                if (StringUtils.isNotBlank(entity.getSenderAdcode())){
                    list.add(cb.like(root.get("senderAdcode").as(String.class),entity.getSenderAdcode()));
                }
                if (null!=entity.getProviderId()){
                    list.add(cb.equal(root.get("providerId").as(Long.class),entity.getProviderId()));

                }
                /*if (StringUtils.isNotBlank(entity.getProviderName())){
                    list.add(cb.equal(root.get("providerName").as(String.class),entity.getProviderName()));
                }*/


                return criteriaQuery.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        };
    }






















    /**
     * 获取
     *
     * @param overTimeMilSeconds
     * @return
     */
    @Transactional(readOnly = true)
    public List<String> getUnCanceledGrabOvertimeOrderNoList(Integer overTimeMilSeconds) {
        Date diff = new Date(overTimeMilSeconds);
        return merchantOrderDao.getUnCanceledGrabOvertimeOrderNoList(diff);
    }

    /**
     * 计算当天已完成的订单
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public Long getTodayFinishOrderNum(String userId) {
        return merchantOrderDao.getTodayFinishOrderNum( DateUtils.getDateStart(new Date()),userId,BaseMerchantEntity.DEL_FLAG_NORMAL);
    }
    /**
     * 计算当天已完成的订单(带有短信通知)
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public Long getTodayFinishOrderWithShortTextNum(String userId) {

        return merchantOrderDao.getTodayFinishOrderWithShortTextNum(DateUtils.getDateStart(new Date()),userId,MerchantConstants.ORDER_MESSAGE_OPEN,BaseMerchantEntity.DEL_FLAG_NORMAL);
    }

    /**
     * 获取指定providerId，和orderStatus unsettledStatus的订单数目
     * @return
     */
    //@Cacheable(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#providerId+'_'")
    @Transactional(readOnly = true)
    public Long getOrderWithProviderIdAndStatus(Long providerId, String orderStatus, String unsettledStatus){
        return merchantOrderDao.count(where(providerId,orderStatus,unsettledStatus));
          //  return merchantOrderDao.getOrderWithProviderIdAndStatus(providerId, orderStatus, unsettledStatus);
    }
    private Specification<OrderEntity> where(final Long providerId, final String orderStatus, final String unsettledStatus) {
        return new Specification<OrderEntity>() {
            @Override
            public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("delFlag").as(String.class), MerchantInfoEntity.DEL_FLAG_NORMAL));
                if(null != providerId){
                    list.add(cb.equal(root.get("providerId").as(Long.class), providerId));
                }
                if(StringUtils.isNotBlank(orderStatus) ){
                    list.add(cb.equal(root.get("orderStatus").as(String.class), orderStatus));
                }
                if(StringUtils.isNotBlank(unsettledStatus) ){
                    list.add(cb.equal(root.get("unsettledStatus").as(String.class), unsettledStatus));
                }
                return criteriaQuery.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }

        };
    }


    /*public Long getOrderWithProviderIdAndStatus(Long providerId, String orderStatus, String unsettledStatus){
        Parameter parameter = new Parameter();
        StringBuffer sb = new StringBuffer();
        sb.append("select count(orderNo) FROM MerchantOrderEntity WHERE delFlag= '0' ");
        if (null!=providerId){
            sb.append("and providerId = :providerId  ");
            parameter.put("providerId", providerId);
        }
        if (StringUtils.isNotBlank(orderStatus)){
            sb.append("and orderStatus = :orderStatus  ");
            parameter.put("orderStatus", orderStatus);
        }
        if (StringUtils.isNotBlank(unsettledStatus)){
            sb.append("and unsettledStatus = :unsettledStatus  ");
            parameter.put("unsettledStatus", unsettledStatus);
        }
        Query query = createQuery(sb.toString(), parameter);
        Long count = (Long) query.uniqueResult();
        return count;
    }*/


    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public boolean rateOrder(String merchantId, String orderNo) {
        int count = merchantOrderDao.rateOrder(true,orderNo,BaseMerchantEntity.DEL_FLAG_NORMAL);
        logger.info("更新订单：{}的状态为已评价，result:{}", orderNo, count);
        return count == 1;
    }

    @Transactional(readOnly = true)
    public List<String> getUnRatedOrderNos(int days) {
        Date daysBefore = DateUtils.getDaysBefore(new Date(),days);
        Date begin = DateUtils.getDateStart(daysBefore);
        Date end = DateUtils.getDateEnd(daysBefore);
        return merchantOrderDao.getUnRatedOrderNos(EnumMerchantOrderStatus.FINISH.getValue(),false,begin,end,BaseMerchantEntity.DEL_FLAG_NORMAL);
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
    public int riderUnsettledOrder(String merchantId, String orderNo, String reason, Date finishOrderTime, Double expiredMinute) {

         merchantOrderDao.riderUnsettledOrder(PostOrderUnsettledStatusEnum.ING.getValue(),finishOrderTime,orderNo,EnumMerchantOrderStatus.DELIVERYING.getValue());
        return merchantOrderDetailRepository.riderUnsettledOrder(reason,expiredMinute,orderNo,BaseMerchantEntity.DEL_FLAG_NORMAL);
    }

    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int merchantHandlerUnsettledOrder(String merchantId, String orderNo,Date unsettledTime,String message) {
         merchantOrderDao.merchantHandlerUnsettledOrder(PostOrderUnsettledStatusEnum.FINISH.getValue(), EnumMerchantOrderStatus.FINISH.getValue()
              ,unsettledTime ,orderNo,PostOrderUnsettledStatusEnum.ING.getValue(),BaseMerchantEntity.DEL_FLAG_NORMAL);
        return merchantOrderDetailRepository.merchantHandlerUnsettledOrder(message,orderNo,BaseMerchantEntity.DEL_FLAG_NORMAL);
    }
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOrder_'+#merchantId+'_*'")
    @Transactional(readOnly = false)
    public int  updateShortMessage(Boolean setting,String  orderNo,String userId){
        return merchantOrderDao.updateShortMessage(setting,userId,orderNo,BaseMerchantEntity.DEL_FLAG_NORMAL);
    }


}
