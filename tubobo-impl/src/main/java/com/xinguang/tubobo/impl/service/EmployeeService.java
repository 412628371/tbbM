package com.xinguang.tubobo.impl.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.xinguang.tubobo.api.enums.EnumRespCode;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.hzmux.hzcms.modules.sys.service.SystemService;
import com.xinguang.tubobo.impl.entity.EmployeeEntity;
import com.xinguang.tubobo.impl.entity.StoreEntity;
import com.xinguang.tubobo.api.enums.EnumDelFlag;
import com.xinguang.tubobo.impl.dao.EmployeeDAO;
import com.xinguang.tubobo.impl.redisCache.RedisCache;

@Service
public class EmployeeService {
	
    @Autowired
    private EmployeeDAO employeeDAO;
    
    @Autowired 
    private EmployeeStatisticsService employeeStatisticsService;
    
    /**
     * 保存或更新employee
     * 更新employee中不为空的属性(belongStore.storeId,password,phone,name,employeeType)
     */
    public EnumRespCode saveOrUpdate(EmployeeEntity employee){
        UpdateOperations<EmployeeEntity> ops = employeeDAO.createUpdateOperations();

        if(StringUtils.isBlank(employee.getId())) {
            employee.setId(IdGen.uuid());
            ops.set("id", employee.getId());
            ops.set("createDate", new Date());
        } else {
            ops.set("updateDate",new Date());
        }
        if(StringUtils.isNotBlank(employee.getPassword())) {
            // encrypth the password
            employee.setPassword(SystemService.entryptPassword(employee.getPassword()));
            ops.set("password", employee.getPassword());
        }
        ops.set("phone", employee.getPhone());
        ops.set("belongStore", employee.getBelongStore());
        ops.set("name", employee.getName());
        ops.set("employeeType", employee.getEmployeeType());
        ops.set("delFlag", EnumDelFlag.NORMAL.getValue());

        employeeDAO.getDatastore().update(employeeDAO.createQuery().field("id").equal(employee.getId()), ops, true);

        RedisCache.removeEmployeeListCache(employee.getBelongStore().getStoreId());
        //校正员工统计数据
        employeeStatisticsService.correct(employee.getBelongStore().getStoreId());
        return EnumRespCode.SUCCESS;
    }

    public EmployeeEntity getByPhone(String phone) {
        Query<EmployeeEntity> q = employeeDAO.createQuery();
        q.field("phone").equal(phone);
        q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
        return employeeDAO.findOne(q);
    }

    public void deleteById(String id) {
    	Query<EmployeeEntity> q = employeeDAO.createQuery();
    	q.field("id").equal(id);
    	EmployeeEntity entity = employeeDAO.findOne(q);
    	
        UpdateOperations<EmployeeEntity> ops = employeeDAO.createUpdateOperations();
        ops.set("delFlag", EnumDelFlag.DELETE.getValue());
        employeeDAO.update(q, ops);

        RedisCache.removeEmployeeListCache(entity.getBelongStore().getStoreId());
        //校正员工统计数据
        employeeStatisticsService.correct(entity.getBelongStore().getStoreId());
    }

    public void deleteFromDB(String storeId) {
    	Query<EmployeeEntity> q = employeeDAO.createQuery();
    	q.field("belongStore").equal(new StoreEntity(storeId));
    	employeeDAO.deleteByQuery(q);
    	
    	RedisCache.removeEmployeeListCache(storeId);
    }

	public int updateTokenInfo(EmployeeEntity entity) {
		UpdateOperations<EmployeeEntity> ops = employeeDAO.createUpdateOperations();
        ops.set("tokenId", entity.getTokenId());
        ops.set("tokenIdExpiredTime", entity.getTokenIdExpiredTime());
        ops.set("signStore", entity.getSignStore());
        ops.set("updateDate", new Date());
        return employeeDAO.update(employeeDAO.createQuery()
        		.field("id").equal(entity.getId())
        		.field("delFlag").equal(EnumDelFlag.NORMAL.getValue()), ops).getUpdatedCount();
	}
	
