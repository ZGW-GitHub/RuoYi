package com.ruoyi.meeting.domain;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备信息对象 bus_device_info
 * 
 * @author Snow
 * @date 2026-01-20
 */
@Data
@TableName("bus_device_info")
@EqualsAndHashCode(callSuper = true)
public class DeviceInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 设备序列号 */
    @Excel(name = "设备序列号")
    private String deviceSerial;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** IP */
    @Excel(name = "IP")
    private String deviceIp;

    /** 端口 */
    @Excel(name = "端口")
    private Long devicePort;

    /** 状态 */
    @Excel(name = "状态")
    private String deviceStatus;

    /** 扩展信息 */
    private String extInfo;

    /** 序号 */
    private Long orderNo;

    /** 删除标志（0代表存在） */
    @TableLogic(value = "0", delval = "id")
    private Long deleted;

    public String getWirelessKey() {
        return StrUtil.format("{}:{}", this.deviceIp, this.devicePort);
    }

}
