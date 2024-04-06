package com.hixtrip.sample.common.pay.model;

import lombok.Getter;

@Getter
public enum NotifyStatus implements BaseEnum {
    UN_CALL("0", "未开始回调"),
    CALLING("1", "回调进行中"),
    SUCCESS("2", "回调成功"),
    FAILED("3", "所有回调都已失败"),
    ;
    private final String value;
    private final String desc;

    NotifyStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
