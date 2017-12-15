package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.utils.CalCulateUtil;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.taskcenter.api.TaskDispatchService;
import com.xinguang.taskcenter.api.TbbTaskResponse;
import com.xinguang.taskcenter.api.common.enums.TaskTypeEnum;
import com.xinguang.taskcenter.api.request.TaskCreateDTO;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayWithOutPwdRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.admin.api.OverFeeService;
import com.xinguang.tubobo.admin.api.dto.OverFeeDTO;
import com.xinguang.tubobo.impl.merchant.amap.RoutePlanning;
import com.xinguang.tubobo.impl.merchant.common.AddressInfoToOrderBeanHelper;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.common.OrderUtil;
import com.xinguang.tubobo.impl.merchant.condition.ProcessQueryCondition;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.*;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import com.xinguang.tubobo.impl.merchant.mq.RmqNoticeProducer;
import com.xinguang.tubobo.impl.merchant.repository.ThirdOrderRepository;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
//@Transactional(readOnly = true)
public class ThirdOrderService {
    Logger logger = LoggerFactory.getLogger(ThirdOrderService.class);

    @Autowired private ThirdOrderRepository thirdOrderRepository;
    @Autowired private MerchantInfoService merchantInfoService;
    @Autowired private AutoResendPostSettingsService autoResendPostSettingsService;
    @Autowired
    Config config;
    @Autowired
    OverFeeService overFeeService;
    @Autowired
    OrderService   orderService;
    @Autowired
    TbbAccountService tbbAccountService;
    @Autowired
    MerchantOrderManager orderManager;
    @Autowired
    TaskDispatchService taskDispatchService;
    @Autowired
    MerchantMessageSettingsService merchantSettingsService;
    @Autowired
    RmqNoticeProducer rmqNoticeService;
    @Autowired
    DeliveryFeeService deliveryFeeService;
    @Autowired
    RoutePlanning routePlanning;
    /**
     * 保存订单到thirdOrder
     * 返回值  true-保存成功
     * */
    @Transactional()
    public boolean saveMtOrder(ThirdOrderEntity mtOrderEntity){
        boolean flag=false;
        ThirdOrderEntity existEntity = thirdOrderRepository.findByOriginOrderIdAndPlatformCode(mtOrderEntity.getOriginOrderId(),
                mtOrderEntity.getPlatformCode());
        if (null == existEntity){
            mtOrderEntity.setCreateDate(new Date());
            mtOrderEntity.setUpdateDate(new Date());
            mtOrderEntity.setDelFlag(BaseMerchantEntity.DEL_FLAG_NORMAL);
            thirdOrderRepository.save(mtOrderEntity);
            //dealAutoSendOrder(mtOrderEntity);
            flag=true;
        }
        return flag;
    }
    /**
    * 自动发单(针对驿站订单 且开启自动发单功能)
    * */
    @Transactional()
    public void dealAutoSendOrder(ThirdOrderEntity mtOrderEntity) {

        String userId = mtOrderEntity.getUserId();
        MerchantInfoEntity merchant = merchantInfoService.findByUserId(userId);
        if (merchant.getProviderId() == null){
            //非驿站商家  不进行自动发单
            return;
        }
        AutoResendPostSettingEntity setting = autoResendPostSettingsService.findByUserIdAndCreate(userId);
        if (setting.getAutoPostOrderResendOpen()){
            //自动发单开启
            try {
                OrderUtil.judgeOrderCondition(merchant.getMerchantStatus(),config.getBeginWorkTime(),config.getEndWorkTime(),false);
            } catch (MerchantClientException e) {
                logger.error("三方订单自动发单审核权限error,:originOrder:{}",mtOrderEntity.toString(), e);
                return ;
            }
            //封装信息并创建订单
            MerchantOrderEntity newOrderEntity = null;
            try {
                newOrderEntity = packageAndSaveOrder(mtOrderEntity, userId, merchant);
            } catch (MerchantClientException e) {
                logger.error("驿站商家自动返单计算配送费error,deliveryFeeDto:{}",mtOrderEntity.toString(),e);
                return ;
            }
            //支付
            //商家扣款 并修改数据   扣款封装小方法 短信费用抽出
            if (newOrderEntity.getShortMessage()){
                Double realPayAmount=CalCulateUtil.sub(newOrderEntity.getPayAmount(),MerchantConstants.MESSAGE_FEE);
                newOrderEntity.setPayAmount(realPayAmount);
            }
            try {
                //支付失败,订单不回滚
                payWithoutPassword(userId, merchant, newOrderEntity);
                }catch (Exception e
                        ){
                    logger.error("自动付款失败",e);
                }
        }
    }
    /**
     * 封装订单信息 创建订单(针对驿站订单 且开启自动发单功能)
     * */
    @Transactional()
    private MerchantOrderEntity packageAndSaveOrder(ThirdOrderEntity mtOrderEntity, String userId, MerchantInfoEntity merchant) throws MerchantClientException {
        MerchantOrderEntity newOrderEntity = new MerchantOrderEntity();
        //开启自动发单功能
        //封装地理信息
        AddressInfoToOrderBeanHelper.putSenderFromMerchantInfoEntity(newOrderEntity,merchant);
        newOrderEntity.setReceiverAddressDetail(ConvertUtil.handleNullString(mtOrderEntity.getReceiverAddressDetail()));
        newOrderEntity.setReceiverName(ConvertUtil.handleNullString(mtOrderEntity.getReceiverName()));
        newOrderEntity.setReceiverPhone(ConvertUtil.handleNullString(mtOrderEntity.getReceiverPhone()));
        newOrderEntity.setReceiverLatitude(mtOrderEntity.getReceiverLatitude());
        newOrderEntity.setReceiverLongitude(mtOrderEntity.getReceiverLongitude());
        //封装费用
        Double devliveryDistance=0.0;
        //获得本地区域码
        String nativeAreaCode = merchant.getAddressAdCode();
        //获取实际距离
        devliveryDistance = routePlanning.getDistanceWithWalkFirst(mtOrderEntity.getReceiverLongitude(),mtOrderEntity.getReceiverLatitude(),
                merchant.getLongitude(),merchant.getLatitude());
        Double deliveryFee = deliveryFeeService.sumDeliveryFeeByLocation(devliveryDistance, merchant);
        deliveryFee=deliveryFee==null?0.0:deliveryFee;
        //调用后台传来的溢价信息
        OverFeeDTO overFee = overFeeService.findOverFee(nativeAreaCode);
        Double peekOverFee=0.0;
        Double weatherOverFee=0.0;
        Double totalOverFee=0.0;
        if (null!=overFee){
            boolean peekIsOpen = overFee.getPeekIsOpen();
            //boolean weatherIsOpen = overFee.getWeatherIsOpen();
            //为空时说明该区域未开启天气溢价
            Double weatherOverFeeRemote=overFee.getWeatherOverFee();
            //校验区域;
            if (peekIsOpen){
                peekOverFee=overFee.getPeekOverFee();
                totalOverFee= CalCulateUtil.add(totalOverFee,peekOverFee);
                //totalOverFee+=peekOverFee;
            }
            if (null!=weatherOverFeeRemote){
                weatherOverFee=weatherOverFeeRemote;
                totalOverFee=CalCulateUtil.add(totalOverFee,weatherOverFee);
//                totalOverFee+=weatherOverFee;
            }
        }
        Double payAmount=0.0;
        payAmount= CalCulateUtil.add(payAmount,totalOverFee);
        payAmount=CalCulateUtil.add(payAmount,deliveryFee);
        MerchantMessageSettingsEntity merchantSettings=merchantSettingsService.findByUserIdAndCreate(userId);
        if (null!=merchantSettings&&merchantSettings.getMessageOpen()){
            //计算短信费
            newOrderEntity.setShortMessage(true);
            payAmount=CalCulateUtil.add(payAmount, Double.valueOf(MerchantConstants.MESSAGE_FEE));
        }else{
            newOrderEntity.setShortMessage(false);

        }
        newOrderEntity.setWeatherOverFee(weatherOverFee);
        newOrderEntity.setPeekOverFee(peekOverFee);
        newOrderEntity.setDeliveryFee(deliveryFee);
        newOrderEntity.setDeliveryDistance(devliveryDistance);
        newOrderEntity.setPayAmount(payAmount);
        newOrderEntity.setOrderType(EnumOrderType.POST_NORMAL_ORDER.getValue());
        newOrderEntity.setPlatformCode(mtOrderEntity.getPlatformCode());
        newOrderEntity.setOriginOrderId(mtOrderEntity.getOriginOrderId());
        newOrderEntity.setOriginOrderViewId(mtOrderEntity.getOriginOrderViewId());
        newOrderEntity.setTipFee(0.0);
        newOrderEntity.setUserId(userId);
        newOrderEntity.setSenderId(userId);
        newOrderEntity.setOrderStatus(EnumMerchantOrderStatus.INIT.getValue());
        newOrderEntity.setOrderTime(new Date());
        newOrderEntity.setPayStatus(EnumPayStatus.UNPAY.getValue());
        newOrderEntity.setDelFlag(MerchantOrderEntity.DEL_FLAG_NORMAL);
        newOrderEntity.setWeatherOverFee(weatherOverFee);
        newOrderEntity.setPeekOverFee(peekOverFee);
        newOrderEntity.setProviderId(merchant.getProviderId());
        newOrderEntity.setSenderAdcode(merchant.getAddressAdCode());
        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(newOrderEntity,orderDetailEntity);
        BeanUtils.copyProperties(newOrderEntity,orderEntity);
        //创建订单
        String orderNo = orderService.saveOrderOnly(userId, orderEntity, orderDetailEntity);
        processOrder(userId,orderEntity.getPlatformCode(),orderEntity.getOriginOrderId());
        return orderService.findByOrderNo(orderNo);
    }

