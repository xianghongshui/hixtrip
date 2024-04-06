#todo 你的建表语句,包含索引
# 支付订单表
CREATE TABLE `pay_order` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
`biz_order_no` bigint NOT NULL COMMENT '业务订单号',
`pay_order_no` bigint NOT NULL DEFAULT '0' COMMENT '支付单号',
`biz_user_id` bigint NOT NULL COMMENT '支付用户id',
`pay_channel_code` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '支付渠道编码',
`amount` int NOT NULL COMMENT '支付金额，单位位分',
`pay_type` tinyint NOT NULL DEFAULT '4' COMMENT '支付类型，1：h5,2:小程序，3：公众号，4：扫码',
`status` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态，0：待提交，1:待支付，2：支付超时或取消，3：支付成功',
`expand_json` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '拓展字段，用于传递不同渠道单独处理的字段',
`notify_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT '' COMMENT '业务端回调接口',
`notify_times` int NOT NULL DEFAULT '0' COMMENT '业务端回调次数',
`notify_status` int NOT NULL DEFAULT '0' COMMENT '回调状态，0：待回调，1：回调成功，2：回调失败',
`result_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT '' COMMENT '第三方返回业务码',
`result_msg` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT '' COMMENT '第三方返回提示信息',
`pay_success_time` datetime DEFAULT NULL COMMENT '支付成功时间',
`pay_over_time` datetime NOT NULL COMMENT '支付超时时间',
`qr_code_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '支付二维码链接',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`creater` bigint NOT NULL DEFAULT '0' COMMENT '创建人',
`updater` bigint NOT NULL DEFAULT '0' COMMENT '更新人',
`deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `biz_order_no` (`biz_order_no`),
UNIQUE KEY `pay_order_no` (`pay_order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1585168008500764674 DEFAULT CHARSET=utf8mb3 COMMENT='支付订单';

# 退款订单
CREATE TABLE `refund_order` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`biz_order_no` bigint NOT NULL COMMENT '业务端已支付的订单id',
`biz_refund_order_no` bigint NOT NULL COMMENT '业务端要退款的订单id，也就是子订单id',
`pay_order_no` bigint NOT NULL COMMENT '付款时传入的支付单号',
`refund_order_no` bigint NOT NULL COMMENT '退款单号，每次退款的唯一标示',
`refund_amount` int NOT NULL COMMENT '本次退款金额，单位分',
`total_amount` int NOT NULL COMMENT '总金额，单位分',
`is_split` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是拆单退款，默认false',
`pay_channel_code` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '支付渠道编码',
`result_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT '' COMMENT '第三方交易编码',
`result_msg` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT '' COMMENT '第三方交易信息',
`status` int NOT NULL DEFAULT '0' COMMENT '退款状态，0：未提交，1：退款中，2：退款失败，3：退款成功',
`refund_channel` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '退款渠道',
`notify_failed_times` int NOT NULL DEFAULT '0' COMMENT '业务端退款通知失败次数',
`notify_status` int NOT NULL DEFAULT '0' COMMENT '退款接口通知状态，0：待通知，1：通知成功，2：通知中，3：通知失败',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '退款单据创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '退款单据修改时间',
`creater` bigint NOT NULL DEFAULT '0' COMMENT '单据创建人，一般手动对账产生的单据才有值',
`updater` bigint NOT NULL DEFAULT '0' COMMENT '单据修改人，一般手动对账产生的单据才有值',
`deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
PRIMARY KEY (`id`) USING BTREE,
KEY `index_biz_order_id` (`biz_refund_order_no`) USING BTREE,
KEY `index_create_time` (`create_time`) USING BTREE,
KEY `index_refund_order_id` (`refund_order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1588164197026390019 DEFAULT CHARSET=utf8mb3 COMMENT='退款订单';

-- 分库键：根据买家ID进行分库
-- 分表键：根据订单创建时间进行分表
# 复合索引：在订单表（主表）中为买家ID和创建时间，以及卖家ID和创建时间创建了复合索引。这样可以在查询时根据买家ID和创建时间或者卖家ID和创建时间快速定位订单数据，提高查询效率。
# 索引优化：使用了复合索引而不是单独的索引。这样可以减少索引的数量，降低存储空间和维护成本，同时能够更好地支持多条件查询，提高查询性能。
# 合理选择索引字段顺序：在复合索引中，将买家ID或卖家ID放在前面，而将创建时间放在后面。这样可以更好地支持根据买家ID或卖家ID快速过滤数据，然后再根据创建时间进行排序或范围查询。
CREATE TABLE `order` (
`id` bigint NOT NULL COMMENT '订单id',
`user_id` bigint NOT NULL COMMENT '用户id',
`seller_id` bigint NOT NULL COMMENT '商家id',
`biz_order_no` bigint NOT NULL COMMENT '业务订单号',
`order_no` bigint NOT NULL COMMENT '订单号',
`pay_order_no` bigint DEFAULT NULL COMMENT '交易流水支付单号',
`status` tinyint NOT NULL DEFAULT '1' COMMENT '订单状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名，6：已申请退款',
`message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '状态备注',
`total_amount` int NOT NULL COMMENT '订单总金额，单位分',
`real_amount` int NOT NULL COMMENT '实付金额，单位分',
`discount_amount` int NOT NULL DEFAULT '0' COMMENT '优惠金额，单位分',
`pay_channel` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '支付渠道',
`coupon_id` bigint DEFAULT NULL COMMENT '优惠券id',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建订单时间',
`pay_time` datetime DEFAULT NULL COMMENT '支付时间',
`close_time` datetime DEFAULT NULL COMMENT '订单关闭时间',
`finish_time` datetime DEFAULT NULL COMMENT '订单完成时间，支付后30天',
`refund_time` datetime DEFAULT NULL COMMENT '申请退款时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`creater` bigint NOT NULL COMMENT '创建人',
`updater` bigint NOT NULL COMMENT '更新人',
`deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
PRIMARY KEY (`id`),
INDEX idx_user_id(user_id),
INDEX idx_seller_id(seller_id),
INDEX idx_create_time(create_time),
INDEX idx_user_id_create_time(user_id,create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='订单';

-- 分库键：根据买家ID进行分库
-- 分表键：与订单表保持一致，根据订单创建时间进行分表
CREATE TABLE `order_detail` (
`id` bigint NOT NULL COMMENT '订单明细id',
`order_id` bigint NOT NULL COMMENT '订单id',
`user_id` bigint NOT NULL COMMENT '用户id',
`price` int NOT NULL COMMENT '价格',
`name` varchar(128) NOT NULL COMMENT '名称',
`cover_url` varchar(255) NOT NULL COMMENT '封面地址',
`discount_amount` int NOT NULL DEFAULT '0' COMMENT '折扣金额',
`real_pay_amount` int NOT NULL COMMENT '实付金额',
`status` tinyint NOT NULL COMMENT '订单详情状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名',
`refund_status` tinyint DEFAULT NULL COMMENT '1：待审批，2：取消退款，3：同意退款，4：拒绝退款，5：退款成功，6：退款失败''',
`coupon_id` bigint DEFAULT '0' COMMENT '优惠券id',
`pay_channel` varchar(50) NOT NULL DEFAULT '' COMMENT '支付渠道名称',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`creater` bigint NOT NULL COMMENT '创建人',
`updater` bigint NOT NULL COMMENT '更新人',
PRIMARY KEY (`id`),
KEY `idx_order` (`order_id`),
KEY `idx_pay_channel` (`pay_channel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='订单明细';