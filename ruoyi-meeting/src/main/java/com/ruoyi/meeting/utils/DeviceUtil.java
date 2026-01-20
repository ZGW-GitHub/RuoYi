package com.ruoyi.meeting.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @author Snow
 */
public class DeviceUtil {

    /**
     * 是无线连接
     *
     * @param connectedDeviceKey 已连接设备密钥
     * @return {@link Boolean }
     */
    public static Boolean isWirelessConnection(String connectedDeviceKey) {
        if (StrUtil.isBlank(connectedDeviceKey)) {
            return false;
        }

        return connectedDeviceKey.contains(":");
    }

    /**
     * 是有线连接
     *
     * @param connectedDeviceKey 已连接设备密钥
     * @return {@link Boolean }
     */
    public static Boolean isWiredConnection(String connectedDeviceKey) {
        return !isWirelessConnection(connectedDeviceKey);
    }

}
