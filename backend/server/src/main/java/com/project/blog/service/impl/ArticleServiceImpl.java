package com.project.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.blog.common.constant.ArticleStatus;
import com.project.blog.common.result.PageResult;
import com.project.blog.common.utils.UserContext;
import com.project.blog.mapper.ArticleMapper;
import com.project.blog.pojo.dto.BasePageDTO;
import com.project.blog.pojo.dto.UserArticlePageDTO;
import com.project.blog.pojo.po.Article;
import com.project.blog.pojo.vo.ArticleDetailVO;
import com.project.blog.pojo.vo.ArticleHomePageVO;
import com.project.blog.pojo.vo.ArticleTablePageVO;
import com.project.blog.pojo.vo.AuthorArticlePublishCountVO;
import com.project.blog.service.IArticleService;
import com.project.blog.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    private final IUserService userService;

    /**
     * 分页查询文章
     *
     * @param basePageDTO
     * @return
     */
    @Override
    public PageResult<List<ArticleHomePageVO>> pageQuery(BasePageDTO basePageDTO) {
        IPage<ArticleHomePageVO> page = new Page<>(basePageDTO.getPage(), basePageDTO.getPageSize());
        page = this.baseMapper.pageQuery(page, basePageDTO);

        return PageResult.<List<ArticleHomePageVO>>builder()
                .total(page.getTotal())
                .result(page.getRecords())
                .build();
    }

    /**
     * 分页查询用户文章
     *
     * @param userArticlePageDTO
     * @return
     */
    @Override
    public PageResult<List<ArticleTablePageVO>> userArticlePageQuery(UserArticlePageDTO userArticlePageDTO) {
        IPage<Article> page = new Page<>(userArticlePageDTO.getPage(), userArticlePageDTO.getPageSize());

        Long userId = UserContext.getUserId();

        // 分页查询用户文章
        page=this.lambdaQuery()
                .eq(userId!=null, Article::getUserId, userId)
                .like(StrUtil.isNotBlank(userArticlePageDTO.getTitle()), Article::getTitle, userArticlePageDTO.getTitle())
                .eq(userArticlePageDTO.getStatus()!=null, Article::getStatus, userArticlePageDTO.getStatus())
                .orderByDesc(Article::getUpdateTime)
                .page(page);

        List<ArticleTablePageVO> articleTablePageVOS = BeanUtil.copyToList(page.getRecords(), ArticleTablePageVO.class);

        return PageResult.<List<ArticleTablePageVO>>builder()
                .result(articleTablePageVOS)
                .total(page.getTotal())
                .build();
    }

    /**
     * 查询文章详情
     *
     * @param id
     * @return
     */
    @Override
    public ArticleDetailVO detail(Long id) {
        return this.baseMapper.detail(id);
    }

    /**
     * 统计作者创作信息
     * @param authorId
     * @return
     */
    @Override
    public AuthorArticlePublishCountVO countAuthorArticlePublishCount(Long authorId) {
        // 统计作者发布文章数
        Integer publishCount = this.lambdaQuery()
                .eq(authorId != null, Article::getUserId, authorId)
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .count();

        // 统计作者最近一周发布文章数
        Integer recentPublishCount = this.lambdaQuery()
                .eq(authorId != null, Article::getUserId, authorId)
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .ge(Article::getCreateTime, System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)
                .count();

        return AuthorArticlePublishCountVO.builder()
                .publishCount(publishCount)
                .recentPublishCount(recentPublishCount)
                .build();
    }
}
