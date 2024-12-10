package com.psm.infrastructure.SocketIO.POJOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomInvitation implements Serializable {
    @NotNull
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    @Length(max = 10, min = 6, message = "The roomId must be less than or equal to 20 characters, and more than or equal to 6 characters")
    String roomId;

    @NotNull
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    String roomOwnerId;

    @NotNull
    @Length(max = 10, min = 1, message = "The roomId must be less than or equal to 20 characters, and more than or equal to 1 characters")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The content format is incorrect")
    String roomName;

    @NotNull
    String roomType;

    @NotNull
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    String srcUserId;

    @NotNull
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The username format is incorrect")
    String srcUserName;

    @NotNull
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    String tgtUserId;

    @NotNull
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The username format is incorrect")
    String tgtUserName;
}
