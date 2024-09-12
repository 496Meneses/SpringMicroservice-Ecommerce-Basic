package com.microservice.product.catalog.mapper;

import com.microservice.product.catalog.Constants;
import com.microservice.product.catalog.domain.dto.ProductDto;
import com.microservice.product.catalog.domain.entities.Product;

public class ProductMapper {
    private ProductMapper() { throw  new IllegalStateException(Constants.UTILITY_CLASS); }
    public static ProductDto mapToDto(Product entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
