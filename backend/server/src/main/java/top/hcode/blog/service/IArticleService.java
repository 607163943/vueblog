package top.hcode.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.blog.common.result.PageResult;
import top.hcode.blog.pojo.dto.BasePageDTO;
import top.hcode.blog.pojo.po.Article;
import top.hcode.blog.pojo.vo.ArticlePageVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-07-19
 */
public interface IArticleService extends IService<Article> {
    /**
     * 分页查询文章
     * @param basePageDTO
     * @return
     */
    PageResult<List<ArticlePageVO>> pageQuery(BasePageDTO basePageDTO);
}
