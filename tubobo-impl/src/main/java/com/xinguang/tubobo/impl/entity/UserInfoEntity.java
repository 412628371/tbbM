package com.xinguang.tubobo.impl.entity;

import java.util.Date;

/**
 * 用户信息
 * @author young
 * @2016年7月7日
 */
public class UserInfoEntity {
	
	private String userId;
	
	private String phone;//全局唯一
	private String password;

	private String loginName;

	private String nickName;
	private String portraitUrl;//头像url
	private String birthDay;//19900907
	private String male;//m:男性 f:女性
	private String email;
	
	private String idCard;
	private String realName;
	private String riderPhotoUrl;//认证骑士图片url
    private String riderStatus;//骑士认证状态 0：未认证 1：审核中;2:审核通过;3:审核失败;
    private Date riderApplyDate;//申请骑士时间
    private Date riderVerifyDate;//客服审核时间

    private String userType;//用户类型 0:默认
    private String yezhuMark;//业主标记
    private String distributionMark;//配送标记 0:自提(默认) 1:配送

	private String wenwen;
//	@Reference(ignoreMissing=true)
//	private WechatOfficialUserEntity wenwen;//问问微信公众号信息
	
	private String bindWechat;
	private String bindQQ;
	private String bindSina;
	
	private Date createDate;
	private Date updateDate;
	private String delFlag;
	
	public String getUserId() {
		return userId;
	}
	public String getPhone() {
		return phone;
	}
	public String getPassword() {
		return password;
	}
	public String getLoginName() {
		return loginName;
	}
	public String getBindWechat() {
		return bindWechat;
	}
	public String getBindQQ() {
		return bindQQ;
	}
	public String getBindSina() {
		return bindSina;
	}
	public String getNickName() {
		return nickName;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public String getMale() {
		return male;
	}
	public String getEmail() {
		return email;
	}
	public String getIdCard() {
		return idCard;
	}
	public String getRealName() {
		return realName;
	}
	public String getRiderStatus() {
		return riderStatus;
	}
	public Date getRiderApplyDate() {
		return riderApplyDate;
	}
	public Date getRiderVerifyDate() {
		return riderVerifyDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public void setBindWechat(String bindWechat) {
		this.bindWechat = bindWechat;
	}
	public void setBindQQ(String bindQQ) {
		this.bindQQ = bindQQ;
	}
	public void setBindSina(String bindSina) {
		this.bindSina = bindSina;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public void setMale(String male) {
		this.male = male;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public void setRiderStatus(String riderStatus) {
		this.riderStatus = riderStatus;
	}
	public void setRiderApplyDate(Date riderApplyDate) {
		this.riderApplyDate = riderApplyDate;
	}
	public void setRiderVerifyDate(Date riderVerifyDate) {
		this.riderVerifyDate = riderVerifyDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getPortraitUrl() {
		return portraitUrl;
	}
	public String getRiderPhotoUrl() {
		return riderPhotoUrl;
	}
	public void setPortraitUrl(String portraitUrl) {
		this.portraitUrl = portraitUrl;
	}
	public void setRiderPhotoUrl(String riderPhotoUrl) {
		this.riderPhotoUrl = riderPhotoUrl;
	}
	public String getDistributionMark() {
		return distributionMark;
	}
	public void setDistributionMark(String distributionMark) {
		this.distributionMark = distributionMark;
	}
	public String getWenwen() {
		return wenwen;
	}
	public void setWenwen(String wenwen) {
		this.wenwen = wenwen;
	}
//	public WechatOfficialUserEntity getWenwen() {
//		return wenwen;
//	}
//	public void setWenwen(WechatOfficialUserEntity wenwen) {
//		this.wenwen = wenwen;
//	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getYezhuMark() {
		return yezhuMark;
	}
	public void setYezhuMark(String yezhuMark) {
		this.yezhuMark = yezhuMark;
	}
	
}

