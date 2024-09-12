package com.microservice.order.management.service.impl;


import com.microservice.order.management.client.ProductClient;
import com.microservice.order.management.domain.dto.OrderDto;
import com.microservice.order.management.domain.dto.OrderItemDTO;
import com.microservice.order.management.domain.dto.ProductDto;

import com.microservice.order.management.domain.dto.ResponseOrderDto;
import com.microservice.order.management.domain.entities.Order;
import com.microservice.order.management.domain.entities.OrderItem;
import com.microservice.order.management.mapper.OrderMapper;
import com.microservice.order.management.repository.OrderItemRepository;
import com.microservice.order.management.repository.OrderRepository;
import com.microservice.order.management.service.IOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductClient productClient;
    @Override
    public List<ProductDto> getProductsByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        List<Long> productIds = orderItems.stream()
                .map(OrderItem::getProductId)
                .collect(Collectors.toList());
        return productIds.stream()
                .map(productClient::getProductById)
                .collect(Collectors.toList());
    }


    public ResponseOrderDto getAllOrders() {
        return ResponseOrderDto.builder().orders(this.orderRepository.findAll().stream().map(order -> OrderMapper.mapToDto(order)).collect(Collectors.toList())).build();
    }
    @Override
    public OrderDto createOrder(OrderDto orderDTO) {
        List<OrderItem> orderItems = orderDTO.getProducts().stream()
                .map(this::mapToOrderItem)
                .collect(Collectors.toList());
        Order order = new Order();
        for (OrderItem item : orderItems) {
            order.setId(orderDTO.getId());
            item.setOrder(order);
        }
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapToDto(savedOrder);
    }
    private OrderItem mapToOrderItem(OrderItemDTO itemDTO) {
        ProductDto product = productClient.getProductById(itemDTO.getProductId());
        if (product.getStock() < itemDTO.getQuantity()) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + product.getName());
        }
        productClient.reduceStock(itemDTO.getProductId(), itemDTO.getQuantity());
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(itemDTO.getProductId());
        orderItem.setQuantity(itemDTO.getQuantity());
        orderItem.setPrice(product.getPrice().doubleValue());
        return orderItem;
    }
}
