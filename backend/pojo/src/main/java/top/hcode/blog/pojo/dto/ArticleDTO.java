package top.hcode.blog.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("文章DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
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

    @ApiModelProperty("文章状态")
    private Integer status;
}
