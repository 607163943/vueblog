package top.hcode.blog.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel("文章分页VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleHomePageVO {
    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章描述")
    private String description;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("作者头像")
    private String authorAvatar;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;
}
