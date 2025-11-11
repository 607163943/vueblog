package com.project.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.blog.common.constant.FileUploadExceptionMessage;
import com.project.blog.common.exception.FileUploadException;
import com.project.blog.mapper.ImageAssetMapper;
import com.project.blog.pojo.po.ImageAsset;
import com.project.blog.service.IImageAssetService;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ImageAssetServiceImpl extends ServiceImpl<ImageAssetMapper, ImageAsset> implements IImageAssetService {
    private final FileStorageService fileStorageService;

    /**
     * 上传图片
     *
     * @param file
     */
    @Override
    public String uploadImage(MultipartFile file,String tempId,Long userId) {
        // 构建年/月/日/开头的目录前缀
        String objectName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
        //上传到MinIO
        FileInfo fileInfo = uploadToMinIO(file,objectName);

        // 记录本次文件上传
        ImageAsset imageAsset = ImageAsset.builder()
                .objectKey(objectName+fileInfo.getFilename())
                .fileUrl(fileInfo.getUrl())
                .userId(userId)
                .status(0)
                .tempId(tempId)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        this.save(imageAsset);

        return fileInfo.getUrl();
    }

    /**
     * 上传到MinIO
     * @param file
     * @return
     */
    private FileInfo uploadToMinIO(MultipartFile file, String objectName) {
        String originalFilename = file.getOriginalFilename();
        // 判断文件空说明上传失败了
        if (originalFilename == null) {
            throw new FileUploadException(FileUploadExceptionMessage.FILE_UPLOAD_ERROR);
        }

        return fileStorageService.of(file)
                .setPath(objectName)
                .upload();
    }
}
