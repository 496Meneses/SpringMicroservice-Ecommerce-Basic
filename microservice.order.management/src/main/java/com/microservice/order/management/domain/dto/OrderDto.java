package com.microservice.order.management.domain.dto;

import com.microservice.order.management.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private Double totalAmount;
    private List<OrderItemDTO> products;
    private OrderStatus status;
}
