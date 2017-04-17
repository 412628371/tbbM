package com.xinguang.tubobo.impl.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.hzmux.hzcms.modules.sys.entity.Office;
import com.xinguang.tubobo.impl.entity.ExpressCompanyEntity;
import com.xinguang.tubobo.impl.entity.StoreEntity;
import com.xinguang.tubobo.impl.dao.StoreDAO;
import com.xinguang.tubobo.impl.redisCache.RedisCache;

@Service
public class StoreService {
    @Autowired
    private StoreDAO storeDAO;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ExpressCompanyService expressCompanyService;

    public StoreEntity get(String storeId) {
        return storeDAO.get(storeId);
    }
    
    public List<StoreEntity> findByAreaCode(String areaCode) {
    	Query<StoreEntity> q = storeDAO.createQuery();
        if(StringUtils.isNotBlank(areaCode)) {
            q.field("areaCode").equal(areaCode);
        }
        return q.asList();
    }
    
    public List<StoreEntity> findByParanteId(String parantId){
    	Query<StoreEntity> q = storeDAO.createQuery();
        q.field("parentIds").contains(parantId);
        return q.asList();
	}
    
    public Page<StoreEntity> find(Page<StoreEntity> page, StoreEntity entity) {
        Query<StoreEntity> q = storeDAO.createQuery();
        if(StringUtils.isNotBlank(entity.getStoreId())) {
            q.field("storeId").equal(entity.getStoreId());
        }
        if(StringUtils.isNotBlank(entity.getStoreName())) {
        	q.field("storeName").contains(Pattern.quote(entity.getStoreName()));
        }
        if(StringUtils.isNotBlank(entity.getStorePhone())) {
            q.field("storePhone").equal(entity.getStorePhone());
        }
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
    
    public List<StoreEntity> findAll() {
        return storeDAO.find().asList();
    }
    
    public void saveOrUpdate(Office office) {
        UpdateOperations<StoreEntity> upo = storeDAO.createUpdateOperations();
        
        upo.set("parentId", office.getParent().getId());
        upo.set("parentIds", office.getParentIds());
        upo.set("areaId", office.getArea().getId());
        upo.set("areaCode", office.getArea().getCode());
        
        upo.set("storeCode", office.getCode());
        upo.set("storeName", office.getName());
        upo.set("storePhone", office.getPhone());
        upo.set("storeType", office.getType());
        upo.set("storeAddressStandard", Office.getFullAddress(office));
        upo.set("storeAddressDetail", office.getAddress());
//        upo.set("riderAble", Constants.OPEN_NO);
        upo.set("updateDate", new Date());
        storeDAO.getDatastore().update(storeDAO.createQuery().field("storeId").equal(office.getId()), upo, true);
        
        RedisCache.removeStoreListCache();
    }

    public int updateStoreInfo(StoreEntity store,String updateBy) {
        UpdateOperations<StoreEntity> ops = storeDAO.createUpdateOperations();
        ops.set("storePhone", store.getStorePhone());
        ops.set("storeAddressDetail", store.getStoreAddressDetail());
        ops.set("updateDate", new Date());
        ops.set("updateBy",updateBy);
        return storeDAO.update(storeDAO.createQuery().field("storeId").equal(store.getStoreId()), ops).getUpdatedCount();
    }
    
    public void delete(String storeId) {
        storeDAO.deleteById(storeId);
        RedisCache.removeStoreListCache();

        employeeService.deleteFromDB(storeId);
    }
    
    /**
     * 查询已经开通骑士配送的门店
     */
//    public List<StoreEntity> findStoreByRiderFunctionOpened() {
//    	return storeDAO.find(storeDAO.createQuery().field("riderAble").equal(Constants.OPEN_YES)).asList();
//    }

    /**
     * 查询已经开通有赞商城的门店
     */
//    public List<StoreEntity> findStoreByYouzanShopOpened() {
//    	return storeDAO.find(storeDAO.createQuery().field("youzanShopAble").equal(Constants.OPEN_YES)).asList();
//    }
    
    public int updateRiderAble(String storeId, String riderAble, String updateBy) {
    	UpdateOperations<StoreEntity> ops = storeDAO.createUpdateOperations();
    	if(StringUtils.isNotBlank(updateBy)) {
    		ops.set("updateBy", updateBy);
    	}
    	ops.set("riderAble", riderAble);
    	ops.set("updateDate", new Date());
    	return storeDAO.update(storeDAO.createQuery().field("storeId").equal(storeId), ops).getUpdatedCount();
    }

    public int updateYouzanShopAble(String storeId, String youzanShopAble, String updateBy) {
    	UpdateOperations<StoreEntity> ops = storeDAO.createUpdateOperations();
    	if(StringUtils.isNotBlank(updateBy)) {
    		ops.set("updateBy", updateBy);
    	}
    	ops.set("youzanShopAble", youzanShopAble);
    	ops.set("updateDate", new Date());
    	return storeDAO.update(storeDAO.createQuery().field("storeId").equal(storeId), ops).getUpdatedCount();
    }
    
    public int openExpressCompany(String storeId, String companyId, String opened) {
        if(StringUtils.isBlank(storeId)
                || StringUtils.isBlank(companyId)
                || !("0".equals(opened)||"1".equals(opened))) {
            return -1;
        } else if(!expressCompanyService.exist(companyId) || !storeDAO.exists("storeId", storeId)) {
            return -2;
        } else {
            UpdateOperations<StoreEntity> ops = storeDAO.createUpdateOperations();
            
            if("1".equals(opened)) {// 启用
                ops.add("expressCompanyList", new ExpressCompanyEntity(companyId));
            } else {
                ops.removeAll("expressCompanyList", new ExpressCompanyEntity(companyId));
            }
            
            UpdateResults urs = storeDAO.update(storeDAO.createQuery().field("storeId").equal(storeId), ops);
            return urs.getUpdatedCount();
        }
    }

}
