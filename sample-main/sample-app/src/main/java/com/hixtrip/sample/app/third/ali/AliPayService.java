package com.hixtrip.sample.app.third.ali;


import com.hixtrip.sample.app.api.IPayService;

import com.hixtrip.sample.common.pay.model.PayStatusResponse;
import com.hixtrip.sample.common.pay.model.PrepayResponse;
import com.hixtrip.sample.common.pay.model.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.hixtrip.sample.common.constans.PayConstants.ALI_CHANNEL_CODE;


@Slf4j
@Service(ALI_CHANNEL_CODE)
@RequiredArgsConstructor
public class AliPayService implements IPayService {


    @Override
    public PrepayResponse createPrepayOrder(String title, String orderNo, Integer amount) {
        return new PrepayResponse();
    }


    @Override
    public PayStatusResponse queryPayOrderStatus(String payOrderNo) {
        return new PayStatusResponse();
    }

    @Override
    public RefundResponse refundOrder(String payOrderNo, String refundOrderNo, Integer refundAmount, Integer totalAmount) {
        return new RefundResponse();
    }

    @Override
    public RefundResponse queryRefundStatus(String orderNo, String refundOrderNo) {
        return new RefundResponse();
    }


}
