package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 短信验证码实体
 * @author young
 * @2016年3月22日
 */
@Entity(noClassnameStored=true)
public class ValidateCodeEntity {
	
	public static String RESULT_INIT = "0";//未验证
	public static String RESULT_SUC = "1";//验证成功
	public static String RESULT_FAIL = "2";//验证失败
	public static String RESULT_EXPIRED = "3";//验证失败
	
	public static String TYPE_REGISTER = "r";//注册
	public static String TYPE_FINDPASSWORD = "f";//找回密码

	public static String TYPE_WEIXIN_BIND_PHONE = "weixin_bind_phone";//微信帐号绑定手机号码
	
	@Id
	private String id;
	
	private String phone;
	private String code;//验证码
	private String type;//用途类型
	private String result;//验证结果
	private Date expireTime;//验证码过期时间
	
	private Date createDate;
	private Date updateDate;
	private String delFlag;
	
	public String getId() {
		return id;
	}
	public String getPhone() {
		return phone;
	}
	public String getCode() {
		return code;
	}
	public String getType() {
		return type;
	}
	public String getResult() {
		return result;
	}
	public Date getExpireTime() {
		return expireTime;
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
	public void setId(String id) {
		this.id = id;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
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
	
}
