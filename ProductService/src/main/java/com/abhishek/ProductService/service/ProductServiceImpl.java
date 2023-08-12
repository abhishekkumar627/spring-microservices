package com.abhishek.ProductService.service;

import com.abhishek.ProductService.entity.ProductEntity;
import com.abhishek.ProductService.exception.ProductNotFoundException;
import com.abhishek.ProductService.model.ProductRequest;
import com.abhishek.ProductService.model.ProductResponse;
import com.abhishek.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding productRequest in productRequest service impl : {} ", productRequest);
        ProductEntity entity = ProductEntity.builder().productName(productRequest.getName())
                .price(productRequest.getPrice()).quantity(productRequest.getQuantity()).build();
        productRepository.save(entity);
        log.info("Product added successfully with entity : {}", entity);
        return entity.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Getting product info from database using product id : {} ", productId);
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with product Id " + productId,
                        "PRODUCT_NOT_FOUND"));
        // .orElseThrow(()->new RuntimeException("Product not found with product Id : "+productId));
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(productEntity, productResponse);
        log.info("Product fetched successfully with reponse : {}", productResponse);
        return productResponse;
    }

    @Override
    public void reduceProductQuantity(long productId, long quantity) {
        log.info("Started reduce product Quantity with product id : {} and quantity : {}", productId, quantity);
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with product id" + productId,
                        "PRODUCT_NOT_FOUND"));

        if (productEntity.getQuantity() < quantity) {
            throw new ProductNotFoundException("Required Product quantity not present",
                    "INSUFFICIENT_PRODUCT_QUANTITY");
        }
        productEntity.setQuantity(productEntity.getQuantity() - quantity);
        productRepository.save(productEntity);
        log.info("Product quantity successfully reduced with quantity : {} of product id : {}", quantity, productId);
    }
}
