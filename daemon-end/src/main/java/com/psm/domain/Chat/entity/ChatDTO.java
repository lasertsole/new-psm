package com.psm.domain.Chat.entity;

import com.tangzc.mpe.autotable.annotation.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO implements Serializable {
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long id;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    @Column(comment = "目标用户id", notNull = true)
    private Long tgtUserId;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    @Column(comment = "来源用户id", notNull = true)
    private Long srcUserId;

    @Size(max = 255, message = "The content length must not exceed 255 characters")
    private String content;
}
