package com.xinguang.tubobo.impl.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 员工信息统计 存储实体
 */
@Entity(noClassnameStored=true)
public class EmployeeStatisticsEntity {

	@Id
    private String storeId;
    private String storeName;

    private int totalNum;
    private int deleteNum;

    private int storeKeeperNum;//店长
    private int storeEmployeeNum;//雇员
    private int storePartTimeJobNum;//兼职
    
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public int getDeleteNum() {
		return deleteNum;
	}
	public void setDeleteNum(int deleteNum) {
		this.deleteNum = deleteNum;
	}
	public int getStoreKeeperNum() {
		return storeKeeperNum;
	}
	public void setStoreKeeperNum(int storeKeeperNum) {
		this.storeKeeperNum = storeKeeperNum;
	}
	public int getStoreEmployeeNum() {
		return storeEmployeeNum;
	}
	public void setStoreEmployeeNum(int storeEmployeeNum) {
		this.storeEmployeeNum = storeEmployeeNum;
	}
	public int getStorePartTimeJobNum() {
		return storePartTimeJobNum;
	}
	public void setStorePartTimeJobNum(int storePartTimeJobNum) {
		this.storePartTimeJobNum = storePartTimeJobNum;
	}
	
}
