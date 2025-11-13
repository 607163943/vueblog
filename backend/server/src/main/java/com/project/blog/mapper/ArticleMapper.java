package com.project.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.project.blog.pojo.dto.BasePageDTO;
import com.project.blog.pojo.po.Article;
import com.project.blog.pojo.vo.ArticleDetailVO;
import com.project.blog.pojo.vo.ArticleHomePageVO;
import org.apache.ibatis.annotations.Param;

public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页查询文章
     * @param page 分页参数
     * @param basePageDTO 基础分页DTO
     * @return 首页分页查询文章结果
     */
    IPage<ArticleHomePageVO> pageQuery(IPage<ArticleHomePageVO> page, @Param(Constants.WRAPPER) BasePageDTO basePageDTO);

    /**
     * 查询文章详情
     * @param id 文章id
     * @return 文章详情VO
     */
    ArticleDetailVO detail(Long id);
}
