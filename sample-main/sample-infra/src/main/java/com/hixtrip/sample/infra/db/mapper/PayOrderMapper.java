package com.hixtrip.sample.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hixtrip.sample.infra.db.dataobject.PayOrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xianghs
 * @Date 2024/4/4
 * @remark:
 */
@Mapper
public interface PayOrderMapper extends BaseMapper<PayOrderDO> {

}
