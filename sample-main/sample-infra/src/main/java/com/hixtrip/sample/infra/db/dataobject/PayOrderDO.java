package com.hixtrip.sample.infra.db.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * 支付订单
 * @author xiang
 * @TableName pay_order
 */
@TableName(value ="pay_order")
@Data
@Builder
public class PayOrderDO implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 业务订单号
     */
    private String bizOrderNo;

    /**
     * 支付单号
     */
    private String payOrderNo;

    /**
     * 支付用户id
     */
    private String bizUserId;

    /**
     * 支付渠道编码
     */
    private String payChannelCode;

    /**
     * 支付金额，单位位分
     */
    private Integer amount;

    /**
     * 支付类型，1：h5,2:小程序，3：公众号，4：扫码
     */
    private Integer payType;

    /**
     * 支付状态，0：待提交，1:待支付，2：支付超时或取消，3：支付成功
     */
    private Integer status;

    /**
     * 拓展字段，用于传递不同渠道单独处理的字段
     */
    private String expandJson;

    /**
     * 业务端回调接口
     */
    private String notifyUrl;

    /**
     * 业务端回调次数
     */
    private Integer notifyTimes;

    /**
     * 回调状态，0：待回调，1：回调成功，2：回调失败
     */
    private String notifyStatus;

    /**
     * 第三方返回业务码
     */
    private String resultCode;

    /**
     * 第三方返回提示信息
     */
    private String resultMsg;

    /**
     * 支付成功时间
     */
    private LocalDateTime paySuccessTime;

    /**
     * 支付超时时间
     */
    private LocalDateTime payOverTime;

    /**
     * 支付二维码链接
     */
    private String qrCodeUrl;

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
    private String  updater;

    /**
     * 逻辑删除
     */
    private Boolean deleted;

}