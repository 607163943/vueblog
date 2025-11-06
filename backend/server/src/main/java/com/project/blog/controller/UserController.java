package com.project.blog.controller;


import com.project.blog.common.result.Result;
import com.project.blog.pojo.dto.LoginDTO;
import com.project.blog.pojo.dto.RegisterDTO;
import com.project.blog.pojo.vo.LoginVO;
import com.project.blog.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户接口")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    /**
     * 用户注册
     * @param registerDTO
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("/user/register")
    public Result<Object> register(@Validated @RequestBody RegisterDTO registerDTO){
        userService.register(registerDTO);

        return Result.success();
    }

    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/user/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO loginDto){
        LoginVO loginVO=userService.login(loginDto);

        return Result.success(loginVO);
    }

    /**
     * 用户退出
     * @return
     */
    @RequiresAuthentication
    @ApiOperation("用户退出")
    @GetMapping("/logout")
    public Result<Object> logout() {
        SecurityUtils.getSubject().logout();
        return Result.success(null, "退出成功");

    }

}
