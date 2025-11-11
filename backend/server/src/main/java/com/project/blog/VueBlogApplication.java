package com.project.blog;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableFileStorage
@EnableScheduling
public class VueBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueBlogApplication.class, args);
    }

}
