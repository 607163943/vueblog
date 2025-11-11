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
     * @param basePageDTO
     * @return
     */
    PageResult<List<ArticleHomePageVO>> pageQuery(BasePageDTO basePageDTO);

    /**
     * 分页查询用户文章
     * @param userArticlePageDTO
     * @return
     */
    PageResult<List<ArticleTablePageVO>> userArticlePageQuery(UserArticlePageDTO userArticlePageDTO);

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    ArticleDetailVO detail(Long id);

    /**
     * 统计作者创作信息
     * @param authorId
     * @return
     */
    AuthorArticlePublishCountVO countAuthorArticlePublishCount(Long authorId);

    /**
     * 查询最新文章
     * @return
     */
    List<ArticleNewVO> getNewArticle();

    /**
     * 修改文章
     * @param articleDTO
     */
    void updateArticle(ArticleDTO articleDTO);

    /**
     * 添加文章
     * @param articleDTO
     */
    void add(ArticleDTO articleDTO);
}
