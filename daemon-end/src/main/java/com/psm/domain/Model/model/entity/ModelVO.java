package com.psm.domain.Model.model.entity;

import com.psm.domain.Model.model.valueObject.Category;
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
    private static final long serialVersionUID = 6383509354550523034L;

    private String id;
    private String userId;
    private String title;
    private String content;
    private String cover;
    private String entity;
    private Category category;
    private Integer visible;
    private Long storage;
    private String createTime;
}
