package com.xinguang.tubobo.impl.merchant.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.impl.merchant.service.RateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * Created by Administrator on 2017/5/22.
 */
public class AutoRateJob implements SimpleJob {
    static Logger logger = LoggerFactory.getLogger(AutoRateJob.class);

    @Autowired
    OrderService orderService;
    @Autowired
    RateService rateService;
    @Override
    public void execute(ShardingContext shardingContext) {
        Integer shardingItem = shardingContext.getShardingItem();

        List<String> orderNos = orderService.getUnRatedOrderNos();
        logger.info("当前任务分片：{},处理为评价的订单：list:{}",shardingItem,orderNos);
        for (String orderNo:orderNos){
//            char last = orderNo.charAt(orderNo.length()-1);
//            int lastNum = Integer.valueOf(String.valueOf(last));
            //TODO 目前只用一台处理自动评价
            switch (shardingItem){
                case 0:
                    MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
                    if (entity == null) {
                        continue;
                    }
                    rateService.rate(entity.getUserId(),orderNo,5,5,"",entity.getRiderId());
                case 1:
//                    System.out.println("1");
                default:

            }


        }
    }
}
