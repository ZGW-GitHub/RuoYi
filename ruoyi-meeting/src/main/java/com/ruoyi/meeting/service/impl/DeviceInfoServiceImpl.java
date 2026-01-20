package com.ruoyi.meeting.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.meeting.domain.DeviceInfo;
import com.ruoyi.meeting.mapper.DeviceInfoMapper;
import com.ruoyi.meeting.service.DeviceInfoService;
import com.ruoyi.meeting.service.DeviceStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

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

}
