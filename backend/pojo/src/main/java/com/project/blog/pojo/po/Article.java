package com.project.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Article extends BasePO {
    // 作者id
    private Long userId;

    // 标题
    private String title;

    // 描述
    private String description;

    // 状态 0发布 1草稿 2删除
    private Integer status;

    // 内容
    private String content;

    @TableLogic
    // 逻辑删除标志
    private Integer deleted;


}
