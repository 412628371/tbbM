package com.xinguang.tubobo.merchant.api;

import com.xinguang.tubobo.merchant.api.dto.MerchantInfoDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderDTO;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;

/**
 * 商家端提供给后台的dubbo接口
 */
public interface MerchantToAdminServiceInterface {

    /**
     * 查询商家详细信息
     * @param userId
     * @return
     */
    public MerchantInfoDTO findByUserId(String userId);

    /**
     * 查询商家信息分页
     * @param pageNo
     * @param pageSize
     * @param dto
     * @return
     */
    public PageDTO<MerchantInfoDTO> findMerchantInfoPage(int pageNo, int pageSize, MerchantInfoDTO dto);

    /**
     * 商家状态审核
     * @param userId
     * @param merchantStatus
     * @param updateBy
     * @return
     */
    public boolean merchantStatusVerify(String userId, String merchantStatus, String updateBy,String identifyType,String reason,Long merTypeId);


    /**
     * 查询商家订单分页
     * @param pageNo
     * @param pageSize
     * @param dto
     * @return
     */
    public PageDTO<MerchantOrderDTO> findMerchantOrderPage(int pageNo, int pageSize, MerchantOrderDTO dto);

    /**
     * 查询商家订单详情
     * @param orderNo
     * @return
     */
    public MerchantOrderDTO findMerchantOrderDetail(String orderNo);
}
