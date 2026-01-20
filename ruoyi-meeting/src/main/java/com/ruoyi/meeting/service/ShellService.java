package com.ruoyi.meeting.service;

import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author Snow
 */
public interface ShellService {

    /**
     * 传输命令集
     *
     * @param deviceId   设备 id
     * @param dirName    目录名称
     * @param sourcePath 源目录
     * @param targetPath 目标目录
     * @return boolean
     */
    boolean transmitCommandJob(String deviceId, String dirName, String sourcePath, String targetPath);

    /**
     * 清除命令集
     */
    boolean clearCommandJob(String deviceKey, String targetDir);

    /**
     * 提交任务
     */
    boolean executeJob(String deviceKey, String type, Callable<Boolean> task);

    /**
     * 执行 Shell 命令
     */
    String executeCommand(String command);

    /**
     * 执行 Shell 命令
     */
    String executeCommand(String command, @Nullable Long timeout, TimeUnit unit);

}
