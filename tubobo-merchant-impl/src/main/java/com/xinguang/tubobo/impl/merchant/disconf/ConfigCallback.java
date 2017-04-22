package com.xinguang.tubobo.impl.merchant.disconf;

import com.baidu.disconf.client.common.update.IDisconfUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by shade on 2017/4/6.
 */
public class ConfigCallback implements IDisconfUpdate {

    private static Logger logger = LoggerFactory.getLogger(ConfigCallback.class);

    @Resource
    private Config config;

    @Override
    public void reload() throws Exception {
        logger.info("Config update:");
    }
}

