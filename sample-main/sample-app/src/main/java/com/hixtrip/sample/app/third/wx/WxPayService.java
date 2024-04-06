package com.hixtrip.sample.app.third.wx;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hixtrip.sample.app.api.IPayService;
import com.hixtrip.sample.common.constans.PayConstants;
import com.hixtrip.sample.common.pay.model.PayStatusResponse;
import com.hixtrip.sample.common.pay.model.PrepayResponse;
import com.hixtrip.sample.common.pay.model.RefundResponse;
import com.hixtrip.sample.infra.db.dataobject.PayOrderDO;
import com.hixtrip.sample.infra.db.mapper.PayOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.hixtrip.sample.common.pay.model.PayStatus.TRADE_FAIL;
import static com.hixtrip.sample.common.pay.model.PayStatus.TRADE_SUCCESS;

@Slf4j
@Service(PayConstants.WX_CHANNEL_CODE)
@RequiredArgsConstructor
public class WxPayService implements IPayService {

    private final PayOrderMapper payOrderMapper;

    @Override
    public PrepayResponse createPrepayOrder(String title, String orderNo, Integer amount) {
        return PrepayResponse.builder()
                .success(true)
                .code("200")
                .msg("提交预支付订单成功")
                .payUrl("2hichat.fun")
                .detail("订单号:" + orderNo)
                .build();
//        return new PrepayResponse();
    }


    @Override
    public PayStatusResponse queryPayOrderStatus(String payOrderNo) {
        String orderNo = IdWorker.get32UUID();
        PayOrderDO payOrderDO = payOrderMapper.selectById(payOrderNo);
        return PayStatusResponse.builder()
                .success(true)
                .code("200")
                .msg(TRADE_SUCCESS.getDesc())
                .payOrderNo(orderNo)
                .payStatus(TRADE_SUCCESS.getValue())
//                .payStatus(TRADE_FAIL.getValue())
                .totalAmount(payOrderDO.getAmount())
                .successTime(payOrderDO.getPaySuccessTime()).build();
//        return new PayStatusResponse();
    }

    @Override
    public RefundResponse refundOrder(String payOrderNo, String refundOrderNo, Integer refundAmount, Integer totalAmount) {
        return new RefundResponse();
    }

    @Override
    public RefundResponse queryRefundStatus(String orderNo, String refundOrderNo) {
        return new RefundResponse();
    }

    private Integer parseRefundStatus(String status) {
        switch (status) {
            case "PROCESSING":
                return 1;
            case "SUCCESS":
                return 2;
            default:
                return 3;
        }
    }

    private Integer parsePayStatus(String tradeState) {
        switch (tradeState) {
            case "REFUND":
            case "CLOSED":
            case "REVOKED":
                return 2;
            case "SUCCESS":
                return 3;
            default:
                return 1;
        }
    }

}
