package com.psm.infrastructure.SocketIO.POJOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RTCSwap implements Serializable {
    @NotNull
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    @Length(max = 10, min = 6, message = "The roomId must be less than or equal to 20 characters, and more than or equal to 6 characters")
    private String roomId;

    @NotNull
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private String srcUserId;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The username format is incorrect")
    String srcUserName;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The avatar format is incorrect")
    @Size(max = 255, message = "The avatar length must not exceed 255 characters")
    private String srcUserAvatar;

    @NotNull
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    String tgtUserId;

    @NotNull
    private String data;//序列化的待转发数据
}
