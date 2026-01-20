package com.ruoyi.meeting.service;

import com.ruoyi.meeting.domain.DeviceInfo;

import java.util.List;

/**
 * @author Snow
 */
public interface DeviceStatusService {

    /**
     * 获取已连接设备
     *
     * @return {@link List }<{@link DeviceInfo }>
     */
    List<DeviceInfo> getConnectedDevice(Boolean updateDB);

    /**
     * 检查连接
     *
     * @param deviceInfoList 设备信息
     */
    void checkConnection(List<DeviceInfo> deviceInfoList);

}
