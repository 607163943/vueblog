package top.hcode.blog.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.blog.common.result.CommonResult;
import top.hcode.blog.pojo.po.User;
import top.hcode.blog.service.MUserService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-07-19
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final MUserService mUserService;

    @RequiresAuthentication //需要登录认证才能访问
    @GetMapping("/index")
    public Object test(){
        User user = mUserService.getById(1L);
        return CommonResult.successResponse(user, "操作成功");
    }
}
