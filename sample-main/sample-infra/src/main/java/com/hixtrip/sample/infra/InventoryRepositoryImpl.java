package com.hixtrip.sample.infra;


import com.hixtrip.sample.common.redisson.annotations.Lock;
import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.hixtrip.sample.common.constans.PayConstants.RedisKeyFormatter.CHANGE_INVENTORY;

/**
 * infra层是domain定义的接口具体的实现
 */
@Slf4j
@Component
public class InventoryRepositoryImpl implements InventoryRepository, InitializingBean {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 库存key
     */
    private static final String INVENTORY = "inventory";

    /**
     * 可售库存key
     */
    private static final String SELL = "sellableQuantity";
    /**
     * 预占库存key
     */
    private static final String PRE_OCCUPY = "withholdingQuantity";

    private static String buildKey(String skuId, String type) {
        return String.join(":", type, INVENTORY, skuId);
    }


    /**
     * 根据skuId 初始化把库存数量到Redis中
     *
     * @param skuId        {@link String 商品id}
     * @param inventoryNum {@link String 商品库存量}
     */
    public void initInventory(String skuId, int inventoryNum, String type) {
        String key = buildKey(skuId, type);
        redisTemplate.opsForValue().set(key, inventoryNum);
    }

    @Override
    public Integer getSellableQuantity(String skuId) {
        Integer num = 0;
        String cacheKey = buildKey(skuId, SELL);
        Object o = redisTemplate.opsForValue().get(cacheKey);
        if (Objects.nonNull(o)) {
            String numStr = o.toString();
            log.info("numStr:{}", numStr);
            num = Integer.valueOf(numStr);
            return num;
        }
        log.info("cacheKey:{},val:{}", cacheKey, num);
        return num;
    }

    @Override
    public Integer getWithholdingQuantity(String skuId) {
        Integer num = 0;
        String cacheKey = buildKey(skuId, PRE_OCCUPY);
        Object o = redisTemplate.opsForValue().get(cacheKey);
        if (Objects.nonNull(o)) {
            String numStr = o.toString();
            log.info("numStr:{}", numStr);
            num = Integer.valueOf(numStr);
            return num;
        }
        log.info("cacheKey:{},val:{}", cacheKey, num);
        return num;
    }

    @Lock(name = CHANGE_INVENTORY, leaseTime = 3, autoUnlock = false)
    @Override
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        String sellKey = buildKey(skuId, SELL);
        redisTemplate.opsForValue().set(sellKey, String.valueOf(sellableQuantity));
        String preOccupyKey = buildKey(skuId, PRE_OCCUPY);
        redisTemplate.opsForValue().set(preOccupyKey, String.valueOf(withholdingQuantity));
        log.info("库存修改成功,当前skuId:{},可售库存:{},预占库存剩余:{}", skuId, sellableQuantity, withholdingQuantity);
        return true;
    }


    @Override
    public void afterPropertiesSet() {
        String skuId = "1";
        int inventoryNum = 100;
        initInventory(skuId, inventoryNum, SELL);
        initInventory(skuId, inventoryNum, PRE_OCCUPY);
        log.info("<<<商品skuId:{}库存,已初始化到缓存中:{}份>>>", skuId, inventoryNum);
    }
}
