package com.ruoyi.meeting.mapper;

import com.ruoyi.common.mybatis.BaseMapper;
import com.ruoyi.meeting.domain.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备信息Mapper接口
 * 
 * @author Snow
 * @date 2026-01-20
 */
@Mapper
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

    /**
     * 查询设备信息列表
     * 
     * @param deviceInfo 设备信息
     * @return 设备信息集合
     */
    List<DeviceInfo> selectDeviceInfoList(DeviceInfo deviceInfo);

    default List<DeviceInfo> listAll() {
        return lambdaChainQueryWrapper()
                .eq(DeviceInfo::getDeleted, 0)
                .list();
    }

}
