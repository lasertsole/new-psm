package com.psm.utils.Timestamp;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimestampUtils {
    public static String generateUTCTimestamp(DateTimeFormatter formatter) {
        // 生成当前 UTC 时间的时间戳(为了国际通用)并格式化为包含微秒的字符串
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        String timestamp = now.format(formatter);

        return timestamp;
    }

    public static String generateUTCTimestamp() {
        return TimestampUtils.generateUTCTimestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
    }
}
