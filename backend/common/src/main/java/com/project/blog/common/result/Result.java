package com.project.blog.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.project.blog.common.constant.ResultCodeStatus;

@ApiModel("统一返回结果类")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    @ApiModelProperty("返回码")
    private Integer code;

    @ApiModelProperty("返回数据")
    private T data;

    @ApiModelProperty("返回信息")
    private String msg;

    public static <T> Result<T> of(Integer code, T data, String msg) {
        return new Result<>(code, data, msg);
    }

    public static <T> Result<T> success(T data,String success) {
        return Result.of(ResultCodeStatus.OK, data, success);
    }

    public static <T> Result<T> success(T data) {
        return Result.success(data, "success");
    }

    public static <T> Result<T> success() {
        return Result.success(null);
    }

    public static <T> Result<T> error(Integer code,String msg) {
        return Result.of(code, null, msg);
    }

    public static <T> Result<T> error(String msg) {
        return Result.error(ResultCodeStatus.SERVER_ERROR, msg);
    }

    public static <T> Result<T> error() {
        return Result.error(ResultCodeStatus.SERVER_ERROR, "error");
    }
}
