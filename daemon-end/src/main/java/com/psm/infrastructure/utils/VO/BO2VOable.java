package com.psm.infrastructure.utils.VO;

//  BO都应该实现该接口BO转VO方法
public interface BO2VOable<R> {
    R toVO();
}
