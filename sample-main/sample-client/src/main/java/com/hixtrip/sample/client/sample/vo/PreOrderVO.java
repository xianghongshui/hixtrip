package com.hixtrip.sample.client.sample.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xianghs
 * @Date 2024/4/5
 * @remark:
 */
@Data
@Builder
public class PreOrderVO {

    /**
     * 生成的唯一订单id
     */
    private String orderId;

    /**
     * 支付链接-用于生成QR-CODE
     */
    private String payUrl;

    /**
     * 待支付的总金额
     */
    private Integer totalAmount;

    /**
     * 待支付的订单，超时时间
     */
    private LocalDateTime payOutTime;
}
