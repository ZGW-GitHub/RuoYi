package com.ruoyi.meeting.mapper;

import com.ruoyi.meeting.domain.MeetingInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * 会议信息 SQLite 数据库操作 Mapper
 *
 * @author Snow
 */
@Slf4j
@Component
public class MeetingInfoSqliteMapper {

    /**
     * 创建会议信息表
     */
    public void createTableIfNotExists(Connection conn) throws Exception {
        String createTableSql = "CREATE TABLE IF NOT EXISTS meeting_info (" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT NOT NULL," +
                "meeting_type TEXT," +
                "meeting_status TEXT," +
                "meeting_time TEXT," +
                "meeting_host TEXT," +
                "participation_info TEXT," +
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
            log.debug("会议信息表创建成功或已存在");
        }
    }

    /**
     * 删除指定ID的会议信息
     */
    public void deleteById(Connection conn, Long meetingId) throws Exception {
        String deleteSql = "DELETE FROM meeting_info WHERE id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setLong(1, meetingId);
            int deletedRows = deleteStmt.executeUpdate();
            log.debug("删除会议信息记录，ID: {}, 删除行数: {}", meetingId, deletedRows);
        }
    }

    /**
     * 插入会议信息
     */
    public void insert(Connection conn, MeetingInfo meetingInfo) throws Exception {
        String insertSql = "INSERT INTO meeting_info (id, title, meeting_type, meeting_status, meeting_time, " +
                "meeting_host, participation_info, file_info, attachment_info, ext_info, order_no, " +
                "create_by, create_time, update_by, update_time, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setLong(1, meetingInfo.getId());
            insertStmt.setString(2, meetingInfo.getTitle());
            insertStmt.setString(3, meetingInfo.getMeetingType());
            insertStmt.setString(4, meetingInfo.getMeetingStatus());
            insertStmt.setString(5, meetingInfo.getMeetingTime());
            insertStmt.setString(6, meetingInfo.getMeetingHost());
            insertStmt.setString(7, meetingInfo.getParticipationInfo());
            insertStmt.setString(8, meetingInfo.getFileInfo());
            insertStmt.setString(9, meetingInfo.getAttachmentInfo());
            insertStmt.setString(10, meetingInfo.getExtInfo());
            insertStmt.setObject(11, meetingInfo.getOrderNo());
            insertStmt.setString(12, meetingInfo.getCreateBy());
            insertStmt.setString(13, meetingInfo.getCreateTime() != null ? meetingInfo.getCreateTime().toString() : null);
            insertStmt.setString(14, meetingInfo.getUpdateBy());
            insertStmt.setString(15, meetingInfo.getUpdateTime() != null ? meetingInfo.getUpdateTime().toString() : null);
            insertStmt.setString(16, meetingInfo.getRemark());
            
            int insertedRows = insertStmt.executeUpdate();
            log.debug("插入会议信息记录，ID: {}, 插入行数: {}", meetingInfo.getId(), insertedRows);
        }
    }

    /**
     * 保存会议信息（先删除后插入）
     */
    public void save(Connection conn, MeetingInfo meetingInfo) throws Exception {
        deleteById(conn, meetingInfo.getId());
        insert(conn, meetingInfo);
        log.info("保存会议信息到SQLite成功，ID: {}, 标题: {}", meetingInfo.getId(), meetingInfo.getTitle());
    }
}