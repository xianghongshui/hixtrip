# hixtrip

## 需求

实现一个标准电商平台的一个下单功能

1. 完整实现一个下单功能，包含以下功能：查询SKU价格(模拟，不需要实现)，扣减库存(只在缓存实现)，支付(模拟,不需要实现,但要处理结果回调), 生成订单
2. 围绕下单功能，设计订单相关业务库表结构，并生成mybatis代码, 库存、商品等无需建表。

## 技术要求
基础设施层限制使用mybatis\spring data redis实现（不考虑事务及分布式事务）。 

**请勾选**
- [ ] 初级
- [√ ] 中级
- [ ] 高级

### 初级
1. 【10分】基于基础代码实现，要求理解DDD思想, 按示例要求(注意看代码注释和TODO)分层实现下单业务。 \
          领域服务已定义, 不允许新增领域层接口, 请注意看代码注释, 并在APP层进行调用。 
2. 【20分】支付回调只处理支付成功场景。
3. 【30分】resources/sql中给出订单表建表语句。 (库存、商品等无需建表)。
4. 【40分】库存扣减只在缓存实现。

### 中级
1. 【10分】基于基础代码实现，要求理解DDD思想, 按示例要求(注意看代码注释和TODO)分层实现下单业务。 \
          领域服务已定义, 不允许新增领域层接口, 请注意看代码注释, 并在APP层进行调用。 
2. 【20分】支付回调使用策略模式(支付成功、支付失败、重复支付), 实现越优雅越好。
3. 【30分】resources/sql中给出订单相关表mysql建表语句。 (库存、商品等无需建表) \
          背景: 近期订单量级2000W, 不考虑增长。 \
          主查询场景如下:  \
          1. 买家频繁查询我的订单,实时性要求高。 \
          2. 卖家频繁查询我的订单,允许秒级延迟。\
          使用mysql分库分表技术, 建表语句文件内使用注释方式描述设计方案。 \
          如：表、索引、分库键、分表键的设计思路及具体结论、同时满足买卖双方查询需求方案等。
4. 【40分】库存扣减只在缓存实现, 需要考虑并发，避免超卖。

### 高级
1. 【10分】基于基础代码实现，要求理解DDD思想, 按示例要求(注意看代码注释和TODO)分层实现下单业务。 \
          领域服务已定义, 不允许新增领域层接口, 请注意看代码注释, 并在APP层进行调用。 \
          领域层需要体现充血模型的实现, 在任意业务上体现即可。
2. 【20分】支付回调需要优雅的使用策略模式(支付成功、支付失败、重复支付)。
3. 【30分】围绕订单，给出相关存储设计。 (库存、商品等无需考虑) \
          背景: 存量订单10亿, 日订单增长百万量级。 \
          主查询场景如下:  \
          1. 买家频繁查询我的订单, 高峰期并发100左右。实时性要求高。 \
          2. 卖家频繁查询我的订单, 高峰期并发30左右。允许秒级延迟。 \
          3. 平台客服频繁搜索客诉订单(半年之内订单, 订单尾号，买家姓名搜索)，高峰期并发10左右。允许分钟级延迟。 \
          4. 平台运营进行订单数据分析，如买家订单排行榜, 卖家订单排行榜。 \
          resources/sql中给出整体设计方案。包含存储基础设施选型, DDL（基础字段即可）, 满足上述场景设计思路。 \
          方案可描述大体方向，面试时可补充交流。
4. 【40分】库存扣减只在缓存实现, 假设业务为秒杀场景，需要考虑高并发(100每秒)，避免超卖。要求无锁设计。
#   h i x t r i p 
 
 