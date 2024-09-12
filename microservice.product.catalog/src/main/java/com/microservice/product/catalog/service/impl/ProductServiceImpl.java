package com.microservice.product.catalog.service.impl;

import com.microservice.product.catalog.domain.dto.ProductDto;
import com.microservice.product.catalog.domain.dto.ResponseProductDto;
import com.microservice.product.catalog.domain.entities.Product;
import com.microservice.product.catalog.mapper.ProductMapper;
import com.microservice.product.catalog.repository.ProductRepository;
import com.microservice.product.catalog.service.IProductService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public ResponseProductDto findAll() {
        return ResponseProductDto.builder().products(productRepository.findAll().stream().map(product -> ProductMapper.mapToDto(product)).toList()).build();
    }

    @Override
    public void save(ProductDto product) {
        this.productRepository.save(ProductMapper.mapToEntity(product));
    }

    @Override
    public ProductDto findById(Long id) {
        return ProductMapper.mapToDto(this.productRepository.findById(id).orElseThrow());
    }

    @Override
    public String reduceStock(Long id, Integer quantity) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return "Producto no encontrado";
        }
        if (product.get().getStock() < quantity) {
            return "Stock insuficiente";
        }
        Product entity = product.get();
        entity.setStock(entity.getStock() - quantity);
        productRepository.save(entity);
        return "Stock reducido";
    }
}