    /**
     * 自动发单(直接免密支付并createTask)
     * */
    @Transactional()
    private void payWithoutPassword(String userId, MerchantInfoEntity merchant, MerchantOrderEntity newOrderEntity) {
        Double payAmount=newOrderEntity.getPayAmount();
        String orderNo=newOrderEntity.getOrderNo();
        PayWithOutPwdRequest payWithOutPwdRequest = new PayWithOutPwdRequest();
        payWithOutPwdRequest.setOrderId(orderNo);
        payWithOutPwdRequest.setAccountId(merchant.getAccountId());
        long amount = ConvertUtil.convertYuanToFen(payAmount);
        long commission = ConvertUtil.convertYuanToFen(newOrderEntity.getPlatformFee());
        payWithOutPwdRequest.setAmount(amount);
        payWithOutPwdRequest.setCommission(commission);
        logger.info("免密支付请求：userId:{}, orderNo:{} ,amount:{}分 ",userId,orderNo,payWithOutPwdRequest.getAmount());
        TbbAccountResponse<PayInfo> response;
        //TODO 拆分抽成金额
        response =  tbbAccountService.payWithOutPwd(payWithOutPwdRequest);

        if (response != null && response.isSucceeded()){
            long payId = response.getData().getId();
            newOrderEntity.setPayId(payId);
            TaskCreateDTO orderDTO = orderManager.buildMerchantOrderDTO(newOrderEntity,merchant);
            orderDTO.setPayId(payId);
            logger.info("pay  SUCCESS. orderNo:{}, accountId:{}, payId:{}, amount:{}",orderNo
                    ,merchant.getAccountId(),response.getData().getId(),amount);
            orderDTO.setTaskType(TaskTypeEnum.POST_NORMAL_ORDER);
            Date payDate = new Date();
            int count = orderService.merchantPay(userId,orderNo,payId,payDate, EnumMerchantOrderStatus.WAITING_GRAB.getValue());
            if (count != 1){
                logger.error("用户支付，数据更新错误，userID：{}，orderNo:{}",userId,orderNo);
                //throw new MerchantClientException(EnumRespCode.FAIL);
                return;
            }
            TbbTaskResponse<Boolean> taskResponse = taskDispatchService.createTask(orderDTO);
            if (taskResponse.isSucceeded() && taskResponse.getData()){
            }else {
                logger.error("调用任务中心发单出错，orderNo:{},errorCode:{},errorMsg:{}",orderNo,taskResponse.getErrorCode(),taskResponse.getMessage());
                return;
            }
        }else {
            if (response == null){
                logger.error("pay  FAIL.orderNo:{}, accountId:{}}",
                        orderNo,merchant.getAccountId());
            }else {
                if (response.getErrorCode().equals(TbbAccountResponse.ErrorCode.ERROR_AMOUNT_NOT_ENOUGH.getCode())){
                    logger.error("驿站订单自动发单  FAIL.,余额不足。orderNo:{}, accountId:{}, errorCode:{}, errorMsg{}",
                            orderNo,merchant.getAccountId(),response.getErrorCode(),response.getMessage());
                    //TODO 处理余额不足逻辑 1.用户自动发单开关调制关闭 2.notice通知
                    //关闭自动返单
                    AutoResendPostSettingEntity newSettings = new AutoResendPostSettingEntity();
                    newSettings.setUserId(userId);
                    newSettings.setAutoPostOrderResendOpen(false);
                    autoResendPostSettingsService.updateSettings(userId,newSettings);
                    //通知客户端余额不足
                    rmqNoticeService.sendForAutoPostOrderWhenMoneyLess(userId);
                   // return new ClientResp(e.getCode() , e.getErrorMsg());
                    return;
                    //throw  new MerchantClientException(EnumRespCode.ACCOUNT_NOT_ENOUGH);
                }
            }
            logger.info("驿站订单自动发单error orderNo:{},error{}",orderNo,EnumRespCode.ACCOUNT_PAY_FAIL);
            return;
            //throw  new MerchantClientException(EnumRespCode.ACCOUNT_PAY_FAIL);
        }
    }

