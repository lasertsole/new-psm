package com.psm.types.common.ES.DO;

import com.psm.types.common.DO.DO;
import com.psm.types.common.ES.BO.ESResultPageBO;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ESResultPageDO implements Serializable, DO<ESResultPageBO> {
    private List<ESResultDO> records = new ArrayList<>();
    private Long total;
    private Integer size;
    private Long from;
//    private Integer current;
//    private Long pages;

    public void setRecords(List<ESResultDO> records) {
        this.records = records;
    };

    public List<ESResultDO> getRecords() {
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

    @Override
    public ESResultPageBO toBO() {
        ESResultPageBO esResultPageBO = new ESResultPageBO();
        BeanUtils.copyProperties(this, esResultPageBO);
        esResultPageBO.setRecords(this.records.stream().map(ESResultDO::toBO).toList());

        return esResultPageBO;
    }
}
