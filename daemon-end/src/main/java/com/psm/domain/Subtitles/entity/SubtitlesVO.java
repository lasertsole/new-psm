package com.psm.domain.Subtitles.entity;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubtitlesVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4359005405758215344L;

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String video;
    private String style;
    private String type;
    private String createTime;
}
