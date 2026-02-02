package com.ruoyi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RuoYiApplication {
    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        log.info("操作系统: {}", os);
        if (os.contains("win")) {
            System.setProperty("log.charset", "GBK");
            System.setProperty("spring.profiles.active", "druid,win");
        } else {
            System.setProperty("log.charset", "UTF-8");
        }

        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("服务启动成功");

    }
}