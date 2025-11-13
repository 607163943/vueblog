package com.project.blog.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("文章DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    @ApiModelProperty("临时id")
    private String tempId;

    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("作者id")
    @NotNull(message = "作者id不能为空")
    private Long userId;

    @ApiModelProperty("文章标题")
    @NotBlank(message = "文章标题不能为空")
    private String title;

    @ApiModelProperty("文章描述")
    @NotBlank(message = "文章描述不能为空")
    private String description;

    @ApiModelProperty("文章内容")
    @NotBlank(message = "文章内容不能为空")
    private String content;

    @ApiModelProperty("文章状态")
    @NotNull(message = "文章状态不能为空")
    private Integer status;
}
