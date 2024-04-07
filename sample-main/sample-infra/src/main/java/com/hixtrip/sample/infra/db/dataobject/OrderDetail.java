package com.hixtrip.sample.infra.db.dataobject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 订单明细
 *  order_detail
 */

@Data
@TableName("order_detail")
public class OrderDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单明细id
     */
    private String id;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 用户id
     */
    private String userId;

    private String skuId;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 名称
     */
    private String name;

    /**
     * 封面地址
     */
    private String coverUrl;

    /**
     * 折扣金额
     */
    private Integer discountAmount;

    /**
     * 实付金额
     */
    private Integer realPayAmount;

    /**
     * 订单详情状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名
     */
    private Integer status;

    /**
     * 1：待审批，2：取消退款，3：同意退款，4：拒绝退款，5：退款成功，6：退款失败'
     */
    private Integer refundStatus;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 支付渠道名称
     */
    private String payChannel;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

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


}