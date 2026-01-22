package com.ruoyi.meeting.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.meeting.TransmitConfig;
import com.ruoyi.meeting.controller.resp.RelatedTopicResp;
import com.ruoyi.meeting.domain.DeviceInfo;
import com.ruoyi.meeting.domain.MeetingInfo;
import com.ruoyi.meeting.mapper.MeetingInfoSqliteMapper;
import com.ruoyi.meeting.mapper.TopicInfoSqliteMapper;
import com.ruoyi.meeting.service.DeviceStatusService;
import com.ruoyi.meeting.service.MeetingInfoService;
import com.ruoyi.meeting.service.TransmitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * 传输控制器
 *
 * @author Snow
 */
@Slf4j
@RestController
@RequestMapping("transmit")
public class TransmitController {

    @Resource
    private TransmitService transmitService;

    @Resource
    private MeetingInfoService meetingInfoService;

    @Resource
    private MeetingInfoSqliteMapper meetingInfoSqliteMapper;

    @Resource
    private TopicInfoSqliteMapper topicInfoSqliteMapper;

    @Resource
    private DeviceStatusService deviceStatusService;

    @PostMapping("meeting")
    public AjaxResult meeting(Long meetingId) {
        Connection conn = null;
        try {
            // 1.1、查询会议信息
            MeetingInfo meetingInfo = meetingInfoService.selectById(meetingId);
            if (meetingInfo == null) {
                return AjaxResult.error("会议信息不存在");
            }
            // 1.2、查询关联的议题信息
            List<RelatedTopicResp> relatedTopicList = meetingInfoService.getRelatedTopic(meetingId);
            if (relatedTopicList == null || relatedTopicList.isEmpty()) {
                return AjaxResult.error("会议关联的议题信息不存在");
            }

            // 获取已连接设备列表
            List<DeviceInfo> connectedDeviceList = deviceStatusService.getConnectedDevice(true);
            if (CollUtil.isEmpty(connectedDeviceList)) {
                return AjaxResult.error("没有已连接的设备");
            }

            // 2.1、创建 SQLite 数据库连接
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + TransmitConfig.getDbFilePath());
            // 2.2、创建数据表（如果不存在）
            meetingInfoSqliteMapper.createTableIfNotExists(conn);
            topicInfoSqliteMapper.createTableIfNotExists(conn);
            // 2.3、保存数据到 SQLite
            meetingInfoSqliteMapper.save(conn, meetingInfo);
            topicInfoSqliteMapper.saveByMeetingId(conn, relatedTopicList, meetingId);

            String dirName = TransmitConfig.getBaseDirName();
            String sourcePath = TransmitConfig.getSource().getAll();
            String targetPath = TransmitConfig.getTarget().getAll();
            Integer successCount = transmitService.transmit(connectedDeviceList, dirName, sourcePath, targetPath);
            log.info("会议信息传输成功，会议ID: {}, 议题数量: {}，成功设备数: {}", meetingId, relatedTopicList.size(), successCount);

            // 3、返回成功
            return AjaxResult.success(StrUtil.format("传输完成，议题数量: {}，成功设备数: {}", relatedTopicList.size(), successCount));
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


    @ResponseBody
    @PostMapping("/clearData")
    public AjaxResult clearData() {
        try {
            List<DeviceInfo> connectedDeviceList = deviceStatusService.getConnectedDevice(true);
            if (CollUtil.isEmpty(connectedDeviceList)) {
                return AjaxResult.error("没有已连接的设备");
            }

            return transmitService.clear(connectedDeviceList);
        } catch (Exception e) {
            log.error("清除会议数据失败. 异常: {}", e.getMessage(), e);
            return AjaxResult.error("清除会议数据失败：" + e.getMessage());
        }
    }

}
