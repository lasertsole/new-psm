package com.psm.domain.Model.modelUserBind.entity;

import com.psm.domain.Model.model.valueObject.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BriefModelVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4840995604366346488L;

    private String id;
    private String title;
    private String content;
    private String cover;
    private String entity;
    private Category category;
    private String createTime;
}
