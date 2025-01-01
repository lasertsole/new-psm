package com.psm.types.common.POJO;

import java.io.Serializable;

//  BO都应该实现该接口DTO转VO方法
public interface BO<DT extends DTO, D extends DO> extends Serializable {
    DT toDTO();
    D toDO();
}
