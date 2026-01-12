package com.ruoyi.meeting.mapper;

import com.ruoyi.common.mybatis.BaseMapper;
import com.ruoyi.meeting.controller.resp.MeetingInfoPageResp;
import com.ruoyi.meeting.domain.MeetingInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 会议信息Mapper接口
 * 
 * @author Snow
 * @date 2026-01-09
 */
@Mapper
public interface MeetingInfoMapper extends BaseMapper<MeetingInfo> {

    /**
     * 查询会议信息列表
     * 
     * @param meetingInfo 会议信息
     * @return 会议信息集合
     */
    List<MeetingInfoPageResp> selectMeetingInfoList(MeetingInfo meetingInfo);

}
