package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

/**
 * 运单追踪记录实体
 */
@Entity(noClassnameStored=true)
public class ExpressTrackEntity {
	
    @Id
    private String id;
    @Indexed
    private String dataId;
    
    private String desc;//描述

    private Date time;//时间

    @Reference(ignoreMissing=true,lazy=true)
    private StoreEntity store;// 门店
    @Reference(ignoreMissing=true,lazy=true)
    private EmployeeEntity operator;// 操作人
    
	public String getId() {
		return id;
	}

	public String getDataId() {
		return dataId;
	}

	public String getDesc() {
		return desc;
	}

	public Date getTime() {
		return time;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public EmployeeEntity getOperator() {
		return operator;
	}

	public void setOperator(EmployeeEntity operator) {
		this.operator = operator;
	}

	public StoreEntity getStore() {
		return store;
	}

	public void setStore(StoreEntity store) {
		this.store = store;
	}
    
}
