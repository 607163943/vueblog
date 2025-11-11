package com.project.blog.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.project.blog.common.constant.ArticleExceptionMessage;
import com.project.blog.common.exception.ArticleException;
import com.project.blog.common.result.PageResult;
import com.project.blog.common.result.Result;
import com.project.blog.common.utils.UserContext;
import com.project.blog.pojo.dto.ArticleDTO;
import com.project.blog.pojo.dto.BasePageDTO;
import com.project.blog.pojo.dto.UserArticlePageDTO;
import com.project.blog.pojo.po.Article;
import com.project.blog.pojo.vo.*;
import com.project.blog.service.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "文章接口")
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    IArticleService articleService;

    /**
     * 分页查询文章
     * @param basePageDTO
     * @return
     */
    @ApiOperation("分页查询文章")
    @GetMapping
    public Result<PageResult<List<ArticleHomePageVO>>> pageQuery(BasePageDTO basePageDTO) {
        Integer pageNum = basePageDTO.getPage();
        if(pageNum == null || pageNum < 1) basePageDTO.setPage(1);

        PageResult<List<ArticleHomePageVO>> pageResult = articleService.pageQuery(basePageDTO);
        return Result.success(pageResult,"获取成功");
    }

    /**
     * 查询最新文章
     * @return
     */
    @ApiOperation("查询最新文章")
    @GetMapping("/new")
    public Result<List<ArticleNewVO>> getNewArticle() {
        List<ArticleNewVO> articleNewVOS = articleService.getNewArticle();
        return Result.success(articleNewVOS,"查询成功");
    }

    /**
     * 查询文章
     * @param id
     * @return
     */
    @ApiOperation("查询文章")
    @GetMapping("/{id}")
    public Result<ArticleVO> getArticle(@PathVariable("id") Long id) {
        Article article = articleService.getById(id);
        if(article==null) {
            throw new ArticleException(ArticleExceptionMessage.ARTICLE_NOT_EXIST);
        }
        ArticleVO articleVO = BeanUtil.copyProperties(article, ArticleVO.class);
        return Result.success(articleVO,"查询成功");
    }

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    @ApiOperation("查询文章详情")
    @GetMapping("/detail/{id}")
    public Result<ArticleDetailVO> detail(@PathVariable("id") Long id) {
        ArticleDetailVO articleDetailVO = articleService.detail(id);

        return Result.success(articleDetailVO,"查询成功");
    }

    /**
     * 修改文章
     * @param articleDTO
     * @return
     */
    @ApiOperation("修改文章")
    @RequiresAuthentication
    @PutMapping
    public Result<Object> update(@Validated @RequestBody ArticleDTO articleDTO) {
        Article article = BeanUtil.copyProperties(articleDTO, Article.class);
        articleService.updateById(article);
        return Result.success( null,"操作成功");
    }

    /**
     * 添加文章
     * @param articleDTO
     * @return
     */
    @ApiOperation("添加文章")
    @RequiresAuthentication
    @PostMapping
    public Result<Object> add(@Validated @RequestBody ArticleDTO articleDTO) {
        // 判断文章创作者和登录用户是否为同一人
        if(!articleDTO.getUserId().equals(UserContext.getUserId())) {
            throw new ArticleException(ArticleExceptionMessage.USER_NOT_SAME);
        }
        Article article = BeanUtil.copyProperties(articleDTO, Article.class);
        articleService.save(article);
        return Result.success( null,"操作成功");
    }

    /**
     * 批量删除文章
     * @param ids
     * @return
     */
    @ApiOperation("批量删除文章")
    @RequiresAuthentication
    @DeleteMapping
    public Result<Object> deleteByIds(@RequestParam("ids") List<Long> ids) {
        if(CollectionUtil.isNotEmpty(ids)) {
            articleService.removeByIds(ids);
        }
        return Result.success( null,"删除成功");
    }

    /**
     * 分页查询用户文章
     * @param username
     * @param userArticlePageDTO
     * @return
     */
    @ApiOperation("分页查询用户文章")
    @RequiresAuthentication
    @GetMapping("/user/{username}")
    public Result<PageResult<List<ArticleTablePageVO>>> userArticlePageQuery(@PathVariable("username") String username, UserArticlePageDTO userArticlePageDTO){
        PageResult<List<ArticleTablePageVO>> articleTablePageVOPageResult=articleService.userArticlePageQuery(userArticlePageDTO);
        return  Result.success(articleTablePageVOPageResult,"查询成功");
    }

    /**
     * 统计作者创作信息
     * @param authorId
     * @return
     */
    @ApiOperation("统计作者创作信息")
    @GetMapping("/author/info/{authorId}")
    public Result<AuthorArticlePublishCountVO> countAuthorArticlePublishCount(@PathVariable("authorId") Long authorId) {
        AuthorArticlePublishCountVO authorArticlePublishCountVO = articleService.countAuthorArticlePublishCount(authorId);
        return Result.success(authorArticlePublishCountVO,"查询成功");
    }
}