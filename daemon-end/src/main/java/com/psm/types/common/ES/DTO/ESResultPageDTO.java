package com.psm.types.common.ES.DTO;

import com.psm.types.common.ES.BO.ESResultBO;
import com.psm.types.common.DTO.DTO;
import com.psm.types.common.ES.BO.ESResultPageBO;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ESResultPageDTO implements Serializable, DTO {
    private List<ESResultDTO> records = new ArrayList<>();
    private Long total;
    private Integer size;
    private Long from;
//    private Integer current;
//    private Long pages;

    public void setRecords(List<ESResultDTO> records) {
        this.records = records;
    };

    public List<ESResultDTO> getRecords() {
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
    }

    public ESResultPageDTO(ESResultPageBO esResultPageBO) {
        BeanUtils.copyProperties(esResultPageBO, this);
        this.records = esResultPageBO.getRecords().stream().map(ESResultBO::toDTO).toList();
    }
}
