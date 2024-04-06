package com.hixtrip.sample.app.convertor;

import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * DTO对像 -> 领域对象转换器
 * 转换器
 */
@Mapper
public interface CommandPayConvertor {

    CommandPayConvertor INSTANCE = Mappers.getMapper(CommandPayConvertor.class);


    CommandPay commandPayDTO2CommandPay(CommandPayDTO commandPayDTO);

}
