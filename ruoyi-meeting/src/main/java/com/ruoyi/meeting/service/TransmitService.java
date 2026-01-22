package com.ruoyi.meeting.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.meeting.domain.DeviceInfo;

import java.util.List;

/**
 * 设备传输服务
 *
 * @author Snow
 */
public interface TransmitService {

    /**
     * 传输数据到所有设备
     */
    Integer transmit(List<DeviceInfo> deviceList, String dirName, String sourcePath, String targetPath);

    /**
     * 清除数据
     */
    AjaxResult clear(List<DeviceInfo> deviceList);

}