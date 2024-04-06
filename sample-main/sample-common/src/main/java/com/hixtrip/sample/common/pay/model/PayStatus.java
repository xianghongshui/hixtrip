package com.hixtrip.sample.common.pay.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum PayStatus implements BaseEnum {
    NOT_COMMIT("0", "未提交"),
    WAIT_BUYER_PAY("1", "待支付"),
    TRADE_CLOSED("2", "已关闭"),
    TRADE_SUCCESS("3", "支付成功"),
    TRADE_FINISHED("3", "支付成功"),
    TRADE_FAIL("4", "支付失败"),
    ;
    private final String value;
    private final String desc;

    PayStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static PayStatus transferByCode(String codeVal){
        for(PayStatus resultCodeEnum : PayStatus.values()){
            if (StringUtils.equals(codeVal,resultCodeEnum.getValue())){
                return resultCodeEnum;
            }
        }
        return null;
    }

}
