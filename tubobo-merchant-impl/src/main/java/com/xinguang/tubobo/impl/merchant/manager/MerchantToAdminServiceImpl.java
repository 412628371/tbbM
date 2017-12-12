package com.xinguang.tubobo.impl.merchant.manager;

import com.hzmux.hzcms.common.utils.AliOss;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.taskcenter.api.common.enums.PostOrderUnsettledStatusEnum;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.entity.OrderEntity;
import com.xinguang.tubobo.impl.merchant.mq.RmqNoticeProducer;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantToAdminServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantInfoDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderDTO;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumAuthentication;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.postHouse.api.dto.ProviderInfoDTO;
import com.xinguang.tubobo.postHouse.api.service.BindToMerchantServiceInterface;
import com.xinguang.tubobo.postHouse.api.service.ProviderServiceInterface;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MerchantToAdminServiceImpl implements MerchantToAdminServiceInterface {

    @Autowired
    private MerchantOrderManager merchantOrderManager;
    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private OrderService orderService;
    @Autowired
    RmqNoticeProducer rmqNoticeProducer;

    @Autowired
    private BindToMerchantServiceInterface bindToMerchantServiceInterface;
    @Autowired
    private ProviderServiceInterface providerServiceInterface;

    /**
     * 查询商家详细信息
     * @param userId
     * @return
     */
    @Override
    public MerchantInfoDTO findByUserId(String userId) {
        if (StringUtils.isBlank(userId)) return null;
        MerchantInfoEntity merchant = merchantInfoService.findByUserId(userId);
        if (merchant != null){
            AliOss.generateMerchantSignedUrl(merchant);
            MerchantInfoDTO dto = new MerchantInfoDTO();
            BeanUtils.copyProperties(merchant,dto);
            //查询服务商信息
            if(null != merchant.getProviderId()){
                ProviderInfoDTO providerInfoDTO = providerServiceInterface.getProviderInfoById(merchant.getProviderId());
                dto.setProviderName(providerInfoDTO.getName());
            }
            return dto;
        }
        return null;
    }

    @Override
    public List<MerchantInfoDTO> findMerchantInfoList(MerchantInfoDTO dto) {
        MerchantInfoEntity entity = new MerchantInfoEntity();
        if (dto != null){
            BeanUtils.copyProperties(dto,entity);
        }
        List<MerchantInfoEntity> list = merchantInfoService.findMerchantInfoList(entity);
        List<MerchantInfoDTO> dtoList = new ArrayList<>();
        for (MerchantInfoEntity merchant : list){
            MerchantInfoDTO infoDto = new MerchantInfoDTO();
            BeanUtils.copyProperties(merchant,infoDto);
            dtoList.add(infoDto);
        }
        return dtoList;
    }

    /**
     * 查询商家信息分页
     * @param pageNo
     * @param pageSize
     * @param dto
     * @return
     */
    @Override
    public PageDTO<MerchantInfoDTO> findMerchantInfoPage(int pageNo, int pageSize, MerchantInfoDTO dto) {
        MerchantInfoEntity entity = new MerchantInfoEntity();
        if (dto != null){
            BeanUtils.copyProperties(dto,entity);
        }
        //市的code前四字母与区的前四个字母一直，将市的code前四个截取用于模糊查询
        if(StringUtils.isNotBlank(dto.getCityCode())){
            entity.setAddressAdCode(dto.getCityCode().substring(0,4));
        }
        Page<MerchantInfoEntity> page = merchantInfoService.findMerchantInfoPage(pageNo,pageSize,entity);
        List<MerchantInfoDTO> dtoList = new ArrayList<>();
        if (page.hasContent()){
            for (MerchantInfoEntity merchant : page){
                MerchantInfoDTO infoDto = new MerchantInfoDTO();
                BeanUtils.copyProperties(merchant,infoDto);
                dtoList.add(infoDto);
            }
        }
        return new PageDTO<MerchantInfoDTO>(pageNo,pageSize,page.getTotalElements(),dtoList);
    }

    /**
     * 商家状态审核
     * @param userId
     * @param merchantStatus
     * @param updateBy
     * @return
     */
    @Override
    public boolean merchantStatusVerify(String userId, String merchantStatus, String updateBy,String identifyType,String reason,Long merTypeId) {
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity )
            return false;
        int i = merchantInfoService.merchantStatusVerify(userId,merchantStatus,updateBy,identifyType,reason,merTypeId);
        if (i > 0){
            //消息放入报表mq
            if("CONSIGNOR".equals(identifyType)){
//                tuboboReportDateMqHelp.goodOwnerStatusVerify(userId,merchantStatus);
            }else{
                if (EnumAuthentication.FAIL.getValue().equals(merchantStatus)){
                    rmqNoticeProducer.sendAuditNotice(userId,false,reason);
                    if (null != infoEntity.getProviderId()){
                        merchantInfoService.unbindProvider(userId,infoEntity.getProviderId());
                        //删除驿站商家绑定信息
                        bindToMerchantServiceInterface.deleteMerchantBindInfo(userId);
                    }
                }else if (EnumAuthentication.SUCCESS.getValue().equals(merchantStatus)){
                    rmqNoticeProducer.sendAuditNotice(userId,true,null);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 查询商家订单分页
     * @param pageNo
     * @param pageSize
     * @param dto
     * @return
     */
    @Override
    public PageDTO<MerchantOrderDTO> findMerchantOrderPage(int pageNo, int pageSize, MerchantOrderDTO dto) {
        MerchantOrderEntity entity = new MerchantOrderEntity();
        if (dto != null){
            BeanUtils.copyProperties(dto,entity);
        }
        if(StringUtils.isNotBlank(dto.getSenderAdcode())&& dto.getSenderAdcode().length()>4){
            entity.setSenderAdcode(dto.getSenderAdcode().substring(0,4));
        }
       Page<OrderEntity> page = merchantOrderManager.adminQueryOrderPage(pageNo,pageSize,entity);
        List<MerchantOrderDTO> dtoList = new ArrayList<>();
        if (page.hasContent()){
            for (OrderEntity order : page){
                MerchantOrderDTO infoDto = new MerchantOrderDTO();
                BeanUtils.copyProperties(order,infoDto);
                infoDto.setPayAmount(order.getPayAmount()==null?0:(int)(order.getPayAmount()*100));
                infoDto.setDeliveryFee(order.getDeliveryFee()==null?0:(int)(order.getDeliveryFee()*100));
                infoDto.setTipFee(order.getTipFee()==null?0:(int)(order.getTipFee()*100));
                if (EnumMerchantOrderStatus.DELIVERYING.getValue().equals(order.getOrderStatus())&& PostOrderUnsettledStatusEnum.ING.getValue().equals(order.getUnsettledStatus())){
                    infoDto.setOrderStatus(EnumMerchantOrderStatus.UNDELIVERED.getValue());
                }else if (EnumMerchantOrderStatus.FINISH.getValue().equals(order.getOrderStatus())&&PostOrderUnsettledStatusEnum.FINISH.getValue().equals(order.getUnsettledStatus())){
                    infoDto.setOrderStatus(EnumMerchantOrderStatus.CONFIRM.getValue());
                }
                dtoList.add(infoDto);
            }
        }
        return new PageDTO<MerchantOrderDTO>(pageNo,pageSize,page.getTotalElements(),dtoList);
    }

    /**
     * 查询商家订单详情
     * @param orderNo
     * @return
     */
    @Override
    public MerchantOrderDTO findMerchantOrderDetail(String orderNo) {
        if (StringUtils.isBlank(orderNo)) return null;
        MerchantOrderEntity order = orderService.findByOrderNo(orderNo);
        if (order != null){
            MerchantOrderDTO dto = new MerchantOrderDTO();
            BeanUtils.copyProperties(order,dto);
            dto.setPayAmount(order.getPayAmount()==null?0:(int)(order.getPayAmount()*100));
            dto.setDeliveryFee(order.getDeliveryFee()==null?0:(int)(order.getDeliveryFee()*100));
            dto.setTipFee(order.getTipFee()==null?0:(int)(order.getTipFee()*100));
            dto.setPeekOverFee(order.getPeekOverFee()==null?0:(int)(order.getPeekOverFee()*100));
            dto.setWeatherOverFee(order.getWeatherOverFee()==null?0:(int)(order.getWeatherOverFee()*100));
            return dto;
        }
        return null;
    }

    @Override
    public boolean processTaskCreateException(String orderNo) {
        return merchantOrderManager.dealGrabOvertimeOrders(orderNo,new Date(),true);
    }
}
