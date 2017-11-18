package com.amir.web.DAO;

import com.amir.web.model.Product;
import com.amir.web.model.Category;

import java.util.List;

public interface ProductDAO {
    int addProduct(Product product);
    List<Product> getProducts();

    int updateProduct(int id, Product product);

    int deleteProduct(int id);

    List<Category> getCategoryList();

    Product getProductById(int id);

    boolean isProductExists(Product product);
}
