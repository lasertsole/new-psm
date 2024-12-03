package com.psm.types.common.BO;

import com.psm.types.common.DO.DO;
import com.psm.types.common.DTO.DTO;

//  BO都应该实现该接口DTO转VO方法
public interface BO<DT extends DTO, D extends DO> {
    DT toDTO();
    D toDO();
}
