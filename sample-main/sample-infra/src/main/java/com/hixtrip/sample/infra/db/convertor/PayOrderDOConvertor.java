package com.hixtrip.sample.infra.db.convertor;

import com.hixtrip.sample.domain.order.model.PayOrder;
import com.hixtrip.sample.infra.db.dataobject.PayOrderDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xianghs
 * @Date 2024/4/6
 * @remark:
 */
@Mapper
public interface PayOrderDOConvertor {

    PayOrderDOConvertor INSTANCE = Mappers.getMapper(PayOrderDOConvertor.class);

    PayOrder do2Domain(PayOrderDO payOrderDO);
    PayOrderDO domain2DO(PayOrder payOrder);
}
