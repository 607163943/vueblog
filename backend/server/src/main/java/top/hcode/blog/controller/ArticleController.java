package top.hcode.blog.controller;


import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hcode.blog.common.result.CommonResult;
import top.hcode.blog.common.result.PageResult;
import top.hcode.blog.common.result.Result;
import top.hcode.blog.pojo.dto.BasePageDTO;
import top.hcode.blog.pojo.po.Article;
import top.hcode.blog.pojo.vo.ArticlePageVO;
import top.hcode.blog.service.IArticleService;
import top.hcode.blog.shiro.ShiroUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-07-19
 */
@Api(tags = "文章接口")
@RestController
@RequestMapping
public class ArticleController {
    @Resource
    IArticleService articleService;

    /**
     * 分页查询文章
     * @param basePageDTO
     * @return
     */
    @ApiOperation("分页查询文章")
    @GetMapping("/article")
    public Result<PageResult<List<ArticlePageVO>>> pageQuery(BasePageDTO basePageDTO) {
        Integer pageNum = basePageDTO.getPage();
        if(pageNum == null || pageNum < 1) basePageDTO.setPage(1);

        PageResult<List<ArticlePageVO>> pageResult = articleService.pageQuery(basePageDTO);
        return Result.success(pageResult,"获取成功");
    }

    @GetMapping("/blog/{id}")
    public CommonResult detail(@PathVariable(name = "id") Long id) {
        Article blog = articleService.getById(id);
        Assert.notNull(blog, "该博文已删除！");
        return CommonResult.successResponse(blog,"查询成功");
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public CommonResult edit(@Validated @RequestBody Article blog) {
        Article temp = null;
        if(blog.getId() != null) {
            temp = articleService.getById(blog.getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
        } else {
            temp = new Article();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreateTime(LocalDateTime.now());
            temp.setStatus(0);
        }
        BeanUtil.copyProperties(blog, temp, "id", "userId", "gmtCreate", "status");
        articleService.saveOrUpdate(temp);
        return CommonResult.successResponse( null,"操作成功");
    }

    @RequiresAuthentication
    @GetMapping("/blog/delete/{id}")
    public CommonResult delete(@PathVariable(name = "id") Long id) {
        boolean result = articleService.removeById(id);
        Assert.isTrue(result, "删除失败！该博文不存在！");
        return CommonResult.successResponse( null,"删除成功");
    }
}