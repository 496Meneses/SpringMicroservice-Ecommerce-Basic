package com.microservice.product.catalog.service.impl;

import com.microservice.product.catalog.domain.dto.ResponseProductDto;
import com.microservice.product.catalog.domain.entities.Product;
import com.microservice.product.catalog.mapper.ProductMapper;
import com.microservice.product.catalog.repository.ProductRepository;
import com.microservice.product.catalog.service.IProductService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public ResponseProductDto findAll() {
        return ResponseProductDto.builder().products(productRepository.findAll().stream().map(product -> ProductMapper.mapToDto(product)).toList()).build();
    }
}
