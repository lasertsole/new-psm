package com.psm.infrastructure.utils.MybatisPlus.Page;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageVO<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -1577207436242422848L;

    private List<T> records;
    private Long total;
    private Long size;
    private Long current;
    private Long pages;
    private List<OrderItem> orders;
    private Long maxLimit;
    private String countId;
}
