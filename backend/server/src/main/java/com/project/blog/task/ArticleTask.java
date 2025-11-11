package com.project.blog.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ArticleTask {
    @Scheduled(cron = "0/5 * * * * ? ")
    public void testTask() {
        System.out.println("这是一个定时任务");
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void clearArticleImage() {
        // 清理前一天多余图片
    }
}
