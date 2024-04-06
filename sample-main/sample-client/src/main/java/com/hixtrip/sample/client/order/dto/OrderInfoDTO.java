package com.hixtrip.sample.client.order.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author xianghs
 * @Date 2024/4/4
 * @remark:
 */
@Data
@Builder
public class OrderInfoDTO {
    /**
     * 商品规格id
     */
    private String skuId;

    /**
     * 购买数量
     */
    private Integer amount;
}
