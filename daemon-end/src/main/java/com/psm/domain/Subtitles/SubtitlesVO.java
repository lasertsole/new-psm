package com.psm.domain.Subtitles;

import java.io.Serializable;

public class SubtitlesVO implements Serializable {
    private static final long serialVersionUID = 1331789288879702334L;

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String video;
    private String category;
}
