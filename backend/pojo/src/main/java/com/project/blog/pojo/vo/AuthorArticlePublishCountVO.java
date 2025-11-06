package com.project.blog.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("作者文章发布统计VO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorArticlePublishCountVO {
    @ApiModelProperty("发布文章数")
    private Integer publishCount;

    @ApiModelProperty("最近发布文章数，统计近一周数据")
    private Integer recentPublishCount;
}
