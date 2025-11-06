package com.project.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.blog.common.constant.UserExceptionMessage;
import com.project.blog.common.constant.UserStatus;
import com.project.blog.common.exception.UserException;
import com.project.blog.common.utils.JWTUtils;
import com.project.blog.mapper.UserMapper;
import com.project.blog.pojo.dto.LoginDTO;
import com.project.blog.pojo.dto.RegisterDTO;
import com.project.blog.pojo.po.User;
import com.project.blog.pojo.vo.LoginVO;
import com.project.blog.pojo.vo.UserInfo;
import com.project.blog.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final JWTUtils jwtUtils;

    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    @Override
    public LoginVO login(LoginDTO loginDto) {
        User user = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(loginDto.getUsername()), User::getUsername, loginDto.getUsername())
                .one();

        if(user==null) {
            throw new UserException(UserExceptionMessage.USER_NOT_EXIST);
        }

        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            throw new UserException(UserExceptionMessage.PASSWORD_ERROR);
        }
        String token = jwtUtils.generateToken(user.getId());

        UserInfo userInfo = BeanUtil.copyProperties(user, UserInfo.class);

        return LoginVO.builder()
                .token(token)
                .userInfo(userInfo)
                .build();
    }

    /**
     * 用户注册
     * @param registerDTO
     */
    @Override
    public void register(RegisterDTO registerDTO) {
        User user = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(registerDTO.getUsername()), User::getUsername, registerDTO.getUsername())
                .one();
        if(user!=null){
            throw new UserException(UserExceptionMessage.USER_REGISTERED);
        }

        User registerUser = BeanUtil.copyProperties(registerDTO, User.class);
        registerUser.setStatus(UserStatus.ACTIVE);
        registerUser.setPassword(SecureUtil.md5(registerDTO.getPassword()));

        boolean saved = this.save(registerUser);
        if (!saved) {
            throw new UserException(UserExceptionMessage.USER_REGISTER_ERROR);
        }
    }
}
