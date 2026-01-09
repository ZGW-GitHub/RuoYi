package com.ruoyi.meeting.service;

import com.ruoyi.meeting.domain.TopicInfo;

import java.util.List;

/**
 * 议题Service接口
 * 
 * @author Snow
 * @date 2026-01-09
 */
public interface TopicInfoService {

    /**
     * 查询议题
     * 
     * @param id 议题主键
     * @return 议题
     */
    TopicInfo selectById(Long id);

    /**
     * 查询议题列表
     * 
     * @param topicInfo 议题
     * @return 议题集合
     */
    List<TopicInfo> selectList(TopicInfo topicInfo);

    /**
     * 新增议题
     * 
     * @param topicInfo 议题
     * @return 结果
     */
    int insert(TopicInfo topicInfo);

    /**
     * 修改议题
     * 
     * @param topicInfo 议题
     * @return 结果
     */
    int update(TopicInfo topicInfo);

    /**
     * 批量删除议题
     * 
     * @param ids 需要删除的议题主键集合
     * @return 结果
     */
    int deleteByIds(String ids);

    /**
     * 删除议题信息
     * 
     * @param id 议题主键
     * @return 结果
     */
    int deleteById(Long id);
}
