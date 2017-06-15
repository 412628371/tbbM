package com.xinguang.tubobo.impl.merchant.disconf;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.xinguang.tubobo.merchant.api.util.ErrorMsgHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;

/**
 * Created by shade on 2017/4/6.
 */
@Configuration
@Scope("singleton")
@DisconfUpdateService(classes = { Config.class, ErrorMsgConfig.class})
public class ConfigCallback implements IDisconfUpdate {

    private static Logger logger = LoggerFactory.getLogger(ConfigCallback.class);

    @Resource
    private Config config;
    @Resource
    private ErrorMsgConfigHelper errorMsgConfigHelper;

    @Override
    public void reload() throws Exception {
        errorMsgConfigHelper.loadErrorMsg();
        logger.info("Config update:");
    }
}

