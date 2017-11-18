package com.amir.web.service;

import com.amir.web.DAO.ProductDAO;
import com.amir.web.model.Category;
import com.amir.web.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO dao;
    public int addProduct(Product product) {
        return dao.addProduct(product);
    }

    @Override
    public List<Product> getProducts() {
        return dao.getProducts();
    }

    @Override
    public void updateProduct(int id, Product product) {
        int productId = dao.updateProduct(id,product);
        product.setId(productId);
    }

    @Override
    public void deleteProduct(int id) {
        dao.deleteProduct(id);
    }

    @Override
    public Product findById(int id) {
        return dao.getProductById(id);
    }

    @Override
    public List<Category> getCategoryList() {
        return dao.getCategoryList();
    }

    @Override
    public boolean exists(Product product) {
        return dao.isProductExists(product);
    }
}
