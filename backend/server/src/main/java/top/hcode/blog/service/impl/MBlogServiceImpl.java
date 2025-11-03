package top.hcode.blog.service.impl;

import top.hcode.blog.pojo.po.Article;
import top.hcode.blog.mapper.MBlogMapper;
import top.hcode.blog.service.MBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-07-19
 */
@Service
public class MBlogServiceImpl extends ServiceImpl<MBlogMapper, Article> implements MBlogService {

}
