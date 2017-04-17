package com.xinguang.tubobo.impl.tuboboZhushouVo;

import java.util.List;

public class ReqDistributionRemain {

	private String tokenId;
	private List<ReqDistributionRemainItem> waybills;
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public List<ReqDistributionRemainItem> getWaybills() {
		return waybills;
	}
	public void setWaybills(List<ReqDistributionRemainItem> waybills) {
		this.waybills = waybills;
	}
	
	public static class ReqDistributionRemainItem {

		private String dealMethod;
		private String remainReason;
		private String waybillNo;
		
		public String getDealMethod() {
			return dealMethod;
		}
		public void setDealMethod(String dealMethod) {
			this.dealMethod = dealMethod;
		}
		public String getRemainReason() {
			return remainReason;
		}
		public void setRemainReason(String remainReason) {
			this.remainReason = remainReason;
		}
		public String getWaybillNo() {
			return waybillNo;
		}
		public void setWaybillNo(String waybillNo) {
			this.waybillNo = waybillNo;
		}
		
	}
}
