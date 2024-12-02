package com.psm.types.common.BO;

//  BO都应该实现该接口DTO转VO方法
public interface BO<R> {
    R toDTO();
}
