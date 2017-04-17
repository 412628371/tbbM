package com.xinguang.tubobo.impl.service;

import java.util.Arrays;
import java.util.Date;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.hzmux.hzcms.modules.sys.entity.User;
import com.xinguang.tubobo.impl.entity.EmployeeEntity;
import com.xinguang.tubobo.impl.entity.StoreEntity;
import com.xinguang.tubobo.impl.entity.WaybillInfoEntity;
import com.xinguang.tubobo.api.enums.EnumWaybillInfoStatus;
import com.xinguang.tubobo.impl.dao.WaybillInfoDAO;

@Service
public class WaybillInfoService {
	
    @Autowired
    private WaybillInfoDAO waybillInfoDAO;

    public int save(WaybillInfoEntity entity,String createBy) throws Exception{
    	if (waybillInfoDAO.exists("waybillNo", entity.getWaybillNo())) {
			throw new Exception("面单号:" + entity.getWaybillNo() + "已存在");
		}
		entity.setStatus(EnumWaybillInfoStatus.INIT.getValue());
		entity.setCreateBy(createBy);
		entity.setCreateDate(new Date());
    	waybillInfoDAO.save(entity);
    	return 1;
    }
    
//    public int update(WaybillInfoEntity entity,String updateBy){
//		UpdateOperations<WaybillInfoEntity> ops = waybillInfoDAO.createUpdateOperations();
//		
//		ops.set("updateBy", updateBy);
//		ops.set("updateDate", new Date());
//		
//		return waybillInfoDAO.update(waybillInfoDAO.createQuery()
//				.field("waybillNo").equal(entity.getWaybillNo())
//				, ops).getUpdatedCount();
//    }
    
