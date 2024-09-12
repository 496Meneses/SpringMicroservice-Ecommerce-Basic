package com.microservice.order.management.service;

import com.microservice.order.management.domain.dto.OrderDto;
import com.microservice.order.management.domain.dto.ProductDto;
import com.microservice.order.management.domain.dto.ResponseOrderDto;
import com.microservice.order.management.domain.entities.Order;

import java.util.List;

public interface IOrderService {
    List<ProductDto> getProductsByOrderId(Long orderId);
    ResponseOrderDto getAllOrders();
    OrderDto createOrder(OrderDto orderDTO);
}
