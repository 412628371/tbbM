package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Page;
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
    private MerchantOrderService merchantOrderService;
    @Autowired
    private MerchantInfoService merchantInfoService;

    /**
     * 查询商家详细信息
     * @param userId
     * @return
     */
    @Override
    public MerchantInfoDTO findByUserId(String userId) {
        MerchantInfoEntity merchant = merchantInfoService.findByUserId(userId);
        if (merchant != null){
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
    public boolean merchantStatusVerify(String userId, String merchantStatus, String updateBy) {
        return merchantInfoService.merchantStatusVerify(userId,merchantStatus,updateBy) > 0;
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
        Page<MerchantOrderEntity> page = merchantOrderService.findMerchantOrderPage(pageNo,pageSize,entity);
        List<MerchantOrderDTO> dtoList = new ArrayList<>();
        if (page!= null && page.getList() != null && page.getList().size() > 0){
            for (MerchantOrderEntity order : page.getList()){
                MerchantOrderDTO infoDto = new MerchantOrderDTO();
                BeanUtils.copyProperties(order,infoDto);
                dtoList.add(infoDto);
            }
        }
        return new PageDTO<MerchantOrderDTO>(page.getPageNo(),page.getPageSize(),page.getCount(),dtoList);
    }
}
