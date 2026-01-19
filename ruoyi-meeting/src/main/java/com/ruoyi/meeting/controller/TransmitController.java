package com.ruoyi.meeting.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.meeting.TransmitConfig;
import com.ruoyi.meeting.controller.resp.RelatedTopicResp;
import com.ruoyi.meeting.domain.MeetingInfo;
import com.ruoyi.meeting.service.MeetingInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * @author Snow
 */
@Slf4j
@RestController
@RequestMapping("transmit")
public class TransmitController {

    @Resource
    private MeetingInfoService meetingInfoService;

    @PostMapping("meeting")
    public AjaxResult meeting(Long meetingId) {
        Connection conn = null;
        try {
            // 1、查询会议信息与其关联的议题信息
            MeetingInfo meetingInfo = meetingInfoService.selectById(meetingId);
            if (meetingInfo == null) {
                return AjaxResult.error("会议信息不存在");
            }
            
            List<RelatedTopicResp> relatedTopics = meetingInfoService.getRelatedTopics(meetingId);

            // 2、将这些信息保存到 sqlite 数据库中，sqlite 数据库文件地址通过该方法获取：com.ruoyi.meeting.TransmitConfig.getDbFilePath
            String dbFilePath = TransmitConfig.getDbFilePath();
            
            // 创建SQLite数据库连接
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
            
            // 创建会议信息表（如果不存在）
            createMeetingInfoTableIfNotExists(conn);
            
            // 创建议题信息表（如果不存在）
            createTopicInfoTableIfNotExists(conn);
            
            // 保存会议信息到SQLite
            saveMeetingInfoToSqlite(conn, meetingInfo);
            
            // 保存议题信息到SQLite
            saveTopicInfoToSqlite(conn, relatedTopics, meetingId);
            
            log.info("会议信息传输成功，会议ID: {}, 议题数量: {}", meetingId, relatedTopics.size());

            // 3、返回成功
            return AjaxResult.success("传输成功");
        } catch (Exception e) {
            log.error("会议信息传输失败，会议ID: {}", meetingId, e);
            return AjaxResult.error("传输失败: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接失败", e);
                }
            }
        }
    }

    /**
     * 创建会议信息表
     */
    private void createMeetingInfoTableIfNotExists(Connection conn) throws Exception {
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
        }
    }

    /**
     * 创建议题信息表
     */
    private void createTopicInfoTableIfNotExists(Connection conn) throws Exception {
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
                "remark TEXT," +
                "FOREIGN KEY (meeting_id) REFERENCES meeting_info(id)" +
                ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        }
    }

    /**
     * 保存会议信息到SQLite
     */
    private void saveMeetingInfoToSqlite(Connection conn, MeetingInfo meetingInfo) throws Exception {
        // 先删除已存在的记录
        String deleteSql = "DELETE FROM meeting_info WHERE id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setLong(1, meetingInfo.getId());
            deleteStmt.executeUpdate();
        }
        
        // 插入新记录
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
            
            insertStmt.executeUpdate();
        }
    }

    /**
     * 保存议题信息到SQLite
     */
    private void saveTopicInfoToSqlite(Connection conn, List<RelatedTopicResp> topics, Long meetingId) throws Exception {
        // 先删除该会议的所有议题记录
        String deleteSql = "DELETE FROM topic_info WHERE meeting_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setLong(1, meetingId);
            deleteStmt.executeUpdate();
        }
        
        // 插入新的议题记录
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
        }
    }
}
