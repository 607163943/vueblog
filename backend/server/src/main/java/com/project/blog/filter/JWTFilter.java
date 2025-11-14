package com.project.blog.filter;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.project.blog.common.constant.HttpHeaderKey;
import com.project.blog.common.constant.UserExceptionMessage;
import com.project.blog.common.exception.UserException;
import com.project.blog.common.utils.JWTUtils;
import com.project.blog.shiro.JWTToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class JWTFilter extends AuthenticatingFilter {

    public static final String KEY="jwt";

    @Resource
    JWTUtils jwtUtils;

    /**
     * 创建认证信息
     *
     * @param servletRequest  请求
     * @param servletResponse 响应
     * @return 认证信息
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        // 获取 token
        HttpServletRequest request = WebUtils.toHttp(servletRequest);
        String token = request.getHeader(HttpHeaderKey.AUTHORIZATION);

        // 为null的token直接抛出异常
        if (StrUtil.isEmpty(token)) {
            log.warn("空令牌认证!,servletRequest={}", servletRequest);
            throw new UserException(UserExceptionMessage.LOGIN_TIMEOUT);
        }

        return JWTToken.builder()
                .token(token)
                .build();
    }

    /**
     * 判断是否允许访问
     *
     * @param request     请求对象
     * @param response    响应对象
     * @param mappedValue the filter-specific config value mapped to this filter in the URL rules mappings.
     * @return 是否允许访问标志
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String token = httpServletRequest.getHeader(HttpHeaderKey.AUTHORIZATION);
        // 匿名访客直接放行
        return StringUtils.isEmpty(token);
    }

    /**
     * 检查认证信息
     *
     * @param servletRequest  the incoming <code>ServletRequest</code>
     * @param servletResponse the outgoing <code>ServletResponse</code>
     * @return 是否允许放行
     * @throws Exception 认证信息检查异常
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取请求头中的令牌
        HttpServletRequest request = WebUtils.toHttp(servletRequest);
        String token = request.getHeader(HttpHeaderKey.AUTHORIZATION);
        // 判断是否已过期
        jwtUtils.check(token);

        // 执行自动登录
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     * Shiro认证失败
     *
     * @param token    认证信息
     * @param e        认证异常
     * @param request  请求
     * @param response 响应
     * @return 认证结果
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("未知原因触发异常！", e);
        // 出现预料外异常，直接交给全局异常处理
        throw new RuntimeException(e);
    }
}