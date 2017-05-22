package com.xinguang.tubobo.impl.merchant.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/5/22.
 */
public class AutoRateJob implements SimpleJob {
    static Logger logger = LoggerFactory.getLogger(AutoRateJob.class);

    @Autowired
    OrderService orderService;
    @Override
    public void execute(ShardingContext shardingContext) {
        Integer shardingItem = shardingContext.getShardingItem();
        logger.info("当前任务分片：{}",shardingItem);

        switch (shardingItem) {
            case 0:
                System.out.println("0");
            case 1:
                System.out.println("1");
            default:
                System.out.println();

        }
    }
}
