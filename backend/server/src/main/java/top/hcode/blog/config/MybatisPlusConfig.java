package top.hcode.blog.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: Himit_ZH
 * @Date: 2020/7/19 21:04
 * @Description:
 */
@Configuration
@EnableTransactionManagement
@MapperScan("top.hcode.blog.mapper")
public class MybatisPlusConfig {


    /**
     * mybatis-plus分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}