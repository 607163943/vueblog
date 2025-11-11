package com.project.blog.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAsset {
    // id
    private Long id;
    // 文件名
    private String objectKey;
    // 文件路径
    private String fileUrl;
    // 用户id
    private Long userId;
    // 文章id
    private Long articleId;
    // 状态
    private Integer status;
    // 临时id
    private String tempId;
    // 创建时间
    private LocalDateTime createTime;
    // 修改事件
    private LocalDateTime updateTime;
}
