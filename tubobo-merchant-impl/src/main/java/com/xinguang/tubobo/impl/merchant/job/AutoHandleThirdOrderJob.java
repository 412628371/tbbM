package com.xinguang.tubobo.impl.merchant.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.xinguang.tubobo.impl.merchant.service.ThirdOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by xuqinghua on 2017/7/5.
 */
public class AutoHandleThirdOrderJob implements SimpleJob {
    Logger logger = LoggerFactory.getLogger(AutoHandleThirdOrderJob.class);
    @Autowired private ThirdOrderService thirdOrderService;
    @Override
    public void execute(ShardingContext shardingContext) {
        Integer shardingItem = shardingContext.getShardingItem();

        logger.info("当前任务分片：{},处理第三方平台订单",shardingItem);
        switch (shardingItem){
            case 0:
                thirdOrderService.deleteAllRecords();
            case 1:
//                    System.out.println("1");
            default:

        }
    }
}
