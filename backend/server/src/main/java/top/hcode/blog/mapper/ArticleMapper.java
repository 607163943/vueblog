package top.hcode.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import top.hcode.blog.pojo.dto.BasePageDTO;
import top.hcode.blog.pojo.po.Article;
import top.hcode.blog.pojo.vo.ArticleHomePageVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-07-19
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页查询文章
     * @param page
     * @param basePageDTO
     * @return
     */
    IPage<ArticleHomePageVO> pageQuery(IPage<ArticleHomePageVO> page, @Param(Constants.WRAPPER) BasePageDTO basePageDTO);
}
