package com.psm.types.entity.page;

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

    private List<T> records; // 当前页数据
    private Long total; // 总记录数
    private Long size; // 每页显示条数
    private Long current; // 当前页
    private Long pages; // 总页数
    private List<OrderItem> orders; // 排序字段
    private Long maxLimit; // 单页显示最大数量
    private String countId; // 查询总记录数 SQLId
}
