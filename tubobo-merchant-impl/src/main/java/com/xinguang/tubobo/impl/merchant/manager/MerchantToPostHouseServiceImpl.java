package com.xinguang.tubobo.impl.merchant.manager;

import com.xinguang.tubobo.api.enums.EnumAuthentication;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.entity.OrderEntity;
import com.xinguang.tubobo.impl.merchant.mq.RmqNoticeProducer;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantToPostHouseServiceInterface;
import com.xinguang.tubobo.merchant.api.condition.MerchantInfoQueryCondition;
import com.xinguang.tubobo.merchant.api.condition.MerchantOrderQueryCondition;
import com.xinguang.tubobo.merchant.api.dto.MerchantInfoDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderDTO;
import com.xinguang.tubobo.merchant.api.dto.OrderStatusStatsDTO;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumBindStatusType;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantPostExceptionCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xuqinghua on 2017/9/1.
 */
public class MerchantToPostHouseServiceImpl implements MerchantToPostHouseServiceInterface {

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private MerchantOrderManager merchantOrderManager;

    @Autowired
    private RmqNoticeProducer rmqNoticeProducer;

    @Override
    public EnumMerchantPostExceptionCode bindProvider(String userId, Long providerId, String providerName) {
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity) {
            return EnumMerchantPostExceptionCode.SHOP_NOT_EXIST;
        }
        if (!EnumAuthentication.SUCCESS.getValue().equals(infoEntity.getMerchantStatus())) {
            return EnumMerchantPostExceptionCode.SHOP_NOT_AUTHED;
        }
        if (EnumBindStatusType.SUCCESS.getValue().equals(infoEntity.getBindStatus())) {
            return EnumMerchantPostExceptionCode.SHOP_ALREADY_BOUND;
        }
        if (EnumBindStatusType.NOOPERATE.getValue().equals(infoEntity.getBindStatus())) {
            return EnumMerchantPostExceptionCode.SHOP_ALREADY_UNBOUND;
        }
        boolean result = merchantInfoService.bindProvider(userId, providerId, providerName);
        if (result) {
            //发送绑定通知
            rmqNoticeProducer.sendMerchantBindNotice(infoEntity.getUserId(), providerName);
            return EnumMerchantPostExceptionCode.SUCCESS;
        }
        return EnumMerchantPostExceptionCode.FAIL;
    }

    @Override
    public EnumMerchantPostExceptionCode unbindProvider(String userId, long providerId) {
        MerchantInfoEntity riderInfoEntity = merchantInfoService.findByUserId(userId);
        if (null == riderInfoEntity) {
            return EnumMerchantPostExceptionCode.SHOP_NOT_EXIST;
        }
        if (!EnumAuthentication.SUCCESS.getValue().equals(riderInfoEntity.getMerchantStatus())) {
            return EnumMerchantPostExceptionCode.SHOP_NOT_AUTHED;
        }
        if (null == riderInfoEntity.getProviderId() || providerId != riderInfoEntity.getProviderId()) {
            return EnumMerchantPostExceptionCode.SHOP_NO_PERMISSION;
        }
        boolean result = merchantInfoService.unbindProvider(userId, providerId);
        if (result) {
            //发送解绑通知
            rmqNoticeProducer.sendMerchantUnbindMotice(riderInfoEntity.getUserId(), riderInfoEntity.getProviderName());
            return EnumMerchantPostExceptionCode.SUCCESS;
        }
        return EnumMerchantPostExceptionCode.FAIL;
    }

    @Override
    public PageDTO<MerchantInfoDTO> findMerchantList(MerchantInfoQueryCondition queryCondition) {
        MerchantInfoEntity infoEntity = new MerchantInfoEntity();
        infoEntity.setPhone(queryCondition.getShopPhone());
        infoEntity.setMerchantName(queryCondition.getShopName());
        infoEntity.setUserId(queryCondition.getShopId());
        infoEntity.setProviderId(queryCondition.getProviderId());
        infoEntity.setBindStatus(queryCondition.getBindStatus());
        Page<MerchantInfoEntity> page = merchantInfoService.findMerchantInfoPage(queryCondition.getPageNo(), queryCondition.getPageSize(), infoEntity);
        List<MerchantInfoDTO> list = new LinkedList<>();
        if (page.hasContent()) {
            for (MerchantInfoEntity entity : page) {
                MerchantInfoDTO infoDTO = new MerchantInfoDTO();
                BeanUtils.copyProperties(entity, infoDTO);
                list.add(infoDTO);
            }
        }
        PageDTO<MerchantInfoDTO> respPage = new PageDTO(queryCondition.getPageNo(), queryCondition.getPageSize(), page.getTotalElements(), list);
        return respPage;
    }

    @Override
    public PageDTO<MerchantOrderDTO> findMerchantOrderList(MerchantOrderQueryCondition queryCondition) {
        MerchantOrderEntity orderEntity = new MerchantOrderEntity();
        orderEntity.setCreateDate(queryCondition.getOrderTimeStart());
        orderEntity.setUpdateDate(queryCondition.getOrderTimeEnd());
        orderEntity.setOrderNo(queryCondition.getOrderNo());
        orderEntity.setOrderStatus(queryCondition.getOrderStatus());
        orderEntity.setOrderType(queryCondition.getOrderType());
        orderEntity.setReceiverPhone(queryCondition.getReceiverPhone());
        orderEntity.setRiderId(queryCondition.getRiderId());
        orderEntity.setRiderName(queryCondition.getRiderName());
        orderEntity.setSenderId(queryCondition.getShopId());
        orderEntity.setSenderName(queryCondition.getShopName());
        orderEntity.setProviderId(queryCondition.getProviderId());
        orderEntity.setUnsettledStatus(queryCondition.getUnsettledStatus());
        Page<OrderEntity> page = merchantOrderManager.postHouseQueryOrderPage(queryCondition.getPageNo(), queryCondition.getPageSize(),
                queryCondition.getExpectFinishTimeSort(), queryCondition.getOrderTimeSort(), orderEntity);
        List<MerchantOrderDTO> list = new LinkedList<>();
        if (page.hasContent()) {
            for (OrderEntity entity : page) {
                MerchantOrderDTO orderDTO = new MerchantOrderDTO();
                BeanUtils.copyProperties(entity, orderDTO);
                orderDTO.setAmount(entity.getPayAmount());
                list.add(orderDTO);
            }
        }
        PageDTO<MerchantOrderDTO> respPage = new PageDTO(queryCondition.getPageNo(), queryCondition.getPageSize(), page.getTotalElements(), list);
        return respPage;
    }

    @Override
    public OrderStatusStatsDTO findMerchantOrderCounts(Long providerId) {
        OrderStatusStatsDTO dto = merchantOrderManager.findMerchantOrderCounts(providerId);
        return dto;
    }
}
