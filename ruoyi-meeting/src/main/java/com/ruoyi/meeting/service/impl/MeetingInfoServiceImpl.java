package com.ruoyi.meeting.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.meeting.controller.resp.MeetingInfoPageResp;
import com.ruoyi.meeting.domain.MeetingInfo;
import com.ruoyi.meeting.domain.MeetingTopic;
import com.ruoyi.meeting.enums.MeetingStatusEnum;
import com.ruoyi.meeting.enums.MeetingTypeEnum;
import com.ruoyi.meeting.mapper.MeetingInfoMapper;
import com.ruoyi.meeting.mapper.MeetingTopicMapper;
import com.ruoyi.meeting.service.MeetingInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * 新增会议信息并关联议题
     * 
     * @param meetingInfo 会议信息
     * @param selectedTopicIds 选中的议题ID，逗号分隔
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertWithTopics(MeetingInfo meetingInfo, String selectedTopicIds) {
        meetingInfo.setMeetingType(MeetingTypeEnum.COMMON.getCode());
        meetingInfo.setMeetingStatus(MeetingStatusEnum.CREATED.getCode());
        
        // 插入会议信息
        int result = meetingInfoMapper.insert(meetingInfo);
        
        // 添加议题关联
        if (result > 0 && StrUtil.isNotBlank(selectedTopicIds)) {
            String[] topicIds = selectedTopicIds.split(",");
            List<MeetingTopic> meetingTopics = new ArrayList<>();
            
            for (int i = 0; i < topicIds.length; i++) {
                String topicIdStr = topicIds[i].trim();
                if (StrUtil.isNotBlank(topicIdStr)) {
                    MeetingTopic meetingTopic = new MeetingTopic();
                    meetingTopic.setMeetingId(meetingInfo.getId());
                    meetingTopic.setTopicId(Long.valueOf(topicIdStr));
                    meetingTopic.setOrderNo(i + 1);
                    meetingTopic.setDeleted(0L);
                    meetingTopic.setCreateBy(ShiroUtils.getLoginName());
                    meetingTopic.setCreateTime(LocalDateTime.now());
                    meetingTopic.setUpdateBy(ShiroUtils.getLoginName());
                    meetingTopic.setUpdateTime(LocalDateTime.now());
                    meetingTopics.add(meetingTopic);
                }
            }
            
            if (!meetingTopics.isEmpty()) {
                meetingTopicMapper.insertBatch(meetingTopics);
            }
        }
        
        return result;
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

    /**
     * 获取会议关联的议题
     * 
     * @param meetingId 会议ID
     * @return 关联的议题列表
     */
    @Override
    public List<Object> getRelatedTopics(Long meetingId) {
        List<Map<String, Object>> topics = meetingTopicMapper.selectRelatedTopics(meetingId);
        return new ArrayList<>(topics);
    }

    /**
     * 修改会议信息并更新关联议题
     * 
     * @param meetingInfo 会议信息
     * @param selectedTopicIds 选中的议题ID，逗号分隔
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWithTopics(MeetingInfo meetingInfo, String selectedTopicIds) {
        // 更新会议信息
        int result = meetingInfoMapper.updateById(meetingInfo);
        
        // 删除原有的议题关联
        meetingTopicMapper.deleteByMeetingId(meetingInfo.getId());
        
        // 添加新的议题关联
        if (StrUtil.isNotBlank(selectedTopicIds)) {
            String[] topicIds = selectedTopicIds.split(",");
            List<MeetingTopic> meetingTopics = new ArrayList<>();
            
            for (int i = 0; i < topicIds.length; i++) {
                String topicIdStr = topicIds[i].trim();
                if (StrUtil.isNotBlank(topicIdStr)) {
                    MeetingTopic meetingTopic = new MeetingTopic();
                    meetingTopic.setMeetingId(meetingInfo.getId());
                    meetingTopic.setTopicId(Long.valueOf(topicIdStr));
                    meetingTopic.setOrderNo(i + 1);
                    meetingTopic.setDeleted(0L);
                    meetingTopic.setCreateBy(ShiroUtils.getLoginName());
                    meetingTopic.setCreateTime(LocalDateTime.now());
                    meetingTopic.setUpdateBy(ShiroUtils.getLoginName());
                    meetingTopic.setUpdateTime(LocalDateTime.now());
                    meetingTopics.add(meetingTopic);
                }
            }
            
            if (!meetingTopics.isEmpty()) {
                meetingTopicMapper.insertBatch(meetingTopics);
            }
        }
        
        return result;
    }
}
