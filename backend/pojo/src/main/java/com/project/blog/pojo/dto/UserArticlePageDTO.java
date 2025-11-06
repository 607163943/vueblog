package com.project.blog.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel("用户文章分页DTO")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserArticlePageDTO extends BasePageDTO{
    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章状态")
    private Integer status;
}
