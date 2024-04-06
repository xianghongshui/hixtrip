package com.hixtrip.sample.common.pay.model;


import lombok.Getter;

@Getter
public enum RefundStatus implements BaseEnum {
    NOT_COMMIT("0", "退款请求未提交"),
    UN_KNOWN("1", "未知，可能是失败或未完成"),
    SUCCESS("2", "退款成功"),
    FAILED("3", "退款失败"),
    ;
    private final String value;
    private final String desc;

    RefundStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
