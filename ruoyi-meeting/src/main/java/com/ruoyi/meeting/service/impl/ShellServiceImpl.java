package com.ruoyi.meeting.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.meeting.TransmitConfig;
import com.ruoyi.meeting.service.ShellService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author Snow
 */
@Slf4j
@Service
public class ShellServiceImpl implements ShellService {

    /**
     * 传输命令集
     *
     * @param deviceId   设备 id
     * @param dirName    目录名称
     * @param sourcePath 源目录
     * @param targetPath 目标目录
     * @return boolean
     */
    @Override
    public boolean transmitCommandJob(String deviceId, String dirName, String sourcePath, String targetPath) {
        try {
            String appBundle = TransmitConfig.getAppBundle();

            // 1. 删除目标目录
            String commandResult = executeCommand("hdc -t " + deviceId + " shell -b " + appBundle + " rm -r \"" + targetPath + "/" + dirName + "\"");
            log.info("【 文件传输 】清理目标目录. 设备: {}, 命令执行结果: {}", deviceId, commandResult);

            // 2. 检查源路径是否为空
            if (FileUtil.isEmpty(new File(sourcePath))) {
                log.info("【 文件传输 】源路径为空. 设备: {}", deviceId);
                return true;
            }

            // 2. 创建目标目录
            // commandResult = executeCommand("hdc -t " + deviceId + " shell -b " + appBundle + " mkdir -p \"" + targetPath + "\"");
            // log.info("【 文件传输 】创建目标目录. 设备: {}, 命令执行结果: {}", deviceId, commandResult);

            // 3. 发送文件
            TimeInterval timer = DateUtil.timer();
            String sendCommand = "hdc -t " + deviceId + " file send -b " + appBundle + " \"" + sourcePath + "\" \"" + targetPath + "\"";
            commandResult = executeCommand(sendCommand);
            log.info("【 文件传输 】设备: {}, 耗时: {}ms, 传输命令执行结果: {}", deviceId, timer.interval(), commandResult);

            // 检查传输结果
            return commandResult != null && commandResult.contains("finish");
        } catch (Exception e) {
            log.error("【 文件传输 】设备: {}, 传输过程中发生错误: {}", deviceId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 清除命令集
     */
    @Override
    public boolean clearCommandJob(String deviceKey, String targetDir) {
        try {
            String appBundle = TransmitConfig.getAppBundle();

            // 1. 删除目标目录
            String commandResult = executeCommand("hdc -t " + deviceKey + " shell -b " + appBundle + " rm -r \"" + targetDir + "\"");
            log.info("【 文件清除 】清理目标目录. 设备: {}, 命令执行结果: {}", deviceKey, commandResult);

            return StrUtil.isBlank(commandResult) || commandResult.contains("No such file or directory");
        } catch (Exception e) {
            log.error("【 文件清除 】设备: {}, 清除过程中发生错误: {}", deviceKey, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 提交任务
     */
    @Override
    public boolean executeJob(String deviceKey, String type, Callable<Boolean> task) {
        log.debug("【 executeJob 】开始执行任务. 设备: {}, 任务类型: {}", deviceKey, type);

        try {
            boolean result = task.call();

            if (result) {
                log.info("【 executeJob 】任务成功. 设备: {}, 任务类型: {}", deviceKey, type);
            } else {
                log.error("【 executeJob 】任务失败. 设备: {}, 任务类型: {}", deviceKey, type);
            }

            return result;
        } catch (Exception e) {
            log.error("【 executeJob 】任务执行异常. 设备: {}, 任务类型: {}, 传输过程中发生异常: {}", deviceKey, type, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 执行 Shell 命令
     */
    @Override
    public String executeCommand(String command) {
        return executeCommand(command, null, null);
    }

    /**
     * 执行 Shell 命令
     *
     * @param command 命令
     * @param timeout 超时时间
     * @param unit    单位
     * @return {@link String }
     */
    @Override
    public String executeCommand(String command, @Nullable Long timeout, TimeUnit unit) {
        // return useJavaExecuteCommand(command, timeout, unit);
        return useApacheExecuteCommand(command, timeout, unit);
    }

    private String useJavaExecuteCommand(String command, @Nullable Long timeout, TimeUnit unit) {
        try {
            log.info("执行命令: {}", command);
            StopWatch stopWatch = new StopWatch();

            stopWatch.start();
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            if (timeout == null || timeout <= 0 || unit == null) {
                process.waitFor();
            } else {
                process.waitFor(timeout, unit);
            }
            reader.close();

            stopWatch.stop();
            log.info("执行命令结束: {}，耗时：{}ms，执行的结果：{}", command, stopWatch.getTotalTimeMillis(), result);

            return result.toString();
        } catch (Exception e) {
            log.error("执行命令失败: {}, 错误信息: {}", command, e.getMessage(), e);
            return null;
        }
    }

    private String useApacheExecuteCommand(String command, Long timeout, TimeUnit unit) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        DefaultExecutor executor = DefaultExecutor.builder().get();
        executor.setStreamHandler(new PumpStreamHandler(outputStream, errorStream));
        if (timeout == null || timeout <= 0 || unit == null) {
            executor.setWatchdog(ExecuteWatchdog.builder().get());
        } else {
            executor.setWatchdog(ExecuteWatchdog.builder().setTimeout(Duration.ofMillis(unit.toMillis(timeout))).get());
        }

        try {
            log.info("开始执行命令: {}", command);
            StopWatch stopWatch = new StopWatch();

            CommandLine cmdLine = CommandLine.parse(command);
            int exitValue = executor.execute(cmdLine);
            if (exitValue != 0) {
                log.warn("命令执行返回非零值: {}, 错误输出: {}", exitValue, errorStream.toString("UTF-8"));
            }
            String result = outputStream.toString("UTF-8");

            log.info("执行命令结束: {}，耗时：{}ms，执行结果：{}", command, stopWatch.getTotalTimeMillis(), result);
            return result;
        } catch (ExecuteException e) {
            if (executor.getWatchdog().killedProcess()) {
                log.error("命令执行超时被终止: {}", command);
            } else {
                log.error("命令执行异常: {}", command, e);
            }
        } catch (IOException e) {
            log.error("IO异常: {}", command, e);
        }
        return null;
    }

}
