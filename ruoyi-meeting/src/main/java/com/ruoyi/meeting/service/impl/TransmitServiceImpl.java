package com.ruoyi.meeting.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.ruoyi.meeting.TransmitConfig;
import com.ruoyi.meeting.service.DeviceService;
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
    public DeviceService deviceService;

    @Resource
    private ShellService shellService;

    /**
     * 传输数据到所有设备
     */
    @Override
    public Integer transmit(String dirName, String sourcePath, String targetPath) {
        FileUtil.mkdir(sourcePath);
        log.info("\n\n== 开始传输数据: ==\n传输的目录: {}\n存放的目录: {}\n删除的旧目录: {}\n", sourcePath, targetPath, targetPath + "/" + dirName);

        List<String> connectedDeviceList = deviceService.getConnectedDevice();
        if (CollUtil.isEmpty(connectedDeviceList)) {
            return 0;
        }

        AtomicInteger successCount = new AtomicInteger(0);
        List<Future<Boolean>> futureList = connectedDeviceList.stream()
                .map(deviceKey -> ConcurrentUtil.executorService.submit(
                        () -> shellService.executeJob(deviceKey, "传输数据",
                                () -> shellService.transmitCommandJob(deviceKey, dirName, sourcePath, targetPath))
                )).collect(Collectors.toList());
        ConcurrentUtil.waitThreadPoolTaskFinish(futureList, successCount);
        return successCount.get();
    }

    /**
     * 清除数据
     */
    @Override
    public String clear() {
        List<String> connectedDeviceList = deviceService.getConnectedDevice();
        if (CollUtil.isEmpty(connectedDeviceList)) {
            return "";
        }

        AtomicInteger successCount = new AtomicInteger(0);
        List<Future<Boolean>> futureList = connectedDeviceList.stream()
                .map(deviceKey -> ConcurrentUtil.executorService.submit(
                        () -> shellService.executeJob(deviceKey, "清除数据",
                                () -> shellService.clearCommandJob(deviceKey, TransmitConfig.getTarget().getPathDb()))
                )).collect(Collectors.toList());
        ConcurrentUtil.waitThreadPoolTaskFinish(futureList, successCount);

        return String.format("成功设备数：%d", successCount.get());
    }

}
