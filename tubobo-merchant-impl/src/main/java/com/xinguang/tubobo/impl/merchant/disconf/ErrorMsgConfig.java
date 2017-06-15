package com.xinguang.tubobo.impl.merchant.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.usertools.DisconfDataGetter;
import com.xinguang.tubobo.merchant.api.util.ErrorMsgHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;

/**
 * Created by lvhantai on 2017/6/14.
 */
@Configuration
@Scope("singleton")
@DisconfFile(filename = "errormsg.properties")
public class ErrorMsgConfig {

}
