package com.hixtrip.sample.app.handler;

import com.hixtrip.sample.common.pay.model.PayStatus;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hixtrip.sample.common.pay.model.PayStatus.TRADE_SUCCESS;

/**
 * @author xianghs
 * @Date 2024/4/6
 * @remark: 支付回调成功处理器
 */
@Component
@RequiredArgsConstructor
public class PaySuccessHandler implements PayCallbackHandler {


    private final OrderDomainService orderDomainService;

    @Override
    public PayStatus getHandlerType() {
        return TRADE_SUCCESS;
    }

    @Override
    public String payCallback(CommandPay commandPay) {
        return orderDomainService.orderPaySuccess(commandPay);
    }

}
