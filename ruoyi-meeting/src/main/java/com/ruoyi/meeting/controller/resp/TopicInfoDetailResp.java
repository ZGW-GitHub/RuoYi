package com.ruoyi.meeting.controller.resp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.meeting.domain.MeetingExtInfoDTO;
import com.ruoyi.meeting.domain.TopicInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Snow
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TopicInfoDetailResp extends TopicInfo {

    private MeetingExtInfoDTO meetingExtInfo;

    public TopicInfoDetailResp(TopicInfo topicInfo) {
        if (topicInfo == null) {
            return;
        }

        BeanUtil.copyProperties(topicInfo, this);

        String extInfo = topicInfo.getExtInfo();
        if (StrUtil.isBlank(extInfo)) {
            this.meetingExtInfo = new MeetingExtInfoDTO();
            return;
        }

        this.meetingExtInfo = JSONUtil.toBean(extInfo, MeetingExtInfoDTO.class);
    }

}
