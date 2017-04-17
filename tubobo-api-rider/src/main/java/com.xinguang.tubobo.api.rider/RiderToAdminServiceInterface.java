package com.xinguang.tubobo.api.rider;

import com.xinguang.tubobo.api.baseDTO.PageDTO;
import com.xinguang.tubobo.api.rider.dto.RiderInfoDTO;

/**
 * 骑手端提供给后台的dubbo接口
 */
public interface RiderToAdminServiceInterface {

    /**
     * 查询骑手详细信息
     * @param userId
     * @return
     */
    public RiderInfoDTO findByUserId(String userId);

    /**
     * 查询骑手信息分页
     * @param pageNo
     * @param pageSize
     * @param dto
     * @return
     */
    public PageDTO<RiderInfoDTO> findRiderInfoPage(int pageNo, int pageSize, RiderInfoDTO dto);

    /**
     * 骑手状态审核
     * @param userId
     * @param riderStatus
     * @param updateBy
     * @return
     */
    public boolean riderStatusVerify(String userId,String riderStatus,String updateBy);

}
