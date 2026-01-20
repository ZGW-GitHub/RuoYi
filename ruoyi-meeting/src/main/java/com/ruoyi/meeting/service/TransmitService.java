package com.ruoyi.meeting.service;

/**
 * 设备传输服务
 *
 * @author Snow
 */
public interface TransmitService {

    /**
     * 传输数据到所有设备
     */
    Integer transmit(String dirName, String sourcePath, String targetPath);

    /**
     * 清除数据
     */
    String clear();

}