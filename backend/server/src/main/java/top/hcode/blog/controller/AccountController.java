package top.hcode.blog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.blog.common.result.CommonResult;
import top.hcode.blog.common.result.Result;
import top.hcode.blog.common.utils.JWTUtils;
import top.hcode.blog.mapper.MUserMapper;
import top.hcode.blog.pojo.dto.LoginDTO;
import top.hcode.blog.pojo.dto.RegisterDTO;
import top.hcode.blog.pojo.po.User;
import top.hcode.blog.service.MUserService;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Himit_ZH
 * @Date: 2020/7/20 00:25
 * @Description:
 */
@Api(tags = "账号接口")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final MUserMapper userServiceDao;
    private final MUserService userService;
    private final JWTUtils jwtUtils;

    @PostMapping("/register")
    public CommonResult register(@Validated @RequestBody RegisterDTO registerDto, HttpServletResponse response){
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

    @PostMapping("/login")
    public CommonResult login(@Validated @RequestBody LoginDTO loginDto, HttpServletResponse response){
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", loginDto.getUsername());
        User user = userService.getOne(wrapper);
        Assert.notNull(user,"用户不存在");
        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return CommonResult.errorResponse("密码不正确");
        }
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwt); //放到信息头部
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        // 用户可以另一个接口
        return CommonResult.successResponse(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map(),"登录成功"
        );
    }

    // 退出
    @GetMapping("/logout")
    @RequiresAuthentication
    public Result<Object> logout() {
        SecurityUtils.getSubject().logout();
        return Result.success(null, "退出成功");

    }

}