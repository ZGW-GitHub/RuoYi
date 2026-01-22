package com.ruoyi.meeting.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.meeting.TransmitConfig;
import com.ruoyi.meeting.domain.DeviceInfo;
import com.ruoyi.meeting.enums.DeviceStatusEnum;
import com.ruoyi.meeting.service.DeviceStatusService;
import com.ruoyi.meeting.service.ShellService;
import com.ruoyi.meeting.service.TransmitService;
import com.ruoyi.meeting.utils.ConcurrentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 设备传输服务
 *
 * @author Snow
 */
@Slf4j
@Service
public class TransmitServiceImpl implements TransmitService {

    @Resource
    public DeviceStatusService deviceStatusService;

    @Resource
    private ShellService shellService;

    /**
     * 传输数据到所有设备
     */
    @Override
    public Integer transmit(List<DeviceInfo> deviceList, String dirName, String sourcePath, String targetPath) {
        FileUtil.mkdir(sourcePath);
        log.info("\n\n== 开始传输数据: ==\n传输的目录: {}\n存放的目录: {}\n删除的旧目录: {}\n", sourcePath, targetPath, targetPath + "/" + dirName);

        AtomicInteger successCount = new AtomicInteger(0);
        List<Future<Boolean>> futureList = deviceList.stream()
                .filter(item -> DeviceStatusEnum.isConnected(item.getDeviceStatus()))
                .map(deviceInfo -> {
                    if (DeviceStatusEnum.CONNECTED_WIRED.getCode().equals(deviceInfo.getDeviceStatus())) {
                        return deviceInfo.getDeviceSerial();
                    }
                    if (DeviceStatusEnum.CONNECTED_WIRELESS.getCode().equals(deviceInfo.getDeviceStatus())) {
                        return deviceInfo.getWirelessKey();
                    }
                    return StrUtil.EMPTY;
                })
                .filter(StrUtil::isNotBlank)
                .map(deviceKey -> {
                    return ConcurrentUtil.executorService.submit(() -> shellService.executeJob(deviceKey, "传输数据"
                            , () -> shellService.transmitCommandJob(deviceKey, dirName, sourcePath, targetPath)));
                }).collect(Collectors.toList());
        ConcurrentUtil.waitThreadPoolTaskFinish(futureList, successCount);
        return successCount.get();
    }

    /**
     * 清除数据
     */
    @Override
    public AjaxResult clear(List<DeviceInfo> deviceList) {
        AtomicInteger successCount = new AtomicInteger(0);
        List<Future<Boolean>> futureList = deviceList.stream()
                .filter(item -> DeviceStatusEnum.isConnected(item.getDeviceStatus()))
                .map(deviceInfo -> {
                    if (DeviceStatusEnum.CONNECTED_WIRED.getCode().equals(deviceInfo.getDeviceStatus())) {
                        return deviceInfo.getDeviceSerial();
                    }
                    if (DeviceStatusEnum.CONNECTED_WIRELESS.getCode().equals(deviceInfo.getDeviceStatus())) {
                        return deviceInfo.getWirelessKey();
                    }
                    return StrUtil.EMPTY;
                })
                .filter(StrUtil::isNotBlank)
                .map(deviceKey -> {
                    return ConcurrentUtil.executorService.submit(() -> shellService.executeJob(deviceKey, "清除数据"
                            , () -> shellService.clearCommandJob(deviceKey, TransmitConfig.getTarget().getPathDb())));
                }).collect(Collectors.toList());
        ConcurrentUtil.waitThreadPoolTaskFinish(futureList, successCount);

        return AjaxResult.success(String.format("清除完成，成功设备数：%d", successCount.get()));
    }

}
