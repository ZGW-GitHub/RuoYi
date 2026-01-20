package com.ruoyi.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Snow
 */
@Getter
@AllArgsConstructor
public enum DeviceStatusEnum {

    UNCONNECTED("unconnected", "未连接"),
    CONNECTED_WIRED("connected_wired", "有线已连接"),
    CONNECTED_WIRELESS("connected_wireless", "无线已连接");

    private final String code;
    private final String desc;

    public static Boolean isConnected(String code) {
        return CONNECTED_WIRED.getCode().equals(code)
                || CONNECTED_WIRELESS.getCode().equals(code);
    }

}
