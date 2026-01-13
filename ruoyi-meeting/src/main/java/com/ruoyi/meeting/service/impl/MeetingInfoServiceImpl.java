package com.ruoyi.meeting.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.meeting.controller.resp.MeetingInfoPageResp;
import com.ruoyi.meeting.controller.resp.RelatedTopicResp;
import com.ruoyi.meeting.domain.MeetingInfo;
import com.ruoyi.meeting.domain.MeetingTopic;
import com.ruoyi.meeting.enums.MeetingStatusEnum;
import com.ruoyi.meeting.enums.MeetingTypeEnum;
import com.ruoyi.meeting.mapper.MeetingInfoMapper;
import com.ruoyi.meeting.mapper.MeetingTopicMapper;
import com.ruoyi.meeting.service.MeetingInfoService;
import com.ruoyi.meeting.service.MeetingTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private MeetingTopicMapper meetingTopicMapper;

    @Resource
    private MeetingTopicService meetingTopicService;

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
    public List<MeetingInfoPageResp> selectList(MeetingInfo meetingInfo) {
        return meetingInfoMapper.selectMeetingInfoList(meetingInfo);
    }

    /**
     * 新增会议信息并关联议题
     *
     * @param meetingInfo      会议信息
     * @param selectedTopicIds 选中的议题ID，逗号分隔
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MeetingInfo meetingInfo, String selectedTopicIds) {
        meetingInfo.setMeetingType(MeetingTypeEnum.COMMON.getCode());
        meetingInfo.setMeetingStatus(MeetingStatusEnum.CREATED.getCode());
        
        // 如果用户没有填写序号，设置默认值为1
        if (meetingInfo.getOrderNo() == null) {
            meetingInfo.setOrderNo(1L);
        }

        // 插入会议信息
        int result = meetingInfoMapper.insert(meetingInfo);
        if (result <= 0 || StrUtil.isBlank(selectedTopicIds)) {
            return result;
        }

        List<String> selectedTopicIdList = StrUtil.split(selectedTopicIds, StrUtil.COMMA).stream().distinct().collect(Collectors.toList());
        List<MeetingTopic> meetingTopicList = new ArrayList<>();
        for (int i = 0; i < selectedTopicIdList.size(); i++) {
            MeetingTopic meetingTopic = new MeetingTopic();
            meetingTopic.setMeetingId(meetingInfo.getId());
            String topicIdStr = selectedTopicIdList.get(i).trim();
            meetingTopic.setTopicId(Long.valueOf(topicIdStr));
            meetingTopic.setOrderNo(i + 1);

            meetingTopicList.add(meetingTopic);
        }

        if (!meetingTopicList.isEmpty()) {
            meetingTopicService.saveBatch(meetingTopicList);
        }

        return result;
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

    /**
     * 获取会议关联的议题
     *
     * @param meetingId 会议ID
     * @return 关联的议题列表
     */
    @Override
    public List<RelatedTopicResp> getRelatedTopics(Long meetingId) {
        List<RelatedTopicResp> topics = meetingTopicMapper.selectRelatedTopics(meetingId);
        return new ArrayList<>(topics);
    }

    /**
     * 修改会议信息并更新关联议题
     *
     * @param meetingInfo      会议信息
     * @param selectedTopicIds 选中的议题ID，逗号分隔
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MeetingInfo meetingInfo, String selectedTopicIds) {
        // 如果用户没有填写序号，设置默认值为1
        if (meetingInfo.getOrderNo() == null) {
            meetingInfo.setOrderNo(1L);
        }
        
        // 更新会议信息
        int result = meetingInfoMapper.updateById(meetingInfo);

        // 删除原有的议题关联
        meetingTopicMapper.deleteByMeetingId(meetingInfo.getId());
        if (StrUtil.isBlank(selectedTopicIds)) {
            return result;
        }

        // 添加新的议题关联
        List<String> selectedTopicIdList = StrUtil.split(selectedTopicIds, StrUtil.COMMA).stream().distinct().collect(Collectors.toList());
        List<MeetingTopic> meetingTopicList = new ArrayList<>();
        for (int i = 0; i < selectedTopicIdList.size(); i++) {
            MeetingTopic meetingTopic = new MeetingTopic();
            meetingTopic.setMeetingId(meetingInfo.getId());
            String topicIdStr = selectedTopicIdList.get(i).trim();
            meetingTopic.setTopicId(Long.valueOf(topicIdStr));
            meetingTopic.setOrderNo(i + 1);

            meetingTopicList.add(meetingTopic);
        }

        if (!meetingTopicList.isEmpty()) {
            meetingTopicService.saveBatch(meetingTopicList);
        }

        return result;
    }
}
