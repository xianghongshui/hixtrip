package com.hixtrip.sample.app.handler;

import com.hixtrip.sample.common.pay.model.PayStatus;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.stereotype.Component;

/**
 * @author xianghs
 * @Date 2024/4/6
 * @remark:
 */
@Component
public interface PayCallbackHandler {

    PayStatus getHandlerType();

    /**
     * 支付回调处理
     * @param orderId {@link String 订单id}
     * @param payStatus {@link String 支付状态}
     */
    String payCallback(CommandPay commandPay);
}
