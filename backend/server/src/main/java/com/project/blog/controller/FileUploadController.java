package com.project.blog.controller;

import cn.hutool.core.lang.UUID;
import com.project.blog.common.result.Result;
import com.project.blog.service.IImageAssetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/file/upload")
@RequiredArgsConstructor
public class FileUploadController {
    private final IImageAssetService articleImgFileUploadService;

    /**
     * 上传图片
     *
     * @param file 上传的图片
     * @return 图片地址
     */
    @RequiresAuthentication
    @ApiOperation("上传图片")
    @PostMapping("/image")
    public Result<String> uploadImage(MultipartFile file, String tempId, Long userId) {
        String imageUrl = articleImgFileUploadService.uploadImage(file, tempId, userId);
        return Result.success(imageUrl);
    }

    /**
     * 获取UUID作为图片上传组名
     *
     * @return UUID
     */
    @RequiresAuthentication
    @GetMapping("/tempId")
    public Result<String> getUUID() {
        return Result.success(UUID.fastUUID().toString());
    }
}
