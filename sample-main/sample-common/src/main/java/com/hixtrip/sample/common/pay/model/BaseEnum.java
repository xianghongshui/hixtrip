package com.hixtrip.sample.common.pay.model;

import org.apache.commons.lang3.StringUtils;

/**
 * xianghs
 */
public interface BaseEnum {
    String getValue();
    String getDesc();

    default boolean equalsValue(String value){
        if (value == null) {
            return false;
        }
        return StringUtils.equals(getValue(),value) ;
    }
}
