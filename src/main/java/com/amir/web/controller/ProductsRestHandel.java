package com.amir.web.controller;

import com.amir.web.model.Category;
import com.amir.web.model.Product;
import com.amir.web.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class ProductsRestHandel {
	private final Logger log = LoggerFactory.getLogger(ProductsRestHandel.class);

	private final ProductService productService;
	@Autowired
	public ProductsRestHandel(ProductService productService) {
		this.productService = productService;
	}



	@RequestMapping(value = "/api/products", method = RequestMethod.GET, headers="Accept=application/json")
	public List<Product> getProducts(HttpServletResponse httpResponse) {
		log.debug("Getting All Products");
		httpResponse.setStatus(HttpStatus.OK.value());
		return productService.getProducts();

	}

	@RequestMapping(value = "/api/products/{productId}", method = RequestMethod.GET, headers="Accept=application/json")
	public Product getProduct(@PathVariable int productId ,HttpServletResponse httpResponse) {
		log.debug("Getting Product: "+productId);
		Product product = productService.findById(productId);
		if (product.getId() == 0){
			log.info("product with id {} not found", productId);
			httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
			return new Product();
		}
		httpResponse.setStatus(HttpStatus.OK.value());
		return productService.findById(productId);

	}
	@RequestMapping(value = "/api/products", method = RequestMethod.POST,headers="Accept=application/json")
	public Product addProduct(@RequestBody Product product, HttpServletResponse httpResponse,
							  WebRequest request) {
		log.debug("Adding Product :"+product.toString());
		if (productService.exists(product)){
			log.info("a product with name " + product.getName() + " already exists");
			httpResponse.setStatus(HttpStatus.CONFLICT.value());
			return new Product();

		}
		int productId = productService.addProduct(product);
		product.setId(productId);
		httpResponse.setStatus(HttpStatus.CREATED.value());
		httpResponse.setHeader("Location", String.format("%s/api/products/%s", request.getContextPath(), productId));

		return product;

	}
	@RequestMapping(value = "/api/products/{productId}", method = RequestMethod.PUT,headers="Accept=application/json")
	public void updateProduct(@PathVariable int productId, @RequestBody Product product ,HttpServletResponse httpResponse) {
		log.debug("Updating Product :"+product.toString());
		Product currentProduct = productService.findById(productId);
		if (currentProduct.getId() == 0){
			log.info("product with id {} not found", productId);
			httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
		}else {
			httpResponse.setStatus(HttpStatus.OK.value());
			productService.updateProduct(productId,product);

		}

	}

	@RequestMapping(value = "/api/products/{id}", method = RequestMethod.DELETE)
	public void removeProduct(@PathVariable int id ,HttpServletResponse httpResponse) {
		log.debug("Removing Product Id :"+id);
		Product product = productService.findById(id);
		if (product.getId() == 0){
			log.info("Unable to delete. Product with id {} not found", id);
			httpResponse.setStatus(HttpStatus.NOT_FOUND.value());

		}else
		{
			productService.deleteProduct(id);
			httpResponse.setStatus(HttpStatus.OK.value());
		}


	}
	@RequestMapping(value = "/api/categoryList", method = RequestMethod.GET, headers="Accept=application/json")
	public List<Category> getCategoryList() {
		log.debug("Getting All Category List From DB");
		return productService.getCategoryList();

	}
}