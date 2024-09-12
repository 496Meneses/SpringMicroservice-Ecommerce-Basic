package com.microservice.order.management.controller;

import com.microservice.order.management.domain.dto.OrderDto;
import com.microservice.order.management.domain.dto.ProductDto;
import com.microservice.order.management.domain.dto.ResponseOrderDto;
import com.microservice.order.management.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/all")
    private ResponseEntity<ResponseOrderDto> getAllOrder() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDTO) {
        OrderDto createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }
    @GetMapping("/getProductsByOrderId/{id}")
    private ResponseEntity<List<ProductDto>> getProductsByOrder(@PathVariable Long id) {
        return ResponseEntity.ok(this.orderService.getProductsByOrderId(id));
    }
    @PutMapping("/update")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto) {
        try {
            OrderDto updatedOrder = orderService.updateOrder(orderDto);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("/{orderId}/complete")
    public ResponseEntity<Void> completeOrder(@PathVariable Long orderId) {
        try {
            orderService.completeOrder(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
