package com.ruoyi.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.meeting.domain.DeviceInfo;

import java.util.List;

/**
 * 设备信息Service接口
 * 
 * @author Snow
 * @date 2026-01-20
 */
public interface DeviceInfoService extends IService<DeviceInfo> {

    /**
     * 查询设备信息
     * 
     * @param id 设备信息主键
     * @return 设备信息
     */
    DeviceInfo selectById(Long id);

    /**
     * 查询设备信息列表
     * 
     * @param deviceInfo 设备信息
     * @return 设备信息集合
     */
    List<DeviceInfo> selectList(DeviceInfo deviceInfo);

    /**
     * 新增设备信息
     * 
     * @param deviceInfo 设备信息
     * @return 结果
     */
    int insert(DeviceInfo deviceInfo);

    /**
     * 修改设备信息
     * 
     * @param deviceInfo 设备信息
     * @return 结果
     */
    int update(DeviceInfo deviceInfo);

    /**
     * 批量删除设备信息
     * 
     * @param ids 需要删除的设备信息主键集合
     * @return 结果
     */
    int deleteByIds(String ids);

    /**
     * 删除设备信息信息
     * 
     * @param id 设备信息主键
     * @return 结果
     */
    int deleteById(Long id);
}
