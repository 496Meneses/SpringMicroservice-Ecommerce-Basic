package com.microservice.product.catalog.service;

import com.microservice.product.catalog.domain.dto.ResponseProductDto;
import com.microservice.product.catalog.domain.entities.Product;

import java.util.List;

public interface IProductService {
    ResponseProductDto findAll();
}
