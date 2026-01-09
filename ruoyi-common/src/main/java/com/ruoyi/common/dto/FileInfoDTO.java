package com.ruoyi.common.dto;

import com.ruoyi.common.utils.FileUploadUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Snow
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FileInfoDTO extends FileInfo {

    private String filePath;

    public FileInfoDTO(FileInfo fileInfo) {
        this.setFileName(fileInfo.getFileName());
        this.setFileUrl(fileInfo.getFileUrl());
        this.filePath = FileUploadUtil.fileUrlToPath(fileInfo.getFileUrl());
    }

}
