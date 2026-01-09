package com.ruoyi.meeting.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.meeting.domain.TopicInfo;
import com.ruoyi.meeting.enums.TopicStatusEnum;
import com.ruoyi.meeting.enums.TopicTypeEnum;
import com.ruoyi.meeting.mapper.TopicInfoMapper;
import com.ruoyi.meeting.service.TopicInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 议题Service业务层处理
 * 
 * @author Snow
 * @date 2026-01-09
 */
@Service
public class TopicInfoServiceImpl implements TopicInfoService {

    @Resource
    private TopicInfoMapper topicInfoMapper;

    /**
     * 查询议题
     * 
     * @param id 议题主键
     * @return 议题
     */
    @Override
    public TopicInfo selectById(Long id) {
        return topicInfoMapper.selectById(id);
    }

    /**
     * 查询议题列表
     * 
     * @param topicInfo 议题
     * @return 议题
     */
    @Override
    public List<TopicInfo> selectList(TopicInfo topicInfo) {
        return topicInfoMapper.selectTopicInfoList(topicInfo);
    }

    /**
     * 新增议题
     * 
     * @param topicInfo 议题
     * @return 结果
     */
    @Override
    public int insert(TopicInfo topicInfo) {
        topicInfo.setTopicStatus(TopicStatusEnum.PENDING.getCode());
        topicInfo.setTopicType(TopicTypeEnum.COMMON.getCode());
        topicInfo.setCreateTime(DateUtils.getNowDate());
        return topicInfoMapper.insert(topicInfo);
    }

    /**
     * 修改议题
     * 
     * @param topicInfo 议题
     * @return 结果
     */
    @Override
    public int update(TopicInfo topicInfo) {
        topicInfo.setUpdateTime(DateUtils.getNowDate());
        return topicInfoMapper.updateById(topicInfo);
    }

    /**
     * 批量删除议题
     * 
     * @param ids 需要删除的议题主键
     * @return 结果
     */
    @Override
    public int deleteByIds(String ids) {
        return topicInfoMapper.deleteBatchIds(StrUtil.split(ids, StrUtil.COMMA));
    }

    /**
     * 删除议题信息
     * 
     * @param id 议题主键
     * @return 结果
     */
    @Override
    public int deleteById(Long id) {
        return topicInfoMapper.deleteById(id);
    }
}
