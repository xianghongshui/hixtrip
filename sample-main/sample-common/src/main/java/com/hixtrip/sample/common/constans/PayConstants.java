package com.hixtrip.sample.common.constans;

public interface PayConstants {
    String ALI_CHANNEL_CODE = "aliPay";
    String WX_CHANNEL_CODE = "wxPay";

    /**
     * redis key前缀
     */
    interface RedisKeyFormatter {
        String PAY_APPLY = "pre:pay:apply:bizUser:";

        String CHANGE_INVENTORY = "chage:inventory:";
    }
}
