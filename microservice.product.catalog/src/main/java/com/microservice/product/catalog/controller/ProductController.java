package com.microservice.product.catalog.controller;

import com.microservice.product.catalog.domain.dto.ResponseProductDto;
import com.microservice.product.catalog.domain.entities.Product;
import com.microservice.product.catalog.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("/all")
    private ResponseEntity<ResponseProductDto> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }
}
