package com.ruoyi.meeting.service.impl;

import com.ruoyi.meeting.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Snow
 */
@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    // @Resource
    // private WirelessDeviceService wirelessDeviceService;

    /**
     * 获取已连接设备
     *
     * @return {@link List }<{@link String }>
     */
    @Override
    public List<String> getConnectedDevice() {
        // 1、获取已连接的无线设备
        // Set<String> wirelessConnectedDeviceSet = wirelessDeviceService.getConnectedDevice();

        // 2、获取已连接的有线设备
        Set<String> wiredConnectedDeviceSet = getWiredConnectedDevice();

        List<String> allConnectedDeviceList = new ArrayList<>();
        // allConnectedDeviceList.addAll(wirelessConnectedDeviceSet);
        allConnectedDeviceList.addAll(wiredConnectedDeviceSet);
        return allConnectedDeviceList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获取已连接的设备列表
     */
    private Set<String> getWiredConnectedDevice() {
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
