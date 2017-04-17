package com.xinguang.tubobo.impl.service;

import java.util.List;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.EmployeeEntity;
import com.xinguang.tubobo.impl.entity.EmployeeStatisticsEntity;
import com.xinguang.tubobo.api.enums.EnumDelFlag;
import com.xinguang.tubobo.api.enums.EnumEmployeeType;
import com.xinguang.tubobo.impl.dao.EmployeeStatisticsDAO;
import com.xinguang.tubobo.impl.redisCache.RedisCache;

@Service
public class EmployeeStatisticsService {

    @Autowired
    private EmployeeStatisticsDAO employeeStatisticsDAO;
    @Autowired
    private EmployeeService employeeService;
    
    private void saveOrUpdate(EmployeeStatisticsEntity entity){
        UpdateOperations<EmployeeStatisticsEntity> ops = employeeStatisticsDAO.createUpdateOperations();
        ops.set("storeName", entity.getStoreName());
        ops.set("totalNum", entity.getTotalNum());
        ops.set("deleteNum", entity.getDeleteNum());
        ops.set("storeKeeperNum", entity.getStoreKeeperNum());
        ops.set("storeEmployeeNum", entity.getStoreEmployeeNum());
        ops.set("storePartTimeJobNum", entity.getStorePartTimeJobNum());
        employeeStatisticsDAO.getDatastore().update(employeeStatisticsDAO.createQuery().field("storeId").equal(entity.getStoreId()), ops, true);
    }

    public void correct(String storeId){
    	EmployeeStatisticsEntity entity = new EmployeeStatisticsEntity();
    	entity.setStoreId(storeId);
    	entity.setStoreName(RedisCache.queryStoreName(storeId));
    	
    	List<EmployeeEntity> employeeList = employeeService.findAllEmployeeByStoreId(storeId);
    	if (employeeList != null && employeeList.size() > 0) {
    		for (EmployeeEntity employee : employeeList) {
    			if (employee.getDelFlag().equals(EnumDelFlag.DELETE.getValue())) {
    				entity.setDeleteNum(entity.getDeleteNum()+1);
    			}else {
    				entity.setTotalNum(entity.getTotalNum()+1);
    				if (employee.getEmployeeType().equals(EnumEmployeeType.KEEPER.getValue())) {
    					entity.setStoreKeeperNum(entity.getStoreKeeperNum()+1);
    				}else if (employee.getEmployeeType().equals(EnumEmployeeType.EMPLOYEE.getValue())) {
    					entity.setStoreEmployeeNum(entity.getStoreEmployeeNum()+1);
    				}else if (employee.getEmployeeType().equals(EnumEmployeeType.PARTTIMEJOB.getValue())) {
    					entity.setStorePartTimeJobNum(entity.getStorePartTimeJobNum()+1);
    				}
    			}
    		}
		}
    	saveOrUpdate(entity);
    }
    
    public void deleteAll(){
    	Query<EmployeeStatisticsEntity> q = employeeStatisticsDAO.createQuery();
    	employeeStatisticsDAO.deleteByQuery(q);
    }
    
    public Page<EmployeeStatisticsEntity> find(Page<EmployeeStatisticsEntity> page,EmployeeStatisticsEntity entity) {
        Query<EmployeeStatisticsEntity> q = employeeStatisticsDAO.createQuery();
        if(StringUtils.isNotBlank(entity.getStoreId())) {
            q.field("storeId").equal(entity.getStoreId());
        }
        q.order("-totalNum");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
    
}
