package com.project.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.blog.common.constant.UserExceptionMessage;
import com.project.blog.common.constant.UserStatus;
import com.project.blog.common.exception.UserException;
import com.project.blog.common.utils.JWTUtils;
import com.project.blog.common.utils.SecurityUtils;
import com.project.blog.mapper.UserMapper;
import com.project.blog.pojo.dto.LoginDTO;
import com.project.blog.pojo.dto.RegisterDTO;
import com.project.blog.pojo.po.User;
import com.project.blog.pojo.vo.LoginVO;
import com.project.blog.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final JWTUtils jwtUtils;

    /**
     * 用户登录
     *
     * @param loginDTO
     * @return
     */
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 用户名匹配
        User user = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(loginDTO.getUsername()), User::getUsername, loginDTO.getUsername())
                .one();
        if (user == null) {
            log.warn("用户不存在,username={}", loginDTO.getUsername());
            throw new UserException(UserExceptionMessage.USER_NOT_EXIST);
        }

        // 密码匹配
        if (!SecurityUtils.matches(loginDTO.getPassword(), user.getSalt(), user.getPassword())) {
            log.warn("登录账号时密码错误,username={}", loginDTO.getUsername());
            throw new UserException(UserExceptionMessage.PASSWORD_ERROR);
        }
        String accessToken = jwtUtils.generateAccessToken(user.getId());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId());

        // 更新登录时间
        this.lambdaUpdate()
                .eq(User::getId, user.getId())
                .set(User::getLastLoginTime, LocalDateTime.now())
                .update();

        log.info("用户登陆成功,username={}", loginDTO.getUsername());

        return LoginVO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 用户注册
     *
     * @param registerDTO
     */
    @Override
    public void register(RegisterDTO registerDTO) {
        // 用户查重
        User user = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(registerDTO.getUsername()), User::getUsername, registerDTO.getUsername())
                .one();
        if (user != null) {
            log.warn("用户已存在,username={}", registerDTO.getUsername());
            throw new UserException(UserExceptionMessage.USER_REGISTERED);
        }

        // 保存用户
        User registerUser = BeanUtil.copyProperties(registerDTO, User.class);
        registerUser.setStatus(UserStatus.ACTIVE);

        registerUser.setSalt(SecurityUtils.generateSalt());
        registerUser.setPassword(SecurityUtils.encryptPassword(registerDTO.getPassword(), registerUser.getSalt()));

        boolean saved = this.save(registerUser);
        if (!saved) {
            throw new UserException(UserExceptionMessage.USER_REGISTER_ERROR);
        }

        log.info("用户注册成功,username={}", registerDTO.getUsername());
    }
}
