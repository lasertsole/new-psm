package com.psm.types.common.ES.BO;

import com.psm.types.common.ES.DO.ESResultDO;
import com.psm.types.common.ES.DO.ESResultPageDO;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ESResultPageBO implements Serializable {
    private List<ESResultBO> records = new ArrayList<>();
    private Long total;
    private Integer size;
    private Long from;
//    private Integer current;
//    private Long pages;

    public void setRecords(List<ESResultBO> records) {
        this.records = records;
    };

    public List<ESResultBO> getRecords() {
        return records;
    };

    public void setTotal(Long total) {
        this.total = total;
    };

    public Long getTotal() {
        return total;
    };

    public void setSize(Integer size) {
        this.size = size;
    };

    public Integer getSize() {
        return size;
    };

    public void setFrom(Integer current) {
        this.from = from;
    };

    public Long getFrom() {
        return this.from;
    };

    public Integer getCurrent() {
        return (int) (this.from / this.size) + 1;
    }

    public Integer getPages() {
        if (total == null || size == null) throw new RuntimeException("total or size is null");
        return (int) Math.ceil((double) this.total / this.size);
    };

    public ESResultPageBO(ESResultPageDO esResultPageDO) {
        BeanUtils.copyProperties(esResultPageDO, this);
        this.records = esResultPageDO.getRecords().stream().map(ESResultDO::toBO).toList();
    }
}
