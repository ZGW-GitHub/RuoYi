package com.ruoyi.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Snow
 */
@Getter
@AllArgsConstructor
public enum MeetingTypeEnum {

    COMMON("common", "通用");

    private final String code;
    private final String desc;

}
