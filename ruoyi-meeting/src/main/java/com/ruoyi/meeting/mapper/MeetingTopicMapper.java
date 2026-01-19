package com.ruoyi.meeting.mapper;

import com.ruoyi.common.mybatis.BaseMapper;
import com.ruoyi.meeting.controller.resp.RelatedTopicResp;
import com.ruoyi.meeting.domain.MeetingTopic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会议议题关联Mapper接口
 * 
 * @author Snow
 * @date 2026-01-12
 */
@Mapper
public interface MeetingTopicMapper extends BaseMapper<MeetingTopic> {

    /**
     * 查询会议关联的议题
     * 
     * @param meetingId 会议ID
     * @return 关联的议题列表
     */
    List<RelatedTopicResp> selectRelatedTopic(@Param("meetingId") Long meetingId);

    /**
     * 删除会议的所有议题关联
     * 
     * @param meetingId 会议ID
     * @return 结果
     */
    int deleteByMeetingId(@Param("meetingId") Long meetingId);

    /**
     * 批量插入会议议题关联
     * 
     * @param meetingTopics 会议议题关联列表
     * @return 结果
     */
    int insertBatch(@Param("list") List<MeetingTopic> meetingTopics);

}