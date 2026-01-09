package com.ruoyi.common.utils;

import com.ruoyi.common.dto.FileInfoDTO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Snow
 */
@Slf4j
public class FileDownloadUtil {

    public static void downloadZip(String zipFileName, List<FileInfoDTO> fileList, HttpServletResponse response) {
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            // 设置响应头
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipFileName, "UTF-8"));

            // 创建 ZIP 输出流
            for (FileInfoDTO fileInfo : fileList) {
                Path filePath = Paths.get(fileInfo.getFilePath());
                if (Files.exists(filePath)) {
                    // 添加文件到 ZIP
                    ZipEntry zipEntry = new ZipEntry(fileInfo.getFileName());
                    zipOut.putNextEntry(zipEntry);

                    try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) >= 0) {
                            zipOut.write(buffer, 0, length);
                        }
                    }

                    zipOut.closeEntry();
                } else {
                    log.warn("文件不存在: {}", filePath);
                }
            }
        } catch (Exception e) {
            log.error("下载议题文件失败: {}", e.getMessage(), e);

            try {
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"下载失败：" + e.getMessage() + "\"}");
            } catch (IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
        }
    }

}
