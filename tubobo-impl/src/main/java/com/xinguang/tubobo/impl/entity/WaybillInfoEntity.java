package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * 面单实体
 */
@Entity(noClassnameStored=true)
public class WaybillInfoEntity {
	
    @Id
    private String waybillNo;//运单号
    
    @Reference(ignoreMissing=true,lazy=true)
    private StoreEntity store; // 门店
    
    @Reference(ignoreMissing=true,lazy=true)
    private ExpressCompanyEntity expressCompany; // 快递公司

    private String price;//单价
    private String source;//来源
    private String status;//面单状态

    private String addManId;//添加人
    private String addManName;
    private Date addDate;

    private String updateManId;//变更人
    private String updateManName;
    private Date updateDate;
    
    private String receiveManId;//领用人
    private String receiveManName;
    private Date receiveDate;
    
    private String createBy;
    private Date createDate;
    
    //以下为查询字段，仅供查询使用
    private String waybillNoRangeBegin;//添加起始面单号
    private String waybillNoRangeEnd;//添加截至面单号
    private Date receiveEndDate;
    private Date updateEndDate;
    private Date addEndDate;

	public StoreEntity getStore() {
		return store;
	}

	public void setStore(StoreEntity store) {
		this.store = store;
	}

	public ExpressCompanyEntity getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(ExpressCompanyEntity expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddManId() {
		return addManId;
	}

	public void setAddManId(String addManId) {
		this.addManId = addManId;
	}

	public String getAddManName() {
		return addManName;
	}

	public void setAddManName(String addManName) {
		this.addManName = addManName;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getUpdateManId() {
		return updateManId;
	}

	public void setUpdateManId(String updateManId) {
		this.updateManId = updateManId;
	}

	public String getUpdateManName() {
		return updateManName;
	}

	public void setUpdateManName(String updateManName) {
		this.updateManName = updateManName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getReceiveManId() {
		return receiveManId;
	}

	public void setReceiveManId(String receiveManId) {
		this.receiveManId = receiveManId;
	}

	public String getReceiveManName() {
		return receiveManName;
	}

	public void setReceiveManName(String receiveManName) {
		this.receiveManName = receiveManName;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getWaybillNoRangeBegin() {
		return waybillNoRangeBegin;
	}

	public void setWaybillNoRangeBegin(String waybillNoRangeBegin) {
		this.waybillNoRangeBegin = waybillNoRangeBegin;
	}

	public String getWaybillNoRangeEnd() {
		return waybillNoRangeEnd;
	}

	public void setWaybillNoRangeEnd(String waybillNoRangeEnd) {
		this.waybillNoRangeEnd = waybillNoRangeEnd;
	}

	public Date getAddEndDate() {
		return addEndDate;
	}

	public void setAddEndDate(Date addEndDate) {
		this.addEndDate = addEndDate;
	}

	public Date getUpdateEndDate() {
		return updateEndDate;
	}

	public void setUpdateEndDate(Date updateEndDate) {
		this.updateEndDate = updateEndDate;
	}

	public Date getReceiveEndDate() {
		return receiveEndDate;
	}

	public void setReceiveEndDate(Date receiveEndDate) {
		this.receiveEndDate = receiveEndDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
    
}
