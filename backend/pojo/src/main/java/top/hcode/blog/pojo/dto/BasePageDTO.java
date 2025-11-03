package top.hcode.blog.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("基础分页DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasePageDTO {
    @ApiModelProperty("当前页码")
    private Integer page;

    @ApiModelProperty("每页数量")
    private Integer pageSize;
}
