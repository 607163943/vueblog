package com.project.blog.pojo.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class User implements Serializable {
    // id
    private Long id;

    // 用户名
    private String username;

    // 头像
    private String avatar;

    // 邮箱
    private String email;

    // 密码
    private String password;

    // 状态
    private Integer status;

    // 创建时间
    private Date created;

    // 最后登录时间
    private Date lastLogin;

}
