package com.psm.types.common.POJO;

import java.io.Serializable;

//  DO都应该实现该接口DO转BO方法
public interface DO<B extends BO,D extends DTO> extends Serializable {
    B toBO();
    D toDTO();
}
