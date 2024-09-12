package com.microservice.order.management.client;

import com.microservice.order.management.domain.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "microservice-product-catalog", url = "localhost:8090/api/product")
public interface ProductClient {
    @GetMapping("search-by-id/{id}")
    ProductDto findProductById(@PathVariable Long id);
    @GetMapping("/search-by-id/{id}")
    ProductDto getProductById(@PathVariable Long id);

    @PutMapping("/reduce-stock/{id}")
    void reduceStock(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);
    @PutMapping("/increase-stock/{id}/{quantity}")
    void increaseStock(@PathVariable("id") Long productId, @PathVariable("quantity") Integer quantity);
}