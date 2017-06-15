package com.xinguang.tubobo.impl.merchant.disconf;

import com.baidu.disconf.client.usertools.DisconfDataGetter;
import com.xinguang.tubobo.merchant.api.util.ErrorMsgHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/15.
 */
@Component
public class ErrorMsgConfigHelper implements InitializingBean {
    public  Map<String, Object> errorMap;
    public  void loadErrorMsg(){
        errorMap = DisconfDataGetter.getByFile("errormsg.properties");
        ErrorMsgHelper.updateErrorMap(errorMap);
    }
    @PostConstruct
    public void  init(){
        loadErrorMsg();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
