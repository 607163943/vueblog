package com.project.blog.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("文章VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVO {
    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("作者id")
    private Long userId;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章描述")
    private String description;

    @ApiModelProperty("文章内容")
    private String content;
}
