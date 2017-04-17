package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.hzmux.hzcms.common.utils.excel.annotation.ExcelField;

/**
 * 投诉反馈
 */
@Entity(noClassnameStored=true)
public class FeedBackEntity {
	
	@Id
    private String id;

	@ExcelField(title="运单号", sort=40)
	@Indexed
    private String waybillNo; // 运单号
	private String storeId; // 门店
    
	@Reference(ignoreMissing=true,lazy=true)
	private WayBillSEntity waybill;
	
	@ExcelField(title="投诉内容", sort=150)
    private String content;//投诉内容
	
    private String result;//处理结果 0：未处理 1：已处理

    private String delFlag;
    @ExcelField(title="投诉时间", sort=140)
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
    
	public String getId() {
		return id;
	}
	
	public String getWaybillNo() {
		return waybillNo;
	}
	public WayBillSEntity getWaybill() {
		return waybill;
	}
	public String getContent() {
		return content;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	public void setWaybill(WayBillSEntity waybill) {
		this.waybill = waybill;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	// **************for excel export of waybill info
	@ExcelField(title="门店", sort=10)
	public String getStoreName() {
	    return (this.waybill!=null&&this.waybill.getBelongStore()!=null)?this.waybill.getBelongStore().getStoreName():"";
	}
	@ExcelField(title="快递公司", sort=20)
	public String getExpressCompany() {
	    return (this.waybill!=null&&this.waybill.getExpressCompany()!=null)?this.waybill.getExpressCompany().getCompanyName():"";
	}
	@ExcelField(title="运单状态", dictType="waybillS_status", sort=30)
	public Integer getWaybillStatus() {
	    return this.waybill!=null?this.waybill.getWaybillStatus():0;
	}
	@ExcelField(title="收件人手机", sort=50)
	public String getReceiverPhone() {
	    return this.waybill!=null?this.waybill.getReceiverPhone():"";
	}
	
	@ExcelField(title="取件码/集包号", sort=60)
	public String getAreaNum() {
	    return this.waybill!=null?this.waybill.getAreaNum():"";
	}
	@ExcelField(title="配送方式", dictType="dispatching_way", sort=70)
    public String getDispatchingWay() {
        return this.waybill!=null?this.waybill.getDispatchingWay():"";
    }
	@ExcelField(title="代收货款", sort=80)
    public Double getPayment() {
        return this.waybill!=null?this.waybill.getPayment():0;
    }
	@ExcelField(title="代收运费", sort=90)
	public Double getPayFreight() {
	    return this.waybill!=null?this.waybill.getPayFreight():0;
	}
	@ExcelField(title="入库人", sort=100)
    public String getDiatchingWay() {
        if(null != this.waybill && null != this.waybill.getInOperator()) {
            return this.waybill.getInOperator().getName();
        }else {
            return "";
        }
    }
	@ExcelField(title="入库时间", sort=110)
    public Date getInTime() {
        if(null != this.waybill && null != this.waybill.getInTime()) {
            return this.waybill.getInTime();
        }else {
            return null;
        }
    }
    
	@ExcelField(title="出库人" , sort=120)
    public String getOutOperator() {
        if(null != this.waybill && null != this.waybill.getOutOperator()) {
            return this.waybill.getOutOperator().getName();
        }else {
            return "";
        }
    }
	
	@ExcelField(title="出库时间", sort=130)
    public Date getOutTime() {
        if(null != this.waybill && null != this.waybill.getOutTime()) {
            return this.waybill.getOutTime();
        }else {
            return null;
        }
    }
    
}
