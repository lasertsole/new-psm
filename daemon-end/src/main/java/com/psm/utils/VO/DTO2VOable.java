package com.psm.utils.VO;

//  BO都应该实现该接口DTO转VO方法
public interface DTO2VOable<R> {
    R toVO();
}
