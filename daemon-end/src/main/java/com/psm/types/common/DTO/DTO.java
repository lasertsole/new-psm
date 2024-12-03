package com.psm.types.common.DTO;

import com.psm.types.common.BO.BO;

public interface DTO<B extends BO> {
    B toBO();
}