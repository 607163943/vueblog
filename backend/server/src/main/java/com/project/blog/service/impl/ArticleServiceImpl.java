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
import com.project.blog.mapper.ArticleMapper;
import com.project.blog.pojo.dto.ArticleDTO;
import com.project.blog.pojo.dto.BasePageDTO;
import com.project.blog.pojo.dto.UserArticlePageDTO;
import com.project.blog.pojo.po.Article;
import com.project.blog.pojo.po.ImageAsset;
import com.project.blog.pojo.vo.*;
import com.project.blog.service.IArticleService;
import com.project.blog.service.IImageAssetService;
import com.project.blog.shiro.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Resource
    private IImageAssetService imageAssetService;

    // 这里需要获取自己类的代理对象，否则会导致事务失效
    @Lazy
    @Resource
    private IArticleService articleService;

    /**
     * 分页查询文章
     *
     * @param basePageDTO 基础分页DTO
     * @return 首页分页查询文章结果
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
     * @param userArticlePageDTO 用户文章分页DTO
     * @return 用户文章分页查询结果
     */
    @Override
    public PageResult<List<ArticleTablePageVO>> userArticlePageQuery(UserArticlePageDTO userArticlePageDTO) {
        IPage<Article> page = new Page<>(userArticlePageDTO.getPage(), userArticlePageDTO.getPageSize());

        UserAccount userAccount =(UserAccount) SecurityUtils.getSubject().getPrincipal();
        Long userId = userAccount.getId();

        // 分页查询用户文章
        page = this.lambdaQuery()
                .eq(Article::getUserId, userId)
                .like(StrUtil.isNotBlank(userArticlePageDTO.getTitle()), Article::getTitle, userArticlePageDTO.getTitle())
                .eq(userArticlePageDTO.getStatus() != null, Article::getStatus, userArticlePageDTO.getStatus())
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
     * @param id 文章id
     * @return 文章详情VO
     */
    @Override
    public ArticleDetailVO detail(Long id) {
        return this.baseMapper.detail(id);
    }

    /**
     * 统计作者创作信息
     *
     * @param authorId 作者id
     * @return 作者创作信息VO
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
     *
     * @return 最新文章VO集合
     */
    @Override
    public List<ArticleNewVO> getNewArticle() {
        IPage<Article> page = new Page<>(1, 5);
        page = this.lambdaQuery().orderByDesc(Article::getUpdateTime).page(page);
        return BeanUtil.copyToList(page.getRecords(), ArticleNewVO.class);
    }

    /**
     * 修改文章
     *
     * @param articleDTO 文章DTO
     */
    @Transactional
    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        // 标记使用图片
        Set<String> strings = MdObjectKeyPickerUtils.extractKeys(articleDTO.getContent());
        imageAssetService.lambdaUpdate()
                .in(CollectionUtil.isNotEmpty(strings), ImageAsset::getObjectKey, strings)
                .eq(StrUtil.isNotBlank(articleDTO.getTempId()), ImageAsset::getTempId, articleDTO.getTempId())
                .set(ImageAsset::getStatus, 1)
                .set(ImageAsset::getTempId, null)
                .update();
        log.info("图片使用状态修改完毕,tempId={}", articleDTO.getTempId());

        Article article = BeanUtil.copyProperties(articleDTO, Article.class);

        articleService.updateById(article);
        log.info("文章修改完毕,articleId={}", article.getId());
    }

    /**
     * 添加文章
     *
     * @param articleDTO 文章DTO
     */
    @Transactional
    @Override
    public void add(ArticleDTO articleDTO) {
        // 判断文章创作者和登录用户是否为同一人
        UserAccount userAccount = (UserAccount) SecurityUtils.getSubject().getPrincipal();
        if (!articleDTO.getUserId().equals(userAccount.getId())) {
            log.warn("用户和文章作者不匹配,articleDTO={},userId={}", articleDTO, userAccount.getId());
            throw new ArticleException(ArticleExceptionMessage.USER_NOT_SAME);
        }
        Article article = BeanUtil.copyProperties(articleDTO, Article.class);
        articleService.save(article);
        log.info("文章添加完毕,articleId={}", article.getId());

        // 标记使用图片
        Set<String> strings = MdObjectKeyPickerUtils.extractKeys(articleDTO.getContent());
        imageAssetService.lambdaUpdate()
                .in(CollectionUtil.isNotEmpty(strings), ImageAsset::getObjectKey, strings)
                .eq(StrUtil.isNotBlank(articleDTO.getTempId()), ImageAsset::getTempId, articleDTO.getTempId())
                .set(ImageAsset::getStatus, 1)
                .set(ImageAsset::getArticleId, article.getId())
                .set(ImageAsset::getTempId, null)
                .update();

        log.info("图片使用状态修改完毕,articleDTO={}", articleDTO);
    }
}
