package com.hixtrip.sample.infra.db.dataobject;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 订单表
 * @author xianghs
 */

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("tb_order")
@SuperBuilder(toBuilder = true)
public class OrderDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 订单id
     */

    private String id;
    /**
     * 用户id
     */

    private String userId;
    /**
     * 商家id
     */

    private String sellerId;
    /**
     * 业务订单号
     */

    private String bizOrderNo;
    /**
     * 订单号
     */

    private String orderNo;
    /**
     * 交易流水支付单号
     */

    private String payOrderNo;
    /**
     * 订单状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名，6：已申请退款
     */

    private Integer status;
    /**
     * 状态备注
     */

    private String message;
    /**
     * 订单总金额，单位分
     */

    private Integer totalAmount;
    /**
     * 实付金额，单位分
     */

    private Integer realAmount;
    /**
     * 优惠金额，单位分
     */

    private Integer discountAmount;
    /**
     * 支付渠道
     */

    private String payChannel;
    /**
     * 优惠券id
     */
    private Long couponId;
    /**
     * 创建订单时间
     */

    private LocalDateTime createTime;
    /**
     * 支付时间
     */

    private LocalDateTime payTime;
    /**
     * 订单关闭时间
     */

    private LocalDateTime closeTime;
    /**
     * 订单完成时间，支付后30天
     */

    private LocalDateTime finishTime;
    /**
     * 申请退款时间
     */

    private LocalDateTime refundTime;
    /**
     * 更新时间
     */


    private LocalDateTime updateTime;
    /**
     * 创建人
     */

    private String creater;
    /**
     * 更新人
     */

    private String updater;
    /**
     * 逻辑删除
     */

    private Integer deleted;


}
