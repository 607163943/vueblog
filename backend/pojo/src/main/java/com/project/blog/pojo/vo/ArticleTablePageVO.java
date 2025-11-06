package com.project.blog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.project.blog.common.constant.JSONTemplateConstant;

import java.time.LocalDateTime;

@ApiModel("文章表格分页VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTablePageVO {
    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("作者id")
    private Long userId;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章描述")
    private String description;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("文章状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = JSONTemplateConstant.LOCAL_DATETIME_TEMPLATE)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = JSONTemplateConstant.LOCAL_DATETIME_TEMPLATE)
    private LocalDateTime updateTime;
}
