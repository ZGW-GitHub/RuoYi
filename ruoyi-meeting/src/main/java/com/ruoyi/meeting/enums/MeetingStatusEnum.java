package com.ruoyi.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Snow
 */
@Getter
@AllArgsConstructor
public enum MeetingStatusEnum {

    CREATED("created", "未开始"),
    FINISHED("finished", "已完成");

    private final String code;
    private final String desc;

}
