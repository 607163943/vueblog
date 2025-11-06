package com.project.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.blog.common.result.PageResult;
import com.project.blog.pojo.dto.BasePageDTO;
import com.project.blog.pojo.dto.UserArticlePageDTO;
import com.project.blog.pojo.po.Article;
import com.project.blog.pojo.vo.ArticleHomePageVO;
import com.project.blog.pojo.vo.ArticleTablePageVO;

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
}
