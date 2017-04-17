package com.xinguang.tubobo.impl.runnable;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hzmux.hzcms.common.utils.RedisUtil;
import com.xinguang.tubobo.impl.redisCache.RedisCache;

/**
 * 后台守护线程 
 * 监听redis 保存待保存的运单信息
 * @author oU_Young
 * @2016年9月18日
 */
public class DaemonSaveWayBill implements Runnable {
    
	protected static Logger logger = LoggerFactory.getLogger(DaemonSaveWayBill.class);
	
	private static Thread thread;
    
	private static boolean alive = false;
    
    private DaemonSaveWayBill() {
    	
    }
    
    public static void start(boolean daemon) {
        if(thread == null) {
            thread = new Thread(new DaemonSaveWayBill());
            thread.setDaemon(daemon);
            alive = true;
            thread.start();
            logger.info("DaemonSaveWayBill start...");
        }
    }
    public static void stop() {
        thread = null;
        alive = false;
        logger.info("DaemonSaveWayBill stop...");
    }
    public static boolean isAlive() {
        return alive;
    }
    public static Thread getThread() {
    	return thread;
    }

    @Override
    public void run() {
        while(alive) {
        	try {
                String recordId = RedisUtil.blpop(0, RedisCache.REDIS_QUEUE_UPLOAD_WAYBILL);
                if(StringUtils.isNotBlank(recordId)) {
            		ThreadPoolUtils.executorSaveWayBill(recordId);
                }
        	} catch (Exception e) {
        		e.printStackTrace();
			}
        }
    }
}


