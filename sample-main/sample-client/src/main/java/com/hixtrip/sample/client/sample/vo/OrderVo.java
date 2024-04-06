package com.hixtrip.sample.client.sample.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author xianghs
 * @Date 2024/4/5
 * @remark:
 */
@Data
@Builder
public class OrderVo {
    private String orderId;
    private String userId;
}
