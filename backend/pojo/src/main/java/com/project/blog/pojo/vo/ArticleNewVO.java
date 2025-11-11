package com.project.blog.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("最新文章VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleNewVO {
    @ApiModelProperty("文章id")
    private Long id;
    @ApiModelProperty("文章标题")
    private String title;
}
