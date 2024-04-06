package com.hixtrip.sample.common.base;


import com.hixtrip.sample.common.constans.Constant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

import static com.hixtrip.sample.common.constans.ErrorInfo.Code.FAILED;
import static com.hixtrip.sample.common.constans.ErrorInfo.Code.SUCCESS;
import static com.hixtrip.sample.common.constans.ErrorInfo.Msg.OK;


@Data
public class R<T> {

    private int code;

    private String msg;

    private T data;

    private String requestId = StringUtils.isBlank(MDC.get("requestId")) ? UUID.randomUUID().toString() : MDC.get("requestId");

    public static R<Void> ok() {
        return new R<Void>(SUCCESS, OK, null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(SUCCESS, OK, data);
    }

    public static <T> R<T> error(String msg) {
        return new R<>(FAILED, msg, null);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }

    public R() {
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.requestId = MDC.get(Constant.REQUEST_ID_HEADER);
    }

    public boolean success() {
        return code == SUCCESS;
    }

    public R<T> requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}
