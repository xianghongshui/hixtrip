package com.hixtrip.sample.entry;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.sample.vo.PreOrderVO;
import com.hixtrip.sample.common.base.R;
import com.hixtrip.sample.common.exception.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * todo 这是你要实现的
 *
 * @author xianghs
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;


    /**
     * todo 这是你要实现的接口
     *
     * @param commandOderCreateDTO 入参对象
     * @return 请修改出参对象
     */

    @PostMapping(path = "/command/order/create")
    public R<PreOrderVO> order(@RequestBody CommandOderCreateDTO commandOderCreateDTO , HttpServletRequest request) {
        //登录信息可以在这里模拟
        var userId = "155800005897";
        commandOderCreateDTO.setUserId(userId);
//        remark-现使用token用户无状态登录请求头校验
        String token = request.getHeader("accessToken");
        if (StringUtils.isBlank(token)) {
            log.error("未登陆，url:{}", request.getRequestURI());
            throw new CommonException("NO_LOGIN");
        }
        log.info("JSONbody:{}", commandOderCreateDTO);
        //TODO 验证 token 过期或则非法
        return R.ok(orderService.createOrder(commandOderCreateDTO));
    }

    /**
     * todo 这是模拟创建订单后，支付结果的回调通知
     * 【中、高级要求】需要使用策略模式处理至少三种场景：支付成功、支付失败、重复支付(自行设计回调报文进行重复判定)
     *
     * @param commandPayDTO 入参对象
     * @return 请修改出参对象
     */
    @PostMapping(path = "/command/order/pay/callback")
    public String payCallback(@RequestBody CommandPayDTO commandPayDTO) {
        return orderService.payCallback(commandPayDTO);
    }

}
