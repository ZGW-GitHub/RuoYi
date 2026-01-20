package com.ruoyi.meeting.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.meeting.domain.DeviceInfo;
import com.ruoyi.meeting.enums.DeviceStatusEnum;
import com.ruoyi.meeting.mapper.DeviceInfoMapper;
import com.ruoyi.meeting.service.DeviceInfoService;
import com.ruoyi.meeting.service.DeviceStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Snow
 */
@Slf4j
@Service
public class DeviceStatusServiceImpl implements DeviceStatusService {

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
            if (StrUtil.isNotBlank(wirelessKey) && connectedDeviceKeySet.contains(wirelessKey)) {
                deviceInfo.setDeviceStatus(DeviceStatusEnum.CONNECTED_WIRELESS.getCode());
                continue;
            }
            deviceInfo.setDeviceStatus(DeviceStatusEnum.UNCONNECTED.getCode());
        }
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