	public int updatePassword(String id, String password) {
		return employeeDAO.updatePassword(id,password);
	}

	public EmployeeEntity getByTokenId(String tokenId) {
        Query<EmployeeEntity> q = employeeDAO.createQuery();
        q.field("tokenId").equal(tokenId);
        q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
        return employeeDAO.findOne(q);
	}
	
    public Page<EmployeeEntity> find(Page<EmployeeEntity> page,
            EmployeeEntity employeeEntity) {
        return employeeDAO.find(page, employeeEntity);
    }
    
    public EmployeeEntity get(String employeeId) {
        return employeeDAO.get(employeeId);
    }

    public List<EmployeeEntity> findEmployeeByStoreId(String storeId) {
        Query<EmployeeEntity> q = employeeDAO.createQuery();
    	q.field("belongStore").equal(new StoreEntity(storeId));
        q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
        return employeeDAO.find(q).asList();
    }

    public List<EmployeeEntity> findAllEmployeeByStoreId(String storeId) {
    	Query<EmployeeEntity> q = employeeDAO.createQuery();
    	q.field("belongStore").equal(new StoreEntity(storeId));
    	return employeeDAO.find(q).asList();
    }

    public List<EmployeeEntity> findChukuableList(String storeId) {
    	
    	Query<EmployeeEntity> q = employeeDAO.createQuery();
    	if (StringUtils.isNotBlank(storeId)) {
    		q.field("signStore").equal(new StoreEntity(storeId));
    	}
    	q.field("tokenIdExpiredTime").greaterThanOrEq(new Date());
    	q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
//        q.or(q.criteria("belongStore").equal(store),
//                q.criteria("signStore").equal(store).criteria("tokenIdExpiredTime").greaterThanOrEq(DateUtils.getDateStart(today))
//                );
    	return employeeDAO.find(q).asList();
    }

    public List<EmployeeEntity> findToExportExcel(EmployeeEntity entity) throws Exception{
        Query<EmployeeEntity> q = employeeDAO.createQuery();
        if(StringUtils.isNotBlank(entity.getName())) {
            q.field("name").contains(Pattern.quote(entity.getName()));
        }
        if(StringUtils.isNotBlank(entity.getPhone())) {
            q.field("phone").contains(Pattern.quote(entity.getPhone()));
        }
        if(StringUtils.isNotBlank(entity.getEmployeeType())) {
            q.field("employeeType").equal(entity.getEmployeeType());
        }
        if(null != entity.getBelongStore() && StringUtils.isNotBlank(entity.getBelongStore().getStoreId())) {
            q.field("belongStore").equal(entity.getBelongStore());
        }
        if(null != entity.getSignStore() && StringUtils.isNotBlank(entity.getSignStore().getStoreId())) {
            q.field("signStore").equal(entity.getSignStore());
        }
        if(StringUtils.isNotBlank(entity.getDelFlag()) && entity.getDelFlag().equals(EnumDelFlag.DELETE.getValue())) {
            q.field("delFlag").equal(EnumDelFlag.DELETE.getValue());
        } else if (StringUtils.isNotBlank(entity.getDelFlag()) && entity.getDelFlag().equals(EnumDelFlag.NORMAL.getValue())){
            q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
        }
        
        q.order("delFlag, -createDate");
        long count = q.countAll();
        if (count > 10000) {
			throw new Exception("失败：批量导出数量不允许超过10000条");
		}else if (count < 1) {
			throw new Exception("失败：符合条件的数据量为0");
		}
        return q.asList();
    }
    

	public long countOnDutyNum(String storeId,Date begin,Date end) {
		Query<EmployeeEntity> q = employeeDAO.createQuery();
		q.field("signStore").equal(new StoreEntity(storeId));
		q.field("tokenIdExpiredTime").greaterThanOrEq(begin);
		q.field("tokenIdExpiredTime").lessThanOrEq(end);
		q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
		return employeeDAO.find(q).countAll();
	}
}
