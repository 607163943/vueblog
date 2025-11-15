package com.project.blog.common.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ignore {
    // 路径
    private String uri;
    // 请求方式
    private List<String> methods;
}
