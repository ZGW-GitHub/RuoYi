package com.ruoyi.common.utils;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.dto.FileInfo;
import com.ruoyi.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Snow
 */
@Slf4j
public class UploadUtil {

    public static String fileUrlToPath(String url) {
        return url.replace(Constants.RESOURCE_PREFIX, RuoYiConfig.getProfile());
    }

    public static String filePathToUrl(String path) {
        return path.replace(RuoYiConfig.getProfile(), Constants.RESOURCE_PREFIX);
    }

    /**
     * 保存文件并返回文件信息列表
     *
     * @param saveDir  saveDir
     * @param fileList 文件数组
     * @return 文件信息列表
     */
    public static List<FileInfo> saveFile(String saveDir, MultipartFile[] fileList) {
        List<FileInfo> fileInfoList = new ArrayList<>();
        try {
            for (MultipartFile file : fileList) {
                if (file.isEmpty()) {
                    continue;
                }

                // 生成唯一文件名
                String originalFilename = file.getOriginalFilename();
                String extension = FileTypeUtil.getType(file.getInputStream());
                String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

                // 保存文件
                File destFile = new File(saveDir, fileName);
                file.transferTo(destFile);

                // 创建文件信息
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(originalFilename);
                fileInfo.setFileUrl(UploadUtil.filePathToUrl(destFile.getAbsolutePath()));

                fileInfoList.add(fileInfo);
            }
        } catch (Exception e) {
            log.error("文件保存异常: {}", e.getMessage(), e);
            throw new ServiceException("文件保存异常：" + e.getMessage());
        }

        return fileInfoList;
    }

    public static String getTopicFilePath(Long meetingId) {
        String filePath = RuoYiConfig.getUploadPath() + "/topic/" + meetingId;
        FileUtil.mkdir(filePath);
        return filePath;
    }

    public static String getMeetingFilePath(Long meetingId) {
        String filePath = RuoYiConfig.getUploadPath() + "/meeting/" + meetingId;
        FileUtil.mkdir(filePath);
        return filePath;
    }

}
