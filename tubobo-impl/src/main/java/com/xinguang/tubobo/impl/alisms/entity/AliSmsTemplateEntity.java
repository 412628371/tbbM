package com.xinguang.tubobo.impl.alisms.entity;

import java.io.Serializable;
import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 阿里短信模版
 */
@Entity(noClassnameStored=true)
public class AliSmsTemplateEntity implements Serializable {
	
	private static final long serialVersionUID = -8645277563286945606L;

	@Id
	private String id;
	
	private String aliSmsId;//阿里短信模版id
	private String aliSignName;//阿里短信模版签名
	
	private String smsType;//短信类型
	private String name;//模版名称
	private String content;//模版内容
	
	private Date createDate;//录入时间
	private Date updateDate;//更新时间
	
	public String getAliSmsId() {
		return aliSmsId;
	}
	public String getAliSignName() {
		return aliSignName;
	}
	public String getSmsType() {
		return smsType;
	}
	public String getContent() {
		return content;
	}
	public void setAliSmsId(String aliSmsId) {
		this.aliSmsId = aliSmsId;
	}
	public void setAliSignName(String aliSignName) {
		this.aliSignName = aliSignName;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
