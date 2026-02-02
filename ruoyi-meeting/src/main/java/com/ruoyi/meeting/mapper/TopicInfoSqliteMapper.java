package com.ruoyi.meeting.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.dto.FileInfoDTO;
import com.ruoyi.common.utils.FileUploadUtil;
import com.ruoyi.meeting.TransmitConfig;
import com.ruoyi.meeting.controller.resp.RelatedTopicResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * 议题信息 SQLite 数据库操作 Mapper
 *
 * @author Snow
 */
@Slf4j
@Component
public class TopicInfoSqliteMapper {

    /**
     * 创建议题信息表
     */
    public void createTableIfNotExists(Connection conn) throws Exception {
        String createTableSql = "CREATE TABLE IF NOT EXISTS topic_info (" +
                "id INTEGER PRIMARY KEY," +
                "meeting_id INTEGER," +
                "title TEXT NOT NULL," +
                "topic_type TEXT," +
                "topic_status TEXT," +
                "report_people TEXT," +
                "report_unit TEXT," +
                "file_info TEXT," +
                "attachment_info TEXT," +
                "ext_info TEXT," +
                "order_no INTEGER," +
                "create_by TEXT," +
                "create_time TEXT," +
                "update_by TEXT," +
                "update_time TEXT," +
                "remark TEXT" +
                ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
            log.debug("议题信息表创建成功或已存在");
        }
    }

    /**
     * 删除指定会议ID的所有议题信息
     */
    public void deleteByMeetingId(Connection conn, Long meetingId) throws Exception {
        String deleteSql = "DELETE FROM topic_info WHERE meeting_id >= ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setLong(1, 0);
            int deletedRows = deleteStmt.executeUpdate();
            log.debug("删除会议议题记录，会议ID: {}, 删除行数: {}", meetingId, deletedRows);
        }
    }

    /**
     * 插入单个议题信息
     */
    public void insert(Connection conn, RelatedTopicResp topic, Long meetingId) throws Exception {
        String insertSql = "INSERT INTO topic_info (id, meeting_id, title, topic_type, topic_status, " +
                "report_people, report_unit, file_info, attachment_info, ext_info, order_no, " +
                "create_by, create_time, update_by, update_time, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setLong(1, topic.getId());
            insertStmt.setLong(2, meetingId);
            insertStmt.setString(3, topic.getTitle());
            insertStmt.setString(4, topic.getTopicType());
            insertStmt.setString(5, topic.getTopicStatus());
            insertStmt.setString(6, topic.getReportPeople());
            insertStmt.setString(7, topic.getReportUnit());
            insertStmt.setString(8, topic.getFileInfo());
            insertStmt.setString(9, topic.getAttachmentInfo());
            insertStmt.setString(10, topic.getExtInfo());
            insertStmt.setObject(11, topic.getOrderNo());
            insertStmt.setString(12, topic.getCreateBy());
            insertStmt.setString(13, topic.getCreateTime() != null ? topic.getCreateTime().toString() : null);
            insertStmt.setString(14, topic.getUpdateBy());
            insertStmt.setString(15, topic.getUpdateTime() != null ? topic.getUpdateTime().toString() : null);
            insertStmt.setString(16, topic.getRemark());
            
            int insertedRows = insertStmt.executeUpdate();
            log.debug("插入议题信息记录，ID: {}, 会议ID: {}, 插入行数: {}", topic.getId(), meetingId, insertedRows);
        }
    }

    /**
     * 批量插入议题信息
     */
    public void batchInsert(Connection conn, List<RelatedTopicResp> topics, Long meetingId) throws Exception {
        String insertSql = "INSERT INTO topic_info (id, meeting_id, title, topic_type, topic_status, " +
                "report_people, report_unit, file_info, attachment_info, ext_info, order_no, " +
                "create_by, create_time, update_by, update_time, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            for (RelatedTopicResp topic : topics) {
                insertStmt.setLong(1, topic.getId());
                insertStmt.setLong(2, meetingId);
                insertStmt.setString(3, topic.getTitle());
                insertStmt.setString(4, topic.getTopicType());
                insertStmt.setString(5, topic.getTopicStatus());
                insertStmt.setString(6, topic.getReportPeople());
                insertStmt.setString(7, topic.getReportUnit());
                insertStmt.setString(8, topic.getFileInfo());
                insertStmt.setString(9, topic.getAttachmentInfo());
                insertStmt.setString(10, topic.getExtInfo());
                insertStmt.setObject(11, topic.getOrderNo());
                insertStmt.setString(12, topic.getCreateBy());
                insertStmt.setString(13, topic.getCreateTime() != null ? topic.getCreateTime().toString() : null);
                insertStmt.setString(14, topic.getUpdateBy());
                insertStmt.setString(15, topic.getUpdateTime() != null ? topic.getUpdateTime().toString() : null);
                insertStmt.setString(16, topic.getRemark());
                
                insertStmt.executeUpdate();
            }
            log.debug("批量插入议题信息记录完成，会议ID: {}, 议题数量: {}", meetingId, topics.size());
        }
    }

    /**
     * 保存议题信息列表（先删除后批量插入）
     */
    public void saveByMeetingId(Connection conn, List<RelatedTopicResp> topicList, Long meetingId) throws Exception {
        deleteByMeetingId(conn, meetingId);
        if (CollUtil.isNotEmpty(topicList)) {
            topicList.forEach(item -> {
                String fileInfoJson = item.getFileInfo();
                if (StrUtil.isBlank(fileInfoJson)) {
                    return;
                }

                List<FileInfoDTO> dtoList = JSONUtil.toList(fileInfoJson, FileInfoDTO.class);
                dtoList.forEach(dto -> {
                    String fileUrl = dto.getFileUrl();
                    if (StrUtil.isBlank(fileUrl)) {
                        return;
                    }

                    String filePath = FileUploadUtil.fileUrlToPath(fileUrl);
                    if (!FileUtil.exist(filePath)) {
                        log.warn("议题关联的文件不存在，议题ID: {}, 文件路径: {}", item.getId(), filePath);
                        return;
                    }
                    FileUtil.copyFile(filePath, TransmitConfig.getSource().getPathDraft() + FileUtil.FILE_SEPARATOR + FileUtil.getName(filePath));
                    fileUrl = StrUtil.subAfter(fileUrl, item.getId() + "", false);
                    dto.setFileUrl(StringUtils.stripStart(fileUrl, "/\\"));
                });
                item.setFileInfo(JSONUtil.toJsonStr(dtoList));
            });
            batchInsert(conn, topicList, meetingId);
        }
        log.info("保存会议议题信息到SQLite成功，会议ID: {}, 议题数量: {}", meetingId, topicList.size());
    }
}