    @Transactional()
    public void processOrder(String userId,String platformCode,String originOrderId){
         thirdOrderRepository.processOrder(true,new Date(),originOrderId,platformCode,userId,BaseMerchantEntity.DEL_FLAG_NORMAL);
    }
    @Transactional()
    public Page<ThirdOrderEntity> findUnProcessedPageByUserId(ProcessQueryCondition condition, @Min(1) int pageNo,@Min(1) int pageSize){

        PageRequest pageRequest = new PageRequest((pageNo-1), pageSize, new Sort(Sort.Direction.DESC, "createDate"));
        Page<ThirdOrderEntity> page = thirdOrderRepository.findAll(where(condition), pageRequest);
        return page;
    }
    @Transactional()
    private Specification<ThirdOrderEntity> where(final ProcessQueryCondition condition) {
        return new Specification<ThirdOrderEntity>() {
            @Override
            public Predicate toPredicate(Root<ThirdOrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(cb.equal(root.get("delFlag").as(String.class), "0"));
                list.add(cb.equal(root.get("processed"), false));
                if (StringUtils.isNotBlank(condition.getUserId())){
                    list.add(cb.equal(root.get("userId").as(String.class), condition.getUserId()));
                }
                if (StringUtils.isNotBlank(condition.getPlatformCode())){
                    list.add(cb.equal(root.get("platformCode").as(String.class), condition.getPlatformCode()));
                }
                if ("PHONE".equals(condition.getQueryType())){
                    if (StringUtils.isNotBlank(condition.getKeyword())){
                        list.add(cb.like(root.get("receiverPhone").as(String.class), "%"+condition.getKeyword()));

                    }
                }else {
                    if (StringUtils.isNotBlank(condition.getKeyword())){
                        list.add(cb.like(root.get("originOrderViewId").as(String.class), "%"+condition.getKeyword()+"%"));
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
        int count = thirdOrderRepository.delRecordsPastHours(BaseMerchantEntity.DEL_FLAG_DELETE,new Date(),lastValidDate,BaseMerchantEntity.DEL_FLAG_NORMAL);
        logger.info("删除 {} 条,{} 小时之前的第三方平台订单记录",count,pastHours);
    }
}
