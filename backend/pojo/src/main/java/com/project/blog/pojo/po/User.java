package com.project.blog.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BasePO {
    // 用户名
    private String username;

    // 头像
    private String avatar;

    // 邮箱
    private String email;

    // 密码
    private String password;

    // 密码盐
    private String salt;

    // 状态
    private Integer status;

    // 最后登录时间
    private LocalDateTime lastLoginTime;
}
