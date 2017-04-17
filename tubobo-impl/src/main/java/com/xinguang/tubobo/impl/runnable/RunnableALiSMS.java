package com.xinguang.tubobo.impl.runnable;

import com.xinguang.tubobo.impl.alisms.entity.AliSmsRecordEntity;
import com.xinguang.tubobo.impl.alisms.utils.AliSmsUtils;

/**
 * 阿里短信发送线程
 * @author oU_Young
 * @2016年9月16日
 */
public class RunnableALiSMS implements Runnable {
    
	private AliSmsRecordEntity entity;
	
	public RunnableALiSMS(AliSmsRecordEntity entity) {
		this.entity = entity;
	}
	@Override
	public void run() {
		try {
			AliSmsUtils.sendAliSms(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


