/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hzmux.hzcms.modules.sys.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

/**
 * 表单验证（包含验证码）过滤类
 * @author Song
 * @version 2014-10-01
 */
@Service
public class ClientFormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        writeOAuthJsonResponse((HttpServletResponse)response, "{\"resultCode\" : \"2\",\"resultDesc\" : \"未登录。\"}");
        return false;
    }
    
    private void writeOAuthJsonResponse(HttpServletResponse response, String json) {
        response.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.print(json);
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Write json to response error", e);
        }
    }
}