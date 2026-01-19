package com.ruoyi.meeting.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.meeting.TransmitConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Snow
 */
@RestController
@RequestMapping("transmit")
public class TransmitController {

    @PostMapping("meeting")
    public AjaxResult meeting(Long meetingId) {
        // 这里可以添加具体的同步逻辑
        // 例如：根据 meetingId 获取会议信息并同步到设备



        System.err.println(TransmitConfig.getTopicFileDir(1L));
        return AjaxResult.success("传输成功");
    }

}
