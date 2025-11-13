package com.project.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.blog.common.constant.ArticleExceptionMessage;
import com.project.blog.common.constant.ArticleStatus;
import com.project.blog.common.exception.ArticleException;
import com.project.blog.common.result.PageResult;
import com.project.blog.common.utils.MdObjectKeyPickerUtils;
import com.project.blog.common.utils.UserContext;
import com.project.blog.mapper.ArticleMapper;
import com.project.blog.pojo.dto.ArticleDTO;
import com.project.blog.pojo.dto.BasePageDTO;
import com.project.blog.pojo.dto.UserArticlePageDTO;
import com.project.blog.pojo.po.Article;
import com.project.blog.pojo.po.ImageAsset;
import com.project.blog.pojo.vo.*;
import com.project.blog.service.IArticleService;
import com.project.blog.service.IImageAssetService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Resource
    private IImageAssetService imageAssetService;

    @Lazy
    @Resource
    private IArticleService articleService;

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

    /**
     * 查询最新文章
     * @return
     */
    @Override
    public List<ArticleNewVO> getNewArticle() {
        IPage<Article> page = new Page<>(1, 5);
        page = this.lambdaQuery().orderByDesc(Article::getUpdateTime).page(page);
        List<ArticleNewVO> articleNewVOS = BeanUtil.copyToList(page.getRecords(), ArticleNewVO.class);
        return articleNewVOS;
    }

    /**
     * 修改文章
     * @param articleDTO
     */
    @Transactional
    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        // 标记使用图片
        Set<String> strings = MdObjectKeyPickerUtils.extractKeys(articleDTO.getContent());
        imageAssetService.lambdaUpdate()
                .in(CollectionUtil.isNotEmpty(strings), ImageAsset::getObjectKey, strings)
                .eq(StrUtil.isNotBlank(articleDTO.getTempId()), ImageAsset::getTempId, articleDTO.getTempId())
                .set(ImageAsset::getStatus,1)
                .set(ImageAsset::getTempId,null)
                .update();

        Article article = BeanUtil.copyProperties(articleDTO, Article.class);

        articleService.updateById(article);
    }

    /**
     * 添加文章
     * @param articleDTO
     */
    @Transactional
    @Override
    public void add(ArticleDTO articleDTO) {
        // 判断文章创作者和登录用户是否为同一人
        if(!articleDTO.getUserId().equals(UserContext.getUserId())) {
            throw new ArticleException(ArticleExceptionMessage.USER_NOT_SAME);
        }
        Article article = BeanUtil.copyProperties(articleDTO, Article.class);
        articleService.save(article);

        // 标记使用图片
        Set<String> strings = MdObjectKeyPickerUtils.extractKeys(articleDTO.getContent());
        imageAssetService.lambdaUpdate()
                .in(CollectionUtil.isNotEmpty(strings), ImageAsset::getObjectKey, strings)
                .eq(StrUtil.isNotBlank(articleDTO.getTempId()), ImageAsset::getTempId, articleDTO.getTempId())
                .set(ImageAsset::getStatus,1)
                .set(ImageAsset::getArticleId,article.getId())
                .set(ImageAsset::getTempId,null)
                .update();
    }
}
