package com.ruoyi.meeting.controller.resp;

import com.ruoyi.meeting.domain.MeetingInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Snow
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingInfoPageResp extends MeetingInfo {

    /** 关联议题数量 */
    private Integer topicCount;

}
