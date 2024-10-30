package com.psm.domain.Model.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.valueObject.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2062472239288579355L;

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
