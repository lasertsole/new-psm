package com.psm.event.valueObject;

import com.psm.types.enums.VisibleEnum;
import lombok.Value;

import java.io.Serializable;

@Value
public class UploadModel3DEvent implements Serializable {
    Long userId;
    Long modelSize;
    VisibleEnum visible;
}
