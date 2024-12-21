package com.psm.types.common.Event;

import com.psm.utils.Timestamp.TimestampUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event<T> implements Serializable {
    T content;
    String timeStamp = TimestampUtils.generateUTCTimestamp();

    public Event(T content) {
        this.content = content;
    }
};
