package com.hixtrip.sample.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hixtrip.sample.infra.db.dataobject.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xianghs
 * @Date 2024/4/6
 * @remark:
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
