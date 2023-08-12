package com.abhishek.ProductService.service;

import com.abhishek.ProductService.model.ProductRequest;
import com.abhishek.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);
}
