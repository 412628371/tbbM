package com.xinguang.tubobo.impl.dao;

import java.util.Date;
import java.util.regex.Pattern;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.EmployeeEntity;
import com.xinguang.tubobo.impl.entity.ExpressCompanyEntity;
import com.xinguang.tubobo.impl.entity.UploadWayBillEntity;
import com.xinguang.tubobo.impl.entity.WayBillSEntity;
import com.xinguang.tubobo.api.enums.EnumDeliveryMethod;
import com.xinguang.tubobo.api.enums.EnumDeliveryType;
import com.xinguang.tubobo.api.enums.EnumWaybillStatus;

@Repository
public class WayBillSDAO extends BasicDAO<WayBillSEntity, String> {

    @Autowired
    protected WayBillSDAO(Datastore ds) {
        super(ds);
    }

    public Page<WayBillSEntity> find(Page<WayBillSEntity> page, WayBillSEntity wayBillEntity) {
        Query<WayBillSEntity> q = createQuery();
        if(null != wayBillEntity) {
        	//如果运单号不为空 就不加时间限制
            if(StringUtils.isNotBlank(wayBillEntity.getWaybillNo())) {
                q.field("waybillNo").contains(Pattern.quote(wayBillEntity.getWaybillNo()));
            }else {
                if(null != wayBillEntity.getCreateDate()) {
                    q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(wayBillEntity.getCreateDate()));
                }
                if(null != wayBillEntity.getUpdateDate()) {
                	q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(wayBillEntity.getUpdateDate()));
                }
			}
            if(StringUtils.isNotBlank(wayBillEntity.getReceiverPhone())) {
                q.field("receiverPhone").contains(Pattern.quote(wayBillEntity.getReceiverPhone()));
            }
            if(StringUtils.isNotBlank(wayBillEntity.getAreaNum())) {
                q.field("areaNum").equal(wayBillEntity.getAreaNum());
            }
            if(StringUtils.isNotBlank(wayBillEntity.getDispatchingWay())) {
            	q.field("dispatchingWay").equal(wayBillEntity.getDispatchingWay());
            }
            if(null != wayBillEntity.getWaybillStatus()) {
                q.field("waybillStatus").equal(wayBillEntity.getWaybillStatus());
            }
            if(null != wayBillEntity.getBelongStore() && StringUtils.isNotBlank(wayBillEntity.getBelongStore().getStoreId())) {
                q.field("belongStore").equal(wayBillEntity.getBelongStore());
            }
            if(null != wayBillEntity.getExpressCompany() && StringUtils.isNotBlank(wayBillEntity.getExpressCompany().getCompanyId())) {
            	q.field("expressCompany").equal(wayBillEntity.getExpressCompany());
            }
            if(null != wayBillEntity.getInOperator() && StringUtils.isNotBlank(wayBillEntity.getInOperator().getId())) {
                q.field("inOperator").equal(wayBillEntity.getInOperator());
            }
            if(null != wayBillEntity.getOutOperator() && StringUtils.isNotBlank(wayBillEntity.getOutOperator().getId())) {
            	q.field("outOperator").equal(wayBillEntity.getOutOperator());
            }
        }
//        q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
        q.order("-createDate");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }

    public int updateByUploadInfo(String normalUserId,UploadWayBillEntity info) {
        UpdateOperations<WayBillSEntity> ops = createUpdateOperations();

        ops.set("normalUserId", normalUserId);
        ops.set("receiverPhone", info.getReceiverPhone());
        ops.set("areaNum", info.getAreaNum());
        
//    	ops.unset("signMan");
    	ops.unset("signOperator");
    	ops.unset("signTime");
    	
        if (StringUtils.isNotBlank(info.getExpressCompanyId())) {
        	ops.set("expressCompany", new ExpressCompanyEntity(info.getExpressCompanyId()));
		}
        if (null != info.getStore()) {
        	ops.set("belongStore", info.getStore());
        }
        if (info.isDelayFlag()) {
        	//滞留件更新快件状态、滞留标记位+1
        	ops.set("waybillStatus", EnumWaybillStatus.DELAY.getValue());
        	ops.inc("remainStatus",1);
        }else {
            //处理从配送件滞留件处理过来的运单 add on 2017-03-20
            if (info.getFromRemainFlag()){
                ops.inc("remainStatus",1);
            }
        	//新到件更新快件状态
        	ops.set("waybillStatus", EnumWaybillStatus.IN.getValue());
		}
        if (info.isSendFlag()) {
        	ops.set("outTime", new Date());
        	ops.set("outOperator", info.getEmployee());
        	ops.set("waybillStatus", EnumWaybillStatus.OUT.getValue());
		} else {
			ops.unset("outTime");
	    	ops.unset("outOperator");
		}
        if (info.isSmsFlag()) {
			// 自提件
        	ops.set("dispatchingWay",EnumDeliveryMethod.ZIQU.getValue());
        	ops.set("pickupType",EnumDeliveryType.ZITI.getValue());
		} else {
			// 配送件
			ops.set("dispatchingWay",EnumDeliveryMethod.PEISONG.getValue());
			ops.set("pickupType",EnumDeliveryType.PEISONG.getValue());
		}
        if (StringUtils.isNotBlank(info.getPayFreight())) {
        	ops.set("payFreight", Double.valueOf(info.getPayFreight()));
        }
        if (StringUtils.isNotBlank(info.getPayment())) {
        	ops.set("payment", Double.valueOf(info.getPayment()));
        }
        ops.set("updateBy", info.getEmployee().getId());
        ops.set("updateDate", new Date());
        return update(createQuery()
        		.field("waybillNo").equal(info.getWaybillNo()), ops).getUpdatedCount();
    }
    
    public int chukuToEmployee(EmployeeEntity employee, String waybillId,String updateBy) {
        Query<WayBillSEntity> q = createQuery();
        q.field("id").equal(waybillId);
        q.field("waybillStatus").equal(EnumWaybillStatus.IN.getValue());

        UpdateOperations<WayBillSEntity> ops = createUpdateOperations();
        ops.set("outTime", new Date());
        ops.set("outOperator", employee);
        ops.set("waybillStatus", EnumWaybillStatus.OUT.getValue());
        ops.set("updateBy", updateBy);
        ops.set("updateDate", new Date());
        
        UpdateResults UpdateResults =  update(q, ops);
        return UpdateResults.getUpdatedCount();
    }

}
