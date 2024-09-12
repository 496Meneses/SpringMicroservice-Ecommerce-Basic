package com.microservice.order.management.service.impl;


import com.microservice.order.management.client.ProductClient;
import com.microservice.order.management.domain.OrderStatus;
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

import java.util.ArrayList;
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

        for (OrderItem item : orderItems) {
            ProductDto product = productClient.getProductById(item.getProductId());
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + product.getName());
            }
        }
        for (OrderItem item : orderItems) {
            productClient.reduceStock(item.getProductId(), item.getQuantity());
        }

        Order order = new Order();
        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.PENDING);
        return OrderMapper.mapToDto(orderRepository.save(order));
    }
    @Override
    public OrderDto updateOrder(OrderDto orderDTO) {
        Order order = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalArgumentException("No se puede editar una orden completada");
        }
        this.restoreStock(order);
        List<OrderItem> orderItems = this.addProductsToOrder(orderDTO, order);
        this.verifyStockAndReduceStock(order);
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        return OrderMapper.mapToDto(order);
    }
    private void verifyStockAndReduceStock(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            ProductDto product = productClient.getProductById(item.getProductId());
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + product.getName());
            }
            productClient.reduceStock(item.getProductId(), item.getQuantity());
        }
    }
    private void restoreStock(Order order) {
        List<OrderItem> oldOrderItems = new ArrayList<>(order.getOrderItems());
        for (OrderItem oldItem : oldOrderItems) {
            ProductDto product = productClient.getProductById(oldItem.getProductId());
            productClient.increaseStock(oldItem.getProductId(), oldItem.getQuantity());
        }
        orderItemRepository.deleteByOrderId(order.getId());
    }
    private List<OrderItem> addProductsToOrder(OrderDto orderDTO, Order order) {
        List<OrderItem> orderItems = orderDTO.getProducts().stream()
                .map(this::mapToOrderItem)
                .collect(Collectors.toList());

        for (OrderItem item : orderItems) {
            item.setOrder(order);
            order.getOrderItems().add(item);
        }
        return orderItems;
    }
    @Override
    public void completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalArgumentException("La orden ya está completada");
        }
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
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
