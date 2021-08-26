package com.transport.mall.utils.common;

public interface GenericCallBackTwoParams<T, Q> {
    void onResponse(T output, Q output2);
}