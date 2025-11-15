package com.project.blog.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
// 确保该过滤器第一个处理请求
@Order(-1)
public class GlobalExceptionFilter extends OncePerRequestFilter {

    @Resource
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) {
        // 确保SpringBoot容器内所有异常都走全局异常处理
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, e);
        }
    }
}
