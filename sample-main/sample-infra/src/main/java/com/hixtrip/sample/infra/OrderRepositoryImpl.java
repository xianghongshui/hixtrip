package com.hixtrip.sample.infra;

import com.alibaba.fastjson.JSON;
import com.hixtrip.sample.client.sample.vo.PreOrderVO;
import com.hixtrip.sample.common.pay.model.NotifyStatus;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.model.PayOrder;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.infra.db.convertor.PayOrderDOConvertor;
import com.hixtrip.sample.infra.db.dataobject.OrderDO;
import com.hixtrip.sample.infra.db.dataobject.OrderDetail;
import com.hixtrip.sample.infra.db.dataobject.PayOrderDO;
import com.hixtrip.sample.infra.db.mapper.OrderDetailMapper;
import com.hixtrip.sample.infra.db.mapper.OrderMapper;
import com.hixtrip.sample.infra.db.mapper.PayOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xianghs
 * @Date 2024/4/4
 * @remark:
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderRepositoryImpl implements OrderRepository {

    private final PayOrderMapper payOrderMapper;

    private final OrderMapper orderMapper;

    private final OrderDetailMapper orderDetailMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean createOrder(Order order, PreOrderVO vo) {
        //插入DB预支付订单
        PayOrderDO payOrderDO = PayOrderDO.builder()
                .id(order.getId())
                .bizOrderNo(vo.getOrderId())
                .payOrderNo(vo.getOrderId())
                .bizUserId(order.getUserId())
                .payChannelCode("wxPay")
                .amount(vo.getTotalAmount())
                .payType(4)
                .status(1)
                .expandJson(JSON.toJSONString(order))
                .notifyUrl("")
                .notifyTimes(0)
                .notifyStatus(NotifyStatus.UN_CALL.getValue())
                .resultCode("")
                .resultMsg("")
                .payOverTime(vo.getPayOutTime())
                .qrCodeUrl(vo.getPayUrl())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .creater(order.getUserId())
                .updater(order.getUserId()).build();
        return payOrderMapper.insert(payOrderDO) > 0;
    }

    @Override
    public PayOrder selectById(String orderId) {
        return PayOrderDOConvertor.INSTANCE.do2Domain(payOrderMapper.selectById(orderId));
    }

    @Override
    public Boolean updatePayOrder(PayOrder payOrder) {
        PayOrderDO payOrderDO = PayOrderDOConvertor.INSTANCE.domain2DO(payOrder);
        return payOrderMapper.updateById(payOrderDO) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrderAndDetail(PayOrder payOrder) {
        log.info("payOrder:{}", JSON.toJSONString(payOrder));
        String orderJson = payOrder.getExpandJson();
        Order order = JSON.parseObject(orderJson, Order.class);
        OrderDO orderDO = new OrderDO();
        orderDO.setId(payOrder.getId());
        orderDO.setUserId(payOrder.getBizUserId());
        orderDO.setSellerId("");
        orderDO.setBizOrderNo(payOrder.getBizOrderNo());
        orderDO.setOrderNo(payOrder.getBizOrderNo());
        orderDO.setPayOrderNo(payOrder.getPayOrderNo());
        orderDO.setStatus(payOrder.getStatus());
        orderDO.setMessage("");
        orderDO.setTotalAmount(payOrder.getAmount());
        orderDO.setRealAmount(payOrder.getAmount());
        orderDO.setDeleted(order.getAmount() - payOrder.getAmount());
        orderDO.setPayChannel("wxPay");
        orderDO.setCouponId(0L);
        orderDO.setCreateTime(LocalDateTime.now());
        orderDO.setPayTime(payOrder.getPaySuccessTime());
        orderDO.setCreateTime(payOrder.getCreateTime());
        // 订单支付后30天的时间
        LocalDateTime successTime = payOrder.getPaySuccessTime();
        LocalDateTime paymentFinishTime = successTime.plusDays(30);
        orderDO.setFinishTime(paymentFinishTime);
        orderDO.setCreater(payOrder.getCreater());
        orderDO.setUpdater(payOrder.getUpdater());
        orderDO.setDeleted(0);
        log.info("orderDo:{}", JSON.toJSONString(orderDO));
        orderMapper.insert(orderDO);
        OrderDetail orderDetail = packageOrderDetail(order, payOrder);
        orderDetailMapper.insert(orderDetail);
    }


    private OrderDetail packageOrderDetail(Order order, PayOrder payOrder) {
        OrderDetail detail = new OrderDetail();
        detail.setUserId(order.getUserId());
        detail.setOrderId(order.getId());
        detail.setStatus(2);
        int money = order.getMoney().multiply(new BigDecimal("100")).intValue();
        detail.setPrice(money);
        detail.setCoverUrl("");
        detail.setName(order.getSkuId());
        detail.setPayChannel("wxPay");
        detail.setDiscountAmount(0);
        detail.setRealPayAmount(payOrder.getAmount());
        detail.setCreater(payOrder.getCreater());
        detail.setCreateTime(payOrder.getCreateTime());
        detail.setUpdater(payOrder.getUpdater());
        detail.setUpdateTime(payOrder.getUpdateTime());
        detail.setSkuId(order.getSkuId());
        return detail;
    }

}
