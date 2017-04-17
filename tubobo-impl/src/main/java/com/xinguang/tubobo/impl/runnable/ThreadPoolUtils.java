package com.xinguang.tubobo.impl.runnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xinguang.tubobo.impl.alisms.entity.AliSmsRecordEntity;

/**
 * 
 * @author young
 * @2016年5月31日
 */
public class ThreadPoolUtils{
	
	/**
	 * 核心线程数，指保留的线程池大小
	 */
	private static int corePoolSize = 20;

	/**线程池**/
//	private static ExecutorService threadPool = new ThreadPoolExecutor(corePoolSize,20,1,TimeUnit.SECONDS,
//			new LinkedBlockingQueue<Runnable>());
//	private static ExecutorService singleThread = Executors.newSingleThreadExecutor();
	private static ExecutorService fixedThread = Executors.newFixedThreadPool(corePoolSize);
	
	private ThreadPoolUtils(){
		
	}
	
	public static void executorSaveWayBill(String recordId){
		fixedThread.execute(new RunnableSaveWayBill(recordId));
	}
	
	public static void executorALiSms(AliSmsRecordEntity entity){
		fixedThread.execute(new RunnableALiSMS(entity));
	}
	
}
