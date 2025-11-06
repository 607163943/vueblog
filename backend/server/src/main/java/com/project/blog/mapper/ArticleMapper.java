package com.project.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import com.project.blog.pojo.dto.BasePageDTO;
import com.project.blog.pojo.po.Article;
import com.project.blog.pojo.vo.ArticleHomePageVO;

public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页查询文章
     * @param page
     * @param basePageDTO
     * @return
     */
    IPage<ArticleHomePageVO> pageQuery(IPage<ArticleHomePageVO> page, @Param(Constants.WRAPPER) BasePageDTO basePageDTO);
}
