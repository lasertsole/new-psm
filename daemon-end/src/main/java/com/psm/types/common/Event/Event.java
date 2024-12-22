package com.psm.types.common.Event;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.psm.utils.Timestamp.TimestampUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
public class Event<T> implements Serializable {
    private String contentJSONString;
    private Class<T> contentClazz;
    private String timeStamp = TimestampUtils.generateUTCTimestamp();

    private String toJSONString(T value) {
        try {
            if (value == null) {
                return null;
            } else{
                return JSON.toJSONString(value);
            }
        } catch (Exception e) {
            log.error("toJSONString error :{}", e.getMessage());
            return null;
        }
    };

    public void setContent(T content) {
        try {
            if (content == null) {
                throw new IllegalArgumentException("The object must not be null.");
            } else{
                this.contentJSONString = toJSONString(content);
            }
        } catch (Exception e) {
            log.error("setContent error :{}", e.getMessage());
        }
    };

    public T getContent() {
        try {
            if (this.contentJSONString == null) {
                return null;
            } else {
                return JSON.parseObject(this.contentJSONString, this.contentClazz, JSONReader.Feature.SupportClassForName);
            }
        } catch (Exception e) {
            log.error("getContent error :{}", e.getMessage());
            return null;
        }
    };

    public Event(T content, Class<T> contentClazz) {
        try{
            this.setContent(content);
            this.contentClazz = contentClazz;
        } catch (Exception e) {
            log.error("Event constructor error :{}", e.getMessage());
        }
    };
};
