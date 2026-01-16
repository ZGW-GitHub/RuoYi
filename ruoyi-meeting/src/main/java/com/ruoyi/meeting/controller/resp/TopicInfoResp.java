package com.ruoyi.meeting.controller.resp;

import com.ruoyi.meeting.domain.TopicInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Snow
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TopicInfoResp extends TopicInfo {

    private String createUserName;

}
