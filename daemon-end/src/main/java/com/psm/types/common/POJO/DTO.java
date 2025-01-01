package com.psm.types.common.POJO;

import java.io.Serializable;

public interface DTO<B extends BO> extends Serializable {
    B toBO();
}