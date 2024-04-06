package com.hixtrip.sample.app.handler;

import com.hixtrip.sample.common.pay.model.PayStatus;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.hixtrip.sample.common.pay.model.PayStatus.TRADE_FAIL;

/**
 * @author xianghs
 * @Date 2024/4/6
 * @remark: 支付回调失败处理器
 */
@RequiredArgsConstructor
@Component
public class PayFailHandler implements PayCallbackHandler{


    private final OrderDomainService orderDomainService;
    @Override
    public PayStatus getHandlerType() {
        return TRADE_FAIL;
    }

    @Override
    public void payCallback(CommandPay commandPay) {
        orderDomainService.orderPayFail(commandPay);
    }
}
