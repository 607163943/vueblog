package com.project.blog.common.utils;

import java.util.*;
import java.util.regex.*;

public class MdObjectKeyPickerUtils {
    // 1) 抓取 Markdown 行内图片 URL：![alt](URL) 或 ![alt](<URL>)
    private static final Pattern MD_IMG_URL = Pattern.compile(
        "!\\[[^\\]]*\\]\\(\\s*<?([^\\s)<>]+)>?[^)]*\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
    );
    // 2) 从 URL 中提取 vueblog-img/ 之后的 object_key
    private static final Pattern KEY_FROM_URL = Pattern.compile(
        "/vueblog-img/([^?#]+)", Pattern.CASE_INSENSITIVE
    );

    public static Set<String> extractKeys(String markdown) {
        Set<String> keys = new LinkedHashSet<>();
        if (markdown == null || markdown.isEmpty()) return keys;

        Matcher m = MD_IMG_URL.matcher(markdown);
        while (m.find()) {
            String url = m.group(1);                  // 取出 URL
            // 去掉 <...> 包裹
            if (url.startsWith("<") && url.endsWith(">")) {
                url = url.substring(1, url.length() - 1);
            }
            // 仅用正则从 URL 中切出 key
            Matcher km = KEY_FROM_URL.matcher(url);
            if (km.find()) {
                String key = km.group(1).replaceAll("^/+", ""); // 防御性去掉前导斜杠
                if (!key.isEmpty()) keys.add(key);
            }
        }
        return keys;
    }

    // 简单演示
    public static void main(String[] args) {
        String md = "![articleimg.jpeg](http://127.0.0.1:19000/vueblog-img/2025/11/11/69133f20bf82b4fc12dd131f.jpeg)";
        System.out.println(extractKeys(md)); // [2025/11/11/69133f20bf82b4fc12dd131f.jpeg]
    }
}
