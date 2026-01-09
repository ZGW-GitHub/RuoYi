package com.ruoyi.meeting.domain;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.function.Consumer;

/**
 * @author Snow
 */
@Data
public class MeetingExtInfoDTO {

    /**
     * 审核备注
     */
    private String auditRemark;

    public static MeetingExtInfoDTO addInfo(String jsonStr, Consumer<MeetingExtInfoDTO> setter) {
        MeetingExtInfoDTO extInfo = toBean(jsonStr);
        if (setter != null) {
            setter.accept(extInfo);
        }

        return extInfo;
    }

    public static MeetingExtInfoDTO toBean(String jsonStr) {
        MeetingExtInfoDTO extInfo;
        if (StrUtil.isEmpty(jsonStr)) {
            extInfo = new MeetingExtInfoDTO();
        } else {
            extInfo = JSONUtil.toBean(jsonStr, MeetingExtInfoDTO.class);
        }
        return extInfo;
    }

}
