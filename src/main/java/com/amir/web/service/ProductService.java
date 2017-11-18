package com.amir.web.service;

import com.amir.web.model.Category;
import com.amir.web.model.Product;

import java.util.List;

public interface ProductService {
    int addProduct(Product product);

    List<Product> getProducts();

    void updateProduct(int id, Product product);

    void deleteProduct(int id);

    Product findById(int id);

    List<Category> getCategoryList();

    boolean exists(Product product);
}
