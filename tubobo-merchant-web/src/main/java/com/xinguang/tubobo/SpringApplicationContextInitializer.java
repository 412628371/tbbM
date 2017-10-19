package com.xinguang.tubobo;

import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.AbstractRefreshableApplicationContext;

/**
 * Created by shade on 2017/10/13.
 */
public class SpringApplicationContextInitializer implements ApplicationContextInitializer<AnnotationConfigEmbeddedWebApplicationContext> {

    @Override
    public void initialize(AnnotationConfigEmbeddedWebApplicationContext annotationConfigEmbeddedWebApplicationContext) {
        annotationConfigEmbeddedWebApplicationContext.setAllowBeanDefinitionOverriding(false);
    }
}
