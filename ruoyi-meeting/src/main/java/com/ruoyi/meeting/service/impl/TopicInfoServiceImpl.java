package com.ruoyi.meeting.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.dto.FileInfo;
import com.ruoyi.common.dto.FileInfoDTO;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.FileDownloadUtil;
import com.ruoyi.common.utils.FileUploadUtil;
import com.ruoyi.meeting.domain.TopicInfo;
import com.ruoyi.meeting.enums.TopicStatusEnum;
import com.ruoyi.meeting.enums.TopicTypeEnum;
import com.ruoyi.meeting.mapper.TopicInfoMapper;
import com.ruoyi.meeting.service.TopicInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 议题Service业务层处理
 *
 * @author Snow
 * @date 2026-01-09
 */
@Slf4j
@Service
public class TopicInfoServiceImpl implements TopicInfoService {

    @Resource
    private TopicInfoMapper topicInfoMapper;

    /**
     * 查询议题
     *
     * @param id 议题主键
     * @return 议题
     */
    @Override
    public TopicInfo selectById(Long id) {
        return topicInfoMapper.selectById(id);
    }

    /**
     * 查询议题列表
     *
     * @param topicInfo 议题
     * @return 议题
     */
    @Override
    public List<TopicInfo> selectList(TopicInfo topicInfo) {
        return topicInfoMapper.selectTopicInfoList(topicInfo);
    }

    /**
     * 新增议题
     *
     * @param topicInfo 议题
     * @return 结果
     */
    private int insert(TopicInfo topicInfo) {
        topicInfo.setTopicStatus(TopicStatusEnum.PENDING.getCode());
        topicInfo.setTopicType(TopicTypeEnum.COMMON.getCode());
        topicInfo.setCreateTime(DateUtils.getNowDate());
        return topicInfoMapper.insert(topicInfo);
    }

    /**
     * 修改议题
     *
     * @param topicInfo 议题
     * @return 结果
     */
    private int update(TopicInfo topicInfo) {
        topicInfo.setUpdateTime(DateUtils.getNowDate());
        return topicInfoMapper.updateById(topicInfo);
    }

    /**
     * 批量删除议题
     *
     * @param ids 需要删除的议题主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(String ids) {
        List<String> idList = StrUtil.split(ids, StrUtil.COMMA);
        topicInfoMapper.deleteBatchIds(idList);
        idList.forEach(id -> FileUploadUtil.deleteFile(FileUploadUtil.getTopicFilePath(Long.valueOf(id))));
        return 1;
    }

    /**
     * 删除议题信息
     *
     * @param id 议题主键
     * @return 结果
     */
    @Override
    public int deleteById(Long id) {
        return topicInfoMapper.deleteById(id);
    }

