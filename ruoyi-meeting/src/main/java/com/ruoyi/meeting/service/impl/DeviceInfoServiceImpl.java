package com.ruoyi.meeting.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.meeting.domain.DeviceInfo;
import com.ruoyi.meeting.enums.DeviceStatusEnum;
import com.ruoyi.meeting.mapper.DeviceInfoMapper;
import com.ruoyi.meeting.service.DeviceInfoService;
import com.ruoyi.meeting.service.DeviceStatusService;
import com.ruoyi.meeting.utils.DeviceUtil;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.mapper.SysConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 设备信息Service业务层处理
 *
 * @author Snow
 * @date 2026-01-20
 */
@Slf4j
@Service
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements DeviceInfoService {

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private DeviceStatusService deviceStatusService;

    @Resource
    private SysConfigMapper sysConfigMapper;

    /**
     * 查询设备信息
     *
     * @param id 设备信息主键
     * @return 设备信息
     */
    @Override
    public DeviceInfo selectById(Long id) {
        return deviceInfoMapper.selectById(id);
    }

    /**
     * 查询设备信息列表
     *
     * @param deviceInfo 设备信息
     * @return 设备信息
     */
    @Override
    public List<DeviceInfo> selectList(DeviceInfo deviceInfo) {
        return deviceInfoMapper.selectDeviceInfoList(deviceInfo);
    }

    /**
     * 新增设备信息
     *
     * @param deviceInfo 设备信息
     * @return 结果
     */
    @Override
    public int insert(DeviceInfo deviceInfo) {
        deviceStatusService.checkConnection(Collections.singletonList(deviceInfo));
        return deviceInfoMapper.insert(deviceInfo);
    }

    /**
     * 修改设备信息
     *
     * @param deviceInfo 设备信息
     * @return 结果
     */
    @Override
    public int update(DeviceInfo deviceInfo) {
        deviceStatusService.checkConnection(Collections.singletonList(deviceInfo));
        return deviceInfoMapper.updateById(deviceInfo);
    }

    /**
     * 批量删除设备信息
     *
     * @param ids 需要删除的设备信息主键
     * @return 结果
     */
    @Override
    public int deleteByIds(String ids) {
        return deviceInfoMapper.deleteBatchIds(StrUtil.split(ids, StrUtil.COMMA));
    }

    /**
     * 删除设备信息信息
     *
     * @param id 设备信息主键
     * @return 结果
     */
    @Override
    public int deleteById(Long id) {
        return deviceInfoMapper.deleteById(id);
    }

    /**
     * 检查连接
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkConnection(Long deviceId) {
        List<DeviceInfo> deviceInfoList = Collections.emptyList();
        if (deviceId == null) {
            deviceInfoList = deviceInfoMapper.listAll();
        } else {
            DeviceInfo deviceInfo = deviceInfoMapper.selectById(deviceId);
            if (deviceInfo != null) {
                deviceInfoList = Collections.singletonList(deviceInfo);
            }
        }

        if (CollUtil.isEmpty(deviceInfoList)) {
            return;
        }
        deviceStatusService.checkConnection(deviceInfoList);
        updateBatchById(deviceInfoList);
    }

    /**
     * 将连接的有线设备保存到数据库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveConnectedWiredDeviceToDB() {
        List<String> wiredConnectedDeviceKeyList = deviceStatusService.getConnectedDeviceKey().stream()
                .filter(DeviceUtil::isWiredConnection).collect(Collectors.toList());
        if (CollUtil.isEmpty(wiredConnectedDeviceKeyList)) {
            log.info("没有已连接的有线设备");
            return;
        }

        List<DeviceInfo> deviceInfoList = deviceInfoMapper.listAll();
        Set<String> savedDeviceSerialSet = deviceInfoList.stream().map(DeviceInfo::getDeviceSerial).collect(Collectors.toSet());

        List<DeviceInfo> newDeviceInfoList = buildNewDeviceInfo(wiredConnectedDeviceKeyList, savedDeviceSerialSet);
        if (CollUtil.isNotEmpty(newDeviceInfoList)) {
            saveBatch(newDeviceInfoList);
            log.info("已保存 {} 台新有线连接设备到数据库", newDeviceInfoList.size());
        }
    }

    private List<DeviceInfo> buildNewDeviceInfo(List<String> wiredConnectedDeviceKeyList, Set<String> savedDeviceSerialSet) {
        List<String> commonIpList = commonIp();

        List<DeviceInfo> newDeviceInfoList = new ArrayList<>();
        for (String deviceSerial : wiredConnectedDeviceKeyList) {
            if (savedDeviceSerialSet.contains(deviceSerial)) {
                continue;
            }

            DeviceInfo newDeviceInfo = new DeviceInfo();
            newDeviceInfo.setDeviceSerial(deviceSerial);
            newDeviceInfo.setDeviceName(StrUtil.EMPTY);
            newDeviceInfo.setDeviceIp(StrUtil.format("{}.{}.{}.0", CollUtil.get(commonIpList, 0),
                    CollUtil.get(commonIpList, 1), CollUtil.get(commonIpList, 2)));
            newDeviceInfo.setDeviceStatus(DeviceStatusEnum.CONNECTED_WIRED.getCode());
            newDeviceInfoList.add(newDeviceInfo);
        }
        return newDeviceInfoList;
    }

    @Override
    public List<String> commonIp() {
        SysConfig sysConfig = sysConfigMapper.lambdaChainQueryWrapper()
                .eq(SysConfig::getConfigKey, "bus.meeting.device.ip")
                .last(" LIMIT 1 ").one();

        String configValue = sysConfig == null ? StrUtil.EMPTY : sysConfig.getConfigValue();
        List<String> configValueList = StrUtil.split(configValue, StrUtil.DOT);

        List<String> commonIpList = CollUtil.newArrayList();
        commonIpList.add(StrUtil.nullToEmpty(CollUtil.get(configValueList, 0)));
        commonIpList.add(StrUtil.nullToEmpty(CollUtil.get(configValueList, 1)));
        commonIpList.add(StrUtil.nullToEmpty(CollUtil.get(configValueList, 2)));
        commonIpList.add(StrUtil.nullToEmpty(CollUtil.get(configValueList, 3)));
        return commonIpList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCommonIp(String ip1, String ip2, String ip3) {
        String newValue = StrUtil.format("{}.{}.{}.0", ip1, ip2, ip3);

        SysConfig sysConfig = sysConfigMapper.lambdaChainQueryWrapper()
                .eq(SysConfig::getConfigKey, "bus.meeting.device.ip")
                .last(" LIMIT 1 ").one();
        sysConfig = sysConfig == null ? createConfig(newValue) : updateConfig(sysConfig, newValue);

        // 更新设备 IP 地址
        List<DeviceInfo> deviceInfoList = deviceInfoMapper.listAll();
        if (CollUtil.isEmpty(deviceInfoList)) {
            return;
        }

        deviceInfoList.forEach(item -> {
            String deviceIp = item.getDeviceIp();
            if (StrUtil.isBlank(deviceIp)) {
                item.setDeviceIp(StrUtil.format("{}.{}.{}.0", ip1, ip2, ip3));
                return;
            }

            List<String> ipPartList = StrUtil.split(deviceIp, StrUtil.DOT);
            item.setDeviceIp(StrUtil.format("{}.{}.{}.{}", ip1, ip2, ip3,
                    StrUtil.blankToDefault(CollUtil.get(ipPartList, 3), "0")));
        });

        // 检测连接状态
        deviceStatusService.checkConnection(deviceInfoList);
        updateBatchById(deviceInfoList);
    }

    private SysConfig updateConfig(SysConfig sysConfig, String newValue) {
        sysConfig.setConfigValue(newValue);
        sysConfig.setUpdateBy(ShiroUtils.getLoginName());
        sysConfig.setUpdateTime(LocalDateTime.now());
        sysConfigMapper.updateConfig(sysConfig);

        return sysConfig;
    }

    private SysConfig createConfig(String newValue) {
        SysConfig sysConfig;
        sysConfig = new SysConfig();
        sysConfig.setConfigKey("bus.meeting.device.ip");
        sysConfig.setConfigName("会议-设备 IP 网段");
        sysConfig.setConfigType("N");
        sysConfig.setConfigValue(newValue);
        sysConfig.setCreateBy(ShiroUtils.getLoginName());
        sysConfig.setCreateTime(LocalDateTime.now());
        sysConfigMapper.insert(sysConfig);

        return sysConfig;
    }
}
