package com.xinguang.tubobo.impl.tuboboZhushouVo;

import java.util.List;

public class ReqSign {

	private String tokenId;
	private List<ReqSignItem> waybills;
	 
	public static class ReqSignItem {

		private String userTags;
	    private String comment;
	    private String waybillNo;
	    private String receiverPhone;
		public String getUserTags() {
			return userTags;
		}
		public void setUserTags(String userTags) {
			this.userTags = userTags;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		public String getWaybillNo() {
			return waybillNo;
		}
		public void setWaybillNo(String waybillNo) {
			this.waybillNo = waybillNo;
		}
		public String getReceiverPhone() {
			return receiverPhone;
		}
		public void setReceiverPhone(String receiverPhone) {
			this.receiverPhone = receiverPhone;
		}
	    
	}
	 
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public List<ReqSignItem> getWaybills() {
		return waybills;
	}
	public void setWaybills(List<ReqSignItem> waybills) {
		this.waybills = waybills;
	}
	 
}
