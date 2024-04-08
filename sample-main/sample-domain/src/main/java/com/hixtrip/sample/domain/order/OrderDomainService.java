package com.hixtrip.sample.domain.order;

import com.alibaba.fastjson.JSON;
import com.hixtrip.sample.client.sample.vo.PreOrderVO;
import com.hixtrip.sample.common.exception.CommonException;
import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.model.PayOrder;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 订单领域服务
 * todo 只需要实现创建订单即可
 */
@RequiredArgsConstructor
@Service
public class OrderDomainService {

    private final OrderRepository orderRepository;

    private final InventoryRepository inventoryRepository;

    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * todo 需要实现
     * 创建待付款订单
     * 抢占库存
     */
    public void createOrder(Order order, PreOrderVO preOrderVO) {
        //需要你在infra实现, 自行定义出入参
        long sellableQuantity = inventoryRepository.getSellableQuantity(order.getSkuId()).longValue();
        //库存检测
        if (sellableQuantity < order.getAmount().longValue()) {
            throw new CommonException("可售库存不足!");
        }
        Boolean result = orderRepository.createOrder(order, preOrderVO);
        //判断创建预支付订单成功则抢占缓存中的库存
        if (Boolean.TRUE.equals(result)) {
            //扣 - 可销售库存
            long withholdingQuantity = inventoryRepository.getWithholdingQuantity(order.getSkuId()).longValue();
            Boolean changeResult = inventoryRepository.changeInventory(order.getSkuId(),
                    sellableQuantity - order.getAmount().longValue(),
                    withholdingQuantity - order.getAmount().longValue(),
                    order.getAmount().longValue());
            //因为预支付已经预占 成功回调则不修改库存-对用户该笔预创建订单新增一个缓存key,检查幂等
            String orderKey = String.join(":", "order", order.getUserId(), order.getSkuId(), String.valueOf(order.getMoney()));
            if (Boolean.TRUE.equals(changeResult)) {
                redisTemplate.opsForValue().set(orderKey, orderKey, 15, TimeUnit.SECONDS);
            }
        }
    }

    public String orderPaySuccess(CommandPay commandPay) {
        String orderId = commandPay.getOrderId();
        PayOrder payOrder = orderRepository.selectById(orderId);
        String orderJson = payOrder.getExpandJson();
        Order order = JSON.parseObject(orderJson, Order.class);
        payOrder.setStatus(3); // 设置订单状态为支付成功
        payOrder.setNotifyStatus(1);
        payOrder.setPaySuccessTime(LocalDateTime.now());
        Boolean updateResult = orderRepository.updatePayOrder(payOrder);
        if (Boolean.TRUE.equals(updateResult)) {
            long sellableQuantity = inventoryRepository.getSellableQuantity(order.getSkuId()).longValue();
            long withholdingQuantity = inventoryRepository.getWithholdingQuantity(order.getSkuId()).longValue();

            // 扣减预占用库存和可售库存
            inventoryRepository.changeInventory(order.getSkuId(),
                    sellableQuantity,
                    withholdingQuantity,
                    order.getAmount().longValue());

            // 将预支付订单创建新建订单和订单详情
            orderRepository.saveOrderAndDetail(payOrder);
        }
        return "pay callBack success";
    }

    public String orderPayFail(CommandPay commandPay) {
        String orderId = commandPay.getOrderId();
        PayOrder payOrder = orderRepository.selectById(orderId);
        String orderJson = payOrder.getExpandJson();
        Order order = JSON.parseObject(orderJson, Order.class);
        payOrder.setStatus(4); // 设置订单状态为支付失败
        payOrder.setNotifyStatus(1);
        payOrder.setPaySuccessTime(LocalDateTime.now());
        Boolean updateResult = orderRepository.updatePayOrder(payOrder);
        if (Boolean.TRUE.equals(updateResult)) {
            long sellableQuantity = inventoryRepository.getSellableQuantity(order.getSkuId()).longValue();
            long withholdingQuantity = inventoryRepository.getWithholdingQuantity(order.getSkuId()).longValue();

            // 恢复之前预占用的库存
            inventoryRepository.changeInventory(order.getSkuId(),
                    sellableQuantity + order.getAmount().longValue(),
                    withholdingQuantity + order.getAmount().longValue(),
                    order.getAmount().longValue());
        }
        return "pay callBack fail";
    }
}
