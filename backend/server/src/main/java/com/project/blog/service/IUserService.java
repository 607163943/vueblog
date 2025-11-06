package com.project.blog.service;

import com.project.blog.pojo.dto.LoginDTO;
import com.project.blog.pojo.dto.RegisterDTO;
import com.project.blog.pojo.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.blog.pojo.vo.LoginVO;

public interface IUserService extends IService<User> {

    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    LoginVO login(LoginDTO loginDto);

    /**
     * 用户注册
     * @param registerDTO
     */
    void register(RegisterDTO registerDTO);
}
