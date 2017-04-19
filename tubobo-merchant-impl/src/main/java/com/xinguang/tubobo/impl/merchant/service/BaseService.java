package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.disconf.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/4/17.
 */
public class BaseService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    protected Config config;
}
