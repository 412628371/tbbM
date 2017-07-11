/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.common.utils;

import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.cache.CacheManager;

/**
 * Cache工具类
 * @author Song
 * @version 2014-10-01
 */
public class CacheUtils {
	
	private static CacheManager cacheManager = ((CacheManager)SpringContextHolder.getBean("cacheManager"));

	private static final String SYS_CACHE = RedisCache.MERCHANT;
//
//	public static Object get(String key) {
//		return get(SYS_CACHE, key);
//	}
//
//	public static void put(String key, Object value) {
//		put(SYS_CACHE, key, value);
//	}

	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}
//	public static Object get(String cacheName, String key) {
//		Element element = getCache(cacheName).get(key);
//		return element==null?null:element.getObjectValue();
//	}

//	public static void put(String cacheName, String key, Object value) {
//		Element element = new Element(key, value);
//		getCache(cacheName).put(element);
//	}

	public static void remove(String cacheName, String key) {
		cacheManager.getCache(cacheName).evict(key);
//		getCache(cacheName).remove(key);
	}
	
//	/**
//	 * 获得一个Cache，没有则创建一个。
//	 * @param cacheName
//	 * @return
//	 */
//	private static Cache getCache(String cacheName){
//		Cache cache = cacheManager.getCache(cacheName);
//		if (cache == null){
//			cacheManager.addCache(cacheName);
//			cache = cacheManager.getCache(cacheName);
//			cache.getCacheConfiguration().setEternal(true);
//		}
//		return cache;
//	}

	public static CacheManager getCacheManager() {
		return cacheManager;
	}
	
}
