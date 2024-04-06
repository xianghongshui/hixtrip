package com.hixtrip.sample.domain.order.repository;

import com.hixtrip.sample.client.sample.vo.PreOrderVO;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.model.PayOrder;


/**
 *
 */
public interface OrderRepository {

    /**
     * 创建订单
     */
    Boolean createOrder(Order order, PreOrderVO preOrderVO);

    PayOrder selectById(String orderId);

    Boolean updatePayOrder(PayOrder payOrder);

    void saveOrderAndDetail(PayOrder payOrder);
}
