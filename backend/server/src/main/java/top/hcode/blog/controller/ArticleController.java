package top.hcode.blog.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hcode.blog.common.constant.ArticleExceptionMessage;
import top.hcode.blog.common.exception.ArticleException;
import top.hcode.blog.common.result.PageResult;
import top.hcode.blog.common.result.Result;
import top.hcode.blog.common.utils.UserContext;
import top.hcode.blog.pojo.dto.ArticleDTO;
import top.hcode.blog.pojo.dto.BasePageDTO;
import top.hcode.blog.pojo.dto.UserArticlePageDTO;
import top.hcode.blog.pojo.po.Article;
import top.hcode.blog.pojo.vo.ArticleHomePageVO;
import top.hcode.blog.pojo.vo.ArticleTablePageVO;
import top.hcode.blog.pojo.vo.ArticleVO;
import top.hcode.blog.service.IArticleService;

import javax.annotation.Resource;
import java.util.List;

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
    public Result<PageResult<List<ArticleHomePageVO>>> pageQuery(BasePageDTO basePageDTO) {
        Integer pageNum = basePageDTO.getPage();
        if(pageNum == null || pageNum < 1) basePageDTO.setPage(1);

        PageResult<List<ArticleHomePageVO>> pageResult = articleService.pageQuery(basePageDTO);
        return Result.success(pageResult,"获取成功");
    }

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    @ApiOperation("查询文章详情")
    @GetMapping("/article/{id}")
    public Result<ArticleVO> detail(@PathVariable("id") Long id) {
        Article article = articleService.getById(id);
        if(article==null) {
            throw new ArticleException(ArticleExceptionMessage.ARTICLE_NOT_EXIST);
        }
        ArticleVO articleVO = BeanUtil.copyProperties(article, ArticleVO.class);
        return Result.success(articleVO,"查询成功");
    }

    /**
     * 修改文章
     * @param articleDTO
     * @return
     */
    @ApiOperation("修改文章")
    @RequiresAuthentication
    @PutMapping("/article/update")
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
    @PostMapping("/article/add")
    public Result<Object> add(@Validated @RequestBody ArticleDTO articleDTO) {
        // 判断文章创作者和登录用户是否为同一人
        if(!articleDTO.getUserId().equals(UserContext.getUserId())) {
            throw new ArticleException(ArticleExceptionMessage.USER_NOT_SAME);
        }
        Article article = BeanUtil.copyProperties(articleDTO, Article.class);
        articleService.saveOrUpdate(article);
        return Result.success( null,"操作成功");
    }

    /**
     * 批量删除文章
     * @param ids
     * @return
     */
    @ApiOperation("批量删除文章")
    @RequiresAuthentication
    @DeleteMapping("/article")
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
    @GetMapping("/article/user/{username}")
    public Result<PageResult<List<ArticleTablePageVO>>> userArticlePageQuery(@PathVariable("username") String username, UserArticlePageDTO userArticlePageDTO){
        PageResult<List<ArticleTablePageVO>> articleTablePageVOPageResult=articleService.userArticlePageQuery(userArticlePageDTO);
        return  Result.success(articleTablePageVOPageResult,"查询成功");
    }
}