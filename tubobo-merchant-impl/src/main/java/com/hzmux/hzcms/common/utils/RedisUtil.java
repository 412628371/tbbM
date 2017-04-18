package com.hzmux.hzcms.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.hzmux.hzcms.common.config.Global;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {
	
	private static JedisPool jedisPool;

	private static boolean redisEnable = true;
	private static int defaultSeconds = 0;
	/**
	 * 初始化Redis连接池
	 */
	static {
		redisEnable = Boolean.valueOf(Global.getConfig("redis.enable"));
		defaultSeconds = Integer.valueOf(Global.getConfig("redis.default.save.seconds"));
		// 创建jedis池配置实例
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置池配置项值
		config.setMaxTotal(Integer.valueOf(Global.getConfig("redis.pool.maxActive")));
		config.setMaxIdle(Integer.valueOf(Global.getConfig("redis.pool.maxIdle")));
		config.setMaxWaitMillis(Long.valueOf(Global.getConfig("redis.pool.maxWait")));
		config.setTestOnBorrow(Boolean.valueOf(Global.getConfig("redis.pool.testOnBorrow")));
		config.setTestOnReturn(Boolean.valueOf(Global.getConfig("redis.pool.testOnReturn")));

		// 根据配置实例化jedis池   config ip port timeout password
		jedisPool = new JedisPool(config, Global.getConfig("redis.ip"),
				Integer.valueOf(Global.getConfig("redis.port")),
				Integer.valueOf(Global.getConfig("redis.timeout")),
				Global.getConfig("redis.password"));
	}

	/**
	 * 向缓存中设置字符串内容
	 * 
	 * @param key
	 * @param value
	 * @param seconds (设置为0表示使用默认值)
	 * @return
	 * @throws Exception
	 */
	public static boolean set(String key, String value, int seconds) {
		if (!redisEnable) {
			return false;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (seconds > 0) {
				jedis.setex(key, seconds, value);
			}else {
				jedis.setex(key, defaultSeconds, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				jedis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 向缓存中设置对象
	 * 
	 * @param key
	 * @param value
	 * @param seconds (设置为0表示使用默认值)
	 * @return
	 */
	public static boolean set(String key, Object value, int seconds) {
		if (!redisEnable) {
			return false;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String str = serialize(value);
			if (str != null && str.trim().length() > 0) {
				if (seconds > 0) {
					jedis.setex(key, seconds, str);
				}else {
					jedis.setex(key, defaultSeconds, str);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				jedis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据key 获取内容
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return get(key, 0);
	}

	public static String get(String key, int expireSeconds) {
		if (!redisEnable) {
			return null;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (expireSeconds > 0 && jedis.exists(key)) {
				jedis.expire(key, expireSeconds);
			}
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				jedis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据key 获取对象
	 * 
	 * @param key
	 * @return
	 */
	public static <T> T get(String key, Class<T> clazz) {
		return get(key, clazz, 0);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(String key, Class<T> clazz, int expireSeconds) {
		if (!redisEnable) {
			return null;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (expireSeconds > 0 && jedis.exists(key)) {
				jedis.expire(key, expireSeconds);
			}
			String str = jedis.get(key);
			if (str != null && str.trim().length() > 0) {
				return (T) unserialize(str);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				jedis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    public static Long incr(String key) {
        if (!redisEnable) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long result = jedis.incr(key);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static Long incrBy(String key, long i) {
        if (!redisEnable) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long result = jedis.incrBy(key, i);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static Long del(String... key){
        if (!redisEnable) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long result = jedis.del(key);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static Long rpush (String key, String ... strings ) {
        if(!redisEnable) {
            return null;
        }
//        try(Jedis jedis = jedisPool.getResource()) {
//            return jedis.rpush(key, strings);
//        }
        
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long result = jedis.rpush(key, strings);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String blpop(int timeout, String key) {
        if(!redisEnable) {
            return null;
        }
//        try(Jedis jedis = jedisPool.getResource()) {
//            List<String> list = jedis.blpop(timeout, key);
//            if(null != list && list.size()==2) {
//                return list.get(1);
//            } else {
//                return null;
//            }
//        }
        
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> list = jedis.blpop(timeout, key);
            if (null != list && list.size() == 2) {
                return list.get(1);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static Long sadd(String key, String string) {
        if(!redisEnable) {
            return null;
        }
        
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long result = jedis.sadd(key, string);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static Set<String> smembers(String key) {
        if(!redisEnable) {
            return null;
        }
        
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String>  result = jedis.smembers(key);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static Set<String> keys(String key) {
        if(!redisEnable) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String>  result = jedis.keys(key);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static long decrBy(String key, long i) {
        if(!redisEnable) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long  result = jedis.decrBy(key,i);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public static Jedis getJedis() {
		try {
			return jedisPool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			// jedisPool.returnResource(jedis);
			try {
				jedis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// serialize
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return baos.toString("ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object unserialize(String value) {
		ByteArrayInputStream bais = null;
		try {
			// deserialize
			bais = new ByteArrayInputStream(value.getBytes("ISO-8859-1"));
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isRedisEnable() {
		return redisEnable;
	}

	/**
	 * 向缓存中设置字符串内容 ,永久有效
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static boolean persist(String key, String value) {
		if (!redisEnable) {
			return false;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			jedis.persist(key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				jedis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
//        Long cout1 = rpush("test1", "hanyong", "test", "hanyong");
//        Long cout2 = rpush("test2", "test2hanyong", "test2test", "test2hanyong");
//        System.out.println(cout1 +"/" +cout2);
        System.out.println(blpop(5, "test1"));
        System.out.println(blpop(5, "test2"));
    }
}