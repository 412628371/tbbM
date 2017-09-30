package com.xinguang.tubobo.impl.merchant.manager;

import com.xinguang.tubobo.impl.merchant.dao.MerchantDeliverFeeConfigDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantDeliverFeeConfigEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.repository.MerchantDeliverFeeConfigRepository;
import com.xinguang.tubobo.merchant.api.MerchantDeliverFeeConfigInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantDeliverFeeConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yanx on 2017/7/11.
 */
@Service
@Transactional
public class MerchantDeliverFeeConfigServiceImpl implements MerchantDeliverFeeConfigInterface {

    private static final Logger logger = LoggerFactory.getLogger(MerchantDeliverFeeConfigInterface.class);

    @Autowired
    private MerchantDeliverFeeConfigRepository feeConfigRepository;
    /**
     * 返回所有配送配置信息
     */
    @Override
    public List<MerchantDeliverFeeConfigDTO> findAll() {
        List<MerchantDeliverFeeConfigEntity> all = feeConfigRepository.findAllByDelFlagOrderByBeginDistance(MerchantInfoEntity.DEL_FLAG_NORMAL);
        return copyFeeEntityToDto(all);
    }
    /**
     * 根据区code查询具体区的费用
     */
    @Override
    public List<MerchantDeliverFeeConfigDTO> findFeeByAreaCode(String areaCode) {
        List<MerchantDeliverFeeConfigEntity> list = feeConfigRepository.findByAreaCodeAndDelFlagOrderByBeginDistance(areaCode, MerchantInfoEntity.DEL_FLAG_NORMAL);
        return copyFeeEntityToDto(list);
    }
    private List<MerchantDeliverFeeConfigDTO> copyFeeEntityToDto(List<MerchantDeliverFeeConfigEntity> list){
        ArrayList<MerchantDeliverFeeConfigDTO> returnList = new ArrayList<>();
        if (null!=list&&list.size()>0){
            for (MerchantDeliverFeeConfigEntity entity : list) {
                MerchantDeliverFeeConfigDTO dto = new MerchantDeliverFeeConfigDTO();
                BeanUtils.copyProperties(entity,dto);
                returnList.add(dto);
            }
        }
        return returnList;
    }

    @Override
    public void saveList(List<MerchantDeliverFeeConfigDTO> list) {

    }
    /**
     * 清空商家配送配置表,并保存新数据
     */
    @Override
    public void clearAndSaveList(List<MerchantDeliverFeeConfigDTO> list) {
        feeConfigRepository.deleteAll();
        ArrayList<MerchantDeliverFeeConfigEntity> saveList = new ArrayList<>();
        if (null!=list&&list.size()>0){
            for (MerchantDeliverFeeConfigDTO dto : list) {
                MerchantDeliverFeeConfigEntity merchantDeliverFeeConfigEntity = new MerchantDeliverFeeConfigEntity();
                BeanUtils.copyProperties(dto,merchantDeliverFeeConfigEntity);
                saveList.add(merchantDeliverFeeConfigEntity);
            }
        }
        feeConfigRepository.save(saveList);
    }

    @Override
    public void clearAndSaveListByAreaCode(List<MerchantDeliverFeeConfigDTO> list) {
        ArrayList<MerchantDeliverFeeConfigEntity> saveList = new ArrayList<>();
        if(null!=list&&list.size()>0){
            MerchantDeliverFeeConfigDTO deliverFeeConfigDTO = list.get(0);
            feeConfigRepository.deleteFeeByAreaCode(MerchantInfoEntity.DEL_FLAG_DELETE, deliverFeeConfigDTO.getAreaCode());
            for (MerchantDeliverFeeConfigDTO dto : list) {
                MerchantDeliverFeeConfigEntity merchantDeliverFeeConfigEntity = new MerchantDeliverFeeConfigEntity();
                BeanUtils.copyProperties(dto,merchantDeliverFeeConfigEntity);
                saveList.add(merchantDeliverFeeConfigEntity);
            }
        }
        feeConfigRepository.save(saveList);
    }
}
