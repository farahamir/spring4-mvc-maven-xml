package com.amir.web.DAO;

import com.amir.web.model.Product;
import com.amir.web.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDAOImpl implements ProductDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public int addProduct(final Product product) {
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO PRODUCT(name,catId,price) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,  product.getName());
            ps.setInt(2,  product.getCatId());
            ps.setDouble(3,  product.getPrice());
            return ps;
        },holder);
        return holder.getKey().intValue();
    }
    @Override
    public List<Product> getProducts() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query("Select * from PRODUCT", resultSet -> {
            List<Product> products = new ArrayList<>();
            while (resultSet.next())
            {
                Product product = new Product(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getDouble(4)
                );
                products.add(product);
            }
            return products;
        });
    }

    @Override
    public int updateProduct(int id, Product product) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.update("UPDATE PRODUCT SET name = ?,catId = ?, price=? WHERE id=?",
                product.getName(),product.getCatId(),product.getPrice(),product.getId());
    }

    @Override
    public int deleteProduct(int id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.update("DELETE FROM PRODUCT WHERE id = ?", new Object[] {id});
    }

    @Override
    public List<Category> getCategoryList() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query("Select * from CATEGORY", resultSet -> {
            List<Category> categories = new ArrayList<>();
            while (resultSet.next())
            {
                Category category = new Category(
                        resultSet.getInt(1),
                        resultSet.getString(2)
                );
                categories.add(category);
            }
            return categories;
        });
    }

    @Override
    public Product getProductById(int id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query("Select * from PRODUCT WHERE id = ?",new Object[] {id},
                resultSet -> { Product product = new Product();
            if (resultSet.next())
            {
                product = new Product(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getDouble(4)
                );
            }
                    return product;
        });
    }

    @Override
    public boolean isProductExists(Product product) {
        return getProductById(product.getId()).getId() != 0;
    }
}
