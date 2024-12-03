package com.psm.types.common.DO;

import com.psm.types.common.BO.BO;
import com.psm.types.common.DTO.DTO;

//  DO都应该实现该接口DO转BO方法
public interface DO<B extends BO,D extends DTO> {
    B toBO();
    D toDTO();
}
