package com.xinguang.tubobo.impl.merchant.manager;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.AliOss;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import com.xinguang.tubobo.impl.merchant.mq.TuboboReportDateMqHelp;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantToAdminServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantInfoDTO;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderDTO;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private TuboboReportDateMqHelp tuboboReportDateMqHelp;

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
            return dto;
        }
        return null;
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
        Page<MerchantInfoEntity> page = merchantInfoService.findMerchantInfoPage(pageNo,pageSize,entity);
        List<MerchantInfoDTO> dtoList = new ArrayList<>();
        if (page!= null && page.getList() != null && page.getList().size() > 0){
            for (MerchantInfoEntity merchant : page.getList()){
                MerchantInfoDTO infoDto = new MerchantInfoDTO();
                BeanUtils.copyProperties(merchant,infoDto);
                dtoList.add(infoDto);
            }
        }
        return new PageDTO<MerchantInfoDTO>(page.getPageNo(),page.getPageSize(),page.getCount(),dtoList);
    }

    /**
     * 商家状态审核
     * @param userId
     * @param merchantStatus
     * @param updateBy
     * @return
     */
    @Override
    public boolean merchantStatusVerify(String userId, String merchantStatus, String updateBy,String identifyType) {
        int i = merchantInfoService.merchantStatusVerify(userId,merchantStatus,updateBy,identifyType);
        if (i > 0){
            //消息放入报表mq
            if("CONSIGNOR".equals(identifyType)){
                tuboboReportDateMqHelp.goodOwnerStatusVerify(userId,merchantStatus);
            }else{
                tuboboReportDateMqHelp.merchantStatusVerify(userId,merchantStatus);
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
        Page<MerchantOrderEntity> page = merchantOrderManager.adminQueryOrderPage(pageNo,pageSize,entity);
        List<MerchantOrderDTO> dtoList = new ArrayList<>();
        if (page!= null && page.getList() != null && page.getList().size() > 0){
            for (MerchantOrderEntity order : page.getList()){
                MerchantOrderDTO infoDto = new MerchantOrderDTO();
                BeanUtils.copyProperties(order,infoDto);
                infoDto.setPayAmount(order.getPayAmount()==null?0:(int)(order.getPayAmount()*100));
                infoDto.setDeliveryFee(order.getDeliveryFee()==null?0:(int)(order.getDeliveryFee()*100));
                infoDto.setTipFee(order.getTipFee()==null?0:(int)(order.getTipFee()*100));
                dtoList.add(infoDto);
            }
        }
        return new PageDTO<MerchantOrderDTO>(page.getPageNo(),page.getPageSize(),page.getCount(),dtoList);
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
            return dto;
        }
        return null;
    }
}
