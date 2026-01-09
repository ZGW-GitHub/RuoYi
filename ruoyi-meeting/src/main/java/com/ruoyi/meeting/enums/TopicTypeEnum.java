package com.ruoyi.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Snow
 */
@Getter
@AllArgsConstructor
public enum TopicTypeEnum {

    COMMON("common", "通用议题");

    private final String code;
    private final String desc;

}
