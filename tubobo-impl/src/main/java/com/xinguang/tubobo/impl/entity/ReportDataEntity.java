package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import javax.persistence.Transient;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

/**
 * 报表数据
 */
@Entity(noClassnameStored=true)
public class ReportDataEntity {
	
	public static final String TOTAL_STORE_ID = "totalStore_id";
	
    @Id
    private String id;
    
    private String storeId;
    private String storeName;
    
    private String date;//报表日期 2016-10-20
    
    @Indexed
    private Date createDate;//数据生成时间
    
    private long employeeNum;//当天派件人数

    /** 对应WaybillSEntity**/
    private long paijianNum;//派件总数(入库时间为当天)

    private long zitiNum;//自提件(入库时间为当天)
    private long peisongNum;//配送件(入库时间为当天)
    
    private long inNum;//状态为入库的快件数(入库时间为当天)
    private long outNum;//状态为出库的快件数(入库时间为当天)
    private long delayNum;//状态为滞留的快件数(入库时间为当天)
    private long signNum;//状态为签收的快件数(入库时间为当天)
    private long returnBackNum;//状态为退回的快件数(入库时间为当天)
    
    private long chukuNum;//出库总数(出库时间为当天的快件总数)
    
    private long chukuAndSignNum;//出库签收数(出库时间为当天且已签收的快件数)

    /** 对应WaybillREntity**/
    private long shoujianNum;//收件总数(入库时间为当天)
    
//    private long feedBackNum;//投诉

    private long yesterdayShoujianNum;//昨日收件
    private long yesterdayPaijianNum;//昨日派件
    private long yesterdaySignNum;//昨日签收
    private long yesterdayDelayNum;//昨日滞留
    private long yesterdayReturnBackNum;//昨日退回

    @Transient
    private Date updateDate;

	@Override
	public String toString() {
		return "[ 报表日期=" + date + ", 门店=" + storeName
				+ ", 派件人数=" + employeeNum + ", 派件总数=" + paijianNum
				+ ", 自提件=" + zitiNum + ", 配送件=" + peisongNum
				+ ", 滞留件=" + delayNum + ", 签收=" + signNum + ", 退回件="
				+ returnBackNum + ", 出库配送=" + chukuNum
				+ ", 出库签收=" + chukuAndSignNum + ", 收件总数=" + shoujianNum + " ]";
	}

	public String getId() {
		return id;
	}

	public String getStoreId() {
		return storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public String getDate() {
		return date;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public long getEmployeeNum() {
		return employeeNum;
	}

	public long getPaijianNum() {
		return paijianNum;
	}

	public long getZitiNum() {
		return zitiNum;
	}

	public long getPeisongNum() {
		return peisongNum;
	}

	public long getInNum() {
		return inNum;
	}

	public long getOutNum() {
		return outNum;
	}

	public long getDelayNum() {
		return delayNum;
	}

	public long getSignNum() {
		return signNum;
	}

	public long getReturnBackNum() {
		return returnBackNum;
	}

	public long getChukuNum() {
		return chukuNum;
	}

	public long getChukuAndSignNum() {
		return chukuAndSignNum;
	}

	public long getShoujianNum() {
		return shoujianNum;
	}

	public long getYesterdayShoujianNum() {
		return yesterdayShoujianNum;
	}

	public long getYesterdayPaijianNum() {
		return yesterdayPaijianNum;
	}

	public long getYesterdaySignNum() {
		return yesterdaySignNum;
	}

	public long getYesterdayDelayNum() {
		return yesterdayDelayNum;
	}

	public long getYesterdayReturnBackNum() {
		return yesterdayReturnBackNum;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setEmployeeNum(long employeeNum) {
		this.employeeNum = employeeNum;
	}

	public void setPaijianNum(long paijianNum) {
		this.paijianNum = paijianNum;
	}

	public void setZitiNum(long zitiNum) {
		this.zitiNum = zitiNum;
	}

	public void setPeisongNum(long peisongNum) {
		this.peisongNum = peisongNum;
	}

	public void setInNum(long inNum) {
		this.inNum = inNum;
	}

	public void setOutNum(long outNum) {
		this.outNum = outNum;
	}

	public void setDelayNum(long delayNum) {
		this.delayNum = delayNum;
	}

	public void setSignNum(long signNum) {
		this.signNum = signNum;
	}

	public void setReturnBackNum(long returnBackNum) {
		this.returnBackNum = returnBackNum;
	}

	public void setChukuNum(long chukuNum) {
		this.chukuNum = chukuNum;
	}

	public void setChukuAndSignNum(long chukuAndSignNum) {
		this.chukuAndSignNum = chukuAndSignNum;
	}

	public void setShoujianNum(long shoujianNum) {
		this.shoujianNum = shoujianNum;
	}

	public void setYesterdayShoujianNum(long yesterdayShoujianNum) {
		this.yesterdayShoujianNum = yesterdayShoujianNum;
	}

	public void setYesterdayPaijianNum(long yesterdayPaijianNum) {
		this.yesterdayPaijianNum = yesterdayPaijianNum;
	}

	public void setYesterdaySignNum(long yesterdaySignNum) {
		this.yesterdaySignNum = yesterdaySignNum;
	}

	public void setYesterdayDelayNum(long yesterdayDelayNum) {
		this.yesterdayDelayNum = yesterdayDelayNum;
	}

	public void setYesterdayReturnBackNum(long yesterdayReturnBackNum) {
		this.yesterdayReturnBackNum = yesterdayReturnBackNum;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
