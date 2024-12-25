package com.psm.trigger.routine;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.domain.IndependentDomain.Communication.Chat.entity.ChatDO;
import com.psm.infrastructure.DB.ChatMapper;
import com.psm.infrastructure.Tus.properties.TusProperties;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class AutoClear {
    @Autowired
    private TusProperties tusProperties;

    @Autowired
    private TusFileUploadService tusFileUploadService;

    @Autowired
    private ChatMapper chatMapper;

    /**
     * 每24小时执行一次，清理大文件上传的临时文件
     */
    @Scheduled(fixedDelayString = "PT24H")
    public void clearUp() {
        //删除过期文件
        Path locksDir = tusProperties.getTusDataPath().resolve("locks");
        if (Files.exists(locksDir)) {
            try {
                tusFileUploadService.cleanup();
            } catch (IOException e) {
                log.error("Error cleaning file upload directory:", e);
            }
        }
    }

    /**
     * 每周日0点执行一次，清理七天前的聊天记录
     */
    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨 0 点执行
    public void deleteOldChatRecords() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 减去七天
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        // 转换为 UTC 时间
        ZonedDateTime utcSevenDaysAgo = sevenDaysAgo.atZone(ZoneId.of("UTC"));

        // 格式化时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String timestamp = utcSevenDaysAgo.format(formatter);
        LambdaQueryWrapper<ChatDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(ChatDO::getTimestamp, timestamp);
        chatMapper.delete(wrapper);
    }
}
