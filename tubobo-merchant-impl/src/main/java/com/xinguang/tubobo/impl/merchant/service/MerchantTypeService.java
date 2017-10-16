package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantTypeEntity;
import com.xinguang.tubobo.impl.merchant.repository.MerchantTypeRepository;
import com.xinguang.tubobo.merchant.api.MerchantTypeInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantTypeDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangxb on 2017/10/13.
 */
@Service
@Transactional(readOnly = true)
public class MerchantTypeService implements MerchantTypeInterface {
    @Autowired
    private MerchantTypeRepository merchantTypeRepository;

    @Override
    @Cacheable(value= RedisCache.MERCHANT,key="'merchantType_all'")
    public List<MerchantTypeDTO> findAll() {
        List<MerchantTypeEntity> merchantTypeEntities = merchantTypeRepository.findAllByDelFlagOrderByName(MerchantTypeEntity.DEL_FLAG_NORMAL);
        List<MerchantTypeDTO> merchantTypeDTOS = new ArrayList<>();

        if (merchantTypeEntities!=null&&merchantTypeEntities.size()>0){
            for (MerchantTypeEntity merchantTypeEntity : merchantTypeEntities) {
                MerchantTypeDTO merchantTypeDTO = new MerchantTypeDTO();
                BeanUtils.copyProperties(merchantTypeEntity, merchantTypeDTO);
                merchantTypeDTOS.add(merchantTypeDTO);
            }
        }

        return merchantTypeDTOS;
    }

    @Override
    @Cacheable(value= RedisCache.MERCHANT,key="'merchantType_'+#id")
    public MerchantTypeDTO findById(Long id) {

       MerchantTypeEntity merchantTypeEntity = merchantTypeRepository.findByIdAndDelFlag(id, MerchantTypeEntity.DEL_FLAG_NORMAL);
       MerchantTypeDTO merchantTypeDTO = new MerchantTypeDTO();

       if (merchantTypeEntity !=null){
           BeanUtils.copyProperties(merchantTypeEntity, merchantTypeDTO);
       }
        return merchantTypeDTO;
    }
    @Override
    @Cacheable(value= RedisCache.MERCHANT,key="'merchantType_'+#name")
    public MerchantTypeDTO findByName(String name) {

        MerchantTypeEntity merchantTypeEntity = merchantTypeRepository.findByNameAndDelFlag(name, MerchantTypeEntity.DEL_FLAG_NORMAL);
        MerchantTypeDTO merchantTypeDTO = new MerchantTypeDTO();

        if (merchantTypeEntity !=null){
            BeanUtils.copyProperties(merchantTypeEntity, merchantTypeDTO);
        }
        return merchantTypeDTO;
    }

    @Override
    @CacheEvict(value= RedisCache.MERCHANT,key="'merchantType_*'")
    @Transactional(readOnly = false)
    public Boolean update(MerchantTypeDTO merchantTypeDTO) {

        MerchantTypeEntity merchantTypeEntity  = new MerchantTypeEntity();
        if (merchantTypeEntity !=null){
            BeanUtils.copyProperties(merchantTypeDTO, merchantTypeEntity);
        }
        MerchantTypeEntity result;
        result = merchantTypeRepository.save(merchantTypeEntity);
        if (result!=null){
            return true;
        }
        return false;
    }

    @Override
    @CacheEvict(value= RedisCache.MERCHANT,key="'merchantType_*'")
    @Transactional(readOnly = false)
    public Boolean delete(Long id) {
        MerchantTypeEntity merchantTypeEntity  = merchantTypeRepository.findByIdAndDelFlag(id, MerchantTypeEntity.DEL_FLAG_NORMAL);
        if (merchantTypeEntity!=null){
            merchantTypeEntity.setDelFlag(MerchantTypeEntity.DEL_FLAG_DELETE);
        }
        merchantTypeRepository.save(merchantTypeEntity);
        return true;
    }

    @Override
    @CacheEvict(value= RedisCache.MERCHANT,key="'merchantType_*'")
    @Transactional(readOnly = false)
    public Boolean save(MerchantTypeDTO merchantTypeDTO) {
        if(merchantTypeDTO == null){
            return false;
        }
        Long id = merchantTypeDTO.getId();
        String name = merchantTypeDTO.getName();
        if (id != null){
            MerchantTypeEntity entity = new MerchantTypeEntity();
            BeanUtils.copyProperties(merchantTypeDTO, entity);
            merchantTypeRepository.save(entity);
            return true;
        }else {
            if (StringUtils.isNotBlank(name)){
                MerchantTypeEntity merchantTypeEntity = merchantTypeRepository.findByNameAndDelFlag(name, MerchantTypeEntity.DEL_FLAG_NORMAL);
                if (merchantTypeEntity==null){
                    merchantTypeEntity = new MerchantTypeEntity();
                    BeanUtils.copyProperties(merchantTypeDTO,merchantTypeEntity);
                    merchantTypeRepository.save(merchantTypeEntity);
                    return true;
                }
            }
            return false;
        }
    }
}
