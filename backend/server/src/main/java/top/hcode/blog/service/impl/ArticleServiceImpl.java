package top.hcode.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.blog.common.result.PageResult;
import top.hcode.blog.mapper.ArticleMapper;
import top.hcode.blog.pojo.dto.BasePageDTO;
import top.hcode.blog.pojo.po.Article;
import top.hcode.blog.pojo.vo.ArticlePageVO;
import top.hcode.blog.service.IArticleService;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-07-19
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    /**
     * 分页查询文章
     * @param basePageDTO
     * @return
     */
    @Override
    public PageResult<List<ArticlePageVO>> pageQuery(BasePageDTO basePageDTO) {
        IPage<ArticlePageVO> page = new Page<>(basePageDTO.getPage(), basePageDTO.getPageSize());
        page=this.baseMapper.pageQuery(page, basePageDTO);

        return PageResult.<List<ArticlePageVO>>builder()
                .total(page.getTotal())
                .result(page.getRecords())
                .build();
    }
}
