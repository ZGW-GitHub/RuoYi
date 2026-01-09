package com.ruoyi.meeting.service;

import com.ruoyi.meeting.domain.MeetingInfo;

import java.util.List;

/**
 * 会议信息Service接口
 * 
 * @author Snow
 * @date 2026-01-09
 */
public interface MeetingInfoService {

    /**
     * 查询会议信息
     * 
     * @param id 会议信息主键
     * @return 会议信息
     */
    MeetingInfo selectById(Long id);

    /**
     * 查询会议信息列表
     * 
     * @param meetingInfo 会议信息
     * @return 会议信息集合
     */
    List<MeetingInfo> selectList(MeetingInfo meetingInfo);

    /**
     * 新增会议信息
     * 
     * @param meetingInfo 会议信息
     * @return 结果
     */
    int insert(MeetingInfo meetingInfo);

    /**
     * 修改会议信息
     * 
     * @param meetingInfo 会议信息
     * @return 结果
     */
    int update(MeetingInfo meetingInfo);

    /**
     * 批量删除会议信息
     * 
     * @param ids 需要删除的会议信息主键集合
     * @return 结果
     */
    int deleteByIds(String ids);

    /**
     * 删除会议信息信息
     * 
     * @param id 会议信息主键
     * @return 结果
     */
    int deleteById(Long id);
}
