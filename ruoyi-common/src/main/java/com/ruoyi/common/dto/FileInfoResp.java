package com.ruoyi.common.dto;

import com.ruoyi.common.utils.UploadUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Snow
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FileInfoResp extends FileInfo {

    private String filePath;

    public FileInfoResp(FileInfo fileInfo) {
        this.setFileName(fileInfo.getFileName());
        this.setFileUrl(fileInfo.getFileUrl());
        this.filePath = UploadUtil.fileUrlToPath(fileInfo.getFileUrl());
    }

}
