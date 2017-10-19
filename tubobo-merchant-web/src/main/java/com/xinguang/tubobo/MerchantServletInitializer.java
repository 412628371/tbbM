package com.xinguang.tubobo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class MerchantServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(com.xinguang.tubobo.Application.class).initializers(new SpringApplicationContextInitializer());
    }
}
