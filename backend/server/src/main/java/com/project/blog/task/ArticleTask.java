package com.project.blog.task;

import com.project.blog.pojo.po.Article;
import com.project.blog.pojo.po.ImageAsset;
import com.project.blog.service.IArticleService;
import com.project.blog.service.IImageAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleTask {
    private final IImageAssetService imageAssetService;

    private final IArticleService articleService;

    /**
     * 定时任务：清理未引用图片
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void clearArticleImage() {
        log.info("开始清理图片");
        // 清理前一天多余图片
        imageAssetService.lambdaUpdate()
                .eq(ImageAsset::getStatus, 0)
                .lt(ImageAsset::getCreateTime, System.currentTimeMillis() - 24 * 60 * 60 * 1000)
                .remove();
        log.info("清理图片完成");
    }

    /**
     * 定时任务：清理已删除且事件查过24小时的文章
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void clearArticle() {
        log.info("开始清理文章");
        articleService.lambdaUpdate()
                .eq(Article::getDeleted,0)
                .lt(Article::getCreateTime, System.currentTimeMillis() - 24 * 60 * 60 * 1000)
                .remove();
        log.info("清理文章完成");
    }
}
