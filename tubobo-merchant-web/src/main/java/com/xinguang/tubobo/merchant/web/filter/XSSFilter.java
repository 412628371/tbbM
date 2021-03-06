package com.xinguang.tubobo.merchant.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Administrator on 2017/4/21.
 */
public class XSSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        XSSRequestWrapper xssRequest = new XSSRequestWrapper(
                (HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

}
