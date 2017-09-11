package com.xinguang.tubobo.impl.merchant.manager;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.api.enums.EnumAuthentication;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantToPostHouseServiceInterface;
import com.xinguang.tubobo.merchant.api.condition.MerchantInfoQueryCondition;
import com.xinguang.tubobo.merchant.api.dto.MerchantInfoDTO;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantPostExceptionCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xuqinghua on 2017/9/1.
 */
public class MerchantToPostHouseServiceImpl implements MerchantToPostHouseServiceInterface {

    @Autowired private MerchantInfoService merchantInfoService;
    @Override
    public EnumMerchantPostExceptionCode bindProvider(String userId, Long providerId, String providerName) {
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            return EnumMerchantPostExceptionCode.SHOP_NOT_EXIST;
        }
        if (!EnumAuthentication.SUCCESS.getValue().equals(infoEntity.getMerchantStatus()) ){
            return EnumMerchantPostExceptionCode.SHOP_NOT_AUTHED;
        }
        if (null != infoEntity.getProviderId()){
            return EnumMerchantPostExceptionCode.SHOP_ALREADY_BOUND;
        }
        boolean result = merchantInfoService.bindProvider(userId,providerId,providerName);
        if (result)
            return EnumMerchantPostExceptionCode.SUCCESS;
        return EnumMerchantPostExceptionCode.FAIL;
    }

    @Override
    public PageDTO<MerchantInfoDTO> findMerchantList(MerchantInfoQueryCondition queryCondition) {
        MerchantInfoEntity infoEntity = new MerchantInfoEntity();
        infoEntity.setPhone(queryCondition.getShopPhone());
        infoEntity.setRealName(queryCondition.getShopName());
        infoEntity.setUserId(queryCondition.getShopId());
        infoEntity.setProviderId(queryCondition.getProviderId());
        Page<MerchantInfoEntity> page = merchantInfoService.findMerchantInfoPage(queryCondition.getPageNo(),queryCondition.getPageSize(),infoEntity);
        List<MerchantInfoDTO> list = new LinkedList<>();
        if (null != page && page.getList()!=null&& page.getList().size()>0){
            for (MerchantInfoEntity entity:page.getList()){
                MerchantInfoDTO infoDTO = new MerchantInfoDTO();
                BeanUtils.copyProperties(entity,infoDTO);
                list.add(infoDTO);
            }
        }
        PageDTO<MerchantInfoDTO> respPage = new PageDTO(queryCondition.getPageNo(),queryCondition.getPageSize(),page.getCount(),list);
        return respPage;
    }
}
