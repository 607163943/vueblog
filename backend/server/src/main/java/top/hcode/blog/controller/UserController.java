package top.hcode.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.blog.common.result.CommonResult;
import top.hcode.blog.mapper.MBlogMapper;
import top.hcode.blog.pojo.po.Article;
import top.hcode.blog.pojo.po.User;
import top.hcode.blog.service.MUserService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-07-19
 */
@RestController
@RequestMapping("/blog/user")
@RequiredArgsConstructor
public class UserController {
    private final MUserService mUserService;

    private final MBlogMapper mBlogService;

    @RequiresAuthentication //需要登录认证才能访问
    @GetMapping("/index")
    public Object test(){
        User user = mUserService.getById(1L);
        return CommonResult.successResponse(user, "操作成功");
    }

    @GetMapping("/{username}")
    public CommonResult getUserBlog(@PathVariable("username")String username){
        QueryWrapper<User> wrapper1 = new QueryWrapper<User>().eq("username", username);

        User user = mUserService.getOne(wrapper1);

        if (user==null){
            return  CommonResult.errorResponse("该用户不存在");
        }


        QueryWrapper<Article> wrapper2 = new QueryWrapper<Article>().eq("user_id", user.getId()).orderByDesc("gmt_create");
        List<Article> articles = mBlogService.selectList(wrapper2);
        return  CommonResult.successResponse(articles, "查询成功");
    }


}
