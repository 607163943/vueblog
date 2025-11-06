package com.project.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.blog.common.constant.CommonConstant;
import com.project.blog.common.result.PageResult;
import com.project.blog.common.utils.UserContext;
import com.project.blog.mapper.ArticleMapper;
import com.project.blog.pojo.dto.BasePageDTO;
import com.project.blog.pojo.dto.UserArticlePageDTO;
import com.project.blog.pojo.po.Article;
import com.project.blog.pojo.vo.ArticleHomePageVO;
import com.project.blog.pojo.vo.ArticleTablePageVO;
import com.project.blog.service.IArticleService;
import com.project.blog.service.IUserService;
import com.project.blog.pojo.po.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        // TODO 改用join SQL实现

        // 分页查询用户文章
        page = this.lambdaQuery()
                .eq(userId != null, Article::getUserId, userId)
                .like(StrUtil.isNotBlank(userArticlePageDTO.getTitle()), Article::getTitle, userArticlePageDTO.getTitle())
                .eq(userArticlePageDTO.getStatus() != null, Article::getStatus, userArticlePageDTO.getStatus())
                .orderByDesc(Article::getUpdateTime)
                .page(page);

        List<ArticleTablePageVO> articleTablePageVOS = BeanUtil.copyToList(page.getRecords(), ArticleTablePageVO.class);

        // 获取作者id集合
        List<Long> userIds = articleTablePageVOS.stream().map(ArticleTablePageVO::getUserId).collect(Collectors.toList());

        // 查询作者并转成作者id和作者名的map
        Map<Long, String> userNameMap = userService.lambdaQuery().in(User::getId, userIds).list()
                .stream().collect(Collectors.toMap(User::getId, User::getUsername));

        // 添加文章作者名
        articleTablePageVOS.stream().map(articleTablePageVO -> {
            articleTablePageVO.setAuthor(userNameMap.getOrDefault(articleTablePageVO.getUserId(), CommonConstant.DEFAULT_AUTHOR));
            return articleTablePageVO;
        });

        return PageResult.<List<ArticleTablePageVO>>builder()
                .result(articleTablePageVOS)
                .total(page.getTotal())
                .build();
    }
}
