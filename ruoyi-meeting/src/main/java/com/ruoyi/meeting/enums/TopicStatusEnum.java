package com.ruoyi.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Snow
 */
@Getter
@AllArgsConstructor
public enum TopicStatusEnum {

    PENDING("pending", "待审核"),
    APPROVED("approved", "审核通过"),
    REJECTED("rejected", "审核驳回");
    // ARCHIVED("archived", "归档");

    private final String code;
    private final String desc;

}
