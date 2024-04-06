package com.hixtrip.sample.common.constans;

public interface Constant {
    String REQUEST_ID_HEADER = "requestId";
    String REQUEST_FROM_HEADER = "x-request-from";

    String GATEWAY_ORIGIN_NAME = "gateway";

    /**
     * 数据已经删除标识值
     */
    boolean DATA_DELETE = true;
    /**
     * 数据未删除标识值
     */
    boolean DATA_NOT_DELETE = false;
    /**
     *
     * 响应结果是否被R标记过
     */
    String BODY_PROCESSED_MARK_HEADER = "IS_BODY_PROCESSED";

    long LONG_NUM_0 = 0;
}