    public Page<WaybillInfoEntity> find(Page<WaybillInfoEntity> page, WaybillInfoEntity entity) {
        Query<WaybillInfoEntity> q = waybillInfoDAO.createQuery();
        if(null != entity) {
        	
        	if(StringUtils.isNotBlank(entity.getWaybillNoRangeBegin()) && StringUtils.isNotBlank(entity.getWaybillNoRangeEnd())) {
        		q.field("waybillNo").greaterThanOrEq(entity.getWaybillNoRangeBegin());
        		q.field("waybillNo").lessThanOrEq(entity.getWaybillNoRangeEnd());
        	}else if(StringUtils.isNotBlank(entity.getWaybillNoRangeBegin()) && StringUtils.isBlank(entity.getWaybillNoRangeEnd())) {
        		q.field("waybillNo").equal(entity.getWaybillNoRangeBegin());
        	}
        	
        	if(null != entity.getStore() && StringUtils.isNotBlank(entity.getStore().getStoreId())) {
        		q.field("store").equal(entity.getStore());
        	}
        	if(null != entity.getExpressCompany() && StringUtils.isNotBlank(entity.getExpressCompany().getCompanyId())) {
        		q.field("expressCompany").equal(entity.getExpressCompany());
        	}
            if(StringUtils.isNotBlank(entity.getSource())) {
            	q.field("source").equal(entity.getSource());
            }
            if(StringUtils.isNotBlank(entity.getStatus())) {
            	q.field("status").equal(entity.getStatus());
            }
            if(StringUtils.isNotBlank(entity.getAddManId())) {
            	q.field("addManId").equal(entity.getAddManId());
            }
            if(StringUtils.isNotBlank(entity.getUpdateManId())) {
            	q.field("updateManId").equal(entity.getUpdateManId());
            }
            if(StringUtils.isNotBlank(entity.getReceiveManId())) {
            	q.field("receiveManId").equal(entity.getReceiveManId());
            }
            if(null != entity.getAddDate()) {
                q.field("addDate").greaterThanOrEq(DateUtils.getDateStart(entity.getAddDate()));
            }
            if(null != entity.getAddEndDate()) {
            	q.field("addDate").lessThanOrEq(DateUtils.getDateEnd(entity.getAddEndDate()));
            }
            if(null != entity.getUpdateDate()) {
            	q.field("updateDate").greaterThanOrEq(DateUtils.getDateStart(entity.getUpdateDate()));
            }
            if(null != entity.getUpdateEndDate()) {
            	q.field("updateDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateEndDate()));
            }
            if(null != entity.getReceiveDate()) {
            	q.field("receiveDate").greaterThanOrEq(DateUtils.getDateStart(entity.getReceiveDate()));
            }
            if(null != entity.getReceiveEndDate()) {
            	q.field("receiveDate").lessThanOrEq(DateUtils.getDateEnd(entity.getReceiveEndDate()));
            }
        }
        q.order("-addDate");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
    
    public WaybillInfoEntity get(String id){
    	return waybillInfoDAO.get(id);
    }

    public WaybillInfoEntity findByWaybillNo(String waybillNo){
    	Query<WaybillInfoEntity> q = waybillInfoDAO.createQuery();
        q.field("waybillNo").equal(waybillNo);
    	return waybillInfoDAO.findOne(q);
    }
    
    public void deleteById(String id){
        waybillInfoDAO.deleteById(id);
    }

    /**
     * 使用运单
     */
    public boolean useWaybillNoSuc(String waybillNo,String updateManId,String updateManName){
    	UpdateOperations<WaybillInfoEntity> ops = waybillInfoDAO.createUpdateOperations();
    	ops.set("status", EnumWaybillInfoStatus.USED.getValue());
    	ops.set("updateManId", updateManId);
		ops.set("updateManName", updateManName);
		ops.set("updateDate", new Date());
    	return waybillInfoDAO.update(waybillInfoDAO.createQuery()
    			.field("waybillNo").equal(waybillNo)
    			.field("status").in(Arrays.asList(EnumWaybillInfoStatus.ALLOT.getValue(),EnumWaybillInfoStatus.USABLE.getValue()))
    			, ops).getUpdatedCount() > 0;
    }
    
    /**
     * 批量操作面单
     * 批量处理面单状态筛选条件根据action参数自己添加
     * 超出限制条数则抛出异常
     * @param WaybillInfoEntity 筛选条件
     * @param action            批量操作动作类型标识
     * @param allotStore        调拨门店
     * @param employee          领用雇员
     * @param user              操作人
     */
    public int batchUpdateStatus(WaybillInfoEntity entity,String action,StoreEntity allotStore,EmployeeEntity employee,User user) throws Exception{
        Query<WaybillInfoEntity> q = waybillInfoDAO.createQuery();
        	
        if (action.equals(EnumWaybillInfoStatus.SOLD.getValue())) {
        	q.field("status").equal(EnumWaybillInfoStatus.INIT.getValue());
		}else if (action.equals(EnumWaybillInfoStatus.USABLE.getValue())) {
			if (employee == null) {
				throw new Exception("领用雇员信息错误");
			}
			q.field("status").in(Arrays.asList(EnumWaybillInfoStatus.INIT.getValue(),EnumWaybillInfoStatus.ALLOT.getValue()));
		}else if (action.equals(EnumWaybillInfoStatus.ALLOT.getValue())) {
			if (allotStore == null) {
				throw new Exception("调拨门店信息错误");
			}
			q.field("status").equal(EnumWaybillInfoStatus.INIT.getValue());
		}else if (action.equals(EnumWaybillInfoStatus.ABANDON.getValue())) {
			q.field("status").in(Arrays.asList(EnumWaybillInfoStatus.INIT.getValue(),EnumWaybillInfoStatus.ALLOT.getValue(),EnumWaybillInfoStatus.USABLE.getValue()));
		}else {
			throw new Exception("批量操作参数action错误");
		}
        
    	if(StringUtils.isNotBlank(entity.getWaybillNoRangeBegin()) && StringUtils.isNotBlank(entity.getWaybillNoRangeEnd())) {
    		q.field("waybillNo").greaterThanOrEq(entity.getWaybillNoRangeBegin());
    		q.field("waybillNo").lessThanOrEq(entity.getWaybillNoRangeEnd());
    	}else if(StringUtils.isNotBlank(entity.getWaybillNoRangeBegin()) && StringUtils.isBlank(entity.getWaybillNoRangeEnd())) {
    		q.field("waybillNo").equal(entity.getWaybillNoRangeBegin());
    	}
    	if(user.getOffice().isStore()) {
    		q.field("store").equal(new StoreEntity(user.getOffice().getId()));
        }else if(null != entity.getStore() && StringUtils.isNotBlank(entity.getStore().getStoreId())) {
        	q.field("store").equal(entity.getStore());
    	}
    	if(null != entity.getExpressCompany() && StringUtils.isNotBlank(entity.getExpressCompany().getCompanyId())) {
    		q.field("expressCompany").equal(entity.getExpressCompany());
    	}
        if(StringUtils.isNotBlank(entity.getSource())) {
        	q.field("source").equal(entity.getSource());
        }
        if(StringUtils.isNotBlank(entity.getAddManId())) {
        	q.field("addManId").equal(entity.getAddManId());
        }
        if(StringUtils.isNotBlank(entity.getUpdateManId())) {
        	q.field("updateManId").equal(entity.getUpdateManId());
        }
        if(StringUtils.isNotBlank(entity.getReceiveManId())) {
        	q.field("receiveManId").equal(entity.getReceiveManId());
        }
        if(null != entity.getAddDate()) {
            q.field("addDate").greaterThanOrEq(DateUtils.getDateStart(entity.getAddDate()));
        }
        if(null != entity.getAddEndDate()) {
        	q.field("addDate").lessThanOrEq(DateUtils.getDateEnd(entity.getAddEndDate()));
        }
        if(null != entity.getUpdateDate()) {
        	q.field("updateDate").greaterThanOrEq(DateUtils.getDateStart(entity.getUpdateDate()));
        }
        if(null != entity.getUpdateEndDate()) {
        	q.field("updateDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateEndDate()));
        }
        if(null != entity.getReceiveDate()) {
        	q.field("receiveDate").greaterThanOrEq(DateUtils.getDateStart(entity.getReceiveDate()));
        }
        if(null != entity.getReceiveEndDate()) {
        	q.field("receiveDate").lessThanOrEq(DateUtils.getDateEnd(entity.getReceiveEndDate()));
        }
        long count = q.countAll();
        if (count > 10000) {
			throw new Exception("失败：批量处理数量不允许超过10000条");
		}else if (count < 1) {
			throw new Exception("失败：符合条件的面单数量为0");
		}

        UpdateOperations<WaybillInfoEntity> ops = waybillInfoDAO.createUpdateOperations();
        if (action.equals(EnumWaybillInfoStatus.SOLD.getValue())) {
		}else if (action.equals(EnumWaybillInfoStatus.USABLE.getValue())) {
			ops.set("receiveManId", employee.getId());
        	ops.set("receiveManName", employee.getName());
        	ops.set("receiveDate", new Date());
		}else if (action.equals(EnumWaybillInfoStatus.ALLOT.getValue())) {
			ops.set("store",allotStore);
		}else if (action.equals(EnumWaybillInfoStatus.ABANDON.getValue())) {
			
		}
        ops.set("status", action);
		ops.set("updateManId", user.getId());
		ops.set("updateManName", user.getName());
		ops.set("updateDate", new Date());
    	return waybillInfoDAO.update(q, ops).getUpdatedCount();
    }
}
