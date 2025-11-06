package com.project.blog.controller;


import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.project.blog.common.result.CommonResult;
import com.project.blog.common.result.Result;
import com.project.blog.mapper.UserMapper;
import com.project.blog.pojo.dto.LoginDTO;
import com.project.blog.pojo.dto.RegisterDTO;
import com.project.blog.pojo.po.User;
import com.project.blog.pojo.vo.LoginVO;
import com.project.blog.service.IUserService;

@Api(tags = "用户接口")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userServiceDao;
    private final IUserService userService;

    /**
     * 用户注册
     * @param registerDto
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public CommonResult register(@Validated @RequestBody RegisterDTO registerDto){
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", registerDto.getUsername());
        User user = userService.getOne(wrapper);
        if(user!=null){
            return CommonResult.errorResponse("注册失败,该用户名已被使用");
        }
        int insert = userServiceDao.insert(new User().setEmail(registerDto.getEmail())
                .setStatus(0)
                .setUsername(registerDto.getUsername())
                .setPassword(SecureUtil.md5(registerDto.getPassword())));
        if (insert == 1) {
            // 用户可以另一个接口
            return CommonResult.successResponse(null, "注册成功");
        }else{
            return CommonResult.errorResponse("注册失败");
        }
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
