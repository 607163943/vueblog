package top.hcode.blog.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("分页结果")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    @ApiModelProperty("总条数")
    private Long total;

    @ApiModelProperty("结果集")
    private T result;
}
