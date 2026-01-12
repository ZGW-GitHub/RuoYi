package com.ruoyi.meeting.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.meeting.domain.MeetingInfo;
import com.ruoyi.meeting.enums.MeetingStatusEnum;
import com.ruoyi.meeting.enums.MeetingTypeEnum;
import com.ruoyi.meeting.mapper.MeetingInfoMapper;
import com.ruoyi.meeting.service.MeetingInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会议信息Service业务层处理
 * 
 * @author Snow
 * @date 2026-01-09
 */
@Slf4j
@Service
public class MeetingInfoServiceImpl implements MeetingInfoService {

    @Resource
    private MeetingInfoMapper meetingInfoMapper;

    /**
     * 查询会议信息
     * 
     * @param id 会议信息主键
     * @return 会议信息
     */
    @Override
    public MeetingInfo selectById(Long id) {
        return meetingInfoMapper.selectById(id);
    }

    /**
     * 查询会议信息列表
     * 
     * @param meetingInfo 会议信息
     * @return 会议信息
     */
    @Override
    public List<MeetingInfo> selectList(MeetingInfo meetingInfo) {
        return meetingInfoMapper.selectMeetingInfoList(meetingInfo);
    }

    /**
     * 新增会议信息
     * 
     * @param meetingInfo 会议信息
     * @return 结果
     */
    @Override
    public int insert(MeetingInfo meetingInfo) {
        meetingInfo.setMeetingType(MeetingTypeEnum.COMMON.getCode());
        meetingInfo.setMeetingStatus(MeetingStatusEnum.CREATED.getCode());
        return meetingInfoMapper.insert(meetingInfo);
    }

    /**
     * 修改会议信息
     * 
     * @param meetingInfo 会议信息
     * @return 结果
     */
    @Override
    public int update(MeetingInfo meetingInfo) {
        return meetingInfoMapper.updateById(meetingInfo);
    }

    /**
     * 批量删除会议信息
     * 
     * @param ids 需要删除的会议信息主键
     * @return 结果
     */
    @Override
    public int deleteByIds(String ids) {
        return meetingInfoMapper.deleteBatchIds(StrUtil.split(ids, StrUtil.COMMA));
    }

    /**
     * 删除会议信息信息
     * 
     * @param id 会议信息主键
     * @return 结果
     */
    @Override
    public int deleteById(Long id) {
        return meetingInfoMapper.deleteById(id);
    }
}
