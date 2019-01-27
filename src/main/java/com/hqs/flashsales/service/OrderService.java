package com.hqs.flashsales.service;

/**
 * @author huangqingshi
 * @Date 2019-01-23
 */
public interface OrderService {

    void initCatalog();

    Long placeOrder(Long catalogId);

    Long placeOrderWithQueue(Long catalogId);

}
