package com.hixtrip.sample.app.convertor;

import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.OrderInfoDTO;
import com.hixtrip.sample.domain.order.model.PayOrder;
import com.hixtrip.sample.infra.db.dataobject.PayOrderDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * DTO对像 -> 领域对象转换器
 * 转换器
 */
@Mapper
public interface OrderConvertor {

    OrderConvertor INSTANCE = Mappers.getMapper(OrderConvertor.class);

    OrderInfoDTO oderCreateDTO2OrderInfo(CommandOderCreateDTO dto);


}
