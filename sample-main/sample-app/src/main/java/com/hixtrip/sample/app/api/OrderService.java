package com.hixtrip.sample.app.api;

import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.sample.vo.PreOrderVO;
import com.hixtrip.sample.domain.order.model.Order;

/**
 * 订单的service层
 */
public interface OrderService {


    PreOrderVO createOrder(CommandOderCreateDTO commandOderCreateDTO);

    String payCallback(CommandPayDTO commandPayDTO);
}
