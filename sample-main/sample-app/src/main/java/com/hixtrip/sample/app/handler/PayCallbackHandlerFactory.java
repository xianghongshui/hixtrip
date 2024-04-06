package com.hixtrip.sample.app.handler;

import com.hixtrip.sample.common.pay.model.PayStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xianghs
 * @Date 2024/4/6
 * @remark: 支付回调处理工厂
 */
@Component
public class PayCallbackHandlerFactory implements InitializingBean {

    @Autowired
    private List<PayCallbackHandler> payCallbackHandlers;

    private final Map<PayStatus, PayCallbackHandler> handlerMap = new HashMap<>();

    public PayCallbackHandler getHandler(String type) {
        PayStatus payStatus = PayStatus.transferByCode(type);
        return handlerMap.get(payStatus);
    }


    @Override
    public void afterPropertiesSet() {
        for (PayCallbackHandler handler : payCallbackHandlers) {
            handlerMap.put(handler.getHandlerType(), handler);
        }
    }

}
