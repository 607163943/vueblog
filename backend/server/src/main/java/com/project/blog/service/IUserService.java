package com.project.blog.service;

import com.project.blog.pojo.dto.LoginDTO;
import com.project.blog.pojo.dto.RegisterDTO;
import com.project.blog.pojo.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.blog.pojo.vo.LoginVO;

public interface IUserService extends IService<User> {

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 用户信息
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户注册
     * @param registerDTO 注册信息
     */
    void register(RegisterDTO registerDTO);
}
