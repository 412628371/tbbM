package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(noClassnameStored=true)
public class AppVersionEntity {
	
	public static String NAME_WENWENZHUSHOU = "wenwenZhushou";
	public static String NAME_WENWENLIFE = "wenwenLife";
	public static String NAME_WUYE = "wuye";
	public static String NAME_WUYEPRIVATE = "wuyePrivate";

	public static String TYPE_ANDROID = "android";
	public static String TYPE_IOS = "ios";
	
	@Id
	private String id;
	
	private String appName;
	
	private String appType;//ios;android
    private String version;
    private String minVersion;
    private String url;
    private String description;
    
    private String delFlag;
    private Date createDate;
    
	public String getId() {
		return id;
	}
	public String getAppName() {
		return appName;
	}
	public String getAppType() {
		return appType;
	}
	public String getVersion() {
		return version;
	}
	public String getMinVersion() {
		return minVersion;
	}
	public String getUrl() {
		return url;
	}
	public String getDescription() {
		return description;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setMinVersion(String minVersion) {
		this.minVersion = minVersion;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
    
}
