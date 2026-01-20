package com.ruoyi.meeting.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Snow
 */
@Slf4j
public class ConcurrentUtil {

    // 线程池用于异步传输
    public static final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2
    );

    public static void waitThreadPoolTaskFinish(List<Future<Boolean>> futureList, AtomicInteger successCount) {
        // 等待所有任务完成并统计成功数量
        for (Future<Boolean> future : futureList) {
            try {
                if (future.get() && successCount != null) {
                    successCount.incrementAndGet();
                }
            } catch (Exception e) {
                log.error("传输任务执行异常: {}", e.getMessage(), e);
            }
        }
    }

}