    /**
     * 新增议题（包含文件处理）
     *
     * @param topicInfo           议题信息
     * @param fileInfoFiles       议题文件
     * @param attachmentInfoFiles 附件文件
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(TopicInfo topicInfo, MultipartFile[] fileInfoFiles, MultipartFile[] attachmentInfoFiles) {
        try {
            // 验证文件个数
            if (fileInfoFiles != null && fileInfoFiles.length > 6) {
                throw new RuntimeException("议题文件不能超过6个");
            }
            if (attachmentInfoFiles != null && attachmentInfoFiles.length > 6) {
                throw new RuntimeException("附件不能超过6个");
            }

            insert(topicInfo);

            // 处理议题文件
            String uploadPath = FileUploadUtil.getTopicFilePath(topicInfo.getId());
            if (fileInfoFiles != null && fileInfoFiles.length > 0) {
                List<FileInfo> fileInfoList = FileUploadUtil.saveFile(uploadPath, fileInfoFiles);
                topicInfo.setFileInfo(JSON.toJSONString(fileInfoList));
            }

            // 处理附件
            if (attachmentInfoFiles != null && attachmentInfoFiles.length > 0) {
                List<FileInfo> attachmentInfoList = FileUploadUtil.saveFile(uploadPath, attachmentInfoFiles);
                topicInfo.setAttachmentInfo(JSON.toJSONString(attachmentInfoList));
            }

            return update(topicInfo);
        } catch (Exception e) {
            log.error("新增议题失败: {}", e.getMessage(), e);
            throw new RuntimeException("新增议题失败：" + e.getMessage(), e);
        }
    }

    /**
     * 修改议题（包含文件处理）
     *
     * @param topicInfo              议题信息
     * @param fileInfoFiles          议题文件
     * @param attachmentInfoFiles    附件文件
     * @param retainedFileInfo       保留的现有文件信息
     * @param retainedAttachmentInfo 保留的现有附件信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TopicInfo topicInfo,
                      MultipartFile[] fileInfoFiles, MultipartFile[] attachmentInfoFiles,
                      String retainedFileInfo, String retainedAttachmentInfo) {
        try {
            // 处理议题文件
            List<FileInfo> finalFileInfoList = new ArrayList<>();

            // 添加保留的现有文件
            String uploadPath = FileUploadUtil.getTopicFilePath(topicInfo.getId());
            if (retainedFileInfo != null && !retainedFileInfo.isEmpty()) {
                List<FileInfo> retainedFiles = JSON.parseArray(retainedFileInfo, FileInfo.class);
                finalFileInfoList.addAll(retainedFiles);
            }

            // 添加新上传的文件
            if (fileInfoFiles != null && fileInfoFiles.length > 0) {
                List<FileInfo> newFiles = FileUploadUtil.saveFile(uploadPath, fileInfoFiles);
                finalFileInfoList.addAll(newFiles);
            }

            // 验证议题文件总数
            if (finalFileInfoList.size() > 6) {
                throw new RuntimeException("议题文件总数不能超过6个");
            }

            if (!finalFileInfoList.isEmpty()) {
                topicInfo.setFileInfo(JSON.toJSONString(finalFileInfoList));
            } else {
                topicInfo.setFileInfo(StrUtil.EMPTY);
            }

            // 处理附件
            List<FileInfo> finalAttachmentInfoList = new ArrayList<>();

            // 添加保留的现有附件
            if (retainedAttachmentInfo != null && !retainedAttachmentInfo.isEmpty()) {
                List<FileInfo> retainedAttachments = JSON.parseArray(retainedAttachmentInfo, FileInfo.class);
                finalAttachmentInfoList.addAll(retainedAttachments);
            }

            // 添加新上传的附件
            if (attachmentInfoFiles != null && attachmentInfoFiles.length > 0) {
                List<FileInfo> newAttachments = FileUploadUtil.saveFile(uploadPath, attachmentInfoFiles);
                finalAttachmentInfoList.addAll(newAttachments);
            }

            // 验证附件总数
            if (finalAttachmentInfoList.size() > 6) {
                throw new RuntimeException("附件总数不能超过6个");
            }

            if (!finalAttachmentInfoList.isEmpty()) {
                topicInfo.setAttachmentInfo(JSON.toJSONString(finalAttachmentInfoList));
            } else {
                topicInfo.setAttachmentInfo(StrUtil.EMPTY);
            }

            return update(topicInfo);
        } catch (Exception e) {
            log.error("更新议题失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新议题失败：" + e.getMessage(), e);
        }
    }

    /**
     * 下载议题文件
     *
     * @param id       议题ID
     * @param response HTTP响应
     */
    @Override
    public void download(Long id, HttpServletResponse response) {
        // 查询议题信息
        TopicInfo topicInfo = selectById(id);
        if (topicInfo == null) {
            throw new RuntimeException("议题不存在");
        }

        // 获取文件存储路径
        String uploadPath = FileUploadUtil.getTopicFilePath(id);
        Path topicDir = Paths.get(uploadPath);

        if (!Files.exists(topicDir)) {
            throw new RuntimeException("议题文件目录不存在");
        }

        // 解析文件信息
        List<FileInfo> allFiles = new ArrayList<>();

        // 添加议题文件
        if (StrUtil.isNotEmpty(topicInfo.getFileInfo())) {
            List<FileInfo> fileInfoList = JSON.parseArray(topicInfo.getFileInfo(), FileInfo.class);
            allFiles.addAll(fileInfoList);
        }

        // 添加附件文件
        if (StrUtil.isNotEmpty(topicInfo.getAttachmentInfo())) {
            List<FileInfo> attachmentInfoList = JSON.parseArray(topicInfo.getAttachmentInfo(), FileInfo.class);
            allFiles.addAll(attachmentInfoList);
        }

        if (allFiles.isEmpty()) {
            throw new RuntimeException("该议题没有文件可下载");
        }

        String zipFileName = topicInfo.getTitle() + "_议题文件_" + System.currentTimeMillis() + ".zip";
        List<FileInfoDTO> fileList = allFiles.stream().map(FileInfoDTO::new).collect(Collectors.toList());
        FileDownloadUtil.downloadZip(zipFileName, fileList, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult audit(Long id, Boolean auditResult, Boolean auditRemark) {
        TopicInfo topicInfo = selectById(id);
        if (topicInfo == null) {
            throw new ServiceException("议题不存在");
        }

        topicInfo.setTopicStatus(auditResult ? TopicStatusEnum.APPROVED.getCode() : TopicStatusEnum.REJECTED.getCode());
        update(topicInfo);
        return AjaxResult.success();
    }

}
