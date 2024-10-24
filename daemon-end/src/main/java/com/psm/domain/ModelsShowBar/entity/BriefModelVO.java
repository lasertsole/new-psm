package com.psm.domain.ModelsShowBar.entity;

import com.psm.domain.Model.valueObject.Category;
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
    private static final long serialVersionUID = 4977840824752597601L;

    private Long id;
    private String title;
    private String cover;
    private Category category;
    private String createTime;
}
