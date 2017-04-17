package com.xinguang.tubobo.impl.service;

import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.hzmux.hzcms.modules.sys.entity.Area;
import com.hzmux.hzcms.modules.sys.service.AreaService;
import com.xinguang.tubobo.api.enums.EnumDelFlag;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.dao.AddressInfoDAO;
import com.xinguang.tubobo.impl.entity.AddressInfoEntity;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AddressInfoService {
	
    @Autowired
    private AddressInfoDAO addressInfoDAO;
    @Autowired
    private AreaService areaService;

    public AddressInfoEntity get(String id){
    	return addressInfoDAO.get(id);
    }

    public List<AddressInfoEntity> findByUserId(String normalUserId){
    	Query<AddressInfoEntity> q = addressInfoDAO.createQuery();
    	q.field("normalUserId").equal(normalUserId);
    	q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
    	return addressInfoDAO.find(q).asList();
    }
    
    public int deleteAddressInfo(String userId, List<String> ids){
    	UpdateOperations<AddressInfoEntity> ops = addressInfoDAO.createUpdateOperations();
    	ops.set("normalUserId", userId);
    	ops.set("delFlag", EnumDelFlag.DELETE.getValue());
    	ops.set("updateDate", new Date());
    	return addressInfoDAO.update(addressInfoDAO.createQuery()
    			.field("id").in(ids), ops).getUpdatedCount();
    }
    
    public int updateAddressInfo(AddressInfoEntity entity){
    	UpdateOperations<AddressInfoEntity> ops = addressInfoDAO.createUpdateOperations();
		ops.set("name", entity.getName());
		ops.set("phone", entity.getPhone());
		ops.set("pcdCode", entity.getPcdCode());
		ops.set("pcdName", entity.getPcdName());
		ops.set("detailAddress", entity.getDetailAddress());
		ops.set("isDefault", entity.getIsDefault());
		ops.set("delFlag", EnumDelFlag.NORMAL.getValue());
		ops.set("updateDate", new Date());
        return addressInfoDAO.update(addressInfoDAO.createQuery()
        		.field("id").equal(entity.getId()), ops).getUpdatedCount();
    }
    
    public int setIsDelfaultToNot(String normalUserId){
    	UpdateOperations<AddressInfoEntity> ops = addressInfoDAO.createUpdateOperations();
		ops.set("isDefault", false);
		ops.set("updateDate", new Date());
    	return addressInfoDAO.update(addressInfoDAO.createQuery()
    			.field("normalUserId").equal(normalUserId)
    			.field("delFlag").equal(EnumDelFlag.NORMAL.getValue()), ops).getUpdatedCount();
    }
    
    /**
     * 地址Id为空时：新增一条地址信息
     * 地址Id不为空且存在时：修改该条信息
     * 地址Id不为空且不存在：新增一条地址信息，并重新生成地址Id
     * 考虑只传最后一级code，需自动填充前级code
     */
    public String addressEdit(String userId,AddressInfoEntity req){
    	if (StringUtils.isBlank(userId) || StringUtils.isBlank(req.getName()) || StringUtils.isBlank(req.getPhone()) ||
    			StringUtils.isBlank(req.getPcdCode()) || StringUtils.isBlank(req.getPcdName()) ||
    			StringUtils.isBlank(req.getDetailAddress())) {
    		return EnumRespCode.PARAMS_ERROR.getValue();
		}
    	AddressInfoEntity entity = new AddressInfoEntity();
    	entity.setId(req.getId());
    	entity.setNormalUserId(userId);
    	entity.setName(req.getName());
    	entity.setPhone(req.getPhone());
    	entity.setPcdCode(req.getPcdCode());
    	entity.setPcdName(req.getPcdName());
    	entity.setDetailAddress(req.getDetailAddress());
    	entity.setIsDefault(req.getIsDefault());
    	//如果是设置为默认地址 则先把同类型的其他地址置为非默认
    	if (req.getIsDefault()) {
			setIsDelfaultToNot(userId);
		}
    	if (StringUtils.isBlank(entity.getId())) {
        	entity.setId(IdGen.uuid());
        	entity.setCreateDate(new Date());
        	entity.setDelFlag(EnumDelFlag.NORMAL.getValue());
        	addressInfoDAO.save(entity);
		}else {
			updateAddressInfo(entity);
		}
    	return EnumRespCode.SUCCESS.getValue();
    }

    @SuppressWarnings("unused")
	private AddressInfoEntity fillAddressInfoByLastCode(AddressInfoEntity entity,String code){
    	Area area = areaService.get(code);
    	if (area != null && !"1".equals(area.getId())) {//"1":中国
    		entity.setPcdName(area.getName() + " " + entity.getPcdName());
    		entity.setPcdCode(area.getCode() + " " + entity.getPcdCode());
    		fillAddressInfoByLastCode(entity, area.getParent().getCode());
		}
    	return entity;
    }
}
