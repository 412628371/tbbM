package com.xinguang.tubobo.impl.tuboboZhushouVo;

import java.util.List;

import com.xinguang.tubobo.impl.entity.UploadWayBillEntity;

public class ReqWaybillUpload {

    private String tokenId;
    private String smsModelId;
    private int uploadType;//0:自提件 1:配送件 2:滞留件
    private boolean sendFlag;//false:默认入库；true:直接出库配送

    private List<UploadWayBillEntity> waybills;
    
	public String getTokenId() {
		return tokenId;
	}
	public String getSmsModelId() {
		return smsModelId;
	}
	public List<UploadWayBillEntity> getWaybills() {
		return waybills;
	}
	public int getUploadType() {
		return uploadType;
	}
	public boolean getSendFlag() {
		return sendFlag;
	}
	
}
