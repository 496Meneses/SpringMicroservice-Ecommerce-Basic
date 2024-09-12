package com.microservice.order.management.mapper;

import com.microservice.order.management.Constants;
import com.microservice.order.management.domain.dto.OrderDto;
import com.microservice.order.management.domain.dto.OrderItemDTO;
import com.microservice.order.management.domain.entities.Order;
import com.microservice.order.management.domain.entities.OrderItem;

import java.util.stream.Collectors;

public class OrderMapper {
    private OrderMapper() { throw  new IllegalStateException(Constants.UTILITY_CLASS); }
    public static OrderDto mapToDto(Order entity) {
        return OrderDto.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .products(entity.getOrderItems().stream().map(orderItem -> mapOrderItemDtoToEntity(orderItem)).collect(Collectors.toList()))
                .build();
    }
    public static Order mapToEntity(OrderDto dto) {
        return Order.builder()
                .id(dto.getId())
                .status(dto.getStatus())
                .build();
    }
    public static OrderItemDTO mapOrderItemDtoToEntity(OrderItem entity) {
        return OrderItemDTO.builder()
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())

                .build();
    }
}
