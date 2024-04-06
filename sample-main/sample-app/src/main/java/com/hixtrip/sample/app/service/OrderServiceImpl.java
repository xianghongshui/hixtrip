package com.hixtrip.sample.app.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hixtrip.sample.app.api.IPayService;
import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.app.convertor.CommandPayConvertor;
import com.hixtrip.sample.app.convertor.OrderConvertor;
import com.hixtrip.sample.app.handler.PayCallbackHandler;
import com.hixtrip.sample.app.handler.PayCallbackHandlerFactory;
import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.order.dto.OrderInfoDTO;
import com.hixtrip.sample.client.sample.vo.PreOrderVO;
import com.hixtrip.sample.common.constans.PayConstants;
import com.hixtrip.sample.common.constans.PayErrorInfo;
import com.hixtrip.sample.common.exception.CommonException;
import com.hixtrip.sample.common.pay.model.PayStatusResponse;
import com.hixtrip.sample.common.pay.model.PrepayResponse;
import com.hixtrip.sample.common.redisson.annotations.Lock;
import com.hixtrip.sample.domain.commodity.CommodityDomainService;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.PayDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static com.hixtrip.sample.common.constans.Constant.LONG_NUM_0;
import static com.hixtrip.sample.common.constans.PayConstants.WX_CHANNEL_CODE;
import static com.hixtrip.sample.common.pay.model.PayStatus.WAIT_BUYER_PAY;


/**
 * app层负责处理request请求，调用领域服务
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrderServiceImpl implements OrderService {

    private final CommodityDomainService commodityDomainService;


    private final OrderDomainService orderDomainService;


    private final Map<String, IPayService> payServiceChannels;

    private final RedisTemplate<String, Object> redisTemplate;

    private final PayDomainService payDomainService;

    private final PayCallbackHandlerFactory payCallbackHandlerFactory;

    /**
     * 注解切面实现分布式锁，以及锁释放的超时时间，支持EL表达式
     */
    @Lock(name = PayConstants.RedisKeyFormatter.PAY_APPLY, leaseTime = 3, autoUnlock = false)
    @Override
    public PreOrderVO createOrder(CommandOderCreateDTO dto) {
        //1.参数校验
        if (dto.getAmount() == null || dto.getAmount() < 1) {
            throw new CommonException("非法参数-商品数量错误");
        }


        //2.计算订单金额--实际业务场景可能害的考虑折扣和优惠计算
        BigDecimal skuPrice = commodityDomainService.getSkuPrice(dto.getSkuId());
        BigDecimal totalPrice = skuPrice.multiply(new BigDecimal(dto.getAmount()));

        //3.TODO 考虑订单号是否传-为空则第一次新建订单-有传则对订单号进行幂等性校验
        String cacheVal = "";
        String orderKey = String.join(":", "order", dto.getUserId(), dto.getSkuId(), String.valueOf(totalPrice));
        Object o = redisTemplate.opsForValue().get(orderKey);
        if (Objects.nonNull(o)) {
            cacheVal = o.toString();
        }
        if (StringUtils.isNotBlank(cacheVal)) {
            throw new CommonException("检查是否重复下单");
        }
        // 4.生成订单id
        String orderId = String.valueOf(IdWorker.getId());

        //5.假设前端用户传支付方式--wx/支付宝
        String payChannel = WX_CHANNEL_CODE;
        IPayService payService = payServiceChannels.get(payChannel);
        OrderInfoDTO orderInfoDTO = OrderConvertor.INSTANCE.oderCreateDTO2OrderInfo(dto);
        log.info("转换对象:{}", JSON.toJSONString(orderInfoDTO));
        // 将totalPrice转换为分，并转换为int类型--方便统一接口一个规范
        int totalPriceInCents = totalPrice.multiply(new BigDecimal("100")).intValue();

        PrepayResponse prepayResponse = payService.createPrepayOrder(JSON.toJSONString(orderInfoDTO), orderId, totalPriceInCents);

        if (!prepayResponse.isSuccess()) {
            log.error("预创建支付单失败，详情：{}", prepayResponse.getDetail());
            throw new CommonException(PayErrorInfo.CREATE_PAY_ORDER_FAILED);
        }
        /*
         * remark--内部跳转支付or扫码支付-预支付会返回支付链接和订单业务流水号给前端 这段业务接口结束
         * 预支付订单会抢占库存，支付回调成功-创建订单，失败则返回库存，重复支付理论在幂等性校验后不会存在，如若存在则开启
         * 新接口作为用户取消订单申请退款通道
         */
        //4.构建预支付订单对象--写入数据库持久化记录
        PreOrderVO preOrderVO = PreOrderVO.builder()
                .orderId(orderId)
                .payUrl(prepayResponse.getPayUrl())
                .totalAmount(totalPriceInCents)
                .payOutTime(LocalDateTime.now().plusMinutes(30)).build();

        Order order = Order.builder()
                .id(orderId)
                .userId(dto.getUserId())
                .skuId(dto.getSkuId())
                .amount(dto.getAmount())
                .money(totalPrice)
                .payTime(LocalDateTime.now())
                .payStatus(String.valueOf(WAIT_BUYER_PAY.getValue()))
                .delFlag(LONG_NUM_0)
                .createBy(dto.getUserId())
                .createTime(LocalDateTime.now())
                .updateBy(dto.getUserId())
                .updateTime(LocalDateTime.now())
                .build();
        orderDomainService.createOrder(order, preOrderVO);
        return preOrderVO;
    }

    @Override
    public String payCallback(CommandPayDTO dto) {
        CommandPay commandPay = CommandPayConvertor.INSTANCE.commandPayDTO2CommandPay(dto);
        payDomainService.payRecord(commandPay);
        /*
            策略工厂模式：
               初始化加载所有被标记@Component到回调工厂key为payStatus
               根据对应的payStatus执行对应的handler
               后续有新的策略也可直接实现PayCallbackHandler接口创建handler
         */
        String payChannel = WX_CHANNEL_CODE;
        IPayService payService = payServiceChannels.get(payChannel);
        PayStatusResponse payStatusResponse = payService.queryPayOrderStatus(dto.getOrderId());
        PayCallbackHandler handler = payCallbackHandlerFactory.getHandler(payStatusResponse.getPayStatus());
        log.debug("支付状态回调查询:{}", JSON.toJSONString(payStatusResponse));
        handler.payCallback(commandPay);
        return null;
    }


}
