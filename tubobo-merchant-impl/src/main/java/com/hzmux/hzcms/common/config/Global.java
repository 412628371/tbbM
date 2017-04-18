/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.common.config;

import java.util.Map;

import com.baidu.disconf.client.usertools.DisconfDataGetter;
import com.hzmux.hzcms.common.utils.StringUtils;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;
import com.hzmux.hzcms.common.utils.PropertiesLoader;

/**
 * 全局配置类
 * @author Song
 * @version 2014-10-01
 */
public class Global {
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();
	
	/**
	 * 属性文件加载对象
	 */
//	private static PropertiesLoader propertiesLoader = new PropertiesLoader("hzcms.properties");

	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (StringUtils.isNotBlank(value)){
			return value;
		}
		value = (String) DisconfDataGetter.getByFileItem("common.properties",key);
		if (null == value) {
			value = (String) DisconfDataGetter.getByFileItem("thirdPart.properties", key);
		}
		if (null == value){
			value = "";
		} else {
			map.put(key, value);
		}
		return value;
	}

	/**
	 * disconf更新
	 * 清除本地map所有缓存
	 */
	public static void disConfUpdatedAndClearMap(){
		map.clear();
	}

	/////////////////////////////////////////////////////////
	
	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	
	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	
	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
	
	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}

	/**
	 * 获取CKFinder上传文件的根目录
	 * @return
	 */
	public static String getCkBaseDir() {
		String dir = getConfig("userfiles.basedir");
		Assert.hasText(dir, "配置文件里没有配置userfiles.basedir属性");
		if(!dir.endsWith("/")) {
			dir += "/";
		}
		return dir;
	}
	
}
