package com.xinguang.tubobo.impl.rider.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.api.baseDTO.PageDTO;
import com.xinguang.tubobo.api.enums.EnumAuthentication;
import com.xinguang.tubobo.api.rider.RiderToAdminServiceInterface;
import com.xinguang.tubobo.api.rider.dto.RiderInfoDTO;
import com.xinguang.tubobo.impl.rider.entity.RiderInfoEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RiderToAdminServiceImpl implements RiderToAdminServiceInterface {

    @Autowired
    private RiderInfoService riderInfoService;

    /**
     * 查询骑手详细信息
     * @param userId
     * @return
     */
    @Override
    public RiderInfoDTO findByUserId(String userId) {
        RiderInfoEntity rider = riderInfoService.findByUserId(userId);
        if (rider != null){
            RiderInfoDTO dto = new RiderInfoDTO();
            BeanUtils.copyProperties(rider,dto);
            return dto;
        }
        return null;
    }

    /**
     * 查询骑手信息分页
     * @param pageNo
     * @param pageSize
     * @param dto
     * @return
     */
    @Override
    public PageDTO<RiderInfoDTO> findRiderInfoPage(int pageNo, int pageSize, RiderInfoDTO dto) {
        RiderInfoEntity entity = new RiderInfoEntity();
        if (dto != null){
            BeanUtils.copyProperties(dto,entity);
        }
        Page<RiderInfoEntity> page = riderInfoService.findRiderInfoPage(pageNo,pageSize,entity);
        List<RiderInfoDTO> dtoList = new ArrayList<>();
        if (page!= null && page.getList() != null && page.getList().size() > 0){
            for (RiderInfoEntity rider : page.getList()){
                RiderInfoDTO infoDto = new RiderInfoDTO();
                BeanUtils.copyProperties(rider,infoDto);
                dtoList.add(infoDto);
            }
        }
        return new PageDTO<RiderInfoDTO>(page.getPageNo(),page.getPageSize(),page.getCount(),dtoList);
    }

    /**
     * 骑手状态审核
     * @param userId
     * @param riderStatus
     * @param updateBy
     * @return
     */
    @Override
    public boolean riderStatusVerify(String userId, String riderStatus, String updateBy) {
        return riderInfoService.riderStatusVerify(userId,riderStatus,updateBy) > 0;
    }
}
