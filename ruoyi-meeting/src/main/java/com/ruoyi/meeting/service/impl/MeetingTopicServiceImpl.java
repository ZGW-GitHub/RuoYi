package com.ruoyi.meeting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.meeting.domain.MeetingTopic;
import com.ruoyi.meeting.mapper.MeetingTopicMapper;
import com.ruoyi.meeting.service.MeetingTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 会议议题关联Service业务层处理
 * 
 * @author Snow
 * @date 2026-01-12
 */
@Slf4j
@Service
public class MeetingTopicServiceImpl extends ServiceImpl<MeetingTopicMapper, MeetingTopic> implements MeetingTopicService {

    @Resource
    private MeetingTopicMapper meetingTopicMapper;

}