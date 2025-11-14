package com.project.blog.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.project.blog.common.constant.UserExceptionMessage;
import com.project.blog.common.constant.UserStatus;
import com.project.blog.common.exception.UserException;
import com.project.blog.common.utils.JWTUtils;
import com.project.blog.pojo.po.User;
import com.project.blog.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountRealm extends AuthorizingRealm {
    private final JWTUtils jwtUtils;

    private final IUserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 这里不做授权
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取令牌
        JWTToken jwtToken = (JWTToken) token;

        // 解析出用户
        String userId = jwtUtils.getUserId(jwtToken.getPrincipal().toString());
        User user = userService.getById(Long.parseLong(userId));
        if (user == null) {
            log.warn("用户不存在,userId={}", userId);
            throw new UserException(UserExceptionMessage.USER_NOT_EXIST);
        }
        // 检测账号状态
        if (user.getStatus().equals(UserStatus.INACTIVE)) {
            log.warn("用户被禁用,userId={}", userId);
            throw new UserException(UserExceptionMessage.USER_LOCKED);
        }

        UserAccount userAccount = BeanUtil.copyProperties(user, UserAccount.class);

        return new SimpleAuthenticationInfo(userAccount, jwtToken.getCredentials(), getName());
    }
}