package com.project.blog.controller;


import cn.hutool.core.bean.BeanUtil;
import com.project.blog.common.result.Result;
import com.project.blog.common.utils.JWTUtils;
import com.project.blog.pojo.dto.LoginDTO;
import com.project.blog.pojo.dto.RegisterDTO;
import com.project.blog.pojo.po.User;
import com.project.blog.pojo.vo.LoginVO;
import com.project.blog.pojo.vo.UserInfoVO;
import com.project.blog.security.UserAccount;
import com.project.blog.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final JWTUtils jwtUtils;

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return void响应
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Object> register(@Validated @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);

        return Result.success();
    }

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 用户信息
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);

        return Result.success(loginVO);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息VO
     */
    @RequiresAuthentication
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        UserAccount userAccount = (UserAccount) SecurityUtils.getSubject().getPrincipal();
        User user = userService.getById(userAccount.getId());
        return Result.success(BeanUtil.copyProperties(user, UserInfoVO.class));
    }

    /**
     * 用户退出
     *
     * @return void响应
     */
    @RequiresAuthentication
    @ApiOperation("用户退出")
    @GetMapping("/logout")
    public Result<Object> logout() {
        // 清除上下文用户数据
        SecurityUtils.getSubject().logout();
        log.info("用户退出成功");
        return Result.success(null, "退出成功");
    }

    /**
     * 刷新令牌
     *
     * @return 新的访问令牌
     */
    @RequiresAuthentication
    @ApiOperation("刷新令牌")
    @GetMapping("/refresh")
    public Result<String> refreshToken() {
        UserAccount userAccount = (UserAccount) SecurityUtils.getSubject().getPrincipal();
        return Result.success(jwtUtils.generateAccessToken(userAccount.getId()));
    }

}
