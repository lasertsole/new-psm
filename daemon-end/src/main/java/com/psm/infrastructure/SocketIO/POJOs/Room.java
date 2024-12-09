package com.psm.infrastructure.SocketIO.POJOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.app.annotation.validation.MustNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Room implements Serializable {
    @NotNull
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    @Length(max = 10, min = 6, message = "The roomId must be less than or equal to 20 characters, and more than or equal to 6 characters")
    private String roomId;

    @NotNull
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private String roomOwnerId;

    @NotNull
    @Length(max = 10, min = 1, message = "The roomId must be less than or equal to 20 characters, and more than or equal to 1 characters")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The content format is incorrect")
    private String roomName;

    @NotNull
    private String roomType;

    @MustNull
    private Set<String> memberIdSet;
}
