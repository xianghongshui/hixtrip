package com.hixtrip.sample.domain.inventory.repository;

/**
 *
 */
public interface InventoryRepository {


    /**
     * 获取sku当前库存 --可售库存
     *
     * @param skuId {@link String 商品id}
     */
    Integer getSellableQuantity(String skuId);

    /**
     * 获取占用库存
     *
     * @param skuId {@link String 商品id}
     */
    Integer getWithholdingQuantity(String skuId);

    /**
     * 修改库存
     *
     * @param skuId {@link String 商品id}
     * @param sellableQuantity {@link Long 可售库存}
     * @param withholdingQuantity {@link Long 预占库存}
     * @param occupiedQuantity  {@link Long 预占库存}
     * @return
     */
    Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity);

}
