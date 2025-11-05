package top.hcode.blog.pojo.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {
    // id
    private Long id;

    // 用户名
    @NotBlank(message = "用户名不能为空")
    private String username;

    // 头像
    private String avatar;

    // 邮箱
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
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
