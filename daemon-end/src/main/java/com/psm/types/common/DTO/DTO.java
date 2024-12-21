package com.psm.types.common.DTO;

import com.psm.types.common.BO.BO;

import java.io.Serializable;

public interface DTO<B extends BO> extends Serializable {
    B toBO();
}