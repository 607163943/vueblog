package top.hcode.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    /**
     * 配置swagger
     * @return
     */
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.hcode.blog.controller"))
                .build();
    }

    /**
     * 创建api文档
     * @return
     */
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("HCODE小站项目测试文档")
                .description("HCODE小站项目测试文档")
                .version("1.0.0")
                .build();
    }
}
