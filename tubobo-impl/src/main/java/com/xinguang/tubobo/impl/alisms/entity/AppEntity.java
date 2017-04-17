package com.xinguang.tubobo.impl.alisms.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 应用信息
 */
@Entity(noClassnameStored=true)
public class AppEntity {
	
	@Id
	private String appId;
	private String appName;
	
	public String getAppId() {
		return appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
}
