package com.project.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.blog.pojo.po.ImageAsset;
import org.springframework.web.multipart.MultipartFile;

public interface IImageAssetService extends IService<ImageAsset> {
    /**
     * 上传图片
     * @param file
     */
    String uploadImage(MultipartFile file,String tempId,Long userId);
}
