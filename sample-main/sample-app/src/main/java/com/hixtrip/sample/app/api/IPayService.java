package com.hixtrip.sample.app.api;


import com.hixtrip.sample.common.pay.model.PayStatusResponse;
import com.hixtrip.sample.common.pay.model.PrepayResponse;
import com.hixtrip.sample.common.pay.model.RefundResponse;

/**
 * @author xianghs
 * @Date 2024/4/4
 * @remark: 统一支付服务接口
 */
public interface IPayService {

    PrepayResponse createPrepayOrder(String title, String orderNo, Integer amount);

    PayStatusResponse queryPayOrderStatus(String payOrderNo);

    RefundResponse refundOrder(String payOrderNo, String refundOrderNo, Integer refundAmount, Integer totalAmount);

    RefundResponse queryRefundStatus(String orderNo, String refundOrderNo);
}
