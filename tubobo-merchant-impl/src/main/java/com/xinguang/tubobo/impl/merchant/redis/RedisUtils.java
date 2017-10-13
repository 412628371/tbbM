/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.core.RedisOperations;

import java.util.Set;


public class RedisUtils extends RedisCache {

	Logger logger  = LoggerFactory.getLogger(RedisCache.class);

	private final RedisOperations redisOperations;
	private final String prefix;

	/**
	 * Constructs a new <code>RedisCache</code> instance.
	 *
	 * @param name            cache name
	 * @param prefix
	 * @param redisOperations
	 * @param expiration
	 */
	public RedisUtils(String name, String prefix, RedisOperations<?, ?> redisOperations, long expiration) {
		super(name, prefix.getBytes(), redisOperations, expiration);
		this.redisOperations = redisOperations;
		this.prefix = prefix;
	}

	@Override
	public void evict(Object key) {
		RedisCacheKey redisKey = new RedisCacheKey(key).usePrefix(this.prefix.getBytes()).withKeySerializer(
				redisOperations.getKeySerializer());
		Set<String> keys = redisOperations.keys(new String(redisKey.getKeyBytes()));
		logger.debug("evict size:{}", keys.size());
		for (String keyd : keys) {
			logger.debug("evict:{}", keyd);
			RedisCacheElement redisCacheElement = new RedisCacheElement(new RedisCacheKey(keyd).usePrefix(null).withKeySerializer(
					redisOperations.getKeySerializer()), null);
			super.evict(redisCacheElement);
		}
	}
}
