package com.project.blog.pojo.po;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAsset extends BasePO {
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
}
