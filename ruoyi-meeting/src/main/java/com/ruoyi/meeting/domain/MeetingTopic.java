package com.ruoyi.meeting.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会议议题关联对象 bus_meeting_topic
 *
 * @author Snow
 * @date 2026-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingTopic extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 会议ID
     */
    private Long meetingId;

    /**
     * 议题ID
     */
    private Long topicId;

    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 序号
     */
    private Integer orderNo;

    /**
     * 删除标志（0代表存在）
     */
    private Long deleted;

}