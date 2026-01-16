package com.ruoyi.meeting.mapper;

import com.ruoyi.common.mybatis.BaseMapper;
import com.ruoyi.meeting.controller.resp.TopicInfoResp;
import com.ruoyi.meeting.domain.TopicInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 议题Mapper接口
 * 
 * @author Snow
 * @date 2026-01-09
 */
@Mapper
public interface TopicInfoMapper extends BaseMapper<TopicInfo> {

    /**
     * 查询议题列表
     * 
     * @param topicInfo 议题
     * @return 议题集合
     */
    List<TopicInfoResp> selectTopicInfoList(TopicInfo topicInfo);

}
