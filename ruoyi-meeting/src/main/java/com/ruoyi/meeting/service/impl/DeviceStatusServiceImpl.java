package com.ruoyi.meeting.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.meeting.domain.DeviceInfo;
import com.ruoyi.meeting.enums.DeviceStatusEnum;
import com.ruoyi.meeting.mapper.DeviceInfoMapper;
import com.ruoyi.meeting.service.DeviceInfoService;
import com.ruoyi.meeting.service.DeviceStatusService;
import com.ruoyi.meeting.service.ShellService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Snow
 */
@Slf4j
@Service
public class DeviceStatusServiceImpl implements DeviceStatusService {

    @Resource
    private ShellService shellService;

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private DeviceInfoService deviceInfoService;

    /**
     * 获取已连接设备
     *
     * @return {@link List }<{@link DeviceInfo }>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DeviceInfo> getConnectedDevice(Boolean updateDB) {
        List<DeviceInfo> deviceInfoList = deviceInfoMapper.listAll();
        checkConnection(deviceInfoList);
        if (updateDB) {
            deviceInfoService.updateBatchById(deviceInfoList);
        }

        return deviceInfoList.stream()
                .filter(item -> DeviceStatusEnum.isConnected(item.getDeviceStatus()))
                .collect(Collectors.toList());
    }

    /**
     * 检查连接
     *
     * @param deviceInfoList 设备信息
     */
    @Override
    public void checkConnection(List<DeviceInfo> deviceInfoList) {
        Set<String> connectedDeviceKeySet = getConnectedDeviceKey();

        for (DeviceInfo deviceInfo : deviceInfoList) {
            String deviceSerial = deviceInfo.getDeviceSerial();
            if (StrUtil.isNotBlank(deviceSerial) && connectedDeviceKeySet.contains(deviceSerial)) {
                deviceInfo.setDeviceStatus(DeviceStatusEnum.CONNECTED_WIRED.getCode());
                continue;
            }

            String wirelessKey = deviceInfo.getWirelessKey();
            if (StrUtil.isBlank(wirelessKey)) {
                deviceInfo.setDeviceStatus(DeviceStatusEnum.UNCONNECTED.getCode());
                continue;
            }

            if (connectedDeviceKeySet.contains(wirelessKey) && checkWirelessConnect(deviceInfo)) {
                deviceInfo.setDeviceStatus(DeviceStatusEnum.CONNECTED_WIRELESS.getCode());
                continue;
            }

            if (tryWirelessConnect(deviceInfo)) {
                deviceInfo.setDeviceStatus(DeviceStatusEnum.CONNECTED_WIRELESS.getCode());
                continue;
            }
            deviceInfo.setDeviceStatus(DeviceStatusEnum.UNCONNECTED.getCode());
        }
    }


    /**
     * 尝试无线连接
     *
     * @param deviceInfo 设备信息
     * @return {@link Boolean }
     */
    public Boolean tryWirelessConnect(DeviceInfo deviceInfo) {
        String shellResult = shellService.executeCommand("hdc tconn " + deviceInfo.getWirelessKey(), 1L, TimeUnit.SECONDS);

        // [Info]Target is connected, repeat operation
        return !(StrUtil.isBlank(shellResult) || shellResult.contains("failed"));
    }

    /**
     * 检查无线连接
     *
     * @param deviceInfo 设备信息
     * @return {@link Boolean }
     */
    private Boolean checkWirelessConnect(DeviceInfo deviceInfo) {
        String shellResult = shellService.executeCommand("hdc -t " + deviceInfo.getWirelessKey() + " shell echo test_connection");

        return shellResult != null && shellResult.contains("test_connection");
    }

    /**
     * 获取已连接的设备列表
     */
    public Set<String> getConnectedDeviceKey() {
        try {
            Process process = Runtime.getRuntime().exec("hdc list targets");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            Set<String> connectedDeviceNoSet = new HashSet<>();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.equals("[Empty]")) {
                    connectedDeviceNoSet.add(line);
                }
            }

            process.waitFor();
            reader.close();

            return connectedDeviceNoSet;
        } catch (Exception e) {
            log.error("获取设备列表失败: {}", e.getMessage(), e);
            return new HashSet<>();
        }
    }

}
