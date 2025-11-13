package com.project.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.blog.common.result.PageResult;
import com.project.blog.pojo.dto.ArticleDTO;
import com.project.blog.pojo.dto.BasePageDTO;
import com.project.blog.pojo.dto.UserArticlePageDTO;
import com.project.blog.pojo.po.Article;
import com.project.blog.pojo.vo.*;

import java.util.List;

public interface IArticleService extends IService<Article> {
    /**
     * 分页查询文章
     * @param basePageDTO 基础分页DTO
     * @return 首页分页查询文章结果
     */
    PageResult<List<ArticleHomePageVO>> pageQuery(BasePageDTO basePageDTO);

    /**
     * 分页查询用户文章
     * @param userArticlePageDTO 用户文章分页DTO
     * @return 用户文章分页查询结果
     */
    PageResult<List<ArticleTablePageVO>> userArticlePageQuery(UserArticlePageDTO userArticlePageDTO);

    /**
     * 查询文章详情
     * @param id 文章id
     * @return 文章详情VO
     */
    ArticleDetailVO detail(Long id);

    /**
     * 统计作者创作信息
     * @param authorId 作者id
     * @return 作者创作信息VO
     */
    AuthorArticlePublishCountVO countAuthorArticlePublishCount(Long authorId);

    /**
     * 查询最新文章
     * @return 最新文章VO集合
     */
    List<ArticleNewVO> getNewArticle();

    /**
     * 修改文章
     * @param articleDTO 文章DTO
     */
    void updateArticle(ArticleDTO articleDTO);

    /**
     * 添加文章
     * @param articleDTO 文章DTO
     */
    void add(ArticleDTO articleDTO);
}
