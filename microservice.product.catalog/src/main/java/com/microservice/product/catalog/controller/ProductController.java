package com.microservice.product.catalog.controller;

import com.microservice.product.catalog.domain.dto.ProductDto;
import com.microservice.product.catalog.domain.dto.ResponseProductDto;
import com.microservice.product.catalog.domain.entities.Product;
import com.microservice.product.catalog.service.IProductService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    private void saveProduct(@RequestBody ProductDto productDto) {
        this.productService.save(productDto);
    }
    @GetMapping("/search-by-id/{id}")
    private ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(this.productService.findById(id));
    }
    @PutMapping("/reduce-stock/{id}")
    public ResponseEntity<String> reduceStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(productService.reduceStock(id, quantity));
    }
}
