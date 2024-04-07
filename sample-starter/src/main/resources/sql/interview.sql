/*
 Navicat Premium Data Transfer

 Source Server         : xianghs-localhost-mysql
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : interview

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 06/04/2024 17:15:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
-- 分库键：根据买家ID进行分库
-- 分表键：与订单表保持一致，根据订单创建时间进行分表
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint NOT NULL COMMENT '订单明细id',
  `order_id` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '订单id',
  `user_id` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户id',
  `sku_id` varchar(80)  CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '商品id',
  `price` int NOT NULL COMMENT '价格',
  `name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '名称',
  `cover_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '封面地址',
  `discount_amount` int NOT NULL DEFAULT 0 COMMENT '折扣金额',
  `real_pay_amount` int NOT NULL COMMENT '实付金额',
  `status` tinyint NOT NULL COMMENT '订单详情状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名',
  `refund_status` tinyint NULL DEFAULT NULL COMMENT '1：待审批，2：取消退款，3：同意退款，4：拒绝退款，5：退款成功，6：退款失败\'',
  `coupon_id` bigint NULL DEFAULT 0 COMMENT '优惠券id',
  `pay_channel` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '支付渠道名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '创建人',
  `updater` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order`(`order_id` ASC) USING BTREE,
  INDEX `idx_pay_channel`(`pay_channel` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '订单明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order`  (
  `id` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'id',
  `biz_order_no` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '业务订单号',
  `pay_order_no` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '支付单号',
  `biz_user_id` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '支付用户id',
  `pay_channel_code` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '支付渠道编码',
  `amount` int NOT NULL COMMENT '支付金额，单位位分',
  `pay_type` tinyint NOT NULL DEFAULT 4 COMMENT '支付类型，1：h5,2:小程序，3：公众号，4：扫码',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态，0：待提交，1:待支付，2：支付超时或取消，3：支付成功',
  `expand_json` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '拓展字段，用于传递不同渠道单独处理的字段',
  `notify_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '业务端回调接口',
  `notify_times` int NOT NULL DEFAULT 0 COMMENT '业务端回调次数',
  `notify_status` int NOT NULL DEFAULT 0 COMMENT '回调状态，0：待回调，1：回调成功，2：回调失败',
  `result_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '第三方返回业务码',
  `result_msg` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '第三方返回提示信息',
  `pay_success_time` datetime NULL DEFAULT NULL COMMENT '支付成功时间',
  `pay_over_time` datetime NOT NULL COMMENT '支付超时时间',
  `qr_code_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '支付二维码链接',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updater` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `biz_order_no`(`biz_order_no` ASC) USING BTREE,
  UNIQUE INDEX `pay_order_no`(`pay_order_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1585168008500764676 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '支付订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_order
-- ----------------------------
INSERT INTO `pay_order` VALUES ('1776535611029602306', '1776535611029602306', '1776535611029602306', '155800005897', 'wxPay', 20000, 4, 4, '{\"amount\":1,\"createBy\":\"155800005897\",\"createTime\":1712394070845,\"delFlag\":0,\"id\":\"1776535611029602306\",\"money\":200,\"payStatus\":\"1\",\"payTime\":1712394070845,\"skuId\":\"1\",\"updateBy\":\"155800005897\",\"updateTime\":1712394070845,\"userId\":\"155800005897\"}', '', 0, 1, '', '', '2024-04-06 17:01:47', '2024-04-06 17:31:11', '2hichat.fun', '2024-04-06 17:01:11', '2024-04-06 17:01:11', '155800005897', '155800005897', b'0');
INSERT INTO `pay_order` VALUES ('1776536292872536065', '1776536292872536065', '1776536292872536065', '155800005897', 'wxPay', 20000, 4, 1, '{\"amount\":1,\"createBy\":\"155800005897\",\"createTime\":1712394233410,\"delFlag\":0,\"id\":\"1776536292872536065\",\"money\":200,\"payStatus\":\"1\",\"payTime\":1712394233410,\"skuId\":\"1\",\"updateBy\":\"155800005897\",\"updateTime\":1712394233410,\"userId\":\"155800005897\"}', '', 0, 0, '', '', NULL, '2024-04-06 17:33:53', '2hichat.fun', '2024-04-06 17:03:53', '2024-04-06 17:03:53', '155800005897', '155800005897', b'0');
INSERT INTO `pay_order` VALUES ('1776536713582149634', '1776536713582149634', '1776536713582149634', '155800005897', 'wxPay', 20000, 4, 1, '{\"amount\":1,\"createBy\":\"155800005897\",\"createTime\":1712394333719,\"delFlag\":0,\"id\":\"1776536713582149634\",\"money\":200,\"payStatus\":\"1\",\"payTime\":1712394333719,\"skuId\":\"1\",\"updateBy\":\"155800005897\",\"updateTime\":1712394333719,\"userId\":\"155800005897\"}', '', 0, 0, '', '', NULL, '2024-04-06 17:35:34', '2hichat.fun', '2024-04-06 17:05:34', '2024-04-06 17:05:34', '155800005897', '155800005897', b'0');
INSERT INTO `pay_order` VALUES ('1776537590904713218', '1776537590904713218', '1776537590904713218', '155800005897', 'wxPay', 20000, 4, 4, '{\"amount\":1,\"createBy\":\"155800005897\",\"createTime\":1712394542878,\"delFlag\":0,\"id\":\"1776537590904713218\",\"money\":200,\"payStatus\":\"1\",\"payTime\":1712394542878,\"skuId\":\"1\",\"updateBy\":\"155800005897\",\"updateTime\":1712394542878,\"userId\":\"155800005897\"}', '', 0, 1, '', '', '2024-04-06 17:09:13', '2024-04-06 17:39:03', '2hichat.fun', '2024-04-06 17:09:03', '2024-04-06 17:09:03', '155800005897', '155800005897', b'0');
INSERT INTO `pay_order` VALUES ('1776537824405753858', '1776537824405753858', '1776537824405753858', '155800005897', 'wxPay', 20000, 4, 4, '{\"amount\":1,\"createBy\":\"155800005897\",\"createTime\":1712394598555,\"delFlag\":0,\"id\":\"1776537824405753858\",\"money\":200,\"payStatus\":\"1\",\"payTime\":1712394598555,\"skuId\":\"1\",\"updateBy\":\"155800005897\",\"updateTime\":1712394598555,\"userId\":\"155800005897\"}', '', 0, 1, '', '', '2024-04-06 17:10:16', '2024-04-06 17:39:59', '2hichat.fun', '2024-04-06 17:09:59', '2024-04-06 17:09:59', '155800005897', '155800005897', b'0');
INSERT INTO `pay_order` VALUES ('1776538239293784066', '1776538239293784066', '1776538239293784066', '155800005897', 'wxPay', 20000, 4, 3, '{\"amount\":1,\"createBy\":\"155800005897\",\"createTime\":1712394697471,\"delFlag\":0,\"id\":\"1776538239293784066\",\"money\":200,\"payStatus\":\"1\",\"payTime\":1712394697471,\"skuId\":\"1\",\"updateBy\":\"155800005897\",\"updateTime\":1712394697471,\"userId\":\"155800005897\"}', '', 0, 1, '', '', '2024-04-06 17:11:44', '2024-04-06 17:41:37', '2hichat.fun', '2024-04-06 17:11:37', '2024-04-06 17:11:37', '155800005897', '155800005897', b'0');
INSERT INTO `pay_order` VALUES ('1776538872709230594', '1776538872709230594', '1776538872709230594', '155800005897', 'wxPay', 20000, 4, 3, '{\"amount\":1,\"createBy\":\"155800005897\",\"createTime\":1712394848486,\"delFlag\":0,\"id\":\"1776538872709230594\",\"money\":200,\"payStatus\":\"1\",\"payTime\":1712394848486,\"skuId\":\"1\",\"updateBy\":\"155800005897\",\"updateTime\":1712394848486,\"userId\":\"155800005897\"}', '', 0, 1, '', '', '2024-04-06 17:14:15', '2024-04-06 17:44:08', '2hichat.fun', '2024-04-06 17:14:08', '2024-04-06 17:14:08', '155800005897', '155800005897', b'0');

-- ----------------------------
-- Table structure for refund_order
-- ----------------------------
DROP TABLE IF EXISTS `refund_order`;
CREATE TABLE `refund_order`  (
  `id` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键',
  `biz_order_no` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '业务端已支付的订单id',
  `biz_refund_order_no` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '业务端要退款的订单id，也就是子订单id',
  `pay_order_no` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '付款时传入的支付单号',
  `refund_order_no` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '退款单号，每次退款的唯一标示',
  `refund_amount` int NOT NULL COMMENT '本次退款金额，单位分',
  `total_amount` int NOT NULL COMMENT '总金额，单位分',
  `is_split` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是拆单退款，默认false',
  `pay_channel_code` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '支付渠道编码',
  `result_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '第三方交易编码',
  `result_msg` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '第三方交易信息',
  `status` int NOT NULL DEFAULT 0 COMMENT '退款状态，0：未提交，1：退款中，2：退款失败，3：退款成功',
  `refund_channel` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '退款渠道',
  `notify_failed_times` int NOT NULL DEFAULT 0 COMMENT '业务端退款通知失败次数',
  `notify_status` int NOT NULL DEFAULT 0 COMMENT '退款接口通知状态，0：待通知，1：通知成功，2：通知中，3：通知失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '退款单据创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '退款单据修改时间',
  `creater` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '单据创建人，一般手动对账产生的单据才有值',
  `updater` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '单据修改人，一般手动对账产生的单据才有值',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_biz_order_id`(`biz_refund_order_no` ASC) USING BTREE,
  INDEX `index_create_time`(`create_time` ASC) USING BTREE,
  INDEX `index_refund_order_id`(`refund_order_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1588164197026390019 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '退款订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_order
-- ----------------------------

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
-- 分库键：根据买家ID进行分库
-- 分表键：根据订单创建时间进行分表
# 复合索引：在订单表（主表）中为买家ID和创建时间，以及卖家ID和创建时间创建了复合索引。这样可以在查询时根据买家ID和创建时间或者卖家ID和创建时间快速定位订单数据，提高查询效率。
# 索引优化：使用了复合索引而不是单独的索引。这样可以减少索引的数量，降低存储空间和维护成本，同时能够更好地支持多条件查询，提高查询性能。
# 合理选择索引字段顺序：在复合索引中，将买家ID或卖家ID放在前面，而将创建时间放在后面。这样可以更好地支持根据买家ID或卖家ID快速过滤数据，然后再根据创建时间进行排序或范围查询。
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `id` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '订单id',
  `user_id` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '用户id',
  `seller_id` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '商家id',
  `biz_order_no` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '业务订单号',
  `order_no` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '订单号',
  `pay_order_no` bigint NULL DEFAULT NULL COMMENT '交易流水支付单号',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '订单状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名，6：已申请退款',
  `message` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '状态备注',
  `total_amount` int NOT NULL COMMENT '订单总金额，单位分',
  `real_amount` int NOT NULL COMMENT '实付金额，单位分',
  `discount_amount` int NOT NULL DEFAULT 0 COMMENT '优惠金额，单位分',
  `pay_channel` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '支付渠道',
  `coupon_id` bigint NULL DEFAULT NULL COMMENT '优惠券id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建订单时间',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `close_time` datetime NULL DEFAULT NULL COMMENT '订单关闭时间',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '订单完成时间，支付后30天',
  `refund_time` datetime NULL DEFAULT NULL COMMENT '申请退款时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updater` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_seller_id`(`seller_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_user_id_create_time`(`user_id` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '订单' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
