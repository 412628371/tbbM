package com.xinguang.tubobo.impl.merchant.manager;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.entity.MerchantDeliverFeeConfigEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.repository.MerchantDeliverFeeConfigRepository;
import com.xinguang.tubobo.merchant.api.MerchantDeliverFeeConfigInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantDeliverFeeConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
     * 查询不同类型的起送费全部数据
     * @param
     * @return
     */
    @Override
    public List<MerchantDeliverFeeConfigDTO> findByOrderType(String orderType){
        List<MerchantDeliverFeeConfigEntity> all = feeConfigRepository.findAllByDelFlagAndOrderTypeOrderByBeginDistance(MerchantInfoEntity.DEL_FLAG_NORMAL,orderType);
        return copyFeeEntityToDto(all);
    }
    /**
     * 多类型查询条件下  查询数据
     * @param
     * @return
     */
    @Override
    public  List<MerchantDeliverFeeConfigDTO> queryAllByCondition(MerchantDeliverFeeConfigDTO merchantDeliverFeeConfigDTO){
        MerchantDeliverFeeConfigEntity entity = new MerchantDeliverFeeConfigEntity();
        BeanUtils.copyProperties(merchantDeliverFeeConfigDTO,entity);
        List<MerchantDeliverFeeConfigEntity> merchantDeliverFeeConfigEntities = feeConfigRepository.findAll(where(entity));

        List<MerchantDeliverFeeConfigDTO> merchantDeliverFeeConfigDTOS = new ArrayList<>();
        if (merchantDeliverFeeConfigEntities!=null&&merchantDeliverFeeConfigEntities.size()>0){
            for (MerchantDeliverFeeConfigEntity merchantDeliverFeeConfigEntity : merchantDeliverFeeConfigEntities) {
                MerchantDeliverFeeConfigDTO merFeeConfig = new MerchantDeliverFeeConfigDTO();
                BeanUtils.copyProperties(merchantDeliverFeeConfigEntity,merFeeConfig);
                merchantDeliverFeeConfigDTOS.add(merFeeConfig);
            }
        }
        return merchantDeliverFeeConfigDTOS;
    }

    private Specification<MerchantDeliverFeeConfigEntity> where(final MerchantDeliverFeeConfigEntity entity){
        return new Specification<MerchantDeliverFeeConfigEntity>() {
            @Override
            public Predicate toPredicate(Root<MerchantDeliverFeeConfigEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                list.add(criteriaBuilder.equal(root.get("delFlag").as(String.class), MerchantDeliverFeeConfigEntity.DEL_FLAG_NORMAL));
                if(StringUtils.isNotBlank(entity.getCityCode())){
                    list.add(criteriaBuilder.equal(root.get("cityCode").as(String.class), entity.getCityCode()));
                }
                if(null != entity.getOrderType()){
                    list.add(criteriaBuilder.equal(root.get("orderType").as(String.class), entity.getOrderType()));
                }
                if(null != entity.getTemId()){
                    list.add(criteriaBuilder.equal(root.get("temId").as(Long.class), entity.getTemId()));
                }
                return criteriaQuery.where(list.toArray(new Predicate[list.size()])).getRestriction();
            }
        };
    }

    /**
     * 根据区code查询具体区的费用
     */
    @Override
    public List<MerchantDeliverFeeConfigDTO> findFeeByAreaCode(String areaCode) {
        List<MerchantDeliverFeeConfigEntity> list = feeConfigRepository.findByAreaCodeAndDelFlagOrderByBeginDistance(areaCode, MerchantInfoEntity.DEL_FLAG_NORMAL);
        return copyFeeEntityToDto(list);
    }
    /**
     * 根据区code和orderType查询具体区的费用
     */
    @Override
    public List<MerchantDeliverFeeConfigDTO> findFeeByAreaCodeAndOrderTypeAndTemId(String areaCode,String orderType,Long temId){
        List<MerchantDeliverFeeConfigEntity> list = feeConfigRepository.findByAreaCodeAndDelFlagAndOrderTypeAndTemIdOrderByBeginDistance(areaCode, MerchantInfoEntity.DEL_FLAG_NORMAL,orderType,temId);
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
    public void clearAndSaveListByAreaCodeAndOrderTypeAndTemId(List<MerchantDeliverFeeConfigDTO> list) {
        ArrayList<MerchantDeliverFeeConfigEntity> saveList = new ArrayList<>();
        if(null!=list&&list.size()>0){
            MerchantDeliverFeeConfigDTO deliverFeeConfigDTO = list.get(0);
            feeConfigRepository.deleteFeeByAreaCodeAndOrderType(MerchantInfoEntity.DEL_FLAG_DELETE, deliverFeeConfigDTO.getAreaCode(),deliverFeeConfigDTO.getOrderType(),deliverFeeConfigDTO.getTemId());
            for (MerchantDeliverFeeConfigDTO dto : list) {
                MerchantDeliverFeeConfigEntity merchantDeliverFeeConfigEntity = new MerchantDeliverFeeConfigEntity();
                BeanUtils.copyProperties(dto,merchantDeliverFeeConfigEntity);
                saveList.add(merchantDeliverFeeConfigEntity);
            }
        }
        feeConfigRepository.save(saveList);
    }
}
