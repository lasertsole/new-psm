package com.psm.domain.Model.entity;

import com.psm.domain.Model.valueObject.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 290416843826672398L;

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String entity;
    private Category category;
    private Short visible;
    private Long storage;
    private String createTime;
}
