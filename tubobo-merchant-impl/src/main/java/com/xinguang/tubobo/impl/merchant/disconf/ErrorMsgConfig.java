package com.xinguang.tubobo.impl.merchant.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by lvhantai on 2017/6/14.
 */
@Configuration
@Scope("singleton")
@DisconfFile(filename = "errormsg.properties")
public class ErrorMsgConfig {
}
