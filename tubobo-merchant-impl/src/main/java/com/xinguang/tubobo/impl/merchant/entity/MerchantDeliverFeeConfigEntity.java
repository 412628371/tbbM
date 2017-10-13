/**
 * 订单实体类;
 */
package com.xinguang.tubobo.impl.merchant.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tubobo_merchant_deliver_fee_config")
@DynamicInsert @DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchantDeliverFeeConfigEntity extends BaseMerchantEntity {

	private static final long serialVersionUID = 1L;

	private Double initDistance;
	private Double initFee;
	private Double beginDistance;
	private Double endDistance;
	private Double perFee;
	private String areaCode;
	private String cityCode;
	private String provinceCode;
	private String orderType;
	private Long temId;			//定价模板id

	public Long getTemId() {
		return temId;
	}

	public void setTemId(Long temId) {
		this.temId = temId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Double getInitDistance() {
		return initDistance;
	}

	public void setInitDistance(Double initDistance) {
		this.initDistance = initDistance;
	}

	public Double getInitFee() {
		return initFee;
	}

	public void setInitFee(Double initFee) {
		this.initFee = initFee;
	}

	public Double getBeginDistance() {
		return beginDistance;
	}

	public void setBeginDistance(Double beginDistance) {
		this.beginDistance = beginDistance;
	}

	public Double getEndDistance() {
		return endDistance;
	}

	public void setEndDistance(Double endDistance) {
		this.endDistance = endDistance;
	}

	public Double getPerFee() {
		return perFee;
	}

	public void setPerFee(Double perFee) {
		this.perFee = perFee;
	}

	@Override
	public String toString() {
		return "MerchantDeliverFeeDTO{" +
				"initDistance=" + initDistance +
				", initFee=" + initFee +
				", beginDistance=" + beginDistance +
				", endDistance=" + endDistance +
				", perFee=" + perFee +
				'}';
	}

}