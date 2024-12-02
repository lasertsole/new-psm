package com.psm.types.common.DO;

//  DO都应该实现该接口DO转BO方法
public interface DO<R> {
    R toBO();
}
