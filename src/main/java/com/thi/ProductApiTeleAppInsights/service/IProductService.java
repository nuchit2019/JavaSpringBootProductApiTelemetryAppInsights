package com.thi.ProductApiTeleAppInsights.service;

import com.thi.ProductApiTeleAppInsights.model.Product;

import java.util.List;

public interface IProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    void deleteById(Long id);
}
