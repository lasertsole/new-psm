package com.psm.utils.DTO;

//  BO都应该实现该接口DTO转VO方法
public interface BO2DTOable<R> {
    R toDTO();
}
