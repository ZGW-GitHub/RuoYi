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

    public static <T> MeetingExtInfoDTO addInfo(String jsonStr, Consumer<T> setter, T value) {
        if (StrUtil.isEmpty(jsonStr)) {
            MeetingExtInfoDTO extInfo = new MeetingExtInfoDTO();

            return extInfo;
        }

        MeetingExtInfoDTO extInfo = JSONUtil.toBean(jsonStr, MeetingExtInfoDTO.class);

        return extInfo;
    }

}
