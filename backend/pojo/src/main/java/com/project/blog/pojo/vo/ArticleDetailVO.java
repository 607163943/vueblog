package com.project.blog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.blog.common.constant.JSONTemplateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel("文章详情VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVO {
    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String author;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("发布时间")
    @JsonFormat(pattern = JSONTemplateConstant.LOCAL_DATETIME_TEMPLATE)
    private LocalDateTime updateTime;
}
