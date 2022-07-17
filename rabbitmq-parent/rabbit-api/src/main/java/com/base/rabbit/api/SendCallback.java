package com.base.rabbit.api;

/**
 * 回调函数
 */
public interface SendCallback {

    void onSuccess();

    void onFailure();
}
