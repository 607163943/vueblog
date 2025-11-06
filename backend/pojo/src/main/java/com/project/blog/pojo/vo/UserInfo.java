package com.project.blog.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户邮箱")
    private String email;
}
