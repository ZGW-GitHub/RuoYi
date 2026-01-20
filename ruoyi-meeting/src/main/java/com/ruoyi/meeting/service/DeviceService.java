package com.ruoyi.meeting.service;

import java.util.List;

/**
 * @author Snow
 */
public interface DeviceService {

    /**
     * 获取已连接设备
     *
     * @return {@link List }<{@link String }>
     */
    List<String> getConnectedDevice();

}
