package com.ruoyi.meeting.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.meeting.domain.TopicInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 议题Service接口
 * 
 * @author Snow
 * @date 2026-01-09
 */
public interface TopicInfoService {

    /**
     * 查询议题
     * 
     * @param id 议题主键
     * @return 议题
     */
    TopicInfo selectById(Long id);

    /**
     * 查询议题列表
     * 
     * @param topicInfo 议题
     * @return 议题集合
     */
    List<TopicInfo> selectList(TopicInfo topicInfo);

    /**
     * 批量删除议题
     * 
     * @param ids 需要删除的议题主键集合
     * @return 结果
     */
    int deleteByIds(String ids);

    /**
     * 删除议题信息
     * 
     * @param id 议题主键
     * @return 结果
     */
    int deleteById(Long id);

    /**
     * 新增议题（包含文件处理）
     *
     * @param topicInfo           议题信息
     * @param fileInfoFiles       议题文件
     * @param attachmentInfoFiles 附件文件
     * @return 结果
     */
    int insert(TopicInfo topicInfo, MultipartFile[] fileInfoFiles, MultipartFile[] attachmentInfoFiles);

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
    int update(TopicInfo topicInfo,
               MultipartFile[] fileInfoFiles, MultipartFile[] attachmentInfoFiles,
               String retainedFileInfo, String retainedAttachmentInfo);

    /**
     * 下载议题文件
     *
     * @param id       议题ID
     * @param response HTTP响应
     */
    void download(Long id, HttpServletResponse response);

    AjaxResult audit(Long id, Boolean auditResult, String auditRemark);

}
