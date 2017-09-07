/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;
import java.util.Date;

public class MerchantDeliverFeeConfigDTO implements Serializable{

	private Double initDistance;
	private Double initFee;
	private Double beginDistance;
	private Double endDistance;
	private Double perFee;
	private String areaCode;
	private String cityCode;
	private String provinceCode;

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